package com.plip.persistence.daos.interfaces;

import com.plip.exceptions.persistence.NullModelAttributesException;
import com.plip.exceptions.persistence.PlipUserNotFoundException;
import com.plip.persistence.model.PlipUser;

public interface PlipUserDao {
	
	public Long addUser(PlipUser user) throws NullModelAttributesException ;
	
	public PlipUser getUser(long idUser) throws PlipUserNotFoundException;
	
	public void updateUser(PlipUser user) throws PlipUserNotFoundException;
	
	public void deleteUser(long userId);
}
