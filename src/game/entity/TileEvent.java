package game.entity;

import game.Game;
import game.entitycomponent.TransformComponent;
import game.worlds.World;

public class TileEvent extends Entity {
	
	public TransformComponent transform = new TransformComponent();
	public World world;
	
	//When moving unto a tile
	public void onStepped(Game game, World world, Player player)
	{
		
	}
	
	//Requires to step on the event
	public boolean onInteraction(Game game, World world, Player player)
	{
		return false;
	}
	
	public boolean isPassable()
	{
		return true;
	}
	
}
