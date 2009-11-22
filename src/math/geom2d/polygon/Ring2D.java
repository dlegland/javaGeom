/* file : Ring2D.java
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
 * Created on 16 avr. 2007
 *
 */

package math.geom2d.polygon;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Point2D;

/**
 * <p>
 * This class is replaced by LinearRing2D class. It will become an interface 
 * in a future release.
 * </p>
 * @deprecated replaced by LinearRing2D (0.8.0)
 * @author dlegland
 */
@Deprecated
public class Ring2D extends LinearRing2D {
	/**
	 * @deprecated Use LinearRing2D instead  (0.8.0)
	 */
	@Deprecated
    public Ring2D() {
        super();
    }

	/**
	 * @deprecated Ring2D will be changed to an interface in a future release.
	 * 		Use LinearRing2D instead  (0.8.0)
	 */
	@Deprecated
     public Ring2D(Point2D initialPoint) {
        super(initialPoint);
    }

	/**
	 * @deprecated Ring2D will be changed to an interface in a future release.
	 * 		Use LinearRing2D instead  (0.8.0)
	 */
	@Deprecated
     public Ring2D(Point2D[] points) {
        super(points);
    }

	/**
	 * @deprecated Ring2D will be changed to an interface in a future release.
	 * 		Use LinearRing2D instead  (0.8.0)
	 */
	@Deprecated
     public Ring2D(double[] xcoords, double[] ycoords) {
        super(xcoords, ycoords);
    }

	/**
	 * @deprecated Ring2D will be changed to an interface in a future release.
	 * 		Use LinearRing2D instead  (0.8.0)
	 */
	@Deprecated
     public Ring2D(Collection<? extends Point2D> points) {
        super(points);
    }
    
    @Override
    public Ring2D clone() {
        ArrayList<Point2D> array = new ArrayList<Point2D>(points.size());
        for(Point2D point : points)
            array.add(point.clone());
        return new Ring2D(array);
    }
}
