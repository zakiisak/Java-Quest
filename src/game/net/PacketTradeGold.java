package game.net;

import java.math.BigInteger;

public class PacketTradeGold {
	public int fromId;
	public int toId;
	public byte[] gold;
	
	public PacketTradeGold() {}
	
	public PacketTradeGold(int fromId, int toId, BigInteger gold)
	{
		this.fromId = fromId;
		this.toId = toId;
		this.gold = gold.toByteArray();
	}
	
}
