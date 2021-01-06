package game.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.battle.Battle;
import game.entitycomponent.GoldRemoverComponent;
import game.entitycomponent.MovementComponent;
import game.entitycomponent.NetComponent;
import game.entitycomponent.PlayerInputComponent;
import game.entitycomponent.SizeComponent;
import game.entitycomponent.SpriteComponent;
import game.entitycomponent.StatComponent;
import game.entitycomponent.StatDrawComponent;
import game.entitycomponent.StepBattleComponent;
import game.entitycomponent.TileZonePlacerComponent;
import game.entitycomponent.TransformComponent;
import game.graphics.Sprite;
import game.tile.Tile;
import game.utils.MathUtils;
import game.utils.Numbers;
import game.worlds.SpawnPoint;
import game.worlds.World;

public class Player extends Entity {
	
	public boolean tickable = true;
	public boolean visible = true;
//	public SpawnPoint spawnPoint = new SpawnPoint(25 + 40 - 20, 99 - 32 - 15 - 10, 0);
	public static SpawnPoint startSpawnPoint = new SpawnPoint(25, 99 - 32, 0); 
	public SpawnPoint spawnPoint = new SpawnPoint(startSpawnPoint);
//	public Point spawnPoint = new Point(25, 99 - 32);
	public boolean killed = false;
	public StatComponent stats;
	private StepBattleComponent stepEncounterComponent;
	public StatDrawComponent statDraw;
	public TransformComponent transform;
	public PlayerInputComponent input;
	public String name = System.getProperty("user.name");
	public BigInteger xpMemory = Numbers.ZERO;
	public long playTime;
	
	public Map<Integer, Long> possessedItems = new HashMap<Integer, Long>(); //new boolean[13 * 14];
	public List<Recipe> recipes = new ArrayList<Recipe>();
	
	public Player(Game game, World world)
	{
		transform = new TransformComponent();
		SizeComponent size = new SizeComponent();
		transform.set(spawnPoint.x, spawnPoint.y);
		size.set(Tile.SIZE, Tile.SIZE);
		
		SpriteComponent sprite = new SpriteComponent(Sprite.getSpritePart(Sprite.getSprite("roguelikecreatures"), 8, 9, 8, 6), transform, size);
		sprite.translationMultiplier = Tile.SIZE;
		MovementComponent movement = new MovementComponent(world, transform);
		stats = new StatComponent();
		stats.maxhp = Numbers.TEN;
		stats.hp = Numbers.TEN;
		stats.atk_min = Numbers.ONE;
		stats.atk_max = Numbers.TWO;
		stats.lvl = Numbers.ONE;
		stats.xplimit = Numbers.TEN;
//		stats.gold = 70050;
		//stats.xp = new BigInteger("10000000000000000000000000000000");//"575047745757389543258934223548923578923578923578923542352323578923549034");
//		stats.xp = 200;//15000 * 90 * 400;
//		stats.xp = 5700 * 80;
//		addItem(Items.SMITH_KEY);
		
		statDraw = new StatDrawComponent(stats);
		game.addEntity(statDraw);
		addComponent(transform);
		addComponent(size);
		addComponent(sprite);
		addComponent(movement);
		addComponent(input = new PlayerInputComponent(world, movement, statDraw));
		addComponent(stats);
		addComponent(stepEncounterComponent = new StepBattleComponent(world, input, transform, 15 * 2, 5));
		addComponent(new GoldRemoverComponent(stats));
		addComponent(new NetComponent(this));
//		addItem(Items.COPPER_INGOT, 100);
//		addItem(Items.HERB, 8);
//		addItem(Items.BOOK_OF_CONCEDE);
//		addItem(Items.MAGIC_KEY, 20);
//		addItem(Items.MISSING_NOTE, 3);
//		addItem(Items.SMITH_KEY);
//		addItem(Items.MAGICAL_CAPE);
//		addItem(Items.AMETHYST, 20);
//		addItem(Items.SAPPHIRE, 20);
//		addItem(Items.RUBY, 20);
//		addItem(Items.TOPAZ, 20);
//		addItem(Items.EMERALD, 20);
//		addItem(Items.EARTH_GEM, 20);
//		addItem(Items.BLUE_PENDANT, 20);
//		addItem(Items.GREEN_PENDANT, 20);
//		addItem(Items.RED_PENDANT, 40);
//		addItem(Items.MYSTERIOUS_KEY, 3);
//		addItem(Items.FULLSCREEN_UPGRADE);
//		addItem(Items.DIAMOND);
		
//		addItem(Items.ENCHANTED_SWORD);
//		addItem(Items.FASTBATTLE_BOOK);
//		addItem(Items.BATTLE_SPEED_UPGRADE);
//		addItem(Items.RING_OF_TELEPORTATION);
//		addItem(Items.CROWN);
		
//		addItem(Items.GREEN_PENDANT);
//		addItem(Items.RED_PENDANT);
//		addItem(Items.BLUE_PENDANT);
		
//		addItem(Items.HEALTH_POTION);
//		addItem(Items.BOOK_OF_CONCEDE);
//		addItem(Items.BOOTS_OF_ESCAPING);
//		addItem(Items.MONSTER_MANUAL);
//		stats.gold += 70000;
//		addItem(Items.BOOK_OF_ALL_KNOWING);
//		addItem(Items.MONSTER_MANUAL);
//		addItem(Items.BOOK_OF_DAMAGE_NUMBERS);
//		addItem(Items.BOOK_OF_FULLSCREEN);
//		addItem(Items.FULLSCREEN_UPGRADE);
		Recipes.addBaseRecipes(recipes);
		world.attachedCameraFollower = transform;
	}
	
