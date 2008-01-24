/* File Point2D.java 
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

import math.geom2d.transform.AffineTransform2D;

// Imports

/**
 * Define a vector in 2 Dimensions. Provides methods to compute cross product
 * and dot product, addition and subtraction of vectors.
 */
public class Vector2D{


	// ===================================================================
	// constants
	
	
	// ===================================================================
	// class variables
	
	protected double dx=1;
	protected double dy=0;
	
	
	// ===================================================================
	// constructors
	
	/** construct a new Vectors initialized with dx=1 and dy=0. */
	public Vector2D(){
		this(1, 0);
	}
	
	/**
	 * construct a new vector between origin and the point.
	 */
	public Vector2D(java.awt.geom.Point2D point){
		this(point.getX(), point.getY());
	}

	/**
	 * construct a new vector between two points
	 */
	public Vector2D(java.awt.geom.Point2D point1, java.awt.geom.Point2D point2){
		this(point2.getX()-point1.getX(), point2.getY()-point1.getY());
	}

	/** constructor with given position. */
	public Vector2D(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
	}
	
	
	// ===================================================================
	// static functions

	public final static Vector2D createPolar(double rho, double theta){
		return new Vector2D(rho*Math.cos(theta), rho*Math.sin(theta));
	}

	/**
	 * Get the dot product of the two vectors, defined by : <p>
	 * <code> dx1*dy2 + dx2*dy1</code> <p>
	 * Dot product is zero if the vectors defined by the 2 vectors are 
	 * orthogonal. It is positive if vectors are in the same direction, and
	 * negative if they are in opposite direction. 
	 */
	public final static double dot(Vector2D v1, Vector2D v2){
		return v1.getDx()*v2.getDx()+v1.getDy()*v2.getDy();
	}
	
	/**
	 * get the cross product of the two vectors, defined by : <p>
	 * <code> dx1*dy2 - dx2*dy1</code><p>
	 * cross product is zero for colinear vectors. It is positive if angle
	 * between vector 1 and vector 2 is comprised between 0 and PI, and
	 * negative otherwise.
	 */
	public final static double cross(Vector2D v1, Vector2D v2){
		return v1.getDx()*v2.getDy()-v2.getDx()*v1.getDy();
	}	
	
	/**
	 * test if the two vectors are colinear
	 * @return true if the vectors are colinear
	 */
	public final static boolean isColinear(Vector2D v1, Vector2D v2){
		v1 = v1.getNormalizedVector();
		v2 = v2.getNormalizedVector();
		return Math.abs(v1.getDx()*v2.getDy() - v1.getDy()*v2.getDx())<Shape2D.ACCURACY;
	}
	
	/**
	 * test if the two vectors are orthogonal
	 * @return true if the vectors are orthogonal
	 */
	public final static boolean isOrthogonal(Vector2D v1, Vector2D v2){			
		v1 = v1.getNormalizedVector();
		v2 = v2.getNormalizedVector();
		return Math.abs(v1.getDx()*v2.getDx()+v1.getDy()*v2.getDy())<Shape2D.ACCURACY;
	}
	
	
	// ===================================================================
	// accessors

	public double getDx(){
		return dx;
	}
	
	public double getDy(){
		return dy;
	}
	

	// ===================================================================
	// modifiers

	public void setDx(double dx){
		this.dx = dx;
	}
	
	public void setDy(double dy){
		this.dy = dy;
	}
	
	public void setVector(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
	}
	
	
	/** 
	 * Set location specified as polar coordinate : distance from origin +
	 * angle with horizontal.
	 */
	public void setAsPolar(double rho, double theta){
		dx = rho*Math.cos(theta);
		dy = rho*Math.sin(theta);
	}
	
	/**
	 * Returns the opposite vector v2 of this, such that the sum of this and
	 * v2 equals the null vector.
	 * @return the vector opposite to <code>this</code>.
	 */
	public Vector2D getOpposite(){
		return new Vector2D(-dx, -dy);
	}

	/**
	 * Computes the norm of the vector
	 * @return the euclidean norm of the vector
	 */
	public double getNorm(){
		return Math.hypot(dx, dy);
	}
	
	/**
	 * Returns the angle with the horizontal axis, in radians.
	 * @return the horizontal angle
	 */
	public double getAngle(){
		return Angle2D.getHorizontalAngle(this);
	}
	
	/**
	 * Normalizes the vector, such that its norms becomes 1.
	 */	
	public void normalize(){
		double r = Math.hypot(this.dx, this.dy);
		this.dx = this.dx/r;
		this.dy = this.dy/r;
	}
	
	/**
	 * Returns the vector with same direction as this one, but with norm equal
	 * to 1.
	 */	
	public Vector2D getNormalizedVector(){
		double r = Math.hypot(this.dx, this.dy);
		return new Vector2D(this.dx/r, this.dy/r);
	}
	
	
	// ===================================================================
	// compare with other vectors

	/**
	 * test if the two vectors are colinear
	 * @return true if the vectors are colinear
	 */
	public boolean isColinear(Vector2D v){
		return Vector2D.isColinear(this, v);
	}
	
	/**
	 * test if the two vectors are orthogonal
	 * @return true if the vectors are orthogonal
	 */
	public boolean isOrthogonal(Vector2D v){			
		return Vector2D.isOrthogonal(this, v);
	}
	
	
	// ===================================================================
	// operations between vectors
	
	/**
	 * Get the dot product with point <code>p</code>. 
	 * Dot product id defined by : <p>
	 * <code> x1*y2 + x2*y1</code> <p>
	 * Dot product is zero if the vectors defined by the 2 points are 
	 * orthogonal. It is positive if vectors are in the same direction, and
	 * negative if they are in opposite direction. 
	 */
	public double dot(Vector2D v){
		return dx*v.getDx()+dy*v.getDy();
	}
	
	/**
	 * Get the cross product with point <code>p</code>. Cross product is 
	 * defined by : <p>
	 * <code> x1*y2 - x2*y1</code><p>
	 * cross product is zero for colinear vector. It is positive if angle
	 * between vector 1 and vector 2 is comprised between 0 and PI, and
	 * negative otherwise.
	 */
	public double cross(Vector2D v){
		return dx*v.getDy()-v.getDx()*dy;
	}
	
	/**
	 * Return the sum of current vector with vector given as parameter.
	 * Inner fields are not modified.
	 */
	public Vector2D plus(Vector2D v){
		return new Vector2D(dx+v.getDx(), dy+v.getDy());
	}
	
	/**	
	 * Return the subtraction of current vector with vector given as parameter.
	 * Inner fields are not modified.
	 */
	public Vector2D minus(Vector2D v){
		return new Vector2D(dx-v.getDx(), dy-v.getDy());
	}
	
	
	/**
	 * Transform the vector, by using only the first 4 parameters of the 
	 * transform. Translation of a vector returns the same vector.
	 * @param trans an affine transform
	 * @return the transformed vector.
	 */
	public Vector2D transform(AffineTransform2D trans){
		double[] tab = trans.getCoefficients();
		return new Vector2D(
			dx*tab[0] + dy*tab[1],
			dx*tab[3] + dy*tab[4]);
	}
	
	/** 
	 * Test whether this object is the same as another vector.
	 */
	public boolean equals(Object obj){
		if(!(obj instanceof Vector2D)) return false;
		Vector2D p = (Vector2D) obj;
		return (Math.abs(p.getDx()-dx)<Shape2D.ACCURACY &&
		 		Math.abs(p.getDy()-dy)<Shape2D.ACCURACY);
	}
	

}