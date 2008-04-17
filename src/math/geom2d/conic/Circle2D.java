/* file : Circle2D.java
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
 * Created on 30 avr. 2006
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
import math.geom2d.curve.CurveUtil;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.curve.SmoothOrientedCurve2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.line.StraightObject2D;

/**
 * A circle in the plane, defined as the set of points located at an equal
 * distance from the circle center. A circle is a particular ellipse, with
 * first and second axis length equal.
 * @author dlegland
 */
public class Circle2D extends Ellipse2D {	

	/** the radius of the circle.*/
	protected double r = 0;
	
	// ===================================================================
	// Constructors

	/** Empty constructor: center 0,0 and radius 0.*/
	public Circle2D(){
		this(0, 0, 0, true);		
	}
	
	
	/** Create a new circle with specified point center and radius */
	public Circle2D(Point2D center, double radius){
		this(center.getX(), center.getY(), radius, true);
	}
	
	/** Create a new circle with specified center and radius */
	public Circle2D(double xcenter, double ycenter, double radius){
		super(xcenter, ycenter, radius, radius, 0, true);
		this.r = radius;
	}

	/** Create a new circle with specified center and radius */
	public Circle2D(double xcenter, double ycenter, double radius, boolean direct){
		super(xcenter, ycenter, radius, radius, 0, direct);
		this.r = radius;
	}
	

	// ===================================================================
	// methods specific to class Circle2D

	/** Returns perimeter of the circle (equal to 2*PI*radius).*/
	public double getLength(){
		return r*Math.PI*2;
	}
	
	public double getRadius(){
		return r;
	}

	public void setRadius(double radius){
		this.r = radius;
		this.r1 = this.r2 = radius;
	}
	
	public void setCircle(double xc, double yc, double r){
		this.xc = xc;
		this.yc = yc;
		this.r = r;
		this.r1 = r;
		this.r2 = r;
	}

	public void setCircle(Point2D center, double r){
		this.xc = center.getX();
		this.yc = center.getY();
		this.r = r;
		this.r1 = r;
		this.r2 = r;
	}

	// ===================================================================
	// methods of Conic2D

	public int getConicType(){
		return Conic2D.ELLIPSE;
	}

	public boolean isEllipse(){return true;}
	public boolean isParabola(){return false;}
	public boolean isHyperbola(){return false;}
	public boolean isCircle(){return true;}			
	public boolean isStraightLine(){return false;}
	public boolean isTwoLines(){return false;}
	public boolean isPoint(){return false;}

	public boolean isDegenerated(){return false;}


	/**
	 * return cartesian equation of the circle:<p>
	 * <code>(x-xc)^2 + (y-yc)^2 = r^2</code>, giving:<p>
	 * <code>x^2 + 0*x*y + y^2 -2*xc*x -2*yc*y + xc*xc+yc*yc-r*r = 0</code>.
	 */
	public double[] getCartesianEquation(){
		return new double[]{1, 0, 1, -2*xc, -2*yc, xc*xc+yc*yc-r*r};
	}

	/**
	 * Returns the length of the first semi-axis of the ellipse.
	 */
	public double getLength1(){
		return r;
	}

	/**
	 * Returns the length of the second semi-axis of the ellipse.
	 */
	public double getLength2(){
		return r;
	}
	
	/**
	 * Return 0, which is the eccentricity of a circle by definition.
	 */
	public double getEccentricity(){
		return 0;
	}

	/**
	 * Return the first focus, whihc for a circle is the same point as the center.
	 */
	public Point2D getFocus1(){
		return new Point2D(xc, yc);
	}

	/**
	 * Return the second focus, which for a circle is the same point as the center.
	 */
	public Point2D getFocus2(){
		return new Point2D(xc, yc);
	}
	
	// ===================================================================
	// methods of SmoothCurve2D interface
	
	public Vector2D getTangent(double t){
		if(!direct) t = -t;
		double cot = Math.cos(theta);
		double sit = Math.sin(theta);
			
		if (direct)
			return new Vector2D(-r*Math.sin(t)*cot - r*Math.cos(t)*sit,
					  			-r*Math.sin(t)*sit + r*Math.cos(t)*cot);
		else
			return new Vector2D(r*Math.sin(t)*cot + r*Math.cos(t)*sit,
					  			r*Math.sin(t)*sit - r*Math.cos(t)*cot);
	}

	// ===================================================================
	// methods of ContinuousCurve2D interface


