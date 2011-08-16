package math.geom2d.point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;

/**
 * A set of static methods for manipulating point sets.
 * @author dlegland
 * @since 0.9.1
 */
public class PointSet2DUtils {

	/**
	 * Tests if the given list of points contains multiple vertices. 
	 * This function can be used to test presence of multiple vertices in 
	 * polylines and polygons. First and last point are not compared.
	 */
    public static<T extends java.awt.geom.Point2D> boolean hasMultipleVertices(
    		List<T> points) {
    	return hasMultipleVertices(points, false);
    }
    
    public static<T extends java.awt.geom.Point2D> boolean hasMultipleVertices(
    		List<T> points, boolean closed) {
    	
    	// initialize iterator
    	Iterator<T> iter = points.iterator();
    	
    	T previous = null; 
    	T current;
    	if (closed) {
    		previous = points.get(points.size()-1);
    	} else {
        	previous = iter.next();
    	}
    	
    	// iterate over couple of points
    	while (iter.hasNext()) {
    		current = iter.next();
    		if (Point2D.getDistance(current, previous) < Shape2D.ACCURACY)
    			return true;
    		previous = current;
    	}
    	return false;
    }
    
    public static<T extends java.awt.geom.Point2D> List<T> filterMultipleVertices(
    		List<T> vertices) {
    	return filterMultipleVertices(vertices, false);
    }

    public static<T extends java.awt.geom.Point2D> List<T> filterMultipleVertices(
    		List<T> vertices, boolean closed) {
    	
    	// First check size
    	int size = vertices.size();
    	ArrayList<T> result = new ArrayList<T>(size);
    	if (size == 0)
    		return result;
    	
    	// If polyline is closed, the previous vertex is initialized with
    	// the last vertex of the curve
    	Iterator<T> iter = vertices.iterator(); 
    	T current, previous = null;
    	if (closed) {
    		previous = vertices.get(size-1);
    	} else {
        	previous = iter.next();
        	result.add(previous);
    	}
    	
    	// compare each couple of contiguous vertex
    	while (iter.hasNext()) {
    		current = iter.next();
    		if (Point2D.getDistance(current, previous) > Shape2D.ACCURACY) {
    			result.add(current);
    		}
    		previous = current;
    	}
    	return result;
    }

}
