package game.net;

public class PacketTradeCommence {
	public int fromId;
	public int toId;
	
	public PacketTradeCommence()
	{
		
	}
	
	public PacketTradeCommence(int fromId, int toId)
	{
		this.fromId = fromId;
		this.toId = toId;
	}
}
