package com.plip.persistence.dao.interfaces;

import java.util.List;

import com.plip.persistence.exceptions.TrayStatusNotFoundException;
import com.plip.persistence.model.Status;
import com.plip.persistence.model.Tray;
import com.plip.persistence.model.TrayStatus;

public interface TrayStatusDao {
	
	public Long addTrayStatus(TrayStatus trayStatus) ;
	
	public List <Tray> getTraysByStatus(long idStatus) throws TrayStatusNotFoundException;
	
	public List <TrayStatus> getStatusByTray(long idTray) throws TrayStatusNotFoundException;
	
	public void updateTrayStatus(TrayStatus trayStatus) throws TrayStatusNotFoundException;
	
	public void deleteTrayStatus(long trayStatusId);

}
