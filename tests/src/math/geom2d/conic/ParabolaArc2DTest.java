/* file : ParabolaArc2DTest.java
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
package math.geom2d.conic;

import java.util.Collection;
import java.util.Iterator;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.line.Polyline2D;
import math.geom2d.line.StraightLine2D;
import junit.framework.TestCase;

public class ParabolaArc2DTest extends TestCase {

	/**
	 * Constructor for Point2DTest.
	 * @param arg0
	 */
	public ParabolaArc2DTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(ParabolaArc2DTest.class);
	}

	public void testGetPoint(){
		
		// Vertical parabola
		ParabolaArc2D arc = new ParabolaArc2D(
				new Parabola2D(0, 0, 1, 0), -5, 5);		
		Point2D p0;
		
		// origin
		p0 = arc.getPoint(0);
		assertEquals(p0, new Point2D(0, 0));

		// after origin
		p0 = arc.getPoint(1);
		assertEquals(p0, new Point2D(1, 1));
		p0 = arc.getPoint(2);
		assertEquals(p0, new Point2D(2, 4));
		
		// before origin
		p0 = arc.getPoint(-1);
		assertEquals(p0, new Point2D(-1, 1));
		p0 = arc.getPoint(-2);
		assertEquals(p0, new Point2D(-2, 4));


		// Horizontal parabola (pointing to the right)
		arc = new ParabolaArc2D(new Parabola2D(0, 0, 1, -Math.PI/2), -5, 5);	

		// origin
		p0 = arc.getPoint(0);
		assertEquals(p0, new Point2D(0, 0));

		// after origin
		p0 = arc.getPoint(1);
		assertEquals(p0, new Point2D(1, -1));
		p0 = arc.getPoint(2);
		assertEquals(p0, new Point2D(4, -2));

		// before origin
		p0 = arc.getPoint(-1);
		assertEquals(p0, new Point2D(1, 1));
		p0 = arc.getPoint(-2);
		assertEquals(p0, new Point2D(4, 2));


		// Shifted horizontal parabola
		arc = new ParabolaArc2D(new Parabola2D(20, 10, 1, -Math.PI/2), -5, 5);	

		// origin
		p0 = arc.getPoint(0);
		assertEquals(p0, new Point2D(20, 10));

		// after origin
		p0 = arc.getPoint(1);
		assertEquals(p0, new Point2D(20+1, 10-1));
		p0 = arc.getPoint(2);
		assertEquals(p0, new Point2D(20+4, 10-2));

		// before origin
		p0 = arc.getPoint(-1);
		assertEquals(p0, new Point2D(20+1, 10+1));
		p0 = arc.getPoint(-2);
		assertEquals(p0, new Point2D(20+4, 10+2));
	}

	public void testGetAsPolyline2D(){
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		ParabolaArc2D parabolaArc = new ParabolaArc2D(parabola, -10, 10);
		
		Polyline2D polyline = parabolaArc.getAsPolyline(4);
		assertTrue(polyline.getPointArray().length==5);
	}
	
	public void testContainsPoint2D(){
		// parabola pointing upwards
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		ParabolaArc2D arc = new ParabolaArc2D(parabola, Double.NEGATIVE_INFINITY, 10);
		
		Point2D point1 = new Point2D(-2, 4);
		assertTrue(arc.contains(point1));
		
		Point2D point2 = new Point2D(2, 4);
		assertTrue(arc.contains(point2));		
	}

	public void testGetPositionPoint2D(){
		// parabola pointing upwards
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		
		Point2D point1 = new Point2D(-2, 4);
		assertEquals(parabola.getPosition(point1), -2, Shape2D.ACCURACY);
		
		Point2D point2 = new Point2D(2, 4);
		assertEquals(parabola.getPosition(point2), 2, Shape2D.ACCURACY);
		
	}

	public void testGetIntersectionsLine(){
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		ParabolaArc2D arc = new ParabolaArc2D(parabola,
				Double.NEGATIVE_INFINITY, 10);
		StraightLine2D line;
		Collection<Point2D> inters;
		Iterator<Point2D> iter;
		Point2D inter;
		
		// Horizontal line cutting in two points
		line = new StraightLine2D(10, 4, -20, 0);		
		inters = arc.getIntersections(line);
		assertTrue(inters.size()==2);
		iter = inters.iterator();
		inter = iter.next();
		assertEquals(inter, new Point2D(-2, 4));
		assertTrue(arc.contains(inter));
		assertEquals(arc.getPosition(inter), -2, Shape2D.ACCURACY);
		inter = iter.next();
		assertEquals(inter, new Point2D(2, 4));
		assertTrue(arc.contains(inter));
		assertEquals(arc.getPosition(inter), 2, Shape2D.ACCURACY);
	}
	
	public void testClipLine2D(){
		// parabola pointing upwards
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		ParabolaArc2D arc = new ParabolaArc2D(parabola, Double.NEGATIVE_INFINITY, 10);
		ParabolaArc2D clippedArc = new ParabolaArc2D(parabola, -2, 2);
		StraightLine2D line = new StraightLine2D(10, 4, -20, 0);
	
		CurveSet2D<?> clippedCurve = line.clipSmoothCurve(arc);
		Curve2D curve = clippedCurve.getFirstCurve();
		
		assertTrue(clippedCurve.getCurveNumber()==1);
		assertTrue(curve instanceof ParabolaArc2D);
		assertTrue(clippedArc.equals(curve));		
	}
	
}
