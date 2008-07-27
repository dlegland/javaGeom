/* File SimplePolygon2D.java 
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
import math.geom2d.domain.Boundary2DUtils;
import math.geom2d.domain.BoundarySet2D;
import math.geom2d.domain.ContinuousBoundary2D;
import math.geom2d.line.ClosedPolyline2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * Represent a polygonal domain whose boundary is a single closed polyline.
 * 
 */
public class SimplePolygon2D implements Polygon2D{


	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables
	
	/**
	 * The inner ordered list of vertices. The last point is connected to the first one.
	 */
	protected ArrayList<Point2D> points = new ArrayList<Point2D>();
	
	// ===================================================================
	// constructors
	
	/**
	 * Empty constructor: no vertex.
	 */
	public SimplePolygon2D(){
	}
	
	/**
	 * Constructor from an array of points
	 * @param tab the vertices stored in an array of Point2D
	 */
	public SimplePolygon2D(Point2D[] tab){
		points = new ArrayList<Point2D>(tab.length);
		for(int i=0; i<tab.length; i++)
			points.add(tab[i]);
	}
	
	/**
	 * Constructor from two arrays, one for each coordinate.
	 * @param xcoords the x coordinate of each vertex
	 * @param ycoords the y coordinate of each vertex
	 */
	public SimplePolygon2D(double[] xcoords, double[] ycoords){
		points = new ArrayList<Point2D>(xcoords.length);
		for(int i=0; i<xcoords.length; i++)
			points.add(new Point2D(xcoords[i], ycoords[i]));
	}
	
	public SimplePolygon2D(Collection<? extends Point2D> points){
		this.points = new ArrayList<Point2D>(points.size());
		this.points.addAll(points);
	}

	// ===================================================================
	// methods specific to SimplePolygon2D
	
	/** 
	 * Add a point as the last vertex.
	 */
	public void addPoint(Point2D point){
		this.points.add(point);
	}
	
	/**
	 * Remove a vertex of the polygon.
	 * @param point the vertex to be removed.
	 */
	public void removePoint(Point2D point){
		this.points.remove(point);
	}
	
	/**
	 * Computes area of the polygon, by returning the absolute value of the
	 * signed area.
	 */
	public double getArea(){
		return Math.abs(this.getSignedArea());
	}
	
	/**
	 * Computes the signed area of the polygon. Algorithm is taken from page:
	 * <a href="http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/">
	 * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/</a>.
	 * Signed are is positive if polygon is oriented counter-clockwise, and
	 * negative otherwise. Result is wrong if polygon is self-intersecting.
	 * @return the signed area of the polygon.
	 */
	public double getSignedArea(){
		double area = 0;
		Point2D prev = this.points.get(points.size()-1);
		for(Point2D point : this.points){
			area += prev.getX()*point.getY() - prev.getY()*point.getX();
			prev = point;
		}
		return area /= 2;
	}
	
	/**
	 * Computes the centroid (center of mass) of the polygon. 
	 * Algorithm is taken from page:
	 * <a href="http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/">
	 * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/</a>.
	 * @return the centroid of the polygon
	 */
	public Point2D getCentroid(){
		double xc = 0;
		double yc = 0;
		double tmp = 0;
		Point2D prev = this.points.get(points.size()-1);
		for(Point2D point : this.points){
			tmp = prev.getX()*point.getY() - prev.getY()*point.getX();
			xc += tmp*(point.getX() + prev.getX());
			yc += tmp*(point.getY() + prev.getY());
			prev = point;
		}
		double area = this.getSignedArea()*6;
		return new Point2D(xc/area, yc/area);
	}
	
	/**
	 * Computes the winding number of the polygon. Algorithm adapted from
	 * http://www.geometryalgorithms.com/Archive/algorithm_0103/algorithm_0103.htm
	 * @param x the x-coordinate of the point
	 * @param y the y-coordinate of the point
	 * @return the number of windings of the curve around the point
	 */
	public int getWindingNumber(double x, double y){
	   int wn = 0;    // the winding number counter

	   Point2D point = new Point2D(x, y);
	   Point2D previous = points.get(points.size()-1);
	   double x1 = previous.getX();
	   double y1 = previous.getY();
	   double x2, y2;
	   
	   for(Point2D p : points){
		   // second vertex of current edge
		   x2 = p.getX();
		   y2 = p.getY();
		   
		   if(y1<=y){
			   if(y2>y)	// an upward crossing
				   if (new LineSegment2D(x1, y1, x2, y2).isInside(point))
					   wn++;
		   }else{
			   if(y2<=y)	// a downward crossing
				   if (!(new LineSegment2D(x1, y1, x2, y2).isInside(point)))
					   wn--;
		   }

		   // for next iteration
		   x1 = x2;
		   y1 = y2;
	   }

	   return wn;
	}

	// ===================================================================
	// methods inherited from AbstractSimplePolygon2D interface
	
	/**
	 * Returns the points of the polygon. The result is a pointer to the inner
	 * collection of vertices.
	 */
	public Collection<Point2D> getVertices(){
		return points;
	}

