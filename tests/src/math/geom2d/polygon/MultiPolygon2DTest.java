package math.geom2d.polygon;

import junit.framework.TestCase;

import java.util.*;

import math.geom2d.Point2D;

public class MultiPolygon2DTest extends TestCase {

	public void testGetCentroid() {
		LinearRing2D ring1 = new LinearRing2D(new Point2D[]{
				new Point2D(10, 10), 
				new Point2D(20, 10),
				new Point2D(20, 20),
				new Point2D(10, 20) });
		
		LinearRing2D ring2 = new LinearRing2D(new Point2D[]{
				new Point2D(30,  0), 
				new Point2D(40,  0),
				new Point2D(40, 30),
				new Point2D(30, 30) });
		
		MultiPolygon2D polygon = new MultiPolygon2D(new LinearRing2D[]{
				ring1, ring2});
		
		Point2D centroid = polygon.getCentroid();
		Point2D expected = new Point2D(30, 15);
		
		assertTrue(expected.equals(centroid));
	}
	
	public void testGetArea() {
		LinearRing2D ring1 = new LinearRing2D(new Point2D[]{
				new Point2D(10, 10), 
				new Point2D(20, 10),
				new Point2D(20, 20),
				new Point2D(10, 20) });
		
		LinearRing2D ring2 = new LinearRing2D(new Point2D[]{
				new Point2D(30,  0), 
				new Point2D(40,  0),
				new Point2D(40, 30),
				new Point2D(30, 30) });
		
		MultiPolygon2D polygon = new MultiPolygon2D(new LinearRing2D[]{
				ring1, ring2});
		
		double area = polygon.getArea();
		double expected = 400;
		
		assertEquals(expected, area);
	}
	
	public void testGetAreaWithHole() {
		// create ring in CCW orientation
		LinearRing2D ring1 = new LinearRing2D(new Point2D[]{
				new Point2D(10, 10), 
				new Point2D(20, 10),
				new Point2D(20, 20),
				new Point2D(10, 20) });
		
		// create ring in CW orientation
		LinearRing2D ring2 = new LinearRing2D(new Point2D[]{
				new Point2D(13, 13), 
				new Point2D(13, 17),
				new Point2D(17, 17),
				new Point2D(17, 13) });
		
		MultiPolygon2D polygon = new MultiPolygon2D(new LinearRing2D[]{
				ring1, ring2});
		
		double area = polygon.getArea();
		double expected = 84;
		
		assertEquals(expected, area);
	}
	
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
		
		MultiPolygon2D polygon = new MultiPolygon2D(new LinearRing2D[]{
				ring1, ring2});
		
		Collection<Point2D> vertices = polygon.getVertices();
		assertEquals(pts1.length + pts2.length, vertices.size());
		
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
		
		MultiPolygon2D polygon = new MultiPolygon2D(new LinearRing2D[]{
				ring1, ring2});
		
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
				new LinearRing2D[]{tri1, tri2});
		
		assertEquals(new Point2D(0, 0), poly.getVertex(0));
		assertEquals(new Point2D(10, 0), poly.getVertex(1));
		assertEquals(new Point2D(10, 10), poly.getVertex(2));
		assertEquals(new Point2D(20, 0), poly.getVertex(3));
		assertEquals(new Point2D(30, 0), poly.getVertex(4));
		assertEquals(new Point2D(30, 10), poly.getVertex(5));
	}

	
	public void testComplement() {
		// create ring in CCW orientation
		LinearRing2D ring1 = new LinearRing2D(new Point2D[]{
				new Point2D(10, 10), 
				new Point2D(20, 10),
				new Point2D(20, 20),
				new Point2D(10, 20) });
		
		// create ring in CW orientation
		LinearRing2D ring2 = new LinearRing2D(new Point2D[]{
				new Point2D(13, 13), 
				new Point2D(13, 17),
				new Point2D(17, 17),
				new Point2D(17, 13) });
		
		MultiPolygon2D polygon = new MultiPolygon2D(new LinearRing2D[]{
				ring1, ring2});

		Polygon2D complement = polygon.complement();
		
		assertEquals(2, complement.getRings().size());
		
		Point2D pIn1 = new Point2D(14, 16);
		assertTrue(complement.contains(pIn1));
		
		Point2D pIn2 = new Point2D(21, 20);
		assertTrue(complement.contains(pIn2));
		
		Point2D pOut = new Point2D(11, 19);
		assertFalse(complement.contains(pOut));
		
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
		assertFalse(polygon.contains(new Point2D(15, 5)));
	}

	public void testContainsPoint2D_WithHole() {
		// create ring in CCW orientation
		LinearRing2D ring1 = new LinearRing2D(new Point2D[]{
				new Point2D(10, 10), 
				new Point2D(20, 10),
				new Point2D(20, 20),
				new Point2D(10, 20) });
		
		// create ring in CW orientation
		LinearRing2D ring2 = new LinearRing2D(new Point2D[]{
				new Point2D(13, 13), 
				new Point2D(13, 17),
				new Point2D(17, 17),
				new Point2D(17, 13) });
		
		MultiPolygon2D polygon = new MultiPolygon2D(new LinearRing2D[]{
				ring1, ring2});

		Point2D pOut1 = new Point2D(14, 16);
		assertFalse(polygon.contains(pOut1));
		
		Point2D pOut2 = new Point2D(21, 20);
		assertFalse(polygon.contains(pOut2));
		
		Point2D pIn = new Point2D(11, 19);
		assertTrue(polygon.contains(pIn));
		
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
