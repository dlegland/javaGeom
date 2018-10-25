/**
 * File: 	CirculinearCurveSet2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 mai 09
 */
package math.geom2d.circulinear;

import math.geom2d.Box2D;
import math.geom2d.curve.ICurveSet2D;

/**
 * A specialization of CurveSet2D that accepts only instances of CirculinearCurve2D.
 * 
 * @author dlegland
 *
 */
public interface ICirculinearCurveSet2D<T extends ICirculinearCurve2D> extends ICurveSet2D<T>, ICirculinearCurve2D {

    // ===================================================================
    // methods implementing the CirculinearCurve2D interface

    public ICirculinearCurveSet2D<? extends ICirculinearCurve2D> clip(Box2D box);

    public ICirculinearCurveSet2D<? extends ICirculinearCurve2D> subCurve(double t0, double t1);

    public ICirculinearCurveSet2D<? extends ICirculinearCurve2D> reverse();
}
