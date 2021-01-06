package game.net;

public class PacketJoinBattle {
	
	public int fromId;
	public int toId;
	
	public PacketJoinBattle()
	{
		
	}
	
	public PacketJoinBattle(int fromId, int toId)
	{
		this.fromId = fromId;
		this.toId = toId;
	}
	
}
