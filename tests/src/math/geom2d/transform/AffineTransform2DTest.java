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
 
package math.geom2d.transform;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.StraightLine2D;
import junit.framework.TestCase;

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

	public void testCreateRotationPointDouble(){
		Point2D center = new Point2D(10, 20);
		Point2D point = new Point2D(10+5, 20);
		double theta = Math.PI/4;
		AffineTransform2D trans = AffineTransform2D.createRotation(center, theta);
		Point2D point2 = trans.transform(point, new Point2D());
		Point2D expect = new Point2D(10+Math.sqrt(2)*5/2, 20+Math.sqrt(2)*5/2);
		assertTrue(expect.distance(point2)<1e-14);
	}
	
	public void testCreateScalingDoubleDouble(){
		double s1 = 2;
		double s2 = 3;
		double x0 = 3;
		double y0 = 5;
		Point2D point = new Point2D(x0, y0);
		AffineTransform2D trans = AffineTransform2D.createScaling(s1, s2);
		Point2D point2 = trans.transform(point, new Point2D());
		Point2D expect = new Point2D(x0*s1, y0*s2);
		assertTrue(expect.distance(point2)<1e-14);
	}	
	
	public void testCreateScalingPointDoubleDouble(){
		double xc = 20;
		double yc = 10;
		double x0 = 30;
		double y0 = 15;
		double s1 = 2;
		double s2 = 3;
		Point2D center = new Point2D(xc, yc);
		Point2D point = new Point2D(x0, y0);
		AffineTransform2D trans = AffineTransform2D.createScaling(center, s1, s2);
		Point2D point2 = trans.transform(point, new Point2D());
		Point2D expect = new Point2D((x0-xc)*s1+xc, (y0-yc)*s2+yc);
		assertTrue(expect.distance(point2)<1e-14);
	}	
	
	public void testCreateTranslationVector(){
		Vector2D vector = new Vector2D(10, 20);
		Point2D point = new Point2D(3, 5);
		AffineTransform2D trans = AffineTransform2D.createTranslation(vector);
		Point2D point2 = trans.transform(point, new Point2D());
		Point2D expect = new Point2D(13, 25);
		assertTrue(expect.distance(point2)<1e-14);
	}
	
	public void testIsIsometryAffineTransform2D(){
		
		assertTrue(AffineTransform2D.isIsometry(
				AffineTransform2D.createTranslation(2, 3)));
		
		assertTrue(AffineTransform2D.isIsometry(
				AffineTransform2D.createRotation(2, 3, Math.PI/3)));
		
		assertTrue(AffineTransform2D.isIsometry(
				AffineTransform2D.createLineReflection(new StraightLine2D(
						new Point2D(10, 20), new Vector2D(3, 2)))));
		
		assertTrue(!AffineTransform2D.isIsometry(
				AffineTransform2D.createScaling(2, .5)));
		
		assertTrue(AffineTransform2D.isIsometry(
				AffineTransform2D.createScaling(1, 1)));
		
	}
	
	public void testIsSimilarityAffineTransform2D(){
		// Test on some isometries
		assertTrue(AffineTransform2D.isSimilarity(
				AffineTransform2D.createTranslation(2, 3)));
		
		assertTrue(AffineTransform2D.isSimilarity(
				AffineTransform2D.createRotation(2, 3, Math.PI/3)));
		
		assertTrue(AffineTransform2D.isSimilarity(
				AffineTransform2D.createLineReflection(new StraightLine2D(
						new Point2D(10, 20), new Vector2D(3, 2)))));
		
		// Test with scaling with various coefficients
		assertTrue(!AffineTransform2D.isSimilarity(
				AffineTransform2D.createScaling(2, .5)));
		
		assertTrue(AffineTransform2D.isSimilarity(
				AffineTransform2D.createScaling(1, 1)));
		
		assertTrue(AffineTransform2D.isSimilarity(
				AffineTransform2D.createScaling(3, 3)));
		
	}
	
	public void testGetInverseTransform(){
		AffineTransform2D trans1  = new AffineTransform2D(1, 0, 5, 0, 1, 10);	
		AffineTransform2D trans1i = (AffineTransform2D)trans1.getInverseTransform();
		AffineTransform2D trans2  = new AffineTransform2D(1, 0, -5, 0, 1, -10);

		assertTrue(trans2.equals(trans1i));
		assertTrue(trans1.equals(trans1i.getInverseTransform()));
		
		AffineTransform2D trans = new AffineTransform2D(1, 2, 3, 4, 5, 6);
		assertTrue(trans.equals(trans.getInverseTransform().getInverseTransform()));
	}
	
	
	public void testEquals(){
		AffineTransform2D trans  = new AffineTransform2D(1, 2, 3, 4, 5, 6);
		assertTrue(trans.equals(trans));
	}
	
}
