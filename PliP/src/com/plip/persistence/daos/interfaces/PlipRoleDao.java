package com.plip.persistence.daos.interfaces;

import com.plip.exceptions.persistence.NullModelAttributesException;
import com.plip.exceptions.persistence.PlipRoleNotFoundException;
import com.plip.persistence.model.PlipRole;

public interface PlipRoleDao {
	
	public Long addRole(PlipRole role) throws NullModelAttributesException;
	
	public PlipRole getRole(long idRole) throws PlipRoleNotFoundException;
	
	public void updateRole(PlipRole role) throws PlipRoleNotFoundException;
	
	public void deleteRole(long roleId);

}
