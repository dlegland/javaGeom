/* file : CircleArc2D.java
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
 * Created on 29 avr. 2006
 *
 */
package math.geom2d.conic;

import java.util.*;

import math.geom2d.Angle2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Ray2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.line.StraightObject2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * A circle arc, defined by the center and the radius of the containing
 * circle, by a starting angle, and by a (signed) angle extent.<p>
 * A circle arc is directed: if angle extent is positive, the arc is counter
 * clockwise. Otherwise, it is clockwise.<p>
 * A circle arc is parameterized using angle from center. The arc contains all
 * points with a parametric equation of t, for each t between 0 and the angle
 * extent.
 * @author dlegland
 */
public class CircleArc2D extends EllipseArc2D{

	protected Circle2D circle;	

	// ====================================================================
	// constructors

	
	
	/**
	 * Create a circle arc whose support circle is centered on (0,0) and has a
	 * radius equal to 1. Start angle is 0, and angle extent is PI/2.
	 */
	public CircleArc2D(){
		this(0, 0, 1, 0, Math.PI/2);
	}

	// Constructors based on Circles
	
	/**
	 * create a new circle arc based on an already existing circle.
	 */
	public CircleArc2D(Circle2D circle, double startAngle, double angleExtent){
		this(circle.xc, circle.yc, circle.r, startAngle, angleExtent);
	}

	/**
	 * create a new circle arc based on an already existing circle, specifying
	 * if arc is direct or not.
	 */
	public CircleArc2D(Circle2D circle, double startAngle, double endAngle, boolean direct){
		this(circle.xc, circle.yc, circle.r, startAngle, endAngle, direct);
	}

	// Constructors based on circles and points
	
	/**
	 * create a new circle arc based on an already existing circle, and two 
	 * points giving angles of arc, specifying whether arc is direct or not.
	 * @deprecated as this is a too much specific function.
	 */
	public CircleArc2D(Circle2D circle, Point2D startPoint, Point2D endPoint, boolean direct){
		this(circle.getCenter(), circle.getRadius(), 0, 0, direct);
		// update angle information
		Point2D center = circle.getCenter();
		this.startAngle = Angle2D.getHorizontalAngle(center, startPoint);
		this.angleExtent = Angle2D.formatAngle(Angle2D.getHorizontalAngle(center, endPoint)-startAngle);
		if(!direct)	angleExtent = angleExtent - Math.PI*2;
	}

	/**
	 * create a new circle arc based on an already existing circle, and two 
	 * points giving angles of arc, assuming arc is direct.
	 */
	public CircleArc2D(Circle2D circle, Point2D startPoint, Point2D endPoint){
		this(circle, startPoint, endPoint, true);
	}

	// Constructors based on points
	
	/** Create a new circle arc with specified point center and radius */
	public CircleArc2D(Point2D center, double radius, double startAngle, double angleExtent){
		this(center.getX(), center.getY(), radius, startAngle, angleExtent);
	}

	/** 
	 * Create a new circle arc with specified point center and radius, 
	 * start and end angles, and by specifying whether arc is direct or not.
	 */
	public CircleArc2D(Point2D center, double radius, double start, 
			double end, boolean direct){
		this(center.getX(), center.getY(), radius, start, end, direct);
	}
		

	
	// Constructors based on doubles
	
	/** Base constructor, for constructiong arc from circle parameters,
	 * start and end angles, and by specifying whether arc is direct or not.
	 */
	public CircleArc2D(double xc, double yc, double r, double start, double end, boolean direct){
		super(xc, yc, r, r, 0, start, end, direct);
		this.circle = new Circle2D(xc, yc, r);
		this.ellipse = this.circle;
	}
	
	
	/** Base constructor with all parameters specified */
	public CircleArc2D(double xc, double yc, double r, double start, double extent){
		super(xc, yc, r, r, 0, start, extent);
		this.circle = new Circle2D(xc, yc, r);
		this.ellipse = this.circle;
//		this.startAngle = start;
//		this.angleExtent = extent;
	}
	
