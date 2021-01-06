package game.utils;

public class Incrementer {
	public static final LimitAction LIMIT_REVERSE_SPEED = new LimitAction()
	{
		public void limitReached(Incrementer incrementer)
		{
			incrementer.speed *= -1;
		}
	};
	
	public static final LimitAction LIMIT_START_FROM_MIN = new LimitAction()
	{
		public void limitReached(Incrementer incrementer)
		{
			if(incrementer.speed < 0) incrementer.speed *= -1;
			incrementer.value = incrementer.min;
		}
	};
	
	public static final LimitAction LIMIT_START_FROM_MAX = new LimitAction()
	{
		public void limitReached(Incrementer incrementer)
		{
			if(incrementer.speed > 0) incrementer.speed *= -1;
			incrementer.value = incrementer.max;
		}
	};
	
	protected float value;
	protected float speed;
	protected float min;
	protected float max;
	protected boolean noLimit = false;
	protected LimitAction limitAction;
	
	public Incrementer(float startVal, boolean noLimit, float speed)
	{
		this.value = startVal;
		this.noLimit = noLimit;
		this.speed = speed;
	}
	
	public Incrementer(float startVal, float min, float max, float speed, LimitAction limitAction)
	{
		this.value = startVal;
		this.min = min;
		this.max = max;
		this.speed = speed;
		this.limitAction = limitAction;
	}
	
	/**
	 * Increments the value according to the speed.<br>
	 * If the speed equals 0, this method won't do anything.
	 */
	public void increment()
	{
		if(speed > 0)
		{
			if(value < max || noLimit)
				value += speed;
			else limitReached();
		}
		else if(speed < 0)
		{
			if(value > min || noLimit)
				value += speed;
			else limitReached();
		}
	}
	
	protected void limitReached()
	{
		limitAction.limitReached(this);
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getMin() {
		return min;
	}

	public void setMin(float min) {
		this.min = min;
	}

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}

	public LimitAction getLimitAction() {
		return limitAction;
	}

	public void setLimitAction(LimitAction limitAction) {
		this.limitAction = limitAction;
	}
	
	public static interface LimitAction
	{
		public void limitReached(Incrementer incrementer);
	}

}
