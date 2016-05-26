package cc;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

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
	/** sprites in the plane of existence **/
	private ArrayList<Sprite> sprites;
	/** Host mainApplet **/
	private MainApplet mainApplet;
	
	public static enum SpriteType
	{
		CIRCLE, OBSTACLE, PROJECTILE
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
		int numberOfObstacles = (int) (Math.random() * 20 + 30);
		for(int x = 0; x < numberOfObstacles; x++)
		{
			spawn(SpriteType.OBSTACLE);
		}
		
		for(int x = 0; x < 5; x++)
		{
			spawn(SpriteType.CIRCLE);
		}



		// test requestInView method
		// System.out.println(requestInView(sprites.get(sprites.size() -
		// 2).getPosition(), sprites.get(sprites.size() - 2).getVelocity(),
		// (float) Math.PI / 2));
		// mainApplet.setGameState(GameState.PAUSED);
	}

	/**
	 * spawns a circle in the world
	 */
	public void spawn(SpriteType type)
	{
		boolean willCollide = false;
		Sprite s = new Circle(0, 0, this);
		switch(type)
		{
		case CIRCLE:
			s = new Circle(0, 0, this);
			break;
		case OBSTACLE:
			Vector2 spriteSize = new Vector2(0, 0);
			Vector2 position = new Vector2(0, 0);
			switch ((int) (Math.random() * 3))
			{
			case 0:
				spriteSize = new Vector2(10, 60);
				break;
			case 1:
				spriteSize = new Vector2(60, 10);
				break;
			case 2:
				spriteSize = new Vector2(30, 30);
				break;
			default:
				break;
			}
			s = new Obstacle(spriteSize, position, new Color(0, 0, 0), this);
			break;
			
			default:	
			break;
		}
		do 
		{
			int x = (int) (Math.random() * (getSize().getX() + 1));
			int y = (int) (Math.random() * (getSize().getY() + 1));
			Vector2 position = new Vector2(x, y);
			s.setPosition(position);
			willCollide = colliding(s);
		} while(willCollide);
		sprites.add(s);
	}

	/**
	 * respawns a circle in the world in a random location
	 * @param respawn the circle to respawn
	 */
	public void spawn(Sprite respawn)
	{
		boolean willCollide = false;
		do
		{
			int x = (int) Math.floor(Math.random() * (getSize().getX() + 1));
			int y = (int) Math.floor(Math.random() * (getSize().getY() + 1));
			Vector2 position = new Vector2(x, y);
			respawn.setPosition(position);
			willCollide = colliding(respawn);
		}while(willCollide);
		respawn.setAlive(true);
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
			else
				sprites.remove(i);
		}
		checkCollisions();
	}

	public ArrayList<Sprite> requestInView(Vector2 position, Vector2 face, float fieldOfView)
	{
		ArrayList<Sprite> observed = new ArrayList<Sprite>();
		for (Sprite s : sprites)
		{
			if (Math.abs(face.angle() - Vector2.sub(s.getPosition(), position).angle()) < Math.abs(fieldOfView / 2))
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
	 * Checks to see if a sprite will collide with any other sprites in the world using an
	 * Axis-Aligned Bounding-Box
	 * @param sprite the sprite to check against the world
	 * @return true, if there is a collision
	 */
	public boolean colliding(Sprite sprite)
	{
		int count = 0;
		boolean willCollide = false;
		while(count < sprites.size() && !willCollide)
		{
			if(colliding(sprites.get(count), sprite))
			{
				willCollide = true;
			}
			count++;
		}
		return willCollide;
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
		if(A == B) //if the sprites are the same
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
}

