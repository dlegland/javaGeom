/* File Ellipse2D.java 
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
package math.geom2d;

import java.util.ArrayList;
import java.util.Collection;


// Imports

/**
 * An ellipse in the plane. It is defined by the center, the orientation
 * angle, and the lengths of the two axis. 
 * No convention is taken about lengths of semiaxis : the second semi axis
 * can be greater than the first one.
 */
public class Ellipse2D implements SmoothCurve2D, Conic2D, ContinuousBoundary2D, Boundary2D{


	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables

	/** coordinate of center. */
	protected double xc;
	protected double yc;
	
	/** length of semi axis */
	protected double r1, r2;
	
	/** orientation of first semi major axis */
	protected double theta=0;
	
	/** directed ellipse or not */
	protected boolean direct = true;
	
	
	// ===================================================================
	// constructors
	
	/** 
	 * Empty constructor, define ellipse centered at origin with both major
	 * and minor semi-axis with length equal to 1.
	 */
	public Ellipse2D(){
		this(0, 0, 1, 1, 0, true);
	}

	/** Main constructor: define center by a point plus major and minor smei axis */
	public Ellipse2D(Point2D center, double l1, double l2){
		this(center.getX(), center.getY(), l1, l2, 0, true);
	}
	
	/** Define center by coordinate, plus major and minor semi axis*/
	public Ellipse2D(double xc, double yc, double l1, double l2){
		this(xc, yc, l1, l2, 0, true);
	}
	
	/**
	 * Define center by point, major and minor semi axis lengths, 
	 * and orientation angle.
	 */
	public Ellipse2D(Point2D center, double l1, double l2, double theta){
		this(center.getX(), center.getY(), l1, l2, theta, true);
	}

	/**
	 * Define center by coordinate, major and minor semi axis lengths, 
	 * and orientation angle.
	 */
	public Ellipse2D(double xc, double yc, double l1, double l2, double theta){
		this(xc, yc, l1, l2, theta, true);
	}
		
	/**
	 * Define center by coordinate, major and minor semi axis lengths, 
	 * orientation angle, and boolean flag for directed ellipse.
	 */
	public Ellipse2D(double xc, double yc, double l1, double l2, double theta, boolean direct){
		this.xc = xc;
		this.yc = yc;
		
		r1 = l1;
		r2 = l2;
		
		this.theta=theta;
		this.direct = direct;
	}
	
	
	/**
	 * construct an ellipse from the java.awt.geom class for ellipse.
	 */
	public Ellipse2D(java.awt.geom.Ellipse2D ellipse){
		this(new Point2D(ellipse.getCenterX(), ellipse.getCenterY()),
			ellipse.getWidth()/2, ellipse.getHeight()/2);
	}
	
	
	// ===================================================================
	// Methods specific to Ellipse2D

	public void setEllipse(double xc, double yc, double r1, double r2, double theta){
		this.setEllipse(xc, yc, r1, r2, theta, true);
	}
	
	public void setEllipse(double xc, double yc, double r1, double r2, double theta, boolean direct){
		this.xc = xc;
		this.yc = yc;
		this.r1 = r1;
		this.r2 = r2;
		this.theta 	= theta;
		this.direct = direct;
	}
	
	public void setEllipse(Point2D center, double r1, double r2, double theta){
		this.setEllipse(center.getX(), center.getY(), r1, r2, theta, true);
	}
	
	public void setEllipse(Point2D center, double r1, double r2, double theta, boolean direct){
		this.setEllipse(center.getX(), center.getY(), r1, r2, theta, direct);
	}
	
	public void setCenter(Point2D center){
		this.setCenter(center.getX(), center.getY());
	}
	
	public void setCenter(double x, double y){
		this.xc = x;
		this.yc = y;
	}
	
	/**
	 * return as a closed polyline with <code>n</code> line segments.
	 * @param n the number of line segments
	 * @return a closed polyline with <code>n</code> line segments.
	 */
	public Polyline2D getAsPolyline(int n){
		Point2D[] points = new Point2D[n];
		double t0 = this.getT0();
		double t1 = this.getT1();
		double dt = (t1-t0)/n;
		if(this.direct)
			for(int i=0; i<n; i++)
				points[i] = this.getPoint((double)i*dt + t0);
		else
			for(int i=0; i<n; i++)
				points[i] = this.getPoint(-(double)i*dt + t0);
		return new ClosedPolyline2D(points);		
	}
	
