package game.main;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.files.FileHandle;

import game.utils.FileUtils;

public class MonsterInverser {
	
	private static class Monster
	{
		public String name, hp, atk, gold, xp, zone, spriteField;
	}
	
	public static void main(String[] args)
	{
		/*
		String name = (String )stream.readObject();
		String hp = (String )stream.readObject();
		String atk = (String )stream.readObject();
		String gold = (String )stream.readObject();
		String xp = (String )stream.readObject();
		String zone = (String )stream.readObject();
		String spriteField = (String )stream.readObject();
		*/
		
		File[] list = new File("res/enemies/").listFiles();
		for(int i = 0; i < list.length; i++)
		{
			ObjectInputStream input = FileUtils.beginReading(new FileHandle(list[i]));
			try {
				String name = (String) input.readObject();
				String hp = (String) input.readObject();
				String atk = (String) input.readObject();
				String gold = (String) input.readObject();
				String xp = (String) input.readObject();
				String zone = (String) input.readObject();
				String spriteField = (String) input.readObject();
				List<Integer> zones = new ArrayList<Integer>();
				String[] zoneSections = zone.split(",");
				
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			FileUtils.endReading(input);
		}
		
	}
	
}
