package com.umsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.umsystem.entity.MyUser;
import com.umsystem.model.UserPrinciple;
import com.umsystem.repository.IUserManagmentRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	 @Autowired
	 private IUserManagmentRepository repo;
	 
	 @Override
	 public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		 
		 MyUser user=repo.findByEmail(email);
		 if(user==null) {
			 throw new UsernameNotFoundException("User not found");
		 }
		 return new UserPrinciple(user);
	 }
}
