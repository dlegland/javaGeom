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

import math.geom2d.IShape2D;
import math.geom2d.circulinear.BoundaryPolyCirculinearCurve2D;
import math.geom2d.circulinear.CirculinearContourArray2D;
import math.geom2d.circulinear.CirculinearCurveArray2D;
import math.geom2d.circulinear.CirculinearCurves2D;
import math.geom2d.circulinear.GenericCirculinearDomain2D;
import math.geom2d.circulinear.GenericCirculinearRing2D;
import math.geom2d.circulinear.ICirculinearBoundary2D;
import math.geom2d.circulinear.ICirculinearContinuousCurve2D;
import math.geom2d.circulinear.ICirculinearContour2D;
import math.geom2d.circulinear.ICirculinearCurve2D;
import math.geom2d.circulinear.ICirculinearDomain2D;
import math.geom2d.circulinear.ICirculinearElement2D;
import math.geom2d.circulinear.PolyCirculinearCurve2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.curve.Curves2DUtil;
import math.geom2d.line.StraightLine2D;
import math.geom2d.point.IPointSet2D;
import math.geom2d.point.Point2D;

/**
 * Compute the buffer of a circulinear curve or domain, and gather some methods for computing parallel curves.
 * <p>
 * This class can be instantiated, but also contains a lot of static methods. The default instance of BufferCalculator is accessible through the static method 'getDefaultInstance'. The public constructor can be called if different cap or join need to be specified.
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

    private IJoinFactory joinFactory;
    private ICapFactory capFactory;

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
    public BufferCalculator(IJoinFactory joinFactory, ICapFactory capFactory) {
        this.joinFactory = joinFactory;
        this.capFactory = capFactory;
    }

    // ===================================================================
    // General methods

    /**
     * Computes the parallel curve of a circulinear curve (composed only of pieces of lines and circles). The result is itself a circulinear curve.
     */
    public ICirculinearCurve2D createParallel(ICirculinearCurve2D curve, double dist) {

        // case of a continuous curve -> call specialized method
        if (curve instanceof ICirculinearContinuousCurve2D) {
            return createContinuousParallel((ICirculinearContinuousCurve2D) curve, dist);
        }

        // Create array for storing result
        CirculinearCurveArray2D<ICirculinearContinuousCurve2D> parallels = new CirculinearCurveArray2D<>();

        // compute parallel of each continuous part, and add it to the result
        for (ICirculinearContinuousCurve2D continuous : curve.continuousCurves()) {
            ICirculinearContinuousCurve2D contParallel = createContinuousParallel(continuous, dist);
            if (contParallel != null)
                parallels.add(contParallel);
        }

        // return the set of parallel curves
        return parallels;
    }

    public ICirculinearBoundary2D createParallelBoundary(ICirculinearBoundary2D boundary, double dist) {

        // in the case of a single contour, return the parallel of the contour
        if (boundary instanceof ICirculinearContour2D)
            return createParallelContour((ICirculinearContour2D) boundary, dist);

        // get the set of individual contours
        Collection<? extends ICirculinearContour2D> contours = boundary.continuousCurves();

        // allocate the array of parallel contours
        Collection<ICirculinearContour2D> parallelContours = new ArrayList<>(contours.size());

        // compute the parallel of each contour
        for (ICirculinearContour2D contour : contours)
            parallelContours.add(contour.parallel(dist));

        // Create an agglomeration of the curves
        return CirculinearContourArray2D.create(parallelContours.toArray(new ICirculinearContour2D[0]));
    }

    public ICirculinearContour2D createParallelContour(ICirculinearContour2D contour, double dist) {

        // straight line is already a circulinear contour
        if (contour instanceof StraightLine2D) {
            return ((StraightLine2D) contour).parallel(dist);
        }
        // The circle is already a circulinear contour
        if (contour instanceof Circle2D) {
            return ((Circle2D) contour).parallel(dist);
        }

        // extract collection of parallel curves, that connect each other
        Collection<ICirculinearContinuousCurve2D> parallelCurves = getParallelElements(contour, dist);

        // Create a new boundary with the set of parallel curves
        return BoundaryPolyCirculinearCurve2D.create(parallelCurves.toArray(new ICirculinearContinuousCurve2D[0]), contour.isClosed());
    }

    /**
     * Compute the parallel curve of a Circulinear and continuous curve. The result is itself an instance of CirculinearContinuousCurve2D.
     */
    public ICirculinearContinuousCurve2D createContinuousParallel(ICirculinearContinuousCurve2D curve, double dist) {

        // For circulinear elements, getParallel() is already implemented
        if (curve instanceof ICirculinearElement2D) {
            return ((ICirculinearElement2D) curve).parallel(dist);
        }

        // extract collection of parallel curves, that connect each other
        Collection<ICirculinearContinuousCurve2D> parallelCurves = getParallelElements(curve, dist);

        // Create a new circulinear continuous curve with the set of parallel
        // curves
        return PolyCirculinearCurve2D.create(parallelCurves.toArray(new ICirculinearContinuousCurve2D[0]), curve.isClosed());
    }

    private Collection<ICirculinearContinuousCurve2D> getParallelElements(ICirculinearContinuousCurve2D curve, double dist) {

        // extract collection of circulinear elements
        Collection<? extends ICirculinearElement2D> elements = curve.smoothPieces();

        Iterator<? extends ICirculinearElement2D> iterator = elements.iterator();

        // previous curve
        ICirculinearElement2D previous = null;
        ICirculinearElement2D current = null;

        // create array for storing result
        ArrayList<ICirculinearContinuousCurve2D> parallelCurves = new ArrayList<>();

        // check if curve is empty
        if (!iterator.hasNext())
            return parallelCurves;

        // add parallel to the first curve
        current = iterator.next();
        ICirculinearElement2D parallel = current.parallel(dist);
        parallelCurves.add(parallel);

        // iterate on circulinear element couples
        ICirculinearContinuousCurve2D join;
        while (iterator.hasNext()) {
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
     * Compute the buffer of a circulinear curve.
     * <p>
     * The algorithm is as follow:
     * <ol>
     * <li>split the curve into a set of curves without self-intersections
     * <li>for each split curve, compute the contour of its buffer
     * <li>split self-intersecting contours into set of disjoint contours
     * <li>split all contour which intersect each other to disjoint contours
     * <li>remove contours which are too close from the original curve
     * <li>create a new domain with the final set of contours
     * </ol>
     */
    public ICirculinearDomain2D computeBuffer(ICirculinearCurve2D curve, double dist) {

        ArrayList<ICirculinearContour2D> contours = new ArrayList<>();

        // iterate on all continuous curves
        for (ICirculinearContinuousCurve2D cont : curve.continuousCurves()) {
            // split the curve into a set of non self-intersecting curves
            for (ICirculinearContinuousCurve2D splitted : CirculinearCurves2D.splitContinuousCurve(cont)) {
                // compute the rings composing the simple curve buffer
                contours.addAll(computeBufferSimpleCurve(splitted, dist));
            }
        }

        // split contours which intersect each others
        contours = new ArrayList<>(CirculinearCurves2D.splitIntersectingContours(contours));

        // Remove contours that cross or that are too close from base curve
        ArrayList<ICirculinearContour2D> contours2 = new ArrayList<>(contours.size());
        Collection<Point2D> intersects;
        Collection<Point2D> vertices;

        for (ICirculinearContour2D contour : contours) {

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
            double distCurves = getDistanceCurveSingularPoints(curve, contour);
            if (distCurves < dist - IShape2D.ACCURACY)
                continue;

            // keep the contours that meet the above conditions
            contours2.add(contour);
        }

        // All the rings are created, we can now create a new domain with the
        // set of rings
        return new GenericCirculinearDomain2D(CirculinearContourArray2D.create(contours2.toArray(new ICirculinearContour2D[0])));
    }

    /**
     * Compute buffer of a point set.
     */
    public ICirculinearDomain2D computeBuffer(IPointSet2D set, double dist) {
        // create array for storing result
        Collection<ICirculinearContour2D> contours = new ArrayList<>(set.size());

        // for each point, add a new circle
        for (Point2D point : set) {
            contours.add(new Circle2D(point, Math.abs(dist), dist > 0));
        }

        // process circles to remove intersections
        contours = CirculinearCurves2D.splitIntersectingContours(contours);

        // Remove contours that cross or that are too close from base curve
        ArrayList<ICirculinearContour2D> contours2 = new ArrayList<>(contours.size());
        for (ICirculinearContour2D ring : contours) {

            // check that vertices of contour are not too close from original
            // curve
            double minDist = CirculinearCurves2D.getDistanceCurvePoints(ring, set.points());
            if (minDist < dist - IShape2D.ACCURACY)
                continue;

            // keep the contours that meet the above conditions
            contours2.add(ring);
        }

        return new GenericCirculinearDomain2D(CirculinearContourArray2D.create(contours2.toArray(new ICirculinearContour2D[0])));
    }

    /**
     * Computes the buffer of a simple curve. This method should replace the method 'computeBufferSimpleContour'.
     */
    private Collection<? extends ICirculinearContour2D> computeBufferSimpleCurve(ICirculinearContinuousCurve2D curve, double d) {

        Collection<ICirculinearContour2D> contours = new ArrayList<>(2);

        // the parallel in each side
        ICirculinearContinuousCurve2D parallel1, parallel2;
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
        Collection<ICirculinearContour2D> contours2 = removeIntersectingContours(contours, curve, d);

        // return the set of created contours
        return contours2;
    }

    /**
     * Creates the unique contour based on two parallels of the base curve, by adding appropriate circle arcs at extremities of the base curve.
     */
    private Collection<ICirculinearContour2D> createSingleContourFromTwoParallels(ICirculinearContinuousCurve2D curve1, ICirculinearContinuousCurve2D curve2) {

        // create array for storing result
        ArrayList<ICirculinearContour2D> contours = new ArrayList<>();

        ICirculinearContinuousCurve2D cap;

        // create new ring using two open curves and two circle arcs
        if (curve1 != null && curve2 != null) {
            // array of elements for creating new ring.
            ArrayList<ICirculinearElement2D> elements = new ArrayList<>();

            // some shortcuts for computing infinity of curve
            boolean b0 = !Curves2DUtil.isLeftInfinite(curve1);
            boolean b1 = !Curves2DUtil.isRightInfinite(curve1);

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

    private Collection<ICirculinearContour2D> removeIntersectingContours(Collection<ICirculinearContour2D> contours, ICirculinearCurve2D curve, double d) {
        // prepare an array to store the set of rings
        ArrayList<ICirculinearContour2D> contours2 = new ArrayList<>();

        // iterate on the set of rings
        for (ICirculinearContour2D contour : contours)
            // split rings into curves which do not self-intersect
            for (ICirculinearContinuousCurve2D splitted : CirculinearCurves2D.splitContinuousCurve(contour)) {

                // compute distance to original curve
                // (assuming it is sufficient to compute distance to vertices
                // of the reference curve).
                double dist = CirculinearCurves2D.getDistanceCurvePoints(curve, splitted.singularPoints());

                // check if distance condition is verified
                if (dist - d < -IShape2D.ACCURACY)
                    continue;

                // convert the set of elements to a Circulinear ring
                contours2.add(convertCurveToBoundary(splitted));
            }

        // return the set of created rings
        return contours2;
    }

    /**
     * Converts the given continuous curve to an instance of CirculinearContour2D. This can be the curve itself, a new instance of GenericCirculinearRing2D if the curve is bounded, or a new instance of BoundaryPolyCirculinearCurve2D if the curve is unbounded.
     */
    private ICirculinearContour2D convertCurveToBoundary(ICirculinearContinuousCurve2D curve) {
        // basic case: curve is already a contour
        if (curve instanceof ICirculinearContour2D)
            return (ICirculinearContour2D) curve;

        // if the curve is closed, return an instance of GenericCirculinearRing2D
        if (curve.isClosed())
            return GenericCirculinearRing2D.create(curve.smoothPieces().toArray(new ICirculinearElement2D[0]));

        return BoundaryPolyCirculinearCurve2D.create(curve.smoothPieces().toArray(new ICirculinearContinuousCurve2D[0]));
    }

    private double getDistanceCurveSingularPoints(ICirculinearCurve2D ref, ICirculinearCurve2D curve) {
        // extract singular points
        Collection<Point2D> points = curve.singularPoints();

        // If no singular point, choose an arbitrary point on the curve
        if (points.isEmpty()) {
            points = new ArrayList<>();
            double t = Curves2DUtil.choosePosition(curve.t0(), curve.t1());
            points.add(curve.point(t));
        }

        // Iterate on points to get minimal distance
        double minDist = Double.MAX_VALUE;
        for (Point2D point : points) {
            minDist = Math.min(minDist, ref.distance(point));
        }
        return minDist;
    }
}
