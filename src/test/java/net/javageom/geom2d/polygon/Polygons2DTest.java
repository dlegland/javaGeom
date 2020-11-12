/**
 * File: 	Polygon2DUtilsTest.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 25 janv. 09
 */
package net.javageom.geom2d.polygon;

import java.util.Collection;

import junit.framework.TestCase;
import net.javageom.geom2d.Point2D;
import net.javageom.geom2d.domain.Boundary2D;
import net.javageom.geom2d.domain.Domain2D;


/**
 * @author dlegland
 *
 */
public class Polygons2DTest extends TestCase {

    /**
     * Test method for {@link net.javageom.geom2d.polygon.Polygons2D#union(net.javageom.geom2d.polygon.Polygon2D, net.javageom.geom2d.polygon.Polygon2D)}.
     */
    public void testUnion_Intersecting() {
        // points of the first polygon
        Point2D pa1 = new Point2D(50, 100);
        Point2D pa2 = new Point2D(250, 100);
        Point2D pa3 = new Point2D(250, 200);
        Point2D pa4 = new Point2D(50, 200);
        Polygon2D poly1 = new SimplePolygon2D(new Point2D[]{
                pa1, pa2, pa3, pa4});
        
        Point2D pb1 = new Point2D(100, 50);
        Point2D pb2 = new Point2D(200, 50);
        Point2D pb3 = new Point2D(200, 250);
        Point2D pb4 = new Point2D(100, 250);
        Polygon2D poly2 = new SimplePolygon2D(new Point2D[]{
                pb1, pb2, pb3, pb4});
       
        Polygon2D union = Polygons2D.union(poly1, poly2);
        assertNotNull(union);
        assertEquals(12, union.vertexNumber());
    }

    public void testUnion_BInsideA() {
        // points of the first polygon
        Point2D pa1 = new Point2D(50, 50);
        Point2D pa2 = new Point2D(250, 50);
        Point2D pa3 = new Point2D(250, 250);
        Point2D pa4 = new Point2D(50, 250);
        Polygon2D poly1 = new SimplePolygon2D(new Point2D[]{
                pa1, pa2, pa3, pa4});
        
        Point2D pb1 = new Point2D(100, 100);
        Point2D pb2 = new Point2D(200, 100);
        Point2D pb3 = new Point2D(200, 200);
        Point2D pb4 = new Point2D(100, 200);
        Polygon2D poly2 = new SimplePolygon2D(new Point2D[]{
                pb1, pb2, pb3, pb4});
       
        Polygon2D union = Polygons2D.union(poly1, poly2);
        assertNotNull(union);
        assertEquals(4, union.vertexNumber());
        //assertTrue(union.getBoundary().equals(poly1.getBoundary()));
    }

    public void testUnion_AInsideB() {
        // points of the first polygon
        Point2D pa1 = new Point2D(100, 100);
        Point2D pa2 = new Point2D(200, 100);
        Point2D pa3 = new Point2D(200, 200);
        Point2D pa4 = new Point2D(100, 200);
        Polygon2D poly1 = new SimplePolygon2D(new Point2D[]{
                pa1, pa2, pa3, pa4});
        
        Point2D pb1 = new Point2D(50, 50);
        Point2D pb2 = new Point2D(250, 50);
        Point2D pb3 = new Point2D(250, 250);
        Point2D pb4 = new Point2D(50, 250);
        Polygon2D poly2 = new SimplePolygon2D(new Point2D[]{
                pb1, pb2, pb3, pb4});
       
        Polygon2D union = Polygons2D.union(poly1, poly2);
        assertNotNull(union);
        assertEquals(union.vertexNumber(), 4);
        //assertTrue(union.getBoundary().equals(poly2.getBoundary()));
    }
    
