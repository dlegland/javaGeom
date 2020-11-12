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
 * Created on 27 d�c. 2003
 */

package net.javageom.geom2d.polygon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;
import net.javageom.geom2d.Point2D;
import net.javageom.geom2d.Vector2D;
import net.javageom.geom2d.circulinear.CirculinearContinuousCurve2D;
import net.javageom.geom2d.circulinear.buffer.BufferCalculator;
import net.javageom.geom2d.conic.CircleArc2D;
import net.javageom.geom2d.domain.Boundary2D;
import net.javageom.geom2d.domain.Domain2D;
import net.javageom.geom2d.line.StraightLine2D;
import net.javageom.geom2d.polygon.Polyline2D;

/**
 * @author Legland
 */
public class Polyline2DTest extends TestCase
{
	/**
	 * Constructor for Polyline2DTest.
	 * 
	 * @param arg0
	 */
    public Polyline2DTest(String arg0)
    {
		super(arg0);
	}

    public void testSimplify()
    {
		CircleArc2D arc = new CircleArc2D(0, 0, 10, 0, Math.PI / 2);
		Polyline2D poly = arc.asPolyline(16);
		Polyline2D poly2 = poly.simplify(2.5);
		
		assertEquals(3, poly2.vertexNumber());
	}
	
    public void testDistanceToPoint()
    {
        ArrayList<Point2D> listPoint= new ArrayList<>();
        listPoint.add(new Point2D(201093.16610711772, 1777477.3696508463)); 
        listPoint.add(new Point2D(202667.84818503872, 1777673.8963489647));
        Polyline2D polyline = Polyline2D.create(listPoint);
        Point2D currentPoint = new Point2D(201926.0433899812, 1777926.9596233366);
        double dist1 = polyline.distance(currentPoint);
//        System.out.println("distance1: " + dist1);
        
        ArrayList<Point2D> listPoint2= new ArrayList<>();
        listPoint2.add(new Point2D(201.09316610711772, 1777.4773696508463));
        listPoint2.add(new Point2D(202.66784818503872, 1777.6738963489647));

        Polyline2D polyline2 = Polyline2D.create(listPoint2);
        Point2D currentPoint2 = new Point2D(201.9260433899812, 1777.9269596233366);
        double dist2 = polyline2.distance(currentPoint2);
//        System.out.println("distance2: " + dist2*1000);
        
        assertEquals(dist1, dist2 * 1000, 1.0);
    }
    
    
    public void testGetLength()
    {
		Polyline2D polyline = createDefaultPolyline();
		assertEquals(polyline.length(), 30, 1e-14);
	}

    public void testGetLengthDouble()
    {
        Polyline2D polyline = createDefaultPolyline();

		assertEquals(polyline.length(0), 0, 1e-14);
		assertEquals(polyline.length(.5), 5, 1e-14);
		assertEquals(polyline.length(1), 10, 1e-14);
		assertEquals(polyline.length(1.5), 15, 1e-14);
		assertEquals(polyline.length(2), 20, 1e-14);
		assertEquals(polyline.length(2.5), 25, 1e-14);
		assertEquals(polyline.length(3), 30, 1e-14);
	}

    public void testGetPositionDouble()
    {
        Polyline2D polyline = createDefaultPolyline();

		assertEquals(polyline.position(0), 0, 1e-14);
		assertEquals(polyline.position(5), .5, 1e-14);
		assertEquals(polyline.position(10), 1, 1e-14);
		assertEquals(polyline.position(15), 1.5, 1e-14);
		assertEquals(polyline.position(20), 2, 1e-14);
		assertEquals(polyline.position(25), 2.5, 1e-14);
		assertEquals(polyline.position(30), 3, 1e-14);
	}

    public void testGetBufferDouble()
    {
		Polyline2D polyline = new Polyline2D(new Point2D[] {
				new Point2D(50, 50), new Point2D(100, 50),
				new Point2D(100, 100), new Point2D(150, 100) });
		double dist = 30;

		Domain2D buffer = polyline.buffer(dist);
		Boundary2D boundary = buffer.boundary();
		assertEquals(1, boundary.continuousCurves().size());
	}

    public void testGetBufferDouble_MultipleVertex()
    {
		Polyline2D polyline = new Polyline2D(new Point2D[] {
				new Point2D(50, 50), new Point2D(100, 50),
				new Point2D(100, 50), new Point2D(100, 100),
				new Point2D(150, 100) });
		double dist = 30;

		Domain2D buffer = polyline.buffer(dist);
		Boundary2D boundary = buffer.boundary();
		assertEquals(1, boundary.continuousCurves().size());
	}

