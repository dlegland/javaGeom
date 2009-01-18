/**
 * File: 	geom3dTests.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 18 janv. 09
 */
package math.geom3d;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * @author dlegland
 *
 */
public class geom3dTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for math.geom3d");
        //$JUnit-BEGIN$
        suite.addTest(math.geom3d.AllTests.suite());
        suite.addTest(math.geom3d.line.AllTests.suite());
        suite.addTest(math.geom3d.plane.AllTests.suite());
        suite.addTest(math.geom3d.transform.AllTests.suite());
        //$JUnit-END$
        return suite;
    }

}