	/**
	 * Return the number of vertices.
	 */
	public int getVerticesNumber(){
		return points.size();
	}
	
	/**
	 * Return the set of edges, as a collection of LineSegment2D.
	 */
	public Collection<LineSegment2D> getEdges(){
		
		int nPoints = this.points.size();		
		ArrayList<LineSegment2D> edges = new ArrayList<LineSegment2D>(nPoints);
		
		if(nPoints==0) return edges;
		
		for(int i=0; i<nPoints-1; i++)
			edges.add(new LineSegment2D((Point2D) points.get(i),
				(Point2D) points.get(i+1)));
		
		edges.add(new LineSegment2D((Point2D)points.get(nPoints-1),
			(Point2D)points.get(0)));
		
		return edges;
	}

	/**
	 * remove all the vertices of the polygon.
	 */
	public void clearPoints(){
		this.points.clear();
	}
	
	// ===================================================================
	// methods inherited from Domain2D interface
	
	/** 
	 * Returns a closed polyline, which encloses the polygon.
	 */
	public Boundary2D getBoundary(){
		Point2D[] array = new Point2D[this.points.size()];
		for(int i=0; i<this.points.size(); i++)
			array[i] = (Point2D) this.points.get(i);
		return new ClosedPolyline2D(array); 
	}


	// ===================================================================
	// methods inherited from Shape2D interface

	/**
	 * Get the distance of the point to the polygon. This is actually the minimal
	 * distance computed for each edge if the polygon, or ZERO if the point
	 * belong to the polygon.
	 */
	public double getDistance(java.awt.geom.Point2D p){
		return getDistance(p.getX(), p.getY());
	}


