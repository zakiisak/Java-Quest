package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FileLoader {
	
	public static FileHandle get(String path)
	{
		return Gdx.files.internal(path);
	}
	
	public static FileHandle[] list(String dir)
	{
		String containerPath = get(dir).pathWithoutExtension() + "_files.txt";
		FileHandle listContainer = get(containerPath);
		
		if(listContainer.exists())
		{
			String[] fileNames = listContainer.readString().split("\n");
			FileHandle[] handles = new FileHandle[fileNames.length];
			for(int i = 0; i < handles.length; i++)
			{
				handles[i] = get(dir + "/" + fileNames[i]);
				System.out.println("Path: " + handles[i].path());
			}
			return handles;
		}
		else
		{
			FileHandle[] files = Gdx.files.local(dir).list();
			String output = "";
			for(FileHandle file : files)
			{				
				output += file.name() + "\n";
			}
			output = output.substring(0, output.length() - 1); //remove the last character
			Gdx.files.local(containerPath).writeString(output, false);
			System.out.println("saving file: " + containerPath);
			return files;
		}
	}
	
	/*
	public static FileHandle[] list(String dir)
	{
		
	}*/
	
}
