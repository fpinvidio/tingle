package com.plip.persistence.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Status
 */
public class Status implements java.io.Serializable {

	private Long idStatus;
	private String description;
	private Set trayStatuses = new HashSet(0);

	public Status() {
	}

	public Status(String description) {
		this.description = description;
	}

	public Status(String description, Set trayStatuses) {
		this.description = description;
		this.trayStatuses = trayStatuses;
	}
	
	public boolean validate(){
		if(description != null){
			return true;
		}else return false;
	}

	public Long getIdStatus() {
		return this.idStatus;
	}

	public void setIdStatus(Long idStatus) {
		this.idStatus = idStatus;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set getTrayStatuses() {
		return this.trayStatuses;
	}

	public void setTrayStatuses(Set trayStatuses) {
		this.trayStatuses = trayStatuses;
	}

}
