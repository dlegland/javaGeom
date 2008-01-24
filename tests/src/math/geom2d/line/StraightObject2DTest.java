/*
 * File : StraightObject2DTest.java
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

import math.geom2d.Point2D;
import junit.framework.TestCase;

/**
 * @author Legland
 */
public class StraightObject2DTest extends TestCase {

	/**
	 * Constructor for StraightObject2DTest.
	 * @param arg0
	 */
	public StraightObject2DTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(StraightObject2DTest.class);
	}
	
	
	/*
	 * Test for boolean contains(double, double)
	 * Make tests with instance of StaightLine2D, since StraightObject2D is abstract
	 */
	public void testContainsdoubledouble() {
		StraightLine2D line;
		
		line = new StraightLine2D(1, 2, 1, 1);
		if(!line.contains(2, 3)) fail();
		if(line.contains(1, 3)) fail();
		if(line.contains(2, 2)) fail();
		
		line = new StraightLine2D(1, 2, 1, 0);
		if(line.contains(2, 3)) fail();
		if(line.contains(1, 3)) fail();
		if(!line.contains(2, 2)) fail();

		line = new StraightLine2D(1, 2, 0, 1);
		if(line.contains(2, 3)) fail();
		if(!line.contains(1, 3)) fail();
		if(line.contains(2, 2)) fail();		
	}

	/*
	 * Test for boolean contains(java.awt.geom.Point2D)
	 * Make tests with instance of StaightLine2D, since StraightObject2D is abstract
	 */
	public void testContainsPoint2D() {
		StraightLine2D line;
		Point2D p1 = new Point2D(2, 3);
		Point2D p2 = new Point2D(1, 3);
		Point2D p3 = new Point2D(2, 2);

		line = new StraightLine2D(1, 2, 1, 1);
		if(!line.contains(p1)) fail();
		if(line.contains(p2)) fail();
		if(line.contains(p3)) fail();

		line = new StraightLine2D(1, 2, 1, 0);
		if(line.contains(p1)) fail();
		if(line.contains(p2)) fail();
		if(!line.contains(p3)) fail();

		line = new StraightLine2D(1, 2, 0, 1);
		if(line.contains(p1)) fail();
		if(!line.contains(p2)) fail();
		if(line.contains(p3)) fail();
	}

	
	public void testIsColinearStraightObject2D(){
		LineSegment2D edge1 = new LineSegment2D(1, 2, 3, 5);
		LineSegment2D edge2 = new LineSegment2D(1, 2, 3, 6);
		LineSegment2D edge3 = new LineSegment2D(0, 0, 2, 3);
		LineSegment2D edge4 = new LineSegment2D(5, 8, 7, 11);
		
		assertTrue(!edge1.isColinear(edge2));
		assertTrue(!edge1.isColinear(edge3));
		assertTrue(edge1.isColinear(edge4));
		assertTrue(!edge2.isColinear(edge3));
		assertTrue(!edge2.isColinear(edge4));
		assertTrue(!edge3.isColinear(edge4));
		
		StraightLine2D line1 = new StraightLine2D(1, 2, 2, 3);
		StraightLine2D line2 = new StraightLine2D(1, 2, -2, -3);
		StraightLine2D line3 = new StraightLine2D(3, 5, 2, 3);
		
		assertTrue(line1.isColinear(line2));
		assertTrue(line1.isColinear(line3));
		assertTrue(line2.isColinear(line3));

		assertTrue(edge1.isColinear(line1));
		assertTrue(edge1.isColinear(line2));
		assertTrue(edge1.isColinear(line3));
		assertTrue(edge4.isColinear(line1));
		assertTrue(edge4.isColinear(line2));
		assertTrue(edge4.isColinear(line3));
	}

	public void testIsInside(){
		StraightLine2D line1 = new StraightLine2D(2, 2, 1, 0);
		StraightLine2D line2 = new StraightLine2D(2, 2, 1, 1);
		StraightLine2D line3 = new StraightLine2D(2, 2, 0, 1);

		Point2D p1 = new Point2D(5, 1); 
		Point2D p2 = new Point2D(5, 3); 
		Point2D p3 = new Point2D(3, 5); 
		Point2D p4 = new Point2D(1, 5);
		
		assertTrue(!line1.isInside(p1));
		assertTrue(!line2.isInside(p1));
		assertTrue(!line3.isInside(p1));
		assertTrue(line1.isInside(p2));
		assertTrue(!line2.isInside(p2));
		assertTrue(!line3.isInside(p2));
		assertTrue(line1.isInside(p3));
		assertTrue(line2.isInside(p3));
		assertTrue(!line3.isInside(p3));
		assertTrue(line1.isInside(p4));
		assertTrue(line2.isInside(p4));
		assertTrue(line3.isInside(p4));
	}

	public void testGetSymmetricPoint2D(){
		StraightLine2D line1 = new StraightLine2D(2, 2, 1, 0);
		StraightLine2D line2 = new StraightLine2D(2, 2, 1, 1);
		StraightLine2D line3 = new StraightLine2D(2, 2, 0, 1);
		StraightLine2D line4 = new StraightLine2D(2, 2, 3, 1);

		Point2D p1 = new Point2D(5, 1); 
		Point2D p2 = new Point2D(5, 3); 
		Point2D p3 = new Point2D(3, 5); 
		Point2D p4 = new Point2D(2, 2);
		
		assertEquals(line1.getSymmetric(p1), new Point2D(5, 3));
		assertEquals(line1.getSymmetric(p2), new Point2D(5, 1));
		assertEquals(line1.getSymmetric(p3), new Point2D(3, -1));
		assertEquals(line1.getSymmetric(p4), new Point2D(2, 2));
		
		assertEquals(line2.getSymmetric(p1), new Point2D(1, 5));
		assertEquals(line2.getSymmetric(p2), new Point2D(3, 5));
		assertEquals(line2.getSymmetric(p3), new Point2D(5, 3));
		assertEquals(line2.getSymmetric(p4), new Point2D(2, 2));

		assertEquals(line3.getSymmetric(p1), new Point2D(-1, 1));
		assertEquals(line3.getSymmetric(p2), new Point2D(-1, 3));
		assertEquals(line3.getSymmetric(p3), new Point2D(1, 5));
		assertEquals(line3.getSymmetric(p4), new Point2D(2, 2));

		p1.setLocation(6, 0);
		assertEquals(line4.getSymmetric(p1), new Point2D(4, 6));
		//assertEquals(line4.getSymmetric(p2), new Point2D(-1, 3));
		//assertEquals(line4.getSymmetric(p3), new Point2D(1, 5));
		//assertEquals(line4.getSymmetric(p4), new Point2D(2, 2));

	}
}
