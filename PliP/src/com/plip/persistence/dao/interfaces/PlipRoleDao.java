package com.plip.persistence.dao.interfaces;

import com.plip.persistence.exceptions.PlipRoleNotFoundException;
import com.plip.persistence.model.PlipRole;

public interface PlipRoleDao {
	
	public Long addRole(PlipRole role);
	
	public PlipRole getRole(long idRole) throws PlipRoleNotFoundException;
	
	public void updateRole(PlipRole role) throws PlipRoleNotFoundException;
	
	public void deleteRole(long roleId);

}
