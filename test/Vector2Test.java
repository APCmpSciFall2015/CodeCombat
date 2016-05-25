import static org.junit.Assert.*;

import org.junit.Test;

import lib.Vector2;

public class Vector2Test
{	
	@Test
	public void testNormalize()
	{
		Vector2 v = new Vector2(5, 5);
		v.normalize();
		assertEquals(v.mag(), 1, 0.1);
		assertEquals(v.getX(), Math.sqrt(2) / 2.0, 0.001);
		assertEquals(v.getY(), Math.sqrt(2) / 2.0, 0.001);
	}
	
	@Test
	public void testNormalizeStatic()
	{
		Vector2 v = new Vector2(5, 5);
		assertTrue(Vector2.normalize(v).equals(new Vector2((float) Math.sqrt(2) / 2, (float) Math.sqrt(2) / 2)));
	}
	
	@Test
	public void testAdd()
	{
		Vector2 v = new Vector2(1, 1);
		v.add(v);
		assertEquals(v.getX(), 2, 0.001);
		assertEquals(v.getY(), 2, 0.001);
	}
	
	@Test
	public void testAddStatic()
	{
		Vector2 v = new Vector2(1, 1);
		assertTrue(Vector2.add(v, v).equals(new Vector2(2, 2)));
	}
	
	@Test
	public void testSub()
	{
		Vector2 v = new Vector2(1, 1);
		v.sub(v);
		assertEquals(v.getX(), 0, 0.001);
		assertEquals(v.getY(), 0, 0.001);
	}
	
	@Test
	public void testSubStatic()
	{
		Vector2 v = new Vector2(1, 1);
		assertTrue(Vector2.sub(v, v).equals(new Vector2(0, 0)));
	}
	
	@Test
	public void testMult()
	{
		Vector2 v = new Vector2(1, 1);
		v.mult(2);
		assertEquals(v.getX(), 2, 0.001);
		assertEquals(v.getY(), 2, 0.001);
	}
	
	@Test
	public void testMultStatic()
	{
		Vector2 v = new Vector2(1, 1);
		assertTrue(Vector2.mult(v, 2).equals(new Vector2(2, 2)));
	}
	
	@Test
	public void testDiv()
	{
		Vector2 v = new Vector2(1, 1);
		v.div(2);
		assertEquals(v.getX(), .5, 0.001);
		assertEquals(v.getY(), .5, 0.001);
	}
	
	@Test
	public void testDivStatic()
	{
		Vector2 v = new Vector2(1, 1);
		assertTrue(Vector2.div(v, 2).equals(new Vector2(.5f, .5f)));
	}
	
	@Test
	public void testAngle()
	{
		Vector2 v = new Vector2(1, 1);
		assertEquals(v.angle(), Math.PI / 4.0, .001);
	}
	
	@Test
	public void testMag()
	{
		Vector2 v = new Vector2(1,1);
		assertEquals(v.mag(), Math.sqrt(2), .001);
	}
	
	@Test
	public void testMagSq()
	{
		Vector2 v = new Vector2(1, 1);
		assertEquals(v.magSq(), 2, .001);
	}
	
	@Test
	public void testRotate()
	{
		Vector2 v = new Vector2(1, 2);
		v.rotate((float) Math.PI / 2f);
		assertTrue(v.equals(new Vector2(-2, 1)));
	}
	
	@Test
	public void testRotateStatic()
	{
		Vector2 v = new Vector2(1, 1);
		assertTrue(Vector2.rotate(v, (float) Math.PI / 2).equals(new Vector2(-1, 1)));
	}
	
	@Test
	public void testLerp()
	{
		Vector2 v1 = new Vector2(-1,1);
		Vector2 v2 = new Vector2(1, 1);
		v1.lerp(v2, .5f);
		assertTrue(v1.equals(new Vector2(0, 1)));
	}
	
	@Test
	public void testLerpStatic()
	{
		Vector2 v1 = new Vector2(1, 1);
		Vector2 v2 = new Vector2(-1, 1);
		assertTrue(Vector2.lerp(v1, v2, .5f).equals(new Vector2(0, 1)));
	}
	
	@Test
	public void testCross()
	{
		Vector2 v1 = new Vector2(2, 1);
		Vector2 v2 = new Vector2(1, 2);
		assertEquals(v1.cross(v2), 5, 0.001);
	}
	
	@Test
	public void testDot()
	{
		Vector2 v1 = new Vector2(2, 1);
		Vector2 v2 = new Vector2(1, 2);
		assertEquals(v1.dot(v2), 4, 0.001);
	}
	
	@Test
	public void testAngleBetween()
	{
		Vector2 v1 = new Vector2(1, 0);
		Vector2 v2 = new Vector2(0,1);
		assertEquals(v1.angleBetween(v2), Math.PI / 2.0f, .001);
	}

	@Test
	public void testDist()
	{
		Vector2 v1 = new Vector2(1, 1);
		Vector2 v2 = new Vector2(2, 2);
		assertEquals(v1.dist(v2), Math.sqrt(2), .001);
	}
}
