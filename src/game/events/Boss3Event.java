package game.events;

import java.io.Serializable;
import java.math.BigInteger;

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
import game.graphics.Sprite;
import game.tile.Tile;
import game.utils.Event;
import game.worlds.World;

public class Boss3Event extends TileEvent implements Serializable {
	
	public boolean inversed;
	
	public Boss3Event(boolean inversed)
	{
		this.inversed = inversed;
	}
	
	public Boss3Event()
	{
		
	}
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		MessageBox.addMessage(new MessageBox("I'm gonna", "destroy you"));
		MessageBox.addMessage(new MessageBox("No, something....", "Stronger....", "like..."));
		MessageBox.addMessage(new MessageBox("OBLITERAATE!!!").setDoneEvent(interactionInitiated));
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		return false;
	}
	
	private Event interactionInitiated = new Event()  {
		
		@Override
		public void run() {
			final Battle battle = Game.instance.enterBossBattle(Enemy.getEnemy((inversed ? "inv_" : "") + "cyclops"));
			battle.battleFinishEvent = battleFinished;
			battle.battlePreFinishEvent = battlePreFinished;
			if(Game.instance.player.possessItem(Items.SPEAR_OF_CYLOPIUM))
			{
				MessageBox.addMessage(new MessageBox("You use the", "SPEAR OF", "CYCLOPIUM").setDoneEvent(new Event() {
					
					@Override
					public void run() {
						battle.hitOpponent(new BigInteger("" + 600), false);
						Game.instance.player.removeItem(Items.SPEAR_OF_CYLOPIUM);
					}
				}));
			}
		}
	};
	
	private Battle.PostEvent battlePreFinished = new Battle.PostEvent()  {
		
		public void onEnded(final Battle battle) {
			if(!battle.victory) //Only do this code if the player was victorious
				return;
			battle.enemy.dead = true;
			ItemPopup popup = new ItemPopup(Items.EMERALD, 5, "You received the", "EMERALD!");
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
	public void drawPost(SpriteBatch batch) {
		super.draw(batch);
		Sprite.getSprite((inversed ? "inv_" : "") + "cyclops").render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
