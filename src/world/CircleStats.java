package world;

/**
 * Essentially a struct for Circle stats.
 * @author robbie
 * @version 0.1
 */
public class CircleStats
{
	/** circle accuracy during existence */
	public final float totalAccuracy;
	/** circle accuracy during life */
	public final float accuracy;
	/** circle shots fired during existence */
	public final int totalShotsFired;
	/** circle shots fired during life */
	public final int shotsFired;
	/** circle hits during existence */
	public final int totalHits;
	/** circle hits during life */
	public final int hits;
	/** circle deaths */
	public final int deaths;
	/** circle kills during existence */
	public final int totalKills;
	/** circle kills during life */
	public final int kills;
	/** circle ticks alive */
	public final int ticksAlive;
	/** shields acquired during existence */
	public final int totalShieldsAcquired;
	/** shields acquired during life */
	public final int shieldsAcquired;
	/** obstacle collisions during existence */
	public final int totalObstacleCollisions;
	/** obstacle collisions during life */
	public final int obstacleCollisions;
	/** wall collisions during existence */
	public final int totalWallCollisions;
	/** wall collisions during life */
	public final int wallCollisions;
	/** mine collisions during life (possible if have shield) */
	public final int mineCollisions;
	/** mine collisions during existence */
	public final int totalMineCollisions;
	/** projectile collisions during life (possible if have shield) */
	public final int projectileCollisions;
	/** projectile collisions during existence */
	public final int totalProjectileCollisions;

	/**
	 * 1-Argument CircleStats Constructor. Generates a CircleStats object from a
	 * given circle.
	 * @param c Circle to generate stats object from.
	 */
	public CircleStats(Circle c)
	{
		this.totalAccuracy = c.getTotalAccuracy();
		this.accuracy = c.getAccuracy();
		this.totalShotsFired = c.getTotalShotsFired();
		this.shotsFired = c.getShotsFired();
		this.totalHits = c.getTotalHits();
		this.hits = c.getHits();
		this.deaths = c.getDeaths();
		this.totalKills = c.getTotalKills();
		this.kills = c.getKills();
		this.ticksAlive = c.getTicksAlive();
		this.totalShieldsAcquired = c.getTotalShieldsAcquired();
		this.shieldsAcquired = c.getShieldsAcquired();
		this.totalObstacleCollisions = c.getTotalObstacleCollisions();
		this.obstacleCollisions = c.getObstacleCollisions();
		this.totalWallCollisions = c.getTotalWallCollisions();
		this.wallCollisions = c.getWallCollisions();
		this.mineCollisions = c.getMineCollisions();
		this.totalMineCollisions = c.getTotalMineCollisions();
		this.projectileCollisions = c.getProjectileCollisions();
		this.totalProjectileCollisions = c.getTotalProjectileCollisions();
	}
}
