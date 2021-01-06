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
import game.tile.Tile;
import game.utils.Event;
import game.entity.Player;
import game.entity.TileEvent;
import game.worlds.World;

public class EvilMirrorBossEvent extends TileEvent {
	
	private boolean victory = false;
	public boolean inversed;
	
	public EvilMirrorBossEvent() {
	}
	
	public EvilMirrorBossEvent(boolean inversed) {
		this.inversed = inversed;
	}
	
	@Override
	public void onStepped(final Game game, World world, Player player) {
		super.onStepped(game, world, player);
		if(inversed)
		{
			MessageBox.addMessage("A red light", "glimmer in", "the dark...").setDoneEvent(new Event() {
				
				@Override
				public void run() {
					Battle battle = game.enterBossBattle(Enemy.getEnemy((inversed ? "inv_" : "") + "evil_mirror"));
					battle.battlePreFinishEvent = preFinish;
					battle.battleFinishEvent = finish;
				}
			});
		}
		else
		{
			MessageBox.addMessage("The crate can", "be opened..");
			MessageBox.addMessage("Open it?").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						MessageBox.addMessage("A red light", "glimmer in", "the dark...").setDoneEvent(new Event() {
							
							@Override
							public void run() {
								Battle battle = game.enterBossBattle(Enemy.getEnemy((inversed ? "inv_" : "") + "evil_mirror"));
								battle.battlePreFinishEvent = preFinish;
								battle.battleFinishEvent = finish;
							}
						});
					}
				}
			}, "Yes", "No");
		}
	}
	
	private PostEvent preFinish = new Battle.PostEvent()
	{
		public void onEnded(final Battle battle) {
			if(battle.victory == false)
				return;
			battle.dead = false;
			ItemPopup popup = new ItemPopup(Items.RED_PENDANT, 5, "You received", "the RED", "PENDANT!!!");
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
				TileEvent event = world.getEvent(44, 49);
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
		Tile.tiles[271].draw(batch, transform.getIntX(), transform.getIntY());
	}
	
}
