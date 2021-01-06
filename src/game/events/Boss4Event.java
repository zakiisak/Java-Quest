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

public class Boss4Event extends TileEvent implements Serializable {
	
	public Boss4Event mainEvent;
	public boolean inversed = false;
	
	public Boss4Event(boolean inversed)
	{
		this.inversed = inversed;
	}
	
	
	public Boss4Event()
	{
		
	}
	
	public Boss4Event(Boss4Event mainEvent)
	{
		this.mainEvent = mainEvent;
	}
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		MessageBox.addMessage(new MessageBox("Don't worry,", "it'll only", "take a second").setDoneEvent(interactionInitiated));
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		return false;
	}
	
	private Event interactionInitiated = new Event()  {
		
		@Override
		public void run() {
			Battle battle = Game.instance.enterBossBattle(Enemy.getEnemy((inversed ? "inv_" : "") + "snake_dragon"));
			((CentifierComponent) battle.enemy.getComponent("centifier")).yoffset -= 60;
			battle.battleFinishEvent = battleFinished;
			battle.battlePreFinishEvent = battlePreFinished;
		}
	};
	
	private Battle.PostEvent battlePreFinished = new Battle.PostEvent()  {
		
		public void onEnded(final Battle battle) {
			if(!battle.victory) //Only do this code if the player was victorious
				return;
			battle.enemy.dead = true;
			ItemPopup popup = new ItemPopup(Items.RUBY, 5, "You received the", "RUBY!");
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
				dead = true;
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
			Sprite.getSprite((inversed ? "inv_" : "") + "snake_dragon").render(batch, transform.x * Tile.SIZE + Tile.SIZE / 2 - Tile.SIZE / 2 * 2.5f, transform.y * Tile.SIZE + Tile.SIZE / 2 - Tile.SIZE * 3, Tile.SIZE * 4, Tile.SIZE * 4);
	}
	
}
