/* File Hyperbola2D.java 
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
package math.geom2d.conic;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.BoundarySet2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.line.StraightObject2D;
import math.geom2d.transform.AffineTransform2D;
import math.geom2d.transform.Rotation2D;
import math.geom2d.transform.Scaling2D;
import math.geom2d.transform.Translation2D;


// Imports

/**
 * Superclass for all linear and pieces smooth curves : polylines, conics,
 * lines ...
 */
public class Hyperbola2D extends BoundarySet2D<HyperbolaBranch2D>
implements Conic2D{

	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables
	
	/** Center of the hyperbola */
	double xc = 0;
	double yc = 0;
	
	/** first focal parameter */
	double a = 1;
	
	/** second focal parameter */
	double b = 1;
	
	/** angle of rotation of the hyperbola */
	double theta = 0;
	
	/** a flag indicating whether the hyperbola is direct or not */
	boolean direct = true;
	
	HyperbolaBranch2D branch1 = null;
	HyperbolaBranch2D branch2 = null;
	
	// ===================================================================
	// constructors
	
	/**
	 * Assume centered hyperbola, with a = b = 1 (orthogonal hyperbola), 
	 * theta=0 (hyperbola is oriented East-West), and direct orientation.
	 */
	public Hyperbola2D(){
		this(0, 0, 1, 1, 0, true);
	}
	
	public Hyperbola2D(Point2D center, double a, double b, double theta, boolean d){
		this(center.getX(), center.getY(), a, b, theta, d);
	}
	
	/** Main constructor */
	public Hyperbola2D(double xc, double yc, double a, double b, double theta, boolean d){
		this.xc = xc;
		this.yc = yc;
		this.a = a;
		this.b = b;
		this.theta = theta;
		this.direct = d;
		
		branch1 = new HyperbolaBranch2D(this, false);
		branch2 = new HyperbolaBranch2D(this, true);
		this.addCurve(branch1);
		this.addCurve(branch2);
	}

	
	// ===================================================================
	// methods specific to Hyperbola2D
	
	/**
	 * transform a point in local coordinate (ie orthogonal centered hyberbola
	 * with a=b=1) to global coordinate system.
	 */
	public Point2D toGlobal(Point2D point){
		point = point.transform(new Scaling2D(a, b));
		point = point.transform(new Rotation2D(theta));
		point = point.transform(new Translation2D(xc, yc));
		return point;
	}

	public Point2D toLocal(Point2D point){
		point = point.transform(new Translation2D(-xc, -yc));
		point = point.transform(new Rotation2D(-theta));
		point = point.transform(new Scaling2D(1/a, 1/b));
		return point;
	}
	
	/** 
	 * Change coordinate of the line to correspond to a standard hyperbola.
	 * Standard hyperbola is such that x^2-y^2=1 for every point.
	 * @param point
	 * @return
	 */
	private StraightObject2D formatLine(StraightObject2D line){
		line = line.transform(new Translation2D(-xc, -yc));
		line = line.transform(new Rotation2D(-theta));
		line = line.transform(new Scaling2D(1.0/a, 1.0/b));
		return line;
	}

	// ===================================================================
	// methods inherited fromConic2D interface
	
	public double getAngle() {
		return theta;
	}

	public double[] getCartesianEquation() {
		// TODO Auto-generated method stub
		return null;
	}

	public Point2D getCenter() {
		return new Point2D(xc, yc);
	}

	public int getConicType() {
		return Conic2D.HYPERBOLA;
	}

	public double getEccentricity() {
		return Math.hypot(1, b*b/a/a);
	}

	public Point2D getFocus1() {
		double c = Math.hypot(a, b);
		return new Point2D(c*Math.cos(theta)+xc, c*Math.sin(theta)+yc);
	}

	public Point2D getFocus2() {
		double c = Math.hypot(a, b);
		return new Point2D(-c*Math.cos(theta)+xc, -c*Math.sin(theta)+yc);
	}

	/** Return 0 */
	public double getLength1() {
		return a;
	}

	/** Return 0 */
	public double getLength2() {
		return b;
	}

	public Vector2D getVector1() {
		return new Vector2D(Math.cos(theta), Math.sin(theta));
	}

	public Vector2D getVector2() {
		return new Vector2D(-Math.sin(theta), Math.cos(theta));
	}

	public boolean isDirect() {
		return direct;
	}
		
	public boolean contains(java.awt.geom.Point2D point) {
		return this.contains(point.getX(), point.getY());
	}

	public boolean contains(double x, double y) {
		Point2D point = toLocal(new Point2D(x, y));
		double xa = point.getX()/a;
		double yb = point.getY()/b;
		double res = xa*xa-yb*yb-1;
		//double res = x*x*b*b - y*y*a*a - a*a*b*b;
		return Math.abs(res)<1e-6;
	}
	
	public Hyperbola2D getReverseCurve(){
		return new Hyperbola2D(this.xc, this.yc, this.a, this.b, this.theta, !this.direct);
	}

	public Collection<Point2D> getIntersections(StraightObject2D line) {
		
		Collection<Point2D> points = new ArrayList<Point2D>();
		
		// format to 'standard' hyperbola
		StraightObject2D line2 = formatLine(line);
		
		// Extract formatted line parameters
		Point2D origin = line2.getOrigin();
		double x0 = origin.getX();
		double y0 = origin.getY();
		double dx = line2.getVector().getDx();
		double dy = line2.getVector().getDy();
		
		// extract line parameters
		// different strategy depending if line is more horizontal or more vertical
		if(Math.abs(dx)>Math.abs(dy)){
			// Line is mainly horizontal
			
			// slope of the line
			double k = dy/dx;
			
			// compute coefficients of second order equation
			double a = 1-k*k;
			double b = -2*k*y0;
			double c = -y0*y0-1;
			
			double delta 	= b*b - 4*a*c;
			if(delta<=0){
				System.out.println("Intersection with horizontal line should alays give positive delta");
				return points;
			}
			
			// x coordinate of intersection points
			double x1 = (-b - Math.sqrt(delta))/(2*a);
			double x2 = (-b + Math.sqrt(delta))/(2*a);
			
			// support line of formatted line
			StraightLine2D support = line2.getSupportLine();

			// check first point is on the line
			double pos1 = support.project(new Point2D(x1, k*x1+y0));
			if(line2.contains(support.getPoint(pos1)))
				points.add(line.getPoint(pos1));
			
			// check second point is on the line
			double pos2 = support.project(new Point2D(x2, k*x2+y0));
			if(line2.contains(support.getPoint(pos2)))
				points.add(line.getPoint(pos2));
			
		}else{
			// Line is mainly vertical
			
			// slope of the line
			double k = dx/dy;
			
			// compute coefficients of second order equation
			double a = k*k-1;
			double b = -2*k*x0;
			double c = x0*x0-1;
			
			double delta 	= b*b - 4*a*c;
			if(delta<=0){
				// No intersection with the hyperbola
				return points;
			}
			
			// x coordinate of intersection points
			double y1 = (-b - Math.sqrt(delta))/(2*a);
			double y2 = (-b + Math.sqrt(delta))/(2*a);

			// support line of formatted line
			StraightLine2D support = line2.getSupportLine();

			// check first point is on the line
			double pos1 = support.project(new Point2D(k*y1+x0, y1));
			if(line2.contains(support.getPoint(pos1)))
				points.add(line.getPoint(pos1));
			
			// check second point is on the line
			double pos2 = support.project(new Point2D(k*y2+x0, y2));
			if(line2.contains(support.getPoint(pos2)))
				points.add(line.getPoint(pos2));
		}

		return points;
	}

	public Hyperbola2D transform(AffineTransform2D trans){
		//TODO: implement it
		return this;
	}
		
}