package com.felipeveiga.domain.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.felipeveiga.domain.Cliente;
import com.felipeveiga.services.validation.ClienteUpdate;

@ClienteUpdate
public class ClienteDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	@NotEmpty(message = "Campo nome não pode ser vazio")
	@Length(min = 5, max =120, message = "O tamnho deve ser entre 5 e 80 caracteres")
	private String nome;
	
	@Email(message = "Email inválido")
	@NotEmpty(message = "Campo email não pode ser vazio")
	@Length(min = 5, max =80, message = "O tamnho deve ser entre 5 e 80 caracteres")
	private String email;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public ClienteDTO() {
		
	}
	public ClienteDTO(Cliente obj) {
		super();
		id = obj.getId();
		nome = obj.getNome();
		email = obj.getEmail();
	}
	
	
	
}
