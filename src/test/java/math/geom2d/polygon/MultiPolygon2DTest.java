package math.geom2d.polygon;

import junit.framework.TestCase;

import java.util.*;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.domain.IDomain2D;
import math.geom2d.transform.CircleInversion2D;

public class MultiPolygon2DTest extends TestCase {

    public void testTransform_Inversion() {
        LinearRing2D ring1 = new LinearRing2D(new Point2D[] { new Point2D(10, 10), new Point2D(20, 10), new Point2D(20, 20), new Point2D(10, 20) });

        LinearRing2D ring2 = new LinearRing2D(new Point2D[] { new Point2D(30, 0), new Point2D(40, 0), new Point2D(40, 30), new Point2D(30, 30) });

        MultiPolygon2D polygon = new MultiPolygon2D(new LinearRing2D[] { ring1, ring2 });

        Point2D center = new Point2D(0, 0);
        Circle2D circle = new Circle2D(center, 5);
        CircleInversion2D inversion = new CircleInversion2D(circle);

        IDomain2D poly2 = polygon.transform(inversion);

        assertFalse(poly2.isEmpty());
        assertTrue(poly2.isBounded());
    }

    public void testCentroid() {
        LinearRing2D ring1 = new LinearRing2D(new Point2D[] { new Point2D(10, 10), new Point2D(20, 10), new Point2D(20, 20), new Point2D(10, 20) });

        LinearRing2D ring2 = new LinearRing2D(new Point2D[] { new Point2D(30, 0), new Point2D(40, 0), new Point2D(40, 30), new Point2D(30, 30) });

        MultiPolygon2D polygon = new MultiPolygon2D(new LinearRing2D[] { ring1, ring2 });

        Point2D centroid = polygon.centroid();
        Point2D expected = new Point2D(30, 15);

        assertTrue(expected.equals(centroid));
    }

    public void testArea() {
        LinearRing2D ring1 = new LinearRing2D(new Point2D[] { new Point2D(10, 10), new Point2D(20, 10), new Point2D(20, 20), new Point2D(10, 20) });

        LinearRing2D ring2 = new LinearRing2D(new Point2D[] { new Point2D(30, 0), new Point2D(40, 0), new Point2D(40, 30), new Point2D(30, 30) });

        MultiPolygon2D polygon = new MultiPolygon2D(new LinearRing2D[] { ring1, ring2 });

        double area = polygon.area();
        double expected = 400;

        assertEquals(expected, area);
    }

    public void testArea_WithHole() {
        // create ring in CCW orientation
        LinearRing2D ring1 = new LinearRing2D(new Point2D[] { new Point2D(10, 10), new Point2D(20, 10), new Point2D(20, 20), new Point2D(10, 20) });

        // create ring in CW orientation
        LinearRing2D ring2 = new LinearRing2D(new Point2D[] { new Point2D(13, 13), new Point2D(13, 17), new Point2D(17, 17), new Point2D(17, 13) });

        MultiPolygon2D polygon = new MultiPolygon2D(new LinearRing2D[] { ring1, ring2 });

        double area = polygon.area();
        double expected = 84;

        assertEquals(expected, area);
    }

    public void testVertices() {
        Point2D[] pts1 = new Point2D[] { new Point2D(0, 0), new Point2D(10, 0), new Point2D(10, 10), new Point2D(0, 10) };
        LinearRing2D ring1 = new LinearRing2D(pts1);

        Point2D[] pts2 = new Point2D[] { new Point2D(20, 0), new Point2D(30, 0), new Point2D(30, 10), new Point2D(20, 10) };
        LinearRing2D ring2 = new LinearRing2D(pts2);

        MultiPolygon2D polygon = new MultiPolygon2D(new LinearRing2D[] { ring1, ring2 });

        Collection<Point2D> vertices = polygon.vertices();
        assertEquals(pts1.length + pts2.length, vertices.size());

        for (int i = 0; i < pts1.length; i++)
            assertTrue(vertices.contains(pts1[i]));
        for (int i = 0; i < pts2.length; i++)
            assertTrue(vertices.contains(pts2[i]));
    }

    public void testVertexNumber() {
        Point2D[] pts1 = new Point2D[] { new Point2D(0, 0), new Point2D(10, 0), new Point2D(10, 10), new Point2D(0, 10) };
        LinearRing2D ring1 = new LinearRing2D(pts1);

        Point2D[] pts2 = new Point2D[] { new Point2D(20, 0), new Point2D(30, 0), new Point2D(30, 10), new Point2D(20, 10) };
        LinearRing2D ring2 = new LinearRing2D(pts2);

        MultiPolygon2D polygon = new MultiPolygon2D(new LinearRing2D[] { ring1, ring2 });

        assertEquals(8, polygon.vertexNumber());
    }

