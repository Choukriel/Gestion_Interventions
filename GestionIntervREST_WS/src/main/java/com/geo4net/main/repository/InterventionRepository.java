package com.geo4net.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
 

import com.geo4net.main.beans.Intervention;

@Repository

public interface InterventionRepository extends JpaRepository<Intervention, String>{

	@Query(value = "SELECT * FROM Intervention i WHERE i.username = ?1", nativeQuery=true)
	public List<Intervention> findByUsername(String username);
	

}
