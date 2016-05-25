package lib;

/**
 * The Vector2 class represents a 2x1 matrix vector and provides common
 * operations on said object. Some design notes:
 * <ul>
 * <li>The cross product is returned as a float, not a 3rd dimensional vector
 * despite that it represents a vector orthogonal to any 2 input vectors.</li>
 * <li>Angles are not stored as fields; all angles are calculated when needed.
 * </li>
 * </ul>
 * @author Robert Meade
 * @version 0.1
 */
public class Vector2
{
	/** epsilon value for floating point comparison **/
	public static final float EPSILON = .001f;
	/** x vector component **/
	private float x;
	/** y vector component **/
	private float y;

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
		if (!isPolar)
			throw new IllegalArgumentException(); // idiot proof
		this.x = (float) Math.cos(angle) * magnitude;
		this.y = (float) Math.sin(angle) * magnitude;
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

	// Overridden methods
	// ---------------------------------------------

	@Override
	public String toString()
	{
		return "[" + this.x + ", " + this.y + "]";
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof Vector2 && this.x < ((Vector2) o).x + EPSILON && this.x > ((Vector2) o).x - EPSILON
				&& this.y < ((Vector2) o).y + EPSILON && this.y > ((Vector2) o).y - EPSILON;
	}

	// Instance methods
	// -----------------------------------------------

	/**
	 * The copy method returns a deep copy of this vector.
	 * @return copy of vector
	 */
	public Vector2 copy()
	{
		return new Vector2(this);
	}

	/**
	 * The array method gets the vector and returns it as a float array.
	 * @return float array
	 */
	public float[] array()
	{
		return new float[] { x, y };
	}

	/**
	 * The dist method returns the distance between this vector and another
	 * vector
	 * @param v vector to find distance to
	 * @return distance between vectors
	 */
	public float dist(Vector2 v)
	{
		return (float) Math.sqrt((v.x - x) * (v.x - x) + (v.y - y) * (v.y - y));
	}

	/**
	 * The mag method returns the magnitude of the vector as a scalar.
	 * @return magnitude of vector
	 */
	public float mag()
	{
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * The magSq method returns the magnitude of the vector squared as a scalar.
	 * @return magnitude of vector
	 */
	public float magSq()
	{
		return x * x + y * y;
	}

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
	 * The angleBetween method returns the angle between this vector and another
	 * in radians.
	 * @param v vector to find angle with
	 * @return angle between vectors
	 */
	public float angleBetween(Vector2 v)
	{
		return (float) Math.acos(dot(v) / (v.mag() * mag()));
	}

	/**
	 * The rotate method rotates this vector by a given angle deltaTheta.
	 * @param deltaTheta angle to rotate by  
	 * @return reference to self
	 */
	public Vector2 rotate(float deltaTheta)
	{
		float theta = angle() + deltaTheta;
		float mag = mag();
		this.x = mag * (float) Math.cos(theta);
		this.y = mag * (float) Math.sin(theta);
		return this;
	}

	/**
	 * The add method adds another vector to this vector.
	 * @param v vector to add
	 * @return reference to self
	 */
	public Vector2 add(Vector2 v)
	{
		this.x += v.x;
		this.y += v.y;
		return this;
	}

	/**
	 * The sub method subtracts another vector from this vector.
	 * @param v vector
	 *  @return reference to self
	 */
	public Vector2 sub(Vector2 v)
	{
		this.x -= v.x;
		this.y -= v.y;
		return this;
	}

	/**
	 * The mult method multiplies this vector by another vector
	 * @param n scalar to multiply by
	 *  @return reference to self
	 */
	public Vector2 mult(float n)
	{
		this.x *= n;
		this.y *= n;
		return this;
	}

	/**
	 * The div method divides this vector by a scalar.
	 * @param n scalar to divide by
	 *  @return reference to self
	 */
	public Vector2 div(float n)
	{
		this.x /= n;
		this.y /= n;
		return this;
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
		return x * v.y + y * v.x;
	}

	/**
	 * The dot method returns the dot product of this and another vector.
	 * @param v vector to compute dot product with
	 * @return dot product
	 */
	public float dot(Vector2 v)
	{
		return x * v.x + y * v.y;
	}

	/**
	 * The normalize method converts the magnitude of this vector to 1, making
	 * it a unit vector in its given direction.
	 *  @return reference to self
	 */
	public Vector2 normalize()
	{
		float theta = angle();
		this.x = (float) Math.cos(theta);
		this.y = (float) Math.sin(theta);
		return this;
	}

	/**
	 * The lerp method computes the linear interpolation of this vector and
	 * another vector and sets it equal to this vector
	 * @param v vector
	 * @param alpha alpha value
	 * @return reference to self
	 */
	public Vector2 lerp(Vector2 v, float alpha)
	{
		this.x *= (1 - alpha) * x + v.x * alpha;
		this.y *= (1 - alpha) * y + v.y * alpha;
		return this;
	}

	// Static methods
	// ----------------------------------------------------------

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

	/**
	 * The normalize method converts the magnitude of this vector to 1, making
	 * it a unit vector in its given direction.
	 * @param v vector
	 * @return normalized vector
	 */
	public static Vector2 normalize(Vector2 v)
	{
		float theta = v.angle();
		return new Vector2((float) Math.cos(theta), (float) Math.sin(theta));
	}

	/**
	 * The lerp method computes the linear interpolation of this vector and
	 * another as a new vector
	 * @param v1 1st vector
	 * @param v2 2nd vector
	 * @param alpha alpha value
	 * @return linear interpolation of v1 and v2
	 */
	public static Vector2 lerp(Vector2 v1, Vector2 v2, float alpha)
	{
		return new Vector2((1 - alpha) * v1.x + v2.x * alpha, (1 - alpha) * v1.y + v2.y * alpha);
	}

	/**
	 * The rotate method returns a vector rotated from the input vector by an
	 * angle deltaTheta.
	 * @param v vector
	 * @param deltaTheta angle to rotate by
	 * @return rotated vector
	 */
	public static Vector2 rotate(Vector2 v, float deltaTheta)
	{
		float theta = v.angle() + deltaTheta;
		float mag = v.mag();
		return new Vector2(mag * (float) Math.cos(theta), mag * (float) Math.sin(theta));
	}

	// Getters and Setters
	// ----------------------------------------

	/**
	 * The setMag method sets the magnitude of the vector (adjusts x and y
	 * accordingly)
	 * @param mag magnitude
	 * @return reference to self
	 */
	public Vector2 setMag(float mag)
	{
		float theta = angle();
		this.x = mag * (float) Math.cos(theta);
		this.y = mag * (float) Math.sin(theta);
		return this;
	}

	/**
	 * The setX method sets the x component of this vector
	 * @param x x vector component
	 * @return reference to self
	 */
	public Vector2 setX(float x)
	{
		this.x = x;
		return this;
	}

	/**
	 * The setY method sets the y component of this vector
	 * @param y y vector component
	 * @return reference to self
	 */
	public Vector2 setY(float y)
	{
		this.y = y;
		return this;
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