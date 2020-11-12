/**
 * 
 */
package net.javageom.geom3d.plane;

import junit.framework.TestCase;
import net.javageom.geom3d.Point3D;
import net.javageom.geom3d.Vector3D;
import net.javageom.geom3d.line.StraightLine3D;
import net.javageom.geom3d.plane.Plane3D;

/**
 * @author dlegland
 *
 */
public class Plane3DTest extends TestCase {

	/**
	 * Test method for {@link net.javageom.geom3d.plane.Plane3D#normal()}.
	 */
	public void testGetNormalVector() {
		Plane3D plane 	= Plane3D.createXYPlane();
		Vector3D vz 	= new Vector3D(0, 0, -1);
		assertTrue(vz.equals(plane.normal()));
	}

	/**
	 * Test method for {@link net.javageom.geom3d.plane.Plane3D#lineIntersection(net.javageom.geom3d.line.StraightLine3D)}.
	 */
	public void testGetLineIntersection() {
		Plane3D plane 	= Plane3D.createXYPlane();
		StraightLine3D line1 = new StraightLine3D(new Point3D(2, 2, 2), new Vector3D(0, 0, 1));
		Point3D p1 = new Point3D(2, 2, 0);
		
		Point3D pInt;
		pInt = plane.lineIntersection(line1);
		
		assertTrue(p1.equals(pInt));
	}
	
	public void testProjectPoint() {
		Plane3D plane 	= Plane3D.createXYPlane();
		Point3D p0 	= new Point3D(3, 4, 0);
		Point3D p1 	= new Point3D(3, 4, 10);
		Point3D p2 	= new Point3D(3, 4, -10);

		assertTrue(p0.equals(plane.projectPoint(p1)));
		assertTrue(p0.equals(plane.projectPoint(p2)));

		plane 	= Plane3D.createXZPlane();
		p0 	= new Point3D(3, 0, 4);
		p1 	= new Point3D(3, 10, 4);
		p2 	= new Point3D(3, -10, 4);

		assertTrue(p0.equals(plane.projectPoint(p1)));
		assertTrue(p0.equals(plane.projectPoint(p2)));

		plane 	= Plane3D.createYZPlane();
		p0 	= new Point3D(0, 3, 4);
		p1 	= new Point3D(10, 3, 4);
		p2 	= new Point3D(-10, 3, 4);

		assertTrue(p0.equals(plane.projectPoint(p1)));
		assertTrue(p0.equals(plane.projectPoint(p2)));
	}

	public void testProjectVector() {
		Vector3D vx 	= new Vector3D(1, 0, 0);
		Vector3D vy 	= new Vector3D(0, 1, 0);
		Vector3D vz 	= new Vector3D(0, 0, 1);

		Plane3D plane 	= new Plane3D(new Point3D(10, 5, 8), vx, vy);
		Vector3D v0 	= new Vector3D(3, 4, 0);
		Vector3D v1 	= new Vector3D(3, 4, 10);
		Vector3D v2 	= new Vector3D(3, 4, -10);

		assertTrue(v0.equals(plane.projectVector(v1)));
		assertTrue(v0.equals(plane.projectVector(v2)));

		plane 	= new Plane3D(new Point3D(10, 5, 8), vx, vz);
		v0 	= new Vector3D(3, 0, 4);
		v1 	= new Vector3D(3, 10, 4);
		v2 	= new Vector3D(3, -10, 4);

		assertTrue(v0.equals(plane.projectVector(v1)));
		assertTrue(v0.equals(plane.projectVector(v2)));

		plane 	= new Plane3D(new Point3D(10, 5, 8), vy, vz);
		v0 	= new Vector3D(0, 3, 4);
		v1 	= new Vector3D(10, 3, 4);
		v2 	= new Vector3D(-10, 3, 4);

		assertTrue(v0.equals(plane.projectVector(v1)));
		assertTrue(v0.equals(plane.projectVector(v2)));
	}

	
	/**
	 * Test method for {@link java.lang.Object#equals(java.lang.Object)}.
	 */
	public void testEquals() {
		Plane3D plane1 = new Plane3D(new Point3D(0, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 1, 0));
		Plane3D plane2 = Plane3D.createXYPlane();
		
		assertTrue(plane1.equals(plane2));
	}
}
