/* file : ParabolaArc2D.java
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
 * 
 * Created on 02 May 2007
 *
 */
package math.geom2d.conic;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.ContinuousCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.curve.SmoothOrientedCurve2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Polyline2D;
import math.geom2d.line.StraightObject2D;
import math.geom2d.polygon.Rectangle2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * An arc of parabola, defined by a parent parabola, and two limits for the
 * parametrization.
 * @author dlegland
 */
public class ParabolaArc2D implements SmoothOrientedCurve2D {


	protected Parabola2D parabola = new Parabola2D();
	
	protected double t0 = -10;
	protected double t1 = 10;
	
	private boolean debug = false;
	
	public ParabolaArc2D(Parabola2D parabola, double t0, double t1) {
		this.parabola = parabola;
		this.t0 = t0;
		this.t1 = t1;
	}

	
	// ==========================================================
	// methods specific to ParabolaArc2D
	

	/**
	 * Returns the polyline approximating this parabola arc, by using 
	 * <code>N</code> line segments. If Parabola arc is not bounded
	 * (i.e. one of the bounds of the parametrization domain is infinite),
	 * parametriztion domain is bounded by an arbitrary value.
	 */
	public Polyline2D getAsPolyline(int n){
		Point2D[] points = new Point2D[n+1];
		
		// avoid the cases where t0 and/or t1 is infinite
		double t0 = Math.max(this.t0, -1000);
		double t1 = Math.min(this.t1, 1000);
		if(debug)
			System.out.println("theta=" + Math.toDegrees(parabola.theta) + " t0=" + t0 + " t1="+t1);
		
		double dt = (t1-t0)/(double)n;
		points[0] = this.getPoint(t0);
		for(int i=1; i<n; i++)
			points[i] = this.getPoint(((double)i)*dt + t0);
		points[n] = this.getPoint(t1);
		
		return new Polyline2D(points); 
	}
	
	public Parabola2D getParabola(){
		return this.parabola;
	}
	
//	/**
//	 * at the moment, return true.
//	 */
//	public boolean isDirect() {
//		return true;
//	}

	// ==========================================================
	// methods implementing the OrientedCurve2D interface

	public double getWindingAngle(java.awt.geom.Point2D point) {
		if(isInside(point))
			return Math.PI*2;
		else
			return 0.0;
	}

	public double getSignedDistance(java.awt.geom.Point2D p) {
		return getSignedDistance(p.getX(), p.getY());
	}

	public double getSignedDistance(double x, double y) {
		if(isInside(new Point2D(x, y)))
			return -getDistance(x, y);
		return -getDistance(x, y);
	}

	public boolean isInside(java.awt.geom.Point2D point) {
		if (parabola.isInside(point)) return true;
		//TODO: implement it
		// compose with ray emanating from ending points
		return false;
	}

	// ==========================================================
	// methods implementing the SmoothCurve2D interface
	
	public Vector2D getTangent(double t) {
		return parabola.getTangent(t);
	}

	/**
	 * returns the curvature of the parabola arc.
	 */
	public double getCurvature(double t){
		return parabola.getCurvature(t);
	}

	
	// ==========================================================
	// methods implementing the ContinuousCurve2D interface

	public Collection<? extends SmoothCurve2D> getSmoothPieces() {
		ArrayList<ParabolaArc2D> list = new ArrayList<ParabolaArc2D>(1);
		list.add(this);
		return list;
	}

	// return false, by definition of a conic arc
	public boolean isClosed() {
		return false;
	}


	// ====================================================================
	// methods implementing the Curve2D interface
	
	/** 
	 * Returns the position of the first point of the parabola arc.
	 */
	public double getT0(){
		return t0;
	}

	/** 
	 * Returns the position of the last point of the parabola arc.
	 */
	public double getT1(){
		return t1;
	}


	public Point2D getPoint(double t) {
		return getPoint(t, new Point2D());
	}

	public Point2D getPoint(double t, Point2D point) {
		t = Math.min(Math.max(t, t0), t1);
		return parabola.getPoint(t);
	}

	/**
	 * return the first point of the parabola arc.
	 */
	public Point2D getFirstPoint() {
		return this.getPoint(t0);
	}

	/**
	 * return the last point of the parabola arc.
	 */
	public Point2D getLastPoint() {
		return this.getPoint(t1);
	}

	public double getPosition(Point2D point) {
		if(!this.parabola.contains(point)) return Double.NaN;
		double t = this.parabola.getPosition(point);
		if(t-t0<-ACCURACY) return Double.NaN;
		if(t1-t<ACCURACY) return Double.NaN;
		return t;
	}

	public double project(Point2D point) {
		double t = this.parabola.getPosition(point);
		return Math.min(Math.max(t, t0), t1);
	}

	public Collection<Point2D> getIntersections(StraightObject2D line) {
		Collection<Point2D> inters0 = this.parabola.getIntersections(line);
		ArrayList<Point2D> inters = new ArrayList<Point2D>();
		for(Point2D point : inters0){
			double pos = this.parabola.getPosition(point);
			if(pos>this.t0 && pos<this.t1)
				inters.add(point);
		}

		return inters;
	}

