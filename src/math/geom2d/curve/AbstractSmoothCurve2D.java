/**
 * File: 	AbstractSmoothCurve2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 21 mai 09
 */
package math.geom2d.curve;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;


/**
 * Provides a base implementation for smooth curves.
 * @author dlegland
 */
public abstract class AbstractSmoothCurve2D extends AbstractContinuousCurve2D
implements SmoothCurve2D, Cloneable {


	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#getLeftTangent(double)
	 */
    public Vector2D getLeftTangent(double t){
    	return this.getTangent(t);
    }
    
	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#getRightTangent(double)
	 */
    public Vector2D getRightTangent(double t){
    	return this.getTangent(t);
    }
    
	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#getSmoothPieces()
	 */
	public Collection<? extends SmoothCurve2D> getSmoothPieces() {
		return wrapCurve(this);
	}

	/** 
	 * Returns an empty set of Point2D, as a smooth curve does not have
	 * singular points by definition. 
	 * @see math.geom2d.curve.Curve2D#getSingularPoints()
	 */
	public Collection<Point2D> getSingularPoints() {
		return new ArrayList<Point2D>(0);
	}

	/** 
	 * Returns a set of Point2D, containing the extremities of the curve 
	 * if they are not infinite. 
	 * @see math.geom2d.curve.Curve2D#getVertices()
	 */
	public Collection<Point2D> getVertices() {
		ArrayList<Point2D> array = new ArrayList<Point2D>(2);
		if (!Double.isInfinite(this.getT0()))
			array.add(this.getFirstPoint());
		if (!Double.isInfinite(this.getT1()))
			array.add(this.getLastPoint());
		return array;
	}

	/**
	 * Returns always false, as a smooth curve does not have singular points
	 * by definition.
	 * @see math.geom2d.curve.Curve2D#isSingular(double)
	 */
	public boolean isSingular(double pos) {
		return false;
	}
	
    @Override
	public abstract SmoothCurve2D clone();
}
