package com.plip.persistence.dao.interfaces;

import com.plip.persistence.model.PlipRole;

public interface PlipRoleDao {
	
	public Integer addRole(PlipRole role) ;
	
	public PlipRole getRole(int idRole);
	
	public void updateRole(PlipRole role);
	
	public void deleteRole(Integer roleId);

}
