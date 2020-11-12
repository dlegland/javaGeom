/**
 * File: 	DomainArray2DTest.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 17 août 10
 */
package net.javageom.geom2d.domain;

import junit.framework.TestCase;
import net.javageom.geom2d.Point2D;
import net.javageom.geom2d.conic.Circle2D;


/**
 * @author dlegland
 *
 */
public class DomainArray2DTest extends TestCase {

	/**
	 * Test method for {@link net.javageom.geom2d.ShapeArray2D#contains(double, double)}.
	 */
	public void testContainsDoubleDouble() {
		Circle2D circle1 = new Circle2D(10, 50, 20);
		Circle2D circle2 = new Circle2D(80, 50, 20);
		Domain2D disk1 = new GenericDomain2D(circle1);
		Domain2D disk2 = new GenericDomain2D(circle2);
		Domain2D[] array = new Domain2D[]{disk1, disk2};
		DomainArray2D<Domain2D> domain = DomainArray2D.create(array);
		
		Point2D pointInside1 = new Point2D(10, 50);
		Point2D pointInside2 = new Point2D(80, 50);
		assertTrue(domain.contains(pointInside1));
		assertTrue(domain.contains(pointInside2));

	}

}
