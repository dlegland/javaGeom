/* File HRectangle2D.java 
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

import java.util.*;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.BoundarySet2D;
import math.geom2d.line.ClosedPolyline2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.transform.AffineTransform2D;

// Imports

/**
 * HRectangle2D defines a rectangle with edges parallel to main axis. Thus,
 * it can not be rotated, contrary to Rectangle2D. This class is actually 
 * simply a wrapper of class <code>java.awt.geom.Rectangle2D.Double</code>
 * with interface <code>AbstractPolygon</code>.
 */
public class HRectangle2D extends java.awt.geom.Rectangle2D.Double implements PolygonalShape2D{

	// ===================================================================
	// constants
	
	private static final long serialVersionUID = 1L;


	// ===================================================================
	// class variables
	
	// ===================================================================
	// constructors

	/** Main constructor */
	public HRectangle2D(double x0, double y0, double w, double h){
		super(x0, y0, w, h);
	}
	
	/** Empty constructor (size and position zero)*/
	public HRectangle2D(){
		super(0, 0, 0, 0);	
	}

	/** Constructor from awt, to allow easy construction from existing apps.*/
	public HRectangle2D(java.awt.geom.Rectangle2D rect){
		super(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}


	/** Main constructor */
	public HRectangle2D(Point2D point, double w, double h){
		super(point.getX(), point.getY(), w, h);
	}


	
	// ===================================================================
	// query states


	/** Always returns true, because a rectangle is always bounded.*/
	public boolean isBounded(){return true;}
	
	/**
	 * Test if the specified Shape is totally contained in this Rectangle.
	 * Note that the test is performed on the bounding box of the shape, then
	 * for rotated rectangles, this method can return false with a shape totally
	 * contained in the rectangle. The problem does not exist for horizontal
	 * rectangle, since edges of rectangle and bounding box are parallel.
	 */
	public boolean containsBounds(Shape2D shape){
		if(!shape.isBounded()) return false;
		for(Point2D vertex : new Box2D(shape.getBounds()).getVertices())
			if(!contains(vertex)) return false;

		return true;
	}
	
	
	// ===================================================================
	// accessors
		
	/**
	 * @deprecated use getVertices() instead.
	 */
	@Deprecated
	public Iterator<Point2D> getPoints(){
		ArrayList<Point2D> points = new ArrayList<Point2D>(4);
		points.add(new Point2D(x, y));
		points.add(new Point2D(x+width, y));
		points.add(new Point2D(x+width, y+height));
		points.add(new Point2D(x, y+height));
		return points.iterator();
	}
	
	public Collection<Point2D> getVertices(){
		ArrayList<Point2D> points = new ArrayList<Point2D>(4);
		points.add(new Point2D(x, y));
		points.add(new Point2D(x+width, y));
		points.add(new Point2D(x+width, y+height));
		points.add(new Point2D(x, y+height));
		return points;
	}

	public int getVerticesNumber(){
		return 4;
	}
	
	public Collection<LineSegment2D> getEdges(){
		ArrayList<LineSegment2D> edges = new ArrayList<LineSegment2D>(4);
		edges.add(new LineSegment2D(x, y, x+width, y));
		edges.add(new LineSegment2D(x+width, y, x+width, y+height));
		edges.add(new LineSegment2D(x+width, y+height, x, y+height));
		edges.add(new LineSegment2D(x, y+height, x, y));
		return edges;
	}

	public Boundary2D getBoundary(){
		Point2D pts[] = new Point2D[5];
		pts[0] = new Point2D(x, y);
		pts[1] = new Point2D(width+x, y);
		pts[2] = new Point2D(width+x, y+height);
		pts[3] = new Point2D(x, y+height);
		pts[4] = new Point2D(x, y);
		return new BoundarySet2D<ClosedPolyline2D>(new ClosedPolyline2D(pts));	
	}
	

	public double getDistance(java.awt.geom.Point2D p){
		return Math.max(getSignedDistance(p.getX(), p.getY()), 0);
	}
	
	public double getDistance(double x, double y){
		return Math.max(getSignedDistance(x, y), 0);
	}
	
	/**
	 * Get the signed distance of the shape to the given point : this distance is
	 * positive if the point lies outside the shape, and is negative if the point
	 * lies inside the shape. In this case, absolute value of distance is equals to 
	 * the distance to the border of the shape.
	 */
	public double getSignedDistance(java.awt.geom.Point2D p){
		return getSignedDistance(p.getX(), p.getY());
	}

	/**
	 * Get the signed distance of the shape to the given point : this distance is
	 * positive if the point lies outside the shape, and is negative if the point
	 * lies inside the shape. In this case, absolute value of distance is equals to 
	 * the distance to the border of the shape.
	 */
	public double getSignedDistance(double x, double y){
		double dist = getBoundary().getDistance(x, y);
		if(contains(x, y)) return -dist;
		else return dist;
	}
	
	/**
	 * return the clipping of the rectangle, as an instance of HRectangle2D.
	 * If rectangle is outside clipping box, return an instance of HRectangle
	 * with 0 width and height.
	 */
	public Shape2D getClippedShape(Box2D box){
		double xmin = Math.max(this.getMinX(), box.getMinX());
		double xmax = Math.min(this.getMaxX(), box.getMaxX());
		double ymin = Math.max(this.getMinY(), box.getMinY());
		double ymax = Math.min(this.getMaxY(), box.getMaxY());
		if(xmin>xmax || ymin>ymax)
			return new HRectangle2D(xmin, ymin, 0, 0);
		else
			return new HRectangle2D(xmin, xmax, xmax-xmin, ymax-ymin);
	}

	/**
	 * Returns the clipping of the rectangle, as an instance of HRectangle2D.
	 * If rectangle is outside clipping box, returns an instance of HRectangle
	 * with 0 width and height.
	 */
	public Shape2D clip(Box2D box){
		double xmin = Math.max(this.getMinX(), box.getMinX());
		double xmax = Math.min(this.getMaxX(), box.getMaxX());
		double ymin = Math.max(this.getMinY(), box.getMinY());
		double ymax = Math.min(this.getMaxY(), box.getMaxY());
		if(xmin>xmax || ymin>ymax)
			return new HRectangle2D(xmin, ymin, 0, 0);
		else
			return new HRectangle2D(xmin, xmax, xmax-xmin, ymax-ymin);
	}

	public Box2D getBoundingBox(){
		return new Box2D(this.getMinX(), this.getMaxX(), this.getMinY(), this.getMaxY());
	}
	

	// ===================================================================
	// mutators
	

	// ===================================================================
	// general methods
	
	/**
	 * Test if rectangles are the same. We consider two rectangles are equal
	 * if their corners are the same. Then, we can have different origins and
	 * different angles, but equal rectangles.
	 */
	public boolean equals(Object obj){
			
			// check class, and cast type
			if(!(obj instanceof HRectangle2D))
				return false;		
			HRectangle2D rect = (HRectangle2D) obj;
			
			// check all 4 corners of the first rectangle
			boolean ok;
			for(Point2D point : this.getVertices()){
				ok = false;
				
				// compare with all 4 corners of second rectangle
				for(Point2D point2 : rect.getVertices())
					if(point.equals(point2))
						ok = true;
				
				// if the point does not belong to the corners of the other rectangle,
				// then the two rectangles are different
				if(!ok) return false;
			}
			
			// test ok for 4 corners, then the two rectangles are the same.
			return true;
		}	
	
	
	// ===================================================================
	// general methods
	
	/** 
	 * Return the new Polygon created by an affine transform of this polygon.
	 */
	public Polygon2D transform(AffineTransform2D trans){
		int nPoints = 4;		
		Point2D[] array = new Point2D[nPoints];
		Point2D[] res = new Point2D[nPoints];
		Iterator<Point2D> iter = this.getVertices().iterator();
		for(int i=0; i<nPoints; i++){
			array[i] = iter.next();
			res[i] = new Point2D();
		}
		
		trans.transform(array, res);
		return new Polygon2D(res);
	}

	
}