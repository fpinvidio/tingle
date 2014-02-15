package com.plip.system.providers;

import com.plip.exceptions.persistence.NoPageRecievedException;
import com.plip.exceptions.persistence.PageNotFoundException;
import com.plip.persistence.model.Page;

public interface PageProvider {
 
	public Page getLastPage() throws NoPageRecievedException, PageNotFoundException;
}
