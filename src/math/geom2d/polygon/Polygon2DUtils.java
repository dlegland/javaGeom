/**
 * 
 */

package math.geom2d.polygon;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.domain.*;

import com.seisw.util.geom.Poly;
import com.seisw.util.geom.PolyDefault;
import com.seisw.util.geom.PolySimple;

/**
 * @author dlegland
 */
public final class Polygon2DUtils {

	/**
	 * Creates a new polygon representing a rectangle centered around a point. 
	 * Rectangle sides are parallel to the main axes. The function returns an
	 * instance of SimplePolygon2D.
	 * @since 0.9.1 
	 */
	public final static SimplePolygon2D createCenteredRectangle(Point2D center, 
			double length, double width) {
		// extract rectangle parameters
		double xc = center.getX();
		double yc = center.getY();
		double len = length / 2;
		double wid = width / 2;
		
		// coordinates of corners
		double x1 = xc - len;
		double y1 = yc - wid;
		double x2 = xc + len;
		double y2 = yc + wid;

		// create result polygon
		return new SimplePolygon2D(new Point2D[]{
				new Point2D(x1, y1),
				new Point2D(x2, y1),
				new Point2D(x2, y2),
				new Point2D(x1, y2),
		});
	}
	
	/**
	 * Creates a new polygon representing an oriented rectangle centered
	 * around a point. 
	 * The function returns an instance of SimplePolygon2D. 
	 * @since 0.9.1 
	 */
	public final static SimplePolygon2D createOrientedRectangle(Point2D center, 
			double length, double width, double theta) {
		// extract rectangle parameters
		double xc = center.getX();
		double yc = center.getY();
		double len = length / 2;
		double wid = width / 2;
		
		// Pre-compute angle quantities
		double cot = Math.cos(theta);
		double sit = Math.sin(theta);
		
		// Create resulting rotated rectangle
		return new SimplePolygon2D(new Point2D[]{
				new Point2D(-len*cot + wid*sit + xc, -len*sit - wid*cot + yc),
				new Point2D( len*cot + wid*sit + xc,  len*sit - wid*cot + yc),
				new Point2D( len*cot - wid*sit + xc,  len*sit + wid*cot + yc),
				new Point2D(-len*cot - wid*sit + xc, -len*sit + wid*cot + yc),
		});
	}

	/**
     * Computes the winding number of the polygon. Algorithm adapted from
     * http://www.geometryalgorithms.com/Archive/algorithm_0103/algorithm_0103.htm
     * http://softsurfer.com/Archive/algorithm_0103/algorithm_0103.htm
     * @param vertices the vertices of a polygon
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
        double y1 = previous.getY();
        double y2;

        // keep y-coordinate of test point
        double y = point.getY();

        // Iterate on couple of vertices, starting from couple (last,first)
        for (Point2D current : vertices) {
            // second vertex of current edge
            y2 = current.getY();
            
            if (y1<=y) {
                if (y2>y) // an upward crossing
                    if (isLeft(previous, current, point)>0)
                        wn++;
            } else {
                if (y2<=y) // a downward crossing
                    if (isLeft(previous, current, point)<0)
                        wn--;
            }

            // for next iteration
            y1 = y2;
            previous = current;
        }

        return wn;
    }

    /**
     * Tests if a point is Left|On|Right of an infinite line.
     * Input:  three points P0, P1, and P2
     * Return: >0 for P2 left of the line through P0 and P1
     *         =0 for P2 on the line
     *         <0 for P2 right of the line
     * See: the January 2001 Algorithm "Area of 2D and 3D Triangles and Polygons"
     */
    private final static int isLeft(java.awt.geom.Point2D p1, 
    		java.awt.geom.Point2D p2, java.awt.geom.Point2D pt) {
    	double x = p1.getX();
    	double y = p1.getY();
    	return (int) Math.signum(
    			(p2.getX() - x) * (pt.getY() - y) - 
    			(pt.getX() - x) * (p2.getY() - y));
    }
    
    /**
     * Computes the buffer at a distance d of the input polygon. The result is
     * a domain whose boundary is composed of line segments and circle arcs.  
     * @see Polygon.getBuffer(double)
     */
    public final static Domain2D createBuffer(Polygon2D polygon, double d) {
        ContourArray2D<BoundaryPolyCurve2D<SmoothOrientedCurve2D>> result = 
            new ContourArray2D<BoundaryPolyCurve2D<SmoothOrientedCurve2D>>();

        for (LinearRing2D ring : polygon.getBoundary())
            result.addCurve(Polyline2DUtils.createClosedParallel(ring, d));

        return new GenericDomain2D(result);
    }
    
    /**
     * Clips a polygon by a box. The result is a new polygon, that can be
     * multiple.
     * @see Polygon.clip(Box2D)
     */
    public final static Polygon2D clipPolygon(Polygon2D polygon, Box2D box) {
    	// Clip the boundary using generic method
    	Boundary2D boundary = polygon.getBoundary();
        ContourArray2D<Contour2D> contours = 
            Boundary2DUtils.clipBoundary(boundary, box);

        // convert boundaries to linear rings
        ArrayList<LinearRing2D> rings = new ArrayList<LinearRing2D>();
        for(Contour2D contour : contours)
        	rings.add(convertContourToLinearRing(contour));
        
        // Create a polygon, either simple or multiple, depending on the ring
        // number
        if (rings.size()==1)
        	return SimplePolygon2D.create(rings.get(0).getVertices());
        else
        	return MultiPolygon2D.create(rings);
    }
    
    private final static LinearRing2D convertContourToLinearRing(
    		Contour2D contour) {
    	// process the basic case of simple class cast
    	if (contour instanceof LinearRing2D)
    		return (LinearRing2D) contour;
    	
    	// extract all vertices of the contour
    	// TODO: check that vertices are not multiple
    	ArrayList<Point2D> vertices = new ArrayList<Point2D>();
    	for(Point2D v : contour.getSingularPoints())
    		vertices.add(v);
    	return LinearRing2D.create(vertices);
    }
    
    /**
     * Computes the union of the two polygons. Uses the GPCJ library, developed by
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
     * Computes the intersection of the two polygons. Uses the GPCJ library, 
     * developed by Solution Engineering, Inc.
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
    
    /**
     * Computes the exclusive XOR of the two polygons. Uses the GPCJ library, 
     * developed by Solution Engineering, Inc.
     */
    public final static Polygon2D exclusiveOr(Polygon2D polygon1, 
    		Polygon2D polygon2) {
    	// convert to GPCJ data structures
    	Poly poly1 = convertToGpcjPolygon(polygon1);
    	Poly poly2 = convertToGpcjPolygon(polygon2);
    	
    	// compute union
    	Poly result = poly1.xor(poly2);
    	
    	// convert result to javaGeom structure
    	return convertFromGpcjPolygon(result);
    }
    
    /**
     * Computes the Difference of the two polygons. Uses the modified GPCJ library, 
     * developed by Solution Engineering, Inc.
     */
    public final static Polygon2D difference(Polygon2D polygon1, 
    		Polygon2D polygon2) {
    	// convert to GPCJ data structures
    	Poly poly1 = convertToGpcjPolygon(polygon1);
    	Poly poly2 = convertToGpcjPolygon(polygon2);
    	
    	// compute union
    	Poly result = poly1.difference(poly2);
    	
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
