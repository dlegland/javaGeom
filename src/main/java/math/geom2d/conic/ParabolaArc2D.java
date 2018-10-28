/* file : ParabolaArc2D.java
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
 * Created on 02 May 2007
 *
 */

package math.geom2d.conic;

import static java.lang.Math.PI;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Angle2DUtil;
import math.geom2d.Box2D;
import math.geom2d.IGeometricObject2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.AbstractSmoothCurve2D;
import math.geom2d.curve.CurveArray2D;
import math.geom2d.curve.Curves2DUtil;
import math.geom2d.curve.ICurve2D;
import math.geom2d.curve.ICurveSet2D;
import math.geom2d.curve.ISmoothCurve2D;
import math.geom2d.domain.ISmoothOrientedCurve2D;
import math.geom2d.exception.UnboundedShape2DException;
import math.geom2d.line.ILinearShape2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.point.Point2D;
import math.geom2d.polygon.Polyline2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * An arc of parabola, defined by a parent parabola, and two limits for the parametrization.
 * 
 * @author dlegland
 */
public class ParabolaArc2D extends AbstractSmoothCurve2D implements ISmoothOrientedCurve2D {
    private static final long serialVersionUID = 1L;

    public static ParabolaArc2D create(Parabola2D parabola, double t0, double t1) {
        return new ParabolaArc2D(parabola, t0, t1);
    }

    // ==========================================================
    // class variables

    /**
     * The parent parabola.
     */
    private final Parabola2D parabola;

    /**
     * The lower bound if the parameterization for this arc.
     */
    private final double t0;

    /**
     * The upper bound if the parameterization for this arc.
     */
    private final double t1;

    // ==========================================================
    // constructor

    public ParabolaArc2D(Parabola2D parabola, double t0, double t1) {
        this.parabola = parabola;
        this.t0 = t0;
        this.t1 = t1;
    }

    // ==========================================================
    // methods specific to ParabolaArc2D

    public Parabola2D getParabola() {
        return this.parabola;
    }

    // ==========================================================
    // methods implementing the OrientedCurve2D interface

    @Override
    public double windingAngle(Point2D point) {
        double angle0, angle1;

        boolean direct = parabola.isDirect();
        boolean inside = this.isInside(point);

        if (Double.isInfinite(t0)) {
            angle0 = parabola.getAngle() + (direct ? +1 : -1) * PI / 2;
        } else {
            angle0 = Angle2DUtil.horizontalAngle(point, parabola.point(t0));
        }

        if (Double.isInfinite(t1)) {
            angle1 = parabola.getAngle() + (direct ? +1 : -1) * PI / 2;
        } else {
            angle1 = Angle2DUtil.horizontalAngle(point, parabola.point(t1));
        }

        if (inside) {
            // turn CCW -> return positive angle
            if (angle0 > angle1)
                return 2 * PI - angle0 + angle1;
            else
                return angle1 - angle0;
        } else {
            // turn CW -> return negative angle
            if (angle0 > angle1)
                return angle1 - angle0;
            else
                return (angle1 - angle0) - 2 * PI;
        }
    }

    @Override
    public double signedDistance(Point2D p) {
        return signedDistance(p.x(), p.y());
    }

    @Override
    public double signedDistance(double x, double y) {
        if (isInside(new Point2D(x, y)))
            return -distance(x, y);
        return -distance(x, y);
    }

    @Override
    public boolean isInside(Point2D point) {
        boolean direct = parabola.isDirect();
        boolean inside = parabola.isInside(point);
        if (inside && direct)
            return true;
        if (!inside && !direct)
            return false;

        double pos = parabola.project(point);

        if (pos < t0) {
            Point2D p0 = parabola.point(t0);
            Vector2D v0 = parabola.tangent(t0);
            StraightLine2D line0 = new StraightLine2D(p0, v0);
            return line0.isInside(point);
        }

        if (pos > t1) {
            Point2D p1 = parabola.point(t1);
            Vector2D v1 = parabola.tangent(t1);
            StraightLine2D line1 = new StraightLine2D(p1, v1);
            return line1.isInside(point);
        }
        return !direct;
    }

