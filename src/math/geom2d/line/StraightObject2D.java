/* File StraightObject2D.java 
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

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.SmoothCurve2D;


// Imports

/**
 * Base class for straight curves, such as straight lines, rays, or edges.<p><p>
 * Internal representation of straight objects is parametric :
 * (x0, y0) is a point in the object, and (dx, dy) is a direction vector of the line.<p>
 * If the line is defined by two point, we can set (x0,y0) to the first point,
 * and (dx,dy) to the vector (p1, p2). <p>
 * Then, coordinates for a point (x,y) such as x=x0+t*dx and y=y0+t=dy,
 * t between 0 and 1 give a point inside p1 and p2, t<0 give a point 'before'
 * p1, and t>1 give a point 'after' p2, so it is convenient to easily manage
 * edges, rays and straight lines.<p>
 * At the difference of LineArc2D, this class is not necessarily connex (it
 * can be the union of several colinear line segments).
 */
//TODO: rename as LinearShape2D?
public abstract class StraightObject2D implements SmoothCurve2D{

	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables

	/**
	 * Coordinates of starting point of the line
	 */
	protected double x0, y0;
	
	/**
	 * Direction vector of the line. dx and dy should not be both zero.
	 */
	protected double dx, dy;	
	
	// ===================================================================
	// static methods

	/**
	 * Returns the unique intersection of two straight objects. If intersection doesn't
	 * exist (parallel lines), return null.
	 */
	public final static Point2D getIntersection(StraightObject2D l1, StraightObject2D l2){
		double t = ((l1.y0-l2.y0)*l2.dx - (l1.x0-l2.x0)*l2.dy ) / 
			(l1.dx*l2.dy - l1.dy*l2.dx) ;
		return new Point2D(l1.x0+t*l1.dx, l1.y0+t*l1.dy);
	}

	/**
	 * Test if the two linear objects are located on the same straight line.
	 */
	public final static boolean isColinear(StraightObject2D line1, StraightObject2D line2){		
		// test if the two lines are parallel
		if(Math.abs(line1.dx*line2.dy - line1.dy*line2.dx)>ACCURACY) return false;
		
		// test if the two lines share at least one point (see contains() method
		// for details on tests)
		return(Math.abs((line2.y0-line1.y0)*line2.dx-(line2.x0-line1.x0)*line2.dy)/
			Math.sqrt(line2.dx*line2.dx+line2.dy*line2.dy)<Shape2D.ACCURACY);
	}

	/**
	 * Test if the two linear objects are parallel.
	 */
	public final static boolean isParallel(StraightObject2D line1, StraightObject2D line2){		
		return(Math.abs(line1.dx*line2.dy - line1.dy*line2.dx)<ACCURACY);
	}

//	/** 
//	 * Returns the horizontal angle formed by the line joining the two given points.
//	 */
//	public final static double getHorizontalAngle(Point2D p1, Point2D p2){
//		return (Math.atan2(p2.getY()-p1.getY(), p2.getX()-p1.getX()) + 2*Math.PI)%(2*Math.PI);
//	}
//
//	/** 
//	 * Returns the horizontal angle formed by the line joining the two given points.
//	 */
//	public final static double getHorizontalAngle(double x1, double y1, double x2, double y2){
//		return (Math.atan2(y2-y1, x2-x1) + 2*Math.PI)%(2*Math.PI);
//	}
//
//
//	/**
//	 * Gets angle between two (directed) straight objects. Result is given in radians, 
//	 * between 0 and 2*PI.
//	 */
//	public final static double getAngle(StraightObject2D obj1, StraightObject2D obj2){
//		double angle1 = obj1.getHorizontalAngle();
//		double angle2 = obj2.getHorizontalAngle();
//		return (angle2-angle1+Math.PI*2)%(Math.PI*2);
//	}
//	
//	public final static double getAngle(Point2D p1, Point2D p2, Point2D p3){
//		double angle1 = getHorizontalAngle(p2, p1);
//		double angle2 = getHorizontalAngle(p2, p3);
//		return (angle2-angle1+Math.PI*2)%(Math.PI*2);
//	}
//
//	public final static double getAngle(double x1, double y1, double x2, double y2, double x3, double y3){
//		double angle1 = getHorizontalAngle(x2, y2, x1, y1);
//		double angle2 = getHorizontalAngle(x2, y2, x1, y1);
//		return (angle2-angle1+Math.PI*2)%(Math.PI*2);
//	}
//	
//	
//	public final static double getAbsoluteAngle(Point2D p1, Point2D p2, Point2D p3){
//		double angle1 = new LineSegment2D(p2, p1).getHorizontalAngle();
//		double angle2 = new LineSegment2D(p2, p3).getHorizontalAngle();
//		angle1 = (angle2-angle1+Math.PI*2)%(Math.PI*2);
//		if(angle1<Math.PI) return angle1;
//		else return Math.PI*2-angle1;
//	}
//
//	public final static double getAbsoluteAngle(double x1, double y1, double x2, double y2, 
//		double x3, double y3){
////			double angle1 = new LineSegment2D(x2, y2, x1, y1).getHorizontalAngle();
////			double angle2 = new LineSegment2D(x2, y2, x3, y3).getHorizontalAngle();
//			double angle1 = getHorizontalAngle(x2, y2, x1, y1);
//			double angle2 = getHorizontalAngle(x2, y2, x3, y3);
//			angle1 = (angle2-angle1+Math.PI*2)%(Math.PI*2);
//			if(angle1<Math.PI) return angle1;
//			else return Math.PI*2-angle1;
//	}
	
