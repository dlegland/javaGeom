/* file : Parabola2DTest.java
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
 * Created on 6 mai 2007
 *
 */
package math.geom2d;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

public class Parabola2DTest extends TestCase {

	/**
	 * Constructor for Point2DTest.
	 * @param arg0
	 */
	public Parabola2DTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(Parabola2DTest.class);
	}

	public void testGetPoint(){
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		
		Point2D p0;
		p0 = parabola.getPoint(0);
		assertEquals(p0, new Point2D(0, 0));

		p0 = parabola.getPoint(1);
		assertEquals(p0, new Point2D(1, 1));
		p0 = parabola.getPoint(-1);
		assertEquals(p0, new Point2D(-1, 1));
	}
	
	public void testGetDistance(){
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		Point2D p = new Point2D(0, -1);
		assertEquals(parabola.getDistance(p), 1, 1e-6);
	}
	
	public void testGetIntersectionsLine(){
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		StraightLine2D line;
		Collection<Point2D> inters;
		Iterator<Point2D> iter;
		
		// Horizontal line cutting in two points
		line = new StraightLine2D(0, 4, 1, 0);		
		inters = parabola.getIntersections(line);
		assertTrue(inters.size()==2);
		iter = inters.iterator();
		assertEquals(iter.next(), new Point2D(-2, 4));
		assertEquals(iter.next(), new Point2D(2, 4));
		
		// Horizontal line cutting in one points
		line = new StraightLine2D(-2, 0, 1, 0);		
		inters = parabola.getIntersections(line);
		assertTrue(inters.size()==2);
		iter = inters.iterator();
		assertEquals(iter.next(), new Point2D(0, 0));
		
		// Horizontal line cutting in one points
		line = new StraightLine2D(-2, -1, 1, 0);		
		inters = parabola.getIntersections(line);
		assertTrue(inters.size()==0);
		
		// Vertical line cutting in origin
		line = new StraightLine2D(0, -6, 0, 1);
		inters = parabola.getIntersections(line);
		assertTrue(inters.size()==1);
		iter = inters.iterator();
		assertEquals(iter.next(), new Point2D(0, 0));
		
		// Vertical line cutting at(1, 1)
		line = new StraightLine2D(1, -6, 0, 1);
		inters = parabola.getIntersections(line);
		assertTrue(inters.size()==1);
		iter = inters.iterator();
		assertEquals(iter.next(), new Point2D(1, 1));
		
		// diagonal line cutting in (0 0) and (1,1).
		line = new StraightLine2D(-2, -2, 3, 3);
		inters = parabola.getIntersections(line);
		assertTrue(inters.size()==2);
		iter = inters.iterator();
		assertEquals(iter.next(), new Point2D(0, 0));
		assertEquals(iter.next(), new Point2D(1, 1));
		
		// Parabola pointing to the right
		parabola = new Parabola2D(0, 0, 1, -Math.PI/2);
		line = new StraightLine2D(4, 0, 0, 1);
		inters = parabola.getIntersections(line);
		assertTrue(inters.size()==2);
		iter = inters.iterator();
		assertEquals(iter.next(), new Point2D(4, 2));
		assertEquals(iter.next(), new Point2D(4, -2));
		
		parabola = new Parabola2D(0, 0, 1./88, 0);
		line = new StraightLine2D(0, 50, -1, 0);
		inters = parabola.getIntersections(line);
		assertTrue(inters.size()==2);

		parabola = new Parabola2D(0, 0, 1./88, Math.PI*1.5);
		line = new StraightLine2D(50, 0, 0, 1);
		inters = parabola.getIntersections(line);
		assertTrue(inters.size()==2);
	}
	
	public void testGetClippedShape(){
		// parabola pointing upwards
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		Box2D box = new Box2D(-10, 10, -4, 4);
		
		CurveSet2D<ContinuousOrientedCurve2D> clippedCurve =
			box.clipContinuousOrientedCurve(parabola);
		Curve2D curve = clippedCurve.getFirstCurve();
		
		assertTrue(clippedCurve.getCurveNumber()==1);
		assertTrue(curve instanceof ParabolaArc2D);

		// parabola pointing to the right
		parabola = new Parabola2D(0, 0, 1, -Math.PI/2);
		box = new Box2D(-4, 4, -10, 10);
		clippedCurve = box.clipContinuousOrientedCurve(parabola);
		assertTrue(clippedCurve.getCurveNumber()==1);
		curve = clippedCurve.getFirstCurve();
		assertTrue(curve instanceof ParabolaArc2D);
		assertTrue(new ParabolaArc2D(parabola, -2, 2).equals(curve));
	}
	
}
