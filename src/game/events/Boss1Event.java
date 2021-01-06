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

public class Boss1Event extends TileEvent implements Serializable {
	
	public boolean inversed = false;
	
	public Boss1Event(boolean inversed)
	{
		this.inversed = inversed;
	}
	
	public Boss1Event()
	{
		
	}
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		MessageBox.addMessage(new MessageBox("Prepare to ", "meet your", " friends"));
		MessageBox.addMessage(new MessageBox("in", "OBLIVIOOOON", "!!!").setDoneEvent(interactionInitiated));
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		return false;
	}
	
	private Event interactionInitiated = new Event()  {
		
		@Override
		public void run() {
			Battle battle = Game.instance.enterBossBattle(Enemy.getEnemy(inversed ? "inv_boar" : "boar"));
			battle.battleFinishEvent = battleFinished;
			battle.battlePreFinishEvent = battlePreFinished;
		}
	};
	
	private Battle.PostEvent battlePreFinished = new Battle.PostEvent()  {
		
		public void onEnded(final Battle battle) {
			if(!battle.victory) //Only do this code if the player was victorious
				return;
			battle.enemy.dead = true;
			ItemPopup popup = new ItemPopup(Items.EARTH_GEM, 5, "You received the", "EARTH GEM!");
			popup.obliterationEvent = new Event() {
				@Override
				public void run() {
					((TurnComponent)battle.getComponent("turn")).turn = -2;
				}
			};
			battle.game.addEntity(popup);
			battle.dead = false;
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
		Sprite.getSprite(inversed ? "inv_boar" : "Boar").render(batch, transform.x * Tile.SIZE + Tile.SIZE, transform.y * Tile.SIZE, -Tile.SIZE, Tile.SIZE);
	}
	
}