	/**
	 * Returns the parabola arc which refers to the reversed parent parabola,
	 * and with inverted parametrization bounds.
	 */
	public ParabolaArc2D getReverseCurve(){
		return new ParabolaArc2D((Parabola2D)this.parabola.getReverseCurve(), -t1, -t0);
	}

	public Collection<ContinuousCurve2D> getContinuousCurves() {
		ArrayList<ContinuousCurve2D> list = new ArrayList<ContinuousCurve2D>(1);
		list.add(this);
		return list;
	}

	public ParabolaArc2D getSubCurve(double t0, double t1){
		if(t1<t0) return null;
		t0 = Math.max(this.t0, t0);
		t1 = Math.min(this.t1, t1);
		return new ParabolaArc2D(parabola, t0, t1);
	}

	
	// ====================================================================
	// methods implementing the Shape2D interface
	
	public double getDistance(java.awt.geom.Point2D p) {
		return getDistance(p.getX(), p.getY());
	}

	public double getDistance(double x, double y) {
		// TODO Auto-generated method stub
		return this.getAsPolyline(100).getDistance(x, y);
	}
	
	/** return true if the arc is bounded, i.e. if both limits are finite.*/
	public boolean isBounded(){
		return (t0!=Double.NEGATIVE_INFINITY && t1!=Double.POSITIVE_INFINITY);
	}

	/**
	 * Clip the parabola arc by a box. The result is an instance of
	 * CurveSet2D<ParabolaArc2D>, which 
	 * contains only instances of ParabolaArc2D. If the parabola arc is not
	 * clipped, the result is an instance of
	 * CurveSet2D<ParabolaArc2D> which contains 0 curves.
	 */
	public CurveSet2D<? extends ParabolaArc2D> clip(Box2D box) {
		// Clip the curve
		CurveSet2D<SmoothCurve2D> set = box.clipSmoothCurve(this);
		
		// Stores the result in appropriate structure
		CurveSet2D<ParabolaArc2D> result =
			new CurveSet2D<ParabolaArc2D> ();
		
		// convert the result
		for(Curve2D curve : set.getCurves()){
			if (curve instanceof ParabolaArc2D)
				result.addCurve((ParabolaArc2D) curve);
		}
		return result;
	}

	public Box2D getBoundingBox() {
		// TODO Auto-generated method stub
		return this.getAsPolyline(100).getBoundingBox();
	}
	
	public ParabolaArc2D transform(AffineTransform2D trans) {
		return new ParabolaArc2D(parabola.transform(trans), t0, t1);
	}

	
	// ====================================================================
	// methods implementing the Shape interface
	
	public boolean contains(double x, double y) {
		if(!parabola.contains(x, y)) return false;
		double t = parabola.getPosition(new Point2D(x, y));
		if(t<this.t0) return false;
		if(t>this.t1) return false;

		return true;
	}

	public boolean contains(java.awt.geom.Point2D point) {
		return contains(point.getX(), point.getY());
	}

	public boolean intersects(double xr, double yr, double wr, double hr){
		for(LineSegment2D edge : new Rectangle2D(xr, yr, wr, hr).getEdges())
			if(this.getIntersections(edge).size()>0) 
				return true;
		return false;
	}

	public boolean intersects(java.awt.geom.Rectangle2D rect) {
		return intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	/**
	 * return false, as every curve.
	 */
	public boolean contains(double arg0, double arg1, double arg2, double arg3) {
		return false;
	}

	/**
	 * return false, as every curve.
	 */
	public boolean contains(java.awt.geom.Rectangle2D arg0) {
		return false;
	}

	/**
	 * Return bounding box of the shape.
	 */
	public java.awt.Rectangle getBounds(){
		return this.getBoundingBox().getAsAWTRectangle();
	}

	/**
	 * Return more precise bounds for the shape.
	 */
	public java.awt.geom.Rectangle2D getBounds2D(){
		return this.getBoundingBox().getAsAWTRectangle2D();
	}


	// ====================================================================
	// Drawing methods
	
	public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path){
		return this.getAsPolyline(32).appendPath(path);
	}
	
	public java.awt.geom.GeneralPath getGeneralPath(){
		return this.getAsPolyline(32).getGeneralPath();
	}
	
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans) {
		return getGeneralPath().getPathIterator(trans);
	}

	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans, double flatness) {
		return getGeneralPath().getPathIterator(trans, flatness);
	}
	
	// ====================================================================
	// Methods inherited from object interface
	
	
	public boolean equals(Object obj){
		if(!(obj instanceof ParabolaArc2D)) return false;
		ParabolaArc2D arc = (ParabolaArc2D) obj;
		
		if(!this.parabola.equals(arc.parabola)) return false;
		if (Math.abs(this.t0-arc.t0)>Shape2D.ACCURACY) return false;
		if (Math.abs(this.t1-arc.t1)>Shape2D.ACCURACY) return false;
		
		return true;
	}
}
