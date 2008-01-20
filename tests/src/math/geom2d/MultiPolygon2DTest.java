package math.geom2d;

import junit.framework.TestCase;

import java.util.*;

public class MultiPolygon2DTest extends TestCase {

	public void testGetVertices() {
		Point2D[] pts1 = new Point2D[]{
				new Point2D(0, 0), new Point2D(10, 0),
				new Point2D(10, 10), new Point2D(0, 10) };
		Polygon2D pol1 = new Polygon2D(pts1);
		
		Point2D[] pts2 = new Point2D[]{
				new Point2D(20, 0), new Point2D(30, 0),
				new Point2D(30, 10), new Point2D(20, 10) };
		Polygon2D pol2 = new Polygon2D(pts2);		
		
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
		Polygon2D pol1 = new Polygon2D(pts1);
		
		Point2D[] pts2 = new Point2D[]{
				new Point2D(20, 0), new Point2D(30, 0),
				new Point2D(30, 10), new Point2D(20, 10) };
		Polygon2D pol2 = new Polygon2D(pts2);		
		
		MultiPolygon2D polygon = new MultiPolygon2D();
		polygon.addPolygon(pol1);
		polygon.addPolygon(pol2);
		
		assertTrue(polygon.getVerticesNumber()==8);
	}

	public void testContainsPoint2D() {
		Point2D[] pts1 = new Point2D[]{
				new Point2D(0, 0), new Point2D(10, 0),
				new Point2D(10, 10), new Point2D(0, 10) };
		Polygon2D pol1 = new Polygon2D(pts1);
		
		Point2D[] pts2 = new Point2D[]{
				new Point2D(20, 0), new Point2D(30, 0),
				new Point2D(30, 10), new Point2D(20, 10) };
		Polygon2D pol2 = new Polygon2D(pts2);		
		
		MultiPolygon2D polygon = new MultiPolygon2D();
		polygon.addPolygon(pol1);
		polygon.addPolygon(pol2);
		
		assertTrue(polygon.contains(new Point2D(5, 5)));
		assertTrue(polygon.contains(new Point2D(25, 5)));
		assertTrue(!polygon.contains(new Point2D(15, 5)));
	}

}
