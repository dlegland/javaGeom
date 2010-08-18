/**
 * 
 */

package math.geom2d.polygon;

import java.util.Collection;

import math.geom2d.Point2D;
import math.geom2d.domain.BoundaryPolyCurve2D;
import math.geom2d.domain.ContourArray2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.domain.GenericDomain2D;
import math.geom2d.domain.SmoothOrientedCurve2D;
import math.geom2d.line.LineSegment2D;

import com.seisw.util.geom.Poly;
import com.seisw.util.geom.PolyDefault;
import com.seisw.util.geom.PolySimple;

/**
 * @author dlegland
 */
public abstract class Polygon2DUtils {

    /**
     * Computes the winding number of the polygon. Algorithm adapted from
     * http://www.geometryalgorithms.com/Archive/algorithm_0103/algorithm_0103.htm
     * 
     * @param vertices the vertices of the polygon
     * @param point the reference point
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

            // TODO: should avoid create new objects, and use a dedicated method (CCW ?)
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
        ContourArray2D<BoundaryPolyCurve2D<SmoothOrientedCurve2D>> result = 
            new ContourArray2D<BoundaryPolyCurve2D<SmoothOrientedCurve2D>>();

        for (LinearRing2D ring : polygon.getBoundary())
            result.addCurve(Polyline2DUtils.createClosedParallel(ring, d));

        return new GenericDomain2D(result);
    }
    
    /**
     * Compute union of the two polygons. Uses the GPCJ library, developed by
     * Solution Engineering, Inc.
     */
    public final static Polygon2D union(Polygon2D polygon1, 
            Polygon2D polygon2) {
    	// convert to GPCJ data structures
    	Poly poly1 = convertToGpcjPolygon(polygon1);
    	Poly poly2 = convertToGpcjPolygon(polygon2);
    	
    	// compute union
    	Poly result = poly1.union(poly2);
    	
    	// convert result to javaGeom structure
    	return convertFromGpcjPolygon(result);
    }
    
    /**
     * Compute union of the two polygons. Uses the GPCJ library, developed by
     * Solution Engineering, Inc.
     */
    public final static Polygon2D intersection(Polygon2D polygon1, 
            Polygon2D polygon2) {
    	// convert to GPCJ data structures
    	Poly poly1 = convertToGpcjPolygon(polygon1);
    	Poly poly2 = convertToGpcjPolygon(polygon2);
    	
    	// compute union
    	Poly result = poly1.intersection(poly2);
    	
    	// convert result to javaGeom structure
    	return convertFromGpcjPolygon(result);
    }
    
    private final static Poly convertToGpcjPolygon(Polygon2D polygon) {
    	PolyDefault result = new PolyDefault();
    	for (LinearRing2D ring : polygon.getRings())
    		result.add(convertToGpcjSimplePolygon(ring));
    	return result;
    }
    
    private final static PolySimple convertToGpcjSimplePolygon(
    		LinearRing2D ring) {
    	PolySimple poly = new PolySimple();
    	for (Point2D point : ring.getVertices())
    		poly.add(point);
    	return poly;
    }
    
    private final static Polygon2D convertFromGpcjPolygon(Poly poly) {
    	int n = poly.getNumInnerPoly();
    	
    	// if the result is single, create a SimplePolygon
    	if (n==1) {
    		Point2D[] points = extractPolyVertices(poly.getInnerPoly(0));
    		return SimplePolygon2D.create(points);
    	}
    	
    	// extract the different rings of the resulting polygon
    	LinearRing2D[] rings = new LinearRing2D[n];
    	for (int i=0; i<n; i++) 
    		rings[i] = convertFromGpcjSimplePolygon(poly.getInnerPoly(i));
    	
    	// create a multiple polygon
    	return MultiPolygon2D.create(rings);
    }
    
    private final static LinearRing2D convertFromGpcjSimplePolygon(
    		Poly poly) {
    	return LinearRing2D.create(extractPolyVertices(poly));
    }
    
    private final static Point2D[] extractPolyVertices(Poly poly) {
    	int n = poly.getNumPoints();
    	Point2D[] points = new Point2D[n];
    	for (int i=0; i<n; i++)
    		points[i] = Point2D.create(poly.getX(i), poly.getY(i));
    	return points;
    }
}
