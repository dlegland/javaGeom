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
import math.geom2d.curve.*;
import math.geom2d.domain.ContinuousOrientedCurve2D;
import math.geom2d.domain.PolyOrientedCurve2D;
import math.geom2d.transform.CircleInversion2D;


/**
 * A continuous curve which is composed of several continuous circulinear
 * curves.
 * @author dlegland
 *
 */
public class PolyCirculinearCurve2D<T extends CirculinearContinuousCurve2D>
extends PolyOrientedCurve2D<T> implements CirculinearContinuousCurve2D {

    // ===================================================================
    // static constructors

    /**
     * Static factory for creating a new PolyCirculinearCurve2D from a
     * collection of curves.
     * @since 0.8.1
     */
    public static <T extends CirculinearContinuousCurve2D> 
    PolyCirculinearCurve2D<T> create(Collection<T> curves) {
    	return new PolyCirculinearCurve2D<T>(curves);
    }
    
    /**
     * Static factory for creating a new PolyCirculinearCurve2D from an array
     * of curves.
     * @since 0.8.1
     */
    public static <T extends CirculinearContinuousCurve2D> 
    PolyCirculinearCurve2D<T> create(T[] curves) {
    	return new PolyCirculinearCurve2D<T>(curves);
    }

    /**
     * Static factory for creating a new PolyCirculinearCurve2D from a
     * collection of curves and a flag indicating if the curve is closed.
     * @since 0.9.0
     */
    public static <T extends CirculinearContinuousCurve2D> 
    PolyCirculinearCurve2D<T> create(Collection<T> curves, boolean closed) {
    	return new PolyCirculinearCurve2D<T>(curves, closed);
    }
    
    /**
     * Static factory for creating a new PolyCirculinearCurve2D from an array
     * of curves and a flag indicating if the curve is closed.
     * @since 0.9.0
     */
    public static <T extends CirculinearContinuousCurve2D> 
    PolyCirculinearCurve2D<T> create(T[] curves, boolean closed) {
    	return new PolyCirculinearCurve2D<T>(curves, closed);
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

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#getLength()
	 */
	public double getLength() {
		double sum = 0;
		for(CirculinearCurve2D curve : this.getCurves())
			sum += curve.getLength();
		return sum;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#getLength(double)
	 */
	public double getLength(double pos) {
		return CirculinearCurve2DUtils.getLength(this, pos);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#getPosition(double)
	 */
	public double getPosition(double length) {
		return CirculinearCurve2DUtils.getPosition(this, length);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearShape2D#getBuffer(double)
	 */
	public CirculinearDomain2D getBuffer(double dist) {
		return CirculinearCurve2DUtils.computeBuffer(this, dist);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearContinuousCurve2D#getParallel(double)
	 */
	public CirculinearContinuousCurve2D getParallel(double d) {
		return CirculinearCurve2DUtils.createContinuousParallel(this, d);
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#transform(math.geom2d.transform.CircleInversion2D)
	 */
	public PolyCirculinearCurve2D<? extends CirculinearContinuousCurve2D>
	transform(CircleInversion2D inv) {
    	// Allocate array for result
		int n = curves.size();
		PolyCirculinearCurve2D<CirculinearContinuousCurve2D> result = 
			new PolyCirculinearCurve2D<CirculinearContinuousCurve2D>(n);
        
        // add each transformed curve
        for (CirculinearContinuousCurve2D curve : curves)
            result.addCurve(curve.transform(inv));
        return result;
	}

    // ===================================================================
    // methods implementing the ContinuousCurve2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.CirculinearContinuousCurve2D#getSmoothPieces()
     */
    @Override
	public Collection<? extends CirculinearElement2D> getSmoothPieces() {
    	// create array for storing result
    	ArrayList<CirculinearElement2D> result = 
    		new ArrayList<CirculinearElement2D>();
    	
    	// add elements of each curve
    	for(CirculinearContinuousCurve2D curve : curves)
    		result.addAll(curve.getSmoothPieces());
    	
    	// return the collection
        return result;
    }

    // ===================================================================
    // methods implementing the Curve2D interface

    @Override
    public Collection<? extends PolyCirculinearCurve2D<?>> 
    getContinuousCurves() {
    	return wrapCurve(this);
    }

    @Override
	public CirculinearCurveSet2D<? extends CirculinearContinuousCurve2D> 
	clip(Box2D box) {
        // Clip the curve
        CurveSet2D<? extends Curve2D> set = Curve2DUtils.clipCurve(this, box);

        // Stores the result in appropriate structure
        int n = set.getCurveNumber();
        CirculinearCurveSet2D<CirculinearContinuousCurve2D> result = 
        	new CirculinearCurveSet2D<CirculinearContinuousCurve2D>(n);

        // convert the result, class cast each curve
        for (Curve2D curve : set.getCurves()) {
            if (curve instanceof CirculinearContinuousCurve2D)
                result.addCurve((CirculinearContinuousCurve2D) curve);
        }
        
        // return the new set of curves
        return result;
	}
	
	@Override
	public PolyCirculinearCurve2D<? extends CirculinearContinuousCurve2D> 
	getReverseCurve() {
    	int n = curves.size();
        // create array of reversed curves
    	CirculinearContinuousCurve2D[] curves2 = 
    		new CirculinearContinuousCurve2D[n];
        
        // reverse each curve
        for (int i = 0; i<n; i++)
            curves2[i] = curves.get(n-1-i).getReverseCurve();
        
        // create the reversed final curve
        return PolyCirculinearCurve2D.create(curves2, this.closed);
        //return PolyCirculinearCurve2D.create(curves2);
	}
	
	@Override
	public PolyCirculinearCurve2D<? extends CirculinearContinuousCurve2D>
	getSubCurve(double t0, double t1) {
		// Call the superclass method
		PolyOrientedCurve2D<? extends ContinuousOrientedCurve2D> subcurve =
			super.getSubCurve(t0, t1);
		
		// prepare result
		int n = subcurve.getCurveNumber();
		PolyCirculinearCurve2D<CirculinearContinuousCurve2D> result = 
			new PolyCirculinearCurve2D<CirculinearContinuousCurve2D>(n);
		
		// add each curve after class cast
		for(Curve2D curve : subcurve) {
			if(curve instanceof CirculinearContinuousCurve2D)
				result.addCurve((CirculinearContinuousCurve2D) curve);
		}
		
		// return the result
		return result;
	}

}