	public void reattachWorldReferences(World world)
	{
		world.attachedCameraFollower = transform;
		((MovementComponent)getComponent("movement")).world = world;
		((PlayerInputComponent)getComponent("player_input")).setWorld(world);
		EntityComponent stepEncounter = getComponent("step_encounter");
		if(stepEncounter != null)
		{
			((StepBattleComponent) stepEncounter).setWorld(world);
		}
		EntityComponent editorComponent = getComponent("tile_zone_placer");
		if(editorComponent != null)
		{
			((TileZonePlacerComponent) editorComponent).world = world;
		}
		world.playerReference = this;
	}
	
	public void onBattleEntrance(Battle battle)
	{
		stats.gold = stats.gold.subtract(((GoldRemoverComponent) getComponent("gold_remover")).goldToGetRemoved);
		((GoldRemoverComponent) getComponent("gold_remover")).goldToGetRemoved = Numbers.ZERO;
		((NetComponent) getComponent("net")).setBattle();
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		if(visible == false)
			return; 
		super.draw(batch);
	}
	
	@Override
	public void drawPost(SpriteBatch batch) {
		if(visible == false)
			return;
		super.drawPost(batch);
	}
	
	private static long lastSecond = System.currentTimeMillis();
	
	public void updatePlayTime()
	{
		if(System.currentTimeMillis() - lastSecond >= 1000)
		{
			playTime += System.currentTimeMillis() - lastSecond;
			lastSecond = System.currentTimeMillis();
		}
	}
	
	@Override
	public void tick() {
		if(tickable == false)
			return;
		super.tick();
		for(int i = 0; i < 10000; i++)
		{
			if(checkLevelUp() == false)
				break;
			
		}
	}
	
	public void onWorldLoaded()
	{
		statDraw.dead = false;
		statDraw.drawHealth = false;
		Game.instance.addEntity(statDraw);
		reattachWorldReferences(Worlds.getActiveWorld());
	}
	
	public boolean canLevelUp()
	{
		return stats.xp.compareTo(stats.xplimit) >= 0;
	}
	
	/*
	public BigInteger getAmountOfLevelUps()
	{//
		BigInteger num = stats.xp.divide(stats.xplimit);
		
		//return num;
	}
	*/
	
	public boolean checkLevelUp()
	{
		if(canLevelUp())
		{
			levelUp();
			return true;
		}
		return false;
	}
	
	private void levelUp()
	{
		BigInteger levels = Numbers.FIVE.add(MathUtils.bigIntSqRootFloor(Numbers.FIVE.multiply(xpMemory.add(stats.xp).add(Numbers.ONE).multiply(Numbers.FOUR)).add(Numbers.FIVE))).divide(Numbers.TEN);
		BigInteger xp = levels.multiply(Numbers.TEN).subtract(Numbers.FIVE).pow(2).subtract(new BigInteger("25")).divide(new BigInteger("20"));
		BigInteger xpMinusOneLevel = stats.lvl.subtract(Numbers.ONE).multiply(Numbers.TEN).subtract(Numbers.FIVE).pow(2).subtract(new BigInteger("25")).divide(new BigInteger("20"));
		BigInteger diffLevels = levels.subtract(stats.lvl);
		System.out.println("levels: " + levels);
		System.out.println("xp: " + xp);
		System.out.println("diffLevels: " + diffLevels);
		System.out.println("stats.xp: " + stats.xp);
		System.out.println("stats.xplimit: " + stats.xplimit);
		System.out.println("stats.lvl: " + stats.lvl);
		stats.xp = stats.xp.add(xpMemory).subtract(xp);//stats.xp.subtract(xp.subtract(xpMemory));
		stats.maxhp = stats.maxhp.add(Numbers.TEN.multiply(diffLevels));
		if(stats.hp.compareTo(Numbers.ZERO) > 0) stats.hp = stats.hp.add(Numbers.TEN.multiply(diffLevels));
		stats.xplimit = stats.xplimit.add(Numbers.TEN.multiply(diffLevels));
		stats.lvl = levels;
		stats.atk_min = stats.atk_min.add(Numbers.TWO.multiply(diffLevels));
		stats.atk_max = stats.atk_max.add(Numbers.TWO.multiply(diffLevels));
		xpMemory = xp;
	}
	
