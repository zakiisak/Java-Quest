package game.entitycomponent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.EntityComponent;
import game.entity.Player;
import game.net.PacketBattleIndicator;
import game.net.PacketTransform;
import game.scene.SceneGame;

public class NetComponent extends EntityComponent {

	public Player player;
	public int lastWorld = -1;
	public float lastX, lastY;
	public int counter = 40;
	
	public NetComponent(Player player) {
		this.player = player;
	}
	
	@Override
	public String getName() {
		return "net";
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		if(Game.instance.client.isConnected() == false)
			return;
		if(player.transform.x != lastX || player.transform.y != lastY || Game.instance.world.id != lastWorld)
		{
			Game.instance.sendUDP(new PacketTransform(-1, Game.instance.world.id, player.transform.x, player.transform.y));
		}
		if(counter <= 0)
		{
			Game.instance.sendTCP(new PacketBattleIndicator(-1, false));
			counter = 40;
		}
		counter--;
	}
	
	public void setBattle()
	{
		Game.instance.sendTCP(new PacketBattleIndicator(-1, true));
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

}