	// ====================================================================
	// methods specific to CircleArc2D

	/**
	 * convert position on curve to angle with circle center.
	 */
	private double positionToAngle(double t){
		if(t>Math.abs(angleExtent)) t = angleExtent;
		if(t<0) t = 0;
		if(angleExtent<0) t = -t;		
		t = t + startAngle;
		return t;
	}
	
	public boolean containsAngle(double angle){
		return Angle2D.containsAngle(startAngle, startAngle+angleExtent, angle, angleExtent>0);
	}

	/**
	 * @deprecated  should access radius from supporting circle
	 */
	public double getRadius(){
		return circle.getRadius();
	}
	
	/**
	 * @deprecated  should access center from supporting circle
	 */
	public Point2D getCenter(){
		return circle.getCenter();
	}
	
	/**
	 * Returns the circle which contains the circle arc.
	 */
	public Circle2D getSupportCircle(){
		return circle;
	}
	
	/**
	 * Change the center of the support circle.
	 * @param point the new center of the arc.
	 */
	public void setCenter(Point2D point){
		circle.xc = point.getX();
		circle.yc = point.getY();
	}
	
	/**
	 * Change the radius of the support circle
	 * @param r the new radius
	 */
	public void setRadius(double r){
		circle.r = r;		
	}
	
	public void setArc(Point2D center, double radius, double start, double extent){
		circle.xc = center.getX();
		circle.yc = center.getY();
		circle.r  = radius;
		startAngle = start;
		angleExtent = extent;
	}

	/**
	 * @deprecated: do not use boolean 'direct' anymore
	 */
	@Deprecated
	public void setArc(Point2D center, double radius, double start, double end, boolean direct){
		circle.xc = center.getX();
		circle.yc = center.getY();
		circle.r = radius;
		startAngle = start;
		angleExtent = end-start;
	}
	
	public boolean isDirect(){
		return angleExtent>0;
	}

	public double getLength() {
		return circle.r*Math.abs(angleExtent);
	}

	
	// ====================================================================
	// methods from interface OrientedCurve2D
	
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

	public boolean isInside(java.awt.geom.Point2D point){
		return getSignedDistance(point.getX(), point.getY())<0;		
	}
	
	public double getSignedDistance(java.awt.geom.Point2D p) {
		return getSignedDistance(p.getX(), p.getY());
	}

	public double getSignedDistance(double x, double y) {
		double dist = getDistance(x, y);
		Point2D point = new Point2D(x, y);
		
		boolean direct = angleExtent>0;
		//boolean inCircle = Point2D.getDistance(x, y, xc, yc)<=r;
		boolean inCircle = circle.isInside(point);
		if(inCircle)
			return angleExtent>0 ?  -dist : dist;

		Point2D p1 = getPoint(startAngle);
		Point2D p2 = getPoint(startAngle+angleExtent);
		boolean onLeft = (new StraightLine2D(p1, p2)).isInside(point);
		
		if(direct && !onLeft)	return dist;
		if(!direct && onLeft)	return -dist;

		boolean left1 = (new Ray2D(p1, this.getTangent(startAngle))).isInside(point);
		if(direct && !left1) return dist;
		if(!direct && left1) return -dist;
		
		boolean left2 = (new Ray2D(p2, this.getTangent(startAngle+angleExtent))).isInside(point);
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
		t = this.positionToAngle(t);
		
		double r = circle.getRadius();
		if (angleExtent>0)
			return new Vector2D(-r*Math.sin(t), r*Math.cos(t));
		else
			return new Vector2D(r*Math.sin(t), -r*Math.cos(t));
	}


	// ===================================================================
	// methods from interface ContinuousCurve2D
	
	public Collection<? extends SmoothCurve2D> getSmoothPieces() {
		ArrayList<CircleArc2D> list = new ArrayList<CircleArc2D>(1);
		list.add(this);
		return list;
	}

