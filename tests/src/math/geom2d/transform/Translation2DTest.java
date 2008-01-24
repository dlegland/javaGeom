package math.geom2d.transform;

import junit.framework.TestCase;

public class Translation2DTest extends TestCase {

	/**
	 * Checks that Translation2D returns true.
	 */
	public void testIsMotion() {
		Translation2D trans = new Translation2D(3, 5);
		assertTrue(trans.isMotion());
	}

	/**
	 * Checks that Translation2D returns true.
	 */
	public void testIsDirect() {
		Translation2D trans = new Translation2D(3, 5);
		assertTrue(trans.isDirect());
	}

	/**
	 * Check the inverse of a translation (another translation).
	 */
	public void testGetInverseTransform() {
		Translation2D trans = new Translation2D(3, 5);
		Translation2D inv = new Translation2D(-3, -5);
		assertTrue(inv.equals(trans.getInverseTransform()));
	}

	/**
	 * checks that the composition of two Translation2D is an instance of
	 * Translation2D.
	 */
	public void testCompose() {
		Translation2D trans1 = new Translation2D(2, 3);
		Translation2D trans2 = new Translation2D(4, 5);
		AffineTransform2D comp = trans1.compose(trans2);
		assertTrue(comp instanceof Translation2D);
	}

}
