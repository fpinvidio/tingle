package com.plip.persistence.managers;

import com.plip.persistence.managers.exceptions.NoPageRecievedException;
import com.plip.persistence.model.Page;

public interface PageManager {
 
	public Page getLastPage() throws NoPageRecievedException;
}
