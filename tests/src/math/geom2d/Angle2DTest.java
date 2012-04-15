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
		
		double angle;
		
		angle = Angle2D.horizontalAngle(line1);
		assertEquals(Math.PI/4, angle, 1e-14);
		
		angle = Angle2D.horizontalAngle(line2);
		assertEquals(3*Math.PI/4, angle, 1e-14);
		
		angle = Angle2D.horizontalAngle(line3);
		assertEquals(Math.PI/2, angle, 1e-14);
		
		angle = Angle2D.horizontalAngle(line4);
		assertEquals(0, angle, 1e-14);
	}

	/*
	 * Test method for 'math.geom2d.Angle2D.getHorizontalAngle(double, double, double, double)'
	 */
	public void testGetHorizontalAngleDoubleDoubleDoubleDouble() {
		double angle;
		
		angle = Angle2D.horizontalAngle(1, 2, 2, 2);
		assertEquals(0, angle, 1e-14);
		
		angle = Angle2D.horizontalAngle(1, 2, 2, 3);
		assertEquals(Math.PI/4, angle, 1e-14);
		
		angle = Angle2D.horizontalAngle(1, 2, 1, 3);
		assertEquals(Math.PI/2, angle, 1e-14);
		
		angle = Angle2D.horizontalAngle(1, 2, -1, 4);
		assertEquals(3*Math.PI/4, angle, 1e-14);
		
		angle = Angle2D.horizontalAngle(1, 2, -2, -1);
		assertEquals(5*Math.PI/4, angle, 1e-14);
	}
	
	/**
	 * Tests with one central point, and 8 points all around.
	 */
	public void testGetPseudoAnglePoint2DPoint2D() {
	    double tx = 5;
	    double ty = 5;	    
	    double eps = 1e-14;
	    double angle;
	    
        Point2D p0 = new Point2D(10, 10);
        
        Point2D p1 = new Point2D(10+tx, 10);
        angle = Angle2D.pseudoAngle(p0, p1);
        assertEquals(0, angle, eps);
        
        Point2D p2 = new Point2D(10+tx, 10+ty);
        angle = Angle2D.pseudoAngle(p0, p2);
        assertEquals(45, angle, eps);
        
        Point2D p3 = new Point2D(10, 10+ty);
        angle = Angle2D.pseudoAngle(p0, p3);
        assertEquals(90, angle, eps);
        
        Point2D p4 = new Point2D(10-tx, 10+ty);
        angle =  Angle2D.pseudoAngle(p0, p4);
        assertEquals(135, angle, eps);
        
        Point2D p5 = new Point2D(10-tx, 10);
        angle = Angle2D.pseudoAngle(p0, p5); 
        assertEquals(180, angle, eps);
        
        Point2D p6 = new Point2D(10-tx, 10-ty);
        angle =  Angle2D.pseudoAngle(p0, p6);
        assertEquals(225, angle, eps);
        
        Point2D p7 = new Point2D(10, 10-ty);
        angle = Angle2D.pseudoAngle(p0, p7);
        assertEquals(270, angle, eps);
        
        Point2D p8 = new Point2D(10+tx, 10-ty);
        angle = Angle2D.pseudoAngle(p0, p8);
        assertEquals(315, angle, eps);
	}
	
    /**
     * Test Angle2D.getHorizontalAngle(Point2D) with all multiple of pi/4.
     */
    public void testGetAngle() {
        double eps = 1e-14;
        double angle;
        
        Point2D p1 = new Point2D(10, 0);
        angle = Angle2D.horizontalAngle(p1);
        assertEquals(0, angle, eps);
        
        Point2D p2 = new Point2D(10, 10);
        assertEquals(Angle2D.horizontalAngle(p2), Math.PI/4, eps);
        
        Point2D p3 = new Point2D(0, 10);
        assertEquals(Angle2D.horizontalAngle(p3), Math.PI/2, eps);
        
        Point2D p4 = new Point2D(-10, 10);
        assertEquals(Angle2D.horizontalAngle(p4), 3*Math.PI/4, eps);
        
        Point2D p5 = new Point2D(-10, 0);
        assertEquals(Angle2D.horizontalAngle(p5), Math.PI, eps);
        
        Point2D p6 = new Point2D(-10, -10);
        assertEquals(Angle2D.horizontalAngle(p6), 5*Math.PI/4, eps);
        
        Point2D p7 = new Point2D(0, -10);
        assertEquals(Angle2D.horizontalAngle(p7), 3*Math.PI/2, eps);
        
        Point2D p8 = new Point2D(10, -10);
        assertEquals(Angle2D.horizontalAngle(p8), 7*Math.PI/4, eps);
    }


	/*
	 * Test method for 'math.geom2d.Angle2D.getAngle(StraightObject2D, StraightObject2D)'
	 */
	public void testGetAngleStraightObject2DStraightObject2D() {
		StraightLine2D line1 = new StraightLine2D(0, 0, 1, 1);
		StraightLine2D line2 = new StraightLine2D(0, 0, -1, 1);
		assertEquals(Angle2D.angle(line1, line2), Math.PI/2, 1e-14);
		assertEquals(Angle2D.angle(line2, line1), 3*Math.PI/2, 1e-14);
	}

	/*
	 * Test method for 'math.geom2d.Angle2D.getAngle(Point2D, Point2D, Point2D)'
	 */
	public void testGetAnglePoint2DPoint2DPoint2D() {
		Point2D p1, p2, p3;
		p1 = new Point2D(0, 0);
		p2 = new Point2D(50, 50);
		p3 = new Point2D(100, 50);
				
		double angle;
		
		angle = Angle2D.angle(p1, p2, p3);
		assertEquals(Math.PI*3/4, angle, Shape2D.ACCURACY);
		angle = Angle2D.angle(p3, p2, p1);
		assertEquals(Math.PI*5/4, angle, Shape2D.ACCURACY);

		p1 = new Point2D(0, 50);
		angle = Angle2D.angle(p1, p2, p3);
		assertEquals(Math.PI, angle, Shape2D.ACCURACY);
		angle = Angle2D.angle(p3, p2, p1);
		assertEquals(Math.PI, angle, Shape2D.ACCURACY);

		// Aligned points
		p1 = new Point2D(50, 0);
		p3 = new Point2D(50, 100);
		angle = Angle2D.angle(p1, p2, p3);
		assertEquals(Math.PI, angle, Shape2D.ACCURACY);
		angle = Angle2D.angle(p3, p2, p1);
		assertEquals(Math.PI, angle, Shape2D.ACCURACY);

		p3 = new Point2D(50, 0);
		angle = Angle2D.angle(p1, p2, p3);
		assertEquals(0, angle, Shape2D.ACCURACY);
		angle = Angle2D.angle(p3, p2, p1);
		assertEquals(0, angle, Shape2D.ACCURACY);
	}

	public void testGetAngleDoubles() {
		double x1 = 0; 
		double y1 = 0;
		double x2 = 50; 
		double y2 = 50;
		double x3 = 100; 
		double y3 = 50;
				
		double angle;
		
		angle = Angle2D.angle(x1, y1, x2, y2, x3, y3);
		assertEquals(Math.PI*3/4, angle, Shape2D.ACCURACY);
		angle = Angle2D.angle(x3, y3, x2, y2, x1, y1);
		assertEquals(Math.PI*5/4, angle, Shape2D.ACCURACY);

		x1 = 0;
		y1 = 50;
		angle = Angle2D.angle(x1, y1, x2, y2, x3, y3);
		assertEquals(Math.PI, angle, Shape2D.ACCURACY);
		angle = Angle2D.angle(x3, y3, x2, y2, x1, y1);
		assertEquals(Math.PI, angle, Shape2D.ACCURACY);

		// Aligned points
		x1 = 50;
		y1 = 0;
		x3 = 50;
		y3 = 100;
		angle = Angle2D.angle(x1, y1, x2, y2, x3, y3);
		assertEquals(Math.PI, angle, Shape2D.ACCURACY);
		angle = Angle2D.angle(x3, y3, x2, y2, x1, y1);
		assertEquals(Math.PI, angle, Shape2D.ACCURACY);

		x3 = 50;
		y3 = 0;
		angle = Angle2D.angle(x1, y1, x2, y2, x3, y3);
		assertEquals(0, angle, Shape2D.ACCURACY);
		angle = Angle2D.angle(x3, y3, x2, y2, x1, y1);
		assertEquals(0, angle, Shape2D.ACCURACY);
	}

	/*
	 * Test method for 'math.geom2d.Angle2D.getAbsoluteAngle(Point2D, Point2D, Point2D)'
	 */
	public void testGetAbsoluteAnglePoint2DPoint2DPoint2D() {
		Point2D p1, p2, p3;
		p1 = new Point2D(0, 0);
		p2 = new Point2D(50, 50);
		p3 = new Point2D(100, 50);
				
		assertEquals(Angle2D.absoluteAngle(p1, p2, p3), Math.PI*3/4, Shape2D.ACCURACY);
		assertEquals(Angle2D.absoluteAngle(p3, p2, p1), Math.PI*3/4, Shape2D.ACCURACY);
		
		p1 = new Point2D(0, 50);
		assertEquals(Angle2D.absoluteAngle(p1, p2, p3), Math.PI, Shape2D.ACCURACY);
		assertEquals(Angle2D.absoluteAngle(p3, p2, p1), Math.PI, Shape2D.ACCURACY);

		p1 = new Point2D(50, 0);
		p3 = new Point2D(50, 100);
		assertEquals(Angle2D.absoluteAngle(p1, p2, p3), Math.PI, Shape2D.ACCURACY);
		assertEquals(Angle2D.absoluteAngle(p3, p2, p1), Math.PI, Shape2D.ACCURACY);

		p3 = new Point2D(50, 0);
		assertEquals(Angle2D.absoluteAngle(p1, p2, p3), 0, Shape2D.ACCURACY);
		assertEquals(Angle2D.absoluteAngle(p3, p2, p1), 0, Shape2D.ACCURACY);
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
