package game.entitycomponent;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.EntityComponent;
import game.tile.Tile;
import game.worlds.World;

public class StepBattleComponent extends EntityComponent {

	private World world;
	public int stepsLimit, stepsLimitVariation;
	private int stepsSetLimit;
	public int steps;
	private float lastX, lastY;
	private TransformComponent transform;
	public PlayerInputComponent input;
	private Random random = new Random();
	public boolean enabled = true;
	
	private boolean resetLastCoordinates;
	
	public void setWorld(World world)
	{
		this.world = world;
		resetLastCoordinates = true;
	}
	
	public StepBattleComponent(World world, PlayerInputComponent input, TransformComponent transform, int stepsLimit, int stepsLimitVariation) {
		this.world = world;
		this.input = input;
		this.transform = transform;
		lastX = transform.x;
		lastY = transform.y;
		this.stepsLimit = stepsLimit;
		this.stepsLimitVariation = stepsLimitVariation;
		stepsSetLimit = stepsLimit - stepsLimitVariation / 2 + random.nextInt(stepsLimitVariation + 1);
	}
	
	@Override
	public String getName() {
		return "step_encounter";
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		if(enabled == false)
			return;
		if(resetLastCoordinates)
		{
			lastX = (int) (transform.x + 0.5f);
			lastY = (int) (transform.y + 0.5f);
			resetLastCoordinates = false;
		}
		if((int) (transform.x + 0.5f) - lastX != 0 || (int) (transform.y + 0.5f) - lastY != 0)
		{
			steps++;
			if(steps > stepsSetLimit && world.getEvent((int) (transform.x + 0.5f), (int) (transform.y + 0.5f)) == null
					&& input.getStandingPlayer() == null)
			{
				steps = 0;
				stepsSetLimit = stepsLimit - stepsLimitVariation / 2 + random.nextInt(stepsLimitVariation + 1);
				Game.instance.enterBattle(world.getRandomEnemyInZone((int) (transform.x + 0.5f), (int) (transform.y + 0.5f)));
			}
		}
		lastX = (int) (transform.x + 0.5f);
		lastY = (int) (transform.y + 0.5f);
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
