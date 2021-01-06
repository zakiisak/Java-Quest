package game.events;

import java.io.Serializable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.battle.Battle;
import game.battle.Zones;
import game.entity.MessageBox;
import game.entity.Player;
import game.entity.TileEvent;
import game.tile.Tile;
import game.utils.Event;
import game.worlds.World;

public class TestEvent extends TileEvent implements Serializable {
	
	public TestEvent()
	{
		
	}
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
//		MessageBox.addMessage(new MessageBox("chug hug mug ", "lug nug", " bug wug"));
//		MessageBox.addMessage(new MessageBox("chug hug mug ", "lug nug", " bug wug"));
		//Battle battle = game.enterBattle(world.getRandomEnemyInZone((int) player.transform.x, (int) player.transform.y));
		Battle battle = game.enterBattle(Zones.getRandomEnemyInZone(1));
		battle.battleFinishEvent = battleFinished;
		return true;
	}
	
	private Battle.PostEvent battleFinished = new Battle.PostEvent()  {
		
		@Override
		public void onEnded(Battle battle) {
			dead = true;
		}
	};
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		Tile.tiles[44].draw(batch, transform.getIntX(), transform.getIntY());
	}
	
}
