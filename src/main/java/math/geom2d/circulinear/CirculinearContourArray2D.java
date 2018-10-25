/**
 * File: 	CirculinearContourArray2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 mai 09
 */
package math.geom2d.circulinear;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import math.geom2d.Box2D;
import math.geom2d.circulinear.buffer.BufferCalculator;
import math.geom2d.curve.ICurve2D;
import math.geom2d.curve.Curves2D;
import math.geom2d.curve.ICurveSet2D;
import math.geom2d.domain.IContinuousOrientedCurve2D;
import math.geom2d.domain.ContourArray2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * A circulinear boundary which is composed of several CirculinearContour2D.
 * <p>
 * Creates an array of circular contours:
 * 
 * <pre>
 * <code>
 *  Circle2D circle1 = new Circle2D(new Point2D(0, 100), 30);
 *	Circle2D circle2 = new Circle2D(new Point2D(0, 100), 30);
 *	CirculinearContourArray2D<Circle2D> array =
 *      CirculinearContourArray2D.create(new Circle2D[]{circle1, circle2});
 * </code>
 * </pre>
 * 
 * @author dlegland
 *
 */
public class CirculinearContourArray2D<T extends ICirculinearContour2D> extends ContourArray2D<T> implements ICirculinearBoundary2D {
    private static final long serialVersionUID = 1L;

    /**
     * Static factory for creating a new CirculinearContourArray2D from a collection of curves.
     * 
     * @since 0.8.1
     */
    /*
     * public static <T extends CirculinearContour2D> CirculinearContourArray2D<T> create(Collection<T> curves) { return new CirculinearContourArray2D<T>(curves); }
     */

    /**
     * Static factory for creating a new CirculinearContourArray2D from an array of curves.
     * 
     * @since 0.8.1
     */
    @SafeVarargs
    public static <T extends ICirculinearContour2D> CirculinearContourArray2D<T> create(T... curves) {
        return new CirculinearContourArray2D<T>(curves);
    }

    // ===================================================================
    // constructors

    /**
     * Empty constructor. Initializes an empty array of curves.
     */
    public CirculinearContourArray2D() {
        this.curves = new ArrayList<T>();
    }

    /**
     * Empty constructor. Initializes an empty array of curves, with a given size for allocating memory.
     */
    public CirculinearContourArray2D(int n) {
        this.curves = new ArrayList<T>(n);
    }

    /**
     * Constructor from an array of curves.
     * 
     * @param curves
     *            the array of curves in the set
     */
    @SafeVarargs
    public CirculinearContourArray2D(T... curves) {
        this.curves = new ArrayList<T>(curves.length);
        for (T element : curves)
            this.add(element);
    }

    /**
     * Constructor from a single curve.
     * 
     * @param curve
     *            the initial contour contained in the array
     */
    public CirculinearContourArray2D(T curve) {
        this.curves = new ArrayList<T>();
        this.curves.add(curve);
    }

    /**
     * Constructor from a collection of curves. The curves are added to the inner collection of curves.
     * 
     * @param curves
     *            the collection of curves to add to the set
     */
    public CirculinearContourArray2D(Collection<? extends T> curves) {
        this.curves = new ArrayList<T>(curves.size());
        this.curves.addAll(curves);
    }

    // ===================================================================
    // methods specific to Boundary2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.domain.Boundary2D#domain()
     */
    public ICirculinearDomain2D domain() {
        return new GenericCirculinearDomain2D(this);
    }

    // ===================================================================
    // methods implementing the CirculinearCurve2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearCurve2D#length()
     */
    public double length() {
        double sum = 0;
        for (ICirculinearCurve2D curve : this.curves())
            sum += curve.length();
        return sum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearCurve2D#length(double)
     */
    public double length(double pos) {
        return CirculinearCurves2D.getLength(this, pos);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearCurve2D#position(double)
     */
    public double position(double length) {
        return CirculinearCurves2D.getPosition(this, length);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearShape2D#buffer(double)
     */
    public ICirculinearDomain2D buffer(double dist) {
        BufferCalculator bc = BufferCalculator.getDefaultInstance();
        return bc.computeBuffer(this, dist);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearContinuousCurve2D#parallel(double)
     */
    public ICirculinearBoundary2D parallel(double d) {
        BufferCalculator bc = BufferCalculator.getDefaultInstance();
        return bc.createParallelBoundary(this, d);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearCurve2D#transform(math.geom2d.transform.CircleInversion2D)
     */
    public CirculinearContourArray2D<? extends ICirculinearContour2D> transform(CircleInversion2D inv) {
        // Allocate array for result
        CirculinearContourArray2D<ICirculinearContour2D> result = new CirculinearContourArray2D<ICirculinearContour2D>(curves.size());

        // add each transformed curve
        for (ICirculinearContour2D curve : curves)
            result.add(curve.transform(inv));
        return result;
    }

    // ===================================================================
    // methods implementing the Curve2D interface

    @Override
    public Collection<T> continuousCurves() {
        return Collections.unmodifiableCollection(this.curves);
    }

    @Override
    public ICirculinearCurveSet2D<? extends ICirculinearContinuousCurve2D> clip(Box2D box) {
        // Clip the curve
        ICurveSet2D<? extends ICurve2D> set = Curves2D.clipCurve(this, box);

        // Stores the result in appropriate structure
        int n = set.size();
        CirculinearCurveArray2D<ICirculinearContinuousCurve2D> result = new CirculinearCurveArray2D<ICirculinearContinuousCurve2D>(n);

        // convert the result, class cast each curve
        for (ICurve2D curve : set.curves()) {
            if (curve instanceof ICirculinearContinuousCurve2D)
                result.add((ICirculinearContinuousCurve2D) curve);
        }

        // return the new set of curves
        return result;
    }

    @Override
    public CirculinearContourArray2D<? extends ICirculinearContour2D> reverse() {
        int n = curves.size();
        // create array of reversed curves
        ICirculinearContour2D[] curves2 = new ICirculinearContour2D[n];

        // reverse each curve
        for (int i = 0; i < n; i++)
            curves2[i] = curves.get(n - 1 - i).reverse();

        // create the reversed final curve
        return new CirculinearContourArray2D<ICirculinearContour2D>(curves2);
    }

    @Override
    public ICirculinearCurveSet2D<? extends ICirculinearContinuousCurve2D> subCurve(double t0, double t1) {
        // get the subcurve
        ICurveSet2D<? extends IContinuousOrientedCurve2D> curveSet = super.subCurve(t0, t1);

        // create subcurve array
        ArrayList<ICirculinearContinuousCurve2D> curves = new ArrayList<ICirculinearContinuousCurve2D>(curveSet.size());

        // class cast each curve
        for (ICurve2D curve : curveSet.curves())
            curves.add((ICirculinearContinuousCurve2D) curve);

        // Create CurveSet for the result
        return CirculinearCurveArray2D.create(curves.toArray(new ICirculinearElement2D[0]));
    }
}