	/**
	 * Return the RHO parameter, in a polar representation of the ellipse, centered
	 * at the center of ellipse.
	 * @param angle : angle from horizontal
	 * @return distance of ellipse from ellipse center in direction theta
	 */
	public double getRho(double angle){
		double cot = Math.cos(angle-theta);
		double sit = Math.cos(angle-theta);
		return Math.sqrt(r1*r1*r2*r2/(r2*r2*cot*cot + r1*r1*sit*sit));
	}

	public Point2D getProjectedPoint(java.awt.geom.Point2D point){
		Vector2D polar = this.getProjectedVector(point, Shape2D.ACCURACY);
		return new Point2D(point.getX()+polar.getDx(), point.getY()+polar.getDy());
	}

	/**
	 * Compute projection of a point onto an ellipse. Return the polar vector
	 * representing the translation from point <code>point</point> to its
	 * projection on the ellipse, with the direction parallel to the local 
	 * normal to the ellipse. The parameter <code>rho</code> of the
	 * PolarVector2D is positive if point lies 
	 * Refs : <p>
	 * http://www.spaceroots.org/documents/distance/distance-to-ellipse.pdf, 
	 * http://www.spaceroots.org/downloads.html
	 * @param point
	 * @param eMax
	 * @return the projection vector
	 */
	public Vector2D getProjectedVector(java.awt.geom.Point2D point, double eMax){
		
		double ot = 1.0 / 3.0;
		
		// center the ellipse
		double x = point.getX()-xc;
		double y = point.getY()-yc;
		
		double la, lb, theta;
		if(r1>=r2){
			la = r1;	lb = r2;
			theta = this.theta;
		}else{
			la = r2;	lb = r1;
			theta = this.theta+Math.PI/2;
			double tmp=x; x=-y; y=tmp;
		}
		
		double cot = Math.cos(theta);
		double sit = Math.sin(theta);
		double tmpx = x, tmpy = y;
		x = tmpx*cot - tmpy*sit; 
		y = tmpx*sit + tmpy*cot; 
		

		
		double ae	= la;
		double f	= 1-lb/la;
		double e2	= f * (2.0 - f);
		double g	= 1.0 - f;
		double g2	= g * g;
		//double e2ae	= e2 * ae;
		double ae2	= ae * ae;

		// compute some miscellaneous variables outside of the loop
		double 	z 			= y;
		double  z2         	= y*y;
		double  r          	= x;
		double  r2         	= x*x;
		double  g2r2ma2    	= g2 * (r2 - ae2);
		//double  g2r2ma2mz2 	= g2r2ma2 - z2;
		double  g2r2ma2pz2 	= g2r2ma2 + z2;
		double  dist       	= Math.sqrt(r2 +z2);
		//double  threshold  	= Math.max(1.0e-14 * dist, eMax);
		boolean inside    	= (g2r2ma2pz2 <= 0);
		
		// point at the center
		if (dist < (1.0e-10 * ae)) {
			System.out.println("point at the center");
			return Vector2D.createPolar(r, 0);
		}
		
		double cz = r / dist;
		double sz = z / dist;
		double t  = z / (dist + r);
		
		// distance to the ellipse along the current line
		// as the smallest root of a 2nd degree polynom :
		// a k^2 - 2 b k + c = 0
		double a  = 1.0 - e2 * cz * cz;
		double b  = g2 * r * cz + z * sz;
		double c  = g2r2ma2pz2;
		double b2 = b * b;
		double ac = a * c;
		double k  = c / (b + Math.sqrt(b2 - ac));
		//double lambda = Math.atan2(cart.y, cart.x);
		double phi    = Math.atan2(z - k * sz, g2 * (r - k * cz));
		
		// point on the ellipse
		if (Math.abs(k) < (1.0e-10 * dist)) {
			//return new Ellipsoidic(lambda, phi, k);
			return Vector2D.createPolar(k, phi);
		}
		
		for (int iterations = 0; iterations < 100; ++iterations) {
			
			// 4th degree normalized polynom describing
			// circle/ellipse intersections
			// tau^4 + b tau^3 + c tau^2 + d tau + e = 0
			// (there is no need to compute e here)
			a        = g2r2ma2pz2 + g2 * (2.0 * r + k) * k;
			b        = -4.0 * k * z / a;
			c        = 2.0 * (g2r2ma2pz2 + (1.0 + e2) * k * k) / a;
			double d = b;
			
			// reduce the polynom to degree 3 by removing
			// the already known real root
			// tau^3 + b tau^2 + c tau + d = 0
			b += t;
			c += t * b;
			d += t * c;
			
			// find the other real root
			b2       = b * b;
			double Q = (3.0 * c - b2) / 9.0;
			double R = (b * (9.0 * c - 2.0 * b2) - 27.0 * d) / 54.0;
			double D = Q * Q * Q + R * R;
			double tildeT, tildePhi;
			if (D >= 0) {
				double rootD = Math.sqrt(D);
				double rMr = R - rootD;
				double rPr = R + rootD;
				tildeT = ((rPr > 0) ?  Math.pow(rPr, ot) : -Math.pow(-rPr, ot))
				+ ((rMr > 0) ?  Math.pow(rMr, ot) : -Math.pow(-rMr, ot))
				- b * ot;
				double tildeT2   = tildeT * tildeT;
				double tildeT2P1 = 1.0 + tildeT2;
				tildePhi = Math.atan2(z * tildeT2P1 - 2 * k * tildeT,
						g2 * (r * tildeT2P1 - k * (1.0 - tildeT2)));
			} else {
				Q = -Q;
				double qRoot     = Math.sqrt(Q);
				double alpha     = Math.acos(R / (Q * qRoot));
				tildeT           = 2 * qRoot * Math.cos(alpha * ot) - b * ot;
				double tildeT2   = tildeT * tildeT;
				double tildeT2P1 = 1.0 + tildeT2;
				tildePhi = Math.atan2(z * tildeT2P1 - 2 * k * tildeT,
						g2 * (r * tildeT2P1 - k * (1.0 - tildeT2)));
				if ((tildePhi * phi) < 0) {
					tildeT    = 2 * qRoot * Math.cos((alpha + 2 * Math.PI) * ot) - b * ot;
					tildeT2   = tildeT * tildeT;
					tildeT2P1 = 1.0 + tildeT2;
					tildePhi  = Math.atan2(z * tildeT2P1 - 2 * k * tildeT,
							g2 * (r * tildeT2P1 - k * (1.0 - tildeT2)));
					if (tildePhi * phi < 0) {
						tildeT    = 2 * qRoot * Math.cos((alpha + 4 * Math.PI) * ot) - b * ot;
						tildeT2   = tildeT * tildeT;
						tildeT2P1 = 1.0 + tildeT2;
						tildePhi  = Math.atan2(z * tildeT2P1 - 2 * k * tildeT,
								g2 * (r * tildeT2P1 - k * (1.0 - tildeT2)));
					}
				}
			}
			
			// midpoint on the ellipse
			double dPhi  = Math.abs(0.5 * (tildePhi - phi));
			phi   = 0.5 * (phi + tildePhi);
			double cPhi  = Math.cos(phi);
			double sPhi  = Math.sin(phi);
			double coeff = Math.sqrt(1.0 - e2 * sPhi * sPhi);
			
			// Eventually display result of iterations
			if (false)
				System.out.println(iterations + ": phi = " + Math.toDegrees(phi)
						+ " +/- " + Math.toDegrees(dPhi)
						+ ", k = " + k);
			
			b = ae / coeff;
			double dR = r - cPhi * b;
			double dZ = z - sPhi * b * g2;
			k = Math.sqrt(dR * dR + dZ * dZ);
			if (inside) {
				k = -k;
			}
			t = dZ / (k + dR);			

			if (dPhi < 1.0e-14) {
				if(this.r1>=this.r2)
					return Vector2D.createPolar(-k, phi+theta); 
							//-(r * cPhi + z * sPhi - ae * coeff), phi+theta);
				else
					return Vector2D.createPolar(-k, phi+theta-Math.PI/2);
							//-(r * cPhi + z * sPhi - ae * coeff), phi+theta-Math.PI/2);
			}			
		}
		
		return null;
	}
	
