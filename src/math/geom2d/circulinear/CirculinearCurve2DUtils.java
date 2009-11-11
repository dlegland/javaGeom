/**
 * File: 	CirculinearCurve2DUtils.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 16 mai 09
 */
package math.geom2d.circulinear;

import static java.lang.Math.PI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.conic.CircularShape2D;
import math.geom2d.curve.ContinuousCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.Curve2DUtils;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.line.LinearShape2D;
import math.geom2d.point.PointSet2D;


/**
 * Some utilities for working with circulinear curves.
 * @author dlegland
 *
 */
public class CirculinearCurve2DUtils {
    
	/**
	 * Converts a curve to a circulinear curve, by concatenating all elements
	 * of the curve to the appropriate circulinear curve type. If the curve
	 * contains onr or more non-circulinear smooth curve, a 
	 * NonCirculinearClassException is thrown.
	 */
	public final static CirculinearCurve2D convert(Curve2D curve) {
		// first check type, to avoid unnecessary computations
		if (curve instanceof CirculinearCurve2D)
			return (CirculinearCurve2D) curve;
		
		// If the curve is continuous, creates a CirculinearContinuousCurve2D
		if (curve instanceof ContinuousCurve2D) {
			// extract smooth pieces
			ContinuousCurve2D continuous = (ContinuousCurve2D) curve;
			Collection<? extends SmoothCurve2D> smoothPieces = 
				continuous.getSmoothPieces();
			
			// prepare array of elements
			ArrayList<CirculinearElement2D> elements = 
				new ArrayList<CirculinearElement2D>(smoothPieces.size());
			
			// class cast for each element, or throw an exception
			for (SmoothCurve2D smooth : smoothPieces) {
				if (smooth instanceof CirculinearElement2D)
					elements.add((CirculinearElement2D) smooth);
				else
					throw new NonCirculinearClassException(smooth);
			}
			
			// create the resulting CirculinearContinuousCurve2D
			return new PolyCirculinearCurve2D<CirculinearElement2D>(elements);
		}
		
		// If the curve is continuous, creates a CirculinearContinuousCurve2D
		if (curve instanceof CurveSet2D) {
			// extract smooth pieces
			CurveSet2D<?> set = (CurveSet2D<?>) curve;
			Collection<? extends ContinuousCurve2D> continuousCurves = 
				set.getContinuousCurves();
			
			// prepare array of elements
			ArrayList<ContinuousCirculinearCurve2D> curves = 
				new ArrayList<ContinuousCirculinearCurve2D>(
						continuousCurves.size());
			
			// class cast for each element, or throw an exception
			for (ContinuousCurve2D continuous : continuousCurves) {
				if (continuous instanceof ContinuousCirculinearCurve2D)
					curves.add((ContinuousCirculinearCurve2D) continuous);
				else
					curves.add((ContinuousCirculinearCurve2D) convert(continuous));
			}
			
			// create the resulting CirculinearContinuousCurve2D
			return new CirculinearCurveSet2D<ContinuousCirculinearCurve2D>(curves);
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#getLength(double)
	 */
	public final static double getLength(
			CurveSet2D<? extends CirculinearCurve2D> curve, 
			double pos) {
		//init
		double length = 0;
		
		// add length of each curve before current curve
		int index = curve.getCurveIndex(pos);
		for(int i=0; i<index; i++)
			length += curve.getCurve(i).getLength();
		
		// add portion of length for last curve
		if(index<curve.getCurveNumber()) {
			double pos2 = curve.getLocalPosition(pos-2*index);
			length += curve.getCurve(index).getLength(pos2);
		}
		
		// return result
		return length;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#getPosition(double)
	 */
	public final static double getPosition(
			CurveSet2D<? extends CirculinearCurve2D> curveSet,
			double length) {
		
		// position to compute
		double pos = 0;
		
		// index of current curve
		int index = 0;
		
		// cumulative length
		double cumLength = getLength(curveSet, curveSet.getT0());
		
		// iterate on all curves
		for(CirculinearCurve2D curve : curveSet.getCurves()) {
			// length of current curve
			double curveLength = curve.getLength();
			
			// add either 2, or fraction of length
			if(cumLength+curveLength<length) {
				cumLength += curveLength;
				index ++;
			} else {
				// add local position on current curve
				double pos2 = curve.getPosition(length-cumLength);
				pos = curveSet.getGlobalPosition(index, pos2);
				break;
			}			
		}
		
		// return the result
		return pos;
	}

	public final static CirculinearCurve2D createParallel(
			CirculinearCurve2D curve, double dist) {
		
		// case of a continuous curve -> call specialized method
		if (curve instanceof ContinuousCirculinearCurve2D) {
			return createContinuousParallel(
					(ContinuousCirculinearCurve2D)curve, dist);
		} 
		
		// Create array for storing result
		CirculinearCurveSet2D<ContinuousCirculinearCurve2D> parallel =
			new CirculinearCurveSet2D<ContinuousCirculinearCurve2D>();
		
		// compute parallel of each continuous part, and add it to the result
		for(ContinuousCirculinearCurve2D continuous : 
			curve.getContinuousCurves()){
			ContinuousCirculinearCurve2D contParallel = 
				createContinuousParallel(continuous, dist);
			if(contParallel!=null)
				parallel.addCurve(contParallel);
		}
		
		// return the set of parallel curves
		return parallel;
	}
    
	
	public final static ContinuousCirculinearCurve2D createContinuousParallel(
			ContinuousCirculinearCurve2D curve, double dist) {
		
		// For circulinear elements, getParallel() is already implemented
		if (curve instanceof CirculinearElement2D) {
			return ((CirculinearElement2D)curve).getParallel(dist);
		} 

		// extract collection of circulinear elements
		Collection<? extends CirculinearElement2D> elements = 
			curve.getSmoothPieces();
		Iterator<? extends CirculinearElement2D> iterator = 
			elements.iterator();

		// previous curve
		CirculinearElement2D previous = null;
		CirculinearElement2D current = null;

		// create array for storing result
		PolyCirculinearCurve2D<ContinuousCirculinearCurve2D> parallel = 
			new PolyCirculinearCurve2D<ContinuousCirculinearCurve2D> ();

		// check if curve is empty
		if(!iterator.hasNext())
			return parallel;

		// add parallel to the first curve
		previous = iterator.next();
		CirculinearElement2D elementParallel = previous.getParallel(dist);
		parallel.addCurve(elementParallel);

		// iterate on smooth pieces
		while(iterator.hasNext()){
			// get current curve
			current = iterator.next();

			// add circle arc between the two curve elements
			addCircularJunction(parallel, previous, current, dist);
			
			// add parallel to current curve
			parallel.addCurve(current.getParallel(dist));

			// prepare for next piece
			previous = current;
		}

		// Add circle arc to close the parallel curve
		if(curve.isClosed()) {
			current = elements.iterator().next();
			addCircularJunction(parallel, previous, current, dist);
			parallel.setClosed(true);
		}

		return parallel;
	}
	
	private final static void addCircularJunction(
			PolyCirculinearCurve2D<ContinuousCirculinearCurve2D> parallel,
			CirculinearElement2D previous, 
			CirculinearElement2D current, double dist) {		
		// center of circle arc
		Point2D center = current.getFirstPoint();

		// compute tangents to each portion
		Vector2D vp = previous.getTangent(previous.getT1());
		Vector2D vc = current.getTangent(current.getT0());

		// compute angles
		double startAngle, endAngle;
		if(dist>0) {
			startAngle = vp.getAngle() - PI/2;
			endAngle = vc.getAngle() - PI/2;
		} else {
			startAngle = vp.getAngle() + PI/2;
			endAngle = vc.getAngle() + PI/2;
		}
		
		// format angles to stay between 0 and 2*PI
		startAngle = Angle2D.formatAngle(startAngle);
		endAngle = Angle2D.formatAngle(endAngle);
		
		// compute angle difference, in absolute value
		double diffAngle = endAngle-startAngle;
		diffAngle = Math.min(diffAngle, 2*PI-diffAngle);
		
		// If the angle difference is too small, we consider the two curves
		// touch at their extremities
		if(Math.abs(diffAngle)<1e-10)
			return;
		
		// otherwise add a circle arc to the polycurve
		parallel.addCurve(new CircleArc2D(
				center, Math.abs(dist), startAngle, endAngle, dist>0));
	}
	
	/**
	 * Compute intersection point of a single curve, by iterating on pair of 
	 * Circulinear elements composing the curve.
	 * @return the set of self-intersection points
	 */
	public final static Collection<Point2D> findSelfIntersections(
			CirculinearCurve2D curve) {
		
		// create array of circulinear elements
		ArrayList<CirculinearElement2D> elements = 
			new ArrayList<CirculinearElement2D>();
		
		// extract all circulinear elements of the curve
		for(ContinuousCirculinearCurve2D cont : curve.getContinuousCurves())
			elements.addAll(cont.getSmoothPieces());
		
		// create array for storing result
		ArrayList<Point2D> result = new ArrayList<Point2D>(0);
		
		// iterate on each couple of elements
		int n = elements.size();
		for(int i=0; i<n-1; i++) {
			CirculinearElement2D elem1 = elements.get(i);
			for(int j=i; j<n; j++) {
				CirculinearElement2D elem2 = elements.get(j);
				// iterate on intersection between consecutive elements
				for(Point2D inter : findIntersections(elem1, elem2)) {
					// do not keep extremities
					if(inter.equals(elem1.getLastPoint()) &&
							inter.equals(elem2.getFirstPoint())) continue;
					if(inter.equals(elem1.getFirstPoint()) &&
							inter.equals(elem2.getLastPoint())) continue;
					// add the intersection if we keep it
					result.add(inter);
				}
			}
		}
		
		// return the set of intersections
		return result;
	}

	public final static double [][] locateSelfIntersections(
			CurveSet2D<? extends CirculinearElement2D> curve) {
		
		// create array for storing result
		ArrayList<Double> list1 = new ArrayList<Double>(0);
		ArrayList<Double> list2 = new ArrayList<Double>(0);
		
		// iterate on each couple of elements
		int n = curve.getCurveNumber();
		for(int i=0; i<n-1; i++) {
			CirculinearElement2D elem1 = curve.getCurve(i);
			for(int j=i+1; j<n; j++) {
				CirculinearElement2D elem2 = curve.getCurve(j);
				// iterate on intersection between consecutive elements
				for(Point2D inter : findIntersections(elem1, elem2)) {
					// do not keep extremities
					if(!Double.isInfinite(elem1.getT1())&&!Double.isInfinite(elem2.getT0()))
						if(inter.equals(elem1.getLastPoint()) &&
								inter.equals(elem2.getFirstPoint())) continue;
					if(!Double.isInfinite(elem1.getT0())&&!Double.isInfinite(elem2.getT1()))
						if(inter.equals(elem1.getFirstPoint()) &&
							inter.equals(elem2.getLastPoint())) continue;
					
					// add the intersection if we keep it
					list1.add(2*i + Curve2DUtils.toUnitSegment(
							elem1.getPosition(inter),
							elem1.getT0(), elem1.getT1()));
					list2.add(2*j + Curve2DUtils.toUnitSegment(
							elem2.getPosition(inter), 
							elem2.getT0(), elem2.getT1()));
				}
			}
		}
		
		// convert the 2 lists into a n*2 array
		int np = list1.size(); 
		double[][] result = new double[np][2];
		for(int i=0; i<np; i++){
			result[i][0] = list1.get(i);
			result[i][1] = list2.get(i);
		}
		
		// return the array of positions
		return result;
	}
	
	public final static Collection<Point2D> findIntersections(
			CirculinearCurve2D curve1, CirculinearCurve2D curve2) {
		
		// create array of circulinear elements
		ArrayList<CirculinearElement2D> elements1 =
			new ArrayList<CirculinearElement2D>();
		ArrayList<CirculinearElement2D> elements2 = 
			new ArrayList<CirculinearElement2D>();
		
		// extract all circulinear elements of the curve
		for(ContinuousCirculinearCurve2D cont : curve1.getContinuousCurves())
			elements1.addAll(cont.getSmoothPieces());
		for(ContinuousCirculinearCurve2D cont : curve2.getContinuousCurves())
			elements2.addAll(cont.getSmoothPieces());
		
		// create array for storing result
		ArrayList<Point2D> result = new ArrayList<Point2D>(0);
		
		// iterate on each couple of elements
		int n1 = elements1.size();
		int n2 = elements2.size();
		for(int i=0; i<n1; i++) {
			CirculinearElement2D elem1 = elements1.get(i);
			for(int j=0; j<n2; j++) {
				CirculinearElement2D elem2 = elements2.get(j);
				// iterate on intersection between consecutive elements
				for(Point2D inter : findIntersections(elem1, elem2)) {
					// add the intersection if we keep it
					result.add(inter);
				}
			}
		}
		
		// return the set of intersections
		return result;
	}
	
	/**
	 * Locate intersection points of two curves. The result is a N-by-2 array
	 * of double, where N is the number of intersections. For each row, the
	 * first element is the position on the first curve, and the second
	 * element is the position on the second curve.
	 */
	public final static double[][] locateIntersections(
			CirculinearCurve2D curve1, CirculinearCurve2D curve2) {
		
		// create array for storing result
		ArrayList<Double> list1 = new ArrayList<Double>(0);
		ArrayList<Double> list2 = new ArrayList<Double>(0);

		// create array of circulinear elements
		ArrayList<CirculinearElement2D> elements1 = 
			new ArrayList<CirculinearElement2D>();
		ArrayList<CirculinearElement2D> elements2 = 
			new ArrayList<CirculinearElement2D>();
		
		// extract all circulinear elements of the curve
		for(ContinuousCirculinearCurve2D cont : curve1.getContinuousCurves())
			elements1.addAll(cont.getSmoothPieces());
		for(ContinuousCirculinearCurve2D cont : curve2.getContinuousCurves())
			elements2.addAll(cont.getSmoothPieces());
		
		// iterate on each couple of elements
		int n1 = elements1.size();
		int n2 = elements2.size();
		for(int i=0; i<n1; i++) {
			CirculinearElement2D elem1 = elements1.get(i);
			for(int j=0; j<n2; j++) {
				CirculinearElement2D elem2 = elements2.get(j);
				// iterate on intersection between consecutive elements
				for(Point2D inter : findIntersections(elem1, elem2)) {
					// add the intersection if we keep it
					list1.add(curve1.getPosition(inter));
					list2.add(curve2.getPosition(inter));
				}
			}
		}
		
		// convert the 2 lists into a n*2 array
		int np = list1.size(); 
		double[][] result = new double[np][2];
		for(int i=0; i<np; i++){
			result[i][0] = list1.get(i);
			result[i][1] = list2.get(i);
		}
		
		// return the array of positions
		return result;
	}
	
	/**
	 * Compute the intersections, if they exist, of two circulinear elements.
	 */
	public final static Collection<Point2D> findIntersections(
			CirculinearElement2D elem1, 
			CirculinearElement2D elem2) {
		
		// First try to use linear shape methods
		if(elem1 instanceof LinearShape2D) {
			return elem2.getIntersections((LinearShape2D) elem1);
		}		
		if(elem2 instanceof LinearShape2D) {
			return elem1.getIntersections((LinearShape2D) elem2);
		}
		
		// From now, both elem1 and elem2 are instances of CircleShape2D
		// It is therefore possible to extract support circles
		Circle2D circ1 = ((CircularShape2D) elem1).getSupportingCircle();
		Circle2D circ2 = ((CircularShape2D) elem2).getSupportingCircle();
		
		// create array for storing result (max 2 possible intersections)
		ArrayList<Point2D> pts = new ArrayList<Point2D>(2);
		
		// for each of the circle intersections, check if they belong to
		// both elements
		for(Point2D inter : Circle2D.getIntersections(circ1, circ2)) {
			if(elem1.contains(inter) && elem2.contains(inter))
				pts.add(inter);
		}
		
		// return found intersections
		return pts;
	}
	
	/**
	 * Split a continuous curve which self-intersects into a set of continuous
	 * circulinear curves which do not self-intersect.
	 * @param curve the curve to split
	 * @return a set of non-self-intersecting continuous curves
	 */
	public final static Collection<ContinuousCirculinearCurve2D> 
	splitContinuousCurve(ContinuousCirculinearCurve2D curve) {
		
		double pos0, pos1, pos2;
		
		// create the array of resulting curves
        ArrayList<ContinuousCirculinearCurve2D> result =
        	new ArrayList<ContinuousCirculinearCurve2D>();

        // Instances of CirculinearElement2D can not self-intersect
		if (curve instanceof CirculinearElement2D){
			result.add(curve);
			return result;
		}
		
		// convert the curve to a poly-circulinear curve, to be able to call
		// the "locateSelfIntersections" method.
		PolyCirculinearCurve2D<CirculinearElement2D> polyCurve = 
			createPolyCurve(curve.getSmoothPieces(), curve.isClosed());
		
        // identify couples of intersections
		double[][] couples = locateSelfIntersections(polyCurve);
		
		// case of curve without self-intersections
		if(couples.length==0){			
	        // create continuous curve formed only by circulinear elements
			result.add(createPolyCurve(polyCurve.getSmoothPieces(),
					curve.isClosed()));
	        return result;
		}
		
		// put all positions into a tree map
		TreeMap<Double, Double> twins = new TreeMap<Double, Double>();
        for (int i=0; i<couples.length; i++) {
        	pos1 = couples[i][0];
        	pos2 = couples[i][1];
        	twins.put(pos1, pos2);
        	twins.put(pos2, pos1);
        }
        
        // an array for the portions of curves
        ArrayList<CirculinearElement2D> elements;
        
        
        // Process the first curve
        
        // create new empty array of elements for current continuous curve
        elements = new ArrayList<CirculinearElement2D>();
        
        // get first intersection
        pos1 = polyCurve.getT0();
        pos2 = twins.firstKey();
        pos0 = pos2;
        
        // add the first portion of curve, starting from the beginning
        //elements.addAll(polyCurve.getSubCurve(pos1, pos2).getSmoothPieces());
        addElements(elements, polyCurve.getSubCurve(pos1, pos2));
        do {
        	// get the position of the new portion of curve
        	pos1 = twins.remove(pos2);
        	
        	// check if there are still intersections to process
        	if(twins.higherKey(pos1)==null)
        		break;
        	
        	// get position of next intersection on the curve
        	pos2 = twins.higherKey(pos1);
        	
        	// add elements
        	addElements(elements, polyCurve.getSubCurve(pos1, pos2));
        } while(true);
        
        // add the last portion of curve, going to the end of original curve
        pos2 = polyCurve.getT1();
        addElements(elements, polyCurve.getSubCurve(pos1, pos2));
        
        // add the continuous curve formed only by circulinear elements
        result.add(createPolyCurve(elements, curve.isClosed()));
        
        // Process other curves, while there are intersections left
        while(!twins.isEmpty()) {
            // create new empty array of elements for current continuous curve
            elements = new ArrayList<CirculinearElement2D>();
            
            // get first intersection
            pos0 = twins.firstKey();
            pos1 = twins.get(pos0);
            pos2 = twins.higherKey(pos1);
            
            // add the first portion of curve, starting from the beginning
            addElements(elements, polyCurve.getSubCurve(pos1, pos2));
            
            while(pos2!=pos0) {
            	// get the position of the new portion of curve
            	pos1 = twins.remove(pos2);
            	
            	// check if there are still intersections to process
            	if(twins.higherKey(pos1)==null)
            		break;
            	
            	// get position of next intersection on the curve
            	pos2 = twins.higherKey(pos1);
            	
            	// add elements
            	addElements(elements, polyCurve.getSubCurve(pos1, pos2));
            }
            
            pos1 = twins.remove(pos2);
        	
            // create continuous curve formed only by circulinear elements
            // and add it to the set of curves
            result.add(createPolyCurve(elements, true));
        }
        
        return result;
	}
	
	/**
	 * Add all circulinear elements of the given curve to the collection of
	 * circulinear elements.
	 */
	private final static void addElements(
			Collection<CirculinearElement2D> elements,
			ContinuousCirculinearCurve2D curve) {
		elements.addAll(curve.getSmoothPieces());
	}
	
	/**
	 * This is a helper method, used to avoid excessive use of generics within
	 * other methods of the class.
	 */
	private final static PolyCirculinearCurve2D<CirculinearElement2D>
	createPolyCurve(Collection<? extends CirculinearElement2D> elements, 
			boolean closed) {
		return new PolyCirculinearCurve2D<CirculinearElement2D>(
				elements, closed);
	}
	
	/**
	 * Split a collection of contours which possibly intersect each other to a
	 * set of contours which do not intersect each others. Each contour is
	 * assumed not to self-intersect.
	 * 
	 * TODO: at the moment, process only closed (i.e. bounded) contours,
	 */
	public final static Collection<CirculinearContour2D> 
	splitIntersectingContours(
			CirculinearContour2D curve1, 
			CirculinearContour2D curve2) {
		
		double pos0, pos1, pos2;
		
		// ----------------
		// Initializations
		
		// create the array of resulting curves
        ArrayList<CirculinearContour2D> contours =
        	new ArrayList<CirculinearContour2D>();

		// identify couples of intersections
		double[][] couples = locateIntersections(curve1, curve2);
		
		// case no intersection between the curves
		if(couples.length==0){			
	        // create continuous curve formed only by circulinear elements
			contours.add(curve1);
			contours.add(curve2);
	        return contours;
		}
		
		// stores couple of points in 'twins'
		TreeMap<Double, Double> twins1 = new TreeMap<Double, Double>();
		TreeMap<Double, Double> twins2 = new TreeMap<Double, Double>();
		
		// stores also positions on each curve in an ordered tree
		TreeSet<Double> positions1 = new TreeSet<Double>();
		TreeSet<Double> positions2 = new TreeSet<Double>();
		
		// iterate on intersections to populate the data
        for (int i=0; i<couples.length; i++) {
        	pos1 = couples[i][0];
        	pos2 = couples[i][1];
        	twins1.put(pos1, pos2);
        	twins2.put(pos2, pos1);
        	positions1.add(pos1);
        	positions2.add(pos2);
        }
        
        // an array for the portions of curves
        ArrayList<CirculinearElement2D> elements;
                
        // Process other curves, while there are intersections left
        while(!twins1.isEmpty()) {
            // create new empty array of elements for current contour
            elements = new ArrayList<CirculinearElement2D>();
            
            // get first intersection
            pos0 = twins2.firstEntry().getValue();
            pos1 = pos0;
            
            do {
                pos2 = nextValue(positions1, pos1);
                
                // add a portion of the first curve
                addElements(elements, curve1.getSubCurve(pos1, pos2));
                
                // get the position of end intersection on second curve
            	pos1 = twins1.remove(pos2);
            	
            	// get position of next intersection on the second curve
            	pos2 = nextValue(positions2, pos1);
            	
            	// add a portion of the second curve
            	addElements(elements, curve2.getSubCurve(pos1, pos2));
                
                // get the position of end intersection on first curve
            	pos1 = twins2.remove(pos2);
            	
            } while (pos1!=pos0);
            
            // create continuous curve formed only by circulinear elements
            // and add it to the set of curves
            contours.add(
            		new BoundaryPolyCirculinearCurve2D<CirculinearElement2D>(
            		elements, true));
        }
        
        return contours;
	}
	
	public final static Collection<CirculinearContour2D>
	splitIntersectingContours(Collection<? extends CirculinearContour2D> curves) {
		
		double pos0=0, pos1, pos2;
		
		// ----------------
		// Initializations
		
        // convert collection to array
        CirculinearContour2D[] curveArray = 
        	curves.toArray(new CirculinearContour2D[0]);
        
		// Create array of tree maps for storing
        // 1) index of crossing curve for each intersection of i-th curve
        // 2) position on crossing curve of the intersection point
        int nCurves = curves.size();
        ArrayList<TreeMap<Double, Integer>>  twinIndices = 
        	new ArrayList<TreeMap<Double, Integer>>(nCurves);
        ArrayList<TreeMap<Double, Double>>  twinPositions = 
        	new ArrayList<TreeMap<Double, Double>>(nCurves);
        
        // Populate the two arrays with empty trees
        for(int i=0; i<nCurves; i++){
        	twinIndices.add(i, new TreeMap<Double, Integer>());
        	twinPositions.add(i, new TreeMap<Double, Double>());
        }
        
        // Create array of tree sets for storing positions of intersections
        // on each curve
        ArrayList<TreeSet<Double>>  positions = 
        	new ArrayList<TreeSet<Double>>(nCurves);
        
        // populate the array with empty tree sets
        for(int i=0; i<nCurves; i++){
        	positions.add(i, new TreeSet<Double>());
        }
       
		// identify couples of intersections on each couple (i,j) of curves
		for(int i=0; i<nCurves-1; i++) {
			CirculinearContour2D curve1 = curveArray[i];
			
			for(int j=i+1; j<nCurves; j++) {
				CirculinearContour2D curve2 = curveArray[j];
				double[][] couples = locateIntersections(curve1, curve2);
				
				// iterate on intersections to populate the data
		        for (int k=0; k<couples.length; k++) {
		        	// position on each curve
		        	pos1 = couples[k][0];
		        	pos2 = couples[k][1];
		        	
		        	// add positions in their tree sets
		        	positions.get(i).add(pos1);
		        	positions.get(j).add(pos2);
		        	
		        	// store indices of corresponding intersecting curves
		        	twinIndices.get(i).put(pos1, j);
		        	twinIndices.get(j).put(pos2, i);
		        	
		        	// store positions of intersection point on the 
		        	// corresponding curve
		        	twinPositions.get(i).put(pos1, pos2);
		        	twinPositions.get(j).put(pos2, pos1);
		        }
			}
		}		
		
		// create the array of resulting curves
        ArrayList<CirculinearContour2D> contours =
        	new ArrayList<CirculinearContour2D>();
		
		// add all curves which do not intersect other curves
        for(int i=0; i<nCurves; i++) {
        	if (twinPositions.get(i).isEmpty())
        		contours.add(curveArray[i]);
        }
       
        // an array for the portions of curves
        ArrayList<CirculinearElement2D> elements;
                
        // Process other curves, while there are intersections left
        while(!isAllEmpty(twinPositions)) {
            // create new empty array of elements for current contour
            elements = new ArrayList<CirculinearElement2D>();
            
            // indices of the two considered curves.
            int ind0=0, ind;
            
            // get first intersection
            for(int i=0;i<nCurves;i++) {
            	// find a curve with unprocessed intersections
            	if(twinPositions.get(i).isEmpty())
            		continue;
            	
            	// find first unprocessed intersection
            	pos0 = twinPositions.get(i).firstEntry().getValue();
            	ind0 = twinIndices.get(i).firstEntry().getValue();
            	break;
            }
            
            if (ind0==0) {
            	System.out.println("No more intersections, but was not detected");
            }
            
            pos1 = pos0;
            ind  = ind0;
            
            do {
                pos2 = nextValue(positions.get(ind), pos1);
                
                // add a portion of the first curve
                addElements(elements, curveArray[ind].getSubCurve(pos1, pos2));
                
                // get the position of end intersection on second curve
                pos1 = twinPositions.get(ind).remove(pos2);
                ind  = twinIndices.get(ind).remove(pos2);            	
            } while (pos1!=pos0 || ind!=ind0);
            
            // create continuous curve formed only by circulinear elements
            // and add it to the set of curves
            contours.add(
            		new BoundaryPolyCirculinearCurve2D<CirculinearElement2D>(
            		elements, true));
        }
        
        return contours;
	}
	
	private final static boolean isAllEmpty(Collection<TreeMap<Double, Double>> coll) {
		for(TreeMap<?,?> map : coll) {
			if (!map.isEmpty())
				return false;
		}
		return true;
	}
	
	/**
	 * Returns either the next value, or the first value of the tree if the
	 * given value is the last one of the tree.
	 */
	private final static double nextValue(TreeSet<Double> tree, double value) {
		if(tree.higher(value)==null)
			return tree.first();
		else
			return tree.higher(value);
	}
	
	/**
	 * Compute the buffer of a circulinear curve.<p>
	 * The algorithm is as folow:
	 * <ol>
	 * <li> split the curve into a set of curves without self-intersections
	 * <li> for each splitted curve, compute the contour of its buffer
	 * <li> split self-intersecting contours into set of disjoint contours
	 * <li> split all contour which intersect each other to disjoint contours
	 * <li> remove contours which are too close from the original curve
	 * <li> create a new domain with the final set of contours
	 * </ol>
	 */
	public final static CirculinearDomain2D computeBuffer(
			CirculinearCurve2D curve, double dist) {
		
		ArrayList<CirculinearContour2D> contours =
			new ArrayList<CirculinearContour2D>();
		
		// iterate on all continuous curves
		for(ContinuousCirculinearCurve2D cont : curve.getContinuousCurves()) {
			// split the curve into a set of non self-intersecting curves
			for(ContinuousCirculinearCurve2D contour : 
				splitContinuousCurve(cont)) {
				// compute the rings composing the simple curve buffer
				contours.addAll(computeBufferSimpleContour(contour, dist));
			}
		}
		
		// split contours which intersect each others
		contours = new ArrayList<CirculinearContour2D>(
				splitIntersectingContours(contours));		
		
		// Remove contours that cross or that are too close from base curve
		ArrayList<CirculinearContour2D> contours2 = 
			new ArrayList<CirculinearContour2D>(contours.size());
		for(CirculinearContour2D contour : contours) {
			
			// do not keep contours which cross original curve
			if(findIntersections(curve, contour).size()>0)
				continue;
			
			// check that vertices of contour are not too close from original
			// curve
			double distCurves = getDistanceCurvePoints(curve, 
					contour.getSingularPoints());
			if(distCurves<dist-Shape2D.ACCURACY)
				continue;
			
			// keep the contours that meet the above conditions
			contours2.add(contour);
		}
		
		// All the rings are created, we can now create a new domain with the
		// set of rings
		return new GenericCirculinearDomain2D(
				new CirculinearBoundarySet2D<CirculinearContour2D>(
						contours2));
	}
	
	/**
	 * Compute buffer of a point set.
	 */
	public final static CirculinearDomain2D computeBuffer(PointSet2D set, 
			double dist) {
		Collection<CirculinearContour2D> rings = 
			new ArrayList<CirculinearContour2D>(set.getPointNumber());
		for(Point2D point : set) {
			rings.add(new Circle2D(point, Math.abs(dist), dist>0));
		}
		
		rings = CirculinearCurve2DUtils.splitIntersectingContours(rings);
		
		// Remove contours that cross or that are too close from base curve
		ArrayList<CirculinearContour2D> rings2 = 
			new ArrayList<CirculinearContour2D>(rings.size());
		for(CirculinearContour2D ring : rings) {
			
			// check that vertices of contour are not too close from original
			// curve
			double minDist = getDistanceCurvePoints(ring, set.getPoints());
			if(minDist<dist-Shape2D.ACCURACY)
				continue;
			
			// keep the contours that meet the above conditions
			rings2.add(ring);
		}

		return new GenericCirculinearDomain2D(
				CirculinearBoundarySet2D.create(rings2));
	}

	/**
	 * Computes the rings that form the buffer of a continuous circulinear
	 * curve that does not self-intersect.
	 */
	public final static Collection<? extends CirculinearContour2D> 
	computeBufferSimpleContour(ContinuousCirculinearCurve2D curve, double d) {
		
		// prepare an array to store the set of rings
		ArrayList<CirculinearContour2D> rings =
			new ArrayList<CirculinearContour2D>();
		
		// the parallel in each side
		ContinuousCirculinearCurve2D parallel1, parallel2;
		parallel1 = curve.getParallel(d);
		parallel2 = curve.getParallel(-d).getReverseCurve();
		
		// split each parallel into continuous curves
		CirculinearCurveSet2D<ContinuousCirculinearCurve2D> curves =
			new CirculinearCurveSet2D<ContinuousCirculinearCurve2D>();
		
		// select only curve parts which do not cross original curve
		for(ContinuousCirculinearCurve2D split : splitContinuousCurve(parallel1)) {
			if(findIntersections(curve, split).size()==0)
				curves.addCurve(split);
		}
		for(ContinuousCirculinearCurve2D split : splitContinuousCurve(parallel2)) {
			if(findIntersections(curve, split).size()==0)
				curves.addCurve(split);
		}
		
		if(curve.isClosed()){
			// create a new ring for each parallel curve
			for(ContinuousCirculinearCurve2D split : curves) {
				rings.add(new GenericCirculinearRing2D(
						split.getSmoothPieces(), true));
			}
		} else {
			// There should be only 2 curves in the list 'curves' that are
			// not rings
			ContinuousCirculinearCurve2D curve1=null;
			ContinuousCirculinearCurve2D curve2=null;
			
			for(ContinuousCirculinearCurve2D split : curves) {
				if(split.isClosed()){
					// simply adds a new ring by using same elements
					rings.add(new GenericCirculinearRing2D(
							split.getSmoothPieces(), true));
				} else {
					if(curve1==null){
						curve1 = split;
					} else if (curve2==null) {
						curve2 = split;
					} else {
						//TODO: throw exception instead of this ugly error management
						System.err.println("more than 2 free curves....");
						return rings;
					}
				}
			}
			
			// create new ring using two open curves and two circle arcs
			if(curve1!=null && curve2!=null){
				// array of elements for creating new ring.
				ArrayList<CirculinearElement2D> elements = 
					new ArrayList<CirculinearElement2D>();
				
				// some shortcuts for computing infinity of curve
				boolean b0 = Curve2DUtils.isLeftInfinite(curve);
				boolean b1 = Curve2DUtils.isRightInfinite(curve);
				
				if (b0 && b1) {
					// case of an infinite curve at both extremities
					// In this case, the two parallel curves do not join,
					// and are added as contours individually					
					rings.add(new BoundaryPolyCirculinearCurve2D<CirculinearElement2D>(
							curve1.getSmoothPieces()));
					rings.add(new BoundaryPolyCirculinearCurve2D<CirculinearElement2D>(
							curve2.getSmoothPieces()));
				} else if (b0 && !b1) {
					// case of a curve starting from infinity, and finishing
					// on a given point
					
					// extremity points
					Point2D p11 = curve1.getFirstPoint();
					Point2D p22 = curve2.getLastPoint();

					// add elements of the new contour
					elements.addAll(curve2.getSmoothPieces());
					elements.add(createArc(
							curve.getFirstPoint(), d, p22, p11, true));
					elements.addAll(curve1.getSmoothPieces());
					
					// create the last ring
					rings.add(new GenericCirculinearRing2D(elements, true));
					
				} else if (b1 && !b0) {
					// case of a curve starting at a point and finishing at
					// the infinity
					
					// extremity points
					Point2D p12 = curve1.getLastPoint();
					Point2D p21 = curve2.getFirstPoint();
					
					// add elements of the new contour
					elements.addAll(curve1.getSmoothPieces());
					elements.add(createArc(
							curve.getFirstPoint(), d, p12, p21, true));
					elements.addAll(curve2.getSmoothPieces());
					
					// create the last contour
					rings.add(new GenericCirculinearRing2D(elements, true));
					
				} else {
					// case of a curve finite at each extremity
				
					// extremity points
					Point2D p11 = curve1.getFirstPoint();
					Point2D p12 = curve1.getLastPoint();
					Point2D p21 = curve2.getFirstPoint();
					Point2D p22 = curve2.getLastPoint();

					// Check how to associate open curves and circle arcs
					//TODO: maybe replace by a dedicated method that could manage several types of junctions ?
					if(p12.equals(parallel1.getLastPoint())){
						elements.addAll(curve1.getSmoothPieces());					
						elements.add(createArc(
								curve.getLastPoint(), d, p12, p21, true));
						elements.addAll(curve2.getSmoothPieces());
						elements.add(createArc(
								curve.getFirstPoint(), d, p22, p11, true));
					} else {
						elements.addAll(curve2.getSmoothPieces());
						elements.add(createArc(
								curve.getLastPoint(), d, p22, p11, true));
						elements.addAll(curve1.getSmoothPieces());					
						elements.add(createArc(
								curve.getFirstPoint(), d, p12, p21, true));
					}
					// create the last ring
					rings.add(new GenericCirculinearRing2D(elements, true));
				}
			}
		}
		
		// prepare an array to store the set of rings
		ArrayList<CirculinearContour2D> rings2 =
			new ArrayList<CirculinearContour2D>();

		// iterate on the set of rings
		for(CirculinearContour2D ring : rings)
			// split rings into curves which do not self-intersect
			for(ContinuousCirculinearCurve2D split : 
				splitContinuousCurve(ring)) {
				
				// compute distance to original curve
				// (assuming it is sufficient to compute distance to vertices
				// of the reference curve).
				double dist = getDistanceCurvePoints(curve, 
						split.getSingularPoints());
				
				// check if distance condition is verified
				if(dist-d<-Shape2D.ACCURACY)
					continue;
				
				// convert the set of elements to a Circulinear ring
				rings2.add(new GenericCirculinearRing2D(
						split.getSmoothPieces(), split.isClosed()));
		}
		
		// return the set of created rings
		return rings2;
	}
	
	private final static CircleArc2D createArc(
			Point2D center, double r, 
			Point2D p1, Point2D p2, boolean direct){
		double angle1 = Angle2D.getHorizontalAngle(center, p1);
		double angle2 = Angle2D.getHorizontalAngle(center, p2);
		return  new CircleArc2D(center, r, angle1, angle2, direct);
	}
	
	public final static double getDistanceCurvePoints(
			CirculinearCurve2D curve, Collection<? extends Point2D> points){
		double minDist = Double.MAX_VALUE;
		for(Point2D point : points){
			minDist = Math.min(minDist, curve.getDistance(point));
		}
		return minDist;
	}
}
