package bots;

import world.Circle;
import world.CircleStats;

public class RobbieBot extends Mind
{
	CircleStats stats;
	
	public RobbieBot(RobbieBot r)
	{
		super(r);
		this.stats = r.stats;
	}
	
	public RobbieBot(Circle c, float variance, float mean)
	{
		super(c, variance, mean);
	}

	@Override
	public Mind copy()
	{
		return new RobbieBot(this);
	}

	@Override
	public void think()
	{
		stats = getStats();
	}
	
	@Override
	public String toString()
	{
		return "Robbie";
	}
}
