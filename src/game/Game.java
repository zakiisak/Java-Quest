package game;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.battle.Battle;
import game.battle.Enemy;
import game.battle.InversedEnemies;
import game.battle.Zones;
import game.entity.Creatures;
import game.entity.Drops;
import game.entity.Entity;
import game.entity.Items;
import game.entity.MessageBox;
import game.entity.Player;
import game.entity.Worlds;
import game.graphics.Font;
import game.graphics.Shaders;
import game.graphics.SheetPacker;
import game.graphics.Sprite;
import game.main.CreatorWindow;
import game.net.NetAction;
import game.net.NetClient;
import game.net.NetServer;
import game.net.SendObject;
import game.scene.Scene;
import game.scene.SceneBattle;
import game.scene.SceneGame;
import game.scene.SceneManager;
import game.scene.SceneTitle;
import game.sound.AudioManager;
import game.sound.Music;
import game.sound.Sound;
import game.tile.Tile;
import game.worlds.Overworld;
import game.worlds.World;

public class Game implements ApplicationListener {
	public static final String GAME_VERSION = "1.0";
	public static int RES_WIDTH = 640;
	public static int RES_HEIGHT = 480;
	
	public static boolean CREATOR_WINDOW = false;
	
	public static Game instance;
	public static boolean debug = false;
	public List<NetAction> netActions = new ArrayList<NetAction>();
	public List<SendObject> netQueue = new ArrayList<SendObject>();
	
	public LwjglApplicationConfiguration config;
	private OrthographicCamera camera;
	private SceneManager sceneManager;
	private SpriteBatch batch;
	private AudioManager audioManager;
	private List<Entity> entities = new ArrayList<Entity>();
	public World world;
	public Player player;
	public static Font baseFont, dmgFont;
	public NetClient client = new NetClient();
	public NetServer server = new NetServer();
	public Battle battle;
	
	@Override
	public void create() {
		/*
		ObjectInputStream oos = FileUtils.beginReading(FileLoader.get("world orig.cgw"));
		try {
			int tempWidth = (int) oos.readObject();
			int tempHeight = (int) oos.readObject();
			short[] background_ids = (short[]) oos.readObject();
			byte[] background_sprites = (byte[]) oos.readObject();
			short[] foreground_ids = (short[]) oos.readObject();
			byte[] foreground_sprites = (byte[]) oos.readObject();
			Main.resizeAndSave(Gdx.files.local("world.cgw"), tempWidth, tempHeight, background_ids, background_sprites, foreground_ids, foreground_sprites, 400, 400);
			
			ObjectInputStream oos2 = FileUtils.beginReading(FileLoader.get("world_data orig.dat"));
			short[] zones = (short[]) oos2.readObject();
			FileUtils.endReading(oos2);
			
			short[] newZones = new short[400 * 400];
			Main.fill(zones, newZones, 100, 100, 400, 400, false);
			FileUtils.save(Gdx.files.local("world_data.dat"), newZones);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileUtils.endReading(oos);
		*/
		
		
		instance = this;
		Sprite.load();
		Gdx.input.setInputProcessor(new Input());
		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		Shaders.init(batch);
		audioManager = new AudioManager();
		audioManager.loadAll();
		sceneManager = new SceneManager(this);
		sceneManager.gameLoadAll();
		baseFont = new Font(FileLoader.get("res/courier"), "courier");
		dmgFont = new Font(FileLoader.get("res/damageFont"), "dmg");
		SheetPacker.pack();
		Sprite.setSpriteReferences();
		Tile.load();
		Zones.init();
		InversedEnemies.init();
		
		Items.init();
		Drops.init();
		Creatures.init();
		Worlds.init();
		sceneManager.gameLoadAllPost();
		//Worlds.transferPlayer(Overworld.class, 72, 75);
//		Worlds.transferPlayer(DarkWorld.class, 72, 75);
//		Worlds.transferPlayer(WorldOfInversement.class, 50, 75);
		//Worlds.transferPlayer(CaveOfDestiny.class, 50, 3);
//		player.data.setBoolean("golem_defeated", true);
//		player.data.setBoolean("dragon_defeated", true);
		sceneManager.setScene(SceneTitle.class);
		if(CREATOR_WINDOW)
		{
			//newGame();
			setScene(SceneGame.class);
			render();
			CreatorWindow.window = new CreatorWindow();
		}
		Sound.masterVolume = 1.0f;
		Music.masterVolume = 0.9f;
	}

	@Override
	public void resize(int width, int height) {
		if(player != null)
		{
			if(player.possessItem(Items.FULLSCREEN_UPGRADE))
			{
				RES_WIDTH  = width;
				RES_HEIGHT = height;
			}
		}
		camera.setToOrtho(true, RES_WIDTH, RES_HEIGHT);
		batch.setProjectionMatrix(camera.combined);
		sceneManager.resize(width, height);
	}
	
