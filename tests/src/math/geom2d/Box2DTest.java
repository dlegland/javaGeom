/* file : Box2DTest.java
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
 * Created on 7 mars 2007
 *
 */
package math.geom2d;

import junit.framework.TestCase;
import java.util.*;

import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.curve.BoundaryPolyCurve2D;
import math.geom2d.curve.BoundarySet2D;
import math.geom2d.curve.ContinuousBoundary2D;
import math.geom2d.curve.ContinuousOrientedCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.PolyOrientedCurve2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Polyline2D;
import math.geom2d.line.StraightLine2D;

public class Box2DTest extends TestCase {

	public void testContains_DoubleDouble(){
		Box2D box = new Box2D(-1, 1, -1, 1);
		assertTrue(box.contains(0, 0));
		assertTrue(!box.contains(-1, 2));
	}	
	
	/*
	 * Test method for 'math.geom2d.Box2D.clipCurve(Curve2D)'
	 */
	public void testClipCurve_LineSegment2D() {
		Box2D box = new Box2D(0, 10, 0, 10);
		LineSegment2D line1 = new LineSegment2D(2, -2, 12, 8);
		LineSegment2D clip1 = new LineSegment2D(4, 0, 10, 6);
		
		CurveSet2D<Curve2D> curveSet = box.clipCurve(line1);
		Curve2D curve = curveSet.getFirstCurve();
		assertTrue(clip1.equals(curve));
	}

	public void testClipCurve_Circle2D() {
		Box2D box = new Box2D(0, 10, 0, 10);
		Circle2D 	line1 = new Circle2D(0, 0, 5, true);
		CircleArc2D clip1 = new CircleArc2D(0, 0, 5, 0, Math.PI/2);
		
		CurveSet2D<Curve2D> curveSet = box.clipCurve(line1);
		Curve2D curve = curveSet.getFirstCurve();
		assertTrue(clip1.equals(curve));
	}

	public void testClipCurve_CircleArc2D() {
		double r = 10;
		double L = 40;
		double l = 10;
		Box2D box = new Box2D(-L/2, L/2, -l/2, l/2);
		CircleArc2D arc1 = new CircleArc2D(0, 0, r, 5*Math.PI/3, 2*Math.PI/3);
		CircleArc2D arc2 = new CircleArc2D(r, 0, r, 2*Math.PI/3, 2*Math.PI/3);		

		CurveSet2D<Curve2D> clipped1 = box.clipCurve(arc1);
		Curve2D curve1 = clipped1.getFirstCurve();
		
		CurveSet2D<Curve2D> clipped2 = box.clipCurve(arc2);
		Curve2D curve2 = clipped2.getFirstCurve();

		double alpha = Math.asin(l/2/r);
		CircleArc2D arc1c = new CircleArc2D(0, 0, r, 
				Angle2D.formatAngle(Math.PI*2-alpha), 2*alpha);
		CircleArc2D arc2c = new CircleArc2D(r, 0, r,
				Angle2D.formatAngle(Math.PI-alpha), 2*alpha);

		assertTrue(arc1c.equals(curve1));
		assertTrue(arc2c.equals(curve2));
	}

	public void testClipCurve_CurveSet2D() {
		double r = 10;
		double L = 40;
		double l = 10;

		Box2D box = new Box2D(-L/2, L/2, -l/2, l/2);
		
		CircleArc2D arc1 	= new CircleArc2D(0, 0, r, 5*Math.PI/3, 2*Math.PI/3);
		CircleArc2D arc2 	= new CircleArc2D(r, 0, r, 2*Math.PI/3, 2*Math.PI/3);		
		CurveSet2D<CircleArc2D> set = new CurveSet2D<CircleArc2D>();
		set.addCurve(arc1);
		set.addCurve(arc2);
		
		CurveSet2D<Curve2D> clippedSet = box.clipCurve(set);
		Iterator<Curve2D> iter 	= clippedSet.getCurves().iterator();
		Curve2D curve1 	= iter.next();
		Curve2D curve2 	= iter.next();
		
		double alpha 	= Math.asin(l/2/r);
		CircleArc2D arc1c = new CircleArc2D(0, 0, r, 
				Angle2D.formatAngle(Math.PI*2-alpha), 2*alpha);
		CircleArc2D arc2c = new CircleArc2D(r, 0, r,
				Angle2D.formatAngle(Math.PI-alpha), 2*alpha);
		
		assertTrue(arc1c.equals(curve1));
		assertTrue(arc2c.equals(curve2));
		
	}
	
	public void testClipCurve_Polyline2D() {
		Polyline2D poly1 = new Polyline2D(new Point2D[]{
				new Point2D(10, -5), 
				new Point2D(25, 10), 
				new Point2D(10, 25), 
				new Point2D(-5, 10),
				new Point2D(10, -5),
		});
		Box2D box = new Box2D(0, 20, 0, 20);
		
		CurveSet2D<Curve2D> clip1 = box.clipCurve(poly1);
		Polyline2D sub1 = new Polyline2D(new Point2D[]{new Point2D(15, 0), new Point2D(20, 5)});
		Polyline2D sub2 = new Polyline2D(new Point2D[]{new Point2D(20, 15), new Point2D(15, 20)});
		Polyline2D sub3 = new Polyline2D(new Point2D[]{new Point2D(5, 20), new Point2D(0, 15)});
		Polyline2D sub4 = new Polyline2D(new Point2D[]{new Point2D(0, 5), new Point2D(5, 0)});
		CurveSet2D<Curve2D> set1 = new CurveSet2D<Curve2D>();
		set1.addCurve(sub1);
		set1.addCurve(sub2);
		set1.addCurve(sub3);
		set1.addCurve(sub4);
		assertTrue(set1.equals(clip1));
	}

