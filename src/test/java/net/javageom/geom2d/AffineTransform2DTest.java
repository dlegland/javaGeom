/*
 * File : AffineTransform2DTest.java
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
 * Created on 25 déc. 2003
 */
 
package net.javageom.geom2d;

import java.awt.geom.AffineTransform;

import junit.framework.TestCase;
import net.javageom.geom2d.line.StraightLine2D;

import static java.lang.Math.*;

/**
 * @author Legland
 */
public class AffineTransform2DTest extends TestCase {

	/**
	 * Constructor for AffineTransform2DTest.
	 * @param arg0
	 */
	public AffineTransform2DTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AffineTransform2DTest.class);
	}

	public void testIdentityConstructor() {
		AffineTransform2D trans = new AffineTransform2D();
		
		// some tests on the created Transform
		assertTrue(trans.isSimilarity());
		assertTrue(trans.isIsometry());
		assertTrue(trans.isDirect());
		assertTrue(trans.isMotion());
		assertTrue(trans.isIdentity());
	}
	
	public void testGlideReflection(){
		StraightLine2D line = new StraightLine2D(
				new Point2D(10, 20), new Point2D(50, 40));
		double distance = 10;
		AffineTransform2D trans = AffineTransform2D.createGlideReflection(line, distance);
		
		// some tests on the created Transform
		assertTrue(trans.isSimilarity());
		assertTrue(trans.isIsometry());
		assertFalse(trans.isDirect());
		assertFalse(trans.isMotion());
		assertFalse(trans.isIdentity());
		
		// test a sample point
		Point2D p = new Point2D(10, 20);
		Point2D pt = trans.transform(p);
		assertTrue(line.contains(pt));
	}
	
	public void testRotation45Centered(){
		Point2D center = new Point2D(10, 20);
		Point2D point = new Point2D(10+5, 20);
		double theta = PI / 4;
		AffineTransform2D trans =  
			AffineTransform2D.createRotation(center, theta);
		
		// basic tests on transform type
		assertTrue(trans.isIsometry());
		assertTrue(trans.isDirect());
		assertTrue(trans.isMotion());
		assertTrue(trans.isSimilarity());
		assertFalse(trans.isIdentity());

		Point2D point2 = trans.transform(point);
		double shift = sqrt(2) * 5 / 2;
		Point2D expect = new Point2D(10 + shift, 20 + shift);
		assertEquals(expect.distance(point2), 0, 1e-14);
	}
	
	public void testRotations() {
		AffineTransform2D trans;

		trans = AffineTransform2D.createRotation(2, 3, PI / 3);
		assertTrue(trans.isMotion());

		trans = AffineTransform2D.createRotation(2, 3, 4 * PI / 3);
		assertTrue(trans.isMotion());

		trans = AffineTransform2D.createRotation(2, 3, 7 * PI / 3);
		assertTrue(trans.isMotion());

		trans = AffineTransform2D.createRotation(2, 3, 11 * PI / 3);
		assertTrue(trans.isMotion());
	}

	public void testQuadrantRotation(){
		AffineTransform2D rot0 = AffineTransform2D.createRotation(0);
		AffineTransform2D qrot0 = AffineTransform2D.createQuadrantRotation(0);
		assertTrue(rot0.equals(qrot0));
		
		AffineTransform2D rot1 = AffineTransform2D.createRotation(PI / 2);
		AffineTransform2D qrot1 = AffineTransform2D.createQuadrantRotation(1);
		assertTrue(rot1.equals(qrot1));
		
		AffineTransform2D rot2 = AffineTransform2D.createRotation(PI);
		AffineTransform2D qrot2 = AffineTransform2D.createQuadrantRotation(2);
		assertTrue(rot2.equals(qrot2));
		
		AffineTransform2D rot3 = AffineTransform2D.createRotation(3 * PI / 2);
		AffineTransform2D qrot3 = AffineTransform2D.createQuadrantRotation(3);
		assertTrue(rot3.equals(qrot3));
		
	}
	
	public void testQuadrantRotationCentered(){
		Point2D center = new Point2D(20, 30);
		
		AffineTransform2D rot0 = AffineTransform2D.createRotation(center, 0);
		AffineTransform2D qrot0 = AffineTransform2D.createQuadrantRotation(center, 0);
		assertTrue(rot0.equals(qrot0));
		
		AffineTransform2D rot1 = AffineTransform2D.createRotation(center, PI / 2);
		AffineTransform2D qrot1 = AffineTransform2D.createQuadrantRotation(center, 1);
		assertTrue(rot1.equals(qrot1));
		
		AffineTransform2D rot2 = AffineTransform2D.createRotation(center, PI);
		AffineTransform2D qrot2 = AffineTransform2D.createQuadrantRotation(center, 2);
		assertTrue(rot2.equals(qrot2));
		
		AffineTransform2D rot3 = AffineTransform2D.createRotation(center, 3 * PI / 2);
		AffineTransform2D qrot3 = AffineTransform2D.createQuadrantRotation(center, 3);
		assertTrue(rot3.equals(qrot3));
		
	}
	
	public void testScaling(){
		double s1 = 2;
		double s2 = 3;
		double x0 = 3;
		double y0 = 5;
		AffineTransform2D trans = AffineTransform2D.createScaling(s1, s2);
		
		// basic tests on transform type
		assertFalse(trans.isIsometry());
		assertTrue(trans.isDirect());
		assertFalse(trans.isMotion());
		assertFalse(trans.isSimilarity());
		assertFalse(trans.isIdentity());

		Point2D point = new Point2D(x0, y0);
		Point2D point2 = trans.transform(point);
		Point2D expect = new Point2D(x0 * s1, y0 * s2);
		assertEquals(expect.distance(point2), 0, 1e-14);
	}	
	
	public void testScalingUniform(){
		double s = 2;
		double x0 = 3;
		double y0 = 5;
		AffineTransform2D trans = AffineTransform2D.createScaling(s, s);
		
		// basic tests on transform type
		assertFalse(trans.isIsometry());
		assertTrue(trans.isDirect());
		assertFalse(trans.isMotion());
		assertTrue(trans.isSimilarity());
		assertFalse(trans.isIdentity());

		Point2D point = new Point2D(x0, y0);
		Point2D point2 = trans.transform(point);
		Point2D expect = new Point2D(x0 * s, y0 * s);
		assertEquals(expect.distance(point2), 0, 1e-14);
	}	
	
	public void testScalingReverse(){
		double s1 = 2;
		double s2 = -3;
		double x0 = 3;
		double y0 = 5;
		AffineTransform2D trans = AffineTransform2D.createScaling(s1, s2);
		
		// basic tests on transform type
		assertFalse(trans.isIsometry());
		assertFalse(trans.isDirect());
		assertFalse(trans.isMotion());
		assertFalse(trans.isSimilarity());
		assertFalse(trans.isIdentity());

		Point2D point = new Point2D(x0, y0);
		Point2D point2 = trans.transform(point);
		Point2D expect = new Point2D(x0 * s1, y0 * s2);
		assertEquals(expect.distance(point2), 0, 1e-14);
	}	
	
	public void testScalingCentered(){
		double xc = 20;
		double yc = 10;
		Point2D center = new Point2D(xc, yc);
		double x0 = 30;
		double y0 = 15;
		double s1 = 2;
		double s2 = 3;
		AffineTransform2D trans = 
		    AffineTransform2D.createScaling(center, s1, s2);
		
		// basic tests on transform type
		assertFalse(trans.isIsometry());
		assertTrue(trans.isDirect());
		assertFalse(trans.isMotion());
		assertFalse(trans.isSimilarity());
		assertFalse(trans.isIdentity());

		Point2D point = new Point2D(x0, y0);
		Point2D point2 = trans.transform(point);
		Point2D expect = new Point2D((x0 - xc) * s1 + xc, (y0 - yc) * s2 + yc);
	assertEquals(expect.distance(point2), 0, 1e-14);
	}	
	
	public void testTranslation(){
		Vector2D vector = new Vector2D(10, 20);
		Point2D point = new Point2D(3, 5);
		AffineTransform2D trans = AffineTransform2D.createTranslation(vector);
		
		// basic tests on transform type
		assertTrue(trans.isIsometry());
		assertTrue(trans.isDirect());
		assertTrue(trans.isMotion());
		assertTrue(trans.isSimilarity());
		assertFalse(trans.isIdentity());
		
		Point2D point2 = trans.transform(point);
		Point2D expect = new Point2D(13, 25);
		assertEquals(expect.distance(point2), 0, 1e-14);
	}
	
	public void testPointReflection() {
		// create transform
		Point2D center = new Point2D(10, 20);
		AffineTransform2D trans = AffineTransform2D.createPointReflection(center);
		
		// basic tests on transform type
		assertTrue(trans.isIsometry());
		assertTrue(trans.isDirect());
		assertTrue(trans.isMotion());
		assertTrue(trans.isSimilarity());
		assertFalse(trans.isIdentity());
		
		// test point transformation
		Point2D point = new Point2D(15, 21);
		Point2D point2 = trans.transform(point);
		Point2D expect = new Point2D(5, 19);		
		assertEquals(expect.distance(point2), 0, 1e-14);
	}
	
	public void testLineReflection() {
		// create transform
		AffineTransform2D trans = AffineTransform2D.createLineReflection(
				new StraightLine2D(new Point2D(10, 20), new Vector2D(3, 2)));
		
		// basic tests on transform type
		assertTrue(trans.isIsometry());
		assertFalse(trans.isDirect());
		assertFalse(trans.isMotion());
		assertTrue(trans.isSimilarity());
		assertFalse(trans.isIdentity());
		
		// test point transformation
		Point2D point = new Point2D(6, 26);
		Point2D point2 = trans.transform(point);
		Point2D expect = new Point2D(14, 14);		
		assertEquals(expect.distance(point2), 0, 1e-14);
	}
	
	public void testConstructorAwtTransform() {
		AffineTransform rot = AffineTransform.getRotateInstance(.4, 20, 30);
		AffineTransform2D trans = new AffineTransform2D(rot);
		AffineTransform rot2 = trans.asAwtTransform();
		assertTrue(rot.equals(rot2));
	}
	
	
	public void testasAwtTransform() {
		AffineTransform awtRot = AffineTransform.getRotateInstance(.4, 20, 30);
		AffineTransform2D rot = AffineTransform2D.createRotation(new Point2D(20, 30), .4);
		AffineTransform rot2 = rot.asAwtTransform();
		
		double[] mat1 = new double[6];
		double[] mat2 = new double[6];
		awtRot.getMatrix(mat1);
		rot2.getMatrix(mat2);
		
		for (int i = 0; i < 6; i++)
			assertEquals(mat1[i], mat2[i], 1e-6);
	}
	
	public void testIsIsometry(){
		AffineTransform2D trans;
		
		trans = AffineTransform2D.createTranslation(2, 3);
		assertTrue(trans.isIsometry());
		
		trans = AffineTransform2D.createRotation(2, 3, Math.PI/3);
		assertTrue(trans.isIsometry());
		
		trans = AffineTransform2D.createLineReflection(new StraightLine2D(
				new Point2D(10, 20), new Vector2D(3, 2)));
		assertTrue(trans.isIsometry());
		
		trans = AffineTransform2D.createScaling(2, .5);
		assertFalse(trans.isIsometry());
		
		trans = AffineTransform2D.createScaling(1, 1);
		assertTrue(trans.isIsometry());
	}
	
	public void testIsSimilarity(){
		// Test on some isometries
		AffineTransform2D trans;
		
		trans = AffineTransform2D.createTranslation(2, 3);
		assertTrue(trans.isSimilarity());
		
		trans = AffineTransform2D.createRotation(2, 3, PI / 3);
		assertTrue(trans.isSimilarity());
		
		trans = AffineTransform2D.createRotation(2, 3, 4 * PI / 3);
		assertTrue(trans.isSimilarity());
		
		trans = AffineTransform2D.createRotation(2, 3, 7 * PI / 3);
		assertTrue(trans.isSimilarity());
		
		trans = AffineTransform2D.createRotation(2, 3, 11 * PI / 3);
		assertTrue(trans.isSimilarity());
		
		trans = AffineTransform2D.createLineReflection(new StraightLine2D(
				new Point2D(10, 20), new Vector2D(3, 2)));
		assertTrue(trans.isSimilarity());
		
		// Glide reflection
		trans = AffineTransform2D.createLineReflection(new StraightLine2D(
				new Point2D(10, 20), new Vector2D(3, 2)));
		assertTrue(trans.isSimilarity());
		
		// Test with scaling with various coefficients
		trans = AffineTransform2D.createScaling(2, .5);
		assertFalse(trans.isSimilarity());
		
		trans = AffineTransform2D.createScaling(1, 1);
		assertTrue(trans.isSimilarity());
		
		trans = AffineTransform2D.createScaling(2, -2);
		assertTrue(trans.isSimilarity());
		
		trans = AffineTransform2D.createScaling(3, 3);
		assertTrue(trans.isSimilarity());
	}
	
	public void testIsMotion(){
		AffineTransform2D trans;
		
		trans = AffineTransform2D.createTranslation(2, 3);
		assertTrue(trans.isMotion());
		
		trans = AffineTransform2D.createRotation(2, 3, PI / 3);
		assertTrue(trans.isMotion());
		
		trans = AffineTransform2D.createRotation(2, 3, 4 * PI / 3);
		assertTrue(trans.isMotion());
		
		trans = AffineTransform2D.createRotation(2, 3, 7 * PI / 3);
		assertTrue(trans.isMotion());
		
		trans = AffineTransform2D.createRotation(2, 3, 11 * PI / 3);
		assertTrue(trans.isMotion());
				
		trans = AffineTransform2D.createLineReflection(new StraightLine2D(
				new Point2D(10, 20), new Vector2D(3, 2)));
		assertFalse(trans.isMotion());
		
		trans = AffineTransform2D.createScaling(2, .5);
		assertFalse(trans.isMotion());
		
		trans = AffineTransform2D.createScaling(1, 1);
		assertTrue(trans.isMotion());
	}
	
    public void testIsIdentity(){
        AffineTransform2D trans;
        
        trans = new AffineTransform2D();
        assertTrue(trans.isIdentity());

        trans = AffineTransform2D.createScaling(1, 1);
        assertTrue(trans.isIdentity());
        
        trans = AffineTransform2D.createScaling(2, .5);
        assertFalse(trans.isIdentity());
    }
    
    public void testIsDirect(){
        AffineTransform2D trans;
        
        trans = new AffineTransform2D();
        assertTrue(trans.isDirect());

        trans = AffineTransform2D.createScaling(2, 3);
        assertTrue(trans.isDirect());
        
        trans = AffineTransform2D.createScaling(2, -3);
        assertFalse(trans.isDirect());
    }
    
	public void testInvert(){
		double eps = Shape2D.ACCURACY;
		
		AffineTransform2D trans1  = new AffineTransform2D(1, 0, 5, 0, 1, 10);	
		AffineTransform2D trans1i = trans1.invert();
		AffineTransform2D trans2  = new AffineTransform2D(1, 0, -5, 0, 1, -10);

		assertTrue(trans2.almostEquals(trans1i, eps));
		assertTrue(trans1.almostEquals(trans1i.invert(), eps));
		
		AffineTransform2D trans = new AffineTransform2D(1, 2, 3, 4, 5, 6);
		assertTrue(trans.almostEquals(trans.invert().invert(), eps));
	}
	
	public void testInvertNonInvertible(){
		AffineTransform2D trans = new AffineTransform2D(1, 0, 5, 2, 0, 10);
		AffineTransform2D res = null;
		
		boolean flag = false;
		try {
			res = trans.invert();
			res.toString();
		} catch (NonInvertibleTransform2DException ex) {
			flag = true;
		}
		assertTrue(flag);
	}
	
	public void testTransformPoint() {
		Point2D p = new Point2D(10, 20);
		AffineTransform2D trans = AffineTransform2D.createTranslation(3, 4);
		Point2D res = trans.transform(p);
		Point2D exp = new Point2D(13, 24);
		
		assertEquals(exp, res);
	}
	
	public void testTransformPointArray() {
		Point2D pts[] = new Point2D[]{
				new Point2D(10, 20),
				new Point2D(20, 40), 
				new Point2D(30, 50)	};
		
		AffineTransform2D trans = AffineTransform2D.createTranslation(3, 4);
		Point2D res[] = trans.transform(pts, null);
		Point2D exp[] = new Point2D[]{
				new Point2D(13, 24),
				new Point2D(23, 44), 
				new Point2D(33, 54)	};
		
		assertEquals(3, res.length);
		for (int i = 0; i < pts.length; i++) {
			assertEquals(exp[i], res[i]);
		}
			
	}
	
	/**
	 * Check contruction of affine matrix on a centered scaling.
	 */
	public void testAffineMatrix() {
		double xc = 20;
		double yc = 30;
		Point2D center = new Point2D(20, 30);
		double s1 = 3;
		double s2 = 4;
		AffineTransform2D trans = 
		    AffineTransform2D.createScaling(center, s1, s2);
		
		double[][] matrix = trans.affineMatrix();
		assertEquals(s1, 			matrix[0][0], Shape2D.ACCURACY);
		assertEquals(0,				matrix[0][1], Shape2D.ACCURACY);
		assertEquals((1-s1) * xc, 	matrix[0][2], Shape2D.ACCURACY);
		assertEquals(0, 			matrix[1][0], Shape2D.ACCURACY);
		assertEquals(s2, 			matrix[1][1], Shape2D.ACCURACY);
		assertEquals((1-s2) * yc, 	matrix[1][2], Shape2D.ACCURACY);
	}
	
	public void testConcatenateAffineTransform2D(){
		double xc = 20;
		double yc = 30;
		double theta = Math.PI/3;
		
		// the reference transform
		AffineTransform2D ref = 
			AffineTransform2D.createRotation(xc, yc, theta);
		
		// Create the same transform using chain() method		
		AffineTransform2D tra = AffineTransform2D.createTranslation(xc, yc);
		AffineTransform2D rot = AffineTransform2D.createRotation(theta);
		AffineTransform2D trans = tra.concatenate(rot).concatenate(tra.invert());
		
		assertTrue(ref.almostEquals(trans, Shape2D.ACCURACY));		
	}
	
	public void testChainAffineTransform2D(){
		double xc = 20;
		double yc = 30;
		double theta = Math.PI/3;
		
		// the reference transform
		AffineTransform2D ref = 
			AffineTransform2D.createRotation(xc, yc, theta);
		
		// Create the same transform using chain() method		
		AffineTransform2D tra = AffineTransform2D.createTranslation(xc, yc);
		AffineTransform2D rot = AffineTransform2D.createRotation(theta);
		AffineTransform2D trans = tra.invert().chain(rot).chain(tra);
		
		assertTrue(ref.almostEquals(trans, Shape2D.ACCURACY));		
	}
	
	public void testToString(){
		AffineTransform2D trans  = new AffineTransform2D(1, 2, 3, 4, 5, 6);
		assertNotNull(trans.toString());
	}
	
	public void testEquals(){
		AffineTransform2D trans  = new AffineTransform2D(1, 2, 3, 4, 5, 6);
		assertTrue(trans.equals(trans));
	}
}
