/*
 * File : ParametricConic2D.java
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
 * author : Legland
 * Created on 30 déc. 2003
 */
package math.geom2d.conic;

import java.awt.Graphics2D;
import java.util.*;

import math.geom2d.*;
import math.geom2d.curve.ContinuousCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.domain.ContinuousOrientedCurve2D;
import math.geom2d.line.LinearShape2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * A parametric Conic is defined directly from its parametric equation:<p>
 * <code>a*x^2 + b*x*y + c*y^2 + d*x + e*y + f</code><p>
 * 
 * @author Legland
 */
public class ParametricConic2D implements Curve2D, Conic2D {
	
	// internal conic parameters
	protected double a;
	protected double b;
	protected double c;
	protected double d;
	protected double e;
	protected double f;
	
	protected Conic2D.Type type;
	
	
	/**
	 * the reduced conic. Its class is given by the integer "type".
	 */
	protected Conic2D conic = new Ellipse2D();

	

	public ParametricConic2D(double parameters []){
		a = parameters[0];
		b = parameters[1];
		c = parameters[2];
		d = parameters[3];
		e = parameters[4];
		f = parameters[5];
		updateParameters();
	}
	
	public ParametricConic2D(double a, double b, double c, double d, double e, double f){
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
		updateParameters();
	}
	
	
	/* (non-Javadoc)
	 * @see math.geom2d.Conic2D#getType()
	 */
	public Type getConicType() {
		return conic.getConicType();
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Conic2D#getConicCoefficients()
	 */
	public double[] getConicCoefficients() {
		return new double[]{a, b, c, d, e, f};
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Conic2D#getEccentricity()
	 */
	public double getEccentricity() {
		return conic.getEccentricity();
	}

	
	// ===================================================================
	// methods inherited from interface OrientedCurve2D

	public double getWindingAngle(java.awt.geom.Point2D point){
		return conic.getWindingAngle(point);
	}

	public boolean isInside(java.awt.geom.Point2D point){
		return conic.isInside(point);
	}

	
	// ===================================================================
	// methods inherited from interface Curve2D
	

	/* (non-Javadoc)
	 * @see math.geom2d.Curve2D#getIntersections(math.geom2d.LinearShape2D)
	 */
	public Collection<Point2D> getIntersections(LinearShape2D line) {
		return conic.getIntersections(line);
	}

	public double getT0(){
		return conic.getT0();
	}
	
	public double getT1(){
		return conic.getT1();
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Curve2D#getPoint(double)
	 */
	public Point2D getPoint(double t) {
		return conic.getPoint(t);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Curve2D#getFirstPoint()
	 */
	public Point2D getFirstPoint(){
		return conic.getFirstPoint();
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.Curve2D#getLastPoint()
	 */
	public Point2D getLastPoint(){
		return conic.getLastPoint();
	}

	public Collection<Point2D> getSingularPoints(){
		return conic.getSingularPoints();
	}
	
	/**
	 * Always returns false, as an ellipse does not have any singular point.
	 */
	public boolean isSingular(double pos) {
		return conic.isSingular(pos);
	}


	public double getPosition(java.awt.geom.Point2D point){
		return conic.getPosition(point);
	}

	public double project(java.awt.geom.Point2D point){
		return conic.project(point);
	}

	public Conic2D getReverseCurve(){
		return conic.getReverseCurve();
	}

	public Collection<? extends ContinuousCurve2D> getContinuousCurves() {
		return conic.getContinuousCurves();
	}

	public Curve2D getSubCurve(double t0, double t1){
		return conic.getSubCurve(t0, t1);
	}

	
	// ===================================================================
	// methods inherited from interface Shape2D
	
	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#getDistance(math.geom2d.Point2D)
	 */
	public double getDistance(java.awt.geom.Point2D p) {
		return getDistance(p.getX(), p.getY());
	}

	public double getDistance(double x, double y){
		return conic.getDistance(x, y);
	}
	

	public double getSignedDistance(java.awt.geom.Point2D p) {
		return getSignedDistance(p.getX(), p.getY());
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#getSignedDistance(math.geom2d.Point2D)
	 */
	public double getSignedDistance(double x, double y) {
		return conic.getSignedDistance(x, y);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#isBounded()
	 */
	public boolean isBounded() {
		return conic.isBounded();
	}

	public boolean isEmpty(){
		return conic.isEmpty();
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#getClippedShape(math.geom2d.Rectangle2D)
	 */
	public CurveSet2D<? extends ContinuousOrientedCurve2D> clip(Box2D box) {
		return conic.clip(box);
	}

	public Box2D getBoundingBox() {
		return conic.getBoundingBox();
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#transform(math.geom2d.AffineTransform2D)
	 */
	public Conic2D transform(AffineTransform2D trans) {
		return conic.transform(trans);
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#contains(double, double)
	 */
	public boolean contains(double x, double y) {
		return conic.contains(x, y);
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#contains(java.awt.geom.Point2D)
	 */
	public boolean contains(java.awt.geom.Point2D point) {
		return conic.contains(point);
	}

	public void draw(Graphics2D g) {
		conic.draw(g);
	}

	/**
	 * When internal parameters are modified, recompute type, focal...
	 *
	 */
	protected void updateParameters(){
		type=Conic2D.Type.NOT_A_CONIC;
		double eps = Shape2D.ACCURACY;
		
		// First compute the discriminant
		double delta = b*b-4*a*c;
		
		// Case of a parabola (delta==0)
		if(Math.abs(delta)<eps){
			
			// Check degenerate cases (no x^2, y^2 or x*y)
			if(Math.abs(a)<eps && Math.abs(b)<eps && Math.abs(c)<eps){
				if(Math.abs(d)>eps || Math.abs(e)>eps){
					type = Conic2D.Type.STRAIGHT_LINE;
					return;
				}else{
					type = Conic2D.Type.NOT_A_CONIC;
					return;
				}
			}
			
			// Case of a parabola without x^2 parameter
			if(Math.abs(a)<eps){
				
				// degenerate case
				if(Math.abs(d)<eps){
					if(e*e-c*f >= 0) type=Conic2D.Type.TWO_LINES; 
					else type=Conic2D.Type.NOT_A_CONIC;				
				}
				
				type=Conic2D.Type.PARABOLA;
				return;
			}
			
			
			int sgn = a>0 ? 1 : -1;		// signe de a
			a*=sgn; b*=sgn; c*=sgn; d*=sgn; e*=sgn; f*=sgn; 

			sgn = b>0 ? 1 : -1;			// signe de b
			double alpha = Math.sqrt(a);
			double beta = Math.sqrt(c*sgn);

			if(d*alpha + e*beta == 0){
				type=Conic2D.Type.NOT_A_CONIC;
				return;
			}else{
				type=Conic2D.Type.PARABOLA;
				return;
			}
			
			
		} else{ // delta != 0
		
			// coordonnees du centre de la conique
			double x1=(d*c-b*e)/delta;
			double y1=(a*e-b*d)/delta;
			
			double ff = a*x1*x1 + 2*b*x1*y1 + c*y1*y1 + 2*d*x1 + 2*e*y1 + f;
			if(ff==0 && delta<0){
				type=Conic2D.Type.POINT;
				return;
			}else{
				if(ff==0 && delta>0){
					type=Conic2D.Type.TWO_LINES;
					return;
				}
			
				if(a==c && b==0){
					if(ff*a>0){
						type=Conic2D.Type.NOT_A_CONIC; // no solution
						return;
					}else{
						type=Conic2D.Type.CIRCLE;    // conic is a Circle2D
						double length1 = Math.sqrt(-ff/a);
						this.conic = new Circle2D(x1, y1, length1);
						return;
					}
				}
				
				double ac = a-c;
				double racine = Math.sqrt(ac*ac + 4*b*b);
				double lambda = (a+c-racine)*.5;
				double mu = (a+c+racine)*.5;
				
				double la = lambda-a;
				double denom = Math.sqrt(la*la + b*b);
				
				Vector2D vector1 = new Vector2D(b/denom, (lambda-a)/denom);
				Vector2D vector2 = new Vector2D(-(lambda-a)/denom, b/denom);

				if(delta<0){
					if((lambda*ff>0)){		// if lamba and ff have same sign
						type=Conic2D.Type.NOT_A_CONIC; // no solution
						return;
					}else{
						type=Conic2D.Type.ELLIPSE; // c'est une ellipse
						double length1 = Math.sqrt(-ff/lambda);
						double length2 = Math.sqrt(-ff/mu);

						// intervertit les vecteurs directeurs
						if(lambda < 0){
							double xx, yy;
							xx = length1; length1=length2; length2=xx;
							xx = vector1.getX(); yy = vector1.getY();
							vector1.setVector(-yy, xx);
							vector2.setVector(xx, -yy);
						}
						
						double angle = Angle2D.getHorizontalAngle(vector1); 
						this.conic = new Ellipse2D(x1, y1, length1, length2, angle);
						
						return;
					}
				}else{		
					type=Conic2D.Type.HYPERBOLA; // conic is a Hyperbola
					
					return;
				}
			}
		}
	}

}
