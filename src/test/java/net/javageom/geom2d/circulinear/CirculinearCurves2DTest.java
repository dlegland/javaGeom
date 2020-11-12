/* file : CirculinearCurve2DUtilsTest.java
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
 * Created on 7 mars 2007
 *
 */
package net.javageom.geom2d.circulinear;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;
import net.javageom.geom2d.Point2D;
import net.javageom.geom2d.Shape2D;
import net.javageom.geom2d.Vector2D;
import net.javageom.geom2d.conic.Circle2D;
import net.javageom.geom2d.conic.CircleArc2D;
import net.javageom.geom2d.curve.ContinuousCurve2D;
import net.javageom.geom2d.curve.CurveArray2D;
import net.javageom.geom2d.curve.PolyCurve2D;
import net.javageom.geom2d.curve.SmoothCurve2D;
import net.javageom.geom2d.line.InvertedRay2D;
import net.javageom.geom2d.line.LineSegment2D;
import net.javageom.geom2d.line.Ray2D;
import net.javageom.geom2d.line.StraightLine2D;
import net.javageom.geom2d.point.PointArray2D;
import net.javageom.geom2d.polygon.LinearRing2D;
import net.javageom.geom2d.spline.CubicBezierCurve2D;

public class CirculinearCurves2DTest extends TestCase {

	public void testConvert_Element() {
		CirculinearCurve2D conv;
		LineSegment2D seg = new LineSegment2D(new Point2D(0, 10), new Point2D(10, 20));
		conv = CirculinearCurves2D.convert(seg);
		assertTrue(seg.equals(conv));
	}
	
	public void testConvert_PolyCurve() {
		CirculinearCurve2D converted;
		Point2D p1 = new Point2D(0, 10);
		Point2D p2 = new Point2D(10, 20);
		Point2D p3 = new Point2D(0, 30);
		LineSegment2D seg1 = new LineSegment2D(p1, p2);
		LineSegment2D seg2 = new LineSegment2D(p2, p3);
		PolyCurve2D<LineSegment2D> curve = 
			PolyCurve2D.create(new LineSegment2D[]{seg1, seg2});
		
		converted = CirculinearCurves2D.convert(curve);
		assertTrue(converted instanceof PolyCirculinearCurve2D);
		
		Collection<? extends SmoothCurve2D> smoothCurves = 
			converted.continuousCurves().iterator().next().smoothPieces();
		
		assertEquals(2, smoothCurves.size());
		assertTrue(smoothCurves.contains(seg1));
		assertTrue(smoothCurves.contains(seg2));
	}
	
	public void testConvert_PolyCurveWithSpline() {
		Point2D p1 = new Point2D(0, 10);
		Point2D p2 = new Point2D(10, 20);
		Point2D p3 = new Point2D(0, 30);
		LineSegment2D seg1 = new LineSegment2D(p1, p2);
		LineSegment2D seg2 = new LineSegment2D(p2, p3);
		CubicBezierCurve2D bezier = new CubicBezierCurve2D(
				p3, new Point2D(0, 10), new Point2D(10, 50), new Point2D(20, 50));
		PolyCurve2D<SmoothCurve2D> curve = 
			PolyCurve2D.create(new SmoothCurve2D[]{seg1, seg2, bezier});
		
		try {
			CirculinearCurves2D.convert(curve);
			fail("should have thrown an exception");
		} catch (NonCirculinearClassException ex) {
			// should go here
		}
	}

	public void testConvert_CurveSet() {
		CirculinearCurve2D res;
		Point2D p1 = new Point2D(0, 10);
		Point2D p2 = new Point2D(10, 20);
		Point2D p3 = new Point2D(0, 30);
		
		LineSegment2D seg1 = new LineSegment2D(p1, p2);
		LineSegment2D seg2 = new LineSegment2D(p2, p3);
		
		CurveArray2D<LineSegment2D> curve = 
			CurveArray2D.create(new LineSegment2D[]{seg1, seg2});
		
		res = CirculinearCurves2D.convert(curve);
		assertTrue(res instanceof CirculinearCurveSet2D);
		
		Collection<? extends ContinuousCurve2D> smoothCurves = 
			res.continuousCurves();

		assertEquals(2, smoothCurves.size());
		assertTrue(smoothCurves.contains(seg1));
		assertTrue(smoothCurves.contains(seg2));
	}

	public void testConvert_CurveSetWithSpline() {
		Point2D p1 = new Point2D(0, 10);
		Point2D p2 = new Point2D(10, 20);
		Point2D p3 = new Point2D(0, 30);
		
		LineSegment2D seg1 = new LineSegment2D(p1, p2);
		LineSegment2D seg2 = new LineSegment2D(p2, p3);
		CubicBezierCurve2D bezier = new CubicBezierCurve2D(
				p3, new Point2D(0, 10), new Point2D(10, 50), new Point2D(20, 50));
		
		CurveArray2D<SmoothCurve2D> curve = 
			CurveArray2D.create(new SmoothCurve2D[]{seg1, seg2, bezier});
		
		try {
			CirculinearCurves2D.convert(curve);
			fail("should have thrown an exception");
		} catch (NonCirculinearClassException ex) {
			// should go here
		}
	}

