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
	 * @see math.geom2d.curve.ContinuousCurve2D#leftTangent(double)
	 */
    public Vector2D leftTangent(double t){
    	return this.tangent(t);
    }
    
	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#rightTangent(double)
	 */
    public Vector2D rightTangent(double t){
    	return this.tangent(t);
    }
    
	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#normal(double)
	 */
    public Vector2D normal(double t){
    	return this.tangent(t).rotate(-Math.PI / 2);
    }
    
	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#smoothPieces()
	 */
	public Collection<? extends SmoothCurve2D> smoothPieces() {
		return wrapCurve(this);
	}

	/** 
	 * Returns an empty set of Point2D, as a smooth curve does not have
	 * singular points by definition. 
	 * @see math.geom2d.curve.Curve2D#singularPoints()
	 */
	public Collection<Point2D> singularPoints() {
		return new ArrayList<Point2D>(0);
	}

	/** 
	 * Returns a set of Point2D, containing the extremities of the curve 
	 * if they are not infinite. 
	 * @see math.geom2d.curve.Curve2D#vertices()
	 */
	public Collection<Point2D> vertices() {
		ArrayList<Point2D> array = new ArrayList<Point2D>(2);
		if (!Double.isInfinite(this.t0()))
			array.add(this.firstPoint());
		if (!Double.isInfinite(this.t1()))
			array.add(this.lastPoint());
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
	
	/**
	 * @deprecated use copy constructor instead (0.11.2)
	 */
	@Deprecated
    @Override
	public abstract SmoothCurve2D clone();
}
