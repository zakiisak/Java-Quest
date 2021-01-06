package game.sound;

import java.util.ArrayList;
import java.util.List;

public abstract class Audio {
	protected float volume = 1.0f;
	protected float pan = 0.0f;
	protected float pitch = 1;
	
	protected List<Long> ids = new ArrayList<Long>();
	
	public abstract long play(float volume);
	
	public abstract long play(float volume, float pitch);
	
	public abstract long play(float volume, float pitch, float pan);
	
	public abstract long play();
	
	public abstract void stop();
	
	public abstract void stop(long id);
	
	public abstract void pause();
	
	public abstract void pause(long id);
	
	public abstract void resume();
	
	public abstract void resume(long id);
	
	public float getVolume() {
		return volume;
	}

	public abstract void setVolume(float volume);
	
	public float getPan() {
		return pan;
	}

	public abstract void setPan(float pan);

	public float getPitch() {
		return pitch;
	}

	public abstract void setPitch(float pitch);

	public List<Long> getIds() {
		return ids;
	}
	
	public abstract void dispose();
	
}
