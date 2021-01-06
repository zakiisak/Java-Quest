package game.sound;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {
	
	private Map<String, Sound> sounds = new HashMap<String, Sound>();
	private Map<String, Music> musics = new HashMap<String, Music>();
	
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
		return sounds.get(name);
	}
	
	public Music getMusic(String name)
	{
		return musics.get(name);
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
		sounds.put(name, sound);
		return sound;
	}
	
	public Music addMusic(Music music, String name)
	{
		if(music == null) return null;
		musics.put(name, music);
		return music;
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
