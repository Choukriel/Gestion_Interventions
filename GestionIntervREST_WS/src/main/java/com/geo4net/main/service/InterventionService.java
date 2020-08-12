package com.geo4net.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geo4net.main.beans.Intervention;
import com.geo4net.main.exception.RecordNotFoundException;
import com.geo4net.main.repository.InterventionRepository;


@Service
public class InterventionService {
	
	
	
	@Autowired
    InterventionRepository repository;
	
	
	public List<Intervention> getAllInterventions(String username){
		List<Intervention> InterventionList = repository.findByUsername(username);
        
        if(InterventionList.size() > 0) 
            return InterventionList;
         else {
            return new ArrayList<Intervention>();}
        
	}
	
	
     
    public List<Intervention> createOrUpdateIntervention(Intervention inervention) throws RecordNotFoundException
    {
    	    	
    	if(inervention.getPsn()!=null)
    	{
    	  Optional<Intervention> interv = repository.findById(inervention.getPsn());
        
    	if(interv.isPresent())
        {
    		Intervention newEntity = interv.get();
            //newEntity.setPsn(inervention.getPsn());
            newEntity.setSim(inervention.getSim());
            newEntity.setMatricule(inervention.getMatricule());
            newEntity.setLvcan(inervention.getLvcan());
            newEntity.setCurrLoc(inervention.getCurrLoc());
            newEntity.setNomTech(inervention.getNomTech());
            newEntity.setUser(inervention.getUser());
            
            repository.save(newEntity);
 
            
             
            return getAllInterventions(newEntity.getUser().getUsername());
        } else {
              repository.save(inervention);
      
            return getAllInterventions(inervention.getUser().getUsername());
        }
    	}
    	
    	else
    	{
    		repository.save(inervention);
     
    		return getAllInterventions(inervention.getUser().getUsername());
    	}	    
 }
    
    public void deleteInterventionById(String psn) throws RecordNotFoundException
    {
        Optional<Intervention> student = repository.findById(psn);
         
        if(student.isPresent())
        {
            repository.deleteById(psn);
        } else {
            throw new RecordNotFoundException("No Intervention record exist for given id  ",psn);
        }
    }

	
	
	
	
	
	/* private static ArrayList<Intervention> allInterventions = new ArrayList<Intervention>();
	 
	 public static void addIntervention(Intervention interv) {
		 allInterventions.add(interv);
		 System.out.println("intervention added with success ----------");
	 }
	   
	 
	 
	 public static void updateIntervention(Intervention interv) {
		 for (Intervention i : allInterventions) {
			if (interv.getPsn().equals(i.getPsn())) {
				allInterventions.set((allInterventions.indexOf(i)), interv);
				 System.out.println("intervention updated with success ----------");
				
			}
			
		}
	 }
	 public static void deleteIntervention(Intervention interv) {
		 for (Intervention i : allInterventions) {
			 if (interv.getPsn().equals(i.getPsn())) {
				 allInterventions.remove(allInterventions.indexOf(i));
				 System.out.println("intervention deleted with success ----------");
			 }
		 }
	 }
	public static ArrayList<Intervention> getAllInterventions() {
		return allInterventions;
	}*/
}
