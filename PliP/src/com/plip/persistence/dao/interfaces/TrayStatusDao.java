package com.plip.persistence.dao.interfaces;

import java.util.List;

import com.plip.persistence.model.Status;
import com.plip.persistence.model.Tray;
import com.plip.persistence.model.TrayStatus;

public interface TrayStatusDao {
	
	public Long addTrayStatus(TrayStatus trayStatus) ;
	
	public List <Tray> getTraysByStatus(long idStatus);
	
	public List <Status> getStatusByTray(long idTray);
	
	public void updateTrayStatus(TrayStatus trayStatus);
	
	public void deleteTrayStatus(long trayStatusId);

}
