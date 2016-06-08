/*
 * File : Point2DTest.java
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
 * Created on 31 déc. 2003
 */

package math.geom2d;
import java.util.ArrayList;

import math.geom2d.conic.Circle2D;
import math.geom2d.domain.Domain2D;

import junit.framework.TestCase;


/**
 * @author Legland
 */
public class Point2DTest extends TestCase {

	/**
	 * Constructor for Point2DTest.
	 * @param arg0
	 */
	public Point2DTest(String arg0) {
		super(arg0);
	}

	/*
	 * Test for double getDistance(Point2D)
	 */
	public void testGetDistancePoint2D() {
		Point2D p1 = new Point2D(2, 3);
		Point2D p2 = new Point2D(1, 4);
		Point2D p3 = new Point2D(2, 4);
		
		assertEquals(p1.distance(p1), 0, Shape2D.ACCURACY);
		assertEquals(p2.distance(p1), Math.sqrt(2), Shape2D.ACCURACY);
		assertEquals(p3.distance(p1), 1, Shape2D.ACCURACY);
		assertEquals(p2.distance(p2), 0, Shape2D.ACCURACY);
		assertEquals(p3.distance(p2), 1, Shape2D.ACCURACY);
		assertEquals(p3.distance(p3), 0, Shape2D.ACCURACY);
		
	}

	/*
	 * Test for double getDistance(double, double)
	 */
	public void testGetDistancedoubledouble() {
		Point2D p1 = new Point2D(2, 3);
		assertEquals(p1.distance(2, 3), 0, Shape2D.ACCURACY);
		assertEquals(p1.distance(1, 3), 1, Shape2D.ACCURACY);
		assertEquals(p1.distance(2, 4), 1, Shape2D.ACCURACY);
		assertEquals(p1.distance(2, 2), 1, Shape2D.ACCURACY);
		assertEquals(p1.distance(1, 2), Math.sqrt(2), Shape2D.ACCURACY);
		assertEquals(p1.distance(0, 0), Math.sqrt(13), Shape2D.ACCURACY);
	}

	public void testCentroid_Collection() {
		Point2D p1 = new Point2D(50, 60);
		Point2D p2 = new Point2D(110, 60);
		Point2D p3 = new Point2D(150, 140);
		Point2D p4 = new Point2D(90, 140);
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		Point2D centroid = Point2D.centroid(points);
		assertEquals(new Point2D(100, 100), centroid);
	}
	
	public void testCentroid_Array() {
		Point2D[] points = new Point2D[] {
				new Point2D(50, 60),
				new Point2D(110, 60),
				new Point2D(150, 140),
				new Point2D(90, 140)};
		Point2D centroid = Point2D.centroid(points);
		assertEquals(new Point2D(100, 100), centroid);
	}

	public void testCentroid_ArrayArray() {
		Point2D[] points = new Point2D[] {
				new Point2D(10, 10),
				new Point2D(20, 10),
				new Point2D(10, 20)};
		double[] weights = new double[]{2, 3, 5};
		Point2D centroid = Point2D.centroid(points, weights);
		assertEquals(new Point2D(13, 15), centroid);
		
		try {
			centroid = Point2D.centroid(points, new double[4]);
			fail("Should throw an exception");
		} catch(Exception ex) {
			// expected exception here
		}

	}

	public void testCentroid_Point2DPoint2DPoint2D() {
		Point2D p1 = new Point2D(10, 10);
		Point2D p2 = new Point2D(70, 10);
		Point2D p3 = new Point2D(10, 70);
		
		Point2D centroid = Point2D.centroid(p1, p2, p3);
		assertEquals(new Point2D(30, 30), centroid);
	}
	
	public void testCreatePolar() {
		Point2D p1 = new Point2D(0, 20);
		Point2D p2 = Point2D.createPolar(20, Math.PI/2);
		assertTrue(p1.distance(p2) < Shape2D.ACCURACY);
	}
	
	public void testCreatePolar_Point2D() {
		Point2D base = new Point2D(10, 20);
		Point2D p1 = new Point2D(10, 40);
		Point2D p2 = Point2D.createPolar(base, 20, Math.PI/2);
		assertTrue(p1.distance(p2) < Shape2D.ACCURACY);
	}
	
	public void testGetBuffer() {
		Point2D pt = new Point2D(10, 20);
		Domain2D buffer = pt.buffer(30);
		Circle2D circle = new Circle2D(10, 20, 30);
		assertTrue(circle.equals(buffer.boundary()));
	}
	
	public void testTranslate() {
	    // base point
	    Point2D p1 = new Point2D(10, 20);
	    
	    // test with positive translate
        Point2D p2 = new Point2D(14, 25);
        assertTrue(p1.translate(4, 5).equals(p2));
        
        // test with negative translate
        Point2D p3 = new Point2D(6, 15);
        assertTrue(p1.translate(-4, -5).equals(p3));
	}
	
    public void testScaleDoubleDouble() {
        // base point
        Point2D p1 = new Point2D(10, 20);
        
        // test with >1 factor
        Point2D p2 = new Point2D(20, 60);
        assertTrue(p1.scale(2, 3).equals(p2));
        
        // test with <1 factor
        Point2D p3 = new Point2D(5, 4);
        assertEquals(p1.scale(1./2., 1./5.), p3);
    }
    