	// ===================================================================
	// methods of OrientedCurve2D interface
	

	public double getSignedDistance(java.awt.geom.Point2D point){
		return getSignedDistance(point.getX(), point.getY());
	}	
	
	public double getSignedDistance(double x, double y){
		if (direct)
			return Point2D.distance(xc, yc, x, y)-r;
		else
			return r-Point2D.distance(xc, yc, x, y);
	}

	// ===================================================================
	// methods of Curve2D interface

	/**
	 * Get the position of the curve from internal parametric representation,
	 * depending on the parameter t. 
	 * This parameter is between the two limits 0 and 2*Math.PI.
	 */
	public Point2D getPoint(double t){
		return this.getPoint(t, new Point2D());
	}
	
	/**
	 * Get the position of the curve from internal parametric representation,
	 * depending on the parameter t. 
	 * This parameter is between the two limits 0 and 2*Math.PI.
	 */
	public Point2D getPoint(double t, Point2D point) {
		double angle = theta+t;
		if(!direct) angle = theta-t;
			
		if(point==null) point = new Point2D();
		point.setLocation(xc + r*Math.cos(angle), yc + r*Math.sin(angle));
		return point;
	}
	

	/**
	 * Get the first point of the circle, which is the same as the last point. 
	 * @return the first point of the curve
	 */
	public Point2D getFirstPoint(){
		return new Point2D(xc + r*Math.cos(theta), yc + r*Math.sin(theta));
	}
	
	/**
	 * Get the last point of the circle, which is the same as the first point. 
	 * @return the last point of the curve.
	 */
	public Point2D getLastPoint(){
		return new Point2D(xc + r*Math.cos(theta), yc + r*Math.sin(theta));
	}

	public double getPosition(Point2D point) {
		double angle = Angle2D.getHorizontalAngle(xc, yc, point.getX(), point.getY());
		if(direct)
			return Angle2D.formatAngle(angle-theta);
		else
			return Angle2D.formatAngle(theta-angle);
	}
	
	/**
	 * Returns the circle with same center and same radius, but with the other
	 * orientation.
	 */
	public Circle2D getReverseCurve(){
		return new Circle2D(this.getCenter().getX(), this.getCenter().getY(), this.getRadius(), !this.direct);
	}

	/**
	 * return a new CircleArc2D. t0 and t1 are position on circle.
	 */
	public CircleArc2D getSubCurve(double t0, double t1){
		double startAngle  	= direct ? t0 : -t0;
		double extent 		= direct ? t1-t0 : t0-t1;
		extent = Angle2D.formatAngle(extent);
		return new CircleArc2D(this, startAngle, extent);
	}

	// ===================================================================
	// methods of Shape2D interface
	
	public double getDistance(java.awt.geom.Point2D point){
		return Math.abs(Point2D.distance(xc, yc, point.getX(), point.getY())-r);
	}
	
	public double getDistance(double x, double y){
		return Math.abs(Point2D.distance(xc, yc, x, y)-r);
	}

	/**
	 * Compute intersections of the circle with a line. Return an array of
	 * Point2D, of size 0, or 2 depending on the distance between circle and
	 * line. If there are 2 intersections points, the first one in the array
	 * is the first one on the line. 
	 */
	public Collection<Point2D> getIntersections(StraightObject2D line) {
		// first compute position of point at intersection of line and from the perpendicular
		// line going through the center of the circle.
		ArrayList<Point2D> intersections = new ArrayList<Point2D>();

		Point2D center = new Point2D(xc, yc);
		StraightLine2D perp = StraightLine2D.createOrthogonalLine2D(line, center);
		Point2D inter = line.getIntersection(perp);
		

		// the line is too far from the circle -> no intersection
		if(inter==null) return intersections;

		double dist = inter.getDistance(xc, yc);		
		
		// if the distance is the radius of the circle, return the intersection point 
		if(Math.abs(dist-r)<Shape2D.ACCURACY){
			if(line.contains(inter))
				intersections.add(inter);
			return intersections;
		}
		
		// compute angle of the line, and distance between 'inter' point and
		// each intersection point
		double angle = line.getHorizontalAngle();
		double d2 = Math.sqrt(r*r - dist*dist);
		
		// Compute position and angle of intersection points
		Point2D p1 = Point2D.createPolar(inter, d2, angle+Math.PI);
		Point2D p2 = Point2D.createPolar(inter, d2, angle);
		
		if(line.contains(p1)) intersections.add(p1);
		if(line.contains(p2)) intersections.add(p2);
		
		return intersections;
	}

