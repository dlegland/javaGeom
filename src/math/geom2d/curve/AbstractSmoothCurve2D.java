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
import math.geom2d.UnboundedShapeException;
import math.geom2d.polygon.Polyline2D;
import math.geom2d.polygon.Ring2D;


/**
 * Provides a base implementation for smooth curves.
 * @author dlegland
 */
public abstract class AbstractSmoothCurve2D implements SmoothCurve2D, Cloneable {


	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#getAsPolyline(int)
	 */
	public Polyline2D getAsPolyline(int n) {
		// Check that the curve is bounded
        if (!this.isBounded())
            throw new UnboundedShapeException();

        // compute start and increment values
        double t0 = this.getT0();
        double dt = (this.getT1()-t0)/n;

		// allocate array of points, and compute each value
        Point2D[] points = new Point2D[n+1];
		for(int i=0; i<n+1;i++)
			points[i] = this.getPoint(t0 + i*dt);

		if(this.isClosed()) {
			return new Ring2D(points);
		} else {
			return new Polyline2D(points);
		}
	}

	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#getSmoothPieces()
	 */
	public Collection<? extends SmoothCurve2D> getSmoothPieces() {
		ArrayList<SmoothCurve2D> array = new ArrayList<SmoothCurve2D>(1);
		array.add(this);
		return array;
	}

//	/* (non-Javadoc)
//	 * @see math.geom2d.curve.Curve2D#draw(java.awt.Graphics2D)
//	 */
//	public void draw(Graphics2D g2) {
//		// TODO Auto-generated method stub
//
//	}
//
//	/* (non-Javadoc)
//	 * @see math.geom2d.curve.Curve2D#getAsAWTShape()
//	 */
//	public Shape getAsAWTShape() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	/* (non-Javadoc)
	 * @see math.geom2d.curve.Curve2D#getContinuousCurves()
	 */
	public Collection<? extends ContinuousCurve2D> getContinuousCurves() {
		ArrayList<ContinuousCurve2D> array =
			new ArrayList<ContinuousCurve2D>(1);
		array.add(this);
		return array;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.curve.Curve2D#getFirstPoint()
	 */
	public Point2D getFirstPoint() {
		double t0 = this.getT0();
		if(Double.isInfinite(t0))
			throw new UnboundedShapeException();
		return this.getPoint(t0);
	}


	/* (non-Javadoc)
	 * @see math.geom2d.curve.Curve2D#getLastPoint()
	 */
	public Point2D getLastPoint() {
		double t1 = this.getT1();
		if(Double.isInfinite(t1))
			throw new UnboundedShapeException();
		return this.getPoint(t1);
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
	 * Returns always false, as a smooth curve does not have singular points
	 * by definition.
	 * @see math.geom2d.curve.Curve2D#isSingular(double)
	 */
	public boolean isSingular(double pos) {
		return false;
	}
	
    public abstract SmoothCurve2D clone();
}
