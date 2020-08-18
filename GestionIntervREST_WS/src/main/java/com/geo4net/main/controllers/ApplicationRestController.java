package com.geo4net.main.controllers;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.geo4net.main.beans.*;
 
import com.geo4net.main.exception.RecordNotFoundException;
import com.geo4net.main.exception.UserExistsException;
import com.geo4net.main.service.InterventionService;
import com.geo4net.main.service.UserService;
 
@RestController
public class ApplicationRestController {
	@Autowired
    UserService uservice;
	@Autowired
	InterventionService iservice;

	
@RequestMapping(method = RequestMethod.POST, value = "/register/user", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
@ResponseBody
public ResponseEntity<List<User>> registerUser(@RequestParam Map<String, String> paramMap) throws UserExistsException{
	  	User user = new User(paramMap.get("username").toString(), paramMap.get("password").toString(), paramMap.get("email").toString());
	 List<User> user3 = new ArrayList<User>();
	 user3=uservice.createUser(user);
     HttpHeaders headers  = new HttpHeaders();
     headers.setContentType(MediaType.APPLICATION_JSON);  
	  return new ResponseEntity<List<User>>(user3, headers ,HttpStatus.OK);
}

@RequestMapping(method = RequestMethod.GET, value = "/user/{username}")
@ResponseBody
public ResponseEntity<User> getUserByID(@PathVariable("username") String username) throws RecordNotFoundException{
	User user = uservice.getUserById(username);
	return new ResponseEntity<User>(user, new HttpHeaders(), HttpStatus.OK);
}

@RequestMapping(method = RequestMethod.GET, value = "login/user/{username}&{password}")
@ResponseBody
public ResponseEntity<Boolean> getUserByUsernameAndPassword(@PathVariable("username") String username, @PathVariable("password") String password) throws RecordNotFoundException{
	Boolean user = uservice.getUserByUsernameAndPassword(username, password);
		return  new ResponseEntity<Boolean>(user, new HttpHeaders(), HttpStatus.OK);
}

@RequestMapping(method = RequestMethod.GET, value = "/intervention/allinterventions", 
consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE} ,
produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
@ResponseBody
public ResponseEntity<List<Intervention>> getAllInterventions(@RequestParam Map<String, String> paramMap){
	List<Intervention>	interventions = iservice.getAllInterventions(paramMap.get("username"));
	 HttpHeaders headers  = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
return new ResponseEntity<List<Intervention>>(interventions, headers, HttpStatus.OK);
}
 
@RequestMapping(method = RequestMethod.POST, value = "/create_or_update/intervention", 
consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},  
produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
@ResponseBody
public ResponseEntity<List<Intervention>> createOrUpdateIntervention(@RequestParam Map<String, String> paramMap) {
  	User user = new User();
  			user=uservice.getUserById(paramMap.get("username"));	
  	List<Intervention> interventions = iservice.createOrUpdateIntervention(new Intervention(paramMap.get("psn"), paramMap.get("tech_name"),
  					paramMap.get("sim"), paramMap.get("lvcan"), paramMap.get("current_location"), paramMap.get("matricule"), user));
    HttpHeaders headers  = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
	return new ResponseEntity<List<Intervention>>(interventions, headers, HttpStatus.OK);
}

@RequestMapping(method = RequestMethod.DELETE, value = "/delete/intervention",
consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},  
produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
@ResponseBody
public HttpStatus deleteInterventionById(@RequestParam Map<String, String> paramMap) throws RecordNotFoundException{	
	iservice.deleteInterventionById(paramMap.get("psn"));
	return HttpStatus.OK;
	
}

}