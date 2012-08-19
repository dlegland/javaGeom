/**
 * 
 */

package math.geom2d.polygon;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.point.PointSets2D;

/**
 * Some utility functions for manipulating Polyline2D.
 * 
 * @author dlegland
 * @since 0.6.3
 */
public abstract class Polylines2D {

	/**
	 * Checks if the open polyline has multiple vertices. Polyline extremities
	 * are not tested for equality.
	 */
    public final static boolean hasMultipleVertices(LinearCurve2D polyline) {
    	return hasMultipleVertices(polyline, false);
    }
    
	/**
	 * Checks if the input polyline has multiple vertices. Extremities are
	 * tested if the polyline is closed (second argument is true).
	 */
    public final static boolean hasMultipleVertices(LinearCurve2D polyline, 
    		boolean closed) {
    	// Test vertices within polyline
    	if(PointSets2D.hasMultipleVertices(polyline.vertices))
    		return true;
    	
    	// Eventually tests extremities
    	if (closed) {
    		Point2D p1 = polyline.firstPoint();
    		Point2D p2 = polyline.lastPoint();
    		if (p1.distance(p2) < Shape2D.ACCURACY)
    			return true;
    	}
    	
    	return false;
    }

    /**
     * Return all intersection points between the 2 polylines.
     * This method implements a naive algorithm, that tests all possible edge
     * couples.
     * It is supposed that only one point is returned by intersection.
     * @param poly1 a first polyline
     * @param poly2 a second polyline
     * @return the set of intersection points
     */
    public static Collection<Point2D> intersect(
    		LinearCurve2D poly1, LinearCurve2D poly2) {
    	// array for storing intersections
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        
        // iterate on edge couples
        Point2D point;
        for (LineSegment2D edge1 : poly1.edges()) {
            for (LineSegment2D edge2 : poly2.edges()) {
            	// if the intersection is not empty, add it to the set
                point = edge1.intersection(edge2);
                if (point != null) {
                	// we keep only one intersection by couple
                	if (!points.contains(point))
                		points.add(point);
                }
            }
        }

        return points;
    }
}
