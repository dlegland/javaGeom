package math.geom2d.circulinear.buffer;

import java.util.Collection;

import junit.framework.TestCase;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.CirculinearBoundary2D;
import math.geom2d.circulinear.CirculinearContinuousCurve2D;
import math.geom2d.circulinear.CirculinearContour2D;
import math.geom2d.circulinear.CirculinearContourArray2D;
import math.geom2d.circulinear.CirculinearCurve2D;
import math.geom2d.circulinear.CirculinearCurve2DUtils;
import math.geom2d.circulinear.CirculinearCurveArray2D;
import math.geom2d.circulinear.CirculinearCurveSet2D;
import math.geom2d.circulinear.CirculinearDomain2D;
import math.geom2d.circulinear.CirculinearElement2D;
import math.geom2d.circulinear.PolyCirculinearCurve2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.line.InvertedRay2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Ray2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.polygon.LinearRing2D;
import math.geom2d.polygon.Polyline2D;

/**
 * Test computation of buffer and parallel on a variety of
 * circulinear curves.
 */
public class BufferCalculatorTest extends TestCase {

	/**
	 * Computes parallels of a straight line
	 */
	public void testCreateParallel_LineSegment() {
		LineSegment2D curve = 
			new LineSegment2D(new Point2D(100, 100), new Point2D(200, 100));

		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		CirculinearCurve2D parallel = bc.createParallel(curve, 20);

		// basic checks
		assertNotNull(parallel);
		assertFalse(parallel.isEmpty());

		// should have 1 circular piece
		assertEquals(1, parallel.getContinuousCurves().size());

		CirculinearCurve2D parallel2 = bc.createParallel(curve, -20);

		// basic checks
		assertNotNull(parallel2);
		assertFalse(parallel2.isEmpty());

		// should have 1 circular piece
		assertEquals(1, parallel2.getContinuousCurves().size());
	}

	/**
	 * Computes parallels of a straight line
	 */
	public void testCreateParallel_TwoLineSegments() {
		LineSegment2D line1 = 
			new LineSegment2D(new Point2D(100, 100), new Point2D(200, 100));
		LineSegment2D line2 = 
			new LineSegment2D(new Point2D(100, 200), new Point2D(200, 200));

		CirculinearCurve2D curve = 
			CirculinearCurveArray2D.create(new LineSegment2D[]{line1, line2});
		
		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		CirculinearCurve2D parallel = bc.createParallel(curve, 20);

		// basic checks
		assertNotNull(parallel);
		assertFalse(parallel.isEmpty());

		// should have 2 linear piece
		assertEquals(2, parallel.getContinuousCurves().size());

		CirculinearCurve2D parallel2 = bc.createParallel(curve, -20);

		// basic checks
		assertNotNull(parallel2);
		assertFalse(parallel2.isEmpty());

		// should have 2 linear piece
		assertEquals(2, parallel2.getContinuousCurves().size());
	}

	/**
	 * Computes parallels of a straight line
	 */
	public void testCreateParallelContour_StraightLine() {
		StraightLine2D curve = 
			new StraightLine2D(new Point2D(100, 100), new Point2D(200, 100));

		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		CirculinearContour2D parallel = bc.createParallelContour(curve, 20);

		// basic checks
		assertNotNull(parallel);
		assertFalse(parallel.isEmpty());
		assertFalse(parallel.isClosed());

		// should have 1 circular piece
		assertEquals(1, parallel.getSmoothPieces().size());

		CirculinearContour2D parallel2 = bc.createParallelContour(curve, -20);

		// basic checks
		assertNotNull(parallel2);
		assertFalse(parallel2.isEmpty());
		assertFalse(parallel2.isClosed());

		// should have 1 circular piece
		assertEquals(1, parallel2.getSmoothPieces().size());
	}

