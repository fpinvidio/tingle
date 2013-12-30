package com.plip.persistence.dao.interfaces;

import com.plip.persistence.exceptions.NullModelAttributesException;
import com.plip.persistence.exceptions.PositionNotFoundException;
import com.plip.persistence.model.Position;

public interface PositionDao {
	
	public Long addPosition(Position position) throws NullModelAttributesException;
	
	public Position getPosition(long idPosition) throws PositionNotFoundException;
	
	public void updatePosition(Position position) throws PositionNotFoundException;
	
	public void deletePosition(long positionId);

}