	// ===================================================================
	// methods of Conic2D

	public int getConicType(){
		if(Math.abs(r1-r2)<Shape2D.ACCURACY)
			return Conic2D.CIRCLE;
		else
			return Conic2D.ELLIPSE;
	}

	public boolean isEllipse(){return true;}
	public boolean isParabola(){return false;}
	public boolean isHyperbola(){return false;}
	public boolean isCircle(){
		return Math.abs(r1-r2)<Shape2D.ACCURACY;
	}			
	public boolean isStraightLine(){return false;}
	public boolean isTwoLines(){return false;}
	public boolean isPoint(){return false;}

	public boolean isDegenerated(){return false;}


	public double[] getCartesianEquation(){
		// TODO: not tested, only analytically expressed
		double cot = Math.cos(theta);
		double sit = Math.sin(theta);
		double cot2 = cot*cot;
		double sit2 = sit*sit;
		double xr  = xc - xc*cot + yc*sit;
		double yr  = yc - xc*sit - yc*cot;
		double r12 = r1*r1;
		double r22 = r2*r2;
		
		return new double[]{
			cot2/r2 + sit2/r22,
			2*sit*cot*(r12-r22)/(r12*r22),
			cot2/r12 + sit2/r22,
			2*(xr*cot/r12 + yr*sit/r22),
			2*(yr*cot/r22 - xr*sit/r12),
			xr*xr/r12 + yr*yr/r22
		};
	}

