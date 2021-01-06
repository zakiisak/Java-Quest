package game.events;

import java.io.Serializable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.battle.Battle;
import game.battle.Enemy;
import game.battle.TurnComponent;
import game.entity.ItemPopup;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.Player;
import game.entity.TileEvent;
import game.entitycomponent.CentifierComponent;
import game.graphics.Sprite;
import game.tile.Tile;
import game.utils.Event;
import game.worlds.World;

public class TwiDragonEvent extends TileEvent implements Serializable {
	
	public TwiDragonEvent mainEvent;
	
	public TwiDragonEvent()
	{
		
	}
	
	public TwiDragonEvent(TwiDragonEvent mainEvent)
	{
		this.mainEvent = mainEvent;
	}
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		MessageBox.addMessage("Don't worry,", "it'll only", "take another", "second...").setDoneEvent(interactionInitiated);
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		return false;
	}
	
	private Event interactionInitiated = new Event()  {
		
		@Override
		public void run() {
			Battle battle = Game.instance.enterBossBattle(Enemy.getEnemy("twilight_dragon"));
			((CentifierComponent) battle.enemy.getComponent("centifier")).yoffset -= 60;
			battle.battleFinishEvent = battleFinished;
		}
	};
	
	private Battle.PostEvent battleFinished = new Battle.PostEvent()  {
		
		public void onEnded(Battle battle) {
			if(battle.victory)
			{
				dead = true;
				battle.player.data.setBoolean("dragon_defeated", true);
				if(mainEvent != null)
					mainEvent.dead = true;
			}
		}
	};
	
	@Override
	public void tick() {
		super.tick();
		if(mainEvent != null)
		{
			if(mainEvent.dead)
				dead = true;
		}
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		if(mainEvent == null)
			Sprite.getSprite("twilight_dragon").render(batch, transform.x * Tile.SIZE + Tile.SIZE / 2 - Tile.SIZE * 1.8f, transform.y * Tile.SIZE + Tile.SIZE / 2 - Tile.SIZE * 3, Tile.SIZE * 4, Tile.SIZE * 4);
	}
	
}
