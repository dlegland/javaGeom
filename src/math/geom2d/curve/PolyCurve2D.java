/* file : PolyCurve2D.java
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

package math.geom2d.curve;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.polygon.Polyline2D;

/**
 * A PolyCurve2D is a set of piecewise smooth curve arcs, such that the end of a
 * curve is the beginning of the next curve, and such that they do not intersect
 * nor self-intersect.
 * <p>
 * 
 * @author dlegland
 */
public class PolyCurve2D<T extends ContinuousCurve2D> extends CurveArray2D<T>
        implements ContinuousCurve2D {

    /** flag for indicating if the curve is closed or not (default is open) */
    protected boolean closed = false;

    // ===================================================================
    // Constructors

    public PolyCurve2D() {
    }
    
    public PolyCurve2D(int n) {
    	super(n);
    }

    public PolyCurve2D(T[] curves) {
        super(curves);
    }

    public PolyCurve2D(T[] curves, boolean closed) {
        super(curves);
        this.closed = closed;
    }

    public PolyCurve2D(Collection<? extends T> curves) {
        super(curves);
    }

    public PolyCurve2D(Collection<? extends T> curves, boolean closed) {
        super(curves);
        this.closed = closed;
    }

    // ===================================================================
    // Methods specific to PolyCurve2D

    /**
     * Toggle the 'closed' flag of the polycurve.
     */
    public void setClosed(boolean b) {
        closed = b;
    }

    // ===================================================================
    // Methods implementing the ContinuousCurve2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#getLeftTangent(double)
	 */
	public Vector2D getLeftTangent(double t) {
		return this.getChildCurve(t).getLeftTangent(this.getLocalPosition(t));
	}

	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#getRightTangent(double)
	 */
	public Vector2D getRightTangent(double t) {
		return this.getChildCurve(t).getRightTangent(this.getLocalPosition(t));
	}

	/**
     * Returns true if the PolyCurve2D is closed.
     */
    public boolean isClosed() {
        return closed;
    }

    public Polyline2D getAsPolyline(int n) {
        Point2D[] points = new Point2D[n+1];
        double t0 = this.getT0();
        double t1 = this.getT1();
        double dt = (t1-t0)/n;
        for (int i = 0; i<n; i++)
            points[i] = this.getPoint(i*dt+t0);
        return new Polyline2D(points);
    }

    /**
     * Returns a collection containing only instances of SmoothCurve2D.
     * 
     * @return a collection of SmoothCurve2D
     */
    public Collection<? extends SmoothCurve2D> getSmoothPieces() {
        ArrayList<SmoothCurve2D> list = new ArrayList<SmoothCurve2D>();
        for (Curve2D curve : this.curves)
            list.addAll(PolyCurve2D.getSmoothCurves(curve));
        return list;
    }

    /**
     * Returns a collection containing only instances of SmoothCurve2D.
     * 
     * @param curve the curve to decompose
     * @return a collection of SmoothCurve2D
     */
    private final static Collection<SmoothCurve2D> getSmoothCurves(Curve2D curve) {
    	// create array for result
        ArrayList<SmoothCurve2D> array = new ArrayList<SmoothCurve2D>();

        // If curve is smooth, add it to the array and return.
        if (curve instanceof SmoothCurve2D) {
            array.add((SmoothCurve2D) curve);
            return array;
        }

        // Otherwise, iterate on curves of the curve set
        if (curve instanceof CurveSet2D) {
            for (Curve2D curve2 : ((CurveSet2D<?>) curve).getCurves())
                array.addAll(getSmoothCurves(curve2));
            return array;
        }

        if (curve==null)
            return array;

        System.err.println("could not find smooth parts of curve with class "
                +curve.getClass().getName());
        return array;
    }

    // ===================================================================
    // Methods implementing the ContinuousCurve2D interface

    @Override
    public Collection<? extends PolyCurve2D<?>> getContinuousCurves() {
        ArrayList<PolyCurve2D<T>> list = new ArrayList<PolyCurve2D<T>>(1);
        list.add(this);
        return list;
    }

   @Override
    public PolyCurve2D<? extends ContinuousCurve2D> getReverseCurve() {
    	// create array for storing reversed curves
    	int n = curves.size();
        ContinuousCurve2D[] curves2 = new ContinuousCurve2D[n];
        
        // reverse each curve
        for (int i = 0; i<n; i++)
            curves2[i] = curves.get(n-1-i).getReverseCurve();
        
        // create the new reversed curve
        return new PolyCurve2D<ContinuousCurve2D>(curves2);
    }

    /**
     * Returns an instance of PolyCurve2D. If t0>t1 and curve is not closed,
     * return a PolyCurve2D without curves inside.
     */
    @Override
    public PolyCurve2D<? extends ContinuousCurve2D> getSubCurve(double t0,
            double t1) {
        // check limit conditions
        if (t1<t0&!this.isClosed())
            return new PolyCurve2D<ContinuousCurve2D>();

        // Call the parent method
        CurveSet2D<?> set = super.getSubCurve(t0, t1);
        
        // create result object, with appropriate numbe of curves
        PolyCurve2D<ContinuousCurve2D> subCurve = 
        	new PolyCurve2D<ContinuousCurve2D>(set.getCurveNumber());

        // If a part is selected, the result is obviously open
        subCurve.setClosed(false);

        // convert to PolySmoothCurve by adding curves, after class cast
        for (Curve2D curve : set.getCurves())
            subCurve.addCurve((ContinuousCurve2D) curve);

        // return the resulting portion of curve
        return subCurve;
    }

    /**
     * Clip the PolyCurve2D by a box. The result is an instance of CurveSet2D<ContinuousCurve2D>,
     * which contains only instances of ContinuousCurve2D. If the PolyCurve2D is
     * not clipped, the result is an instance of CurveSet2D<ContinuousCurve2D>
     * which contains 0 curves.
     */
    @Override
    public CurveSet2D<? extends ContinuousCurve2D> clip(Box2D box) {
        // Clip the curve
        CurveSet2D<? extends Curve2D> set = Curve2DUtils.clipCurve(this, box);

        // Stores the result in appropriate structure
        CurveArray2D<ContinuousCurve2D> result = 
        	new CurveArray2D<ContinuousCurve2D>(set.getCurveNumber());

        // convert the result
        for (Curve2D curve : set.getCurves()) {
            if (curve instanceof ContinuousCurve2D)
                result.addCurve((ContinuousCurve2D) curve);
        }
        return result;
    }

    @Override
    public PolyCurve2D<? extends ContinuousCurve2D> transform(
            AffineTransform2D trans) {
        PolyCurve2D<ContinuousCurve2D> result = new PolyCurve2D<ContinuousCurve2D>();
        for (ContinuousCurve2D curve : curves)
            result.addCurve(curve.transform(trans));
        result.setClosed(this.isClosed());
        return result;
    }

    public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path) {
        Point2D point;
        for (ContinuousCurve2D curve : getCurves()) {
            point = curve.getPoint(curve.getT0());
            path.lineTo((float) point.getX(), (float) point.getY());
            curve.appendPath(path);
        }

        // eventually close the curve
        if (closed) {
            point = this.getFirstPoint();
            path.lineTo((float) point.getX(), (float) point.getY());
        }

        return path;
    }

    @Override
    public java.awt.geom.GeneralPath getGeneralPath() {
        // create new path
        java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();

        // avoid degenerate case
        if (curves.size()==0)
            return path;

        // move to the first point
        Point2D start, current;
        start = this.getFirstPoint();
        path.moveTo((float) start.getX(), (float) start.getY());
        current = start;

        // add the path of the first curve
        for(ContinuousCurve2D curve : curves) {
        	start = curve.getFirstPoint();
        	if (start.distance(current)>Shape2D.ACCURACY)
        		path.lineTo((float) start.getX(), (float) start.getY());
        	path = curve.appendPath(path);
        	current = start;
        }
        
        // eventually closes the curve
        if (closed) {
            path.closePath();
        }

        // return the final path
        return path;
    }
    
    @Override
     public void draw(Graphics2D g2) {
    	g2.draw(this.getGeneralPath());
    }

    @Override
    public boolean equals(Object obj) {
        // check class, and cast type
        if (!(obj instanceof CurveSet2D))
            return false;
        PolyCurve2D<?> curveSet = (PolyCurve2D<?>) obj;

        // check the number of curves in each set
        if (this.getCurveNumber()!=curveSet.getCurveNumber())
            return false;

        // return false if at least one couple of curves does not match
        for(int i=0; i<curves.size(); i++)
            if(!this.curves.get(i).equals(curveSet.curves.get(i)))
                return false;
        
        // otherwise return true
        return true;
    }

}