    public void testUnion_ADisjointB() {
        // points of the first polygon
        Point2D pa1 = new Point2D(50, 100);
        Point2D pa2 = new Point2D(100, 100);
        Point2D pa3 = new Point2D(100, 200);
        Point2D pa4 = new Point2D(50, 200);
        Polygon2D poly1 = new SimplePolygon2D(new Point2D[]{
                pa1, pa2, pa3, pa4});
        
        Point2D pb1 = new Point2D(200, 100);
        Point2D pb2 = new Point2D(250, 100);
        Point2D pb3 = new Point2D(250, 200);
        Point2D pb4 = new Point2D(200, 200);
        Polygon2D poly2 = new SimplePolygon2D(new Point2D[]{
                pb1, pb2, pb3, pb4});
       
        Polygon2D union = Polygons2D.union(poly1, poly2);
        assertNotNull(union);
        assertEquals(union.vertexNumber(), 8);
    }

    /**
     * Test method for {@link net.javageom.geom2d.polygon.Polygons2D#union(net.javageom.geom2d.polygon.Polygon2D, net.javageom.geom2d.polygon.Polygon2D)}.
     */
    public void testUnion_TwoConcentricRings() {
    	LinearRing2D outerRing1 = LinearRing2D.create(new Point2D[]{
    			new Point2D(50, 50),
    			new Point2D(450, 50),
    			new Point2D(450, 450),
    			new Point2D(50, 450)
    	});
    	LinearRing2D innerRing1 = LinearRing2D.create(new Point2D[]{
    			new Point2D(100, 100),
    			new Point2D(100, 400),
    			new Point2D(400, 400),
    			new Point2D(400, 100)
    	});
    	Polygon2D poly1 = MultiPolygon2D.create(new LinearRing2D[]{
    			outerRing1, innerRing1});
    	
    	LinearRing2D outerRing2 = LinearRing2D.create(new Point2D[]{
    			new Point2D(150, 150),
    			new Point2D(350, 150),
    			new Point2D(350, 350),
    			new Point2D(150, 350)
    	});
    	LinearRing2D innerRing2 = LinearRing2D.create(new Point2D[]{
    			new Point2D(200, 200),
    			new Point2D(200, 300),
    			new Point2D(300, 300),
    			new Point2D(300, 200)
    	});
    	Polygon2D poly2 = MultiPolygon2D.create(new LinearRing2D[]{
    			outerRing2, innerRing2});
       
        Polygon2D union = Polygons2D.union(poly1, poly2);
        assertNotNull(union);
        assertEquals(16, union.vertexNumber());
    }

    /**
     * Test method for {@link net.javageom.geom2d.polygon.Polygons2D#union(net.javageom.geom2d.polygon.Polygon2D, net.javageom.geom2d.polygon.Polygon2D)}.
     */
    public void testUnion_TwoIntersectingRings() {
    	LinearRing2D outerRing1 = LinearRing2D.create(new Point2D[]{
    			new Point2D(50, 150),
    			new Point2D(350, 150),
    			new Point2D(350, 450),
    			new Point2D(50, 450)
    	});
    	LinearRing2D innerRing1 = LinearRing2D.create(new Point2D[]{
    			new Point2D(100, 200),
    			new Point2D(100, 400),
    			new Point2D(300, 400),
    			new Point2D(300, 200)
    	});
    	Polygon2D poly1 = MultiPolygon2D.create(new LinearRing2D[]{
    			outerRing1, innerRing1});
    	
    	LinearRing2D outerRing2 = LinearRing2D.create(new Point2D[]{
    			new Point2D(150, 50),
    			new Point2D(450, 50),
    			new Point2D(450, 350),
    			new Point2D(150, 350)
    	});
    	LinearRing2D innerRing2 = LinearRing2D.create(new Point2D[]{
    			new Point2D(200, 100),
    			new Point2D(200, 300),
    			new Point2D(400, 300),
    			new Point2D(400, 100)
    	});
    	Polygon2D poly2 = MultiPolygon2D.create(new LinearRing2D[]{
    			outerRing2, innerRing2});
       
        Polygon2D union = Polygons2D.union(poly1, poly2);
        assertNotNull(union);
        assertEquals(24, union.vertexNumber());
        
        assertEquals(4, union.contours().size());
    }

