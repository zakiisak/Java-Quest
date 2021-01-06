package game.net;

import game.entitycomponent.StatComponent;

public class PacketStatComponent {
	
	public int id;
	public StatComponent stats;
	public String name;
	
	public PacketStatComponent() {}
	
	public PacketStatComponent(int id, StatComponent stats, String name)
	{
		this.id = id;
		this.stats = stats;
		this.name = name;
	}
	
}
