/**
 * File: 	CirculinearElement2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 mai 09
 */
package math.geom2d.circulinear;

import math.geom2d.Box2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.domain.SmoothOrientedCurve2D;
import math.geom2d.transform.CircleInversion2D;


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
}
