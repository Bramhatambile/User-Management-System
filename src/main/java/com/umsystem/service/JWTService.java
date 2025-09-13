package com.umsystem.service;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
	
	private String secretkey="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJpQV30";
	

	public String generateToken(String email) {
		Map<String, Object> claims = new HashMap<String, Object>();
		
		return Jwts
				.builder()
				.claims()
				.add(claims)
				.subject(email)
				.issuedAt(new Date (System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()))
				.and()
				.signWith(getKey())
				.compact();
	}
		
	
	public SecretKey getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretkey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String extractEmail(String token) {
		return extractClaims(token , Claims :: getSubject);
	}


	public <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
		final Claims claims=extractAllClaims(token);
		return claimResolver.apply(claims);
	}

//
//	public <T> T  extractClaims(String token, Function<T, R> <Claims, T> claimResolver) {
//		final Claims claims=extractAllClaims(token);
//		return claimResolver.apply(claims);
//	}


	public  Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(getKey())
				.build().parseSignedClaims(token)
				.getPayload();
	}
	
	public  boolean validateToken (String token , UserDetails userDetails) {
		final String email=extractEmail(token);
		return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}


	public boolean isTokenExpired(String token) {
		
		return extractExpiration(token).before(new Date());
	}


	public Date extractExpiration(String token) {
		
		return extractClaims(token,Claims::getExpiration);
	}
	

}
