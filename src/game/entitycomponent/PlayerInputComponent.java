package game.entitycomponent;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.math.BigInteger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.Input;
import game.battle.Zones;
import game.entity.AreaText;
import game.entity.EntityComponent;
import game.entity.ItemPopup;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.NetPlayer;
import game.entity.Player;
import game.entity.TileEvent;
import game.scene.SceneBattle;
import game.scene.SceneTeleport;
import game.utils.Numbers;
import game.worlds.CaveOfDestiny;
import game.worlds.CaveOfMysteries;
import game.worlds.World;
import game.worlds.WorldOfInversement;

public class PlayerInputComponent extends EntityComponent {
	private World world;
	public MovementComponent movement;
	public StatDrawComponent statDraw;
	
	private int lastX, lastY;
	public boolean resetLastCoordinates = false;
	public int currentZone;
	
	public void cancelResetCoordinates()
	{
		resetLastCoordinates = false;
		this.lastY--;
	}
	
	public void setWorld(World world)
	{
		this.world = world;
		int tx = (int) (movement.transform.x + 0.5f);
		int ty = (int) (movement.transform.y + 0.5f);
		this.lastX = tx;
		this.lastY = ty;
		resetLastCoordinates = true;
	}
	
	public PlayerInputComponent(World world, MovementComponent movement, StatDrawComponent statDraw)
	{
		this.world = world;
		this.movement = movement;
		this.statDraw = statDraw;
		currentZone = world.getZone(movement.transform.getIntX(), movement.transform.getIntY());
	}
	
	@Override
	public String getName() {
		return "player_input";
	}

	@Override
	public void create() {
		
	}

