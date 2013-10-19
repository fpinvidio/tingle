package eventhandling;


public interface GenericTrayEvent {

	public boolean isOfType(String type);
	public String getEventType();

}
