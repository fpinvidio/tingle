package com.plip.persistence.dao.interfaces;

import com.plip.persistence.model.Tray;

public interface TrayDao {
	
	public Long addTray(Tray tray);
	
	public Tray getTray(int idTray);
	
	public void updateTray(Tray tray);
	
	public void deleteTray(Tray trayId);

}
