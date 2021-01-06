package game.worlds;

import java.io.Serializable;

public class SpawnPoint implements Serializable {
	
	public int x, y, world;
	
	public SpawnPoint() {}
	
	
	public SpawnPoint(SpawnPoint sp)
	{
		this.x = sp.x;
		this.y = sp.y;
		this.world = sp.world;
	}
	
	
	public SpawnPoint(int x, int y, int world)
	{
		this.x = x;
		this.y = y;
		this.world = world;
	}
	
	public void set(int x, int y, int world)
	{
		this.x = x;
		this.y = y;
		this.world = world;
	}
	
	public void set(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void set(int world)
	{
		this.world = world;
	}
	
}
