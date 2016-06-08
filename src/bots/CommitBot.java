package bots;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.HashMap;

import lib.Vector2;
import world.Circle;
import world.CircleStats;
import world.Mine;
import world.Obstacle;
import world.Projectile;
import world.Shield;
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

		public void calcAverageVelocity()
		{
			Vector2 averageVelocity = new Vector2(0, 0);
			int dataPoints = 0;
			for (int i = 0; i < velocities.length; i++)
			{
				if (velocities[i] != null)
				{
					dataPoints++;
					averageVelocity.add(velocities[i]);
				}
			}
			s.setVelocity(averageVelocity.div(dataPoints));
		}

		public void calcAveragePosition()
		{
			Vector2 averagePosition = new Vector2(0, 0);
			int dataPoints = 0;
			for (int i = 0; i < positions.length; i++)
			{
				if (positions[i] != null)
				{
					dataPoints++;
					averagePosition.add(positions[i]);
				}
			}
			s.setPosition(averagePosition.div(dataPoints));
		}

		public void updateData(Vector2 position, Vector2 velocity)
		{
			positions[ticks % positions.length] = position;
			velocities[ticks % velocities.length] = velocity;
			lastUpdateTick = ticks;
			calcAveragePosition();
			calcAverageVelocity();
		}

		@Override
		public String toString()
		{
			return "" + s + ": " + s.getPosition() + ", " + s.getVelocity();
		}
	}

	/**
	 * The colliding method checks if 2 sprites are colliding using an
	 * Axis-Aligned Bounding-Box.
	 * 
	 * @param A 1st sprite
	 * @param B 2nd sprite
	 * @return are the sprites colliding
	 */
	public boolean colliding(Sprite A, Sprite B)
	{
		if (A == B) // if the sprites are the same
		{
			return false;
		}
		// @formatter:off
		return colliding(
				A.getPosition().getX() - A.getSize().getX() / 2, A.getPosition().getY() - A.getSize().getY() / 2,
				A.getPosition().getX() + A.getSize().getX() / 2, A.getPosition().getY() + A.getSize().getY() / 2,
				B.getPosition().getX() - B.getSize().getX() / 2, B.getPosition().getY() - B.getSize().getY() / 2,
				B.getPosition().getX() + B.getSize().getX() / 2, B.getPosition().getY() + B.getSize().getY() / 2);
		// @formatter:on
	}

	/**
	 * The colliding method checks if there is a collision between 2
	 * Axis-Aligned Bounding-Boxes. !(AX < Bx || BX < Ax || AY < By || BY < Ay)
	 * 
	 * @param Ax Ax
	 * @param Ay Ay
	 * @param AX AX
	 * @param AY AY
	 * @param Bx Bx
	 * @param By By
	 * @param BX BX
	 * @param BY By
	 * @return are the bounding boxes colliding?
	 */
	public boolean colliding(float Ax, float Ay, float AX, float AY, float Bx, float By, float BX, float BY)
	{
		return !(AX < Bx || BX < Ax || AY < By || BY < Ay);
	}

	private HashMap<Integer, SpriteData> tracked = new HashMap<>();
	private ArrayList<Sprite> inView;
	private int ticks = 0;

	private void updateTracked(ArrayList<Sprite> inView)
	{
		// clear the current datapoint
		for (Entry<Integer, SpriteData> e : tracked.entrySet())
		{
			// System.out.println(e.getValue());
			Sprite s = e.getValue().s;
			if (inView.contains(e.getValue().s))
				e.getValue().clearTick();
			// kill supposed dead projectiles
			if (e.getValue().s instanceof Projectile && ticks - e.getValue().lastUpdateTick > 30)
				tracked.remove(e.getKey());
			// update circles out of view
			if (!inView.contains(e.getValue().s) && e.getValue().s instanceof Circle)
			{
				e.getValue().s.setPosition(s.getVelocity().add(s.getPosition()));
				for (Entry<Integer, SpriteData> e1 : tracked.entrySet())
				{
					if (s.getId() != e1.getValue().s.getId() && colliding(s, e1.getValue().s))
					{
						s.collide(e1.getValue().s);
					}
				}
				s.slideWalls();
			}
		}

		for (Sprite s : inView)
		{
			int id = s.getId();
			if (tracked.get(s) == null)
			{
				tracked.put(id, new SpriteData(s));
			}
			else
			{
				tracked.get(id).updateData(s.getPosition(), s.getVelocity());
			}
		}

		// System.out.println("Sprite Data (" + ticks + "): ");
		// for(Entry<Integer, SpriteData> e : tracked.entrySet())
		// {
		// System.out.println(e.getValue());
		// }
		// System.out.println("\n");
	}

	private void turnTowards(Sprite s)
	{
		Vector2 direction = s.getPosition().sub(calcEyePosition(getPosition(), getVelocity()));
		Vector2 left = new Vector2(1, getVelocity().angle() - Circle.FOV / 2, true);
		Vector2 right = new Vector2(1, getVelocity().angle() + Circle.FOV / 2, true);
		if (Math.acos(direction.dot(left) / (direction.mag()))
				- Math.acos(direction.dot(right) / (direction.mag())) > 0)
		{
			turn((float) Math.acos(direction.dot(getVelocity()) / direction.mag() / getVelocity().mag()));
		}
		else
		{
			turn((float) -Math.acos(direction.dot(getVelocity()) / direction.mag() / getVelocity().mag()));
		}
	}

	private void turnAwayFrom(Sprite s)
	{
		Vector2 direction = s.getPosition().sub(calcEyePosition(getPosition(), getVelocity()));
		Vector2 left = new Vector2(1, getVelocity().angle() - Circle.FOV / 2, true);
		Vector2 right = new Vector2(1, getVelocity().angle() + Circle.FOV / 2, true);
		if (Math.acos(direction.dot(left) / (direction.mag()))
				- Math.acos(direction.dot(right) / (direction.mag())) > 0)
		{
			turn((float) Math.acos(direction.dot(getVelocity()) / direction.mag() / getVelocity().mag())
					+ (float) Math.PI);
		}
		else
		{
			turn((float) -Math.acos(direction.dot(getVelocity()) / direction.mag() / getVelocity().mag())
					- (float) Math.PI);
		}
	}

	private Vector2 oldPosition = getPosition();

	@Override
	public void think()
	{
		if (isAlive())
		{
			inView = requestInView();
			updateTracked(inView);

			Vector2 eyePosition = calcEyePosition(getPosition(), getVelocity());
			Sprite closestShield = null;
			Sprite closestTarget = null;
			Sprite closestMine = null;
			Sprite closestObstacle = null;
			Sprite sightedBehind = null;
			Sprite sightedFront = null;
			Sprite closestProjectile = null;

			for (Entry<Integer, SpriteData> e : tracked.entrySet())
			{
				Sprite s = e.getValue().s;

				Vector2 direction = s.getPosition().sub(getPosition());
				if (Math.abs(Math.acos(direction.dot(getVelocity()) / (getVelocity().mag() * direction.mag()))) < Math
						.abs(Circle.FOV / 2) && !inView.contains(s))
				{
					tracked.remove(s.getId());
				}

				if (s instanceof Circle)
				{
					if (inView.contains(s))
					{
						// get closest target circle
						if (closestTarget == null || (closestTarget != null
								&& eyePosition.dist(s.getPosition()) < eyePosition.dist(closestTarget.getPosition())))
						{
							closestTarget = s;
						}

						// check if circle sees me
						direction = getPosition().sub(s.getPosition());
						if (Math.abs(Math.acos(
								direction.dot(s.getVelocity()) / (s.getVelocity().mag() * direction.mag()))) < Math
										.abs(Circle.FOV / 2))
						{
							sightedFront = s;
							// System.out.println("circle in front sees me (" +
							// ticks + "): " + s);
						}
					}
					else
					{
						// check if circle sees me
						direction = getPosition().sub(s.getPosition());
						if (Math.abs(Math.acos(
								direction.dot(s.getVelocity()) / (s.getVelocity().mag() * direction.mag()))) < Math
										.abs(Circle.FOV / 2))
						{
							sightedBehind = s;
							// System.out.println("circle behind sees me (" +
							// ticks + "): " + s);
						}
					}
				}
				else if (s instanceof Shield)
				{
					if (closestShield == null || (closestShield != null
							&& eyePosition.dist(s.getPosition()) < eyePosition.dist(closestShield.getPosition())))
					{
						closestShield = s;
					}
				}
				else if (s instanceof Mine)
				{
					if (closestMine == null || (closestMine != null
							&& eyePosition.dist(s.getPosition()) < eyePosition.dist(closestMine.getPosition())))
					{
						closestMine = s;
					}
				}
				else if (s instanceof Obstacle)
				{
					if (closestObstacle == null || (closestObstacle != null
							&& eyePosition.dist(s.getPosition()) < eyePosition.dist(closestObstacle.getPosition())))
					{
						closestObstacle = s;
					}
				}
				else if (s instanceof Projectile)
				{
					if (closestProjectile == null || (closestProjectile != null
							&& eyePosition.dist(s.getPosition()) < eyePosition.dist(closestProjectile.getPosition())))
					{
						closestProjectile = s;
					}
				}
			}

			if (closestProjectile != null && !((Projectile) closestProjectile).isOwner(getCircle())
					&& clearPath(closestProjectile))
			{
				if (getShootTimer() <= 0)
				{
					turnTowards(closestProjectile);
					shoot();
				}
				else
				{
					turnAwayFrom(closestProjectile);
				}
			}
			else if (sightedFront != null && clearPath(sightedFront) && getShootTimer() < ((Circle) sightedFront).getShootTimer())
			{
				turnTowards(sightedFront);
			}
			else if (closestTarget != null && clearPath(closestTarget))
			{
				turnTowards(closestTarget);
				shoot();
			}
			else if (sightedBehind != null)
			{
				turnTowards(sightedBehind);
			}
			else if (!getCircle().isShielded() && closestShield != null && clearPath(closestShield))
			{
				turnTowards(closestShield);
			}
			else if (closestMine != null
					&& closestMine.getPosition().dist(getPosition()) < Mine.RADIUS + Circle.RADIUS * 3)
			{
				turnAwayFrom(closestMine);
			}
			// obstacle avoidance
			else
			{
				turn((float) Circle.MAX_TURNING_ANGLE);
			}
		}

		ticks++;
	}

	private boolean clearPath(Sprite s)
	{
		Vector2 eyePosition = calcEyePosition(getPosition(), getVelocity());
		Vector2 path = s.getPosition().sub(eyePosition);
		for (Entry<Integer, SpriteData> e : tracked.entrySet())
		{
			if (e.getValue().s instanceof Obstacle)
			{
				Vector2 obstruction = e.getValue().s.getPosition().sub(eyePosition);
				float x = s.getSize().getX() / 2;
				float y = s.getSize().getY() / 2;
				Vector2[] offsets = new Vector2[] { obstruction.copy().add(new Vector2(x, y)),
						obstruction.copy().add(new Vector2(-x, y)), obstruction.copy().add(new Vector2(-x, -y)),
						obstruction.copy().add(new Vector2(x, -y)) };

				for (int i = 0; i < offsets.length; i++)
				{
					if (offsets[i].dist(eyePosition) < obstruction.dist(eyePosition))
						obstruction = offsets[i];
				}

				if (Math.abs(Math.acos(path.dot(obstruction) / (obstruction.mag() * path.mag()))) < Math.toRadians(1)
						&& obstruction.mag() < path.mag())
				{
					System.out.println("" + e.getValue() + ""
							+ Math.abs(Math.acos(path.dot(obstruction) / (obstruction.mag() * path.mag()))));
					System.out.println(obstruction + "|" + path);
					System.out.println(obstruction.mag() + "|" + path.mag());
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "CommitBot";
	}

	private Sprite seek()
	{
		Sprite target = null;
		for (Entry<Integer, SpriteData> e : tracked.entrySet())
		{
			Sprite s = e.getValue().s;

			Vector2 direction = s.getPosition().sub(getPosition());
			if (Math.abs(Math.acos(direction.dot(getVelocity()) / (getVelocity().mag() * direction.mag()))) < Math
					.abs(Circle.FOV / 2) && !inView.contains(s))
			{
				tracked.remove(s.getId());
			}

			if (s instanceof Circle || (!getCircle().isShielded() && s instanceof Shield && ((Shield) s).isUnbound()))
			{
				if (target != null && s.getPosition().dist(getPosition()) < target.getPosition().dist(getPosition()))
				{
					target = s;
				}
				else if (target == null)
				{
					target = s;
				}
			}
		}
		return target;
	}

	private Sprite avoid()
	{
		Sprite target = null;
		for (Entry<Integer, SpriteData> e : tracked.entrySet())
		{
			Sprite s = e.getValue().s;

			Vector2 direction = s.getPosition().sub(getPosition());
			if (Math.abs(Math.acos(direction.dot(getVelocity()) / (getVelocity().mag() * direction.mag()))) < Math
					.abs(Circle.FOV / 2) && !inView.contains(s))
			{
				tracked.remove(s.getId());
			}

			if (s instanceof Mine || s instanceof Obstacle)
			{
				if (target != null && s.getPosition().dist(getPosition()) < target.getPosition().dist(getPosition()))
				{
					target = s;
				}
				else if (target == null)
				{
					target = s;
				}
			}
		}
		return target;
	}
}
