package math.geom3d;

import junit.framework.TestCase;

public class Vector3DTest extends TestCase {

	public void testDotProduct() {
		Vector3D v1 = new Vector3D(1, 0, 0);
		Vector3D v2 = new Vector3D(1, 0, 0);
		Vector3D v3 = new Vector3D(0, 1, 0);

		assertEquals(Vector3D.dotProduct(v1, v2), 1, 1e-14);
		assertEquals(Vector3D.dotProduct(v1, v3), 0, 1e-14);
	}

	public void testCrossProduct() {
		Vector3D v1 = new Vector3D(1, 0, 0);
		Vector3D v2 = new Vector3D(0, 1, 0);
		Vector3D v3 = new Vector3D(0, 0, 1);

		assertTrue(v3.equals(Vector3D.crossProduct(v1, v2)));
	}

	public void testIsColinear() {
		Vector3D v1 = new Vector3D(1, 2, 3);
		Vector3D v2 = new Vector3D(1.5, 3, 4.5);
		assertTrue(Vector3D.isColinear(v1, v2));
	}

	public void testIsOrthogonal() {
		Vector3D v1 = new Vector3D(1, 0, 0);
		Vector3D v2 = new Vector3D(0, 1, 0);
		Vector3D v3 = new Vector3D(0, 0, 1);
		
		assertTrue(Vector3D.isOrthogonal(v1, v2));
		assertTrue(Vector3D.isOrthogonal(v1, v3));
		assertTrue(Vector3D.isOrthogonal(v2, v3));
	}

	public void testPlus() {
		Vector3D v1 = new Vector3D(1, 2, 3);
		Vector3D v2 = new Vector3D(4, 5, 6);
		Vector3D v3 = new Vector3D(5, 7, 9);
		assertTrue(v1.plus(v2).equals(v3));
	}

	public void testMinus() {
		Vector3D v1 = new Vector3D(1, 2, 3);
		Vector3D v2 = new Vector3D(4, 5, 6);
		Vector3D v3 = new Vector3D(-3, -3, -3);
		assertTrue(v1.minus(v2).equals(v3));
	}

	public void testTimes() {
		Vector3D v1 = new Vector3D(1, 2, 3);
		Vector3D v2 = new Vector3D(1.5, 3, 4.5);
		assertTrue(v1.times(1.5).equals(v2));
	}

	public void testGetNorm() {
		Vector3D v1 = new Vector3D(1, 2, 3);
		assertEquals(v1.getNorm(), Math.sqrt(14), 1e-14);
	}

}
