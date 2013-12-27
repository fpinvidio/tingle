package com.plip.persistence.dao.interfaces;

import com.plip.persistence.model.Position;

public interface PositionDao {
	
	public Long addPosition(Position position);
	
	public Position getPosition(long idPosition);
	
	public void updatePosition(Position position);
	
	public void deletePosition(long positionId);

}
