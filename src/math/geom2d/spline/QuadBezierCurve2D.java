/* File QuadBezierCurve2D.java 
 *
 * Project : geometry
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package math.geom2d.spline;

import java.awt.geom.QuadCurve2D;
import java.util.Collection;

import math.geom2d.*;
import math.geom2d.curve.*;
import math.geom2d.domain.ContinuousOrientedCurve2D;
import math.geom2d.line.LinearShape2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.polygon.Polyline2D;

/**
 * A quadratic Bezier curve, defined by 3 control points.
 * The curve starts at the first control point and finished at the third
 * control point. The second point is used to defined the curvature of the
 * curve. 
 * 
 * From javaGeom 0.8.0, this shape does not extends.
 * java.awt.geom.QuadCurve2D.Double anymore
 * 
 * @author Legland
 */
public class QuadBezierCurve2D extends AbstractSmoothCurve2D
implements SmoothCurve2D, ContinuousOrientedCurve2D, Cloneable {

    // ===================================================================
    // static methods
   
    /**
     * Static factory for creating a new Quadratic Bezier curve from 3 points.
	 *
	 * @deprecated since 0.11.1
	 */
	@Deprecated
    public static QuadBezierCurve2D create(Point2D p1, Point2D p2, Point2D p3) {
    	return new QuadBezierCurve2D(p1, p2, p3);
    }
    

    // ===================================================================
    // class variables
   
    /**
     * Coordinates of the first point of the curve
     */
	protected double x1, y1;
    
	/**
     * Coordinates of the control point of the curve
     */
	protected double ctrlx, ctrly;

	/**
     * Coordinates of the last point of the curve
     */
	protected double x2, y2;

    // ===================================================================
    // constructors

    /**
     * Creates an empty quadratic bezier curve.
     */
    public QuadBezierCurve2D() {
        this(0, 0, 0, 0, 0, 0);
    }

    /**
     * Build a new Bezier curve from its array of coefficients. The array must
     * have size 2*3.
     * 
     * @param coefs the coefficients of the QuadBezierCurve2D.
     */
    public QuadBezierCurve2D(double[][] coefs) {
		this(coefs[0][0], coefs[1][0], 
				coefs[0][0] + coefs[0][1] / 2.0,
				coefs[1][0] + coefs[1][1] / 2.0, 
				coefs[0][0] + coefs[0][1] + coefs[0][2], 
				coefs[1][0] + coefs[1][1] + coefs[1][2]);
    }

    /**
     * Build a new quadratic Bezier curve by specifying position of extreme
     * points and position of control point. The resulting curve is totally
     * contained in the convex polygon formed by the 3 control points.
     * 
     * @param p1 first point
     * @param ctrl control point
     * @param p2 last point
     */
    public QuadBezierCurve2D(Point2D p1, Point2D ctrl, Point2D p2) {
        this(p1.x(), p1.y(), ctrl.x(), ctrl.y(), p2.x(), p2.y());
    }

    public QuadBezierCurve2D(Point2D[] pts) {
		this(pts[0].x(), pts[0].y(), pts[1].x(), pts[1].y(), 
				pts[2].x(), pts[2].y());
    }

    /**
     * Build a new quadratic Bezier curve by specifying position of extreme
     * points and position of control point. The resulting curve is totally
     * contained in the convex polygon formed by the 3 control points.
     */
    public QuadBezierCurve2D(double x1, double y1, double xctrl, double yctrl,
            double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.ctrlx = xctrl;
        this.ctrly = yctrl;
        this.x2 = x2;
        this.y2 = y2;
    }

    // ===================================================================
    // methods specific to QuadBezierCurve2D

    public Point2D getControl() {
        return new Point2D(ctrlx, ctrly);
    }

    public Point2D getP1() {
    	return this.firstPoint();
    }
    
    public Point2D getP2() {
    	return this.lastPoint();
    }
    
    public Point2D getCtrl() {
    	return this.getControl();
    }
    
    /**
     * Returns the matrix of parametric representation of the line. Result is a
     * 2x3 array with coefficients:
     * <p>
     * <code>[ cx0  cx1 cx2] </code>
     * <p>
     * <code>[ cy0  cy1 cy2] </code>
     * <p>
     * Coefficients are from the parametric equation : <code>
     * x(t) = cx0 + cx1*t + cx2*t^2 
     * y(t) = cy0 + cy1*t + cy2*t^2
     * </code>
     */
    public double[][] getParametric() {
        double[][] tab = new double[2][3];
		tab[0][0] = x1;
		tab[0][1] = 2 * ctrlx - 2 * x1;
		tab[0][2] = x2 - 2 * ctrlx + x1;

		tab[1][0] = y1;
		tab[1][1] = 2 * ctrly - 2 * y1;
		tab[1][2] = y2 - 2 * ctrly + y1;
        return tab;
    }

    // ===================================================================
    // methods from OrientedCurve2D interface

    /**
     * Use winding angle of approximated polyline
     * 
     * @see math.geom2d.domain.OrientedCurve2D#windingAngle(Point2D)
     */
    public double windingAngle(Point2D point) {
        return this.asPolyline(100).windingAngle(point);
    }

    /**
     * Returns true if the point is 'inside' the domain bounded by the curve.
     * Uses a polyline approximation.
     * 
     * @param pt a point in the plane
     * @return true if the point is on the left side of the curve.
     */
    public boolean isInside(Point2D pt) {
        return this.asPolyline(100).isInside(pt);
    }

    public double signedDistance(Point2D point) {
        if (isInside(point))
            return -distance(point.x(), point.y());
        else
            return distance(point.x(), point.y());
    }

    /**
     * @see math.geom2d.domain.OrientedCurve2D#signedDistance(Point2D)
     */
    public double signedDistance(double x, double y) {
        if (isInside(new Point2D(x, y)))
            return -distance(x, y);
        else
            return distance(x, y);
    }

    // ===================================================================
    // methods from SmoothCurve2D interface

    public Vector2D tangent(double t) {
		double[][] c = getParametric();
		double dx = c[0][1] + 2 * c[0][2] * t;
		double dy = c[1][1] + 2 * c[1][2] * t;
		return new Vector2D(dx, dy);
	}

    /**
     * Returns the curvature of the Curve.
     */
    public double curvature(double t) {
        double[][] c = getParametric();
		double xp = c[0][1] + 2 * c[0][2] * t;
		double yp = c[1][1] + 2 * c[1][2] * t;
		double xs = 2 * c[0][2];
		double ys = 2 * c[1][2];

		return (xp * ys - yp * xs) / Math.pow(Math.hypot(xp, yp), 3);
    }

    // ===================================================================
    // methods from ContinousCurve2D interface

    /**
     * Returns false, as a quadratic curve is never closed.
     */
    public boolean isClosed() {
        return false;
    }

	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#asPolyline(int)
	 */
	public Polyline2D asPolyline(int n) {

        // compute increment value
        double dt = 1.0 / n;

        // allocate array of points, and compute each value.
        // Computes also value for last point.
        Point2D[] points = new Point2D[n + 1];
        for (int i = 0; i < n + 1; i++)
        	points[i] = this.point(i * dt);

        return new Polyline2D(points);
	}

    // ===================================================================
    // methods from Curve2D interface

    /**
     * Returns 0, as Bezier curve is parameterized between 0 and 1.
     */
    public double t0() {
        return 0;
    }

    /**
     * @deprecated replaced by t0() (since 0.11.1).
     */
    @Deprecated
    public double getT0() {
    	return t0();
    }
    
    /**
     * Returns 1, as Bezier curve is parametrized between 0 and 1.
     */
    public double t1() {
        return 1;
    }

    /**
     * @deprecated replaced by t1() (since 0.11.1).
     */
    @Deprecated
    public double getT1() {
    	return t1();
    }
    
    /**
     * Use approximation, by replacing Bezier curve with a polyline.
     * 
     * @see math.geom2d.curve.Curve2D#intersections(math.geom2d.line.LinearShape2D)
     */
    public Collection<Point2D> intersections(LinearShape2D line) {
        return this.asPolyline(100).intersections(line);
    }

    /**
     * @see math.geom2d.curve.Curve2D#point(double)
     */
    public Point2D point(double t) {
        t = Math.min(Math.max(t, 0), 1);
		double[][] c = getParametric();
		double x = c[0][0] + (c[0][1] + c[0][2] * t) * t;
		double y = c[1][0] + (c[1][1] + c[1][2] * t) * t;
		return new Point2D(x, y);
    }

    /**
     * Returns the first point of the curve, that corresponds to the first control point.
     * 
     * @return the first point of the curve
     */
	@Override
    public Point2D firstPoint() {
        return new Point2D(this.x1, this.y1);
    }

    /**
     * Returns the last point of the curve, that corresponds to the third control point.
     * 
     * @return the last point of the curve.
     */
	@Override
    public Point2D lastPoint() {
        return new Point2D(this.x2, this.y2);
    }

    /**
     * Computes position by approximating cubic spline with a polyline.
     */
    public double position(Point2D point) {
		int N = 100;
		return this.asPolyline(N).position(point) / (N);
   }

    /**
     * Computes position by approximating cubic spline with a polyline.
     */
    public double project(Point2D point) {
        int N = 100;
        return this.asPolyline(N).project(point)/(N);
    }

    /**
     * Returns the bezier curve given by control points taken in reverse order.
     */
    public QuadBezierCurve2D reverse() {
        return new QuadBezierCurve2D(
        		this.lastPoint(), this.getControl(), this.firstPoint());
    }

    /**
     * Computes portion of BezierCurve. If t1<t0, returns null.
     */
    public QuadBezierCurve2D subCurve(double t0, double t1) {
        t0 = Math.max(t0, 0);
        t1 = Math.min(t1, 1);
		if (t0 > t1)
            return null;

        // Extreme points
        Point2D p0 = point(t0);
        Point2D p1 = point(t1);

        // tangent vectors at extreme points
        Vector2D v0 = tangent(t0);
        Vector2D v1 = tangent(t1);

        // compute position of control point as intersection of tangent lines
        StraightLine2D tan0 = new StraightLine2D(p0, v0);
        StraightLine2D tan1 = new StraightLine2D(p1, v1);
        Point2D control = tan0.intersection(tan1);

        // build the new quad curve
        return new QuadBezierCurve2D(p0, control, p1);
    }

    // ===================================================================
    // methods from Shape2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#contains(double, double)
	 */
	public boolean contains(double x, double y) {
		return new QuadCurve2D.Double(
				x1, y1, ctrlx, ctrly, x2, y2).contains(x, y);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#contains(Point2D)
	 */
	public boolean contains(Point2D p) {
		return this.contains(p.x(), p.y());
	}

	/**
     * @see math.geom2d.Shape2D#distance(Point2D)
     */
    public double distance(Point2D p) {
        return this.distance(p.x(), p.y());
    }

    /**
     * Computes approximated distance, computed on a polyline.
     * 
     * @see math.geom2d.Shape2D#distance(double, double)
     */
    public double distance(double x, double y) {
        return this.asPolyline(100).distance(x, y);
    }

    /**
     * Returns true, a cubic Bezier Curve is always bounded.
     */
    public boolean isBounded() {
        return true;
    }

    public boolean isEmpty() {
        return false;
    }

    /**
     * Clip the curve by a box. The result is an instance of
     * CurveSet2D, which contains only instances of QuadBezierCurve2D. 
     * If the curve is not clipped, the result is an instance of CurveSet2D
     * which contains 0 curves.
     */
    public CurveSet2D<? extends QuadBezierCurve2D> clip(Box2D box) {
        // Clip the curve
        CurveSet2D<SmoothCurve2D> set = Curves2D.clipSmoothCurve(this, box);

        // Stores the result in appropriate structure
        CurveArray2D<QuadBezierCurve2D> result = 
        	new CurveArray2D<QuadBezierCurve2D>(set.size());

        // convert the result
        for (Curve2D curve : set.curves()) {
            if (curve instanceof QuadBezierCurve2D)
                result.add((QuadBezierCurve2D) curve);
        }
        return result;
    }

    /**
     * Returns the approximate bounding box of this curve. Actually, computes
     * the bounding box of the set of control points.
     */
    public Box2D boundingBox() {
    	Point2D p1 = this.firstPoint();
        Point2D p2 = this.getControl();
        Point2D p3 = this.lastPoint();
        double xmin = Math.min(Math.min(p1.x(), p2.x()), p3.x());
        double xmax = Math.max(Math.max(p1.x(), p2.x()), p3.x());
        double ymin = Math.min(Math.min(p1.y(), p2.y()), p3.y());
        double ymax = Math.max(Math.max(p1.y(), p2.y()), p3.y());
        return new Box2D(xmin, xmax, ymin, ymax);
    }

    /**
     * Returns the Bezier Curve transformed by the given AffineTransform2D. This
     * is simply done by transforming control points of the curve.
     */
    public QuadBezierCurve2D transform(AffineTransform2D trans) {
        return new QuadBezierCurve2D(
                trans.transform(this.firstPoint()), 
                trans.transform(this.getControl()),
                trans.transform(this.lastPoint()));
    }

    public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path) {
        Point2D p2 = this.getControl();
        Point2D p3 = this.lastPoint();
        path.quadTo(p2.x(), p2.y(), p3.x(), p3.y());
        return path;
    }

    public java.awt.geom.GeneralPath getGeneralPath() {
        java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
        Point2D p1 = this.firstPoint();
        Point2D p2 = this.getControl();
        Point2D p3 = this.lastPoint();
        path.moveTo(p1.x(), p1.y());
        path.quadTo(p2.x(), p2.y(), p3.x(), p3.y());
        return path;
    }


	// ===================================================================
	// methods implementing the GeometricObject2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
	 */
    public boolean almostEquals(GeometricObject2D obj, double eps) {
    	if (this==obj)
    		return true;
    	
        if(!(obj instanceof QuadBezierCurve2D))
            return false;
        
        // Class cast
        QuadBezierCurve2D bezier = (QuadBezierCurve2D) obj;
        
        // Compare each field
        if(Math.abs(this.x1-bezier.x1)>eps) return false;
        if(Math.abs(this.y1-bezier.y1)>eps) return false;
        if(Math.abs(this.ctrlx-bezier.ctrlx)>eps) return false;
        if(Math.abs(this.ctrly-bezier.ctrly)>eps) return false;
        if(Math.abs(this.x2-bezier.x2)>eps) return false;
        if(Math.abs(this.y2-bezier.y2)>eps) return false;
        
        return true;
    }

	// ===================================================================
	// methods overriding the class Object

    @Override
    public boolean equals(Object obj) {
    	if (this==obj)
    		return true;
    	
        if(!(obj instanceof QuadBezierCurve2D))
            return false;
        
        // Class cast
        QuadBezierCurve2D bezier = (QuadBezierCurve2D) obj;
        
        // Compare each field
        if(Math.abs(this.x1-bezier.x1)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.y1-bezier.y1)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.ctrlx-bezier.ctrlx)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.ctrly-bezier.ctrly)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.x2-bezier.x2)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.y2-bezier.y2)>Shape2D.ACCURACY) return false;
        
        return true;
    }
    
	/**
	 * @deprecated not necessary to clone immutable objects (0.11.2)
	 */
	@Deprecated
	@Override
    public QuadBezierCurve2D clone() {
        return new QuadBezierCurve2D(x1, y1, ctrlx, ctrly, x2, y2);
    }
}
