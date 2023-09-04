package com.edhm.duenio.api.services;

import java.util.List;

import com.edhm.duenio.api.entities.Duenio;

public interface DuenioService {
	public List<Duenio> findAll();

	public Duenio findById(Long id);

	public void save(Duenio duenio);

	public void deleteById(Long id);

}
