package game.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Music extends Audio {
	
	private com.badlogic.gdx.audio.Music music;
	
	public Music(FileHandle handle) {
		music = Gdx.audio.newMusic(handle);
	}
	
	public long play(float volume) {
		music.setVolume(volume * this.volume);
		music.setPan(pan, volume * this.volume);
		music.play();
		return 0;
	}
	
	public long play(float volume, float pitch) {
		return play(volume);
	}
	
	public long play(float volume, float pitch, float pan) {
		music.setVolume(volume * this.volume);
		music.setPan(pan * this.pan, volume * this.volume);
		music.play();
		return 0;
	}
	
	public long play() {
		music.setVolume(this.volume);
		music.setPan(pan, this.volume);
		music.play();
		return 0;
	}
	
	public void stop() {
		music.stop();
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
	
}
