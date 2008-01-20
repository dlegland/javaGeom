/* File AbstractPolygon2D.java 
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

// Imports
import java.util.*;

/**
 * Represent any class made of a finite set of simply connected edges. This
 * class can be specialized in General Polygons, Polygon2D, which is 
 * a simple polygon, or more specialized shapes (square, rhombus...)
 */
public interface PolygonalShape2D extends Domain2D{


	/** Return the vertices (singular points) of the polygon*/
	public abstract Collection<Point2D> getVertices();
	
	/** Return the number of vertices of the polygon*/
	public abstract int getVerticesNumber();
	
	/** Return the edges as line segments of the polygon*/
	public abstract Collection<LineSegment2D> getEdges();
	
/*	{
		Point2D[] points = getPoints();
		Edge2D[] tab = new Edge2D[points.length];
		int i;
		for(i=0; i<points.length-1; i++)
			tab[i] = new Edge2D(points[i], points[i+1]);
		tab[i] = new Edge2D(points[i], points[0]);
		return tab;
	}*/

	

/*
	* 
	 * Return true if the point p lies on the line, with precision given by 
	 * Shape2D.ACCURACY.
	 *
	public boolean contains(java.awt.geom.Point2D p){
		return contains(p.getX(), p.getY());
	}*/


	/* 
	 * Return true if the point (x, y) lies on the line, with precision given 
	 * by Shape2D.ACCURACY.
	 *
	public boolean contains(double x, double y){
		
		
		return false;
		
		// Old version of algorithm, does not work properly
		// for self crossing polygons.
		
		/*
		double dist=1, minDist;
		Edge2D[] edges = getEdges();
		Vector closeEdges = new Vector(4);
		
		// first, compute min distance and store all edges sharing
		// this minimal distance to test point
		minDist = Double.POSITIVE_INFINITY;
		for(int i=0; i<edges.length; i++){
			dist = edges[i].getDistance(x, y);
			if(Math.abs(dist-minDist)<Shape2D.ACCURACY)
				closeEdges.add(edges[i]);
			else if(dist<minDist){
				closeEdges.clear();
				closeEdges.add(edges[i]);
				minDist = dist;
			}
		}
		
		// test if the point belongs to boundary
		if(minDist<Shape2D.ACCURACY) return true;
		
		
		// only one edge -> just convert unique element of the vector,
		// and test the signedDistance
		if(closeEdges.size()==1)
			return ((Edge2D) closeEdges.firstElement()).getSignedDistance(x, y)<0;
		
		// Several edges share the same minimal distance to the point (x, y).
		// We look at the selected edges to :
		// - first find an edge vertex whose distance to (x,y) is equal to minDist
		// - then find all edges sharing this vertex (2, or 4, or more ...)
		// Special attention should be taken when point is closest both from
		// an edge (by orthogonal projection) and from a vertex, not belonging
		// to the first edge. 
		// The special case, when point lies on the bisector of
		// two edges not following each others is easily solved.
				
		// 1 - find a point shared by at least two edges = one of the points
		// closest to the edge.
		Point2D point=null;
		Edge2D edge = null;
		for(int i=0; i<closeEdges.size(); i++){
			// consider all previoulsy selected edges
			edge = (Edge2D) closeEdges.elementAt(i);
			
			// break the loop when either point1 or point2 of one of the edges has
			// the correct distance
			point = edge.getPoint1();
			if(Math.abs(point.getDistance(x, y)-minDist)<Shape2D.ACCURACY) break;
			point = edge.getPoint2();
			if(Math.abs(point.getDistance(x, y)-minDist)<Shape2D.ACCURACY) break;
		}
		
		// If there was no point with specified distance was found, we are in the
		// special case the point lies on the bisector of two edges. Then, we can
		// make the test with any edge, for example the last one selected.
		if(Math.abs(point.getDistance(x, y)-minDist)<Shape2D.ACCURACY){
			return edge.getSignedDistance(x, y)<0;
		}
		
		// 2 - find all edges sharing this point
		edges = new Edge2D[closeEdges.size()];
		minDist = Double.POSITIVE_INFINITY;
		int n=0;	// number of edges
		for(int i=0; i<closeEdges.size(); i++)
			if(((Edge2D)closeEdges.elementAt(i)).contains(point))
				edges[n++]=(Edge2D)closeEdges.elementAt(i);
		
		// select the edge with minimal angle between selected point, edges common
		// point, and opposite vertex of the edge.
		double minAngle = 10;
		double angle;
		Point2D p = new Point2D(x, y);
		for(int i=0; i<n; i++){
			angle = StraightObject2D.getAbsoluteAngle(p, point, edges[i].getOtherPoint(point));
			if(angle<minAngle){
				edge = edges[i];
				minAngle = angle;
			}
		}
		
		// We finally have the good edge to test signed distance :
		return edge.getSignedDistance(x, y)<=0;
	}*/


	/** 
	 * Return an edge pathiterator.
	 */
	/*
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform t){
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		Point2D[] points = getPoints();
		path.moveTo((float)(points[0].getX()), (float)(points[0].getY()));
		int i;
		for(i=0; i<points.length; i++)
			path.lineTo((float)(points[i].getX()), (float)(points[i].getY()));
		path.lineTo((float)(points[0].getX()), (float)(points[0].getY()));
		return path.getPathIterator(t);
	}*/

	/** 
	 * Return null
	 */
	/*
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform t, double flatness){
		Point2D[] points = getPoints();
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo((float)(points[0].getX()), (float)(points[0].getY()));
		int i;
		for(i=0; i<points.length; i++)
			path.lineTo((float)(points[i].getX()), (float)(points[i].getY()));
		path.lineTo((float)(points[0].getX()), (float)(points[0].getY()));
		return path.getPathIterator(t, flatness);
	}*/
	

	// ===================================================================
	// general methods

	/** 
	 * Return the new Polygon created by an affine transform of this polygon.
	 */
	public PolygonalShape2D transform(AffineTransform2D trans);

}