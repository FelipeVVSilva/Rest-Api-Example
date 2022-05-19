package com.felipeveiga.domain.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipeveiga.domain.dto.CredenciaisDTO;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private JWTUtil jwtUtil;

	private AuthenticationManager authenticationManager;
	
	public JWTAuthenticationFilter(JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
		super();
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;
	}


	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
												HttpServletResponse res) throws AuthenticationException{
		try {
			CredenciaisDTO creds = new ObjectMapper()
					.readValue(req.getInputStream(), CredenciaisDTO.class);
			
			UsernamePasswordAuthenticationToken authToken =  new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());
			
			Authentication auth = authenticationManager.authenticate(authToken);
			
			return auth;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}
	
	@Override
	public void successfulAuthentication(HttpServletRequest req,
										HttpServletResponse res,
										FilterChain chain,
										Authentication auth) throws IOException, ServletException{
		String username = ((UserSS) auth.getPrincipal()).getUsername();
		String token = jwtUtil.generateToken(username);
		res.addHeader("Authorization", "Bearer" + token);
	}
}
