/**
 * File: 	BufferCalculator.java
 * Project: javageom-buffer
 * 
 * Distributed under the LGPL License.
 *
 * Created: 4 janv. 2011
 */
package math.geom2d.circulinear.buffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.circulinear.*;
import math.geom2d.conic.Circle2D;
import math.geom2d.curve.Curves2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.point.PointSet2D;


/**
 * Compute the buffer of a circulinear curve or domain, and gather some
 * methods for computing parallel curves.<p>
 * This class can be instantiated, but also contains a lot of static methods.
 * The default instance of BufferCalculator is accessible through the static
 * method 'getDefaultInstance'. The public constructor can be called if
 * different cap or join need to be specified.
 * 
 * @author dlegland
 *
 */
public class BufferCalculator {
	
    // ===================================================================
    // static methods and variables

	private static BufferCalculator defaultInstance = null;
	
	/**
	 * Returns the default instance of bufferCalculator.
	 */
	public static BufferCalculator getDefaultInstance() {
		if (defaultInstance == null)
			defaultInstance = new BufferCalculator();
		return defaultInstance;
	}
	
    // ===================================================================
    // Class variables

	private JoinFactory joinFactory;
	private CapFactory capFactory;
	
    // ===================================================================
    // Constructors

	/**
	 * Creates a new buffer calculator with default join and cap factories.
	 */
	public BufferCalculator() {
		this.joinFactory = new RoundJoinFactory();
		this.capFactory = new RoundCapFactory();
	}
	
	/**
	 * Creates a new buffer calculator with specific join and cap factories.
	 */
	public BufferCalculator(JoinFactory joinFactory, CapFactory capFactory) {
		this.joinFactory = joinFactory;
		this.capFactory = capFactory;
	}
	
	
    // ===================================================================
    // General methods

	/**
	 * Computes the parallel curve of a circulinear curve (composed only of
	 * pieces of lines and circles). 
	 * The result is itself a circulinear curve.
	 */
	public CirculinearCurve2D createParallel(
			CirculinearCurve2D curve, double dist) {
		
		// case of a continuous curve -> call specialized method
		if (curve instanceof CirculinearContinuousCurve2D) {
			return createContinuousParallel(
					(CirculinearContinuousCurve2D)curve, dist);
		} 
		
		// Create array for storing result
		CirculinearCurveArray2D<CirculinearContinuousCurve2D> parallels =
			new CirculinearCurveArray2D<CirculinearContinuousCurve2D>();
		
		// compute parallel of each continuous part, and add it to the result
		for (CirculinearContinuousCurve2D continuous : 
			curve.continuousCurves()){
			CirculinearContinuousCurve2D contParallel = 
				createContinuousParallel(continuous, dist);
			if (contParallel != null)
				parallels.add(contParallel);
		}
		
		// return the set of parallel curves
		return parallels;
	}

	public CirculinearBoundary2D createParallelBoundary(
			CirculinearBoundary2D boundary, double dist) {
		
		// in the case of a single contour, return the parallel of the contour
		if (boundary instanceof CirculinearContour2D)
			return createParallelContour((CirculinearContour2D) boundary, dist);
		
		// get the set of individual contours
		Collection<? extends CirculinearContour2D> contours = 
			boundary.continuousCurves();
		
		// allocate the array of parallel contours
		Collection<CirculinearContour2D> parallelContours = 
			new ArrayList<CirculinearContour2D>(contours.size());
		
		// compute the parallel of each contour
		for(CirculinearContour2D contour : contours)
			parallelContours.add(contour.parallel(dist));
		
		// Create an agglomeration of the curves
		return CirculinearContourArray2D.create(parallelContours);
	}

	public CirculinearContour2D createParallelContour(
			CirculinearContour2D contour, double dist) {
		
		// straight line is already a circulinear contour
		if (contour instanceof StraightLine2D) {
			return ((StraightLine2D) contour).parallel(dist);
		} 
		// The circle is already a circulinear contour
		if (contour instanceof Circle2D) {
			return ((Circle2D) contour).parallel(dist);
		} 

		// extract collection of parallel curves, that connect each other
		Collection<CirculinearContinuousCurve2D> parallelCurves = 
			getParallelElements(contour, dist);
		
		// Create a new boundary with the set of parallel curves
		return BoundaryPolyCirculinearCurve2D.create(parallelCurves, 
				contour.isClosed());
	}
	
