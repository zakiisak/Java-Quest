package game.events;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import game.Game;
import game.battle.Battle;
import game.battle.Enemy;
import game.battle.TurnComponent;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.Recipe.Integrant;
import game.entitycomponent.MovementComponent;
import game.entitycomponent.StepBattleComponent;
import game.entity.Player;
import game.entity.Recipes;
import game.entity.TileEvent;
import game.entity.Worlds;
import game.graphics.Sprite;
import game.tile.Tile;
import game.utils.Event;
import game.utils.Point;
import game.worlds.SpawnPoint;
import game.worlds.World;

public class SecretFinaleBossEvent extends TileEvent implements Serializable {
	
	public SecretFinaleBossEvent()
	{
		
	}
	
	@Override
	public void onStepped(Game game, World world, Player player) {
		MessageBox.addMessage("HWWHWHWWWHWHWWW", "hHWWWWWWHWWWw!!", "HWWHWWWWHWHWHWW").setDoneEvent(interactionInitiated);
	}
	
	@Override
	public boolean onInteraction(Game game, World world, Player player) {
		return false;
	}
	
	private Event interactionInitiated = new Event()  {
		
		@Override
		public void run() {
			Battle battle = Game.instance.enterCrazyBossBattle(Enemy.getEnemy("hvasefjaset"));
			battle.battleFinishEvent = battleFinished;
			battle.battlePreFinishEvent = battlePreFinished;
		}
	};
	
	private static void resetAll()
	{
		Player player = Game.instance.player;
		Worlds.resetWorlds();
		player.spawnPoint = new SpawnPoint(Player.startSpawnPoint);
		Worlds.transferPlayer(0, player.spawnPoint.x, player.spawnPoint.y, true);
		
		StepBattleComponent sbc = (StepBattleComponent) Game.instance.player.getComponent("step_encounter");
		if(sbc != null)
		{
			sbc.enabled = !sbc.enabled;
		}
		
		MovementComponent movement = (MovementComponent) player.getComponent("movement");
		if(movement != null)
		{
			movement.noclip = false;
		}
		
		//Resets teleportation
		List<Point> points = new ArrayList<Point>();
		points.add(new Point(25, 67));
		player.data.setObject("overworld" + "_points", points);
		
		List<Point> dw_points = new ArrayList<Point>();
		points.add(new Point(50, 49));
		player.data.setObject("dark_world" + "_points", dw_points);
		
		//Remove items
		player.deleteItem(Items.EARTH_GEM);
		player.deleteItem(Items.SAPPHIRE);
		player.deleteItem(Items.EMERALD);
		player.deleteItem(Items.RUBY);
		player.deleteItem(Items.AMETHYST);
		player.deleteItem(Items.DIAMOND);
		player.deleteItem(Items.TOPAZ);
		player.deleteItem(Items.BLUE_PENDANT);
		player.deleteItem(Items.GREEN_PENDANT);
		player.deleteItem(Items.RED_PENDANT);
		player.deleteItem(Items.ANCIENT_SCROLL);
		player.deleteItem(Items.MAGICAL_CAPE);
		player.deleteItem(Items.MAGIC_KEY);
		player.deleteItem(Items.SMITH_KEY);
		player.deleteItem(Items.ENCHANTED_SWORD);
		player.deleteItem(Items.CROWN);
		player.deleteItem(Items.CALMING_POTION);
//		player.deleteItem(Items.CURSED_RING);
//		player.deleteItem(Items.MONSTER_MANUAL);
//		player.deleteItem(Items.BOOK_OF_DAMAGE_NUMBERS);
//		player.deleteItem(Items.TELEPORT_UPGRADE);
//		player.deleteItem(Items.FREE_MOVEMENT_BOOK);
		player.deleteItem(Items.SPEAR_OF_CYLOPIUM);
//		player.deleteItem(Items.FASTBATTLE_BOOK);
//		player.deleteItem(Items.BATTLE_SPEED_UPGRADE);
		player.deleteItem(Items.GLOVES_OF_STRENGTH);
		player.deleteItem(Items.MISSING_NOTE);
//		player.deleteItem(Items.MAGIC_DICE);
		player.deleteItem(Items.MAGICAL_LEAF);
		player.deleteItem(Items.ROCK);
		player.deleteItem(Items.HERB);
		player.deleteItem(Items.HEALTH_POTION);
		player.deleteItem(Items.BOOK_OF_CONCEDE);
//		player.deleteItem(Items.BOOTS_OF_ESCAPING);
//		player.deleteItem(Items.RING_OF_TELEPORTATION);
//		player.deleteItem(Items.BOOK_OF_FULLSCREEN);
//		player.deleteItem(Items.FULLSCREEN_UPGRADE);
		player.deleteItem(Items.COPPER_ORE);
		player.deleteItem(Items.SILVER_ORE);
		player.deleteItem(Items.MALACHITE_ORE);
		player.deleteItem(Items.LEAD_ORE);
		player.deleteItem(Items.DAIZUM_ORE);
		player.deleteItem(Items.DARK_COMPOUND);
		player.deleteItem(Items.COPPER_INGOT);
		player.deleteItem(Items.SILVER_INGOT);
		player.deleteItem(Items.MALACHITE_INGOT);
		player.deleteItem(Items.LEAD_INGOT);
		player.deleteItem(Items.DAIZUM_INGOT);
		player.deleteItem(Items.DARK_INGOT);
		player.deleteItem(Items.MYSTERIOUS_KEY);
//		player.deleteItem(Items.BOOK_OF_ALL_KNOWING);
		player.deleteItem(Items.BOOK_OF_COLORS);
//		player.deleteItem(Items.OMNI_GEM);
		
		if(player.isInNewGamePlus() == false && player.data.getBoolean("boak_recipe_added") == false)
		{
			MessageBox.addMessage("New recipe", "added!");
			Recipes.add(player.recipes, Items.BOOK_OF_ALL_KNOWING,  new Integrant(Items.OMNI_GEM), new Integrant(Items.OMNI_GEM), new Integrant(Items.OMNI_GEM));
			player.data.setBoolean("boak_recipe_added", true);
		}
		if(player.data.getBoolean("op_recipe_added") == false)
		{
			if(MathUtils.random() < 0.25f)
			{
				MessageBox.addMessage("Strange", "Recipe added!");
				Recipes.add(player.recipes, Items.OMEGA_PICKAXE,  new Integrant(Items.OMNI_GEM), new Integrant(Items.OMNI_GEM), new Integrant(Items.OMNI_GEM));
				player.data.setBoolean("boak_recipe_added", true);
			}
		}
		
		
		
		Worlds.attachPlayerReferences(player);
		player.data.setObject("enemy_multiplier", player.getEnemyMultiplier().multiply(new BigInteger("1000000000")));
		System.out.println("New Multiplier: " + player.getEnemyMultiplier());
		Game.instance.saveGame();
		
		
	}
	