	/**
	 * a circle arc is never closed by definition.
	 */
	public boolean isClosed(){
		return false;
	}
	
	
	// ====================================================================
	// methods from interface Curve2D
	
	/** Always return 0 */
	public double getT0() {
		return 0;
	}

	/** 
	 * return the last position of the circle are, which is given by the 
	 * angle extent of the arc. */
	public double getT1() {
		return Math.abs(this.angleExtent);
	}

	/**
	 * Returns the position of a point form the curvilinear position.
	 */
	public Point2D getPoint(double t) {
		return getPoint(t, new Point2D());
	}

	public Point2D getPoint(double t, Point2D point) {		
		t = this.positionToAngle(t);
		return circle.getPoint(t, point);
	}

	/**
	 * Get the first point of the curve. 
	 * @return the first point of the curve
	 */
	public Point2D getFirstPoint(){
		return circle.getPoint(startAngle, new Point2D());
	}
	
	/**
	 * Get the last point of the curve. 
	 * @return the last point of the curve.
	 */
	public Point2D getLastPoint(){
		return circle.getPoint(startAngle+angleExtent, new Point2D());
	}

	/**
	 * return relative position between 0 and the angle extent.
	 */
	public double getPosition(Point2D point) {
		double angle = Angle2D.getHorizontalAngle(circle.getCenter(), point);
		if(containsAngle(angle))
			if(angleExtent>0)
				return Angle2D.formatAngle(angle-startAngle);
			else
				return Angle2D.formatAngle(startAngle-angle);
		
		// return either 0 or 1, depending on which extremity is closer.
		return getFirstPoint().distance(point) < getLastPoint().distance(point) ? 0 : Math.abs(angleExtent);

	}


	/**
	 * Compute intersections of the circle arc with a line. Return an array of
	 * Point2D, of size 0, 1 or 2 depending on the distance between circle and
	 * line. If there are 2 intersections points, the first one in the array
	 * is the first one on the line. 
	 */
	public Collection<Point2D> getIntersections(StraightObject2D line) {
		// extract intersection with supporting circle
		Collection<Point2D> points = circle.getIntersections(line);
				
		// if no intersection, return empty array
		if(points.size()==0)
			return points;
		
		// prepare iteration on points
		//double[] angle = new double[points.length];
		Point2D center = circle.getCenter();
		ArrayList<Point2D> list = new ArrayList<Point2D>();

		// iteration for each point
		for(Point2D point : points){
			// angle of current point with horizontal
			double angle = Angle2D.getHorizontalAngle(center, point);
			
			// keep only points on the line, and with angle condition
			if(this.containsAngle(angle) && line.contains(point))
				list.add(point);
		}
		
		// return result;
		return list;
	}

	// ====================================================================
	// methods from interface Shape2D
	
	public double getDistance(java.awt.geom.Point2D p) {
		return getDistance(p.getX(), p.getY());
	}

	public double getDistance(double x, double y) {
		double angle = Angle2D.getHorizontalAngle(circle.xc, circle.yc, x, y);

		if(containsAngle(angle))
			return Math.abs(Point2D.getDistance(circle.xc, circle.yc, x, y)-circle.r);
		else
			return Math.min(
					getFirstPoint().getDistance(x, y),
					getLastPoint().getDistance(x, y));
	}

	/** Always return true */
	public boolean isBounded() {
		return true;
	}

	/**
	 * return a new CircleArc2D. Variables t0 and t1 must be comprised
	 * between 0 and the angle extent of the arc.
	 */
	public CircleArc2D getSubCurve(double t0, double t1){
		// convert position to angle
		t0 = Angle2D.formatAngle(startAngle + t0);
		t1 = Angle2D.formatAngle(startAngle + t1);
		
		// check bounds of angles
		if(!Angle2D.containsAngle(startAngle, startAngle+angleExtent, t0, angleExtent>0))
			t0 = startAngle;
		if(!Angle2D.containsAngle(startAngle, startAngle+angleExtent, t1, angleExtent>0))
			t1 = angleExtent;
		
		// create new arc
		return new CircleArc2D(circle, t0, t1, angleExtent>0);
	}
	
