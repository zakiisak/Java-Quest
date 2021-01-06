package game.battle;

import game.Game;
import game.entity.Player;

public interface BattleAction {
	public String getName();
	public void onSelected(Game game, TurnComponent turn, Battle battle, Player player);
}