	/**
	 * Compute the parallel curve of a Circulinear and continuous curve. 
	 * The result is itself an instance of CirculinearContinuousCurve2D.
	 */
	public CirculinearContinuousCurve2D createContinuousParallel(
			CirculinearContinuousCurve2D curve, double dist) {
		
		// For circulinear elements, getParallel() is already implemented
		if (curve instanceof CirculinearElement2D) {
			return ((CirculinearElement2D) curve).parallel(dist);
		} 

		// extract collection of parallel curves, that connect each other
		Collection<CirculinearContinuousCurve2D> parallelCurves = 
			getParallelElements(curve, dist);
		
		// Create a new circulinear continuous curve with the set of parallel
		// curves
		return PolyCirculinearCurve2D.create(parallelCurves, curve.isClosed());
	}
	
	private Collection<CirculinearContinuousCurve2D> getParallelElements(
			CirculinearContinuousCurve2D curve, double dist) {
		
		// extract collection of circulinear elements
		Collection<? extends CirculinearElement2D> elements = 
			curve.smoothPieces();
		
		Iterator<? extends CirculinearElement2D> iterator = 
			elements.iterator();

		// previous curve
		CirculinearElement2D previous = null;
		CirculinearElement2D current = null;

		// create array for storing result
		ArrayList<CirculinearContinuousCurve2D> parallelCurves = 
			new ArrayList<CirculinearContinuousCurve2D> ();

		// check if curve is empty
		if (!iterator.hasNext())
			return parallelCurves;

		// add parallel to the first curve
		current = iterator.next();
		CirculinearElement2D parallel = current.parallel(dist);
		parallelCurves.add(parallel);

		// iterate on circulinear element couples
		CirculinearContinuousCurve2D join;
		while (iterator.hasNext()){
			// update the couple of circulinear elements
			previous = current;
			current = iterator.next();

			// add circle arc between the two curve elements
			join = joinFactory.createJoin(previous, current, dist);
			if (join.length() > 0)
				parallelCurves.add(join);
			
			// add parallel to set of parallels
			parallelCurves.add(current.parallel(dist));
		}

		// Add eventually a circle arc to close the parallel curve
		if (curve.isClosed()) {
			previous = current;
			current = elements.iterator().next();
			
			join = joinFactory.createJoin(previous, current, dist);
			if (join.length() > 0)
				parallelCurves.add(join);
		}

		return parallelCurves;
	}
	
	/**
	 * Compute the buffer of a circulinear curve.<p>
	 * The algorithm is as follow:
	 * <ol>
	 * <li> split the curve into a set of curves without self-intersections
	 * <li> for each split curve, compute the contour of its buffer
	 * <li> split self-intersecting contours into set of disjoint contours
	 * <li> split all contour which intersect each other to disjoint contours
	 * <li> remove contours which are too close from the original curve
	 * <li> create a new domain with the final set of contours
	 * </ol>
	 */
	public CirculinearDomain2D computeBuffer(
			CirculinearCurve2D curve, double dist) {
		
		ArrayList<CirculinearContour2D> contours =
			new ArrayList<CirculinearContour2D>();
		
		// iterate on all continuous curves
		for (CirculinearContinuousCurve2D cont : curve.continuousCurves()) {
			// split the curve into a set of non self-intersecting curves
			for (CirculinearContinuousCurve2D splitted : 
				CirculinearCurves2D.splitContinuousCurve(cont)) {
				// compute the rings composing the simple curve buffer
				contours.addAll(computeBufferSimpleCurve(splitted, dist));
			}
		}
		
		// split contours which intersect each others
		contours = new ArrayList<CirculinearContour2D>(
				CirculinearCurves2D.splitIntersectingContours(contours));		
		
		// Remove contours that cross or that are too close from base curve
		ArrayList<CirculinearContour2D> contours2 = 
			new ArrayList<CirculinearContour2D>(contours.size());
		Collection<Point2D> intersects;
		Collection<Point2D> vertices;
		
		for (CirculinearContour2D contour : contours) {
			
			// do not keep contours which cross original curve
			intersects = CirculinearCurves2D.findIntersections(curve, contour);
			
			// remove intersection points that are vertices of the reference curve
			vertices = curve.singularPoints();
			vertices = curve.vertices();
			intersects.removeAll(vertices);
			
			if (intersects.size() > 0)
				continue;
			
			// check that vertices of contour are not too close from original
			// curve
			double distCurves = 
				getDistanceCurveSingularPoints(curve, contour);
			if(distCurves < dist-Shape2D.ACCURACY)
				continue;
			
			// keep the contours that meet the above conditions
			contours2.add(contour);
		}
		
		// All the rings are created, we can now create a new domain with the
		// set of rings
		return new GenericCirculinearDomain2D(
				CirculinearContourArray2D.create(contours2));
	}
	
