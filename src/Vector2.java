/**
 * The Vector2 class represents a 2x1 matrix vector and provides common
 * operations on said object.
 * <ul>
 * Some design notes:
 * <li>The cross product is returned as a float, not a 3rd dimensional vector
 * despite that it represents a vector orthogonal to any 2 input vectors.</li>
 * <li>Angles are not stored as fields; all angles are calculated when needed.</li>
 * </ul>
 * @author Robert Meade
 * @version 0.1
 */
public class Vector2
{
	/** x vector component **/
	float x;
	/** y vector component **/
	float y;

	// Constructors
	// ---------------------------------------------

	/**
	 * 2-Argument Vector2 Constructor
	 * @param x x vector component
	 * @param y y vector component
	 */
	public Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * 3-Argument Vector2 Constructor
	 * @param magnitude vector magnitude
	 * @param angle vector angle
	 * @param isPolar is this input as a polar coordinate (must be true to use)
	 */
	public Vector2(float magnitude, float angle, boolean isPolar) throws IllegalArgumentException
	{
		if(!isPolar) throw new IllegalArgumentException(); // idiot proof
		this.x = (float) Math.cos(angle);
		this.y = (float) Math.sin(angle);
	}

	/**
	 * Vector2 Copy Constructor
	 * @param v vector to copy
	 */
	public Vector2(Vector2 v)
	{
		this.x = v.x;
		this.y = v.y;
	}

	// Instance methods
	// -----------------------------------------------

	/**
	 * The angle method returns the angle formed between the x and y components
	 * of the vector in radians
	 * @return angle in radians
	 */
	public float angle()
	{
		return (float) Math.atan2(y, x);
	}

	/**
	 * The add method adds another vector to this vector.
	 * @param v vector to add
	 */
	public void add(Vector2 v)
	{
		this.x += v.x;
		this.y += v.y;
	}

	/**
	 * The sub method subtracts another vector from this vector.
	 * @param v vector
	 */
	public void sub(Vector2 v)
	{
		this.x -= v.x;
		this.y -= v.y;
	}

	/**
	 * The mult method multiplies this vector by another vector
	 * @param n scalar to multiply by
	 */
	public void mult(float n)
	{
		this.x *= n;
		this.y *= n;
	}

	/**
	 * The div method divides this vector by a scalar.
	 * @param n scalar to divide by
	 */
	public void div(float n)
	{
		this.x /= n;
		this.y /= n;
	}

	/**
	 * The cross method returns the cross product of this and another vector as
	 * a float. This value is indicative of a vector orthogonal to the 2 vectors
	 * involved in its computation but is not returned as a 3rd dimensional
	 * vector object.
	 * @param v vector to compute cross product with
	 * @return cross product as float
	 */
	public float cross(Vector2 v)
	{
		return this.x * v.y + this.y * v.x;
	}

	/**
	 * The dot method returns the dot product of this and another vector.
	 * @param v vector to compute dot product with
	 * @return dot product
	 */
	public float dot(Vector2 v)
	{
		return this.x * v.x + this.y * v.y;
	}

	/**
	 * The normalize method converts the magnitude of this vector to 1, making
	 * it a unit vector in its given direction.
	 */
	public void normalize()
	{
		float theta = angle();
		this.x = (float) Math.cos(theta);
		this.y = (float) Math.sin(theta);
	}

	// Static methods
	// ----------------------------------------------------------
	
	/**
	 * The cross method returns the cross product of 2 vectors as
	 * a float. This value is indicative of a vector orthogonal to the 2 vectors
	 * involved in its computation but is not returned as a 3rd dimensional
	 * vector object.
	 * @param v1 1st vector in computation
	 * @param v2 2nd vector in computation
	 * @return cross product as float
	 */
	public static float cross(Vector2 v1, Vector2 v2)
	{
		return v1.x * v2.y + v1.y * v2.x;
	}

	/**
	 * The dot method returns the dot product of 2 vectors.
	 * @param v1 1st vector in computation
	 * @param v2 2nd vector in computation
	 * @return dot product
	 */
	public static float dot(Vector2 v1, Vector2 v2)
	{
		return v1.x * v2.x + v1.y * v2.y;
	}

	/**
	 * The add method adds 2 vectors and produces a new vector.
	 * @param v1 1st vector
	 * @param v2 2nd vector
	 * @return vector sum
	 */
	public static Vector2 add(Vector2 v1, Vector2 v2)
	{
		return new Vector2(v1.x + v2.x, v1.y + v2.y);
	}

	/**
	 * The sub method subtracts two vectors (v1 - v2) and return a new vector.
	 * @param v1 1st vector
	 * @param v2 2nd vector
	 * @return vector difference
	 */
	public static Vector2 sub(Vector2 v1, Vector2 v2)
	{
		return new Vector2(v1.x - v2.x, v1.y - v2.y);
	}

	/**
	 * The mult method multiplies a vector by a scalar and returns a new vector.
	 * @param v vector
	 * @param n scalar to multiply by
	 * @return vector product
	 */
	public static Vector2 mult(Vector2 v, float n)
	{
		return new Vector2(v.x * n, v.y * n);
	}

	/**
	 * The div method multiplies a vector by a scalar and returns a new vector.
	 * @param v vector
	 * @param n scalar to multiply by
	 * @return vector quotient
	 */
	public static Vector2 div(Vector2 v, float n)
	{
		return new Vector2(v.x / n, v.y / n);
	}

	// Getters and Setters
	// ----------------------------------------

	/**
	 * The setX method sets the x component of this vector
	 * @param x x vector component
	 */
	public void setX(float x)
	{
		this.x = x;
	}

	/**
	 * The setY method sets the y component of this vector
	 * @param y y vector component
	 */
	public void setY(float y)
	{
		this.y = y;
	}

	/**
	 * The getX method gets the x component of this vector
	 * @return x vector component
	 */
	public float getX()
	{
		return x;
	}

	/**
	 * The getY method gets the y component of this vector
	 * @return y vector component
	 */
	public float getY()
	{
		return y;
	}
}
