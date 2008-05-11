package math.geom3d.transform;

import junit.framework.TestCase;

public class AffineTransform3DTest extends TestCase {

	public void testGetInverseTransform() {
		double tx = 2;
		double ty = 3;
		double tz = 4;
		AffineTransform3D T234 = new AffineTransform3D(1, 0, 0, tx, 0, 1, 0, ty, 0, 0, 1, tz);
		AffineTransform3D inv = (AffineTransform3D)T234.getInverseTransform();
		AffineTransform3D T234m = new AffineTransform3D(1, 0, 0, -tx, 0, 1, 0, -ty, 0, 0, 1, -tz);
		assertTrue(inv.equals(T234m));
	}

}
