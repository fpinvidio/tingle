package com.plip.persistence.dao.interfaces;

import com.plip.persistence.model.Status;

public interface StatusDao {
	
	public Integer addStatus(Status status);
	
	public Status getStatus(int idStatus);
	
	public void updateStatus(Status status);
	
	public void deleteStatus(Integer statusId);

}
