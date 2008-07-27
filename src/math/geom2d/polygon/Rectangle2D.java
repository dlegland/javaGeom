/* File Rectangle2D.java 
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

// Imports
import java.util.*;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.line.ClosedPolyline2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * Rectangle2D defines a rectangle rotated around its first corner.
 */
public class Rectangle2D implements Polygon2D{


	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables
	
	protected double x0;
	protected double y0;
	protected double w;
	protected double h;
	protected double theta;
	
	// ===================================================================
	// constructors

	/** Main constructor */
	public Rectangle2D(double x0, double y0, double w, double h, double theta){
		this.x0 = x0;
		this.y0 = y0;
		this.w = w;
		this.h = h;
		this.theta = theta;
	}
	
	/** Empty contructor (size and position zero)*/
	public Rectangle2D(){
		this(0, 0, 0, 0, 0);	
	}

	/** Constructor from awt, to allow easy construction from existing apps.*/
	public Rectangle2D(java.awt.geom.Rectangle2D rect){
		this.x0 = rect.getX();
		this.y0 = rect.getY();
		this.w = rect.getWidth();
		this.h = rect.getHeight();
		this.theta = 0;
	}


	/** Main constructor */
	public Rectangle2D(double x0, double y0, double w, double h){
		this.x0 = x0;
		this.y0 = y0;
		this.w = w;
		this.h = h;
		this.theta = 0;
	}

	/** Main constructor */
	public Rectangle2D(Point2D point, double w, double h, double theta){
		this.x0 = point.getX();
		this.y0 = point.getY();
		this.w = w;
		this.h = h;
		this.theta = theta;
	}

	/** Main constructor */
	public Rectangle2D(Point2D point, double w, double h){
		this.x0 = point.getX();
		this.y0 = point.getY();
		this.w = w;
		this.h = h;
		this.theta = 0;
	}


	
	// ===================================================================
	// accessors

	public double getX(){
		return x0;
	}

	public double getY(){
		return y0;
	}

	public double getWidth(){
		return w;
	}

	public double getHeight(){
		return h;
	}

	public double getTheta(){
		return theta;
	}
	
	// ===================================================================
	// mutators
	
	/**
	 * Apply the characteristics of the given Rectangle to this object.
	 */
	public void setRectangle(Rectangle2D rect){
		this.x0 = rect.x0;
		this.y0 = rect.y0;
		this.w = rect.w;
		this.h = rect.h;
		this.theta = rect.theta;
	}

	/**
	 * Apply the characteristics of the given Rectangle to this object.
	 */
	public void setRectangle(java.awt.geom.Rectangle2D rect){
		this.x0 = rect.getX();
		this.y0 = rect.getY();
		this.w = rect.getWidth();
		this.h = rect.getHeight();
		this.theta = 0;
	}

	/**
	 * Apply the characteristics of the given Rectangle to this object.
	 */
	public void setRectangle(double x, double y, double width, double height, double theta){
		this.x0 = x;
		this.y0 = y;
		this.w = width;
		this.h = height;
		this.theta = theta;
	}

	
	// ===================================================================
	// methods inherited from interface AbstractPolygon2D	
	
	/**
	 * Returns the vertices of the rectangle.
	 * @deprecated use getVertices() method instead.
	 * @return the vertices of the rectangle.
	 */
	@Deprecated
	public Iterator<Point2D> getPoints(){
		AffineTransform2D rot = AffineTransform2D.createRotation(x0, y0, theta);
		ArrayList<Point2D> array = new ArrayList<Point2D>(4);
		
		array.add((Point2D) new Point2D(x0, y0).transform(rot));
		array.add((Point2D) new Point2D(x0+w, y0).transform(rot));
		array.add((Point2D) new Point2D(x0+w, y0+h).transform(rot));
		array.add((Point2D) new Point2D(x0, y0+h).transform(rot));
		
		return array.iterator();
	}		
	
	/**
	 * Returns the vertices of the rectangle as a collection of points.
	 * @return the vertices of the rectangle.
	 */
	public Collection<Point2D> getVertices(){
		AffineTransform2D rot = AffineTransform2D.createRotation(x0, y0, theta);
		ArrayList<Point2D> array = new ArrayList<Point2D>(4);
		
		array.add((Point2D) new Point2D(x0, y0).transform(rot));
		array.add((Point2D) new Point2D(x0+w, y0).transform(rot));
		array.add((Point2D) new Point2D(x0+w, y0+h).transform(rot));
		array.add((Point2D) new Point2D(x0, y0+h).transform(rot));
		
		return array;
	}
	
	public int getVerticesNumber(){
		return 4;
	}
	
