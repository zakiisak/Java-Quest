package game.net;

public class PacketBattleUpdate {
	public int[] players;
	public Object data;
	
	public PacketBattleUpdate()
	{
		
	}
	
	public PacketBattleUpdate(Object data)
	{
		this.data = data;
	}
}