	@Override
	public void render() {
		Input.justPressedKeysThisFrame.clear();
		for(int i = 0; i < netActions.size(); i++)
		{
			NetAction action = netActions.get(i);
			action.run(this, client);
			netActions.remove(i);
			i--;
		}
		Input.inputStatistics();
		batch.begin();
		sceneManager.render();
		for(int i = 0; i < entities.size(); i++)
		{
			Entity entity = entities.get(i);
			entity.tick();
			if(entity.dead)
			{
				entities.remove(i);
				entity.onKilled();
				entity.added = false;
				i--;
			}
		}
		
		for(int i = 0; i < entities.size(); i++)
		{
			entities.get(i).draw(batch);
		}
		//FontBatch.flushAll(batch);
		for(int i = 0; i < entities.size(); i++)
		{
			entities.get(i).drawPost(batch);
		}
		/*
		if(player.possessItem(Items.MONSTER_MANUAL) == false)
			player.addItem(Items.MONSTER_MANUAL);
		if(player.possessItem(Items.BOOK_OF_COLORS) == false)
			player.addItem(Items.BOOK_OF_COLORS);
		if(player.possessItem(Items.BOOK_OF_DAMAGE_NUMBERS) == false)
			player.addItem(Items.BOOK_OF_DAMAGE_NUMBERS);
		*/
		MessageBox msgBox = MessageBox.current();
		if(msgBox != null)
		{
			msgBox.tick();
			msgBox.draw(batch);
			msgBox.drawPost(batch);
		}
		if(Input.keyJustPressed(Keys.F3))
		{
			debug = !debug;
		}
		if(Input.keyJustPressed(Keys.F12))
		{
			String input = JOptionPane.showInputDialog("Enter Code: ", Console.lastSelection);
			if(input != null)
			{
				if(input.replaceAll("", "").isEmpty() == false)
				{
					Console.eval(input);
				}
			}
		}
		/*
		if(Input.keyJustPressed(Keys.B))
		{
			deleteAllBattles();
			setScene(SceneGame.class);
		}*/
		
		if(player != null)
		{
			player.updatePlayTime();
			double playTime = (double) player.playTime / 1000d;
			double hours = playTime / 3600d;
			double minutes = (hours - (double) (long) hours) * 60d;
			double seconds = (minutes - (double) (long) minutes) * 60d;
			String result = "";
			if(hours > 0)
				result += (hours < 10 ? "0" : "") + (long) hours + ":";
			if(minutes > 0)
				result += (minutes < 10 ? "0" : "") + (long) minutes + ":";
			if(seconds > 0)
				result += (seconds < 10 ? "0" : "") + (long) seconds;
			Gdx.graphics.setTitle("Java Quest " + result);
		}
		/*
		if(Input.keyJustPressed(Keys.NUM_3))
		{
			long atk = player.stats.gold / 1000L;
			player.removeGold(atk * 1000L);
			player.stats.atk_min += atk;
			player.stats.atk_max += atk;
		}
		if(Input.keyJustPressed(Keys.NUM_4))
		{
			long hp = player.stats.gold / 400L;
			player.removeGold(hp * 400L);
			player.stats.maxhp += hp;
			player.stats.hp += hp;
		}
		*/
		/*
		int battleCount = 0;
		for(int i = 0; i < entities.size(); i++)
		{
			if(entities.get(i) instanceof Battle)
				battleCount++;
		}
		Gdx.graphics.setTitle("Battle Count: " + battleCount);
		*/
		/*
		if(Gdx.input.isKeyPressed(Keys.X))
			player.stats.xp += player.stats.xplimit;
		if(Gdx.input.isKeyPressed(Keys.U))
			Worlds.transferPlayer(DarkWorld.class, 50, 49);
		if(Gdx.input.isKeyJustPressed(Keys.H))
			player.stats.gold += player.stats.gold;
			*/
		batch.end();
		
		if(client.isConnected())
		{
			for(int i = 0; i < netQueue.size(); i++)
			{
				SendObject sendObj = netQueue.get(i);
				if(sendObj.tcp)
					client.getCommunication().sendTCP(sendObj.obj);
				else 
					client.getCommunication().sendUDP(sendObj.obj);
				netQueue.remove(i);
				i--;
			}
		}
	}

	@Override
	public void pause() {
		sceneManager.pause();
	}

	@Override
	public void resume() {
		sceneManager.resume();
	}

	@Override
	public void dispose() {
		sceneManager.disposeAll();
		SheetPacker.clearAtlasses();
		Shaders.dispose();
		batch.dispose();
		audioManager.dispose();
	}
	
	public AudioManager getAudio()
	{
		return audioManager;
	}
	
	public SpriteBatch getSpriteBatch()
	{
		return batch;
	}
	
	public void setScene(Class<? extends Scene> sceneId)
	{
		sceneManager.setScene(sceneId);
	}
	
	public SceneManager getSceneManager()
	{
		return sceneManager;
	}

	public void addEntity(Entity entity)
	{
		entities.add(entity);
		entity.added = true;
		entity.onAdded(this);
	}
	
