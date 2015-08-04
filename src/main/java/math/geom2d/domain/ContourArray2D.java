/* file : ContourArray2D.java
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
 * Created on 1 mai 2006
 *
 */

package math.geom2d.domain;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.curve.*;

/**
 * A ContourArray2D is a set of contours. Each contour in the set defines its
 * own domain.
 * <p>
 * 
 * @author dlegland
 */
public class ContourArray2D<T extends Contour2D> extends CurveArray2D<T>
implements Boundary2D {

    // ===================================================================
    // static methods

    /**
     * Static factory for creating a new ContourArray2D from a collection of
     * contours.
     * @since 0.8.1
     */
    /*public static <T extends Contour2D> ContourArray2D<T> create(
    		Collection<T> curves) {
    	return new ContourArray2D<T>(curves);
    }*/
    
    /**
     * Static factory for creating a new ContourArray2D from an array of
     * contours.
     * @since 0.8.1
     */
	@SafeVarargs
    public static <T extends Contour2D> ContourArray2D<T> create(
    		T... curves) {
    	return new ContourArray2D<T>(curves);
    }

    // ===================================================================
    // Constructors

    public ContourArray2D() {
    }

    public ContourArray2D(int size) {
    	super(size);
    }

	@SafeVarargs
    public ContourArray2D(T... curves) {
        super(curves);
    }

    public ContourArray2D(Collection<? extends T> curves) {
        super(curves);
    }

    public ContourArray2D(T curve) {
        super();
        this.add(curve);
    }

    
    // ===================================================================
    // Methods implementing Boundary2D interface

    public Collection<? extends T> continuousCurves() {
    	return Collections.unmodifiableCollection(this.curves);
    }

    public Domain2D domain() {
        return new GenericDomain2D(this);
    }

    public void fill(Graphics2D g2) {
        g2.fill(this.getGeneralPath());
    }

    // ===================================================================
    // Methods implementing OrientedCurve2D interface

    public double windingAngle(Point2D point) {
        double angle = 0;
        for (OrientedCurve2D curve : this.curves())
            angle += curve.windingAngle(point);
        return angle;
    }

    public double signedDistance(Point2D p) {
        return signedDistance(p.x(), p.y());
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#signedDistance(math.geom2d.Point2D)
     */
    public double signedDistance(double x, double y) {
        double minDist = Double.POSITIVE_INFINITY;
        double dist = Double.POSITIVE_INFINITY;

        for (OrientedCurve2D curve : this.curves()) {
            dist = Math.min(dist, curve.signedDistance(x, y));
            if (Math.abs(dist)<Math.abs(minDist))
                minDist = dist;
        }
        return minDist;
    }

    public boolean isInside(Point2D point) {
        return this.signedDistance(point.x(), point.y()) < 0;
    }

    // ===================================================================
    // Methods implementing Curve2D interface

    @Override
    public ContourArray2D<? extends Contour2D> reverse() {
        Contour2D[] curves2 = new Contour2D[curves.size()];
        int n = curves.size();
        for (int i = 0; i<n; i++)
            curves2[i] = curves.get(n-1-i).reverse();
        return new ContourArray2D<Contour2D>(curves2);
    }

    @Override
    public CurveSet2D<? extends ContinuousOrientedCurve2D> subCurve(
            double t0, double t1) {
        // get the subcurve
        CurveSet2D<? extends Curve2D> curveSet = super.subCurve(t0, t1);

        // create subcurve array
        ArrayList<ContinuousOrientedCurve2D> curves = 
        	new ArrayList<ContinuousOrientedCurve2D>();
        for (Curve2D curve : curveSet.curves())
            curves.add((ContinuousOrientedCurve2D) curve);

        // Create CurveSet for the result
        return new CurveArray2D<ContinuousOrientedCurve2D>(curves);
    }

    // ===================================================================
    // Methods implementing the Shape2D interface

    /**
     * Clip the curve by a box. The result is an instance of
     * CurveSet2D<ContinuousOrientedCurve2D>, which contains
     * only instances of ContinuousOrientedCurve2D. 
     * If the curve is not clipped, the result is an instance of 
     * CurveSet2D<ContinuousOrientedCurve2D> which contains 0 curves.
     */
    @Override
    public CurveSet2D<? extends ContinuousOrientedCurve2D> clip(Box2D box) {
        // Clip the curve
        CurveSet2D<? extends Curve2D> set = Curves2D.clipCurve(this, box);

        // Stores the result in appropriate structure
        CurveArray2D<ContinuousOrientedCurve2D> result = 
        	new CurveArray2D<ContinuousOrientedCurve2D>(set.size());

        // convert the result
        for (Curve2D curve : set.curves()) {
            if (curve instanceof ContinuousOrientedCurve2D)
                result.add((ContinuousOrientedCurve2D) curve);
        }
        return result;
    }

    @Override
    public ContourArray2D<? extends Contour2D> transform(
            AffineTransform2D trans) {
        ContourArray2D<Contour2D> result = 
        	new ContourArray2D<Contour2D>(curves.size());
        for (Curve2D curve : curves)
            result.add((Contour2D) curve.transform(trans));
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        // check class
        if (!(obj instanceof ContourArray2D<?>))
            return false;
        // call superclass method
        return super.equals(obj);
    }

}
