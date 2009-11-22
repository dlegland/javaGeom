/*
 * File : InvertedRay2DTest.java
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

package math.geom2d.line;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.UnboundedShape2DException;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import junit.framework.TestCase;


/**
 * @author Legland
 */
public class InvertedRay2DTest extends TestCase {

	/**
	 * Constructor for InvertedRay2DTest.
	 * @param arg0
	 */
	public InvertedRay2DTest(String arg0) {
		super(arg0);
	}

    public void testGetParallelDouble() {
    	Point2D p1 = new Point2D(1, 1);
    	Point2D p2 = new Point2D(1, 3);
    	InvertedRay2D line1 = new InvertedRay2D(p1, p2);
    	
    	Point2D p1p = new Point2D(2, 1);
    	Point2D p2p = new Point2D(2, 3);
    	InvertedRay2D line1p = new InvertedRay2D(p1p, p2p);
    	
    	assertTrue(line1.getParallel(1).equals(line1p));
    }

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(InvertedRay2DTest.class);
	}

	public void testIsBounded(){
		InvertedRay2D ray1 = new InvertedRay2D(2, 2, 1, 0);
		assertTrue(!ray1.isBounded());
	}
	
	public void testGetFirstPoint(){
		InvertedRay2D ray1 = new InvertedRay2D(2, 2, 1, 0);
		try {
		    ray1.getFirstPoint();
		    fail("Should throw an InfiniteShapeException");
		}catch(UnboundedShape2DException ex){		    
		}
	}
	
	public void testGetLastPoint(){
		InvertedRay2D ray1 = new InvertedRay2D(2, 2, 1, 0);
		Point2D lastPoint = ray1.getLastPoint();
		assertTrue(lastPoint.equals(new Point2D(2, 2)));
	}
	
	public void testGetBoundingBox(){
		double plus = Double.POSITIVE_INFINITY;
		double minus = Double.NEGATIVE_INFINITY;
		
		InvertedRay2D ray1 = new InvertedRay2D(2, 2, 1, 1);
		assertTrue(ray1.getBoundingBox().equals(new Box2D(minus, 2, minus, 2)));
		
		InvertedRay2D ray2 = new InvertedRay2D(2, 2, 1, 0);
		assertTrue(ray2.getBoundingBox().equals(new Box2D(minus, 2, 2, 2)));
		
		InvertedRay2D ray3 = new InvertedRay2D(2, 2, -1, -1);
		assertTrue(ray3.getBoundingBox().equals(new Box2D(2, plus, 2, plus)));
	}
	
	public void testGetReverseCurve(){
		InvertedRay2D ray1 = new InvertedRay2D(2, 2, 1, 0);
		Ray2D inv1 = new Ray2D(2, 2, -1, 0);

		Curve2D inverted = ray1.getReverseCurve();
		assertTrue(inverted.equals(inv1));
		
	}
	
	public void testContainsDoubleDouble(){
		InvertedRay2D ray1 = new InvertedRay2D(2, 2, 1, 0);
		assertTrue(ray1.contains(2, 2));
		assertTrue(ray1.contains(-3, 2));
		
		InvertedRay2D ray2 = new InvertedRay2D(2, 2, 1, 2);
		assertTrue(ray2.contains(2, 2));
		assertTrue(ray2.contains(-2, -6));
	}
	
	
	public void testClipBox2D(){
		InvertedRay2D ray1 = new InvertedRay2D(2, 2, 1, 0);
		Box2D box = new Box2D(10, -10, 10, -10);
		CurveSet2D<?> clipped = ray1.clip(box);
		assertTrue(clipped.iterator().next().equals(
				new LineSegment2D(-10, 2, 2, 2)));
	}
	
	public void testGetDistancePoint2D() {
		InvertedRay2D ray = new InvertedRay2D(1, 2, 3, 4);
		Point2D pt;
		
		// test origin point
		pt = new Point2D(1, 2);
		assertEquals(0, ray.getDistance(pt), 1e-14);
		
		// point on the line (positive extent)
		pt = new Point2D(1+2*3, 2+2*4);
		assertEquals(10, ray.getDistance(pt), 1e-14);
		
		// point on the line (negative extent)
		pt = new Point2D(1-2*3, 2-2*4);
		assertEquals(0, ray.getDistance(pt), 1e-14);
		
		// point outside the line
		pt = new Point2D(5, -1);
		assertEquals(5, ray.getDistance(pt), 1e-14);	
		
		// point outside the line, in the other side
		pt = new Point2D(-3, 5);
		assertEquals(5, ray.getDistance(pt), 1e-14);	
	}

	public void testClone(){
        InvertedRay2D ray = new InvertedRay2D(10, 20, 30, 40);
        assertTrue(ray.equals(ray.clone()));
    }
}
