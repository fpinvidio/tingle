package com.plip.persistence.dao.interfaces;

import com.plip.persistence.model.Status;

public interface StatusDao {
	
	public Long addStatus(Status status);
	
	public Status getStatus(long idStatus);
	
	public void updateStatus(Status status);
	
	public void deleteStatus(long statusId);

}