	/**
	 * Computes parallels of a triangular linear ring
	 */
	public void testCreateParallelContour_LinearRing() {
		LinearRing2D curve = new LinearRing2D(new Point2D[] {
				new Point2D(100, 100), new Point2D(200, 100),
				new Point2D(200, 200) });

		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		CirculinearContour2D parallel = bc.createParallelContour(curve, 20);

		// basic checks
		assertNotNull(parallel);
		assertFalse(parallel.isEmpty());
		assertTrue(parallel.isClosed());
		
		// should have 3 linear pieces, and 3 circular pieces
		assertEquals(6, parallel.getSmoothPieces().size());

		CirculinearContour2D parallel2 = bc.createParallelContour(curve, -20);

		// basic checks
		assertNotNull(parallel2);
		assertFalse(parallel2.isEmpty());
		assertTrue(parallel.isClosed());
		
		// should have 3 linear pieces, and 3 circular pieces
		assertEquals(6, parallel2.getSmoothPieces().size());
	}

	/**
	 * Check polyline computation on a polyline with a multiple vertex
	 */
	public void testCreateParallelContour_ColinearPolyline() {
		// polyline with two edges, that are colinear
		Polyline2D curve = new Polyline2D(new Point2D[] {
				new Point2D(100, 100), new Point2D(150, 100),
				new Point2D(200, 100) });

		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		CirculinearContinuousCurve2D parallel = bc.createContinuousParallel(
				curve, 20);

		assertNotNull(parallel);
		assertFalse(parallel.isEmpty());
		assertEquals(2, parallel.getSmoothPieces().size());

		CirculinearContinuousCurve2D parallel2 = bc.createContinuousParallel(
				curve, -20);

		assertNotNull(parallel2);
		assertFalse(parallel2.isEmpty());
		assertEquals(2, parallel2.getSmoothPieces().size());
	}

	/**
	 * Computes parallels of a circle
	 */
	public void testCreateParallelContour_Circle() {
		Circle2D curve = new Circle2D(new Point2D(100, 100), 50);

		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		CirculinearContour2D parallel = bc.createParallelContour(curve, 20);

		// basic checks
		assertNotNull(parallel);
		assertFalse(parallel.isEmpty());
		assertTrue(parallel.isClosed());

		// should have 1 circular piece
		assertEquals(1, parallel.getSmoothPieces().size());

		CirculinearContour2D parallel2 = bc.createParallelContour(curve, -20);

		// basic checks
		assertNotNull(parallel2);
		assertFalse(parallel2.isEmpty());
		assertTrue(parallel2.isClosed());

		// should have 1 circular piece
		assertEquals(1, parallel2.getSmoothPieces().size());
	}

	/**
	 * Computes parallels of a circle
	 */
	public void testCreateParallelBoundary_TwoCircles() {
		Circle2D circle1 = new Circle2D(new Point2D(100, 100), 50);
		Circle2D circle2 = new Circle2D(new Point2D(300, 400), 50);
		CirculinearBoundary2D curve = CirculinearContourArray2D.create(
				new Circle2D[]{circle1, circle2});

		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		CirculinearBoundary2D parallel = bc.createParallelBoundary(curve, 20);

		// basic checks
		assertNotNull(parallel);
		assertFalse(parallel.isEmpty());

		// should have 2 circular piece
		assertEquals(2, parallel.getContinuousCurves().size());

		CirculinearBoundary2D parallel2 = bc.createParallelBoundary(curve, -20);

		// basic checks
		assertNotNull(parallel2);
		assertFalse(parallel2.isEmpty());
		
		// should have 2 circular piece
		assertEquals(2, parallel2.getContinuousCurves().size());
	}

