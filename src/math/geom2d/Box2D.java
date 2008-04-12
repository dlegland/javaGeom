/* File Box2D.java 
 *
 * Project : Java Geometry Library
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
 * Created on 05 mar. 2007
 */

// package
package math.geom2d;

// Imports
import java.util.*;

import math.geom2d.transform.AffineTransform2D;
import math.geom2d.curve.Boundary2D;
import math.geom2d.curve.BoundaryPolyCurve2D;
import math.geom2d.curve.BoundarySet2D;
import math.geom2d.curve.ContinuousBoundary2D;
import math.geom2d.curve.ContinuousCurve2D;
import math.geom2d.curve.ContinuousOrientedCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.line.ClosedPolyline2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.polygon.HRectangle2D;

/**
 * This class defines bounds of a shape. It stores limits in each direction:
 * <code>x</code> and <code>y</code>. It also provides methods for clipping
 * others shapes, depending on their type.
 */
public class Box2D {

	// ===================================================================
	// class variables
	
	private double xmin=0;
	private double xmax=0;
	private double ymin=0;
	private double ymax=0;
	
	// ===================================================================
	// constructors

	/** Empty constructor (size and position zero)*/
	public Box2D(){
		this(0, 0, 0, 0);	
	}

	/** Main constructor, given bounds for x coord, then bounds for y coord.
	 * A check is performed to ensure first bound is lower than second bound.
	 */
	public Box2D(double x0, double x1, double y0, double y1){
		xmin = Math.min(x0, x1); 
		xmax = Math.max(x0, x1);
		ymin = Math.min(y0, y1);
		ymax = Math.max(y0, y1);
	}
	
	/** Constructor from awt, to allow easy construction from existing apps.*/
	public Box2D(java.awt.geom.Rectangle2D rect){
		this(rect.getX(), rect.getX()+rect.getWidth(), 
				rect.getY(), rect.getY()+rect.getHeight());
	}

	/** Constructor from 2 points, giving extreme coordinates of the box.*/
	public Box2D(Point2D p1, Point2D p2){
		this(p1.getX(), p2.getX(), p1.getY(), p2.getY());
	}


	/** Constructor from a point, a width and an height */
	public Box2D(Point2D point, double w, double h){
		this(point.getX(), point.getX()+w, point.getY(), point.getY()+h);
	}
	
	// ===================================================================
	// accessors to Box2D fields

	public double getMinX(){
		return xmin;
	}
	
	public double getMinY(){
		return ymin;
	}
	
	public double getMaxX(){
		return xmax;
	}
	
	public double getMaxY(){
		return ymax;
	}
	
	public double getWidth(){
		return xmax-xmin;
	}
	
	public double getHeight(){
		return ymax-ymin;
	}
	
	/**
	 * Returns the set of lines defining half-planes which all contains the
	 * box.
	 * @return a set of straight lines
	 */
	public Collection<StraightLine2D> getClippingLines(){
		ArrayList<StraightLine2D> lines = new ArrayList<StraightLine2D>(4);
		lines.add(new StraightLine2D(xmin, ymin, xmax-xmin, 0));
		lines.add(new StraightLine2D(xmax, ymin, 0, ymax-ymin));
		lines.add(new StraightLine2D(xmax, ymax, xmin-xmax, 0));
		lines.add(new StraightLine2D(xmin, ymax, 0, ymin-ymax));
		return lines;
	}
	
	// ===================================================================
	// methods specific to Box2D
	
