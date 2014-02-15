package com.plip.persistence.daos.interfaces;

import com.plip.exceptions.persistence.NullModelAttributesException;
import com.plip.exceptions.persistence.PositionNotFoundException;
import com.plip.persistence.model.Position;

public interface PositionDao {
	
	public Long addPosition(Position position) throws NullModelAttributesException;
	
	public Position getPosition(long idPosition) throws PositionNotFoundException;
	
	public void updatePosition(Position position) throws PositionNotFoundException;
	
	public void deletePosition(long positionId);

}
