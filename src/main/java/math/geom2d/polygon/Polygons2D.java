/**
 * 
 */

package math.geom2d.polygon;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.circulinear.CirculinearDomain2D;
import math.geom2d.circulinear.buffer.BufferCalculator;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Boundaries2D;
import math.geom2d.domain.Contour2D;
import math.geom2d.domain.ContourArray2D;
import math.geom2d.point.PointSets2D;
import math.geom2d.polygon.convhull.JarvisMarch2D;

import com.seisw.util.geom.Poly;
import com.seisw.util.geom.PolyDefault;
import com.seisw.util.geom.PolySimple;

/**
 * Several utility functions for working on polygons, including polygon
 * creation, and basic computations such as polygon area or centroid.  
 * 
 * @author dlegland
 */
public final class Polygons2D {

	/**
	 * Creates a new polygon representing a rectangle with edges parallel to
	 * the main directions, and having the two specified opposite corners.
	 * @since 0.10.3
	 */
	public final static SimplePolygon2D createRectangle(Point2D p1, Point2D p2) {
		// corners coordinates
    	double x1 = p1.getX();
    	double y1 = p1.getY();
    	double x2 = p2.getX();
    	double y2 = p2.getY();

    	return createRectangle(x1, y1, x2, y2);
	}

	/**
	 * Creates a new polygon representing a rectangle with edges parallel to
	 * the main directions, and having the two specified opposite corners.
	 * @since 0.10.3
	 */
	public final static SimplePolygon2D createRectangle(double x1, double y1, 
			double x2, double y2) {
		// extremes coordinates
        double xmin = min(x1, x2);
        double xmax = max(x1, x2);
        double ymin = min(y1, y2);
        double ymax = max(y1, y2);
 
		// create result polygon
		return new SimplePolygon2D(
				new Point2D(xmin, ymin),
				new Point2D(xmax, ymin),
				new Point2D(xmax, ymax),
				new Point2D(xmin, ymax)	);
	}

	/**
	 * Creates a new polygon representing a rectangle centered around a point. 
	 * Rectangle sides are parallel to the main axes. The function returns an
	 * instance of SimplePolygon2D.
	 * @since 0.9.1 
	 */
	public final static SimplePolygon2D createCenteredRectangle(Point2D center, 
			double length, double width) {
		// extract rectangle parameters
		double xc = center.x();
		double yc = center.y();
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
		double xc = center.x();
		double yc = center.y();
		double len = length / 2;
		double wid = width / 2;
		
		// Pre-compute angle quantities
		double cot = cos(theta);
		double sit = sin(theta);
		
		// Create resulting rotated rectangle
		return new SimplePolygon2D(new Point2D[]{
				new Point2D(-len*cot + wid*sit + xc, -len*sit - wid*cot + yc),
				new Point2D( len*cot + wid*sit + xc,  len*sit - wid*cot + yc),
				new Point2D( len*cot - wid*sit + xc,  len*sit + wid*cot + yc),
				new Point2D(-len*cot - wid*sit + xc, -len*sit + wid*cot + yc),
		});
	}
	
	/**
	 * Computes the centroid of the given polygon.
	 * @since 0.9.1
	 */
	public final static Point2D computeCentroid(Polygon2D polygon) {
		// process case of simple polygon 
    	if (polygon instanceof SimplePolygon2D) {
    		LinearRing2D ring = ((SimplePolygon2D) polygon).getRing();
    		return computeCentroid(ring);
    	}
    	
    	double xc = 0;
    	double yc = 0;
    	double area;
    	double cumArea = 0;
    	Point2D centroid;
    	
    	for (LinearRing2D ring : polygon.contours()) {
    		area = computeArea(ring);
    		centroid = computeCentroid(ring);
    		xc += centroid.x() * area;
    		yc += centroid.y() * area;
    		cumArea += area;
    	}
    	
    	xc /= cumArea;
    	yc /= cumArea;
    	return new Point2D(xc, yc);
	}
	
	/**
	 * Computes the centroid of the given linear ring.
	 * @since 0.9.1
	 */
	public final static Point2D computeCentroid(LinearRing2D ring) {
        double xc = 0;
        double yc = 0;
        
        double x, y;
        double xp, yp;
        double tmp = 0;
        
        // number of vertices
        int n = ring.vertexNumber();
       
        // initialize with the last vertex
        Point2D prev = ring.vertex(n-1);
        xp = prev.x();
        yp = prev.y();

        // iterate on vertices
        for (Point2D point : ring.vertices()) {
        	x = point.x();
        	y = point.y();
        	tmp = xp * y - yp * x;
            xc += (x + xp) * tmp;
            yc += (y + yp) * tmp;
            
            prev = point;
            xp = x;
            yp = y;
        }
        
        double denom = computeArea(ring) * 6;
        return new Point2D(xc / denom, yc / denom);
	}

	
    /**
     * Computes the signed area of the polygon. Algorithm is taken from page: <a
     * href="http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/">
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/</a>. Signed area
     * is positive if polygon is oriented counter-clockwise, and negative
     * otherwise. Result is wrong if polygon is self-intersecting.
     * 
     * @return the signed area of the polygon.
	 * @since 0.9.1
     */
    public final static double computeArea(Polygon2D polygon) {
    	double area = 0;
    	for (LinearRing2D ring : polygon.contours()) {
    		area += computeArea(ring);
    	}
    	return area;
    }