	/**
	 * Returns the length of the first semi-axis of the ellipse.
	 */
	public double getLength1(){
		return r1;
	}

	/**
	 * Returns the length of the second semi-axis of the ellipse.
	 */
	public double getLength2(){
		return r2;
	}
	
	/**
	 * compute eccentricity of ellipse, depending on the lengths of the
	 * semi axis.
	 */
	public double getEccentricity(){
		double a = Math.max(r1, r2);
		double b = Math.min(r1, r2);
		return Math.sqrt(1-a*a/b/b);
	}

	/**
	 * return center of ellipse.
	 */
	public Point2D getCenter(){
		return new Point2D(xc, yc);
	}

	/**
	 * Return the first focus. It ius defined as the first focus on the Major
	 * axis, in the direction given by angle theta.
	 */
	public Point2D getFocus1(){
		double a, b, theta;
		if(r1>r2){
			a = r1;	b = r2;
			theta = this.theta;
		}else{
			a = r2;	b = r1;
			theta = this.theta + Math.PI/2;
		}
		return Point2D.createPolar(xc, yc, Math.sqrt(a*a-b*b), theta+Math.PI);
	}

	/**
	 * Return the second focus. It is defined as the second focus on the Major
	 * axis, in the direction given by angle theta.
	 */
	public Point2D getFocus2(){
		double a, b, theta;
		if(r1>r2){
			a = r1;	b = r2;
			theta = this.theta;
		}else{
			a = r2;	b = r1;
			theta = this.theta + Math.PI/2;
		}
		return Point2D.createPolar(xc, yc, Math.sqrt(a*a-b*b), theta);
	}
	
	public Vector2D getVector1(){
		return new Vector2D(Math.cos(theta), Math.sin(theta));
	}

	public Vector2D getVector2(){
		if(direct)
			return new Vector2D(-Math.sin(theta), Math.cos(theta));
		else
			return new Vector2D(Math.sin(theta), -Math.cos(theta));
	}

	/** 
	 * return the angle of the ellipse first axis with the Ox axis. 
	 */
	public double getAngle(){
		return theta;
	}
	
	/**
	 * return true if ellipse has a direct orientation.
	 */
	public boolean isDirect(){
		return direct;
	}
	
	// ===================================================================
	// methods of SmoothCurve2D interface
	
	public Vector2D getTangent(double t){
		if(!direct) t = -t;
		double cot = Math.cos(theta);
		double sit = Math.sin(theta);
			
		if (direct)
			return new Vector2D(-r1*Math.sin(t)*cot - r2*Math.cos(t)*sit,
					  			-r1*Math.sin(t)*sit + r2*Math.cos(t)*cot);
		else
			return new Vector2D(r1*Math.sin(t)*cot + r2*Math.cos(t)*sit,
					  			r1*Math.sin(t)*sit - r2*Math.cos(t)*cot);
	}