	public void testGetParallels_SmallAnglePolyline() {
		Polyline2D polyline = new Polyline2D(new Point2D[] {
				new Point2D(200, 100), new Point2D(100, 100),
				new Point2D(180, 140) });
		double dist = 30;

		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		Domain2D buffer = bc.computeBuffer(polyline, dist);

		assertEquals(1, buffer.boundary().continuousCurves().size());
	}

	public void testGetBufferDouble_SmallAnglePolyline() {
		Polyline2D polyline = new Polyline2D(new Point2D[] {
				new Point2D(200, 100), new Point2D(100, 100),
				new Point2D(180, 140) });
		double dist = 30;

		Domain2D buffer = polyline.buffer(dist);
		Boundary2D boundary = buffer.boundary();
		// Fails for the moment
		assertEquals(1, boundary.continuousCurves().size());
	}

    public void testGetParallelDouble()
    {
		Polyline2D polyline = new Polyline2D(new Point2D[] {
				new Point2D(50, 50), new Point2D(100, 50),
				new Point2D(100, 100), new Point2D(150, 100) });

		CirculinearContinuousCurve2D parallel = polyline.parallel(30);
		assertTrue(parallel != null);
	}

    public void testGetLeftTangent()
    {
		Polyline2D line = new Polyline2D(new Point2D[] { new Point2D(10, 10),
				new Point2D(20, 10), new Point2D(20, 20) });

		assertTrue(line.leftTangent(1).equals(new Vector2D(10, 0)));
		assertTrue(line.leftTangent(2).equals(new Vector2D(0, 10)));
	}

    public void testGetRightTangent()
    {
		Polyline2D line = new Polyline2D(new Point2D[] { new Point2D(10, 10),
				new Point2D(20, 10), new Point2D(20, 20) });

		assertTrue(line.rightTangent(0).equals(new Vector2D(10, 0)));
		assertTrue(line.rightTangent(1).equals(new Vector2D(0, 10)));
	}

    public void testAddVertex()
    {
		Point2D[] points = new Point2D[4];
		points[0] = new Point2D(0, 0);
		points[1] = new Point2D(10, 0);
		points[2] = new Point2D(10, 10);
		points[3] = new Point2D(20, 20);
		Polyline2D line = new Polyline2D(points);
		line.addVertex(new Point2D(30, 20));
		assertEquals(line.vertexNumber(), points.length + 1);
	}

    public void testRemoveVertex_Point2D()
    {
		Point2D[] points = new Point2D[4];
		points[0] = new Point2D(0, 0);
		points[1] = new Point2D(10, 0);
		points[2] = new Point2D(10, 10);
		points[3] = new Point2D(20, 20);
		Polyline2D line = new Polyline2D(points);
		line.removeVertex(new Point2D(10, 10));
		assertEquals(3, line.vertexNumber());
	}

    public void testRemoveVertex_int()
    {
		Point2D[] points = new Point2D[4];
		points[0] = new Point2D(0, 0);
		points[1] = new Point2D(10, 0);
		points[2] = new Point2D(10, 10);
		points[3] = new Point2D(20, 20);
		Polyline2D line = new Polyline2D(points);
		line.removeVertex(2);
		assertEquals(3, line.vertexNumber());
	}

    public void testInsertVertex()
    {
		Point2D[] points = new Point2D[4];
		points[0] = new Point2D(0, 0);
		points[1] = new Point2D(10, 0);
		points[2] = new Point2D(10, 10);
		points[3] = new Point2D(20, 20);
		Polyline2D line = new Polyline2D(points);
		line.insertVertex(2, new Point2D(15, 20));
		assertEquals(5, line.vertexNumber());
		assertEquals(line.vertex(2), new Point2D(15, 20));
	}

    public void testGetPoint()
    {
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
		assertTrue(p00.equals(line1.point(-.5)));
		assertTrue(p00.equals(line1.point(0)));

		// find intermediate points
		Point2D p05 = new Point2D(5, 0);
		assertTrue(p05.equals(line1.point(.5)));
		Point2D p15 = new Point2D(10, 5);
		assertTrue(p15.equals(line1.point(1.5)));
		Point2D p25 = new Point2D(15, 15);
		assertTrue(p25.equals(line1.point(2.5)));

		// find the last point
		Point2D p30 = new Point2D(20, 20);
		assertTrue(p30.equals(line1.point(3)));
		assertTrue(p30.equals(line1.point(3.5)));
	}

