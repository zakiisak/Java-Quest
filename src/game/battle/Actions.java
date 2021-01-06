package game.battle;

import java.math.BigInteger;

import game.Game;
import game.entity.AttackAnimation;
import game.entity.Items;
import game.entity.Player;
import game.net.BattleNet;
import game.utils.Color;
import game.utils.Event;
import game.utils.Rand;

public class Actions {
	
	public static BattleAction attack = new BattleAction() {
		@Override
		public void onSelected(Game game, TurnComponent turn, Battle battle, Player player) {
			game.addEntity(new AttackAnimation(new Color(1.0f, 1.0f, 1.0f, 0.65f), 0, 40, player.possessItem(Items.BATTLE_SPEED_UPGRADE) ? AttackAnimation.UPGRADE_SPEED : AttackAnimation.DEFAULT_SPEED).setEndReachedEvent(turn.animationEnd).setReversed());
			turn.doingAnimation = true;
			turn.dmgTemp = player.stats.atk();
			if(battle.netBattle)
			{
				BattleNet.attackEnemy(battle, turn.dmgTemp);
			}
		}

		@Override
		public String getName() {
			return "Attack";
		}
	};
	
	public static BattleAction guard = new BattleAction() {
		@Override
		public void onSelected(Game game, TurnComponent turn, Battle battle, Player player) {
			turn.turn++;
			if(battle.netBattle)
			{
				BattleNet.shiftTurn(battle);
			}
		}

		@Override
		public String getName() {
			return "§Guard";
		}
	};
	
	public static BattleAction potion = new BattleAction() {
		@Override
		public void onSelected(Game game, final TurnComponent turn, final Battle battle, Player player) {
			game.addEntity(new AttackAnimation(new Color(0, 1.0f, 0, 0.65f), 0, 40, player.possessItem(Items.BATTLE_SPEED_UPGRADE) ? AttackAnimation.UPGRADE_SPEED : AttackAnimation.DEFAULT_SPEED).setEndReachedEvent(new Event() {
				
				@Override
				public void run() {
					turn.doingAnimation = false;
					BattleNet.shiftTurn(battle);
				}
			}));
			turn.doingAnimation = true;
			BigInteger healAmount = player.stats.maxhp.divide(new BigInteger("2")); 
			player.heal(healAmount);
			player.removeItem(Items.HEALTH_POTION);
			turn.turn++;
			if(battle.netBattle)
			{
				BattleNet.healPlayer(battle, game.client.id, healAmount);
			}
		}

		@Override
		public String getName() {
			return "§Potion";
		}
	};
	
	public static BattleAction escape = new BattleAction() {
		@Override
		public void onSelected(Game game, TurnComponent turn, Battle battle, Player player) {
			float greaterThan = 0.5f;
			if(battle.enemy.stats.battlePower().compareTo(player.stats.battlePower()) > 0)
			{
				greaterThan += 0.25f;
			}
			else if(player.stats.battlePower().compareTo(battle.enemy.stats.battlePower()) > 0)
			{
				greaterThan -= 0.5f;
			}
			if(Rand.rng.nextFloat() > greaterThan || player.possessItem(Items.MAGIC_DICE))
			{
				battle.fled = true;
				battle.abort();
				player.restoreHp();
			}
			else
			{
				turn.turn++;
			}
		}

		@Override
		public String getName() {
			return "§Escape";
		}
	};
	
	public static BattleAction concede = new BattleAction() {
		@Override
		public void onSelected(Game game, TurnComponent turn, Battle battle, Player player) {
			player.killed = true;
			player.stats.hp = BigInteger.ZERO;
			if(battle.netBattle)
			{
				turn.turn++;
				BattleNet.shiftTurn(battle);
			}
			else
				battle.abort();
		}

		@Override
		public String getName() {
			return "§Concede";
		}
	};
	
}
