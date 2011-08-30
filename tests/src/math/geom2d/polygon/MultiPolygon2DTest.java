package math.geom2d.polygon;

import junit.framework.TestCase;

import java.util.*;

import math.geom2d.Point2D;

public class MultiPolygon2DTest extends TestCase {

	public void testGetVertices() {
		Point2D[] pts1 = new Point2D[]{
				new Point2D(0, 0), 
				new Point2D(10, 0),
				new Point2D(10, 10),
				new Point2D(0, 10) };
		LinearRing2D ring1 = new LinearRing2D(pts1);
		
		Point2D[] pts2 = new Point2D[]{
				new Point2D(20, 0),
				new Point2D(30, 0),
				new Point2D(30, 10), 
				new Point2D(20, 10) };
		LinearRing2D ring2 = new LinearRing2D(pts2);		
		
		MultiPolygon2D polygon = new MultiPolygon2D();
		polygon.addRing(ring1);
		polygon.addRing(ring2);
		
		Collection<Point2D> vertices = polygon.getVertices();
		assertTrue(vertices.size() == pts1.length + pts2.length);
		
		for(int i = 0; i < pts1.length; i++)
			assertTrue(vertices.contains(pts1[i]));
		for(int i = 0; i < pts2.length; i++)
			assertTrue(vertices.contains(pts2[i]));
	}

	public void testGetVerticesNumber() {
		Point2D[] pts1 = new Point2D[]{
				new Point2D(0, 0), 
				new Point2D(10, 0),
				new Point2D(10, 10),
				new Point2D(0, 10) };
		LinearRing2D ring1 = new LinearRing2D(pts1);
		
		Point2D[] pts2 = new Point2D[]{
				new Point2D(20, 0), 
				new Point2D(30, 0),
				new Point2D(30, 10),
				new Point2D(20, 10) };
		LinearRing2D ring2 = new LinearRing2D(pts2);		
		
		MultiPolygon2D polygon = new MultiPolygon2D();
		polygon.addRing(ring1);
		polygon.addRing(ring2);
		
		assertEquals(8, polygon.getVertexNumber());
	}
	
	public void testGetVertex(){
	    LinearRing2D tri1 = new LinearRing2D(new Point2D[]{
				new Point2D(0, 0), 
				new Point2D(10, 0), 
				new Point2D(10, 10)});
		LinearRing2D tri2 = new LinearRing2D(new Point2D[]{
				new Point2D(20, 0),
				new Point2D(30, 0), 
				new Point2D(30, 10)});
		MultiPolygon2D poly = new MultiPolygon2D(
				new LinearRing2D[]{tri1,tri2});
		
		assertEquals(new Point2D(0, 0), poly.getVertex(0));
		assertEquals(new Point2D(10, 0), poly.getVertex(1));
		assertEquals(new Point2D(10, 10), poly.getVertex(2));
		assertEquals(new Point2D(20, 0), poly.getVertex(3));
		assertEquals(new Point2D(30, 0), poly.getVertex(4));
		assertEquals(new Point2D(30, 10), poly.getVertex(5));
	}

	public void testContainsPoint2D() {
		Point2D[] pts1 = new Point2D[]{
				new Point2D(0, 0),
				new Point2D(10, 0),
				new Point2D(10, 10),
				new Point2D(0, 10) };
		LinearRing2D ring1 = new LinearRing2D(pts1);
		
		Point2D[] pts2 = new Point2D[]{
				new Point2D(20, 0),
				new Point2D(30, 0),
				new Point2D(30, 10),
				new Point2D(20, 10) };
		LinearRing2D ring2 = new LinearRing2D(pts2);		
		
		MultiPolygon2D polygon = new MultiPolygon2D();
		polygon.addRing(ring1);
		polygon.addRing(ring2);
		
		assertTrue(polygon.contains(new Point2D(5, 5)));
		assertTrue(polygon.contains(new Point2D(25, 5)));
		assertTrue(!polygon.contains(new Point2D(15, 5)));
	}

	public void testClone() {
        Point2D[] pts1 = new Point2D[]{
                new Point2D(0, 0), 
                new Point2D(10, 0),
                new Point2D(10, 10), 
                new Point2D(0, 10) };
        LinearRing2D ring1 = new LinearRing2D(pts1);
        
        Point2D[] pts2 = new Point2D[]{
                new Point2D(20, 0),
                new Point2D(30, 0),
                new Point2D(30, 10), 
                new Point2D(20, 10) };
        LinearRing2D ring2 = new LinearRing2D(pts2);       
        
        MultiPolygon2D polygon = new MultiPolygon2D();
        polygon.addRing(ring1);
        polygon.addRing(ring2);

        assertTrue(polygon.equals(polygon.clone()));
	}
}
