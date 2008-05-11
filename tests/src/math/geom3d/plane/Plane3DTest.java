/**
 * 
 */
package math.geom3d.plane;

import math.geom3d.Point3D;
import math.geom3d.Vector3D;
import math.geom3d.line.StraightLine3D;
import junit.framework.TestCase;

/**
 * @author dlegland
 *
 */
public class Plane3DTest extends TestCase {

	/**
	 * Test method for {@link math.geom3d.plane.Plane3D#getNormalVector()}.
	 */
	public void testGetNormalVector() {
		Plane3D plane 	= Plane3D.createXYPlane();
		Vector3D vz 	= new Vector3D(0, 0, -1);
		assertTrue(vz.equals(plane.getNormalVector()));
	}

	/**
	 * Test method for {@link math.geom3d.plane.Plane3D#getLineIntersection(math.geom3d.line.StraightLine3D)}.
	 */
	public void testGetLineIntersection() {
		Plane3D plane 	= Plane3D.createXYPlane();
		StraightLine3D line1 = new StraightLine3D(new Point3D(2, 2, 2), new Vector3D(0, 0, 1));
		Point3D p1 = new Point3D(2, 2, 0);
		
		Point3D pInt;
		pInt = plane.getLineIntersection(line1);
		
		assertTrue(p1.equals(pInt));
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