    public void testGetPosition()
    {
		Polyline2D line = new Polyline2D(new Point2D[] { new Point2D(0, 0),
				new Point2D(10, 0), new Point2D(10, 10), new Point2D(20, 20) });
		double eps = 1e-14;

		// corners
		assertEquals(line.position(new Point2D(0, 0)), 0, eps);
		assertEquals(line.position(new Point2D(10, 0)), 1, eps);
		assertEquals(line.position(new Point2D(10, 10)), 2, eps);
		assertEquals(line.position(new Point2D(20, 20)), 3, eps);

		// middles of edges
		assertEquals(line.position(new Point2D(5, 0)), .5, eps);
		assertEquals(line.position(new Point2D(10, 5)), 1.5, eps);
		assertEquals(line.position(new Point2D(15, 15)), 2.5, eps);

	}

    public void testgetIntersections()
    {
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

		Collection<Point2D> inters1 = poly.intersections(line);
		assertTrue(inters1.size() == 2);
		Iterator<Point2D> iter1 = inters1.iterator();
		assertTrue(iter1.next().equals(new Point2D(5, 0)));
		assertTrue(iter1.next().equals(new Point2D(15, 0)));

		Collection<Point2D> inters2 = poly.intersections(edge);
		assertTrue(inters2.size() == 2);
		Iterator<Point2D> iter2 = inters1.iterator();
		assertTrue(iter2.next().equals(new Point2D(5, 0)));
		assertTrue(iter2.next().equals(new Point2D(15, 0)));
	}

    public void testGetSubCurve()
    {
        // create polyline
        Polyline2D line1 = createDefaultPolyline();

        Polyline2D sub = line1.subCurve(.5, 2.5);

        // to check result
        Polyline2D line2 = new Polyline2D(new Point2D[] { new Point2D(15, 10),
                new Point2D(20, 10), new Point2D(20, 20), new Point2D(15, 20) });

        // check two objects are the same
        assertTrue(line2.equals(sub));
    }

	public void testIsInside() 
	{
		Polyline2D polyline = new Polyline2D(new Point2D[] {
				new Point2D(150, 50), new Point2D(150, 150),
				new Point2D(100, 100), new Point2D(50, 150),
				new Point2D(50, 50) });

		assertTrue(polyline.isInside(new Point2D(60, 60)));
		assertTrue(polyline.isInside(new Point2D(140, 60)));
		assertTrue(polyline.isInside(new Point2D(55, 140)));
		assertTrue(polyline.isInside(new Point2D(145, 140)));
		assertTrue(polyline.isInside(new Point2D(100, -50)));

		assertTrue(!polyline.isInside(new Point2D(100, 110)));
		assertTrue(!polyline.isInside(new Point2D(200, 50)));
		assertTrue(!polyline.isInside(new Point2D(0, 50)));
	}

    public void testCreate_Collection()
    {
		ArrayList<Point2D> array = new ArrayList<Point2D>(4);
		array.add(new Point2D(10, 10));
		array.add(new Point2D(20, 10));
		array.add(new Point2D(20, 20));
		array.add(new Point2D(10, 20));
		Polyline2D ring = Polyline2D.create(array);
		assertNotNull(ring);
	}

    public void testCreate_Array()
    {
		Point2D[] array = new Point2D[4];
		array[0] = new Point2D(10, 10);
		array[1] = new Point2D(20, 10);
		array[2] = new Point2D(20, 20);
		array[3] = new Point2D(10, 20);
		Polyline2D ring = Polyline2D.create(array);
		assertNotNull(ring);
	}

    public void testCopyConstructor()
    {
		Polyline2D polyline = new Polyline2D(new Point2D[] {
				new Point2D(150, 50), new Point2D(150, 150),
				new Point2D(100, 100), new Point2D(50, 150),
				new Point2D(50, 50) });

		Polyline2D copy = new Polyline2D(polyline);
		assertTrue(polyline.equals(copy));
	}

	/**
	 * @return an open polyline composed of three line segment of length 10. 
	 */
	private static final Polyline2D createDefaultPolyline()
	{
        return new Polyline2D(new Point2D[] { 
                new Point2D(10, 10),
                new Point2D(20, 10), 
                new Point2D(20, 20), 
                new Point2D(10, 20) });
	}
}
