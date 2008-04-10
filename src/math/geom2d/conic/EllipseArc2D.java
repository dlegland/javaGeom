/* file : EllipseArc2D.java
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
 * Created on 24 avr. 2006
 *
 */
package math.geom2d.conic;


import java.util.*;

import math.geom2d.Angle2D;
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
import math.geom2d.line.Ray2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.line.StraightObject2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * An arc of ellipse. It is defined by an ellipse, 
 * a starting angle, and a signed angle extent.
 * @author dlegland
 */
public class EllipseArc2D implements SmoothOrientedCurve2D{
	protected Ellipse2D ellipse;
		
	protected double startAngle=0;
	protected double angleExtent=Math.PI;
	
	// ====================================================================
	// Constructors
	
	/**
	 * Construct a default Ellipse arc, centered on (0,0), with radii equal to
	 * 1 and 1, orientation equal to 0, start angle equal to 0, and angle
	 * extent equal to PI/2.
	 * 
	 */
	public EllipseArc2D(){
		this(0, 0, 1, 1, 0, 0, Math.PI/2);
	}
	
	/**
	 * Specify supporting ellipse, start angle and angle extent.
	 * @param ell the supporting ellipse
	 * @param start the starting angle (angle between 0 and 2*PI)
	 * @param extent the angle extent (signed angle)
	 */
	public EllipseArc2D(Ellipse2D ell, double start, double extent){
		this(ell.xc, ell.yc, ell.r1, ell.r2, ell.theta, start, extent);
	}

	/**
	 * Specify supporting ellipse, start angle and end angle, and a flag
	 * indicating wheter the arc is directed or not.
	 * @param ell the supporting ellipse
	 * @param start the starting angle
	 * @param end the ending angle
	 * @param direct flag indicating if the arc is direct
	 */
	public EllipseArc2D(Ellipse2D ell, double start, double end, boolean direct){
		this(ell.xc, ell.yc, ell.r1, ell.r2, ell.theta, start, end, direct);
	}
	
	
	/**
	 * Specify parameters of supporting ellipse, start angle, and angle
	 * extent. 
	 */
	public EllipseArc2D(double xc, double yc, double a, double b, 
		double theta, double start, double extent){
		this.ellipse = new Ellipse2D(xc, yc, a, b, theta);
		this.startAngle = start;
		this.angleExtent = extent;
	}

	/**
	 * Specify parameters of supporting ellipse, bounding angles and flag 
	 * for direct ellipse. 
	 */
	public EllipseArc2D(double xc, double yc, double a, double b, 
			double theta, double start, double end, boolean direct){
		this.ellipse = new Ellipse2D(xc, yc, a, b, theta, direct);
		this.startAngle = start;
		this.angleExtent = Angle2D.formatAngle(end-start);
		if(!direct) this.angleExtent = this.angleExtent - Math.PI*2;
	}
	
	// ====================================================================
	// methods specific to EllipseArc2D
	
	/** Get angle associated to given position*/
	public double getAngle(double position){
		if(position<0) position=0;
		if(position>Math.abs(angleExtent)) position=Math.abs(angleExtent);
		if(angleExtent<0) position = -position;
		return Angle2D.formatAngle(startAngle+position);
	}
	
	// ====================================================================
	// methods from interface OrientedCurve2D
	
	/* (non-Javadoc)
	 * @see math.geom2d.ContinuousCurve2D#getViewAngle(math.geom2d.Point2D)
	 */
	public double getWindingAngle(java.awt.geom.Point2D point) {
		Point2D p1 = getPoint(startAngle);
		Point2D p2 = getPoint(startAngle+angleExtent);

		// compute angle of point with extreme points
		double angle1 = Angle2D.getHorizontalAngle(point, p1);
		double angle2 = Angle2D.getHorizontalAngle(point, p2);
		
		// test on which 'side' of the arc the point lie
		boolean b1 = (new StraightLine2D(p1, p2)).isInside(point);
		boolean b2 = ellipse.isInside(point);
		
		if(angleExtent>0){
			if(b1 || b2){
				if(angle2>angle1) return angle2 - angle1;
				else return 2*Math.PI - angle1 + angle2;
			}else{
				if(angle2>angle1) return angle2 - angle1 - 2*Math.PI;
				else return angle2 - angle1;
			}
		}else{
			if(b1 || b2){
				if(angle1>angle2) return angle1 - angle2;
				else return 2*Math.PI - angle2 + angle1;
			}else{
				if(angle1>angle2) return angle1 - angle2 - 2*Math.PI;
				else return angle1 - angle2;
			}
		}
	}

