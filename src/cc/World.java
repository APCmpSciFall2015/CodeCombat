package cc;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import bots.TestBot;
import cc.Main.GameState;
import lib.Vector2;

/**
 * The world class represents a plane of existence for sprites.
 * @author Josh, Matt, Jack, Abe, Robert
 * @version 0.1
 * @see Sprite
 */
public class World
{
	/** size of world **/
	private Vector2 size;
	/** ticks executed **/
	private long ticks = 0;
	/** sprites in the plane of existence **/
	private ArrayList<Sprite> sprites;
	/** Host mainApplet **/
	private MainApplet mainApplet;

	public static enum SpriteType
	{
		CIRCLE, OBSTACLE, PROJECTILE, SHIELD
	}

	// Constructors
	// ---------------------------------------------------------

	/**
	 * 2-Argument World Constructor
	 * @param mainApplet host mainApplet
	 * @param size size of world
	 */
	public World(MainApplet mainApplet, Vector2 size)
	{
		this.mainApplet = mainApplet; // shallow copy
		this.size = size.copy();

		// initialize game objects
		sprites = new ArrayList<Sprite>();
		int numberOfObstacles = 5;//(int) (Math.random() * 20 + 30);
		for (int i = 0; i < numberOfObstacles; i++)
		{
			spawn(SpriteType.OBSTACLE);
		}

		for (int i = 0; i < 5; i++)
		{
			spawn(SpriteType.SHIELD);
			spawn(SpriteType.CIRCLE);
		}
		((Circle) sprites.get(sprites.size() - 1)).setMind(new TestBot(((Circle) sprites.get(sprites.size() - 1)), new Random()));
	}

	/**
	 * spawns a circle in the world
	 */
	public void spawn(SpriteType type)
	{
		Sprite s;

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
		default:
			System.err.println("Invalid Sprite Type");
			return;
		}

		do
		{
			// correct position to avoid collision
			// @formatter:off 
			s.setPosition(new Vector2(
					(float) (Math.random() * (getSize().getX() - s.getSize().getX()) + s.getSize().getX() / 2),
					(float) (Math.random() * (getSize().getY() - s.getSize().getY()) + s.getSize().getY() / 2)
					));
			// @formatter:on
		} while (colliding(s));

		sprites.add(s);
	}

	/**
	 * respawns a circle in the world in a random location
	 * @param respawn the circle to respawn
	 */
	public void respawn(Sprite s)
	{
		do
		{
			s.setPosition(new Vector2(
					(float) ((Math.random() * (size.getX()) - (s.getSize().getX()))) + s.getSize().getX() / 2,
					(float) (((Math.random() * (size.getY()) - (s.getSize().getY()))) + s.getSize().getY() / 2)));
		} while (colliding(s));
		s.setAlive(true);
		;
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

	public ArrayList<Sprite> requestInView(Vector2 position, Vector2 face, float fieldOfView)
	{
		ArrayList<Sprite> observed = new ArrayList<Sprite>();
		Vector2 direction;
		for (Sprite s : sprites)
		{
			direction = s.getPosition().sub(position);
			// angle between vectors: theta = acos (a dot b / (mag a * mag b)
			// https://en.wikipedia.org/wiki/Dot_product
			if (s.getExistence() && s.isAlive() && Math.abs(Math.acos(direction.dot(face) / (face.mag() * direction.mag()))) < Math.abs(fieldOfView / 2))
				observed.add(s.copy());
		}
		return observed;
	}

	/**
	 * The paint method paints the world and all its sprites
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
	public void generateObject(Sprite s)
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
	 * Checks to see if a sprite will collide with any other sprites in the
	 * world using an Axis-Aligned Bounding-Box
	 * @param sprite the sprite to check against the world
	 * @return true, if there is a collision
	 */
	public boolean colliding(Sprite sprite)
	{
		for (Sprite s : sprites)
		{
			if (colliding(s, sprite))
				return true;
		}
		return false;
	}

	/**
	 * The colliding method checks if 2 sprites are colliding using an
	 * Axis-Aligned Bounding-Box
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

	protected MainApplet getMainApplet()
	{
		return mainApplet;
	}

	protected void setMainApplet(MainApplet mainApplet)
	{
		this.mainApplet = mainApplet;
	}

	public Vector2 getSize()
	{
		return size.copy();
	}

	public ArrayList<Sprite> getSprites()
	{
		return sprites;
	}

	public void setSprites(ArrayList<Sprite> sprites)
	{
		this.sprites = sprites;
	}
	
	public long getTicks()
	{
		return ticks;
	}

	public void setTicks(long ticks)
	{
		this.ticks = ticks;
	}
}