package com.felipeveiga.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.felipeveiga.domain.security.UserSS;

@Service
public class UserService {

	//Retorna um usuário logado no sistema
	public static UserSS authenticaeted() {
		try {
			return (UserSS) SecurityContextHolder .getContext().getAuthentication().getPrincipal();
		}
		catch(Exception e) {
			return null;
		}
		
	}
}
