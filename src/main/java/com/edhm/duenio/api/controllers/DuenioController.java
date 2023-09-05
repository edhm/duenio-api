package com.edhm.duenio.api.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edhm.duenio.api.entities.Duenio;
import com.edhm.duenio.api.services.DuenioService;

@RestController
public class DuenioController {
	private static final Logger logger = LoggerFactory.getLogger(DuenioController.class);
	@Value("${app.storage.path}")
	private String STORAGEPATH;
	@Autowired
	private DuenioService duenioService;

	@GetMapping("/duenios")
	public List<Duenio> duenios() {
		logger.info("call duenios");
		List<Duenio> duenios = duenioService.findAll();
		logger.info("duenios: " + duenios);
		return duenios;
	}

	@GetMapping("/duenios/fotos/{filename:.+}")
	public ResponseEntity<Resource> files(@PathVariable String filename) throws Exception {
		logger.info("call fotos: " + filename);
		Path path = Paths.get(STORAGEPATH).resolve(filename);
		logger.info("Path: " + path);
		if (!Files.exists(path)) {
			return ResponseEntity.notFound().build();
		}
		org.springframework.core.io.Resource resource = new UrlResource(path.toUri());
		logger.info("Resource: " + resource);
		return ResponseEntity.ok()
				.header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
						"inline;filename=\"" + resource.getFilename() + "\"")
				.header(org.springframework.http.HttpHeaders.CONTENT_TYPE,
						Files.probeContentType(Paths.get(STORAGEPATH).resolve(filename)))
				.header(org.springframework.http.HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()))
				.body(resource);
	}

	@PostMapping("/duenios")
	public Duenio crear(@RequestParam(name = "foto", required = false) MultipartFile foto,
			@RequestParam("nombre") String nombre, @RequestParam("edad") int edad,
			@RequestParam("direccion") String direccion, @RequestParam("nroContacto") String nroContacto)
			throws Exception {
		logger.info("call crear(" + nombre + ", " + edad + ", " + direccion + ", " + nroContacto + ", " + foto + ")");
		Duenio duenio = new Duenio();
		duenio.setNombre(nombre);
		duenio.setEdad(edad);
		duenio.setDireccion(direccion);
		duenio.setNroContacto(nroContacto);

		if (foto != null && !foto.isEmpty()) {
			String filename = System.currentTimeMillis()
					+ foto.getOriginalFilename().substring(foto.getOriginalFilename().lastIndexOf("."));
			duenio.setFoto(filename);
			if (Files.notExists(Paths.get(STORAGEPATH))) {
				Files.createDirectories(Paths.get(STORAGEPATH));
			}
			Files.copy(foto.getInputStream(), Paths.get(STORAGEPATH).resolve(filename));
		}
		duenioService.save(duenio);
		return duenio;
	}

	@DeleteMapping("/duenios/{id}")
	public ResponseEntity<String> eliminar(@PathVariable Long id) {
		logger.info("call eliminar: " + id);
		duenioService.deleteById(id);
		return ResponseEntity.ok().body("Registro eliminado");
	}

	@GetMapping("/duenios/{id}")
	public Duenio obtener(@PathVariable Long id) {
		logger.info("call obtener: " + id);
		Duenio duenio = duenioService.findById(id);
		return duenio;
	}

}
