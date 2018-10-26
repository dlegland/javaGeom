/**
 * File: 	GeometricObject3D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 26 sept. 2010
 */
package math.geom3d;

/**
 * Grouping interface for all objects operating on 3 dimensions. This includes shapes, boxes, transforms, vectors...
 * 
 * @author dlegland
 *
 */
public interface IGeometricObject3D {

    /**
     * Checks if the two objects are similar up to a given threshold value. This method can be used to compare the results of geometric computations, that introduce errors due to numerical computations.
     *
     * @param obj
     *            the object to compare
     * @param eps
     *            a threshold value, for example the minimal coordinate difference
     * @return true if both object have the same value up to the threshold
     */
    boolean almostEquals(IGeometricObject3D obj, double eps);
}
