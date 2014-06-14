/* file : Parabola2D.java
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
 * Created on 29 janv. 2007
 *
 */

package math.geom2d.conic;
import static java.lang.Math.*;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.*;
import math.geom2d.curve.*;
import math.geom2d.domain.Contour2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.domain.GenericDomain2D;
import math.geom2d.line.LinearShape2D;
import math.geom2d.line.StraightLine2D;
import math.utils.EqualUtils;

/**
 * A parabola, defined by its vertex, its orientation, and its pedal.
 * Orientation is defined as the orientation of derivative at vertex point, with
 * the second derivative pointing to the top.
 * <p>
 * Following parametric representation is used:
 * <p>
 * <code>x(t)=t </code>
 * <p>
 * <code>y(t)=a*t^2</code>
 * <p>
 * This is a signed parameter (negative a makes the parabola point to opposite
 * side).
 * 
 * @author dlegland
 */
public class Parabola2D extends AbstractSmoothCurve2D 
implements Contour2D, Conic2D, Cloneable {

    // ==========================================================
    // static constructors

    /**
     * Creates a parabola by supplying the vertex and the focus.
     * 
     * @param vertex the vertex point of the parabola
     * @param focus the focal point of the parabola
     * @return the parabola with given vertex and focus
     */
    public final static Parabola2D create(Point2D vertex, Point2D focus) {
		double p = Point2D.distance(vertex, focus);
		double theta = Angle2D.horizontalAngle(vertex, focus) - PI / 2;
		return new Parabola2D(vertex, 1 / (4 * p), theta);
	}

    
    // ==========================================================
    // class variables

    /** Coordinate of the vertex */
    protected double xv    = 0, yv = 0;

    /** orientation of the parabola */
    protected double theta = 0;

    /** The parameter of the parabola. If positive, the parabola is direct. */
    protected double a     = 1;

    private boolean  debug = false;

    
    // ==========================================================
    // constructors

    /**
     * Empty constructor.
     */
    public Parabola2D() {
        super();
    }

    public Parabola2D(Point2D vertex, double a, double theta) {
        this(vertex.x(), vertex.y(), a, theta);
    }

    public Parabola2D(double xv, double yv, double a, double theta) {
        super();
        this.xv = xv;
        this.yv = yv;
        this.a = a;
        this.theta = theta;
    }

    // ==========================================================
    // methods specific to Parabola2D

    /**
     * Returns the focus of the parabola.
     */
    public Point2D getFocus() {
		double c = 1 / a / 4.0;
		return new Point2D(xv - c * sin(theta), yv + c * cos(theta));
   }

    public double getParameter() {
        return a;
    }

    public double getFocusDistance() {
		return 1.0 / (4 * a);
	}

    public Point2D getVertex() {
        return new Point2D(xv, yv);
    }

    /**
     * Returns the first direction vector of the parabola
     */
    public Vector2D getVector1() {
        Vector2D vect = new Vector2D(1, 0);
        return vect.transform(AffineTransform2D.createRotation(theta));
    }

    /**
     * Returns the second direction vector of the parabola.
     */
    public Vector2D getVector2() {
        Vector2D vect = new Vector2D(1, 0);
		return vect.transform(AffineTransform2D.createRotation(theta + PI / 2));
    }

    /**
     * Returns orientation angle of parabola. It is defined as the angle of the
     * derivative at the vertex.
     */
    public double getAngle() {
        return theta;
    }

    /**
     * Returns true if the parameter a is positive.
     */
    public boolean isDirect() {
		return a > 0;
    }

    /**
     * Changes coordinate of the point to correspond to a standard parabola.
     * Standard parabola s such that y=x^2 for every point of the parabola.
     * 
     * @param point
     * @return
     */
    private Point2D formatPoint(Point2D point) {
		Point2D p2 = point;
		p2 = p2.transform(AffineTransform2D.createTranslation(-xv, -yv));
		p2 = p2.transform(AffineTransform2D.createRotation(-theta));
		p2 = p2.transform(AffineTransform2D.createScaling(1, 1.0 / a));
        return p2;
    }

    /**
     * Changes coordinate of the line to correspond to a standard parabola.
     * Standard parabola s such that y=x^2 for every point of the parabola.
     * 
     * @param point
     * @return
     */
    private LinearShape2D formatLine(LinearShape2D line) {
        line = line.transform(AffineTransform2D.createTranslation(-xv, -yv));
        line = line.transform(AffineTransform2D.createRotation(-theta));
		line = line.transform(AffineTransform2D.createScaling(1, 1.0 / a));
        return line;
    }

    // ==========================================================
    // methods implementing the Conic2D interface

    public Conic2D.Type conicType() {
        return Conic2D.Type.PARABOLA;
    }

    public double[] conicCoefficients() {
    	// The transformation matrix from base parabola y=x^2
    	AffineTransform2D transform =
    		AffineTransform2D.createRotation(theta).chain(
    				AffineTransform2D.createTranslation(xv, yv));
        	
    	// Extract coefficients of inverse transform
        double[][] coefs = transform.invert().affineMatrix();
        double m00 = coefs[0][0];
        double m01 = coefs[0][1];
        double m02 = coefs[0][2];
        double m10 = coefs[1][0];
        double m11 = coefs[1][1];
        double m12 = coefs[1][2];
        
        // Default conic coefficients are A=a, F=1.
        // Compute result of transformed coefficients, which simplifies in:
		double A = a * m00 * m00;
		double B = 2 * a * m00 * m01;
		double C = a * m01 * m01;
		double D = 2 * a * m00 * m02 - m10;
		double E = 2 * a * m01 * m02 - m11;
		double F = a * m02 * m02 - m12;
        
        // arrange into array
		return new double[] { A, B, C, D, E, F };
    }

    /**
     * Return 1, by definition for a parabola.
     */
    public double eccentricity() {
        return 1.0;
    }

    // ==========================================================
    // methods implementing the Boundary2D interface

    public Domain2D domain() {
        return new GenericDomain2D(this);
    }

    // ==========================================================
    // methods implementing the OrientedCurve2D interface

    public double windingAngle(Point2D point) {
		if (isDirect()) {
			if (isInside(point))
				return PI * 2;
			else
				return 0.0;
		} else {
			if (isInside(point))
				return 0.0;
			else
				return -PI * 2;
		}
    }

    public double signedDistance(Point2D p) {
        return signedDistance(p.x(), p.y());
    }

    public double signedDistance(double x, double y) {
        if (isInside(new Point2D(x, y)))
            return -distance(x, y);
        return -distance(x, y);
    }

    public boolean isInside(Point2D point) {
        // Process the point to be in a referentiel such that parabola is
        // vertical
        Point2D p2 = formatPoint(point);

        // get coordinate of transformed point
        double x = p2.x();
        double y = p2.y();

        // check condition of parabola
		return y > x * x ^ a < 0;
    }

    // ==========================================================
    // methods implementing the SmoothCurve2D interface

    public Vector2D tangent(double t) {
		Vector2D vect = new Vector2D(1, 2.0 * a * t);
		return vect.transform(AffineTransform2D.createRotation(theta));
    }

    /**
     * Returns the curvature of the parabola at the given position.
     */
    public double curvature(double t) {
		return 2 * a / pow(hypot(1, 2 * a * t), 3);
    }

    // ==========================================================
    // methods implementing the ContinuousCurve2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.curve.Curve2D#continuousCurves()
	 */
	public Collection<? extends Parabola2D> continuousCurves() {
		return wrapCurve(this);
	}
	
   /**
     * Returns false, as a parabola is an open curve.
     */
    public boolean isClosed() {
        return false;
    }

    // ==========================================================
    // methods implementing the Curve2D interface

    /**
     * Returns the parameter of the first point of the line, which is always
     * Double.NEGATIVE_INFINITY.
     */
    public double t0() {
        return Double.NEGATIVE_INFINITY;
    }

    /**
     * @deprecated replaced by t0() (since 0.11.1).
     */
    @Deprecated
    public double getT0() {
    	return t0();
    }
    
    /**
     * Returns the parameter of the last point of the line, which is always
     * Double.POSITIVE_INFINITY.
     */
    public double t1() {
        return Double.POSITIVE_INFINITY;
    }

    /**
     * @deprecated replaced by t1() (since 0.11.1).
     */
    @Deprecated
    public double getT1() {
    	return t1();
    }
    

    public Point2D point(double t) {
		Point2D point = new Point2D(t, a * t * t);
        point = AffineTransform2D.createRotation(theta).transform(point);
        point = AffineTransform2D.createTranslation(xv, yv).transform(point);
        return point;
    }

    /**
     * Returns position of point on the parabola. If point is not on the
     * parabola returns the positions on its "vertical" projection (i.e. its
     * projection parallel to the symetry axis of the parabola).
     */
    public double position(Point2D point) {
        // t parameter is x-coordinate of point
        return formatPoint(point).x();
    }

    /**
     * Returns position of point on the parabola. If point is not on the
     * parabola returns the positions on its "vertical" projection (i.e. its
     * projection parallel to the symetry axis of the parabola).
     */
    public double project(Point2D point) {
        // t parameter is x-coordinate of point
        return formatPoint(point).x();
    }

    public Collection<Point2D> intersections(LinearShape2D line) {
        // Computes the lines which corresponds to a "Unit" parabola.
        LinearShape2D line2 = this.formatLine(line);
        double dx = line2.direction().x();
        double dy = line2.direction().y();

        ArrayList<Point2D> points = new ArrayList<Point2D>();

        // case of vertical or quasi-vertical line
        if (Math.abs(dx) < Shape2D.ACCURACY) {
            if (debug)
                System.out.println("intersect parabola with vertical line ");
            double x = line2.origin().x();
			Point2D point = new Point2D(x, x * x);
            if (line2.contains(point))
                points.add(line.point(line2.position(point)));
            return points;
        }

        // Extract formatted line parameters
        Point2D origin = line2.origin();
        double x0 = origin.x();
        double y0 = origin.y();

        // Solve second order equation
		double k = dy / dx; // slope of the line
		double yl = k * x0 - y0;
		double delta = k * k - 4 * yl;

        // Case of a line 'below' the parabola
		if (delta < 0)
            return points;

        // There are two intersections with supporting line,
        // need to check these points belong to the line.

        double x;
        Point2D point;
        StraightLine2D support = line2.supportingLine();

        // test first intersection point
		x = (k - Math.sqrt(delta)) * .5;
		point = new Point2D(x, x * x);
        if (line2.contains(support.projectedPoint(point)))
            points.add(line.point(line2.position(point)));

        // test second intersection point
		x = (k + Math.sqrt(delta)) * .5;
		point = new Point2D(x, x * x);
        if (line2.contains(support.projectedPoint(point)))
            points.add(line.point(line2.position(point)));

        return points;
    }

    /**
     * Returns the parabola with same vertex, direction vector in opposite
     * direction and opposite parameter <code>p</code>.
     */
    public Parabola2D reverse() {
		return new Parabola2D(xv, yv, -a, Angle2D.formatAngle(theta + PI));
    }

    /**
     * Returns a new ParabolaArc2D, or null if t1<t0.
     */
    public ParabolaArc2D subCurve(double t0, double t1) {
        if (debug)
			System.out.println("theta = " + Math.toDegrees(theta));
		if (t1 < t0)
            return null;
        return new ParabolaArc2D(this, t0, t1);
    }

    public double distance(Point2D p) {
        return distance(p.x(), p.y());
    }

    public double distance(double x, double y) {
        // TODO Computes on polyline approximation, needs to compute on whole
        // curve
        return new ParabolaArc2D(this, -100, 100).distance(x, y);
    }

    // ===============================================
    // Drawing methods (curve interface)

    /** Throws an infiniteShapeException */
    public java.awt.geom.GeneralPath appendPath(
    		java.awt.geom.GeneralPath path) {
        throw new UnboundedShape2DException(this);
    }

    /** Throws an infiniteShapeException */
    public void fill(Graphics2D g2) {
        throw new UnboundedShape2DException(this);
    }

    // ===============================================
    // methods implementing the Shape2D interface

    /** Always returns false, because a parabola is not bounded. */
    public boolean isBounded() {
        return false;
    }

    /**
     * Returns false, as a parabola is never empty.
     */
    public boolean isEmpty() {
        return false;
    }

    /**
     * Clip the parabola by a box. The result is an instance of CurveSet2D<ParabolaArc2D>,
     * which contains only instances of ParabolaArc2D. If the parabola is not
     * clipped, the result is an instance of CurveSet2D<ParabolaArc2D> which
     * contains 0 curves.
     */
    public CurveSet2D<ParabolaArc2D> clip(Box2D box) {
        // Clip the curve
        CurveSet2D<SmoothCurve2D> set = Curves2D.clipSmoothCurve(this, box);

        // Stores the result in appropriate structure
        CurveArray2D<ParabolaArc2D> result = 
        	new CurveArray2D<ParabolaArc2D>(set.size());

        // convert the result
        for (Curve2D curve : set.curves()) {
            if (curve instanceof ParabolaArc2D)
                result.add((ParabolaArc2D) curve);
        }
        return result;
    }

    public Box2D boundingBox() {
        // TODO: manage parabolas with horizontal or vertical orientations
        return new Box2D(
        		Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * Transforms the parabola by an affine transform. The transformed parabola
     * is direct if this parabola and the affine transform are both either
     * direct or indirect.
     */
    public Parabola2D transform(AffineTransform2D trans) {
    	//TODO: check if transform work also for non-motion transforms...
        Point2D vertex = this.getVertex().transform(trans);
        Point2D focus = this.getFocus().transform(trans);
		double a = 1 / (4.0 * Point2D.distance(vertex, focus));
		double theta = Angle2D.horizontalAngle(vertex, focus) - PI / 2;

        // check orientation of resulting parabola
		if (this.a < 0 ^ trans.isDirect())
            // normal case
            return new Parabola2D(vertex, a, theta);
        else
            // inverted case
			return new Parabola2D(vertex, -a, theta + PI);
    }

    // ===============================================
    // methods implementing the Shape interface

    public boolean contains(double x, double y) {
        // Process the point to be in a basis such that parabola is vertical
        Point2D p2 = formatPoint(new Point2D(x, y));

        // get coordinate of transformed point
        double xp = p2.x();
        double yp = p2.y();

        // check condition of parabola
		return abs(yp - xp * xp) < Shape2D.ACCURACY;
    }

    public boolean contains(Point2D point) {
        return contains(point.x(), point.y());
    }

	// ===================================================================
	// methods implementing the GeometricObject2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
	 */
    public boolean almostEquals(GeometricObject2D obj, double eps) {
    	if (this==obj)
    		return true;
    	
        if (!(obj instanceof Parabola2D))
            return false;
        Parabola2D parabola = (Parabola2D) obj;

        if ((this.xv-parabola.xv)>eps) 
            return false;
        if ((this.yv-parabola.yv)>eps) 
            return false;
        if ((this.a-parabola.a)>eps)
            return false;
        if (!Angle2D.almostEquals(this.theta, parabola.theta, eps))
            return false;

        return true;
    }

    // ====================================================================
    // Methods inherited from the object class

    @Override
    public String toString() {
        return String.format("Parabola2D(%f,%f,%f,%f)", 
                xv, yv, a, theta);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Parabola2D))
            return false;
        Parabola2D that = (Parabola2D) obj;

        // Compare each field
		if (!EqualUtils.areEqual(this.xv, that.xv)) 
			return false;
		if (!EqualUtils.areEqual(this.yv, that.yv)) 
			return false;
		if (!EqualUtils.areEqual(this.a, that.a)) 
			return false;
		if (!EqualUtils.areEqual(this.theta, that.theta)) 
			return false;
        
        return true;
    }
    
	/**
	 * @deprecated use copy constructor instead (0.11.2)
	 */
	@Deprecated
    @Override
    public Parabola2D clone() {
        return new Parabola2D(xv, yv, a, theta);
    }
}