    // ==========================================================
    // methods implementing the SmoothCurve2D interface

    @Override
    public Vector2D tangent(double t) {
        return parabola.tangent(t);
    }

    /**
     * Returns the curvature of the parabola arc.
     */
    @Override
    public double curvature(double t) {
        return parabola.curvature(t);
    }

    // ==========================================================
    // methods implementing the ContinuousCurve2D interface

    /** Returns false, by definition of a parabola arc */
    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public Polyline2D asPolyline(int n) {
        // Check that the curve is bounded
        if (!this.isBounded())
            throw new UnboundedShape2DException(this);

        // compute start and increment values
        double t0 = this.t0();
        double dt = (this.t1() - t0) / n;

        // allocate array of points, and compute each value.
        Point2D[] points = new Point2D[n + 1];
        for (int i = 0; i < n + 1; i++)
            points[i] = this.point(t0 + i * dt);

        return new Polyline2D(points);
    }

    // ====================================================================
    // methods implementing the Curve2D interface

    /**
     * Returns the position of the first point of the parabola arc.
     */
    @Override
    public double t0() {
        return t0;
    }

    /**
     * Returns the position of the last point of the parabola arc.
     */
    @Override
    public double t1() {
        return t1;
    }

    @Override
    public Point2D point(double t) {
        t = min(max(t, t0), t1);
        return parabola.point(t);
    }

    @Override
    public double position(Point2D point) {
        if (!this.parabola.contains(point))
            return Double.NaN;
        double t = this.parabola.position(point);
        if (t - t0 < -ACCURACY)
            return Double.NaN;
        if (t1 - t < ACCURACY)
            return Double.NaN;
        return t;
    }

    @Override
    public double project(Point2D point) {
        double t = this.parabola.project(point);
        return min(max(t, t0), t1);
    }

    @Override
    public Collection<Point2D> intersections(ILinearShape2D line) {
        Collection<Point2D> inters0 = this.parabola.intersections(line);
        ArrayList<Point2D> inters = new ArrayList<>(2);
        for (Point2D point : inters0) {
            double pos = this.parabola.position(point);
            if (pos > this.t0 && pos < this.t1)
                inters.add(point);
        }

        return inters;
    }

    /**
     * Returns the parabola arc which refers to the reversed parent parabola, and with inverted parameterization bounds.
     */
    @Override
    public ParabolaArc2D reverse() {
        return new ParabolaArc2D(this.parabola.reverse(), -t1, -t0);
    }

    @Override
    public ParabolaArc2D subCurve(double t0, double t1) {
        if (t1 < t0)
            return null;
        t0 = max(this.t0, t0);
        t1 = min(this.t1, t1);
        return new ParabolaArc2D(parabola, t0, t1);
    }

    // ====================================================================
    // methods implementing the Shape2D interface

    @Override
    public double distance(Point2D p) {
        return distance(p.x(), p.y());
    }

    @Override
    public double distance(double x, double y) {
        // TODO Auto-generated method stub
        return this.asPolyline(100).distance(x, y);
    }

    /**
     * Returns true if the arc is bounded, i.e. if both limits are finite.
     */
    @Override
    public boolean isBounded() {
        if (t0 == Double.NEGATIVE_INFINITY)
            return false;
        if (t1 == Double.POSITIVE_INFINITY)
            return false;
        return true;
    }

    /**
     * Return true if t1<t0.
     */
    @Override
    public boolean isEmpty() {
        return t1 <= t0;
    }

    /**
     * Clip the parabola arc by a box. The result is an instance of CurveSet2D<ParabolaArc2D>, which contains only instances of ParabolaArc2D. If the parabola arc is not clipped, the result is an instance of CurveSet2D<ParabolaArc2D> which contains 0 curves.
     */
    @Override
    public ICurveSet2D<? extends ParabolaArc2D> clip(Box2D box) {
        // Clip the curve
        ICurveSet2D<ISmoothCurve2D> set = Curves2DUtil.clipSmoothCurve(this, box);

        // Stores the result in appropriate structure
        CurveArray2D<ParabolaArc2D> result = new CurveArray2D<>(set.size());

        // convert the result
        for (ICurve2D curve : set.curves()) {
            if (curve instanceof ParabolaArc2D)
                result.add((ParabolaArc2D) curve);
        }
        return result;
    }

