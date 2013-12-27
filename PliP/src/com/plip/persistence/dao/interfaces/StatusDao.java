package com.plip.persistence.dao.interfaces;

import com.plip.persistence.exceptions.StatusNotFoundException;
import com.plip.persistence.model.Status;

public interface StatusDao {
	
	public Long addStatus(Status status);
	
	public Status getStatus(long idStatus) throws StatusNotFoundException;
	
	public void updateStatus(Status status) throws StatusNotFoundException;
	
	public void deleteStatus(long statusId);

}
