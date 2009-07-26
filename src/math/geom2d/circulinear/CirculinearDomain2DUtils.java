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
public class CirculinearDomain2DUtils {
    
	
	
	public final static CirculinearDomain2D computeBuffer(
			CirculinearDomain2D domain, double d) {
		
		ArrayList<CirculinearContour2D> rings =
			new ArrayList<CirculinearContour2D>();
		
		// iterate on all continuous curves
		for(ContinuousCirculinearCurve2D cont : 
			domain.getBoundary().getContinuousCurves()) {
			// split the curve into a set of non self-intersecting curves
			for(ContinuousCirculinearCurve2D simpleCurve : 
				CirculinearCurve2DUtils.splitContinuousCurve(cont)) {
				CirculinearContour2D boundary = 
					new BoundaryPolyCirculinearCurve2D<ContinuousCirculinearCurve2D>(
							simpleCurve.getSmoothPieces());
				// compute the rings composing the simple curve buffer
				rings.addAll(computeBufferSimpleRing(boundary, d));
			}
		}
		
		// All the rings are created, we can now create a new domain with the
		// set of rings
		return new GenericCirculinearDomain2D(
				new CirculinearBoundarySet2D<CirculinearContour2D>(rings));
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
		ContinuousCirculinearCurve2D parallel1 = curve.getParallel(d);
		
		// split each parallel into continuous curves
		CirculinearCurveSet2D<ContinuousCirculinearCurve2D> curves =
			new CirculinearCurveSet2D<ContinuousCirculinearCurve2D>();
		
		// select only curve parts which do not cross original curve
		for(ContinuousCirculinearCurve2D split : 
				CirculinearCurve2DUtils.splitContinuousCurve(parallel1)) {
			if(CirculinearCurve2DUtils.findIntersections(curve, split).size()==0)
				curves.addCurve(split);
		}
		
		// create a new boundary for each parallel curve
		for(ContinuousCirculinearCurve2D split : curves) {
			rings.add(
					new BoundaryPolyCirculinearCurve2D<ContinuousCirculinearCurve2D>(
							split.getSmoothPieces(), split.isClosed()));
		}
		
		// prepare an array to store the set of rings
		ArrayList<CirculinearContour2D> rings2 =
			new ArrayList<CirculinearContour2D>();

		// iterate on the set of rings
		for(CirculinearContour2D ring : rings)
			// split rings into curves which do not self-intersect
			for(ContinuousCirculinearCurve2D split : 
				CirculinearCurve2DUtils.splitContinuousCurve(ring)) {
				
				// compute distance to original curve
				// (assuming it is sufficient to compute distance to vertices
				// of the reference curve).
				double dist = CirculinearCurve2DUtils.getDistanceCurvePoints(
						curve, split.getSingularPoints());
				
				// check if distance condition is verified
				if(dist-d<-Shape2D.ACCURACY)
					continue;
				
				// convert the set of elements to a Circulinear ring
				rings2.add(
						new BoundaryPolyCirculinearCurve2D<ContinuousCirculinearCurve2D>(
								split.getSmoothPieces(), split.isClosed()));
		}
		
		// return the set of created rings
		return rings2;
	}
}
