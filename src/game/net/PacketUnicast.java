package game.net;

public class PacketUnicast {
	public int fromId;
	public int toId;
	
	public PacketUnicast(int fromId, int toId)
	{
		this.fromId = fromId;
		this.toId = toId;
	}
	
	public PacketUnicast() {
		
	}
	
}