	/**
	 * returns the curvature of the ellipse.
	 */
	public double getCurvature(double t){
		if(!direct) t = -t;
		double cot = Math.cos(t);
		double sit = Math.sin(t);
		return r1*r2/Math.pow(r2*r2*cot*cot+r1*r1*sit*sit, 1.5);
	}

	
	// ===================================================================
	// methods of Boundary2D interface
	
	public Collection<ContinuousBoundary2D> getBoundaryCurves(){
		ArrayList<ContinuousBoundary2D> list = new ArrayList<ContinuousBoundary2D>(1);
		list.add(this);
		return list;
	}


	// ===================================================================
	// methods of ContinuousCurve2D interface

	/** 
	 * Returns a set of smooth curves, which contains only the ellipse.
	 */
	public Collection<? extends SmoothCurve2D> getSmoothPieces() {
		ArrayList<Ellipse2D> list = new ArrayList<Ellipse2D>(1);
		list.add(this);
		return list;
	}

	/**
	 * return true, as an ellipse is always closed.
	 */
	public boolean isClosed(){
		return true;
	}

	// ===================================================================
	// methods of OrientedCurve2D interface
	
	/**
	 * Return either 0, 2*PI or -2*PI, depending whether the point is located
	 * inside the interior of the ellipse or not.
	 */
	public double getWindingAngle(java.awt.geom.Point2D point){
		if(this.getSignedDistance(point)>0)
			return 0;
		else
			return direct ? Math.PI*2 : -Math.PI*2;
	}
	
	/**
	 * Test whether  the point is inside the ellipse. The test is performed
	 * by rotating the ellipse and the point to align with axis, rescaling
	 * in each direction, then computing distance to origin.
	 */
	public boolean isInside(java.awt.geom.Point2D point){
		AffineTransform2D rot = new Rotation2D(this.xc, this.yc, -this.theta);
		Point2D pt = rot.transform(point, new Point2D());
		double xp = pt.getX()/this.r1;
		double yp = pt.getY()/this.r2;
		if (direct)
			return xp*xp + yp*yp < 1;
		else
			return xp*xp + yp*yp > 1;
	}
	
	public double getSignedDistance(java.awt.geom.Point2D point){
		Vector2D vector = this.getProjectedVector(point, 1e-10);
		if(isInside(point))
			return -vector.getNorm();
		else
			return vector.getNorm();
	}	
	
	public double getSignedDistance(double x, double y){
		return getSignedDistance(new Point2D(x, y));
	}

	// ===================================================================
	// methods of Curve2D interface

	/** 
	 * Returns the parameter of the first point of the ellipse, set to 0.
	 */
	public double getT0(){
		return 0;
	}

	/** 
	 * Returns the parameter of the last point of the ellipse, set to 2*PI.
	 */
	public double getT1(){
		return 2*Math.PI;
	}

	/**
	 * get the position of the curve from internal parametric representation,
	 * depending on the parameter t. 
	 * This parameter is between the two limits 0 and 2*Math.PI.
	 */
	public Point2D getPoint(double t){
		return this.getPoint(t, new Point2D());
	}
	
	public Point2D getPoint(double t, Point2D point){
		if(point==null) point = new Point2D();
		if(!direct) t = -t;
		double cot = Math.cos(theta);
		double sit = Math.sin(theta);
		point.setLocation(xc + r1*Math.cos(t)*cot - r2*Math.sin(t)*sit,
						  yc + r1*Math.cos(t)*sit + r2*Math.sin(t)*cot);
		return point;
	}

	/**
	 * Get the first point of the ellipse, which is the same as the last point. 
	 * @return the first point of the curve
	 */
	public Point2D getFirstPoint(){
		return new Point2D(xc + r1*Math.cos(theta), yc + r1*Math.sin(theta));
	}
	
	/**
	 * Get the last point of the ellipse, which is the same as the first point. 
	 * @return the last point of the curve.
	 */
	public Point2D getLastPoint(){
		return new Point2D(xc + r1*Math.cos(theta), yc + r1*Math.sin(theta));
	}

