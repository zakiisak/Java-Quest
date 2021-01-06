package game.net;

public class PacketArenaBattleUpdate extends PacketUnicast {
	public String header; //Can either be change owner or change combattant id.
	public int data;
	
	public PacketArenaBattleUpdate()
	{
		
	}
	
	public PacketArenaBattleUpdate(int fromId, int toId, String header, int data)
	{
		this.fromId = fromId;
		this.toId = toId;
		this.header = header;
		this.data = data;
	}
}
