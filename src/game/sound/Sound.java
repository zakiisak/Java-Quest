package game.sound;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Sound extends Audio {
	public static float masterVolume = 1.0f;
	
	private com.badlogic.gdx.audio.Sound sound;
	private float duration;
	private long lastTimePlayed;
	
	public Sound(FileHandle handle) {
		sound = Gdx.audio.newSound(handle);
		/*
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(handle.file());
			duration = (float) ((double) audioInputStream.getFrameLength() / (double) audioInputStream.getFormat().getFrameRate());
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
	
	private void updatePlayTimer()
	{
		lastTimePlayed = System.currentTimeMillis();
	}
	
	public boolean isPlaying()
	{
		long duration = (long) ((double) this.duration * 1000D);
		return System.currentTimeMillis() - lastTimePlayed <= duration;
	}
	
	public long play(float volume) {
		long id = sound.play(volume * this.volume * masterVolume, pitch, pan);
		ids.add(id);
		updatePlayTimer();
		return id;
	}
	
	public long play(float volume, float pitch) {
		long id = sound.play(volume * this.volume * masterVolume, pitch * this.pitch, pan);
		ids.add(id);
		updatePlayTimer();
		return id;
	}
	
	public long play(float volume, float pitch, float pan) {
		long id = sound.play(volume * this.volume * masterVolume, pitch * this.pitch, pan * this.pan);
		ids.add(id);
		updatePlayTimer();
		return id;
	}
	
	public long play() {
		long id = sound.play(volume * masterVolume, pitch, pan);
		ids.add(id);
		updatePlayTimer();
		return id;
	}
	
	public void stop() {
		sound.stop();
		ids.clear();
		lastTimePlayed -= (long) ((double) this.duration * 1000D);
	}
	
	public void stop(long id) {
		sound.stop(id);
		ids.remove(id);
	}
	
	public void pause() {
		sound.pause();
	}
	
	public void pause(long id) {
		sound.pause(id);
	}
	
	public void resume() {
		sound.resume();
	}
	
	public void resume(long id) {
		sound.resume(id);
	}
	
	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
		for(long l : ids)
			sound.setVolume(l, volume);
	}

	public void setPan(float pan) {
		this.pan = pan;
		for(long l : ids)
			sound.setPan(l, pan, volume);
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
		for(long l : ids)
			sound.setPitch(l, pitch);
	}

	public com.badlogic.gdx.audio.Sound getSound() {
		return sound;
	}

	public List<Long> getIds() {
		return ids;
	}
	
	public void dispose() {
		sound.stop();
		sound.dispose();
	}
	
	public float getDuration()
	{
		return duration;
	}
}
