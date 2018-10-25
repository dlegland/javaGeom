/**
 * File: 	PolyCirculinearCurve2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 mai 09
 */
package math.geom2d.circulinear;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Box2D;
import math.geom2d.circulinear.buffer.BufferCalculator;
import math.geom2d.curve.*;
import math.geom2d.domain.IContinuousOrientedCurve2D;
import math.geom2d.domain.PolyOrientedCurve2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * A continuous curve which is composed of several continuous circulinear curves.
 * 
 * @author dlegland
 *
 */
public class PolyCirculinearCurve2D<T extends ICirculinearContinuousCurve2D> extends PolyOrientedCurve2D<T> implements ICirculinearContinuousCurve2D {

    // ===================================================================
    // static constructors

    /**
     * Static factory for creating a new PolyCirculinearCurve2D from a collection of curves.
     * 
     * @since 0.8.1
     */
    /*
     * public static <T extends CirculinearContinuousCurve2D> PolyCirculinearCurve2D<T> create(Collection<T> curves) { return new PolyCirculinearCurve2D<T>(curves); }
     */

    /**
     * Static factory for creating a new PolyCirculinearCurve2D from an array of curves.
     * 
     * @since 0.8.1
     */
    @SafeVarargs
    public static <T extends ICirculinearContinuousCurve2D> PolyCirculinearCurve2D<T> create(T... curves) {
        return new PolyCirculinearCurve2D<T>(curves);
    }

    /**
     * Static factory for creating a new PolyCirculinearCurve2D from a collection of curves and a flag indicating if the curve is closed.
     * 
     * @since 0.9.0
     */
    /*
     * public static <T extends CirculinearContinuousCurve2D> PolyCirculinearCurve2D<T> create(Collection<T> curves, boolean closed) { return new PolyCirculinearCurve2D<T>(curves, closed); }
     */

    /**
     * Static factory for creating a new PolyCirculinearCurve2D from an array of curves and a flag indicating if the curve is closed.
     * 
     * @since 0.9.0
     */
    public static <T extends ICirculinearContinuousCurve2D> PolyCirculinearCurve2D<T> create(T[] curves, boolean closed) {
        return new PolyCirculinearCurve2D<T>(curves, closed);
    }

    /**
     * Static factory for creating a new PolyCirculinearCurve2D from an array of curves and a flag indicating if the curve is closed.
     * 
     * @since 0.9.0
     */
    @SafeVarargs
    public static <T extends ICirculinearContinuousCurve2D> PolyCirculinearCurve2D<T> createClosed(T... curves) {
        return new PolyCirculinearCurve2D<T>(curves, true);
    }

    // ===================================================================
    // constructors

    public PolyCirculinearCurve2D() {
        super();
    }

    public PolyCirculinearCurve2D(int size) {
        super(size);
    }

    public PolyCirculinearCurve2D(T[] curves) {
        super(curves);
    }

    public PolyCirculinearCurve2D(T[] curves, boolean closed) {
        super(curves, closed);
    }

    public PolyCirculinearCurve2D(Collection<? extends T> curves) {
        super(curves);
    }

    public PolyCirculinearCurve2D(Collection<? extends T> curves, boolean closed) {
        super(curves, closed);
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
    public ICirculinearContinuousCurve2D parallel(double d) {
        BufferCalculator bc = BufferCalculator.getDefaultInstance();
        return bc.createContinuousParallel(this, d);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearCurve2D#transform(math.geom2d.transform.CircleInversion2D)
     */
    public PolyCirculinearCurve2D<? extends ICirculinearContinuousCurve2D> transform(CircleInversion2D inv) {
        // Allocate array for result
        int n = curves.size();
        PolyCirculinearCurve2D<ICirculinearContinuousCurve2D> result = new PolyCirculinearCurve2D<ICirculinearContinuousCurve2D>(n);

        // add each transformed curve
        for (ICirculinearContinuousCurve2D curve : curves)
            result.add(curve.transform(inv));
        return result;
    }

    // ===================================================================
    // methods implementing the ContinuousCurve2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.CirculinearContinuousCurve2D#smoothPieces()
     */
    @Override
    public Collection<? extends ICirculinearElement2D> smoothPieces() {
        // create array for storing result
        ArrayList<ICirculinearElement2D> result = new ArrayList<ICirculinearElement2D>();

        // add elements of each curve
        for (ICirculinearContinuousCurve2D curve : curves)
            result.addAll(curve.smoothPieces());

        // return the collection
        return result;
    }

    // ===================================================================
    // methods implementing the Curve2D interface

    @Override
    public Collection<? extends PolyCirculinearCurve2D<?>> continuousCurves() {
        return wrapCurve(this);
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
    public PolyCirculinearCurve2D<? extends ICirculinearContinuousCurve2D> reverse() {
        // create array of reversed curves
        int n = curves.size();
        ICirculinearContinuousCurve2D[] curves2 = new ICirculinearContinuousCurve2D[n];

        // reverse each curve
        for (int i = 0; i < n; i++)
            curves2[i] = curves.get(n - 1 - i).reverse();

        // create the reversed final curve
        return PolyCirculinearCurve2D.create(curves2, this.closed);
    }

    @Override
    public PolyCirculinearCurve2D<? extends ICirculinearContinuousCurve2D> subCurve(double t0, double t1) {
        // Call the superclass method
        PolyOrientedCurve2D<? extends IContinuousOrientedCurve2D> subcurve = super.subCurve(t0, t1);

        // prepare result
        int n = subcurve.size();
        PolyCirculinearCurve2D<ICirculinearContinuousCurve2D> result = new PolyCirculinearCurve2D<ICirculinearContinuousCurve2D>(n);

        // add each curve after class cast
        for (ICurve2D curve : subcurve) {
            if (curve instanceof ICirculinearContinuousCurve2D)
                result.add((ICirculinearContinuousCurve2D) curve);
        }

        // return the result
        return result;
    }

}