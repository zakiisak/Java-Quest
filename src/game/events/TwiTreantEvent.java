package game.events;

import java.io.Serializable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.battle.Battle;
import game.battle.Enemy;
import game.battle.TurnComponent;
import game.battle.Zones;
import game.entity.ItemPopup;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.Player;
import game.entity.TileEvent;
import game.graphics.Sprite;
import game.tile.Tile;
import game.utils.Event;
import game.worlds.World;

public class TwiTreantEvent extends TileEvent implements Serializable {
	
	public TwiTreantEvent()
	{
		
	}
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		MessageBox.addMessage(new MessageBox("Taste my", "branch!!!").setDoneEvent(interactionInitiated));
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		onStepped(game, world, player);
		return true;
	}
	
	private Event interactionInitiated = new Event()  {
		
		@Override
		public void run() {
			Battle battle = Game.instance.enterBossBattle(Enemy.getEnemy("twilight_golem"));
			battle.battleFinishEvent = battleFinished;
		}
	};
	
	private Battle.PostEvent battleFinished = new Battle.PostEvent()  {
		
		public void onEnded(Battle battle) {
			if(battle.victory)
			{
				dead = true;
				battle.game.world.foregroundTiles[transform.getIntX() + transform.getIntY() * world.width] = 0;
				Game.instance.player.data.setBoolean("golem_defeated", true);
			}
		}
	};
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
	}
	
}
