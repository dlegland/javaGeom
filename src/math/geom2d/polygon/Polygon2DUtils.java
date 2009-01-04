/**
 * 
 */

package math.geom2d.polygon;

import java.util.Collection;

import math.geom2d.Point2D;
import math.geom2d.domain.BoundaryPolyCurve2D;
import math.geom2d.domain.BoundarySet2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.domain.GenericDomain2D;
import math.geom2d.domain.SmoothOrientedCurve2D;
import math.geom2d.line.ClosedPolyline2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Polyline2DUtils;

/**
 * @author dlegland
 */
public abstract class Polygon2DUtils {

    /**
     * Computes the winding number of the polygon. Algorithm adapted from
     * http://www.geometryalgorithms.com/Archive/algorithm_0103/algorithm_0103.htm
     * 
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return the number of windings of the curve around the point
     */
    public final static int windingNumber(Collection<Point2D> vertices,
            java.awt.geom.Point2D point) {
        int wn = 0; // the winding number counter

        // Extract the last point of the collection
        Point2D previous = null;
        for (Point2D vertex : vertices)
            previous = vertex;
        double x1 = previous.getX();
        double y1 = previous.getY();
        double x2, y2;

        // Iterate on couple of vertices, starting from couple (last,first)
        double y = point.getY();
        for (Point2D p : vertices) {
            // second vertex of current edge
            x2 = p.getX();
            y2 = p.getY();

            // TODO: should avoid create new objects, and use a dedicated method
            if (y1<=y) {
                if (y2>y) // an upward crossing
                    if (new LineSegment2D(x1, y1, x2, y2).isInside(point))
                        wn++;
            } else {
                if (y2<=y) // a downward crossing
                    if (!(new LineSegment2D(x1, y1, x2, y2).isInside(point)))
                        wn--;
            }

            // for next iteration
            x1 = x2;
            y1 = y2;
        }

        return wn;
    }

    public final static Domain2D createBuffer(Polygon2D polygon, double d) {
        BoundarySet2D<BoundaryPolyCurve2D<SmoothOrientedCurve2D>> result = new BoundarySet2D<BoundaryPolyCurve2D<SmoothOrientedCurve2D>>();

        for (ClosedPolyline2D polyline : polygon.getBoundary())
            result.addCurve(Polyline2DUtils.createClosedParallel(polyline, d));

        return new GenericDomain2D(result);
    }
}