	// ===================================================================
	// accessors

	
	public Point2D getOrigin(){
		return new Point2D(x0, y0);
	}

	public Vector2D getVector(){
		return new Vector2D(dx, dy);
	}

	public boolean isColinear(StraightObject2D line){		
		// test if the two lines are parallel
		if(!isParallel(line)) return false;
		
		// test if the two lines share at least one point (see contains() method
		// for details on tests)
		if(Math.abs(dx)>Math.abs(dy)){
			if(Math.abs((line.x0-x0)*dy/dx+y0-line.y0) > Shape2D.ACCURACY) return false;
			else return true;
		}else{
			if(Math.abs((line.y0-y0)*dx/dy+x0-line.x0) > Shape2D.ACCURACY) return false;
			else return true;
		}
	}

	/**
	 * Test if the this object is parallel to the given one.
	 */
	public boolean isParallel(StraightObject2D line){		
		return(Math.abs(dx*line.dy - dy*line.dx)<Shape2D.ACCURACY);
	}
	
	/** 
	 * Always returns false, because we can not come back to starting point if we
	 * always go straight ...
	 */
	public boolean isClosed(){
		return false;
	}

	/** 
	 * Returns a set of smooth curves. Actually, return the curve itself.
	 */
	public Collection<? extends SmoothCurve2D> getSmoothPieces() {
		ArrayList<StraightObject2D> list = new ArrayList<StraightObject2D>(1);
		list.add(this);
		return list;
	}

	/**
	 * Get the distance of the StraightObject2d to the given point. This method is not
	 * designed to be used directly, because StraightObject2D is an abstract class, 
	 * but it can be used by subclasses to help computations.
	 */
	public double getDistance(Point2D p){
		return getDistance(p.getX(), p.getY());
	}

	/**
	 * Get the distance of the StraightObject2d to the given point. This method is not
	 * designed to be used directly, because StraightObject2D is an abstract class, 
	 * but it can be used by subclasses to help computations.
	 * @param x , y : position of point
	 * @return distance between this object and the point (x,y)
	 */
	public double getDistance(double x, double y){
		return Math.abs((y-y0)*dx-(x-x0)*dy)/Math.sqrt(dx*dx+dy*dy);
	}

	/**
	 * Get the signed distance of the StraightObject2d to the given point. The signed
	 * distance is positive if point lies 'to the right' of the line, when moving in
	 * the direction given by direction vector. This method is not
	 * designed to be used directly, because StraightObject2D is an abstract class, 
	 * but it can be used by subclasses to help computations.
	 */
	public double getSignedDistance(java.awt.geom.Point2D p){
		return getSignedDistance(p.getX(), p.getY());
	}

