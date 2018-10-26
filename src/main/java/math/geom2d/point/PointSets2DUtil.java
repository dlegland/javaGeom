package math.geom2d.point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import math.geom2d.IShape2D;

/**
 * A set of static methods for manipulating point sets.
 * 
 * @author dlegland
 * @since 0.9.1
 */
public final class PointSets2DUtil {

    /**
     * Tests if the given list of points contains multiple vertices. This function can be used to test presence of multiple vertices in polylines and polygons. First and last point are not compared.
     */
    public static boolean hasMultipleVertices(List<Point2D> points) {
        return hasMultipleVertices(points, false);
    }

    /**
     * Tests if the given list of points contains multiple vertices. This function can be used to test presence of multiple vertices in polylines and polygons. First and last point are compared if flag 'closed' is set to true.
     */
    public static boolean hasMultipleVertices(List<Point2D> points, boolean closed) {
        // Note:
        // it could be possible to use the 'countMultipleVertices' method,
        // but using a specific method allow to terminate at the first
        // detection of a multiple vertex

        // initialize iterator
        Iterator<Point2D> iter = points.iterator();

        Point2D previous = null;
        Point2D current;
        if (closed) {
            previous = points.get(points.size() - 1);
        } else {
            previous = iter.next();
        }

        // iterate over couple of points
        while (iter.hasNext()) {
            current = iter.next();
            if (Point2D.distance(current, previous) < IShape2D.ACCURACY)
                return true;
            previous = current;
        }
        return false;
    }

    /**
     * Counts the number of multiple vertices in this point list.
     */
    public static int countMultipleVertices(List<Point2D> points) {
        return PointSets2DUtil.countMultipleVertices(points, false);
    }

    /**
     * Counts the number of multiple vertices in this point list. If flag 'closed' is set to true, extremities are also compared.
     */
    public static int countMultipleVertices(List<Point2D> points, boolean closed) {

        int count = 0;

        // initialize iterator
        Iterator<Point2D> iter = points.iterator();

        // initialize "previous" vertex: either the last or the first one
        Point2D previous = null;
        Point2D current;
        if (closed) {
            previous = points.get(points.size() - 1);
        } else {
            previous = iter.next();
        }

        // iterate over couple of points
        while (iter.hasNext()) {
            current = iter.next();
            if (Point2D.distance(current, previous) < IShape2D.ACCURACY)
                count++;
            previous = current;
        }

        // return number of multiple vertices
        return count;
    }

    public static List<Point2D> filterMultipleVertices(List<Point2D> vertices) {
        return filterMultipleVertices(vertices, false);
    }

    public static List<Point2D> filterMultipleVertices(List<Point2D> vertices, boolean closed) {

        // Compute number of vertices, and of multiple vertices
        int size = vertices.size();
        int nMulti = countMultipleVertices(vertices, closed);

        // create result with the right number of simple vertices
        ArrayList<Point2D> result = new ArrayList<>(size - nMulti);

        // If array is empty, return empty array
        if (size == 0)
            return result;

        // If polyline is closed, the previous vertex is initialized with
        // the last vertex of the curve
        Iterator<Point2D> iter = vertices.iterator();
        Point2D current, previous = null;
        if (closed) {
            previous = vertices.get(size - 1);
        } else {
            previous = iter.next();
            result.add(previous);
        }

        // compare each couple of contiguous vertex
        while (iter.hasNext()) {
            current = iter.next();
            if (Point2D.distance(current, previous) > IShape2D.ACCURACY) {
                result.add(current);
            }
            previous = current;
        }
        return result;
    }
}
