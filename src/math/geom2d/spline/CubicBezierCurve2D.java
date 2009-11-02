/* File CubicBezierCurve2D.java 
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

import java.awt.geom.CubicCurve2D;
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.AbstractSmoothCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.Curve2DUtils;
import math.geom2d.curve.CurveArray2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.domain.ContinuousOrientedCurve2D;
import math.geom2d.line.LinearShape2D;

/**
 * A cubic bezier curve, defined by 4 points.
 * 
 * From javaGeom 0.8.0, this shape does not extends.
 * java.awt.geom.CubicCurve2D.Double anymore
 * 
 * @author Legland
 */
public class CubicBezierCurve2D extends AbstractSmoothCurve2D
implements SmoothCurve2D, ContinuousOrientedCurve2D, Cloneable {

	protected double x1, y1;
	protected double ctrlx1, ctrly1;
	protected double ctrlx2, ctrly2;
	protected double x2, y2;
	
	
    // ===================================================================
    // constructors

    public CubicBezierCurve2D() {
        this(0, 0, 0, 0, 0, 0, 0, 0);
    }

    /**
     * Build a new Bezier curve from its array of coefficients. The array must
     * have size 2*4.
     * 
     * @param coefs the coefficients of the CubicBezierCurve2D.
     */
    public CubicBezierCurve2D(double[][] coefs) {
        this(coefs[0][0], coefs[1][0], coefs[0][0]+coefs[0][1]/3.0, coefs[1][0]
                +coefs[1][1]/3.0,
                coefs[0][0]+2*coefs[0][1]/3.0+coefs[0][2]/3.0, coefs[1][0]+2
                        *coefs[1][1]/3.0+coefs[1][2]/3.0, coefs[0][0]
                        +coefs[0][1]+coefs[0][2]+coefs[0][3], coefs[1][0]
                        +coefs[1][1]+coefs[1][2]+coefs[1][3]);
    }

    /**
     * Build a new Bezier curve of degree 3 by specifying position of extreme
     * points and position of 2 control points. The resulting curve is totally
     * contained in the convex polygon formed by the 4 control points.
     * 
     * @param p1 first point
     * @param ctrl1 first control point
     * @param ctrl2 second control point
     * @param p2 last point
     */
    public CubicBezierCurve2D(java.awt.geom.Point2D p1, java.awt.geom.Point2D ctrl1,
            java.awt.geom.Point2D ctrl2, java.awt.geom.Point2D p2) {
        this(p1.getX(), p1.getY(), ctrl1.getX(), ctrl1.getY(), ctrl2.getX(),
                ctrl2.getY(), p2.getX(), p2.getY());
    }

    /**
     * Build a new Bezier curve of degree 3 by specifying position and tangent
     * of first and last points.
     * 
     * @param p1 first point
     * @param v1 first tangent vector
     * @param p2 position of last point
     * @param v2 last tangent vector
     */
    public CubicBezierCurve2D(
    		java.awt.geom.Point2D p1, Vector2D v1,
            java.awt.geom.Point2D p2, Vector2D v2) {
        this(	p1.getX(), p1.getY(), 
        		p1.getX()+v1.getX()/3, p1.getY()+v1.getY()/3,
        		p2.getX()-v2.getX()/3, p2.getY()-v2.getY()/3,
        		p2.getX(), p2.getY());
    }

    /**
     * Build a new Bezier curve of degree 3 by specifying position of extreme
     * points and position of 2 control points. The resulting curve is totally
     * containe in the convex polygon formed by the 4 control points.
     */
    public CubicBezierCurve2D(double x1, double y1, double xctrl1, double yctrl1,
            double xctrl2, double yctrl2, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.ctrlx1 = xctrl1;
        this.ctrly1 = yctrl1;
        this.ctrlx2 = xctrl2;
        this.ctrly2 = yctrl2;
        this.x2 = x2;
        this.y2 = y2;
       }
    

    // ===================================================================
    // static methods

    public final static CubicBezierCurve2D create (
    		Point2D p1, Point2D c1, Point2D c2, Point2D p2) {
    	return new CubicBezierCurve2D(p1, c1, c2, p2);
    }
    
    public final static CubicBezierCurve2D create (
    		Point2D p1, Vector2D v1, Point2D p2, Vector2D v2) {
    	return new CubicBezierCurve2D(p1, v1, p2, v2);
    }
    
     
    // ===================================================================
    // methods specific to CubicBezierCurve2D

    public Point2D getControl1() {
        return new Point2D(ctrlx1, ctrly1);
    }

    public Point2D getControl2() {
        return new Point2D(ctrlx2, ctrly2);
    }

    public Point2D getP1() {
    	return this.getFirstPoint();
    }
    
    public Point2D getP2() {
    	return this.getLastPoint();
    }
    
    public Point2D getCtrlP1() {
    	return this.getControl1();
    }
    
    public Point2D getCtrlP2() {
    	return this.getControl2();
    }
    
   /**
     * Returns the matrix of parametric representation of the line. Result has
     * the form :
     * <p>
     * <code>[ x0  dx dx2 dx3] </code>
     * <p>
     * <code>[ y0  dy dy2 dy3] </code>
     * <p>
     * Coefficients are from the parametric equation : x(t) = x0 + dx*t +
     * dx2*t^2 + dx3*t^3 y(t) = y0 + dy*t + dy2*t^2 + dy3*t^3
     */
    public double[][] getParametric() {
        double[][] tab = new double[2][4];
        tab[0][0] = x1;
        tab[0][1] = 3*ctrlx1-3*x1;
        tab[0][2] = 3*x1-6*ctrlx1+3*ctrlx2;
        tab[0][3] = x2-3*ctrlx2+3*ctrlx1-x1;

        tab[1][0] = y1;
        tab[1][1] = 3*ctrly1-3*y1;
        tab[1][2] = 3*y1-6*ctrly1+3*ctrly2;
        tab[1][3] = y2-3*ctrly2+3*ctrly1-y1;
        return tab;
    }

    // /**
    // * Return the parallel curve located at a distance d from this Bezier
    // * curve.
    // */
    // public CubicBezierCurve2D getParallel(double d){
    // double[][] tab = this.getParametric();
    // double[][] tab2 = new double[2][4];
    //		
    // d = d/Math.hypot(tab[0][1], tab[1][1]);
    //		
    // tab2[0][0] = tab[0][0] + d*tab[1][1];
    // tab2[1][0] = tab[1][0] - d*tab[0][1];
    //		
    // tab2[0][1] = tab[0][1] + 2*d*tab[1][2];
    // tab2[1][1] = tab[1][1] - 2*d*tab[0][2];
    //		
    // tab2[0][2] = tab[0][2] + 3*d*tab[1][3];
    // tab2[1][2] = tab[1][2] - 3*d*tab[0][3];
    //
    // tab2[0][3] = tab[0][3];
    // tab2[1][3] = tab[1][3];
    //
    // return new CubicBezierCurve2D(tab2);
    // }

    // ===================================================================
    // methods from OrientedCurve2D interface

    /**
     * Use winding angle of approximated polyline
     * 
     * @see math.geom2d.domain.OrientedCurve2D#getWindingAngle(java.awt.geom.Point2D)
     */
    public double getWindingAngle(java.awt.geom.Point2D point) {
        return this.getAsPolyline(100).getWindingAngle(point);
    }

    /**
     * return true if the point is 'inside' the domain bounded by the curve.
     * Uses a polyline approximation.
     * 
     * @param pt a point in the plane
     * @return true if the point is on the left side of the curve.
     */
    public boolean isInside(java.awt.geom.Point2D pt) {
        return this.getAsPolyline(100).isInside(pt);
    }

    public double getSignedDistance(java.awt.geom.Point2D point) {
        if (isInside(point))
            return -getDistance(point.getX(), point.getY());
        else
            return getDistance(point.getX(), point.getY());
    }

    /**
     * @see math.geom2d.domain.OrientedCurve2D#getSignedDistance(java.awt.geom.Point2D)
     */
    public double getSignedDistance(double x, double y) {
        if (isInside(new Point2D(x, y)))
            return -getDistance(x, y);
        else
            return getDistance(x, y);
    }

    // ===================================================================
    // methods from SmoothCurve2D interface

    public Vector2D getTangent(double t) {
        double[][] c = getParametric();
        double dx = c[0][1]+(2*c[0][2]+3*c[0][3]*t)*t;
        double dy = c[1][1]+(2*c[1][2]+3*c[1][3]*t)*t;
        return new Vector2D(dx, dy);
    }

    /**
     * returns the curvature of the Curve.
     */
    public double getCurvature(double t) {
        double[][] c = getParametric();
        double xp = c[0][1]+(2*c[0][2]+3*c[0][3]*t)*t;
        double yp = c[1][1]+(2*c[1][2]+3*c[1][3]*t)*t;
        double xs = 2*c[0][2]+6*c[0][3]*t;
        double ys = 2*c[1][2]+6*c[1][3]*t;

        return (xp*ys-yp*xs)/Math.pow(Math.hypot(xp, yp), 3);
    }

    // ===================================================================
    // methods from ContinousCurve2D interface

    /**
     * The cubic curve is never closed.
     */
    public boolean isClosed() {
        return false;
    }

    // ===================================================================
    // methods from Curve2D interface

    /**
     * returns 0, as Bezier curve is parameterized between 0 and 1.
     */
    public double getT0() {
        return 0;
    }

    /**
     * Returns 1, as Bezier curve is parameterized between 0 and 1.
     */
    public double getT1() {
        return 1;
    }

    /**
     * Use approximation, by replacing Bezier curve with a polyline.
     * 
     * @see math.geom2d.curve.Curve2D#getIntersections(math.geom2d.line.LinearShape2D)
     */
    public Collection<Point2D> getIntersections(LinearShape2D line) {
        return this.getAsPolyline(100).getIntersections(line);
    }

    /**
     * @see math.geom2d.curve.Curve2D#getPoint(double)
     */
    public Point2D getPoint(double t) {
        t = Math.min(Math.max(t, 0), 1);
        double[][] c = getParametric();
        double x = c[0][0]+(c[0][1]+(c[0][2]+c[0][3]*t)*t)*t;
        double y = c[1][0]+(c[1][1]+(c[1][2]+c[1][3]*t)*t)*t;
        return new Point2D(x, y);
    }

    /**
     * Returns the first point of the curve.
     * 
     * @return the first point of the curve
     */
	@Override
    public Point2D getFirstPoint() {
        return new Point2D(this.x1, this.y1);
    }

    /**
     * Returns the last point of the curve.
     * 
     * @return the last point of the curve.
     */
	@Override
    public Point2D getLastPoint() {
        return new Point2D(this.x2, this.y2);
    }

    /**
     * Computes position by approximating cubic spline with a polyline.
     */
    public double getPosition(java.awt.geom.Point2D point) {
        int N = 100;
        return this.getAsPolyline(N).getPosition(point)/(N);
    }

    /**
     * Computes position by approximating cubic spline with a polyline.
     */
    public double project(java.awt.geom.Point2D point) {
        int N = 100;
        return this.getAsPolyline(N).project(point)/(N);
    }

    /**
     * Returns the Bezier curve given by control points taken in reverse
     * order.
     */
    public CubicBezierCurve2D getReverseCurve() {
        return new CubicBezierCurve2D(
                this.getLastPoint(), this.getControl1(),
                this.getControl2(), this.getFirstPoint());
    }

    /**
     * Computes portion of BezierCurve. If t1<t0, returns null.
     */
    public CubicBezierCurve2D getSubCurve(double t0, double t1) {
        t0 = Math.max(t0, 0);
        t1 = Math.min(t1, 1);
        if (t0>t1)
            return null;

        double dt = t1-t0;
        Vector2D v0 = getTangent(t0).times(dt);
        Vector2D v1 = getTangent(t1).times(dt);
        return new CubicBezierCurve2D(getPoint(t0), v0, getPoint(t1), v1);
    }

    // ===================================================================
    // methods from Shape2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#contains(double, double)
	 */
	public boolean contains(double x, double y) {
		return new CubicCurve2D.Double(
				x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2).contains(x, y);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#contains(java.awt.geom.Point2D)
	 */
	public boolean contains(java.awt.geom.Point2D p) {
		return this.contains(p.getX(), p.getY());
	}

	/**
     * @see math.geom2d.Shape2D#getDistance(java.awt.geom.Point2D)
     */
    public double getDistance(java.awt.geom.Point2D p) {
        return this.getDistance(p.getX(), p.getY());
    }

    /**
     * Compute approximated distance, computed on a polyline.
     * 
     * @see math.geom2d.Shape2D#getDistance(double, double)
     */
    public double getDistance(double x, double y) {
    	return this.getAsPolyline(100).getDistance(x, y);
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
     * Clip the Bezier curve by a box. Return a set of CubicBezierCurve2D.
     */
    public CurveSet2D<? extends CubicBezierCurve2D> clip(Box2D box) {
        // Clip the curve
        CurveSet2D<SmoothCurve2D> set = 
            Curve2DUtils.clipSmoothCurve(this, box);

        // Stores the result in appropriate structure
        CurveArray2D<CubicBezierCurve2D> result = 
        	new CurveArray2D<CubicBezierCurve2D>(set.getCurveNumber());

        // convert the result
        for (Curve2D curve : set.getCurves()) {
            if (curve instanceof CubicBezierCurve2D)
                result.addCurve((CubicBezierCurve2D) curve);
        }
        return result;
    }

    public Box2D getBoundingBox() {
    	Point2D p1 = this.getFirstPoint();
        Point2D p2 = this.getControl1();
        Point2D p3 = this.getControl2();
        Point2D p4 = this.getLastPoint();
        double xmin = Math.min(Math.min(p1.getX(), p2.getX()), 
        		Math.min(p3.getX(), p4.getX()));
        double xmax = Math.max(Math.max(p1.getX(), p2.getX()), 
        		Math.max(p3.getX(), p4.getX()));
        double ymin = Math.min(Math.min(p1.getY(), p2.getY()), 
        		Math.min(p3.getY(), p4.getY()));
        double ymax = Math.max(Math.max(p1.getY(), p2.getY()), 
        		Math.max(p3.getY(), p4.getY()));
        return new Box2D(xmin, xmax, ymin, ymax);
    }

    /**
     * Returns the Bezier Curve transformed by the given AffineTransform2D. This
     * is simply done by transforming control points of the curve.
     */
    public CubicBezierCurve2D transform(AffineTransform2D trans) {
        return new CubicBezierCurve2D(
                trans.transform(this.getFirstPoint()), 
                trans.transform(this.getControl1()),
                trans.transform(this.getControl2()),
                trans.transform(this.getLastPoint()));
    }

    public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path) {
        Point2D p2 = this.getControl1();
        Point2D p3 = this.getControl2();
        Point2D p4 = this.getLastPoint();
        path.curveTo(
        		p2.getX(), p2.getY(), 
        		p3.getX(), p3.getY(), 
        		p4.getX(), p4.getY());
        return path;
    }

    public java.awt.geom.GeneralPath getGeneralPath() {
        java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
        Point2D p1 = this.getFirstPoint();
        Point2D p2 = this.getControl1();
        Point2D p3 = this.getControl2();
        Point2D p4 = this.getLastPoint();
        path.moveTo(p1.getX(), p1.getY());
        path.curveTo(
        		p2.getX(), p2.getY(), 
        		p3.getX(), p3.getY(), 
        		p4.getX(), p4.getY());
        return path;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CubicBezierCurve2D))
            return false;
        
        // class cast
        CubicBezierCurve2D bezier = (CubicBezierCurve2D) obj;
        
        // compare each field
        if(Math.abs(this.x1-bezier.x1)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.y1-bezier.y1)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.ctrlx1-bezier.ctrlx1)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.ctrly1-bezier.ctrly1)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.ctrlx2-bezier.ctrlx2)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.ctrly2-bezier.ctrly2)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.x2-bezier.x2)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.y2-bezier.y2)>Shape2D.ACCURACY) return false;
        
        return true;
    }
    
    @Override
    public CubicBezierCurve2D clone() {
        return new CubicBezierCurve2D(
        		x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
    }
}
