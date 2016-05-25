package cc;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

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
	/** sprites in the plane of existence **/
	private ArrayList<Sprite> sprites;
	/** Host applet **/
	private Main main;

	/**
	 * 1-Argument World Constructor
	 * @param main host applet
	 */
	public World(Main main)
	{
		this.main = main; // shallow copy

		// initialize game objects
		sprites = new ArrayList<Sprite>();
		do
		{
			sprites = new ArrayList<Sprite>();
			int numberOfObstacles = (int) (Math.random() * 5 + 6);
			for (int i = 0; i < numberOfObstacles; i++)
			{
				Vector2 size = new Vector2(0, 0);
				switch ((int) (Math.random() * 3))
				{
				case 0:
					size = new Vector2(10, 60);
					break;
				case 1:
					size = new Vector2(60, 10);
					break;
				case 2:
					size = new Vector2(30, 30);
					break;
				default:
					break;
				}
				Vector2 position = new Vector2(
						(int) (Math.random() * (main.getWidth() - size.getX()) + size.getX() / 2),
						(int) (Math.random() * (main.getHeight() - size.getY()) + size.getY() / 2));
				sprites.add(new Obstacle(size, position, new Color(0, 0, 0), this));
				if (Main.DEBUG)
					System.out.println("Obstacle made");
			}
			if (Main.DEBUG)
				System.out.println("remaking");
		} while (checkCollisions());
		if (Main.DEBUG)
			System.out.println("Done");
		// generate test Circles
		sprites.add(new Circle(200, 300, Color.BLUE, this));
		sprites.add(new Circle(400, 300, Color.RED, this));

		// test requestInView method
		// System.out.println(requestInView(sprites.get(sprites.size() -
		// 2).getPosition(), sprites.get(sprites.size() - 2).getVelocity(),
		// (float) Math.PI / 2));
		// main.setGameState(GameState.PAUSED);
	}

	/**
	 * The update method updates the state of the world and all its sprites.
	 */
	public void update()
	{
		for (int i = sprites.size() - 1; i >= 0; i--)
		{
			if (sprites.get(i).isAlive())
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
				if (colliding(sprites.get(i), sprites.get(j)))
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
	 * The colliding method checks if 2 sprites are colliding using an
	 * Axis-Aligned Bounding-Box
	 * @param A 1st sprite
	 * @param B 2nd sprite
	 * @return are the sprites colliding
	 */
	public boolean colliding(Sprite A, Sprite B)
	{
		// @formatter:off
		return colliding(A.getPosition().getX() - A.getSize().getX() / 2, A.getPosition().getY() - A.getSize().getY() / 2,
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

	public Vector2 getSize()
	{
		return new Vector2(main.getWidth(), main.getHeight());
	}
}
