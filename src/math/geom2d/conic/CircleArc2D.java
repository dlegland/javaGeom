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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import math.geom2d.*;
import math.geom2d.circulinear.CirculinearDomain2D;
import math.geom2d.circulinear.CirculinearElement2D;
import math.geom2d.circulinear.buffer.BufferCalculator;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.Curve2DUtils;
import math.geom2d.curve.CurveArray2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.LinearShape2D;
import math.geom2d.line.Ray2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.transform.CircleInversion2D;
import static java.lang.Math.*;

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
public class CircleArc2D extends EllipseArc2D
implements Cloneable, CircularShape2D, CirculinearElement2D {

    // ====================================================================
    // static factories

    public static CircleArc2D create(Circle2D support, double startAngle,
    		double angleExtent) {
    	return new CircleArc2D(support, startAngle, angleExtent);
    }
    
    public static CircleArc2D create(Circle2D support, double startAngle,
    		double endAngle, boolean direct) {
    	return new CircleArc2D(support, startAngle, endAngle, direct);
    }
    
    public static CircleArc2D create(Point2D center, double radius,
    		double startAngle, double angleExtent) {
    	return new CircleArc2D(center, radius, startAngle, angleExtent);
    }
    
    public static CircleArc2D create(Point2D center, double radius,
    		double startAngle, double endAngle, boolean direct) {
    	return new CircleArc2D(center, radius, startAngle, endAngle, direct);
    }
    
    
    // ====================================================================
    // class variables

    protected Circle2D circle;

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
        this(center.getX(), center.getY(), radius, startAngle, angleExtent);
    }

    /**
     * Create a new circle arc with specified point center and radius, start and
     * end angles, and by specifying whether arc is direct or not.
     */
    public CircleArc2D(Point2D center, double radius, double start, double end,
            boolean direct) {
        this(center.getX(), center.getY(), radius, start, end, direct);
    }

    // Constructors based on doubles

    /**
     * Base constructor, for constructiong arc from circle parameters, start and
     * end angles, and by specifying whether arc is direct or not.
     */
    public CircleArc2D(double xc, double yc, double r, double start,
            double end, boolean direct) {
        super(xc, yc, r, r, 0, start, end, direct);
        this.circle = new Circle2D(xc, yc, r);
        this.ellipse = this.circle;
    }

    /** Base constructor with all parameters specified */
    public CircleArc2D(double xc, double yc, double r, double start,
            double extent) {
        super(xc, yc, r, r, 0, start, extent);
        this.circle = new Circle2D(xc, yc, r);
        this.ellipse = this.circle;
    }

    // ====================================================================
    // methods specific to CircleArc2D

    /**
     * convert position on curve to angle with circle center.
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
     * Returns the circle which contains the circle arc.
     */
    public Circle2D supportingCircle() {
        return circle;
    }

    // ===================================================================
    // Methods implementing the CirculinearCurve2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearShape2D#getBuffer(double)
	 */
	public CirculinearDomain2D buffer(double dist) {
		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		return bc.computeBuffer(this, dist);
	}

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
	 * @see math.geom2d.circulinear.CirculinearCurve2D#getLength(double)
	 */
	public double length(double pos) {
		return pos * circle.radius();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see math.geom2d.circulinear.CirculinearCurve2D#getPosition(double)
	 */
	public double position(double length) {
		return length / circle.radius();
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#transform(math.geom2d.transform.CircleInversion2D)
	 */
	public CirculinearElement2D transform(CircleInversion2D inv) {
		// Extract inversion parameters
        Point2D center = inv.center();        
        
        // transform the extremities
        Point2D p1 = this.firstPoint().transform(inv);
        Point2D p2 = this.lastPoint().transform(inv);
        	
        CirculinearElement2D element = circle.transform(inv);
        
        if(element instanceof Circle2D) {
        	return new CircleArc2D(
        			(Circle2D)element, 
        			Angle2D.horizontalAngle(center, p1),
        			Angle2D.horizontalAngle(center, p2),
        			!this.isDirect());
        } else if (element instanceof StraightLine2D) {
            //TODO: add processing of special cases (arc contains transform center)            
        	return new LineSegment2D(p1, p2);
        } 
        
        System.err.println(
        		"CircleArc2D.transform(): error in transforming circle by inversion");
        return null;

	}

    // ====================================================================
    // methods from interface OrientedCurve2D

    @Override
    public double windingAngle(Point2D point) {
        Point2D p1 = firstPoint();
        Point2D p2 = lastPoint();

        // compute angle of point with extreme points
        double angle1 = Angle2D.horizontalAngle(point, p1);
        double angle2 = Angle2D.horizontalAngle(point, p2);

        boolean b1 = (new StraightLine2D(p1, p2)).isInside(point);
        boolean b2 = ellipse.isInside(point);

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

    @Override
    public boolean isInside(Point2D point) {
		return distanceSigned(point.getX(), point.getY()) < 0;
    }

    @Override
    public double distanceSigned(Point2D p) {
        return distanceSigned(p.getX(), p.getY());
    }

    @Override
    public double distanceSigned(double x, double y) {
        double dist = distance(x, y);
        Point2D point = new Point2D(x, y);

        boolean direct = angleExtent>0;
        // boolean inCircle = Point2D.getDistance(x, y, xc, yc)<=r;
        boolean inCircle = circle.isInside(point);
        if (inCircle)
			return angleExtent > 0 ? -dist : dist;

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

    @Override
    public Vector2D tangent(double t) {
        t = this.positionToAngle(t);

        double r = circle.radius();
		if (angleExtent > 0)
			return new Vector2D(-r * sin(t), r * cos(t));
		else
			return new Vector2D(r * sin(t), -r * cos(t));
    }

    // ===================================================================
    // methods from interface ContinuousCurve2D

    @Override
    public Collection<? extends CircleArc2D> smoothPieces() {
        ArrayList<CircleArc2D> list = new ArrayList<CircleArc2D>(1);
        list.add(this);
        return list;
    }

    /**
     * a circle arc is never closed by definition.
     */
    @Override
    public boolean isClosed() {
        return false;
    }

    // ====================================================================
    // methods from interface Curve2D

    /** Always return 0 */
    @Override
    public double getT0() {
        return 0;
    }

    /**
     * return the last position of the circle are, which is given by the angle
     * extent of the arc.
     */
    @Override
    public double getT1() {
        return abs(this.angleExtent);
    }

    /**
     * Returns the position of a point form the curvilinear position.
     */
    @Override
    public Point2D point(double t) {
        t = this.positionToAngle(t);
        return circle.point(t);
    }

    /**
     * return relative position between 0 and the angle extent.
     */
    @Override
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
     * Compute intersections of the circle arc with a line. Return an array of
     * Point2D, of size 0, 1 or 2 depending on the distance between circle and
     * line. If there are 2 intersections points, the first one in the array is
     * the first one on the line.
     */
    @Override
    public Collection<Point2D> intersections(LinearShape2D line) {
    	return Circle2D.getIntersections(this, line);
    }

    @Override
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

    // ====================================================================
    // methods from interface Shape2D

    @Override
    public double distance(Point2D p) {
        return distance(p.getX(), p.getY());
    }

    @Override
    public double distance(double x, double y) {
        double angle = Angle2D.horizontalAngle(circle.xc, circle.yc, x, y);

        if (containsAngle(angle))
            return abs(Point2D.distance(circle.xc, circle.yc, x, y)
                    -circle.r);
        else
            return min(firstPoint().distance(x, y), lastPoint()
                    .distance(x, y));
    }

    /** Always return true */
    @Override
    public boolean isBounded() {
        return true;
    }

    /**
     * return a new CircleArc2D. Variables t0 and t1 must be comprised between 0
     * and the angle extent of the arc.
     */
    @Override
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
     * Returns the circle arc which refers to the same parent circle, with same
     * start angle, and with opposite angle extent.
     */
    @Override
    public CircleArc2D reverse() {
        return new CircleArc2D(this.circle, Angle2D.formatAngle(startAngle
                +angleExtent), -angleExtent);
    }

    @Override
    public Collection<? extends CircleArc2D> continuousCurves() {
    	return wrapCurve(this);
    }

    /**
     * Clip the circle arc by a box. The result is a CurveSet2D, which contains
     * only instances of CircleArc2D. If circle arc is not clipped, the result
     * is an instance of CurveSet2D with zero curves.
     */
    @Override
    public CurveSet2D<CircleArc2D> clip(Box2D box) {
        // Clip he curve
        CurveSet2D<SmoothCurve2D> set = Curve2DUtils.clipSmoothCurve(this, box);

        // create a new structure for storing result
        CurveArray2D<CircleArc2D> result = 
        	new CurveArray2D<CircleArc2D>(set.curveNumber());

        // convert result
        for (Curve2D curve : set.curves()) {
            if (curve instanceof CircleArc2D)
                result.addCurve((CircleArc2D) curve);
        }
        return result;
    }

    /**
     * Returns an instance of EllipseArc2D, or CircleArc2D if transform is a
     * similarity.
     */
    @Override
    public EllipseArc2D transform(AffineTransform2D trans) {
        if (!AffineTransform2D.isSimilarity(trans))
            return super.transform(trans);

        // System.out.println("transform a circle");

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
        double factor = Math.sqrt(coefs[0]*coefs[0]+coefs[3]*coefs[3]);

        // compute parameters of new circle arc
        double xc = center.getX(), yc = center.getY();
        double r2 = circle.radius()*factor;
        double startAngle = angle1;
        double angleExtent = Angle2D.formatAngle(angle2-angle1);

        boolean b1 = AffineTransform2D.isDirect(trans);
        boolean b2 = this.isDirect();
        if (b1&!b2|!b1&b2)
            angleExtent = angleExtent-2*Math.PI;

        // return new CircleArc
        return new CircleArc2D(xc, yc, r2, startAngle, angleExtent);
    }


    @Override
    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    @Override
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

    // ===================================================================
    // methods implementing GeometricObject2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
	 */
	public boolean almostEquals(GeometricObject2D obj, double eps) {
    	if (this==obj)
    		return true;
    	
        if (!(obj instanceof EllipseArc2D))
            return false;

        if (!(obj instanceof CircleArc2D))
            return super.equals(obj);

        CircleArc2D arc = (CircleArc2D) obj;
        // test whether supporting ellipses have same support
        if (abs(circle.xc-arc.circle.xc)>eps)
            return false;
        if (abs(circle.yc-arc.circle.yc)>eps)
            return false;
        if (abs(circle.r-arc.circle.r)>eps)
            return false;
        if (abs(circle.r1-arc.circle.r1)>eps)
            return false;
        if (abs(circle.r2-arc.circle.r2)>eps)
            return false;
        if (abs(circle.theta-arc.circle.theta)>eps)
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

    @Override
    public String toString() {
    	Point2D center = circle.center();
        return String.format(Locale.US, 
                "CircleArc2D(%7.2f,%7.2f,%7.2f,%7.5f,%7.5f)", 
                center.getX(), center.getY(), circle.radius(),
                getStartAngle(), getAngleExtent());
    }

    /**
     * Two circle arc are equal if the have same center, same radius, same
     * starting and ending angles, and same orientation.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EllipseArc2D))
            return false;

        if (!(obj instanceof CircleArc2D))
            return super.equals(obj);

        CircleArc2D arc = (CircleArc2D) obj;
        // test whether supporting ellipses have same support
        if (abs(circle.xc-arc.circle.xc)>Shape2D.ACCURACY)
            return false;
        if (abs(circle.yc-arc.circle.yc)>Shape2D.ACCURACY)
            return false;
        if (abs(circle.r-arc.circle.r)>Shape2D.ACCURACY)
            return false;
        if (abs(circle.r1-arc.circle.r1)>Shape2D.ACCURACY)
            return false;
        if (abs(circle.r2-arc.circle.r2)>Shape2D.ACCURACY)
            return false;
        if (abs(circle.theta-arc.circle.theta)>Shape2D.ACCURACY)
            return false;

		// test is angles are the same
		if (abs(Angle2D.formatAngle(startAngle)
				- Angle2D.formatAngle(arc.startAngle)) > Shape2D.ACCURACY)
			return false;
		if (abs(Angle2D.formatAngle(angleExtent)
				- Angle2D.formatAngle(arc.angleExtent)) > Shape2D.ACCURACY)
			return false;

        // if no difference, this is the same
        return true;
    }

    @Override
    public CircleArc2D clone() {
        return new CircleArc2D(circle.clone(), startAngle, angleExtent);
    }
}