	public double getPosition(Point2D point){
		// TODO: return a raw approximation. Should return exact value. or create other method.
		double xp = point.getX();
		double yp = point.getY();
		
		// translate
		xp = xp-this.xc;
		yp = yp-this.yc;
		
		// rotate
		double xp1 = xp*Math.cos(theta) + yp*Math.sin(theta);
		double yp1 = -xp*Math.sin(theta) + yp*Math.cos(theta);
		xp = xp1;
		yp = yp1;

		// scale
		xp = xp/this.r1;
		yp = yp/this.r2;
		
		// compute angle
		double angle = Angle2D.getHorizontalAngle(xp, yp);
		
		return angle;
	}
	
	/**
	 * Returns the ellipse with same center and same radius, but with the other
	 * orientation.
	 */
	public Ellipse2D getReverseCurve(){
		return new Ellipse2D(xc, yc, r1, r2, theta, !direct);
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
		double startAngle  	= direct ? t0 : -t0;
		double extent 		= direct ? t1-t0 : t0-t1;
		return new EllipseArc2D(this, startAngle, extent);
	}
	
	// ===================================================================
	// methods of Shape2D interface

	/** Always returns true, because an ellipse is bounded.*/
	public boolean isBounded(){
		return true;
	}

	public double getDistance(java.awt.geom.Point2D point){
//		PolarVector2D vector = this.getProjectedVector(point, 1e-10);
//		return Math.abs(vector.getRho());
		return this.getAsPolyline(128).getDistance(point);
	}
	
	
	public double getDistance(double x, double y){
		return getDistance(new Point2D(x, y));
	}
		
	public Shape2D getClippedShape(Box2D box){
		return box.clipContinuousOrientedCurve(this);
	}
	
	/**
	 * Clip the ellipse by a box. The result is an instance of
	 * CurveSet2D<ContinuousOrientedCurve2D>, 
	 * which contains only instances of Ellipse2D or EllipseArc2D.
	 * If the ellipse is not clipped, the result is an instance of
	 * CurveSet2D<ContinuousOrientedCurve2D> which contains 0 curves.
	 */
	public CurveSet2D<? extends ContinuousOrientedCurve2D> clip(Box2D box) {
		// Clip the curve
		CurveSet2D<SmoothCurve2D> set = box.clipSmoothCurve(this);
		
		// Stores the result in appropriate structure
		CurveSet2D<ContinuousOrientedCurve2D> result =
			new CurveSet2D<ContinuousOrientedCurve2D> ();
		
		// convert the result
		for(Curve2D curve : set.getCurves()){
			if (curve instanceof EllipseArc2D)
				result.addCurve((EllipseArc2D) curve);
			if (curve instanceof Ellipse2D)
				result.addCurve((Ellipse2D) curve);
		}
		return result;
	}

	/**
	 * Return more precise bounds for the ellipse. Return an instance of Box2D.
	 */
	public Box2D getBoundingBox(){
		// we consider the two parametric equations x(t) and y(t). From the ellipse
		// definition, x(t)=r1*cos(t), y(t)=r2*sin(t), and the result is moved
		// (rotated with angle theta, and translated with (xc,yc) ).		
		// Each equation can then be written in the form : x(t) = Xm*cos(t+theta_X).
		// We compute Xm and Ym, and use it to calculate bounds. 
		double cot = Math.cos(theta);
		double sit = Math.sin(theta);
		double xm = Math.sqrt(r1*r1*cot*cot +r2*r2*sit*sit);
		double ym = Math.sqrt(r1*r1*sit*sit +r2*r2*cot*cot);
		return new Box2D(xc-xm, xc+xm, yc-ym, yc+ym);
	}

	/**
	 * Compute intersections of the ellipse with a straight object (line, 
	 * line segment, ray...).<p>
	 * Principle of the algorithm is to transform line and ellipse such
	 * that ellipse becomes a circle, then using the intersections computation
	 * from circle. 
	 */
	public Collection<Point2D> getIntersections(StraightObject2D line){
		// Compute the transform2D which transforms ellipse into unit circle
		AffineTransform2D trans = new GenericAffineTransform2D();
		trans = trans.compose(new Scaling2D(1/this.r1, 1/this.r2));
		trans = trans.compose(new Rotation2D(-this.theta));
		trans = trans.compose(new Translation2D(-this.xc, -this.yc));
//		trans.preConcatenate(AffineTransform2D.createTranslation(-this.xc, -this.yc));
//		trans.preConcatenate(AffineTransform2D.createRotation(-this.theta));
//		trans.preConcatenate(AffineTransform2D.createScaling(1/this.r1, 1/this.r2));
		
		// transform the line accordingly
		StraightObject2D line2 = (StraightObject2D) line.transform(trans);
		
		// The list of intersections
		Collection<Point2D> points;
		
		// Compute intersection points with circle
		Circle2D circle = new Circle2D(0, 0, 1);
		points = circle.getIntersections(line2);
		if(points.size()==0) return points;
		
		// convert points on circle as angles
		ArrayList<Point2D> res = new ArrayList<Point2D>(points.size());
		double angle;
		for(Point2D point : points){
			angle = circle.getPosition(point);
			res.add(this.getPoint(angle));
		}
			
		// return the result
		return res;
	}
	
