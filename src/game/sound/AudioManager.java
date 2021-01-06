package game.sound;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import game.FileLoader;

public class AudioManager {
	
	public Map<String, Sound> sounds = new HashMap<String, Sound>();
	public Map<String, Music> musics = new HashMap<String, Music>();
	
	public void loadAll()
	{
		FileHandle[] sounds = FileLoader.list("res/sounds/");
		for(FileHandle handle : sounds)
		{
			addSound(handle);
		}
		
		FileHandle[] music = FileLoader.list("res/music/");
		for(FileHandle handle : music)
		{
			addMusic(handle);
		}
	}
	
	public AudioManager() {}
	
	public Audio get(String name) {
		Audio sound = sounds.get(name);
		if(sound != null) return sound;
		Audio music = musics.get(name);
		if(music != null) return music;
		return null;
		
	}
	
	public Sound getSound(String name)
	{
		return sounds.get(name.toLowerCase());
	}
	
	public Music getMusic(String name)
	{
		return musics.get(name.toLowerCase());
	}
	
	public Audio add(Audio audio, String name) {
		if(audio == null) return null;
		if(audio instanceof Sound)
			sounds.put(name, (Sound) audio);
		if(audio instanceof Music)
			musics.put(name, (Music) audio);
		return audio;
	}
	
	public Sound addSound(Sound sound, String name)
	{
		if(sound == null) return null;
		sounds.put(name.toLowerCase(), sound);
		return sound;
	}
	
	public void addSound(FileHandle handle)
	{
		addSound(new Sound(handle), handle.nameWithoutExtension());
	}
	
	public void addSound(String path)
	{
		FileHandle handle = FileLoader.get(path);
		addSound(new Sound(handle), handle.nameWithoutExtension());
	}

	
	public Music addMusic(Music music, String name)
	{
		if(music == null) return null;
		musics.put(name, music);
		return music;
	}
	
	public void addMusic(String path)
	{
		FileHandle handle = FileLoader.get(path);
		addMusic(new Music(handle), handle.nameWithoutExtension());
	}
	
	public void addMusic(FileHandle handle)
	{
		addMusic(new Music(handle), handle.nameWithoutExtension());
	}
	
	public void dispose() {
		for(Sound s : sounds.values())
			s.dispose();
		for(Music m : musics.values())
			m.dispose();
		sounds.clear();
		musics.clear();
	}
}
