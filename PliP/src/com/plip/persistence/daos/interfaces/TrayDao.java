package com.plip.persistence.daos.interfaces;

import com.plip.exceptions.persistence.NullModelAttributesException;
import com.plip.exceptions.persistence.TrayNotFoundException;
import com.plip.persistence.model.Tray;

public interface TrayDao {
	
	public Long addTray(Tray tray) throws NullModelAttributesException;
	
	public Tray getTray(long idTray) throws TrayNotFoundException;
	
	public void updateTray(Tray tray) throws TrayNotFoundException;
	
	public void deleteTray(long trayId);

}