	public Ellipse2D transform(AffineTransform2D trans){
		double tmp1, tmp2;
		double cot = Math.cos(theta);
		double sit = Math.sin(theta);

		// coef of the transform
		double tab[] = trans.getCoefficients();
		
		// compute coordinate of new center
		double xc2 = xc*tab[0] + yc*tab[1] + tab[2];
		double yc2 = xc*tab[3] + yc*tab[4] + tab[5];
		
		// square of r1 of new ellipse
		tmp1 = tab[0]*r1*cot + tab[1]*r1*sit;
		tmp2 = tab[3]*r1*cot + tab[4]*r1*sit;
		double r12 = Math.sqrt(tmp1*tmp1 + tmp2*tmp2);
		
		// square of r2 of new ellipse
		tmp1 = tab[0]*r2*cot - tab[1]*r2*sit;
		tmp2 = tab[3]*r2*cot - tab[4]*r2*sit;
		double r22 = Math.sqrt(tmp1*tmp1 + tmp2*tmp2);
		
		if(false){
			double theta2_a = Math.atan2(tab[4]*sit+tab[3]*cot, tab[1]*sit+tab[0]*cot);
			double theta2_b = Math.atan2(tab[3]*cot+tab[4]*sit, tab[0]*cot+tab[1]*sit);
			double theta2_c = Math.asin((tab[3]*r1*cot+tab[4]*r1*sit)/r12);
			double theta2_d = Math.acos((tab[0]*r1*cot+tab[1]*r1*sit)/r12);
			System.out.println("theta2_a = " +theta2_a);
			System.out.println("theta2_b = " +theta2_b);
			System.out.println("theta2_c = " +theta2_c);
			System.out.println("theta2_d = " +theta2_d);
		}
		
		double theta2 = Math.atan2(tab[4]*sit+tab[3]*cot, tab[1]*sit+tab[0]*cot);

		double cot2 = Math.cos(theta2);
		double sit2 = Math.sin(theta2);
		if(Math.abs(cot2)>Math.abs(sit2)){
			r12 = (tab[0]*r1*cot + tab[1]*r1*sit)/cot2;
			r22 = (tab[4]*r2*cot - tab[3]*r2*sit)/cot2;
		}else{
			r12 = (tab[3]*r1*cot + tab[4]*r1*sit)/sit2;
			r22 = (tab[0]*r2*sit - tab[1]*r2*cot)/sit2;
		}
		
		//System.out.println("theta 2 : " + theta2); 
		return new Ellipse2D(xc2, yc2, r12, r22, theta2);
	}
	

	// ===================================================================
	// methods of Shape2D interface

	/** 
	 * Return true if the point p lies on the ellipse, with precision given by 
	 * Shape2D.ACCURACY.
	 */
	public boolean contains(java.awt.geom.Point2D p){
		return contains(p.getX(), p.getY());
	}

	/** 
	 * Return true if the point (x, y) lies on the ellipse, with precision
	 * given by Shape2D.ACCURACY.
	 */
	public boolean contains(double x, double y){
		return this.getDistance(x, y)<Shape2D.ACCURACY;
	}

	/** returns false, as an ellipse cannot contains a rectangle */
	public boolean contains(double x, double y, double w, double h){
		return false;
	}

