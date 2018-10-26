/**
 * File: 	CircleLine2D.java
 * Project: javaGeom-circulinear
 * 
 * Distributed under the LGPL License.
 *
 * Created: 5 juil. 09
 */
package math.geom2d.circulinear;

import math.geom2d.domain.ISmoothContour2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * Tagging interface to be able to consider in a same way circles and lines.
 * 
 * @author dlegland
 *
 */
public interface ICircleLine2D extends ICirculinearContour2D, ICirculinearElement2D, ISmoothContour2D {

    // ===================================================================
    // redefines declaration of some interfaces

    @Override
    public ICircleLine2D parallel(double dist);

    @Override
    public ICircleLine2D transform(CircleInversion2D inv);

    @Override
    public ICircleLine2D reverse();
}