	/**
	 * Clip a curve, and return a CurveSet2D. If the curve is totally outside
	 * the box, return a CurveSet2D with 0 curves inside. If the curve is
	 * totally inside the box, return a CurveSet2D with only one curve, which
	 * is the original curve.
	 */
	public CurveSet2D<Curve2D> clipCurve(Curve2D curve){
		// Case of continuous curve
		if(curve instanceof ContinuousCurve2D)
			return this.clipContinuousCurve((ContinuousCurve2D) curve);
		
		// case of a CurveSet2D
		if(curve instanceof CurveSet2D && !(curve instanceof ContinuousCurve2D))
			return this.clipCurveSet((CurveSet2D<?>) curve);
		
		// Unknown case
		System.err.println("Unknown curve class in Box2D.clipCurve()");
		return new CurveSet2D<Curve2D>();
	}
	
//	public CurveSet2D<Curve2D> clipCurveOld(Curve2D curve){		
//		// First evacuate special case of (non continuous) curve sets.
//		// Iterate on curves, clip each curve -> get a curve set, and add each
//		// component of the clipped curve set to the result curve set
//		if(curve instanceof CurveSet2D || !(curve instanceof ContinuousCurve2D)){
//			// Clip the current curve
//			CurveSet2D<?> curveSet = (CurveSet2D<?>) curve;
//			CurveSet2D<Curve2D> result = new CurveSet2D<Curve2D>();
//			CurveSet2D<?> clipped;
//			
//			// a clipped parts of current curve to the result
//			for(Curve2D continuous : curveSet){
//				clipped = this.clipCurve(continuous);
//				for(Curve2D clippedPart : clipped)
//					result.addCurve(clippedPart);
//			}
//			
//			// return a set of curves
//			return result;
//		}
//		
//		// create array of points
//		ArrayList<Point2D> points = new ArrayList<Point2D>();
//		
//		// extract edges of the box boundary
//		Collection<LineSegment2D> edges = this.getEdges();
//		
//		// add the intersections with each edge to the list
//		for(LineSegment2D edge : edges)
//			points.addAll(curve.getIntersections(edge));
//				
//		// convert list to point array, sorted wrt to their position on the curve
//		SortedSet<java.lang.Double> set = new TreeSet<java.lang.Double>();
//		for(Point2D p : points)
//			set.add(new java.lang.Double(curve.getPosition(p)));
//			
//				
//		// Create CurveSet2D for storing the result
//		CurveSet2D<Curve2D> res = new CurveSet2D<Curve2D>();		
//		
//		// extract first point of the curve
//		Point2D point1 = curve.getFirstPoint();
//
//		// if no intersection point, the curve is totally either inside or outside the box
//		if(set.size()==0){
//			if(this.contains(point1))
//				res.addCurve(curve);
//			return res;
//		}
//		
//		double pos1, pos2;
//		Iterator<java.lang.Double> iter = set.iterator();
//		
//		// different behavior depending if first point lies inside the box
//		if(this.contains(point1) && !this.getBoundary().contains(point1))
//			res.addCurve(curve.getSubCurve(curve.getT0(), iter.next()));
//		
//		// add the portions of curve between couples of intersections
//		while(iter.hasNext()){
//			pos1 = iter.next().doubleValue();
//			if(iter.hasNext())
//				pos2 = iter.next().doubleValue();
//			else
//				pos2 = curve.getT1();
//			res.addCurve(curve.getSubCurve(pos1, pos2));
//		}
//		
//		return res;
//	}
	
	/**
	 * clip a CurveSet2D.
	 */
	private CurveSet2D<Curve2D> clipCurveSet(CurveSet2D<?> curveSet){
		// Clip the current curve
		CurveSet2D<Curve2D> result = new CurveSet2D<Curve2D>();
		CurveSet2D<?> clipped;

		// a clipped parts of current curve to the result
		for(Curve2D curve : curveSet){
			clipped = this.clipCurve(curve);
			for(Curve2D clippedPart : clipped)
				result.addCurve(clippedPart);
		}

		// return a set of curves
		return result;
	}
		
