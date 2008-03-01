/* File Circle2DTest.java 
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
 */

package math.geom2d.conic;

import junit.framework.TestCase;

import java.util.*;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.curve.ContinuousOrientedCurve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.polygon.HRectangle2D;

/**
 * @author Legland
 *
 */
public class Circle2DTest extends TestCase {

	/**
	 * Constructor for Circle2DTest.
	 * @param arg0
	 */
	public Circle2DTest(String arg0) {
		super(arg0);
	}

	public void testContainsDoubleDouble() {
		Circle2D circle = new Circle2D(0, 0, 10);
		
		assertTrue(circle.contains(10, 0));
		assertTrue(circle.contains(-10, 0));
		assertTrue(circle.contains(0, 10));
		assertTrue(circle.contains(0, -10));
		assertTrue(!circle.contains(0, 0));
	}
	
	public void testIntersectsRectangle(){
		Circle2D circle = new Circle2D(0, 0, 10);
		// rectangle totally inside circle
		assertTrue(circle.intersects(0, 0, 1, 1));
		
		// circle totally inside rectangle
		assertTrue(circle.intersects(-50, -50, 100, 100));
	}
	
	public void testIntersectsRectangle2(){
		Circle2D circle = new Circle2D(0, 0, 10);
		// rectangle totally inside circle
		assertTrue(circle.intersects(new HRectangle2D(0, 0, 1, 1)));
		
		// circle totally inside rectangle
		assertTrue(circle.intersects(new HRectangle2D(-50, -50, 100, 100)));
	}
	
	/*
	 * Test for double getDistance(Point2D)
	 */
	public void testGetDistancePoint2D() {
		Circle2D circle = new Circle2D(0, 0, 10);
		assertEquals(circle.getDistance(10, 0), 0, 1e-14);
		
		circle = new Circle2D(2, 3, 4);
		assertEquals(circle.getDistance(2, 3), 4, 1e-14);
		assertEquals(circle.getDistance(6, 3), 0, 1e-14);
		assertEquals(circle.getDistance(2, 7), 0, 1e-14);
		assertEquals(circle.getDistance(-2, 3), 0, 1e-14);
		assertEquals(circle.getDistance(2, -1), 0, 1e-14);
	}
	
	/*
	 * Test for boolean isInside(Point2D)
	 */
	public void testIsInsidePoint2D() {
		Circle2D circle = new Circle2D(0, 0, 10);
		
		Point2D point1 = new Point2D(9, 0);
		assertTrue(circle.isInside(point1));
		
		Point2D point2 = new Point2D(11, 0);
		assertTrue(!circle.isInside(point2));
		
		circle = new Circle2D(20, 50, 10);
		point1 = new Point2D(29, 50);
		assertTrue(circle.isInside(point1));
		
		point2 = new Point2D(31, 50);
		assertTrue(!circle.isInside(point2));
	}

	/*
	 * Test for Point2D[] getIntersections(StraightObject2D)
	 */
	public void testGetIntersectionsStraightObject2D() {
		Circle2D circle = new Circle2D(2, 3, 4);
		Collection<Point2D> points;
		
		StraightLine2D line0 = new StraightLine2D(6, 0, 0, 1);
		points = circle.getIntersections(line0);
		assertTrue(points.size() == 1);

		StraightLine2D line1 = new StraightLine2D(2, 0, 0, 1);
		points = circle.getIntersections(line1);
		assertTrue(points.size() == 2);
		Iterator<Point2D> iter = points.iterator();
		Point2D point1 = iter.next();
		assertEquals(point1.getX(), 2, 1e-14);
		assertEquals(point1.getY(), -1, 1e-14);
		Point2D point2 = iter.next();
		assertEquals(point2.getX(), 2, 1e-14);
		assertEquals(point2.getY(), 7, 1e-14);

		StraightLine2D line2 = new StraightLine2D(6.2, 0, 0, 1);
		points = circle.getIntersections(line2);
		assertTrue(points.size() == 0);

		// Test with a centered circle and 4 edges in each main direction
		Circle2D circle2 = new Circle2D(0, 0, 10);
		Collection<Point2D> points1 = circle2.getIntersections(new LineSegment2D(0, 0, 20, 0));
		Collection<Point2D> points2 = circle2.getIntersections(new LineSegment2D(0, 0, 0, 20));
		Collection<Point2D> points3 = circle2.getIntersections(new LineSegment2D(0, 0, -20, 0));
		Collection<Point2D> points4 = circle2.getIntersections(new LineSegment2D(0, 0, 0, -20));
		assertTrue(points1.iterator().next().equals(new Point2D(10, 0)));
		assertTrue(points2.iterator().next().equals(new Point2D(0, 10)));
		assertTrue(points3.iterator().next().equals(new Point2D(-10, 0)));
		assertTrue(points4.iterator().next().equals(new Point2D(0, -10)));		
	}
	
