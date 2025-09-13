package com.umsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umsystem.entity.MyUser;

public interface IUserManagmentRepository extends JpaRepository<MyUser,Integer> {

	public MyUser findByEmail(String email);

	

}
