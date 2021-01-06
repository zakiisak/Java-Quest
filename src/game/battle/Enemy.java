package game.battle;

import static game.utils.Numbers.ZERO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import game.FileLoader;
import game.Game;
import game.entity.Entity;
import game.entitycomponent.CentifierComponent;
import game.entitycomponent.LabelComponent;
import game.entitycomponent.SizeComponent;
import game.entitycomponent.SpriteComponent;
import game.entitycomponent.SpriteOverlayComponent;
import game.entitycomponent.StatComponent;
import game.entitycomponent.TransformComponent;
import game.graphics.Sprite;
import game.utils.Color;
import game.utils.FileUtils;
import game.utils.Rand;

public class Enemy extends Entity {
	
	public StatComponent stats = new StatComponent();
	public SpriteOverlayComponent overlay;
	public String name = "Unnamed";
	private LabelComponent label;
	private Sprite sprite;
	
	public Enemy(String name)
	{
		this(getEnemy(name));
	}
	
	public Enemy(Enemy enemy)
	{
		this(((SpriteComponent) enemy.getComponent("sprite")).sprite, 
				enemy.overlay.sprite, enemy.stats.hp, enemy.stats.atk_min, enemy.stats.atk_max, enemy.stats.xp, enemy.stats.gold);
		this.name = enemy.name;
	}
	
	public Enemy(Sprite sprite, Sprite overlay, BigInteger hp, BigInteger atk_min, BigInteger atk_max, BigInteger xp, BigInteger gold)
	{
		this.sprite = sprite;
		TransformComponent transform = new TransformComponent();
		SizeComponent size = new SizeComponent();
		size.set(Math.max(128, (int) sprite.getWidth()), Math.max(128, (int) sprite.getHeight()));
		stats.maxhp = hp;
		stats.hp = hp;
		stats.atk_min = atk_min;
		stats.atk_max = atk_max;
		stats.xp = xp;
		stats.gold = gold;
		
		addComponent(transform);
		addComponent(size);
		addComponent(new CentifierComponent(transform, size).setOffset(0, 40));
		SpriteComponent spriteComponent;
		addComponent(spriteComponent = new SpriteComponent(sprite, transform, size));
		spriteComponent.outlineWidth = 1;
		if(overlay != null)
			addComponent(this.overlay = new SpriteOverlayComponent(transform, size, overlay));
		addComponent(stats);
		addComponent(label = new LabelComponent(new TransformComponent(Game.RES_WIDTH / 2, 30), name, Game.baseFont).setCentered());
		label.color = new Color(1.0f, 1.0f, 1.0f);
		label.scale = 3.0f;
		label.outlineWidth = 2;
	}
	
	public Enemy multiplySize(int scale)
	{
		((SizeComponent) getComponent("size")).set(Math.max(128 * scale, (int) sprite.getWidth()), Math.max(128 * scale, (int) sprite.getHeight()));
		return this;
	}
	
	public void setSprite(Sprite sprite)
	{
		this.sprite = sprite;
		((SpriteComponent) getComponent("sprite")).sprite = sprite;
	}
	
	public Sprite getSprite()
	{
		return sprite;
	}
	
	
	public void setName(String name)
	{
		this.name = name;
		label.text = name;
	}
	
	@Override
	public void tick() {
		label.transform.x = Game.RES_WIDTH / 2;
		super.tick();
	}
	
	public void showOverlay()
	{
		if(overlay == null)
			return;
		overlay.show();
	}
	
	public Enemy cpy()
	{
		return new Enemy(this);
	}
	
	public static Enemy getEnemy(String enemy)
	{
		return loadEnemy(FileLoader.get("res/enemies/" + enemy + ".enf"));
	}
	
	public static String getZones(FileHandle enemyFile)
	{
		ObjectInputStream stream = FileUtils.beginReading(enemyFile);
		try {
			String data_name = (String) stream.readObject();
			String hp = (String) stream.readObject();
			String atk = (String) stream.readObject();
			String gold = (String) stream.readObject();
			String xp = (String) stream.readObject();
			String zone = (String) stream.readObject();
			String data_sprite = (String) stream.readObject();
			return zone;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Enemy loadEnemy(FileHandle handle)
	{
		boolean inversed = false;
		if(handle.nameWithoutExtension().startsWith("inv_"))
		{
			inversed = true;
			handle = FileLoader.get("res/enemies/" + handle.name().substring(4));
		}
		ObjectInputStream stream = FileUtils.beginReading(handle);
		try {
			String data_name = (String) stream.readObject();
			String hp = (String) stream.readObject();
			String atk = (String) stream.readObject();
			String gold = (String) stream.readObject();
			String xp = (String) stream.readObject();
			String zone = (String) stream.readObject();
			String data_sprite = (String) stream.readObject();
			
			BigInteger data_hp = ZERO;
			if(hp.contains("-"))
			{
				String[] splitter = hp.split("-");
				BigInteger hp_min = new BigInteger(splitter[0]);
				data_hp = hp_min.add(Rand.next(new BigInteger(splitter[1]).subtract(hp_min)));
			}
			else data_hp = new BigInteger(hp);
			
			BigInteger data_atk_min = ZERO;
			BigInteger data_atk_max = ZERO;
			if(atk.contains("-"))
			{
				String[] splitter = atk.split("-");
				data_atk_min = new BigInteger(splitter[0]);
				data_atk_max = new BigInteger(splitter[1]);
			}
			else 
			{
				data_atk_min = new BigInteger(atk);
				data_atk_max = data_atk_min;
			}
			
			BigInteger data_gold = ZERO;
			if(gold.contains("-"))
			{
				String[] splitter = gold.split("-");
				BigInteger gold_min = new BigInteger(splitter[0]);
				data_gold = gold_min.add(Rand.next(new BigInteger(splitter[1]).subtract(gold_min)));
			}
			else data_gold = new BigInteger(gold);
			
			BigInteger data_xp = ZERO;
			if(xp.contains("-"))
			{
				String[] splitter = xp.split("-");
				BigInteger xp_min = new BigInteger(splitter[0]);
				data_xp = xp_min.add(Rand.next(new BigInteger(splitter[1]).subtract(xp_min)));
			}
			else data_xp = new BigInteger(xp);
			
			System.out.println("Name: " + data_name);
			Enemy enemy = new Enemy(Sprite.getSprite(data_sprite), Sprite.getSprite(data_sprite + "_overlay"), data_hp, data_atk_min, data_atk_max, data_xp, data_gold);
			if(inversed)
			{
				data_name = "Inversed " + data_name;
				InversedEnemies.multiplyStats(enemy, enemy.stats);
				enemy.setSprite(Sprite.getSprite("inv_" + enemy.sprite.nameId));
			}
			if(Game.instance.player != null)
			{
				Object multObj = Game.instance.player.data.getObject("enemy_multiplier"); 
				if(multObj != null && multObj instanceof BigInteger)
					multiplyStats(enemy.stats, (BigInteger) multObj);
			}
			System.out.println(enemy.stats);
			enemy.setName(data_name);
			return enemy;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void multiplyStats(StatComponent stats, BigInteger times)
	{
		stats.maxhp = stats.maxhp.multiply(times);
		stats.hp = stats.hp.multiply(times);
		stats.atk_min = stats.atk_min.multiply(times);
		stats.atk_max = stats.atk_max.multiply(times);
		stats.lvl = stats.lvl.multiply(times);
		stats.xp = stats.xp.multiply(times.pow(2));
		stats.xplimit = stats.xplimit.multiply(times);
		stats.gold = stats.gold.multiply(times);
	}
	
}