	/**
	 * Get the signed distance of the StraightObject2d to the given point. The signed
	 * distance is positive if point lies 'to the right' of the line, when moving in
	 * the direction given by direction vector. This method is not
	 * designed to be used directly, because StraightObject2D is an abstract class, 
	 * but it can be used by subclasses to help computations.
	 */
	public double getSignedDistance(double x, double y){
		return ((x-x0)*dy-(y-y0)*dx)/Math.sqrt(dx*dx+dy*dy);
	}

	/**
	 * return true if the given point lies to the left of the line when travelling along
	 * the line in the direcion given by its direction vector.
	 * @param p the point to test
	 * @return true if point p lies on the 'left' of the line.
	 */
	public boolean isInside(Point2D p){
		return( (p.getX()-x0)*dy-(p.getY()-y0)*dx < 0);
	}

	/**
	 * Returns the matrix of parametric representation of the line. Result has
	 * the form : <p>
	 * [ x0  dx ] <p>
	 * [ y0  dy ] <p>
	 * It can be easily extended to higher dimensions and/or higher polynomial
	 * forms.
	 */
	public double[][] getParametric(){
		double tab [][] = new double[2][2];
		tab[0][0] = x0;
		tab[0][1] = dx;
		tab[1][0] = y0;
		tab[1][1] = dy;
		return tab;
	}
		
	/** 
	 * Returns the coefficient of the cartesian representation of the line. Cartesian
	 * equation has the form : ax+by+c=0. The returned array is {a, b, c}.
	 */
	public double[] getCartesianEquation(){
		double tab[] = new double[3];
		tab[0] = dy;
		tab[1] = -dx;
		tab[2] = dx*y0 - dy*x0;
		return tab;
	}

	
	/**
	 * Returns two coefficients : the minimal distance between the straight line covering
	 * this object and the origin, and the angle of object with horizontal.
	 */
	public double[] getPolarCoefficients(){
		double tab[] = new double[2];
		double d = getSignedDistance(0, 0);
		tab[0] = Math.abs(d);
		if(d>0) tab[1]=(getHorizontalAngle()+Math.PI)%(2*Math.PI);
		else tab[1]=getHorizontalAngle();
		return tab;
	}

	/**
	 * Returns the polar coefficients, but distance to origin can be negative : this
	 * allows representation of directed lines.
	 */
	public double[] getSignedPolarCoefficients(){
		double tab[] = new double[2];
		tab[0] = getSignedDistance(0, 0);
		tab[1] = getHorizontalAngle();
		return tab;
	}
	
	
	/** 
	 * Gets Angle with axis (O,i), counted counter-clockwise. Result is given between
	 * 0 and 2*pi.
	 */
	public double getHorizontalAngle(){
		return (Math.atan2(dy, dx) + 2*Math.PI)%(2*Math.PI);
	}


	public double getPositionOnLine(Point2D point){
		return getPositionOnLine(point.getX(), point.getY());
	}
	
	/**	 
	 * Compute position on the line, that is the number t such that if the
	 * point belong to the line, it location is given by x=x0+t*dx and  
	 * y=y0+t*dy. <p>
	 * If the point does not belong to the line, the method returns the
	 * position of its projection on the line.
	 */
	public double getPositionOnLine(double x, double y){
		return  ( (y-y0)*dy + (x-x0)*dx ) / (dx*dx + dy*dy) ;
	}

	

	/**
	 * Return the intersection points of the line with the specified curve.
	 * The length of the result array is the number of intersection points.
	 */
	public Collection<Point2D> getIntersections(Curve2D curve){
		// use the method getIntersection(StraightObject), since it is easier
		// to use.
		return curve.getIntersections(this);
	}
	
