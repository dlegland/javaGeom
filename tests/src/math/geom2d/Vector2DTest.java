/*
 * File : Vector2DTest.java
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
 * Created on 31 déc. 2003
 */

package math.geom2d;

import junit.framework.TestCase;

/**
 * @author Legland
 */
public class Vector2DTest extends TestCase {

	/**
	 * Constructor for Vector2DTest.
	 * 
	 * @param arg0
	 */
	public Vector2DTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(Vector2DTest.class);
	}

	public void testDotVector2DVector2D() {
	    double eps = 1e-14;
	    
        Vector2D v1 = new Vector2D(1, 2);
        Vector2D v2 = new Vector2D(2, 4);
        Vector2D v3 = new Vector2D(-2, -4);
        Vector2D v4 = new Vector2D(-2, 1);

        assertEquals(10, Vector2D.dot(v1, v2), eps);
        assertEquals(-10, Vector2D.dot(v1, v3), eps);
        assertEquals(0, Vector2D.dot(v1, v4), eps);
	}
	
    public void testDotVector2D() {
        double eps = 1e-14;
        
        Vector2D v1 = new Vector2D(1, 2);
        Vector2D v2 = new Vector2D(2, 4);
        Vector2D v3 = new Vector2D(-2, -4);
        Vector2D v4 = new Vector2D(-2, 1);

        assertEquals(10, v1.dot(v2), eps);
        assertEquals(-10, v1.dot(v3), eps);
        assertEquals(0, v1.dot(v4), eps);
    }
	
    public void testCrossVector2DVector2D() {
        double eps = 1e-14;
        
        Vector2D v1 = new Vector2D(1, 2);
        Vector2D v2 = new Vector2D(2, 4);
        Vector2D v3 = new Vector2D(-2, -4);
        Vector2D v4 = new Vector2D(-2, 1);
        Vector2D v5 = new Vector2D(2, -1);

        assertEquals(0, Vector2D.cross(v1, v2), eps);
        assertEquals(0, Vector2D.cross(v1, v3), eps);
        assertEquals(5, Vector2D.cross(v1, v4), eps);
        assertEquals(-5, Vector2D.cross(v1, v5), eps);
    }
    
    public void testCrossVector2D() {
        double eps = 1e-14;
        
        Vector2D v1 = new Vector2D(1, 2);
        Vector2D v2 = new Vector2D(2, 4);
        Vector2D v3 = new Vector2D(-2, -4);
        Vector2D v4 = new Vector2D(-2, 1);
        Vector2D v5 = new Vector2D(2, -1);

        assertEquals(0, v1.cross(v2), eps);
        assertEquals(0, v1.cross(v3), eps);
        assertEquals(5, v1.cross(v4), eps);
        assertEquals(-5, v1.cross(v5), eps);
    }
    
	/**
	 * Test colinearity for instance of vector
	 */
	public void testIsColinearVector2D() {

		Vector2D v1 = new Vector2D(1, 2);
		Vector2D v2 = new Vector2D(2, 4);
		Vector2D v3 = new Vector2D(-4, -8);
		assertTrue(v1.isColinear(v2));
		assertTrue(v1.isColinear(v3));
		assertTrue(v2.isColinear(v1));
		assertTrue(v2.isColinear(v3));
		assertTrue(v3.isColinear(v1));
		assertTrue(v3.isColinear(v2));

		v1 = new Vector2D(1, 2);
		v2 = new Vector2D(2, 4.000000001);
		v3 = new Vector2D(-4.000000001, -8);
		assertTrue(!v1.isColinear(v2));
		assertTrue(!v1.isColinear(v3));
		assertTrue(!v2.isColinear(v1));
		assertTrue(!v2.isColinear(v3));
		assertTrue(!v3.isColinear(v1));
		assertTrue(!v3.isColinear(v2));
	}

	/**
	 * Test colinearity detection in static method
	 */
	public void testIsColinearVector2DVector2D() {

		Vector2D v1 = new Vector2D(1, 2);
		Vector2D v2 = new Vector2D(2, 4);
		Vector2D v3 = new Vector2D(-4, -8);
		assertTrue(Vector2D.isColinear(v1, v2));
		assertTrue(Vector2D.isColinear(v1, v3));
		assertTrue(Vector2D.isColinear(v2, v1));
		assertTrue(Vector2D.isColinear(v2, v3));
		assertTrue(Vector2D.isColinear(v3, v1));
		assertTrue(Vector2D.isColinear(v3, v2));

		v1 = new Vector2D(1, 2);
		v2 = new Vector2D(2, 4.000000001);
		v3 = new Vector2D(-4.000000001, -8);
		assertTrue(!Vector2D.isColinear(v1, v2));
		assertTrue(!Vector2D.isColinear(v1, v3));
		assertTrue(!Vector2D.isColinear(v2, v1));
		assertTrue(!Vector2D.isColinear(v2, v3));
		assertTrue(!Vector2D.isColinear(v3, v1));
		assertTrue(!Vector2D.isColinear(v3, v2));
	}

	public void testIsOrthogonalVector2D() {
		Vector2D v1 = new Vector2D(1, 2);
		Vector2D v2 = new Vector2D(2, 4);
		Vector2D v3 = new Vector2D(-6, 3);
		Vector2D v4 = new Vector2D(3, 4);
		
		// parallel vectors
		assertTrue(!v1.isOrthogonal(v1));
		assertTrue(!v1.isOrthogonal(v2));
		assertTrue(!v2.isOrthogonal(v1));
		
		// orthogonal vectors
		assertTrue(v1.isOrthogonal(v3));
		assertTrue(v2.isOrthogonal(v3));
		
		// other vectors
		assertTrue(!v1.isOrthogonal(v4));
		assertTrue(!v3.isOrthogonal(v4));
	}
	
	public void testGetOpposite() {
		Vector2D v1 = new Vector2D(2, 3);
		assertEquals(v1.getOpposite(), new Vector2D(-2, -3));
	}

	public void testNormalize() {
		Vector2D v1 = new Vector2D(2, 3);
		v1.normalize();
		assertEquals(v1, new Vector2D(2.0 / Math.sqrt(13), 3.0 / Math.sqrt(13)));
	}

	public void testGetNormalizedVector() {
		Vector2D v1 = new Vector2D(2, 3);
		Vector2D v1n = new Vector2D(2.0 / Math.sqrt(13), 3.0 / Math.sqrt(13));
		assertEquals(v1.getNormalizedVector(), v1n);
	}

	/**
	 * Test Vector2D.getAngle() with all multiple of pi/4.
	 */
	public void testGetAngle() {
	    double eps = 1e-14;
	    
	    Vector2D v1 = new Vector2D(10, 0);
	    assertEquals(v1.getAngle(), 0, eps);
	    
        Vector2D v2 = new Vector2D(10, 10);
        assertEquals(v2.getAngle(), Math.PI/4, eps);
        
        Vector2D v3 = new Vector2D(0, 10);
        assertEquals(v3.getAngle(), Math.PI/2, eps);
        
        Vector2D v4 = new Vector2D(-10, 10);
        assertEquals(v4.getAngle(), 3*Math.PI/4, eps);
        
        Vector2D v5 = new Vector2D(-10, 0);
        assertEquals(v5.getAngle(), Math.PI, eps);
        
        Vector2D v6 = new Vector2D(-10, -10);
        assertEquals(v6.getAngle(), 5*Math.PI/4, eps);
        
        Vector2D v7 = new Vector2D(0, -10);
        assertEquals(v7.getAngle(), 3*Math.PI/2, eps);
        
        Vector2D v8 = new Vector2D(10, -10);
        assertEquals(v8.getAngle(), 7*Math.PI/4, eps);
	}

	public void testPlusVector2D() {
        Vector2D v1 = new Vector2D(10, 20);
        Vector2D v2 = new Vector2D(30, 40);
        Vector2D v3 = new Vector2D(40, 60);
        
        assertEquals(v3, v1.plus(v2));
	}

    public void testMinusVector2D() {
        Vector2D v1 = new Vector2D(10, 20);
        Vector2D v2 = new Vector2D(30, 40);
        Vector2D v3 = new Vector2D(-20, -20);
        
        assertEquals(v3, v1.minus(v2));
    }
    
    public void testTimesDouble() {
        Vector2D v1 = new Vector2D(10, 20);
        
        Vector2D v2 = new Vector2D(30, 60);
        assertEquals(v2, v1.times(3));
        
        Vector2D v3 = new Vector2D(5, 10);
        assertEquals(v3, v1.times(1./2.));
    }

    /*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		Vector2D v1 = new Vector2D(2, 3);
		Vector2D v2 = new Vector2D(2, 3);
		Vector2D v3 = new Vector2D(1, 3);
		Vector2D v4 = new Vector2D(2, 4);
		Vector2D v5 = new Vector2D(3, 2);

		assertTrue(v1.equals(v1));
		assertTrue(v1.equals(v2));
		assertTrue(!v1.equals(v3));
		assertTrue(!v1.equals(v4));
		assertTrue(!v1.equals(v5));
	}

    public void testClone() {
        Vector2D vector = new Vector2D(10, 20);
        assertTrue(vector.equals(vector.clone()));
    }
}
