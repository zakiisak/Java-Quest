package game.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import game.FileLoader;
import game.graphics.Sprite;
import game.utils.Rand;

public class Zones {
	
	public static Map<Integer, Zone> zones = new HashMap<Integer, Zone>();
	
	public static void init()
	{
		//addEnemy(0, new StaticEnemy(new Enemy(Sprite.getSprite("Warrier 2"), Sprite.getSprite("Warrier 2_overlay"), 20, 29, 31, 1000000, 2)));
	
		FileHandle[] handles = FileLoader.list("res/enemies/");
		
		for(FileHandle handle : handles)
		{
			String zones = Enemy.getZones(handle);
			String[] splitter = zones.split(",");
			for(int i = 0; i < splitter.length; i++)
			{
				String section = splitter[i];
				if(section.contains(";"))
				{
					String[] toSplitter = section.split(";");
					int from = Integer.parseInt(toSplitter[0]);
					int to = Integer.parseInt(toSplitter[1]);
					for(int k = from; k < to; k++)
					{
						addEnemy(k, new DynamicEnemy(handle.nameWithoutExtension()));
					}
				}
				else
				{
					addEnemy(Integer.parseInt(section), new DynamicEnemy(handle.nameWithoutExtension()));
				}
			}
		}
		setZoneText(1, "Fields", "1");
		setZoneText(2, "Wilderness", "2");
		setZoneText(3, "Crazy Wilderness", "7");
		setZoneText(4, "Centia", "17");
		setZoneText(5, "Strange Wilderness", "33");
		setZoneText(6, "Strange Wilderness", "33");
		setZoneText(7, "Dark Cave", "68");
		setZoneText(8, "Chilly Grounds", "95");
		setZoneText(9, "Cave of Destiny", "150");
		setZoneText(10, "Center Grounds", "300");
		setZoneText(11, "Eastia", "310");
		setZoneText(12, "Westia", "350-420");
		setZoneText(13, "Twilia", "550");
		setZoneText(-50, "Memorial of Ancients", ""); zones.get(-50).levelText = "";
		
		setZoneText(-900, "Secret Passage", ""); zones.get(-900).levelText = "";
		
		setZoneText(20, "World of Inversement", "???");
		setZoneText(21, "World of Inversement", "???");
		setZoneText(22, "World of Inversement", "???");
		setZoneText(23, "World of Inversement", "???");
		setZoneText(24, "World of Inversement", "???");
		setZoneText(25, "World of Inversement", "???");
		setZoneText(26, "World of Inversement", "???");
		setZoneText(27, "World of Inversement", "???");
		setZoneText(28, "World of Inversement", "???");
		setZoneText(29, "World of Inversement", "???");
		setZoneText(30, "World of Inversement", "???");
		setZoneText(31, "World of Inversement", "???");
		setZoneText(32, "World of Inversement", "???");
		setZoneText(33, "Finale Grounds", "");
		
		//Add other monsters
		FileHandle list = FileLoader.get("");
		
	}
	
	public static void setZoneText(int zone, String areaName, String level)
	{
		Zone z = zones.get(zone);
		if(z == null)
		{
			z = new Zone();
			zones.put(zone, z);
		}
		z.name = areaName;
		z.levelText += level;
	}
	
	public static void addMonstersToZone(int zone, EnemyEntry... enemies)
	{
		for(EnemyEntry enemy : enemies)
		{
			addEnemy(zone, enemy);
		}
	}
	
	public static void addEnemy(int zone, EnemyEntry enemy)
	{
		Zone z = zones.get(zone);
		if(z == null)
		{
			z = new Zone();
			z.entries = new ArrayList<EnemyEntry>();
			zones.put(zone, z);
		}
		z.entries.add(enemy);
	}
	
	public static List<EnemyEntry> getEnemiesInZone(int zone)
	{
		if(zones.get(zone) == null)
			return null;
		return zones.get(zone).entries;
	}
	
	public static String getZoneText(int zone)
	{
		if(zones.get(zone) == null) return "";
		return zones.get(zone).name;
	}
	
	public static String getZoneLevelText(int zone)
	{
		if(zones.get(zone) == null) return "Req lvl: ?";
		return zones.get(zone).levelText;
	}
	
	public static Enemy getRandomEnemyInZone(int zone)
	{
		List<EnemyEntry> enemies = getEnemiesInZone(zone);
		if(enemies == null) return null;
		if(enemies.isEmpty()) return null;
		return enemies.get(Rand.rng.nextInt(enemies.size())).getEnemy().cpy();
		
	}
	
	private static interface EnemyEntry
	{
		public Enemy getEnemy();
	}
	
	public static class StaticEnemy implements EnemyEntry
	{

		public Enemy enemy;
		
		public StaticEnemy(Enemy enemy) 
		{
			this.enemy = enemy;
		}
		
		@Override
		public Enemy getEnemy() {
			return enemy;
		}
		
	}
	
	public static class DynamicEnemy implements EnemyEntry
	{

		public String enemy;
		
		public DynamicEnemy(String enemy) 
		{
			this.enemy = enemy;
		}
		
		@Override
		public Enemy getEnemy() {
			return Enemy.getEnemy(enemy);
		}
		
	}
	
	public static class Zone
	{
		public String name = "";
		public String levelText = "Req lvl: ";
		public List<EnemyEntry> entries = new ArrayList<EnemyEntry>();
	}
	
}
