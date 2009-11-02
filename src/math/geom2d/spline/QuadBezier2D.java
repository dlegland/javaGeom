/* File QuadBezier2D.java 
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
 */

package math.geom2d.spline;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.Curve2DUtils;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.domain.ContinuousOrientedCurve2D;
import math.geom2d.line.StraightLine2D;

/**
 * An extension of the QuadCurve2D curve provided in java.awt.geom, with support
 * for SmoothCurve2D and OrientedCurve2D.
 * 
 * @deprecated replaced by QuadBezierCurve2D (0.7.1)
 * @author Legland
 */
@Deprecated
public class QuadBezier2D extends QuadBezierCurve2D
implements SmoothCurve2D, ContinuousOrientedCurve2D, Cloneable {

    private static final long serialVersionUID = 1L;

    // ===================================================================
    // constructors

    public QuadBezier2D() {
        this(0, 0, 0, 0, 0, 0);
    }

    /**
     * Build a new Bezier curve from its array of coefficients. The array must
     * have size 2*3.
     * 
     * @param coefs the coefficients of the QuadBezier2D.
     */
    public QuadBezier2D(double[][] coefs) {
        this(coefs[0][0], coefs[1][0], coefs[0][0]+coefs[0][1]/2.0, coefs[1][0]
                +coefs[1][1]/2.0, coefs[0][0]+coefs[0][1]+coefs[0][2],
                coefs[1][0]+coefs[1][1]+coefs[1][2]);
    }

    /**
     * Build a new quadratic Bezier curve by specifying position of extreme
     * points and position of control point. The resulting curve is totally
     * contained in the convex polygon formed by the 3 control points.
     * 
     * @param p1 first point
     * @param ctrl control point
     * @param p2 last point
     */
    public QuadBezier2D(java.awt.geom.Point2D p1, java.awt.geom.Point2D ctrl,
            java.awt.geom.Point2D p2) {
        this(p1.getX(), p1.getY(), ctrl.getX(), ctrl.getY(), p2.getX(), p2
                .getY());
    }

    public QuadBezier2D(java.awt.geom.Point2D[] pts) {
        this(pts[0].getX(), pts[0].getY(), pts[1].getX(), pts[1].getY(), pts[2]
                .getX(), pts[2].getY());
    }

    /**
     * Build a new quadratic Bezier curve by specifying position of extreme
     * points and position of control point. The resulting curve is totally
     * contained in the convex polygon formed by the 3 control points.
     */
    public QuadBezier2D(double x1, double y1, double xctrl, double yctrl,
            double x2, double y2) {
        super(x1, y1, xctrl, yctrl, x2, y2);
    }


    /**
     * Returns the bezier curve given by control points taken in reverse order.
     */
    @Override
    public QuadBezier2D getReverseCurve() {
        return new QuadBezier2D(this.getP2(), this.getControl(), this.getP1());
    }

    /**
     * Computes portion of BezierCurve. If t1<t0, returns null.
     */
    @Override
    public QuadBezier2D getSubCurve(double t0, double t1) {
        t0 = Math.max(t0, 0);
        t1 = Math.min(t1, 1);
        if (t0>t1)
            return null;

        // Extreme points
        Point2D p0 = getPoint(t0);
        Point2D p1 = getPoint(t1);

        // tangent vectors at extreme points
        Vector2D v0 = getTangent(t0);
        Vector2D v1 = getTangent(t1);

        // compute position of control point as intersection of tangent lines
        StraightLine2D tan0 = new StraightLine2D(p0, v0);
        StraightLine2D tan1 = new StraightLine2D(p1, v1);
        Point2D control = tan0.getIntersection(tan1);

        // build the new quad curve
        return new QuadBezier2D(p0, control, p1);
    }

    /**
     * Clip the circle arc by a box. The result is an instance of
     * ContinuousOrientedCurveSet2D<QuadBezier2D>, which contains only
     * instances of EllipseArc2D. If the ellipse arc is not clipped, the result
     * is an instance of ContinuousOrientedCurveSet2D<QuadBezier2D>
     * which contains 0 curves.
     */
    @Override
    public CurveSet2D<? extends QuadBezier2D> clip(Box2D box) {
        // Clip the curve
        CurveSet2D<SmoothCurve2D> set = Curve2DUtils.clipSmoothCurve(this, box);

        // Stores the result in appropriate structure
        CurveSet2D<QuadBezier2D> result = new CurveSet2D<QuadBezier2D>();

        // convert the result
        for (Curve2D curve : set.getCurves()) {
            if (curve instanceof QuadBezier2D)
                result.addCurve((QuadBezier2D) curve);
        }
        return result;
    }

    /**
     * Returns the Bezier Curve transformed by the given AffineTransform2D. This
     * is simply done by transforming control points of the curve.
     */
    @Override
    public QuadBezier2D transform(AffineTransform2D trans) {
        return new QuadBezier2D(
        		trans.transform(this.getP1()),
        		trans.transform(this.getControl()), 
        		trans.transform(this.getP2()));
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof java.awt.geom.QuadCurve2D.Double))
            return false;
        
        java.awt.geom.QuadCurve2D.Double bezier = 
            (java.awt.geom.QuadCurve2D.Double) obj;
        if(Math.abs(this.x1-bezier.x1)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.y1-bezier.y1)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.ctrlx-bezier.ctrlx)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.ctrly-bezier.ctrly)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.x2-bezier.x2)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.y2-bezier.y2)>Shape2D.ACCURACY) return false;
        
        return true;
    }
    
	@Override
    public QuadBezier2D clone() {
        return new QuadBezier2D(x1, y1, ctrlx, ctrly, x2, y2);
    }
}
