/* file : LineArc2D.java
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
 * Created on 24 déc. 2005
 *
 */
package math.geom2d.line;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Angle2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.ContinuousCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.CurveUtil;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.domain.ContinuousOrientedCurve2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * LineArc2D is a generic class to represent edges, straight lines, and rays.
 * It is defeined as a StraightObject2D : origin point, and direction vector.
 * Moreover, two internal variables t0 and t1 define the limit of the object
 * (with t0<t1).
 * 
 * t0=0 and t1=1: this is an edge. t0=-inf and t1=inf: this is a straight
 * line. t0=0 and t1=inf: this is a ray.
 * @author dlegland
 */
public class LineArc2D extends StraightObject2D 
	implements SmoothCurve2D, ContinuousOrientedCurve2D{

	protected double t0=0;
	protected double t1=1;
	
	
	// ===================================================================
	// Constructors
	
	/**
	 * @param point1 the point located at t=0
	 * @param point2 the point located at t=1
	 * @param t0 the lower bound of line arc parameterization
	 * @param t1 the upper bound of line arc parameterization
	 */
	public LineArc2D(Point2D point1, Point2D point2, double t0, double t1) {
		this(point1.getX(), point1.getY(), point2.getX(), point2.getY(), t0, t1);
	}

	/**
	 * Construct a line arc contained in the same straight line as first
	 * argument, with bounds of arc given by t0 and t1
	 * @param line an object defining the supporting line
	 * @param t0 the lower bound of line arc parameterization
	 * @param t1 the upper bound of line arc parameterization
	 */
	public LineArc2D(StraightObject2D line, double t0, double t1) {
		this(line.x0, line.y0, line.dx, line.dy, t0, t1);
	}
	
	/**
	 * Construcion by copy of another line arc
	 * @param line the line to copy
	 */
	public LineArc2D(LineArc2D line) {
		this(line.x0, line.y0, line.dx, line.dy, line.t0, line.t0);
	}

	
	/**
	 * Construct a line arc by the coordinate of two points and two positions
	 * on the line.
	 * @param x1 the x-coordinate of the first point
	 * @param y1 the y-coordinate of the first point
	 * @param x2 the x-coordinate of the second point
	 * @param y2 the y-coordinate of the second point
	 * @param t0 the starting position of the arc
	 * @param t1 the ending position of the arc
	 */
	public LineArc2D(double x1, double y1, double dx, double dy, double t0, double t1) {
		this.x0 = x1;
		this.y0 = y1;
		this.dx = dx;
		this.dy = dy;
		this.t0=t0;
		this.t1=t1;
	}
	

	// ===================================================================
	// methods specific to LineArc2D
	
	/** 
	 * Returns the length of the edge.
	 */
	public double getLength(){
		if(t0!=Double.NEGATIVE_INFINITY && t1!=Double.POSITIVE_INFINITY)
			return getPoint1().getDistance(getPoint2());
		else
			return Double.POSITIVE_INFINITY;
	}
	
	/**
	 * Return the first point of the edge. In the case of a line, or a ray
	 * starting from -infinity, returns Point2D.INFINITY_POINT.
	 * @return the first point of the arc
	 */
	public Point2D getPoint1(){
		if(t0!=Double.NEGATIVE_INFINITY)
			return new Point2D(x0+t0*dx, y0+t0*dy);
		else
			return Point2D.INFINITY_POINT;
	}
	
	/**
	 * Return the last point of the edge. In the case of a line, or a ray
	 * ending at infinity, returns Point2D.INFINITY_POINT.
	 * @return the last point of the arc.
	 */
	public Point2D getPoint2(){
		if(t1!=Double.POSITIVE_INFINITY)
			return new Point2D(x0+t1*dx, y0+t1*dy);
		else
			return Point2D.INFINITY_POINT;
	}
	
	public double getX1(){
		if(t0!=Double.NEGATIVE_INFINITY)
			return x0+t0*dx;
		else
			return Double.NEGATIVE_INFINITY;
	}
	
	public double getY1(){
		if(t0!=Double.NEGATIVE_INFINITY)
			return y0+t0*dy;
		else
			return Double.NEGATIVE_INFINITY;
	}
	
	public double getX2(){
		if(t1!=Double.POSITIVE_INFINITY)
			return x0+t1*dx;
		else
			return Double.POSITIVE_INFINITY;
	}
	
	public double getY2(){
		if(t1!=Double.POSITIVE_INFINITY)
			return y0+t1*dy;
		else
			return Double.POSITIVE_INFINITY;
	}

	// ===================================================================
	// methods of SmoothCurve2D interface
	
	public Vector2D getTangent(double t){
		return new Vector2D(dx, dy);
	}

	/**
	 * returns 0 as every straight object.
	 */
	public double getCurvature(double t){
		return 0.0;
	}
	
	
	// ===================================================================
	// methods of ContinuousCurve2D interface
	
	public Polyline2D getAsPolyline(int n){
		Point2D[] points = new Point2D[n+1];
		double t0 = this.getT0();
		double t1 = this.getT1();
		double dt = (t1-t0)/n;
		for(int i=0; i<n; i++)
			points[i] = this.getPoint((double)i*dt + t0);
		return new Polyline2D(points);
	}

	/** 
	 * Returns an array containing the curve itself.
	 */
	public Collection<? extends SmoothCurve2D> getSmoothPieces() {
		ArrayList<LineArc2D> list = new ArrayList<LineArc2D>(1);
		list.add(this);
		return list;
	}

	// ===================================================================
	// methods of OrientedCurve2D interface
	
	public double getWindingAngle(java.awt.geom.Point2D point){

		double angle1, angle2;
		if(t0==Double.NEGATIVE_INFINITY)
			angle1 = Angle2D.getHorizontalAngle(0, 0, -dx, -dy);
		else
			angle1 = Angle2D.getHorizontalAngle(point.getX(), point.getY(), x0+t0*dx, y0+t0*dy);
		
		if(t1==Double.POSITIVE_INFINITY)
			angle2 = Angle2D.getHorizontalAngle(0, 0, dx, dy);
		else
			angle2 = Angle2D.getHorizontalAngle(point.getX(), point.getY(), x0+t1*dx, y0+t1*dy);
		
		if(this.isInside(point)){
			if(angle2>angle1) return angle2 - angle1;
			else return 2*Math.PI - angle1 + angle2;
		}else{
			if(angle2>angle1) return angle2 - angle1 - 2*Math.PI;
			else return angle2 - angle1;
		}
	}

	/**
	 * return true if the given point lies to the left of the line when travaling along
	 * the line in the direcion given by its direction vector.
	 * @param p the point to test
	 * @return true if point p lies on the 'left' of the line.
	 */
	public boolean isInside(java.awt.geom.Point2D p){
		return( (p.getX()-x0)*dy-(p.getY()-y0)*dx < 0);
	}

	// ===================================================================
	// methods of Curve2D interface
	
	/** 
	 * Returns the parameter of the first point of the edge, arbitrarly set to 0.
	 */
	public double getT0(){
		return t0;
	}

	/** 
	 * Returns the parameter of the last point of the edge, arbitraly set to 1.
	 */
	public double getT1(){
		return t1;
	}


	public Point2D getPoint(double t){
		return getPoint(t, new Point2D());
	}

	public Point2D getPoint(double t, Point2D point){
		if(point==null) point = new Point2D();
		if(t<t0) t=t0;
		if(t>t1) t=t1;
		
		if(t==Double.NEGATIVE_INFINITY || t==Double.POSITIVE_INFINITY)
			point.setLocation(Point2D.INFINITY_POINT.getX(), Point2D.INFINITY_POINT.getY());
		else 
			point.setLocation(x0 + dx*t, y0 + dy*t);
		return point;
	}
	
	/**
	 * Return the first point of the edge. In the case of a line, or a ray
	 * starting from -infinity, returns Point2D.INFINITY_POINT.
	 * @return the last point of the arc
	 */
	public Point2D getFirstPoint(){
		if(t0!=Double.NEGATIVE_INFINITY)
			return new Point2D(x0+t0*dx, y0+t0*dy);
		else
			return Point2D.INFINITY_POINT;
	}
	
	/**
	 * Return the last point of the edge. In the case of a line, or a ray
	 * ending at infinity, returns Point2D.INFINITY_POINT.
	 * @return the last point of the arc
	 */
	public Point2D getLastPoint(){
		if(t1!=Double.POSITIVE_INFINITY)
			return new Point2D(x0+t1*dx, y0+t1*dy);
		else
			return Point2D.INFINITY_POINT;
	}
	
	public Collection<Point2D> getSingularPoints(){
		ArrayList<Point2D> list = new ArrayList<Point2D>(2);
		if(t0!=Double.NEGATIVE_INFINITY)
			list.add(this.getFirstPoint());
		if(t1!=Double.POSITIVE_INFINITY)
			list.add(this.getLastPoint());
		return list;
	}

	/**
	 * Gets the position of the point on the line arc.
	 * If point belongs to the line, this position is defined by the ratio:<p>
	 * <code> t = (xp - x0)/dx <\code>, or equivalently:<p>
	 * <code> t = (yp - y0)/dy <\code>.<p>
	 * If point does not belong to edge, returns Double.NaN.
	 */
	public double getPosition(Point2D point){
		double pos;
		// uses the direction with the biggest derivative of line arc, 
		// in order to avoid divisions by zero.		
		if(Math.abs(dx)>Math.abs(dy))
			pos = (point.getX()-x0)/dx;
		else
			pos = (point.getY()-y0)/dy;
		
		// return either pos or NaN
		if(pos<t0) return Double.NaN;
		if(pos>t1) return Double.NaN;
		return pos;
	}

	/**
	 * Gets the position of the closest point on the line arc.
	 * If point belongs to the line, this position is defined by the ratio:<p>
	 * <code> t = (xp - x0)/dx <\code>, or equivalently:<p>
	 * <code> t = (yp - y0)/dy <\code>.<p>
	 * If point does not belong to edge, returns t0, or t1, depending on which
	 * one is the closest. 
	 */
	public double project(Point2D point){
		double pos;
		// uses the direction with the biggest derivative of line arc, 
		// in order to avoid divisions by zero.		
		if(Math.abs(dx)>Math.abs(dy))
			pos = (point.getX()-x0)/dx;
		else
			pos = (point.getY()-y0)/dy;
		
		// Bounds between t0 and t1
		return Math.min(Math.max(pos, t0), t1);
	}

	/**
	 * Returns the line arc which have the same trace, but has the inverse
	 * parametrization.
	 */
	public LineArc2D getReverseCurve(){
		return new LineArc2D(x0, y0, -dx, -dy, -t1, -t0);
	}

	public Collection<ContinuousCurve2D> getContinuousCurves() {
		ArrayList<ContinuousCurve2D> list = new ArrayList<ContinuousCurve2D>(1);
		list.add(this);
		return list;
	}

	/** Return a new LineArc2D, which is the portion of the linearc delimited
	 * by parameters t0 and t1.
	 */
	public LineArc2D getSubCurve(double t0, double t1){
		t0 = Math.max(t0, this.t0);
		t1 = Math.min(t1, this.t1);
		return new LineArc2D(this, t0, t1);
	}
	

	// ===================================================================
	// methods of Shape2D interface
	
	/** return true if both t0 and t1 are different from infinity.*/
	public boolean isBounded(){
		if(t1==Double.POSITIVE_INFINITY) return false;
		if(t0==Double.NEGATIVE_INFINITY) return false;
		return true;		
	}
	
	public boolean isEmpty(){
		return false;
	}

	/**
	 * Get the distance of the point (x, y) to this object.
	 */
	public double getDistance(java.awt.geom.Point2D p){
		return getDistance(p.getX(), p.getY());
	}
	
	/**
	 * Get the distance of the point (x, y) to this object.
	 */
	public double getDistance(double x, double y){
		Point2D proj = super.getProjectedPoint(x, y);
		if(contains(proj)) return proj.distance(x, y);
		double d1 = Math.sqrt((x0+t0*dx-x)*(x0+t0*dx-x) + (y0+t0*dy-y)*(y0+t0*dy-y));
		double d2 = Math.sqrt((x0+t1*dx-x)*(x0+t1*dx-x) + (y0+t1*dy-y)*(y0+t1*dy-y));
		return Math.min(d1, d2);
	}

//	/**
//	 * Return either an instance of LineSegment2D, representing the visible
//	 * portion of the object inside the given rectangle, or a null pointer.
//	 */
//	public Shape2D getClippedShape(Box2D box){		
//		// get dimension of rectangle
//		double x = box.getMinX();
//		double y = box.getMinY();
//		double tmp;
//
//		double tvmin = Double.NEGATIVE_INFINITY;
//		double tvmax = Double.POSITIVE_INFINITY;
//		double thmin = Double.NEGATIVE_INFINITY;
//		double thmax = Double.POSITIVE_INFINITY;
//		
//		// case of vertical lines
//		if(Math.abs(dy)>Shape2D.ACCURACY){
//			thmin = (y-y0)/dy;
//			thmax = (y+box.getHeight()-y0)/dy;
//			if(thmax<thmin){tmp=thmin; thmin=thmax; thmax=tmp;}
//			
//			thmin = Math.max(thmin, t0);
//			thmax = Math.min(thmax, t1);
//		}
//		
//		// case of horizontal lines
//		if(Math.abs(dx)>Shape2D.ACCURACY){
//			tvmin = (x-x0)/dx;
//			tvmax = (x+box.getWidth()-x0)/dx;
//			if(tvmax<tvmin){tmp=tvmin; tvmin=tvmax; tvmax=tmp;}
//			
//			tvmin = Math.max(tvmin, t0);
//			tvmax = Math.min(tvmax, t1);
//		}
//
//		double tmin = Math.max(tvmin, thmin);
//		double tmax = Math.min(tvmax, thmax);
//		
//		if(tmin<tmax)
//			return new LineSegment2D(getPoint(tmin), getPoint(tmax));
//		else
//			return null;
//	}

	/**
	 * Clip the circle arc by a box. The result is an instance of
	 * CurveSet2D<LineArc2D>, which 
	 * contains only instances of LineArc2D. If the ellipse arc is not
	 * clipped, the result is an instance of
	 * CurveSet2D<LineArc2D> which contains 0 curves.
	 */
	public CurveSet2D<? extends LineArc2D> clip(Box2D box) {
		// Clip the curve
		CurveSet2D<Curve2D> set = CurveUtil.clipCurve(this, box);
		
		// Stores the result in appropriate structure
		CurveSet2D<LineArc2D> result =
			new CurveSet2D<LineArc2D> ();
		
		// convert the result
		for(Curve2D curve : set.getCurves()){
			if (curve instanceof LineArc2D)
				result.addCurve((LineArc2D) curve);
		}
		return result;
	}


	public Box2D getBoundingBox(){
		return new Box2D(x0+t0*dx, x0+t1*dx, y0+t0*dy, y0+t1*dy);
	}
	
	// ===================================================================
	// methods of Shape interface
	
	public boolean contains(java.awt.geom.Point2D pt){
		return contains(pt.getX(), pt.getY());
	}

	public boolean contains(double xp, double yp){
		if(!super.contains(xp, yp)) return false;
		
		// compute position on the line
		double t = getPositionOnLine(xp, yp);

		if(t-t0<-ACCURACY) return false;
		if(t-t1>ACCURACY) return false;

		return true;
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

	
	public java.awt.geom.GeneralPath getInnerPath(){
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.lineTo((float)(x0+t0*dx), (float)(y0+t0*dy));
		path.lineTo((float)(x0+t1*dx), (float)(y0+t1*dy));
		return path;		
	}
	
	/**
	 * append a line to the current path. If t0 or t1 is infinite, 
	 * does not append anything.
	 * @param path the path to modify
	 * @return the modified path
	 */
	public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path){
		if(t0==Double.NEGATIVE_INFINITY) return path;
		if(t1==Double.POSITIVE_INFINITY) return path;
		path.lineTo((float)getX1(), (float)getY1());
		path.lineTo((float)getX2(), (float)getY2());
		return path;
	}

	
	/** 
	 * Return pathiterator for this line arc.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform t){
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo((float)(x0+t0*dx), (float)(y0+t0*dy));
		path.lineTo((float)(x0+t1*dx), (float)(y0+t1*dy));
		return path.getPathIterator(t);
	}

	/** 
	 * Return pathiterator for this line arc.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform t, double flatness){
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo((float)(x0+t0*dx), (float)(y0+t0*dy));
		path.lineTo((float)(x0+t1*dx), (float)(y0+t1*dy));
		return path.getPathIterator(t, flatness);
	}
	
	/**
	 * Tests if the Line intersects the interior of a specified rectangular area.
	 */
	public boolean intersects(double x, double y, double w, double h){
		return clip(new Box2D(x, x+w, y, y+h)).isEmpty();
	}

	/**
	 * Tests if the Line intersects the interior of a specified rectangle2D.
	 */
	public boolean intersects(java.awt.geom.Rectangle2D r){
		return !clip(new Box2D(r)).isEmpty();
	}

	public LineArc2D transform(AffineTransform2D trans){
		double[] tab = trans.getCoefficients();
		double x1 = x0*tab[0] + y0*tab[1] + tab[2];
		double y1 = x0*tab[3] + y0*tab[4] + tab[5];
		return new LineArc2D(x1, y1, 
			dx*tab[0]+dy*tab[1], dx*tab[3]+dy*tab[4], 
			t0, t1);
	}

	public String toString(){
		return Double.toString(x0).concat(new String(" ")).concat(Double.toString(y0)).concat(
			new String(" ")).concat(Double.toString(dx)).concat(new String(" ")).concat(Double.toString(dy));
	}	

	// ===================================================================
	// methods of Object interface
	
	public boolean equals(Object obj){
		if(!(obj instanceof LineArc2D)) return false;
		return equals((LineArc2D)obj);
	}
	
	/**
	 * Compare two edges, and returns true if they have the two same vertices.
	 * @param arc : the line arcs to compare to.
	 * @return true if extremities of both edges are the same.
	 */
	public boolean equals(LineArc2D arc){
		// First check if two arcs lie on the same line
		if(!this.isColinear(arc)) return false;
		
		// Check limits for straight lines
		if(t0==Double.NEGATIVE_INFINITY && t1==Double.POSITIVE_INFINITY){
			// Check limits
			if(arc.t0!=Double.NEGATIVE_INFINITY) return false;
			if(arc.t1!=Double.POSITIVE_INFINITY) return false;
			return true;
		}
		
		// Check limits for rays
		if(t0==Double.NEGATIVE_INFINITY){
			// Check limits
			if(arc.t0==Double.NEGATIVE_INFINITY)
				return this.getPoint2().getDistance(arc.getPoint2())<Shape2D.ACCURACY;
			if(arc.t1==Double.POSITIVE_INFINITY)
				return this.getPoint2().getDistance(arc.getPoint1())<Shape2D.ACCURACY;
			return false;
		}
		if(t1==Double.POSITIVE_INFINITY){
			// Check limits
			if(arc.t0==Double.NEGATIVE_INFINITY)
				return this.getPoint1().getDistance(arc.getPoint2())<Shape2D.ACCURACY;
			if(arc.t1==Double.POSITIVE_INFINITY)
				return this.getPoint1().getDistance(arc.getPoint1())<Shape2D.ACCURACY;
			return false;
		}
		
		// current line arc is neither a line nor an arc, check that arc is an edge
		if(arc.t0==Double.NEGATIVE_INFINITY || arc.t0==Double.POSITIVE_INFINITY) 
			return false;
		if(arc.t1==Double.NEGATIVE_INFINITY || arc.t1==Double.POSITIVE_INFINITY) 
			return false;
		
		// We still have to test the case of edges
		if(getPoint1().getDistance(arc.getPoint1())<ACCURACY)
			return getPoint2().getDistance(arc.getPoint2())<ACCURACY;
		
		if(getPoint1().getDistance(arc.getPoint2())>ACCURACY) return false;
		if(getPoint2().getDistance(arc.getPoint1())>ACCURACY) return false;
		return true;
	}	
			
}