	/**
	 * returns the circle arc which refers to the reversed parent circle,
	 * with same start angle, and with opposite angle extent.
	 */
	public CircleArc2D getReverseCurve(){
		return new CircleArc2D(this.circle.getReverseCurve(), 
				startAngle, -angleExtent);
	}
		
	/**
	 * Clip the circle arc by a box. The result is a CurveSet2D, which 
	 * contains only instances of CircleArc2D. If circle arc is not clipped,
	 * the result is an instance of CurveSet2D with zero curves.
	 */
	@Override
	public CurveSet2D<CircleArc2D> clip(Box2D box) {
		// Clip he curve
		CurveSet2D<SmoothCurve2D> set = box.clipSmoothCurve(this);
		
		// create a new structure for storing result
		CurveSet2D<CircleArc2D> result =
			new CurveSet2D<CircleArc2D> ();
		
		// convert result
		for(Curve2D curve : set.getCurves()){
			if (curve instanceof CircleArc2D)
				result.addCurve((CircleArc2D) curve);
		}
		return result;
	}

	/**
	 * return an instance of EllipseArc2D, or CircleArc2D if transform is a
	 * similarity.
	 */
	@Override
	public EllipseArc2D transform(AffineTransform2D trans) {
		if(!AffineTransform2D.isSimilarity(trans))
			return super.transform(trans);
		
		//System.out.println("transform a circle");
		
		// extract the control points
		Point2D center = circle.getCenter();
		Point2D point1 = this.getFirstPoint();
		Point2D point2 = this.getLastPoint();
		
		// transform each point
		center = (Point2D)center.transform(trans);
		point1 = (Point2D)point1.transform(trans);
		point2 = (Point2D)point2.transform(trans);
		
		// compute new angles
		double angle1 = Angle2D.getHorizontalAngle(center, point1);
		double angle2 = Angle2D.getHorizontalAngle(center, point2);

		// compute factor of transform
		double[] coefs = trans.getCoefficients();
		double factor = Math.sqrt(coefs[0]*coefs[0]+coefs[3]*coefs[3]);
		
		// compute parameters of new circle arc
		double xc = center.getX(), yc = center.getY();
		double r2 = circle.getRadius()*factor;
		double startAngle = angle1;
		double angleExtent = Angle2D.formatAngle(angle2-angle1);
		
		boolean b1 = AffineTransform2D.isDirect(trans);
		boolean b2 = this.isDirect();
		if(b1&!b2 | !b1&b2)
			angleExtent = angleExtent-2*Math.PI;
		
		// return new CircleArc
		return new CircleArc2D(xc, yc, r2, startAngle, angleExtent);
	}

// 	// following are inherited from EllipseArc2D	
//	public java.awt.Rectangle getBounds() {
//		java.awt.geom.Rectangle2D bounds = this.getBounds2D();
//		int xmin = (int) bounds.getMinX();
//		int ymin = (int) bounds.getMinY();
//		int xmax = (int) Math.ceil(bounds.getMaxX());
//		int ymax = (int) Math.ceil(bounds.getMaxY());
//		return new java.awt.Rectangle(xmin, ymin, xmax-xmin, ymax-ymin);
//	}
//
//	/**
//	 * Returns more precise bounds for the shape. Result is an instance of Box2D.
//	 */
//	public java.awt.geom.Rectangle2D getBounds2D() {
//		Point2D p;	double x, y;
//		
//		double xc = circle.xc;
//		double yc = circle.yc;
//		double r  = circle.r;
//
//		p = getFirstPoint(); x = p.getX(); y = p.getY();
//		double xmin = x; double ymin = y;
//		double xmax = x; double ymax = y;
//
//		p = getLastPoint(); x = p.getX(); y = p.getY();
//		xmin = Math.min(xmin, x); ymin = Math.min(ymin, y);
//		xmax = Math.max(xmax, x); ymax = Math.max(ymax, y);		
//
//		if(containsAngle(0)){
//			x = xc+r; y = yc;
//			xmin = Math.min(xmin, x); ymin = Math.min(ymin, y);
//			xmax = Math.max(xmax, x); ymax = Math.max(ymax, y);		
//		}
//
//		if(containsAngle(Math.PI)){
//			x = xc-r; y = yc;
//			xmin = Math.min(xmin, x); ymin = Math.min(ymin, y);
//			xmax = Math.max(xmax, x); ymax = Math.max(ymax, y);		
//		}
//
//		if(containsAngle(Math.PI/2)){
//			x = xc; y = yc+r;
//			xmin = Math.min(xmin, x); ymin = Math.min(ymin, y);
//			xmax = Math.max(xmax, x); ymax = Math.max(ymax, y);		
//		}
//
//		if(containsAngle(3*Math.PI/2)){
//			x = xc; y = yc-r;
//			xmin = Math.min(xmin, x); ymin = Math.min(ymin, y);
//			xmax = Math.max(xmax, x); ymax = Math.max(ymax, y);		
//		}
//		
//		return new Box2D(xmin, ymin, (xmax-xmin), (ymax-ymin));
//	}

