package com.umsystem.service;

import java.util.List;

import com.umsystem.entity.MyUser;
import com.umsystem.model.LoginUser;

public interface IUserManagementService {

	 public String signIn(MyUser user);

	public String verifyLogin(LoginUser log);

	public List<MyUser> getAllUsers();

	public String removeAllUsers();

	public MyUser getUserById(Integer id);

	String removeUserById(Integer id);

	 String updateUser(MyUser user);

}
