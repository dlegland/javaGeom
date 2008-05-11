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

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.line.StraightObject2D;
import math.geom2d.polygon.Polygon2D;
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
	public HalfPlane2D(StraightObject2D line){
		this.line = new StraightLine2D(line);
	}
	
	
	// ===================================================================
	// methods inherited from AbstractDomain2D interface

	/** 
	 * Returns the straight line that defines the limit of this half-plane.
	 */
	public Boundary2D getBoundary(){
		return line;
	}
	
	
	// ===================================================================
	// methods inherited from Shape2D interface

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
	
	public Shape2D clip(Box2D box){
		// edges and corners of the rectangle
		// get vertices of rectangle
		double x = box.getMinX();
		double y = box.getMinY();
		double w = box.getWidth();
		double h = box.getHeight();
		
		// create lines corresponding to rectangle edges.
		LineSegment2D[] edge = new LineSegment2D[4];
		edge[0] = new LineSegment2D(x, y, x+w, y);
		edge[1] = new LineSegment2D(x+w, y, x+w, y+h);
		edge[2] = new LineSegment2D(x+w, y+h, x, y+h);
		edge[3] = new LineSegment2D(x, y+h, x, y);
		Point2D[] corners = new Point2D[4];
		corners[0] = new Point2D(x, y);
		corners[1] = new Point2D(x+w, y);
		corners[2] = new Point2D(x+w, y+h);
		corners[3] = new Point2D(x, y+h);
		
		System.out.println("clip halfplane");
		// if clipping rectangle lies entirely inside the halplane, returns
		// a copy of the clipping rectangle.
		if( contains(x, y) && contains(x+w, y) &&
			contains(x+w, y+h) && contains(x, y+h) )
				return box.getAsRectangle();
		
		// compute intersection points between line and rectangle edges.
		Point2D point1=null;	// two intersection points
		Point2D point2=null;		
		double t1=0, t2=0; 	// positions of points along the boundary of rectangle.
		for(int i=0; i<4; i++){
			point1 = line.getIntersection(edge[i]);
			if(point1!=null){
				System.out.println("intersect : " + i + " -> " + point1.getX() + " " + point1.getY());
				t1 = edge[i].getPosition(point1)+i;
				for(int i2=i+1; i2<4; i2++){
					point2 = line.getIntersection(edge[i2]);
					if(point2!=null){
						t2 = edge[i2].getPosition(point2)+i2;
						System.out.println("intersect : " + i2 + " -> " + point2.getX() + " " + point2.getY());
						break;
					}
				}
				break; 
			}
		}
		
		// if no intersection point, rectangle is outside the halfplane : return null.
		if(point1==null){
			System.out.println("no intersection point");
			return null;
		}

		if(point2==null){
			System.out.println("only one intersection point : What ! ? !");
		}
		// there is only two intersection points : point1 and point2, whose position is
		// given by t1 and t2.
		
		// we first check that position of point1 on the line is before position of
		// point2. If not, we exchange them.
		System.out.println("points position :" + line.getPositionOnLine(point1) + "  " +
			line.getPositionOnLine(point2) );
		if(line.getPositionOnLine(point1)>line.getPositionOnLine(point2)){
			Point2D tmpPoint = point1;
			point1 = point2;
			point2 = tmpPoint;
			double tmp = t1;
			t1 = t2;
			t2 = tmp;
		}
		
		// compute number of points of result polygon
		int n;
		if(t2>t1)   // polygon contains first vertex of bounding rectangle.
			n = (int)(6 - Math.floor(t2) + Math.floor(t1));
		else	// polygon does not contain first vertex
			n = (int)(2 - Math.floor(t2) + Math.floor(t1));
		System.out.println("t1 = " + t1 + "  t2 = " + t2);
		System.out.println(n + " points for polygon");
		// create Points of polygon
		Point2D[] point = new Point2D[n];
		point[0] = point1;
		point[1] = point2;
		int p=2;
		
		// now, add some vertices of the rectangle
		
		// if contains first vertex, add vertices between point2 and first vertex
		// dt is current index of rectangle corner, from 0->4.
		int dt = (int)(Math.floor(t2)+1);
		if(t2>t1){
			// add vertices between point2 and vertex 1.
			while(dt<4)	point[p++] = corners[dt++];

			// add first vertex, and reset corner index.
			point[p++] = corners[0];
			dt=1;
		}
	
		// add vertices from current vertex to point1
		while(dt<t1)
			point[p++] = corners[dt++];
		
		return new Polygon2D(point);
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
	 * Returns PathIterator of halfplane clipped by the default clipping window.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform t){
		return clip(defaultClipWindow).getPathIterator(t);	
	}

	/** 
	 * Returns PathIterator of halfplane clipped by the default clipping window.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform t, double flatness){
		return clip(defaultClipWindow).getPathIterator(t, flatness);
	}
}