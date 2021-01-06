package game.entity;

import game.entitycomponent.StatComponent;
import game.entitycomponent.TransformComponent;

public class MPEntity extends Entity
{
	public int id = -1;
	public int world = 0;
	public TransformComponent transform;
	public long lastTimeUpdated = System.currentTimeMillis();
	public boolean inBattle = false;
	public String name = "Unknown";
	public StatComponent stats = new StatComponent();
	
	public MPEntity()
	{
		
	}
	
	public MPEntity(MPEntity entity)
	{
		this.id = entity.id;
		this.transform = entity.transform;
		this.world = entity.world;
		this.lastTimeUpdated = entity.lastTimeUpdated;
		this.inBattle = entity.inBattle;
		this.name = entity.name;
		this.stats = entity.stats;
	}
	
}