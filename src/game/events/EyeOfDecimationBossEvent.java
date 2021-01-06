package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.battle.Battle;
import game.battle.Battle.PostEvent;
import game.battle.Enemy;
import game.battle.TurnComponent;
import game.entity.ItemPopup;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.graphics.Sprite;
import game.tile.Tile;
import game.utils.Event;
import game.entity.Player;
import game.entity.TileEvent;
import game.worlds.World;

public class EyeOfDecimationBossEvent extends TileEvent {
	
	private boolean victory = false;
	public boolean inversed;
	
	public EyeOfDecimationBossEvent() {
		// TODO Auto-generated constructor stub
	}
	
	public EyeOfDecimationBossEvent(boolean inversed)
	{
		this.inversed = inversed;
	}
	
	@Override
	public void onStepped(final Game game, World world, Player player) {
		super.onStepped(game, world, player);
		MessageBox.addMessage("Prepare to", "get decimated!!").setDoneEvent(new Event() {
			@Override
			public void run() {
				Battle battle = game.enterBossBattle(Enemy.getEnemy((inversed ? "inv_" : "") + "eye_of_decimation"));
				battle.battlePreFinishEvent = preFinish;
				battle.battleFinishEvent = finish;
			}
		});
	}
	
	private PostEvent preFinish = new Battle.PostEvent()
	{
		public void onEnded(final Battle battle) {
			if(battle.victory == false)
				return;
			battle.dead = false;
			ItemPopup popup = new ItemPopup(Items.GREEN_PENDANT, 5, "You received", "the GREEN", "PENDANT!!!");
			popup.obliterationEvent = new Event() {
				
				@Override
				public void run() {
					((TurnComponent)battle.getComponent("turn")).turn = -2;					
				}
			};
			battle.game.addEntity(popup);
		}
	};
	
	private PostEvent finish = new Battle.PostEvent()
	{
		@Override
		public void onEnded(Battle battle) {
			if(battle.victory)
			{
				victory = true;
				dead = true;
				TileEvent event = world.getEvent(50, 55);
				if(event != null)
					event.dead = true;
			}
		}
	};
	
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		onStepped(game, world, player);
		return true;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		Sprite.getSprite((inversed ? "inv_" : "") + "eye_of_decimation").render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
