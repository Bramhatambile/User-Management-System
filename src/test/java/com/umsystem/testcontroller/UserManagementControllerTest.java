package com.umsystem.testcontroller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.SslInfo.CertificateValidityInfo.Status;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umsystem.controller.UsersManagementController;
import com.umsystem.entity.MyUser;
import com.umsystem.jwtfilter.JWTfilter;
import com.umsystem.model.LoginUser;
import com.umsystem.service.IUserManagementService;
import com.umsystem.testconfig.SecurityConfigTest;

@WebMvcTest(controllers = UsersManagementController.class,excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = JWTfilter.class)
})
@Import(SecurityConfigTest.class)
public class UserManagementControllerTest {

	 @Autowired
	 private MockMvc mockMvc;
	 
	 @MockitoBean
	 private IUserManagementService ser;
	 
	 
	 @Autowired
	 private ObjectMapper objectMapper;
	 
	 
	 @Test
	 void testGetHome()throws Exception{
		 mockMvc.perform(MockMvcRequestBuilders.get("/home"))
		 .andExpect(status().isOk())
		 .andExpect(content().string("User Registration"));
	 }
	 
	 @Test
	 void testAddUser() throws Exception{
		 MyUser user= new MyUser( "b@gmail.com","b@123","ROLE_USER");
		 when(ser.signIn(any(MyUser.class))).thenReturn("User Registered");
		 mockMvc.perform(MockMvcRequestBuilders.post("/register")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(objectMapper.writeValueAsString(user)))
		 .andExpect(status().isOk())
		 .andExpect(content().string("User Registered"));
		 		
	 }
	 
	 @Test
	 void testLogin() throws Exception{
		 LoginUser loginUser =new LoginUser("bt@gmail.com","bt@123");
		 when(ser.verifyLogin(any(LoginUser.class))).thenReturn("JWT Token");
		 
		 mockMvc.perform(MockMvcRequestBuilders.post("/login")
		 .contentType(MediaType.APPLICATION_JSON)
		 .content(objectMapper.writeValueAsString(loginUser)))
		 .andExpect(status().isOk())
		 .andExpect(content().string("JWT Token"));
	 }
	  @Test 
	  void TestShowAllUsers() throws Exception{
		 List<MyUser> userList= Arrays.asList(new MyUser("bt@gmail.com","bt@123","ROLE_USER"));
		 when(ser.getAllUsers()).thenReturn(userList);
		 
		 mockMvc.perform(MockMvcRequestBuilders.get("/allUsers"))
		 .andExpect(status().isOk())
		 .andExpect(content().json(objectMapper.writeValueAsString(userList)));
	  }

		@Test
		void testShowUserById() throws Exception {
		    MyUser user = new MyUser("bt@gmail.com", "bt@123", "ROLE_USER");
		    when(ser.getUserById(1)).thenReturn(user);

		    mockMvc.perform(MockMvcRequestBuilders.get("/getByid/1"))
		        .andExpect(status().isOk())
		        .andExpect(content().json(objectMapper.writeValueAsString(user)));
		}

		@Test
		void testDeleteUserById() throws Exception {
		    when(ser.removeUserById(1)).thenReturn("User deleted");

		    mockMvc.perform(MockMvcRequestBuilders.delete("/delete/1"))
		        .andExpect(status().isOk())
		        .andExpect(content().string("User deleted"));
		}

		@Test
		void testDeleteAllUsers() throws Exception {
		    when(ser.removeAllUsers()).thenReturn("All users deleted");

		    mockMvc.perform(MockMvcRequestBuilders.delete("/deleteAll"))
		        .andExpect(status().isOk())
		        .andExpect(content().string("All users deleted"));
		}

		@Test
		void testUpdateUser() throws Exception {
		    MyUser user = new MyUser("bt@gmail.com", "bt@123", "ROLE_ADMIN");
		    when(ser.updateUser(any(MyUser.class))).thenReturn("User updated");

		    mockMvc.perform(MockMvcRequestBuilders.put("/updateUser")
		            .contentType(MediaType.APPLICATION_JSON)
		            .content(objectMapper.writeValueAsString(user)))
		        .andExpect(status().isOk())
		        .andExpect(content().string("User updated"));
		}
		
		@Test
		void wrongInputTest() throws Exception {
		    when(ser.getUserById(99)).thenThrow(new IllegalArgumentException("User not found"));

		    mockMvc.perform(MockMvcRequestBuilders.get("/getByid/99"))
		        .andExpect(status().isBadRequest())
		        .andExpect(content().string("Problem ::User not found"));
		}

		@Test
		void anyExceptionTest() throws Exception {
		    when(ser.removeUserById(10)).thenThrow(new RuntimeException("Something went wrong"));

		    mockMvc.perform(MockMvcRequestBuilders.delete("/delete/10"))
		        .andExpect(status().isInternalServerError())
		        .andExpect(content().string("Problem ::Something went wrong"));
		}
	  
}
