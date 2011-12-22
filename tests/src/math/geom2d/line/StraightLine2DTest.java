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
package math.geom2d.line;

import junit.framework.TestCase;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.CircleLine2D;
import math.geom2d.circulinear.CirculinearDomain2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.transform.CircleInversion2D;

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

	public void testGetParallel() {
		
	}
	
	public void testGetBuffer() {
		// create an infinite curve, here a straight line
		Point2D p0 = new Point2D(10, 20);
		Vector2D v0 = new Vector2D(10, 20);
		StraightLine2D line = new StraightLine2D(p0, v0);
	
		// computes buffer
		CirculinearDomain2D buffer = line.getBuffer(10);
		
		// check assertions
		assertNotNull(buffer);
		assertFalse(buffer.isEmpty());
		assertFalse(buffer.isBounded());
	}
	
	public void testTransformInversion() {
		// create an infinite curve, here a straight line
		Point2D p0 = new Point2D(30, 40);
		Vector2D v0 = new Vector2D(10, 20);
		StraightLine2D line = new StraightLine2D(p0, v0);
		
		Point2D center = new Point2D(50, 0);
		Circle2D circle = new Circle2D(center, 50);
		CircleInversion2D inv = new CircleInversion2D(circle);
		
		CircleLine2D res = line.transform(inv);
		assertNotNull(res);
		assertTrue(res instanceof Circle2D);		
		
		// the new shape does not contains the circle center
		assertTrue(res.contains(center));
	}
	
	public void testTransformInversion_CenterOnLine() {
		// create an infinite curve, here a straight line
		Point2D p0 = new Point2D(30, 40);
		Vector2D v0 = new Vector2D(10, 20);
		StraightLine2D line = new StraightLine2D(p0, v0);
		
		Circle2D circle = new Circle2D(30, 40, 50);
		CircleInversion2D inv = new CircleInversion2D(circle);
		
		CircleLine2D res = line.transform(inv);
		assertNotNull(res);
		assertTrue(res instanceof StraightLine2D);		
	}
	
	public void testGetBoundaryCurves() {
		// create an infinite curve, here a straight line
		Point2D p0 = new Point2D(30, 40);
		Vector2D v0 = new Vector2D(10, 20);
		StraightLine2D line = new StraightLine2D(p0, v0);
	
		assertEquals(1, line.getBoundaryCurves().size());
	}
	
	public void testIsBounded() {
		StraightLine2D line;
		
		line = new StraightLine2D(1, 2, 1, 1);
		assertFalse(line.isBounded());

		line = new StraightLine2D(1, 2, 1, 0);
		assertFalse(line.isBounded());

		line = new StraightLine2D(1, 2, 0, 1);
		assertFalse(line.isBounded());
	}

	public void testIsColinear() {
	    // lines roughly horizontal
	    StraightLine2D lineA1 = new StraightLine2D(2, 1, 4, 2);
	    StraightLine2D lineA2 = new StraightLine2D(6, 3, 2, 1);
	    StraightLine2D lineA3 = new StraightLine2D(6, 4, 2, 1);
	    assertTrue(lineA1.isColinear(lineA2));
	    assertTrue(lineA2.isColinear(lineA1));
        assertFalse(lineA1.isColinear(lineA3));

        // lines roughly vertical
        StraightLine2D lineB1 = new StraightLine2D(1, 2, 2, 4);
        StraightLine2D lineB2 = new StraightLine2D(3, 6, 1, 2);
        StraightLine2D lineB3 = new StraightLine2D(4, 6, 1, 2);
        assertTrue(lineB1.isColinear(lineB2));
        assertTrue(lineB2.isColinear(lineB1));
        assertFalse(lineB1.isColinear(lineB3));
	}

	public void testIsParallel() {
	    // lines roughly horizontal
        StraightLine2D lineA1 = new StraightLine2D(2, 1, 4, 2);
        StraightLine2D lineA2 = new StraightLine2D(6, 3, 2, 1);
        StraightLine2D lineA3 = new StraightLine2D(6, 4, 2, 1);
	    assertTrue(lineA1.isParallel(lineA2));
	    assertTrue(lineA2.isParallel(lineA1));
	    assertTrue(lineA1.isParallel(lineA3));

	    // lines roughly vertical
        StraightLine2D lineB1 = new StraightLine2D(1, 2, 2, 4);
        StraightLine2D lineB2 = new StraightLine2D(3, 6, 1, 2);
        StraightLine2D lineB3 = new StraightLine2D(4, 6, 1, 2);
	    assertTrue(lineB1.isParallel(lineB2));
	    assertTrue(lineB2.isParallel(lineB1));
	    assertTrue(lineB1.isParallel(lineB3));
	}

	public void testEquals() {
	    StraightLine2D line1 = new StraightLine2D(1, 2, 1, 1);
	    StraightLine2D line2 = new StraightLine2D(2, 3, 1, 1);
		StraightLine2D line3 = new StraightLine2D(1, 2, -1, -1);
		StraightLine2D line4 = new StraightLine2D(2, 3, 2, 2);
		
		assertTrue(line1.equals(line1));
		assertTrue(!line1.equals(line2));
		assertTrue(!line1.equals(line3));
		assertTrue(!line1.equals(line4));
		assertTrue(!line2.equals(line1));
		assertTrue(line2.equals(line2));
		assertTrue(!line2.equals(line3));
		assertTrue(!line2.equals(line4));
		assertTrue(!line3.equals(line1));
		assertTrue(!line3.equals(line2));
		assertTrue(line3.equals(line3));
		assertTrue(!line3.equals(line4));
		assertTrue(!line4.equals(line1));
		assertTrue(!line4.equals(line2));
		assertTrue(!line4.equals(line3));
		assertTrue(line4.equals(line4));
	}

    public void testGetParallelDouble() {
    	Point2D p1 = new Point2D(1, 1);
    	Point2D p2 = new Point2D(1, 3);
    	StraightLine2D line1 = new StraightLine2D(p1, p2);
    	
    	Point2D p1p = new Point2D(2, 1);
    	Point2D p2p = new Point2D(2, 3);
    	StraightLine2D line1p = new StraightLine2D(p1p, p2p);
    	
    	assertTrue(line1.getParallel(1).equals(line1p));
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
	public void testGetDistancePoint2D() {
		StraightLine2D line = new StraightLine2D(1, 2, 3, 4);
		Point2D pt;
		
		// test origin point
		pt = new Point2D(1, 2);
		assertEquals(0, line.getDistance(pt), 1e-14);
		
		// point on the line (positive extent)
		pt = new Point2D(1+1.5*3, 2+1.5*4);
		assertEquals(0, line.getDistance(pt), 1e-14);
		
		// point on the line (negative extent)
		pt = new Point2D(1-1.5*3, 2-1.5*4);
		assertEquals(0, line.getDistance(pt), 1e-14);
		
		// point outside the line
		pt = new Point2D(5, -1);
		assertEquals(5, line.getDistance(pt), 1e-14);	
		
		// point outside the line, in the other side
		pt = new Point2D(-3, 5);
		assertEquals(5, line.getDistance(pt), 1e-14);	
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

	public void testClone() {
	    StraightLine2D line = new StraightLine2D(new Point2D(10, 20), 
	            new Vector2D(30, 40));
	    assertTrue(line.equals(line.clone()));
	}
}
