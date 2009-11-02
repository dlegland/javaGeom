/* File CurveArray2D.java 
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

package math.geom2d.curve;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.line.LinearShape2D;

/**
 * <p>
 * A parameterized set of curves. A curve cannot be included twice in a
 * CurveArray2D.
 * </p>
 * <p>
 * The k-th curve contains points with positions between 2*k and 2*k+1. This
 * allows to differentiate extremities of contiguous curves. The points with
 * positions t between 2*k+1 and 2*k+2 belong to the curve k if t<2*k+1.5, or
 * to the curve k+1 if t>2*k+1.5
 * </p>
 * 
 * @author Legland
 */
public class CurveArray2D<T extends Curve2D> extends CurveSet2D<T> 
implements Iterable<T>, Cloneable {

//    /** The inner array of curves */
//    protected ArrayList<T> curves;

    // ===================================================================
    // Constructors

    /**
     * Empty constructor. Initializes an empty array of curves.
     */
    @SuppressWarnings("deprecation")
	public CurveArray2D() {
    	this.curves = new ArrayList<T>();
    }

    /**
     * Empty constructor. Initializes an empty array of curves, 
     * with a given size for allocating memory.
     */
    public CurveArray2D(int n) {
    	this();
    	this.curves = new ArrayList<T>(n);
    }

    /**
     * Constructor from an array of curves.
     * 
     * @param curves the array of curves in the set
     */
    public CurveArray2D(T[] curves) {
    	this();
    	this.curves = new ArrayList<T>(curves.length);
        for (T element : curves)
            this.addCurve(element);
    }

    /**
     * Constructor from a collection of curves. The curves are added to the
     * inner collection of curves.
     * 
     * @param curves the collection of curves to add to the set
     */
    public CurveArray2D(Collection<? extends T> curves) {
    	this();
    	this.curves = new ArrayList<T>(curves.size());
        this.curves.addAll(curves);
    }

    // ===================================================================
    // methods specific to CurveArray2D

    public static <T extends Curve2D> CurveArray2D<T> create(
    		Collection<T> curves) {
    	return new CurveArray2D<T>(curves);
    }
    
    public static <T extends Curve2D> CurveArray2D<T> create(
    		T[] curves) {
    	return new CurveArray2D<T>(curves);
    }
    
    /**
     * Converts the position on the curve set, which is comprised between 0 and
     * 2*Nc-1 with Nc being the number of curves, to the position on the curve
     * which contains the position. The result is comprised between the t0 and
     * the t1 of the child curve.
     * 
     * @see #getGlobalPosition(int, double)
     * @see #getCurveIndex(double)
     * @param t the position on the curve set
     * @return the position on the subcurve
     */
    @Override
	public double getLocalPosition(double t) {
        int i = this.getCurveIndex(t);
        T curve = curves.get(i);
        double t0 = curve.getT0();
        double t1 = curve.getT1();
        return Curve2DUtils.fromUnitSegment(t-2*i, t0, t1);
    }

    /**
     * Converts a position on a curve (between t0 and t1 of the curve) to the
     * position on the curve set (between 0 and 2*Nc-1).
     * 
     * @see #getLocalPosition(double)
     * @see #getCurveIndex(double)
     * @param i the index of the curve to consider
     * @param t the position on the curve
     * @return the position on the curve set, between 0 and 2*Nc-1
     */
    @Override
	public double getGlobalPosition(int i, double t) {
        T curve = curves.get(i);
        double t0 = curve.getT0();
        double t1 = curve.getT1();
        return Curve2DUtils.toUnitSegment(t, t0, t1)+i*2;
    }

    /**
     * Returns the index of the curve corresponding to a given position.
     * 
     * @param t the position on the set of curves, between 0 and twice the
     *            number of curves minus 1
     * @return the index of the curve which contains position t
     */
    @Override
	public int getCurveIndex(double t) {

        // check bounds
        if (curves.size()==0)
            return 0;
        if (t>curves.size()*2-1)
            return curves.size()-1;

        // curve index
        int nc = (int) Math.floor(t);

        // check index if even-> corresponds to a curve
        int indc = (int) Math.floor(nc/2);
        if (indc*2==nc)
            return indc;
        else
            return t-nc<.5 ? indc : indc+1;
    }

    // ===================================================================
    // Management of curves

    /**
     * Adds the curve to the curve set, if it does not already belongs to the
     * set.
     * 
     * @param curve the curve to add
     */
    @Override
	public void addCurve(T curve) {
        if (!curves.contains(curve))
            curves.add(curve);
    }

    /**
     * Removes the specified curve from the curve set.
     * 
     * @param curve the curve to remove
     */
    @Override
	public void removeCurve(T curve) {
        curves.remove(curve);
    }

    /**
     * Checks if the curve set contains the given curve.
     */
    @Override
	public boolean containsCurve(Curve2D curve) {
    	return curves.contains(curve);
    }
    
    /**
     * Clears the inner curve collection.
     */
    @Override
	public void clearCurves() {
        curves.clear();
    }

    /**
     * Returns the collection of curves
     * 
     * @return the inner collection of curves
     */
    @Override
	public Collection<T> getCurves() {
        return curves;
    }

    /**
     * Returns the inner curve corresponding to the given index.
     * 
     * @param index index of the curve
     * @return the i-th inner curve
     * @since 0.6.3
     */
    @Override
	public T getCurve(int index) {
        return curves.get(index);
    }

    /**
     * Returns the child curve corresponding to a given position.
     * 
     * @param t the position on the set of curves, between 0 and twice the
     *            number of curves
     * @return the curve corresponding to the position.
     * @since 0.6.3
     */
    @Override
	public T getChildCurve(double t) {
        if (curves.size()==0)
            return null;
        return curves.get(getCurveIndex(t));
    }

    /**
     * Returns the first curve of the collection if it exists, null otherwise.
     * 
     * @return the first curve of the collection
     */
    @Override
	public T getFirstCurve() {
        if (curves.size()==0)
            return null;
        return curves.get(0);
    }

    /**
     * Returns the last curve of the collection if it exists, null otherwise.
     * 
     * @return the last curve of the collection
     */
    @Override
	public T getLastCurve() {
        if (curves.size()==0)
            return null;
        return curves.get(curves.size()-1);
    }

    /**
     * Returns the number of curves in the collection
     * 
     * @return the number of curves in the collection
     */
    @Override
	public int getCurveNumber() {
        return curves.size();
    }

    /**
     * Returns true if the CurveSet does not contain any curve.
     */
    @Override
	public boolean isEmpty() {
        return curves.size()==0;
    }

    // ===================================================================
    // methods inherited from interface Curve2D

    @Override
	public Collection<Point2D> getIntersections(LinearShape2D line) {
        ArrayList<Point2D> intersect = new ArrayList<Point2D>();

        // add intersections with each curve
        for (Curve2D curve : curves)
            intersect.addAll(curve.getIntersections(line));

        return intersect;
    }

    @Override
	public double getT0() {
        return 0;
    }

    @Override
	public double getT1() {
        return Math.max(curves.size()*2-1, 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Curve2D#getPoint(double)
     */
    @Override
	public Point2D getPoint(double t) {
        if (curves.size()==0)
            return null;
        if (t<getT0())
            return this.getFirstCurve().getFirstPoint();
        if (t>getT1())
            return this.getLastCurve().getLastPoint();

        // curve index
        int nc = (int) Math.floor(t);

        // check index if even-> corresponds to a curve
        int indc = (int) Math.floor(nc/2);
        if (indc*2==nc) {
            Curve2D curve = curves.get(indc);
            double pos = Curve2DUtils.fromUnitSegment(t-nc, 
            		curve.getT0(), curve.getT1());
            return curve.getPoint(pos);
        } else {
            // return either last point of preceding curve,
            // or first point of next curve
            if (t-nc<.5)
                return curves.get(indc).getLastPoint();
            else
                return curves.get(indc+1).getFirstPoint();
        }
    }

    /**
     * Get the first point of the curve.
     * 
     * @return the first point of the curve
     */
    @Override
	public Point2D getFirstPoint() {
        if (curves.size()==0)
            return null;
        return getFirstCurve().getFirstPoint();
    }

    /**
     * Get the last point of the curve.
     * 
     * @return the last point of the curve.
     */
    @Override
	public Point2D getLastPoint() {
        if (curves.size()==0)
            return null;
        return getLastCurve().getLastPoint();
    }

    /**
     * Computes the set of singular points as the set of singular points
     * of each curve, plus the extremities of each curve.
     * Each point is referenced only once.
     */
    @Override
	public Collection<Point2D> getSingularPoints() {
    	// create array for result
    	ArrayList<Point2D> points = new ArrayList<Point2D>();
    	
    	// iterate on curves composing the array
        for (Curve2D curve : curves){
        	// Add singular points inside curve
            for (Point2D point : curve.getSingularPoints())
                if (!points.contains(point))
                	points.add(point);
            
            // add first point
            if(!Curve2DUtils.isLeftInfinite(curve)){
            	Point2D point = curve.getFirstPoint();
            	if (!points.contains(point))
                	points.add(point);
            }
            
            // add last point
            if(!Curve2DUtils.isRightInfinite(curve)){
            	Point2D point = curve.getLastPoint();
            	if (!points.contains(point))
                	points.add(point);
            }
        }
        // return the set of singular points
        return points;
    }

    @Override
	public boolean isSingular(double pos) {
        if (Math.abs(pos-Math.round(pos))<Shape2D.ACCURACY)
            return true;

        int nc = this.getCurveIndex(pos);
        // int nc = (int) Math.floor(pos);
        if (nc-Math.floor(pos/2.0)>0)
            return true; // if is between 2
        // curves

        Curve2D curve = curves.get(nc);
        // double pos2 = fromUnitSegment(pos-2*nc, curve.getT0(),
        // curve.getT1());
        return curve.isSingular(this.getLocalPosition(pos));
    }

    @Override
	public double getPosition(java.awt.geom.Point2D point) {
        double minDist = Double.MAX_VALUE, dist = minDist;
        double x = point.getX(), y = point.getY();
        double pos = 0, t0, t1;

        int i = 0;
        for (Curve2D curve : curves) {
            dist = curve.getDistance(x, y);
            if (dist<minDist) {
                minDist = dist;
                pos = curve.getPosition(point);
                // format position
                t0 = curve.getT0();
                t1 = curve.getT1();
                pos = Curve2DUtils.toUnitSegment(pos, t0, t1)+i*2;
            }
            i++;
        }
        return pos;
    }

    @Override
	public double project(java.awt.geom.Point2D point) {
        double minDist = Double.MAX_VALUE, dist = minDist;
        double x = point.getX(), y = point.getY();
        double pos = 0, t0, t1;

        int i = 0;
        for (Curve2D curve : curves) {
            dist = curve.getDistance(x, y);
            if (dist<minDist) {
                minDist = dist;
                pos = curve.project(point);
                // format position
                t0 = curve.getT0();
                t1 = curve.getT1();
                pos = Curve2DUtils.toUnitSegment(pos, t0, t1)+i*2;
            }
            i++;
        }
        return pos;
    }

    @Override
	public Curve2D getReverseCurve() {
    	int n = curves.size();
        // create array of reversed curves
        Curve2D[] curves2 = new Curve2D[n];
        
        // reverse each curve
        for (int i = 0; i<n; i++)
            curves2[i] = curves.get(n-1-i).getReverseCurve();
        
        // create the reversed final curve
        return new CurveArray2D<Curve2D>(curves2);
    }

    /**
     * Return an instance of CurveArray2D.
     */
    @Override
	public CurveSet2D<? extends Curve2D> getSubCurve(double t0, double t1) {
        // number of curves in the set
        int nc = curves.size();

        // create a new empty curve set
        CurveArray2D<Curve2D> res = new CurveArray2D<Curve2D>();
        Curve2D curve;

        // format to ensure t is between T0 and T1
        t0 = Math.min(Math.max(t0, 0), nc*2-.6);
        t1 = Math.min(Math.max(t1, 0), nc*2-.6);

        // find curves index
        double t0f = Math.floor(t0);
        double t1f = Math.floor(t1);

        // indices of curves supporting points
        int ind0 = (int) Math.floor(t0f/2);
        int ind1 = (int) Math.floor(t1f/2);

        // case of t a little bit after a curve
        if (t0-2*ind0>1.5)
            ind0++;
        if (t1-2*ind1>1.5)
            ind1++;

        // start at the beginning of a curve
        t0f = 2*ind0;
        t1f = 2*ind1;

        double pos0, pos1;

        // need to subdivide only one curve
        if (ind0==ind1&&t0<t1) {
            curve = curves.get(ind0);
            pos0 = Curve2DUtils.fromUnitSegment(t0-t0f, 
            		curve.getT0(), curve.getT1());
            pos1 = Curve2DUtils.fromUnitSegment(t1-t1f, 
            		curve.getT0(), curve.getT1());
            res.addCurve(curve.getSubCurve(pos0, pos1));
            return res;
        }

        // add the end of the curve containing first cut
        curve = curves.get(ind0);
        pos0 = Curve2DUtils.fromUnitSegment(t0-t0f, 
        		curve.getT0(), curve.getT1());
        res.addCurve(curve.getSubCurve(pos0, curve.getT1()));

        if (ind1>ind0) {
            // add all the whole curves between the 2 cuts
            for (int n = ind0+1; n<ind1; n++)
                res.addCurve(curves.get(n));
        } else {
            // add all curves until the end of the set
            for (int n = ind0+1; n<nc; n++)
                res.addCurve(curves.get(n));

            // add all curves from the beginning of the set
            for (int n = 0; n<ind1; n++)
                res.addCurve(curves.get(n));
        }

        // add the beginning of the last cut curve
        curve = curves.get(ind1);
        pos1 = Curve2DUtils.fromUnitSegment(t1-t1f, 
        		curve.getT0(), curve.getT1());
        res.addCurve(curve.getSubCurve(curve.getT0(), pos1));

        // return the curve set
        return res;
    }

    // ===================================================================
    // methods inherited from interface Shape2D

    @Override
	public double getDistance(java.awt.geom.Point2D p) {
        return getDistance(p.getX(), p.getY());
    }

    @Override
	public double getDistance(double x, double y) {
        double dist = Double.POSITIVE_INFINITY;
        for (Curve2D curve : curves)
            dist = Math.min(dist, curve.getDistance(x, y));
        return dist;
    }

    /**
     * return true, if all curve pieces are bounded
     */
    @Override
	public boolean isBounded() {
        for (Curve2D curve : curves)
            if (!curve.isBounded())
                return false;
        return true;
    }

    /**
     * Clips a curve, and return a CurveArray2D. If the curve is totally outside
     * the box, return a CurveArray2D with 0 curves inside. If the curve is
     * totally inside the box, return a CurveArray2D with only one curve, which is
     * the original curve.
     */
    @Override
	public CurveSet2D<? extends Curve2D> clip(Box2D box) {
    	// Simply calls the generic method in Curve2DUtils
    	return Curve2DUtils.clipCurveSet(this, box);
    }

    /**
     * Returns bounding box for the CurveArray2D.
     */
    @Override
	public Box2D getBoundingBox() {
        double xmin = Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        double ymax = Double.MIN_VALUE;

        Box2D box;
        for (Curve2D curve : curves) {
            box = curve.getBoundingBox();
            xmin = Math.min(xmin, box.getMinX());
            ymin = Math.min(ymin, box.getMinY());
            xmax = Math.max(xmax, box.getMaxX());
            ymax = Math.max(ymax, box.getMaxY());
        }

        return new Box2D(xmin, xmax, ymin, ymax);
    }

    /**
     * Transforms each curve, and build a new CurveArray2D with the set of
     * transformed curves.
     */
    @Override
	public CurveArray2D<? extends Curve2D> transform(AffineTransform2D trans) {
    	// Allocate array for result
        CurveArray2D<Curve2D> result = new CurveArray2D<Curve2D>(curves.size());
        
        // add each transformed curve
        for (Curve2D curve : curves)
            result.addCurve(curve.transform(trans));
        return result;
    }

    @Override
	public Collection<? extends ContinuousCurve2D> getContinuousCurves() {
    	// create array for storing result
        ArrayList<ContinuousCurve2D> continuousCurves = 
        	new ArrayList<ContinuousCurve2D>();

        // Iterate on curves, and add either the curve itself, or the set of
        // continuous curves making the curve
        for (Curve2D curve : curves) {
            if (curve instanceof ContinuousCurve2D) {
                continuousCurves.add((ContinuousCurve2D) curve);
            } else {
                continuousCurves.addAll(curve.getContinuousCurves());
            }
        }

        return continuousCurves;
    }

    // ===================================================================
    // methods inherited from interface Shape2D

    /** Returns true if one of the curves contains the point */
    @Override
	public boolean contains(java.awt.geom.Point2D p) {
        return contains(p.getX(), p.getY());
    }

    /** Returns true if one of the curves contains the point */
    @Override
	public boolean contains(double x, double y) {
        for (Curve2D curve : curves) {
            if (curve.contains(x, y))
                return true;
        }
        return false;
    }

    @Override
	public java.awt.geom.GeneralPath getGeneralPath() {
        // create new path
        java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();

        // check case of empty curve set
        if (curves.size()==0)
            return path;

        // move to the first point of the first curves
        Point2D point;
        for (ContinuousCurve2D curve : this.getContinuousCurves()) {
            point = curve.getFirstPoint();
            path.moveTo((float) point.getX(), (float) point.getY());
            path = curve.appendPath(path);
        }

        // return the final path
        return path;
    }

    /* (non-Javadoc)
     * @see math.geom2d.curve.Curve2D#getAsAWTShape()
     */
    @Override
	public Shape getAsAWTShape() {
        return this.getGeneralPath();
    }

    @Override
	public void draw(Graphics2D g2) {
    	for(Curve2D curve : curves)
    		curve.draw(g2);
    }

    // ===================================================================
    // methods inherited from interface Object

    /**
     * Returns true if obj is a CurveArray2D with the same number of curves, and
     * such that each curve belongs to both objects.
     */
    @Override
    public boolean equals(Object obj) {
        // check class, and cast type
        if (!(obj instanceof CurveArray2D))
            return false;
        CurveArray2D<?> curveSet = (CurveArray2D<?>) obj;

        // check the number of curves in each set
        if (this.getCurveNumber()!=curveSet.getCurveNumber())
            return false;

        // return false if at least one couple of curves does not match
        for(int i=0; i<curves.size(); i++)
            if(!curves.get(i).equals(curveSet.curves.get(i)))
                return false;
        
        // otherwise return true
        return true;
    }

    @Override
    public CurveArray2D<? extends Curve2D> clone() {
        ArrayList<Curve2D> array = new ArrayList<Curve2D>(curves.size());
        for(T curve : curves)
            array.add(curve);
        return new CurveArray2D<Curve2D>(array);
    }
    
    // ===================================================================
    // methods implementing the Iterable interface

   /*
     * (non-Javadoc)
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
	public Iterator<T> iterator() {
        return curves.iterator();
    }
}
