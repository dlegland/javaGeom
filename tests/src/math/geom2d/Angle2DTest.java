/* file : Angle2DTest.java
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
 * Created on 2 déc. 2006
 *
 */
package math.geom2d;

import math.geom2d.line.StraightLine2D;
import junit.framework.TestCase;

public class Angle2DTest extends TestCase {

	/*
	 * Test method for 'math.geom2d.Angle2D.getHorizontalAngle(SraightObject2D)'
	 */
	public void testGetHorizontalAngleStraightObject2D() {
		StraightLine2D line1 = new StraightLine2D(1, 2, 2, 2);
		StraightLine2D line2 = new StraightLine2D(1, 2, -3, 3);
		StraightLine2D line3 = new StraightLine2D(1, 2, 0, 3);
		StraightLine2D line4 = new StraightLine2D(1, 2, 2, 0);
		
		assertEquals(Angle2D.getHorizontalAngle(line1), Math.PI/4, 1e-14);
		assertEquals(Angle2D.getHorizontalAngle(line2), 3*Math.PI/4, 1e-14);
		assertEquals(Angle2D.getHorizontalAngle(line3), Math.PI/2, 1e-14);
		assertEquals(Angle2D.getHorizontalAngle(line4), 0, 1e-14);

	}

	/*
	 * Test method for 'math.geom2d.Angle2D.getHorizontalAngle(double, double, double, double)'
	 */
	public void testGetHorizontalAngleDoubleDoubleDoubleDouble() {
		assertEquals(Angle2D.getHorizontalAngle(1, 2, 2, 2), 0, 1e-14);
		assertEquals(Angle2D.getHorizontalAngle(1, 2, 2, 3), Math.PI/4, 1e-14);
		assertEquals(Angle2D.getHorizontalAngle(1, 2, 1, 3), Math.PI/2, 1e-14);
		assertEquals(Angle2D.getHorizontalAngle(1, 2, -1, 4), 3*Math.PI/4, 1e-14);
		assertEquals(Angle2D.getHorizontalAngle(1, 2, -2, -1), 5*Math.PI/4, 1e-14);
	}
	
    /**
     * Test Angle2D.getHorizontalAngle(Point2D) with all multiple of pi/4.
     */
    public void testGetAngle() {
        double eps = 1e-14;
        
        Point2D p1 = new Point2D(10, 0);
        assertEquals(Angle2D.getHorizontalAngle(p1), 0, eps);
        
        Point2D p2 = new Point2D(10, 10);
        assertEquals(Angle2D.getHorizontalAngle(p2), Math.PI/4, eps);
        
        Point2D p3 = new Point2D(0, 10);
        assertEquals(Angle2D.getHorizontalAngle(p3), Math.PI/2, eps);
        
        Point2D p4 = new Point2D(-10, 10);
        assertEquals(Angle2D.getHorizontalAngle(p4), 3*Math.PI/4, eps);
        
        Point2D p5 = new Point2D(-10, 0);
        assertEquals(Angle2D.getHorizontalAngle(p5), Math.PI, eps);
        
        Point2D p6 = new Point2D(-10, -10);
        assertEquals(Angle2D.getHorizontalAngle(p6), 5*Math.PI/4, eps);
        
        Point2D p7 = new Point2D(0, -10);
        assertEquals(Angle2D.getHorizontalAngle(p7), 3*Math.PI/2, eps);
        
        Point2D p8 = new Point2D(10, -10);
        assertEquals(Angle2D.getHorizontalAngle(p8), 7*Math.PI/4, eps);
    }


	/*
	 * Test method for 'math.geom2d.Angle2D.getAngle(StraightObject2D, StraightObject2D)'
	 */
	public void testGetAngleStraightObject2DStraightObject2D() {
		StraightLine2D line1 = new StraightLine2D(0, 0, 1, 1);
		StraightLine2D line2 = new StraightLine2D(0, 0, -1, 1);
		assertEquals(Angle2D.getAngle(line1, line2), Math.PI/2, 1e-14);
		assertEquals(Angle2D.getAngle(line2, line1), 3*Math.PI/2, 1e-14);
	}

