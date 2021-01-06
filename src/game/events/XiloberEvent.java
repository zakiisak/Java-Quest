package game.events;

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

public class XiloberEvent extends TileEvent {
	
	public boolean visible = false;
	public boolean inversed;
	
	public XiloberEvent() {
		// TODO Auto-generated constructor stub
	}
	public XiloberEvent(boolean inversed)
	{
		this.inversed = true;
		visible = true;
	}
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		super.onStepped(game, world, player);
		if(inversed)
			onInteraction(game, world, player);
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		MessageBox.addMessage("Hahaahaa, so", "we meet at", "last", "Let me put", "you out of", "your MISERY!!").setDoneEvent(interactionInitiated);
		visible = true;
		return true;
	}
	
	private Event interactionInitiated = new Event()  {
		@Override
		public void run() {
			Battle battle = Game.instance.enterBossBattle(Enemy.getEnemy((inversed ? "inv_" : "") + "xilober"));
			battle.battleFinishEvent = battleFinished;
			battle.battlePreFinishEvent = battlePreFinished;
		}
	};
	
	private Battle.PostEvent battlePreFinished = new Battle.PostEvent()  {
		
		public void onEnded(final Battle battle) {
			if(!battle.victory) //Only do this code if the player was victorious
				return;
			battle.enemy.dead = false;
			MessageBox.addMessage("NOOOO!", "How can a mere", "human have this", "kind of power").setDoneEvent(new Event() {
				@Override
				public void run() {
					battle.enemy.dead = true;
					ItemPopup popup = new ItemPopup(Items.BLUE_PENDANT, 5, "You received", "the BLUE", "PENDANT!");
					popup.obliterationEvent = new Event() {
						@Override
						public void run() {
							((TurnComponent)battle.getComponent("turn")).turn = -2;
						}
					};
					battle.game.addEntity(popup);
				}
			});
			
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
	public void drawPost(SpriteBatch batch) {
		if(visible)
		{
			if(inversed)
			{
				Sprite.getSprite("inv_xilober").render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
			}
			else
			{
				Sprite.getSprite("xilober").render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE + Tile.SIZE, Tile.SIZE, Tile.SIZE);
			}
		}
	}
	
}