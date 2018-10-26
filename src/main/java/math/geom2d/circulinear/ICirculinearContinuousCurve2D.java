/**
 * File: 	CirculinearContinuousCurve2D.java
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
import math.geom2d.domain.IContinuousOrientedCurve2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * A tagging interface defining a circulinear curve which is continuous.
 * 
 * @author dlegland
 *
 */
public interface ICirculinearContinuousCurve2D extends ICirculinearCurve2D, IContinuousOrientedCurve2D {

    // ===================================================================
    // redefines declaration of CirculinearCurve2D interfaces

    @Override
    public ICirculinearContinuousCurve2D parallel(double d);

    @Override
    public ICirculinearContinuousCurve2D transform(CircleInversion2D inv);

    // ===================================================================
    // redefines declaration of some parent interfaces

    /**
     * Returns a set of circulinear elements, which are basis for circulinear curves.
     */
    @Override
    public abstract Collection<? extends ICirculinearElement2D> smoothPieces();

    @Override
    public ICurveSet2D<? extends ICirculinearContinuousCurve2D> clip(Box2D box);

    @Override
    public ICirculinearContinuousCurve2D subCurve(double t0, double t1);

    @Override
    public ICirculinearContinuousCurve2D reverse();
}
