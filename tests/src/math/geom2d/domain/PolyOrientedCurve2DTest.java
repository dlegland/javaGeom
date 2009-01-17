/**
 * File: 	PolyOrientedCurve2DTest.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 16 nov. 08
 */
package math.geom2d.domain;

import math.geom2d.Point2D;
import math.geom2d.conic.CircleArc2D;
import junit.framework.TestCase;

import static java.lang.Math.*;

/**
 * @author dlegland
 *
 */
public class PolyOrientedCurve2DTest extends TestCase {

	/**
	 * Test method for {@link math.geom2d.domain.PolyOrientedCurve2D#getWindingAngle(java.awt.geom.Point2D)}.
	 * Use a curve set formed by two circle arcs.
	 */
	public void testGetWindingAnglePoint() {
		double x0 = 150;
		double x1 = 100;
		double x2 = 200;
		double r = 100;
		double y0 = 100;
		
		Point2D c0 = new Point2D(x0, y0);
		Point2D c1 = new Point2D(x1, y0);
		Point2D c2 = new Point2D(x2, y0);
		
		double extent = 4*Math.PI/3;
		CircleArc2D arc1 = new CircleArc2D(c1, r, Math.PI/3, extent);
		CircleArc2D arc2 = new CircleArc2D(c2, r, 4*Math.PI/3, extent);
		
		PolyOrientedCurve2D<CircleArc2D> curve = new PolyOrientedCurve2D<CircleArc2D>(
				new CircleArc2D[]{arc1, arc2});
		
		double eps = 1e-14;
		assertEquals(2*PI, curve.getWindingAngle(c0), eps);
		assertEquals(2*PI, curve.getWindingAngle(c1), eps);
		assertEquals(2*PI, curve.getWindingAngle(c2), eps);
		
		assertEquals(0, curve.getWindingAngle(new Point2D(0, 0)), eps);
		assertEquals(0, curve.getWindingAngle(new Point2D(150, 200)), eps);
		assertEquals(0, curve.getWindingAngle(new Point2D(250, 200)), eps);
		
		OrientedCurve2D reverse = curve.getReverseCurve();
		assertEquals(-2*PI, reverse.getWindingAngle(c0), eps);
		assertEquals(-2*PI, reverse.getWindingAngle(c1), eps);
		assertEquals(-2*PI, reverse.getWindingAngle(c2), eps);
		
		assertEquals(0, reverse.getWindingAngle(new Point2D(0, 0)), eps);
		assertEquals(0, reverse.getWindingAngle(new Point2D(150, 200)), eps);
		assertEquals(0, reverse.getWindingAngle(new Point2D(250, 200)), eps);
	}

}