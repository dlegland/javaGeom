
package math.geom2d.conic;
import static java.lang.Math.*;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.*;
import math.geom2d.curve.*;
import math.geom2d.domain.Domain2D;
import math.geom2d.domain.GenericDomain2D;
import math.geom2d.domain.SmoothContour2D;
import math.geom2d.line.LinearShape2D;

/**
 * Branch of an Hyperbola2D.
 */
public class HyperbolaBranch2D extends AbstractSmoothCurve2D
implements SmoothContour2D, Cloneable {

    // ===================================================================
    // Static constructor

    /**
     * Generic constructor, using a parent Hyperbola, and a boolean to
     * specifies if the branch is the right one (crossing the Ox axis on
     * positive side, b true), or the left one (crossing the Oy axis on the
     * negative side, b false).
     */
    public static HyperbolaBranch2D create(Hyperbola2D hyperbola, boolean b) {
        return new HyperbolaBranch2D(hyperbola, b);
    }

    
    // ===================================================================
    // inner fields

	/** The parent hyperbola */
    Hyperbola2D hyperbola = null;
    
    /** 
     * This field is true if it crosses the positive axis, in the basis of the
     * parent hyperbola.
     */
    boolean     positive  = true;

    
    // ===================================================================
    // Constructors

    /**
     * Generic constructor, using a parent Hyperbola, and a boolean to
     * specifies if the branch is the right one (crossing the Ox axis on
     * positive side, b true), or the left one (crossing the Oy axis on the
     * negative side, b false).
     */
    public HyperbolaBranch2D(Hyperbola2D hyperbola, boolean b) {
        this.hyperbola = hyperbola;
        this.positive = b;
    }

    
    // ===================================================================
    // methods specific to HyperbolaBranch2D

    /**
     * Returns the supporting hyperbola of this branch.
     */
    public Hyperbola2D getHyperbola() {
        return hyperbola;
    }

    /**
     * Returns true if this branch is the positive one, i.e. it contains the
     * positive axis in the basis of the supporting hyperbola.
     * 
     * @return true if this branch contains the positive axis.
     */
    public boolean isPositiveBranch() {
        return positive;
    }

    // ===================================================================
    // methods inherited from SmoothCurve2D interface

    /**
     * Use formula given in 
     * <a href="http://mathworld.wolfram.com/Hyperbola.html">
     * http://mathworld.wolfram.com/Hyperbola.html</a>
     */
    public double curvature(double t) {
		double a = hyperbola.a;
		double b = hyperbola.b;
		double asih = a * sinh(t);
		double bcoh = b * cosh(t);
		return (a * b) / pow(hypot(bcoh, asih), 3);
    }

    public Vector2D tangent(double t) {
        double a = hyperbola.a;
        double b = hyperbola.b;
        double theta = hyperbola.theta;
        double dx, dy;
        if (positive) {
			dx = a * sinh(t);
			dy = b * cosh(t);
		} else {
			dx = -a * sinh(t);
			dy = -b * cosh(t);
		}
		double cot = cos(theta);
		double sit = sin(theta);
		return new Vector2D(dx * cot - dy * sit, dx * sit + dy * cot);
    }

    // ===================================================================
    // methods inherited from Boundary2D interface

    public Domain2D domain() {
        return new GenericDomain2D(this);
    }

    /** Throws an UnboundedShapeException */
    public void fill(Graphics2D g2) {
        throw new UnboundedShape2DException(this);
    }

    // ===================================================================
    // methods inherited from OrientedCurve2D interface

    public double signedDistance(Point2D point) {
        double dist = this.distance(point);
        return this.isInside(point) ? -dist : dist;
    }

    public double signedDistance(double x, double y) {
        return this.signedDistance(new Point2D(x, y));
    }

    public double windingAngle(Point2D point) {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean isInside(Point2D point) {
		if (hyperbola.isDirect()) {
			if (hyperbola.isInside(point))
				return true;
			double x = hyperbola.toLocal(point).x();
			return positive ? x < 0 : x > 0;
		} else {
			if (!hyperbola.isInside(point))
				return false;
			double x = hyperbola.toLocal(point).x();
			return positive ? x > 0 : x < 0;
		}
    }

    // ===================================================================
    // methods inherited from ContinuousCurve2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.curve.Curve2D#continuousCurves()
	 */
	public Collection<? extends HyperbolaBranch2D> continuousCurves() {
		return wrapCurve(this);
	}

	/** Return false, by definition of Hyperbola branch */
    public boolean isClosed() {
        return false;
    }

    public java.awt.geom.GeneralPath appendPath(
    		java.awt.geom.GeneralPath path) {
    	throw new UnboundedShape2DException(this);
    }

    
    // ===================================================================
    // methods inherited from Curve2D interface

    public Point2D point(double t) {
        if (Double.isInfinite(t))
            throw new UnboundedShape2DException(this);

        double x, y;
        if (positive) {
            x = cosh(t);
            if (Double.isInfinite(x))
                x = abs(t);
            y = sinh(t);
            if (Double.isInfinite(y))
                y = t;
        } else {
            x = -cosh(t);
            if (Double.isInfinite(x))
                x = -abs(t);
            y = -sinh(t);
            if (Double.isInfinite(y))
                y = -t;
        }
        return hyperbola.toGlobal(new Point2D(x, y));
    }

    public double position(Point2D point) {
		Point2D pt = hyperbola.toLocal(point);
		double y = this.positive ? pt.y() : -pt.y();
		return log(y + hypot(y, 1));
	}

	public double project(Point2D point) {
		Point2D pt = hyperbola.toLocal(point);
		double y = this.positive ? pt.y() : -pt.y();
		return log(y + hypot(y, 1));
    }

    public HyperbolaBranch2D reverse() {
        Hyperbola2D hyper2 = new Hyperbola2D(hyperbola.xc, hyperbola.yc,
                hyperbola.a, hyperbola.b, hyperbola.theta, !hyperbola.direct);
        return new HyperbolaBranch2D(hyper2, positive);
    }

    /**
     * Returns an instance of HyprbolaBranchArc2D initialized with
     * <code>this</code>.
     */
    public HyperbolaBranchArc2D subCurve(double t0, double t1) {
        return new HyperbolaBranchArc2D(this, t0, t1);
    }

    /** 
     * Returns Double.NEGATIVE_INFINITY. 
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
     * Returns Double.POSITIVE_INFINITY. 
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
    
    public Collection<Point2D> intersections(LinearShape2D line) {
        // compute intersections with support hyperbola
        Collection<Point2D> inters = hyperbola.intersections(line);

        // check which points belong to this branch
        Collection<Point2D> result = new ArrayList<Point2D>();
        for (Point2D point : inters) {
			if (!(hyperbola.toLocal(point).x() > 0 ^ positive))
				result.add(point);
        }

        // return result
        return result;
    }

    
    // ===================================================================
    // methods inherited from Shape2D interface

    /** Returns a bounding box with infinite bounds in every direction */
    public Box2D boundingBox() {
        return Box2D.INFINITE_BOX;
    }

    /**
     * Clips the curve with a box. The result is an instance of
     * CurveSet2D, which contains only instances of HyperbolaBranchArc2D. 
     * If the curve does not intersect the boundary of the box,
     * the result is an instance of CurveSet2D which contains 0 curves.
     */
    public CurveSet2D<? extends HyperbolaBranchArc2D> clip(Box2D box) {
        // Clip the curve
        CurveSet2D<SmoothCurve2D> set = Curves2D.clipSmoothCurve(this, box);

        // Stores the result in appropriate structure
        CurveArray2D<HyperbolaBranchArc2D> result = 
        	new CurveArray2D<HyperbolaBranchArc2D>(set.size());

        // convert the result
        for (Curve2D curve : set.curves()) {
            if (curve instanceof HyperbolaBranchArc2D)
                result.add((HyperbolaBranchArc2D) curve);
        }
        return result;
    }

    public double distance(Point2D point) {
        Point2D projected = this.point(this.project(point));
        return projected.distance(point);
    }

    public double distance(double x, double y) {
        Point2D projected = this.point(this.project(new Point2D(x, y)));
        return projected.distance(x, y);
    }

    /** Returns false, as an hyperbola branch is never bounded. */
    public boolean isBounded() {
        return false;
    }

    /**
     * Returns false, as an hyperbola branch is never empty.
     */
    public boolean isEmpty() {
        return false;
    }

    public HyperbolaBranch2D transform(AffineTransform2D trans) {
    	// The transform the base hypebola, and a point of the branch
    	Hyperbola2D hyperbola = this.hyperbola.transform(trans);
    	Point2D base = this.point(0).transform(trans);
    	
    	// compute distance of the transformed point to each branch
    	double d1 = hyperbola.positiveBranch().distance(base);
    	double d2 = hyperbola.negativeBranch().distance(base);
    	
    	// choose the 'positivity' of the branch from the closest branch
        return new HyperbolaBranch2D(hyperbola, d1 < d2);
    }

    public boolean contains(Point2D point) {
        return this.contains(point.x(), point.y());
    }

    public boolean contains(double x, double y) {
        if (!hyperbola.contains(x, y))
            return false;
        Point2D point = hyperbola.toLocal(new Point2D(x, y));
        return point.x() > 0;
    }

	// ===================================================================
	// methods implementing the GeometricObject2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
	 */
    public boolean almostEquals(GeometricObject2D obj, double eps) {
    	if (this == obj)
    		return true;
    	
        if(!(obj instanceof HyperbolaBranch2D))
            return false;
        HyperbolaBranch2D branch = (HyperbolaBranch2D) obj;
        
        if(!hyperbola.almostEquals(branch.hyperbola, eps)) return false;
        return positive == branch.positive;
    }

    // ===================================================================
    // methods overriding Object class

    @Override
    public boolean equals(Object obj) {
    	if (this == obj)
    		return true;
    	
        if(!(obj instanceof HyperbolaBranch2D))
            return false;
        HyperbolaBranch2D branch = (HyperbolaBranch2D) obj;
        
        if(!hyperbola.equals(branch.hyperbola)) return false;
        return positive == branch.positive;
    }
    
	/**
	 * @deprecated use copy constructor instead (0.11.2)
	 */
	@Deprecated
    @Override
    public HyperbolaBranch2D clone() {
        return new HyperbolaBranch2D(hyperbola.clone(), positive);
    }
}