	public Collection<LineSegment2D> getEdges(){
		ArrayList<LineSegment2D> edges = new ArrayList<LineSegment2D>(4);
		double cot = Math.cos(theta);
		double sit = Math.sin(theta);
	
		double x1 = w*cot+x0; 
		double y1 = w*sit+y0;
		double x2 = w*cot-h*sit+x0; 
		double y2 = w*sit+h*cot+y0;
		double x3 = -h*sit+x0; 
		double y3 = h*cot+y0;		
		
		edges.add(new LineSegment2D(x0, y0, x1, y1));
		edges.add(new LineSegment2D(x1, y1, x2, y2));
		edges.add(new LineSegment2D(x2, y2, x3, y3));
		edges.add(new LineSegment2D(x3, y3, x0, y0));
		return edges;
	}	

	
	// ===================================================================
	// methods inherited from interface AbstractDomain2D	
	
	public Boundary2D getBoundary(){
		double cot = Math.cos(theta);
		double sit = Math.sin(theta);
		Point2D pts[] = new Point2D[5];
		pts[0] = new Point2D(x0, y0);
		pts[1] = new Point2D(w*cot+x0, w*sit+y0);
		pts[2] = new Point2D(w*cot-h*sit+x0, w*sit+h*cot+y0);
		pts[3] = new Point2D(-h*sit+x0, h*cot+y0);
		pts[4] = new Point2D(x0, y0);
		//return new BoundarySet2D(new ClosedPolyline2D(pts));	
		return new ClosedPolyline2D(pts);
	}	

	
	// ===================================================================
	// methods inherited from Shape2D interface
	
	/** Always returns true, because a rectangle is always bounded.*/
	public boolean isBounded(){
		return true;
	}
	
