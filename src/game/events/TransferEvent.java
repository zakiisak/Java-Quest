package game.events;

import game.Game;
import game.entity.Player;
import game.entity.TileEvent;
import game.entity.Worlds;
import game.entitycomponent.PlayerInputComponent;
import game.graphics.Sprite;
import game.utils.Event;
import game.utils.Point;
import game.worlds.World;

public class TransferEvent extends TileEvent {
	
	private int world;
	private final Point teleportTo;
	public Event onTransferred;
	
	public TransferEvent(int world, int x, int y)
	{
		this.world = world;
		this.teleportTo = new Point(x, y);
	}
	
	public TransferEvent(int x, int y)
	{
		this.world = -2;
		this.teleportTo = new Point(x, y);
	}
	
	public TransferEvent(Class<? extends World> worldClass, int x, int y) {
		this.world = Worlds.getWorldId(worldClass);
		this.teleportTo = new Point(x, y);
		System.out.println("Transfer Event world: " + this.world);
	}
	
	public TransferEvent setOnTransferredEvent(Event event)
	{
		this.onTransferred = event;
		return this;
	}
	
	private void callOnTransferredEvent()
	{
		if(onTransferred != null)
			onTransferred.run();
	}
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		if(this.world >= 0)
		{
			System.out.println("transfer player");
			Worlds.transferPlayer(this.world, teleportTo.x, teleportTo.y);
			System.out.println("transfer player post");
			callOnTransferredEvent();
		}
		else if(this.world == -2)
		{
			player.transform.set(teleportTo.x, teleportTo.y);
			((PlayerInputComponent)player.getComponent("player_input")).resetLastCoordinates = true;
			callOnTransferredEvent();
		}
	}
	
}
