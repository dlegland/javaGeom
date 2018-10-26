/* file : EllipseArc2DTest.java
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
 * Created on 7 mars 2007
 *
 */
package math.geom2d.conic;

import static java.lang.Math.PI;

import junit.framework.TestCase;
import math.geom2d.AffineTransform2D;
import math.geom2d.IShape2D;
import math.geom2d.Vector2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.polygon.Polyline2D;

public class EllipseArc2DTest extends TestCase {

    /**
     * Create ellipse arcs with various constructors, and test if they are equal between them.
     */
    public void testEllipseArc2D() {
        // direct ellipse
        EllipseArc2D arc0 = new EllipseArc2D(0, 0, 1, 1, 0, 0, PI / 2);
        Ellipse2D ellipse = new Ellipse2D(0, 0, 1, 1, 0, true);
        EllipseArc2D arc1 = new EllipseArc2D(ellipse, 0, Math.PI / 2);
        EllipseArc2D arc2 = new EllipseArc2D(ellipse, 0, Math.PI / 2, true);
        EllipseArc2D arc3 = new EllipseArc2D(0, 0, 1, 1, 0, 0, Math.PI / 2);
        EllipseArc2D arc4 = new EllipseArc2D(0, 0, 1, 1, 0, 0, Math.PI / 2, true);
        assertTrue(arc1.equals(arc0));
        assertTrue(arc1.equals(arc2));
        assertTrue(arc1.equals(arc3));
        assertTrue(arc1.equals(arc4));

        // direct ellipse, with different angles
        ellipse = new Ellipse2D(0, 0, 1, 1, 0, true);
        arc1 = new EllipseArc2D(ellipse, Math.PI / 2, Math.PI / 2);
        arc2 = new EllipseArc2D(ellipse, Math.PI / 2, Math.PI, true);
        arc3 = new EllipseArc2D(0, 0, 1, 1, 0, Math.PI / 2, Math.PI / 2);
        arc4 = new EllipseArc2D(0, 0, 1, 1, 0, Math.PI / 2, Math.PI, true);
        assertTrue(arc1.equals(arc2));
        assertTrue(arc1.equals(arc3));
        assertTrue(arc1.equals(arc4));

        // indirect ellipse, with different angles
        ellipse = new Ellipse2D(0, 0, 1, 1, 0, true);
        arc1 = new EllipseArc2D(ellipse, Math.PI / 2, -Math.PI / 2);
        arc2 = new EllipseArc2D(ellipse, Math.PI / 2, 2 * Math.PI, false);
        arc3 = new EllipseArc2D(0, 0, 1, 1, 0, Math.PI / 2, -Math.PI / 2);
        arc4 = new EllipseArc2D(0, 0, 1, 1, 0, Math.PI / 2, 2 * Math.PI, false);
        assertTrue(arc1.equals(arc2));
        assertTrue(arc1.equals(arc3));
        assertTrue(arc1.equals(arc4));
    }

    public void testTransformAffineTransform2D() {
        EllipseArc2D arc, transformed;
        double x0 = 0;
        double y0 = 0;
        double a = 20;
        double b = 10;

        double tx = 20;
        double ty = 10;
        double theta = Math.PI / 3;
        double sx = 3;
        double sy = 2;

        // Simple direct arc
        arc = new EllipseArc2D(x0, y0, a, b, 0, 0, Math.PI / 2);

        // translation
        AffineTransform2D tra = AffineTransform2D.createTranslation(tx, ty);
        assertTrue(arc.transform(tra).almostEquals(new EllipseArc2D(tx, ty, a, b, 0, 0, Math.PI / 2), IShape2D.ACCURACY));

        // rotation
        AffineTransform2D rot = AffineTransform2D.createRotation(theta);
        assertTrue(arc.transform(rot).almostEquals(new EllipseArc2D(x0, y0, a, b, theta, 0, Math.PI / 2), IShape2D.ACCURACY));

        // scaling with unequal factors
        AffineTransform2D sca = AffineTransform2D.createScaling(sx, sy);
        assertTrue(arc.transform(sca).almostEquals(new EllipseArc2D(x0, y0, a * sx, b * sy, 0, 0, Math.PI / 2), IShape2D.ACCURACY));

        // line reflections
        AffineTransform2D refOx = AffineTransform2D.createLineReflection(new StraightLine2D(0, 0, 1, 0));
        assertTrue(arc.transform(refOx).almostEquals(new EllipseArc2D(x0, y0, a, b, 0, 0, -Math.PI / 2), IShape2D.ACCURACY));
        AffineTransform2D refOy = AffineTransform2D.createLineReflection(new StraightLine2D(0, 0, 0, 1));
        assertTrue(arc.transform(refOy).almostEquals(new EllipseArc2D(x0, y0, a, b, 0, Math.PI, -Math.PI / 2), IShape2D.ACCURACY));

        // Rotated ellipse
        arc = new EllipseArc2D(x0, y0, a, b, Math.PI / 3, 0, Math.PI / 2);
        transformed = arc.transform(refOy);
        assertTrue(transformed.almostEquals(new EllipseArc2D(x0, y0, a, b, 2 * Math.PI / 3, 0, -Math.PI / 2), IShape2D.ACCURACY));

        // Rotated ellipse, from indirect ellipse
        Ellipse2D ell = new Ellipse2D(x0, y0, a, b, Math.PI / 3, true);
        Ellipse2D ell2 = new Ellipse2D(x0, y0, a, b, 2 * Math.PI / 3, false);
        arc = new EllipseArc2D(ell, 0, Math.PI / 2);
        transformed = arc.transform(refOy);
        assertTrue(transformed.almostEquals(new EllipseArc2D(ell2, 0, -Math.PI / 2), IShape2D.ACCURACY));
    }

    public void testAsPolyline() {
        Ellipse2D ellipse = new Ellipse2D(0, 0, 50, 20, 0);
        EllipseArc2D arc = new EllipseArc2D(ellipse, 0, PI / 2);
        Polyline2D poly = arc.asPolyline(4);
        assertEquals(5, poly.vertexNumber());
    }

    public void testGetTangent() {
        Ellipse2D ellipse = new Ellipse2D(0, 0, 50, 20, 0);
        double eps = IShape2D.ACCURACY;

        // Direct arc
        EllipseArc2D arc1 = new EllipseArc2D(ellipse, 0, Math.PI / 2);
        assertAlmostEquals(new Vector2D(0, 20), arc1.tangent(0), eps);
        assertAlmostEquals(new Vector2D(-50, 0), arc1.tangent(Math.PI / 2), eps);

        // Inverse arc
        EllipseArc2D arc2 = new EllipseArc2D(ellipse, 0, -Math.PI / 2);
        assertAlmostEquals(new Vector2D(0, -20), arc2.tangent(0), eps);
        assertAlmostEquals(new Vector2D(-50, 0), arc2.tangent(Math.PI / 2), eps);

    }

    private void assertAlmostEquals(Vector2D v1, Vector2D v2, double eps) {
        assertTrue(v1.almostEquals(v2, eps));
    }
}