	public Battle enterBattle(Enemy enemy)
	{
		if(this.battle != null)
		{
			if(this.battle.dead == false)
				return this.battle;
		}
		if(enemy == null) return null;
		sceneManager.setScene(SceneBattle.class);
		Battle battle;
		addEntity(battle = new Battle(this, enemy, false));
		player.onBattleEntrance(battle);
		return battle;
	}
	
	public Battle enterBossBattle(Enemy enemy)
	{
		if(this.battle != null)
		{
			if(this.battle.dead == false)
				return this.battle;
		}
		if(enemy == null) return null;
		sceneManager.setScene(SceneBattle.class);
		Battle battle;
		addEntity(battle = new Battle(this, enemy, true));
		player.onBattleEntrance(battle);
		return battle;
	}
	
	public Battle enterCrazyBossBattle(Enemy enemy)
	{
		if(this.battle != null)
		{
			if(this.battle.dead == false)
				return this.battle;
		}
		if(enemy == null) return null;
		sceneManager.setScene(SceneBattle.class);
		Battle battle;
		addEntity(battle = new Battle(this, enemy, true, "boss2"));
		player.onBattleEntrance(battle);
		return battle;
	}
	
	public Battle enterBossBattle(Enemy enemy, boolean force)
	{
		if(this.battle != null)
		{
			if(force)
			{
				this.battle.dead = true;
				if(battle.enemy.stats.hp.compareTo(BigInteger.ZERO) <= 0 && battle.victory == false)
					battle.victory();
				this.battle = null;
				killAllBattles();
			}
			else
			{
				if(this.battle.dead == false)
					return this.battle;
			}
		}
		if(enemy == null) return null;
		sceneManager.setScene(SceneBattle.class);
		Battle battle;
		addEntity(battle = new Battle(this, enemy, true));
		player.onBattleEntrance(battle);
		return battle;
	}
	
	public Battle enterCrazyBossBattle(Enemy enemy, boolean force)
	{
		if(this.battle != null)
		{
			if(force)
			{
				this.battle.dead = true;
				if(battle.enemy.stats.hp.compareTo(BigInteger.ZERO) <= 0 && battle.victory == false)
					battle.victory();
				this.battle = null;
				killAllBattles();
			}
			else
			{
				if(this.battle.dead == false)
					return this.battle;
			}
		}
		if(enemy == null) return null;
		sceneManager.setScene(SceneBattle.class);
		Battle battle;
		addEntity(battle = new Battle(this, enemy, true, "boss2"));
		player.onBattleEntrance(battle);
		return battle;
	}
	
	public Battle enterBattle(Battle battle, boolean force)
	{
		if(this.battle != null)
		{
			System.out.println("Battle is not null!");
			if(force)
			{
				this.battle.dead = true;
				if(this.battle.enemy.stats.hp.compareTo(BigInteger.ZERO) <= 0 && this.battle.victory == false)
					this.battle.victory();
				this.battle = null;
				killAllBattles();
			}
			else
			{
				if(this.battle.dead == false)
					return this.battle;
			}
		}
		else System.out.println("Battle is null!");
		sceneManager.setScene(SceneBattle.class);
		addEntity(battle);
		player.onBattleEntrance(battle);
		return battle;
	}
	
	public void deleteAllBattles()
	{
		for(int i = 0; i < entities.size(); i++) 
		{
			if(entities.get(i) instanceof Battle)
			{
				entities.remove(i);
				i--;
			}
		}
	}
	
	public void killAllBattles()
	{
		for(int i = 0; i < entities.size(); i++) 
		{
			if(entities.get(i) instanceof Battle)
				entities.get(i).dead = true;
		}
	}
	
	public void saveGame()
	{
		Worlds.save(Gdx.files.local("world.sav"));
		//world.save(Gdx.files.local("world.sav"));
	}
	
	public Music backgroundMusic = null;
	
	public void startBackgroundMusic(String musicName, float pitch)
	{
		if(audioManager.getMusic(musicName) == null) return;
		if(backgroundMusic == audioManager.getMusic(musicName)) return;
		audioManager.getMusic(musicName).setLooping(true);
		audioManager.getMusic(musicName).play(1, pitch);
		backgroundMusic = audioManager.getMusic(musicName);
	}
	
	public void stopBackgroundMusic()
	{
		backgroundMusic = null;
		for(Music m : audioManager.musics.values())
		{
			m.stop();
		}
	}
	
	public void sendUDP(Object obj)
	{
		netQueue.add(new SendObject(obj, false));
	}
	
	public void sendTCP(Object obj)
	{
		netQueue.add(new SendObject(obj, true));
	}
	
	public boolean worldSaveExists()
	{
		return Worlds.compatibleGameSave;
	}
	
	public void newGame() 
	{
		Worlds.resetWorlds();
		world = Worlds.getActiveWorld();
		player = new Player(this, world);
		world.playerReference = player;
		world.addEntity(player);
	}
}
