package com.felipeveiga.domain.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

public class EmailDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Email(message = "Email inválido")
	@NotEmpty(message = "Campo email não pode ser vazio")
	@Length(min = 5, max =80, message = "O tamnho deve ser entre 5 e 80 caracteres")
	private String email;

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public EmailDTO(String email) {
	}
	
	
	
}
