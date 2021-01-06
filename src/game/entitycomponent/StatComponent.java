package game.entitycomponent;

import java.math.BigInteger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.entity.EntityComponent;
import game.utils.Rand;

public class StatComponent extends EntityComponent {
	
	public static class StatComponentData
	{
		public byte[] maxhp;
		public byte[] hp;
		public byte[] atk_min, atk_max;
		
		public byte[] lvl;
		public byte[] xp;
		public byte[] xplimit;
		public byte[] gold;
		
		public StatComponent toStats()
		{
			StatComponent stats = new StatComponent();
			stats.maxhp = new BigInteger(maxhp);
			stats.hp = new BigInteger(hp);
			stats.atk_min = new BigInteger(atk_min);
			stats.atk_max = new BigInteger(atk_max);
			stats.lvl = new BigInteger(lvl);
			stats.xp = new BigInteger(xp);
			stats.xplimit = new BigInteger(xplimit);
			stats.gold = new BigInteger(gold);
			return stats;
		}
	}
	
	public StatComponentData toData()
	{
		StatComponentData data = new StatComponentData();
		data.maxhp = maxhp.toByteArray();
		data.hp = hp.toByteArray();
		data.atk_min = atk_min.toByteArray();
		data.atk_max = atk_max.toByteArray();
		data.lvl = lvl.toByteArray();
		data.xp = xp.toByteArray();
		data.xplimit = xplimit.toByteArray();
		data.gold = gold.toByteArray();
		return data;
	}
	
	public BigInteger maxhp = new BigInteger("0");
	public BigInteger hp = new BigInteger("0");
	public BigInteger atk_min = new BigInteger("0"), atk_max = new BigInteger("0");
	
	public BigInteger lvl = new BigInteger("0");
	public BigInteger xp = new BigInteger("0");
	public BigInteger xplimit = new BigInteger("0");
	public BigInteger gold = new BigInteger("0");
	
	public void multiply(BigInteger times)
	{
		maxhp.multiply(times);
		hp.multiply(times);
		atk_min.multiply(times);
		atk_max.multiply(times);
		lvl.multiply(times);
		xp.multiply(times);
		xplimit.multiply(times);
		gold.multiply(times);
	}
	
	public void set(StatComponent stats)
	{
		this.maxhp = stats.maxhp;
		this.hp = stats.hp;
		this.atk_min = stats.atk_min;
		this.atk_max = stats.atk_max;
		this.lvl = stats.lvl;
		this.xp = stats.xp;
		this.xplimit = stats.xplimit;
		this.gold = stats.gold;
	}
	
	public BigInteger battlePower()
	{
		return maxhp.add(atk_max);
	}
	
	public BigInteger atk()
	{
		if(atk_min.equals(atk_max))
			return atk_min;
		return atk_min.add(new BigInteger("" + Rand.next((atk_max.subtract(atk_min).add(new BigInteger("1"))))));
	}
	
	@Override
	public String getName() {
		return "stats";
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
	
	@Override
	public boolean isTickable() {
		return false;
	}
	
	@Override
	public boolean isVisible() {
		return false;
	}
	
	
	@Override
	public String toString() {
		return super.toString() + " [maxhp=" + maxhp + ",hp=" + hp + ",atk_min=" + atk_min + ",atk_max=" + atk_max + ",lvl=" + lvl + ",xp=" + xp + ",xplimit=" + xplimit + ",gold=" + gold + "]";
	}
	
	
}
