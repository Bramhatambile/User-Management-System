package com.umsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.umsystem.entity.MyUser;
import com.umsystem.model.LoginUser;
import com.umsystem.service.IUserManagementService;

@RestController
public class UsersManagementController {
	@Autowired
	private IUserManagementService ums;
	
	@GetMapping("/home")
	public ResponseEntity<String> getHome(){
		ResponseEntity<String> ums =new ResponseEntity<String>("Welcome to Spring boot",HttpStatus.OK);
		return ums;
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> checkUser(@RequestBody MyUser user){
		String msg=ums.signIn(user);
		 ResponseEntity<String> response=new ResponseEntity<String>(msg,HttpStatus.OK);
		 return response;
		 
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> checkUser(@RequestBody LoginUser log){
		String msg=ums.verifyLogin(log);
		ResponseEntity<String> response =new ResponseEntity<String>(msg,HttpStatus.OK);
		return response;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/allUsers")
	public ResponseEntity<List<MyUser>>showAllUsers(){
		List<MyUser> allUsers= ums.getAllUsers();
		ResponseEntity<List<MyUser>> response= new ResponseEntity<List<MyUser>>(allUsers,HttpStatus.OK);
		return response;
	}
	
	
	@GetMapping("getById/{id}")
	public ResponseEntity<MyUser> showById(@PathVariable Integer id){
		MyUser userById=ums.getUserById(id);
		ResponseEntity<MyUser> response= new ResponseEntity<MyUser>(userById,HttpStatus.OK);
		return response;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deleteAll")
	public ResponseEntity<String> deleteAllUsers(){
		String msg=ums.removeAllUsers();
		ResponseEntity<String> response = new  ResponseEntity<String>(msg,HttpStatus.OK);
		return response;
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String>deleteUser(@PathVariable Integer id){
		String msg=ums.removeUserById(id);
		ResponseEntity<String>response = new ResponseEntity<String>(msg,HttpStatus.OK);
		return response;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updateUser")
	public ResponseEntity<String> userUpdate(@RequestBody MyUser user){
		String msg =ums.updateUser(user);
		ResponseEntity<String> response= new ResponseEntity<String>(msg,HttpStatus.OK);
		return response;
	}
}
