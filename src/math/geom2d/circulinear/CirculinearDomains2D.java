/**
 * File: 	CirculinearDomain2DUtils.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 16 mai 09
 */
package math.geom2d.circulinear;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Shape2D;


/**
 * Some utilities for working with circulinear curves.
 * @author dlegland
 *
 */
public class CirculinearDomains2D {
	
	public final static CirculinearDomain2D computeBuffer(
			CirculinearDomain2D domain, double dist) {
		
		ArrayList<CirculinearContour2D> rings =
			new ArrayList<CirculinearContour2D>();
		
		// iterate on all continuous curves
		for(CirculinearContour2D contour : domain.contours()) {
			// split the curve into a set of non self-intersecting curves
			for(CirculinearContinuousCurve2D simpleCurve : 
				CirculinearCurves2D.splitContinuousCurve(contour)) {
				CirculinearContour2D boundary = 
					new BoundaryPolyCirculinearCurve2D<CirculinearContinuousCurve2D>(
							simpleCurve.smoothPieces(), contour.isClosed());
				// compute the rings composing the simple curve buffer
				rings.addAll(computeBufferSimpleRing(boundary, dist));
			}
		}
		
		// All the rings are created, we can now create a new domain with the
		// set of rings
		return new GenericCirculinearDomain2D(
				new CirculinearContourArray2D<CirculinearContour2D>(rings));
	}
	
	/**
	 * Computes the rings that form the domain of a circulinear curve which
	 * does not self-intersect.
	 */
	public final static Collection<CirculinearContour2D> 
	computeBufferSimpleRing(CirculinearContour2D curve, double d) {
		
		// prepare an array to store the set of rings
		ArrayList<CirculinearContour2D> rings =
			new ArrayList<CirculinearContour2D>();
		
		// the parallel in the positive side
		CirculinearContinuousCurve2D parallel1 = curve.parallel(d);
		
		// split each parallel into continuous curves
		CirculinearCurveArray2D<CirculinearContinuousCurve2D> curves =
			new CirculinearCurveArray2D<CirculinearContinuousCurve2D>();
		
		// select only curve parts which do not cross original curve
		for(CirculinearContinuousCurve2D split : 
				CirculinearCurves2D.splitContinuousCurve(parallel1)) {
			if(CirculinearCurves2D.findIntersections(curve, split).size()==0)
				curves.add(split);
		}
		
		// create a new boundary for each parallel curve
		for(CirculinearContinuousCurve2D split : curves) {
			rings.add(
					new BoundaryPolyCirculinearCurve2D<CirculinearContinuousCurve2D>(
							split.smoothPieces(), split.isClosed()));
		}
		
		// prepare an array to store the set of rings
		ArrayList<CirculinearContour2D> rings2 =
			new ArrayList<CirculinearContour2D>();

		// iterate on the set of rings
		for(CirculinearContour2D ring : rings)
			// split rings into curves which do not self-intersect
			for(CirculinearContinuousCurve2D split : 
				CirculinearCurves2D.splitContinuousCurve(ring)) {
				
				// compute distance to original curve
				// (assuming it is sufficient to compute distance to vertices
				// of the reference curve).
				double dist = CirculinearCurves2D.getDistanceCurvePoints(
						curve, split.singularPoints());
				
				// check if distance condition is verified
				if(dist-d<-Shape2D.ACCURACY)
					continue;
				
				// convert the set of elements to a Circulinear ring
				rings2.add(
						new BoundaryPolyCirculinearCurve2D<CirculinearContinuousCurve2D>(
								split.smoothPieces(), split.isClosed()));
		}
		
		// return the set of created rings
		return rings2;
	}
}