    /**
     * Test method for {@link net.javageom.geom2d.polygon.Polygons2D#union(net.javageom.geom2d.polygon.Polygon2D, net.javageom.geom2d.polygon.Polygon2D)}.
     */
    public void testIntersection_Intersecting() {
        // points of the first polygon
        Point2D pa1 = new Point2D(50, 100);
        Point2D pa2 = new Point2D(250, 100);
        Point2D pa3 = new Point2D(250, 200);
        Point2D pa4 = new Point2D(50, 200);
        Polygon2D poly1 = new SimplePolygon2D(new Point2D[]{
                pa1, pa2, pa3, pa4});
        
        Point2D pb1 = new Point2D(100, 50);
        Point2D pb2 = new Point2D(200, 50);
        Point2D pb3 = new Point2D(200, 250);
        Point2D pb4 = new Point2D(100, 250);
        Polygon2D poly2 = new SimplePolygon2D(new Point2D[]{
                pb1, pb2, pb3, pb4});
       
        Polygon2D inter = Polygons2D.intersection(poly1, poly2);
        assertNotNull(inter);
        assertEquals(4, inter.vertexNumber());
    }
    
    /**
     * Test method for {@link net.javageom.geom2d.polygon.Polygons2D#union(net.javageom.geom2d.polygon.Polygon2D, net.javageom.geom2d.polygon.Polygon2D)}.
     */
    public void testIntersection_Disjoint() {
        // points of the first polygon
        Point2D pa1 = new Point2D(50, 100);
        Point2D pa2 = new Point2D(100, 100);
        Point2D pa3 = new Point2D(100, 200);
        Point2D pa4 = new Point2D(50, 200);
        Polygon2D poly1 = new SimplePolygon2D(new Point2D[]{
                pa1, pa2, pa3, pa4});
        
        Point2D pb1 = new Point2D(200, 100);
        Point2D pb2 = new Point2D(250, 100);
        Point2D pb3 = new Point2D(250, 200);
        Point2D pb4 = new Point2D(200, 200);
        Polygon2D poly2 = new SimplePolygon2D(new Point2D[]{
                pb1, pb2, pb3, pb4});
       
        Polygon2D inter = Polygons2D.intersection(poly1, poly2);
        assertNotNull(inter);
        assertEquals(0, inter.vertexNumber());
    }
    
    /**
     * Test method for {@link net.javageom.geom2d.polygon.Polygons2D#union(net.javageom.geom2d.polygon.Polygon2D, net.javageom.geom2d.polygon.Polygon2D)}.
     */
    public void testIntersection_TwoConcentricRings() {
    	LinearRing2D outerRing1 = LinearRing2D.create(new Point2D[]{
    			new Point2D(50, 50),
    			new Point2D(450, 50),
    			new Point2D(450, 450),
    			new Point2D(50, 450)
    	});
    	LinearRing2D innerRing1 = LinearRing2D.create(new Point2D[]{
    			new Point2D(100, 100),
    			new Point2D(100, 400),
    			new Point2D(400, 400),
    			new Point2D(400, 100)
    	});
    	Polygon2D poly1 = MultiPolygon2D.create(new LinearRing2D[]{
    			outerRing1, innerRing1});
    	
    	LinearRing2D outerRing2 = LinearRing2D.create(new Point2D[]{
    			new Point2D(150, 150),
    			new Point2D(350, 150),
    			new Point2D(350, 350),
    			new Point2D(150, 350)
    	});
    	LinearRing2D innerRing2 = LinearRing2D.create(new Point2D[]{
    			new Point2D(200, 200),
    			new Point2D(200, 300),
    			new Point2D(300, 300),
    			new Point2D(300, 200)
    	});
    	Polygon2D poly2 = MultiPolygon2D.create(new LinearRing2D[]{
    			outerRing2, innerRing2});
       
        Polygon2D union = Polygons2D.intersection(poly1, poly2);
        assertNotNull(union);
        assertEquals(0, union.vertexNumber());
    }

