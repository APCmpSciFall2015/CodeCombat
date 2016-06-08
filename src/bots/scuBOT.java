package bots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import lib.Vector2;
import world.*;

public class scuBOT extends Mind
{
	private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	private HashMap<Projectile, Integer> projectiles = new HashMap<Projectile, Integer>(); 
	private HashMap<Mine, Integer> mines = new HashMap<Mine, Integer>();
	private HashMap<Shield, Integer> shields = new HashMap<Shield, Integer>();
	private HashMap<Circle, Integer> circles = new HashMap<Circle, Integer>();
	private static final int HISTORY_LENGTH = 10;
	private ArrayList<Vector2> posHistory = new ArrayList<Vector2>();
	private Vector2 estimatedLastPos;
	private static final float NEAR_DEFINITION = 120f;
	
	private PriorityQueue<Float> todo = new PriorityQueue<Float>();
	
	private int timeSerp = 0;
	private Vector2 serpAngle = null;
	
	public scuBOT(scuBOT t)
	{
		super(t);
	}

	public scuBOT(Circle c, float variance, float mean)
	{
		super(c, variance, mean);
	}

	public Mind copy()
	{
		return new scuBOT(this);
	}

	@Override
	public void think()
	{
		if (isAlive())
		{
			if (posHistory.size() > 0)
				calcLastPos();
			
			see();
			
			Sprite target;
			if (todo.size() > 0)
			{
				turn(todo.poll());
			}
			else if (obstacles.size() > 0 && (target = getClosest(obstacles)).getPosition().dist(getPosition()) < NEAR_DEFINITION/4)
			{
				turnAwayFrom(target.getPosition());
				while (todo.size() <= 10)
				{
					todo.add(-1*getDirTowards(target.getPosition())*Circle.MAX_TURNING_ANGLE);
				}
			}
			else if (mines.size() > 0 && (target = getClosest(mines)).getPosition().dist(getPosition()) < NEAR_DEFINITION)
			{
				turnAwayFrom(target.getPosition());
			}
//			else if ( projectiles.size() > 0 && (target = getComingTowards(projectiles)) != null)
//			{
//				turnAwayFrom(target.getPosition());
//				System.out.println("Projectile Avoidance");
//			}
			else if (circles.size() > 0 && (target = getComingTowards(circles)) != null)
			{
				turnAwayFrom(target.getPosition());
			}
			else if (estimatedLastPos != null && estimatedLastPos.dist(getPosition()) < getVelocity().mag())
			{
				float turn = Circle.MAX_TURNING_ANGLE;
				if ((estimatedLastPos.getX() - getPosition().getX() < 0.1 && Math.tan(getVelocity().angle()) < 0)
						|| (estimatedLastPos.getY() - getPosition().getY() < 0.1 && Math.tan(getVelocity().angle()) > 0))
				{
					turn *= -1;
				}
				turn(turn);
				while (todo.size() <= 10)
				{
					todo.add(turn);
				}
			}
			else if (circles.size() > 0)
			{
				turnTowards(getClosest(circles).getPosition());
			}
			else if (obstacles.size() > 0
					&& ((target = getFacing(obstacles)) != null
					&& ((target = getClosest(obstacles)).getPosition().dist(getPosition()) < NEAR_DEFINITION)))
			{
				turnAwayFrom(target.getPosition());
			}
			else
			{
				if (timeSerp/30%2 == 0)
					turn(Circle.MAX_TURNING_ANGLE);
				else
					turn(-Circle.MAX_TURNING_ANGLE);

				timeSerp++;
			}
			

			shoot();
			
			updateMemory();
			posHistory.add(getPosition());
			if (posHistory.size() > HISTORY_LENGTH)
				posHistory.remove(0);
		}
	}
	
	private void calcLastPos() 
	{
		float x = getPosition().getX();
		float y = getPosition().getY();
		Vector2 ave = posHistory.get(0);
		for (int i = 1; i < posHistory.size(); i++)
		{
			ave.add(posHistory.get(i));
		}
		ave = new Vector2(ave.getX()/posHistory.size(), ave.getY()/posHistory.size());
		ave = getPosition().sub(ave);
		
		estimatedLastPos = new Vector2(x-ave.getX()*2/posHistory.size(), y-ave.getY()*2/posHistory.size());
	}

	private void see()
	{
		for (Sprite s : requestInView())
		{
			if (s instanceof Obstacle)
			{
				if (!obstacles.contains(s))
					obstacles.add((Obstacle) s);
			}
			else if (s instanceof Projectile)
				projectiles.put((Projectile) s,0);
			else if (s instanceof Shield)
				shields.put((Shield) s, 0);
			else if (s instanceof Mine)
				mines.put((Mine) s, 0);
			else if (s instanceof Circle)
				circles.put((Circle) s, 0);
		}
	}
	
