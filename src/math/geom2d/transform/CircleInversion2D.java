/* File CircleInversion2D.java 
 *
 * Project : Java Geometry Library
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
 */

// package

package math.geom2d.transform;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.circulinear.CirculinearCurve2D;
import math.geom2d.circulinear.CirculinearRing2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.domain.BoundaryPolyCurve2D;
import math.geom2d.domain.ContourArray2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.LinearRing2D;
import math.geom2d.polygon.Polygon2D;

// Imports

/**
 * circle inversion : performs a bijection between points outside the circle and
 * points inside the circle.
 */
public class CircleInversion2D implements Bijection2D {

    // ===================================================================
    // static constructors

	public static CircleInversion2D create(Point2D center, double radius) {
		return new CircleInversion2D(center, radius);
	}
	
	public static CircleInversion2D create(Circle2D circle) {
		return new CircleInversion2D(circle);
	}
	
    // ===================================================================
    // class variables
	
    protected Point2D center;
    protected double radius;

    // ===================================================================
    // constructors

    /**
     * Construct a new circle inversion based on the unit circle centered on the
     * origin.
     */
    public CircleInversion2D() {
        this.center = new Point2D();
        this.radius = 1;
    }

    public CircleInversion2D(Circle2D circle) {
        this.center = circle.center().clone();
        this.radius = circle.radius();
    }

    public CircleInversion2D(Point2D center, double radius) {
        this.center = center.clone();
        this.radius = radius;
    }

    public CircleInversion2D(double xc, double yc, double radius) {
        this.center = new Point2D(xc, yc);
        this.radius = radius;
    }

    // ===================================================================
    // accessors

    public Point2D getCenter() {
    	return center;
    }
    
    public double getRadius() {
    	return radius;
    }
   
    /**
     * @deprecated create a new CircleInversion instead (0.9.0)
     */
    @Deprecated
    public void setCircle(double xc, double yc, double r) {
        this.center = new Point2D(xc, yc);
        this.radius = r;
    }

    /**
     * @deprecated create a new CircleInversion instead (0.9.0)
     */
    @Deprecated
    public void setCircle(Circle2D circle) {
        this.center = circle.center().clone();
        this.radius = circle.radius();
    }

    // ===================================================================
    // methods specific to class

    /**
     * Transforms a general shape, and return the transformed shape.
     * <p>
     * Transformed shape can be computed for different cases:
     * <ul>
     * <li>Point2D is transformed into another Point2D</li>
     * <li>LinearShape2D is transformed into a CircleArc2D or a Circle2D</li>
     * <li>Circle2D is transformed into another Circle2D</li>
     * <li>Polyline2D is transformed into a continuous set of circle arcs</li>
     * </ul>
     * @deprecated replaced by CirculinearShape2D interface (0.9.0)
     */
    @Deprecated
    public Shape2D transformShape(Shape2D shape) {

    	if (shape instanceof Point2D) {
        	return transform((Point2D) shape);
        } else if (shape instanceof CirculinearCurve2D) {
        	CirculinearCurve2D curve = (CirculinearCurve2D) shape;
        	return curve.transform(this);

        } else if (shape instanceof Polygon2D) {
            // get all rings of polygon
            Collection<? extends LinearRing2D> rings = ((Polygon2D) shape).rings();

            // for each ring, create a curve formed by several circle arcs
            ArrayList<CirculinearRing2D> curves = 
                new ArrayList<CirculinearRing2D>(rings.size());    
            for (LinearRing2D ring : rings)
                curves.add(ring.transform(this));

            // create new shape by putting all boundaries together
            return new ContourArray2D<CirculinearRing2D>(curves);
        }

        return null;
    }
    
   /**
    * @deprecated replaced by CirculinearShape2D interface (0.9.0)
    */
    @Deprecated
    public BoundaryPolyCurve2D<CircleArc2D> transformRing(LinearRing2D ring) {    
        // get all edges of the ring
        Collection<LineSegment2D> edges = ring.edges();

        // transform each edge into a circle arc
        ArrayList<CircleArc2D> arcs = new ArrayList<CircleArc2D>();
        for (LineSegment2D edge : edges)
            arcs.add((CircleArc2D) this.transformShape(edge));

        // create new shape by putting all arcs together
        return new BoundaryPolyCurve2D<CircleArc2D>(arcs);
    }
    
    // ===================================================================
    // methods implementing the Bijection2D interface
    
   /**
    * Returns this circle inversion.
    */
    public CircleInversion2D invert() {
    	return this;
    }

    // ===================================================================
    // methods implementing the Transform2D interface

    public Point2D transform(Point2D pt) {
    	double r = radius;
        
        double d = r*r/Point2D.getDistance(pt, center);
        double theta = Angle2D.horizontalAngle(center, pt);
        return Point2D.createPolar(center, d, theta);
    }

    /** Transforms an array of points, and returns the transformed points. */
    public Point2D[] transform(Point2D[] src, Point2D[] dst) {

        double d, theta;
        double xc, yc, r;

        // create the array if necessary
        if (dst==null)
            dst = new Point2D[src.length];

        // create instances of Points if necessary
        if (dst[0]==null)
            for (int i = 0; i<dst.length; i++)
                dst[i] = new Point2D();

        xc = center.getX();
        yc = center.getY();
        r  = radius;

        // transform each point
        for (int i = 0; i<src.length; i++) {
            d = Point2D.getDistance(src[i].getX(), src[i].getY(), xc, yc);
            d = r*r/d;
            theta = Math.atan2(src[i].getY()-yc, src[i].getX()-xc);
            dst[i] = new Point2D(d*Math.cos(theta), d*Math.sin(theta));
        }

        return dst;
    }
}