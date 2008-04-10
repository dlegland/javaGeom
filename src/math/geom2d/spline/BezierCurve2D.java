/* File BezierCurve2D.java 
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

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.*;
import math.geom2d.line.Polyline2D;
import math.geom2d.line.StraightObject2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * An extension of the Bezier curve provided in java.awt.geom, with support for 
 * SmoothCurve2D and OrientedCurve2D.
 * @author Legland
 *
 */
public class BezierCurve2D 
	extends java.awt.geom.CubicCurve2D.Double
	implements SmoothCurve2D, ContinuousOrientedCurve2D {

	private static final long serialVersionUID = 1L;

	// ===================================================================
	// constructors 

	public BezierCurve2D(){
		this(0, 0, 0, 0, 0, 0, 0, 0);
	}
	
	/**
	 *  Build a new Bezier curve of degree 3 by specifying position of extreme
	 *  points and position of 2 control points. The resulting curve is
	 *  totally containe in the convex polygon formed by the 4 control points. 
	 * @param p1 first point
	 * @param ctrl1 first control point
	 * @param ctrl2 second control point
	 * @param p2 last point
	 */
	public BezierCurve2D(
			java.awt.geom.Point2D p1, java.awt.geom.Point2D ctrl1,
			java.awt.geom.Point2D ctrl2, java.awt.geom.Point2D p2){
		this(p1.getX(), p1.getY(), ctrl1.getX(), ctrl1.getY(), 
			ctrl2.getX(), ctrl2.getY(), p2.getX(), p2.getY());
	}

	/**
	 * Build a new Bezier curve of degree 3 by specifying position and tangent
	 * of first and last points.
	 * @param p1 first point
	 * @param v1 first tangent vector
	 * @param p2 position of last point
	 * @param v2 last tangent vector
	 */
	public BezierCurve2D(
			java.awt.geom.Point2D p1, Vector2D v1, 
			java.awt.geom.Point2D p2, Vector2D v2){
		this(p1.getX(), p1.getY(), p1.getX()+v1.getDx()/3, p1.getY()+v1.getDy()/3, 
			p2.getX()-v2.getDx()/3, p2.getY()-v2.getDy()/3, p2.getX(), p2.getY());
	}

	/**
	 *  Build a new Bezier curve of degree 3 by specifying position of extreme
	 *  points and position of 2 control points. The resulting curve is
	 *  totally containe in the convex polygon formed by the 4 control points. 
	 */
	public BezierCurve2D(double x1, double y1, double xctrl1, double yctrl1, 
		double xctrl2, double yctrl2, double x2, double y2){
		super(x1, y1, xctrl1, yctrl1, xctrl2, yctrl2, x2, y2);
	}
	
	
	// ===================================================================
	// methods specific to BezierCurve2D
		
	/**
	 * Returns the matrix of parametric representation of the line. Result has the 
	 * form : <p>
	 * <code>[ x0  dx dx2 dx3] </code><p>
	 * <code>[ y0  dy dy2 dy3] </code><p>
	 * Coefficients are from the parametric equation :
	 * x(t) = x0 + dx*t + dx2*t^2 + dx3*t^3
	 * y(t) = y0 + dy*t + dy2*t^2 + dy3*t^3
	 */
	public double[][] getParametric(){
		double[][] tab = new double[2][4];
		tab[0][0] = x1;
		tab[0][1] = 3*ctrlx1 - 3*x1;
		tab[0][2] = 3*x1 -6*ctrlx1 + 3*ctrlx2;
		tab[0][3] = x2 - 3*ctrlx2 + 3*ctrlx1 - x1;
		
		tab[1][0] = y1;		
		tab[1][1] = 3*ctrly1 - 3*y1;
		tab[1][2] = 3*y1 -6*ctrly1 + 3*ctrly2;
		tab[1][3] = y2 - 3*ctrly2 + 3*ctrly1 - y1;
		return tab;
	}

	/**
	 * Creates a polyline with <code>n</code> line segments approximating
	 * the Bezier curve.
	 * @param n number of line segments of polyline
	 * @return a polyline with <code>n</code> line segments.
	 */
	public Polyline2D getAsPolyline(int n){
		Point2D[] points = new Point2D[n+1];
		for(int i=0; i<n+1; i++)
			points[i] = this.getPoint((double)i/(double)n);
		return new Polyline2D(points);		
	}
	
	
	// ===================================================================
	// methods from SmoothCurve2D interface
	
	public Vector2D getTangent(double t){
		double [][] c = getParametric();
		double dx = c[0][1] +  (2*c[0][2] + 3*c[0][3]*t)*t;
		double dy = c[1][1] +  (2*c[1][2] + 3*c[1][3]*t)*t;
		return new Vector2D(dx, dy);
	}


	/**
	 * returns the curvature of the Curve.
	 */
	public double getCurvature(double t){
		double [][] c = getParametric();
		double xp = c[0][1] +  (2*c[0][2] + 3*c[0][3]*t)*t;
		double yp = c[1][1] +  (2*c[1][2] + 3*c[1][3]*t)*t;
		double xs = 2*c[0][2] + 6*c[0][3]*t;
		double ys = 2*c[1][2] + 6*c[1][3]*t;
		
		return (xp*ys - yp*xs)/Math.pow(xp*xp + yp*yp, 1.5);
	}
	
	// ===================================================================
	// methods from ContinousCurve2D interface 
	
	public Collection<? extends SmoothCurve2D> getSmoothPieces() {
		ArrayList<BezierCurve2D> list = new ArrayList<BezierCurve2D>(1);
		list.add(this);
		return list;
	}
	
	/**
	 * The cubic curve is never closed.
	 */
	public boolean isClosed() {
		return false;
	}

	// ===================================================================
	// methods from OrientedCurve2D interface 
	
	/**
	 * Use winding angle of approximated polyline
	 * @see math.geom2d.OrientedCurve2D#getWindingAngle(java.awt.geom.Point2D)
	 */
	public double getWindingAngle(java.awt.geom.Point2D point) {
		return this.getAsPolyline(100).getWindingAngle(point);
	}
	
	/**
	 * return true if the point is 'inside' the domain bounded by the curve.
	 * Uses a polyline approximation.
	 * @param pt a point in the plane
	 * @return true if the point is on the left side of the curve.
	 */
	public boolean isInside(java.awt.geom.Point2D pt){
		return this.getAsPolyline(100).isInside(pt);
	}

	public double getSignedDistance(java.awt.geom.Point2D point) {
		if(isInside(point))
			return -getDistance(point.getX(), point.getY());
		else
			return getDistance(point.getX(), point.getY());
	}

	/**
	 * @see math.geom2d.OrientedCurve2D#getSignedDistance(java.awt.geom.Point2D)
	 */
	public double getSignedDistance(double x, double y) {
		if(isInside(new Point2D(x, y)))
			return -getDistance(x, y);
		else
			return getDistance(x, y);
	}

	
	// ===================================================================
	// methods from Curve2D interface 

	/**
	 * returns 0, as Bezier curve is parametrized between 0 and 1.
	 */
	public double getT0() {
		return 0;
	}
	
	/**
	 * returns 1, as Bezier curve is parametrized between 0 and 1.
	 */
	public double getT1() {
		return 1;
	}

	/**
	 * Use approximation, by replacing Bezier curve with a polyline. 
	 * @see math.geom2d.Curve2D#getIntersections(math.geom2d.StraightObject2D)
	 */
	public Collection<Point2D> getIntersections(StraightObject2D line) {
		return this.getAsPolyline(100).getIntersections(line);
	}

	/**
	 * @see math.geom2d.Curve2D#getPoint(double)
	 */
	public Point2D getPoint(double t){
		return getPoint(t, new Point2D());
	}

	/**
	 * @see math.geom2d.Curve2D#getPoint(double, math.geom2d.Point2D)
	 */
	public Point2D getPoint(double t, Point2D point) {
		if(t<0 || t>1) return null;
		double [][] c = getParametric();
		double x = c[0][0] + (c[0][1] + (c[0][2] + c[0][3]*t)*t)*t;
		double y = c[1][0] + (c[1][1] + (c[1][2] + c[1][3]*t)*t)*t;
		point.setLocation(x, y);
		return point;
	}

	/**
	 * Get the first point of the curve. 
	 * @return the first point of the curve
	 */
	public Point2D getFirstPoint(){
		return new Point2D(this.x1, this.y1);
	}
	
	/**
	 * Get the last point of the curve. 
	 * @return the last point of the curve.
	 */
	public Point2D getLastPoint(){
		return new Point2D(this.x2, this.y2);
	}

	public Collection<Point2D> getSingularPoints(){
		return new ArrayList<Point2D>(0);
	}
	
	/**
	 * Compute position by approximating cubic spline with a polyline.
	 */
	public double getPosition(Point2D point){
		int N=100;
		return this.getAsPolyline(N).getPosition(point)/((double)N);
	}

	/**
	 * Compute position by approximating cubic spline with a polyline.
	 */
	public double project(Point2D point){
		int N=100;
		return this.getAsPolyline(N).project(point)/((double)N);
	}

	/**
	 * Returns the bezier curve given by control points taken in reverse
	 * order.
	 */
	public BezierCurve2D getReverseCurve(){
		return new BezierCurve2D(this.getP2(), this.getCtrlP2(), this.getCtrlP1(), this.getP1());
	}
	
	/**
	 * Computes subcurve. If t1<t0, returns null.
	 */
	public BezierCurve2D getSubCurve(double t0, double t1){
		t0 = Math.max(t0, 0);
		t1 = Math.min(t1, 1);
		if(t0>t1) return null;
		
		double dt = t1-t0;
		Vector2D v0 = getTangent(t0); 
		Vector2D v1 = getTangent(t1);
		v0.setDx(v0.getDx()*dt);
		v0.setDy(v0.getDy()*dt);
		v1.setDx(v1.getDx()*dt);
		v1.setDy(v1.getDy()*dt);
		return new BezierCurve2D(getPoint(t0), v0, getPoint(t1), v1);
	}

	// ===================================================================
	// methods from Shape2D interface 

	/**
	 * @see math.geom2d.Shape2D#getDistance(java.awt.geom.Point2D)
	 */
	public double getDistance(java.awt.geom.Point2D p) {
		return this.getDistance(p.getX(), p.getY());
	}

	/**
	 * Compute approximated distance, computed on a polyline.
	 * @see math.geom2d.Shape2D#getDistance(double, double)
	 */
	public double getDistance(double x, double y) {
//		int N=100;
//		Point2D[] points = new Point2D[N];
//		for(int i=0; i<N; i++)
//			points[i] = this.getPoint((double)i/(N-1.0));
//		Polyline2D polyline = new Polyline2D(points);
		
		return this.getAsPolyline(100).getDistance(x, y);
	}

	/**
	 * return true, a cubic Bezier Curve is always bounded.
	 */
	public boolean isBounded() {
		return true;
	}

	/**
	 * @see math.geom2d.Shape2D#getClippedShape(math.geom2d.Box2D)
	 */
	public Shape2D getClippedShape(Box2D box) {
		return box.clipContinuousOrientedCurve(this);
	}

	/**
	 * Clip the circle arc by a box. The result is an instance of
	 * ContinuousOrientedCurveSet2D<BezierCurve2D>, which 
	 * contains only instances of EllipseArc2D. If the ellipse arc is not
	 * clipped, the result is an instance of
	 * ContinuousOrientedCurveSet2D<BezierCurve2D> which contains 0 curves.
	 */
	public CurveSet2D<? extends BezierCurve2D> clip(Box2D box) {
		// Clip the curve
		CurveSet2D<SmoothCurve2D> set = box.clipSmoothCurve(this);
		
		// Stores the result in appropriate structure
		CurveSet2D<BezierCurve2D> result =
			new CurveSet2D<BezierCurve2D> ();
		
		// convert the result
		for(Curve2D curve : set.getCurves()){
			if (curve instanceof BezierCurve2D)
				result.addCurve((BezierCurve2D) curve);
		}
		return result;
	}


	public Box2D getBoundingBox(){
		java.awt.geom.Rectangle2D rect = this.getBounds2D();
		return new Box2D(rect.getMinX(), rect.getMaxX(), rect.getMinY(), rect.getMaxY());
	}
	
	/**
	 * Returns the Bezier Curve transformed by the given AffineTransform2D.
	 * This is simply done by transforming control points of the curve.
	 */
	public BezierCurve2D transform(AffineTransform2D trans) {
		return new BezierCurve2D(
				trans.transform(this.getP1()), 
				trans.transform(this.getCtrlP1()), 
				trans.transform(this.getCtrlP2()), 
				trans.transform(this.getP2())) ;
	}
	
	
	public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path){
		Point2D p = new Point2D();
		for(double t=0; t<1;t+=.01){
			p = this.getPoint(t);
			path.lineTo((float)p.getX(), (float)p.getY());
		}
		p = this.getPoint(getT1());
		path.lineTo((float)p.getX(), (float)p.getY());
		return path;
	}
	
	public java.awt.geom.GeneralPath getInnerPath(){
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		Point2D p = new Point2D();
		for(double t=0; t<1;t+=.01){
			p = this.getPoint(t);
			path.lineTo((float)p.getX(), (float)p.getY());
		}
		return path;
	}

	public Collection<ContinuousCurve2D> getContinuousCurves() {
		ArrayList<ContinuousCurve2D> list = new ArrayList<ContinuousCurve2D>(1);
		list.add(this);
		return list;
	}
}
