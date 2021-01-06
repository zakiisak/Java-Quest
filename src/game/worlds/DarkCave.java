package game.worlds;

import game.FileLoader;
import game.events.Boss4Event;
import game.events.TransferEvent;
import game.scene.SceneBattle;

public class DarkCave extends World {

	public DarkCave() {
		super(FileLoader.get("maps/dark_cave"));
		backgroundMusic = "cave";
	}
	
	@Override
	public void initEvents() {
		putEvent(new TransferEvent(Overworld.class, 12, 21), 44, 99);
		
		Boss4Event boss = new Boss4Event();
		putEvent(boss, 45, 46);
		putEvent(new Boss4Event(boss), 45, 48);
		putEvent(new Boss4Event(boss), 46, 48);
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