	/** returns false, as an ellipse cannot contains a rectangle.*/
	public boolean contains(java.awt.geom.Rectangle2D rect){
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

	/**
	 * Tests if the Ellipse intersects the interior of a specified rectangular area.
	 */
	public boolean intersects(double x, double y, double w, double h) {
		
		// circle arc contained in the rectangle
		if(new Box2D(x, x+w, y, y+h).contains(xc, yc)) return true;
		
		// if distance of first corner to center lower than radius, then intersect
		if(Point2D.getDistance(x, y, xc, yc)<Math.min(r1, r2)) return true;
		
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

	/**
	 * Tests if the Ellipse intersects the interior of a specified rectangle2D.
	 */
	public boolean intersects(java.awt.geom.Rectangle2D r){
		return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}

//	public java.awt.geom.GeneralPath getInnerPath(){
//		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
//		double cot = Math.cos(theta);
//		double sit = Math.sin(theta);
//		
////		// position to the first point
////		path.moveTo((float)(xc+r1*cot), (float)(yc+r1*sit));
//
//		// draw each line of the boundary
//		if(direct)
//			for(double t=0; t<=2*Math.PI; t+=.1)
//				path.lineTo((float)(xc+r1*Math.cos(t)*cot-r2*Math.sin(t)*sit), 
//							(float)(yc+r2*Math.sin(t)*cot+r1*Math.cos(t)*sit));
//		else
//			for(double t=0; t<=2*Math.PI; t+=.1)
//				path.lineTo((float)(xc+r1*Math.cos(t)*cot+r2*Math.sin(t)*sit), 
//							(float)(yc-r2*Math.sin(t)*cot+r1*Math.cos(t)*sit));
//		
//		// close to the last point
//		path.closePath();
//		return path;
//	}
	
	/**
	 * Add the path of the ellipse to the given path. 
	 * @param path the path to be completed
	 * @return the completed path
	 */
	public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path){
		double cot = Math.cos(theta);
		double sit = Math.sin(theta);
		
		// position to the first point
		path.moveTo((float)(xc+r1*cot), (float)(yc+r1*sit));

		// draw each line of the boundary
		if(direct)
			for(double t=0; t<=2*Math.PI; t+=.1)
				path.lineTo((float)(xc+r1*Math.cos(t)*cot-r2*Math.sin(t)*sit), 
							(float)(yc+r2*Math.sin(t)*cot+r1*Math.cos(t)*sit));
		else
			for(double t=0; t<=2*Math.PI; t+=.1)
				path.lineTo((float)(xc+r1*Math.cos(t)*cot+r2*Math.sin(t)*sit), 
							(float)(yc-r2*Math.sin(t)*cot+r1*Math.cos(t)*sit));
		
		// loop to the first point
		path.lineTo((float)(xc+r1*cot), (float)(yc+r1*sit));
		
		// close to the last point
		path.closePath();
		return path;
	}
	
	protected java.awt.geom.GeneralPath getGeneralPath(){
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		
//		double cot = Math.cos(theta);
//		double sit = Math.sin(theta);
		
		// position to the first point
		//path.moveTo((float)(xc+r1*cot), (float)(yc+r1*sit));
		
		path = this.appendPath(path);
		
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
	// methods of Object superclass
	
	public boolean equals(Object obj){
		if(!(obj instanceof Ellipse2D)) return false;
		
		Ellipse2D conic = (Ellipse2D) obj;
		
		if(!conic.getCenter().equals(this.getCenter())) return false;
		if(conic.getLength1()!=this.getLength1()) return false;
		if(conic.getLength2()!=this.getLength2()) return false;
		if(conic.getAngle()!=this.getAngle()) return false;
		if(conic.isDirect()!=this.isDirect()) return false;
		return true;
	}
	
	public String toString(){
		return Double.toString(xc).concat(new String(" ")).concat(Double.toString(yc)).concat(
			new String(" ")).concat(Double.toString(r1).concat(new String(" ")).concat(Double.toString(r2)));
	}


//	/**
//	 * A class to compute shortest distance of a point to an ellipse.
//	 * @author dlegland
//	 */
//	private class Ellipsoidic {
//		/** angle of the line joining current point to ref point.*/
//		public final double lambda;
//		
//		/** normal angle of ellipse at the cuurent point */
//		public final double phi;
//		
//		/** shortest signed distance of the point to the ellipse
//		 * (negative if inside ellipse). */
//		public final double h;
//		
//		public Ellipsoidic (double lambda, double phi, double h) {
//			this.lambda = lambda;
//			this.phi    = phi;
//			this.h      = h;
//		}
//	}
}