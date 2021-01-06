package game.net;

public class PacketTransform {
	public int id, world;
	public float x, y;
	
	public PacketTransform() {}
	public PacketTransform(int id, int world, float x, float y) {
		this.id = id;
		this.world = world;
		this.x = x;
		this.y = y;
	}
	
}
