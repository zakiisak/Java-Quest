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
import game.entity.Worlds;
import game.graphics.Sprite;
import game.tile.Tile;
import game.utils.Event;
import game.worlds.CavePassage;
import game.worlds.World;

public class Boss6Event extends TileEvent implements Serializable {
	
	public boolean inversed;
	
	public Boss6Event(boolean inversed)
	{
		this.inversed = inversed;
	}
	
	public Boss6Event()
	{
		
	}
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		MessageBox.addMessage(new MessageBox("Let me put", "you out of", "your misery!").setDoneEvent(interactionInitiated));
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		return false;
	}
	
	private Event interactionInitiated = new Event()  {
		
		@Override
		public void run() {
			final Battle battle = Game.instance.enterBossBattle(Enemy.getEnemy((inversed ? "inv_" : "") + "wildpine"));
			battle.battleFinishEvent = battleFinished;
			battle.battlePreFinishEvent = battlePreFinished;
			if(Game.instance.player.possessItem(Items.CALMING_POTION))
			{
				MessageBox.addMessage(new MessageBox("You use the", "CALMING POTION").setDoneEvent(new Event() {
					
					@Override
					public void run() {
						battle.hitOpponent(battle.enemy.stats.maxhp, false);
						Game.instance.player.removeItem(Items.CALMING_POTION);
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
			ItemPopup popup = new ItemPopup(Items.TOPAZ, 5, "You received the", "TOPAZ!");
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
			{
				if(inversed == false)
				{
					MessageBox.addMessage(new MessageBox("What..."));
					MessageBox.addMessage(new MessageBox("What happened", "to me...?"));
					MessageBox.addMessage(new MessageBox("You...             ", "Thank you."));
					MessageBox.addMessage(new MessageBox("Someone ", "possessed me....                ", "I heard a voice"));
					MessageBox.addMessage(new MessageBox("inside my", "head that. It", "made me go"));
					MessageBox.addMessage(new MessageBox("here. I fear", "something ", "terrible"));
					MessageBox.addMessage(new MessageBox("would happen if", "the seal ahead", "should break...", ""));
					MessageBox.addMessage(new MessageBox("I will go see", "my brother...", "Thanks again!").setDoneEvent(new Event() {
						
						@Override
						public void run() {
							Worlds.getWorld(CavePassage.class).putEvent(new CalmWildPineEvent(), 68, 134);
							dead = true;
							Game.instance.saveGame();
						}
					}));
				}
				else
				{
					dead = true;
				}
			}
		}
	};
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.draw(batch);
		Sprite.getSprite((inversed ? "inv_" : "") + "wildpine").render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
