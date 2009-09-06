/*
 * File : LineSegment2DTest.java
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

import java.util.Collection;

import junit.framework.TestCase;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Domain2D;

/**
 * @author Legland
 */
public class LineSegment2DTest extends TestCase {

	/**
	 * Constructor for Edge2DTest.
	 * @param arg0
	 */
	public LineSegment2DTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(LineSegment2DTest.class);
	}

	public void testGetBuffer() {
		// create a line segment, and computes its buffer
	    LineSegment2D line = new LineSegment2D(50, 100, 150, 100);
	    Domain2D buffer = line.getBuffer(20);
	    
	    // The buffer should be defined by a single boundary curve
	    Boundary2D boundary = buffer.getBoundary();
	    assertEquals(1, boundary.getContinuousCurves().size());
	    
	    // The contour is composed of 4 smooth pieces (2 line segments, and
	    // two circle arcs)
	    Collection<? extends SmoothCurve2D> smoothCurves =
	    	boundary.getContinuousCurves().iterator().next().getSmoothPieces();
	    assertTrue(smoothCurves.size()==4);
	}
	
	public void testGetOtherPoint() {
        Point2D p1 = new Point2D(10, 10);
        Point2D p2 = new Point2D(40, 50);
        Point2D p3 = new Point2D(20, 30);
        LineSegment2D edge = new LineSegment2D(p1, p2);
        
        assertEquals(p2, edge.getOtherPoint(p1));    
        assertEquals(p1, edge.getOtherPoint(p2));    
        assertEquals(null, edge.getOtherPoint(p3));    
	}

	public void testIsBounded() {
        Point2D p1 = new Point2D(10, 10);
        Point2D p2 = new Point2D(40, 50);
        LineSegment2D edge = new LineSegment2D(p1, p2);
        assertTrue(edge.isBounded());    
	}

	public void testIntersects(){
		Point2D p1 = new Point2D(10, 10);
		Point2D p2 = new Point2D(20, 30);
		Point2D p3 = new Point2D(00, 30);
		Point2D p4 = new Point2D(40, 10);
		Point2D p5 = new Point2D(15, 20);
		Point2D p6 = new Point2D(30, 00);
		
		LineSegment2D edge1 = new LineSegment2D(p1, p2);
		LineSegment2D edge2 = new LineSegment2D(p3, p4);
		LineSegment2D edge3 = new LineSegment2D(p5, p6);
		
		assertTrue(LineSegment2D.intersects(edge1, edge2));
		assertTrue(LineSegment2D.intersects(edge2, edge1));
		assertTrue(LineSegment2D.intersects(edge1, edge3));
		assertTrue(LineSegment2D.intersects(edge3, edge1));
		assertTrue(!LineSegment2D.intersects(edge2, edge3));
		assertTrue(!LineSegment2D.intersects(edge3, edge2));
	}
	
    public void testIsColinear() {
        // lines roughly horizontal
        LineSegment2D lineA1 = new LineSegment2D(new Point2D(2, 1), new Point2D(6, 3));
        LineSegment2D lineA2 = new LineSegment2D(new Point2D(6, 3), new Point2D(8, 4));
        LineSegment2D lineA3 = new LineSegment2D(new Point2D(6, 4), new Point2D(8, 5));
        assertTrue(lineA1.isColinear(lineA2));
        assertTrue(lineA2.isColinear(lineA1));
        assertFalse(lineA1.isColinear(lineA3));

        // lines roughly vertical
        LineSegment2D lineB1 = new LineSegment2D(new Point2D(1, 2), new Point2D(3, 6));
        LineSegment2D lineB2 = new LineSegment2D(new Point2D(3, 6), new Point2D(4, 8));
        LineSegment2D lineB3 = new LineSegment2D(new Point2D(4, 6), new Point2D(5, 8));
        assertTrue(lineB1.isColinear(lineB2));
        assertTrue(lineB2.isColinear(lineB1));
        assertFalse(lineB1.isColinear(lineB3));
    }

    public void testIsParallel() {
        // lines roughly horizontal
        LineSegment2D lineA1 = new LineSegment2D(new Point2D(2, 1), new Point2D(6, 3));
        LineSegment2D lineA2 = new LineSegment2D(new Point2D(6, 3), new Point2D(8, 4));
        LineSegment2D lineA3 = new LineSegment2D(new Point2D(6, 4), new Point2D(8, 5));
        assertTrue(lineA1.isParallel(lineA2));
        assertTrue(lineA2.isParallel(lineA1));
        assertTrue(lineA1.isParallel(lineA3));

        // lines roughly vertical
        LineSegment2D lineB1 = new LineSegment2D(new Point2D(1, 2), new Point2D(3, 6));
        LineSegment2D lineB2 = new LineSegment2D(new Point2D(3, 6), new Point2D(4, 8));
        LineSegment2D lineB3 = new LineSegment2D(new Point2D(4, 6), new Point2D(5, 8));
        assertTrue(lineB1.isParallel(lineB2));
        assertTrue(lineB2.isParallel(lineB1));
        assertTrue(lineB1.isParallel(lineB3));
    }
    
	public void testGetLength() {
        Point2D p1 = new Point2D(10, 10);
        Point2D p2 = new Point2D(40, 50);
        LineSegment2D edge = new LineSegment2D(p1, p2);
        assertEquals(50, edge.getLength(), 1e-14);    
	}
	
    public void testGetParallelDouble() {
    	Point2D p1 = new Point2D(1, 1);
    	Point2D p2 = new Point2D(1, 3);
    	LineSegment2D line1 = new LineSegment2D(p1, p2);
    	
    	Point2D p1p = new Point2D(2, 1);
    	Point2D p2p = new Point2D(2, 3);
    	LineSegment2D line1p = new LineSegment2D(p1p, p2p);
    	
    	assertTrue(line1.getParallel(1).equals(line1p));
    }

	/*
	 * Test for double getDistance(Point2D)
	 */
	public void testGetDistanceDoubleDouble() {
		
		// basic
		LineSegment2D edge = new LineSegment2D(1, 1, 4, 3);
		
		assertEquals(edge.getDistance(1, 1), 0, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2.5, 2), 0, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(4, 3), 0, Shape2D.ACCURACY);
	
		double d1 = Math.sqrt(13)/2;
		assertEquals(edge.getDistance(-.5, 0), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2, -.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(3.5, .5), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(5, 1.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(5.5, 4), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(3, 4.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(1.5, 3.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(0, 2.5), d1, Shape2D.ACCURACY);

		double d2 = Math.sqrt(26)/2;
		assertEquals(edge.getDistance(0.5, -1.5), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(-1.5, 1.5), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(6.5, 2.5), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(4.5, 5.5), d2, Shape2D.ACCURACY);
		
		// horizontal edge
		edge = new LineSegment2D(1, 1, 4, 1);
		assertEquals(edge.getDistance(1, 1), 0, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2.5, 1), 0, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(4, 1), 0, Shape2D.ACCURACY);
		
		d1 = 1;
		assertEquals(edge.getDistance(0, 1), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(5, 1), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(1, 0), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2.5, 0), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(4, 0), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(1, 2), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2.5, 2), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(4, 2), d1, Shape2D.ACCURACY);
		
		d2=Math.sqrt(2);
		assertEquals(edge.getDistance(0, 0), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(0, 2), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(5, 0), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(5, 2), d2, Shape2D.ACCURACY);
		
		// vertical edge
		edge = new LineSegment2D(1, 1, 1, 4);
		assertEquals(edge.getDistance(1, 1), 0, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(1, 2.5), 0, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(1, 4), 0, Shape2D.ACCURACY);
		
		assertEquals(edge.getDistance(1, 0), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(1, 5), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(0, 1), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(0, 2.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(0, 4), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2, 1), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2, 2.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2, 4), d1, Shape2D.ACCURACY);

		assertEquals(edge.getDistance(0, 0), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2, 0), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2, 5), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(0, 5), d2, Shape2D.ACCURACY);
	}

	/*
	 * Test for double getSignedDistance(Point2D)
	 */
	public void testGetSignedDistancePoint2D() {
	}

	public void testGetEdgeAngleLineSegment2DLineSegment2D(){
		Point2D p1, p2, p3;
		p1 = new Point2D(0, 0);
		p2 = new Point2D(50, 50);
		p3 = new Point2D(100, 50);
		
		LineSegment2D edge1d = new LineSegment2D(p1, p2);
		LineSegment2D edge1i = new LineSegment2D(p2, p1);
		LineSegment2D edge2d = new LineSegment2D(p2, p3);
		LineSegment2D edge2i = new LineSegment2D(p3, p2);
		
		assertEquals(LineSegment2D.getEdgeAngle(edge1d, edge2d), Math.PI*3/4, Shape2D.ACCURACY);
		assertEquals(LineSegment2D.getEdgeAngle(edge1d, edge2i), Math.PI*3/4, Shape2D.ACCURACY);
		assertEquals(LineSegment2D.getEdgeAngle(edge1i, edge2d), Math.PI*3/4, Shape2D.ACCURACY);
		assertEquals(LineSegment2D.getEdgeAngle(edge1i, edge2i), Math.PI*3/4, Shape2D.ACCURACY);

		assertEquals(LineSegment2D.getEdgeAngle(edge2d, edge1d), Math.PI*5/4, Shape2D.ACCURACY);
		assertEquals(LineSegment2D.getEdgeAngle(edge2d, edge1i), Math.PI*5/4, Shape2D.ACCURACY);
		assertEquals(LineSegment2D.getEdgeAngle(edge2i, edge1d), Math.PI*5/4, Shape2D.ACCURACY);
		assertEquals(LineSegment2D.getEdgeAngle(edge2i, edge1i), Math.PI*5/4, Shape2D.ACCURACY);
	}
	
	/*
	 * Test for Point2D getIntersection(StraightObject2D)
	 */
	public void testGetIntersectionStraightObject2D(){
		LineSegment2D edge1 = new LineSegment2D(1, 1, 3, 2);
		LineSegment2D edge2 = new LineSegment2D(1, 1, 0, 4);
		assertTrue(edge1.getIntersection(edge2).equals(new Point2D(1, 1)));
		assertTrue(edge2.getIntersection(edge1).equals(new Point2D(1, 1)));
		
		LineSegment2D edge3 = new LineSegment2D(3, 2, 0, 4);
		assertTrue(edge1.getIntersection(edge3).equals(new Point2D(3, 2)));
		assertTrue(edge3.getIntersection(edge1).equals(new Point2D(3, 2)));
		assertTrue(edge2.getIntersection(edge3).equals(new Point2D(0, 4)));
		assertTrue(edge3.getIntersection(edge2).equals(new Point2D(0, 4)));
		
		LineSegment2D edge4 = new LineSegment2D(0, 0, 5, 1);
		assertEquals(edge1.getIntersection(edge4), null);
		assertEquals(edge2.getIntersection(edge4), null);
		assertEquals(edge3.getIntersection(edge4), null);
		
		edge1 = new LineSegment2D(1, 1, 5, 5);
		edge2 = new LineSegment2D(1, 5, 5, 1);
		assertTrue(edge1.getIntersection(edge2).equals(new Point2D(3, 3)));
		assertTrue(edge2.getIntersection(edge1).equals(new Point2D(3, 3)));
	}

	/*
	 * Test for boolean contains(double, double)
	 */
	public void testContainsdoubledouble() {
		LineSegment2D edge;
		
		// diagonal edge
		edge = new LineSegment2D(1, 1, 3, 2);
		assertTrue(edge.contains(1, 1));
		assertTrue(edge.contains(3, 2));
		assertTrue(edge.contains(2, 1.5));
		assertFalse(edge.contains(0, 0));
		assertFalse(edge.contains(-1, 0));
		assertFalse(edge.contains(5, 3));
		
		// horizontal edge
		edge = new LineSegment2D(1, 1, 3, 1);
		assertTrue(edge.contains(1, 1));
		assertTrue(edge.contains(3, 1));
		assertTrue(edge.contains(2, 1));
		assertFalse(edge.contains(0, 0));
		assertFalse(edge.contains(0, 1));
		assertFalse(edge.contains(4, 1));

		// vertical edge
		edge = new LineSegment2D(1, 1, 1, 3);
		assertTrue(edge.contains(1, 1));
		assertTrue(edge.contains(1, 3));
		assertTrue(edge.contains(1, 2));
		assertFalse(edge.contains(0, 0));
		assertFalse(edge.contains(1, 0));
		assertFalse(edge.contains(1, 4));
	}

	public void testGetBoundingBox() {
		LineSegment2D edge;		

		// diagonal edge
		edge = new LineSegment2D(1, 1, 3, 2);
		Box2D box = edge.getBoundingBox();
		assertTrue(box.equals(new Box2D(1, 3, 1, 2)));
	}
	
	public void testGetReverseCurve() {
	    LineSegment2D edge = new LineSegment2D(1, 1, 3, 2);
	    assertEquals(edge.getReverseCurve(), new LineSegment2D(3, 2, 1, 1));
	}

	public void testEquals(){
		LineSegment2D edge1 = new LineSegment2D(1, 2, 3, 4);
		assertTrue(edge1.equals(edge1));
		LineSegment2D edge2 = new LineSegment2D(3, 4, 1, 2);
		assertTrue(!edge1.equals(edge2));
		assertTrue(!edge2.equals(edge1));
		
		LineSegment2D edge3 = new LineSegment2D(1, 4, 3, 2);
		assertTrue(!edge1.equals(edge3));
		assertTrue(!edge2.equals(edge3));
		assertTrue(!edge3.equals(edge1));
		assertTrue(!edge3.equals(edge2));
		
		LineSegment2D edge4 = new LineSegment2D(1, 2, 3, 2);
		assertTrue(!edge1.equals(edge4));
		assertTrue(!edge2.equals(edge4));
		assertTrue(!edge4.equals(edge1));
		assertTrue(!edge4.equals(edge2));		
	}

	public void testGetViewAnglePoint2D(){
		
		Point2D p1 = new Point2D(1, 1);
		Point2D p2 = new Point2D(3, 1);
		Point2D p3 = new Point2D(1, 1);
		Point2D p4 = new Point2D(1, 3);
		
		LineSegment2D edge1 = new LineSegment2D(2, 0, 2, 2);
		assertEquals(edge1.getWindingAngle(p1), Math.PI/2, 1e-14);
		assertEquals(edge1.getWindingAngle(p2), -Math.PI/2, 1e-14);
		
		LineSegment2D edge2 = new LineSegment2D(2, 2, 2, 0);
		assertEquals(edge2.getWindingAngle(p1), -Math.PI/2, 1e-14);
		assertEquals(edge2.getWindingAngle(p2), Math.PI/2, 1e-14);

		LineSegment2D edge3 = new LineSegment2D(0, 2, 2, 2);
		assertEquals(edge3.getWindingAngle(p3), -Math.PI/2, 1e-14);
		assertEquals(edge3.getWindingAngle(p4), Math.PI/2, 1e-14);

		LineSegment2D edge4 = new LineSegment2D(2, 2, 0, 2);
		assertEquals(edge4.getWindingAngle(p3), Math.PI/2, 1e-14);
		assertEquals(edge4.getWindingAngle(p4), -Math.PI/2, 1e-14);
	}

	public void testProjectPoint2D(){
		// Test on a basic line segment (diagonal +1;+1)
		LineSegment2D line1 = new LineSegment2D(
				new Point2D(-1, 2), new Point2D(1, 4));
		Point2D point = new Point2D(1, 2);
		
		assertEquals(line1.project(point), .5, 1e-12);	

		
		// Create a line segment with (1,2)*d direction vector
		double x0 = 20;
		double y0 = 30;
		double d = 10;
		LineSegment2D line2 = new LineSegment2D(
				new Point2D(x0, y0), new Point2D(x0+d, y0+2*d));
		
		// Test points which project 'nicely' on the line segment
		Point2D p0_0 = new Point2D(x0, y0);
		Point2D p1_0 = new Point2D(x0+d, y0);
		Point2D p0_1 = new Point2D(x0, y0+d);
		Point2D p1_1 = new Point2D(x0+d, y0+d);
		Point2D p0_2 = new Point2D(x0, y0+2*d);
		Point2D p1_2 = new Point2D(x0+d, y0+2*d);
		
		assertEquals(line2.project(p0_0), 0, 1e-12);
		assertEquals(line2.project(p1_0), .2, 1e-12);
		assertEquals(line2.project(p0_1), .4, 1e-12);
		assertEquals(line2.project(p1_1), .6, 1e-12);
		assertEquals(line2.project(p0_2), .8, 1e-12);
		assertEquals(line2.project(p1_2), 1, 1e-12);
		
		// Test points which project on first point
		Point2D pl1 = new Point2D(x0, y0-d);
		Point2D pl2 = new Point2D(x0-d, y0);
		Point2D pl3 = new Point2D(x0+d, y0-d);
		assertEquals(line2.project(pl1), 0, 1e-12);
		assertEquals(line2.project(pl2), 0, 1e-12);
		assertEquals(line2.project(pl3), 0, 1e-12);
		
		// Test points which project on last point
		Point2D pu1 = new Point2D(x0+d, y0+3*d);
		Point2D pu2 = new Point2D(x0+2*d, y0+2*d);
		Point2D pu3 = new Point2D(x0, y0+3*d);
		assertEquals(line2.project(pu1), 1, 1e-12);
		assertEquals(line2.project(pu2), 1, 1e-12);
		assertEquals(line2.project(pu3), 1, 1e-12);
	}
	
	public void testClone() {
	    LineSegment2D edge = new LineSegment2D(10, 20, 30, 40);
	    assertTrue(edge.equals(edge.clone()));
	}
}
