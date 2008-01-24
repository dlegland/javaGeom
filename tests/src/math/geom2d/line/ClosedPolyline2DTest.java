/*
 * File : ClosedPolyline2DTest.java
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
 * Created on 16 Apr. 2007
 */

package math.geom2d.line;
import java.util.Collection;
import java.util.Iterator;

import math.geom2d.Point2D;

import junit.framework.TestCase;


/**
 * @author Legland
 */
public class ClosedPolyline2DTest extends TestCase {

	/**
	 * Constructor for ClosedClosedPolyline2DTest.
	 * @param arg0
	 */
	public ClosedPolyline2DTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(ClosedPolyline2DTest.class);
	}

	public void testAddLine(){
		Point2D[] points = new Point2D[4];
		points[0] = new Point2D(0, 0);
		points[1] = new Point2D(10, 0);
		points[2] = new Point2D(10, 10);
		points[3] = new Point2D(20, 20);
		ClosedPolyline2D line = new ClosedPolyline2D(points);
		line.addPoint(new Point2D(30, 20));
		assertEquals(line.getVerticesNumber(), points.length+1);		
	}

	public void testGetPoint() {
		// initialize points
		Point2D[] points = new Point2D[4];
		points[0] = new Point2D(0, 0);
		points[1] = new Point2D(10, 0);
		points[2] = new Point2D(10, 10);
		points[3] = new Point2D(20, 20);
		
		// create polyline
		ClosedPolyline2D line1 = new ClosedPolyline2D(points);

		// find the first point
		Point2D p00 = new Point2D(0, 0);
		assertTrue(p00.equals(line1.getPoint(-.5)));
		assertTrue(p00.equals(line1.getPoint(0)));
		
		// find intermediate points
		Point2D p05 = new Point2D(5, 0);
		assertTrue(p05.equals(line1.getPoint(.5)));		
		Point2D p15 = new Point2D(10, 5);
		assertTrue(p15.equals(line1.getPoint(1.5)));		
		Point2D p25 = new Point2D(15, 15);
		assertTrue(p25.equals(line1.getPoint(2.5)));
		Point2D p30 = new Point2D(20, 20);
		assertTrue(p30.equals(line1.getPoint(3)));
		Point2D p35 = new Point2D(10, 10);
		assertTrue(p35.equals(line1.getPoint(3.5)));
		
		// find the last point
		Point2D p40 = new Point2D(0, 0);
		assertTrue(p40.equals(line1.getPoint(4)));
		assertTrue(p40.equals(line1.getPoint(4.1)));
	}

	public void testgetIntersections() {
		// initialize points
		Point2D[] points = new Point2D[3];
		points[0] = new Point2D(0, -5);
		points[1] = new Point2D(10, 5);
		points[2] = new Point2D(20, -5);
		
		// create polyline
		ClosedPolyline2D poly = new ClosedPolyline2D(points);

		// line to intersect with
		StraightLine2D line = new StraightLine2D(0, 0, 1, 0);
		StraightLine2D edge = new StraightLine2D(0, 0, 20, 0);
		
		Collection<Point2D> inters1 = poly.getIntersections(line);
		assertTrue(inters1.size()==2);
		Iterator<Point2D> iter1 = inters1.iterator();
		assertTrue(iter1.next().equals(new Point2D(5, 0)));
		assertTrue(iter1.next().equals(new Point2D(15, 0)));
		
		Collection<Point2D> inters2 = poly.getIntersections(edge);
		assertTrue(inters2.size()==2);
		Iterator<Point2D> iter2 = inters2.iterator();
		assertTrue(iter2.next().equals(new Point2D(5, 0)));
		assertTrue(iter2.next().equals(new Point2D(15, 0)));
	}

	public void testGetSubCurve(){
		// initialize points
		Point2D[] points = new Point2D[4];
		points[0] = new Point2D(0, 0);
		points[1] = new Point2D(10, 0);
		points[2] = new Point2D(10, 10);
		points[3] = new Point2D(20, 20);
		
		// create polyline, and subcurve
		ClosedPolyline2D line1 = new ClosedPolyline2D(points);
		Polyline2D sub = (Polyline2D) line1.getSubCurve(.5, 2.5);
		
		// to check result
		Polyline2D line2 = new Polyline2D(new Point2D[]{
				new Point2D(5, 0), 		new Point2D(10, 0), 
				new Point2D(10, 10),	new Point2D(15, 15)	});
		
		// check two objects are the same
		assertTrue(line2.equals(sub));
	}
	
	public void testGetSignedArea(){
		ClosedPolyline2D polyline = new ClosedPolyline2D(new Point2D[]{
				new Point2D(20, 10),
				new Point2D(20, 20),
				new Point2D(10, 20),
				new Point2D(10, 10)
		});
		assertEquals(polyline.getSignedArea(), 100, 1e-10);
		assertEquals(polyline.getArea(), 100, 1e-10);

		ClosedPolyline2D invert = new ClosedPolyline2D(new Point2D[]{
				new Point2D(20, 10),
				new Point2D(10, 10),
				new Point2D(10, 20),
				new Point2D(20, 20)
		});
		assertEquals(invert.getSignedArea(), -100, 1e-10);
		assertEquals(invert.getArea(), 100, 1e-10);
	}
	
	public void testIsInside(){
		ClosedPolyline2D polyline = new ClosedPolyline2D(new Point2D[]{
				new Point2D(150, 50),
				new Point2D(150, 150),
				new Point2D(100, 100),
				new Point2D(50, 150),
				new Point2D(50, 50)
		});
		
		assertTrue(polyline.isInside(new Point2D(60, 60)));
		assertTrue(polyline.isInside(new Point2D(140, 60)));
		assertTrue(polyline.isInside(new Point2D(55, 140)));
		assertTrue(polyline.isInside(new Point2D(145, 140)));
		
		assertTrue(!polyline.isInside(new Point2D(100, -50)));
		assertTrue(!polyline.isInside(new Point2D(100, 110)));
		assertTrue(!polyline.isInside(new Point2D(200, 50)));
		assertTrue(!polyline.isInside(new Point2D(0, 50)));		

		// Try the other orientation
		polyline = new ClosedPolyline2D(new Point2D[]{
				new Point2D(50, 50),
				new Point2D(50, 150),
				new Point2D(100, 100),
				new Point2D(150, 150),
				new Point2D(150, 50),
		});
		
//		assertTrue(!polyline.isInside(new Point2D(60, 60)));
//		assertTrue(!polyline.isInside(new Point2D(140, 60)));
//		assertTrue(!polyline.isInside(new Point2D(55, 140)));
//		assertTrue(!polyline.isInside(new Point2D(145, 140)));
//		
//		assertTrue(polyline.isInside(new Point2D(100, -50)));
//		assertTrue(polyline.isInside(new Point2D(100, 110)));
//		assertTrue(polyline.isInside(new Point2D(200, 50)));
//		assertTrue(polyline.isInside(new Point2D(0, 50)));		
	
	}
}
