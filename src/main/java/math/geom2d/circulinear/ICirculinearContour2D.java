/**
 * File: 	ContinuousCirculinearBoundary2D.java
 * Project: javaGeom-circulinear
 * 
 * Distributed under the LGPL License.
 *
 * Created: 5 juil. 09
 */
package math.geom2d.circulinear;

import math.geom2d.domain.IContour2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * Tagging interface to gather Continuous and boundary circulinear curves.
 * 
 * @author dlegland
 *
 */
public interface ICirculinearContour2D extends IContour2D, ICirculinearContinuousCurve2D, ICirculinearBoundary2D {

    @Override
    public ICirculinearContour2D parallel(double d);

    @Override
    public ICirculinearContour2D transform(CircleInversion2D inv);

    @Override
    public ICirculinearContour2D reverse();
}
