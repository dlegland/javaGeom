/**
 * 
 */

package math.geom2d.domain;

import java.util.ArrayList;
import java.util.Iterator;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.UnboundedBox2DException;
import math.geom2d.curve.ContinuousCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.Curves2D;
import math.geom2d.curve.CurveArray2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.polygon.Polyline2D;

/**
 * Collects some useful methods for operating on boundary curves.
 * 
 * @author dlegland
 */
public abstract class Boundaries2D {

    /**
     * Clip a curve, and return a CurveSet2D. If the curve is totally outside
     * the box, return a CurveSet2D with 0 curves inside. If the curve is
     * totally inside the box, return a CurveSet2D with only one curve, which is
     * the original curve.
     */
    public final static CurveSet2D<ContinuousOrientedCurve2D> 
    clipContinuousOrientedCurve(ContinuousOrientedCurve2D curve, Box2D box) {

    	// create result array
    	CurveArray2D<ContinuousOrientedCurve2D> result = 
        	new CurveArray2D<ContinuousOrientedCurve2D>();
    	
    	// for each clipped curve, add its pieces
        for (ContinuousCurve2D cont : Curves2D.clipContinuousCurve(curve, box))
            if (cont instanceof ContinuousOrientedCurve2D)
                result.add((ContinuousOrientedCurve2D) cont);

        return result;

        // // create array of points
        // ArrayList<Point2D> points = new ArrayList<Point2D>();
        //
        // // add the intersections with edges of the box boundary
        // for(StraightObject2D edge : box.getEdges())
        // points.addAll(curve.getIntersections(edge));
        //		
        // // convert list to point array, sorted wrt to their position on the
        // curve
        // SortedSet<java.lang.Double> set = new TreeSet<java.lang.Double>();
        // for(Point2D p : points)
        // set.add(new java.lang.Double(curve.getPosition(p)));
        //				
        // // Create curveset for storing the result
        // CurveSet2D<ContinuousOrientedCurve2D> res =
        // new CurveSet2D<ContinuousOrientedCurve2D>();
        //				
        // // extract first point of the curve
        // Point2D point1 = curve.getFirstPoint();
        //		
        // // case of empty curve set, for example
        // if(point1==null)
        // return res;
        //
        // // if no intersection point, the curve is totally either inside or
        // outside the box
        // if(set.size()==0){
        // if(box.contains(point1))
        // res.addCurve(curve);
        // return res;
        // }
        //		
        // double pos1, pos2;
        // Iterator<java.lang.Double> iter = set.iterator();
        //		
        // double pos0=0;
        //		
        // // different behavior depending if first point lies inside the box
        // if(box.contains(point1) && !box.getBoundary().contains(point1))
        // pos0 = iter.next().doubleValue();
        //		
        //		
        // // add the portions of curve between couples of intersections
        // while(iter.hasNext()){
        // pos1 = iter.next().doubleValue();
        // if(iter.hasNext())
        // pos2 = iter.next().doubleValue();
        // else
        // pos2 = pos0;
        // res.addCurve(curve.getSubCurve(pos1, pos2));
        // }
        //		
        // return res;
    }

