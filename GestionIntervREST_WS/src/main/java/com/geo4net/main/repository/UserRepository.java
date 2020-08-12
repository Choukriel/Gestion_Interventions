package com.geo4net.main.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.geo4net.main.beans.User;

@Repository
public interface UserRepository  extends JpaRepository<User, String>{
	
	  @Query("SELECT count(u)>0 FROM User u WHERE u.username = ?1 and u.password = ?2")
	  public Boolean existsByUsernameAndPassword(String username, String password);

}
