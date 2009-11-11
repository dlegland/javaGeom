/*
 * File : Polyline2DTest.java
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
 * Created on 27 déc. 2003
 */

package math.geom2d.polygon;
import junit.framework.TestCase;

import java.util.*;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.ContinuousCirculinearCurve2D;

import math.geom2d.line.StraightLine2D;

/**
 * @author Legland
 */
public class Polyline2DTest extends TestCase {

	/**
	 * Constructor for Polyline2DTest.
	 * @param arg0
	 */
	public Polyline2DTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(Polyline2DTest.class);
	}


	public void testGetLength() {
		Polyline2D polyline = new Polyline2D(new Point2D[]{
				new Point2D(0, 0),
				new Point2D(10, 0),
				new Point2D(10, 10), 
				new Point2D(20, 10)});
		
		assertEquals(polyline.getLength(), 30, 1e-14);
	}
	
	public void testGetLengthDouble() {
		Polyline2D polyline = new Polyline2D(new Point2D[]{
				new Point2D(0, 0),
				new Point2D(10, 0),
				new Point2D(10, 10), 
				new Point2D(20, 10)});
		
		assertEquals(polyline.getLength(0), 0, 1e-14);
		assertEquals(polyline.getLength(.5), 5, 1e-14);
		assertEquals(polyline.getLength(1), 10, 1e-14);
		assertEquals(polyline.getLength(1.5), 15, 1e-14);
		assertEquals(polyline.getLength(2), 20, 1e-14);
		assertEquals(polyline.getLength(2.5), 25, 1e-14);
		assertEquals(polyline.getLength(3), 30, 1e-14);
	}
	
	public void testGetPositionDouble() {
		Polyline2D polyline = new Polyline2D(new Point2D[]{
				new Point2D(0, 0),
				new Point2D(10, 0),
				new Point2D(10, 10), 
				new Point2D(20, 10)});
		
		assertEquals(polyline.getPosition(0), 0, 1e-14);
		assertEquals(polyline.getPosition(5), .5, 1e-14);
		assertEquals(polyline.getPosition(10), 1, 1e-14);
		assertEquals(polyline.getPosition(15), 1.5, 1e-14);
		assertEquals(polyline.getPosition(20), 2, 1e-14);
		assertEquals(polyline.getPosition(25), 2.5, 1e-14);
		assertEquals(polyline.getPosition(30), 3, 1e-14);
	}
	
	public void testGetParallelDouble() {
		Polyline2D polyline = new Polyline2D(new Point2D[]{
				new Point2D(50, 50),
				new Point2D(100, 50),
				new Point2D(100, 100),
				new Point2D(150, 100) });
		
		ContinuousCirculinearCurve2D parallel = polyline.getParallel(30);
		assertTrue(parallel!=null);
	}
	
	public void testGetLeftTangent(){
		Polyline2D line = new Polyline2D(new Point2D[]{
				new Point2D(10, 10),
				new Point2D(20, 10),
				new Point2D(20, 20)});
		
		assertTrue(line.getLeftTangent(1).equals(new Vector2D(10, 0)));
		assertTrue(line.getLeftTangent(2).equals(new Vector2D(0, 10)));
	}
	
	public void testGetRightTangent(){
		Polyline2D line = new Polyline2D(new Point2D[]{
				new Point2D(10, 10),
				new Point2D(20, 10),
				new Point2D(20, 20)});
		
		assertTrue(line.getRightTangent(0).equals(new Vector2D(10, 0)));
		assertTrue(line.getRightTangent(1).equals(new Vector2D(0, 10)));
	}

	public void testAddLine(){
		Point2D[] points = new Point2D[4];
		points[0] = new Point2D(0, 0);
		points[1] = new Point2D(10, 0);
		points[2] = new Point2D(10, 10);
		points[3] = new Point2D(20, 20);
		Polyline2D line = new Polyline2D(points);
		line.addPoint(new Point2D(30, 20));
		assertEquals(line.getVertexNumber(), points.length+1);		
	}

	public void testGetPoint() {
		// initialize points
		Point2D[] points = new Point2D[4];
		points[0] = new Point2D(0, 0);
		points[1] = new Point2D(10, 0);
		points[2] = new Point2D(10, 10);
		points[3] = new Point2D(20, 20);
		
		// create polyline
		Polyline2D line1 = new Polyline2D(points);

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
		
		// find the last point
		Point2D p30 = new Point2D(20, 20);
		assertTrue(p30.equals(line1.getPoint(3)));
		assertTrue(p30.equals(line1.getPoint(3.5)));
	}

	public void testGetPosition() {
		Polyline2D line = new Polyline2D(new Point2D[]{
				 new Point2D(0, 0),
				 new Point2D(10, 0),
				 new Point2D(10, 10),
				 new Point2D(20, 20) });
		double eps = 1e-14;
		
		// corners
		assertEquals(line.getPosition(new Point2D(0, 0)), 0, eps);
		assertEquals(line.getPosition(new Point2D(10, 0)), 1, eps);
		assertEquals(line.getPosition(new Point2D(10, 10)), 2, eps);
		assertEquals(line.getPosition(new Point2D(20, 20)), 3, eps);
		
		// middles of edges
		assertEquals(line.getPosition(new Point2D(5, 0)), .5, eps);
		assertEquals(line.getPosition(new Point2D(10, 5)), 1.5, eps);
		assertEquals(line.getPosition(new Point2D(15, 15)), 2.5, eps);
		
	}
	
	
	public void testgetIntersections() {
		// initialize points
		Point2D[] points = new Point2D[3];
		points[0] = new Point2D(0, -5);
		points[1] = new Point2D(10, 5);
		points[2] = new Point2D(20, -5);
		
		// create polyline
		Polyline2D poly = new Polyline2D(points);

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
		Iterator<Point2D> iter2 = inters1.iterator();
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
		Polyline2D line1 = new Polyline2D(points);
		Polyline2D sub = line1.getSubCurve(.5, 2.5);
		
		// to check result
		Polyline2D line2 = new Polyline2D(new Point2D[]{
				new Point2D(5, 0), 		new Point2D(10, 0), 
				new Point2D(10, 10),	new Point2D(15, 15)	});
		
		// check two objects are the same
		assertTrue(line2.equals(sub));
	}
	
	public void testIsInside(){
		Polyline2D polyline = new Polyline2D(new Point2D[]{
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
		assertTrue(polyline.isInside(new Point2D(100, -50)));
		
		assertTrue(!polyline.isInside(new Point2D(100, 110)));
		assertTrue(!polyline.isInside(new Point2D(200, 50)));
		assertTrue(!polyline.isInside(new Point2D(0, 50)));
	}
	
	public void testCreate_Collection() {
		ArrayList<Point2D> array = new ArrayList<Point2D>(4);
		array.add(new Point2D(10, 10));
		array.add(new Point2D(20, 10));
		array.add(new Point2D(20, 20));
		array.add(new Point2D(10, 20));
		Polyline2D ring = Polyline2D.create(array);
		assertNotNull(ring);
	}
	
	public void testCreate_Array() {
		Point2D[] array = new Point2D[4];
		array[0] = new Point2D(10, 10);
		array[1] = new Point2D(20, 10);
		array[2] = new Point2D(20, 20);
		array[3] = new Point2D(10, 20);
		Polyline2D ring = Polyline2D.create(array);
		assertNotNull(ring);
	}
	
	public void testClone() {
        Polyline2D polyline = new Polyline2D(new Point2D[]{
                new Point2D(150, 50),
                new Point2D(150, 150),
                new Point2D(100, 100),
                new Point2D(50, 150),
                new Point2D(50, 50)
        });
	    
        assertTrue(polyline.equals(polyline.clone()));
	}
}
