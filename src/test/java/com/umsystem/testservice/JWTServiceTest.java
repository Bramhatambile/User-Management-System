package com.umsystem.testservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import com.umsystem.service.JWTService;

public class JWTServiceTest {


    private JWTService jwtService;

    @BeforeEach
    void setup() {
        jwtService = new JWTService();
    }

    @Test
    void testGenerateTokenAndExtractEmail() {
        String email = "test@example.com";
        String token = jwtService.generateToken(email);

        assertNotNull(token);

        String extractedEmail = jwtService.extractEmail(token);
        assertEquals(email, extractedEmail);
    }

    @Test
    void testValidateTokenWithCorrectEmail() {
        String email = "user@example.com";
        String token = jwtService.generateToken(email);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(email);

        boolean isValid = jwtService.validateToken(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void testValidateTokenWithIncorrectEmail() {
        String token = jwtService.generateToken("real@example.com");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("fake@example.com");

        boolean isValid = jwtService.validateToken(token, userDetails);
        assertFalse(isValid);
    }

    @Test
    void testTokenShouldNotBeExpired() {
        String token = jwtService.generateToken("future@user.com");

        boolean expired = jwtService.isTokenExpired(token);
        assertFalse(expired);
    }

    @Test
    void testExtractExpirationShouldReturnFutureDate() {
        String token = jwtService.generateToken("future@user.com");

        Date expiration = jwtService.extractExpiration(token);

        assertTrue(expiration.after(new Date()));
    }
}