	/**
	 * Clip with a box when circle is totally inside the box
	 */
	public void testClipBox_Inside(){
		Circle2D circle = new Circle2D(5, 5, 2);
		Box2D box = new Box2D(0, 10, 0, 10);
		
		CurveSet2D<? extends ContinuousOrientedCurve2D> clipped =
			circle.clip(box);
		Collection<? extends ContinuousOrientedCurve2D> curves = 
			clipped.getCurves();
		assertTrue(curves.size()==1);
		
		ContinuousOrientedCurve2D curve = curves.iterator().next();
		assertTrue(curve instanceof Circle2D);
	}
	
	/**
	 * Clip with a box when circle is totally outside the box
	 */
	public void testClipBox_Outside(){
		Circle2D circle = new Circle2D(5, 5, 2);
		Box2D box = new Box2D(10, 20, 0, 10);
		
		CurveSet2D<? extends ContinuousOrientedCurve2D> clipped =
			circle.clip(box);
		Collection<? extends ContinuousOrientedCurve2D> curves = 
			clipped.getCurves();
		assertTrue(curves.size()==0);
	}
	
	/**
	 * Clip with a box which keeps only circle arc from 0 to PI/2.
	 */
	public void testClipBox_Quarter(){
		Circle2D circle = new Circle2D(0, 0, 10);
		Box2D box = new Box2D(0, 20, 0, 20);
		
		CurveSet2D<? extends ContinuousOrientedCurve2D> clipped =
			circle.clip(box);
		Collection<? extends ContinuousOrientedCurve2D> curves = 
			clipped.getCurves();
		assertTrue(curves.size()==1);
		
		ContinuousOrientedCurve2D curve = curves.iterator().next();
		assertTrue(curve instanceof CircleArc2D);
		
		CircleArc2D arc = (CircleArc2D) curve;
		assertTrue(new CircleArc2D(circle, 0, Math.PI/2).equals(arc));
	}
	
	public void testGetSubCurve(){
		Circle2D circle;
		CircleArc2D arc1, arc2;
		
		// try with a direct circle 
		circle = new Circle2D(0, 0, 10, true);
		arc1 = new CircleArc2D(circle, Math.PI/2, Math.PI/2);		
		arc2 = (CircleArc2D) circle.getSubCurve(Math.PI/2, Math.PI);
		assertTrue(arc1.equals(arc2));
		
		// try again with an indirect circle
		circle = new Circle2D(0, 0, 10, false);
		arc1 = new CircleArc2D(circle, Math.PI/2, -Math.PI/2);
		arc2 = (CircleArc2D) circle.getSubCurve(3*Math.PI/2, 2*Math.PI);
		assertTrue(arc1.equals(arc2));
	}
	
	/**
	 * Clip with a box when circle is totally inside the box
	 */
	public void testClipBox_Touch(){
		Circle2D circle = new Circle2D(50, 50, 50);
		Box2D box = new Box2D(0, 100, 0, 100);
		
		CurveSet2D<? extends SmoothCurve2D> clipped = box.clipSmoothCurve(circle);
		Collection<?> curves = clipped.getCurves();
		assertTrue(curves.size()==1);
		assertTrue(curves.iterator().next().equals(circle));
	}
	

}
