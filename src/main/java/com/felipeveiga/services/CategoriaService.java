package com.felipeveiga.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.felipeveiga.domain.Categoria;
import com.felipeveiga.domain.Produto;
import com.felipeveiga.domain.dto.CategoriaDTO;
import com.felipeveiga.repositories.CategoriaRepository;
import com.felipeveiga.services.exceptions.DataIntegrityViolationException;
import com.felipeveiga.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;
	
	public List<Categoria> findAll(){
		List<Categoria> list = repo.findAll();
		return list;
	}
	
	public Categoria findById(Integer id){
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Id informado não existe: " + id + 
				", Tipo: " + Categoria.class.getName()));
	}
	
	@Transactional
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		Categoria newObj = findById(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void updateData(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
	}
	
	public void delete(Integer id) {
		Categoria obj = findById(id);
		List<Produto> produtos = obj.getProdutos();
		try {
			if(produtos != null) {
				throw new DataIntegrityViolationException("Não é possível excluir uma Categoria que pussui produtos");
			}
			repo.deleteById(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException(e.getMessage());
		}
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Categoria fromDTO(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}
}
