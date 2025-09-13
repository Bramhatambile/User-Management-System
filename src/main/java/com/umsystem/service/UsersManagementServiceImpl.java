package com.umsystem.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.umsystem.entity.MyUser;
import com.umsystem.model.LoginUser;
import com.umsystem.repository.IUserManagmentRepository;

@Service
public class UsersManagementServiceImpl  implements IUserManagementService{
  @Autowired
  private IUserManagmentRepository repo;
  
  @Autowired
  private AuthenticationManager authManager;
  
  @Autowired
  private JWTService jwtsvr;
   
  private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
  
  @Override
  public String signIn(MyUser user) {
	  user.setPassword(encoder.encode(user.getPassword()));
	  MyUser saveUser=repo.save(user);
	  return "User registered with"+saveUser.getId();
  }
  
  
  @Override
  public String verifyLogin(LoginUser log) {
	  Authentication authentication=authManager.authenticate(
			  new UsernamePasswordAuthenticationToken(log.getEmail(), log.getPassword()));
	  if(authentication.isAuthenticated()) {
		  String token=jwtsvr.generateToken(log.getEmail());
		  return "User Logged in Successfully with Token :"+token;
	  }
	  throw new IllegalArgumentException("Check Login Credentials, Login Failed");
  
  }
    
  
  @Override
  public List<MyUser> getAllUsers(){
	  List<MyUser>all=repo.findAll();
	  return all;
  }
  
   
  @Override
  public MyUser getUserById(Integer id) {
	  Optional<MyUser>opt=repo.findById(id);
	  if (opt.isPresent()) {
		return opt.get();
	}
	  throw new IllegalArgumentException("Record ID="+id+"you want to delete does not exist");
	  
  }
  
  
  @Override
  public String removeUserById(Integer id) {
	  Optional<MyUser> optionalobj=repo.findById(id);
	  if(optionalobj.isPresent()) {
		  repo.deleteById(id);
		  return "User with "+id+"ID is deleted";
	  }
	  throw new IllegalArgumentException("Record ID="+id+"you want to delete does not exist");
	  
  }
  
  
  @Override
  public String removeAllUsers() {
	  repo.deleteAll();
	  return "All records are deleted";
  }
  
  
    @Override
    public String updateUser(MyUser user) {
    	MyUser userData=repo.findById(user.getId()).orElseThrow(()-> new IllegalArgumentException("Recored ID="+user.getId()+"you want to updayte does not exist"));
    	BeanUtils.copyProperties(user, userData);
    	repo.save(userData);
    	return "id"+user.getId()+"user updated";
    }
  
}