	/**
	 * Compute buffer of a point set.
	 */
	public CirculinearDomain2D computeBuffer(PointSet2D set, 
			double dist) {
		// create array for storing result
		Collection<CirculinearContour2D> contours = 
			new ArrayList<CirculinearContour2D>(set.size());
		
		// for each point, add a new circle
		for (Point2D point : set) {
			contours.add(new Circle2D(point, Math.abs(dist), dist > 0));
		}
		
		// process circles to remove intersections
		contours = CirculinearCurves2D.splitIntersectingContours(contours);
		
		// Remove contours that cross or that are too close from base curve
		ArrayList<CirculinearContour2D> contours2 = 
			new ArrayList<CirculinearContour2D>(contours.size());
		for (CirculinearContour2D ring : contours) {
			
			// check that vertices of contour are not too close from original
			// curve
			double minDist = CirculinearCurves2D.getDistanceCurvePoints(
					ring, set.points());
			if(minDist < dist-Shape2D.ACCURACY)
				continue;
			
			// keep the contours that meet the above conditions
			contours2.add(ring);
		}

		return new GenericCirculinearDomain2D(
				CirculinearContourArray2D.create(contours2));
	}

	/**
	 * Computes the buffer of a simple curve.
	 * This method should replace the method 'computeBufferSimpleContour'.
	 */
	private Collection<? extends CirculinearContour2D> 
	computeBufferSimpleCurve(CirculinearContinuousCurve2D curve, double d) {
		
		Collection<CirculinearContour2D> contours = 
			new ArrayList<CirculinearContour2D>(2);

		// the parallel in each side
		CirculinearContinuousCurve2D parallel1, parallel2;
		parallel1 = createContinuousParallel(curve, d);
		parallel2 = createContinuousParallel(curve, -d).reverse();
		
		if (curve.isClosed()) {
			// each parallel is itself a contour
			contours.add(convertCurveToBoundary(parallel1));
			contours.add(convertCurveToBoundary(parallel2));
		} else {
			// create a new contour from the two parallels and 2 caps
			contours.addAll(createSingleContourFromTwoParallels(parallel1, parallel2));
		}
				
		// some contours may intersect, so we split them
		Collection<CirculinearContour2D> contours2 =
			removeIntersectingContours(contours, curve, d);

		// return the set of created contours
		return contours2;
	}
	
