package com.felipeveiga.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.felipeveiga.domain.security.JWTAuthenticationFilter;
import com.felipeveiga.domain.security.JWTAuthorizationFilter;
import com.felipeveiga.domain.security.JWTUtil;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private Environment env;
	@Autowired
	private JWTUtil jwtUtil;
	
	private static final String[] PUBLIC_MACHES = {
			"/h2-console/**",
	};
	
	private static final String[] PUBLIC_MACHES_GET = {
			"/categorias/**",
			"/produtos/**"
	};
	
	private static final String[] PUBLIC_MACHES_POST = {
			"/clientes/**",
			"/auth/forgot/**"
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//libera o acesso ao H2
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		http.cors().and().csrf().disable();
		http.authorizeRequests()
			.antMatchers(PUBLIC_MACHES).permitAll()
			.antMatchers(HttpMethod.GET, PUBLIC_MACHES_GET).permitAll()
			.antMatchers(HttpMethod.POST, PUBLIC_MACHES_POST).permitAll()
			.anyRequest().authenticated();
		//Fala qual é o filtro de autenticação e qualo gerador de Token
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource(){
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//Fala para o Spring Security quem é o UserDetailsService e qual é o algoritmo de codificação da senha
	 public void configure(AuthenticationManagerBuilder auth) throws Exception {
		 auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
		 
	 }
}
