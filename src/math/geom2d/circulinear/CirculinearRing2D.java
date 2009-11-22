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
 * Interface for circulinear boundary curves which are both bounded
 * and closed.
 * @author dlegland
 *
 */
public interface CirculinearRing2D extends CirculinearContour2D {

	public CirculinearRing2D getParallel(double d);
	public CirculinearRing2D getReverseCurve();
}
