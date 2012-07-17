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

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;
import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.line.StraightLine2D;

/**
 * @author Legland
 */
public class Ellipse2DTest extends TestCase {

	private void assertVectorAlmostEquals(Vector2D v1, Vector2D v2, double eps) {
		assertTrue(v1.almostEquals(v2, eps));
	}
	
	private void assertPointAlmostEquals(Point2D p1, Point2D p2, double eps) {
		assertTrue(p1.almostEquals(p2, eps));
	}
	
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

	public void testCreatePointPointDouble(){
		Ellipse2D base = new Ellipse2D(20, 30, 10, 4, Math.PI/3);
		Point2D focus1 = base.focus1();
		Point2D focus2 = base.focus2();
		double chord = base.semiMajorAxisLength()*2;
		Ellipse2D created = Ellipse2D.create(focus1, focus2, chord);
		
		assertTrue(base.almostEquals(created, Shape2D.ACCURACY));
	}
	
	public void testReduceCentered(){
		double[] coefs = {1./400., 0, 1./100.};
		Ellipse2D ell0 = Ellipse2D.reduceCentered(coefs);
		assertTrue(ell0.equals(new Ellipse2D(0, 0, 20, 10, 0)));
		
		double[] coefs2 = {1./400., 0, 1./100., 0, 0, -1};
		Ellipse2D ell2 = Ellipse2D.reduceCentered(coefs2);
		assertTrue(ell2.equals(new Ellipse2D(0, 0, 20, 10, 0)));

		double[] coefs3 = {1., 0, 4., 0, 0, -400};
		Ellipse2D ell3 = Ellipse2D.reduceCentered(coefs3);
		assertTrue(ell3.equals(new Ellipse2D(0, 0, 20, 10, 0)));

		double theta = Math.PI / 3;
		double[] rotCoefs = Conics2D.transformCentered(coefs,
				AffineTransform2D.createRotation(theta));
		Ellipse2D ellRot = Ellipse2D.reduceCentered(rotCoefs);
		Ellipse2D expected = new Ellipse2D(0, 0, 20, 10, theta);
		assertTrue(ellRot.almostEquals(expected, Shape2D.ACCURACY));
	}
	
	public void testTransformCentered(){
		Ellipse2D ell0 = new Ellipse2D(0, 0, 20, 10, 0);
		
		// Check rotation of an ellipse
		double theta = Math.PI/3;
		AffineTransform2D rot60 = AffineTransform2D.createRotation(theta);
		Ellipse2D resRot = Ellipse2D.transformCentered(ell0, rot60);
		Ellipse2D expRot = new Ellipse2D(0, 0, 20, 10, theta);
		assertTrue(resRot.almostEquals(expRot, Shape2D.ACCURACY));
		
		// Check scaling of an ellipse
		double sx = 2.5; double sy = 3;
		AffineTransform2D sca = AffineTransform2D.createScaling(sx, sy);
		Ellipse2D resSca = Ellipse2D.transformCentered(ell0, sca);
		Ellipse2D expSca = new Ellipse2D(0, 0, 20.*sx, 10.*sy, 0);
		assertTrue(resSca.almostEquals(expSca, Shape2D.ACCURACY));

		// Check scaling and rotation
		Ellipse2D resBoth = Ellipse2D.transformCentered(resSca, rot60);
		Ellipse2D expBoth = new Ellipse2D(0, 0, 20.*sx, 10.*sy, theta);
		assertTrue(resBoth.almostEquals(expBoth, Shape2D.ACCURACY));
	}
	
	public void testGetProjectedPoint(){
		Ellipse2D el1 = new Ellipse2D(0, 0, 10, 10);
		double eps = Shape2D.ACCURACY;
		
		assertPointAlmostEquals(new Point2D(10, 0), 
				el1.projectedPoint(new Point2D(20, 0)), eps);
		assertPointAlmostEquals(new Point2D(0, 10), 
				el1.projectedPoint(new Point2D(0, 30)), eps);
		assertPointAlmostEquals(Point2D.createPolar(10, Math.PI/4), 
				el1.projectedPoint(new Point2D(30, 30)), eps);
	}
	
