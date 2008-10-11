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

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(Point2DTest.class);
	}

	/*
	 * Test for double getDistance(Point2D)
	 */
	public void testGetDistancePoint2D() {
		Point2D p1 = new Point2D(2, 3);
		Point2D p2 = new Point2D(1, 4);
		Point2D p3 = new Point2D(2, 4);
		
		assertEquals(p1.getDistance(p1), 0, Shape2D.ACCURACY);
		assertEquals(p2.getDistance(p1), Math.sqrt(2), Shape2D.ACCURACY);
		assertEquals(p3.getDistance(p1), 1, Shape2D.ACCURACY);
		assertEquals(p2.getDistance(p2), 0, Shape2D.ACCURACY);
		assertEquals(p3.getDistance(p2), 1, Shape2D.ACCURACY);
		assertEquals(p3.getDistance(p3), 0, Shape2D.ACCURACY);
		
	}

	/*
	 * Test for double getDistance(double, double)
	 */
	public void testGetDistancedoubledouble() {
		Point2D p1 = new Point2D(2, 3);
		assertEquals(p1.getDistance(2, 3), 0, Shape2D.ACCURACY);
		assertEquals(p1.getDistance(1, 3), 1, Shape2D.ACCURACY);
		assertEquals(p1.getDistance(2, 4), 1, Shape2D.ACCURACY);
		assertEquals(p1.getDistance(2, 2), 1, Shape2D.ACCURACY);
		assertEquals(p1.getDistance(1, 2), Math.sqrt(2), Shape2D.ACCURACY);
		assertEquals(p1.getDistance(0, 0), Math.sqrt(13), Shape2D.ACCURACY);
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
		assertTrue(!p1.equals(p3));
		assertTrue(!p1.equals(p4));
		assertTrue(!p1.equals(p5));
	}

}
