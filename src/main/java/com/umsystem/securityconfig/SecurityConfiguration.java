package com.umsystem.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.JmsProperties.Listener.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.umsystem.jwtfilter.JWTfilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfiguration {

		@Autowired
		private UserDetailsService service;
		
		
		@Autowired
		private JWTfilter jwtfil;
		
		
		@Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception
		{
			return security
					.csrf(Customizer -> Customizer.disable())
					.authorizeHttpRequests(request -> request
					.requestMatchers("/register","/login","/home").permitAll()
					.anyRequest().authenticated())
					.httpBasic(Customizer.withDefaults())
					.sessionManagement(Session -> Session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
					.addFilterBefore(jwtfil, UsernamePasswordAuthenticationFilter.class)
					.build();
					
		}
		
		
		@Bean
		public AuthenticationProvider authenticationProvider() {
			DaoAuthenticationProvider provider =new DaoAuthenticationProvider(service);
			provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
			return provider;
		}
		
		
		@Bean
		public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception
		{
			return configuration.getAuthenticationManager();
		}
}
