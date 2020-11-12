/**
 * File: 	CirculinearElement2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 mai 09
 */
package net.javageom.geom2d.circulinear;

import net.javageom.geom2d.Box2D;
import net.javageom.geom2d.Point2D;
import net.javageom.geom2d.curve.CurveSet2D;
import net.javageom.geom2d.domain.SmoothOrientedCurve2D;
import net.javageom.geom2d.transform.CircleInversion2D;


/**
 * <p>
 * Circulinear elements are lowest level of circulinear curve: each
 * circulinear curve can be divided into a set of circulinear elements.</p>
 * <p>
 * Circulinear elements can be either linear elements (implementations of 
 * LinearShape2D), or circular elements (circle or circle arcs).</p>
 * 
 * @author dlegland
 *
 */
public interface CirculinearElement2D extends CirculinearContinuousCurve2D,
		SmoothOrientedCurve2D {

	public CirculinearElement2D parallel(double d);
	public CirculinearElement2D transform(CircleInversion2D inv);
	
	public CurveSet2D<? extends CirculinearElement2D> clip(Box2D box);
    public CirculinearElement2D subCurve(double t0, double t1);
	public CirculinearElement2D reverse();

	/**
	 * Returns true if the orthogonal projection of the point <code>p</code> on
	 * the supporting shape of this curve (either e straight line or a circle) also belongs to this curve.
	 * 
	 * @param p a point in the plane
	 * @return true if the projection of p on the supporting curve also belongs to this curve
	 */
    public boolean containsProjection(Point2D p);
}
