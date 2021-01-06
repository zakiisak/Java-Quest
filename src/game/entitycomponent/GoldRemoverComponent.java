package game.entitycomponent;

import java.math.BigInteger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Game;
import game.entity.EntityComponent;
import game.utils.Numbers;

public class GoldRemoverComponent extends EntityComponent {

	private float cooldown = 8;
	private float tick = 0;
	
	public StatComponent stats;
	public BigInteger goldToGetRemoved = Numbers.ZERO;
	public int goldTick = 0;
	
	public GoldRemoverComponent(StatComponent stats)
	{
		this.stats = stats;
	}
	
	@Override
	public String getName() {
		return "gold_remover";
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		if(goldToGetRemoved.compareTo(BigInteger.ZERO) > 0)
		{
//			goldTick++;
//			if(goldTick == 2)
//			{
			
			long loop = 20;
			if(goldToGetRemoved.compareTo(new BigInteger("100000")) >= 0)
				loop = 100000L;
			System.out.println("");
			BigInteger MILLION = new BigInteger("1000000");
			for(long i = 0; i < loop; i++)
			{
				if(goldToGetRemoved.compareTo(Numbers.ZERO) <= 0) break;
				if(goldToGetRemoved.mod(MILLION).equals(Numbers.ZERO))
				{
					goldToGetRemoved = goldToGetRemoved.subtract(MILLION);
					stats.gold = stats.gold.subtract(MILLION);
				}
				else if(goldToGetRemoved.mod(Numbers.HUNDRED).equals(Numbers.ZERO))
				{
					goldToGetRemoved = goldToGetRemoved.subtract(Numbers.HUNDRED);
					stats.gold = stats.gold.subtract(Numbers.HUNDRED);
				}
				else if(goldToGetRemoved.mod(Numbers.TWO).equals(Numbers.ZERO))
				{
					goldToGetRemoved = goldToGetRemoved.subtract(Numbers.TWO);
					stats.gold = stats.gold.subtract(Numbers.TWO);
				}
				else
				{
					goldToGetRemoved = goldToGetRemoved.subtract(Numbers.ONE);
					stats.gold = stats.gold.subtract(Numbers.ONE);
				}
			}
//				goldTick = 0;
//			}
			if(tick <= 0)
			{
				tick = cooldown;
				Game.instance.getAudio().getSound("sell").play();
			}
			tick--;
			
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

}
