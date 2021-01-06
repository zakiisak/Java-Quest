package game.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.battle.Battle;
import game.battle.Enemy;
import game.battle.TurnComponent;
import game.entity.AreaText;
import game.entity.MessageBox;
import game.entity.Player;
import game.entity.TileEvent;
import game.entity.Worlds;
import game.graphics.Sprite;
import game.tile.Tile;
import game.utils.Event;
import game.worlds.DarkWorld;
import game.worlds.Overworld;
import game.worlds.World;

public class LastBossEvent extends TileEvent {
	
	public static boolean defeated = false;
	public boolean draw = false;
	
	public LastBossEvent() {
		defeated = false;
	}
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		draw = true;
		MessageBox.addMessage(new MessageBox("Huahaaua", "You have my", "thanks"));
		MessageBox.addMessage(new MessageBox("Because of", "you, the seal", "has been opened"));
		MessageBox.addMessage(new MessageBox("to the dark", "world.        ", "And now      "));
		MessageBox.addMessage(new MessageBox("Only you", "remain in", "my way    "));
		MessageBox.addMessage(new MessageBox("Then I can", "finally achieve", "what's"));
		MessageBox.addMessage(new MessageBox("rightfully mine                    ", "The power of", "Xilober              ").setDoneEvent(interactionInitiated));
	}
	
	private Event interactionInitiated = new Event()  {
		
		@Override
		public void run() {
			Battle battle = Game.instance.enterCrazyBossBattle(Enemy.getEnemy("shadow"));
			battle.battleFinishEvent = battleFinished;
			battle.battlePreFinishEvent = battlePreFinished;
		}
	};
	
	private Battle.PostEvent battlePreFinished = new Battle.PostEvent()  {
		
		public void onEnded(final Battle battle) {
			if(!battle.victory) //Only do this code if the player was victorious
				return;
			battle.enemy.dead = true;
			MessageBox.addMessage(new MessageBox("Nooo!!!", "at least I can", "draw you in!"));
			MessageBox.addMessage(new MessageBox("HUAUAUUHAU", "HAHAHAHAHAUHA", "HAHAHAHAAA....").setDoneEvent(new Event() {
				
				@Override
				public void run() {
					((TurnComponent)battle.getComponent("turn")).turn = -2;
				}
			}));
			battle.dead = false;
		}
	};
	
	private Battle.PostEvent battleFinished = new Battle.PostEvent()  {
		
		public void onEnded(Battle battle) {
			if(battle.victory)
			{
				defeated = true;
				dead = true;
				Worlds.getWorld(Overworld.class).putEvent(new RainbowTransferEvent(DarkWorld.class, 50, 49), 44, 51);
				Worlds.transferPlayer(DarkWorld.class, 50, 49);
				Game.instance.addEntity(new AreaText("Dark World", "Req lvl: " + 300).setNotCancelable());
				Game.instance.player.spawnPoint.set(50, 49, Worlds.getWorldId(DarkWorld.class));
			}
		}
	};
	
	@Override
	public void tick() {
		super.tick();
		if(defeated)
			dead = true;
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.drawPost(batch);
		if(draw)
			Sprite.getSprite("shadow").render(batch, 48.5f * Tile.SIZE - Tile.SIZE / 2, 43 * Tile.SIZE, Tile.SIZE * 2, Tile.SIZE * 2);
	}
	
}
