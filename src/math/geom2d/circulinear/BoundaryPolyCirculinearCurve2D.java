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
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.Curve2DUtils;
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
public class BoundaryPolyCirculinearCurve2D<T extends ContinuousCirculinearCurve2D>
extends PolyCirculinearCurve2D<T> 
implements ContinuousCirculinearCurve2D, CirculinearContour2D {

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
	 * @see math.geom2d.circulinear.ContinuousCirculinearCurve2D#getParallel(double)
	 */
	public ContinuousCirculinearCurve2D getParallel(double d) {
		return CirculinearCurve2DUtils.createContinuousParallel(this, d);
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#transform(math.geom2d.transform.CircleInversion2D)
	 */
	public BoundaryPolyCirculinearCurve2D<? extends ContinuousCirculinearCurve2D>
	transform(CircleInversion2D inv) {
    	// Allocate array for result
		int n = curves.size();
		BoundaryPolyCirculinearCurve2D<ContinuousCirculinearCurve2D> result = 
			new BoundaryPolyCirculinearCurve2D<ContinuousCirculinearCurve2D>(n);
        
        // add each transformed curve
        for (ContinuousCirculinearCurve2D curve : curves)
            result.addCurve(curve.transform(inv));
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
	 * @see math.geom2d.domain.Boundary2D#getBoundaryCurves()
	 */
	public Collection<BoundaryPolyCirculinearCurve2D<T>> getBoundaryCurves() {
        ArrayList<BoundaryPolyCirculinearCurve2D<T>> list = 
            new ArrayList<BoundaryPolyCirculinearCurve2D<T>>(1);
        list.add(this);
        return list;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.domain.Boundary2D#getDomain()
	 */
	public CirculinearDomain2D getDomain() {
		return new GenericCirculinearDomain2D(this);
	}

	// ===================================================================
    // methods implementing the ContinuousCurve2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.ContinuousCirculinearCurve2D#getSmoothPieces()
     */
    public Collection<? extends CirculinearElement2D> getSmoothPieces() {
    	// create array for storing result
    	ArrayList<CirculinearElement2D> result = 
    		new ArrayList<CirculinearElement2D>();
    	
    	// add elements of each curve
    	for(ContinuousCirculinearCurve2D curve : curves)
    		result.addAll(curve.getSmoothPieces());
    	
    	// return the collection
        return result;
    }

    // ===================================================================
    // methods implementing the Curve2D interface

    @Override
    public Collection<? extends BoundaryPolyCirculinearCurve2D<?>> 
    getContinuousCurves() {
        ArrayList<BoundaryPolyCirculinearCurve2D<T>> list = 
        	new ArrayList<BoundaryPolyCirculinearCurve2D<T>>(1);
        list.add(this);
        return list;
    }

	public BoundaryPolyCirculinearCurve2D<? extends ContinuousCirculinearCurve2D> 
	getReverseCurve() {
    	int n = curves.size();
        // create array of reversed curves
    	ContinuousCirculinearCurve2D[] curves2 = 
    		new ContinuousCirculinearCurve2D[n];
        
        // reverse each curve
        for (int i = 0; i<n; i++)
            curves2[i] = curves.get(n-1-i).getReverseCurve();
        
        // create the reversed final curve
        return new BoundaryPolyCirculinearCurve2D<ContinuousCirculinearCurve2D>(curves2);
	}
	
	public PolyCirculinearCurve2D<? extends ContinuousCirculinearCurve2D>
	getSubCurve(double t0, double t1) {
		// Call the superclass method
		PolyOrientedCurve2D<? extends ContinuousOrientedCurve2D> subcurve =
			super.getSubCurve(t0, t1);
		
		// prepare result
		int n = subcurve.getCurveNumber();
		PolyCirculinearCurve2D<ContinuousCirculinearCurve2D> result = 
			new PolyCirculinearCurve2D<ContinuousCirculinearCurve2D>(n);
		
		// add each curve after class cast
		for(Curve2D curve : subcurve) {
			if(curve instanceof ContinuousCirculinearCurve2D)
				result.addCurve((ContinuousCirculinearCurve2D) curve);
		}
		
		// return the result
		return result;
	}

    // ===================================================================
    // methods implementing the Shape2D interface

    public CirculinearCurveSet2D<? extends ContinuousCirculinearCurve2D> 
	clip(Box2D box) {
        // Clip the curve
        CurveSet2D<Curve2D> set = Curve2DUtils.clipCurve(this, box);

        // Stores the result in appropriate structure
        int n = set.getCurveNumber();
        CirculinearCurveSet2D<ContinuousCirculinearCurve2D> result = 
        	new CirculinearCurveSet2D<ContinuousCirculinearCurve2D>(n);

        // convert the result, class cast each curve
        for (Curve2D curve : set.getCurves()) {
            if (curve instanceof ContinuousCirculinearCurve2D)
                result.addCurve((ContinuousCirculinearCurve2D) curve);
        }
        
        // return the new set of curves
        return result;
	}
	
	@Override
	public BoundaryPolyCurve2D<? extends ContinuousOrientedCurve2D> 
	transform(AffineTransform2D trans) {
		// number of curves
		int n = this.getCurveNumber();
		
		// create result curve
		BoundaryPolyCurve2D<ContinuousOrientedCurve2D> result =
        	new BoundaryPolyCurve2D<ContinuousOrientedCurve2D>(n);
        
        // add each curve after class cast
        for (ContinuousOrientedCurve2D curve : curves)
            result.addCurve(curve.transform(trans));
        
        result.setClosed(this.isClosed());
        return result;
	}

}