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
package math.geom2d.conic;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;
import math.geom2d.AffineTransform2D;
import math.geom2d.Angle2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.Curve2DUtils;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.line.StraightLine2D;

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
		// Vertical parabola
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);		
		Point2D p0;
		
		// origin
		p0 = parabola.point(0);
		assertEquals(p0, new Point2D(0, 0));

		// after origin
		p0 = parabola.point(1);
		assertEquals(p0, new Point2D(1, 1));
		p0 = parabola.point(2);
		assertEquals(p0, new Point2D(2, 4));
		
		// before origin
		p0 = parabola.point(-1);
		assertEquals(p0, new Point2D(-1, 1));
		p0 = parabola.point(-2);
		assertEquals(p0, new Point2D(-2, 4));
		
		
		// Horizontal parabola (pointing to the right)
		 parabola = new Parabola2D(0, 0, 1, -Math.PI/2);	

		 // origin
		 p0 = parabola.point(0);
		 assertEquals(p0, new Point2D(0, 0));

		 // after origin
		 p0 = parabola.point(1);
		 assertEquals(p0, new Point2D(1, -1));
		 p0 = parabola.point(2);
		 assertEquals(p0, new Point2D(4, -2));

		 // before origin
		 p0 = parabola.point(-1);
		 assertEquals(p0, new Point2D(1, 1));
		 p0 = parabola.point(-2);
		 assertEquals(p0, new Point2D(4, 2));
		 
		 
		// Shifted horizontal parabola
		 parabola = new Parabola2D(20, 10, 1, -Math.PI/2);	

		 // origin
		 p0 = parabola.point(0);
		 assertEquals(p0, new Point2D(20, 10));

		 // after origin
		 p0 = parabola.point(1);
		 assertEquals(p0, new Point2D(20+1, 10-1));
		 p0 = parabola.point(2);
		 assertEquals(p0, new Point2D(20+4, 10-2));

		 // before origin
		 p0 = parabola.point(-1);
		 assertEquals(p0, new Point2D(20+1, 10+1));
		 p0 = parabola.point(-2);
		 assertEquals(p0, new Point2D(20+4, 10+2));
	}
	
	public void testContainsPoint2D(){
		// parabola pointing upwards
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		
		Point2D point1 = new Point2D(-2, 4);
		assertTrue(parabola.contains(point1));
		
		Point2D point2 = new Point2D(2, 4);
		assertTrue(parabola.contains(point2));
		
	}

	public void testGetPositionPoint2D(){
		// parabola pointing upwards
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		
		Point2D point1 = new Point2D(-2, 4);
		assertEquals(parabola.position(point1), -2, Shape2D.ACCURACY);
		
		Point2D point2 = new Point2D(2, 4);
		assertEquals(parabola.position(point2), 2, Shape2D.ACCURACY);
		
	}

	
	public void testGetDistance(){
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		Point2D p = new Point2D(0, -1);
		assertEquals(parabola.distance(p), 1, 1e-6);
	}
	
	public void testGetIntersectionsLine(){
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		StraightLine2D line;
		Collection<Point2D> inters;
		Iterator<Point2D> iter;
		Point2D inter;
		
		// Horizontal line cutting in two points
		line = new StraightLine2D(10, 4, -20, 0);		
		inters = parabola.intersections(line);
		assertTrue(inters.size()==2);
		iter = inters.iterator();
		inter = iter.next();
		assertEquals(inter, new Point2D(-2, 4));
		assertTrue(parabola.contains(inter));
		inter = iter.next();
		assertEquals(inter, new Point2D(2, 4));
		
		// Horizontal line cutting in one points
		line = new StraightLine2D(-2, 0, 1, 0);		
		inters = parabola.intersections(line);
		assertTrue(inters.size()==2);
		iter = inters.iterator();
		assertEquals(iter.next(), new Point2D(0, 0));
		
		// Horizontal line cutting in one points
		line = new StraightLine2D(-2, -1, 1, 0);		
		inters = parabola.intersections(line);
		assertTrue(inters.size()==0);
		
		// Vertical line cutting in origin
		line = new StraightLine2D(0, -6, 0, 1);
		inters = parabola.intersections(line);
		assertTrue(inters.size()==1);
		iter = inters.iterator();
		assertEquals(iter.next(), new Point2D(0, 0));
		
		// Vertical line cutting at(1, 1)
		line = new StraightLine2D(1, -6, 0, 1);
		inters = parabola.intersections(line);
		assertTrue(inters.size()==1);
		iter = inters.iterator();
		assertEquals(iter.next(), new Point2D(1, 1));
		
		// diagonal line cutting in (0 0) and (1,1).
		line = new StraightLine2D(-2, -2, 3, 3);
		inters = parabola.intersections(line);
		assertTrue(inters.size()==2);
		iter = inters.iterator();
		assertEquals(iter.next(), new Point2D(0, 0));
		assertEquals(iter.next(), new Point2D(1, 1));
		
		// Parabola pointing to the right
		parabola = new Parabola2D(0, 0, 1, -Math.PI/2);
		line = new StraightLine2D(4, 0, 0, 1);
		inters = parabola.intersections(line);
		assertTrue(inters.size()==2);
		iter = inters.iterator();
		assertEquals(iter.next(), new Point2D(4, 2));
		assertEquals(iter.next(), new Point2D(4, -2));
		
		// Parabola pointing to the top, line over -> 2 intersections
		parabola = new Parabola2D(0, 0, 1./88, 0);
		line = new StraightLine2D(0, 50, -1, 0);
		inters = parabola.intersections(line);
		assertTrue(inters.size()==2);

		// Parabola pointing to the right, line at the right -> 2 intersections
		parabola = new Parabola2D(0, 0, 1./88, Math.PI*1.5);
		line = new StraightLine2D(50, 0, 0, 1);
		inters = parabola.intersections(line);
		assertTrue(inters.size()==2);
		
		// Translated parabola pointing to the right
		parabola = new Parabola2D(20, 10, 1, -Math.PI/2);
		line = new StraightLine2D(24, 0, 0, 20);
		inters = parabola.intersections(line);
		assertTrue(inters.size()==2);
		iter = inters.iterator();
		assertEquals(iter.next(), new Point2D(24, 12));
		assertEquals(iter.next(), new Point2D(24, 8));
	}
	
	public void testClipLine2D(){
		// parabola pointing upwards
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		StraightLine2D line = new StraightLine2D(10, 4, -20, 0);
	
		CurveSet2D<?> clippedCurve;
		
		clippedCurve = Curve2DUtils.clipSmoothCurve(parabola, line);
		Curve2D curve = clippedCurve.firstCurve();
		
		assertTrue(clippedCurve.size()==1);
		assertTrue(curve instanceof ParabolaArc2D);
		
		
		// translated parabola pointing to the right
		parabola = new Parabola2D(20, 10, 1, -Math.PI/2);
		line = new StraightLine2D(24, 0, 0, 20);
		clippedCurve = Curve2DUtils.clipSmoothCurve(parabola, line);
		assertTrue(clippedCurve.size()==1);
		curve = clippedCurve.firstCurve();
		assertTrue(curve instanceof ParabolaArc2D);
		assertTrue(new ParabolaArc2D(parabola, -2, 2).almostEquals(curve, Shape2D.ACCURACY));
		
		
		// Oblic line
		parabola = new Parabola2D(20, 10, 1, 0);
		line = new StraightLine2D(20, 10, -1, -2);
		clippedCurve = Curve2DUtils.clipSmoothCurve(parabola, line);
		assertTrue(clippedCurve.size()==1);
		curve = clippedCurve.firstCurve();
		assertTrue(curve instanceof ParabolaArc2D);
		assertTrue(new ParabolaArc2D(parabola, 0, 2).almostEquals(curve, Shape2D.ACCURACY));
		
		// Oblic line 2
		parabola = new Parabola2D(0, 0, 1, 0);
		line = new StraightLine2D(0, 8, -1, -2);
		clippedCurve = Curve2DUtils.clipSmoothCurve(parabola, line);
		assertTrue(clippedCurve.size()==1);
		curve = clippedCurve.firstCurve();
		assertTrue(curve instanceof ParabolaArc2D);
		assertTrue(new ParabolaArc2D(parabola, -2, 4).almostEquals(curve, Shape2D.ACCURACY));
		
		// Oblic parabola with horizontal line
		double theta = Angle2D.formatAngle(-Math.atan(2));
		parabola = new Parabola2D(20, 10, 1, theta);
		Point2D origin = new Point2D(20, 10+8);
		origin = origin.transform(AffineTransform2D.createRotation(20, 10, theta));
		line = new StraightLine2D(origin, new Vector2D(-1, 0));
		clippedCurve = Curve2DUtils.clipSmoothCurve(parabola, line);
		assertTrue(clippedCurve.size()==1);
		curve = clippedCurve.firstCurve();
		assertTrue(curve instanceof ParabolaArc2D);
		assertTrue(new ParabolaArc2D(parabola, -2, 4).almostEquals(curve, Shape2D.ACCURACY));
	}
	
	public void testClipBox2D(){
		// parabola pointing upwards
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		Box2D box = new Box2D(-10, 10, -4, 4);
		
		CurveSet2D<ParabolaArc2D> clippedCurve = parabola.clip(box);
		Curve2D curve = clippedCurve.firstCurve();
		
		assertTrue(clippedCurve.size()==1);
		assertTrue(curve instanceof ParabolaArc2D);

		// parabola pointing to the right
		parabola = new Parabola2D(0, 0, 1, -Math.PI/2);
		box = new Box2D(-4, 4, -10, 10);
		clippedCurve = parabola.clip(box);
		assertTrue(clippedCurve.size()==1);
		curve = clippedCurve.firstCurve();
		assertTrue(curve instanceof ParabolaArc2D);
		assertTrue(new ParabolaArc2D(parabola, -2, 2).equals(curve));
		
		
		// translated parabola pointing to the right
		parabola = new Parabola2D(20, 10, 1, -Math.PI/2);
		box = new Box2D(20-4, 20+4, 10-10, 10+10);
		clippedCurve = parabola.clip(box);
		assertTrue(clippedCurve.size()==1);
		curve = clippedCurve.firstCurve();
		assertTrue(curve instanceof ParabolaArc2D);
		assertTrue(new ParabolaArc2D(parabola, -2, 2).equals(curve));
	}

	public void testGetCurvatureDouble(){
		double xv = 0;
		double yv = 0;
		double a = 1;
		double theta = 0;
		
		Parabola2D parabola = new Parabola2D(xv, yv, a, theta);
		
		double t0 = 0;
		assertEquals(parabola.curvature(t0), 2*a, 1e-12);
		
		double t1 = 1;
		double k1 = 2 * a / Math.pow(Math.hypot(1, 2 * a * t1), 3);
		assertEquals(parabola.curvature(t1), k1, 1e-12);
	}

	public void testTransformAffineTransform2D(){
		double a = 2;
		Parabola2D base = new Parabola2D(0, 0, a, 0);
		
		double s = 3;
		Parabola2D scaled = base.transform(AffineTransform2D.createScaling(1, 3));
		assertTrue(scaled.almostEquals(new Parabola2D(0, 0, a*s, 0), Shape2D.ACCURACY));
		
		double theta = Math.PI/3;
		Parabola2D rotated = base.transform(AffineTransform2D.createRotation(theta));
		assertTrue(rotated.almostEquals(new Parabola2D(0, 0, a, theta), Shape2D.ACCURACY));
		
		double dx=2, dy=3;
		Parabola2D translated = base.transform(AffineTransform2D.createTranslation(dx, dy));
		assertTrue(translated.almostEquals(new Parabola2D(dx, dy, a, 0), Shape2D.ACCURACY));
		
		
		Parabola2D reflected = base.transform(
				AffineTransform2D.createLineReflection(new StraightLine2D(
						new Point2D(0, 0), new Vector2D(1, 0))));
		assertTrue(reflected.almostEquals(new Parabola2D(0, 0, -a, 0), Shape2D.ACCURACY));
	}
	
	public void testIsInsidePoint2D_Direct(){
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		Point2D pt;
		
		pt = new Point2D(0, 2);
		assertTrue(parabola.isInside(pt));
		
		pt = new Point2D(0, -2);
		assertTrue(!parabola.isInside(pt));		
		
		pt = new Point2D(-2, 10);
		assertTrue(parabola.isInside(pt));
		
		pt = new Point2D(-2, 0);
		assertTrue(!parabola.isInside(pt));		
		
		pt = new Point2D(2, 10);
		assertTrue(parabola.isInside(pt));
		
		pt = new Point2D(2, 0);
		assertTrue(!parabola.isInside(pt));		
	}
	
	public void testIsInsidePoint2D_Inverse(){
		Parabola2D parabola = new Parabola2D(0, 0, -1, 0);
		Point2D pt;
		
		pt = new Point2D(0, -2);
		assertTrue(!parabola.isInside(pt));
		
		pt = new Point2D(0, 2);
		assertTrue(parabola.isInside(pt));		
		
		pt = new Point2D(-2, -10);
		assertTrue(!parabola.isInside(pt));
		
		pt = new Point2D(-2, 0);
		assertTrue(parabola.isInside(pt));		
		
		pt = new Point2D(2, -10);
		assertTrue(!parabola.isInside(pt));
		
		pt = new Point2D(2, 0);
		assertTrue(parabola.isInside(pt));		
	}
	
	public void testGetWindingAnglePoint2D(){
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		Parabola2D inverse = new Parabola2D(0, 0, -1, 0);
		Point2D pt;
		double eps = 1e-12;
		
		// point inside a direct parabola
		pt = new Point2D(1, 2);
		assertEquals(parabola.windingAngle(pt), Math.PI*2, eps);
		
		// point outside a direct parabola
		pt = new Point2D(1, -2);
		assertEquals(parabola.windingAngle(pt), 0, eps);
		
		// point inside an inverse parabola
		pt = new Point2D(1, 2);
		assertEquals(inverse.windingAngle(pt), 0, eps);
		
		// point outside an ibverse parabola
		pt = new Point2D(1, -2);
		assertEquals(inverse.windingAngle(pt), -2*Math.PI, eps);
	}
	
	public void testClone() {
	    Parabola2D parabola = new Parabola2D(10, 20, 2, Math.PI/3);
	    assertTrue(parabola.equals(parabola.clone()));
	    
        parabola = new Parabola2D(10, 20, -2, Math.PI/3);
        assertTrue(parabola.equals(parabola.clone()));
	}
}