	/**
	 * clip a continuous curve.
	 */
	private CurveSet2D<Curve2D> clipContinuousCurve(ContinuousCurve2D curve){
		//TODO: there is a problem for degenerate cases. Possible solution:
		// each curve returns 2 intersection points in case of tangential intersection
		//TODO: should take into account unbounded boxes
		
		// create array of points
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		
		// extract edges of the box boundary
		Collection<LineSegment2D> edges = this.getEdges();
		
		// add the intersections with each edge to the list
		for(LineSegment2D edge : edges)
			points.addAll(curve.getIntersections(edge));
				
		// convert list to point array, sorted wrt to their position on the curve
		SortedSet<java.lang.Double> set = new TreeSet<java.lang.Double>();
		for(Point2D p : points)
			set.add(new java.lang.Double(curve.getPosition(p)));				
				
		// Create CurveSet2D for storing the result
		CurveSet2D<Curve2D> res = new CurveSet2D<Curve2D>();		
		
		// extract first point of the curve
		Point2D point1 = curve.getFirstPoint();

		// if no intersection point, the curve is totally either inside or outside the box
		if(set.size()==0){
			if(this.contains(point1))
				res.addCurve(curve);
			return res;
		}
		
		double pos1, pos2;
		Iterator<java.lang.Double> iter = set.iterator();
		
		// different behavior depending if first point lies inside the box
		if(this.contains(point1) && !this.getBoundary().contains(point1))
			res.addCurve(curve.getSubCurve(curve.getT0(), iter.next()));
		
		// add the portions of curve between couples of intersections
		while(iter.hasNext()){
			pos1 = iter.next().doubleValue();
			if(iter.hasNext())
				pos2 = iter.next().doubleValue();
			else
				pos2 = curve.getT1();
			res.addCurve(curve.getSubCurve(pos1, pos2));
		}
		
		return res;
	}
		
