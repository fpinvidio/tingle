package eventhandling.events;

import java.util.EventObject;

public class StartedCountEvent extends EventObject {

	private static final long serialVersionUID = 3615741944257423287L;

	public StartedCountEvent(Object source) {
		super(source);
	}

}
