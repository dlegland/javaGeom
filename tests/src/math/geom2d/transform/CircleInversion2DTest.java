/**
 * File: 	CircleInversion2DTest.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 22 nov. 09
 */
package math.geom2d.transform;

import math.geom2d.Point2D;
import junit.framework.TestCase;


/**
 * @author dlegland
 *
 */
public class CircleInversion2DTest extends TestCase {

	/**
	 * Test method for {@link math.geom2d.transform.CircleInversion2D#center()}.
	 */
	public void testGetCenter() {
		CircleInversion2D inv = CircleInversion2D.create(new Point2D(10, 20), 30);
		Point2D center = Point2D.create(10, 20);
		assertEquals(center, inv.center());
	}

	/**
	 * Test method for {@link math.geom2d.transform.CircleInversion2D#radius()}.
	 */
	public void testGetRadius() {
		CircleInversion2D inv = new CircleInversion2D(10, 20, 30);
		assertEquals(30., inv.radius());
	}

	/**
	 * Test method for {@link math.geom2d.transform.CircleInversion2D#invert()}.
	 */
	public void testInvert() {
		CircleInversion2D inv = new CircleInversion2D(10, 20, 30);
		assertEquals(inv, inv.invert());
	}

	/**
	 * Test method for {@link math.geom2d.transform.CircleInversion2D#transform(java.awt.geom.Point2D)}.
	 */
	public void testTransformPoint2D() {
		double xc = 10;
		double yc = 20;
		double r = 12;
		CircleInversion2D inv = new CircleInversion2D(xc, yc, r);
		
		Point2D p1 = Point2D.create(xc+9, yc);
		Point2D r1 = Point2D.create(xc+16, yc);
		assertEquals(r1, inv.transform(p1));
	}
}
