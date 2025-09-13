package com.umsystem.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MyUser {

	public MyUser(String string, String string2, String string3) {
		// TODO Auto-generated constructor stub
	}

	@Id
//	@SequenceGenerator(name = "user", sequenceName ="USER_SEQ", allocationSize = 1, initialValue = 100)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NonNull
	@Column(length = 30)
	private String email;
	
	@NonNull
	@Column(length = 90)
	private String password;
	
	@NonNull
	@Column(length = 30)
	private String role;
	
	 @CreationTimestamp
	 @Column(updatable = false)
	 private LocalDateTime registerDatetime;
	 
	 @UpdateTimestamp
	 @Column(insertable = false)
	 private LocalDateTime updationDateTime;
	 
	@Version
	private Integer updateCount;

	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
