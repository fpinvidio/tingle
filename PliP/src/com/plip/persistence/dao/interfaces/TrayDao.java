package com.plip.persistence.dao.interfaces;

import com.plip.persistence.exceptions.NullModelAttributesException;
import com.plip.persistence.exceptions.TrayNotFoundException;
import com.plip.persistence.model.Tray;

public interface TrayDao {
	
	public Long addTray(Tray tray) throws NullModelAttributesException;
	
	public Tray getTray(long idTray) throws TrayNotFoundException;
	
	public void updateTray(Tray tray) throws TrayNotFoundException;
	
	public void deleteTray(long trayId);

}