    /**
     * Clips a boundary and closes the result curve. Returns an instance of
     * ContourArray2D.
     */
    public final static ContourArray2D<Contour2D> clipBoundary(
            Boundary2D boundary, Box2D box) {

    	// basic check-up
        if (!box.isBounded())
            throw new UnboundedBox2DException(box);

        // iteration variable
        ContinuousOrientedCurve2D curve;

        // The set of boundary curves. Each curve of this set is either a
        // curve of the original boundary, or a composition of a portion of
        // original boundary with a portion of the box.
		ContourArray2D<Contour2D> res = new ContourArray2D<Contour2D>();

        // to store result of curve clipping
        CurveSet2D<ContinuousOrientedCurve2D> clipped;

        // to store set of all clipped curves
        CurveArray2D<ContinuousOrientedCurve2D> curveSet = 
        	new CurveArray2D<ContinuousOrientedCurve2D>();

        // Iterate on contours: clip each contour with box, 
        // and add clipped curves to the array 'curveSet'
        for (Contour2D contour : boundary.continuousCurves()) {
            clipped = Boundaries2D.clipContinuousOrientedCurve(contour, box);

            for (ContinuousOrientedCurve2D clip : clipped)
                curveSet.add(clip);
        }

        // array of position on the box for first and last point of each curve
        int nc = curveSet.size();
        double[] startPositions = new double[nc];
        double[] endPositions = new double[nc];

        // Flag indicating if the curve intersects the boundary of the box
        boolean intersect = false;

        // also create array of curves
        ContinuousOrientedCurve2D[] curves = new ContinuousOrientedCurve2D[nc];

        // boundary of the box
        Curve2D boxBoundary = box.boundary();

        // compute position on the box for first and last point of each curve
        Iterator<ContinuousOrientedCurve2D> iter = curveSet.curves().iterator();

        for (int i = 0; i < nc; i++) {
            // save current curve
            curve = iter.next();
            curves[i] = curve;

            if (curve.isClosed()) {
                startPositions[i] = java.lang.Double.NaN;
                endPositions[i] = java.lang.Double.NaN;
                continue;
            }

            // compute positions of first point and last point on box boundary
            startPositions[i] = boxBoundary.position(curve.firstPoint());
            endPositions[i] = boxBoundary.position(curve.lastPoint());

            // set up the flag
            intersect = true;
        }

        // theoretical number of boundary curves. Set to the number of clipped
        // curves, but total number can be reduced if several clipped curves
        // belong to the same boundary curve.
        int nb = nc;

        // current index of curve
        int c = 0;

		// iterate while there are boundary curve to build
		while (c < nb) {
			int ind = c;
			// find the current curve (used curves are removed from array)
			while (curves[ind] == null)
				ind++;

            // current curve
            curve = curves[ind];

            // if curve is closed, we can switch to next curve
            if (curve.isClosed()) {
                // Add current boundary to the set of boundary curves
                if (curve instanceof Contour2D) {
                    res.add((Contour2D) curve);
                } else {
                    BoundaryPolyCurve2D<ContinuousOrientedCurve2D> bnd = 
                    	new BoundaryPolyCurve2D<ContinuousOrientedCurve2D>();
                    bnd.add(curve);
                    res.add(bnd);
                }
                curves[ind] = null;

                // switch to next curve
                c++;
                continue;
            }

            // create a new Boundary curve
            BoundaryPolyCurve2D<ContinuousOrientedCurve2D> boundary0 = 
            	new BoundaryPolyCurve2D<ContinuousOrientedCurve2D>();

            // add current curve to boundary curve
            boundary0.add(curve);

            // get last points (to add a line with next curve)
            Point2D p0 = curve.firstPoint();
            Point2D p1 = curve.lastPoint();

            // index of first curve, used as a stop flag
            int ind0 = ind;

            // store indices of curves, to remove them later
            ArrayList<Integer> indices = new ArrayList<Integer>();
            indices.add(new Integer(ind));

            // position of last point of current curve on box boundary
            ind = findNextCurveIndex(startPositions, endPositions[ind0]);

            // iterate while we don't come back to first point
            while (ind != ind0) {
                // find the curve whose first point is just after last point
                // of current curve on box boundary
                curve = curves[ind];

                // add a link between previous curve and current curve
                Point2D p0i = curve.firstPoint();
				boundary0.add(getBoundaryPortion(box, p1, p0i));

                // add to current boundary
                boundary0.add(curve);

                indices.add(new Integer(ind));

                // find index and position of next curve
                ind = findNextCurveIndex(startPositions, endPositions[ind]);

                // get last points
                p1 = curve.lastPoint();

                // decrease total number of boundary curves
                nb--;
            }

            // add a line from last point to first point
            boundary0.add(getBoundaryPortion(box, p1, p0));

            // Add current boundary to the set of boundary curves
            res.add(boundary0);

            // remove curves from array
            Iterator<Integer> iter2 = indices.iterator();
            while (iter2.hasNext())
                curves[iter2.next().intValue()] = null;

            // next curve !
            c++;
        }

        // Add processing when the box boundary does not intersect the curve.
        // In this case add the boundary of the box to the resulting boundary
        // set.
        if (!intersect) {
            Point2D vertex = box.vertices().iterator().next();
            if (boundary.isInside(vertex))
                res.add(box.asRectangle().boundary().firstCurve());
        }

        // return the result
        return res;
    }

    public final static int findNextCurveIndex(double[] positions, double pos) {
        int ind = -1;
        double posMin = java.lang.Double.MAX_VALUE;
        for (int i = 0; i<positions.length; i++) {
            // avoid NaN
            if (java.lang.Double.isNaN(positions[i]))
                continue;
            // avoid values before
            if (positions[i]-pos<Shape2D.ACCURACY)
                continue;

            // test if closer that other points
            if (positions[i]<posMin) {
                ind = i;
                posMin = positions[i];
            }
        }

        if (ind!=-1)
            return ind;

        // if not found, return index of smallest value (mean that pos is last
        // point on the boundary, so we need to start at the beginning).
        for (int i = 0; i<positions.length; i++) {
            if (java.lang.Double.isNaN(positions[i]))
                continue;
            if (positions[i]-posMin<Shape2D.ACCURACY) {
                ind = i;
                posMin = positions[i];
            }
        }
        return ind;
    }

    /**
     * Extracts a portion of the boundary of a bounded box.
     * 
     * @param box the box from which one extract a portion of boundary
     * @param p0 the first point of the portion
     * @param p1 the last point of the portion
     * @return the portion of the bounding box boundary as a Polyline2D
     */
    public final static Polyline2D getBoundaryPortion(Box2D box, Point2D p0,
            Point2D p1) {
        Boundary2D boundary = box.boundary();

        // position of start and end points
        double t0 = boundary.position(p0);
        double t1 = boundary.position(p1);

        // curve index of each point
        int ind0 = (int) Math.floor(t0);
        int ind1 = (int) Math.floor(t1);

        // Simple case: returns a polyline with only 2 vertices
		if (ind0 == ind1 && t0 < t1)
			return new Polyline2D(new Point2D[] { p0, p1 });

        // Create an array to store vertices
        // Array can contain at most 6 vertices: 4 for the box corners,
        // and 2 for curve extremities.
        ArrayList<Point2D> vertices = new ArrayList<Point2D>(6);

        // add the first point.
        vertices.add(p0);

		// compute index of first box boundary edge
		int ind = (ind0 + 1) % 4;

		// add all vertices segments between the 2 end points
		while (ind != ind1) {
			vertices.add(boundary.point(ind));
			ind = (ind + 1) % 4;
		}
        vertices.add(boundary.point(ind));

        // add the last line segment
        vertices.add(p1);

        return new Polyline2D(vertices);
    }
}