	/**
	 * Get the distance of the point to the polygon. This is actually the minimal
	 * distance computed for each edge if the polygon, or ZERO if the point
	 * belong to the polygon.
	 */
	public double getDistance(double x, double y){
		if(contains(x, y)) return 0;
		return getBoundary().getDistance(x, y);
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
	 * Return the shape formed by the polygon clipped by the given box. 
	 */
	public Shape2D clip(Box2D box){
		BoundarySet2D<ContinuousBoundary2D> boundarySet = 
			Boundary2DUtils.clipBoundary(this.getBoundary(), box);
			
		//TODO: should return an instance of MultiPolygon2D.
		return boundarySet;
	}
	
	/**
	 * Return the bounding box of the polygon.
	 */
	public Box2D getBoundingBox(){
		return getBoundary().getBoundingBox();
	}

	/** 
	 * Always returns true if polygon is oriented counter-clockwise, 
	 * false otherwise.
	 */
	public boolean isBounded(){
		return this.getSignedArea()>0;
	}

	public boolean isEmpty(){
		return points.size()==0;
	}
	/** 
	 * Return the new Polygon created by an affine transform of this polygon.
	 */
	public SimplePolygon2D transform(AffineTransform2D trans){
		int nPoints = this.points.size();
		
		Point2D[] array = new Point2D[nPoints];
		Point2D[] res = new Point2D[nPoints];		
		for(int i=0; i<nPoints; i++){
			array[i] = (Point2D) this.points.get(i);
			res[i] = new Point2D();
		}
		
		trans.transform(array, res);
		return new SimplePolygon2D(res);
	}


	// ===================================================================
	// methods inherited from Shape interface

	/** return false, because a line cannot contain a rectangle.*/
	public boolean contains(double x, double y, double w, double h){
		return false;
	}

	/** return false, because a line cannot contain a rectangle.*/
	public boolean contains(java.awt.geom.Rectangle2D r){
		return false;
	}
	
	/** 
	 * Return true if the point p lies inside the polygon, with precision given by 
	 * Shape2D.ACCURACY.
	 */
	public boolean contains(java.awt.geom.Point2D p){
		return contains(p.getX(), p.getY());
	}

	/** 
	 * Return true if the point (x, y) lies inside the polygon, with precision given 
	 * by Shape2D.ACCURACY.
	 */
	public boolean contains(double x, double y){
		return this.getWindingNumber(x, y)==1 || this.getBoundary().contains(x, y);
	}

// The following is a old algorithm, based on the principle:
// find closest edge, and check if point is located on the left side of edge.
// If point is close to a vertex, chech if point is located on the left side
// of each neighbor edges.
// Not used because to complicated, but seems to work....
//
//	/** 
//	 * Return true if the point (x, y) lies inside the polygon, with precision given 
//	 * by Shape2D.ACCURACY.
//	 */
//	public boolean contains(double x, double y){
//		//return false;
//		
//		// Old version of algorithm, does not work properly
//		// for self crossing polygons.
//		
//		
//		double dist=1, minDist;
//		Collection<LineSegment2D> edges = getEdges();
//		Vector<LineSegment2D> closeEdges = new Vector<LineSegment2D>(4);
//		
//		// first, compute min distance and store all edges sharing
//		// this minimal distance to test point
//		minDist = Double.POSITIVE_INFINITY;
//		for(LineSegment2D edge : edges){
//			dist = edge.getDistance(x, y);
//			if(Math.abs(dist-minDist)<Shape2D.ACCURACY)
//				closeEdges.add(edge);
//			else if(dist<minDist){
//				closeEdges.clear();
//				closeEdges.add(edge);
//				minDist = dist;
//			}
//		}
//		
//		// test if the point belongs to boundary
//		if(minDist<Shape2D.ACCURACY) return true;
//		
//		
//		// only one edge -> just convert unique element of the vector,
//		// and test the signedDistance
//		if(closeEdges.size()==1)
//			return ((LineSegment2D) closeEdges.firstElement()).getSignedDistance(x, y)<0;
//		
//		// Several edges share the same minimal distance to the point (x, y).
//		// We look at the selected edges to :
//		// - first find an edge vertex whose distance to (x,y) is equal to minDist
//		// - then find all edges sharing this vertex (2, or 4, or more ...)
//		// Special attention should be taken when point is closest both from
//		// an edge (by orthogonal projection) and from a vertex, not belonging
//		// to the first edge. 
//		// The special case, when point lies on the bisector of
//		// two edges not following each others is easily solved.
//				
//		// 1 - find a point shared by at least two edges = one of the points
//		// closest to the edge.
//		Point2D point=null;
//		LineSegment2D edge = null;
//		for(int i=0; i<closeEdges.size(); i++){
//			// consider all previoulsy selected edges
//			edge = (LineSegment2D) closeEdges.elementAt(i);
//			
//			// break the loop when either point1 or point2 of one of the edges has
//			// the correct distance
//			point = edge.getPoint1();
//			if(Math.abs(point.getDistance(x, y)-minDist)<Shape2D.ACCURACY) break;
//			point = edge.getPoint2();
//			if(Math.abs(point.getDistance(x, y)-minDist)<Shape2D.ACCURACY) break;
//		}
//		
//		// If there was no point with specified distance was found, we are in the
//		// special case the point lies on the bisector of two edges. Then, we can
//		// make the test with any edge, for example the last one selected.
//		if(Math.abs(point.getDistance(x, y)-minDist)<Shape2D.ACCURACY){
//			return edge.getSignedDistance(x, y)<0;
//		}
//		
//		// 2 - find all edges sharing this point
//		//edges = new LineSegment2D[closeEdges.size()];
//		edges = new ArrayList<LineSegment2D>();
//		for(LineSegment2D closeEdge : closeEdges)
//			if(closeEdge.contains(point))
//				edges.add(closeEdge);
//		
//		// select the edge with minimal angle between selected point, edges common
//		// point, and opposite vertex of the edge.
//		double minAngle = 10;
//		double angle;
//		Point2D p = new Point2D(x, y);
//		for(LineSegment2D closeEdge : edges){
//			angle = Angle2D.getAbsoluteAngle(p, point, closeEdge.getOtherPoint(point));
//			if(angle<minAngle){
//				edge = closeEdge;
//				minAngle = angle;
//			}
//		}
//		
//		// We finally have the good edge to test signed distance :
//		return edge.getSignedDistance(x, y)<=0;
//	}

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
	 * Tests if the Polygon intersects the interior of a specified rectangular area.
	 */
	public boolean intersects(double x, double y, double w, double h){
		if(this.contains(x, y)) return true;
		return this.getBoundingBox().getAsRectangle().intersects(x, y, w, h);
	}

	/**
	 * Tests if the Polygon intersects the interior of a specified rectangle2D.
	 */
	public boolean intersects(java.awt.geom.Rectangle2D r){
		return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}

	/** 
	 * Return a general path iterator.
	 */
	public java.awt.geom.GeneralPath getGeneralPath(){
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		if(points.size()<2) return path;
		
		// move to first point
		Point2D point = (Point2D) points.get(0);
		path.moveTo((float)(point.getX()), (float)(point.getY()));
		
		// line to each point
		for(int i=0; i<points.size(); i++){
			point = (Point2D) points.get(i);
			path.lineTo((float)(point.getX()), (float)(point.getY()));
		}
		
		// close polygon
		point = (Point2D) points.get(0);
		path.lineTo((float)(point.getX()), (float)(point.getY()));
		path.closePath();
		
		return path;
	}
	
	/** 
	 * Return pathiterator for this polygon.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans){
		return this.getGeneralPath().getPathIterator(trans);
	}

	/**
	 * Return pathiterator for this polygon.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans, double flatness){
		return this.getGeneralPath().getPathIterator(trans, flatness);
	}
	
	// ===================================================================
	// methods inherited from Object interface
	
	/**
	 * Test if the two polygons are equal. Test first the number of vertices,
	 * then the bounding boxes, then if each vertex of the polygon is
	 * contained in the vertices array of this polygon.
	 */
	public boolean equals(Object obj){
		if(!(obj instanceof SimplePolygon2D))
			return false;
		
		SimplePolygon2D polygon = (SimplePolygon2D) obj;
		
		if(polygon.getVerticesNumber()!=this.getVerticesNumber())
			return false;
		
		if(!polygon.getBoundingBox().equals(this.getBoundingBox()))
			return false;
		
		for(Point2D point : polygon.getVertices()){
			if(!this.points.contains(point))
				return false;
		}
		
		return true;
	}
}