	public void testClipBoundary_Circle2D() {
		Box2D box = new Box2D(0, 10, 0, 10);

		Circle2D circle1 = new Circle2D(5, 7, 2);
		BoundarySet2D<ContinuousBoundary2D> boundary = 
			new BoundarySet2D<ContinuousBoundary2D>(circle1);		
		BoundarySet2D<ContinuousBoundary2D> clipped =
			box.clipBoundary(boundary);
		 
		Collection<ContinuousBoundary2D> curves = clipped.getBoundaryCurves();
		ContinuousBoundary2D curve = curves.iterator().next();
		assertTrue(curve instanceof Circle2D);
		assertTrue(circle1.equals(curve));
			
		
		Circle2D circle2 = new Circle2D(5, 0, 2);
		CircleArc2D circlearc2 = new CircleArc2D(5, 0, 2, 0, Math.PI);
		//BoundarySet2D boundary2 = new BoundarySet2D(circle2);		
		BoundarySet2D<ContinuousBoundary2D> clipped2 =
			box.clipBoundary(circle2);
		 
		Collection<ContinuousBoundary2D> curves2 = clipped2.getBoundaryCurves();
		ContinuousBoundary2D curve2 = curves2.iterator().next();
		assertTrue(!(curve2 instanceof CircleArc2D));
		assertTrue(curve2 instanceof PolyOrientedCurve2D);
		
		Iterator<?> iter = ((PolyOrientedCurve2D<?>) curve2).getCurves().iterator();
		ContinuousOrientedCurve2D curve3 = (ContinuousOrientedCurve2D) iter.next();
		
		assertTrue(curve3 instanceof CircleArc2D);
		assertTrue(curve3.equals(circlearc2));
		
		double r = 10;
		CircleArc2D arc1 = new CircleArc2D(0, 0, r, 5*Math.PI/3, 2*Math.PI/3);
		CircleArc2D arc2 = new CircleArc2D(r, 0, r, 2*Math.PI/3, 2*Math.PI/3);		
		BoundaryPolyCurve2D<CircleArc2D> set = new BoundaryPolyCurve2D<CircleArc2D>();
		set.addCurve(arc1);
		set.addCurve(arc2);
		boundary = new BoundarySet2D<ContinuousBoundary2D>();
		boundary.addCurve(set);
		double L = 40;
		double l = 10;
		box = new Box2D(-L/2, L/2, -l/2, l/2);
		clipped = box.clipBoundary(boundary);
		curves = clipped.getBoundaryCurves();
		assertTrue(curves.size()==1);
		curve = curves.iterator().next();	
		assertTrue(curve instanceof PolyOrientedCurve2D);
	}
	
	public void testClipBoundary_StraightLine2D() {
		Box2D box = new Box2D(0, 10, 0, 10);
		StraightLine2D line = new StraightLine2D(4, 4, 2, 4);
		
		LineSegment2D segment = new LineSegment2D(2, 0, 7, 10);
		
		CurveSet2D<Curve2D> clipped = box.clipCurve(line);
		assertTrue(clipped.getCurveNumber()==1);
		Iterator<Curve2D> iter = clipped.getCurves().iterator();
		assertTrue(iter.next().equals(segment));
	}
	
//	public void testClipBoundary_Polyline2D() {
//		Polyline2D poly1 = new Polyline2D(new Point2D[]{
//				new Point2D(10, -5), 
//				new Point2D(25, 10), 
//				new Point2D(10, 25), 
//				new Point2D(-5, 10),
//				new Point2D(10, -5),
//		});
//		Box2D box = new Box2D(0, 0, 20, 20);
//		
//		CurveSet2D clip1 = box.clipCurve(poly1);
//		Polyline2D sub1 = new Polyline2D(new Point2D[]{new Point2D(15, 0), new Point2D(20, 5)});
//		Polyline2D sub2 = new Polyline2D(new Point2D[]{new Point2D(20, 15), new Point2D(15, 20)});
//		Polyline2D sub3 = new Polyline2D(new Point2D[]{new Point2D(5, 20), new Point2D(0, 15)});
//		Polyline2D sub4 = new Polyline2D(new Point2D[]{new Point2D(0, 5), new Point2D(5, 0)});
//		CurveSet2D set1 = new CurveSet2D();
//		set1.addCurve(sub1);
//		set1.addCurve(sub2);
//		set1.addCurve(sub3);
//		set1.addCurve(sub4);
//		assertTrue(set1.equals(clip1));
//	}
	
	
	public void testFindNextCurveIndex() {
		double nan = Double.NaN;
		double[] tab1 = new double[]{nan, nan, .6, .2, nan, .4};
		assertEquals(Box2D.findNextCurveIndex(tab1, .1), 3);
		assertEquals(Box2D.findNextCurveIndex(tab1, .5), 2);
		assertEquals(Box2D.findNextCurveIndex(tab1, .3), 5);
		assertEquals(Box2D.findNextCurveIndex(tab1, .8), 3);
	}
}