	/*
	 * Test method for 'math.geom2d.Angle2D.getAngle(Point2D, Point2D, Point2D)'
	 */
	public void testGetAnglePoint2DPoint2DPoint2D() {
		Point2D p1, p2, p3;
		p1 = new Point2D(0, 0);
		p2 = new Point2D(50, 50);
		p3 = new Point2D(100, 50);
				
		assertEquals(Angle2D.getAngle(p1, p2, p3), Math.PI*3/4, Shape2D.ACCURACY);
		assertEquals(Angle2D.getAngle(p3, p2, p1), Math.PI*5/4, Shape2D.ACCURACY);

		p1 = new Point2D(0, 50);
		assertEquals(Angle2D.getAngle(p1, p2, p3), Math.PI, Shape2D.ACCURACY);
		assertEquals(Angle2D.getAngle(p3, p2, p1), Math.PI, Shape2D.ACCURACY);

		p1 = new Point2D(50, 0);
		p3 = new Point2D(50, 100);
		assertEquals(Angle2D.getAngle(p1, p2, p3), Math.PI, Shape2D.ACCURACY);
		assertEquals(Angle2D.getAngle(p3, p2, p1), Math.PI, Shape2D.ACCURACY);

		p3 = new Point2D(50, 0);
		assertEquals(Angle2D.getAngle(p1, p2, p3), 0, Shape2D.ACCURACY);
		assertEquals(Angle2D.getAngle(p3, p2, p1), 0, Shape2D.ACCURACY);
	}

	/*
	 * Test method for 'math.geom2d.Angle2D.getAbsoluteAngle(Point2D, Point2D, Point2D)'
	 */
	public void testGetAbsoluteAnglePoint2DPoint2DPoint2D() {
		Point2D p1, p2, p3;
		p1 = new Point2D(0, 0);
		p2 = new Point2D(50, 50);
		p3 = new Point2D(100, 50);
				
		assertEquals(Angle2D.getAbsoluteAngle(p1, p2, p3), Math.PI*3/4, Shape2D.ACCURACY);
		assertEquals(Angle2D.getAbsoluteAngle(p3, p2, p1), Math.PI*3/4, Shape2D.ACCURACY);
		
		p1 = new Point2D(0, 50);
		assertEquals(Angle2D.getAbsoluteAngle(p1, p2, p3), Math.PI, Shape2D.ACCURACY);
		assertEquals(Angle2D.getAbsoluteAngle(p3, p2, p1), Math.PI, Shape2D.ACCURACY);

		p1 = new Point2D(50, 0);
		p3 = new Point2D(50, 100);
		assertEquals(Angle2D.getAbsoluteAngle(p1, p2, p3), Math.PI, Shape2D.ACCURACY);
		assertEquals(Angle2D.getAbsoluteAngle(p3, p2, p1), Math.PI, Shape2D.ACCURACY);

		p3 = new Point2D(50, 0);
		assertEquals(Angle2D.getAbsoluteAngle(p1, p2, p3), 0, Shape2D.ACCURACY);
		assertEquals(Angle2D.getAbsoluteAngle(p3, p2, p1), 0, Shape2D.ACCURACY);
	}


	/*
	 * Test method for 'math.geom2d.Angle2D.containsAngle(double, double, double, boolean)'
	 */
	public void testContainsAngleDoubleDoubleDoubleBoolean() {
		// direct arcs
		assertTrue(Angle2D.containsAngle(Math.PI/2, 3*Math.PI/2, Math.PI, true));
		assertTrue(!Angle2D.containsAngle(Math.PI/2, 3*Math.PI/2, 0, true));
		assertTrue(!Angle2D.containsAngle(3*Math.PI/2, Math.PI/2, Math.PI, true));
		assertTrue(Angle2D.containsAngle(3*Math.PI/2, Math.PI/2, 0, true));

		// indirect arcs
		assertTrue(Angle2D.containsAngle(Math.PI/2, 3*Math.PI/2, Math.PI, true));
		assertTrue(!Angle2D.containsAngle(Math.PI/2, 3*Math.PI/2, 0, true));
		assertTrue(!Angle2D.containsAngle(3*Math.PI/2, Math.PI/2, Math.PI, true));
		assertTrue(Angle2D.containsAngle(3*Math.PI/2, Math.PI/2, 0, true));
	}

}
