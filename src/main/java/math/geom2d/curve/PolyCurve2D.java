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

    // ===================================================================
    // static factories

    /**
     * Static factory for creating a new PolyCurve2D from a collection of
     * curves.
     * @since 0.8.1
     */
    /*public static <T extends ContinuousCurve2D> PolyCurve2D<T> create(
    		Collection<T> curves) {
    	return new PolyCurve2D<T>(curves);
    }*/
    
    /**
     * Static factory for creating a new PolyCurve2D from an array of
     * curves.
     * @since 0.8.1
     */
	@SafeVarargs
    public static <T extends ContinuousCurve2D> PolyCurve2D<T> create(
    		T... curves) {
    	return new PolyCurve2D<T>(curves);
    }

    /**
     * Static factory for creating a new closed PolyCurve2D from an array of
     * curves.
     * @since 0.10.0
     */
	@SafeVarargs
    public static <T extends ContinuousCurve2D> PolyCurve2D<T> createClosed(
    		T... curves) {
    	return new PolyCurve2D<T>(curves, true);
    }

    /**
     * Static factory for creating a new PolyCurve2D from a collection of
     * curves and a flag indicating if the curve is closed or not.
     * @since 0.9.0
     */
    public static <T extends ContinuousCurve2D> PolyCurve2D<T> create(
    		Collection<T> curves, boolean closed) {
    	return new PolyCurve2D<T>(curves, closed);
    }
    
    /**
     * Static factory for creating a new PolyCurve2D from an array of
     * curves and a flag indicating if the curve is closed or not.
     * @since 0.9.0
     */
    public static <T extends ContinuousCurve2D> PolyCurve2D<T> create(
    		T[] curves, boolean closed) {
    	return new PolyCurve2D<T>(curves, closed);
    }

	/**
	 * Shortcut function to convert a curve of class T to a collection of T
	 * containing only the input curve.
	 */
	protected static <T extends ContinuousCurve2D> Collection<T> wrapCurve(T curve) {
		ArrayList<T> list = new ArrayList<T> (1);
		list.add(curve);
		return list;
	}
	
   
    // ===================================================================
    // class variables

    /** flag for indicating if the curve is closed or not (default is false, for open) */
    protected boolean closed = false;

    // ===================================================================
    // Constructors

    /**
     * Empty constructor.
     */
    public PolyCurve2D() {
    }
    
    /**
     * Constructor that reserves space for the specified number of inner curves. 
     */
    public PolyCurve2D(int n) {
    	super(n);
    }

    /**
     * Creates a new PolyCurve2D from the specified list of curves.
     * @param curves the curves that constitutes this PolyCurve2D
     */
	@SafeVarargs
    public PolyCurve2D(T... curves) {
        super(curves);
    }

    /**
     * Creates a new closed PolyCurve2D from the specified list of curves.
     * @param curves the curves that constitutes this PolyCurve2D
     */
    public PolyCurve2D(T[] curves, boolean closed) {
        super(curves);
        this.closed = closed;
    }

    /**
     * Creates a new PolyCurve2D from the specified collection of curves.
     * @param curves the curves that constitutes this PolyCurve2D
     */
    public PolyCurve2D(Collection<? extends T> curves) {
        super(curves);
    }

    /**
     * Creates a new PolyCurve2D from the specified collection of curves.
     * @param curves the curves that constitutes this PolyCurve2D
     */
    public PolyCurve2D(Collection<? extends T> curves, boolean closed) {
        super(curves);
        this.closed = closed;
    }

    /**
     * Copy constructor of PolyCurve2D.
     * @param polyCurve the polyCurve object to copy.
     */
    public PolyCurve2D(PolyCurve2D<? extends T> polyCurve) {
    	super(polyCurve.curves);
        this.closed = polyCurve.closed;
    }
    
    // ===================================================================
    // Methods specific to PolyCurve2D

    /**
     * Toggle the 'closed' flag of this polycurve.
     */
    public void setClosed(boolean b) {
        closed = b;
    }

    
    // ===================================================================
    // Methods implementing the ContinuousCurve2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#leftTangent(double)
	 */
	public Vector2D leftTangent(double t) {
		return this.childCurve(t).leftTangent(this.localPosition(t));
	}

	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#rightTangent(double)
	 */
	public Vector2D rightTangent(double t) {
		return this.childCurve(t).rightTangent(this.localPosition(t));
	}

	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#leftTangent(double)
	 */
	public double curvature(double t) {
		return this.childCurve(t).curvature(this.localPosition(t));
	}

	/**
     * Returns true if the PolyCurve2D is closed.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Converts this PolyCurve2D into a polyline with the given number of edges.
     * @param n the number of edges of the resulting polyline
     * @see Polyline2D 
     */
    public Polyline2D asPolyline(int n) {
    	// allocate point array
        Point2D[] points = new Point2D[n+1];
        
        // get parameterisation bounds
        double t0 = this.t0();
        double t1 = this.t1();
		double dt = (t1 - t0) / n;
		
		// create vertices
		for (int i = 0; i < n; i++)
			points[i] = this.point(i * dt + t0);
		points[n] = this.lastPoint();
		
		// return new polyline
		return new Polyline2D(points);
	}

    /**
     * Returns a collection containing only instances of SmoothCurve2D.
     * 
     * @return a collection of SmoothCurve2D
     */
    public Collection<? extends SmoothCurve2D> smoothPieces() {
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
    private static Collection<SmoothCurve2D> getSmoothCurves(Curve2D curve) {
    	// create array for result
        ArrayList<SmoothCurve2D> array = new ArrayList<SmoothCurve2D>();

        // If curve is smooth, add it to the array and return.
        if (curve instanceof SmoothCurve2D) {
            array.add((SmoothCurve2D) curve);
            return array;
        }

        // Otherwise, iterate on curves of the curve set
        if (curve instanceof CurveSet2D<?>) {
            for (Curve2D curve2 : ((CurveSet2D<?>) curve).curves())
                array.addAll(getSmoothCurves(curve2));
            return array;
        }

        if (curve == null)
            return array;

        throw new IllegalArgumentException("could not find smooth parts of curve with class "
                + curve.getClass().getName());
    }

    // ===================================================================
    // Methods implementing the ContinuousCurve2D interface

    /**
     * Returns a collection of PolyCurve2D that contains only this instance.
     */
    @Override
    public Collection<? extends PolyCurve2D<?>> continuousCurves() {
    	return wrapCurve(this);
    }

    /**
     * Returns the reverse curve of this PolyCurve2D.
     */
   @Override
    public PolyCurve2D<? extends ContinuousCurve2D> reverse() {
    	// create array for storing reversed curves
    	int n = curves.size();
        ContinuousCurve2D[] curves2 = new ContinuousCurve2D[n];
        
        // reverse each curve
        for (int i = 0; i<n; i++)
            curves2[i] = curves.get(n-1-i).reverse();
        
        // create the new reversed curve
        return new PolyCurve2D<ContinuousCurve2D>(curves2, this.closed);
    }

    /**
     * Returns an instance of PolyCurve2D. If t0>t1 and curve is not closed,
     * return a PolyCurve2D without curves inside.
     */
    @Override
    public PolyCurve2D<? extends ContinuousCurve2D> subCurve(double t0,
            double t1) {
        // check limit conditions
        if (t1<t0&!this.isClosed())
            return new PolyCurve2D<ContinuousCurve2D>();

        // Call the parent method
        CurveSet2D<?> set = super.subCurve(t0, t1);
        
        // create result object, with appropriate numbe of curves
        PolyCurve2D<ContinuousCurve2D> subCurve = 
        	new PolyCurve2D<ContinuousCurve2D>(set.size());

        // If a part is selected, the result is obviously open
        subCurve.setClosed(false);

        // convert to PolySmoothCurve by adding curves, after class cast
        for (Curve2D curve : set.curves())
            subCurve.add((ContinuousCurve2D) curve);

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
        CurveSet2D<? extends Curve2D> set = Curves2D.clipCurve(this, box);

        // Stores the result in appropriate structure
        CurveArray2D<ContinuousCurve2D> result = 
        	new CurveArray2D<ContinuousCurve2D>(set.size());

        // convert the result
        for (Curve2D curve : set.curves()) {
            if (curve instanceof ContinuousCurve2D)
                result.add((ContinuousCurve2D) curve);
        }
        return result;
    }

    /**
     * Transforms each smooth piece in this PolyCurve2D and returns a new
     * instance of PolyCurve2D.
     */
    @Override
    public PolyCurve2D<? extends ContinuousCurve2D> transform(
            AffineTransform2D trans) {
        PolyCurve2D<ContinuousCurve2D> result = new PolyCurve2D<ContinuousCurve2D>();
        for (ContinuousCurve2D curve : curves)
            result.add(curve.transform(trans));
        result.setClosed(this.isClosed());
        return result;
    }

    public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path) {
        Point2D point;
        for (ContinuousCurve2D curve : curves()) {
            point = curve.point(curve.t0());
            path.lineTo((float) point.x(), (float) point.y());
            curve.appendPath(path);
        }

        // eventually close the curve
        if (closed) {
            point = this.firstPoint();
            path.lineTo((float) point.x(), (float) point.y());
        }

        return path;
    }

    /* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#getGeneralPath()
	 */
    @Override
    public java.awt.geom.GeneralPath getGeneralPath() {
        // create new path
        java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();

        // avoid degenerate case
        if (curves.size()==0)
            return path;

        // move to the first point
        Point2D start, current;
        start = this.firstPoint();
        path.moveTo((float) start.x(), (float) start.y());
        current = start;

        // add the path of the first curve
        for(ContinuousCurve2D curve : curves) {
        	start = curve.firstPoint();
			if (start.distance(current) > Shape2D.ACCURACY)
				path.lineTo((float) start.x(), (float) start.y());
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
    
	/* (non-Javadoc)
	 * @see math.geom2d.curve.Curve2D#draw(Graphics2D)
	 */
    @Override
     public void draw(Graphics2D g2) {
    	g2.draw(this.getGeneralPath());
    }

    @Override
    public boolean equals(Object obj) {
        // check class, and cast type
        if (!(obj instanceof CurveSet2D<?>))
            return false;
        PolyCurve2D<?> curveSet = (PolyCurve2D<?>) obj;

		// check the number of curves in each set
		if (this.size() != curveSet.size())
			return false;

		// return false if at least one couple of curves does not match
		for (int i = 0; i < curves.size(); i++)
			if (!this.curves.get(i).equals(curveSet.curves.get(i)))
				return false;

        // otherwise return true
        return true;
    }

}