	public boolean contains(java.awt.geom.Point2D p) {
		return contains(p.getX(), p.getY());
	}

	public boolean contains(double x, double y) {
		// Check if radius is correct
		if(Math.abs(Point2D.getDistance(circle.xc, circle.yc, x, y)-circle.r)>Shape2D.ACCURACY)
			return false;
		
		// check if angle is contained in interval [startAngle-angleExtent]
		double angle = Angle2D.getHorizontalAngle(circle.xc, circle.yc, x, y);
		if(angleExtent>0)
			return Angle2D.formatAngle(angle-startAngle) <= 
				Angle2D.formatAngle(angleExtent);
		else
			return Angle2D.formatAngle(angle-startAngle) >= 
				Angle2D.formatAngle(angleExtent); 
	}

	public boolean intersects(double x, double y, double w, double h) {
		
		// circle arc contained in the rectangle
		if(new Box2D(x, x+w, y, y+h).contains(circle.xc, circle.yc)) return true;
		
		// if distance of first corner to center lower than radius, then intersect
		if(Point2D.getDistance(x, y, circle.xc, circle.yc)<circle.r) return true;
		
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

	public boolean intersects(java.awt.geom.Rectangle2D r) {
		return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}

	/** Always return false */
	public boolean contains(double arg0, double arg1, double arg2, double arg3) {
		return false;
	}

	/** Always return false */
	public boolean contains(java.awt.geom.Rectangle2D arg0) {
		return false;
	}
	
	public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path){
		double cot = Math.cos(circle.theta);
		double sit = Math.sin(circle.theta);
		double xc = circle.xc;
		double yc = circle.yc;
		double r = circle.r;
		double endAngle = startAngle+angleExtent;
		
		// position to the first point
		path.lineTo((float)(xc+r*Math.cos(startAngle)*cot-r*Math.sin(startAngle)*sit), 
					(float)(yc+r*Math.cos(startAngle)*sit+r*Math.sin(startAngle)*cot));

		if(angleExtent>0)
			for(double t=startAngle; t<endAngle; t+=angleExtent/100)
				path.lineTo((float)(xc+r*Math.cos(t)*cot-r*Math.sin(t)*sit), 
							(float)(yc+r*Math.cos(t)*sit+r*Math.sin(t)*cot));
		else
			for(double t=startAngle; t>endAngle; t+=angleExtent/100)
				path.lineTo((float)(xc+r*Math.cos(t)*cot-r*Math.sin(t)*sit), 
							(float)(yc+r*Math.cos(t)*sit+r*Math.sin(t)*cot));

		// position to the last point
		path.lineTo((float)(xc+r*Math.cos(endAngle)*cot-r*Math.sin(endAngle)*sit), 
					(float)(yc+r*Math.cos(endAngle)*sit+r*Math.sin(endAngle)*cot));

		return path;
	}
	
	
//	/* (non-Javadoc)
//	 * @see java.awt.Shape#getPathIterator(java.awt.geom.AffineTransform, double)
//	 */
//	public java.awt.geom.GeneralPath getInnerPath() {
//		
//		// Creates the path
//		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
//		
//		double cot = Math.cos(circle.theta);
//		double sit = Math.sin(circle.theta);
//		double xc = circle.xc;
//		double yc = circle.yc;
//		double r = circle.r;
//		double endAngle = startAngle+angleExtent;
//		
//		// position to the first point
//		path.lineTo((float)(xc+r*Math.cos(startAngle)*cot-r*Math.sin(startAngle)*sit), 
//					(float)(yc+r*Math.cos(startAngle)*sit+r*Math.sin(startAngle)*cot));
//
//		if(angleExtent>0)
//			for(double t=startAngle; t<endAngle; t+=angleExtent/100)
//				path.lineTo((float)(xc+r*Math.cos(t)*cot-r*Math.sin(t)*sit), 
//							(float)(yc+r*Math.cos(t)*sit+r*Math.sin(t)*cot));
//		else
//			for(double t=startAngle; t>endAngle; t+=angleExtent/100)
//				path.lineTo((float)(xc+r*Math.cos(t)*cot-r*Math.sin(t)*sit), 
//							(float)(yc+r*Math.cos(t)*sit+r*Math.sin(t)*cot));
//		
//		return path;
//	}

