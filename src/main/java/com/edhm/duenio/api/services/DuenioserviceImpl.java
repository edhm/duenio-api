package com.edhm.duenio.api.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edhm.duenio.api.entities.Duenio;
import com.edhm.duenio.api.repositories.DuenioRepository;

@Service
@Transactional
public class DuenioserviceImpl implements DuenioService {
	@Autowired
	private DuenioRepository duenioRepository;

	@Override
	public List<Duenio> findAll() {
		return duenioRepository.findAll();
	}

	@Override
	public Duenio findById(Long id) {
		return duenioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No existe registro"));
	}

	@Override
	public void save(Duenio duenio) {
		duenioRepository.save(duenio);
	}

	@Override
	public void deleteById(Long id) {
		duenioRepository.deleteById(id);
	}

}