    /**
     * Computes the signed area of the linear ring. Algorithm is taken from page: <a
     * href="http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/">
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/</a>. Signed area
     * is positive if linear ring is oriented counter-clockwise, and negative
     * otherwise. Result is wrong if linear ring is self-intersecting.
     * 
     * @return the signed area of the polygon.
	 * @since 0.9.1
     */
    public final static double computeArea(LinearRing2D ring) {
        double area = 0;
        
        // number of vertices
        int n = ring.vertexNumber();
       
        // initialize with the last vertex
        Point2D prev = ring.vertex(n-1);
        
        // iterate on edges
        for (Point2D point : ring.vertices()) {
            area += prev.x() * point.y() - prev.y() * point.x();
            prev = point;
        }
        
        return area /= 2;
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
            Point2D point) {
        int wn = 0; // the winding number counter

        // Extract the last point of the collection
        Point2D previous = null;
        for (Point2D vertex : vertices)
            previous = vertex;
        double y1 = previous.y();
        double y2;

        // keep y-coordinate of test point
        double y = point.y();

        // Iterate on couple of vertices, starting from couple (last,first)
        for (Point2D current : vertices) {
            // second vertex of current edge
            y2 = current.y();
            
			if (y1 <= y) {
				if (y2 > y) // an upward crossing
					if (isLeft(previous, current, point) > 0)
						wn++;
			} else {
				if (y2 <= y) // a downward crossing
					if (isLeft(previous, current, point) < 0)
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
    private final static int isLeft(Point2D p1, Point2D p2, Point2D pt) {
    	double x = p1.x();
    	double y = p1.y();
    	return (int) Math.signum(
    			(p2.x() - x) * (pt.y() - y) - (pt.x() - x) * (p2.y() - y));
    }
    
	/**
	 * Returns the convex hull of the given set of points. Uses the Jarvis March
	 * algorithm.
	 * 
	 * @param points
	 *            a collection of points
	 * @return the convex hull of the set of points
	 */
    public final static Polygon2D convexHull(Collection<? extends Point2D> points) {
    	return new JarvisMarch2D().convexHull(points);
    }

    /**
     * Computes the buffer at a distance d of the input polygon. The result is
     * a domain whose boundary is composed of line segments and circle arcs.  
     * @see Polygon2D#buffer(double)
     */
    public final static CirculinearDomain2D createBuffer(Polygon2D polygon, 
    		double dist) {
    	// get current instance of buffer calculator
        BufferCalculator bc = BufferCalculator.getDefaultInstance();
        
        // compute buffer
        return bc.computeBuffer(polygon.boundary(), dist);
    }
    
    /**
     * Clips a polygon by a box. The result is a new polygon, that can be
     * multiple.
     * @see Polygon2D#clip(Box2D)
     */
    public final static Polygon2D clipPolygon(Polygon2D polygon, Box2D box) {
    	// Clip the boundary using generic method
    	Boundary2D boundary = polygon.boundary();
        ContourArray2D<Contour2D> contours = 
            Boundaries2D.clipBoundary(boundary, box);

        // convert boundaries to linear rings
        ArrayList<LinearRing2D> rings = new ArrayList<LinearRing2D>();
        for(Contour2D contour : contours)
        	rings.add(convertContourToLinearRing(contour));
        
        // Create a polygon, either simple or multiple, depending on the ring
        // number
        if (rings.size() == 1)
        	return SimplePolygon2D.create(rings.get(0).vertices());
        else
        	return MultiPolygon2D.create(rings);
    }
    
    private final static LinearRing2D convertContourToLinearRing(
    		Contour2D contour) {
    	// process the basic case of simple class cast
    	if (contour instanceof LinearRing2D)
    		return (LinearRing2D) contour;
    	
    	// extract all vertices of the contour
    	List<Point2D> vertices = new ArrayList<Point2D>();
    	for(Point2D v : contour.singularPoints())
    		vertices.add(v);

    	// remove adjacent multiple vertices
    	vertices = PointSets2D.filterMultipleVertices(vertices, true);
 	
    	// Create new ring with vertices
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
     * @since 0.9.1
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
    	for (LinearRing2D ring : polygon.contours())
    		result.add(convertToGpcjSimplePolygon(ring));
    	return result;
    }
    
    private final static PolySimple convertToGpcjSimplePolygon(
    		LinearRing2D ring) {
    	PolySimple poly = new PolySimple();
    	for (Point2D point : ring.vertices())
    		poly.add(new com.seisw.util.geom.Point2D(point.x(), point.y()));
    	return poly;
    }
    
    
    private final static Polygon2D convertFromGpcjPolygon(Poly poly) {
    	int n = poly.getNumInnerPoly();
    	
    	// if the result is single, create a SimplePolygon
    	if (n == 1) {
    		Point2D[] points = extractPolyVertices(poly.getInnerPoly(0));
    		return SimplePolygon2D.create(points);
    	}
    	
    	// extract the different rings of the resulting polygon
    	LinearRing2D[] rings = new LinearRing2D[n];
    	for (int i = 0; i < n; i++) 
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
    	for (int i = 0; i < n; i++)
    		points[i] = new Point2D(poly.getX(i), poly.getY(i));
    	return points;
    }
}
