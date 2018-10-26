/* File Point2S.java 
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
 * author : Legland
 * Created on 31 mai 2004
 */

package math.geom2s;

import java.io.Serializable;

/**
 * A point in spherical coordinates.
 * 
 * @author Legland
 */
public class Point2S implements IShape2S, Serializable {
    private static final long serialVersionUID = 1L;

    double phi;
    double theta;

    public Point2S(double phi, double theta) {
        this.phi = phi;
        this.theta = theta;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(phi);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(theta);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point2S other = (Point2S) obj;
        if (Double.doubleToLongBits(phi) != Double.doubleToLongBits(other.phi))
            return false;
        if (Double.doubleToLongBits(theta) != Double.doubleToLongBits(other.theta))
            return false;
        return true;
    }
}
