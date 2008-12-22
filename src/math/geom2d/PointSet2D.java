/* file : PointSet2D.java
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
 * Created on 5 mai 2006
 *
 */
package math.geom2d;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.*;

import math.geom2d.transform.AffineTransform2D;
/**
 * Represent the union of a finite number of Point2D.
 *
 * @author dlegland
 */
public class PointSet2D implements Shape2D, Iterable<Point2D>{
	//TODO: could consider as Interface, and use different implementations
	
	/**
	 * The inner collection of points composing the set.
	 */
	protected ArrayList<Point2D> points = null;
	
	/**
	 * Creates a new PointSet2D without any points.
	 */
	public PointSet2D(){
		this(0);
	}
	
	/**
	 * Creates a new empty PointSet2D, but preallocates the memory for storing
	 * a given amount of points.
	 * @param n the expected number of points in the PointSet2D.
	 */
	public PointSet2D(int n){
		points = new ArrayList<Point2D>();
	}
	
	/**
	 * Instances of Point2D are directly added, other Point are converted to 
	 * Point2D with the same location.
	 */
	public PointSet2D(java.awt.geom.Point2D[] points){
		this(points.length);
		for(int i=0; i<points.length; i++)
			if(Point2D.class.isInstance(points[i]))
				this.points.add((Point2D) points[i]);
			else
				this.points.add(new Point2D(points[i]));
	}
	
	/**
	 * Points must be a collection of java.awt.Point. Instances of Point2D are
	 * directly added, other Point are converted to Point2D with the same 
	 * location.
	 * @param points
	 */
	public PointSet2D(Collection<? extends java.awt.geom.Point2D> points){
		this(points.size());
		
		for(java.awt.geom.Point2D point : points){
			if(point instanceof Point2D)
				this.points.add((Point2D) point);
			else
				this.points.add(new Point2D(point));
		}
	}
	
	/**
	 * Add a new point to the set of point. If point is not an instance of
	 * Point2D, a Point2D with same location is added instead of point.
	 * @param point
	 */
	public void addPoint(java.awt.geom.Point2D point){
		if(point instanceof Point2D)
			this.points.add((Point2D) point);
		else
			this.points.add(new Point2D(point));
	}

	/**
	 * Add a series of points
	 * @param points an array of points
	 */
	public void addPoints(java.awt.geom.Point2D[] points){
		for(int i=0; i<points.length; i++)
			this.addPoint(points[i]);
	}
	
	public void addPoints(Collection<Point2D> points){
		this.points.addAll(points);
	}
	
	/**
	 * return an iterator on the internal point collection.
	 * @return the collection of points
	 */
	public Iterator<Point2D> getPoints(){
		//TODO: should return a collection to be consistent with other such methods
		return points.iterator();
	}
	
	/**
	 * remove all points of the set.
	 */
	public void clearPoints(){
		this.points.clear();
	}
	
	
	/**
	 * Return the number of points in the set.
	 * @deprecated use getPointNumber() instead
	 * @return the number of points
	 */
	public int getPointsNumber(){
		return points.size();
	}
	
	/**
	 * Returns the number of points in the set.
	 * @return the number of points
	 */
	public int getPointNumber(){
		return points.size();
	}
	
	/**
	 * Return distance to the closest point of the collection
	 */
	public double getDistance(java.awt.geom.Point2D p) {
		return getDistance(p.getX(), p.getY());
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#getDistance(double, double)
	 */
	public double getDistance(double x, double y) {
		if(points.isEmpty()) return Double.NaN;
		double dist = Double.MAX_VALUE;
		for(Point2D point : points)
			dist = Math.min(dist, point.getDistance(x, y));
		return dist;
	}

	/**
	 * always return true.
	 */
	public boolean isBounded() {
		return true;
	}
	
	public boolean isEmpty(){
		return points.size()==0;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#getClippedShape(java.awt.geom.Rectangle2D)
	 */
	public PointSet2D clip(Box2D box) {
		PointSet2D res = new PointSet2D();
		
		for(Shape2D point : points){
			point = point.clip(box);
			if(point instanceof java.awt.geom.Point2D)
				if(point instanceof Point2D)
					res.points.add((Point2D) point);
				else
					res.points.add(new Point2D((java.awt.geom.Point2D)point));
		}
		return res;
	}

	public Box2D getBoundingBox(){
		double xmin = Double.MAX_VALUE;
		double ymin = Double.MAX_VALUE;
		double xmax = Double.MIN_VALUE;
		double ymax = Double.MIN_VALUE;
		
		for(Point2D point : points){
			xmin = Math.min(xmin, point.getX());
			ymin = Math.min(ymin, point.getY());
			xmax = Math.max(xmax, point.getX());
			ymax = Math.max(ymax, point.getY());
		}
		return new Box2D(xmin, xmax, ymin, ymax);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#transform(math.geom2d.AffineTransform2D)
	 */
	public PointSet2D transform(AffineTransform2D trans) {
		PointSet2D res = new PointSet2D();
		
		for(Point2D point : points)
			res.addPoint(point.transform(trans));
		
		return res;
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

	/* (non-Javadoc)
	 * @see java.awt.Shape#contains(double, double)
	 */
	public boolean contains(double x, double y) {
		for(Point2D point : points)
			if(point.getDistance(x, y)<Shape2D.ACCURACY)
				return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#contains(java.awt.geom.Point2D)
	 */
	public boolean contains(java.awt.geom.Point2D point) {
		return contains(point.getX(), point.getY());
	}

	/**
	 * Return true if at least one of the points intersect the rectangle defined by
	 * x0, y0 , w, and y.
	 */
	public boolean intersects(double x0, double y0, double w, double h) {
		for(Point2D point : points)
			if(point.intersects(x0, y0, w, h))
				return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#intersects(java.awt.geom.Rectangle2D)
	 */
	public boolean intersects(java.awt.geom.Rectangle2D rect) {
		return intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	/** Always return false : a point cannot contain a rectangle...*/
	public boolean contains(double arg0, double arg1, double arg2, double arg3) {
		return false;
	}

	/** Always return false : a point cannot contain a rectangle...*/
	public boolean contains(java.awt.geom.Rectangle2D arg0) {
		return false;
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#getPathIterator(java.awt.geom.AffineTransform)
	 */
	public PathIterator getPathIterator(AffineTransform trans) {
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		for(Point2D point : points){
			path.moveTo((float)point.getX(), (float)point.getY());
		}
		
		return path.getPathIterator(trans);
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#getPathIterator(java.awt.geom.AffineTransform, double)
	 */
	public PathIterator getPathIterator(AffineTransform trans, double flatness) {
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		for(Point2D point : points){
			path.moveTo((float)point.getX(), (float)point.getY());
		}
		
		return path.getPathIterator(trans, flatness);
	}

	/**
	 * Draws the point set on the specified Graphics2D, using default radius
	 * equal to 1.
	 * @param g2 the graphics to draw the point set
	 */
	public void draw(Graphics2D g2){
		this.draw(g2, 1);
	}

	/**
	 * Draws the point set on the specified Graphics2D, by filling a disc with
	 * a given radius.
	 * @param g2 the graphics to draw the point set
	 */
	public void draw(Graphics2D g2, double r){		
		for(Point2D point : points)
			g2.fill(new java.awt.geom.Ellipse2D.Double(
					point.x-r, point.y-r, 2*r, 2*r));
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Point2D> iterator() {
		return points.iterator();
	}
}