    public void testVertex() {
        LinearRing2D tri1 = new LinearRing2D(new Point2D[] { new Point2D(0, 0), new Point2D(10, 0), new Point2D(10, 10) });
        LinearRing2D tri2 = new LinearRing2D(new Point2D[] { new Point2D(20, 0), new Point2D(30, 0), new Point2D(30, 10) });

        MultiPolygon2D poly = new MultiPolygon2D(new LinearRing2D[] { tri1, tri2 });

        assertEquals(new Point2D(0, 0), poly.vertex(0));
        assertEquals(new Point2D(10, 0), poly.vertex(1));
        assertEquals(new Point2D(10, 10), poly.vertex(2));
        assertEquals(new Point2D(20, 0), poly.vertex(3));
        assertEquals(new Point2D(30, 0), poly.vertex(4));
        assertEquals(new Point2D(30, 10), poly.vertex(5));
    }

    public void testComplement() {
        // create ring in CCW orientation
        LinearRing2D ring1 = new LinearRing2D(new Point2D[] { new Point2D(10, 10), new Point2D(20, 10), new Point2D(20, 20), new Point2D(10, 20) });

        // create ring in CW orientation
        LinearRing2D ring2 = new LinearRing2D(new Point2D[] { new Point2D(13, 13), new Point2D(13, 17), new Point2D(17, 17), new Point2D(17, 13) });

        MultiPolygon2D polygon = new MultiPolygon2D(new LinearRing2D[] { ring1, ring2 });

        IPolygon2D complement = polygon.complement();

        assertEquals(2, complement.contours().size());

        Point2D pIn1 = new Point2D(14, 16);
        assertTrue(complement.contains(pIn1));

        Point2D pIn2 = new Point2D(21, 20);
        assertTrue(complement.contains(pIn2));

        Point2D pOut = new Point2D(11, 19);
        assertFalse(complement.contains(pOut));

    }

    public void testBoundingBox2D() {
        Point2D[] pts1 = new Point2D[] { new Point2D(0, 0), new Point2D(10, 0), new Point2D(10, 10), new Point2D(0, 10) };
        LinearRing2D ring1 = new LinearRing2D(pts1);

        Point2D[] pts2 = new Point2D[] { new Point2D(20, 10), new Point2D(30, 10), new Point2D(30, 20), new Point2D(20, 20) };
        LinearRing2D ring2 = new LinearRing2D(pts2);

        MultiPolygon2D polygon = new MultiPolygon2D();
        polygon.addRing(ring1);
        polygon.addRing(ring2);

        Box2D expBox = new Box2D(0, 30, 0, 20);
        Box2D bbox = polygon.boundingBox();
        assertTrue(expBox.almostEquals(bbox, 1e-14));
    }

    public void testContainsPoint2D() {
        Point2D[] pts1 = new Point2D[] { new Point2D(0, 0), new Point2D(10, 0), new Point2D(10, 10), new Point2D(0, 10) };
        LinearRing2D ring1 = new LinearRing2D(pts1);

        Point2D[] pts2 = new Point2D[] { new Point2D(20, 0), new Point2D(30, 0), new Point2D(30, 10), new Point2D(20, 10) };
        LinearRing2D ring2 = new LinearRing2D(pts2);

        MultiPolygon2D polygon = new MultiPolygon2D();
        polygon.addRing(ring1);
        polygon.addRing(ring2);

        assertTrue(polygon.contains(new Point2D(5, 5)));
        assertTrue(polygon.contains(new Point2D(25, 5)));
        assertFalse(polygon.contains(new Point2D(15, 5)));
    }

    public void testContainsPoint2D_WithHole() {
        // create ring in CCW orientation
        LinearRing2D ring1 = new LinearRing2D(new Point2D[] { new Point2D(10, 10), new Point2D(20, 10), new Point2D(20, 20), new Point2D(10, 20) });

        // create ring in CW orientation
        LinearRing2D ring2 = new LinearRing2D(new Point2D[] { new Point2D(13, 13), new Point2D(13, 17), new Point2D(17, 17), new Point2D(17, 13) });

        MultiPolygon2D polygon = new MultiPolygon2D(new LinearRing2D[] { ring1, ring2 });

        Point2D pOut1 = new Point2D(14, 16);
        assertFalse(polygon.contains(pOut1));

        Point2D pOut2 = new Point2D(21, 20);
        assertFalse(polygon.contains(pOut2));

        Point2D pIn = new Point2D(11, 19);
        assertTrue(polygon.contains(pIn));

    }

    public void testCopyConstructor() {
        Point2D[] pts1 = new Point2D[] { new Point2D(0, 0), new Point2D(10, 0), new Point2D(10, 10), new Point2D(0, 10) };
        LinearRing2D ring1 = new LinearRing2D(pts1);

        Point2D[] pts2 = new Point2D[] { new Point2D(20, 0), new Point2D(30, 0), new Point2D(30, 10), new Point2D(20, 10) };
        LinearRing2D ring2 = new LinearRing2D(pts2);

        MultiPolygon2D polygon = new MultiPolygon2D();
        polygon.addRing(ring1);
        polygon.addRing(ring2);

        MultiPolygon2D copy = new MultiPolygon2D(polygon);
        assertTrue(polygon.equals(copy));
    }
}
