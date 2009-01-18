/**
 * File: 	KDTree2DTest.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 18 janv. 09
 */
package math.geom2d.point;

import java.util.ArrayList;

import math.geom2d.Point2D;
import junit.framework.TestCase;


/**
 * @author dlegland
 *
 */
public class KDTree2DTest extends TestCase {

    /**
     * Test method for {@link math.geom2d.point.KDTree2D#contains(math.geom2d.Point2D)}.
     */
    public void testContains() {
        int n = 5;
        ArrayList<Point2D> points = new ArrayList<Point2D>(n);
        points.add(new Point2D(5, 5));
        points.add(new Point2D(10, 10));
        points.add(new Point2D(20, 20));
        points.add(new Point2D(8, 12));
        points.add(new Point2D(12, 8));
        
        KDTree2D tree = new KDTree2D(points);
        
        assertTrue(tree.contains(new Point2D(10, 10)));
        assertTrue(tree.contains(new Point2D(8, 12)));
        assertFalse(tree.contains(new Point2D(12, 12)));
    }
    
    public void testGetNodePoint2D() {
        int n = 5;
        ArrayList<Point2D> points = new ArrayList<Point2D>(n);
        points.add(new Point2D(5, 5));
        points.add(new Point2D(10, 10));
        points.add(new Point2D(20, 20));
        points.add(new Point2D(8, 12));
        points.add(new Point2D(12, 8));
        
        KDTree2D tree = new KDTree2D(points);
        
        assertTrue(tree.getNode(new Point2D(10, 10))!=null);
        assertTrue(tree.getNode(new Point2D(13, 10))==null);
    }
    
    public void testAddPoint2D() {
        int n = 5;
        ArrayList<Point2D> points = new ArrayList<Point2D>(n);
        points.add(new Point2D(5, 5));
        points.add(new Point2D(10, 10));
        points.add(new Point2D(20, 20));
        points.add(new Point2D(8, 12));
        points.add(new Point2D(12, 8));
        
        KDTree2D tree = new KDTree2D(points);
        
        Point2D point = new Point2D(12, 13);
        tree.add(point);
        assertTrue(tree.contains(point));
    }
    
    public void testNearestNeighborPoint2D() {
        ArrayList<Point2D> points = new ArrayList<Point2D>(3);
        points.add(new Point2D(12, 10));
        points.add(new Point2D(7, 6));
        points.add(new Point2D(15, 16));
        KDTree2D tree = new KDTree2D(points);
        
        assertEquals(tree.nearestNeighbor(new Point2D(11, 11)), new Point2D(12, 10));
       
        points = new ArrayList<Point2D>(7);
        points.add(new Point2D(12, 10));
        points.add(new Point2D(7, 6));
        points.add(new Point2D(15, 16));
        points.add(new Point2D(4, 3));
        points.add(new Point2D(6, 14));
        points.add(new Point2D(16, 8));
        points.add(new Point2D(14, 18));
        tree = new KDTree2D(points);
        
        assertEquals(tree.nearestNeighbor(new Point2D(11, 18)), new Point2D(14, 18));
        assertEquals(tree.nearestNeighbor(new Point2D(13, 0)), new Point2D(7, 6));
   }
}
