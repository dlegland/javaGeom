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
 * @author dlegland
 * @see GenericCirculinearRing2D
 */
public interface CirculinearRing2D extends CirculinearContour2D {

	public CirculinearDomain2D domain();
	public CirculinearRing2D parallel(double d);
	public CirculinearRing2D reverse();
}
