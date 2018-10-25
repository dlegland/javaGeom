/**
 * File: 	CirculinearBoundary2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 mai 09
 */
package math.geom2d.circulinear;

import java.util.Collection;

import math.geom2d.Box2D;
import math.geom2d.curve.ICurveSet2D;
import math.geom2d.domain.IBoundary2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * A Boundary which is composed of Circulinear elements.
 * 
 * @author dlegland
 *
 */
public interface ICirculinearBoundary2D extends ICirculinearCurve2D, IBoundary2D {

    // ===================================================================
    // redefines declaration of some interfaces

    public ICirculinearDomain2D domain();

    public ICirculinearBoundary2D parallel(double d);

    public Collection<? extends ICirculinearContour2D> continuousCurves();

    public ICurveSet2D<? extends ICirculinearContinuousCurve2D> clip(Box2D box);

    public ICirculinearBoundary2D transform(CircleInversion2D inv);

    public ICirculinearBoundary2D reverse();
}
