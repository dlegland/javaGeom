/**
 * File: 	javaGeomTests.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 18 janv. 09
 */
package math;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author dlegland
 *
 */
public class javaGeomTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for math");
        // $JUnit-BEGIN$
        suite.addTest(math.geom2d.geom2dTests.suite());
        suite.addTest(math.geom3d.geom3dTests.suite());
        // $JUnit-END$
        return suite;
    }

}
