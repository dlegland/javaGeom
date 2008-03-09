/*
 * File : Ellipse2DTest.java
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
 * Created on 27 déc. 2003
 */

package math.geom2d.conic;

import junit.framework.TestCase;

import java.util.*;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.transform.AffineTransform2D;
import math.geom2d.transform.GenericAffineTransform2D;

/**
 * @author Legland
 */
public class Ellipse2DTest extends TestCase {

	/**
	 * Constructor for Ellipse2DTest.
	 * @param arg0
	 */
	public Ellipse2DTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(Ellipse2DTest.class);
	}

	
	public void testGetProjectedPoint(){
		Ellipse2D el1 = new Ellipse2D(0, 0, 10, 10);
		assertEquals(el1.getProjectedPoint(new Point2D(20, 0)), new Point2D(10, 0));
		assertEquals(el1.getProjectedPoint(new Point2D(0, 30)), new Point2D(0, 10));
		assertEquals(el1.getProjectedPoint(new Point2D(30, 30)), 
				Point2D.createPolar(10, Math.PI/4));
		
	}
	
	public void testGetTangent() {
		Ellipse2D el1 = new Ellipse2D(0, 0, 20, 10);
		assertTrue(el1.getTangent(0).equals(new Vector2D(0, 10)));
		assertTrue(el1.getTangent(Math.PI/2).equals(new Vector2D(-20, 0)));

		Ellipse2D el2 = new Ellipse2D(0, 0, 20, 10, Math.PI/2);
		assertTrue(el2.getTangent(0).equals(new Vector2D(-10, 0)));
		assertTrue(el2.getTangent(Math.PI/2).equals(new Vector2D(0, -20)));

		Ellipse2D el3 = new Ellipse2D(0, 0, 20, 10, 0, false);
		assertTrue(el3.getTangent(0).equals(new Vector2D(0, -10)));
		assertTrue(el3.getTangent(Math.PI/2).equals(new Vector2D(-20, 0)));

		Ellipse2D el4 = new Ellipse2D(0, 0, 20, 10, Math.PI/2, false);
		assertTrue(el4.getTangent(0).equals(new Vector2D(10, 0)));
		assertTrue(el4.getTangent(Math.PI/2).equals(new Vector2D(0, -20)));
	}
	
	public void testGetCurvature() {
		double a = 20;
		double b = 10;
		Ellipse2D el1 = new Ellipse2D(0, 0, a, b);
		double k1 = el1.getCurvature(0);
		assertEquals(k1, a/b/b, Shape2D.ACCURACY);
		double k2 = el1.getCurvature(Math.PI/2);
		assertEquals(k2, b/a/a, Shape2D.ACCURACY);
	}
	
	public void testGetType() {
		Ellipse2D el1 = new Ellipse2D(20, 30, 20, 30, Math.PI/3);
		Ellipse2D el2 = new Ellipse2D(20, 30, 20, 20, Math.PI/3);
		assertEquals(el1.getConicType(), Conic2D.ELLIPSE);
		assertEquals(el2.getConicType(), Conic2D.CIRCLE);
	}

	public void testGetCartesianEquation() {
		Ellipse2D ellipse = new Ellipse2D(0, 0, 1, 1, 0);
		double[] coefs = ellipse.getCartesianEquation();
		assertEquals(coefs[0], 1, 1e-14);
		assertEquals(coefs[1], 0, 1e-14);
		assertEquals(coefs[2], 1, 1e-14);
		assertEquals(coefs[3], 0, 1e-14);
		assertEquals(coefs[4], 0, 1e-14);
		assertEquals(coefs[5], -1, 1e-14);
		
		ellipse = new Ellipse2D(20, 30, 1, 1, 0);
		coefs = ellipse.getCartesianEquation();
		assertEquals(coefs[0], 1, 1e-14);
		assertEquals(coefs[1], 0, 1e-14);
		assertEquals(coefs[2], 1, 1e-14);
		assertEquals(coefs[3], -40, 1e-14);
		assertEquals(coefs[4], -60, 1e-14);
		assertEquals(coefs[5], 1299, 1e-14);
		
		ellipse = new Ellipse2D(20, 30, 20, 10, 0);
		coefs = ellipse.getCartesianEquation();
		assertEquals(coefs[0], 100, 1e-14);
		assertEquals(coefs[1], 0, 1e-14);
		assertEquals(coefs[2], 400, 1e-14);
//		assertEquals(coefs[3], -800, 1e-14);
//		assertEquals(coefs[4], -600, 1e-14);
//		assertEquals(coefs[5], 1100, 1e-14);
	}
	
	public void testIsCircle(){
		Ellipse2D el1 = new Ellipse2D(20, 30, 20, 30);
		assertTrue(!el1.isCircle());
		Ellipse2D el2 = new Ellipse2D(20, 30, 20, 20);
		assertTrue(el2.isCircle());		
	}

	public void testGetExcentricity(){
	}

	public void testGetSignedDistance(){
		Ellipse2D el1 = new Ellipse2D(0, 0, 20, 10);
		assertEquals(el1.getSignedDistance(new Point2D(25, 0)), 5, Shape2D.ACCURACY);
		assertEquals(el1.getSignedDistance(new Point2D(0, 15)), 5, Shape2D.ACCURACY);
//		assertEquals(el1.getSignedDistance(new Point2D(15, 0)), -5, Shape2D.ACCURACY);
//		assertEquals(el1.getSignedDistance(new Point2D(0, 5)), -5, Shape2D.ACCURACY);
	}

	public void testIsInside(){
		Ellipse2D el1 = new Ellipse2D(0, 0, 20, 10);
		assertTrue(el1.isInside(new Point2D(0, 0)));
		assertTrue(el1.isInside(new Point2D(19.5, 0)));
		assertTrue(el1.isInside(new Point2D(0, 9.5)));
		assertTrue(!el1.isInside(new Point2D(20.5, 0)));
		assertTrue(!el1.isInside(new Point2D(0, 10.5)));

		Ellipse2D el2 = new Ellipse2D(0, 0, 20, 10, Math.PI/2);
		assertTrue(el2.isInside(new Point2D(0, 0)));
		assertTrue(el2.isInside(new Point2D(9.5, 0)));
		assertTrue(el2.isInside(new Point2D(0, 19.5)));
		assertTrue(!el2.isInside(new Point2D(10.5, 0)));
		assertTrue(!el2.isInside(new Point2D(0, 20.5)));
	}

	public void testGetDistance(){
		Ellipse2D el1 = new Ellipse2D(0, 0, 20, 10);
		assertEquals(el1.getDistance(new Point2D(30, 0)), 10, Shape2D.ACCURACY);
		assertEquals(el1.getDistance(new Point2D(0, 20)), 10, Shape2D.ACCURACY);
	}
	
	/**
	 * check that for a given position, computing the point on the curve, 
	 * then computing position of the point, gives the initial position.
	 */
	public void testGetPosition(){
		Ellipse2D el1 = new Ellipse2D(0, 0, 20, 10);
		assertEquals(el1.getPosition(el1.getPoint(.4)), .4, Shape2D.ACCURACY);
		//assertEquals(el1.getPosition(new Point2D(30, 0)), 0, Shape2D.ACCURACY);

		Ellipse2D el2 = new Ellipse2D(10, 5, 20, 10);
		assertEquals(el2.getPosition(el2.getPoint(.4)), .4, Shape2D.ACCURACY);
		
		Ellipse2D el3 = new Ellipse2D(10, 5, 20, 10, Math.PI/3);
		assertEquals(el3.getPosition(el3.getPoint(.4)), .4, Shape2D.ACCURACY);
	}
	
	/** 
	 * Test intersection of 'horizontal' ellipse with a vertical straight
	 * line cutting ellipse at x=r1/2. 
	 */
	public void testGetIntersectionsLine(){
		Ellipse2D el1 = new Ellipse2D(0, 0, 20, 10);
		StraightLine2D line = new StraightLine2D(10, 0, 0, 1);
		Point2D point1 = new Point2D(10, -Math.sqrt(3)*5);
		Point2D point2 = new Point2D(10, Math.sqrt(3)*5);
		Collection<Point2D> points = el1.getIntersections(line);
		Iterator<Point2D> iter = points.iterator();
		assertEquals(point1, iter.next());
		assertEquals(point2, iter.next());		
	}
	
	public void testGetSubCurve(){
		Ellipse2D ellipse;
		EllipseArc2D arc1, arc2;
		
		// try with a direct ellipse 
		ellipse = new Ellipse2D(0, 0, 20, 10, 0, true);
		arc1 = new EllipseArc2D(ellipse, Math.PI/2, Math.PI/2);		
		arc2 = (EllipseArc2D) ellipse.getSubCurve(Math.PI/2, Math.PI);
		assertTrue(arc1.equals(arc2));
		
		// try again with an indirect ellipse
		ellipse = new Ellipse2D(0, 0, 20, 10, 0, false);
		arc1 = new EllipseArc2D(ellipse, Math.PI/2, -Math.PI/2);
		arc2 = (EllipseArc2D) ellipse.getSubCurve(3*Math.PI/2, 2*Math.PI);
		assertTrue(arc1.equals(arc2));
	}

	public void testEquals(){
		Ellipse2D ell1 = new Ellipse2D(100, 100, 50, 30, Math.PI/3);
		Ellipse2D ell2 = new Ellipse2D(100, 100, 50, 30, Math.PI/3);
		Ellipse2D ell3 = new Ellipse2D(100, 100, 50, 31, Math.PI/3);
		Ellipse2D ell4 = new Ellipse2D(100, 100, 50, 30, Math.PI/4);
		
		assertTrue(ell1.equals(ell2));
		assertTrue(!ell1.equals(ell3));
		assertTrue(!ell1.equals(ell4));
	}
	
	public void testTransform(){
		// goal is to check affine transform of an ellipse
		Ellipse2D ellipse = new Ellipse2D(100, 100, 50, 30, 0);
		
		AffineTransform2D aff = new GenericAffineTransform2D(1, 0, 0, 0, 1, 0);
		Ellipse2D ell1 = ellipse.transform(aff);
		assertTrue(ell1 instanceof Ellipse2D);
		assertTrue(ell1.equals(ellipse));
		
		AffineTransform2D aff2 = new GenericAffineTransform2D(1./5., 0, 0, 0, 1./3., 0);
		Ellipse2D ell2 = ellipse.transform(aff2);
		Ellipse2D ell2th = new Circle2D(100./5., 100./3., 10);
		assertTrue(ell2 instanceof Ellipse2D);
		assertTrue(ell2.equals(ell2th));
		
		// Try with a rotated base ellipse
		ellipse = new Ellipse2D(100, 100, 50, 30, Math.PI/3);
		AffineTransform2D aff3 = new GenericAffineTransform2D(1, 0, 0, 0, 1, 0);
		Ellipse2D ell3 = ellipse.transform(aff3);
		assertTrue(ell3 instanceof Ellipse2D);
		assertTrue(ell3.equals(ellipse));
		
		// At the moment, I do not how to compute parameters of transformed ellipse,
		// so I can only check results visually.
//		AffineTransform2D aff4 = new AffineTransform2D(1./5., 0, 0, 0, 1./3., 0);
//		Ellipse2D ell4 = (Ellipse2D) ellipse.transform(aff4);
//		Ellipse2D ell4th = new Ellipse2D(100./5., 100./3., 10, 10, Math.PI/3);
//		assertTrue(ell4.isEllipse());
//		assertTrue(ell4.equals(ell4th));
	}

}