	@Override
	public void tick() {
		if(ItemPopup.activePopups > 0 || MessageBox.current() != null || Game.instance.getSceneManager().getActiveScene() instanceof SceneBattle) return;
		int mx = 0, my = 0;
		float speed = Game.instance.player.data.getFloat("move_speed", 1);
		if(movement.free)
		{
			if(Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP))
			{
				my -= speed;
			}
			if(Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN))
			{
				my += speed;
			}
			if(Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT))
			{
				mx -= speed;
			}
			if(Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT))
			{
				mx += speed;
			}
		}
		else
		{
			if(Input.keyJustPressed(Keys.W) || Input.keyJustPressed(Keys.UP))
			{
				my -= speed;
			}
			if(Input.keyJustPressed(Keys.S) || Input.keyJustPressed(Keys.DOWN))
			{
				my += speed;
			}
			if(Input.keyJustPressed(Keys.A) || Input.keyJustPressed(Keys.LEFT))
			{
				mx -= speed;
			}
			if(Input.keyJustPressed(Keys.D) || Input.keyJustPressed(Keys.RIGHT))
			{
				mx += speed;
			}
			
		}
		if(Input.keyJustPressed(Keys.SPACE))
		{
			//Check for action
			//if no action, change the display information toggle xp draw
			TileEvent standingEvent = world.getEvent(movement.transform.getIntX(), movement.transform.getIntY());
			boolean interacted = false;
			if(standingEvent != null)
			{
				interacted = standingEvent.onInteraction(Game.instance, world, world.playerReference);
			}
			if(interacted == false)
			{
				NetPlayer standingPlayer = getStandingPlayer();
				if(standingPlayer != null)
				{
					standingPlayer.interacted(Game.instance.player);
				}
			}
		}
		if(Input.keyJustPressed(Keys.E) && Game.instance.player.possessItem(Items.CROWN))
		{
			StepBattleComponent sbc = (StepBattleComponent) Game.instance.player.getComponent("step_encounter");
			if(sbc != null)
			{
				sbc.enabled = !sbc.enabled;
				MessageBox.addMessage("Encounters have", "been " + (sbc.enabled ? "Enabled" : "Disabled"));
			}
		}
		if(Input.keyJustPressed(Keys.C))
		{
			statDraw.drawXp = !statDraw.drawXp;
			Game.instance.getAudio().getSound("switch").play(0.5f, 1.5f);
		}
		if(Input.keyJustPressed(Keys.M) && Game.instance.player.possessItem(Items.CURSED_RING))
		{
			Player player = Game.instance.player;
			Game.instance.enterBattle(world.getRandomEnemyInZone(player.transform.getIntX(), player.transform.getIntY()));
			EntityComponent stepEncounter = player.getComponent("step_encounter");
			if(stepEncounter != null)
			{
				((StepBattleComponent)stepEncounter).steps = 0;
			}
		}
		if(Input.keyJustPressed(Keys.F) && Game.instance.player.possessItem(Items.BOOK_OF_FULLSCREEN))
		{
			Dimension monitor = Toolkit.getDefaultToolkit().getScreenSize();
			boolean fullscreen = !Gdx.graphics.isFullscreen();
			int width = fullscreen ? monitor.width : (Game.instance.player.possessItem(Items.FULLSCREEN_UPGRADE) ? 640 : Game.RES_WIDTH);
			int height = fullscreen ? monitor.height : (Game.instance.player.possessItem(Items.FULLSCREEN_UPGRADE) ? 480 : Game.RES_HEIGHT);
			Gdx.graphics.setDisplayMode(width, height, fullscreen);
		}
		if(Input.keyJustPressed(Keys.T) && Game.instance.player.possessItem(Items.RING_OF_TELEPORTATION) && 
				world instanceof WorldOfInversement == false)
		{
			Game.instance.setScene(SceneTeleport.class);
		}
		if(Game.instance.player.possessItem(Items.BOOK_OF_ALL_KNOWING))
		{
			if(Gdx.input.isKeyPressed(Keys.G))
			{
				Game.instance.player.addGold(new BigInteger("1000000").max(Game.instance.player.getGold().divide(Numbers.TWO)));
				//Game.instance.player.stats.gold = Math.max(1000000, Game.instance.player.stats.gold / 2);
				if(Game.instance.player.getGold().compareTo(Numbers.ZERO) < 0 )
				{
					Game.instance.player.stats.gold = Numbers.ZERO;
					
				}
			}
			if(Gdx.input.isKeyPressed(Keys.X))
			{
				Game.instance.player.stats.xp = Game.instance.player.stats.xp.add(
						new BigInteger("1000000").max(Game.instance.player.stats.xp.multiply(Game.instance.player.stats.xp.divide(Numbers.TWO))));
			}
		}
		if(Game.instance.player.possessItem(Items.OMEGA_PICKAXE))
		{
			for(int i = 0; i < 6; i++)
			{
				if(Gdx.input.isKeyPressed(Keys.NUMPAD_1 + i))
				{
					int itemId = Items.COPPER_INGOT + i;
					Game.instance.player.addItem(itemId);
				}
			}
		}
		if(Game.instance.player.possessItem(Items.MAGICAL_CAPE) && Input.keyJustPressed(Keys.P))
		{
			if(world instanceof CaveOfMysteries == false && world instanceof CaveOfDestiny == false)
			{
				movement.noclip = !movement.noclip;
				MessageBox.addMessage("Noclip " + (movement.noclip ? "enabled" : "disabled") + "!");
				Game.instance.getAudio().getSound("teleport").play(1, 1.5f);
			}
		}
		boolean moved = movement.move(mx, my);
		int tx = (int) (movement.transform.x + 0.5f);
		int ty = (int) (movement.transform.y + 0.5f);
		if(resetLastCoordinates)
		{
			lastX = tx;
			lastY = ty;
			resetLastCoordinates = false;
		}
		if(moved)
		{
			int newZone = world.getZone(tx, ty);
			if(newZone != currentZone)
			{
				if(Zones.getZoneText(newZone).equals(Zones.getZoneText(currentZone)) == false && 
						Zones.getZoneLevelText(newZone).equals(Zones.getZoneLevelText(currentZone)) == false
						&& Zones.getZoneText(newZone).isEmpty() == false)
				{
					System.out.println("new zone!");
					AreaText areaText = new AreaText(Zones.getZoneText(newZone), Zones.getZoneLevelText(newZone));
					Game.instance.addEntity(areaText);
					currentZone = newZone;
				}
			}
			TileEvent eventWalkedTo = world.getEvent(tx, ty);
			if((tx != (int) lastX || ty != (int) lastY))
			{
				if(eventWalkedTo != null)
					eventWalkedTo.onStepped(Game.instance, world, world.playerReference);
				else
				{
					NetPlayer standingPlayer = getStandingPlayer();
					if(standingPlayer != null) standingPlayer.playerSteppedOn(Game.instance.player);
				}
						
			}
		}
		lastX = tx;
		lastY = ty;
	}

	@Override
	public void draw(SpriteBatch batch) {
		
	}

	
	/**
	 * @return the first found standing-on-player
	 */
	public NetPlayer getStandingPlayer()
	{
		int tx = (int) (movement.transform.x + 0.5f);
		int ty = (int) (movement.transform.y + 0.5f);
		for(int i = 0; i < Game.instance.client.clients.size(); i++)
		{
			NetPlayer player = Game.instance.client.clients.get(i);
			if((int)(player.transform.x + 0.5f) == tx &&
				(int) (player.transform.y + 0.5f) == ty)
			{
				return player;
			}
		}
		return null;
	}
	
	
}
