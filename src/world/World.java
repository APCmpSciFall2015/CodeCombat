package world;

import java.awt.Graphics;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import app.Main;
import app.MainApplet;
import bots.*;
import lib.Vector2;

/**
 * The world class represents a plane of existence for sprites.
 * @author Josh, Matt, Jack, Abe, Robert
 * @version 0.1
 * @see Sprite
 */
public class World
{

	/** variance (standard deviation) of gaussian noise in measurements */
	public static final float NOISE_VARIANCE = Float.parseFloat(Main.CONFIG.get("worldNoiseVariance"));

	/** mean of gaussian noise in measurements */
	public static final float NOISE_MEAN = Float.parseFloat(Main.CONFIG.get("worldNoiseMean"));

	/** Number of obstacles */
	public static final int NUM_OBSTACLES = Integer.parseInt(Main.CONFIG.get("worldNumObstacles"));

	/** Number of Shields */
	public static final int NUM_SHIELDS = Integer.parseInt(Main.CONFIG.get("worldNumShields"));

	/** Maximum amount of circles */
	public static final int MAX_CIRCLES = Integer.parseInt(Main.CONFIG.get("worldMaxCircles"));

	/** Number of mines. */
	public static final int NUM_MINES = Integer.parseInt(Main.CONFIG.get("worldNumMines"));

	// Instance variables
	// ------------------------------------------------

	/** size of world. */
	private Vector2 size;

	/** ticks executed. */
	private long ticks = 0;

	/** sprites in the plane of existence. */
	private ArrayList<Sprite> sprites;

	/** Host mainApplet. */
	private MainApplet mainApplet;

	/**
	 * The Enum SpriteType.
	 */
	public static enum SpriteType
	{
		/** Circle/Bot. */
		CIRCLE,
		/** Obstacle */
		OBSTACLE,
		/** Projectile */
		PROJECTILE,
		/** Shield */
		SHIELD,
		/** mine */
		MINE
	}

	// Constructors
	// ---------------------------------------------------------

	/**
	 * 2-Argument World Constructor.
	 * @param mainApplet host mainApplet
	 * @param size size of world
	 */
	public World(MainApplet mainApplet, Vector2 size)
	{
		this.mainApplet = mainApplet; // shallow copy
		this.size = size.copy();
		init();
	}

	// Functional methods
	// ---------------------------------------

	/**
	 * Initializes the world
	 **/
	public void init()
	{
		long startTime = System.currentTimeMillis();

		// initialize game objects
		sprites = new ArrayList<Sprite>();

		// init circles
		String circleGenLog = "Initialized Circles: {";
		for (int i = 1; i <= MAX_CIRCLES; i++)
		{
			String slotData = Main.GAME_SETTINGS.get("slot" + i);
			if (slotData != null && slotData.length() > 0)
			{
				circleGenLog += slotData + ", ";
				Sprite s = spawn(SpriteType.CIRCLE);
				if (!slotData.equals("null"))
				{
					// use magic (java reflection) to bring life to the circles
					// It's ALIVE!!!
					try
					{
						Class c = Class.forName("bots." + slotData);
						Constructor cons = c.getConstructor(Circle.class, float.class, float.class);
						Mind m = (Mind) cons.newInstance((Circle) s, NOISE_VARIANCE, NOISE_MEAN);
						((Circle) s).setMind(m);
					}
					catch (Exception e)
					{
						System.err.println("ERROR: Failed to instantiate specified class in game_settings");
					}
				}
			}
		}
		// log circle gen
		System.out.println(circleGenLog.substring(0, circleGenLog.length() - 2) + "}");

		// init obstacles, shields, and minds and log
		for (int i = 0; i < NUM_OBSTACLES; i++)
		{
			spawn(SpriteType.OBSTACLE);
		}
		System.out.println("Initialized " + NUM_OBSTACLES + "obstacles");
		for (int i = 0; i < NUM_SHIELDS; i++)
		{
			spawn(SpriteType.SHIELD);
		}
		System.out.println("Initialized " + NUM_SHIELDS + "shields");
		for (int i = 0; i < NUM_MINES; i++)
		{
			spawn(SpriteType.MINE);
		}
		System.out.println("Initialized " + NUM_MINES + "mines");
		System.out.println("#Initialization Complete: took " + (System.currentTimeMillis() - startTime) + "ms#\n");
	}

	/**
	 * Restarts the game.
	 */
	public void restart()
	{
		ticks = 0;
		Sprite.idCount = 0;
		init();
	}

	/**
	 * spawns a new sprite in the world.
	 * @param type the type of sprite to spawn
	 * @return the sprite
	 */
	public Sprite spawn(SpriteType type)
	{
		Sprite s = null;

		switch (type)
		{
		case CIRCLE:
			s = new Circle(this);
			break;
		case OBSTACLE:
			s = new Obstacle(this);
			break;
		case SHIELD:
			s = new Shield(this);
			break;
		case MINE:
			s = new Mine(this);
			break;
		default:
			System.err.println("Invalid Sprite Type");
			return s;
		}
		assignAvailablePosition(s);
		sprites.add(s);
		return s;
	}

