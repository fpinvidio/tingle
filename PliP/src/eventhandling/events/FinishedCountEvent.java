package eventhandling.events;

import java.util.EventObject;

public class FinishedCountEvent extends EventObject {

	private static final long serialVersionUID = 272855349758957367L;

	public FinishedCountEvent(Object source) {
		super(source);
	}

}
