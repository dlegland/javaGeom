/* File CurveSet2D.java 
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

import math.geom2d.Point2D;
import math.geom2d.Box2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.StraightObject2D;
import math.geom2d.transform.AffineTransform2D;

import java.util.*;

/**
 * A parameterized set of curves. 
 * A curve cannot be included twice in a CurveSet2D.
 * 
 * @author Legland
 *
 */
public class CurveSet2D<T extends Curve2D> implements Curve2D, Iterable<T>{

	/** The inner array of curves*/
	protected ArrayList<T> curves = new ArrayList<T>();
	
	// ===================================================================
	// static methods 

	/**
	 * Mapping of the parameter t, relative to the local curve, into the
	 * interval [0 1], [0 1[, ]0 1], or ]0 1[, depending on the values of
	 * t0 and t1.
	 * @param t
	 * @param t0
	 * @param t1
	 * @return
	 */
	protected final static double toUnitSegment(double t, double t0, double t1){
		if(t<=t0) return 0;
		if(t>=t1) return 1;

		if(t0 == Double.NEGATIVE_INFINITY && t1==Double.POSITIVE_INFINITY)
			return Math.atan(t)/Math.PI + .5;
		
		if(t0 == Double.NEGATIVE_INFINITY)
			return Math.atan(t-t1)*2/Math.PI + 1;
			
		if(t1 == Double.POSITIVE_INFINITY)
			return Math.atan(t-t0)*2/Math.PI;
		
		// t0 and t1 are both finite
		return (t-t0)/(t1-t0);
	}
	
	/**
	 * Transform the value t between 0 and 1 in a value given between t0 and t1. 
	 * @param t
	 * @param t0
	 * @param t1
	 * @return
	 */
	protected final static double fromUnitSegment(double t, double t0, double t1){
		if(t<=0) return t0;
		if(t>=1) return t1;
		
		if(t0 == Double.NEGATIVE_INFINITY && t1==Double.POSITIVE_INFINITY)
			return Math.tan((t-.5)*Math.PI);
		
		if(t0 == Double.NEGATIVE_INFINITY)
			return Math.tan((t-1)*Math.PI/2) + t1;
			
		if(t1 == Double.POSITIVE_INFINITY)
			return Math.tan(t*Math.PI/2) + t0;
			
		// t0 and t1 are both finite
		return t*(t1-t0) + t0;
	}
	
	// ===================================================================
	// Constructors 

	/**
	 * Empty constructor. Initializes an empty array of curves.
	 */
	public CurveSet2D(){
	}

	/**
	 * Constructor from an array of curves. 
	 * @param curves the array of curves in the set
	 */
	public CurveSet2D(T[] curves){
		for(int i=0; i<curves.length; i++)
			this.addCurve(curves[i]);
	}
	
	/**
	 * Constructor from a collection of curves. The curves are added to the
	 * inner collection of curves.
	 * @param curves the collection of curves to add to the set
	 */
	public CurveSet2D(Collection<? extends T> curves){
		this.curves.addAll(curves);
	}

	// ===================================================================
	// methods specific to CurveSet2D

	/**
	 * Adds the curve to the curve set, if it does not already belongs to the
	 * set. 
	 * @param curve the curve to add
	 */
	public void addCurve(T curve){
		if(!curves.contains(curve))
			curves.add(curve);
	}
	
	/**
	 * Removes the specified curve from the curve set.
	 * @param curve the curve to remove
	 */
	public void removeCurve(T curve){
		curves.remove(curve);
	}
	
	/**
	 * Clears the inner curve collection.
	 */
	public void clearCurves(){
		curves.clear();
	}
	
	/**
	 * Returns the collection of curves
	 * @return the inner collection of curves
	 */
	public Collection<T> getCurves(){
		return curves;
	}
	
	/**
	 * Returns the first curve of the collection if it exists, null otherwise.
	 * @return the first curve of the collection
	 */
	public T getFirstCurve(){
		if(curves.size()==0) return null;
		return curves.get(0);
	}
	
	/**
	 * Returns the last curve of the collection if it exists, null otherwise.
	 * @return the last curve of the collection
	 */
	public T getLastCurve(){
		if(curves.size()==0) return null;
		return curves.get(curves.size()-1);
	}
	
	/**
	 * Returns the number of curves in the collection
	 * @return the number of curves in the collection
	 */
	public int getCurveNumber(){
		return curves.size();
	}
	