	/**
	 * Assigns an area the sprite will not be colliding with on the world.
	 * @param s the sprite to assign the position to
	 */
	public void assignAvailablePosition(Sprite s)
	{
		// correct position to avoid collision
		// @formatter:off 
		s.setPosition(new Vector2(
				(float) (Math.random() * (getSize().getX() - s.getSize().getX()) + s.getSize().getX() / 2),
				(float) (Math.random() * (getSize().getY() - s.getSize().getY()) + s.getSize().getY() / 2)
				));
		s.collide(collidingWith(s));
		s.update();
		// @formatter:on	
	}

	/**
	 * respawns a circle in the world in a random location.
	 * @param s the s
	 */
	public void respawn(Sprite s)
	{
		s.setAlive(true);
		assignAvailablePosition(s);
	}

	/**
	 * The update method updates the state of the world and all its sprites.
	 */
	public void update()
	{
		for (int i = sprites.size() - 1; i >= 0; i--)
		{
			if (sprites.get(i).getExistence())
				sprites.get(i).update();
			else sprites.remove(i);
		}
		checkCollisions();
		ticks++;
	}

	/**
	 * Requests all sprites in the object's Field of View.
	 * @param position the position of the sprite
	 * @param face the position the FOV broadcasts from
	 * @param fieldOfView the Field of View of the sprite
	 * @return an ArrayList containing all sprites within the Field of View
	 */
	public ArrayList<Sprite> requestInView(Vector2 position, Vector2 face, float fieldOfView)
	{
		ArrayList<Sprite> inView = new ArrayList<Sprite>();
		for (Sprite s : sprites)
		{
			Vector2 direction = s.getPosition().sub(position);
			// angle between vectors: theta = acos (a dot b / (mag a * mag b)
			// https://en.wikipedia.org/wiki/Dot_product
			if (s.getExistence() && s.isAlive() && Math
					.abs(Math.acos(direction.dot(face) / (face.mag() * direction.mag()))) < Math.abs(fieldOfView / 2))
				inView.add(s.copy());
		}
		return inView;
	}

	/**
	 * The paint method paints the world and all its sprites.
	 * @param g Graphics to paint on
	 */
	public void paint(Graphics g)
	{
		for (Sprite s : sprites)
		{
			s.paint(g);
		}
	}

	/**
	 * The generateObeject method adds the input sprite to the world.
	 * @param s Sprite to add to world
	 */
	protected void generateObject(Sprite s)
	{
		sprites.add(s);
	}

	/**
	 * The checkCollisions method manages checking for collisions on a given
	 * update.
	 * @return was there a collision?
	 */
	public boolean checkCollisions()
	{
		boolean collisions = false;
		// iterate over combinations and check collisions using AABB
		for (int i = 0; i < sprites.size() - 1; i++)
		{
			for (int j = i + 1; j < sprites.size(); j++)
			{
				if (sprites.get(i).isAlive() && sprites.get(j).isAlive() && colliding(sprites.get(i), sprites.get(j)))
				{
					collisions = true;
					Sprite s = sprites.get(i).copy();
					sprites.get(i).collide(sprites.get(j));
					sprites.get(j).collide(s);
				}
			}
		}
		return collisions;
	}

	/**
	 * checks to see if a sprite is colliding with any other sprite in the
	 * world.
	 * @param sprite the sprite to check against the world
	 * @return the sprite the passed argument is colliding with, or null
	 *         otherwise
	 */
	public Sprite collidingWith(Sprite sprite)
	{
		for (Sprite s : sprites)
		{
			if (colliding(s, sprite))
				return s;
		}
		return null;
	}

	/**
	 * The colliding method checks if 2 sprites are colliding using an
	 * Axis-Aligned Bounding-Box.
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

	// Getters and Setters
	// ---------------------------------------------

	/**
	 * Gets the main applet the world is contained in.
	 * @return the main applet
	 */
	protected MainApplet getMainApplet()
	{
		return mainApplet;
	}

	/**
	 * Sets the main applet the world is contained in.
	 * @param mainApplet the new main applet
	 */
	protected void setMainApplet(MainApplet mainApplet)
	{
		this.mainApplet = mainApplet;
	}

	/**
	 * Gets the size of the world.
	 * @return the size of the world
	 */
	public Vector2 getSize()
	{
		return size.copy();
	}

	/**
	 * Gets the sprites in the world. (shallow copy)
	 * @return the sprites
	 */
	public ArrayList<Sprite> getSprites()
	{
		return sprites;
	}

	/**
	 * Sets the sprites in the world. (shallow copy)
	 * @param sprites the new sprites
	 */
	public void setSprites(ArrayList<Sprite> sprites)
	{
		this.sprites = sprites;
	}

	/**
	 * Gets the ticks.
	 * @return the ticks
	 */
	public long getTicks()
	{
		return ticks;
	}

	/**
	 * Sets the ticks.
	 * @param ticks the new ticks
	 */
	public void setTicks(long ticks)
	{
		this.ticks = ticks;
	}
}