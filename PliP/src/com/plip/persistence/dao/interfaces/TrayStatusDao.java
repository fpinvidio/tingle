package com.plip.persistence.dao.interfaces;

import java.util.List;

import com.plip.persistence.model.Status;
import com.plip.persistence.model.Tray;
import com.plip.persistence.model.TrayStatus;

public interface TrayStatusDao {
	
	public Integer addTrayStatus(TrayStatus trayStatus) ;
	
	public List <Tray> getTraysByStatus(int idStatus);
	
	public List <Status> getStatusByTray(int idTray);
	
	public void updateTrayStatus(TrayStatus trayStatus);
	
	public void deleteTrayStatus(Integer trayStatusId);

}
