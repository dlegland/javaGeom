/**
 * File: 	CircularShape2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 17 mai 09
 */
package net.javageom.geom2d.conic;

import net.javageom.geom2d.Box2D;
import net.javageom.geom2d.Point2D;
import net.javageom.geom2d.circulinear.CirculinearElement2D;
import net.javageom.geom2d.curve.CurveSet2D;
import net.javageom.geom2d.domain.SmoothOrientedCurve2D;


/**
 * Tagging interface for grouping Circle2D and CircleArc2D.
 * @author dlegland
 *
 */
public interface CircularShape2D 
extends CirculinearElement2D, SmoothOrientedCurve2D {

	
    // ===================================================================
    // method specific to CircularShape2D

	/**
	 * Returns the circle that contains this shape.
	 */
	public Circle2D supportingCircle();

	/**
	 * Returns true if the orthogonal projection of the point <code>p</code> on
	 * the supporting circle of this curve belongs to this curve.
	 * 
	 * @param p a point in the plane
	 * @return true if the projection of p on the supporting circle also belongs to this curve
	 */
    public boolean containsProjection(Point2D p);
    
    // ===================================================================
    // methods inherited from Shape2D and Curve2D

	public CurveSet2D<? extends CircularShape2D> clip(Box2D box);
	public CircularShape2D subCurve(double t0, double t1);
	public CircularShape2D reverse();
}
