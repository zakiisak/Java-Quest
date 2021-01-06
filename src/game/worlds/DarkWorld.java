package game.worlds;

import com.badlogic.gdx.Gdx;

import game.FileLoader;
import game.Game;
import game.entity.Items;
import game.events.BlockerEvent;
import game.events.EvilMirrorBossEvent;
import game.events.EyeOfDecimationBossEvent;
import game.events.FinaleBossEvent;
import game.events.GhostSecondBrotherEvent;
import game.events.MissingNoteEvent;
import game.events.PendantSocketEvent;
import game.events.RainbowTransferEvent;
import game.events.SpawnPointEvent;
import game.events.TransferEvent;
import game.events.ViggoMortensenEvent;
import game.scene.SceneTeleport;

public class DarkWorld extends World {
	
	public DarkWorld() {
		super(FileLoader.get("maps/dark_world"));
		customShader = "inverted";
		backgroundMusic = "dark_world";
	}
	
	@Override
	public void initEvents() {
		putEvent(new RainbowTransferEvent(Overworld.class, 44, 51), 50, 49);
		putEvent(new GhostSecondBrotherEvent(), 72, 47);
		putEvent(new EvilMirrorBossEvent(), 93, 27);
		putEvent(new BlockerEvent(), 44, 49);
		putEvent(new BlockerEvent(), 50, 55);
		
		putEvent(new MissingNoteEvent(), 69, 46);
		
//		putEvent(new BlockerEvent(), 56, 49);

		//Region 2
		putEvent(new EyeOfDecimationBossEvent(), 13, 13);
		putEvent(new MissingNoteEvent(), 14, 9);
		
		//Region 3
		putEvent(new TransferEvent(TwilightCave.class, 18, 97), 36, 82);
		putEvent(new TransferEvent(TwilightCave.class, 57, 97), 63, 82);
		putEvent(new TransferEvent(TwilightCave.class, 37, 55), 50, 91);
		
		//Spawnpoints
		putEvent(new SpawnPointEvent(), 81, 46);
		putEvent(new SpawnPointEvent(), 52, 73);
		putEvent(new SpawnPointEvent(), 21, 49);
		
		
		//Pendants
		putEvent(new PendantSocketEvent(Items.GREEN_PENDANT, "GREEN PENDANT"), 49, 26);
		putEvent(new PendantSocketEvent(Items.RED_PENDANT, "RED PENDANT"), 51, 26);
		putEvent(new PendantSocketEvent(Items.BLUE_PENDANT, "BLUE PENDANT"), 50, 28);
		
		//After Pendants
		putEvent(new ViggoMortensenEvent(), 50, 15);
		putEvent(new FinaleBossEvent(), 50, 1);
	}
	
	@Override
	public void onEntered() {
		super.onEntered();
		//Start music
		SceneTeleport.worldMap = "dark_world";
	}
	
	@Override
	public void onLeft() {
		super.onLeft();
	}
	

}
