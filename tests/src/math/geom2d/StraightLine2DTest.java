/*
 * File : StraightLine2DTest.java
 *
 * Project : geometry
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 * 
 * author : Legland
 * Created on 29 déc. 2003
 */
package math.geom2d;

import junit.framework.TestCase;
import java.util.Collection;

/**
 * @author Legland
 */
public class StraightLine2DTest extends TestCase {

	/**
	 * Constructor for StraightLine2DTest.
	 * @param arg0
	 */
	public StraightLine2DTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(StraightLine2DTest.class);
	}


	public void testIsBounded() {
		StraightLine2D line;
		
		line = new StraightLine2D(1, 2, 1, 1);
		assertTrue(!line.isBounded());

		line = new StraightLine2D(1, 2, 1, 0);
		assertTrue(!line.isBounded());

		line = new StraightLine2D(1, 2, 0, 1);
		assertTrue(!line.isBounded());
	}

	public void testEqualsStraightLine2D() {
		StraightLine2D line1 = new StraightLine2D(1, 2, 1, 1);
		StraightLine2D line2 = new StraightLine2D(2, 3, 1, 1);
		StraightLine2D line3 = new StraightLine2D(1, 2, -1, -1);
		StraightLine2D line4 = new StraightLine2D(2, 3, 2, 2);
		
		assertTrue(line1.equals(line1));
		assertTrue(line1.equals(line2));
		assertTrue(line1.equals(line3));
		assertTrue(line1.equals(line4));
		assertTrue(line2.equals(line1));
		assertTrue(line2.equals(line2));
		assertTrue(line2.equals(line3));
		assertTrue(line2.equals(line4));
		assertTrue(line3.equals(line1));
		assertTrue(line3.equals(line2));
		assertTrue(line3.equals(line3));
		assertTrue(line3.equals(line4));
		assertTrue(line4.equals(line1));
		assertTrue(line4.equals(line2));
		assertTrue(line4.equals(line3));
		assertTrue(line4.equals(line4));
	}

	/*
	 * Test for boolean contains(double, double)
	 */
	public void testContainsdoubledouble() {
		StraightLine2D line;
		
		line = new StraightLine2D(1, 2, 1, 1);
		assertTrue(line.contains(2, 3));
		assertTrue(!line.contains(1, 3));
		assertTrue(!line.contains(2, 2));	
		assertTrue(!line.contains(0, 0));
			
		line = new StraightLine2D(1, 2, 1, 0);
		assertTrue(!line.contains(1, 3));
		assertTrue(line.contains(2, 2));
		assertTrue(!line.contains(1, 1));

		line = new StraightLine2D(1, 2, 0, 1);
		assertTrue(line.contains(1, 3));
		assertTrue(!line.contains(0, 2));
		assertTrue(!line.contains(2, 2));		
	}

	/*
	 * Test for boolean contains(double, double)
	 */
	public void testGetSignedDistancedoubledouble() {
		StraightLine2D line;
		
		line = new StraightLine2D(1, 2, 1, 1);
		assertEquals(line.getSignedDistance(2, 3), 0, Shape2D.ACCURACY);
		assertEquals(line.getSignedDistance(1, 3), -Math.sqrt(2)/2, Shape2D.ACCURACY);
		assertEquals(line.getSignedDistance(2, 2), Math.sqrt(2)/2, Shape2D.ACCURACY);
		
		line = new StraightLine2D(1, 2, -1, -1);
		assertEquals(line.getSignedDistance(2, 3), 0, Shape2D.ACCURACY);
		assertEquals(line.getSignedDistance(1, 3), Math.sqrt(2)/2, Shape2D.ACCURACY);
		assertEquals(line.getSignedDistance(2, 2), -Math.sqrt(2)/2, Shape2D.ACCURACY);
		
		line = new StraightLine2D(1, 2, 1, 0);
		assertEquals(line.getSignedDistance(1, 3), -1, Shape2D.ACCURACY);
		assertEquals(line.getSignedDistance(1, 1), 1, Shape2D.ACCURACY);
		assertEquals(line.getSignedDistance(2, 2), 0, Shape2D.ACCURACY);

		line = new StraightLine2D(1, 2, -1, 0);
		assertEquals(line.getSignedDistance(1, 3), 1, Shape2D.ACCURACY);
		assertEquals(line.getSignedDistance(1, 1), -1, Shape2D.ACCURACY);
		assertEquals(line.getSignedDistance(2, 2), 0, Shape2D.ACCURACY);

		line = new StraightLine2D(1, 2, 0, 1);
		assertEquals(line.getSignedDistance(1, 3), 0, Shape2D.ACCURACY);
		assertEquals(line.getSignedDistance(0, 2), -1, Shape2D.ACCURACY);
		assertEquals(line.getSignedDistance(2, 2), 1, Shape2D.ACCURACY);

		line = new StraightLine2D(1, 2, 0, -1);
		assertEquals(line.getSignedDistance(1, 3), 0, Shape2D.ACCURACY);
		assertEquals(line.getSignedDistance(0, 2), 1, Shape2D.ACCURACY);
		assertEquals(line.getSignedDistance(2, 2), -1, Shape2D.ACCURACY);
	}

	/*
	 * Test for boolean contains(double, double)
	 */
	public void testGetProjectedPointPoint2D() {
		StraightLine2D line;
		Point2D p1 = new Point2D(2, 3);
		Point2D p2 = new Point2D(1, 3);
		Point2D p3 = new Point2D(2, 2);

		line = new StraightLine2D(1, 2, 1, 1);
		assertEquals(line.getProjectedPoint(p1), p1);
		assertEquals(line.getProjectedPoint(p2), new Point2D(1.5, 2.5));
		assertEquals(line.getProjectedPoint(p3), new Point2D(1.5, 2.5));
		
		line = new StraightLine2D(1, 2, -1, -1);
		assertEquals(line.getProjectedPoint(p1), p1);
		assertEquals(line.getProjectedPoint(p2), new Point2D(1.5, 2.5));
		assertEquals(line.getProjectedPoint(p3), new Point2D(1.5, 2.5));
		
		line = new StraightLine2D(1, 2, 1, 0);
		assertEquals(line.getProjectedPoint(p1), p3);
		assertEquals(line.getProjectedPoint(p2), new Point2D(1, 2));
		assertEquals(line.getProjectedPoint(p3), p3);

		line = new StraightLine2D(1, 2, -1, 0);
		assertEquals(line.getProjectedPoint(p1), p3);
		assertEquals(line.getProjectedPoint(p2), new Point2D(1, 2));
		assertEquals(line.getProjectedPoint(p3), p3);

		line = new StraightLine2D(1, 2, 0, 1);
		assertEquals(line.getProjectedPoint(p1), p2);
		assertEquals(line.getProjectedPoint(p2), p2);
		assertEquals(line.getProjectedPoint(p3), new Point2D(1, 2));

		line = new StraightLine2D(1, 2, 0, -1);
		assertEquals(line.getProjectedPoint(p1), p2);
		assertEquals(line.getProjectedPoint(p2), p2);
		assertEquals(line.getProjectedPoint(p3), new Point2D(1, 2));
	}

	public void testGetPolarCoefficients(){
		StraightLine2D line;
		double tab[];
		
		line = new StraightLine2D(1, 2, 1, 1);
		tab = line.getPolarCoefficients();
		assertEquals(tab[0], Math.sqrt(2)/2, Shape2D.ACCURACY);
		assertEquals(tab[1], 5*Math.PI/4, Shape2D.ACCURACY);

		line = new StraightLine2D(1, 2, -1, -1);
		tab = line.getPolarCoefficients();
		assertEquals(tab[0], Math.sqrt(2)/2, Shape2D.ACCURACY);
		assertEquals(tab[1], 5*Math.PI/4, Shape2D.ACCURACY);

		line = new StraightLine2D(1, 2, 1, 0);
		tab = line.getPolarCoefficients();
		assertEquals(tab[0], 2, Shape2D.ACCURACY);
		assertEquals(tab[1], Math.PI, Shape2D.ACCURACY);

		line = new StraightLine2D(1, 2, -1, 0);
		tab = line.getPolarCoefficients();
		assertEquals(tab[0], 2, Shape2D.ACCURACY);
		assertEquals(tab[1], Math.PI, Shape2D.ACCURACY);

		line = new StraightLine2D(1, 2, 0, 1);
		tab = line.getPolarCoefficients();
		assertEquals(tab[0], 1, Shape2D.ACCURACY);
		assertEquals(tab[1], Math.PI/2, Shape2D.ACCURACY);

		line = new StraightLine2D(1, 2, 0, -1);
		tab = line.getPolarCoefficients();
		assertEquals(tab[0], 1, Shape2D.ACCURACY);
		assertEquals(tab[1], Math.PI/2, Shape2D.ACCURACY);
	
	}
	
	public void testGetViewAnglePoint2D(){
		
		StraightLine2D line1 = new StraightLine2D(2, 2, 1, 0);
		StraightLine2D line2 = new StraightLine2D(2, 2, 1, 1);
		StraightLine2D line3 = new StraightLine2D(2, 2, 0, 1);

		Point2D p1 = new Point2D(5, 1); 
		Point2D p2 = new Point2D(5, 3); 
		Point2D p3 = new Point2D(3, 5); 
		Point2D p4 = new Point2D(1, 5);
		assertEquals(line1.getWindingAngle(p1), -Math.PI, 1e-14);
		assertEquals(line2.getWindingAngle(p1), -Math.PI, 1e-14);
		assertEquals(line3.getWindingAngle(p1), -Math.PI, 1e-14);
		assertEquals(line1.getWindingAngle(p2), Math.PI, 1e-14);
		assertEquals(line2.getWindingAngle(p2), -Math.PI, 1e-14);
		assertEquals(line3.getWindingAngle(p2), -Math.PI, 1e-14);
		assertEquals(line1.getWindingAngle(p3), Math.PI, 1e-14);
		assertEquals(line2.getWindingAngle(p3), Math.PI, 1e-14);
		assertEquals(line3.getWindingAngle(p3), -Math.PI, 1e-14);
		assertEquals(line1.getWindingAngle(p4), Math.PI, 1e-14);
		assertEquals(line2.getWindingAngle(p4), Math.PI, 1e-14);
		assertEquals(line3.getWindingAngle(p4), Math.PI, 1e-14);		
	}
	
	public void testClipSmoothCurveHalfPlane(){
		Circle2D circle = new Circle2D(50, 50, 50);
		
		StraightLine2D line1 = new StraightLine2D(0, 0, 1, 0);		
		CurveSet2D<SmoothCurve2D> clipped1 = line1.clipSmoothCurve(circle);
		Collection<SmoothCurve2D> curves1 = clipped1.getCurves();
		assertTrue(curves1.size()==1);
		assertTrue(curves1.iterator().next().equals(circle));
		
		StraightLine2D line2 = new StraightLine2D(0, 0, 0, -1);		
		CurveSet2D<SmoothCurve2D> clipped2 = line2.clipSmoothCurve(circle);
		Collection<SmoothCurve2D> curves2 = clipped2.getCurves();
		assertTrue(curves2.size()==1);
		assertTrue(curves2.iterator().next().equals(circle));
	}
}
