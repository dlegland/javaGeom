/**
 * File: 	CirculinearBoundarySet2D.java
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
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.Curve2DUtils;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.domain.BoundarySet2D;
import math.geom2d.domain.ContinuousOrientedCurve2D;
import math.geom2d.transform.CircleInversion2D;


/**
 * A circulinear boundary which is composed of several CirculinearRing2D.
 * @author dlegland
 *
 */
public class CirculinearBoundarySet2D<T extends CirculinearContour2D> 
extends BoundarySet2D<T> implements	CirculinearBoundary2D {

    // ===================================================================
    // static constructors

	public final static CirculinearBoundarySet2D<CirculinearContour2D>
	create(Collection<? extends CirculinearContour2D> curves) {
		return new CirculinearBoundarySet2D<CirculinearContour2D>(curves);
	}
	

    // ===================================================================
    // constructors

	/**
     * Empty constructor. Initializes an empty array of curves.
     */
    public CirculinearBoundarySet2D() {
    	this.curves = new ArrayList<T>();
    }

    /**
     * Empty constructor. Initializes an empty array of curves, 
     * with a given size for allocating memory.
     */
    public CirculinearBoundarySet2D(int n) {
    	this.curves = new ArrayList<T>(n);
    }

    /**
     * Constructor from an array of curves.
     * 
     * @param curves the array of curves in the set
     */
    public CirculinearBoundarySet2D(T[] curves) {
    	this.curves = new ArrayList<T>(curves.length);
        for (T element : curves)
            this.addCurve(element);
    }

    /**
     * Constructor from a collection of curves. The curves are added to the
     * inner collection of curves.
     * 
     * @param curves the collection of curves to add to the set
     */
    public CirculinearBoundarySet2D(Collection<? extends T> curves) {
    	this.curves = new ArrayList<T>(curves.size());
        this.curves.addAll(curves);
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
	 * @see math.geom2d.circulinear.ContinuousCirculinearCurve2D#getParallel(double)
	 */
	public CirculinearCurve2D getParallel(double d) {
		return CirculinearCurve2DUtils.createParallel(this, d);
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#transform(math.geom2d.transform.CircleInversion2D)
	 */
	public CirculinearBoundarySet2D<? extends CirculinearContour2D> 
	transform(CircleInversion2D inv) {
    	// Allocate array for result
		CirculinearBoundarySet2D<CirculinearContour2D> result = 
			new CirculinearBoundarySet2D<CirculinearContour2D>(
					curves.size());
        
        // add each transformed curve
        for (CirculinearContour2D curve : curves)
            result.addCurve(curve.transform(inv));
        return result;
	}
	
    // ===================================================================
    // methods implementing the Curve2D interface

    @Override
    public Collection<? extends CirculinearContour2D> getContinuousCurves() {
    	// create array for storing result
    	ArrayList<T> result = new ArrayList<T>();

    	// return the set of curves
    	result.addAll(curves);
    	return result;
    }

	public CirculinearCurveSet2D<? extends ContinuousCirculinearCurve2D> clip(Box2D box) {
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
    
	public CirculinearBoundarySet2D<? extends CirculinearContour2D>
	getReverseCurve(){
    	int n = curves.size();
        // create array of reversed curves
    	CirculinearContour2D[] curves2 = new CirculinearContour2D[n];
        
        // reverse each curve
        for (int i = 0; i<n; i++)
            curves2[i] = curves.get(n-1-i).getReverseCurve();
        
        // create the reversed final curve
        return new CirculinearBoundarySet2D<CirculinearContour2D>(curves2);
	}
	
    @Override
    public CirculinearCurveSet2D<? extends ContinuousCirculinearCurve2D> getSubCurve(
            double t0, double t1) {
        // get the subcurve
    	CurveSet2D<? extends ContinuousOrientedCurve2D> curveSet =
    		super.getSubCurve(t0, t1);

        // create subcurve array
        ArrayList<ContinuousCirculinearCurve2D> curves = 
        	new ArrayList<ContinuousCirculinearCurve2D>(
        			curveSet.getCurveNumber());
        
        // class cast each curve
        for (Curve2D curve : curveSet.getCurves())
            curves.add((ContinuousCirculinearCurve2D) curve);

        // Create CurveSet for the result
        return new CirculinearCurveSet2D<ContinuousCirculinearCurve2D>(curves);
    }
}
