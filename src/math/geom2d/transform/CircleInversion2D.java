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

import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;

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
        this.center = circle.center();
        this.radius = circle.radius();
    }

    public CircleInversion2D(Point2D center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public CircleInversion2D(double xc, double yc, double radius) {
        this.center = new Point2D(xc, yc);
        this.radius = radius;
    }

    // ===================================================================
    // accessors

    public Point2D center() {
    	return center;
    }
    
    public double radius() {
    	return radius;
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
        
        double d = r*r/Point2D.distance(pt, center);
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

        xc = center.x();
        yc = center.y();
        r  = radius;

        // transform each point
        for (int i = 0; i<src.length; i++) {
            d = Point2D.distance(src[i].x(), src[i].y(), xc, yc);
            d = r*r/d;
            theta = Math.atan2(src[i].y()-yc, src[i].x()-xc);
            dst[i] = new Point2D(d*Math.cos(theta), d*Math.sin(theta));
        }

        return dst;
    }
}