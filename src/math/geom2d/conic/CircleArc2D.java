/* file : CircleArc2D.java
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
 * Created on 29 avr. 2006
 *
 */

package math.geom2d.conic;

import static java.lang.Math.*;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Locale;

import math.geom2d.*;
import math.geom2d.circulinear.CirculinearDomain2D;
import math.geom2d.circulinear.CirculinearElement2D;
import math.geom2d.circulinear.buffer.BufferCalculator;
import math.geom2d.curve.*;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.LinearShape2D;
import math.geom2d.line.Ray2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.polygon.Polyline2D;
import math.geom2d.transform.CircleInversion2D;
import math.utils.EqualUtils;

/**
 * A circle arc, defined by the center and the radius of the containing circle,
 * by a starting angle, and by a (signed) angle extent.
 * <p>
 * A circle arc is directed: if angle extent is positive, the arc is counter
 * clockwise. Otherwise, it is clockwise.
 * <p>
 * A circle arc is parameterized using angle from center. The arc contains all
 * points with a parametric equation of t, for each t between 0 and the angle
 * extent.
 * 
 * @author dlegland
 */
public class CircleArc2D extends AbstractSmoothCurve2D
implements EllipseArcShape2D, CircularShape2D, CirculinearElement2D, Cloneable {

    // ====================================================================
    // static factories

	/**
	 * @deprecated since 0.11.1
	 */
	@Deprecated
    public static CircleArc2D create(Circle2D support, double startAngle,
    		double angleExtent) {
    	return new CircleArc2D(support, startAngle, angleExtent);
    }
    
	/**
	 * @deprecated since 0.11.1
	 */
	@Deprecated
	public static CircleArc2D create(Circle2D support, double startAngle,
    		double endAngle, boolean direct) {
    	return new CircleArc2D(support, startAngle, endAngle, direct);
    }
    
	/**
	 * @deprecated since 0.11.1
	 */
	@Deprecated
    public static CircleArc2D create(Point2D center, double radius,
    		double startAngle, double angleExtent) {
    	return new CircleArc2D(center, radius, startAngle, angleExtent);
    }
    
	/**
	 * @deprecated since 0.11.1
	 */
	@Deprecated
    public static CircleArc2D create(Point2D center, double radius,
    		double startAngle, double endAngle, boolean direct) {
    	return new CircleArc2D(center, radius, startAngle, endAngle, direct);
    }
    
    
    // ====================================================================
    // Class variables

    /** The supporting circle */
    protected Circle2D circle;
    
    /** The starting position on circle, in radians between 0 and +2PI */
    protected double    startAngle  = 0;

    /** The signed angle extent, in radians between -2PI and +2PI. */
    protected double    angleExtent = PI;

    
    // ====================================================================
    // constructors

    /**
     * Create a circle arc whose support circle is centered on (0,0) and has a
     * radius equal to 1. Start angle is 0, and angle extent is PI/2.
     */
    public CircleArc2D() {
        this(0, 0, 1, 0, PI/2);
    }
    
    // Constructors based on Circles

    /**
     * create a new circle arc based on an already existing circle.
     */
    public CircleArc2D(Circle2D circle, double startAngle, double angleExtent) {
        this(circle.xc, circle.yc, circle.r, startAngle, angleExtent);
    }

    /**
     * create a new circle arc based on an already existing circle, specifying
     * if arc is direct or not.
     */
    public CircleArc2D(Circle2D circle, double startAngle, double endAngle,
            boolean direct) {
        this(circle.xc, circle.yc, circle.r, startAngle, endAngle, direct);
    }

    // Constructors based on points

    /** Create a new circle arc with specified point center and radius */
    public CircleArc2D(Point2D center, double radius, double startAngle,
            double angleExtent) {
        this(center.x(), center.y(), radius, startAngle, angleExtent);
    }

    /**
     * Create a new circle arc with specified point center and radius, start and
     * end angles, and by specifying whether arc is direct or not.
     */
    public CircleArc2D(Point2D center, double radius, double start, double end,
            boolean direct) {
        this(center.x(), center.y(), radius, start, end, direct);
    }

    // Constructors based on doubles

    /**
     * Base constructor, for constructing arc from circle parameters, start and
     * end angles, and by specifying whether arc is direct or not.
     */
    public CircleArc2D(double xc, double yc, double r, double startAngle,
            double endAngle, boolean direct) {
        this.circle = new Circle2D(xc, yc, r);
        this.startAngle = startAngle;
        this.angleExtent = endAngle;
		this.angleExtent = Angle2D.formatAngle(endAngle - startAngle);
		if (!direct)
			this.angleExtent = this.angleExtent - PI * 2;
    }

    /** Base constructor with all parameters specified */
    public CircleArc2D(double xc, double yc, double r, double start,
            double extent) {
        this.circle = new Circle2D(xc, yc, r);
        this.startAngle = start;
        this.angleExtent = extent;
    }

    // ====================================================================
    // methods specific to CircleArc2D

    /**
     * Returns true if the circle arc is direct, i.e. if the angle extent is
     * positive.
     */
    public boolean isDirect() {
    	return angleExtent >= 0;
    }
    
    public double getStartAngle() {
    	return startAngle;
    }
    
    public double getAngleExtent() {
    	return angleExtent;
    }
    
    public boolean containsAngle(double angle) {
        return Angle2D.containsAngle(
        		startAngle, startAngle+angleExtent, angle, angleExtent>=0);
    }

    /** Returns the angle associated with the given position */
    public double getAngle(double position) {
		if (position < 0)
			position = 0;
		if (position > abs(angleExtent))
			position = abs(angleExtent);
		if (angleExtent < 0)
			position = -position;
		return Angle2D.formatAngle(startAngle + position);
    }

    /**
     * Converts position on curve to angle with circle center.
     */
	private double positionToAngle(double t) {
		if (t > abs(angleExtent))
			t = abs(angleExtent);
		if (t < 0)
			t = 0;
		if (angleExtent < 0)
			t = -t;
		t = t + startAngle;
		return t;
	}

    // ===================================================================
    // methods implementing CircularShape2D interface

    /**
     * Returns the circle that contains the circle arc.
     */
    public Circle2D supportingCircle() {
        return circle;
    }

    // ===================================================================
    // Methods implementing the CirculinearCurve2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearShape2D#buffer(double)
	 */
	public CirculinearDomain2D buffer(double dist) {
		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		return bc.computeBuffer(this, dist);
	}

	/**
	 * Returns the circle arc parallel to this circle arc, at the distance
	 * dist. 
	 */
    public CircleArc2D parallel (double dist) {
    	double r = circle.radius();
		double r2 = max(angleExtent > 0 ? r + dist : r - dist, 0);
		return new CircleArc2D(circle.center(), r2, startAngle, angleExtent);
    }
    
    public double length() {
		return circle.radius() * abs(angleExtent);
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see math.geom2d.circulinear.CirculinearCurve2D#length(double)
	 */
	public double length(double pos) {
		return pos * circle.radius();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see math.geom2d.circulinear.CirculinearCurve2D#position(double)
	 */
	public double position(double length) {
		return length / circle.radius();
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#transform(math.geom2d.transform.CircleInversion2D)
	 */
	public CirculinearElement2D transform(CircleInversion2D inv) {
        // Transform the support circle
		CirculinearElement2D support = circle.transform(inv);
		
        // transform the extremities
        Point2D p1 = this.firstPoint().transform(inv);
        Point2D p2 = this.lastPoint().transform(inv);
        	
        if(support instanceof Circle2D) {
        	Circle2D circle2 = (Circle2D) support;
        	Point2D center = circle2.center();        

        	return new CircleArc2D(
        			circle2.center(), circle2.radius(), 
        			Angle2D.horizontalAngle(center, p1),
        			Angle2D.horizontalAngle(center, p2),
        			!this.isDirect() ^ circle2.isDirect());
        	
        } else if (support instanceof StraightLine2D) {
            //TODO: add processing of special cases (arc contains transform center)            
        	return new LineSegment2D(p1, p2);
        } 
        
        System.err.println(
        		"CircleArc2D.transform(): error in transforming circle by inversion");
        return null;

	}

    // ====================================================================
    // methods from interface OrientedCurve2D

    public double windingAngle(Point2D point) {
        Point2D p1 = firstPoint();
        Point2D p2 = lastPoint();

        // compute angle of point with extreme points
        double angle1 = Angle2D.horizontalAngle(point, p1);
        double angle2 = Angle2D.horizontalAngle(point, p2);

        boolean b1 = (new StraightLine2D(p1, p2)).isInside(point);
        boolean b2 = this.circle.isInside(point);

		if (angleExtent > 0) {
			if (b1 || b2) {
				if (angle2 > angle1)
					return angle2 - angle1;
				else
					return 2 * Math.PI - angle1 + angle2;
			} else {
				if (angle2 > angle1)
					return angle2 - angle1 - 2 * Math.PI;
				else
					return angle2 - angle1;
			}
		} else {
			if (!b1 || b2) {
				if (angle1 > angle2)
					return angle2 - angle1;
				else
					return angle2 - angle1 - 2 * Math.PI;
			} else {
				if (angle1 > angle2)
					return angle2 - angle1 + 2 * Math.PI;
				else
					return angle2 - angle1;
			}
        }
    }

    public boolean isInside(Point2D point) {
		return signedDistance(point.x(), point.y()) < 0;
    }

    public double signedDistance(Point2D p) {
        return signedDistance(p.x(), p.y());
    }

    public double signedDistance(double x, double y) {
        double dist = distance(x, y);
        Point2D point = new Point2D(x, y);

		boolean direct = angleExtent > 0;
        boolean inCircle = circle.isInside(point);
        if (inCircle)
			return direct ? -dist : dist;

        Point2D p1 = circle.point(startAngle);
		Point2D p2 = circle.point(startAngle + angleExtent);
        boolean onLeft = (new StraightLine2D(p1, p2)).isInside(point);

		if (direct && !onLeft)
			return dist;
		if (!direct && onLeft)
			return -dist;

		Vector2D tangent = circle.tangent(startAngle);
		boolean left1 = (new Ray2D(p1, tangent)).isInside(point);
		if (direct && !left1)
			return dist;
		if (!direct && left1)
			return -dist;

		tangent = circle.tangent(startAngle + angleExtent);
		boolean left2 = (new Ray2D(p2, tangent)).isInside(point);
		if (direct && !left2)
			return dist;
		if (!direct && left2)
			return -dist;

        if (direct)
            return -dist;
        else
            return dist;
    }

    // ====================================================================
    // methods from interface SmoothCurve2D

    public Vector2D tangent(double t) {
        t = this.positionToAngle(t);

        double r = circle.radius();
		if (angleExtent > 0)
			return new Vector2D(-r * sin(t), r * cos(t));
		else
			return new Vector2D(r * sin(t), -r * cos(t));
    }

    /**
     * Returns curvature of the circle arc. This is the same as the curvature
     * of the parent circle, with a control on the sign that depends on the
     * orientation. 
     */
	public double curvature(double t) {
		double kappa = circle.curvature(t);
		return this.isDirect() ? kappa : -kappa;
	}

	
	// ===================================================================
    // methods from interface ContinuousCurve2D

	/**
	 * Returns a collection of curves containing only this circle arc.
	 */
    public Collection<? extends CircleArc2D> smoothPieces() {
    	return wrapCurve(this);
    }

    /**
     * Returns false, as a circle arc is never closed by definition.
     */
    public boolean isClosed() {
        return false;
    }

	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#asPolyline(int)
	 */
	public Polyline2D asPolyline(int n) {

        // compute increment value
        double dt = Math.abs(this.angleExtent) / n;

        // allocate array of points, and compute each value.
        // Computes also value for last point.
        Point2D[] points = new Point2D[n + 1];
        for (int i = 0; i < n + 1; i++)
        	points[i] = this.point(i * dt);

        return new Polyline2D(points);
	}


    // ====================================================================
    // methods from interface Curve2D

    /**
     * Returns 0.
     */
	public double t0() {
		return 0;
	}

 	/**
 	 * @deprecated replaced by t0() 
 	 */
 	@Deprecated
    public double getT0() {
        return 0;
    }

    /**
     * Returns the last position of the circle are, which is given by the
     * absolute angle of angle extent of this arc.
     */
 	public double t1() {
		return abs(this.angleExtent);
	}

 	/**
 	 * @deprecated replaced by t1() 
 	 */
 	@Deprecated
 	public double getT1() {
        return abs(this.angleExtent);
    }

    /**
     * Returns the position of a point form the curvilinear position.
     */
    public Point2D point(double t) {
        t = this.positionToAngle(t);
        return circle.point(t);
    }

    /**
     * Returns relative position between 0 and the angle extent.
     */
    public double position(Point2D point) {
        double angle = Angle2D.horizontalAngle(circle.center(), point);
        if (containsAngle(angle))
			if (angleExtent > 0)
				return Angle2D.formatAngle(angle - startAngle);
			else
				return Angle2D.formatAngle(startAngle - angle);

        // return either 0 or 1, depending on which extremity is closer.
        return firstPoint().distance(point) < 
        lastPoint().distance(point) ? 0 : abs(angleExtent);
    }

    /**
     * Computes intersections of the circle arc with a line. Return an array of
     * Point2D, of size 0, 1 or 2 depending on the distance between circle and
     * line. If there are 2 intersections points, the first one in the array is
     * the first one on the line.
     */
    public Collection<Point2D> intersections(LinearShape2D line) {
    	return Circle2D.lineCircleIntersections(line, this);
    }

    public double project(Point2D point) {
        double angle = circle.project(point);

        // Case of an angle contained in the circle arc
        if (Angle2D.containsAngle(startAngle, startAngle+angleExtent, angle,
				angleExtent > 0)) {
			if (angleExtent > 0)
				return Angle2D.formatAngle(angle - startAngle);
			else
				return Angle2D.formatAngle(startAngle - angle);
        }

        Point2D p1 = this.firstPoint();
        Point2D p2 = this.lastPoint();
		if (p1.distance(point) < p2.distance(point))
            return 0;
        else
            return abs(angleExtent);
    }

    /**
     * Returns a new CircleArc2D. Variables t0 and t1 must be comprised between 0
     * and the angle extent of the arc.
     */
    public CircleArc2D subCurve(double t0, double t1) {
        // convert position to angle
        if (angleExtent>0) {
			t0 = Angle2D.formatAngle(startAngle + t0);
			t1 = Angle2D.formatAngle(startAngle + t1);
		} else {
			t0 = Angle2D.formatAngle(startAngle - t0);
			t1 = Angle2D.formatAngle(startAngle - t1);
        }

        // check bounds of angles
		if (!Angle2D.containsAngle(startAngle, startAngle + angleExtent, t0,
				angleExtent > 0))
			t0 = startAngle;
		if (!Angle2D.containsAngle(startAngle, startAngle + angleExtent, t1,
				angleExtent > 0))
			t1 = Angle2D.formatAngle(startAngle + angleExtent);

        // create new arc
        return new CircleArc2D(circle, t0, t1, angleExtent>0);
    }

    /**
     * Returns the circle arc which refers to the same parent circle, but
     * with exchanged extremities.
     */
    public CircleArc2D reverse() {
    	double newStart = Angle2D.formatAngle(startAngle + angleExtent);
        return new CircleArc2D(this.circle, newStart, -angleExtent);
    }

	/**
	 * Returns a collection of curves containing only this circle arc.
	 */
    public Collection<? extends CircleArc2D> continuousCurves() {
    	return wrapCurve(this);
    }

    
    // ====================================================================
    // methods from interface Shape2D

    public double distance(Point2D p) {
        return distance(p.x(), p.y());
    }

    public double distance(double x, double y) {
        double angle = Angle2D.horizontalAngle(circle.xc, circle.yc, x, y);

        if (containsAngle(angle))
            return abs(Point2D.distance(circle.xc, circle.yc, x, y) - circle.r);
        else 
            return min(firstPoint().distance(x, y), lastPoint().distance(x, y));
    }

    /** Returns true, as a circle arc is bounded by definition. */
    public boolean isBounded() {
        return true;
    }

    /**
     * Clips the circle arc by a box. The result is a CurveSet2D, which contains
     * only instances of CircleArc2D. If circle arc is not clipped, the result
     * is an instance of CurveSet2D with zero curves.
     */
    public CurveSet2D<CircleArc2D> clip(Box2D box) {
        // Clip he curve
        CurveSet2D<SmoothCurve2D> set = Curves2D.clipSmoothCurve(this, box);

        // create a new structure for storing result
        CurveArray2D<CircleArc2D> result = 
        	new CurveArray2D<CircleArc2D>(set.size());

        // convert result
        for (Curve2D curve : set.curves()) {
            if (curve instanceof CircleArc2D)
                result.add((CircleArc2D) curve);
        }
        return result;
    }

    /**
     * Returns an instance of EllipseArc2D, or CircleArc2D if transform is a
     * similarity.
     */
    public EllipseArcShape2D transform(AffineTransform2D trans) {
    	
    	// When the transform is not a similarity, should switch to EllipseArc
    	// computation
        if (!AffineTransform2D.isSimilarity(trans)) {
        	Ellipse2D ellipse = this.circle.asEllipse();
			EllipseArc2D arc = new EllipseArc2D(ellipse, this.startAngle,
					this.angleExtent);
            return arc.transform(trans);
        }

        // System.out.println("transform a circle arc");

        // extract the control points
        Point2D center = circle.center();
        Point2D point1 = this.firstPoint();
        Point2D point2 = this.lastPoint();

        // transform each point
        center = center.transform(trans);
        point1 = point1.transform(trans);
        point2 = point2.transform(trans);

        // compute new angles
        double angle1 = Angle2D.horizontalAngle(center, point1);
        double angle2 = Angle2D.horizontalAngle(center, point2);

        // compute factor of transform
        double[] coefs = trans.coefficients();
        double factor = Math.hypot(coefs[0], coefs[3]);

        // compute parameters of new circle arc
        double xc = center.x(), yc = center.y();
        double r2 = circle.radius() * factor;
        double startAngle = angle1;
        double angleExtent = Angle2D.formatAngle(angle2-angle1);

        boolean b1 = AffineTransform2D.isDirect(trans);
        boolean b2 = this.isDirect();
		if (b1 && !b2 || !b1 && b2)
			angleExtent = angleExtent - 2 * Math.PI;

        // return new CircleArc
        return new CircleArc2D(xc, yc, r2, startAngle, angleExtent);
    }

    public boolean contains(Point2D p) {
        return contains(p.x(), p.y());
    }

    public boolean contains(double x, double y) {
        // Check if radius is correct
    	double r = circle.radius();
		if (abs(Point2D.distance(circle.xc, circle.yc, x, y) - r) > Shape2D.ACCURACY)
            return false;

        // angle from circle center to point
        double angle = Angle2D.horizontalAngle(circle.xc, circle.yc, x, y);
        
        // check if angle is contained in interval [startAngle-angleExtent]
        return this.containsAngle(angle);
    }

    /**
     * Returns false.
     */
	public boolean isEmpty() {
		return false;
	}

    public Box2D boundingBox() {

        // first get ending points
        Point2D p0 = firstPoint();
        Point2D p1 = lastPoint();

        // get coordinate of ending points
        double x0 = p0.x();
        double y0 = p0.y();
        double x1 = p1.x();
        double y1 = p1.y();

        // intialize min and max coords
        double xmin = min(x0, x1);
        double xmax = max(x0, x1);
        double ymin = min(y0, y1);
        double ymax = max(y0, y1);

        // Pre-computations
        Point2D center = circle.center();
        double xc = center.x();
		double yc = center.y();
		double endAngle = startAngle + angleExtent;
		boolean direct = angleExtent >= 0;
		
        // check cases arc contains one maximum
		if (Angle2D.containsAngle(startAngle, endAngle, PI / 2 + circle.theta, direct))
			ymax = max(ymax, yc + circle.r);
		if (Angle2D.containsAngle(startAngle, endAngle, 3 * PI / 2
				+ circle.theta, direct))
			ymin = min(ymin, yc - circle.r);
		if (Angle2D.containsAngle(startAngle, endAngle, circle.theta, direct))
			xmax = max(xmax, xc + circle.r);
		if (Angle2D.containsAngle(startAngle, endAngle, PI + circle.theta,
				direct))
			xmin = min(xmin, xc - circle.r);

        // return a bounding with computed limits
        return new Box2D(xmin, xmax, ymin, ymax);
    }

    public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path) {
    	// number of curves to approximate the arc
		int nSeg = (int) ceil(abs(angleExtent) / (PI / 2));
    	nSeg = min(nSeg, 4);
    	
    	// angular extent of each curve
		double ext = angleExtent / nSeg;
    	
    	// compute coefficient 
    	double k = btan(abs(ext));
    	
		for (int i = 0; i < nSeg; i++) {
			// position of the two extremities
			double ti0 = abs(i * ext);
			double ti1 = abs((i + 1) * ext);
    		
    		// extremity points
    		Point2D p1 = this.point(ti0);
    		Point2D p2 = this.point(ti1);
    		
    		// tangent vectors, multiplied by appropriate coefficient
    		Vector2D v1 = this.tangent(ti0).times(k);
    		Vector2D v2 = this.tangent(ti1).times(k);
    		
    		// append a cubic curve to the path
    		path.curveTo(
    				p1.x() + v1.x(), p1.y() + v1.y(),
					p2.x() - v2.x(), p2.y() - v2.y(), 
					p2.x(), p2.y());
    	}
		return path;    		
    }

    public java.awt.geom.GeneralPath getGeneralPath() {
        // create new path
        java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();

        // move to the first point
        Point2D point = this.firstPoint();
        path.moveTo((float) point.x(), (float) point.y());

        // append the curve
        path = this.appendPath(path);

        // return the final path
        return path;
    }

	@Override
    public void draw(Graphics2D g2) {
        g2.draw(this.getGeneralPath());
    }

    /**
     * 
     * btan computes the length (k) of the control segments at
     * the beginning and end of a cubic Bezier that approximates
     * a segment of an arc with extent less than or equal to
     * 90 degrees.  This length (k) will be used to generate the
     * 2 Bezier control points for such a segment.
     *
     *   Assumptions:
     *     a) arc is centered on 0,0 with radius of 1.0
     *     b) arc extent is less than 90 degrees
     *     c) control points should preserve tangent
     *     d) control segments should have equal length
     *
     *   Initial data:
     *     start angle: ang1
     *     end angle:   ang2 = ang1 + extent
     *     start point: P1 = (x1, y1) = (cos(ang1), sin(ang1))
     *     end point:   P4 = (x4, y4) = (cos(ang2), sin(ang2))
     *
     *   Control points:
     *     P2 = (x2, y2)
     *     | x2 = x1 - k * sin(ang1) = cos(ang1) - k * sin(ang1)
     *     | y2 = y1 + k * cos(ang1) = sin(ang1) + k * cos(ang1)
     *
     *     P3 = (x3, y3)
     *     | x3 = x4 + k * sin(ang2) = cos(ang2) + k * sin(ang2)
     *     | y3 = y4 - k * cos(ang2) = sin(ang2) - k * cos(ang2)
     *
     * The formula for this length (k) can be found using the
     * following derivations:
     *
     *   Midpoints:
     *     a) Bezier (t = 1/2)
     *        bPm = P1 * (1-t)^3 +
     *              3 * P2 * t * (1-t)^2 +
     *              3 * P3 * t^2 * (1-t) +
     *              P4 * t^3 =
     *            = (P1 + 3P2 + 3P3 + P4)/8
     *
     *     b) arc
     *        aPm = (cos((ang1 + ang2)/2), sin((ang1 + ang2)/2))
     *
     *   Let angb = (ang2 - ang1)/2; angb is half of the angle
     *   between ang1 and ang2.
     *
     *   Solve the equation bPm == aPm
     *
     *     a) For xm coord:
     *        x1 + 3*x2 + 3*x3 + x4 = 8*cos((ang1 + ang2)/2)
     *
     *        cos(ang1) + 3*cos(ang1) - 3*k*sin(ang1) +
     *        3*cos(ang2) + 3*k*sin(ang2) + cos(ang2) =
     *        = 8*cos((ang1 + ang2)/2)
     *
     *        4*cos(ang1) + 4*cos(ang2) + 3*k*(sin(ang2) - sin(ang1)) =
     *        = 8*cos((ang1 + ang2)/2)
     *
     *        8*cos((ang1 + ang2)/2)*cos((ang2 - ang1)/2) +
     *        6*k*sin((ang2 - ang1)/2)*cos((ang1 + ang2)/2) =
     *        = 8*cos((ang1 + ang2)/2)
     *
     *        4*cos(angb) + 3*k*sin(angb) = 4
     *
     *        k = 4 / 3 * (1 - cos(angb)) / sin(angb)
     *
     *     b) For ym coord we derive the same formula.
     *
     * Since this formula can generate "NaN" values for small
     * angles, we will derive a safer form that does not involve
     * dividing by very small values:
     *     (1 - cos(angb)) / sin(angb) =
     *     = (1 - cos(angb))*(1 + cos(angb)) / sin(angb)*(1 + cos(angb)) =
     *     = (1 - cos(angb)^2) / sin(angb)*(1 + cos(angb)) =
     *     = sin(angb)^2 / sin(angb)*(1 + cos(angb)) =
     *     = sin(angb) / (1 + cos(angb))
     *
     * Function taken from java.awt.geom.ArcIterator.
     */
    private static double btan(double increment) {
        increment /= 2.0;
        return 4.0 / 3.0 * sin(increment) / (1.0 + cos(increment));
    }

	// ===================================================================
    // methods implementing GeometricObject2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
	 */
	public boolean almostEquals(GeometricObject2D obj, double eps) {
		if (this == obj)
			return true;

		if (!(obj instanceof CircleArc2D))
			return super.equals(obj);

        CircleArc2D arc = (CircleArc2D) obj;
		// test whether supporting ellipses have same support
		if (abs(circle.xc - arc.circle.xc) > eps)
			return false;
		if (abs(circle.yc - arc.circle.yc) > eps)
			return false;
		if (abs(circle.r - arc.circle.r) > eps)
			return false;
		if (abs(circle.theta - arc.circle.theta) > eps)
			return false;

        // test is angles are the same
        if (abs(Angle2D.formatAngle(startAngle)
                -Angle2D.formatAngle(arc.startAngle))>eps)
            return false;
        if (abs(Angle2D.formatAngle(angleExtent)
                -Angle2D.formatAngle(arc.angleExtent))>eps)
            return false;

        // if no difference, this is the same
        return true;
	}

    // ===================================================================
    // methods implementing Object interface

    public String toString() {
    	Point2D center = circle.center();
        return String.format(Locale.US, 
                "CircleArc2D(%7.2f,%7.2f,%7.2f,%7.5f,%7.5f)", 
                center.x(), center.y(), circle.radius(),
                getStartAngle(), getAngleExtent());
    }

    /**
     * Two circle arc are equal if the have same center, same radius, same
     * starting and ending angles, and same orientation.
     */
    @Override
    public boolean equals(Object obj) {
    	if (this == obj)
    		return true;
    
        if (!(obj instanceof CircleArc2D))
            return false;
        CircleArc2D that = (CircleArc2D) obj;
        
        // test whether supporting circles have same support
        if (!this.circle.equals(that.circle))
        	return false;

        // test if angles are the same
        if (!EqualUtils.areEqual(startAngle, that.startAngle))
            return false;
        if (!EqualUtils.areEqual(angleExtent, that.angleExtent))
            return false;

        // if no difference, this is the same
        return true;
    }

	/**
	 * @deprecated use copy constructor instead (0.11.2)
	 */
	@Deprecated
    @Override
    public CircleArc2D clone() {
        return new CircleArc2D(circle.clone(), startAngle, angleExtent);
    }

}
