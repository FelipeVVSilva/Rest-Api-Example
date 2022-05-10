package com.felipeveiga.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.felipeveiga.domain.Pedido;
import com.felipeveiga.repositories.PedidoRepository;
import com.felipeveiga.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	public Pedido findById(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Id informado não existe: " + id + 
				", Tipo: " + Pedido.class.getName()));
	}
	
}
