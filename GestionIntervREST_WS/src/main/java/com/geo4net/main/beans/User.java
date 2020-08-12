package com.geo4net.main.beans;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id; 
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "USER")
public class User {
	
	 
	
	  @Id
	  @Column(name = "username")
	  private String username;
	  
	  @Column(name = "password")
	  private String password;
	  
	  @Column(name = "email")
	  private String email;
	  
	  @JsonManagedReference
	  @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user")
	  private List<Intervention> userInterventions = new ArrayList<Intervention>();
	  

	public User(String username, String password, String email) {
		
		this.username = username;
		this.password = password;
		this.email = email;
	}
	public User() {
		
	}
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	 
	 public List<Intervention> getUserInterventions() {
		return userInterventions;
	}

	public void setUserInterventions(List<Intervention> userInterventions) {
		this.userInterventions = userInterventions;
	} 
	@Override
	public String toString() {
		return "User [username=" + username + ", password=*****" + ", email=" + email + "]";
	}
	
	  
	  
}
