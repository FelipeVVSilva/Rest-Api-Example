package com.felipeveiga.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.felipeveiga.domain.Cliente;
import com.felipeveiga.repositories.ClienteRepository;
import com.felipeveiga.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	public Cliente findById(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Id informado não existe: " + id + 
				", Tipo: " + Cliente.class.getName()));
	}
	
}
