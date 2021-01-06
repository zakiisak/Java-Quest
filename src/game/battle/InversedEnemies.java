package game.battle;

import java.math.BigInteger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import game.FileLoader;
import game.entitycomponent.StatComponent;

public class InversedEnemies {
	
	public static void init()
	{
		FileHandle[] list = FileLoader.list("res/enemies/");
		for(int i = 0; i < list.length; i++)
		{
			FileHandle handle = list[i];
			String[] zones = Enemy.getZones(handle).split(",");
			String name = "inv_" + handle.nameWithoutExtension();
			for(int k = 0; k < zones.length; k++)
			{
				String zone = zones[k];
				if(zone.contains(";") == false)
				{
					int zoneNum = Integer.parseInt(zone);
					if(zoneNum > 0)
						Zones.addEnemy(19 + zoneNum, new Zones.DynamicEnemy(name));
				}
			}
		}
	}
	
	public static void multiplyStats(Enemy enemy, StatComponent stats)
	{
		BigInteger times = new BigInteger("1000000");
		stats.maxhp = stats.maxhp.multiply(times);
		stats.hp = stats.hp.multiply(times);
		stats.atk_min = stats.atk_min.multiply(times);
		stats.atk_max = stats.atk_max.multiply(times);
		stats.lvl = stats.lvl.multiply(times);
		stats.xp = stats.xp.multiply(times).pow(2);
		stats.xplimit = stats.xplimit.multiply(times);
		stats.gold = stats.gold.multiply(times);
	}
	
}
