package game.net;

public class PacketRequestTrade {
	
	public int fromId;
	public int toId;
	
	public PacketRequestTrade()
	{
		
	}
	
	public PacketRequestTrade(int fromId, int toId)
	{
		this.fromId = fromId;
		this.toId = toId;
	}
	
}
