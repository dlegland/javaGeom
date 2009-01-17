package math.geom2d.polygon;

import junit.framework.TestCase;

import java.util.*;

import math.geom2d.Point2D;

public class MultiPolygon2DTest extends TestCase {

	public void testGetVertices() {
		Point2D[] pts1 = new Point2D[]{
				new Point2D(0, 0), new Point2D(10, 0),
				new Point2D(10, 10), new Point2D(0, 10) };
		SimplePolygon2D pol1 = new SimplePolygon2D(pts1);
		
		Point2D[] pts2 = new Point2D[]{
				new Point2D(20, 0), new Point2D(30, 0),
				new Point2D(30, 10), new Point2D(20, 10) };
		SimplePolygon2D pol2 = new SimplePolygon2D(pts2);		
		
		MultiPolygon2D polygon = new MultiPolygon2D();
		polygon.addPolygon(pol1);
		polygon.addPolygon(pol2);
		
		Collection<Point2D> vertices = polygon.getVertices();
		assertTrue(vertices.size()==pts1.length+pts2.length);
		
		for(int i=0; i<pts1.length; i++)
			assertTrue(vertices.contains(pts1[i]));
		for(int i=0; i<pts2.length; i++)
			assertTrue(vertices.contains(pts2[i]));
	}

	public void testGetVerticesNumber() {
		Point2D[] pts1 = new Point2D[]{
				new Point2D(0, 0), new Point2D(10, 0),
				new Point2D(10, 10), new Point2D(0, 10) };
		SimplePolygon2D pol1 = new SimplePolygon2D(pts1);
		
		Point2D[] pts2 = new Point2D[]{
				new Point2D(20, 0), new Point2D(30, 0),
				new Point2D(30, 10), new Point2D(20, 10) };
		SimplePolygon2D pol2 = new SimplePolygon2D(pts2);		
		
		MultiPolygon2D polygon = new MultiPolygon2D();
		polygon.addPolygon(pol1);
		polygon.addPolygon(pol2);
		
		assertTrue(polygon.getVertexNumber()==8);
	}
	
	public void testGetVertex(){
	    Ring2D tri1 = new Ring2D(new Point2D[]{
				new Point2D(0, 0), new Point2D(10, 0), new Point2D(10, 10)});
		Ring2D tri2 = new Ring2D(new Point2D[]{
				new Point2D(20, 0), new Point2D(30, 0), new Point2D(30, 10)});
		MultiPolygon2D poly = new MultiPolygon2D(
				new Ring2D[]{tri1,tri2});
		
		assertEquals(poly.getVertex(0), new Point2D(0, 0));
		assertEquals(poly.getVertex(1), new Point2D(10, 0));
		assertEquals(poly.getVertex(2), new Point2D(10, 10));
		assertEquals(poly.getVertex(3), new Point2D(20, 0));
		assertEquals(poly.getVertex(4), new Point2D(30, 0));
		assertEquals(poly.getVertex(5), new Point2D(30, 10));
	}

	public void testContainsPoint2D() {
		Point2D[] pts1 = new Point2D[]{
				new Point2D(0, 0), new Point2D(10, 0),
				new Point2D(10, 10), new Point2D(0, 10) };
		SimplePolygon2D pol1 = new SimplePolygon2D(pts1);
		
		Point2D[] pts2 = new Point2D[]{
				new Point2D(20, 0), new Point2D(30, 0),
				new Point2D(30, 10), new Point2D(20, 10) };
		SimplePolygon2D pol2 = new SimplePolygon2D(pts2);		
		
		MultiPolygon2D polygon = new MultiPolygon2D();
		polygon.addPolygon(pol1);
		polygon.addPolygon(pol2);
		
		assertTrue(polygon.contains(new Point2D(5, 5)));
		assertTrue(polygon.contains(new Point2D(25, 5)));
		assertTrue(!polygon.contains(new Point2D(15, 5)));
	}

}