	public void testSplitContinuousCurve() {
		// elements
		LineSegment2D edge1 = new LineSegment2D(
				new Point2D(50, 100), new Point2D(150, 100));
		CircleArc2D arc1 = new CircleArc2D(new Point2D(100, 100), 50, 0, Math.PI/2);
		LineSegment2D edge2 = new LineSegment2D(
				new Point2D(100, 150), new Point2D(100, 50));
		
		// create the curve
		CirculinearContinuousCurve2D curve = PolyCirculinearCurve2D.create(		
				new CirculinearElement2D[]{edge1, arc1, edge2} );
		
		// split the curve
		Collection<CirculinearContinuousCurve2D> set = 
			CirculinearCurves2D.splitContinuousCurve(curve);
		
		// should be two parts
		assertEquals(2, set.size());
	}
	
	public void testSplitIntersectingContours_Circles() {
		Circle2D circle1 = new Circle2D(0, 0, 10);
		Circle2D circle2 = new Circle2D(10, 0, 10);
		Collection<? extends CirculinearContour2D> contours = 
			CirculinearCurves2D.splitIntersectingContours(circle1, circle2);
		assertEquals(contours.size(), 2);
	}
	
	public void testSplitIntersectingContours_Rectangles() {
		LinearRing2D poly1 = new LinearRing2D(new Point2D[]{
				new Point2D(10, 20), new Point2D(40, 20), 
				new Point2D(40, 30), new Point2D(10, 30) });
		LinearRing2D poly2 = new LinearRing2D(new Point2D[]{
				new Point2D(10, 20), new Point2D(40, 20), 
				new Point2D(40, 30), new Point2D(10, 30) });
		
		Collection<CirculinearContour2D> contours = 
			CirculinearCurves2D.splitIntersectingContours(poly1, poly2);
		assertEquals(contours.size(), 2);
	}
	
	public void testSplitIntersectingContours_3Circles() {
		Circle2D ring1 = new Circle2D(150, 150, 100);
		Circle2D ring2 = new Circle2D(250, 150, 100);
		Circle2D ring3 = new Circle2D(200, 220, 100);
		
		ArrayList<CirculinearContour2D> rings = 
			new ArrayList<CirculinearContour2D>(3);
		rings.add(ring1);
		rings.add(ring2);
		rings.add(ring3);

		Collection<CirculinearContour2D> result = 
			CirculinearCurves2D.splitIntersectingContours(rings);
		assertTrue(result.size()==3);
	}
	
	public void testSplitIntersectingContours_4Lines() {
		// shortcuts for reference elements
		Point2D p1 = new Point2D(10, 0);
		Point2D p2 = new Point2D(-10, 0);
		Point2D p3 = new Point2D(0, 10);
		Point2D p4 = new Point2D(0, -10);
		Vector2D v1 = new Vector2D(0, 10);
		Vector2D v2 = new Vector2D(0, -10);
		Vector2D v3 = new Vector2D(-10, 0);
		Vector2D v4 = new Vector2D(10, 0);
		
		// create two orthogonal lines
		StraightLine2D line1 = new StraightLine2D(p1, v1);
		StraightLine2D line2 = new StraightLine2D(p2, v2);
		StraightLine2D line3 = new StraightLine2D(p3, v3);
		StraightLine2D line4 = new StraightLine2D(p4, v4);
		
		// put lines in a set
		CirculinearCurveSet2D<StraightLine2D> set = 
			CirculinearCurveArray2D.create(new StraightLine2D[]{
					line1, line2, line3, line4});
		
		Collection<StraightLine2D> curves = set.curves();
		Collection<CirculinearContour2D> contours = 
			CirculinearCurves2D.splitIntersectingContours(curves);
		assertEquals(5, contours.size());
	}

	public void testFindSelfIntersections () {
		// create polyline
		LinearRing2D line = new LinearRing2D(new Point2D[]{
				new Point2D(100, 0), 
				new Point2D(0, 0),
				new Point2D(0, 100),
				new Point2D(200, 100),
				new Point2D(200, 200),
				new Point2D(100, 200) });
		
		// detection of intersections
		Collection<Point2D> points = 
			CirculinearCurves2D.findSelfIntersections(line);

		assertEquals(points.size(), 1);
		
		assertTrue(points.contains(new Point2D(100, 100)));
	}
	
	public void testSplitContinuousCurveParallelBiRay () {
		// first defines some constants
		Point2D origin = new Point2D(10, 10);
		Vector2D v1 = new Vector2D(-3, -4);
		Vector2D v2 = new Vector2D(4, 3);
		
		// create elements of the curve
		InvertedRay2D ray1 = new InvertedRay2D(origin, v1);
		Ray2D ray2 = new Ray2D(origin, v2);
		
		// create the curve
		PolyCirculinearCurve2D<CirculinearElement2D> curve =
			new PolyCirculinearCurve2D<CirculinearElement2D>();
		curve.add(ray1);
		curve.add(ray2);
		assertEquals(2, curve.smoothPieces().size());

		// computes the positive parallel
		CirculinearContinuousCurve2D parallel = curve.parallel(10);
		Collection<CirculinearContinuousCurve2D> splittedCurves =
			CirculinearCurves2D.splitContinuousCurve(parallel);
		
		assertFalse(splittedCurves.isEmpty());
		assertEquals(1, splittedCurves.size());
		
		// computes the negative parallel
		CirculinearContinuousCurve2D parallel2 = curve.parallel(-10);
		Collection<Point2D> points = parallel2.singularPoints();
		assertEquals(2, points.size());
		
		PointArray2D pointSet = PointArray2D.create(points);
		assertTrue(pointSet.distance(new Point2D(18, 4)) < Shape2D.ACCURACY);
		assertTrue(pointSet.distance(new Point2D(4, 18)) < Shape2D.ACCURACY);
	}
	

}
