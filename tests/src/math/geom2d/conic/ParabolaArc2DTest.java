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

import junit.framework.TestCase;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.Curves2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.polygon.LinearCurve2D;

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
		p0 = arc.point(0);
		assertEquals(p0, new Point2D(0, 0));

		// after origin
		p0 = arc.point(1);
		assertEquals(p0, new Point2D(1, 1));
		p0 = arc.point(2);
		assertEquals(p0, new Point2D(2, 4));
		
		// before origin
		p0 = arc.point(-1);
		assertEquals(p0, new Point2D(-1, 1));
		p0 = arc.point(-2);
		assertEquals(p0, new Point2D(-2, 4));


		// Horizontal parabola (pointing to the right)
		arc = new ParabolaArc2D(new Parabola2D(0, 0, 1, -Math.PI/2), -5, 5);	

		// origin
		p0 = arc.point(0);
		assertEquals(p0, new Point2D(0, 0));

		// after origin
		p0 = arc.point(1);
		assertEquals(p0, new Point2D(1, -1));
		p0 = arc.point(2);
		assertEquals(p0, new Point2D(4, -2));

		// before origin
		p0 = arc.point(-1);
		assertEquals(p0, new Point2D(1, 1));
		p0 = arc.point(-2);
		assertEquals(p0, new Point2D(4, 2));


		// Shifted horizontal parabola
		arc = new ParabolaArc2D(new Parabola2D(20, 10, 1, -Math.PI/2), -5, 5);	

		// origin
		p0 = arc.point(0);
		assertEquals(p0, new Point2D(20, 10));

		// after origin
		p0 = arc.point(1);
		assertEquals(p0, new Point2D(20+1, 10-1));
		p0 = arc.point(2);
		assertEquals(p0, new Point2D(20+4, 10-2));

		// before origin
		p0 = arc.point(-1);
		assertEquals(p0, new Point2D(20+1, 10+1));
		p0 = arc.point(-2);
		assertEquals(p0, new Point2D(20+4, 10+2));
	}

	public void testGetAsPolyline2D(){
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		ParabolaArc2D parabolaArc = new ParabolaArc2D(parabola, -10, 10);
		
		LinearCurve2D polyline = parabolaArc.asPolyline(4);
		assertTrue(polyline.vertexArray().length==5);
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
		assertEquals(parabola.position(point1), -2, Shape2D.ACCURACY);
		
		Point2D point2 = new Point2D(2, 4);
		assertEquals(parabola.position(point2), 2, Shape2D.ACCURACY);
		
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
		inters = arc.intersections(line);
		assertTrue(inters.size()==2);
		iter = inters.iterator();
		inter = iter.next();
		assertEquals(inter, new Point2D(-2, 4));
		assertTrue(arc.contains(inter));
		assertEquals(arc.position(inter), -2, Shape2D.ACCURACY);
		inter = iter.next();
		assertEquals(inter, new Point2D(2, 4));
		assertTrue(arc.contains(inter));
		assertEquals(arc.position(inter), 2, Shape2D.ACCURACY);
	}
	
	public void testClipLine2D(){
		// parabola pointing upwards
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		ParabolaArc2D arc = new ParabolaArc2D(parabola, 
				Double.NEGATIVE_INFINITY, 10);
		ParabolaArc2D clippedArc = new ParabolaArc2D(parabola, -2, 2);
		StraightLine2D line = new StraightLine2D(10, 4, -20, 0);
	
		CurveSet2D<?> clippedCurve = Curves2D.clipSmoothCurve(arc, line);
		Curve2D curve = clippedCurve.firstCurve();
		
		assertTrue(clippedCurve.size()==1);
		assertTrue(curve instanceof ParabolaArc2D);
		assertTrue(clippedArc.equals(curve));		
	}
	
	public void testIsInside_Direct(){
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		ParabolaArc2D arc = new ParabolaArc2D(parabola, -1, 2);
		Point2D pt;
		
		// inside parent parabola
		pt = new Point2D(0, 2);
		assertTrue(arc.isInside(pt));

		// inside first tangent
		pt = new Point2D(-2, 3.5);
		assertTrue(arc.isInside(pt));

		// inside second tangent
		pt = new Point2D(3, 8.5);
		assertTrue(arc.isInside(pt));

		// outside parent parabola, but inside both tangents
		pt = new Point2D(.5, 0);
		assertTrue(!arc.isInside(pt));

		// outside first tangent
		pt = new Point2D(-1, 0);
		assertTrue(!arc.isInside(pt));

		// outside second tangent
		pt = new Point2D(2, 0);
		assertTrue(!arc.isInside(pt));

		// outside both tangent
		pt = new Point2D(1, -4);
		assertTrue(!arc.isInside(pt));
	}

	public void testIsInside_Inverse(){
		Parabola2D parabola = new Parabola2D(0, 0, -1, 0);
		ParabolaArc2D arc = new ParabolaArc2D(parabola, -1, 2);
		Point2D pt;
		
		// inside parent parabola
		pt = new Point2D(0, -2);
		assertTrue(!arc.isInside(pt));

		// inside first tangent
		pt = new Point2D(-2, 2);
		assertTrue(arc.isInside(pt));

		// inside second tangent
		pt = new Point2D(4, -4);
		assertTrue(arc.isInside(pt));

		// outside parent parabola, but inside both tangents
		pt = new Point2D(.5, 0);
		assertTrue(arc.isInside(pt));

		// outside first tangent
		pt = new Point2D(-2, -3.5);
		assertTrue(!arc.isInside(pt));

		// outside second tangent
		pt = new Point2D(3, -8.5);
		assertTrue(!arc.isInside(pt));

		// outside both tangent
		pt = new Point2D(1, 4);
		assertTrue(arc.isInside(pt));
	}
	
	public void testGetWindingAnglePoint2D_Direct(){
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		ParabolaArc2D arc = new ParabolaArc2D(parabola, -1, 2);
		Point2D pt;
		double eps = 1e-12;
		
		pt = new Point2D(0, 2);
		assertEquals(arc.windingAngle(pt), Math.PI, eps);
		
		pt = new Point2D(-1, 4);
		assertEquals(arc.windingAngle(pt), Math.PI/2, eps);
		
		pt = new Point2D(2, 1);
		assertEquals(arc.windingAngle(pt), -Math.PI/2, eps);
		
		
		parabola = new Parabola2D(0, 0, 1, Math.PI);
		arc = new ParabolaArc2D(parabola, -1, 2);
		
		pt = new Point2D(0, -2);
		assertEquals(arc.windingAngle(pt), Math.PI, eps);
		
		pt = new Point2D(1, -4);
		assertEquals(arc.windingAngle(pt), Math.PI/2, eps);
		
		pt = new Point2D(-2, -1);
		assertEquals(arc.windingAngle(pt), -Math.PI/2, eps);		
	}
	
	public void testGetWindingAnglePoint2D_Inverse(){
		Parabola2D parabola = new Parabola2D(0, 0, -1, 0);
		ParabolaArc2D arc = new ParabolaArc2D(parabola, -2, 1);
		Point2D pt;
		double eps = 1e-12;
		
		pt = new Point2D(0, -2);
		assertEquals(arc.windingAngle(pt), -Math.PI, eps);
		
		pt = new Point2D(1, -4);
		assertEquals(arc.windingAngle(pt), -Math.PI/2, eps);
		
		pt = new Point2D(-2, -1);
		assertEquals(arc.windingAngle(pt), Math.PI/2, eps);
		
		
		parabola = new Parabola2D(0, 0, -1, Math.PI);
		arc = new ParabolaArc2D(parabola, -2, 1);
		
		pt = new Point2D(0, 2);
		assertEquals(arc.windingAngle(pt), -Math.PI, eps);
		
		pt = new Point2D(-1, 4);
		assertEquals(arc.windingAngle(pt), -Math.PI/2, eps);
		
		pt = new Point2D(2, 1);
		assertEquals(arc.windingAngle(pt), Math.PI/2, eps);		
	}
	
	public void testClone() {
	    Parabola2D parabola = new Parabola2D(10, 20, 2, Math.PI/3);
	    ParabolaArc2D arc = new ParabolaArc2D(parabola, -10, 20);
	    assertTrue(arc.equals(arc.clone()));

	    parabola = new Parabola2D(10, 20, -2, Math.PI/3);
	    arc = new ParabolaArc2D(parabola, -10, 20);
        assertTrue(arc.equals(arc.clone()));
	}
}