	private void updateMemory()
	{
		for (Projectile p : new ArrayList<Projectile>(projectiles.keySet()))
		{
			if (projectiles.get(p) > 60*5)
				projectiles.remove(p);
			else
			{
				p.setPosition(p.getPosition().add(p.getVelocity()));
				
				projectiles.put(p,projectiles.get(p)+1);
			}
		}

		for (Mine m : new ArrayList<Mine>(mines.keySet()))
		{
			if (mines.get(m) > 60*20)
				mines.remove(m);
			else
			{
				m.setAcceleration(new Vector2(1f / 60f, (float) (m.getVelocity().angle() + Math.PI / 2), true));
				Vector2 previousVelocity = m.getVelocity();
				m.setVelocity(m.getVelocity().add(m.getAcceleration()).normalize());
				m.setPosition(m.getPosition().add(m.getVelocity().add(previousVelocity).div(2)));
				
				mines.put(m, mines.get(m)+1);
			}
		}

		for (Shield s : new ArrayList<Shield>(shields.keySet()))
		{
			if (shields.get(s) > 60*20)
				shields.remove(s);
			else
			{
				s.setAcceleration(new Vector2(1f / 60f, (float) (s.getVelocity().angle() + Math.PI / 2), true));
				Vector2 previousVelocity = s.getVelocity();
				s.setVelocity(s.getVelocity().add(s.getAcceleration()).normalize());
				s.setPosition(s.getPosition().add(s.getVelocity().add(previousVelocity).div(2)));
				
				shields.put(s, shields.get(s)+1);
			}
		}
		
		for (Circle c : new ArrayList<Circle>(circles.keySet()))
		{
			if (circles.get(c) > 60)
				circles.remove(c);
			else
			{
				c.setPosition(c.getPosition().add(c.getVelocity()));
				
				circles.put(c,circles.get(c)+1);
			}
		}
	}

	private void turnTowardsAngle(Vector2 a)
	{
		if (a != null)
		{
			float angleBet = getPosition().angleBetween(a);
			
			turn((float)(getDirTowardsAngle(a)*(Math.PI-angleBet)));
		}
	}
	
	private void turnTowards(Vector2 v)
	{
		if (v != null)
		{
			float angleBet = getPosition().angleBetween(v);

			turn((float)(getDirTowards(v)*(Math.PI-angleBet)));
		}
	}
	
	private void turnAwayFrom(Vector2 v)
	{
		if (v != null)
		{
			float angleBet = getPosition().angleBetween(v);

			turn((float)(-1*getDirTowards(v)*(Math.PI-angleBet)));
		}
	}
	
	private Sprite getClosest(ArrayList<? extends Sprite> list)
	{
		Sprite closest = null;
		for (Sprite s : list)
		{
			if (closest != null
					&& s.getPosition().dist(getPosition()) < closest.getPosition().dist(getPosition()))
			{
				closest = s;
			}
			else if (closest == null)
			{
				closest = s;
			}
		}
		return closest;
	}
	
	private Sprite getClosest(HashMap<? extends Sprite, Integer> map)
	{
		Sprite closest = null;
		for (Sprite s : map.keySet())
		{
			if (closest != null
					&& s.getPosition().dist(getPosition()) < closest.getPosition().dist(getPosition()))
			{
				closest = s;
			}
			else if (closest == null)
			{
				closest = s;
			}
		}
		return closest;
	}
	
	private Sprite getComingTowards(HashMap<? extends Sprite, Integer> map)
	{
		for (Sprite s : map.keySet())
		{
			if (s.getVelocity().angle()-s.getPosition().angleBetween(getPosition()) < Math.PI/6)
			{
				return s;
			}
		}
		return null;
	}
	
	private Sprite getFacing(ArrayList<? extends Sprite> list)
	{
		for (Sprite s : list)
		{
			if (getVelocity().angle()-s.getPosition().angleBetween(s.getPosition()) < Math.PI/6)
			{
				return s;
			}
		}
		return null;
	}
	
	private Sprite getFacing(HashMap<? extends Sprite, Integer> map)
	{
		for (Sprite s : map.keySet())
		{
			if (getVelocity().angleBetween(s.getPosition()) < Math.PI/6)
			{
				return s;
			}
		}
		return null;
	}

	private int getDirTowardsAngle(Vector2 a)
	{
		int dir = 1;
		if (getVelocity().angle() > a.angle())
			dir *= -1;
		return dir;
	}
	
	private int getDirTowards(Vector2 v)
	{
		int dir = 1;
		if (getPosition().angleBetween(v) > getVelocity().angle())
			dir *= -1;
		return dir;
	}

	@Override
	public String toString()
	{
		return "scuBOT";
	}
}
