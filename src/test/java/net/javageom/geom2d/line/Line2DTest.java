/*
 * File : LineObject2DTest.java
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
import net.javageom.geom2d.Box2D;
import net.javageom.geom2d.Point2D;
import net.javageom.geom2d.Shape2D;

/**
 * @author Legland
 */
public class Line2DTest extends TestCase {

	/**
	 * Constructor for LineObject2DTest.
	 * @param arg0
	 */
	public Line2DTest(String arg0) {
		super(arg0);
	}

	public void testGetParallelDouble() {
    	Point2D p1 = new Point2D(1, 1);
    	Point2D p2 = new Point2D(1, 3);
    	Line2D line1 = new Line2D(p1, p2);
    	
    	Point2D p1p = new Point2D(2, 1);
    	Point2D p2p = new Point2D(2, 3);
    	Line2D line1p = new Line2D(p1p, p2p);
    	
    	assertTrue(line1.parallel(1).equals(line1p));
    }

	/*
	 * Test for double getDistance(Point2D)
	 */
	public void testInstanceOfLinearShape2D() {
		Point2D p1 = new Point2D(2, 3);
		Point2D p2 = new Point2D(4, 7);
		Line2D line = new Line2D(p1, p2);
		assertTrue(LinearShape2D.class.isInstance(line));
	}
	
    public void testIsColinear() {
        // lines roughly horizontal
        Line2D lineA1 = new Line2D(new Point2D(2, 1), new Point2D(6, 3));
        Line2D lineA2 = new Line2D(new Point2D(6, 3), new Point2D(8, 4));
        Line2D lineA3 = new Line2D(new Point2D(6, 4), new Point2D(8, 5));
        assertTrue(lineA1.isColinear(lineA2));
        assertTrue(lineA2.isColinear(lineA1));
        assertFalse(lineA1.isColinear(lineA3));

        // lines roughly vertical
        Line2D lineB1 = new Line2D(new Point2D(1, 2), new Point2D(3, 6));
        Line2D lineB2 = new Line2D(new Point2D(3, 6), new Point2D(4, 8));
        Line2D lineB3 = new Line2D(new Point2D(4, 6), new Point2D(5, 8));
        assertTrue(lineB1.isColinear(lineB2));
        assertTrue(lineB2.isColinear(lineB1));
        assertFalse(lineB1.isColinear(lineB3));
    }

    public void testIsParallel() {
        // lines roughly horizontal
        Line2D lineA1 = new Line2D(new Point2D(2, 1), new Point2D(6, 3));
        Line2D lineA2 = new Line2D(new Point2D(6, 3), new Point2D(8, 4));
        Line2D lineA3 = new Line2D(new Point2D(6, 4), new Point2D(8, 5));
        assertTrue(lineA1.isParallel(lineA2));
        assertTrue(lineA2.isParallel(lineA1));
        assertTrue(lineA1.isParallel(lineA3));

        // lines roughly vertical
        Line2D lineB1 = new Line2D(new Point2D(1, 2), new Point2D(3, 6));
        Line2D lineB2 = new Line2D(new Point2D(3, 6), new Point2D(4, 8));
        Line2D lineB3 = new Line2D(new Point2D(4, 6), new Point2D(5, 8));
        assertTrue(lineB1.isParallel(lineB2));
        assertTrue(lineB2.isParallel(lineB1));
        assertTrue(lineB1.isParallel(lineB3));
    }

    public void testIntersects(){
        Point2D p1 = new Point2D(10, 10);
        Point2D p2 = new Point2D(20, 30);
        Point2D p3 = new Point2D(00, 30);
        Point2D p4 = new Point2D(40, 10);
        Point2D p5 = new Point2D(15, 20);
        Point2D p6 = new Point2D(30, 00);
        
        Line2D edge1 = new Line2D(p1, p2);
        Line2D edge2 = new Line2D(p3, p4);
        Line2D edge3 = new Line2D(p5, p6);
        
        assertTrue(Line2D.intersects(edge1, edge2));
        assertTrue(Line2D.intersects(edge2, edge1));
        assertTrue(Line2D.intersects(edge1, edge3));
        assertTrue(Line2D.intersects(edge3, edge1));
        assertFalse(Line2D.intersects(edge2, edge3));
        assertFalse(Line2D.intersects(edge3, edge2));
    }