	/**
	 * Get the path, with moveTo().
	 * @return the general path for this circle
	 */
	protected java.awt.geom.GeneralPath getGeneralPath(){
		// Creates the path
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		Point2D point = getFirstPoint();
		path.moveTo((float) point.getX(), (float) point.getY());
		this.appendPath(path);
		return path;
	}
	
	/** 
	 * Return pathiterator for this circle arc.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans){
		return getGeneralPath().getPathIterator(trans);
	}

	/**
	 * Return pathiterator for this circle arc.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans, double flatness){
		return getGeneralPath().getPathIterator(trans, flatness);
	}
	

 	/**
	 * two circle arc arre equal if the have same center, same radius, same 
	 * starting and ending angles, and same orientation.
	 */
	public boolean equals(Object obj){
		if(!(obj instanceof EllipseArc2D))
			return false;

		if(!(obj instanceof CircleArc2D))
			return super.equals(obj);
		
		CircleArc2D arc = (CircleArc2D) obj;
		// test whether supporting ellipses have same support
		if(Math.abs(circle.xc-arc.circle.xc)>Shape2D.ACCURACY) return false;
		if(Math.abs(circle.yc-arc.circle.yc)>Shape2D.ACCURACY) return false;
		if(Math.abs(circle.r1-arc.circle.r1)>Shape2D.ACCURACY) return false;
		if(Math.abs(circle.r2-arc.circle.r2)>Shape2D.ACCURACY) return false;
		if(Math.abs(circle.theta-arc.circle.theta)>Shape2D.ACCURACY) return false;
		
		// test is angles are the same
		if(Math.abs(Angle2D.formatAngle(startAngle)-Angle2D.formatAngle(arc.startAngle))>
				Shape2D.ACCURACY) return false;
		if(Math.abs(Angle2D.formatAngle(angleExtent)-Angle2D.formatAngle(arc.angleExtent))>
				Shape2D.ACCURACY) return false;
		
		// if no difference, this is the same
		return true;
	}

}
