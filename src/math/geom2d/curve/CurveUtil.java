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
import math.geom2d.line.StraightLine2D;
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
		// Case of continuous curve
		if(curve instanceof ContinuousCurve2D)
			return CurveUtil.clipContinuousCurve((ContinuousCurve2D) curve, box);
		
		// case of a CurveSet2D
		if(curve instanceof CurveSet2D && !(curve instanceof ContinuousCurve2D))
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
	 * clip a continuous curve.
	 */
	public final static CurveSet2D<Curve2D> clipContinuousCurve(ContinuousCurve2D curve, Box2D box){
		//TODO: there is a problem for degenerate cases. Possible solution:
		// each curve returns 2 intersection points in case of tangential intersection
		//TODO: should take into account unbounded boxes
		
		// create array of points
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		
		// add the intersections with edges of the box boundary
		for(StraightObject2D edge : box.getEdges())
			points.addAll(curve.getIntersections(edge));
				
		// convert list to point array, sorted wrt to their position on the curve
		SortedSet<java.lang.Double> set = new TreeSet<java.lang.Double>();
		for(Point2D p : points)
			set.add(new java.lang.Double(curve.getPosition(p)));				
				
		// Create CurveSet2D for storing the result
		CurveSet2D<Curve2D> res = new CurveSet2D<Curve2D>();		
		
		// extract first point of the curve
		Point2D point1 = curve.getFirstPoint();

		// if no intersection point, the curve is totally either inside or 
		// outside the box
		if(set.size()==0){
			if(box.contains(point1))
				res.addCurve(curve);
			return res;
		}
		
		double pos1, pos2;
		Iterator<java.lang.Double> iter = set.iterator();
		
		// different behavior depending if first point lies inside the box
		double pos0 = Double.NaN;
		if(box.contains(point1) && !box.getBoundary().contains(point1))
			if(curve.isClosed())
				pos0 = iter.next();
			else
				res.addCurve(curve.getSubCurve(curve.getT0(), iter.next()));
		
		// add the portions of curve between each couple of intersections
		while(iter.hasNext()){
			pos1 = iter.next().doubleValue();
			if(iter.hasNext())
				pos2 = iter.next().doubleValue();
			else 
				pos2 = curve.isClosed() ? pos0 :  curve.getT1();
			res.addCurve(curve.getSubCurve(pos1, pos2));
		}
		
		return res;
	}
		
	/**
	 * clip a continuous smooth curve.
	 */
	public final static CurveSet2D<SmoothCurve2D> clipSmoothCurve(SmoothCurve2D curve, Box2D box){
		// create two CurveSet2D to be used in fip-flop
		CurveSet2D<SmoothCurve2D> result = new CurveSet2D<SmoothCurve2D>();
		CurveSet2D<SmoothCurve2D> buffer;
		
		// init first buffer with current curve
		result.addCurve(curve);
		
		// Iterate on each clipping line
		for(StraightLine2D line : box.getClippingLines()){
			buffer = new CurveSet2D<SmoothCurve2D>();
			for(SmoothCurve2D smooth : result){
				for(SmoothCurve2D c : line.clipSmoothCurve(smooth))
					buffer.addCurve(c);
			}
			result = buffer;
		}
		
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
	
		// create array of points
		ArrayList<Point2D> points = new ArrayList<Point2D>();

		// add the intersections with edges of the box boundary
		for(StraightObject2D edge : box.getEdges())
			points.addAll(curve.getIntersections(edge));
		
		// convert list to point array, sorted wrt to their position on the curve
		SortedSet<java.lang.Double> set = new TreeSet<java.lang.Double>();
		for(Point2D p : points)
			set.add(new java.lang.Double(curve.getPosition(p)));			
				
		// Create curveset for storing the result
		CurveSet2D<ContinuousOrientedCurve2D> res =
			new CurveSet2D<ContinuousOrientedCurve2D>();		
				
		// extract first point of the curve
		Point2D point1 = curve.getFirstPoint();
		
		// case of empty curve set, for example
		if(point1==null)
			return res;

		// if no intersection point, the curve is totally either inside or outside the box
		if(set.size()==0){
			if(box.contains(point1))
				res.addCurve(curve);
			return res;
		}
		
		double pos1, pos2;
		Iterator<java.lang.Double> iter = set.iterator();
		
		double pos0=0;
		
		// different behavior depending if first point lies inside the box
		if(box.contains(point1) && !box.getBoundary().contains(point1))
			pos0 = iter.next().doubleValue();
		
		
		// add the portions of curve between couples of intersections
		while(iter.hasNext()){
			pos1 = iter.next().doubleValue();
			if(iter.hasNext())
				pos2 = iter.next().doubleValue();
			else
				pos2 = pos0;
			res.addCurve(curve.getSubCurve(pos1, pos2));
		}
		
		return res;
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
	
}
