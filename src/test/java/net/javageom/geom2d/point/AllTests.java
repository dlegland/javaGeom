/**
 * File: 	AllTests.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 18 janv. 09
 */
package net.javageom.geom2d.point;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * @author dlegland
 *
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for math.geom2d.point");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(KDTree2DTest.class));
        suite.addTest(new TestSuite(PointArray2DTest.class));
        suite.addTest(new TestSuite(PointSets2DTest.class));
        //$JUnit-END$
        return suite;
    }

}