	/**
	 * clip a continuous smooth curve.
	 */
	public CurveSet2D<SmoothCurve2D> clipSmoothCurve(SmoothCurve2D curve){
		// create two CurveSet2D to be used in fip-flop
		CurveSet2D<SmoothCurve2D> result = new CurveSet2D<SmoothCurve2D>();
		CurveSet2D<SmoothCurve2D> buffer;
		
		// init first buffer with current curve
		result.addCurve(curve);
		
		// Iterate on each clipping line
		for(StraightLine2D line : this.getClippingLines()){
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
	public CurveSet2D<ContinuousOrientedCurve2D>
			clipContinuousOrientedCurve(ContinuousOrientedCurve2D curve){
	
		// create array of points
		ArrayList<Point2D> points = new ArrayList<Point2D>();

		// extract edges of the box boundary
		Collection<LineSegment2D> edges = this.getEdges();
		
		// add the intersections with each edge to the list
		for(LineSegment2D edge : edges)
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
			if(this.contains(point1))
				res.addCurve(curve);
			return res;
		}
		
		double pos1, pos2;
		Iterator<java.lang.Double> iter = set.iterator();
		
		double pos0=0;
		
		// different behavior depending if first point lies inside the box
		if(this.contains(point1) && !this.getBoundary().contains(point1))
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
	public BoundarySet2D<ContinuousBoundary2D>
	clipBoundary(Boundary2D boundary){
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
			clipped = this.clipContinuousOrientedCurve(boundaryCurve);
			
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
		Curve2D boxBoundary = this.getBoundary();
		
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
	 * convert to AWT rectangle. 
	 * @return an instance of java.awt.geom.Rectangle2D
	 */
	public java.awt.Rectangle getAsAWTRectangle(){
		int xr = (int) Math.floor(this.xmin);
		int yr = (int) Math.floor(this.ymin);
		int wr = (int) Math.ceil(this.xmax-xr);
		int hr = (int) Math.ceil(this.ymax-yr);
		return new java.awt.Rectangle(xr, yr, wr, hr);
	}
	
	/**
	 * convert to AWT Rectangle2D. Result is an instance of HRectangle, which extends
	 * java.awt.geom.Rectangle2D.Double.
	 * @return an instance of java.awt.geom.Rectangle2D
	 */
	public java.awt.geom.Rectangle2D getAsAWTRectangle2D(){
		return new HRectangle2D(xmin, ymin, xmax-xmin, ymax-ymin);
	}
	
	/**
	 * Converts to a rectangle. Result is an instance of HRectangle,
	 * which extends java.awt.geom.Rectangle2D.Double.
	 * @return an instance of HRectangle2D
	 */
	public Shape2D getAsRectangle(){
		return new HRectangle2D(xmin, ymin, xmax-xmin, ymax-ymin);
	}
	
	/**
	 * change the bounds of the box to also include bounds of the
	 * argument.
	 * @param box the bounding box to include
	 * @return this
	 */
	public Box2D merge(Box2D box){
		this.xmin = Math.min(this.xmin, box.xmin);
		this.xmax = Math.max(this.xmax, box.xmax);
		this.ymin = Math.min(this.ymin, box.ymin);
		this.ymax = Math.max(this.ymax, box.ymax);
		return this;
	}
	
	/**
	 * Returns the Box2D which contains both this box and the specified box.
	 * @param box the bounding box to include
	 * @return this
	 */
	public Box2D union(Box2D box){
		double xmin = Math.min(this.xmin, box.xmin);
		double xmax = Math.max(this.xmax, box.xmax);
		double ymin = Math.min(this.ymin, box.ymin);
		double ymax = Math.max(this.ymax, box.ymax);
		return new Box2D(xmin, xmax, ymin, ymax);
	}
	
	/**
	 * Returns the Box2D which is contained both by this box and by the
	 * specified box.
	 * @param box the bounding box to include
	 * @return this
	 */
	public Box2D intersection(Box2D box){
		double xmin = Math.max(this.xmin, box.xmin);
		double xmax = Math.min(this.xmax, box.xmax);
		double ymin = Math.max(this.ymin, box.ymin);
		double ymax = Math.min(this.ymax, box.ymax);
		return new Box2D(xmin, xmax, ymin, ymax);
	}
	
	
	
	// ===================================================================
	// methods from interface PolygonalShape2D
	
	public Collection<Point2D> getVertices(){
		ArrayList<Point2D> points = new ArrayList<Point2D>(4);
		points.add(new Point2D(xmin, ymin));
		points.add(new Point2D(xmax, ymin));
		points.add(new Point2D(xmax, ymax));
		points.add(new Point2D(xmin, ymax));
		return points;
	}

	/** Returns 4, the number of vertices of a rectangle*/
	public int getVerticesNumber(){
		return 4;
	}
	
	public Collection<LineSegment2D> getEdges(){
		ArrayList<LineSegment2D> edges = new ArrayList<LineSegment2D>(4);
		edges.add(new LineSegment2D(xmin, ymin, xmax, ymin));
		edges.add(new LineSegment2D(xmax, ymin, xmax, ymax));
		edges.add(new LineSegment2D(xmax, ymax, xmin, ymax));
		edges.add(new LineSegment2D(xmin, ymax, xmin, ymin));
		return edges;
	}
	
	
	// ===================================================================
	// methods from interface AbstractDomain2D
	
	public Boundary2D getBoundary(){
		Point2D pts[] = new Point2D[5];
		pts[0] = new Point2D(xmin, ymin);
		pts[1] = new Point2D(xmax, ymin);
		pts[2] = new Point2D(xmax, ymax);
		pts[3] = new Point2D(xmin, ymax);
		pts[4] = new Point2D(xmin, ymin);
		return new BoundarySet2D<ClosedPolyline2D>(new ClosedPolyline2D(pts));	
	}
	

	
	// ===================================================================
	// methods from Shape2D interface
	

	public double getDistance(java.awt.geom.Point2D p){
		return Math.max(getBoundary().getSignedDistance(p), 0);
	}
	
	public double getDistance(double x, double y){
		return Math.max(getBoundary().getSignedDistance(x, y), 0);
	}

	/** Returns true if all bounds are finite.*/
	public boolean isBounded(){
		if(Double.isInfinite(xmin)) return false;
		if(Double.isInfinite(ymin)) return false;
		if(Double.isInfinite(xmax)) return false;
		if(Double.isInfinite(ymax)) return false;
		return true;
	}
	
	/**
	 * Test if the specified Shape is totally contained in this Box2D.
	 * Note that the test is performed on the bounding box of the shape, then
	 * for rotated rectangles, this method can return false with a shape totally
	 * contained in the rectangle. The problem does not exist for horizontal
	 * rectangle, since edges of rectangle and bounding box are parallel.
	 */
	public boolean containsBounds(Shape2D shape){
		if(!shape.isBounded()) return false;
		for(Point2D point : shape.getBoundingBox().getVertices())
			if(!contains(point)) return false;

		return true;
	}

	/**
	 * Returns an instance of Box2D.
	 */
	public Box2D clip(Box2D box){
		return new Box2D(
				Math.max(this.xmin, box.xmin),
				Math.min(this.xmax, box.xmax),
				Math.max(this.ymin, box.ymin),
				Math.min(this.ymax, box.ymax));
	}

	/** 
	 * Return the new domain created by an affine transform of this box.
	 */
	public Box2D transform(AffineTransform2D trans){
		if(this.isBounded()){
			// Extract the 4 vertices, transform them, and compute
			// the new bounding box.
			Collection<Point2D> points = this.getVertices();
			double xmin = Double.POSITIVE_INFINITY;
			double xmax = Double.NEGATIVE_INFINITY;
			double ymin = Double.POSITIVE_INFINITY;
			double ymax = Double.NEGATIVE_INFINITY;
			for(Point2D point : points){
				point = point.transform(trans);
				xmin = Math.min(xmin, point.getX());
				ymin = Math.min(ymin, point.getY());
				xmax = Math.max(xmax, point.getX());
				ymax = Math.max(ymax, point.getY());
			}
			return new Box2D(xmin, xmax, ymin, ymax);
		}
		
		//TODO: implement a more precise method
		double xmin = Double.NEGATIVE_INFINITY;
		double xmax = Double.POSITIVE_INFINITY;
		double ymin = Double.NEGATIVE_INFINITY;
		double ymax = Double.POSITIVE_INFINITY;
		
		return new Box2D(xmin, xmax, ymin, ymax);
	}

	// ===================================================================
	// methods from Shape interface
	
	
	public boolean contains(java.awt.geom.Point2D point){
		double x = point.getX();
		double y = point.getY();
		if(x<xmin) return false;
		if(y<ymin) return false;
		if(x>xmax) return false;
		if(y>ymax) return false;
		return true;
	}
	
	public boolean contains(double x, double y){
		if(x<xmin) return false;
		if(y<ymin) return false;
		if(x>xmax) return false;
		if(y>ymax) return false;
		return true;
	}
	
	public boolean contains(double x0, double y0, double w, double h){
		if(!this.contains(x0, y0)) return false;
		if(!this.contains(x0+w, y0)) return false;
		if(!this.contains(x0+w, y0+h)) return false;
		if(!this.contains(x0, y0+h)) return false;
		return true;
	}
	
	public boolean contains(java.awt.geom.Rectangle2D rect){
		return this.contains(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	// ===================================================================
	// methods from Object interface
	
	/**
	 * Test if boxes are the same. two boxes are the same if the have the same
	 * bounds.
	 */
	public boolean equals(Object obj){		
		// check class, and cast type
		if(!(obj instanceof Box2D))
			return false;		
		Box2D box = (Box2D) obj;
		
		if(Math.abs(box.xmin-this.xmin)>Shape2D.ACCURACY) return false;
		if(Math.abs(box.ymin-this.ymin)>Shape2D.ACCURACY) return false;
		if(Math.abs(box.xmax-this.xmax)>Shape2D.ACCURACY) return false;
		if(Math.abs(box.ymax-this.ymax)>Shape2D.ACCURACY) return false;
		
		return true;
	}
}