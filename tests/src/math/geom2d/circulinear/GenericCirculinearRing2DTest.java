package math.geom2d.circulinear;

import static java.lang.Math.PI;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.curve.Curve2D;
import junit.framework.TestCase;

public class GenericCirculinearRing2DTest extends TestCase {

	public void testClipBox2D_Astroid() {

		Point2D center = new Point2D(200, 200);
		double radius = 100;

		Point2D c1 = center.translate(radius, radius);
		CircleArc2D arc1 = new CircleArc2D(c1, 100, 3*PI/2, -PI/2);
		Point2D c2 = center.translate(-radius, radius);
		CircleArc2D arc2 = new CircleArc2D(c2, 100, 0, -PI/2);
		Point2D c3 = center.translate(-radius, -radius);
		CircleArc2D arc3 = new CircleArc2D(c3, 100, PI/2, -PI/2);
		Point2D c4 = center.translate(radius, -radius);
		CircleArc2D arc4 = new CircleArc2D(c4, 100, PI, -PI/2);

		// create a poly circulinear curve
		GenericCirculinearRing2D ring = new GenericCirculinearRing2D(arc1, arc2, arc3, arc4);

		Box2D box = new Box2D(50, 350, 50, 350);
		Curve2D clipped = ring.clip(box);
		
		assertEquals(1, clipped.getContinuousCurves().size());	
	}

	public void testIsInsidePoint2D_Astroid() {

		Point2D center = new Point2D(200, 200);
		double radius = 100;

		Point2D c1 = center.translate(radius, radius);
		CircleArc2D arc1 = new CircleArc2D(c1, 100, 3*PI/2, -PI/2);
		Point2D c2 = center.translate(-radius, radius);
		CircleArc2D arc2 = new CircleArc2D(c2, 100, 0, -PI/2);
		Point2D c3 = center.translate(-radius, -radius);
		CircleArc2D arc3 = new CircleArc2D(c3, 100, PI/2, -PI/2);
		Point2D c4 = center.translate(radius, -radius);
		CircleArc2D arc4 = new CircleArc2D(c4, 100, PI, -PI/2);

		// create a poly circulinear curve
		GenericCirculinearRing2D ring = new GenericCirculinearRing2D(arc1, arc2, arc3, arc4);

		Point2D point = new Point2D(50, 50);
		assertFalse(ring.isInside(point));
		
		point = new Point2D(220, 210);
		assertTrue(ring.isInside(point));
		
	}
}
