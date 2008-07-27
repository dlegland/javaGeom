/* File StraightLine2D.java 
 *
 * Project : Java Geometry Library
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

// package
package math.geom2d.line;

//Imports

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Angle2D;
import math.geom2d.Box2D;
import math.geom2d.Shape2D;
import math.geom2d.UnboundedShapeException;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.domain.ContinuousBoundary2D;
import math.geom2d.transform.AffineTransform2D;


/**
 * Representation of straight lines. Such lines can	be constructed using two points,
 * a point and a parallel line or straight object, or with coefficient of the 
 * Cartesian equation.
 */
public class StraightLine2D extends StraightObject2D implements ContinuousBoundary2D{

	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables
	
	
	// ===================================================================
	// static methods
	
	/**
	 * Creates a straight line going through a point and with a given angle.
	 * 
	 */
	public final static StraightLine2D create(java.awt.geom.Point2D point, double angle){
		return new StraightLine2D(point.getX(), point.getY(), 
				Math.cos(angle), Math.sin(angle));
	}

	/**
	 * Creates a straight line through 2 points.
	 */
	public final static StraightLine2D create(java.awt.geom.Point2D p1, java.awt.geom.Point2D p2){
		return new StraightLine2D(p1, p2);
	}

	/**
	 * Creates a straight line through a point and with a given direction vector.
	 */
	public final static StraightLine2D create(java.awt.geom.Point2D origin, Vector2D direction){
		return new StraightLine2D(origin, direction);
	}

	/**
	 * @deprecated use create(Point2D, Point2D) instead
	 */
	@Deprecated
	public final static StraightLine2D createStraightLine2D(java.awt.geom.Point2D p1, java.awt.geom.Point2D p2){
		return new StraightLine2D(p1, p2);
	}

	/**
	 * Creates a median between 2 points.
	 * @param p1 one point
	 * @param p2 another point
	 * @return the median of points p1 and p2
	 */
	public final static StraightLine2D createMedian2D(java.awt.geom.Point2D p1, java.awt.geom.Point2D p2){
		Point2D mid = Point2D.midPoint(p1, p2);
		StraightLine2D line = StraightLine2D.createStraightLine2D(p1, p2);	
		return StraightLine2D.createOrthogonalLine2D(line, mid);
	}

	/** 
	 * Return a new Straight line going through the given point, and with
	 * the specified direction vector.
	 */
	public final static StraightLine2D createStraightLine2D(java.awt.geom.Point2D point, double dx, double dy){
		return new StraightLine2D(point, dx, dy);
	}


	/** 
	 * Return a new Straight line, parallel to another straight object (ray,
	 * straight line or edge), and going through the given point.
	 */
	public final static StraightLine2D createParallelLine2D(StraightObject2D line, java.awt.geom.Point2D point){
		return new StraightLine2D(line, point);
	}

	/** 
	 * Return a new Straight line, parallel to another straight object (ray,
	 * straight line or edge), and going through the given point.
	 */
	public final static StraightLine2D createParallelLine2D(StraightObject2D line, double d){
		double dd = Math.sqrt(line.dx*line.dx+line.dy*line.dy);
		return new StraightLine2D(line.x0+line.dy*d/dd, line.y0-line.dx*d/dd, line.dx, line.dy);
	}

	/** 
	 * Return a new Straight line, parallel to another straigth object (ray,
	 * straight line or edge), and going through the given point.
	 */
	public final static StraightLine2D createOrthogonalLine2D(StraightObject2D line, Point2D point){
		return new StraightLine2D(point, -line.dy, line.dx);
	}

	/** 
	 * Return a new Straight line, with the given coefficient of the cartesian
	 * equation (a*x + b*y + c = 0).
	 */
	public final static StraightLine2D createCartesianLine2D(double a, double b, double c){
		return new StraightLine2D(a, b, c);
	}

	/**
	 * Compute the intersection point of the two (infinite) lines going through
	 * p1 and p2 for the first one, and p3 and p4 for the second one. Returns null
	 * if two lines are parallel.
	 */
	public final static Point2D getIntersection(
			java.awt.geom.Point2D p1, java.awt.geom.Point2D p2, 
			java.awt.geom.Point2D p3, java.awt.geom.Point2D p4){
		StraightLine2D line1 = new StraightLine2D(p1, p2);
		StraightLine2D line2 = new StraightLine2D(p3, p4);
		return line1.getIntersection(line2);
	}

	
	// ===================================================================
	// constructors
	
	/** Empty constructor: a straight line corresponding to horizontal axis.*/
	public StraightLine2D(){
		this(0,0,1,0);
	}
	
