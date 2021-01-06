package game.worlds;

import com.badlogic.gdx.Gdx;

import game.FileLoader;
import game.Game;
import game.entity.AreaText;
import game.entity.Items;
import game.events.ItemPayerEvent;
import game.events.MissingNoteEvent;
import game.events.MysteriousDogEvent;
import game.events.MysteriousKeyEvent;
import game.events.RolpineEvent;
import game.events.TransferEvent;
import game.scene.SceneBattle;

public class CaveOfMysteries extends World {

	public CaveOfMysteries() {
		super(FileLoader.get("maps/cave_of_mysteries"));
		backgroundMusic = "cave";
	}
	
	@Override
	public void initEvents() {
		putEvent(new TransferEvent(Overworld.class, 46, 68), 26, 43);
		
		putEvent(new MysteriousDogEvent(), 26, 42);
		
		putEvent(new ItemPayerEvent(Items.COPPER_INGOT, 100), 26, 33);
		putEvent(new ItemPayerEvent(Items.LEAD_INGOT, 600), 26, 30);
		putEvent(new ItemPayerEvent(Items.SILVER_INGOT, 25), 26, 27);
		putEvent(new ItemPayerEvent(Items.MALACHITE_INGOT, 75), 26, 24);
		putEvent(new ItemPayerEvent(Items.DAIZUM_INGOT, 30), 26, 21);
		putEvent(new ItemPayerEvent(Items.DARK_INGOT, 20), 26, 18);
		
		putEvent(new MysteriousKeyEvent(), 26, 6);
	}
	
	@Override
	public void onEntered() {
		super.onEntered();
		SceneBattle.battle_back = "cave_back";
		Game.instance.player.input.movement.noclip = false;
		Game.instance.addEntity(new AreaText("Cave of Mysteries", ""));
	}
	
	@Override
	public void onLeft() {
		super.onLeft();
		SceneBattle.battle_back = "battle_back";
	}
	

}
