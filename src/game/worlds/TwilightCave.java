package game.worlds;

import com.badlogic.gdx.Gdx;

import game.FileLoader;
import game.Game;
import game.entity.CameraMovement;
import game.entity.Player;
import game.entity.Tag;
import game.entity.Timer;
import game.entity.Worlds;
import game.events.MissningNoteLeverEvent;
import game.events.TransferEvent;
import game.events.TwiDragonEvent;
import game.events.TwiTreantEvent;
import game.events.TwilightLeverEvent;
import game.events.XiloberEvent;
import game.scene.SceneBattle;
import game.utils.Event;
import game.utils.Point;

public class TwilightCave extends World {

	public TwilightCave() {
		super(FileLoader.get("maps/twi_cave"));
		backgroundMusic = "cave";
	}
	
	private void validate()
	{
		final Player player = Game.instance.player;
		if(player.data.getBoolean("cm_twilight_cs_done")) return;
		Tag data = player.data;
		if(data.getBoolean("golem_defeated") && data.getBoolean("dragon_defeated"))
		{
			final CameraMovement cm = new CameraMovement(new Point(player.transform), new Point(49, 84), 0.1f);
			cm.setEndEvent(new Event() {
				@Override
				public void run() {
					cm.dead = false;
					World w = Worlds.getWorld(DarkWorld.class);
					w.setPassable(true, 49, 84);
					w.setPassable(true, 50, 84);
					w.setPassable(true, 49, 85);
					w.setPassable(true, 50, 85);
					w.foregroundTiles[49 + 84 * w.width] = 274;
					w.foregroundTiles[50 + 84 * w.width] = 274;
					w.foregroundTiles[49 + 85 * w.width] = 265;
					w.foregroundTiles[50 + 85 * w.width] = 265;
					Game.instance.addEntity(new Timer(1.5f, new Event() {
						
						@Override
						public void run() {
							cm.dead = true;
							player.data.setBoolean("cm_twilight_cs_done", true);
						}
					}, true));
					
				}
			});
			Game.instance.addEntity(cm);
		}
	}
	
	@Override
	public void initEvents() {
		//Wings
		putEvent(new TransferEvent(DarkWorld.class, 63, 82).setOnTransferredEvent(new Event() {
			@Override
			public void run() {
				validate();
			}
		}), 57, 97);
		putEvent(new TransferEvent(DarkWorld.class, 36, 82).setOnTransferredEvent(new Event() {
			@Override
			public void run() {
				validate();
			}
		}), 18, 97);
		
		//Main
		putEvent(new TransferEvent(DarkWorld.class, 50, 91), 37, 55);
		putEvent(new XiloberEvent(), 37, 3);
		
		putEvent(new TwilightLeverEvent("twilight_lever_west", "twilight_lever_east"), 30, 54);
		putEvent(new TwilightLeverEvent("twilight_lever_east", "twilight_lever_west"), 44, 54);
		
		putEvent(new MissningNoteLeverEvent("missing_note_lever_west", "missing_note_lever_east"), 21, 78);
		putEvent(new MissningNoteLeverEvent("missing_note_lever_east", "missing_note_lever_west"), 53, 75);
		
		//East Wing
		putEvent(new TwiTreantEvent(), 57, 77);
		
		//West Wing
		TwiDragonEvent dragon = new TwiDragonEvent();
		putEvent(dragon, 18, 76);
		putEvent(new TwiDragonEvent(dragon), 18, 78);
		putEvent(new TwiDragonEvent(dragon), 17, 78);
		putEvent(new TwiDragonEvent(dragon), 19, 78);
		
	}
	
	@Override
	public void onEntered() {
		super.onEntered();
		SceneBattle.battle_back = "cave_back";
	}
	
	@Override
	public void onLeft() {
		super.onLeft();
		SceneBattle.battle_back = "battle_back";
	}
	

}
