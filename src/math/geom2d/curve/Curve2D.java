/* File Curve2D.java 
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
 */

// package
package math.geom2d.curve;

// Imports
import math.geom2d.Shape2D;
import math.geom2d.Point2D;
import math.geom2d.Box2D;
import math.geom2d.transform.AffineTransform2D;
import math.geom2d.line.StraightObject2D;

import java.util.*;

/**
 * Interface for all linear and pieces smooth curves : polylines, conics, 
 * lines ... A Curve2D object can be a continuous object (line, spiral, 
 * conic, ...), or the boundary of a Region. It can also be the union
 * of several continuous curves.<p>
 * Paramaterized curves are actually considered. Parameterization is left
 * to the implementation.
 */
public interface Curve2D extends Shape2D{

	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables
	
	
	// ===================================================================
	// constructors
	

	// ===================================================================
	// accessors

	/** 
	 * Get value of parameter t for the first point of the curve. 
	 * It can be -Infinity, in this case the piece of curve is not bounded.
	 */
	public abstract double getT0();
	
	/** 
	 * Get value of parameter t for the last point of the curve. It can be
	 * +Infinity, in this case the piece of curve is not bounded.
	 */
	public abstract double getT1();
	

	/** 
	 * Gets the point from a parametric representation of the curve. If the
	 * parameter lies outside the definition range, the parameter
	 * corresponding to the closest bound is used instead.
	 *  
	 * This method can be used to draw an approximated outline of a curve,
	 * by selecting multiple values for t and drawing lines 
	 * between them.
	 */
	public abstract Point2D getPoint(double t);
	
	/** 
	 * Same as getPoint(t), but gives the point as a parameter. This avoids 
	 * repetitive memory allocations.
	 */
	public abstract Point2D getPoint(double t, Point2D point);
	
	/**
	 * Get the first point of the curve. It must returns the same result as 
	 * <code>getPoint(getT0())</code>.
	 * @return the first point of the curve
	 */
	public abstract Point2D getFirstPoint();
	
	/**
	 * Get the last point of the curve. It must returns the same result as 
	 * <code>getPoint(getT1())</code>.
	 * @return the last point of the curve.
	 */
	public abstract Point2D getLastPoint();
	
	/**
	 * Get position of the point on the curve. If the point does not belong to
	 * the curve, return Double.NaN.
	 * @param point a point belonging to the curve
	 * @return the position of the point on the curve
	 */
	public abstract double getPosition(Point2D point);
	
	/**
	 * Returns the position of the closest orthogonal projection of the point
	 * on the curve, or of the closest singular point. This function should
	 * always returns a valid value. 
	 * @param point a point to project
	 * @return the position of the closest orthogonal projection
	 */
	public abstract double project(Point2D point);
	
		
	/**
	 * Returns the intersection points of the curve with the specified line.
	 * The length of the result array is the number of intersection points.
	 */
	public abstract Collection<Point2D> getIntersections(StraightObject2D line);

	/**
	 * Return the path for tracing the curve, when cursor is already located
	 * at the beginning of the curve. Using this method allows to concatenate
	 * curves and to draw polycurves.
	 * @return the path for tracing the curve.
	 */
	//public abstract java.awt.geom.GeneralPath getInnerPath();
	
	/**
	 * Returns the curve with same trace on the plane with parametrization
	 * in reverse order.
	 */
	public abstract Curve2D getReverseCurve();
	
	/**
	 * Returns the collection of continuous curves which constitute this curve.
	 * @return a collection of continuous curves.
	 */
	public abstract Collection<? extends ContinuousCurve2D> getContinuousCurves();
	
	/**
	 * Returns a portion of the original curve, delimited by two positions on
	 * the curve.
	 * @param t0 position of the start of the sub-curve
	 * @param t1 position of the end of the sub-curve
	 * @return the portion of original curve comprised between t0 and t1. 
	 */
	public abstract Curve2D getSubCurve(double t0, double t1);

	/**
	 * Transforms the curve by an affine transform. The result is an instance
	 * of Curve2D.
	 */
	public abstract Curve2D transform(AffineTransform2D trans);
	
	/**
	 * When a curve is clipped, the result is a set of curves.
	 */
	public abstract CurveSet2D<? extends Curve2D> clip(Box2D box);
}