package game.worlds;

import com.badlogic.gdx.Gdx;

import game.FileLoader;
import game.Game;
import game.entity.Items;
import game.entity.Player;
import game.events.Boss4Event;
import game.events.Boss6Event;
import game.events.GemSocketEvent;
import game.events.LastBossEvent;
import game.events.MysteriousLockEvent;
import game.events.SecretFinaleBossEvent;
import game.events.SecretSocketEvent;
import game.events.TransferEvent;
import game.scene.SceneBattle;

public class CaveOfDestiny extends World {

	public CaveOfDestiny() {
		super(FileLoader.get("maps/cave_of_destiny"));
		backgroundTiles[0] = 312;
		
		for(int i = 0; i < zones.length; i++)
		{
			zones[i] = 9;
		}
		backgroundMusic = "cave";
	}
	
	@Override
	public void initEvents() {
		putEvent(new TransferEvent(Overworld.class, 61, 20), 39, 178);
		putEvent(new TransferEvent(49, 134)
		{
			@Override
			public void onStepped(Game game, World world, Player player) {
				super.onStepped(game, world, player);
				if(game.backgroundMusic != null)
				{
					if(game.backgroundMusic != game.getAudio().getMusic("cave"))
					{
						game.stopBackgroundMusic();
						game.startBackgroundMusic("cave", 1);
					}
				}
				else game.startBackgroundMusic("cave", 1);
			}
		}, 45, 94);
		
		putEvent(new Boss6Event(), 49, 148);
		putEvent(new GemSocketEvent(Items.EARTH_GEM, "EARTH GEM"), 49, 142);
		putEvent(new GemSocketEvent(Items.EMERALD, "EMERALD"), 51, 141);
		putEvent(new GemSocketEvent(Items.AMETHYST, "AMETHYST"), 51, 139);
		putEvent(new GemSocketEvent(Items.RUBY, "RUBY"), 49, 138);
		putEvent(new GemSocketEvent(Items.SAPPHIRE, "SAPPHIRE"), 47, 139);
		putEvent(new GemSocketEvent(Items.TOPAZ, "TOPAZ"), 47, 141);
		putEvent(new LastBossEvent(), 48, 45);
		putEvent(new LastBossEvent(), 49, 45);
		
		
		putEvent(new SecretSocketEvent(Items.EARTH_GEM, "S_EARTH GEM"), 49, 24);
		putEvent(new SecretSocketEvent(Items.EMERALD, "S_EMERALD"), 51, 23);
		putEvent(new SecretSocketEvent(Items.AMETHYST, "S_AMETHYST"), 51, 21);
		putEvent(new SecretSocketEvent(Items.RUBY, "S_RUBY"), 49, 20);
		putEvent(new SecretSocketEvent(Items.SAPPHIRE, "S_SAPPHIRE"), 47, 21);
		putEvent(new SecretSocketEvent(Items.TOPAZ, "S_TOPAZ"), 47, 23);
		putEvent(new SecretSocketEvent(Items.DIAMOND, "S_DIAMOND"), 49, 22);
		
		putEvent(new SecretSocketEvent(Items.RED_PENDANT, "S_RED"), 50, 21);
		putEvent(new SecretSocketEvent(Items.GREEN_PENDANT, "S_GREEN"), 48, 21);
		putEvent(new SecretSocketEvent(Items.BLUE_PENDANT, "S_BLUE"), 49, 23);
		
		putEvent(new MysteriousLockEvent(), 49, 6);
		
		
		putEvent(new SecretFinaleBossEvent(), 49, 2);
	}
	
	@Override
	public void onEntered() {
		super.onEntered();
		SceneBattle.battle_back = "cave_back";
		Game.instance.player.input.movement.noclip = false;
	}
	
	@Override
	public void onLeft() {
		super.onLeft();
		SceneBattle.battle_back = "battle_back";
	}
	

}