    public void testContainsdoubledouble() {
        Line2D edge;
        
        // diagonal edge
        edge = new Line2D(1, 1, 3, 2);
        assertTrue(edge.contains(1, 1));
        assertTrue(edge.contains(3, 2));
        assertTrue(edge.contains(2, 1.5));
        assertFalse(edge.contains(0, 0));
        assertFalse(edge.contains(-1, 0));
        assertFalse(edge.contains(5, 3));
        
        // horizontal edge
        edge = new Line2D(1, 1, 3, 1);
        assertTrue(edge.contains(1, 1));
        assertTrue(edge.contains(3, 1));
        assertTrue(edge.contains(2, 1));
        assertFalse(edge.contains(0, 0));
        assertFalse(edge.contains(0, 1));
        assertFalse(edge.contains(4, 1));

        // vertical edge
        edge = new Line2D(1, 1, 1, 3);
        assertTrue(edge.contains(1, 1));
        assertTrue(edge.contains(1, 3));
        assertTrue(edge.contains(1, 2));
        assertFalse(edge.contains(0, 0));
        assertFalse(edge.contains(1, 0));
        assertFalse(edge.contains(1, 4));
    }

    public void testGetDistanceDoubleDouble() {
        
        // basic
        Line2D edge = new Line2D(new Point2D(1, 1), new Point2D(4, 3));
        
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
        edge = new Line2D(new Point2D(1, 1), new Point2D(4, 1));
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
        edge = new Line2D(new Point2D(1, 1), new Point2D(1, 4));
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
     * Test for Point2D getIntersection(StraightObject2D)
     */
    public void testGetIntersectionStraightObject2D(){
        Line2D line1 = new Line2D(1, 1, 3, 2);
        Line2D line2 = new Line2D(1, 1, 0, 4);
        assertTrue(line1.intersection(line2).equals(new Point2D(1, 1)));
        assertTrue(line2.intersection(line1).equals(new Point2D(1, 1)));
        
        Line2D line3 = new Line2D(3, 2, 0, 4);
        assertTrue(line1.intersection(line3).equals(new Point2D(3, 2)));
        assertTrue(line3.intersection(line1).equals(new Point2D(3, 2)));
        assertTrue(line2.intersection(line3).equals(new Point2D(0, 4)));
        assertTrue(line3.intersection(line2).equals(new Point2D(0, 4)));
        
        Line2D line4 = new Line2D(0, 0, 5, 1);
        assertEquals(line1.intersection(line4), null);
        assertEquals(line2.intersection(line4), null);
        assertEquals(line3.intersection(line4), null);
        
        line1 = new Line2D(1, 1, 5, 5);
        line2 = new Line2D(1, 5, 5, 1);
        assertTrue(line1.intersection(line2).equals(new Point2D(3, 3)));
        assertTrue(line2.intersection(line1).equals(new Point2D(3, 3)));
    }

    public void testGetBoundingBox() {
        // diagonal edge
        Line2D edge = new Line2D(1, 1, 3, 2);
        Box2D box = edge.boundingBox();
        assertTrue(box.equals(new Box2D(1, 3, 1, 2)));
    }

    public void testGetReverseCurve() {
        Line2D edge = new Line2D(1, 1, 3, 2);
        assertEquals(edge.reverse(), new Line2D(3, 2, 1, 1));
    }

    public void testGetWindingAngle() {
		Point2D p1 = new Point2D(2, 2);
		Point2D p2 = new Point2D(0, 2);
		Point2D pr = new Point2D(1, 1);
		
		Line2D line1 = new Line2D(p1, p2);
		assertEquals(line1.windingAngle(pr), Math.PI/2, Shape2D.ACCURACY);
	}
    
    public void testConstructorSamePoint() {
    	Point2D p1 = new Point2D(5000, 6000);
		Point2D p2 = new Point2D(5000, 6000);
		try {
			@SuppressWarnings("unused")
			StraightLine2D line = new StraightLine2D(p1, p2);
			assertTrue(false);
		} catch (RuntimeException ex) {
			
		}
    }
    
    public void testEquals() {
        Line2D line1 = new Line2D(10, 20, 30, 40);
        Line2D line2 = new Line2D(new Point2D(10, 20), new Point2D(30, 40));
        assertTrue(line1.equals(line1));
        assertTrue(line1.equals(line2));
        assertTrue(line2.equals(line1));
    }
    
    public void testCopyConstructor() {
        Line2D line = new Line2D(10, 20, 30, 40);
        Line2D copy = new Line2D(line);
        assertTrue(line.equals(copy));
    }
}
