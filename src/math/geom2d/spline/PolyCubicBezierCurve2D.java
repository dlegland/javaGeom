/* file : PolyCubicBezierCurve2D.java
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
 * Created on 8 mai 2006
 *
 */

package math.geom2d.spline;

import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.Curve2DUtils;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.PolyCurve2D;

/**
 * A set of Bezier curves, making a continuous curve.
 * 
 * @author dlegland
 */
public class PolyCubicBezierCurve2D extends PolyCurve2D<CubicBezierCurve2D> {

    // ===================================================================
    // Static methods

    /**
     * Creates a series a cubic bezier curves, by grouping 4 adjacent points.
     * Two consecutive curves share one point, N curves will require 3*n+1
     * points.
     */
    public final static PolyCubicBezierCurve2D create(Point2D... points){
    	// number of points
    	int np = points.length;
    	
    	// compute number of curves
    	int nc = (np-1)/3;
    	
    	// create array of curves
    	PolyCubicBezierCurve2D polyBezier = new PolyCubicBezierCurve2D(nc);
    	
    	// build each curve
    	for(int i=0; i<nc; i++)
    		polyBezier.add(new CubicBezierCurve2D(
    				points[i*3], points[i*3+1], points[i*3+2], points[i*3+3]));
    	
    	// return the curve
    	return polyBezier;
    }

    
    /**
     * Creates a series a cubic bezier curves, by grouping consecutive couples
     * of points and vectors. A polycurve composed of N Bezier curves requires
     * N+1 points and N+1 vectors. 
     */
    public final static PolyCubicBezierCurve2D create(
    		Point2D[] points, Vector2D[] vectors){
    	// number of points
    	int np = Math.min(points.length, vectors.length);
    	
		// compute number of curves
		int nc = (np - 1) / 2;

		// create array of curves
		PolyCubicBezierCurve2D polyBezier = new PolyCubicBezierCurve2D(nc);

		// build each curve
		for (int i = 0; i < nc; i++)
			polyBezier.add(new CubicBezierCurve2D(
					points[i * 2], vectors[i*2], points[i*2+1], vectors[i*2+1]));
    	
    	// return the curve
    	return polyBezier;
    }

    
	// ===================================================================
    // Constructors

    public PolyCubicBezierCurve2D() {
        super();
    }

    public PolyCubicBezierCurve2D(int n) {
        super(n);
    }

    public PolyCubicBezierCurve2D(CubicBezierCurve2D... curves) {
        super(curves);
    }

    public PolyCubicBezierCurve2D(Collection<CubicBezierCurve2D> curves) {
        super(curves);
    }
    

    // ===================================================================
    // Methods specific to PolyCubicBezierCurve2D

    /**
     * Returns a new PolyCubicBezierCurve2D.
     */
    @Override
    public PolyCubicBezierCurve2D clip(Box2D box) {
        // Clip the curve
        CurveSet2D<? extends Curve2D> set = Curve2DUtils.clipCurve(this, box);

        // Stores the result in appropriate structure
        PolyCubicBezierCurve2D result = new PolyCubicBezierCurve2D(
        		set.size());

        // convert the result
        for (Curve2D curve : set.curves()) {
            if (curve instanceof CubicBezierCurve2D)
                result.add((CubicBezierCurve2D) curve);
        }
        return result;
    }

    @Override
    public PolyCubicBezierCurve2D transform(AffineTransform2D trans) {
        PolyCubicBezierCurve2D result = new PolyCubicBezierCurve2D(this.curves.size());
        for (CubicBezierCurve2D curve : curves)
            result.add(curve.transform(trans));
        return result;
    }

}
