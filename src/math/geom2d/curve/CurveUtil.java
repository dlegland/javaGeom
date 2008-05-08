/**
 * 
 */
package math.geom2d.curve;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.StraightObject2D;

/**
 * Collects some useful methods for clipping curves.
 * @author dlegland
 *
 */
public abstract class CurveUtil {
	
	/**
	 * Clip a curve, and return a CurveSet2D. If the curve is totally outside
	 * the box, return a CurveSet2D with 0 curves inside. If the curve is
	 * totally inside the box, return a CurveSet2D with only one curve, which
	 * is the original curve.
	 */
	public final static CurveSet2D<Curve2D> clipCurve(Curve2D curve, Box2D box){
		// Case of continuous curve:
		// convert the result of ClipContinuousCurve to CurveSet of Curve2D
		if(curve instanceof ContinuousCurve2D)
			return new CurveSet2D<Curve2D>(
					CurveUtil.clipContinuousCurve(
							(ContinuousCurve2D) curve, box).getCurves());
		
		// case of a CurveSet2D
		if(curve instanceof CurveSet2D)
			return CurveUtil.clipCurveSet((CurveSet2D<?>) curve, box);
		
		// Unknown case
		System.err.println("Unknown curve class in Box2D.clipCurve()");
		return new CurveSet2D<Curve2D>();
	}
	
	
	/**
	 * clip a CurveSet2D.
	 */
	public final static CurveSet2D<Curve2D> clipCurveSet(CurveSet2D<?> curveSet, Box2D box){
		// Clip the current curve
		CurveSet2D<Curve2D> result = new CurveSet2D<Curve2D>();
		CurveSet2D<?> clipped;

		// a clipped parts of current curve to the result
		for(Curve2D curve : curveSet){
			clipped = CurveUtil.clipCurve(curve, box);
			for(Curve2D clippedPart : clipped)
				result.addCurve(clippedPart);
		}

		// return a set of curves
		return result;
	}
		