    /**
     * Test method for {@link net.javageom.geom2d.polygon.Polygons2D#union(net.javageom.geom2d.polygon.Polygon2D, net.javageom.geom2d.polygon.Polygon2D)}.
     */
    public void testIntersection_TwoIntersectingRings() {
    	LinearRing2D outerRing1 = LinearRing2D.create(new Point2D[]{
    			new Point2D(50, 150),
    			new Point2D(350, 150),
    			new Point2D(350, 450),
    			new Point2D(50, 450)
    	});
    	LinearRing2D innerRing1 = LinearRing2D.create(new Point2D[]{
    			new Point2D(100, 200),
    			new Point2D(100, 400),
    			new Point2D(300, 400),
    			new Point2D(300, 200)
    	});
    	Polygon2D poly1 = MultiPolygon2D.create(new LinearRing2D[]{
    			outerRing1, innerRing1});
    	
    	LinearRing2D outerRing2 = LinearRing2D.create(new Point2D[]{
    			new Point2D(150, 50),
    			new Point2D(450, 50),
    			new Point2D(450, 350),
    			new Point2D(150, 350)
    	});
    	LinearRing2D innerRing2 = LinearRing2D.create(new Point2D[]{
    			new Point2D(200, 100),
    			new Point2D(200, 300),
    			new Point2D(400, 300),
    			new Point2D(400, 100)
    	});
    	Polygon2D poly2 = MultiPolygon2D.create(new LinearRing2D[]{
    			outerRing2, innerRing2});
       
        Polygon2D union = Polygons2D.intersection(poly1, poly2);
        assertNotNull(union);
        assertEquals(8, union.vertexNumber());
        
        assertEquals(2, union.contours().size());
    }

    /**
     * Test with two shifted squares. The result is a square with a corner
     * removed.
     */
    public void testDifference_ShiftedSquares() {
        // points of the first polygon
        Point2D pa1 = new Point2D(50, 50);
        Point2D pa2 = new Point2D(150, 50);
        Point2D pa3 = new Point2D(150, 150);
        Point2D pa4 = new Point2D(50, 150);
        Polygon2D poly1 = new SimplePolygon2D(new Point2D[]{
                pa1, pa2, pa3, pa4});
        
        // Create second polygon
        Point2D pb1 = new Point2D(100, 100);
        Point2D pb2 = new Point2D(200, 100);
        Point2D pb3 = new Point2D(200, 200);
        Point2D pb4 = new Point2D(100, 200);
        Polygon2D poly2 = new SimplePolygon2D(new Point2D[]{
                pb1, pb2, pb3, pb4});
       
        // compute difference
        Polygon2D diff = Polygons2D.difference(poly1, poly2);
        assertNotNull(diff);
        assertEquals(6, diff.vertexNumber());
        
        // Check if vertices are the good ones
        Collection<Point2D> vertices = diff.vertices();
        assertTrue(vertices.contains(new Point2D(50, 50)));
        assertTrue(vertices.contains(new Point2D(150, 50)));
        assertTrue(vertices.contains(new Point2D(150, 100)));
        assertTrue(vertices.contains(new Point2D(100, 100)));
        assertTrue(vertices.contains(new Point2D(100, 150)));
        assertTrue(vertices.contains(new Point2D(50, 150)));
    }
    
    /**
     * Test with two nested squares. The result is a square polygon with a
     * square hole.
     */
    public void testDifference_NestedSquares() {
        // points of the first polygon
        Point2D pa1 = new Point2D(100, 100);
        Point2D pa2 = new Point2D(400, 100);
        Point2D pa3 = new Point2D(400, 400);
        Point2D pa4 = new Point2D(100, 400);
        Polygon2D poly1 = new SimplePolygon2D(new Point2D[]{
                pa1, pa2, pa3, pa4});
        
        // Create second polygon
        Point2D pb1 = new Point2D(200, 200);
        Point2D pb2 = new Point2D(300, 200);
        Point2D pb3 = new Point2D(300, 300);
        Point2D pb4 = new Point2D(200, 300);
        Polygon2D poly2 = new SimplePolygon2D(new Point2D[]{
                pb1, pb2, pb3, pb4});
    	
        // compute difference
        Polygon2D diff = Polygons2D.difference(poly1, poly2);
        assertNotNull(diff);
        assertEquals(8, diff.vertexNumber());
        
        assertEquals(2, diff.contours().size());
        
        // Check if vertices are the good ones
        Collection<Point2D> vertices = diff.vertices();
        assertTrue(vertices.contains(new Point2D(100, 100)));
        assertTrue(vertices.contains(new Point2D(400, 100)));
        assertTrue(vertices.contains(new Point2D(400, 400)));
        assertTrue(vertices.contains(new Point2D(100, 400)));
        assertTrue(vertices.contains(new Point2D(200, 200)));
        assertTrue(vertices.contains(new Point2D(300, 200)));
        assertTrue(vertices.contains(new Point2D(300, 300)));
        assertTrue(vertices.contains(new Point2D(200, 300)));
    }

