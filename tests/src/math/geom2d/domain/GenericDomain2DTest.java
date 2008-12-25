/**
 * File: 	GenericDomain2DTest.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 23 nov. 08
 */
package math.geom2d.domain;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.StraightLine2D;
import junit.framework.TestCase;

/**
 * @author dlegland
 *
 */
public class GenericDomain2DTest extends TestCase {

	/**
	 * Test method for {@link math.geom2d.domain.GenericDomain2D#isBounded()}.
	 */
	public void testIsBounded() {
		double xc = 10;
		double yc = 20;
		double r = 30;
		Circle2D circle = new Circle2D(xc, yc, r);
		
		Domain2D boundedDomain = new GenericDomain2D(circle);
		assertTrue(boundedDomain.isBounded());
		
		Domain2D unboundedDomain = new GenericDomain2D(circle.getReverseCurve());
		assertTrue(!unboundedDomain.isBounded());
		
		StraightLine2D line = 
			new StraightLine2D(new Point2D(xc, yc), new Vector2D(1, 2));
		Domain2D halfDomain = new GenericDomain2D(line);
		assertTrue(!halfDomain.isBounded());
		
		Domain2D halfDomain2 = new GenericDomain2D(line.getReverseCurve());
		assertTrue(!halfDomain2.isBounded());
		
	}

}
