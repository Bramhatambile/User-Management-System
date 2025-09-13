package com.umsystem.testservice;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.umsystem.entity.MyUser;
import com.umsystem.model.LoginUser;
import com.umsystem.repository.IUserManagmentRepository;
import com.umsystem.service.JWTService;
import com.umsystem.service.UsersManagementServiceImpl;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserManagementServiceImplTest {


    @InjectMocks
    private UsersManagementServiceImpl service;

    @Mock
    private IUserManagmentRepository repo;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JWTService jwtSer;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignIn() {
        MyUser input = new MyUser("v@gmail.com", "password", "ROLE_USER");
        MyUser saved = new MyUser("v@gmail.com", "encoded", "ROLE_USER");
        saved.setId(1);

        when(repo.save(any(MyUser.class))).thenReturn(saved);

        String result = service.signIn(input);
        assertTrue(result.contains("User registered with"));
    }

    @Test
    void testVerifyLoginSuccess() {
        LoginUser loginUser = new LoginUser("v@gmail.com", "pass");

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtSer.generateToken("v@gmail.com")).thenReturn("mocked-token");

        String result = service.verifyLogin(loginUser);
        assertTrue(result.contains("Token"));
    }

    @Test
    void testVerifyLoginFailure() {
        LoginUser loginUser = new LoginUser("v@gmail.com", "wrongpass");

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.verifyLogin(loginUser));
    }

    @Test
    void testGetAllUsers() {
        List<MyUser> users = List.of(new MyUser("a@gmail.com", "123", "ROLE_USER"));
        when(repo.findAll()).thenReturn(users);

        List<MyUser> result = service.getAllUsers();
        assertEquals(1, result.size());
    }

    @Test
    void testGetUserByIdSuccess() {
        MyUser user = new MyUser("v@gmail.com", "123", "ROLE_USER");
        when(repo.findById(1)).thenReturn(Optional.of(user));

        MyUser result = service.getUserById(1);
        assertEquals("v@gmail.com", result.getEmail());
    }

    @Test
    void testGetUserByIdFailure() {
        when(repo.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.getUserById(1));
        assertTrue(exception.getMessage().contains("does not exist"));
    }

    @Test
    void testRemoveUserByIdSuccess() {
        MyUser user = new MyUser("v@gmail.com", "123", "ROLE_USER");
        when(repo.findById(1)).thenReturn(Optional.of(user));

        String result = service.removeUserById(1);
        verify(repo).deleteById(1);
        assertTrue(result.contains("deleted"));
    }

    @Test
    void testRemoveUserByIdFailure() {
        when(repo.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.removeUserById(1));
        assertTrue(exception.getMessage().contains("does not exist"));
    }

    @Test
    void testRemoveAllUsers() {
        String result = service.removeAllUsers();
        verify(repo).deleteAll();
        assertEquals("All records are deleted", result);
    }

    @Test
    void testUpdateUserSuccess() {
        MyUser input = new MyUser("v@gmail.com", "newpass", "ROLE_USER");
        input.setId(1);
        MyUser existing = new MyUser("old@gmail.com", "oldpass", "ROLE_USER");
        existing.setId(1);

        when(repo.findById(1)).thenReturn(Optional.of(existing));
        when(repo.save(any(MyUser.class))).thenReturn(input);

        String result = service.updateUser(input);
        assertTrue(result.contains("updated"));
    }

    @Test
    void testUpdateUserFailure() {
        MyUser input = new MyUser("v@gmail.com", "newpass", "ROLE_USER");
        input.setId(1);

        when(repo.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.updateUser(input));
        assertTrue(exception.getMessage().contains("does not exist"));
    }
}
