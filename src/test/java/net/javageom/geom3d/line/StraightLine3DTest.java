/**
 * 
 */
package net.javageom.geom3d.line;

import junit.framework.TestCase;
import net.javageom.geom3d.Point3D;
import net.javageom.geom3d.Vector3D;

/**
 * @author dlegland
 *
 */
public class StraightLine3DTest extends TestCase {

	/**
	 * Test method for {@link net.javageom.geom3d.line.StraightLine3D#contains(net.javageom.geom3d.Point3D)}.
	 */
	public void testContains() {
		StraightLine3D line = new StraightLine3D(new Point3D(2, 3, 4), new Vector3D(1, 2, 3));
		Point3D p1 = new Point3D(1, 1, 1);
		assertTrue(line.contains(p1));
	}

	/**
	 * Test method for {@link net.javageom.geom3d.line.StraightLine3D#project(net.javageom.geom3d.Point3D)}.
	 */
	public void testProjectPoint3D() {
		StraightLine3D line = new StraightLine3D(new Point3D(2, 2, 2), new Vector3D(2, 0, 0));
		Point3D p1 = new Point3D(4, 2, 2);
		
		assertEquals(line.position(p1), 1, 1e-14);
	}

}
