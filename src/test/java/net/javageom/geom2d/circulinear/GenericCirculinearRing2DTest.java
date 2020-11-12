package net.javageom.geom2d.circulinear;

import static java.lang.Math.PI;

import junit.framework.TestCase;
import net.javageom.geom2d.Box2D;
import net.javageom.geom2d.Point2D;
import net.javageom.geom2d.conic.CircleArc2D;
import net.javageom.geom2d.curve.Curve2D;
import net.javageom.geom2d.domain.Domain2D;
import net.javageom.geom2d.line.LineSegment2D;

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
		
		assertEquals(1, clipped.continuousCurves().size());	
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

	public void testIsInsidePoint2D_ArbelosDown() {
		double x0 = 50;
		double y0 = 50;
		double r1 = 10;
		double r2 = 20;
		double r3 = 30;
		
		Point2D c1 = new Point2D(x0 + r1, y0);
		Point2D c2 = new Point2D(x0 + 2*r1 + r2, y0);
		Point2D c3 = new Point2D(x0 + r3, y0);
		
		CircleArc2D arc1 = new CircleArc2D(c1, r1, Math.PI, -Math.PI);
		CircleArc2D arc2 = new CircleArc2D(c2, r2, Math.PI, -Math.PI);
		CircleArc2D arc3 = new CircleArc2D(c3, r3, 0, Math.PI);
		
		GenericCirculinearRing2D ring = new GenericCirculinearRing2D(arc1, arc2, arc3);

		assertFalse(ring.isInside(new Point2D(x0 - 1, y0 - 1)));
		assertFalse(ring.isInside(new Point2D(x0 ,    y0 - 1)));
		assertFalse(ring.isInside(new Point2D(x0 + 1, y0 - 1)));

		assertFalse(ring.isInside(new Point2D(x0 + 2 * r1 - 1, y0 - 1)));
		Point2D p22 = new Point2D(x0 + 2 * r1 , y0 - 1);
		assertFalse(ring.isInside(p22));
		assertFalse(ring.isInside(new Point2D(x0 + 2 * r1 + 1, y0 - 1)));

		assertFalse(ring.isInside(new Point2D(x0 + 2 * r3 - 1, y0 - 1)));
		assertFalse(ring.isInside(new Point2D(x0 + 2 * r3 , y0 - 1)));
		assertFalse(ring.isInside(new Point2D(x0 + 2 * r3 + 1, y0 - 1)));
	}

	public void testIsInsidePoint2D_CurvedHeart() {
		double x0 = 50;
		double y0 = 50;
		double r1 = 10;
		double r2 = 20;
		double r3 = 30;
		
		Point2D c1 = new Point2D(x0 + r1, y0);
		Point2D c2 = new Point2D(x0 + 2*r1 + r2, y0);
		Point2D c3 = new Point2D(x0 + r3, y0);
		
		CircleArc2D arc1 = new CircleArc2D(c2, r2, 0, Math.PI);
		CircleArc2D arc2 = new CircleArc2D(c1, r1, 0, Math.PI);
		CircleArc2D arc3 = new CircleArc2D(c3, r3, Math.PI, Math.PI);
		
		GenericCirculinearRing2D ring = new GenericCirculinearRing2D(arc1, arc2, arc3);

		assertTrue(ring.isInside(new Point2D(x0 + 2 * r1 - 1, y0 - 1)));
		assertTrue(ring.isInside(new Point2D(x0 + 2 * r1 ,    y0 - 1)));
		assertTrue(ring.isInside(new Point2D(x0 + 2 * r1 + 1, y0 - 1)));
	}

	public void testIsInsidePoint2D_CurvedBand() {
		double x0 = 50;
		double y0 = 150;
		double r1 = 20;
		double r2 = 30;
		
		Point2D c1 = new Point2D(x0 + 2*r1 + r2, y0);
		CircleArc2D arc1 = new CircleArc2D(c1, r2, 0, Math.PI);
		Point2D c2 = new Point2D(x0 + r1, y0);
		CircleArc2D arc2 = new CircleArc2D(c2, r1, 0, Math.PI);
		
		double x1 = 50;
		double y1 = 50;
		
		Point2D c3 = new Point2D(x0 + r1, y0);
		CircleArc2D arc3 = new CircleArc2D(c3, r1, Math.PI, -Math.PI);
		Point2D c4 = new Point2D(x0 + 2*r1 + r2, y0);
		CircleArc2D arc4 = new CircleArc2D(c4, r2, Math.PI, -Math.PI);
		
		LineSegment2D line1 = new LineSegment2D(new Point2D(x0, y0),
				new Point2D(x1, y1));
		double diam = 2 * r1 + 2 * r2;
		LineSegment2D line2 = new LineSegment2D(new Point2D(x1 + diam, y1), 
				new Point2D(x0 + diam, y0));
		
		GenericCirculinearRing2D ring = new GenericCirculinearRing2D(
				arc1, arc2, line1, arc3, arc4, line2);

		Point2D p1 = new Point2D(x0 + 2 * r1 - 1, y0 - 1);
		assertTrue(ring.isInside(p1));
		assertTrue(ring.isInside(new Point2D(x0 + 2 * r1 , y0 - 1)));
		assertTrue(ring.isInside(new Point2D(x0 + 2 * r1 + 1, y0 - 1)));

		assertFalse(ring.isInside(new Point2D(x1 + 2 * r1 - 1, y1 - 1)));
		assertFalse(ring.isInside(new Point2D(x1 + 2 * r1 , y1 - 1)));
		assertFalse(ring.isInside(new Point2D(x1 + 2 * r1 + 1, y1 - 1)));

		assertFalse(ring.isInside(new Point2D(x1 - 1, y1 - 1)));
		assertFalse(ring.isInside(new Point2D(x1    , y1 - 1)));
		assertFalse(ring.isInside(new Point2D(x1 + 1, y1 - 1)));

		assertFalse(ring.isInside(new Point2D(x1 + diam - 1, y1 - 1)));
		assertFalse(ring.isInside(new Point2D(x1 + diam , y1 - 1)));
		assertFalse(ring.isInside(new Point2D(x1 + diam + 1, y1 - 1)));
	}
	
	public void testIsInsidePoint2D_TwinCurvedCorner() {
		double x0 = 100;
		double y0 = 100;
		
		double r1 = 60;
		double r2 = 20;
		double d = r1 - r2;
		
		CircleArc2D arc1 = new CircleArc2D(new Point2D(x0, y0), r1, 0, PI/2);
		CircleArc2D arc2 = new CircleArc2D(new Point2D(x0, y0+d), r2, PI/2, -PI/2);
		CircleArc2D arc3 = new CircleArc2D(new Point2D(x0+r1+r2, y0+d), r1, PI, PI/2);
		CircleArc2D arc4 = new CircleArc2D(new Point2D(x0+r1+r2, y0), r2, 3*PI/2, -PI/2);
		
		GenericCirculinearRing2D ring = new GenericCirculinearRing2D(
				arc1, arc2, arc3, arc4);

		assertFalse(ring.isInside(new Point2D(x0 - 1, y0 + r1 + 1)));
		assertFalse(ring.isInside(new Point2D(x0 - 1, y0 + r1)));
		assertFalse(ring.isInside(new Point2D(x0 - 1, y0 + r1 - 1)));

		assertFalse(ring.isInside(new Point2D(x0 + r1 + r2 + 1, y0 - r2 + 1)));
		assertFalse(ring.isInside(new Point2D(x0 + r1 + r2 + 1, y0 - r2)));
		assertFalse(ring.isInside(new Point2D(x0 + r1 + r2 + 1, y0 - r2 - 1)));
	}

	public void testGetBuffer_Astroid() {

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

		Domain2D buffer = ring.buffer(20);
		
		assertEquals(2, buffer.boundary().continuousCurves().size());
	}
}
