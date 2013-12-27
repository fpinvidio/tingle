package com.plip.persistence.dao.interfaces;

import com.plip.persistence.exceptions.PlipUserNotFoundException;
import com.plip.persistence.model.PlipUser;

public interface PlipUserDao {
	
	public Long addUser(PlipUser user) ;
	
	public PlipUser getUser(long idUser) throws PlipUserNotFoundException;
	
	public void updateUser(PlipUser user) throws PlipUserNotFoundException;
	
	public void deleteUser(long userId);
}