	/**
	 * Clip the circle by a box. The result is an instance of
	 * CurveSet2D<SmoothOrientedCurve2D>, which 
	 * contains only instances of CircleArc2D or Circle2D.
	 * If the circle is not clipped, the result is an instance of
	 * CurveSet2D<SmoothOrientedCurve2D> which contains 0 curves.
	 */
	@Override
	public CurveSet2D<? extends SmoothOrientedCurve2D> clip(Box2D box) {
		// Clip the curve
		CurveSet2D<SmoothCurve2D> set = CurveUtil.clipSmoothCurve(this, box);
		
		// Stores the result in appropriate structure
		CurveSet2D<SmoothOrientedCurve2D> result =
			new CurveSet2D<SmoothOrientedCurve2D> ();
		
		// convert the result
		for(Curve2D curve : set.getCurves()){
			if (curve instanceof CircleArc2D)
				result.addCurve((CircleArc2D) curve);
			if (curve instanceof Circle2D)
				result.addCurve((Circle2D) curve);
		}
		return result;
	}

	

	// ===================================================================
	// methods of Shape interface
	

	/** 
	 * Return true if the point (x, y) lies exactly on the circle.
	 */
	public boolean contains(double x, double y){
		return Math.abs(getDistance(x, y))<=Shape2D.ACCURACY;
	}

	/**
	 * Return bounding box of the circle.
	 */
	public java.awt.Rectangle getBounds() {
		return this.getBoundingBox().getAsAWTRectangle();
	}

	/**
	 * Return more precise bounds of the circle.
	 */
	public java.awt.geom.Rectangle2D getBounds2D() {
		return this.getBoundingBox().getAsAWTRectangle2D();
	}
	

	public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path){
		double cot = Math.cos(theta);
		double sit = Math.sin(theta);
		
		// position to the first point
		path.lineTo((float)(xc+r*cot), 
					(float)(yc+r*sit));

		
		if(direct)
			for(double t=0; t<Math.PI*2; t+=.1)
				path.lineTo((float)(xc+r*Math.cos(t)*cot-r*Math.sin(t)*sit), 
							(float)(yc+r*Math.cos(t)*sit+r*Math.sin(t)*cot));
		else
			for(double t=0; t>Math.PI*2; t+=.1)
				path.lineTo((float)(xc+r*Math.cos(t)*cot+r*Math.sin(t)*sit), 
							(float)(yc+r*Math.cos(t)*sit-r*Math.sin(t)*cot));

		// line to first point
		path.lineTo((float)(xc+r*cot), 
					(float)(yc+r*sit));
		
		return path;
	}
	

	protected java.awt.geom.GeneralPath getGeneralPath(){
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		
		// position to the first point
		path.moveTo((float)(xc+r), (float)(yc));

		// draw each line of the boundary
		if(direct)
			for(double t=0; t<=2*Math.PI; t+=.1)
				path.lineTo((float)(xc+r*Math.cos(t)), (float)(yc+r*Math.sin(t)));
		else
			for(double t=0; t<=2*Math.PI; t+=.1)
				path.lineTo((float)(xc+r*Math.cos(t)), (float)(yc-r*Math.sin(t)));
		
		// line to first point
		path.lineTo((float)(xc+r), 
					(float)(yc));
		
		// close to the last point
		path.closePath();
		return path;
	}
	
	/** 
	 * Return pathiterator for this circle.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans){
		return this.getGeneralPath().getPathIterator(trans);
	}

	/**
	 * Return pathiterator for this circle.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans, double flatness){
		return this.getGeneralPath().getPathIterator(trans, flatness);
	}
	
	// ===================================================================
	// methods of Object interface
	
	public boolean equals(Object obj){
		if(!(obj instanceof Ellipse2D)) return false;
		
		if(obj instanceof Circle2D){
			Circle2D circle = (Circle2D) obj;
			
			if(Math.abs(circle.xc-xc)>Shape2D.ACCURACY) return false;
			if(Math.abs(circle.yc-yc)>Shape2D.ACCURACY) return false;
			if(Math.abs(circle.r-r)>Shape2D.ACCURACY) return false;
			if(circle.direct!=direct) return false;
			return true;
		}
		return super.equals(obj);
	}
	
}
