/**
 * File: 	CirculinearRing2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 mai 09
 */
package math.geom2d.circulinear;

import java.awt.Graphics2D;
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.domain.BoundaryPolyCurve2D;
import math.geom2d.domain.ContinuousOrientedCurve2D;
import math.geom2d.transform.CircleInversion2D;


/**
 * A basic implementation of a CirculinearContour2D, which is supposed to be
 * always bounded and closed.
 * @author dlegland
 *
 */
public class CirculinearRing2D 
extends PolyCirculinearCurve2D<CirculinearElement2D> 
implements CirculinearContour2D {
//TODO: transform to an interface
//TODO: parameterize with ContinuousCirculinearCurve
    // ===================================================================
    // constructors

    public CirculinearRing2D() {
        super();
        this.closed = true;
    }

    public CirculinearRing2D(int size) {
        super(size);
        this.closed = true;
    }

    public CirculinearRing2D(CirculinearElement2D[] curves) {
        super(curves, true);
        this.closed = true;
        }

    public CirculinearRing2D(CirculinearElement2D[] curves, 
    		boolean closed) {
        super(curves, closed);
    }

    public CirculinearRing2D(
    		Collection<? extends CirculinearElement2D> curves) {
        super(curves);
        this.closed = true;
       }

    public CirculinearRing2D(
    		Collection<? extends CirculinearElement2D> curves, 
    		boolean closed) {
        super(curves, closed);
    }


    // ===================================================================
    // static methods

	@Override
    public CirculinearRing2D getParallel(double dist) {
    	return new CirculinearRing2D(
    			CirculinearCurve2DUtils.createContinuousParallel(this, dist)
    			.getSmoothPieces());
    }
    
	@Override
	public CirculinearRing2D transform(CircleInversion2D inv) {
    	// Allocate array for result
		CirculinearRing2D result = new CirculinearRing2D(curves.size());
        
        // add each transformed element
        for (CirculinearElement2D element : curves)
            result.addCurve(element.transform(inv));
        return result;
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.domain.Boundary2D#fill(java.awt.Graphics2D)
	 */
	public void fill(Graphics2D g2) {
		g2.fill(this.getGeneralPath());
	}

	/* (non-Javadoc)
	 * @see math.geom2d.domain.Boundary2D#getBoundaryCurves()
	 */
	public Collection<? extends CirculinearContour2D> getBoundaryCurves() {
        return wrapCurve(this);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.domain.Boundary2D#getDomain()
	 */
	public CirculinearDomain2D getDomain() {
		return new GenericCirculinearDomain2D(this);
	}

	@Override
    public Collection<? extends CirculinearRing2D> getContinuousCurves() {
    	return wrapCurve(this);
    }

	@Override
    public CirculinearRing2D getReverseCurve(){
    	int n = curves.size();
        // create array of reversed curves
    	CirculinearElement2D[] curves2 = new CirculinearElement2D[n];
        
        // reverse each curve
        for (int i = 0; i<n; i++)
            curves2[i] = curves.get(n-1-i).getReverseCurve();
        
        // create the reversed final curve
        return new CirculinearRing2D(curves2);
	}
	
	@Override
	public BoundaryPolyCurve2D<ContinuousOrientedCurve2D> 
	transform(AffineTransform2D trans) {
		// number of curves
		int n = this.getCurveNumber();
		
		// create result curve
        BoundaryPolyCurve2D<ContinuousOrientedCurve2D> result =
        	new BoundaryPolyCurve2D<ContinuousOrientedCurve2D>(n);
        
        // add each curve after class cast
        for (ContinuousOrientedCurve2D curve : curves)
            result.addCurve(curve.transform(trans));
        return result;
	}

}
