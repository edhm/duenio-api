package com.edhm.duenio.api.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.edhm.duenio.api.entities.Duenio;

public interface DuenioRepository extends CrudRepository<Duenio, Long> {
	@Override
	List<Duenio> findAll();
}
