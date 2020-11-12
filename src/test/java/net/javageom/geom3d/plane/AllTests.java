/**
 * File: 	AllTests.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 18 janv. 09
 */
package net.javageom.geom3d.plane;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * @author dlegland
 *
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for math.geom3d.plane");
        //$JUnit-BEGIN$
        suite.addTestSuite(Plane3DTest.class);
        //$JUnit-END$
        return suite;
    }

}
