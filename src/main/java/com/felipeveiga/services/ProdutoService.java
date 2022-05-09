package com.felipeveiga.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.felipeveiga.domain.Produto;
import com.felipeveiga.repositories.ProdutoRepository;
import com.felipeveiga.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repo;
	
	public List<Produto> findAll(){
		List<Produto> list = repo.findAll();
		return list;
	}
	
	public Produto findById(Integer id){
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Id informado não existe: " + id + 
				", Tipo: " + Produto.class.getName()));
	}
}