    @Override
    public Box2D boundingBox() {
        // TODO Auto-generated method stub
        return this.asPolyline(100).boundingBox();
    }

    @Override
    public ParabolaArc2D transform(AffineTransform2D trans) {
        Parabola2D par = parabola.transform(trans);

        // Compute position of end points on the transformed parabola
        double startPos = Double.isInfinite(t0) ? Double.NEGATIVE_INFINITY : par.project(this.firstPoint().transform(trans));
        double endPos = Double.isInfinite(t1) ? Double.POSITIVE_INFINITY : par.project(this.lastPoint().transform(trans));

        // Compute the new arc
        return new ParabolaArc2D(par, startPos, endPos);
    }

    // ====================================================================
    // methods implementing the Shape interface

    @Override
    public boolean contains(double x, double y) {
        // Check on parent parabola
        if (!parabola.contains(x, y))
            return false;

        // Check if position of point is inside of bounds
        double t = parabola.position(new Point2D(x, y));
        if (t < this.t0)
            return false;
        if (t > this.t1)
            return false;

        return true;
    }

    @Override
    public boolean contains(Point2D point) {
        return contains(point.x(), point.y());
    }

    // ====================================================================
    // Drawing methods

    @Override
    public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path) {
        // Check curve is bounded
        if (!this.isBounded())
            throw new UnboundedShape2DException(this);

        // Compute position and tangent at extremities
        Point2D p1 = this.firstPoint();
        Point2D p2 = this.lastPoint();
        Vector2D v1 = this.tangent(this.t0);
        Vector2D v2 = this.tangent(this.t1);

        // Compute tangent lines at extremities
        StraightLine2D line1 = new StraightLine2D(p1, v1);
        StraightLine2D line2 = new StraightLine2D(p2, v2);

        // Compute intersection point of tangent lines
        Point2D pc = line1.intersection(line2);

        // Use quadratic curve to represent (exactly) the parabola arc
        path.quadTo(pc.x(), pc.y(), p2.x(), p2.y());
        return path;
    }

    public java.awt.geom.GeneralPath getGeneralPath() {
        if (!this.isBounded())
            throw new UnboundedShape2DException(this);
        return this.asPolyline(32).asGeneralPath();
    }

    // ===================================================================
    // methods implementing the GeometricObject2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
     */
    @Override
    public boolean almostEquals(IGeometricObject2D obj, double eps) {
        if (this == obj)
            return true;

        if (!(obj instanceof ParabolaArc2D))
            return false;
        ParabolaArc2D arc = (ParabolaArc2D) obj;

        if (!this.parabola.almostEquals(arc.parabola, eps))
            return false;
        if (Math.abs(this.t0 - arc.t0) > eps)
            return false;
        if (Math.abs(this.t1 - arc.t1) > eps)
            return false;

        return true;
    }

    // ====================================================================
    // Methods inherited from object interface

    @Override
    public String toString() {
        return "ParabolaArc2D(" + parabola.getVertex() + "," + parabola.getParameter() + "," + parabola.getAngle() + "," + t0 + "," + t1 + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((parabola == null) ? 0 : parabola.hashCode());
        long temp;
        temp = Double.doubleToLongBits(t0);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(t1);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ParabolaArc2D other = (ParabolaArc2D) obj;
        if (parabola == null) {
            if (other.parabola != null)
                return false;
        } else if (!parabola.equals(other.parabola))
            return false;
        if (Double.doubleToLongBits(t0) != Double.doubleToLongBits(other.t0))
            return false;
        if (Double.doubleToLongBits(t1) != Double.doubleToLongBits(other.t1))
            return false;
        return true;
    }

}
