/**
 * File: 	javaGeomTests.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 18 janv. 09
 */
package net.javageom;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * @author dlegland
 *
 */
public class javaGeomTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for math");
        //$JUnit-BEGIN$
        suite.addTest(net.javageom.geom2d.geom2dTests.suite());
        suite.addTest(net.javageom.geom3d.geom3dTests.suite());
        //$JUnit-END$
        return suite;
    }

}
