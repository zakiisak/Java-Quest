package game.entity;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import game.FileLoader;
import game.Game;
import game.utils.FileUtils;
import game.worlds.CaveOfDestiny;
import game.worlds.CaveOfMysteries;
import game.worlds.CavePassage;
import game.worlds.DarkCave;
import game.worlds.DarkWorld;
import game.worlds.Overworld;
import game.worlds.TwilightCave;
import game.worlds.World;
import game.worlds.WorldData;
import game.worlds.WorldOfInversement;

public class Worlds {
	
	private static int activeWorld;
	public static World[] worlds;
	public static boolean compatibleGameSave;
	
	public static void init()
	{
		if(Gdx.files.local("world.sav").exists())
		{
			WorldData data = load(Gdx.files.local("world.sav"));
			compatibleGameSave = data != null;
			if(data != null)
			{
				activeWorld = data.activeWorld;
				worlds = data.worlds;
				Game.instance.world = worlds[activeWorld];
				return;
			}
		}
		resetWorlds();
	}
	
	public static void resetWorlds()
	{
		if(worlds != null)
		{
			for(int i = 0; i < worlds.length; i++)
			{
				worlds[i].dead = true;
			}
		}
		activeWorld = 0;
		worlds = new World[] {new Overworld(), new CavePassage(), new DarkCave(), new CaveOfDestiny(), new DarkWorld(), new TwilightCave(), new WorldOfInversement(), new CaveOfMysteries()};
		for(int i = 0; i < worlds.length; i++)
		{
			worlds[i].id = i;
			worlds[i].initEvents();
		}
	}
	
	public static void attachPlayerReferences(Player player)
	{
		for(int i = 0; i < worlds.length; i++)
			worlds[i].playerReference = player;
	}
	
	public static void transferPlayer(int world, int x, int y)
	{
		transferPlayer(world, x, y, false);
	}
	
	public static void transferPlayer(int world, int x, int y, boolean force)
	{
		Game game = Game.instance;
		game.player.transform.set(x, y);
		if(activeWorld == world && force == false)
		{
			game.player.reattachWorldReferences(game.world);	
			return;
		}
		game.world.onLeft();
		game.world.dead = true;
		game.world = worlds[world];
		game.world.dead = false;
		game.player.reattachWorldReferences(game.world);
		game.addEntity(game.world);
		if(!game.world.playerAdded)
			game.world.addEntity(game.player);
		activeWorld = world;
		game.world.onEntered();
	}
	
	public static void transferPlayer(Class<? extends World> worldClass, int x, int y)
	{
		for(int i = 0; i < worlds.length; i++)
		{
			if(worldClass.isInstance(worlds[i]))
			{
				transferPlayer(i, x, y);
				return;
			}
		}
	}
	
	public static World getActiveWorld()
	{
		return worlds[activeWorld];
	}
	
	public static World getWorld(Class<? extends World> worldClass)
	{
		int worldId = getWorldId(worldClass);
		if(worldId == -1)
			return null;
		return worlds[worldId];
	}
	
	public static int getWorldId(Class<? extends World> worldClass)
	{
		for(int i = 0; i < worlds.length; i++)
		{
			if(worldClass.isInstance(worlds[i]))
			{
				return i;
			}
		}
		return -1;
	}
	
	public static void save(FileHandle handle)
	{
		FileUtils.save(handle, activeWorld, worlds);
	}
	
	public static WorldData load(FileHandle handle)
	{
		ObjectInputStream ois = FileUtils.beginReading(handle);
		try {
			int activeWorld = (Integer) ois.readObject();
			World[] worlds = (World[]) ois.readObject();
			ois.close();
			for(int i = 0; i < worlds.length; i++)
			{
				World world = worlds[i];
				for(int j = 0; j < world.entities.size(); j++)
				{
					Entity e = world.entities.get(j);
					if(e instanceof MPEntity)
					{
						world.entities.remove(j);
						j--;
					}
				}
			}
			WorldData data = new WorldData();
			data.worlds = worlds;
			data.activeWorld = activeWorld;
			return data;
		} catch (Exception e ) {
			e.printStackTrace();
			MessageBox.addMessage("World save", "is corrupted!");
//			JOptionPane.showMessageDialog(null, "World Save is corrupted! To start over,\ndelete the world save file next to the executable,\n named: \"world.sav\"", "Save File Corrupted", JOptionPane.ERROR_MESSAGE, null);
			try {
				ois.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
//			System.exit(1);
		}
		return null;
	}
	
}
