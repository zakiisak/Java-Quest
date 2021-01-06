package game.net;

public class PacketArenaProceed extends PacketUnicast {
	public int index;
	
	public PacketArenaProceed(int fromId, int toId, int index)
	{
		this.fromId = fromId;
		this.toId = toId;
		this.index = index;
	}
	
}
