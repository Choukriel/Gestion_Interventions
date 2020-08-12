package com.geo4net.main.beans;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "INTERVENTION")
public class Intervention {
	
	 @Id
	 @Column(name = "psn")
	 private String psn;
	
	 @Column(name = "Tech_name")
	 private String nomTech;
	 
	 @Column(name = "sim")
	 private String sim;
	 
	 @Column(name = "lvcan")
	 private String lvcan;
	
	 @Column(name = "current_ocation")
	 private String currLoc;
	 
	 @Column(name = "matricule")
	 private String matricule;
	 
	 
	 @JsonBackReference
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "username", referencedColumnName = "username", insertable=true,updatable=true  )
	 private User user;

	 public Intervention(String psn, String nomTech, String sim, String lvcan, String currLoc, String matricule,
			User user) {
		super();
		this.psn = psn;
		this.nomTech = nomTech;
		this.sim = sim;
		this.lvcan = lvcan;
		this.currLoc = currLoc;
		this.matricule = matricule;
		this.user = user;
	}
	public Intervention() {
		
	 }
	public String getNomTech() {
		return nomTech;
	}
	public void setNomTech(String nomTech) {
		this.nomTech = nomTech;
	}
	public String getSim() {
		return sim;
	}
	public void setSim(String sim) {
		this.sim = sim;
	}
	public String getLvcan() {
		return lvcan;
	}
	public void setLvcan(String lvcan) {
		this.lvcan = lvcan;
	}
	public String getPsn() {
		return psn;
	}
	public void setPsn(String psn) {
		this.psn = psn;
	}
	public String getMatricule() {
		return matricule;
	}
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
	public String getCurrLoc() {
		return currLoc;
	}
	public void setCurrLoc(String currLoc) {
		this.currLoc = currLoc;
	}
	
	 
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	  
	
	 
	 
}
