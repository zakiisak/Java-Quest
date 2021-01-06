package game.entitycomponent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.entity.EntityComponent;

public class MonsterStatsComponent extends EntityComponent {

	public int maxhp_min, maxhp_max;
	public int hp;
	public int atk_min, atk_max;
	
	public int xp_min, xp_max;
	public int gold_min, gold_max;
	
	@Override
	public String getName() {
		return "monster_stats";
	}
	
	@Override
	public boolean isTickable() {
		return false;
	}
	
	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

}
