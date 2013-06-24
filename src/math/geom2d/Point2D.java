/* File Point2D.java 
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

package math.geom2d;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import math.geom2d.circulinear.CirculinearDomain2D;
import math.geom2d.circulinear.CirculinearShape2D;
import math.geom2d.circulinear.GenericCirculinearDomain2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.point.PointArray2D;
import math.geom2d.point.PointSet2D;
import math.geom2d.point.PointShape2D;
import math.geom2d.transform.CircleInversion2D;
import math.utils.EqualUtils;

import static java.lang.Math.*;


/**
 * <p>
 * A point in the plane defined by its 2 Cartesian coordinates x and y. The
 * class provides static methods to compute distance between two points.
 * </p>
 */
public class Point2D 
implements GeometricObject2D, PointShape2D, Cloneable, CirculinearShape2D {

	// ===================================================================
	// class variables
	
	/** The x coordinate of this point.*/
	protected double x;
	
	/** The y coordinate of this point.*/
	protected double y;
	

	// ===================================================================
	// static methods

	/**
	 * Static factory for creating a new point in cartesian coordinates.
	 * 
	 * @deprecated since 0.11.1
	 */
	@Deprecated
	public static Point2D create(double x, double y) {
		return new Point2D(x, y);
	}

	/**
	 * Static factory for creating a new point from an existing instance of java
	 * point.
	 * 
	 * @deprecated since 0.11.1
	 */
	@Deprecated
	public static Point2D create(java.awt.geom.Point2D point) {
		return new Point2D(point.getX(), point.getY());
	}

	/**
	 * Static factory for creating a new point from an existing instance of 
	 * javageom  point.
	 * 
	 * @since 0.10.0
	 */
	public static Point2D create(Point2D point) {
		return new Point2D(point.x, point.y);
	}

	/**
	 * Creates a new point from polar coordinates <code>rho</code> and
	 * <code>theta</code>.
	 */
	public static Point2D createPolar(double rho, double theta) {
		return new Point2D(rho * cos(theta), rho * sin(theta));
	}

	/**
	 * Creates a new point from polar coordinates <code>rho</code> and
	 * <code>theta</code>, from the given point.
	 */
	public static Point2D createPolar(Point2D point, double rho, double theta) {
		return new Point2D(point.x + rho * cos(theta), point.y + rho * sin(theta));
	}

	/**
	 * Creates a new point from polar coordinates <code>rho</code> and
	 * <code>theta</code>, from the position (x0,y0).
	 */
	public static Point2D createPolar(double x0, double y0, double rho,
			double theta) {
		return new Point2D(x0 + rho * cos(theta), y0 + rho * sin(theta));
	}

	/**
	 * Computes the Euclidean distance between two points, given by their
	 * coordinates. Uses robust computation (via Math.hypot() method).
	 * 
	 * @return the Euclidean distance between p1 and p2.
	 */
	public static double distance(double x1, double y1, double x2, double y2) {
		return hypot(x2 - x1, y2 - y1);
	}

	/**
	 * Computes the Euclidean distance between two points. Uses robust
	 * computation (via Math.hypot() method).
	 * 
	 * @param p1 the first point
	 * @param p2 the second point
	 * @return the Euclidean distance between p1 and p2.
	 */
	public static double distance(Point2D p1, Point2D p2) {
		return hypot(p1.x - p2.x, p1.y - p2.y); 
	}

	/**
	 * Tests if the three points are colinear.
	 * 
	 * @return true if three points lie on the same line.
	 */
	public static boolean isColinear(Point2D p1, Point2D p2, Point2D p3) {
		double dx1, dx2, dy1, dy2;
		dx1 = p2.x - p1.x;
		dy1 = p2.y - p1.y;
		dx2 = p3.x - p1.x;
		dy2 = p3.y - p1.y;

		// tests if the two lines are parallel
		return Math.abs(dx1 * dy2 - dy1 * dx2) < Shape2D.ACCURACY;
	}

	/**
	 * Computes the orientation of the 3 points: returns +1 is the path
	 * P0->P1->P2 turns Counter-Clockwise, -1 if the path turns Clockwise, and 0
	 * if the point P2 is located on the line segment [P0 P1]. Algorithm taken
	 * from Sedgewick.
	 * 
	 * @param p0 the initial point
	 * @param p1 the middle point
	 * @param p2 the last point
	 * @return +1, 0 or -1, depending on the relative position of the points
	 */
	public static int ccw(Point2D p0, Point2D p1, Point2D p2) {
		double x0 = p0.x;
		double y0 = p0.y;
		double dx1 = p1.x - x0;
		double dy1 = p1.y - y0;
		double dx2 = p2.x - x0;
		double dy2 = p2.y - y0;

		if (dx1 * dy2 > dy1 * dx2)
			return +1;
		if (dx1 * dy2 < dy1 * dx2)
			return -1;
		if ((dx1 * dx2 < 0) || (dy1 * dy2 < 0))
			return -1;
		if (hypot(dx1, dy1) < hypot(dx2, dy2))
			return +1;
		return 0;
	}

	public static Point2D midPoint(Point2D p1, Point2D p2) {
		return new Point2D((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}

	/**
	 * Computes the centroid, or center of mass, of an array of points.
	 * 
	 * @param points an array of points
	 * @return the centroid of the points
	 */
	public static Point2D centroid(Point2D[] points) {
		int n = points.length;
		double sx = 0, sy = 0;
		for (int i = 0; i < n; i++) {
			sx += points[i].x;
			sy += points[i].y;
		}
		return new Point2D(sx / n, sy / n);
	}

	/**
	 * Computes the weighted centroid, or center of mass, of an array of points.
	 * 
	 * @param points an array of points
	 * @param weights an array of weights the same size as points
	 * @return the centroid of the points
	 */
	public static Point2D centroid(Point2D[] points,
			double[] weights) {
		// number of points
		int n = points.length;

		// check size of second array
		if (n != weights.length) {
			throw new RuntimeException("Arrays must have the same size");
		}

		// sum up weighted coordinates
		double sx = 0, sy = 0, sw = 0;
		double w;
		for (int i = 0; i<n; i++) {
			w = weights[i];
			sx += points[i].x * w;
			sy += points[i].y * w;
			sw += w;
		}

		// compute weighted average of each coordinate
		return new Point2D(sx / sw, sy / sw);
	}

	/**
	 * Computes the centroid, or center of mass, of a collection of points.
	 * 
	 * @param points a collection of points
	 * @return the centroid of the points
	 */
	public static Point2D centroid(Collection<? extends Point2D> points) {
		int n = points.size();
		double sx = 0, sy = 0;
		for (Point2D point : points) {
			sx += point.x;
			sy += point.y;
		}
		return new Point2D(sx/n, sy/n);
	}

	/**
	 * Computes the centroid of three points.
	 * 
	 * @param pt1 the first point
	 * @param pt2 the second point
	 * @param pt3 the third point
	 * @return the centroid of the 3 points
	 */
	public static Point2D centroid(Point2D pt1,	Point2D pt2, Point2D pt3) {
		return new Point2D(
				(pt1.x + pt2.x + pt3.x) / 3, 
				(pt1.y + pt2.y + pt3.y) / 3);
	}

	// ===================================================================
	// class variables

	// coordinates are inherited from java class for Point

	// ===================================================================
	// constructors

	/** Constructs a new Point2D at position (0,0). */
	public Point2D() {
	}

	/**
	 * Constructs a new Point2D at the given given position.
	 */
	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructs a new Point2D by copying coordinates of given java point.
	 */
	public Point2D(java.awt.geom.Point2D point) {
		this(point.getX(), point.getY());
	}

	/**
	 * Constructs a new Point2D by copying coordinates of given point.
	 * @deprecated immutable objects do not need copy constructor (0.11.2)
	 */
	@Deprecated
	public Point2D(Point2D point) {
		this(point.x, point.y);
	}

	// ===================================================================
	// Methods specific to Point2D

	/**
	 * Adds the coordinates of the given point to the coordinates of this
	 * point.
	 */
	public Point2D plus(Point2D p) {
		return new Point2D(this.x + p.x, this.y + p.y);
	}

	/**
	 * Adds the coordinates of the given vector to the coordinates of this
	 * point.
	 */
	public Point2D plus(Vector2D v) {
		return new Point2D(this.x + v.x, this.y + v.y);
	}

	/**
	 * Removes the coordinates of the given point from the coordinates of this
	 * point.
	 */
	public Point2D minus(Point2D p) {
		return new Point2D(this.x - p.x, this. y -p.y);
	}

	/**
	 * Removes the coordinates of the given vector from the coordinates of
	 * this point.
	 */
	public Point2D minus(Vector2D v) {
		return new Point2D(this.x - v.x, this.y - v.y);
	}

	/**
	 * Returns the new point translated by amount given in each direction.
	 * 
	 * @param tx the translation in x direction
	 * @param ty the translation in y direction
	 * @return the translated point
	 */
	public Point2D translate(double tx, double ty) {
		return new Point2D(this.x + tx, this.y + ty);
	}

	/**
	 * Returns the new point scaled by amount given in each direction.
	 * 
	 * @param kx the scale factor in x direction
	 * @param ky the scale factor in y direction
	 * @return the scaled point
	 */
	public Point2D scale(double kx, double ky) {
		return new Point2D(this.x * kx, this.y * ky);
	}

	/**
	 * Returns the new point scaled by the same amount in each direction.
	 * 
	 * @param k the scale factor
	 * @return the scaled point
	 */
	public Point2D scale(double k) {
		return new Point2D(this.x * k, this.y * k);
	}

	/**
	 * Rotates the point by a given angle around the origin.
	 * 
	 * @param theta the angle of rotation, in radians
	 * @return the rotated point.
	 */
	public Point2D rotate(double theta) {
		double cot = cos(theta);
		double sit = sin(theta);
		return new Point2D(x * cot - y * sit, x * sit + y * cot);
	}

	/**
	 * Rotates the point by a given angle around an arbitrary center.
	 * 
	 * @param center the center of the rotation
	 * @param theta the angle of rotation, in radians
	 * @return the rotated point.
	 */
	public Point2D rotate(Point2D center, double theta) {
		double cx = center.x;
		double cy = center.y;
		double cot = cos(theta);
		double sit = sin(theta);
		return new Point2D(
				x * cot - y * sit + (1 - cot) * cx + sit * cy, 
				x * sit + y * cot + (1 - cot) * cy - sit * cx);
	}

	// ===================================================================
	// Methods specific to Point2D

	/**
	 * Converts point to an integer version. Coordinates are rounded to the
	 * nearest integer.
	 * 
	 * @return an instance of java.awt.Point
	 */
	public java.awt.Point getAsInt() {
		return new java.awt.Point((int) x, (int) y);
	}

	/**
	 * Converts point to a double version.
	 */
	public java.awt.geom.Point2D.Double getAsDouble() {
		return new java.awt.geom.Point2D.Double(x, y);
	}

	/**
	 * Converts point to a float version. Coordinates are rounded to the nearest
	 * float.
	 */
	public java.awt.geom.Point2D.Float getAsFloat() {
		return new java.awt.geom.Point2D.Float((float) x, (float) y);
	}

	// ===================================================================
	// Getter and setter
	
	/**
	 * Returns the x-coordinate of this point.
	 */
	public double x() {
		return x;
	}

	/**
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * Returns the y-coordinate of this point.
	 */
	public double y(){
		return y;
	}
	
	/**
	 * Returns the y-coordinate of this point.
	 */
	public double getY() {
		return y;
	}

	// ===================================================================
	// Methods implementing CirculinearShape2D interface

	/*
	 * (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearShape2D#buffer(double)
	 */
	public CirculinearDomain2D buffer(double dist) {
		return new GenericCirculinearDomain2D(
				new Circle2D(this, Math.abs(dist), dist > 0));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * math.geom2d.circulinear.CirculinearShape2D#transform(CircleInversion2D)
	 */
	public Point2D transform(CircleInversion2D inv) {
		// get inversion parameters
		Point2D center = inv.center();
		double r = inv.radius();

		// compute distance and angle of transformed point
		double d = r * r / Point2D.distance(this, center);
		double theta = Angle2D.horizontalAngle(center, this);

		// create the new point
		return Point2D.createPolar(center, d, theta);
	}

	
	// ===================================================================
	// Methods implementing the PointShape2D interface

	/*
	 * (non-Javadoc)
	 * @see math.geom2d.point.PointShape2D#size()
	 */
	public int size() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * @see math.geom2d.point.PointShape2D#points()
	 */
	public Collection<Point2D> points() {
		ArrayList<Point2D> array = new ArrayList<Point2D>(1);
		array.add(this);
		return array;
	}


	// ===================================================================
	// Methods implementing Shape2D interface

	/**
	 * Computes the distance between this and the point <code>point</code>.
	 */
	public double distance(Point2D point) {
		return distance(point.x, point.y);
	}

	/**
	 * Computes the distance between current point and point with coordinate
	 * <code>(x,y)</code>. Uses the <code>Math.hypot()</code> function for
	 * better robustness than simple square root.
	 */
	public double distance(double x, double y) {
		return hypot(this.x - x, this.y - y);
	}

	/**
	 * Returns true if the point is bounded. A point is unbounded if at least
	 * one of its coordinates is infinite or NaN.
	 * 
	 * @return true if both coordinates of the point are finite
	 */
	public boolean isBounded() {
		if (java.lang.Double.isInfinite(this.x))
			return false;
		if (java.lang.Double.isInfinite(this.y))
			return false;
		if (java.lang.Double.isNaN(this.x))
			return false;
		if (java.lang.Double.isNaN(this.y))
			return false;
		return true;
	}

	/**
	 * Returns false, as a point can not be empty.
	 */
	public boolean isEmpty() {
		return false;
	}

	/**
	 * Returns true if the two points are equal.
	 */
	public boolean contains(double x, double y) {
		return this.equals(new Point2D(x, y));
	}

	/**
	 * Returns true if the two points are equal.
	 */
	public boolean contains(Point2D p) {
		return this.equals(p);
	}

	/**
	 * Returns a PointSet2D, containing 0 or 1 point, depending on whether the
	 * point lies inside the specified box.
	 */
	public PointSet2D clip(Box2D box) {
		// init result array
		PointSet2D set = new PointArray2D(0);

		// return empty array if point is clipped
		if (x < box.getMinX())
			return set;
		if (y < box.getMinY())
			return set;
		if (x > box.getMaxX())
			return set;
		if (y > box.getMaxY())
			return set;

		// return an array with the point
		set.add(this);
		return set;
	}

	/**
	 * Returns a bounding box with zero width and zero height, whose coordinates
	 * limits are point coordinates.
	 */
	public Box2D boundingBox() {
		return new Box2D(x, x, y, y);
	}

	/**
	 * Returns the transformed point.
	 */
	public Point2D transform(AffineTransform2D trans) {
		double[] tab = trans.coefficients();
		return new Point2D(
				x * tab[0] + y * tab[1] + tab[2], 
				x * tab[3] + y * tab[4] + tab[5]);
	}

	// ===================================================================
	// Graphical methods

	/**
	 * Draws the point on the specified Graphics2D, using default radius equal
	 * to 1.
	 * 
	 * @param g2 the graphics to draw the point
	 */
	public void draw(Graphics2D g2) {
		this.draw(g2, 1);
	}

	/**
	 * Draws the point on the specified Graphics2D, by filling a disc with a
	 * given radius.
	 * 
	 * @param g2 the graphics to draw the point
	 */
	public void draw(Graphics2D g2, double r) {
		g2.fill(new java.awt.geom.Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r));
	}

	// ===================================================================
	// methods implementing the GeometricObject2D interface

    /**
     * Test whether this object is the same as another point, with respect
     * to a given threshold along each coordinate.
     */
    public boolean almostEquals(GeometricObject2D obj, double eps) {
    	if (this==obj)
    		return true;
    	
        if (!(obj instanceof Point2D))
            return false;
        Point2D p = (Point2D) obj;
        
        if (Math.abs(this.x - p.x) > eps)
        	return false;
        if (Math.abs(this.y - p.y) > eps)
        	return false;
        
        return true;
    }

	// ===================================================================
	// methods implementing the Iterable interface

    /*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Point2D> iterator() {
		return this.points().iterator();
	}

	// ===================================================================
	// Override of Object methods

	@Override
	public String toString() {
		return new String("Point2D("+x+", "+y+")");
	}

	/**
	 * Two points are considered equal if their Euclidean distance is less than
	 * Shape2D.ACCURACY.
	 */
	@Override
	public boolean equals(Object obj) {
    	if (this == obj)
    		return true;
    	
		if (!(obj instanceof Point2D))
			return false;
		Point2D that = (Point2D) obj;
		
        // Compare each field
		if (!EqualUtils.areEqual(this.x, that.x)) 
			return false;
		if (!EqualUtils.areEqual(this.y, that.y)) 
			return false;

        return true;
	}

	/**
	 * @deprecated not necessary to clone immutable objects (0.11.2)
	 */
	@Deprecated
	@Override
	public Point2D clone() {
		return new Point2D(x, y);
	}
}
