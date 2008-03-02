/*
 * File : AllTests.java
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
 * Created on 30 déc. 2003
 */
package math.geom2d;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Legland
 */
public class geom2dTest {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests for package math.geom2d");
		//$JUnit-BEGIN$
		suite.addTest(math.geom2d.AllTests.suite());
		suite.addTest(math.geom2d.conic.AllTests.suite());
		suite.addTest(math.geom2d.curve.AllTests.suite());
		suite.addTest(math.geom2d.line.AllTests.suite());
		suite.addTest(math.geom2d.polygon.AllTests.suite());
		suite.addTest(math.geom2d.spline.AllTests.suite());
		suite.addTest(math.geom2d.transform.AllTests.suite());
		//$JUnit-END$
		return suite;
	}
}
