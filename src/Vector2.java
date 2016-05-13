
public class Vector2
{
	float x;
	float y;

	// Constructors
	// ---------------------------------------------

	public Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2(float magnitude, float angle, boolean isVector)
	{
		this.x = (float) Math.cos(angle);
		this.y = (float) Math.sin(angle);
	}
	
	public Vector2(Vector2 v)
	{
		this.x = v.x;
		this.y = v.y;
	}

	// Instance methods
	// -----------------------------------------------

	public float angle()
	{
		return (float) Math.atan2(y, x);
	}
	
	public void add(Vector2 v)
	{
		this.x += v.x;
		this.y += v.y;
	}

	public void sub(Vector2 v)
	{
		this.x -= v.x;
		this.y -= v.y;
	}

	public void mult(Vector2 v)
	{
		this.x *= v.x;
		this.y *= v.y;
	}

	public void div(Vector2 v)
	{
		this.x /= v.x;
		this.y /= v.y;
	}

	public float cross(Vector2 v)
	{
		return this.x * v.y + this.y * v.x;
	}

	public float dot(Vector2 v)
	{
		return this.x * v.x + this.y * v.y;
	}

	public void normalize()
	{
		float theta = angle();
		this.x = (float) Math.cos(theta);
		this.y = (float) Math.sin(theta);
	}

	// Static methods
	// ----------------------------------------------------------

	public static float cross(Vector2 v1, Vector2 v2)
	{
		return v1.x * v2.y + v1.y * v2.x;
	}

	public static float dot(Vector2 v1, Vector2 v2)
	{
		return v1.x * v2.x + v1.y * v2.y;
	}

	public static Vector2 add(Vector2 v1, Vector2 v2)
	{
		return new Vector2(v1.x + v2.x, v1.y + v2.y);
	}

	public static Vector2 sub(Vector2 v1, Vector2 v2)
	{
		return new Vector2(v1.x - v2.x, v1.y - v2.y);
	}

	public static Vector2 mult(Vector2 v1, Vector2 v2)
	{
		return new Vector2(v1.x * v2.x, v1.y * v2.y);
	}

	public static Vector2 div(Vector2 v1, Vector2 v2)
	{
		return new Vector2(v1.x / v2.x, v1.y / v2.y);
	}

	// Getters and Setters
	// ----------------------------------------

	public void setX(float x)
	{
		this.x = x;
	}

	public void setY(float y)
	{
		this.y = y;
	}
}