	/**
	 * Return the intersection points of the curve with the specified line.
	 * The length of the result array is the number of intersection points.
	 */
	public Collection<Point2D> getIntersections(StraightObject2D line){
		
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		
		Point2D point = getIntersection(line);
		if(point==null) return points;

		// return array with the intersection point.
		points.add(point);
		return points;
	}

	
	/**
	 * Returns the unique intersection with a straight object. If intersection doesn't
	 * exist (parallel lines), return null.
	 */
	public Point2D getIntersection(StraightObject2D line){
		// test if two lines are parallel
		if(Math.abs(dx*line.dy - dy*line.dx)<Shape2D.ACCURACY)
			return null;
			
		// compute position on the line
		double t = (  (y0-line.y0)*line.dx - (x0-line.x0)*line.dy ) / 
			(dx*line.dy - dy*line.dx) ;
			
		// compute position of intersection point
		Point2D point = new Point2D(x0+t*dx, y0+t*dy);

		// check if point is inside the bounds of the obejct. This test
		// is left to derivated classes.
		if(contains(point) && line.contains(point)) return point;
		return null;
	}

	/**
	 * Return the projection of point p on the line. The returned point can be used
	 * to compute distance from point to line.
	 * @param p a point outside the line (if point p lies on the line, it is returned)
	 * @return the projection of the point p on the line
	 */
	public Point2D getProjectedPoint(Point2D p){
		return getProjectedPoint(p.getX(), p.getY());
	}
	
	/**
	 * returns one straight line which contains this straight object.
	 * @return the Straight line which contains this object
	 */
	public StraightLine2D getSupportLine(){
		return new StraightLine2D(this);
	}
	
	/**
	 * Return the projection of point p on the line. The returned point can be used
	 * to compute distance from point to line.
	 * @param x : coordinate x of point to be projected
	 * @param y : coordinate y of point to be projected
	 * @return the projection of the point p on the line
	 */
	public Point2D getProjectedPoint(double x, double y){
		if(contains(x, y))return new Point2D(x, y);

		// compute position on the line
		double t = getPositionOnLine(x, y);
			
		// compute position of intersection point
		return new Point2D(x0+t*dx, y0+t*dy);		
	}
	
	/**
	 * Return the symmetric of point p relative to this straight line.
	 * @param p a point outside the line (if point p lies on the line, it is returned)
	 * @return the projection of the point p on the line
	 */
	public Point2D getSymmetric(Point2D p){
		return getSymmetric(p.getX(), p.getY());		
	}
	
	/**
	 * Return the symmetric of point with coordinate (x, y) relative to this
	 * straight line.
	 * @param x : coordinate x of point to be projected
	 * @param y : coordinate y of point to be projected
	 * @return the projection of the point (x,y) on the line
	 */
	public Point2D getSymmetric(double x, double y){
		//if(contains(x, y))return new Point2D(x, y);

		// compute position on the line
		double t = 2*getPositionOnLine(x, y);
			
		// compute position of intersection point
		return new Point2D(2*x0+t*dx-x, 2*y0+t*dy-y);		
	}
	
	/**
	 * Create a straight line parallel to this object, and going through the
	 * given point.
	 * @param point : the point to go through
	 * @return the parallel through the point
	 */
	public StraightLine2D getParallel(Point2D point){
		return new StraightLine2D(point, this.dx, this.dy);		
	}
	
	/**
	 * Create a straight line perpendicular to this object, and going through 
	 * the given point.
	 * @param point the point to go through
	 * @return the perpendicular through the point
	 */
	public StraightLine2D getPerpendicular(Point2D point){
		return new StraightLine2D(point, -this.dy, this.dx);		
	}
	
	
	// ===================================================================
	// mutators


	// ===================================================================
	// general methods

	/** 
	 * Returns true if the point (x, y) lies on the line covering the object, with 
	 * precision given by Shape2D.ACCURACY.
	 */
	public boolean contains(double x, double y){
		return(Math.abs((x-x0)*dy-(y-y0)*dx)/Math.sqrt(dx*dx+dy*dy) < Shape2D.ACCURACY);
	}

	/** Returns false, because a line cannot contain a rectangle.*/
	public boolean contains(double x, double y, double w, double h){
		return false;
	}

	/** Returns false, because a line cannot contain a rectangle.*/
	public boolean contains(java.awt.geom.Rectangle2D r){
		return false;
	}
}