	/**
	 * Clip a continuous curve.
	 */
	public final static CurveSet2D<ContinuousCurve2D> clipContinuousCurve(ContinuousCurve2D curve, Box2D box){
		//TODO: there is a problem for degenerate cases. Possible solution:
		// each curve returns 2 intersection points in case of tangential intersection
		
		// Create CurveSet2D for storing the result
		CurveSet2D<ContinuousCurve2D> res = new CurveSet2D<ContinuousCurve2D>();		
		

		// ------ Compute ordered list of intersections
		
		// create array of intersection points
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		
		// add all the intersections with edges of the box boundary
		for(StraightObject2D edge : box.getEdges())
			points.addAll(curve.getIntersections(edge));
				
		// convert list to point array, sorted wrt to their position on the curve
		SortedSet<java.lang.Double> set = new TreeSet<java.lang.Double>();
		for(Point2D p : points)
			set.add(new java.lang.Double(curve.getPosition(p)));				
		
		// iterator on the intersection positions
		Iterator<java.lang.Double> iter = set.iterator();

		
		// ----- remove intersections which do not cross the boundary
		
		// init arrays
		int nInter = set.size();
		double[] positions = new double[nInter+2];
		double[] between = new double[nInter+1];
		
		// fill up array of positions, with extreme positions of curve
		positions[0] = curve.getT0();
		for(int i=0; i<nInter; i++)
			positions[i+1] = iter.next();
		positions[nInter+1] = curve.getT1();
		
		// compute positions of points between intersections
		for(int i=0; i<nInter+1; i++)
			between[i] = choosePosition(positions[i], positions[i+1]);
		
		// array of positions to remove
		ArrayList<Double> toRemove = new ArrayList<Double>();
		
		// remove an intersection point if the curve portions before and after
		// are both either inside or outside of the box.
		for(int i=0; i<nInter; i++){
			Point2D p1 = curve.getPoint(between[i]);
			Point2D p2 = curve.getPoint(between[i+1]);
			boolean b1 = box.contains(p1);
			boolean b2 = box.contains(p2);
			if(b1==b2)
				toRemove.add(positions[i+1]);
		}
			
		// remove unnecessary intersections
		set.removeAll(toRemove);
		
		// iterator on the intersection positions
		iter = set.iterator();
		
		
		// ----- Check case of no intersection point
		
		// if no intersection point, the curve is totally either inside or 
		// outside the box
		if(set.size()==0){
			// compute position of an arbitrary point on the curve
			Point2D point;
			if(curve.isBounded()){
				point = curve.getFirstPoint();
			}else{
				double pos = choosePosition(curve.getT0(), curve.getT1());
				point = curve.getPoint(pos);
			}
			
			// if the box contains a point, it contains the whole curve
			if(box.contains(point))
				res.addCurve(curve);
			return res;
		}
		
		
		// ----- Check if the curve starts inside of the box

		// the flag for a curve that starts inside the box
		boolean inside = false;
		boolean touch  = false;
		
		// different behavior if curve is bounded or not
		double t0 = curve.getT0();
		if(Double.isInfinite(t0)){
			// choose point between -infinite and first intersection
			double pos = choosePosition(t0, set.iterator().next());
			inside = box.contains(curve.getPoint(pos));
		}else{
			// extract first point of the curve
			Point2D point = curve.getFirstPoint();
			inside = box.contains(point);
			
			// if first point is on the boundary, then choose another point
			// located between first point and first intersection
			if(box.getBoundary().contains(point)){
				touch = true;
				
				double pos = choosePosition(t0, iter.next());
				while(Math.abs(pos-t0)<Shape2D.ACCURACY && iter.hasNext())
					pos = choosePosition(t0, iter.next());
				if(Math.abs(pos-t0)<Shape2D.ACCURACY)
					pos = choosePosition(t0, curve.getT1());
				point = curve.getPoint(pos);
				
				// remove the first point from the list of intersections
				set.remove(t0);
				
				// if inside, adds the first portion of the curve, 
				// and remove next intersection
				if(box.contains(point)){
					pos = set.iterator().next();
					res.addCurve(curve.getSubCurve(t0, pos));
					set.remove(pos);
				}
				
				// update iterator
				iter = set.iterator();
				
				inside = false;
			}							
		}
		
		// different behavior depending if first point lies inside the box
		double pos0 = Double.NaN;
		if(inside&&!touch)
			if(curve.isClosed())
				pos0 = iter.next();
			else
				res.addCurve(curve.getSubCurve(curve.getT0(), iter.next()));
		
		
		// ----- add portions of curve between each couple of intersections
		
		double pos1, pos2;
		while(iter.hasNext()){
			pos1 = iter.next().doubleValue();
			if(iter.hasNext())
				pos2 = iter.next().doubleValue();
			else 
				pos2 = curve.isClosed()&&!touch ? pos0 :  curve.getT1();
			res.addCurve(curve.getSubCurve(pos1, pos2));
		}
		
		return res;
	}
		
	/**
	 * clip a continuous smooth curve. Currently just call the static method
	 * clipContinuousCurve, and cast clipped curves.
	 */
	public final static CurveSet2D<SmoothCurve2D> clipSmoothCurve(SmoothCurve2D curve, Box2D box){
		CurveSet2D<SmoothCurve2D> result = new CurveSet2D<SmoothCurve2D>();
		for(ContinuousCurve2D cont : CurveUtil.clipContinuousCurve(curve, box))
			if(cont instanceof SmoothCurve2D)
				result.addCurve((SmoothCurve2D) cont);
		
		return result;
	}
		
