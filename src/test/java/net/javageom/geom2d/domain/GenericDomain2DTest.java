/**
 * File: 	GenericDomain2DTest.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 23 nov. 08
 */
package net.javageom.geom2d.domain;

import junit.framework.TestCase;
import net.javageom.geom2d.Box2D;
import net.javageom.geom2d.Point2D;
import net.javageom.geom2d.Vector2D;
import net.javageom.geom2d.conic.Circle2D;
import net.javageom.geom2d.line.StraightLine2D;
import net.javageom.geom2d.polygon.Polygon2D;

/**
 * @author dlegland
 *
 */
public class GenericDomain2DTest extends TestCase {

	/**
	 * Test method for {@link net.javageom.geom2d.domain.GenericDomain2D#isBounded()}.
	 */
	public void testIsBounded() {
		double xc = 10;
		double yc = 20;
		double r = 30;
		Circle2D circle = new Circle2D(xc, yc, r);
		
		Domain2D boundedDomain = new GenericDomain2D(circle);
		assertTrue(boundedDomain.isBounded());
		
		Domain2D unboundedDomain = new GenericDomain2D(circle.reverse());
		assertTrue(!unboundedDomain.isBounded());
		
		StraightLine2D line = 
			new StraightLine2D(new Point2D(xc, yc), new Vector2D(1, 2));
		Domain2D halfDomain = new GenericDomain2D(line);
		assertTrue(!halfDomain.isBounded());
		
		Domain2D halfDomain2 = new GenericDomain2D(line.reverse());
		assertTrue(!halfDomain2.isBounded());
		
	}

	public void testGetAsPolygonInt_Disc() {
		double xc = 10;
		double yc = 20;
		double r = 30;
		Circle2D circle = new Circle2D(xc, yc, r);
		
		Domain2D domain = new GenericDomain2D(circle);
		
		Polygon2D polygon = domain.asPolygon(32);
		
		assertTrue(polygon.isBounded());
		
		Box2D expBox = new Box2D(-20, 40, -10, 50);
		Box2D bbox = polygon.boundingBox();
		assertTrue(bbox.almostEquals(expBox, 1e-14));
	}
	
	public void testGetAsPolygonInt_TwoDiscs() {
		double xc1 = 10;
		double yc1 = 20;
		double r1 = 30;
		Circle2D circle1 = new Circle2D(xc1, yc1, r1);
		
		double xc2 = 40;
		double yc2 = 80;
		double r2 = 20;
		Circle2D circle2 = new Circle2D(xc2, yc2, r2);

		ContourArray2D<Circle2D> discs = new ContourArray2D<Circle2D>(
				new Circle2D[]{circle1, circle2});
		
		Domain2D domain = new GenericDomain2D(discs);
		
		Polygon2D polygon = domain.asPolygon(32);
		
		assertTrue(polygon.isBounded());
		
		Box2D expBox = new Box2D(-20, 60, -10, 100);
		Box2D bbox = polygon.boundingBox();
		assertTrue(bbox.almostEquals(expBox, 1e-14));
	}
}
