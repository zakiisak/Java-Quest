package game.net;

public class SendObject {
	public Object obj;
	public boolean tcp = false;
	
	public SendObject(Object obj, boolean tcp)
	{
		this.obj = obj;
		this.tcp = tcp;
	}
	
}
