/*
 * File : Rectangle2DTest.java
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
 * Created on 31 déc. 2003
 */
package math.geom2d.polygon;

import junit.framework.TestCase;
import math.geom2d.Point2D;
import math.geom2d.IShape2D;

/**
 * @author Legland
 */
public class Rectangle2DTest extends TestCase {

    /**
     * Constructor for Rectangle2DTest.
     * 
     * @param arg0
     */
    public Rectangle2DTest(String arg0) {
        super(arg0);
    }

    public void testCentroid() {
        Rectangle2D rect = new Rectangle2D(10, 20, 30, 40);
        Point2D exp = new Point2D(25, 40);
        Point2D center = rect.centroid();
        assertTrue(exp.almostEquals(center, IShape2D.ACCURACY));
    }

    public void testVertexNumber() {
        Rectangle2D rect = new Rectangle2D(10, 20, 30, 40);
        assertEquals(rect.vertexNumber(), 4);
    }

    public void testDistance() {
        Rectangle2D rect = new Rectangle2D(10, 20, 30, 40);
        assertEquals(rect.distance(10, 20), 0, IShape2D.ACCURACY);
        assertEquals(rect.distance(40, 20), 0, IShape2D.ACCURACY);
        assertEquals(rect.distance(40, 60), 0, IShape2D.ACCURACY);
        assertEquals(rect.distance(10, 60), 0, IShape2D.ACCURACY);
    }

    /*
     * Test for boolean equals(Rectangle2D)
     */
    public void testEquals_Rectangle2D() {
        Rectangle2D rect1 = new Rectangle2D(10, 20, 30, 40);
        assertTrue(rect1.equals(rect1));
        Rectangle2D rect2 = new Rectangle2D(10, 20, 30, 40);
        assertTrue(rect1.equals(rect2));
        Rectangle2D rect3 = new Rectangle2D(10, 20, 30, 30);
        assertFalse(rect1.equals(rect3));
    }
}
