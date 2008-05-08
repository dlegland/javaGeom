/* File BezierCurve2DTest.java 
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

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import junit.framework.TestCase;

/**
 * @author Legland
 */
public class BezierCurve2DTest extends TestCase {

	/**
	 * Constructor for BezierCurve2DTest.
	 * @param arg0
	 */
	public BezierCurve2DTest(String arg0) {
		super(arg0);
	}

	
	public void testGetParametric() {
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(0, 1);
		Point2D p3 = new Point2D(1, 1);
		Point2D p4 = new Point2D(1, 0);
		
		BezierCurve2D bezier1 = new BezierCurve2D(p1, p2, p3, p4);
		double[][] tab = bezier1.getParametric();
		
		assertEquals(tab[0][0], 0, 1e-14);
		assertEquals(tab[0][1], 0, 1e-14);
		assertEquals(tab[0][2], 3, 1e-14);
		assertEquals(tab[0][3], -2, 1e-14);

		assertEquals(tab[1][0], 0, 1e-14);
		assertEquals(tab[1][1], 3, 1e-14);
		assertEquals(tab[1][2], -3, 1e-14);
		assertEquals(tab[1][3], 0, 1e-14);
	}	
	
	public void testBezierCurve2D_DoubleArray() {
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(0, 1);
		Point2D p3 = new Point2D(1, 1);
		Point2D p4 = new Point2D(1, 0);
		
		BezierCurve2D bezier1 = new BezierCurve2D(p1, p2, p3, p4);
		double[][] tab = bezier1.getParametric();
		
		BezierCurve2D bezier2 = new BezierCurve2D(tab);
		assertTrue(p1.equals(bezier2.getFirstPoint()));
		assertTrue(p2.equals(bezier2.getControl1()));
		assertTrue(p3.equals(bezier2.getControl2()));
		assertTrue(p4.equals(bezier2.getLastPoint()));
		
		p1 = new Point2D(100, 100);
		p2 = new Point2D(100, 200);
		p3 = new Point2D(200, 100);
		p4 = new Point2D(300, 200);
		
		bezier1 = new BezierCurve2D(p1, p2, p3, p4);
		tab = bezier1.getParametric();		
		bezier2 = new BezierCurve2D(tab);
		
		assertTrue(p1.equals(bezier2.getFirstPoint()));
		assertTrue(p2.equals(bezier2.getControl1()));
		assertTrue(p3.equals(bezier2.getControl2()));
		assertTrue(p4.equals(bezier2.getLastPoint()));
	}
	

	/*
	 * Test for Point2D getPoint(double)
	 */
	public void testGetPointdouble(){
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(0, 1);
		Point2D p3 = new Point2D(1, 1);
		Point2D p4 = new Point2D(1, 0);
		
		BezierCurve2D bezier1 = new BezierCurve2D(p1, p2, p3, p4);
		
		assertEquals(bezier1.getPoint(0), p1);
		assertEquals(bezier1.getPoint(1./3.), new Point2D(7./27., 2./3.));
		assertEquals(bezier1.getPoint(.5), new Point2D(.5, .75));
		assertEquals(bezier1.getPoint(2./3.), new Point2D(20./27., 2./3.));
		assertEquals(bezier1.getPoint(1), p4);
	}
		
	public void testGetTangent(){
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(0, 1);
		Point2D p3 = new Point2D(1, 1);
		Point2D p4 = new Point2D(1, 0);		
		BezierCurve2D bezier1 = new BezierCurve2D(p1, p2, p3, p4);
		
		Vector2D vect0 = bezier1.getTangent(0);
		assertEquals(vect0, new Vector2D(0, 3));
		Vector2D vect1 = bezier1.getTangent(1);
		assertEquals(vect1, new Vector2D(0, -3));
	}
	
	public void testGetPosition(){
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(0, 1);
		Point2D p3 = new Point2D(1, 1);
		Point2D p4 = new Point2D(1, 0);
		
		BezierCurve2D bezier1 = new BezierCurve2D(p1, p2, p3, p4);
//		System.out.println(bezier1.getPosition(p1));
//		System.out.println(bezier1.getPosition(p2));
//		System.out.println(bezier1.getPosition(p3));
//		System.out.println(bezier1.getPosition(p4));
		assertEquals(bezier1.getPosition(p1), 0, 1e-6);
		assertEquals(bezier1.getPosition(p4), 1, 1e-6);		
	}
	
	public void testGetDistance(){
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(0, 1);
		Point2D p3 = new Point2D(1, 1);
		Point2D p4 = new Point2D(1, 0);
		
		BezierCurve2D bezier1 = new BezierCurve2D(p1, p2, p3, p4);

		assertEquals(bezier1.getDistance(p1), 0, 1e-10);
		assertEquals(bezier1.getDistance(p4), 0, 1e-10);
	}	
}
