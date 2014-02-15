package com.plip.persistence.daos.interfaces;

import com.plip.exceptions.persistence.NullModelAttributesException;
import com.plip.exceptions.persistence.StatusNotFoundException;
import com.plip.persistence.model.Status;

public interface StatusDao {
	
	public Long addStatus(Status status) throws NullModelAttributesException;
	
	public Status getStatus(long idStatus) throws StatusNotFoundException;
	
	public void updateStatus(Status status) throws StatusNotFoundException;
	
	public void deleteStatus(long statusId);

}
