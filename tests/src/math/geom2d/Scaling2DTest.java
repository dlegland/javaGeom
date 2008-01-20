package math.geom2d;

import junit.framework.TestCase;

public class Scaling2DTest extends TestCase {

	public void testScaling2DPoint2DDoubleDouble() {
		Scaling2D scaling1 = new Scaling2D(2, 3);
		assertTrue(scaling1 instanceof AffineTransform2D);
		Scaling2D scaling2 = new Scaling2D(new Point2D(3, 4), 2, 3);
		Translation2D trans = new Translation2D(3, 4);
		AffineTransform2D compose = trans;
		compose = compose.compose(scaling1);
		compose = compose.compose(trans.getInverseTransform());
			
		assertTrue(compose.equals(scaling2));
	}

	public void testGetInverseTransform() {
		Scaling2D scaling = new Scaling2D(new Point2D(3, 4), 2, 3);
		AffineTransform2D inverse = scaling.getInverseTransform();
		AffineTransform2D compose = inverse.compose(scaling);
		assertTrue(AbstractAffineTransform2D.isIdentity(compose));
	}

}