    /**
     * Test with two shifted squares. The result is the union of two L-shapes
     */
    public void testExclusiveOr_ShiftedSquares() {
        // points of the first polygon
        Point2D pa1 = new Point2D(50, 50);
        Point2D pa2 = new Point2D(150, 50);
        Point2D pa3 = new Point2D(150, 150);
        Point2D pa4 = new Point2D(50, 150);
        Polygon2D poly1 = new SimplePolygon2D(new Point2D[]{
                pa1, pa2, pa3, pa4});
        
        // Create second polygon
        Point2D pb1 = new Point2D(100, 100);
        Point2D pb2 = new Point2D(200, 100);
        Point2D pb3 = new Point2D(200, 200);
        Point2D pb4 = new Point2D(100, 200);
        Polygon2D poly2 = new SimplePolygon2D(new Point2D[]{
                pb1, pb2, pb3, pb4});
       
        // compute exclusive or
        Polygon2D xor = Polygons2D.exclusiveOr(poly1, poly2);
        assertNotNull(xor);
        assertEquals(12, xor.vertexNumber());
        
        // resulting polygon should contain all vertices of input polygons
        Collection<Point2D> vertices = xor.vertices();
        for (Point2D vertex : poly1.vertices())
        	assertTrue(vertices.contains(vertex));
        for (Point2D vertex : poly2.vertices())
        	assertTrue(vertices.contains(vertex));
    }
    

    /**
     * Test with two shifted squares. The result is the union of two L-shapes
     */
    public void testExclusiveOr_NestedSquares() {
        // points of the first polygon
        Point2D pa1 = new Point2D(100, 100);
        Point2D pa2 = new Point2D(400, 100);
        Point2D pa3 = new Point2D(400, 400);
        Point2D pa4 = new Point2D(100, 400);
        Polygon2D poly1 = new SimplePolygon2D(new Point2D[]{
                pa1, pa2, pa3, pa4});
        
        // Create second polygon
        Point2D pb1 = new Point2D(200, 200);
        Point2D pb2 = new Point2D(300, 200);
        Point2D pb3 = new Point2D(300, 300);
        Point2D pb4 = new Point2D(200, 300);
        Polygon2D poly2 = new SimplePolygon2D(new Point2D[]{
                pb1, pb2, pb3, pb4});
       
        // compute exclusive or
        Polygon2D xor = Polygons2D.exclusiveOr(poly1, poly2);
        assertNotNull(xor);
        assertEquals(8, xor.vertexNumber());
        
        // resulting polygon should contain all vertices of input polygons
        Collection<Point2D> vertices = xor.vertices();
        for (Point2D vertex : poly1.vertices())
        	assertTrue(vertices.contains(vertex));
        for (Point2D vertex : poly2.vertices())
        	assertTrue(vertices.contains(vertex));
    }
    
    /**
     * Computes  the buffer of two polygons close to each other.
     * The result should be composed of only one curve.
     */
    public void testBufferTwoClosePolygons() {
    	LinearRing2D ring1 = new LinearRing2D(new Point2D[]{
        		new Point2D(50, 100),	
        		new Point2D(100, 100),	
        		new Point2D(100, 150),	
        		new Point2D(50, 150)});
    	LinearRing2D ring2 = new LinearRing2D(new Point2D[]{
        		new Point2D(150, 100),	
        		new Point2D(200, 100),	
        		new Point2D(220, 125),	
        		new Point2D(200, 150),	
        		new Point2D(10, 150)});
    	
    	Polygon2D polygon = new MultiPolygon2D(
    			new LinearRing2D[]{ring1, ring2});
    	
    	Domain2D domain = Polygons2D.createBuffer(polygon, 30);
    	assertNotNull(domain);
    	Boundary2D boundary = domain.boundary();
    	assertNotNull(boundary);
    	
    	assertEquals(1, boundary.continuousCurves().size());
    }
    	
}
