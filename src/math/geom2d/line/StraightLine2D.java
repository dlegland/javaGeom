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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.ContinuousBoundary2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.transform.AffineTransform2D;

// Imports

/**
 * Representation of straigth lines. Such lines can	be constructed using two points,
 * a point and a parallel line or straight object, or with coefficient of the 
 * cartesian equation.
 */
public class StraightLine2D extends LineArc2D implements ContinuousBoundary2D{

	//TODO: should be better not to override LineArc2D -> faster computation

	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables
	
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
		this(point.getX(), point.getY(), direction.getDx(), direction.getDy());
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
		super(xp, yp, dx, dy, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
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
	// static methods
	
	public final static StraightLine2D createStraightLine2D(java.awt.geom.Point2D p1, java.awt.geom.Point2D p2){
		return new StraightLine2D(p1, p2);
	}

	/**
	 * *
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

//	// ===================================================================
//	// accessors
//
//	/** Always returns false, because a line is not bounded.*/
//	public  boolean isBounded(){return false;}
//
//	public boolean equals(Object obj){
//		if(!(obj instanceof StraightLine2D)) return false;
//		return isColinear((StraightLine2D) obj);
//	}
//
//	/**
//	 * Returns the part of the line visible inside the rectangle 
//	 * <code>rect<\code>. The result is either an Edge2D, if line is
//	 * partially visible, or null, if line does not intersect the rectangle.
//	 * The resulting Edge2D is bounded by the rectangle.
//	 */
//	public Shape2D getClippedShape(java.awt.geom.Rectangle2D rect){
//		// get vertices of rectangle
//		double x = rect.getX();
//		double y = rect.getY();
//		double w = rect.getWidth();
//		double h = rect.getHeight();
//
//		// case of vertical lines
//		if(Math.abs(dx)<Shape2D.ACCURACY)
//			if(x0>=x && x0<=x+w)
//				return new LineSegment2D(x0, y, x0, y0+h);
//			else
//				return null;		
//		
//		// case of horizontal lines
//		if(Math.abs(dy)<Shape2D.ACCURACY)
//			if(y0>=y && y0<=y+h)
//				return new LineSegment2D(x, y0, x+w, y0);
//			else
//				return null;
//		
//		double t1 = (y-y0)/dy;
//		double t2 = (x+w-x0)/dx;
//		double t3 = (y+h-y0)/dy;
//		double t4 = (x-x0)/dx;
//		
//		// sort the positions of the 4 points in ascending order
//		double tmp;
//		
//		if(t1>t2){tmp=t1;t1=t2;t2=tmp;}
//		if(t3>t4){tmp=t3;t3=t4;t4=tmp;}
//		if(t2>t3){tmp=t2;t2=t3;t3=tmp;}
//		if(t1>t2){tmp=t1;t1=t2;t2=tmp;}
//		if(t3>t4){tmp=t3;t3=t4;t4=tmp;}
//		if(t2>t3){tmp=t2;t2=t3;t3=tmp;}
//		return new LineSegment2D(getPoint(t2), getPoint(t3));
//	}
//
//	/** 
//	 * Always returns Double.POSITIVE_INFINITY, since a line is infinite...
//	 */
//	public double getLength(){
//		return Double.POSITIVE_INFINITY;
//	}
//
//	public double getViewAngle(Point2D point){
//		if (super.isPositivelyOriented(point)) return Math.PI;
//		else return -Math.PI;
//	}
//	
//	/** 
//	 * Returns the parameter of the first point of the line, which is always Double.NEGATIVE_INFINITY.
//	 */
//	public double getT0(){
//		return Double.NEGATIVE_INFINITY;
//	}
//
//	/** 
//	 * Returns the parameter of the last point of the line, which is always Double.POSITIVE_INFINITY.
//	 */
//	public double getT1(){
//		return Double.POSITIVE_INFINITY;
//	}
//
//	/**
//	 * Gets the point specified with the parametric representation of the line.
//	 */
//	public Point2D getPoint(double t){
//		return new Point2D(x0 + dx*t, y0+dy*t);
//	}
//
//	/**
//	 * Gets the point specified with the parametric representation of the line.
//	 */
//	public Point2D getPoint(double t, Point2D point){
//		if(point==null) point = new Point2D();
//		point.setLocation(x0 + dx*t, y0 + dy*t);
//		return point;
//	}
//
//
//	/** 
//	 * Returns true if the point (x, y) lies on the line, with precision given 
//	 * by Shape2D.ACCURACY.
//	 */
//	public boolean contains(double x, double y){
//		return super.contains(x, y);
//	}
//
//
//	/** 
//	 * Returns true if the point p lies on the line, with precision given by 
//	 * Shape2D.ACCURACY.
//	 */
//	public boolean contains(java.awt.geom.Point2D p){
//		return super.contains(p.getX(), p.getY());
//	}
//
//	/**
//	 * Returns null, because an infinite line has no bounds.
//	 */
//	public java.awt.Rectangle getBounds(){
//		return null;
//	}
//	
//	/**
//	 * Returns null, because an infinite line has no bounds.
//	 */
//	public java.awt.geom.Rectangle2D getBounds2D(){
//		return null;
//	}
//	
	/** 
	 * Return a new Straight line, parallel to another straigth object (ray,
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
	// mutators

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
	 * clip a continuous smooth curve by the half-plane defined by this line.
	 */
	public CurveSet2D<SmoothCurve2D> clipSmoothCurve(SmoothCurve2D curve){
		
		// get the list of intersections with the line
		ArrayList<Point2D> list = new ArrayList<Point2D>();				
		list.addAll(curve.getIntersections(this));
				
		// convert list to point array, sorted with respect to their position
		// on the curve, but do not add tangent points with curvature greater
		// than 0
		SortedSet<java.lang.Double> set = new TreeSet<java.lang.Double>();
		double position;
		Vector2D vector = this.getVector();
		for(Point2D point : list){
			// get position of intersection on the curve (use project to avoid
			// round-off problems)
			position = curve.project(point);
			
			// Condition of colinearity with direction vector of line
			Vector2D tangent = curve.getTangent(position);
			if(Vector2D.isColinear(tangent, vector)){			
				// condition on the curvature (close to zero = cusp point)
				double curv = curve.getCurvature(position);
				if(Math.abs(curv)>Shape2D.ACCURACY) continue;
			}
			set.add(new java.lang.Double(position));
		}		
				
		// Create CurveSet2D for storing the result
		CurveSet2D<SmoothCurve2D> res = new CurveSet2D<SmoothCurve2D>();		
		
		// extract first point of the curve, or a point arbitrarily far
		Point2D point1 = curve.getFirstPoint();
		if(Double.isInfinite(curve.getT0()))
			point1 = curve.getPoint(-1000);

		// Extract first valid intersection point, if it exists
		double pos1, pos2;
		Iterator<java.lang.Double> iter = set.iterator();
			
		// if no intersection point, the curve is either totally inside
		// or totally outside the box
		if(!iter.hasNext()){
			// Find a point on the curve and not on the line
			// First tries with first point
			double t0 = curve.getT0(); 
			if(t0==Double.NEGATIVE_INFINITY) t0=-100;
			while(this.contains(point1)){
				double t1 = curve.getT1(); 
				if(t1==Double.POSITIVE_INFINITY) t1=+100;
				t0 = (t0+t1)/2;
				point1 = curve.getPoint(t0);
			}
			if(this.getSignedDistance(point1)<0)
				res.addCurve(curve);
			return res;
		}
		
		// different behavior depending if first point lies inside the box
		if(this.getSignedDistance(point1)<0 && !this.contains(point1)){
			pos1 = iter.next().doubleValue();
			res.addCurve(curve.getSubCurve(curve.getT0(), pos1));
		}
		
		// add the portions of curve between couples of intersections
		while(iter.hasNext()){
			pos1 = iter.next().doubleValue();
			if(iter.hasNext())
				pos2 = iter.next().doubleValue();
			else
				pos2 = curve.getT1();
			res.addCurve(curve.getSubCurve(pos1, pos2));
		}
		
		return res;
	}

	// ===================================================================
	// methods specific to Boundary2D interface
	
	public Collection<ContinuousBoundary2D> getBoundaryCurves(){
		ArrayList<ContinuousBoundary2D> list = new ArrayList<ContinuousBoundary2D>(1);
		list.add(this);
		return list;
	}

	/**
	 * Returns the straight line with same origin but with opposite
	 * direction vector.
	 */
	public StraightLine2D getReverseCurve(){
		return new StraightLine2D(this.x0, this.y0, -this.dx, -this.dy);
	}


//	// ===================================================================
//	// general methods

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

}