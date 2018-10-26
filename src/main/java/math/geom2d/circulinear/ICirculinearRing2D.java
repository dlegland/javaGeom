/**
 * File: 	CirculinearRing2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 mai 09
 */
package math.geom2d.circulinear;

/**
 * Interface for circulinear contours which are both bounded and closed.
 * 
 * @author dlegland
 * @see GenericCirculinearRing2D
 */
public interface ICirculinearRing2D extends ICirculinearContour2D {
    @Override
    ICirculinearDomain2D domain();

    @Override
    ICirculinearRing2D parallel(double d);

    @Override
    ICirculinearRing2D reverse();
}
