/**
 * File: 	GenericCirculinearRing2D.java
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
import math.geom2d.domain.BoundaryPolyCurve2D;
import math.geom2d.domain.ContinuousOrientedCurve2D;
import math.geom2d.transform.CircleInversion2D;


/**
 * A basic implementation of a CirculinearRing2D.
 * @author dlegland
 *
 */
public class GenericCirculinearRing2D 
extends CirculinearRing2D {

    // ===================================================================
    // constructors

    public GenericCirculinearRing2D() {
        super();
    }

    public GenericCirculinearRing2D(int size) {
        super(size);
    }

    public GenericCirculinearRing2D(CirculinearElement2D[] curves) {
        super(curves);
    }

    public GenericCirculinearRing2D(CirculinearElement2D[] curves, 
    		boolean closed) {
        super(curves, closed);
    }

    public GenericCirculinearRing2D(
    		Collection<? extends CirculinearElement2D> curves) {
        super(curves);
    }

    public GenericCirculinearRing2D(
    		Collection<? extends CirculinearElement2D> curves, 
    		boolean closed) {
        super(curves, closed);
    }

	@Override
	public GenericCirculinearRing2D transform(CircleInversion2D inv) {
    	// Allocate array for result
		GenericCirculinearRing2D result =
			new GenericCirculinearRing2D(curves.size());
        
        // add each transformed element
        for (CirculinearElement2D element : curves)
            result.addCurve(element.transform(inv));
        return result;
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.domain.Boundary2D#fill(java.awt.Graphics2D)
	 */
	@Override
	public void fill(Graphics2D g2) {
		g2.fill(this.getGeneralPath());
	}

	/* (non-Javadoc)
	 * @see math.geom2d.domain.Boundary2D#getBoundaryCurves()
	 */
	@Override
	public Collection<CirculinearContour2D> getBoundaryCurves() {
        ArrayList<CirculinearContour2D> list = 
            new ArrayList<CirculinearContour2D>(1);
        list.add(this);
        return list;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.domain.Boundary2D#getDomain()
	 */
	@Override
	public CirculinearDomain2D getDomain() {
		return new GenericCirculinearDomain2D(this);
	}

	@Override
	public GenericCirculinearRing2D getReverseCurve(){
    	int n = curves.size();
        // create array of reversed curves
    	CirculinearElement2D[] curves2 = new CirculinearElement2D[n];
        
        // reverse each curve
        for (int i = 0; i<n; i++)
            curves2[i] = curves.get(n-1-i).getReverseCurve();
        
        // create the reversed final curve
        return new GenericCirculinearRing2D(curves2);
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
