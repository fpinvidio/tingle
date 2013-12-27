package com.plip.persistence.dao.interfaces;

import com.plip.persistence.model.PlipUser;

public interface PlipUserDao {
	
	public Long addUser(PlipUser user) ;
	
	public PlipUser getUser(long idUser);
	
	public void updateUser(PlipUser user);
	
	public void deleteUser(long userId);
}
