/* file : Shape3D.java
 * 
 * Project : geometry
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 * 
 * Created on 27 nov. 2005
 *
 */

package math.geom3d;

import math.geom3d.point.Point3D;
import math.geom3d.transform.AffineTransform3D;

/**
 * General interface for shapes in 3 dimensions.
 * 
 * @author dlegland
 */
public interface IShape3D extends IGeometricObject3D {

    public final static double ACCURACY = 1e-12;

    boolean isEmpty();

    /**
     * Returns true if the shape is bounded, that is if we can draw a finite rectangle enclosing the shape. For example, a straight line or a parabola are not bounded.
     */
    boolean isBounded();

    Box3D boundingBox();

    IShape3D clip(Box3D box);

    IShape3D transform(AffineTransform3D trans);

    /**
     * Gets the distance of the shape to the given point, or the distance of point to the frontier of the shape in the case of a plain shape.
     */
    double distance(Point3D p);

    boolean contains(Point3D point);
}
