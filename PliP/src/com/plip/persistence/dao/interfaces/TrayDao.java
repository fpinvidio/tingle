package com.plip.persistence.dao.interfaces;

import com.plip.persistence.model.Tray;

public interface TrayDao {
	
	public Long addTray(Tray tray);
	
	public Tray getTray(long idTray);
	
	public void updateTray(Tray tray);
	
	public void deleteTray(long trayId);

}
