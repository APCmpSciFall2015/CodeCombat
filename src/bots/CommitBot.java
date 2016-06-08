package bots;

import java.util.ArrayList;
import java.util.Map.Entry;

import app.Main;

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
	/** Object to represent tracked sprite **/
	private class SpriteData
	{
		public int lastUpdateTick = 0;
		public float deltaTheta = 0;
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
			if (dataPoints != 0)
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
			if (dataPoints != 0)
				s.setPosition(averagePosition.div(dataPoints));
		}

		public void calcDeltaTheta()
		{
			deltaTheta = 0;
			int dataPoints = 0;
			for (int i = 0; i < velocities.length - 1; i++)
			{
				if (velocities[i] != null && velocities[i + 1] != null)
				{
					deltaTheta += velocities[i].angle() - velocities[i + 1].angle();
					dataPoints++;
				}
			}
			if (dataPoints != 0)
				deltaTheta /= dataPoints;
		}

		public void updateData(Vector2 position, Vector2 velocity)
		{
			positions[ticks % positions.length] = position;
			velocities[ticks % velocities.length] = velocity;
			lastUpdateTick = ticks;
			calcAveragePosition();
			calcAverageVelocity();
			calcDeltaTheta();
		}

		@Override
		public String toString()
		{
			return "" + s + ": " + s.getPosition() + ", " + s.getVelocity();
		}
	}

	private HashMap<Integer, SpriteData> tracked = new HashMap<>();
	private ArrayList<Sprite> inView;
	private int ticks = 0;
	private Vector2 position = getPosition();
	private Vector2 velocity = getVelocity();
	private Vector2 eyePosition = calcEyePosition(position, velocity);
	private Circle circle = getCircle();
	private Sprite target = null;
	private Sprite closestShield = null;
	private Sprite closestTarget = null;
	private Sprite predictedTarget = null;
	private Sprite closestMine = null;
	private Sprite sightedBehind = null;
	private Sprite sightedFront = null;
	private Sprite closestProjectile = null;
	// Constructors
	// ------------------------------------------------

	public CommitBot(CommitBot r)
	{
		super(r);
	}

	public CommitBot(Circle c, float variance, float mean)
	{
		super(c, variance, mean);
	}

	// Functional methods
	// -----------------------------------------------

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

	private void updateTracked(ArrayList<Sprite> inView)
	{
		// update old data
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

		// update with new data
		for (Sprite s : inView)
		{
			int id = s.getId();
			if (tracked.get(id) == null)
			{
				tracked.put(id, new SpriteData(s));
			}
			else
			{
				tracked.get(id).updateData(s.getPosition(), s.getVelocity());
			}
		}

		// System.out.println("Sprite Data (" + ticks + "): ");
		// for (Entry<Integer, SpriteData> e : tracked.entrySet())
		// {
		// System.out.println(e.getValue());
		// }
		// System.out.println("\n");
	}

	/**
	 * Enacts a turn toward the specified sprite
	 * 
	 * @param s Sprite to turn toward
	 */
	private void turnTowards(Sprite s)
	{
		Vector2 direction = s.getPosition().sub(eyePosition);
		Vector2 left = new Vector2(1, velocity.angle() - Circle.FOV / 2, true);
		Vector2 right = new Vector2(1, velocity.angle() + Circle.FOV / 2, true);
		if (Math.acos(direction.dot(left) / (direction.mag()))
				- Math.acos(direction.dot(right) / (direction.mag())) > 0)
		{
			turn((float) Math.acos(direction.dot(velocity) / direction.mag() / velocity.mag()));
		}
		else
		{
			turn((float) -Math.acos(direction.dot(velocity) / direction.mag() / velocity.mag()));
		}
	}

	/**
	 * Enacts a turn away from the specified sprite.
	 * 
	 * @param s Sprite to turn away from
	 */
	private void turnAwayFrom(Sprite s)
	{
		Vector2 direction = s.getPosition().sub(eyePosition);
		Vector2 left = new Vector2(1, velocity.angle() - Circle.FOV / 2, true);
		Vector2 right = new Vector2(1, velocity.angle() + Circle.FOV / 2, true);
		if (Math.acos(direction.dot(left) / (direction.mag()))
				- Math.acos(direction.dot(right) / (direction.mag())) > 0)
		{
			turn((float) Math.acos(direction.dot(velocity) / direction.mag() / velocity.mag()) + (float) Math.PI);
		}
		else
		{
			turn((float) -Math.acos(direction.dot(velocity) / direction.mag() / velocity.mag()) - (float) Math.PI);
		}
	}

	/**
	 * The clearPath method checks if there is a clear line of sight between a
	 * this and a target object.
	 * 
	 * @param s Sprite to target
	 * @return clear path to sprite?
	 */
	private boolean clearPath(Sprite s)
	{
		Vector2 eyePosition = calcEyePosition(getPosition(), getVelocity());
		Vector2 path = s.getPosition().sub(eyePosition);
		for (Entry<Integer, SpriteData> e : tracked.entrySet())
		{
			if (e.getValue().s instanceof Obstacle)
			{
				Vector2 obstruction = e.getValue().s.getPosition().sub(eyePosition);
				Vector2 obstruction2 = obstruction.copy();
				float x = s.getSize().getX() / 2;
				float y = s.getSize().getY() / 2;
				Vector2[] offsets = new Vector2[] { obstruction.copy().add(new Vector2(x, y)),
						obstruction.copy().add(new Vector2(-x, y)), obstruction.copy().add(new Vector2(-x, -y)),
						obstruction.copy().add(new Vector2(x, -y)) };

				for (int i = 0; i < offsets.length; i++)
				{
					if (offsets[i].dist(eyePosition) < obstruction.dist(eyePosition))
						obstruction = offsets[i];
					if (offsets[i].dist(eyePosition) > obstruction2.dist(eyePosition))
						obstruction2 = offsets[i];
				}

				if (Math.acos(path.dot(obstruction) / (obstruction.mag() * path.mag()))
						+ Math.acos(path.dot(obstruction2) / (obstruction2.mag() * path.mag())) < Math
								.acos(obstruction.dot(obstruction2) / (obstruction.mag() * obstruction2.mag()))
						&& obstruction.mag() < path.mag())
				{
					return false;
				}
			}
		}
		return true;
	}

	// Overridden methods
	// -----------------------------------------------

	@Override
	public Mind copy()
	{
		return new CommitBot(this);
	}

	@Override
	public void think()
	{
		if (isAlive())
		{
			position = getPosition();
			velocity = getVelocity();
			eyePosition = calcEyePosition(position, velocity);
			inView = requestInView();
			circle = getCircle();
			target = null;
			closestShield = null;
			closestTarget = null;
			predictedTarget = null;
			closestMine = null;
			sightedBehind = null;
			sightedFront = null;
			closestProjectile = null;
			updateTracked(inView);

			// Process data
			// ---------------------------------------------------------------------

			for (Entry<Integer, SpriteData> e : tracked.entrySet())
			{
				Sprite s = e.getValue().s;

				Vector2 direction = s.getPosition().sub(position);
				if (Math.acos(direction.dot(velocity) / (velocity.mag() * direction.mag())) < Circle.FOV / 2
						&& !inView.contains(s))
				{
					tracked.remove(s.getId());
				}

				// get closest circle and who sees me
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
						direction = position.sub(s.getPosition());
						if (Math.acos(
								direction.dot(s.getVelocity()) / (s.getVelocity().mag() * direction.mag())) < Circle.FOV
										/ 2)
						{
							sightedFront = s;
						}
					}
					else
					{
						// check if circle in memory should see me
						direction = position.sub(s.getPosition());
						if (Math.acos(
								direction.dot(s.getVelocity()) / (s.getVelocity().mag() * direction.mag())) < Circle.FOV
										/ 2)
						{
							sightedBehind = s;
						}
					}
				}
				// get closest shield
				else if (s instanceof Shield)
				{
					if (closestShield == null || (closestShield != null
							&& position.dist(s.getPosition()) < position.dist(closestShield.getPosition())))
					{
						closestShield = s;
					}
				}
				// get closest mine
				else if (s instanceof Mine)
				{
					if (closestMine == null || (closestMine != null
							&& position.dist(s.getPosition()) < position.dist(closestMine.getPosition())))
					{
						closestMine = s;
					}
				}
				// get closest projectile
				else if (s instanceof Projectile)
				{
					if (closestProjectile == null || (closestProjectile != null
							&& position.dist(s.getPosition()) < position.dist(closestProjectile.getPosition())))
					{
						closestProjectile = s;
					}
				}
			}

			// react to data
			// ----------------------------------------------------------

			// defend from attacker in front
			if (sightedFront != null && clearPath(sightedFront))
			{
				target = sightedFront;
			}
			else if (closestTarget != null && clearPath(closestTarget))
			{
				target = closestTarget;
			}

			// predict future location of closest target or greatest danger
			if (target != null)
			{
				predictedTarget = target.copy();

				// predict location at impact
				int ticksTillImpact = (int) (target.getPosition().dist(position) / Projectile.SPEED);
				for (int i = 0; i < ticksTillImpact; i++)
				{
					predictedTarget.setPosition(predictedTarget.getPosition().add(predictedTarget.getVelocity()));
					for (Entry<Integer, SpriteData> e : tracked.entrySet())
					{
						Sprite s = e.getValue().s;
						if (predictedTarget.getId() != s.getId() && colliding(predictedTarget, s))
						{
							predictedTarget.collide(s);
						}
					}
					predictedTarget.slideWalls();
				}
				// corect some amount proportional to the distance
				predictedTarget.setPosition(target.getPosition()
						.add(predictedTarget.getPosition()
								.mult((float) position.dist(target.getPosition()) / (float) Main.WORLD_HEIGHT / 2))
						.div(2));
			}

			// avoid mines
			if (closestMine != null && closestMine.getPosition().dist(position) < Mine.RADIUS + Circle.RADIUS * 3)
			{
				turnAwayFrom(closestMine);
			}
			// attack predicted location of target
			else if (predictedTarget != null && clearPath(predictedTarget))
			{
				turnTowards(predictedTarget);
				Vector2 direction = predictedTarget.getPosition().sub(eyePosition);
				if (Math.acos(direction.dot(velocity) / (direction.mag() * velocity.mag())) < Math.toRadians(Circle.FOV / 3))
					shoot();
			}
			// dodge incoming projectile
			else if (closestProjectile != null && !((Projectile) closestProjectile).isOwner(circle)
					&& clearPath(closestProjectile))
			{
				turnAwayFrom(closestProjectile);
			}
			// check back
			else if (sightedBehind != null)
			{
				turnTowards(sightedBehind);
			}
			// get shields
			else if (!circle.isShielded() && closestShield != null && clearPath(closestShield))
			{
				turnTowards(closestShield);
			}
			// obstacle avoidance
			else
			{
				turn(Circle.MAX_TURNING_ANGLE);
			}
		}
		ticks++;
	}

	@Override
	public String toString()
	{
		return "CommitBot";
	}
}