	public void testGetTangent() {
		double eps = Shape2D.ACCURACY;
		
		Ellipse2D el1 = new Ellipse2D(0, 0, 20, 10);
		assertVectorAlmostEquals(new Vector2D(0, 10), el1.tangent(0), eps);
		assertVectorAlmostEquals(new Vector2D(-20, 0), el1.tangent(Math.PI/2), eps);

		Ellipse2D el2 = new Ellipse2D(0, 0, 20, 10, Math.PI/2);
		assertVectorAlmostEquals(new Vector2D(-10, 0), el2.tangent(0), eps);
		assertVectorAlmostEquals(new Vector2D(0, -20), el2.tangent(Math.PI/2), eps);

		Ellipse2D el3 = new Ellipse2D(0, 0, 20, 10, 0, false);
		assertVectorAlmostEquals(new Vector2D(0, -10), el3.tangent(0), eps);
		assertVectorAlmostEquals(new Vector2D(-20, 0), el3.tangent(Math.PI/2), eps);

		Ellipse2D el4 = new Ellipse2D(0, 0, 20, 10, Math.PI/2, false);
		assertVectorAlmostEquals(new Vector2D(10, 0), el4.tangent(0), eps);
		assertVectorAlmostEquals(new Vector2D(0, -20), el4.tangent(Math.PI/2), eps);
	}
	
	public void testGetCurvature() {
		double a = 20;
		double b = 10;
		Ellipse2D el1 = new Ellipse2D(0, 0, a, b);
		double k1 = el1.curvature(0);
		assertEquals(k1, a/b/b, Shape2D.ACCURACY);
		double k2 = el1.curvature(Math.PI/2);
		assertEquals(k2, b/a/a, Shape2D.ACCURACY);
	}
	
	public void testGetType() {
		Ellipse2D el1 = new Ellipse2D(20, 30, 20, 30, Math.PI/3);
		Ellipse2D el2 = new Ellipse2D(20, 30, 20, 20, Math.PI/3);
		assertEquals(el1.conicType(), Conic2D.Type.ELLIPSE);
		assertEquals(el2.conicType(), Conic2D.Type.CIRCLE);
	}

	public void testGetConicCoefficients() {
		double a1 = 1;	double a2 = 20;
		double b1 = 1;  double b2 = 10;
		double xc = 20; double yc = 30;
		double theta = Math.PI/3;
		int Npts = 13;
		
		Ellipse2D ellipse = new Ellipse2D(0, 0, a1, 1, 0);
		double[] coefs = ellipse.conicCoefficients();
		assertEquals(coefs[0], 1, 1e-14);
		assertEquals(coefs[1], 0, 1e-14);
		assertEquals(coefs[2], 1, 1e-14);
		assertEquals(coefs[3], 0, 1e-14);
		assertEquals(coefs[4], 0, 1e-14);
		assertEquals(coefs[5], -1, 1e-14);
		
		ellipse = new Ellipse2D(xc, yc, a1, b1, 0);
		coefs = ellipse.conicCoefficients();
		assertEquals(coefs[0], 1, 1e-14);
		assertEquals(coefs[1], 0, 1e-14);
		assertEquals(coefs[2], 1, 1e-14);
		assertEquals(coefs[3], -40, 1e-14);
		assertEquals(coefs[4], -60, 1e-14);
		assertEquals(coefs[5], 1299, 1e-14);
		
		ellipse = new Ellipse2D(xc, yc, a2, b2, 0);
		coefs = ellipse.conicCoefficients();
		for(int i=0; i<Npts; i++){
			double pos =((double)i)*2*Math.PI/Npts;
			Point2D point = ellipse.point(pos);
			double x = point.x();
			double y = point.y();
			double sum = coefs[0]*x*x + coefs[1]*x*y + coefs[2]*y*y +
				coefs[3]*x + coefs[4]*y + coefs[5];
			assertTrue(Math.abs(sum)<1e-12);			
		}
		
		ellipse = new Ellipse2D(xc, yc, a2, b2, theta);
		coefs = ellipse.conicCoefficients();
		for(int i=0; i<Npts; i++){
			double pos =((double)i)*2*Math.PI/Npts;
			Point2D point = ellipse.point(pos);
			double x = point.x();
			double y = point.y();
			double sum = coefs[0] * x * x + coefs[1] * x * y + coefs[2] * y * y
					+ coefs[3] * x + coefs[4] * y + coefs[5];
			assertTrue(Math.abs(sum) < 1e-12);	
		}
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
		assertEquals(el1.signedDistance(new Point2D(25, 0)), 5, Shape2D.ACCURACY);
		assertEquals(el1.signedDistance(new Point2D(0, 15)), 5, Shape2D.ACCURACY);
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
		assertEquals(el1.distance(new Point2D(30, 0)), 10, Shape2D.ACCURACY);
		assertEquals(el1.distance(new Point2D(0, 20)), 10, Shape2D.ACCURACY);
		
		double pos = .3;
		Point2D p0 = el1.point(pos);
		Vector2D v0 = el1.normal(pos);
		
		double d = 6;
		Point2D p1 = p0.plus(v0.normalize().times(d));
		double dist = p1.distance(p0);
		assertEquals(d, dist, 1e-6);
	}
	
