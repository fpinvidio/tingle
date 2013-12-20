package com.plip.persistence.dao.interfaces;

import com.plip.persistence.model.PlipUser;

public interface PlipUserDao {
	
	public Integer addUser(PlipUser user) ;
	
	public PlipUser getUser(int idUser);
	
	public void updateUser(PlipUser user);
	
	public void deleteUser(Integer userId);
}