	private Battle.PostEvent battlePreFinished = new Battle.PostEvent()  {
		
		public void onEnded(final Battle battle) {
			if(!battle.victory) //Only do this code if the player was victorious
				return;
			battle.enemy.dead = true;
//			ItemPopup popup = new ItemPopup(Items.BOOK_OF_ALL_KNOWING, 5, "You received", "the BOOK OF", "ALL KNOWING!", "With this,", "you can press", " G or X", "to magically", "receive gold", "or xp!").setObliterationEvent(new Event() {
//				@Override
//				public void run() {
//					((TurnComponent)battle.getComponent("turn")).turn = -2;
//				}
//			});
//			ItemPopup popup = new ItemPopup(Items.OMNI_GEM, 1, "You received", "an OMNI GEM!").setObliterationEvent(new Event() {
//				@Override
//				public void run() {
//					((TurnComponent)battle.getComponent("turn")).turn = -2;
//				}
//			});
			MessageBox.addMessage("You defeated", "HVAESEFJAESET!!!", "");
			MessageBox.addMessage("Would you", "like to", "increase", "difficulty", "by making", "enemies 1B", "times stronger?", "Note most", "of your items", "will be removed,", "and you won't", "be able to", "come back", "to this", "difficulty.").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						resetAll();
						battle.dead = true;
					}
					else
					{
						((TurnComponent)battle.getComponent("turn")).turn = -2;
					}
				}
			}, "Yes", "No");
//			battle.game.addEntity(popup);
			battle.dead = false;
		}
	};
	
	private Battle.PostEvent battleFinished = new Battle.PostEvent()  {
		
		public void onEnded(Battle battle) {
			
		}
	};
	
	@Override
	public void drawPost(SpriteBatch batch) {
		super.draw(batch);
		Sprite.getSprite("hvasefjaset").render(batch, transform.x * Tile.SIZE + Tile.SIZE, transform.y * Tile.SIZE, -Tile.SIZE, Tile.SIZE);
	}
	
}
