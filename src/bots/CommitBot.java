package bots;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;

import lib.Vector2;
import world.Circle;
import world.CircleStats;
import world.Projectile;
import world.Sprite;

public class CommitBot extends Mind
{
	CircleStats stats;
	
	
	
	
	public CommitBot(CommitBot r)
	{
		super(r);
		this.stats = r.stats;
	}
	
	public CommitBot(Circle c, float variance, float mean)
	{
		super(c, variance, mean);
	}

	@Override
	public Mind copy()
	{
		return new CommitBot(this);
	}

	
	
	private class SpriteData
	{
		public int lastUpdateTick = 0;
		public Sprite s;
		public Vector2[] positions = new Vector2[30];
		public Vector2[] velocities = new Vector2[30];
		
		public SpriteData(Sprite s)
		{
			this.s = s;
			positions[0] = s.getPosition();
			velocities[0] = s.getVelocity();
		}
		
		public void clearTick()
		{
			positions[ticks % positions.length] = null;
			velocities[ticks % velocities.length] = null;
		}
		
		public Vector2 getAverageVelocity()
		{
			Vector2 averagePosition = new Vector2(0,0);
			int dataPoints = 0;
			for(int i = 0; i < velocities.length; i++)
			{
				if(velocities[i] != null)
				{
					dataPoints++;
					averagePosition.add(velocities[i]);
				}
			}
			return averagePosition.div(dataPoints);
		}
		
		public Vector2 getAveragePosition()
		{
			Vector2 averagePosition = new Vector2(0,0);
			int dataPoints = 0;
			for(int i = 0; i < positions.length; i++)
			{
				if(positions[i] != null)
				{
					dataPoints++;
					averagePosition.add(positions[i]);
				}
			}
			return averagePosition.div(dataPoints);
		}
		
		public void updateData(Vector2 position, Vector2 velocity)
		{
			positions[ticks % positions.length] = position;
			velocities[ticks % velocities.length] = velocity;
			lastUpdateTick = ticks;
		}
		
		
		@Override
		public String toString()
		{
			return "" + s + ": " + getAveragePosition() + ", " + getAverageVelocity();
		}
	}
	
	private HashMap<Integer, SpriteData> tracked = new HashMap<>(); 
	
	private int ticks = 0;
	
	private void updateTracked(ArrayList<Sprite> inView)
	{
		// clear the current datapoint
		for(Entry<Integer, SpriteData> e : tracked.entrySet())
		{
			if(inView.contains(e.getValue().s)) e.getValue().clearTick();
			if(e.getValue().s instanceof Projectile && ticks - e.getValue().lastUpdateTick > 30) tracked.remove(e.getKey());
		}
		
		for(Sprite s : inView)
		{
			int id = s.getId();
			if(tracked.get(s) == null)
			{
				tracked.put(id, new SpriteData(s));
			}
			else
			{
				tracked.get(id).updateData(s.getPosition(), s.getVelocity());
			}
		}
		
		System.out.println("Sprite Data (" + ticks + "): ");
		for(Entry<Integer, SpriteData> e : tracked.entrySet())
		{
			System.out.println(e.getValue());
		}
		System.out.println("\n");
	}
	
	@Override
	public void think()
	{
		if (isAlive())
		{
			ArrayList<Sprite> inView = requestInView();
			updateTracked(inView);
			Sprite target = null;
			for (Sprite s : inView)
			{
				if (s instanceof Circle)
				{
					if (target != null
							&& s.getPosition().dist(getPosition()) < target.getPosition().dist(getPosition()))
					{
						target = s;
					}
					else if (target == null)
					{
						target = s;
					}
				}
			}

			if (target != null)
			{
				Vector2 direction = target.getPosition().sub(calcEyePosition(getPosition(), getVelocity()));
				Vector2 left = new Vector2(1, getVelocity().angle() - Circle.FOV / 2, true);
				Vector2 right = new Vector2(1, getVelocity().angle() + Circle.FOV / 2, true);
				if(Math.acos(direction.dot(left) / (direction.mag()))
						- Math.acos(direction.dot(right) / (direction.mag())) > 0)
				{
					turn((float) Math.acos(direction.dot(getVelocity()) / direction.mag() / getVelocity().mag()));
				}
				else
				{
					turn((float) -Math.acos(direction.dot(getVelocity()) / direction.mag() / getVelocity().mag()));
				}
				
			}
			else
			{
				turn((float) Circle.MAX_TURNING_ANGLE);
			}

			if (target != null)
				shoot();
			ticks++;
		}
		else
		{
			stats = getStats();
		}
	}
	
	@Override
	public String toString()
	{
		return "CommitBot";
	}
}
