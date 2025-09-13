package com.umsystem.jwtfilter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.umsystem.service.JWTService;
import com.umsystem.service.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTfilter extends OncePerRequestFilter {

		@Autowired
		private JWTService jwtsvr;
		
		@Autowired
		private ApplicationContext  context;
		
		@Override
		 public void doFilterInternal(HttpServletRequest request , HttpServletResponse response , FilterChain filterChain) 
		throws ServletException , IOException
		{
			String authHeader= request.getHeader("Authorization");
			String token = null;
			String email=null;
			if (authHeader != null && authHeader.startsWith("Bearer")) {
				
				token =authHeader.substring(7);
				email=jwtsvr.extractEmail(token);
			}
			
			if (email != null && SecurityContextHolder.getContext().getAuthentication()== null) {
				
				UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(email);
				if (jwtsvr.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
			filterChain.doFilter(request, response);
		}
}