	/** Define a new Straight line going through the two given points. */
	public StraightLine2D(java.awt.geom.Point2D point1, java.awt.geom.Point2D point2){
		this(point1, new Vector2D(point1, point2));
	}

	/** 
	 * Define a new Straight line going through the given point, and with
	 * the specified direction vector.
	 */
	public StraightLine2D(java.awt.geom.Point2D point, Vector2D direction){
		this(point.getX(), point.getY(), direction.getX(), direction.getY());
	}

	/** 
	 * Define a new Straight line going through the given point, and with
	 * the specified direction vector.
	 */
	public StraightLine2D(java.awt.geom.Point2D point, double dx, double dy){
		this(point.getX(), point.getY(), dx, dy);
	}

	/** 
	 * Define a new Straight line going through the given point, and with
	 * the specified direction given by angle.
	 */
	public StraightLine2D(java.awt.geom.Point2D point, double angle){
		this(point.getX(), point.getY(), Math.cos(angle), Math.sin(angle));
	}

	/** 
	 * Define a new Straight line at the same position and with the same direction
	 * than an other straight object (line, edge or ray).
	 */
	public StraightLine2D(StraightObject2D obj){
		this(obj.x0, obj.y0, obj.dx, obj.dy);
	}


	/** 
	 * Define a new Straight line going through the point (xp, yp) and with
	 * the direction dx, dy.
	 */
	public StraightLine2D(double xp, double yp, double dx, double dy){
		super(xp, yp, dx, dy);
	}

	/** 
	 * Define a new Straight line, parallel to another straigth object (ray,
	 * straight line or edge), and going through the given point.
	 */
	public StraightLine2D(StraightObject2D line, java.awt.geom.Point2D point){
		this(point.getX(), point.getY(), line.dx, line.dy);
	}
	
	
	/**
	 * Define a new straight line, from the coefficients of the cartesian equation.
	 * The starting point of the line is then the point of the line closest to the 
	 * origin, and the direction vector has unit norm.
	 */
	public StraightLine2D(double a, double b, double c){
		this(0,0,1,0);
		double d = a*a+b*b;
		x0 = -a*c/d;
		y0 = -b*c/d;
		double theta = Math.atan2(-a, b);
		dx = Math.cos(theta);
		dy = Math.sin(theta);		
	}
	
	// ===================================================================
	// methods specific to StraightLine2D

	public void setLine(double x0, double y0, double dx, double dy){
		this.x0 = x0;
		this.y0 = y0;
		this.dx = dx;
		this.dy = dy;
	}

	public void setPoints(double x1, double y1, double x2, double y2){
		this.x0 = x1;
		this.y0 = y1;
		this.dx = x2-x1;
		this.dy = y2-y1;
	}

	public void setLine(java.awt.geom.Point2D p1, java.awt.geom.Point2D p2){
		this.x0 = p1.getX();
		this.y0 = p1.getY();
		this.dx = p2.getX()-x0;
		this.dy = p2.getY()-y0;
	}

	public void setCartesianEquation(double a, double b, double c){
		dx = -b;
		dy = a;
		x0 = -a*c/(a*a+b*b);
		y0 = -b*c/(a*a+b*b);
	}
	
	/** 
	 * Returns a new Straight line, parallel to another straight object (ray,
	 * straight line or edge), and going through the given point.
	 */
	public StraightLine2D getParallel(java.awt.geom.Point2D point){
		return new StraightLine2D(point, dx, dy);
	}

	/** 
	 * Return the parallel line located at a distance d. Distance is positive
	 * in the 'right' side of the line (outside of the limiting half-plane),
	 * and negative in the 'left' of the line.
	 */
	public StraightLine2D getParallel(double d){
		double dd = Math.sqrt(dx*dx+dy*dy);
		return new StraightLine2D(x0+dy*d/dd, y0-dx*d/dd, dx, dy);
	}

	/** 
	 * Return a new Straight line, parallel to another straigth object (ray,
	 * straight line or edge), and going through the given point.
	 */
	public StraightLine2D getPerpendicular(Point2D point){
		return new StraightLine2D(point, -dy, dx);
	}
	
	
	// ===================================================================
	// methods specific to Boundary2D interface
	
	public Collection<ContinuousBoundary2D> getBoundaryCurves(){
		ArrayList<ContinuousBoundary2D> list = new ArrayList<ContinuousBoundary2D>(1);
		list.add(this);
		return list;
	}


	// ===================================================================
	// methods specific to OrientedCurve2D interface
	
