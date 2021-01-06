package game.worlds;

import com.badlogic.gdx.Gdx;

import game.FileLoader;
import game.Game;
import game.entity.AreaText;
import game.events.MissingNoteEvent;
import game.events.RolpineEvent;
import game.events.TransferEvent;
import game.scene.SceneBattle;

public class CavePassage extends World {

	public CavePassage() {
		super(FileLoader.get("maps/cave_passage"));
		backgroundMusic = "cave";
	}
	
	@Override
	public void initEvents() {
		System.out.println("Initialized events");
		putEvent(new TransferEvent(Overworld.class, 47, 70), 97, 173);
		putEvent(new TransferEvent(Overworld.class, 45, 70), 36, 176);
		putEvent(new RolpineEvent(), 66, 134);
		putEvent(new MissingNoteEvent(), 34, 160);
	}
	
	@Override
	public void onEntered() {
		super.onEntered();
		SceneBattle.battle_back = "cave_back";
		Game.instance.addEntity(new AreaText("Cave Passage", "Req lvl: " + 12));
	}
	
	@Override
	public void onLeft() {
		super.onLeft();
		SceneBattle.battle_back = "battle_back";
	}
	

}
