package game.net;

import game.Game;

public interface NetAction {
	public void run(Game game, NetClient client);
	
}
