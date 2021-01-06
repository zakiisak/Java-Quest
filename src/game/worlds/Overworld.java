package game.worlds;

import com.badlogic.gdx.Gdx;

import game.FileLoader;
import game.events.ArenaEvent;
import game.events.BattleMerchant;
import game.events.BlackSmithHouseEvent;
import game.events.Boss1Event;
import game.events.Boss2Event;
import game.events.Boss3Event;
import game.events.Boss5Event;
import game.events.CaveNearStartEntranceEvent;
import game.events.CityEvent;
import game.events.ColorSellerEvent;
import game.events.CraftingEvent;
import game.events.FreeMovementEvent;
import game.events.FullscreenSellerEvent;
import game.events.GhostFirstBrotherEvent;
import game.events.InversementManEvent;
import game.events.ItemShowerEvent;
import game.events.MagicDiceSellerEvent;
import game.events.MallCopEvent;
import game.events.MissingNoteEvent;
import game.events.NameChangerEvent;
import game.events.NetEvent;
import game.events.PotionSellerEvent;
import game.events.RainbowTransferEvent;
import game.events.SellerEvent;
import game.events.SpawnPointEvent;
import game.events.TeleporterMerchant;
import game.events.TestEvent;
import game.events.TransferEvent;
import game.events.TreantEvent;
import game.scene.SceneTeleport;

public class Overworld extends World {

	public Overworld() {
		super(FileLoader.get("maps/world"));
		backgroundMusic = "overworld";
	}
	
	@Override
	public void initEvents() {
		//Region 1
		putEvent(new CaveNearStartEntranceEvent(), 47, 70);
		putEvent(new TransferEvent(CavePassage.class, 36, 176), 45, 70);
		putEvent(new TestEvent(), 27, 99 - 32);
		putEvent(new ColorSellerEvent(), 36, 68);
		putEvent(new Boss1Event(), 64, 50);
		
		putEvent(new FreeMovementEvent(), 42, 69);
		putEvent(new FullscreenSellerEvent(), 70, 80);
		putEvent(new NameChangerEvent(), 3, 73);
		
		
		//Region 2
		putEvent(new Boss2Event(), 53, 41);
		putEvent(new TreantEvent(), 86, 51);
		putEvent(new SpawnPointEvent(), 30, 68);
		putEvent(new SpawnPointEvent(), 54, 40);
		putEvent(new SpawnPointEvent(), 49, 39);
		putEvent(new SpawnPointEvent(), 31, 41);
		putEvent(new SpawnPointEvent(), 30, 26);
		putEvent(new SpawnPointEvent(), 56, 24);
		putEvent(new SpawnPointEvent().setVisible(), 95, 95);
		putEvent(new RainbowTransferEvent(CaveOfMysteries.class, 26, 43), 46, 68);
		
		putEvent(new InversementManEvent(), 98, 98);
		putEvent(new MagicDiceSellerEvent(), 83, 81);
		
		putEvent(new MissingNoteEvent(), 22, 30);
		
		putEvent(new BattleMerchant(), 38, 80);
		putEvent(new TeleporterMerchant(), 52, 43);
		putEvent(new ItemShowerEvent(), 35, 36);
		putEvent(new CraftingEvent(), 64, 32);
		
		
		//Net region
		putEvent(new ArenaEvent(), 72, 74);
		
		
		putEvent(new Boss3Event(), 31, 33);
		CityEvent cityEvent = new CityEvent();
		putEvent(cityEvent, 32, 40);
		putEvent(cityEvent, 33, 40);
		putEvent(cityEvent, 32, 41);
		putEvent(cityEvent, 33, 41);
		putEvent(new PotionSellerEvent(), 31, 40);
		putEvent(new SellerEvent(), 35, 42);
		putEvent(new GhostFirstBrotherEvent(), 26, 40);
		putEvent(new BlackSmithHouseEvent(), 34, 42);
		putEvent(new TransferEvent(DarkCave.class, 44, 99), 12, 21);
		putEvent(new NetEvent(), 25, 66);
		
		//Large Cave: 61 20
		putEvent(new TransferEvent(CaveOfDestiny.class, 39, 178), 61, 20);
		
		putEvent(new MallCopEvent(), 95, 30);
		putEvent(new Boss5Event(), 87, 6);
	}
	
	@Override
	public void onEntered() {
		super.onEntered();
		SceneTeleport.worldMap = "overworld";
	}
	
	@Override
	public void onLeft() {
		super.onLeft();
	}

}
