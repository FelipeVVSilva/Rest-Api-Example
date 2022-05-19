package com.felipeveiga.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.felipeveiga.domain.Cidade;
import com.felipeveiga.domain.Cliente;
import com.felipeveiga.domain.Endereco;
import com.felipeveiga.domain.Pedido;
import com.felipeveiga.domain.dto.ClienteDTO;
import com.felipeveiga.domain.dto.ClienteNewDTO;
import com.felipeveiga.domain.enums.Perfil;
import com.felipeveiga.domain.enums.TipoCliente;
import com.felipeveiga.domain.security.UserSS;
import com.felipeveiga.repositories.ClienteRepository;
import com.felipeveiga.repositories.EnderecoRepository;
import com.felipeveiga.services.exceptions.AuthorizationException;
import com.felipeveiga.services.exceptions.DataIntegrityViolationException;
import com.felipeveiga.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	public List<Cliente> findAll() {
		List<Cliente> list = repo.findAll();
		return list;
	}
	
	public Cliente findById(Integer id) {
		UserSS user = UserService.authenticaeted();
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Id informado não existe: " + id + 
				", Tipo: " + Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = findById(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		//newObj.setSenha(obj.getSenha());
	}
	
	public void delete(Integer id) {
		Cliente obj = findById(id);
		List<Pedido> pedidos = obj.getPedidos();
		try {
			if(pedidos != null) {
				throw new DataIntegrityViolationException("Não é possível excluir uma Cliente que pussui pedidos");
			}
			repo.deleteById(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException(e.getMessage());
		}
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if(objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		if(objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		return cli;
	}
}