    public void testScaleDouble() {
        // base point
        Point2D p1 = new Point2D(10, 20);
        
        // test with >1 factor
        Point2D p2 = new Point2D(20, 40);
        assertTrue(p1.scale(2).equals(p2));
        
        // test with <1 factor
        Point2D p3 = new Point2D(5, 10);
        assertEquals(p1.scale(1./2.), p3);
    }
    
	public void testRotate() {
        // base point
        Point2D p1 = new Point2D(10, 20);
        
        // test with basic angle
        Point2D p2 = new Point2D(-20, 10);
        Point2D p1r = p1.rotate(Math.PI/2);
        assertTrue(p1r.distance(p2) < Shape2D.ACCURACY);
        
        // test with center
        Point2D p3 = new Point2D(0, 10);
        Point2D p1rc = p1.rotate(new Point2D(10, 10), Math.PI/2);
        assertTrue(p1rc.distance(p3) < Shape2D.ACCURACY);
	}
	
	public void testPlusVector() {
		Point2D p1 = new Point2D(20, 30);
		Vector2D v = new Vector2D(40, 50);
		
		Point2D res = p1.plus(v);
		Point2D exp = new Point2D(60, 80);
		assertTrue(res.almostEquals(exp, Shape2D.ACCURACY));
	}
	
	public void testMinusVector() {
		Point2D p1 = new Point2D(60, 80);
		Vector2D v = new Vector2D(40, 50);
		
		Point2D res = p1.minus(v);
		Point2D exp = new Point2D(20, 30);
		assertTrue(res.almostEquals(exp, Shape2D.ACCURACY));
	}
	
	public void testIsBounded() {
		Point2D p1 = new Point2D(2, 3);
		Point2D p2 = new Point2D(1, 4);
		Point2D p3 = new Point2D(2, 4);
		
		assertTrue(p1.isBounded());
		assertTrue(p2.isBounded());
		assertTrue(p3.isBounded());
	}

	public void testIsColinear() {	
		
		Point2D p1 = new Point2D(1, 2);
		Point2D p2 = new Point2D(2, 4);
		Point2D p3 = new Point2D(4, 8);
		assertTrue(Point2D.isColinear(p1, p2, p3));
		assertTrue(Point2D.isColinear(p1, p3, p2));
		assertTrue(Point2D.isColinear(p2, p1, p3));
		assertTrue(Point2D.isColinear(p2, p3, p1));
		assertTrue(Point2D.isColinear(p3, p1, p2));
		assertTrue(Point2D.isColinear(p3, p2, p1));

		p1 = new Point2D(1, 2);
		p2 = new Point2D(2, 4.000000001);
		p3 = new Point2D(4, 8);
		assertTrue(!Point2D.isColinear(p1, p2, p3));
		assertTrue(!Point2D.isColinear(p1, p3, p2));
		assertTrue(!Point2D.isColinear(p2, p1, p3));
		assertTrue(!Point2D.isColinear(p2, p3, p1));
		assertTrue(!Point2D.isColinear(p3, p1, p2));
		assertTrue(!Point2D.isColinear(p3, p2, p1));		
	}
	
	public void testCcw(){
		Point2D p1 = new Point2D(10, 10);
		Point2D p2 = new Point2D(20, 10);
		Point2D p3 = new Point2D(30, 10);
		Point2D p4 = new Point2D(30, 20);
		
		// Simple cases
		assertEquals(Point2D.ccw(p1, p3, p4), 1);
		assertEquals(Point2D.ccw(p1, p4, p3), -1);
		
		// aligned points
		assertEquals(Point2D.ccw(p1, p2, p3), 1);
		assertEquals(Point2D.ccw(p3, p2, p1), 1);
		assertEquals(Point2D.ccw(p2, p1, p3), -1);
		assertEquals(Point2D.ccw(p2, p3, p1), -1);
		assertEquals(Point2D.ccw(p1, p3, p2), 0);
		assertEquals(Point2D.ccw(p3, p1, p2), 0);
	}
	
	/*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		Point2D p1 = new Point2D(2, 3);
		Point2D p2 = new Point2D(2, 3);
		Point2D p3 = new Point2D(1, 3);
		Point2D p4 = new Point2D(2, 4);
		Point2D p5 = new Point2D(3, 2);
		
		assertTrue(p1.equals(p1));
		assertTrue(p1.equals(p2));
		assertFalse(p1.equals(p3));
		assertFalse(p1.equals(p4));
		assertFalse(p1.equals(p5));
	}

	/*
	 * Test for boolean equals(Object)
	 */
	public void testHashCode() {
		Point2D p1 = new Point2D(2, 3);
		Point2D p2 = new Point2D(2, 3);
		Point2D p3 = new Point2D(1, 3);
		
		assertTrue(p1.hashCode() == p2.hashCode());
		assertFalse(p1.hashCode() == p3.hashCode());
	}

	public void testContains() {
		Point2D p1 = new Point2D(2, 3);
		Point2D p2 = new Point2D(2, 3);
		Point2D p3 = new Point2D(1, 3);
		
		assertTrue(p1.contains(p2));
		assertTrue(p2.contains(p1));
		assertFalse(p1.contains(p3));
		assertFalse(p3.contains(p1));
	}

}
