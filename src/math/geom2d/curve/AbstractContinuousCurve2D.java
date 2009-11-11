/**
 * File: 	AbstractContinuousCurve2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 21 mai 09
 */
package math.geom2d.curve;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Point2D;
import math.geom2d.UnboundedShapeException;
import math.geom2d.polygon.LinearRing2D;
import math.geom2d.polygon.Polyline2D;


/**
 * Provides a base implementation for smooth curves.
 * @author dlegland
 */
public abstract class AbstractContinuousCurve2D 
implements ContinuousCurve2D, Cloneable {

	protected static <T extends ContinuousCurve2D> Collection<T> wrapCurve(T curve) {
		ArrayList<T> list = new ArrayList<T> (1);
		list.add(curve);
		return list;
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#getAsPolyline(int)
	 */
	public Polyline2D getAsPolyline(int n) {
		// Check that the curve is bounded
        if (!this.isBounded())
            throw new UnboundedShapeException(this);

        // compute start and increment values
        double t0 = this.getT0();
        double dt = (this.getT1()-t0)/n;

		if(this.isClosed()) {
			// compute position of points, without the last one, 
			// which is included by default with linear rings
	        Point2D[] points = new Point2D[n];
			for(int i=0; i<n;i++)
				points[i] = this.getPoint(t0 + i*dt);

			return new LinearRing2D(points);
		} else {
			// allocate array of points, and compute each value.
			// Computes also value for last point.
	        Point2D[] points = new Point2D[n+1];
			for(int i=0; i<n+1;i++)
				points[i] = this.getPoint(t0 + i*dt);

			return new Polyline2D(points);
		}
	}

	/* (non-Javadoc)
	 * @see math.geom2d.curve.Curve2D#getContinuousCurves()
	 */
	public Collection<? extends ContinuousCurve2D> getContinuousCurves() {
		return wrapCurve(this);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.curve.Curve2D#getFirstPoint()
	 */
	public Point2D getFirstPoint() {
		double t0 = this.getT0();
		if(Double.isInfinite(t0))
			throw new UnboundedShapeException(this);
		return this.getPoint(t0);
	}


	/* (non-Javadoc)
	 * @see math.geom2d.curve.Curve2D#getLastPoint()
	 */
	public Point2D getLastPoint() {
		double t1 = this.getT1();
		if(Double.isInfinite(t1))
			throw new UnboundedShapeException(this);
		return this.getPoint(t1);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.curve.Curve2D#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g2) {
		g2.draw(this.getAsAWTShape());
	}

	/* (non-Javadoc)
	 * @see math.geom2d.curve.Curve2D#getAsAWTShape()
	 */
	public Shape getAsAWTShape() {
		// Check that the curve is bounded
        if (!this.isBounded())
            throw new UnboundedShapeException(this);

        java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
        
		Point2D point = this.getFirstPoint();
        path.moveTo((float) point.getX(), (float)  point.getY());
        path = this.appendPath(path);
        return path;
	}
	
	@Override
    public abstract ContinuousCurve2D clone();
}
