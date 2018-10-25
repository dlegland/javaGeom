/**
 * File: 	DomainArray2DTest.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 17 août 10
 */
package math.geom2d.domain;

import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import junit.framework.TestCase;

/**
 * @author dlegland
 *
 */
public class DomainArray2DTest extends TestCase {

    /**
     * Test method for {@link math.geom2d.ShapeArray2D#contains(double, double)}.
     */
    public void testContainsDoubleDouble() {
        Circle2D circle1 = new Circle2D(10, 50, 20);
        Circle2D circle2 = new Circle2D(80, 50, 20);
        IDomain2D disk1 = new GenericDomain2D(circle1);
        IDomain2D disk2 = new GenericDomain2D(circle2);
        IDomain2D[] array = new IDomain2D[] { disk1, disk2 };
        DomainArray2D<IDomain2D> domain = DomainArray2D.create(array);

        Point2D pointInside1 = new Point2D(10, 50);
        Point2D pointInside2 = new Point2D(80, 50);
        assertTrue(domain.contains(pointInside1));
        assertTrue(domain.contains(pointInside2));

    }

}