	public void testCreateContinousParallel_InfiniteCurve() {
		// create an infinite curve, here a straight line
		Point2D p0 = new Point2D(10, 20);
		Vector2D v0 = new Vector2D(10, 20);
		StraightLine2D line = StraightLine2D.create(p0, v0);

		BufferCalculator bc = BufferCalculator.getDefaultInstance();

		// compute parallel
		CirculinearContinuousCurve2D parallel = bc.createContinuousParallel(
				line, 10);
		assertNotNull(parallel);
		assertEquals(1, parallel.getContinuousCurves().size());
		assertFalse(parallel.isEmpty());
		assertEquals(1, parallel.getSmoothPieces().size());

		// same in other direction
		CirculinearContinuousCurve2D parallel2 = bc.createContinuousParallel(
				line, -10);
		assertNotNull(parallel2);
		assertEquals(1, parallel2.getContinuousCurves().size());
		assertFalse(parallel2.isEmpty());
		assertEquals(1, parallel2.getSmoothPieces().size());
	}

	public void testCreateContinousParallel_BiRay() {
		// first defines some constants
		Point2D origin = new Point2D(10, 20);
		Vector2D v1 = new Vector2D(2, 3);
		Vector2D v2 = new Vector2D(3, 2);

		// create elements of the curve
		InvertedRay2D ray1 = new InvertedRay2D(origin, v1);
		Ray2D ray2 = new Ray2D(origin, v2);

		// create the curve
		PolyCirculinearCurve2D<CirculinearElement2D> curve = 
			new PolyCirculinearCurve2D<CirculinearElement2D>();
		curve.addCurve(ray1);
		curve.addCurve(ray2);

		// computes the parallel
		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		CirculinearContinuousCurve2D parallel = bc.createContinuousParallel(
				curve, 10);
		assertNotNull(parallel);
		assertFalse(parallel.isEmpty());
		assertEquals(3, parallel.getSmoothPieces().size());

		// the same in opposite direction
		CirculinearContinuousCurve2D parallel2 = bc.createContinuousParallel(
				curve, 10);
		assertNotNull(parallel2);
		assertFalse(parallel2.isEmpty());
		assertEquals(3, parallel2.getSmoothPieces().size());

		Collection<CirculinearContinuousCurve2D> splittedCurves = 
			CirculinearCurve2DUtils.splitContinuousCurve(parallel2);
		assertEquals(2, splittedCurves.size());
	}

	public void testComputeBuffer_LineSegment() {
		// polyline with two edges that are colinear
		LineSegment2D line = new LineSegment2D(new Point2D(100, 100),
				new Point2D(200, 100));

		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		CirculinearDomain2D buffer = bc.computeBuffer(line, 20);

		assertNotNull(buffer);
		assertFalse(buffer.isEmpty());

		Boundary2D boundary = buffer.getBoundary();
		assertEquals(1, boundary.getContinuousCurves().size());
	}

	public void testComputeBuffer_ColinearPolyline() {
		// polyline with two edges that are colinear
		Polyline2D curve = Polyline2D.create(new Point2D[] {
				new Point2D(100, 100), new Point2D(150, 100),
				new Point2D(200, 100) });

		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		CirculinearDomain2D buffer = bc.computeBuffer(curve, 20);

		assertNotNull(buffer);
		assertFalse(buffer.isEmpty());
	}

	public void testComputeBuffer_StraightLine() {
		BufferCalculator bc = BufferCalculator.getDefaultInstance();

		CirculinearCurve2D curve = new StraightLine2D(new Point2D(10, 20),
				new Point2D(30, 50));
		double dist = 30;

		Domain2D buffer = bc.computeBuffer(curve, dist);

		assertFalse(buffer.isEmpty());
		assertFalse(buffer.isBounded());

		Boundary2D boundary = buffer.getBoundary();

		assertEquals(2, boundary.getBoundaryCurves().size());
	}

	public void testComputeBuffer_BiRay() {
		// first defines some constants
		Point2D origin = new Point2D(10, 20);
		Vector2D v1 = new Vector2D(3, 4);
		Vector2D v2 = new Vector2D(4, 3);

		// create elements of the curve
		InvertedRay2D ray1 = new InvertedRay2D(origin, v1);
		Ray2D ray2 = new Ray2D(origin, v2);

		// create the curve
		PolyCirculinearCurve2D<CirculinearElement2D> curve =
			new PolyCirculinearCurve2D<CirculinearElement2D>();
		curve.addCurve(ray1);
		curve.addCurve(ray2);
		assertEquals(2, curve.getSmoothPieces().size());

		// computes the buffer
		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		CirculinearDomain2D buffer = bc.computeBuffer(curve, 10);
		assertNotNull(buffer);
		assertFalse(buffer.isEmpty());

		// Extract boundary of buffer
		Boundary2D boundary = buffer.getBoundary();
		assertEquals(2, boundary.getContinuousCurves().size());
	}