	/**
	 * Returns true if the CurveSet does not contain any curve.
	 */
	public boolean isEmpty(){
		return curves.size()==0;
	}
	
	// ===================================================================
	// methods inherited from interface Curve2D 
	
	
	public Collection<Point2D> getIntersections(StraightObject2D line) {
		ArrayList<Point2D> intersect = new ArrayList<Point2D>();

		// add intersections with each curve
		for(Curve2D curve : curves)
			intersect.addAll(curve.getIntersections(line));
		
		return intersect;
	}


	public double getT0(){
		return 0;
	}
	
	public double getT1(){
		return Math.max(curves.size()*2-1, 0);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Curve2D#getPoint(double)
	 */
	public Point2D getPoint(double t) {
		return getPoint(t, new Point2D());
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Curve2D#getPoint(double, math.geom2d.Point2D)
	 */
	public Point2D getPoint(double t, Point2D point) {
		if(curves.size()==0) return null;
		if(t<getT0())
			return this.getFirstCurve().getPoint(this.getFirstCurve().getT0(), point);
		if(t>getT1()) 
			return this.getLastCurve().getPoint(this.getLastCurve().getT1(), point);
		
		// curve index
		int nc = (int) Math.floor(t);
		
		// check index if even-> corresponds to a curve
		int indc = (int)Math.floor(nc/2);
		if(indc*2 == nc){
			Curve2D curve = curves.get(indc);
			double pos = fromUnitSegment(t-nc, curve.getT0(), curve.getT1());
			return curve.getPoint(pos, point);
		}else{
			// return either last point of preceding curve, 
			// or first point of next curve
			if(t-nc<.5)
				return curves.get(indc).getLastPoint();
			else
				return curves.get(indc+1).getFirstPoint();
		}
	}

	/**
	 * Get the first point of the curve. 
	 * @return the first point of the curve
	 */
	public Point2D getFirstPoint(){
		if(curves.size()==0) return null;
		return getFirstCurve().getFirstPoint();
	}
	
	/**
	 * Get the last point of the curve. 
	 * @return the last point of the curve.
	 */
	public Point2D getLastPoint(){
		if(curves.size()==0) return null;
		return getLastCurve().getLastPoint();
	}

	public double getPosition(Point2D point){
		double minDist = Double.MAX_VALUE, dist=minDist;
		double x=point.getX(), y=point.getY();
		double pos = 0, t0, t1;
		
		int i=0;
		for(Curve2D curve : curves){
			dist = curve.getDistance(x, y);
			if(dist<minDist){
				minDist = dist;
				pos = curve.getPosition(point);
				// format position
				t0 = curve.getT0();
				t1 = curve.getT1();
				pos = toUnitSegment(pos, t0, t1)+i*2;
			}
			i++;
		}
		return pos;
	}

	public double project(Point2D point){
		double minDist = Double.MAX_VALUE, dist=minDist;
		double x=point.getX(), y=point.getY();
		double pos = 0, t0, t1;
		
		int i=0;
		for(Curve2D curve : curves){
			dist = curve.getDistance(x, y);
			if(dist<minDist){
				minDist = dist;
				pos = curve.getPosition(point);
				// format position
				t0 = curve.getT0();
				t1 = curve.getT1();
				pos = toUnitSegment(pos, t0, t1)+i*2;
			}
			i++;
		}
		return pos;
	}

	
	public Curve2D getReverseCurve(){
		Curve2D[] curves2 = new Curve2D[curves.size()];
		int n=curves.size();
		for(int i=0; i<n; i++)
			curves2[i] = curves.get(n-1-i).getReverseCurve();
		return new CurveSet2D<Curve2D>(curves2);
	}

	/** 
	 * Return an instance of CurveSet2D.
	 */
	public CurveSet2D<? extends Curve2D> getSubCurve(double t0, double t1){
		// number of curves in the set
		int nc = curves.size();
		
		// create a new empty curve set
		CurveSet2D<Curve2D> res = new CurveSet2D<Curve2D>();
		Curve2D curve;

		// format to ensure t is between T0 and T1
		t0 = Math.min(Math.max(t0, 0), nc*2-.6);
		t1 = Math.min(Math.max(t1, 0), nc*2-.6);

		// find curves index
		double 	t0f = Math.floor(t0);
		double 	t1f = Math.floor(t1);
		
		// indices of curves supporting points
		int ind0 = (int) Math.floor(t0f/2);
		int ind1 = (int) Math.floor(t1f/2);
		
		// case of t a little bit after a curve 
		if(t0-2*ind0 > 1.5)	ind0++;
		if(t1-2*ind1 > 1.5)	ind1++;
	
		// start at the beginning of a curve 
		t0f=2*ind0;
		t1f=2*ind1;

		double pos0, pos1;
		
		// need to subdivide only one curve
		if(ind0==ind1 && t0<t1){
			curve = curves.get(ind0);
			pos0 = fromUnitSegment(t0-t0f, curve.getT0(), curve.getT1()); 
			pos1 = fromUnitSegment(t1-t1f, curve.getT0(), curve.getT1());
			res.addCurve(curve.getSubCurve(pos0, pos1));
			return res;
		}		

		// add the end of the curve containing first cut
		curve = curves.get(ind0);
		pos0 = fromUnitSegment(t0-t0f, curve.getT0(), curve.getT1()); 
		res.addCurve(curve.getSubCurve(pos0, curve.getT1()));
		
		if(ind1>ind0){
			// add all the whole curves between the 2 cuts
			for(int n=ind0+1; n<ind1; n++)
				res.addCurve(curves.get(n));
		}else{
			// add all curves until the end of the set
			for(int n=ind0+1; n<nc; n++)
				res.addCurve(curves.get(n));
			
			// add all curves from the beginning of the set
			for(int n=0; n<ind1; n++)
				res.addCurve(curves.get(n));
		}
		
		// add the beginning of the last cut curve
		curve = curves.get(ind1);
		pos1 = fromUnitSegment(t1-t1f, curve.getT0(), curve.getT1()); 
		res.addCurve(curve.getSubCurve(curve.getT0(), pos1));
		
		// return the curve set
		return res;
	}

	
	// ===================================================================
	// methods inherited from interface Shape2D 

	public double getDistance(java.awt.geom.Point2D p) {
		return getDistance(p.getX(), p.getY());
	}

	public double getDistance(double x, double y) {
		double dist = Double.POSITIVE_INFINITY;		
		for(Curve2D curve : curves)
			dist = Math.min(dist, curve.getDistance(x, y));
		return dist;
	}


	/**
	 * return true, if all curve pieces are bounded
	 */
	public boolean isBounded() {
		for(Curve2D curve : curves)
			if(!curve.isBounded())
				return false;
		return true;
	}

	/**
	 * Clip a curve, and return a CurveSet2D. If the curve is totally outside
	 * the box, return a CurveSet2D with 0 curves inside. If the curve is
	 * totally inside the box, return a CurveSet2D with only one curve, which
	 * is the original curve.
	 */
	public CurveSet2D<? extends Curve2D> clip(Box2D box){	
		
		// create array of points
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		
		// extract edges of the box boundary
		Collection<LineSegment2D> edges = box.getEdges();
		
		// add the intersections with each edge to the list
		for(LineSegment2D edge : edges)
			points.addAll(this.getIntersections(edge));
		
		// convert list to point array, sorted wrt to their position on the curve
		SortedSet<Double> set = new TreeSet<Double>();
		for(Point2D p : points)
			set.add(new java.lang.Double(this.getPosition(p)));
					
		// Create CurveSet2D for storing the result
		CurveSet2D<Curve2D> res = new CurveSet2D<Curve2D>();		
		
		// extract first point of the curve
		Point2D point1 = this.getFirstPoint();

		// if no intersection point, the curve is totally either inside or outside the box
		if(set.size()==0){
			if(box.contains(point1))
				res.addCurve(this);
			return res;
		}
		
		double pos1, pos2;
		Iterator<java.lang.Double> iter = set.iterator();
		
		// different behavior depending if first point lies inside the box
		if(this.contains(point1) && !box.getBoundary().contains(point1))
			res.addCurve(this.getSubCurve(this.getT0(), iter.next()));
		
		// add the portions of curve between couples of intersections
		while(iter.hasNext()){
			pos1 = iter.next().doubleValue();
			if(iter.hasNext())
				pos2 = iter.next().doubleValue();
			else
				pos2 = this.getT1();
			res.addCurve(this.getSubCurve(pos1, pos2));
		}
		
		return res;
	}

	/**
	 * Return bounding box for the CurveSet2D.
	 */
	public Box2D getBoundingBox(){
		double xmin = Double.MAX_VALUE;	
		double ymin = Double.MAX_VALUE;
		double xmax = Double.MIN_VALUE;	
		double ymax = Double.MIN_VALUE;
		
		java.awt.geom.Rectangle2D rect;
		for(Curve2D curve : curves){
			rect = curve.getBounds();
			xmin = Math.min(xmin, rect.getX());
			ymin = Math.min(ymin, rect.getY());
			xmax = Math.max(xmax, rect.getX()+rect.getWidth());
			ymax = Math.max(ymax, rect.getY()+rect.getHeight());
		}

		return new Box2D(xmin, xmax, ymin, ymax);
	}

	/**
	 * Transform each curve, and build a new CurveSet2D with the set
	 * of transformed curves.
	 */
	public CurveSet2D<? extends Curve2D> transform(AffineTransform2D trans) {
		CurveSet2D<Curve2D> result = new CurveSet2D<Curve2D>();		
		for(Curve2D curve : curves)
			result.addCurve(curve.transform(trans));
		return result;
	}

	public Collection<ContinuousCurve2D> getContinuousCurves(){
		ArrayList<ContinuousCurve2D> continuousCurves = 
			new ArrayList<ContinuousCurve2D>();
		
		for(Curve2D curve : curves){
			if (curve instanceof ContinuousCurve2D){
				continuousCurves.add((ContinuousCurve2D) curve);
			}else{
				continuousCurves.addAll(curve.getContinuousCurves());
			}
		}
		
		return continuousCurves;
	}
	
	// ===================================================================
	// methods inherited from interface Shape2D 

	/** return true if one of the curves contains the point */
	public boolean contains(java.awt.geom.Point2D p){
		return contains(p.getX(), p.getY());	
	}
	
	/** return true if one of the curves contains the point */
	public boolean contains(double x, double y){
		for(Curve2D curve : curves){
			if(curve.contains(x, y))
				return true;
		}
		return false;	
	}

	/** Always return false */
	public boolean contains(java.awt.geom.Rectangle2D rect){
		return false;	
	}

	/** Always return false */
	public boolean contains(double x, double y, double w, double h){
		return false;	
	}

	/**
	 * Return bounding box of the shape.
	 */
	public java.awt.Rectangle getBounds(){
		return this.getBoundingBox().getAsAWTRectangle();
	}
	
	/**
	 * Return more precise bounds for the shape.
	 */
	public java.awt.geom.Rectangle2D getBounds2D(){
		return this.getBoundingBox().getAsAWTRectangle2D();
	}

	
	public boolean intersects(java.awt.geom.Rectangle2D rect){
		return intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}
	
	public boolean intersects(double x, double y, double w, double h){
		for(Curve2D curve : curves){
			if(curve.intersects(x, y, w, h))
				return true;
		}
		return false;
	}	

	protected java.awt.geom.GeneralPath getGeneralPath(){
		// create new path
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		
		if(curves.size()==0)
			return path;
		
		Point2D point;
		
		// move to the first point of the first curves
		for(ContinuousCurve2D curve : this.getContinuousCurves()){
			point = curve.getFirstPoint();
			path.moveTo((float)point.getX(), (float)point.getY());
			path = curve.appendPath(path);
		}
		
		// return the final path
		return path;		
	}
	
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans){
		java.awt.geom.GeneralPath path = getGeneralPath();
		
		if(path==null)
			return null;
		return path.getPathIterator(trans);		
	}
	
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans, double flatness){
		java.awt.geom.GeneralPath path = getGeneralPath();
		
		if(path==null)
			return null;
		return path.getPathIterator(trans, flatness);		
	}	

	// ===================================================================
	// methods inherited from interface Object 

	/**
	 * Return true if obj is a CurveSet2D with the same number of
	 * curves, and such that each curve belongs to both objects.
	 */
	public boolean equals(Object obj){
		
		// check class, and cast type
		if(!(obj instanceof CurveSet2D)) return false;
		CurveSet2D<?> curveSet = (CurveSet2D<?>) obj;
		
		// check the number of curves in each set
		if(this.getCurveNumber()!=curveSet.getCurveNumber()) 
			return false;
		
		boolean ok;
		
		// iterate on the curves of the first set
		for(Curve2D curve1 : this.getCurves()){
			ok = false;
			
			// check for each curve of second set if it correspond to curve1
			for(Curve2D curve2 : curveSet.getCurves()){
				if(curve1.equals(curve2)){
					ok = true;
					break;
				}
			}
			if(!ok) return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<T> iterator() {
		return curves.iterator();
	}

}
