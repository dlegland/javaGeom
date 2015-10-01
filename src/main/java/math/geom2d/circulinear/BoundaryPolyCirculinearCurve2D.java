/**
 * File: 	BoundaryPolyCirculinearCurve2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 mai 09
 */
package math.geom2d.circulinear;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.circulinear.buffer.BufferCalculator;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.Curves2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.domain.BoundaryPolyCurve2D;
import math.geom2d.domain.ContinuousOrientedCurve2D;
import math.geom2d.domain.PolyOrientedCurve2D;
import math.geom2d.transform.CircleInversion2D;


/**
 * A continuous boundary which is composed of several continuous circulinear
 * curves. Instances of this class can be circulinear rings (composed of
 * several continuous and finite circulinear curves that form a loop), or
 * an open curve with two infinite circulinear curve at each extremity.
 * @author dlegland
 *
 */
public class BoundaryPolyCirculinearCurve2D<T extends CirculinearContinuousCurve2D>
extends PolyCirculinearCurve2D<T> 
implements CirculinearContinuousCurve2D, CirculinearContour2D {

    // ===================================================================
    // static methods

    /**
     * Static factory for creating a new BoundaryPolyCirculinearCurve2D from a
     * collection of curves.
     * @since 0.8.1
     */
	/*public static <T extends CirculinearContinuousCurve2D>
	BoundaryPolyCirculinearCurve2D<T>
	create(Collection<T> curves) {
		return new BoundaryPolyCirculinearCurve2D<T>(curves);
	}*/

    /**
     * Static factory for creating a new BoundaryPolyCirculinearCurve2D from a
     * collection of curves.
     * @since 0.8.1
     */
	/*public static <T extends CirculinearContinuousCurve2D> 
	BoundaryPolyCirculinearCurve2D<T>
	create(Collection<T> curves, boolean closed) {
		return new BoundaryPolyCirculinearCurve2D<T>(curves, closed);
	}*/

    /**
     * Static factory for creating a new BoundaryPolyCirculinearCurve2D from an
     * array of curves.
     * @since 0.8.1
     */
	@SafeVarargs
    public static <T extends CirculinearContinuousCurve2D> 
    BoundaryPolyCirculinearCurve2D<T> create(T... curves) {
    	return new BoundaryPolyCirculinearCurve2D<T>(curves);
    }

    /**
     * Static factory for creating a new BoundaryPolyCirculinearCurve2D from an
     * array of curves.
     * @since 0.8.1
     */
    public static <T extends CirculinearContinuousCurve2D> 
    BoundaryPolyCirculinearCurve2D<T> create(T[] curves, boolean closed) {
    	return new BoundaryPolyCirculinearCurve2D<T>(curves, closed);
    }

    /**
     * Static factory for creating a new BoundaryPolyCirculinearCurve2D from an
     * array of curves.
     * @since 0.8.1
     */
	@SafeVarargs
    public static <T extends CirculinearContinuousCurve2D> 
    BoundaryPolyCirculinearCurve2D<T> createClosed(T... curves) {
    	return new BoundaryPolyCirculinearCurve2D<T>(curves, true);
    }

    
    // ===================================================================
    // constructors

    public BoundaryPolyCirculinearCurve2D() {
        super();
    }

    public BoundaryPolyCirculinearCurve2D(int size) {
        super(size);
    }

    public BoundaryPolyCirculinearCurve2D(T[] curves) {
        super(curves);
    }

    public BoundaryPolyCirculinearCurve2D(T[] curves, boolean closed) {
        super(curves, closed);
    }

    public BoundaryPolyCirculinearCurve2D(Collection<? extends T> curves) {
        super(curves);
    }

    public BoundaryPolyCirculinearCurve2D(Collection<? extends T> curves, boolean closed) {
        super(curves, closed);
    }

    
    // ===================================================================
    // methods implementing the CirculinearCurve2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#length()
	 */
	@Override
	public double length() {
		double sum = 0;
		for(CirculinearCurve2D curve : this.curves())
			sum += curve.length();
		return sum;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#length(double)
	 */
	@Override
	public double length(double pos) {
		return CirculinearCurves2D.getLength(this, pos);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#position(double)
	 */
	@Override
	public double position(double length) {
		return CirculinearCurves2D.getPosition(this, length);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearContinuousCurve2D#parallel(double)
	 */
    @Override
	public CirculinearRing2D parallel(double dist) {
		BufferCalculator bc = BufferCalculator.getDefaultInstance();

    	return GenericCirculinearRing2D.create(
    			bc.createContinuousParallel(this, dist).smoothPieces().toArray(new CirculinearElement2D[0]));
    }
    
	
	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#transform(math.geom2d.transform.CircleInversion2D)
	 */
	@Override
	public BoundaryPolyCirculinearCurve2D<? extends CirculinearContinuousCurve2D>
	transform(CircleInversion2D inv) {
    	// Allocate array for result
		int n = curves.size();
		BoundaryPolyCirculinearCurve2D<CirculinearContinuousCurve2D> result = 
			new BoundaryPolyCirculinearCurve2D<CirculinearContinuousCurve2D>(n);
        
        // add each transformed curve
        for (CirculinearContinuousCurve2D curve : curves)
            result.add(curve.transform(inv));
        return result;
	}

	// ===================================================================
    // methods implementing the Boundary2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.domain.Boundary2D#fill(java.awt.Graphics2D)
	 */
	public void fill(Graphics2D g2) {
		g2.fill(this.getGeneralPath());
	}

	/* (non-Javadoc)
	 * @see math.geom2d.domain.Boundary2D#domain()
	 */
	public CirculinearDomain2D domain() {
		return new GenericCirculinearDomain2D(this);
	}

	// ===================================================================
    // methods implementing the ContinuousCurve2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.CirculinearContinuousCurve2D#smoothPieces()
     */
    @Override
	public Collection<? extends CirculinearElement2D> smoothPieces() {
    	// create array for storing result
    	ArrayList<CirculinearElement2D> result = 
    		new ArrayList<CirculinearElement2D>();
    	
    	// add elements of each curve
    	for(CirculinearContinuousCurve2D curve : curves)
    		result.addAll(curve.smoothPieces());
    	
    	// return the collection
        return result;
    }

    // ===================================================================
    // methods implementing the Curve2D interface

    @Override
    public Collection<? extends BoundaryPolyCirculinearCurve2D<?>> 
    continuousCurves() {
    	return wrapCurve(this);
    }

	@Override
	public BoundaryPolyCirculinearCurve2D<? extends CirculinearContinuousCurve2D> 
	reverse() {
    	int n = curves.size();
        // create array of reversed curves
    	CirculinearContinuousCurve2D[] curves2 = 
    		new CirculinearContinuousCurve2D[n];
        
        // reverse each curve
        for (int i = 0; i<n; i++)
            curves2[i] = curves.get(n-1-i).reverse();
        
        // create the reversed final curve
        return new BoundaryPolyCirculinearCurve2D<CirculinearContinuousCurve2D>(curves2);
	}
	
	@Override
	public PolyCirculinearCurve2D<? extends CirculinearContinuousCurve2D>
	subCurve(double t0, double t1) {
		// Call the superclass method
		PolyOrientedCurve2D<? extends ContinuousOrientedCurve2D> subcurve =
			super.subCurve(t0, t1);
		
		// prepare result
		int n = subcurve.size();
		PolyCirculinearCurve2D<CirculinearContinuousCurve2D> result = 
			new PolyCirculinearCurve2D<CirculinearContinuousCurve2D>(n);
		
		// add each curve after class cast
		for(Curve2D curve : subcurve) {
			if(curve instanceof CirculinearContinuousCurve2D)
				result.add((CirculinearContinuousCurve2D) curve);
		}
		
		// return the result
		return result;
	}

    // ===================================================================
    // methods implementing the Shape2D interface

    @Override
	public CirculinearCurveSet2D<? extends CirculinearContinuousCurve2D> 
	clip(Box2D box) {
        // Clip the curve
        CurveSet2D<? extends Curve2D> set = Curves2D.clipCurve(this, box);

        // Stores the result in appropriate structure
        int n = set.size();
        CirculinearCurveArray2D<CirculinearContinuousCurve2D> result = 
        	new CirculinearCurveArray2D<CirculinearContinuousCurve2D>(n);

        // convert the result, class cast each curve
        for (Curve2D curve : set.curves()) {
            if (curve instanceof CirculinearContinuousCurve2D)
                result.add((CirculinearContinuousCurve2D) curve);
        }
        
        // return the new set of curves
        return result;
	}
	
	@Override
	public BoundaryPolyCurve2D<? extends ContinuousOrientedCurve2D> 
	transform(AffineTransform2D trans) {
		// number of curves
		int n = this.size();
		
		// create result curve
		BoundaryPolyCurve2D<ContinuousOrientedCurve2D> result =
        	new BoundaryPolyCurve2D<ContinuousOrientedCurve2D>(n);
        
        // add each curve after class cast
        for (ContinuousOrientedCurve2D curve : curves)
            result.add(curve.transform(trans));
        
        result.setClosed(this.isClosed());
        return result;
	}

}