	/**
	 * Clip a curve, and return a CurveSet2D. If the curve is totally outside
	 * the box, return a CurveSet2D with 0 curves inside. If the curve is
	 * totally inside the box, return a CurveSet2D with only one curve, which
	 * is the original curve.
	 */
	public final static CurveSet2D<ContinuousOrientedCurve2D>
			clipContinuousOrientedCurve(ContinuousOrientedCurve2D curve, Box2D box){
		
		CurveSet2D<ContinuousOrientedCurve2D> result = 
			new CurveSet2D<ContinuousOrientedCurve2D>();
		for(ContinuousCurve2D cont : CurveUtil.clipContinuousCurve(curve, box))
			if(cont instanceof ContinuousOrientedCurve2D)
				result.addCurve((ContinuousOrientedCurve2D) cont);
		
		return result;
	
//		// create array of points
//		ArrayList<Point2D> points = new ArrayList<Point2D>();
//
//		// add the intersections with edges of the box boundary
//		for(StraightObject2D edge : box.getEdges())
//			points.addAll(curve.getIntersections(edge));
//		
//		// convert list to point array, sorted wrt to their position on the curve
//		SortedSet<java.lang.Double> set = new TreeSet<java.lang.Double>();
//		for(Point2D p : points)
//			set.add(new java.lang.Double(curve.getPosition(p)));			
//				
//		// Create curveset for storing the result
//		CurveSet2D<ContinuousOrientedCurve2D> res =
//			new CurveSet2D<ContinuousOrientedCurve2D>();		
//				
//		// extract first point of the curve
//		Point2D point1 = curve.getFirstPoint();
//		
//		// case of empty curve set, for example
//		if(point1==null)
//			return res;
//
//		// if no intersection point, the curve is totally either inside or outside the box
//		if(set.size()==0){
//			if(box.contains(point1))
//				res.addCurve(curve);
//			return res;
//		}
//		
//		double pos1, pos2;
//		Iterator<java.lang.Double> iter = set.iterator();
//		
//		double pos0=0;
//		
//		// different behavior depending if first point lies inside the box
//		if(box.contains(point1) && !box.getBoundary().contains(point1))
//			pos0 = iter.next().doubleValue();
//		
//		
//		// add the portions of curve between couples of intersections
//		while(iter.hasNext()){
//			pos1 = iter.next().doubleValue();
//			if(iter.hasNext())
//				pos2 = iter.next().doubleValue();
//			else
//				pos2 = pos0;
//			res.addCurve(curve.getSubCurve(pos1, pos2));
//		}
//		
//		return res;
	}
		

