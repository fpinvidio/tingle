package com.plip.persistence.dao.interfaces;

import com.plip.persistence.model.Position;

public interface PositionDao {
	
	public Integer addPosition(Position position);
	
	public Position getPosition(int idPosition);
	
	public void updatePosition(Position position);
	
	public void deletePosition(Integer positionId);

}