	public void testComputeBuffer_TwoLines() {
		// shortcuts for reference elements
		Point2D origin = new Point2D(0, 0);
		Vector2D v1 = new Vector2D(10, 0);
		Vector2D v2 = new Vector2D(0, 10);

		// create two orthogonal lines
		StraightLine2D line1 = StraightLine2D.create(origin, v1);
		StraightLine2D line2 = StraightLine2D.create(origin, v2);

		// put lines in a set
		CirculinearCurveSet2D<StraightLine2D> set = 
			CirculinearCurveArray2D.create(new StraightLine2D[] {line1, line2});

		// compute set buffer and buffer boundary
		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		CirculinearDomain2D buffer = bc.computeBuffer(set, 10);
		CirculinearBoundary2D boundary = buffer.getBoundary();
		assertEquals(4, boundary.getBoundaryCurves().size());
	}

	public void testComputeBuffer_TwoCrossingLines() {
		BufferCalculator bc = BufferCalculator.getDefaultInstance();

		StraightLine2D line1 = new StraightLine2D(new Point2D(10, 20),
				new Point2D(10, 0));
		StraightLine2D line2 = new StraightLine2D(new Point2D(20, 10),
				new Point2D(0, 10));
		CirculinearCurve2D curve = 
			CirculinearCurveArray2D.create(new StraightLine2D[] {line1, line2});
		double dist = 3;

		Domain2D buffer = bc.computeBuffer(curve, dist);

		assertFalse(buffer.isEmpty());
		assertFalse(buffer.isBounded());

		Boundary2D boundary = buffer.getBoundary();

		assertEquals(4, boundary.getBoundaryCurves().size());
	}

	public void testComputeBuffer_TwoCrossingRays() {
		BufferCalculator bc = BufferCalculator.getDefaultInstance();

		Ray2D ray1 = new Ray2D(new Point2D(20, 0), new Point2D(0, 10));
		Ray2D ray2 = new Ray2D(new Point2D(0, 20), new Point2D(10, 0));
		CirculinearCurve2D curve = CirculinearCurveArray2D.create(new Ray2D[] {
				ray1, ray2 });
		double dist = 3;

		Domain2D buffer = bc.computeBuffer(curve, dist);

		assertFalse(buffer.isEmpty());
		assertFalse(buffer.isBounded());

		Boundary2D boundary = buffer.getBoundary();

		assertEquals(2, boundary.getBoundaryCurves().size());
	}

	public void testComputeBuffer_PolyRay() {
		BufferCalculator bc = new BufferCalculator();

		// some base points
		Point2D p1 = new Point2D(0, 20);
		Point2D p2 = new Point2D(20, 0);
		
		// curve elements
		InvertedRay2D ray1 = new InvertedRay2D(p1, new Vector2D(0, -10));
		LineSegment2D edge = new LineSegment2D(p1, p2);
		Ray2D ray2 = new Ray2D(p2, new Point2D(30, 0));
		
		// create the curve
		CirculinearCurve2D curve = PolyCirculinearCurve2D.create(
				new CirculinearElement2D[] {ray1, edge, ray2 });
		
		// compute buffer
		double dist = 3;
		Domain2D buffer = bc.computeBuffer(curve, dist);

		assertFalse(buffer.isEmpty());
		assertFalse(buffer.isBounded());

		Boundary2D boundary = buffer.getBoundary();

		assertEquals(2, boundary.getBoundaryCurves().size());
	}
}
