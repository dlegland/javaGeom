/**
 * File: 	CirculinearBoundary2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 mai 09
 */
package net.javageom.geom2d.circulinear;

import java.util.Collection;

import net.javageom.geom2d.Box2D;
import net.javageom.geom2d.curve.CurveSet2D;
import net.javageom.geom2d.domain.Boundary2D;
import net.javageom.geom2d.transform.CircleInversion2D;


/**
 * A Boundary which is composed of Circulinear elements.
 * @author dlegland
 *
 */
public interface CirculinearBoundary2D extends CirculinearCurve2D, Boundary2D {
	
    // ===================================================================
    // redefines declaration of some interfaces

	public CirculinearDomain2D domain();
	public CirculinearBoundary2D parallel(double d);
    public Collection<? extends CirculinearContour2D> continuousCurves();
	public CurveSet2D<? extends CirculinearContinuousCurve2D> clip(Box2D box);
    public CirculinearBoundary2D transform(CircleInversion2D inv);
	public CirculinearBoundary2D reverse();
}
