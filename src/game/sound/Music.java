package game.sound;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Music extends Audio {
	public static float masterVolume = 1.0f;
	
	private com.badlogic.gdx.audio.Music music;
	private float duration;
	private long lastTimePlayed;
	
	public Music(FileHandle handle) {
		music = Gdx.audio.newMusic(handle);
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(handle.file());
			duration = (float) ((double) audioInputStream.getFrameLength() / (double) audioInputStream.getFormat().getFrameRate());
			
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		music.setVolume(volume * this.volume * masterVolume);
		music.setPan(pan, volume * this.volume * masterVolume);
		music.play();
		updatePlayTimer();
		return 0;
	}
	
	public long play(float volume, float pitch) {
		return play(volume);
	}
	
	public long play(float volume, float pitch, float pan) {
		music.setVolume(volume * this.volume * masterVolume);
		music.setPan(pan * this.pan, volume * this.volume * masterVolume);
		music.play();
		updatePlayTimer();
		return 0;
	}
	
	public long play() {
		music.setVolume(this.volume * masterVolume);
		music.setPan(pan, this.volume * masterVolume);
		music.play();
		updatePlayTimer();
		return 0;
	}
	
	public void stop() {
		music.stop();
		lastTimePlayed -= (long) ((double) this.duration * 1000D);
	}
	
	public void stop(long id) {
		stop();
	}
	
	public void pause() {
		music.pause();
	}
	
	public void pause(long id) {
		pause();
	}
	
	public void resume() {
		music.play();
	}
	
	public void resume(long id) {
		resume();
	}
	
	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
		music.setVolume(volume);
	}

	public void setPan(float pan) {
		this.pan = pan;
		music.setPan(pan, this.volume);
	}
	
	/**
	 * Does nothing as it's not possible
	 * to pitch music.
	 */
	public void setPitch(float pitch) {
		
	}

	public com.badlogic.gdx.audio.Music getMusic() {
		return music;
	}
	
	public boolean isLooping()
	{
		return music.isLooping();
	}
	
	public void setLooping(boolean looping)
	{
		music.setLooping(looping);
	}
	
	public void dispose() {
		music.stop();
		music.dispose();
	}
	
	public float getDuration()
	{
		return duration;
	}
	
}