	/**
	 * check that for a given position, computing the point on the curve, 
	 * then computing position of the point, gives the initial position.
	 */
	public void testGetPosition(){
		double eps = Shape2D.ACCURACY;
		
		Ellipse2D el1 = new Ellipse2D(0, 0, 20, 10);
		assertEquals(el1.position(el1.point(.4)), .4, eps);
		
		Ellipse2D el2 = new Ellipse2D(10, 5, 20, 10);
		assertEquals(el2.position(el2.point(.4)), .4, eps);
		
		Ellipse2D el3 = new Ellipse2D(10, 5, 20, 10, Math.PI/3);
		assertEquals(el3.position(el3.point(.4)), .4, eps);
		
		Ellipse2D el1i = new Ellipse2D(0, 0, 20, 10, 0, false);
		assertEquals(el1i.position(el1i.point(.4)), .4, eps);
		
		Ellipse2D el2i = new Ellipse2D(10, 5, 20, 10, 0, false);
		assertEquals(el2i.position(el2i.point(.4)), .4, eps);
		
		Ellipse2D el3i = new Ellipse2D(10, 5, 20, 10, Math.PI/3, false);
		assertEquals(el3i.position(el3i.point(.4)), .4, eps);
	}
	
	/**
	 * check that for a given point, projecting the point on the curve, 
	 * then computing the location of the point, gives the initial position.
	 */
	public void testProject(){
		double eps = Shape2D.ACCURACY;
		
		Ellipse2D el1 = new Ellipse2D(0, 0, 20, 10);
		assertEquals(el1.project(el1.point(.4)), .4, eps);
		
		Ellipse2D el2 = new Ellipse2D(10, 5, 20, 10);
		assertEquals(el2.project(el2.point(.4)), .4, eps);
		
		Ellipse2D el3 = new Ellipse2D(10, 5, 20, 10, Math.PI/3);
		assertEquals(el3.project(el3.point(.4)), .4, eps);
		
		Ellipse2D el1i = new Ellipse2D(0, 0, 20, 10, 0, false);
		assertEquals(el1i.project(el1i.point(.4)), .4, eps);
		
		Ellipse2D el2i = new Ellipse2D(10, 5, 20, 10, 0, false);
		assertEquals(el2i.project(el2i.point(.4)), .4, eps);
		
		Ellipse2D el3i = new Ellipse2D(10, 5, 20, 10, Math.PI/3, false);
		assertEquals(el3i.project(el3i.point(.4)), .4, eps);
	}
	