	public void respawn()
	{
		Worlds.transferPlayer(spawnPoint.world, spawnPoint.x, spawnPoint.y);
		killed = false;
		stepEncounterComponent.steps = 0;
		stats.hp = stats.maxhp;
		Game.instance.player.visible = true;
		Game.instance.saveGame();
		Game game = Game.instance;
		if(game.getAudio().getMusic(game.world.backgroundMusic) == game.backgroundMusic)
		{
			game.backgroundMusic.resume();
		}
		else
		{
			game.startBackgroundMusic(game.world.backgroundMusic, 1);
		}
	}
	
	public boolean possessItem(int id)
	{
		Long count = possessedItems.get(id);
		if(count == null)
			return false;
		return (long) count > 0;
	}
	
	public void addItem(int id)
	{
		if(possessedItems.get(id) == null)
		{
			possessedItems.put(id, 1L);
		}
		else
			possessedItems.put(id, possessedItems.get(id) + 1);
	}
	
	public void addItem(int id, long count)
	{
		if(possessedItems.get(id) == null)
		{
			possessedItems.put(id, count);
		}
		else
			possessedItems.put(id, possessedItems.get(id) + count);
	}
	
	public void addItemWithMessage(int id, String... message)
	{
		addItem(id);
		MessageBox.addMessage(new MessageBox(message));
	}
	
	public void removeItem(int id)
	{
		if(possessedItems.get(id) == null)
			return;
		possessedItems.put(id, possessedItems.get(id) - 1);
		if(possessedItems.get(id) <= 0)
			possessedItems.put(id, 0L);
	}
	
	public void deleteItem(int id)
	{
		if(possessedItems.get(id) == null)
			return;
		possessedItems.put(id, 0L);
	}
	
	public void removeItem(int id, long count)
	{
		if(possessedItems.get(id) == null)
			return;
		possessedItems.put(id, possessedItems.get(id) - count);
		if(possessedItems.get(id) <= 0)
			possessedItems.put(id, 0L);
	}
	
	public void addGold(BigInteger gold)
	{
		stats.gold = stats.gold.add(gold);
	}
	
	/*
	public void obliterateItem(int id)
	{
		possessedItems.put(id, null);
	}
	*/
	
	public BigInteger getItemCount(int id)
	{
		if(id == Items.GOLD())
			return getGold();
		if(possessItem(id) == false)
			return Numbers.ZERO;
		return new BigInteger("" + possessedItems.get(id));
	}
	
	public BigInteger getGold()
	{
		return stats.gold.subtract(((GoldRemoverComponent) getComponent("gold_remover")).goldToGetRemoved);
	}
	
	public boolean canAfford(BigInteger goldAmount)
	{       
		return stats.gold.subtract(((GoldRemoverComponent) getComponent("gold_remover")).goldToGetRemoved).compareTo(goldAmount) >= 0;
	}
	
	public void removeGold(BigInteger amount)
	{
		((GoldRemoverComponent) getComponent("gold_remover")).goldToGetRemoved = 
				((GoldRemoverComponent) getComponent("gold_remover")).goldToGetRemoved.add(amount);
	}
	
	public void heal(BigInteger amount)
	{
		stats.hp = stats.hp.add(amount);
		if(stats.hp.compareTo(stats.maxhp) > 0)
			stats.hp = stats.maxhp;
	}
	
	public void restoreHp()
	{
		stats.hp = stats.maxhp;
	}
	
	public BigInteger getEnemyMultiplier()
	{
		Object multObj = Game.instance.player.data.getObject("enemy_multiplier"); 
		if(multObj != null && multObj instanceof BigInteger)
		{
			BigInteger multiplier = (BigInteger) multObj;
			return multiplier;
		}
		return Numbers.ONE;
	}
	
	public boolean isInNewGamePlus()
	{
		BigInteger multiplier = getEnemyMultiplier();
		return multiplier.compareTo(Numbers.ONE) > 0;
	}
	
}
