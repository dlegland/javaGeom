/*
 * File : LineArc2DTest.java
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
 * Created on 29 d�c. 2003
 */
package net.javageom.geom2d.line;

import junit.framework.TestCase;
import net.javageom.geom2d.Point2D;
import net.javageom.geom2d.Shape2D;

/**
 * @author Legland
 */
public class LineArc2DTest extends TestCase {

	/**
	 * Constructor for Edge2DTest.
	 * @param arg0
	 */
	public LineArc2DTest(String arg0) {
		super(arg0);
	}

	public void testGetParallelDouble() {
		Point2D p1 = new Point2D(1, 1);
		Point2D p2 = new Point2D(1, 3);
		LineArc2D line1 = new LineArc2D(p1, p2, -1, 2);

		Point2D p1p = new Point2D(2, 1);
		Point2D p2p = new Point2D(2, 3);
		LineArc2D line1p = new LineArc2D(p1p, p2p, -1, 2);

		assertTrue(line1.parallel(1).equals(line1p));
	}

	/*
	 * Test for double getDistance(Point2D)
	 */
	public void testGetDistanceDoubleDouble() {
		
		// basic
		LineArc2D edge = new LineArc2D(1, 1, 3, 2, 0, 1);
		
		assertEquals(edge.distance(1, 1), 0, Shape2D.ACCURACY);
		assertEquals(edge.distance(2.5, 2), 0, Shape2D.ACCURACY);
		assertEquals(edge.distance(4, 3), 0, Shape2D.ACCURACY);
	
		double d1 = Math.sqrt(13)/2;
		assertEquals(edge.distance(-.5, 0), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(2, -.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(3.5, .5), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(5, 1.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(5.5, 4), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(3, 4.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(1.5, 3.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(0, 2.5), d1, Shape2D.ACCURACY);

		double d2 = Math.sqrt(26)/2;
		assertEquals(edge.distance(0.5, -1.5), d2, Shape2D.ACCURACY);
		assertEquals(edge.distance(-1.5, 1.5), d2, Shape2D.ACCURACY);
		assertEquals(edge.distance(6.5, 2.5), d2, Shape2D.ACCURACY);
		assertEquals(edge.distance(4.5, 5.5), d2, Shape2D.ACCURACY);
		
		// horizontal edge
		edge = new LineArc2D(1, 1, 3, 0, 0, 1);
		assertEquals(edge.distance(1, 1), 0, Shape2D.ACCURACY);
		assertEquals(edge.distance(2.5, 1), 0, Shape2D.ACCURACY);
		assertEquals(edge.distance(4, 1), 0, Shape2D.ACCURACY);
		
		d1 = 1;
		assertEquals(edge.distance(0, 1), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(5, 1), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(1, 0), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(2.5, 0), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(4, 0), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(1, 2), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(2.5, 2), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(4, 2), d1, Shape2D.ACCURACY);
		
		d2=Math.sqrt(2);
		assertEquals(edge.distance(0, 0), d2, Shape2D.ACCURACY);
		assertEquals(edge.distance(0, 2), d2, Shape2D.ACCURACY);
		assertEquals(edge.distance(5, 0), d2, Shape2D.ACCURACY);
		assertEquals(edge.distance(5, 2), d2, Shape2D.ACCURACY);
		
		// vertical edge
		edge = new LineArc2D(1, 1, 0, 3, 0, 1);
		assertEquals(edge.distance(1, 1), 0, Shape2D.ACCURACY);
		assertEquals(edge.distance(1, 2.5), 0, Shape2D.ACCURACY);
		assertEquals(edge.distance(1, 4), 0, Shape2D.ACCURACY);
		
		assertEquals(edge.distance(1, 0), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(1, 5), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(0, 1), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(0, 2.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(0, 4), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(2, 1), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(2, 2.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.distance(2, 4), d1, Shape2D.ACCURACY);

		assertEquals(edge.distance(0, 0), d2, Shape2D.ACCURACY);
		assertEquals(edge.distance(2, 0), d2, Shape2D.ACCURACY);
		assertEquals(edge.distance(2, 5), d2, Shape2D.ACCURACY);
		assertEquals(edge.distance(0, 5), d2, Shape2D.ACCURACY);
	}

	/*
	 * Test for double getSignedDistance(Point2D)
	 */
	public void testGetSignedDistancePoint2D() {
	}


	/*
	 * Test for Point2D getIntersection(StraightObject2D)
	 */
	public void testGetIntersectionStraightObject2D(){
		LineArc2D edge1 = new LineArc2D(1, 1, 2, 1, 0, 1);
		LineArc2D edge2 = new LineArc2D(1, 1, -1, 3, 0, 1);
		assertTrue(edge1.intersection(edge2).equals(new Point2D(1, 1)));
		assertTrue(edge2.intersection(edge1).equals(new Point2D(1, 1)));
		
		LineArc2D edge3 = new LineArc2D(3, 2, -3, 2, 0, 1);
		assertTrue(edge1.intersection(edge3).equals(new Point2D(3, 2)));
		assertTrue(edge3.intersection(edge1).equals(new Point2D(3, 2)));
		assertTrue(edge2.intersection(edge3).equals(new Point2D(0, 4)));
		assertTrue(edge3.intersection(edge2).equals(new Point2D(0, 4)));
		
		LineArc2D edge4 = new LineArc2D(0, 0, 5, 1, 0, 1);
		assertEquals(edge1.intersection(edge4), null);
		assertEquals(edge2.intersection(edge4), null);
		assertEquals(edge3.intersection(edge4), null);
		
		edge1 = new LineArc2D(1, 1, 4, 4, 0, 1);
		edge2 = new LineArc2D(1, 5, 4, -4, 0, 1);
		assertTrue(edge1.intersection(edge2).equals(new Point2D(3, 3)));
		assertTrue(edge2.intersection(edge1).equals(new Point2D(3, 3)));
	}

	/*
	 * Test for boolean contains(double, double)
	 */
	public void testContainsdoubledouble() {
		LineArc2D edge;
		
		// diagonal edge
		edge = new LineArc2D(1, 1, 2, 1, 0, 1);
		assertTrue(edge.contains(1, 1));
		assertTrue(edge.contains(3, 2));
		assertTrue(edge.contains(2, 1.5));
		assertTrue(!edge.contains(0, 0));
		assertTrue(!edge.contains(-1, 0));
		assertTrue(!edge.contains(5, 3));
		
		// horizontal edge
		edge = new LineArc2D(1, 1, 2, 0, 0, 1);
		assertTrue(edge.contains(1, 1));
		assertTrue(edge.contains(3, 1));
		assertTrue(edge.contains(2, 1));
		assertTrue(!edge.contains(0, 0));
		assertTrue(!edge.contains(0, 1));
		assertTrue(!edge.contains(4, 1));

		// vertical edge
		edge = new LineArc2D(1, 1, 0, 2, 0, 1);
		assertTrue(edge.contains(1, 1));
		assertTrue(edge.contains(1, 3));
		assertTrue(edge.contains(1, 2));
		assertTrue(!edge.contains(0, 0));
		assertTrue(!edge.contains(1, 0));
		assertTrue(!edge.contains(1, 4));
	}

	public void testGetViewAnglePoint2D(){
		
		Point2D p1 = new Point2D(1, 1);
		Point2D p2 = new Point2D(3, 1);
		Point2D p3 = new Point2D(1, 1);
		Point2D p4 = new Point2D(1, 3);
		
		LineArc2D edge1 = new LineArc2D(2, 0, 0, 2, 0, 1);
		assertEquals(edge1.windingAngle(p1), Math.PI/2, 1e-14);
		assertEquals(edge1.windingAngle(p2), -Math.PI/2, 1e-14);
		
		LineArc2D edge2 = new LineArc2D(2, 2, 0, -2, 0, 1);
		assertEquals(edge2.windingAngle(p1), -Math.PI/2, 1e-14);
		assertEquals(edge2.windingAngle(p2), Math.PI/2, 1e-14);

		LineArc2D edge3 = new LineArc2D(0, 2, 2, 0, 0, 1);
		assertEquals(edge3.windingAngle(p3), -Math.PI/2, 1e-14);
		assertEquals(edge3.windingAngle(p4), Math.PI/2, 1e-14);

		LineArc2D edge4 = new LineArc2D(2, 2, -2, 0, 0, 1);
		assertEquals(edge4.windingAngle(p3), Math.PI/2, 1e-14);
		assertEquals(edge4.windingAngle(p4), -Math.PI/2, 1e-14);
		
	}
	
	public void testGetSubCurve(){
		LineArc2D arc1 = new LineArc2D(3, 4, 1, 2, 0, 10);
		LineArc2D arc2 = new LineArc2D(3, 4, 1, 2, 1, 2);
		LineArc2D sub  = arc1.subCurve(1, 2);
		assertTrue(arc2.equals(sub));
	}
	
	public void testGetReverseCurve() {
		LineArc2D arc = new LineArc2D(1, 2, 3, 4, 0, 1);
		LineArc2D rev = new LineArc2D(1, 2, -3, -4, -1, 0);
		assertTrue(rev.almostEquals(arc.reverse(), Shape2D.ACCURACY));
	}
	
    public void testEqualsLineArc2D(){
        LineArc2D edge1 = new LineArc2D(1, 2, 2, 2, 0, 1);
        assertTrue(edge1.equals(edge1));
        LineArc2D edge2 = new LineArc2D(3, 4, -2, -2, 0, 1);
        assertTrue(!edge1.equals(edge2));
        assertTrue(!edge2.equals(edge1));
        
        LineArc2D edge3 = new LineArc2D(1, 4, 2, -2, 0, 1);
        assertTrue(!edge1.equals(edge3));
        assertTrue(!edge2.equals(edge3));
        assertTrue(!edge3.equals(edge1));
        assertTrue(!edge3.equals(edge2));
        
        LineArc2D edge4 = new LineArc2D(1, 2, 2, 0, 0, 1);
        assertTrue(!edge1.equals(edge4));
        assertTrue(!edge2.equals(edge4));
        assertTrue(!edge4.equals(edge1));
        assertTrue(!edge4.equals(edge2));       
    }

}
