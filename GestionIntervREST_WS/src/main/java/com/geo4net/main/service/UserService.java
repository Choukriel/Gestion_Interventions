package com.geo4net.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geo4net.main.beans.User;
import com.geo4net.main.exception.RecordNotFoundException;
import com.geo4net.main.exception.UserExistsException;
import com.geo4net.main.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
    UserRepository repository;
	
	public List<User> getAllUsers(){
		List<User> userList = repository.findAll();
        
        if(userList.size() > 0) 
            return userList;
         else { 
        	 return new ArrayList<User>();
        	 }
        
	}
	
	public List<User> createUser(User user) throws RecordNotFoundException
    {
		List<User> user1 = new ArrayList<User>();
    	    	
    	if(user.getUsername()!=null)
    	{
    	  Optional<User> u = repository.findById(user.getUsername());
        
    	if(u.isPresent())
        {
    		
             
            throw new UserExistsException("User already exists with id  ", user.getUsername());
        }else {
        	user1.add(repository.save(user));
             System.out.println("User added successfully ...");
            return user1;
        }
    	}
    	
    	else{
    		
        	user1.add(repository.save(user));
        	
            System.out.println("User added successfully ...");

    		return user1;
    	}	    
 }
   
	
	public User getUserById(String username) throws RecordNotFoundException
    {
        Optional<User> user = repository.findById(username);
         
        if(user.isPresent()) {
            return user.get();
        } else {
            throw new RecordNotFoundException("No student record exist for given id  ",username);
        }
    }
	
	public boolean getUserByUsernameAndPassword(String username, String password){
		
		
		
        boolean  existsUser= repository.existsByUsernameAndPassword(username, password);

		if (existsUser) {
			return true;
		}else {
			throw new RecordNotFoundException("No user record exist for given infos  ", username + "  "+password);
		}
		
		
	}
	
     
	
	
}
