/* file : Point3D.java
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

import math.geom3d.transform.AffineTransform3D;

/**
 * A 3-dimensional point.
 * 
 * @author dlegland
 */
public class Point3D implements Shape3D {

	// ===================================================================
	// Class variables
	
    private double x = 0;
    private double y = 0;
    private double z = 0;

	// ===================================================================
	// Constructors

    /**
     * Initialize at coordinate (0,0,0).
     */
    public Point3D() {
        this(0, 0, 0);
    }

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

	// ===================================================================
	// Methods specific to Point3D
	
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

	public Point3D plus(Vector3D vec) {
		return new Point3D(this.x + vec.x, this.y + vec.y, this.z + vec.z);
	}
	
	public Point3D plus(Point3D p2) {
		return new Point3D(this.x + p2.x, this.y + p2.y, this.z + p2.z);
	}
	
	public Point3D minus(Vector3D vec) {
		return new Point3D(this.x - vec.x, this.y - vec.y, this.z - vec.z);
	}
	
	public Point3D minus(Point3D p2) {
		return new Point3D(this.x - p2.x, this.y - p2.y, this.z - p2.z);
	}
	
	// ===================================================================
	// Methods implementing the Shape3D interface
	
	public double distance(Point3D point) {
		double dx = point.x - x;
		double dy = point.y - y;
		double dz = point.z - z;

		return Math.hypot(Math.hypot(dx, dy), dz);
	}

    /**
     * A point 'contains' another point if their euclidean distance is less than
     * the accuracy.
     */
    public boolean contains(Point3D point) {
        if (distance(point) > ACCURACY)
            return false;
        return true;
    }

    /**
     * Returns false, as a point is never empty.
     */
    public boolean isEmpty() {
        return false;
    }

    /**
     * Returns true, as a point is always bounded.
     */
    public boolean isBounded() {
        return true;
    }

    public Box3D boundingBox() {
        return new Box3D(x, x, y, y, z, z);
    }

    /**
     * Returns the clipped point, or null if empty.
     */
    public PointSet3D clip(Box3D box) {
    	PointSet3D set = new PointSet3D(1);
        if (x < box.getMinX() || x > box.getMaxX())
            return set;
        if (y < box.getMinY() || y > box.getMaxY())
            return set;
        if (z < box.getMinZ() || z > box.getMaxZ())
            return set;
        
        set.addPoint(this);
        return set;
    }

    /**
     * Applies the given affine transform to the point, and return the 
     * transformed point.
     */
    public Point3D transform(AffineTransform3D trans) {
		double coef[] = trans.coefficients();
		return new Point3D(
				x * coef[0] + y * coef[1] + z * coef[2] + coef[3], 
				x * coef[4] + y * coef[5] + z * coef[6] + coef[7],
				x * coef[8] + y * coef[9] + z * coef[10] + coef[11]);
        

    }

    // ===================================================================
    // methods overriding Object superclass

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point3D))
            return false;
        Point3D point = (Point3D) obj;

        if (Math.abs(point.x-this.x)>Shape3D.ACCURACY)
            return false;
        if (Math.abs(point.y-this.y)>Shape3D.ACCURACY)
            return false;
        if (Math.abs(point.z-this.z)>Shape3D.ACCURACY)
            return false;
        return true;
    }
}
