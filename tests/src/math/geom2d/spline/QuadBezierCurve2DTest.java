/* File QuadBezierCurve2DTest.java 
 *
 * Project : geometry
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 * 
 * author : Legland
 * Created on 3 août 2004
 */

package math.geom2d.spline;

import junit.framework.TestCase;
import math.geom2d.Point2D;
import math.geom2d.polygon.Polyline2D;

/**
 * @author Legland
 */
public class QuadBezierCurve2DTest extends TestCase {

	/**
	 * Constructor for QuadCurve2DTest.
	 * @param arg0
	 */
	public QuadBezierCurve2DTest(String arg0) {
		super(arg0);
	}

	public void testAsPolyline() {
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(0, 1);
		Point2D p3 = new Point2D(1, 1);
		
		QuadBezierCurve2D bezier = new QuadBezierCurve2D(p1, p2, p3);
		Polyline2D poly = bezier.asPolyline(4);
		assertEquals(5, poly.vertexNumber());
	}
	
	public void testBezierCurve2D_DoubleArray() {
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(0, 1);
		Point2D p3 = new Point2D(1, 1);
		
		QuadBezierCurve2D bezier1 = new QuadBezierCurve2D(p1, p2, p3);
		double[][] tab = bezier1.getParametric();
		
		QuadBezierCurve2D bezier2 = new QuadBezierCurve2D(tab);
		assertTrue(p1.equals(bezier2.firstPoint()));
		assertTrue(p2.equals(bezier2.getControl()));
		assertTrue(p3.equals(bezier2.lastPoint()));
		
		p1 = new Point2D(100, 100);
		p2 = new Point2D(100, 200);
		p3 = new Point2D(200, 100);
		
		bezier1 = new QuadBezierCurve2D(p1, p2, p3);
		tab = bezier1.getParametric();		
		bezier2 = new QuadBezierCurve2D(tab);
		
		assertTrue(p1.equals(bezier2.firstPoint()));
		assertTrue(p2.equals(bezier2.getControl()));
		assertTrue(p3.equals(bezier2.lastPoint()));
	}
	

	/*
	 * Test for Point2D getPoint(double)
	 */
	public void testGetPointdouble(){
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(0, 1);
		Point2D p3 = new Point2D(1, 1);
		
		QuadBezierCurve2D bezier1 = new QuadBezierCurve2D(p1, p2, p3);
		
		assertEquals(bezier1.point(0), p1);
		assertEquals(bezier1.point(1), p3);
	}
		
	public void testGetPosition(){
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(0, 1);
		Point2D p3 = new Point2D(1, 1);
		
		QuadBezierCurve2D bezier1 = new QuadBezierCurve2D(p1, p2, p3);
		assertEquals(bezier1.position(p1), 0, 1e-6);
		assertEquals(bezier1.position(p3), 1, 1e-6);		
	}
	
	public void testGetDistance(){
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(0, 1);
		Point2D p3 = new Point2D(1, 1);
		
		QuadBezierCurve2D bezier1 = new QuadBezierCurve2D(p1, p2, p3);

		assertEquals(bezier1.distance(p1), 0, 1e-10);
		assertEquals(bezier1.distance(p3), 0, 1e-10);
	}

}
