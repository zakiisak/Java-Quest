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

public class Boss2Event extends TileEvent implements Serializable {
	
	public boolean inversed = false;
	
	public Boss2Event(boolean inversed)
	{
		this.inversed = inversed;
	}
	
	public Boss2Event()
	{
		
	}
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		//MessageBox.addMessage(new MessageBox("So you made it", "through the Boar", "huh.."));
		//MessageBox.addMessage(new MessageBox("Now prepare to", "meet your", "MAKERRR!!!").setDoneEvent(interactionInitiated));
		MessageBox.addMessage(new MessageBox("Already here huh", "........"));
		MessageBox.addMessage(new MessageBox("Well, if you're", "in such a hurry", "to meet your doom").setDoneEvent(interactionInitiated));
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		return false;
	}
	
	private Event interactionInitiated = new Event()  {
		
		@Override
		public void run() {
			Battle battle = Game.instance.enterBossBattle(Enemy.getEnemy(inversed ? "inv_knight" : "knight"));
			battle.battleFinishEvent = battleFinished;
			battle.battlePreFinishEvent = battlePreFinished;
		}
	};
	
	private Battle.PostEvent battlePreFinished = new Battle.PostEvent()  {
		
		public void onEnded(final Battle battle) {
			if(!battle.victory) //Only do this code if the player was victorious
				return;
			battle.enemy.dead = true;
			MessageBox.addMessage(new MessageBox("Nooo!!!"));
			MessageBox.addMessage(new MessageBox("How did this", "happen!!!").setDoneEvent(new Event() {
				
				@Override
				public void run() {
					if(inversed == false)
					{
						if(battle.player.possessItem(Items.BOOK_OF_CONCEDE) == false)
						{
							ItemPopup popup = new ItemPopup(Items.BOOK_OF_CONCEDE, 5, "You received", "the BOOK OF ", "CONCEDE!", "With this you", "can surrender", "any battle").setObliterationEvent(new Event() {
								@Override
								public void run() {
									((TurnComponent)battle.getComponent("turn")).turn = -2;
								}
							});
							battle.game.addEntity(popup);
						}
						else ((TurnComponent)battle.getComponent("turn")).turn = -2;
					}
					else
						((TurnComponent)battle.getComponent("turn")).turn = -2;
				}
			}));
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
		Sprite.getSprite(inversed ? "inv_knight" : "knight").render(batch, transform.x * Tile.SIZE + Tile.SIZE, transform.y * Tile.SIZE, -Tile.SIZE, Tile.SIZE);
	}
	
}
