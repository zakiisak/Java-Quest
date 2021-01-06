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

public class Boss5Event extends TileEvent implements Serializable {
	
	public boolean visible = false;
	public boolean inversed;
	
	public Boss5Event(boolean inversed)
	{
		this.inversed = inversed;
		this.visible = true;
	}
	
	
	public Boss5Event()
	{
		
	}
	
	public Boss5Event(Boss5Event mainEvent)
	{
		
	}
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		if(inversed)
			onInteraction(game, world, player);
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		MessageBox.addMessage(new MessageBox("Let me show", "you the wrath", "of LATHROS!!").setDoneEvent(interactionInitiated));
		visible = true;
		return true;
	}
	
	private Event interactionInitiated = new Event()  {
		@Override
		public void run() {
			Battle battle = Game.instance.enterBossBattle(Enemy.getEnemy((inversed ? "inv_" : "") + "lathros_bane"));
			((CentifierComponent) battle.enemy.getComponent("centifier")).yoffset -= 40;
			battle.battleFinishEvent = battleFinished;
			battle.battlePreFinishEvent = battlePreFinished;
		}
	};
	
	private Battle.PostEvent battlePreFinished = new Battle.PostEvent()  {
		
		public void onEnded(final Battle battle) {
			if(!battle.victory) //Only do this code if the player was victorious
				return;
			battle.enemy.dead = true;
			ItemPopup popup = new ItemPopup(Items.SAPPHIRE, 5, "You received the", "SAPPHIRE!");
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
			}
		}
	};
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		if(visible)
		{
			if(inversed) 
			{
				Sprite.getSprite("inv_lathros_bane").render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
			}
			else
			{
				Sprite.getSprite("lathros_bane").render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE + Tile.SIZE, Tile.SIZE, Tile.SIZE);
			}
			
		}
	}
	
}
