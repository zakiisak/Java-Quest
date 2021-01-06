package game.worlds;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

import game.FileLoader;
import game.Game;
import game.battle.Enemy;
import game.battle.Zones;
import game.entity.Entity;
import game.entity.Items;
import game.entity.Player;
import game.entity.TileEvent;
import game.entitycomponent.SizeComponent;
import game.entitycomponent.TransformComponent;
import game.graphics.FontRenderer;
import game.graphics.Shaders;
import game.graphics.Sprite;
import game.tile.Tile;
import game.tile.Tile.TileSet;
import game.utils.Color;
import game.utils.FileUtils;

public class World extends Entity {
	
	public String path;
	public final List<Entity> entities = new ArrayList<Entity>();
	public final short[] backgroundTiles;
	public final short[] foregroundTiles;
	public final TileEvent[] events;
	public short[] zones;
	public boolean[] passables;
	public final int width, height;
	public Matrix4 transformMatrix = new Matrix4().idt();
	public boolean playerAdded = false;
	public String customShader = null;
	public int id;
	
	public Player playerReference;
	public TransformComponent attachedCameraFollower = new TransformComponent();
	public float cameraX, cameraY;
	public String backgroundMusic;
	
	private int waveMaxTick = 130;
	private int waveTick = 0;
	private int waveBlock = 53;
	private float waveAlpha = 0;
	
