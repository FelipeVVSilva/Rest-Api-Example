package com.felipeveiga.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felipeveiga.domain.dto.EmailDTO;
import com.felipeveiga.domain.security.JWTUtil;
import com.felipeveiga.domain.security.UserSS;
import com.felipeveiga.services.AuthService;
import com.felipeveiga.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthResource {

	@Autowired
	private JWTUtil jwtUtil; 
	@Autowired
	private AuthService service; 
	
	@PostMapping("/refresh_token")
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UserService.authenticaeted();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/forgot")
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO objDto) {
		service.sendNewPassword(objDto.getEmail());
		return ResponseEntity.noContent().build();
	}
}