	/**
	 * Clips a boundary and closes the result curve. Return an instance of
	 * BoundarySet2D.
	 */
	public final static BoundarySet2D<ContinuousBoundary2D>
	clipBoundary(Boundary2D boundary, Box2D box){
		// iteration variable
		ContinuousOrientedCurve2D curve;
		
		// The set of boundary curves. Each curve of this set is either a
		// curve of the original boundary, or a composition of a portion of
		// original boundary with a portion of the box.
		BoundarySet2D<ContinuousBoundary2D> res = 
			new BoundarySet2D<ContinuousBoundary2D>();
		
		// to store result of curve clipping
		CurveSet2D<ContinuousOrientedCurve2D> clipped;

		// to store set of all clipped curves
		CurveSet2D<ContinuousOrientedCurve2D> curveSet =
			new CurveSet2D<ContinuousOrientedCurve2D>();

		// extract the oriented curves which constitutes the boundary
		Collection<ContinuousBoundary2D> boundaryCurves = 
			boundary.getBoundaryCurves();
		
		// Iterate on boundary curves: extract current curve (continuous and
		// oriented), clip it with box, and add clipped curves to the array
		// 'curves'
		for(ContinuousBoundary2D boundaryCurve : boundaryCurves){
			clipped = CurveUtil.clipContinuousOrientedCurve(boundaryCurve, box);
			
			for(ContinuousOrientedCurve2D clip : clipped)
				curveSet.addCurve(clip);			
		}

		// array of position on the box for first and last point of each curve
		int nc = curveSet.getCurveNumber();
		double[] startPositions = new double[nc];
		double[] endPositions 	= new double[nc];
		
		// also create array of curves
		ContinuousOrientedCurve2D[] curves = new ContinuousOrientedCurve2D[nc]; 
		
		// boundary of the box
		Curve2D boxBoundary = box.getBoundary();
		
		// compute position on the box for first and last point of each curve
		Iterator<ContinuousOrientedCurve2D> iter = 
			curveSet.getCurves().iterator();
		
		for(int i=0; i<nc; i++){
			// save current curve
			curve = iter.next();
			curves[i] = curve;

			if (curve.isClosed()){
				startPositions[i] 	= java.lang.Double.NaN;
				endPositions[i] 	= java.lang.Double.NaN;
				continue;
			}

			// compute positions of first point and last point on box boundary
			startPositions[i] 	= boxBoundary.getPosition(curve.getFirstPoint());
			endPositions[i] 	= boxBoundary.getPosition(curve.getLastPoint());
		}
		
		// theoretical number of boundary curves. Set to the number of clipped
		// curves, but total number can be reduced if several clipped curves
		// belong to the same boundary curve.
		int nb = nc;
		
		// current index of curve
		int c=0;
		
		// iterate while there are boundary curve to build
		while(c<nb){
			int ind = c;
			// find the current curve (used curves are removed from array)
			while(curves[ind]==null)
				ind++;

			// current curve
			curve = curves[ind];
			
			// if curve is closed, we can switch to next curve
			if(curve.isClosed()){
				// Add current boundary to the set of boundary curves
				if(curve instanceof ContinuousBoundary2D){
					res.addCurve((ContinuousBoundary2D) curve);
				}else{
					BoundaryPolyCurve2D<ContinuousOrientedCurve2D> bnd = 
						new BoundaryPolyCurve2D<ContinuousOrientedCurve2D>();
					bnd.addCurve(curve);
					res.addCurve(bnd);
				}
				curves[ind]=null;
				
				// switch to next curve
				c++;
				continue;
			}
			
			// create a new Boundary curve
			BoundaryPolyCurve2D<ContinuousOrientedCurve2D> boundary0 =
				new BoundaryPolyCurve2D<ContinuousOrientedCurve2D>();
			
			// add current curve to boundary curve 
			boundary0.addCurve(curve);
			
			// get last points (to add a line with next curve)
			Point2D p0 	= curve.getFirstPoint();
			Point2D p1 	= curve.getLastPoint();
			
			// start position of first curve, used as stop flag
			double pos0	= startPositions[ind];
			

			// store indices of curves, to remove them later
			ArrayList<Integer> indices = new ArrayList<Integer>();
			indices.add(new Integer(ind));
			
			// position of last point of current curve on box boundary
			ind = findNextCurveIndex(startPositions, pos0);
			double pos = startPositions[ind];
						
			// iterate while we don't come back to first point
			while(pos!=pos0){				
				// find the curve whose first point is just after last point
				// of current curve on box boundary
				curve = curves[ind];

				// add a link between previous curve and current curve
				boundary0.addCurve(new LineSegment2D(p1, curve.getFirstPoint()));
				
				// add to current boundary
				boundary0.addCurve(curve);

				// find index and position of next curve
				ind = findNextCurveIndex(startPositions, pos);
				pos = startPositions[ind];
				
				// get last points
				p1 = curve.getLastPoint();

				// decrease total number of boundary curves
				nb--;				
			}
			
			// add a line from last point to first point
			boundary0.addCurve(new LineSegment2D(p1, p0));			
			
			// Add current boundary to the set of boundary curves
			res.addCurve(boundary0);
			
			// remove curves from array
			Iterator<Integer> iter2 = indices.iterator();
			while(iter2.hasNext())
				curves[iter2.next().intValue()] = null;
			
			// next curve !
			c++;
		}

		return res;
	}
		
	public final static int findNextCurveIndex(double[] positions, double pos){
		int ind = -1;
		double posMin = java.lang.Double.MAX_VALUE;
		for(int i=0; i<positions.length; i++){
			// avoid NaN
			if(java.lang.Double.isNaN(positions[i]))
				continue;
			// avoid values before
			if(positions[i]-pos<Shape2D.ACCURACY)
				continue;
			
			// test if closer that other points
			if(positions[i]<posMin){
				ind = i;
				posMin = positions[i];
			}
		}
		
		if(ind!=-1)
			return ind;
		
		// if not found, return index of smallest value (mean that pos is last
		// point on the boundary, so we need to start at the beginning).
		for(int i=0; i<positions.length; i++){
			if(java.lang.Double.isNaN(positions[i]))
				continue;			
			if(positions[i]-posMin<Shape2D.ACCURACY){
				ind = i;
				posMin = positions[i];
			}
		}
		return ind;
	}

	/** 
	 * Choose an arbitrary position between positions t0 and t1, which can be
	 * infinite.
	 * @param t0 the first bound of a curve parameterization
	 * @param t1 the second bound of a curve parameterization
	 * @return a position located between t0 and t1
	 */
	private final static double choosePosition(double t0, double t1){
		if(Double.isInfinite(t0)){
			if(Double.isInfinite(t1))
				return 0;
			return t1-10;
		}
		
		if(Double.isInfinite(t1))
			return t0+10;
		
		return (t0+t1)/2;
	}
}
