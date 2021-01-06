package game.worlds;

import com.badlogic.gdx.Gdx;

import game.FileLoader;
import game.events.Boss1Event;
import game.events.Boss2Event;
import game.events.Boss3Event;
import game.events.Boss4Event;
import game.events.Boss5Event;
import game.events.Boss6Event;
import game.events.DarkLordEvent;
import game.events.EvilMirrorBossEvent;
import game.events.EyeOfDecimationBossEvent;
import game.events.RainbowTransferEvent;
import game.events.XiloberEvent;

public class WorldOfInversement extends World {
	
	public WorldOfInversement() {
		super(FileLoader.get("maps/woi"));
		customShader = "inverted";
		backgroundMusic = "dark_world";
	}
	
	@Override
	public void initEvents() {
		putEvent(new RainbowTransferEvent(Overworld.class, 98, 98), 49, 88);
		putEvent(new Boss1Event(true), 34, 62);
		putEvent(new Boss2Event(true), 19, 51);
		putEvent(new Boss3Event(true), 47, 43);
		putEvent(new Boss4Event(true), 74, 42);
		putEvent(new Boss5Event(true), 81, 25);
		putEvent(new Boss6Event(true), 66, 15);
		putEvent(new EvilMirrorBossEvent(true), 52, 13);
		putEvent(new EyeOfDecimationBossEvent(true), 38, 13);
		putEvent(new EvilMirrorBossEvent(true), 52, 13);
		putEvent(new XiloberEvent(true), 9, 15);
		
		putEvent(new DarkLordEvent(), 9, 29);
		putEvent(new RainbowTransferEvent(Overworld.class, 98, 98), 9, 32);
		
		
		
	}
	
	@Override
	public void onEntered() {
		super.onEntered();
	}
	
	@Override
	public void onLeft() {
		super.onLeft();
	}
	

}
