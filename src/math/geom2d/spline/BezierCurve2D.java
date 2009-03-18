/* File BezierCurve2D.java 
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
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.Curve2DUtils;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.SmoothCurve2D;

/**
 * An extension of the Bezier curve provided in java.awt.geom, with support for
 * SmoothCurve2D and OrientedCurve2D.
 * 
 * @deprecated replaced by CubicBezierCurve2D (0.7.1)
 * @author Legland
 */
@Deprecated
public class BezierCurve2D extends CubicBezierCurve2D implements Cloneable {
//TODO: transform as interface for cubic and quad bezier curves ?
    /**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

  
    // ===================================================================
    // constructors

	public BezierCurve2D() {
        this(0, 0, 0, 0, 0, 0, 0, 0);
    }

    /**
     * Build a new Bezier curve from its array of coefficients. The array must
     * have size 2*4.
     * 
     * @param coefs the coefficients of the BezierCurve2D.
     */
    public BezierCurve2D(double[][] coefs) {
    	this(coefs[0][0], coefs[1][0], 
    			coefs[0][0]+coefs[0][1]/3.0, 
    			coefs[1][0]+coefs[1][1]/3.0,
    			coefs[0][0]+2*coefs[0][1]/3.0+coefs[0][2]/3.0, 
    			coefs[1][0]+2*coefs[1][1]/3.0+coefs[1][2]/3.0, 
    			coefs[0][0]+coefs[0][1]+coefs[0][2]+coefs[0][3], 
    			coefs[1][0]+coefs[1][1]+coefs[1][2]+coefs[1][3]);
    }

    /**
     * Build a new Bezier curve of degree 3 by specifying position of extreme
     * points and position of 2 control points. The resulting curve is totally
     * contained in the convex polygon formed by the 4 control points.
     * 
     * @param p1 first point
     * @param ctrl1 first control point
     * @param ctrl2 second control point
     * @param p2 last point
     */
    public BezierCurve2D(java.awt.geom.Point2D p1, java.awt.geom.Point2D ctrl1,
            java.awt.geom.Point2D ctrl2, java.awt.geom.Point2D p2) {
        this(p1.getX(), p1.getY(), ctrl1.getX(), ctrl1.getY(), ctrl2.getX(),
                ctrl2.getY(), p2.getX(), p2.getY());
    }

    /**
     * Build a new Bezier curve of degree 3 by specifying position and tangent
     * of first and last points.
     * 
     * @param p1 first point
     * @param v1 first tangent vector
     * @param p2 position of last point
     * @param v2 last tangent vector
     */
    public BezierCurve2D(
    		java.awt.geom.Point2D p1, Vector2D v1,
            java.awt.geom.Point2D p2, Vector2D v2) {
    	this(p1.getX(), p1.getY(), p1.getX()+v1.getX()/3,
    			p1.getY()+v1.getY()/3, p2.getX()-v2.getX()/3, 
    			p2.getY()-v2.getY()/3, p2.getX(), p2.getY());
    }

    /**
     * Build a new Bezier curve of degree 3 by specifying position of extreme
     * points and position of 2 control points. The resulting curve is totally
     * containe in the convex polygon formed by the 4 control points.
     */
    public BezierCurve2D(double x1, double y1, double xctrl1, double yctrl1,
            double xctrl2, double yctrl2, double x2, double y2) {
        super(x1, y1, xctrl1, yctrl1, xctrl2, yctrl2, x2, y2);
    }

    // ===================================================================
    // methods specific to BezierCurve2D

    /**
     * Returns the Bezier curve given by control points taken in reverse
     * order.
     */
    public BezierCurve2D getReverseCurve() {
        return new BezierCurve2D(
                this.getP2(), this.getCtrlP2(),
                this.getCtrlP1(), this.getP1());
    }

    /**
     * Computes portion of BezierCurve. If t1<t0, returns null.
     */
    public BezierCurve2D getSubCurve(double t0, double t1) {
        t0 = Math.max(t0, 0);
        t1 = Math.min(t1, 1);
        if (t0>t1)
            return null;

        double dt = t1-t0;
        Vector2D v0 = getTangent(t0).times(dt);
        Vector2D v1 = getTangent(t1).times(dt);
        return new BezierCurve2D(getPoint(t0), v0, getPoint(t1), v1);
    }

    /**
     * Clip the Bezier curve by a box. REturn a set of BezierCurve2D.
     */
    public CurveSet2D<? extends BezierCurve2D> clip(Box2D box) {
        // Clip the curve
        CurveSet2D<SmoothCurve2D> set = 
            Curve2DUtils.clipSmoothCurve(this, box);

        // Stores the result in appropriate structure
        CurveSet2D<BezierCurve2D> result = new CurveSet2D<BezierCurve2D>();

        // convert the result
        for (Curve2D curve : set.getCurves()) {
            if (curve instanceof BezierCurve2D)
                result.addCurve((BezierCurve2D) curve);
        }
        return result;
    }

    /**
     * Returns the Bezier Curve transformed by the given AffineTransform2D. This
     * is simply done by transforming control points of the curve.
     */
    public BezierCurve2D transform(AffineTransform2D trans) {
        return new BezierCurve2D(
                trans.transform(this.getP1()), 
                trans.transform(this.getCtrlP1()),
                trans.transform(this.getCtrlP2()),
                trans.transform(this.getP2()));
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof java.awt.geom.CubicCurve2D.Double))
            return false;
        
        java.awt.geom.CubicCurve2D.Double bezier = 
            (java.awt.geom.CubicCurve2D.Double) obj;
        if(Math.abs(this.x1-bezier.x1)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.y1-bezier.y1)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.ctrlx1-bezier.ctrlx1)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.ctrly1-bezier.ctrly1)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.ctrlx2-bezier.ctrlx2)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.ctrly2-bezier.ctrly2)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.x2-bezier.x2)>Shape2D.ACCURACY) return false;
        if(Math.abs(this.y2-bezier.y2)>Shape2D.ACCURACY) return false;
        
        return true;
    }
    
    @Override
    public BezierCurve2D clone() {
        return new BezierCurve2D(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
    }
}
