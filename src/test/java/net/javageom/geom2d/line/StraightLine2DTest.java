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
package net.javageom.geom2d.line;

import junit.framework.TestCase;
import net.javageom.geom2d.Point2D;
import net.javageom.geom2d.Shape2D;
import net.javageom.geom2d.Vector2D;
import net.javageom.geom2d.circulinear.CircleLine2D;
import net.javageom.geom2d.circulinear.CirculinearDomain2D;
import net.javageom.geom2d.conic.Circle2D;
import net.javageom.geom2d.line.StraightLine2D;
import net.javageom.geom2d.transform.CircleInversion2D;

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

	public void testGetParallel() {
		
	}
	
	public void testGetBuffer() {
		// create an infinite curve, here a straight line
		Point2D p0 = new Point2D(10, 20);
		Vector2D v0 = new Vector2D(10, 20);
		StraightLine2D line = new StraightLine2D(p0, v0);
	
		// computes buffer
		CirculinearDomain2D buffer = line.buffer(10);
		
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
	
		assertEquals(1, line.continuousCurves().size());
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
    	
    	assertTrue(line1.parallel(1).equals(line1p));
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
		assertEquals(0, line.distance(pt), 1e-14);
		
		// point on the line (positive extent)
		pt = new Point2D(1+1.5*3, 2+1.5*4);
		assertEquals(0, line.distance(pt), 1e-14);
		
		// point on the line (negative extent)
		pt = new Point2D(1-1.5*3, 2-1.5*4);
		assertEquals(0, line.distance(pt), 1e-14);
		
		// point outside the line
		pt = new Point2D(5, -1);
		assertEquals(5, line.distance(pt), 1e-14);	
		
		// point outside the line, in the other side
		pt = new Point2D(-3, 5);
		assertEquals(5, line.distance(pt), 1e-14);	
	}
	
	/*
	 * Test for boolean contains(double, double)
	 */
	public void testGetSignedDistancedoubledouble() {
		StraightLine2D line;
		
		line = new StraightLine2D(1, 2, 1, 1);
		assertEquals(line.signedDistance(2, 3), 0, Shape2D.ACCURACY);
		assertEquals(line.signedDistance(1, 3), -Math.sqrt(2)/2, Shape2D.ACCURACY);
		assertEquals(line.signedDistance(2, 2), Math.sqrt(2)/2, Shape2D.ACCURACY);
		
		line = new StraightLine2D(1, 2, -1, -1);
		assertEquals(line.signedDistance(2, 3), 0, Shape2D.ACCURACY);
		assertEquals(line.signedDistance(1, 3), Math.sqrt(2)/2, Shape2D.ACCURACY);
		assertEquals(line.signedDistance(2, 2), -Math.sqrt(2)/2, Shape2D.ACCURACY);
		
		line = new StraightLine2D(1, 2, 1, 0);
		assertEquals(line.signedDistance(1, 3), -1, Shape2D.ACCURACY);
		assertEquals(line.signedDistance(1, 1), 1, Shape2D.ACCURACY);
		assertEquals(line.signedDistance(2, 2), 0, Shape2D.ACCURACY);

		line = new StraightLine2D(1, 2, -1, 0);
		assertEquals(line.signedDistance(1, 3), 1, Shape2D.ACCURACY);
		assertEquals(line.signedDistance(1, 1), -1, Shape2D.ACCURACY);
		assertEquals(line.signedDistance(2, 2), 0, Shape2D.ACCURACY);

		line = new StraightLine2D(1, 2, 0, 1);
		assertEquals(line.signedDistance(1, 3), 0, Shape2D.ACCURACY);
		assertEquals(line.signedDistance(0, 2), -1, Shape2D.ACCURACY);
		assertEquals(line.signedDistance(2, 2), 1, Shape2D.ACCURACY);

		line = new StraightLine2D(1, 2, 0, -1);
		assertEquals(line.signedDistance(1, 3), 0, Shape2D.ACCURACY);
		assertEquals(line.signedDistance(0, 2), 1, Shape2D.ACCURACY);
		assertEquals(line.signedDistance(2, 2), -1, Shape2D.ACCURACY);
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
		assertEquals(line.projectedPoint(p1), p1);
		assertEquals(line.projectedPoint(p2), new Point2D(1.5, 2.5));
		assertEquals(line.projectedPoint(p3), new Point2D(1.5, 2.5));
		
		line = new StraightLine2D(1, 2, -1, -1);
		assertEquals(line.projectedPoint(p1), p1);
		assertEquals(line.projectedPoint(p2), new Point2D(1.5, 2.5));
		assertEquals(line.projectedPoint(p3), new Point2D(1.5, 2.5));
		
		line = new StraightLine2D(1, 2, 1, 0);
		assertEquals(line.projectedPoint(p1), p3);
		assertEquals(line.projectedPoint(p2), new Point2D(1, 2));
		assertEquals(line.projectedPoint(p3), p3);

		line = new StraightLine2D(1, 2, -1, 0);
		assertEquals(line.projectedPoint(p1), p3);
		assertEquals(line.projectedPoint(p2), new Point2D(1, 2));
		assertEquals(line.projectedPoint(p3), p3);

		line = new StraightLine2D(1, 2, 0, 1);
		assertEquals(line.projectedPoint(p1), p2);
		assertEquals(line.projectedPoint(p2), p2);
		assertEquals(line.projectedPoint(p3), new Point2D(1, 2));

		line = new StraightLine2D(1, 2, 0, -1);
		assertEquals(line.projectedPoint(p1), p2);
		assertEquals(line.projectedPoint(p2), p2);
		assertEquals(line.projectedPoint(p3), new Point2D(1, 2));
	}

	public void testLineContainsPoint()
	{
		Point2D p1 = new Point2D(-123.70, 157.92);
		Point2D p2 = new Point2D(-125.83, 155.80);
		StraightLine2D line = new StraightLine2D(p1, p2);
		Point2D point2d = new Point2D(-125.03, 157.13);
		Point2D projectedPoint = line.projectedPoint(point2d);

//		assertEquals("Point need to be the same", new Point2D(-124768.55791617256, 156863.99791617255),  projectedPoint);
		assertTrue("Point is on line", line.containsProjection(point2d));
		assertTrue("Projected point has to be located on the line", line.contains(projectedPoint));
	}


	public void testLineContainsPoint_LargeValues()
	{
		Point2D p1 = new Point2D(-123707.89774439273, 157924.65808795238);
		Point2D p2 = new Point2D(-125829.21808795238, 155803.33774439275);
		StraightLine2D line = new StraightLine2D(p1, p2);
		Point2D point2d = new Point2D(-125034.27520951361, 157129.7152095136);
		Point2D projectedPoint = line.projectedPoint(point2d);

		assertEquals("Point need to be the same", new Point2D(-124768.55791617256, 156863.99791617255),  projectedPoint);
		assertTrue("Point is on line", line.containsProjection(point2d));
		assertTrue("Projected point has to be located on the line", line.contains(projectedPoint));
	}

	public void testGetPolarCoefficients(){
		StraightLine2D line;
		double tab[];
		
		line = new StraightLine2D(1, 2, 1, 1);
		tab = line.polarCoefficients();
		assertEquals(tab[0], Math.sqrt(2)/2, Shape2D.ACCURACY);
		assertEquals(tab[1], 5*Math.PI/4, Shape2D.ACCURACY);

		line = new StraightLine2D(1, 2, -1, -1);
		tab = line.polarCoefficients();
		assertEquals(tab[0], Math.sqrt(2)/2, Shape2D.ACCURACY);
		assertEquals(tab[1], 5*Math.PI/4, Shape2D.ACCURACY);

		line = new StraightLine2D(1, 2, 1, 0);
		tab = line.polarCoefficients();
		assertEquals(tab[0], 2, Shape2D.ACCURACY);
		assertEquals(tab[1], Math.PI, Shape2D.ACCURACY);

		line = new StraightLine2D(1, 2, -1, 0);
		tab = line.polarCoefficients();
		assertEquals(tab[0], 2, Shape2D.ACCURACY);
		assertEquals(tab[1], Math.PI, Shape2D.ACCURACY);

		line = new StraightLine2D(1, 2, 0, 1);
		tab = line.polarCoefficients();
		assertEquals(tab[0], 1, Shape2D.ACCURACY);
		assertEquals(tab[1], Math.PI/2, Shape2D.ACCURACY);

		line = new StraightLine2D(1, 2, 0, -1);
		tab = line.polarCoefficients();
		assertEquals(tab[0], 1, Shape2D.ACCURACY);
		assertEquals(tab[1], Math.PI/2, Shape2D.ACCURACY);
	
	}
	
	public void testIntersection() {
	    StraightLine2D line2d = new StraightLine2D(110337.28297120638, 48934.88776858141, 1650.316028793619, 1052.3122314185894);

	    StraightLine2D line1 = new StraightLine2D(110083.599, 47397.2, 3808.0, 0.0);
	    StraightLine2D line2 = new StraightLine2D(113891.599, 47397.2, 0.0, 5180.0);
	    StraightLine2D line3 = new StraightLine2D(113891.599, 52577.2, -3808.0, 0.0);
	    StraightLine2D line4 = new StraightLine2D(110083.599, 52577.2, 0.0, -5180.0);

	    assertNotNull(line2d.intersection(line1));
	    assertNotNull(line2d.intersection(line2));
	    assertNotNull(line2d.intersection(line3));
	    assertNotNull(line2d.intersection(line4));
	}
	
	public void testGetViewAnglePoint2D(){
		
		StraightLine2D line1 = new StraightLine2D(2, 2, 1, 0);
		StraightLine2D line2 = new StraightLine2D(2, 2, 1, 1);
		StraightLine2D line3 = new StraightLine2D(2, 2, 0, 1);

		Point2D p1 = new Point2D(5, 1); 
		Point2D p2 = new Point2D(5, 3); 
		Point2D p3 = new Point2D(3, 5); 
		Point2D p4 = new Point2D(1, 5);
		assertEquals(line1.windingAngle(p1), -Math.PI, 1e-14);
		assertEquals(line2.windingAngle(p1), -Math.PI, 1e-14);
		assertEquals(line3.windingAngle(p1), -Math.PI, 1e-14);
		assertEquals(line1.windingAngle(p2), Math.PI, 1e-14);
		assertEquals(line2.windingAngle(p2), -Math.PI, 1e-14);
		assertEquals(line3.windingAngle(p2), -Math.PI, 1e-14);
		assertEquals(line1.windingAngle(p3), Math.PI, 1e-14);
		assertEquals(line2.windingAngle(p3), Math.PI, 1e-14);
		assertEquals(line3.windingAngle(p3), -Math.PI, 1e-14);
		assertEquals(line1.windingAngle(p4), Math.PI, 1e-14);
		assertEquals(line2.windingAngle(p4), Math.PI, 1e-14);
		assertEquals(line3.windingAngle(p4), Math.PI, 1e-14);		
	}

	public void testCopyConstructor() {
	    StraightLine2D line = new StraightLine2D(new Point2D(10, 20), 
	            new Vector2D(30, 40));
	    StraightLine2D copy = new StraightLine2D(line);
	    assertTrue(line.equals(copy));
	}
}