	public boolean isInside(java.awt.geom.Point2D p) {
		return getSignedDistance(p.getX(), p.getY())<0;
	}
	
	public double getSignedDistance(java.awt.geom.Point2D p) {
		return getSignedDistance(p.getX(), p.getY());
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#getSignedDistance(math.geom2d.Point2D)
	 */
	public double getSignedDistance(double x, double y) {
		boolean direct = angleExtent>0;
		
		double dist = getDistance(x, y);
		Point2D point = new Point2D(x, y);
		
		boolean inside = ellipse.isInside(point);
		if(inside)
			return angleExtent>0 ?  -dist : dist;
		
		Point2D p1 = getPoint(startAngle);
		Point2D p2 = getPoint(startAngle+angleExtent);
		boolean onLeft = (new StraightLine2D(p1, p2)).isInside(point);
		
		if(direct && !onLeft)	return dist;
		if(!direct && onLeft)	return -dist;

		boolean left1 = (new Ray2D(p1, -Math.sin(startAngle), 
				Math.cos(startAngle))).isInside(point);
		if(direct && !left1) return dist;
		if(!direct && left1) return -dist;
		
		boolean left2 = (new Ray2D(p2, -Math.sin(startAngle+angleExtent), 
				Math.cos(startAngle+angleExtent))).isInside(point);
		if(direct && !left2) return dist;
		if(!direct && left2) return -dist;

		if(direct)
			return -dist;
		else
			return dist; 
	}

	// ====================================================================
	// methods from interface SmoothCurve2D
	
	public Vector2D getTangent(double t){
		// convert position to angle
		if(angleExtent<0)	t = startAngle-t;
		else 				t = startAngle+t;
		return ellipse.getTangent(t);
	}
	
	/**
	 * returns the curvature of the ellipse arc.
	 */
	public double getCurvature(double t){
		// convert position to angle
		if(angleExtent<0)	t = startAngle-t;
		else 				t = startAngle+t;
		return ellipse.getCurvature(t);
	}

		
	// ====================================================================
	// methods from interface ContinuousCurve2D
	
	public Polyline2D getAsPolyline(int n){
		Point2D[] points = new Point2D[n+1];
		
		double dt = this.angleExtent/(double)n;
		if(this.angleExtent>0)
			for(int i=0; i<n+1; i++)
				points[i] = this.getPoint(((double)i)*dt + startAngle);
		else
			for(int i=0; i<n+1; i++)
				points[i] = this.getPoint(-((double)i)*dt + startAngle);			
		
		return new Polyline2D(points); 
	}

	/** Return false, as an ellipse arc is never closed.*/
	public boolean isClosed() {
		return false;
	}

	/** 
	 * return a SmoothCurve array containing this ellipse arc.
	 * @see math.geom2d.ContinuousCurve2D#getSmoothPieces()
	 */
	public Collection<? extends SmoothCurve2D> getSmoothPieces() {
		ArrayList<EllipseArc2D> list = new ArrayList<EllipseArc2D>(1);
		list.add(this);
		return list;
	}


	// ====================================================================
	// methods from interface Curve2D
	
	/** Always returns 0 */
	public double getT0() {
		return 0;
	}

	/** Always returns the absolute value of the angle extent */
	public double getT1() {
		return Math.abs(angleExtent);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Curve2D#getPoint(double)
	 */
	public Point2D getPoint(double t) {
		return getPoint(t, new Point2D());
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Curve2D#getPoint(double, math.geom2d.Point2D)
	 */
	public Point2D getPoint(double t, Point2D point) {
		// check bounds
		t = Math.max(t, 0);
		t = Math.min(t, Math.abs(angleExtent));
		
		// convert position to angle
		if(angleExtent<0)	t = startAngle-t;
		else 				t = startAngle+t;
		
		// return corresponding point
		return ellipse.getPoint(t, point);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Curve2D#getPosition(math.geom2d.Point2D)
	 */
	public double getPosition(Point2D point) {
		if(!ellipse.contains(point)) return Double.NaN;
		double angle = ellipse.getPosition(point);
		angle = angle-startAngle;
		if(angleExtent>0)
			angle = - angle;
		
		if(angle<0) return Double.NaN;
		if(angle>Math.abs(angleExtent)) return Double.NaN;
		
		return angle;
	}
	
	public double project(Point2D point) {
		double angle = ellipse.project(point);
		
		// convert to arc parameterization
		if(angleExtent>0)
			angle = Angle2D.formatAngle(angle-startAngle);
		else
			angle = Angle2D.formatAngle(startAngle-angle);
		
		// ensure projection lies on the arc
		if(angle<0) return 0;
		if(angle>Math.abs(angleExtent)) return Math.abs(angleExtent);
		
		return angle;
	}

	/**
	 * Get the first point of the curve. 
	 * @return the first point of the curve
	 */
	public Point2D getFirstPoint(){
		return ellipse.getPoint(startAngle);
	}
	
	/**
	 * Get the last point of the curve. 
	 * @return the last point of the curve.
	 */
	public Point2D getLastPoint(){
		return ellipse.getPoint(startAngle+angleExtent);
	}

	public Collection<Point2D> getSingularPoints(){
		ArrayList<Point2D> list = new ArrayList<Point2D>(2);
		list.add(this.getFirstPoint());
		list.add(this.getLastPoint());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.Curve2D#getIntersections(math.geom2d.StraightObject2D)
	 */
	public Collection<Point2D> getIntersections(StraightObject2D line) {
		
		// check point contained in it
		ArrayList<Point2D> array = new ArrayList<Point2D>();
		for(Point2D point : ellipse.getIntersections(line))
			if(contains(point))
				array.add(point);
		
		return array;
	}

	/**
	 * returns the ellipse arc which refers to the reversed parent ellipse,
	 * with same start angle, and with opposite angle extent.
	 */
	public EllipseArc2D getReverseCurve(){
		return new EllipseArc2D((Ellipse2D)this.ellipse.getReverseCurve(), 
				startAngle, -angleExtent);
	}
	
	public Collection<ContinuousCurve2D> getContinuousCurves() {
		ArrayList<ContinuousCurve2D> list = new ArrayList<ContinuousCurve2D>(1);
		list.add(this);
		return list;
	}

	/**
	 * return a new EllipseArc2D.
	 */
	public EllipseArc2D getSubCurve(double t0, double t1){
		// convert position to angle
		t0 = Angle2D.formatAngle(startAngle + t0);
		t1 = Angle2D.formatAngle(startAngle + t1);
		
		// check bounds of angles
		if(!Angle2D.containsAngle(startAngle, startAngle+angleExtent, t0, angleExtent>0))
			t0 = startAngle;
		if(!Angle2D.containsAngle(startAngle, startAngle+angleExtent, t1, angleExtent>0))
			t1 = angleExtent;
		
		// create new arc
		return new EllipseArc2D(ellipse, t0, t1, angleExtent>0);
	}
	
	
	// ====================================================================
	// methods from interface Shape2D
	
	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#getDistance(math.geom2d.Point2D)
	 */
	public double getDistance(java.awt.geom.Point2D point) {
		return getDistance(point.getX(), point.getY());
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#getDistance(double, double)
	 */
	public double getDistance(double x, double y) {
		Point2D p = getPoint(project(new Point2D(x, y)));
		return p.getDistance(x, y);
	}

	/** Always return true: an ellipse arc is bounded by definition */
	public boolean isBounded() {
		return true;
	}

	/**
	 * Clip the ellipse arc by a box. The result is an instance of
	 * CurveSet2D<EllipseArc2D>, which 
	 * contains only instances of EllipseArc2D.
	 * If the ellipse arc is not clipped, the result is an instance of
	 * CurveSet2D<EllipseArc2D> which contains 0 curves.
	 */
	public CurveSet2D<? extends EllipseArc2D> clip(Box2D box) {
		// Clip the curve
		CurveSet2D<SmoothCurve2D> set = box.clipSmoothCurve(this);
		
		// Stores the result in appropriate structure
		CurveSet2D<EllipseArc2D> result =
			new CurveSet2D<EllipseArc2D> ();
		
		// convert the result
		for(Curve2D curve : set.getCurves()){
			if (curve instanceof EllipseArc2D)
				result.addCurve((EllipseArc2D) curve);
		}
		return result;
	}


	public Box2D getBoundingBox() {
		
		// first get ending points
		Point2D p0 = getFirstPoint();
		Point2D p1 = getLastPoint();
		
		// get coordinate of ending points
		double x0 = p0.getX();
		double y0 = p0.getY();
		double x1 = p1.getX();
		double y1 = p1.getY();
		
		// intialize min and max coords
		double xmin = Math.min(x0, x1);
		double xmax = Math.max(x0, x1);
		double ymin = Math.min(y0, y1);
		double ymax = Math.max(y0, y1);
		
		// check cases arc contains one maximum
		Point2D center = ellipse.getCenter();
		double xc = center.getX();
		double yc = center.getY();
		if(Angle2D.containsAngle(startAngle, startAngle+angleExtent, 
				Math.PI/2+ellipse.theta, angleExtent>=0))
			ymax = Math.max(ymax, yc + ellipse.r1); 
		if(Angle2D.containsAngle(startAngle, startAngle+angleExtent, 
				3*Math.PI/2+ellipse.theta, angleExtent>=0))
			ymin = Math.min(ymin, yc - ellipse.r1); 
		if(Angle2D.containsAngle(startAngle, startAngle+angleExtent, 
				ellipse.theta, angleExtent>=0))
			xmax = Math.max(xmax, xc + ellipse.r2); 
		if(Angle2D.containsAngle(startAngle, startAngle+angleExtent, 
				Math.PI+ellipse.theta, angleExtent>=0))
			xmin = Math.min(xmin, xc - ellipse.r2); 
			
		// return a bounding with computed limits
		return new Box2D(xmin, xmax, ymin, ymax);
	}

	
	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#transform(math.geom2d.AffineTransform2D)
	 */
	public EllipseArc2D transform(AffineTransform2D trans) {
		Ellipse2D ell = ellipse.transform(trans);
		return new EllipseArc2D(ell, startAngle, angleExtent);
	}


	// ====================================================================
	// methods from interface java.awt.Shape
	
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


	/* (non-Javadoc)
	 * @see java.awt.Shape#contains(double, double)
	 */
	public boolean contains(double x, double y) {
		return getDistance(x, y)>Shape2D.ACCURACY;
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#contains(java.awt.geom.Point2D)
	 */
	public boolean contains(java.awt.geom.Point2D point) {
		return contains(point.getX(), point.getY());
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#intersects(double, double, double, double)
	 */
	public boolean intersects(double x, double y, double w, double h) {
		// circle arc contained in the rectangle
		if(new Box2D(x, x+w, y, y+h).contains(getFirstPoint())) return true;
		if(new Box2D(x, x+w, y, y+h).contains(getLastPoint())) return true;
		
		if(this.getIntersections(new LineSegment2D(x, y, x+w, y)).size()>0) 
			return true;
		if(this.getIntersections(new LineSegment2D(x+w, y, x+w, y+h)).size()>0) 
			return true;
		if(this.getIntersections(new LineSegment2D(x+w, y+h, x, y+h)).size()>0) 
			return true;
		if(this.getIntersections(new LineSegment2D(x, y+h, x, y)).size()>0) 
			return true;
			
		return false;
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#intersects(java.awt.geom.Rectangle2D)
	 */
	public boolean intersects(java.awt.geom.Rectangle2D rect) {
		return intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	/** return always false, as every curve */
	public boolean contains(double xr, double yr, double wr, double hr) {
		return false;
	}

	/** return always false, as every curve */
	public boolean contains(java.awt.geom.Rectangle2D arg0) {
		return false;
	}

	public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path){
		
		double cot = Math.cos(ellipse.theta);
		double sit = Math.sin(ellipse.theta);
		double xc = ellipse.xc;
		double yc = ellipse.yc;
		double r1 = ellipse.r1;
		double r2 = ellipse.r2;
		double endAngle = startAngle+angleExtent;
		
		if(angleExtent>0)
			for(double t=startAngle; t<endAngle; t+=angleExtent/100)
				path.lineTo((float)(xc+r1*Math.cos(t)*cot-r2*Math.sin(t)*sit), 
						(float)(yc+r1*Math.cos(t)*sit+r2*Math.sin(t)*cot));
		else
			for(double t=startAngle; t>endAngle; t+=angleExtent/100)
				path.lineTo((float)(xc+r1*Math.cos(t)*cot-r2*Math.sin(t)*sit), 
						(float)(yc+r1*Math.cos(t)*sit+r2*Math.sin(t)*cot));
		
		return path;
	}

	
	public java.awt.geom.GeneralPath getInnerPath() {
		
		// Creates the path
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		
		double cot = Math.cos(ellipse.theta);
		double sit = Math.sin(ellipse.theta);
		double xc = ellipse.xc;
		double yc = ellipse.yc;
		double r1 = ellipse.r1;
		double r2 = ellipse.r2;
		double endAngle = startAngle+angleExtent;
		
		// position to the first point
//		path.moveTo((float)(xc+r1*Math.cos(startAngle)*cot-r2*Math.sin(startAngle)*sit), 
//					(float)(yc+r1*Math.cos(startAngle)*sit+r2*Math.sin(startAngle)*cot));

		if(angleExtent>0)
			for(double t=startAngle; t<endAngle; t+=angleExtent/100)
				path.lineTo((float)(xc+r1*Math.cos(t)*cot-r2*Math.sin(t)*sit), 
							(float)(yc+r1*Math.cos(t)*sit+r2*Math.sin(t)*cot));
		else
			for(double t=startAngle; t>endAngle; t+=angleExtent/100)
				path.lineTo((float)(xc+r1*Math.cos(t)*cot-r2*Math.sin(t)*sit), 
							(float)(yc+r1*Math.cos(t)*sit+r2*Math.sin(t)*cot));
		
		return path;
	}
	
	protected java.awt.geom.GeneralPath getGeneralPath() {
		
		// Creates the path
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		
		double cot = Math.cos(ellipse.theta);
		double sit = Math.sin(ellipse.theta);
		double xc = ellipse.xc;
		double yc = ellipse.yc;
		double r1 = ellipse.r1;
		double r2 = ellipse.r2;
		double endAngle = startAngle+angleExtent;
		
		// position to the first point
		path.moveTo(
				(float)(xc+r1*Math.cos(startAngle)*cot-r2*Math.sin(startAngle)*sit), 
				(float)(yc+r1*Math.cos(startAngle)*sit+r2*Math.sin(startAngle)*cot));

		// add several inner points
		if(angleExtent>0)
			for(double t=startAngle; t<endAngle; t+=angleExtent/100)
				path.lineTo((float)(xc+r1*Math.cos(t)*cot-r2*Math.sin(t)*sit), 
							(float)(yc+r1*Math.cos(t)*sit+r2*Math.sin(t)*cot));
		else
			for(double t=startAngle; t>endAngle; t+=angleExtent/100)
				path.lineTo((float)(xc+r1*Math.cos(t)*cot-r2*Math.sin(t)*sit), 
							(float)(yc+r1*Math.cos(t)*sit+r2*Math.sin(t)*cot));
		
		return path;
	}	
	
	/** 
	 * Return pathiterator for this ellipse arc.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans){
		return this.getGeneralPath().getPathIterator(trans);
	}

	/**
	 * Return pathiterator for this ellipse arc.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans, double flatness){
		return this.getGeneralPath().getPathIterator(trans, flatness);
	}

	// ====================================================================
	// methods from interface Object
	
	public boolean equals(Object obj){
		if(!(obj instanceof EllipseArc2D))
			return false;
		EllipseArc2D arc = (EllipseArc2D) obj;
		
		// test whether supporting ellipses have same support
		if(Math.abs(ellipse.xc-arc.ellipse.xc)>Shape2D.ACCURACY) return false;
		if(Math.abs(ellipse.yc-arc.ellipse.yc)>Shape2D.ACCURACY) return false;
		if(Math.abs(ellipse.r1-arc.ellipse.r1)>Shape2D.ACCURACY) return false;
		if(Math.abs(ellipse.r2-arc.ellipse.r2)>Shape2D.ACCURACY) return false;
		if(Math.abs(ellipse.theta-arc.ellipse.theta)>Shape2D.ACCURACY) return false;
		
		// test is angles are the same
		if(Math.abs(Angle2D.formatAngle(startAngle)-Angle2D.formatAngle(arc.startAngle))>
				Shape2D.ACCURACY) return false;
		if(Math.abs(Angle2D.formatAngle(angleExtent)-Angle2D.formatAngle(arc.angleExtent))>
				Shape2D.ACCURACY) return false;
		return true;
	}
	
}
