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
import game.graphics.Sprite;
import game.tile.Tile;
import game.utils.Event;
import game.worlds.World;

public class ViggoMortensenEvent extends TileEvent {
	
	public boolean visible;
	
	@Override
	public void onStepped(Game game, World world, final Player player) {
		super.onStepped(game, world, player);
		visible = true;
		MessageBox.addMessage("Not so fast").setDoneEvent(new Event() {
			@Override
			public void run() {
				Battle battle = Game.instance.enterBossBattle(Enemy.getEnemy(player.possessItem(Items.ENCHANTED_SWORD) ? "viggo_mortensen" : "transparent_man"));
				battle.battleFinishEvent = battleFinished;
				battle.battlePreFinishEvent = battlePreFinished;
			}
		});
		
	}
	
	private Battle.PostEvent battlePreFinished = new Battle.PostEvent()  {

		public void onEnded(final Battle battle) {
			if (!battle.victory) // Only do this code if the player was
									// victorious
				return;
			battle.enemy.dead = true;
			ItemPopup popup = new ItemPopup(Items.CROWN, 5, "You received the", "CROWN!", "", "This item", "enables you to", "disable", "encounters by", "pressing E!");
			popup.obliterationEvent = new Event() {
				@Override
				public void run() {
					((TurnComponent) battle.getComponent("turn")).turn = -2;
				}
			};
			battle.game.addEntity(popup);
			battle.dead = false;
		}
	};

	private Battle.PostEvent battleFinished = new Battle.PostEvent() {

		public void onEnded(Battle battle) {
			if (battle.victory)
				dead = true;
		}
	};
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.drawPost(batch);
		if(visible)
			Sprite.getSprite("transparent_man").render(batch, transform.x * Tile.SIZE, transform.y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
	}
	
}