	public double getWindingAngle(java.awt.geom.Point2D point){

		double angle1 = Angle2D.getHorizontalAngle(-dx, -dy);
		double angle2 = Angle2D.getHorizontalAngle(dx, dy);

		if(this.isInside(point)){
			if(angle2>angle1) return angle2 - angle1;
			else return 2*Math.PI - angle1 + angle2;
		}else{
			if(angle2>angle1) return angle2 - angle1 - 2*Math.PI;
			else return angle2 - angle1;
		}
	}

	
	// ===================================================================
	// methods implementing the ContinuousCurve2D interface
	
	/**
	 * Throws an exception when called.
	 */
	public Polyline2D getAsPolyline(int n) {
		throw new UnboundedShapeException();
	}

	
	// ===================================================================
	// methods implementing the Curve2D interface
	
	public Point2D getFirstPoint() {
		return Point2D.INFINITY_POINT;
	}

	public Point2D getLastPoint() {
		return Point2D.INFINITY_POINT;
	}

	/** Returns an empty list of points. */
	public Collection<Point2D> getSingularPoints() {
		return new ArrayList<Point2D>(0);
	}

	/** Returns false, whatever the position.*/
	public boolean isSingular(double pos) {
		return false;
	}

	/** 
	 * Returns the parameter of the first point of the line, which is always Double.NEGATIVE_INFINITY.
	 */
	public double getT0(){
		return Double.NEGATIVE_INFINITY;
	}

	/** 
	 * Returns the parameter of the last point of the line, which is always Double.POSITIVE_INFINITY.
	 */
	public double getT1(){
		return Double.POSITIVE_INFINITY;
	}

	/**
	 * Gets the point specified with the parametric representation of the line.
	 */
	public Point2D getPoint(double t){
		return new Point2D(x0 + dx*t, y0+dy*t);
	}

	/**
	 * Gets the point specified with the parametric representation of the line.
	 */
	public Point2D getPoint(double t, Point2D point){
		if(point==null) point = new Point2D();
		point.setLocation(x0 + dx*t, y0 + dy*t);
		return point;
	}

	/**
	 * Returns the straight line with same origin but with opposite
	 * direction vector.
	 */
	public StraightLine2D getReverseCurve(){
		return new StraightLine2D(this.x0, this.y0, -this.dx, -this.dy);
	}

	public GeneralPath appendPath(GeneralPath path) {
		throw new UnboundedShapeException();
	}


	// ===================================================================
	// methods implementing the Shape2D interface

	/** Always returns false, because a line is not bounded.*/
	public  boolean isBounded(){return false;}

	/**
	 * Get the distance of the point (x, y) to this object.
	 */
	public double getDistance(double x, double y){
		Point2D proj = super.getProjectedPoint(x, y);
		return proj.distance(x, y);
	}

	public Box2D getBoundingBox() {
		if(Math.abs(dx)<0)
			return new Box2D(x0, x0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		if(Math.abs(dy)<0)
			return new Box2D(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, x0, y0);

		return new Box2D(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	/**
	 * Returns the transformed line. The result is still a StraightLine2D.
	 */
	public StraightLine2D transform(AffineTransform2D trans){
		double[] tab = trans.getCoefficients();
		return new StraightLine2D(
			x0*tab[0] + y0*tab[1] + tab[2],
			x0*tab[3] + y0*tab[4] + tab[5],
			dx*tab[0] + dy*tab[1],
			dx*tab[3] + dy*tab[4] );
	}
	
	
	// ===================================================================
	// methods implementing the Shape interface

	/** 
	 * Returns true if the point (x, y) lies on the line, with precision given 
	 * by Shape2D.ACCURACY.
	 */
	public boolean contains(double x, double y){
		return super.supportContains(x, y);
	}

	/** 
	 * Returns true if the point p lies on the line, with precision given by 
	 * Shape2D.ACCURACY.
	 */
	public boolean contains(java.awt.geom.Point2D p){
		return super.supportContains(p.getX(), p.getY());
	}
	
	/** Throws an infiniteShapeException */
	public PathIterator getPathIterator(AffineTransform at) {
		throw new UnboundedShapeException();
	}

	/** Throws an infiniteShapeException */
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		throw new UnboundedShapeException();
	}


	// ===================================================================
	// methods implementing the Object interface

	public boolean equals(Object obj){
		if(!(obj instanceof StraightLine2D)) return false;
		StraightLine2D line = (StraightLine2D) obj;
		if(Math.abs(x0-line.x0)>Shape2D.ACCURACY) return false;
		if(Math.abs(y0-line.y0)>Shape2D.ACCURACY) return false;
		if(Math.abs(dx-line.dx)>Shape2D.ACCURACY) return false;
		if(Math.abs(dy-line.dy)>Shape2D.ACCURACY) return false;
		return true;
	}

}