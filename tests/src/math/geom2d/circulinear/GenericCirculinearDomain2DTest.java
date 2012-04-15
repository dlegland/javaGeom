/**
 * File: 	GenericCirculinearDomain2DTest.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 7 nov. 2010
 */
package math.geom2d.circulinear;

import static java.lang.Math.PI;
import junit.framework.TestCase;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.ContourArray2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.transform.CircleInversion2D;


/**
 * @author dlegland
 *
 */
public class GenericCirculinearDomain2DTest extends TestCase {

	/**
	 * Test method for {@link math.geom2d.circulinear.GenericCirculinearDomain2D#boundary()}.
	 */
	public void testGetBoundary() {
		// create a boundary
		Point2D center = Point2D.create(10, 20);
		Circle2D circle = Circle2D.create(center, 30);
		
		// create the domain
		Domain2D domain = GenericCirculinearDomain2D.create(circle);
		
		// extract boundary, and compare to base circle
		Boundary2D boundary = domain.boundary();
		
		assertTrue(boundary.equals(circle));
	}

	/**
	 * Test method for {@link math.geom2d.circulinear.GenericCirculinearDomain2D#complement()}.
	 */
	public void testComplement() {
		// create a boundary
		Point2D center = Point2D.create(10, 20);
		Circle2D circle = Circle2D.create(center, 30);
		
		// create the domain
		Domain2D domain = GenericCirculinearDomain2D.create(circle);
		
		// extract boundary, and compare to base circle
		Domain2D complement = domain.complement();
		Boundary2D boundary = complement.boundary();
		
		assertTrue(boundary instanceof Circle2D);
		Circle2D circle2 = Circle2D.create(center, 30, false);
		assertTrue(boundary.equals(circle2));
	}

	/**
	 * Test method for {@link math.geom2d.circulinear.GenericCirculinearDomain2D#buffer(double)}.
	 */
	public void testGetBuffer() {
		// create a boundary
		Point2D center = Point2D.create(10, 20);
		Circle2D circle = Circle2D.create(center, 30);
		
		// create the domain
		CirculinearDomain2D domain = GenericCirculinearDomain2D.create(circle);
		
		// extract boundary, and compare to base circle
		Domain2D buffer = domain.buffer(15);
		Boundary2D boundary = buffer.boundary();
		
		// compare with the theoretical circle
		Circle2D circle2 = Circle2D.create(center, 30+15, true);
		assertTrue(boundary instanceof ContourArray2D<?>);
		ContourArray2D<?> array = (ContourArray2D<?>) boundary;
		assertTrue(array.containsCurve(circle2));
	}

	public void testClip_Astroid() {
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
		GenericCirculinearDomain2D domain = new GenericCirculinearDomain2D(ring);
		
		Box2D box = new Box2D(50, 350, 50, 350);
		Domain2D clipped = domain.clip(box);
		
		Boundary2D boundary = clipped.boundary();
		assertEquals(1, boundary.continuousCurves().size());
		
	}

	public void testContains_Astroid() {
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
	}

	public void testTransform_CircleInversion2D() {
		Circle2D circle1 = new Circle2D(new Point2D(0, 100), 30);
		Circle2D circle2 = new Circle2D(new Point2D(100, 100), 30);

		CirculinearDomain2D domain = GenericCirculinearDomain2D.create(
				CirculinearContourArray2D.create(circle1, circle2));

		CircleInversion2D inv = new CircleInversion2D(new Point2D(40, 30), 50);
		CirculinearDomain2D res = domain.transform(inv);
		
		assertFalse(res.isEmpty());
		
		CirculinearBoundary2D boundary = res.boundary();
		assertEquals(2, boundary.continuousCurves().size());
		for (CirculinearContour2D contour : boundary.continuousCurves())
			assertTrue(contour instanceof Circle2D);
	}
}
