/**
 * File: 	GenericCirculinearDomain2DTest.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 7 nov. 2010
 */
package math.geom2d.circulinear;

import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.ContourArray2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.transform.CircleInversion2D;
import junit.framework.TestCase;


/**
 * @author dlegland
 *
 */
public class GenericCirculinearDomain2DTest extends TestCase {

	/**
	 * Test method for {@link math.geom2d.circulinear.GenericCirculinearDomain2D#getBoundary()}.
	 */
	public void testGetBoundary() {
		// create a boundary
		Point2D center = Point2D.create(10, 20);
		Circle2D circle = Circle2D.create(center, 30);
		
		// create the domain
		Domain2D domain = GenericCirculinearDomain2D.create(circle);
		
		// extract boundary, and compare to base circle
		Boundary2D boundary = domain.getBoundary();
		
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
		Boundary2D boundary = complement.getBoundary();
		
		assertTrue(boundary instanceof Circle2D);
		Circle2D circle2 = Circle2D.create(center, 30, false);
		assertTrue(boundary.equals(circle2));
	}

	/**
	 * Test method for {@link math.geom2d.circulinear.GenericCirculinearDomain2D#getBuffer(double)}.
	 */
	public void testGetBuffer() {
		// create a boundary
		Point2D center = Point2D.create(10, 20);
		Circle2D circle = Circle2D.create(center, 30);
		
		// create the domain
		CirculinearDomain2D domain = GenericCirculinearDomain2D.create(circle);
		
		// extract boundary, and compare to base circle
		Domain2D buffer = domain.getBuffer(15);
		Boundary2D boundary = buffer.getBoundary();
		
		// compare with the theoretical circle
		Circle2D circle2 = Circle2D.create(center, 30+15, true);
		assertTrue(boundary instanceof ContourArray2D<?>);
		ContourArray2D<?> array = (ContourArray2D<?>) boundary;
		assertTrue(array.containsCurve(circle2));
	}


	public void testTransform_CircleInversion2D() {
		Circle2D circle1 = new Circle2D(new Point2D(0, 100), 30);
		Circle2D circle2 = new Circle2D(new Point2D(100, 100), 30);

		CirculinearDomain2D domain = GenericCirculinearDomain2D.create(
				CirculinearContourArray2D.create(circle1, circle2));

		CircleInversion2D inv = new CircleInversion2D(new Point2D(40, 30), 50);
		CirculinearDomain2D res = domain.transform(inv);
		
		assertFalse(res.isEmpty());
		
		CirculinearBoundary2D boundary = res.getBoundary();
		assertEquals(2, boundary.getContinuousCurves().size());
		for (CirculinearContour2D contour : boundary.getContinuousCurves())
			assertTrue(contour instanceof Circle2D);
	}
}