	public boolean isEmpty(){
		return false;
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
	 * Return the clipped polygon, as an instance of Polygon2D. 
	 * If the Rectangle2D is totally clipped, return EMPTY_SET.
	 */
	public Shape2D clip(Box2D box){
		// Extract the boundary
		ClosedPolyline2D boundary = (ClosedPolyline2D) this.getBoundary();
		
		// to keep intersection points
		ArrayList<Point2D> intersections = new ArrayList<Point2D>();
		
		// iterate on box edges
		for(StraightLine2D line : box.getClippingLines())
			for(Point2D point : boundary.getIntersections(line))
				intersections.add(point);
		
		
		// if no intersection, 3 possibilities:
		// - rectangle totally inside box: return this
		// - box totally inside rectangle: return new rectangle based on box
		// - disjoint sets: return EMPTY_SET
		if(intersections.size()==0){
			if(box.contains(boundary.getFirstPoint()))
				return this;
			if(this.contains(box.getMinX(), box.getMinY()))
				return new Rectangle2D(box.getMinX(), box.getMinY(), box.getWidth(), box.getHeight(), 0);
			return Shape2D.EMPTY_SET;
		}
		
		// sort the intersection points with respect to their position on the boundary
		TreeMap<Double, Point2D> hash = new TreeMap<Double, Point2D>();
		for(Point2D point : intersections)
			hash.put(boundary.getPosition(point), point);
		
		// creates the new polygon
		Point2D[] array = hash.values().toArray(new Point2D[0]);
		return new SimplePolygon2D(array);
	}
	
	/**
	 * Return bounding box of the rectangle.
	 */
	public Box2D getBoundingBox(){
		double xmin = x0;
		double xmax = x0;
		double ymin = y0;
		double ymax = y0;
		double x, y;
		double cot = Math.cos(theta);
		double sit = Math.sin(theta);
		
		x = w*cot+x0; y = w*sit+y0;
		if(xmin>x) xmin=x; if(ymin>y) ymin=y;
		if(xmax<x) xmax=x; if(ymax<y) ymax=y;
		
		x = w*cot-h*sit+x0; y=w*sit+h*cot+y0;
		if(xmin>x) xmin=x; if(ymin>y) ymin=y;
		if(xmax<x) xmax=x; if(ymax<y) ymax=y;
		
		x = h*sit+x0; y=h*cot+y0;
		if(xmin>x) xmin=x; if(ymin>y) ymin=y;
		if(xmax<x) xmax=x; if(ymax<y) ymax=y;
		
		return new Box2D(xmin, xmax, ymin, ymax);
	}

	/** 
	 * Return the new Polygon created by an affine transform of this polygon.
	 */
	public SimplePolygon2D transform(AffineTransform2D trans){
		int nPoints = 4;		
		Point2D[] array = new Point2D[nPoints];
		Point2D[] res = new Point2D[nPoints];
		Iterator<Point2D> iter = this.getVertices().iterator();
		for(int i=0; i<nPoints; i++){
			array[i] = (Point2D) iter.next();
			res[i] = new Point2D();
		}
		
		trans.transform(array, res);
		return new SimplePolygon2D(res);
	}
	
	
	// ===================================================================
	// methods inherited from Shape interface

	/**
	 * This method simply invoke ancestor method. It is redefined to avoid ambiguity
	 * with contains(Shape2D).
	 */
	public boolean contains(java.awt.geom.Point2D point){
		return contains(point.getX(), point.getY());
	}
	
	public boolean contains(double x, double y){
		double cot = Math.cos(theta);
		double sit = Math.sin(theta);
	
		double x1 = w*cot+x0; 
		double y1 = w*sit+y0;
		double x2 = w*cot-h*sit+x0; 
		double y2 = w*sit+h*cot+y0;
		double x3 = -h*sit+x0; 
		double y3 = h*cot+y0;
		
		StraightLine2D line = new StraightLine2D(x0, y0, x1-x0, y1-y0);
		if(line.getSignedDistance(x, y)>0) return false;
		line = new StraightLine2D(x1, y1, x2-x1, y2-y1);		
		if(line.getSignedDistance(x, y)>0) return false;
		line = new StraightLine2D(x2, y2, x3-x2, y3-y2);		
//		line.setPoints(x2, y2, x3, y3);		
		if(line.getSignedDistance(x, y)>0) return false;
		line = new StraightLine2D(x3, y3, x0-x3, y0-y3);		
//		line.setPoints(x3, y3, x0, y0);		
		if(line.getSignedDistance(x, y)>0) return false;
		return true;
	}
	
	public boolean contains(double x, double y, double w, double h){
		if(!contains(x, y)) return false;
		if(!contains(x+w, y)) return false;
		if(!contains(x+w,y+h)) return false;
		if(!contains(x, y+h)) return false;
		return false;
	}
	
	public boolean contains(java.awt.geom.Rectangle2D rect){
		return contains(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}
	
	/**
	 * Test if the specified Shape is totally contained in this Rectangle.
	 * Note that the test is performed on the bounding box of the shape, then
	 * for rotated rectangles, this method can return false with a shape totally
	 * contained in the rectangle. The problem does not exist for horizontal
	 * rectangle, since edges of rectangle and bounding box are parallel.
	 */
	public boolean containsBounds(Shape2D shape){
		// check if shape is bounded
		if(!shape.isBounded())
			return false;
		
		// If at least one vertex is inside rectangle, return false
		Collection<Point2D> points = new Box2D(shape.getBounds2D()).getVertices();
		for(Point2D point : points)
			if(!this.contains(point)) 
				return false;

		// Otherwise return true
		return true;
	}
	
	public boolean intersects(double x0, double y0, double w0, double h0){
		return false;
	}
	
	public boolean intersects(java.awt.geom.Rectangle2D r){
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

	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform t){
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		double cot = Math.cos(theta);
		double sit = Math.sin(theta);
		
		path.moveTo((float)x0, (float)y0);
		path.lineTo((float)(x0+w*cot), (float)(w*sit+y0));
		path.lineTo((float)(w*cot-h*sit+x0), (float)(w*sit+h*cot+y0));
		path.lineTo((float)(-h*sit+x0), (float)(h*cot+y0));
		path.lineTo((float)x0, (float)y0);
				
		return path.getPathIterator(t);
	}

	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform t, double flatness){
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		double cot = Math.cos(theta);
		double sit = Math.sin(theta);
		
		path.moveTo((float)x0, (float)y0);
		path.lineTo((float)(x0+w*cot), (float)(w*sit+y0));
		path.lineTo((float)(w*cot-h*sit+x0), (float)(w*sit+h*cot+y0));
		path.lineTo((float)(-h*sit+x0), (float)(h*cot+y0));
		path.lineTo((float)x0, (float)y0);
				
		return path.getPathIterator(t, flatness);
	}
	
	
	// ===================================================================
	// methods inherited from Object interface
	
	/**
	 * Test if retangles are the same. We consider two rectangles are equals
	 * if their corners are the same. Then, we can have different origin and
	 * different angles, but equal rectangles.
	 */
	public boolean equals(Object obj){		
		// check class, and cast type
		if(!(obj instanceof Rectangle2D))
			return false;		
		Rectangle2D rect = (Rectangle2D) obj;
		
		// first get list of corners of the 2 rectangles.
//		Iterator<Point2D> iter1 = this.getPoints();
//		Point2D point;
		
		// check all 4 corners of the first rectangle
//		while(iter1.hasNext()){
//			point = (Point2D) iter1.next();
		boolean ok;
		for(Point2D point : this.getVertices()){
			ok = false;
			
			// compare with all 4 corners of second rectangle
//			Iterator<Point2D> iter2 = rect.getPoints();
//			while(iter2.hasNext())
//				if(point.equals(iter2.next()))
//					ok = true;
			for(Point2D point2 : rect.getVertices())
				if(point.equals(point2)){
					ok = true;
					break;
				}
			
			// if the point does not belong to the corners of the other rectangle,
			// then the two rect are different
			if(!ok) return false;
		}
		
		// test ok for 4 corners, then the two rectangles are the same.
		return true;
	}	

	
}