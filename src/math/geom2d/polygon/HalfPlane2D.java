/* File HalfPlane2D.java 
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
package math.geom2d.polygon;

import java.awt.Graphics2D;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.UnboundedShapeException;
import math.geom2d.Vector2D;
import math.geom2d.domain.Boundary2DUtils;
import math.geom2d.domain.Domain2D;
import math.geom2d.domain.GenericDomain2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.line.LinearShape2D;
import math.geom2d.transform.AffineTransform2D;

// Imports

/**
 * a Half-plane, delimited by a straight line.
 */
public class HalfPlane2D implements Domain2D{


	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables
	
	protected StraightLine2D line;
	
	
	// ===================================================================
	// constructors
	
	/** Main constructor */
	public HalfPlane2D(LinearShape2D line){
		this.line = new StraightLine2D(line);
	}
	
	
	// ===================================================================
	// methods implementing the Domain2D interface

	/** 
	 * Returns the straight line that defines the limit of this half-plane.
	 */
	public StraightLine2D getBoundary(){
		return line;
	}
	
	public HalfPlane2D complement(){
		return new HalfPlane2D(line.getReverseCurve());
	}
	
	// ===================================================================
	// methods implementing the Shape2D interface

	/** Always returns false, because a half-plane is not bounded.*/
	public boolean isBounded(){return false;}
	
	public boolean isEmpty(){
		return false;
	}

	/**
	 * Gets the distance of the point to the half-plane. This distance is zero
	 * if the point lies inside the plane. 
	 */
	public double getDistance(java.awt.geom.Point2D p){
		return Math.max(line.getSignedDistance(p.getX(), p.getY()), 0);
	}

	/**
	 * Gets the distance of the point to the half-plane. This distance is zero
	 * if the point lies inside the plane. 
	 */
	public double getDistance(double x, double y){
		return Math.max(line.getSignedDistance(x, y), 0);
	}


	/**
	 * Gets the signed distance of the point to this half-plane. Result is negative
	 * if the point lies inside the half-plane. In this case, absolute value of the
	 * distance equals the distance to the limiting line.
	 */
	public double getSignedDistance(java.awt.geom.Point2D p){
		return line.getSignedDistance(p);
	}
	
	public Domain2D clip(Box2D box){
		return new GenericDomain2D(Boundary2DUtils.clipBoundary(
				this.getBoundary(), box));
	}
	
	/** 
	 * Return the new HalfPlane created by an affine transform of this halfplane.
	 */
	public HalfPlane2D transform(AffineTransform2D trans){
		return new HalfPlane2D((StraightLine2D) line.transform(trans));
	}

	/**
	 * Return the bounding box of the half-plane. If support line of the 
	 * half-plane is vertical or horizontal, one of the bounds of the
	 * bounding box is finite.
	 */
	public Box2D getBoundingBox(){
		// check director vector of supporting line
		Vector2D vect = line.getVector();
		
		// check if horizontal
		if(vect.isColinear(new Vector2D(1, 0))){
			Point2D pos = line.getOrigin();
			if(vect.getX()>0)
				return new Box2D(
						Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 
						pos.getY(), Double.POSITIVE_INFINITY);
			else
				return new Box2D(
						Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 
						Double.NEGATIVE_INFINITY, pos.getY());
				
		}

		// check if horizontal
		if(vect.isColinear(new Vector2D(0, 1))){
			Point2D pos = line.getOrigin();
			if(vect.getY()>0)
				return new Box2D(
						Double.NEGATIVE_INFINITY, pos.getX(), 
						Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
			else
				return new Box2D(
						pos.getX(), Double.POSITIVE_INFINITY, 
						Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
				
		}

		// Classical case: all bounds infinite
		return new Box2D(
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	
	// ===================================================================
	// methods inherited from Shape interface

	/** 
	 * Tests if the point lies inside the halfplane.
	 */
	public boolean contains(double x, double y){
		return(line.getSignedDistance(x, y)<=Shape2D.ACCURACY);
	}

	/**
	 * Tests if the point lies inside the halfplane.
	 */
	public boolean contains(java.awt.geom.Point2D p){
		return contains(p.getX(), p.getY());
	}

	/** Tests if the given rectangle lies completely inside the halfplane.*/
	public boolean contains(double x, double y, double w, double h){
		if(!contains(x, y)) return false;
		if(!contains(x+w, y)) return false;
		if(!contains(x+w, y+h)) return false;
		if(!contains(x, y+h)) return false;
		return true;
	}

	/** Tests if the given rectangle lies completely inside the halfplane.*/
	public boolean contains(java.awt.geom.Rectangle2D r){
		return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
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
	 * Tests if the half-plane intersects the given rectangle.
	 */
	public boolean intersects(double x, double y, double w, double h){
		
		return false;
	}

	/**
	 * Tests if the half-plane intersects the given rectangle.
	 */
	public boolean intersects(java.awt.geom.Rectangle2D r){
		return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}

	/** 
	 * Throws an UnboundedShapeException.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform t){
		throw new UnboundedShapeException();	
	}

	/** 
	 * Throws an UnboundedShapeException.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform t, double flatness){
		throw new UnboundedShapeException();	
	}
	
	/** 
	 * Throws an UnboundedShapeException.
	 */
	public void draw(Graphics2D g2){
		throw new UnboundedShapeException();	
	}

	/** 
	 * Throws an UnboundedShapeException.
	 */
	public void fill(Graphics2D g){
		throw new UnboundedShapeException();	
	}
}