	public World(FileHandle handle)
	{
		this.path = handle.path();
		FileHandle worldTiles = FileLoader.get(handle.path() + ".cgw");
		FileHandle worldData = FileLoader.get(handle.path() + "_data.dat");
		int tempWidth = 0;
		int tempHeight = 0;
		short[] background_ids = null;
		byte[] background_sprites = null;
		short[] foreground_ids = null;
		byte[] foreground_sprites = null;
		try {
			ObjectInputStream oos = new ObjectInputStream(worldTiles.read());
			tempWidth = (int) oos.readObject();
			tempHeight = (int) oos.readObject();
			background_ids = (short[]) oos.readObject();
			background_sprites = (byte[]) oos.readObject();
			foreground_ids = (short[]) oos.readObject();
			foreground_sprites = (byte[]) oos.readObject();
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("[World]: Unable to load world! (World.java)");
		}
		width = tempWidth;
		height = tempHeight;
		backgroundTiles = new short[width * height];
		foregroundTiles = new short[width * height];
		events = new TileEvent[width * height];
		
		ObjectInputStream stream = FileUtils.beginReading(worldData);
		try {
			zones = (short[]) stream.readObject();
		} catch (Exception e) {
			zones = new short[width * height];
		}
		try {
			passables = (boolean[]) stream.readObject();
		} catch (Exception e) {
			passables = new boolean[width * height];
			for(int i = 0; i < passables.length; i++)
				passables[i] = true;
		}
		System.out.println("width: " + width);
		System.out.println("height: " + height);
		
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				short backId = background_ids[x + y * width];
				short foreId = foreground_ids[x + y * width];
				byte backSpr = background_sprites[x + y * width];
				byte foreSpr = foreground_sprites[x + y * width];
				if(backId > 4 && backId < 8)
				{
					TileSet set = Tile.tilesets[(backId - 5)];
					backId = (short) ((backId - 5) * 81 + backSpr + 1);
				}
				else if(backId >= 10 && backId <= 17)
				{
					/*int addition = 178;
					System.out.println("backSpr: " + backSpr);
					if(backId > 12) backId--;
					backId = (short) (addition + (backId - 10) * 81 + backSpr + 1);
					*/
					backId = Tile.getCaveID(backId, backSpr);
				}
				
				if(foreId > 4 && foreId < 8)
				{
					//foreId = (short) ((foreId -5) * 81 + foreSpr + 1);
					TileSet set = Tile.tilesets[(foreId - 5)];
					foreId = (short) ((foreId - 5) * 81 + foreSpr + 1);
				}
				else if(foreId >= 10 && foreId <= 17)
				{
					/*
					int addition = 178;
					if(backId > 12) addition -= (81 - 16);
					foreId = (short) (addition + (foreId - 10) * 81 + foreSpr + 1);
					*/
					foreId = Tile.getCaveID(foreId, foreSpr);
				}
				
				int y1 = height - 1 - y;
				backgroundTiles[x + y1 * width] = backId;
				foregroundTiles[x + y1 * width] = foreId;
			}
		}
		
	}
	
	public void onEntered()
	{
		if(backgroundMusic != null)
			Game.instance.startBackgroundMusic(backgroundMusic, 1);
	}
	
	public void onLeft()
	{
		Game.instance.stopBackgroundMusic();
	}
	
	public void initEvents()
	{
		
	}
	
	private int x1, y1, x2, y2;
	
	public void tick()
	{
		if(waveTick >= waveMaxTick)
		{
			waveTick = 0;
			if(waveBlock == 53)
				waveBlock = 4;
			else if(waveBlock == 4)
				waveBlock = 5;
			else if(waveBlock == 5)
				waveBlock = 53;
		}
		waveTick++;
		waveAlpha = (float) waveTick / (float) waveMaxTick;
		if(!playerReference.killed)
		{
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
		}
		TransformComponent transform = attachedCameraFollower;//(TransformComponent) playerReference.getComponent("transform");
		SizeComponent size = (SizeComponent) playerReference.getComponent("size");
		cameraX = (int) (transform.x * Tile.SIZE - (Game.RES_WIDTH / 2 - size.width / 2));
		cameraY = (int) (transform.y * Tile.SIZE - (Game.RES_HEIGHT/ 2 - size.height));
		x1 = (int) (cameraX / Tile.SIZE) - 1;
		y1 = (int) (cameraY / Tile.SIZE) - 1;
		x2 = x1 + (int) Math.ceil((float)Game.RES_WIDTH / (float)Tile.SIZE) + 2;
		y2 = y1 + (int) Math.ceil((float)Game.RES_HEIGHT / (float) Tile.SIZE) + 2;
		
		for(int x = x1; x < x2; x++)
		{
			for(int y = y1; y < y2; y++)
			{
				if(x < 0 || y < 0 || x >= width || y >= height)
					continue;
				TileEvent event = getEvent(x, y);
				if(event != null)
				{
					event.tick();
					if(event.dead)
						events[x + y * width] = null;
				}
			}
		}
	}
	
	public void draw(SpriteBatch batch)
	{
		transformMatrix.set(-cameraX, -cameraY, 0, 0, 0, 0, 0, 1, 1, 1);
		batch.setTransformMatrix(transformMatrix);
		ShaderProgram defShader = batch.getShader();
		String customShader = this.customShader == null ? null : new String(this.customShader);
		if(customShader != null) 
		{
			if(Game.instance.player.possessItem(Items.BOOK_OF_COLORS) == false)
				customShader = "gray";
			batch.setShader(Shaders.get(customShader));
		}
		for(int x = x1; x < x2; x++)
		{
			for(int y = y1; y < y2; y++)
			{
				if(x < 0 || y < 0 || x >= width || y >= height)
				{
					short tile= getBackgroundTile(0, 0);
					if(tile == 53)
						drawWave(batch, x, y);
					else
						Tile.tiles[tile].draw(batch, x, y);
					continue;
				}
				short id = getBackgroundTile(x, y);
				if(id != 0)
				{
					if(id == 53)
					{
						drawWave(batch, x, y);
					}
					else
						Tile.tiles[id].draw(batch, x, y);
					
				}
			}
		}
		if(customShader != null) batch.setShader(defShader);
		for(int x = x1; x < x2; x++)
		{
			for(int y = y1; y < y2; y++)
			{
				if(x < 0 || y < 0 || x >= width || y >= height)
					continue;
				TileEvent event = getEvent(x, y);
				if(event != null)
					event.draw(batch);
			}
		}
		if(customShader != null) batch.setShader(Shaders.get(customShader));
		for(int x = x1; x < x2; x++)
		{
			for(int y = y1; y < y2; y++)
			{
				if(x < 0 || y < 0 || x >= width || y >= height)
					continue;
				short id = getForegroundTile(x, y);
				if(Tile.tiles[id] != null)
				{
					if(id == 53)
					{
						drawWave(batch, x, y);
					}
					else
						Tile.tiles[id].draw(batch, x, y);
				}
			}
		}
		if(customShader != null) batch.setShader(defShader);
		for(int i = 0; i < entities.size(); i++)
		{
			entities.get(i).draw(batch);
		}
		
		for(int x = x1; x < x2; x++)
		{
			for(int y = y1; y < y2; y++)
			{
				if(x < 0 || y < 0 || x >= width || y >= height)
					continue;
				TileEvent event = getEvent(x, y);
				if(event != null)
					event.drawPost(batch);
			}
		}
		batch.end();
		batch.begin();
		transformMatrix.setTranslation(0, 0, 0);
		batch.setTransformMatrix(transformMatrix);
		for(int i = 0; i < entities.size(); i++)
		{
			entities.get(i).drawPost(batch);
		}
	}
	
	private void drawWave(SpriteBatch batch, int x, int y)
	{
		int waveBlock = this.waveBlock;
		int thisWaveBlock = this.waveBlock;
		int i = x + y;
		if(i % 2 != 0)
		{
			if(thisWaveBlock == 53)
				thisWaveBlock = 4;
			else if(thisWaveBlock == 4)
				thisWaveBlock = 5;
			else if(thisWaveBlock == 5)
				thisWaveBlock = 53;
		}
		for(int k = 0; k < 1 + (i % 2); k++)
		{
			if(waveBlock == 53)
				waveBlock = 4;
			else if(waveBlock == 4)
				waveBlock = 5;
			else if(waveBlock == 5)
				waveBlock = 53;
		}
		batch.setColor(1, 1, 1, 1.0f);
		Tile.tiles[thisWaveBlock].draw(batch, x, y);
		batch.setColor(1, 1, 1, waveAlpha);
		Tile.tiles[waveBlock].draw(batch, x, y);
		batch.setColor(1, 1, 1, 1);
	}
	
	public void drawZoneOverlay(SpriteBatch batch)
	{
		transformMatrix.set(-cameraX, -cameraY, 0, 0, 0, 0, 0, 1, 1, 1);
		batch.setTransformMatrix(transformMatrix);
		Color color = new Color();
		final int SIZE = Tile.SIZE;
		for(int x = x1; x < x2; x++)
		{
			for(int y = y1; y < y2; y++)
			{
				if(x < 0 || y < 0 || x >= width || y >= height)
					continue;
				short zone = getZone(x, y);
				color.set(Color.getSineRainbow((int) zone * 10));
				batch.setColor(color.r, color.g, color.b, 0.4f);
				Sprite.white.render(batch, x * SIZE, y * SIZE, SIZE, SIZE);
				batch.setColor(1, 1, 1, 1);
				FontRenderer.draw(batch, Game.baseFont, "" + zone, x * SIZE + 16 - Game.baseFont.getWidth("" + zone) / 2, y * SIZE + 16 - Game.baseFont.lineHeight / 2);
			}
		}
		batch.end();
		batch.begin();
		transformMatrix.setTranslation(0, 0, 0);
		batch.setTransformMatrix(transformMatrix);
	}
	
	public void drawSolids(SpriteBatch batch)
	{
		transformMatrix.set(-cameraX, -cameraY, 0, 0, 0, 0, 0, 1, 1, 1);
		batch.setTransformMatrix(transformMatrix);
		final int SIZE = Tile.SIZE;
		for(int x = x1; x < x2; x++)
		{
			for(int y = y1; y < y2; y++)
			{
				if(x < 0 || y < 0 || x >= width || y >= height)
					continue;
				boolean passable = passables[x + y * width];
				if(passable)
					batch.setColor(0, 1, 0, 0.6f);
				else batch.setColor(1, 0, 0, 0.6f);
				Sprite.white.render(batch, x * SIZE, y * SIZE, SIZE, SIZE);
			}
		}
		batch.setColor(1, 1, 1, 1);
		batch.end();
		batch.begin();
		transformMatrix.setTranslation(0, 0, 0);
		batch.setTransformMatrix(transformMatrix);
	}
	
	public void addEntity(Entity entity)
	{
		if(entity instanceof Player)
		{
			playerAdded = true;
		}
		entities.add(entity);
		entity.added = true;
		entity.onAdded(this);
	}
	
	public void putEvent(TileEvent event, int x, int y)
	{
		event.transform.set(x, y);
		event.world = this;
		events[x + y * width] = event;
		event.onAdded(this);
	}
	
	public void setZone(short zone, int x, int y)
	{
		if(x < 0 || y < 0 || x >= width || y >= height) return;
		zones[x + y * width] = zone;
	}
	
	public void setPassable(boolean passable, int x, int y)
	{
		if(x < 0 || y < 0 || x >= width || y >= height) return;
		passables[x + y * width] = passable;
	}
	
	public short getForegroundTile(int x, int y)
	{
		if(x < 0 || y < 0 || x >= width || y >= height) return 0;
		return foregroundTiles[x + y * width];
	}
	
	public short getBackgroundTile(int x, int y)
	{
		if(x < 0 || y < 0 || x >= width || y >= height) return 0;
		return backgroundTiles[x + y * width];
	}
	
	public TileEvent getEvent(int x, int y)
	{
		if(x < 0 || y < 0 || x >= width || y >= height) return null;
		TileEvent event = events[x + y * width];
		if(event != null)
		{
			if(event.dead)
				return null;
		}
		return event;
	}
	
	public short getZone(int x, int y)
	{
		if(x < 0 || y < 0 || x >= width || y >= height) return -1;
		return zones[x + y * width];
	}
	
	public boolean isEventPassable(int x, int y)
	{
		if(getEvent(x, y) == null)
			return true;
		return getEvent(x, y).isPassable();
	}
	
	public boolean isPassable(int x, int y)
	{
		if(x < 0 || y < 0 || x >= width || y >= height) return false;
		return passables[x + y * width];
		/*
		return Tile.tiles[getForegroundTile(x, y)].isPassable(true) && 
				Tile.tiles[getBackgroundTile(x, y)].isPassable(false) &&
				isEventPassable(x, y);*/
		/*
		boolean foregroundPassable = Tile.tiles[getForegroundTile(x, y)].isPassable();
		if(foregroundPassable)
		{
			return Tile.tiles[getBackgroundTile(x, y)].isPassable();
		}
		return foregroundPassable;
		*/
	}
	
	public Enemy getRandomEnemyInZone(int x, int y)
	{
		short zone = getZone(x, y);
		Enemy enemy = Zones.getRandomEnemyInZone(zone);
		return enemy;//new Enemy(Sprite.getSprite("Warrier 2"), Sprite.getSprite("Warrier 2_overlay"), 10000, 29, 1000000, 2);
	}
	
	public void save(FileHandle handle)
	{
		FileUtils.save(handle, this);
	}
	
	public ShaderProgram getShader()
	{
		return customShader == null ? Shaders.defaultShader : Shaders.get(customShader);
	}
	
	
	
}