	/**
	 * Creates the unique contour based on two parallels of the base curve, by
	 * adding appropriate circle arcs at extremities of the base curve.
	 */
	private Collection<CirculinearContour2D> 
	createSingleContourFromTwoParallels(
			CirculinearContinuousCurve2D curve1,
			CirculinearContinuousCurve2D curve2) {
		
		// create array for storing result
		ArrayList<CirculinearContour2D> contours = 
			new ArrayList<CirculinearContour2D>();
		
		CirculinearContinuousCurve2D cap;
		
		// create new ring using two open curves and two circle arcs
		if (curve1 != null && curve2 != null){
			// array of elements for creating new ring.
			ArrayList<CirculinearElement2D> elements = 
				new ArrayList<CirculinearElement2D>();

			// some shortcuts for computing infinity of curve
			boolean b0 = !Curves2D.isLeftInfinite(curve1);
			boolean b1 = !Curves2D.isRightInfinite(curve1);

			if (b0 && b1) {
					// case of a curve finite at each extremity

					// extremity points
					Point2D p11 = curve1.firstPoint();
					Point2D p12 = curve1.lastPoint();
					Point2D p21 = curve2.firstPoint();
					Point2D p22 = curve2.lastPoint();

					// Check how to associate open curves and circle arcs
					elements.addAll(curve1.smoothPieces());					
					cap = capFactory.createCap(p12, p21);
					elements.addAll(cap.smoothPieces());
					elements.addAll(curve2.smoothPieces());
					cap = capFactory.createCap(p22, p11);
					elements.addAll(cap.smoothPieces());
					
					// create the last ring
					contours.add(new GenericCirculinearRing2D(elements));
					
			} else if (!b0 && !b1) {
				// case of an infinite curve at both extremities
				// In this case, the two parallel curves do not join,
				// and are added as contours individually					
				contours.add(convertCurveToBoundary(curve1));
				contours.add(convertCurveToBoundary(curve2));
				
			} else if (b0 && !b1) {
				// case of a curve starting from infinity, and finishing
				// on a given point

				// extremity points
				Point2D p11 = curve1.firstPoint();
				Point2D p22 = curve2.lastPoint();

				// add elements of the new contour
				elements.addAll(curve2.smoothPieces());
				cap = capFactory.createCap(p22, p11);
				elements.addAll(cap.smoothPieces());
				elements.addAll(curve1.smoothPieces());

				// create the last ring
				contours.add(new GenericCirculinearRing2D(elements));
				
			} else if (b1 && !b0) {
				// case of a curve starting at a point and finishing at
				// the infinity

				// extremity points
				Point2D p12 = curve1.lastPoint();
				Point2D p21 = curve2.firstPoint();

				// add elements of the new contour
				elements.addAll(curve1.smoothPieces());
				cap = capFactory.createCap(p12, p21);
				elements.addAll(cap.smoothPieces());
				elements.addAll(curve2.smoothPieces());

				// create the last contour
				contours.add(new GenericCirculinearRing2D(elements));

			}
		}
		
		return contours;
	}
	
	private Collection<CirculinearContour2D> removeIntersectingContours (
			Collection<CirculinearContour2D> contours, 
			CirculinearCurve2D curve, double d) {
		// prepare an array to store the set of rings
		ArrayList<CirculinearContour2D> contours2 =
			new ArrayList<CirculinearContour2D>();

		// iterate on the set of rings
		for (CirculinearContour2D contour : contours)
			// split rings into curves which do not self-intersect
			for (CirculinearContinuousCurve2D splitted : 
				CirculinearCurves2D.splitContinuousCurve(contour)) {
				
				// compute distance to original curve
				// (assuming it is sufficient to compute distance to vertices
				// of the reference curve).
				double dist = CirculinearCurves2D.getDistanceCurvePoints(
						curve, splitted.singularPoints());
				
				// check if distance condition is verified
				if (dist-d < -Shape2D.ACCURACY)
					continue;
				
				// convert the set of elements to a Circulinear ring
				contours2.add(convertCurveToBoundary(splitted));
		}
		
		// return the set of created rings
		return contours2;		
	}
	
	/**
	 * Converts the given continuous curve to an instance of
	 * CirculinearContour2D. This can be the curve itself, a new instance of
	 * GenericCirculinearRing2D if the curve is bounded, or a new instance of
	 * BoundaryPolyCirculinearCurve2D if the curve is unbounded.
	 */
	private CirculinearContour2D convertCurveToBoundary (
			CirculinearContinuousCurve2D curve) {
		// basic case: curve is already a contour
		if (curve instanceof CirculinearContour2D)
			return (CirculinearContour2D) curve;
		
		// if the curve is closed, return an instance of GenericCirculinearRing2D
		if (curve.isClosed())
			return GenericCirculinearRing2D.create(curve.smoothPieces());
		
		return BoundaryPolyCirculinearCurve2D.create(curve.smoothPieces());
	}
	
	private double getDistanceCurveSingularPoints(
			CirculinearCurve2D ref, CirculinearCurve2D curve){
		// extract singular points
		Collection<Point2D> points = curve.singularPoints();
		
		// If no singular point, choose an arbitrary point on the curve
		if (points.isEmpty()) {
			points = new ArrayList<Point2D>();
			double t = Curves2D.choosePosition(curve.t0(), curve.t1());
			points.add(curve.point(t));
		}
		
		// Iterate on points to get minimal distance
		double minDist = Double.MAX_VALUE;
		for (Point2D point : points){
			minDist = Math.min(minDist, ref.distance(point));
		}
		return minDist;
	}
}
