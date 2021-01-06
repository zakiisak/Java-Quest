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

public class TreantEvent extends TileEvent implements Serializable {
	
	public TreantEvent()
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
			Battle battle = Game.instance.enterBossBattle(Enemy.getEnemy("treant"));
			battle.battlePreFinishEvent = battlePreFinished;
			battle.battleFinishEvent = battleFinished;
		}
	};
	
	private Battle.PostEvent battlePreFinished = new Battle.PostEvent()  {
		
		public void onEnded(final Battle battle) {
			if(!battle.victory) //Only do this code if the player was victorious
				return;
			if(battle.player.possessItem(Items.SMITH_KEY) == false)
			{
				battle.enemy.dead = true;
				ItemPopup popup = new ItemPopup(Items.SMITH_KEY, 5, "You received the", "SMITH KEY!");
				popup.obliterationEvent = new Event() {
					@Override
					public void run() {
						((TurnComponent)battle.getComponent("turn")).turn = -2;
					}
				};
				battle.game.addEntity(popup);
				battle.dead = false;
			}
		}
	};
	
	private Battle.PostEvent battleFinished = new Battle.PostEvent()  {
		
		public void onEnded(Battle battle) {
			if(battle.victory)
				dead = true;
		}
	};
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		Tile.tiles[44].draw(batch, (int) transform.x, (int) transform.y);
	}
	
}