	/**
	 * check that for a given position, computing the point on the curve, 
	 * then computing position of the point, gives the initial position.
	 */
	public void testGetPoint(){
		double eps = Shape2D.ACCURACY;
		
		Ellipse2D el1 = new Ellipse2D(0, 0, 20, 10);
		Point2D point = el1.point(Math.PI/2);
		Point2D expected = new Point2D(0, 10);
		assertPointAlmostEquals(expected, point, eps);
		
		Ellipse2D el2 = new Ellipse2D(10, 5, 20, 10);
		point = el2.point(Math.PI/2);
		expected = new Point2D(10, 15);
		assertPointAlmostEquals(expected, point, eps);
		
		Ellipse2D el1i = new Ellipse2D(0, 0, 20, 10, 0, false);
		point = el1i.point(Math.PI/2);
		expected = new Point2D(0, -10);
		assertPointAlmostEquals(expected, point, eps);
		
		Ellipse2D el2i = new Ellipse2D(10, 5, 20, 10, 0, false);
		point = el2i.point(Math.PI/2);
		expected = new Point2D(10, -5);
		assertPointAlmostEquals(expected, point, eps);
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
		Collection<Point2D> points = el1.intersections(line);
		Iterator<Point2D> iter = points.iterator();
		
		double eps = Shape2D.ACCURACY;
		assertPointAlmostEquals(point1, iter.next(), eps);
		assertPointAlmostEquals(point2, iter.next(), eps);		
	}
	
	public void testGetSubCurve(){
		Ellipse2D ellipse;
		EllipseArc2D arc1, arc2;
		
		// try with a direct ellipse 
		ellipse = new Ellipse2D(0, 0, 20, 10, 0, true);
		arc1 = new EllipseArc2D(ellipse, Math.PI/2, Math.PI/2);		
		arc2 = ellipse.subCurve(Math.PI/2, Math.PI);
		assertTrue(arc1.equals(arc2));
		
		// try again with an indirect ellipse
		ellipse = new Ellipse2D(0, 0, 20, 10, 0, false);
		arc1 = new EllipseArc2D(ellipse, 3*Math.PI/2, -Math.PI/2);
		arc2 = ellipse.subCurve(Math.PI/2, Math.PI);
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
	
	/**
	 * Check transformation of ellipse with various transforms.
	 */
	public void testTransform(){
		// goal is to check affine transform of an ellipse
		Ellipse2D ellipse = new Ellipse2D(100, 100, 50, 30, 0);
		
		// Identity
		AffineTransform2D aff = new AffineTransform2D(1, 0, 0, 0, 1, 0);
		Ellipse2D ell1 = ellipse.transform(aff);
		assertTrue(ell1.equals(ellipse));
		
		// Non uniform scaling
		AffineTransform2D aff2 = new AffineTransform2D(1./5., 0, 0, 0, 1./3., 0);
		Ellipse2D ell2 = ellipse.transform(aff2);
		Ellipse2D ell2th = new Circle2D(100./5., 100./3., 10);
		assertTrue(ell2.almostEquals(ell2th, Shape2D.ACCURACY));
		
		// Try with a rotated base ellipse
		ellipse = new Ellipse2D(100, 100, 50, 30, Math.PI/3);
		AffineTransform2D aff3 = new AffineTransform2D(1, 0, 0, 0, 1, 0);
		Ellipse2D ell3 = ellipse.transform(aff3);
		assertTrue(ell3.almostEquals(ellipse, Shape2D.ACCURACY));
		
		// At the moment, I do not how to compute parameters of transformed ellipse,
		// so I can only check results visually.
//		AffineTransform2D aff4 = new AffineTransform2D(1./5., 0, 0, 0, 1./3., 0);
//		Ellipse2D ell4 = (Ellipse2D) ellipse.transform(aff4);
//		Ellipse2D ell4th = new Ellipse2D(100./5., 100./3., 10, 10, Math.PI/3);
//		assertTrue(ell4.isEllipse());
//		assertTrue(ell4.equals(ell4th));
	}
	
	public void testClone() {
	    // direct ellipse
	    Ellipse2D ellipse = new Ellipse2D(10, 20, 30, 40, Math.PI/3);
	    assertTrue(ellipse.equals(ellipse.clone()));
	    
	    // indirect ellipse
        ellipse = new Ellipse2D(10, 20, 30, 40, Math.PI/3, false);
        assertTrue(ellipse.equals(ellipse.clone()));
	}

}
