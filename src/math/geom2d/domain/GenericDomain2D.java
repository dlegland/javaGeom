/* File Domain2D.java 
 *
 * Project : Java Geometry Library
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
 * Created on 18 sept. 2004
 */

package math.geom2d.domain;

import java.awt.Graphics2D;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.GeometricObject2D;
import math.geom2d.Point2D;

/**
 * A domain defined from its boundary. The boundary curve must be correctly
 * oriented, non self intersecting, and clearly separating interior and
 * exterior.
 * <p>
 * All contains and intersect tests are computed from the signed distance of the
 * boundary curve.
 * 
 * @author Legland
 */
public class GenericDomain2D implements Domain2D {

    // ===================================================================
    // Static factories
	
	public static GenericDomain2D create(Boundary2D boundary) {
		return new GenericDomain2D(boundary);
	}
	
	
    // ===================================================================
    // Class variables

	protected Boundary2D boundary = null;

    // ===================================================================
    // Constructors

    public GenericDomain2D(Boundary2D boundary) {
        this.boundary = boundary;
    }

    // ===================================================================
    // methods implementing the Domain2D interface

    public Boundary2D getBoundary() {
        return boundary;
    }

    public Domain2D complement() {
        return new GenericDomain2D(boundary.getReverseCurve());
    }

    // ===================================================================
    // methods implementing the Shape2D interface

    public double getDistance(java.awt.geom.Point2D p) {
        return Math.max(boundary.getSignedDistance(p.getX(), p.getY()), 0);
    }

    public double getDistance(double x, double y) {
        return Math.max(boundary.getSignedDistance(x, y), 0);
    }

    /**
     * Returns true if the domain is bounded. The domain is unbounded if either
     * its boundary is unbounded, or a point located outside of the boundary
     * bounding box is located inside of the domain.
     */
    public boolean isBounded() {
        // If boundary is not bounded, the domain is not bounded.
        if (!boundary.isBounded())
            return false;

        // If boundary is bounded, get the bounding box, choose a point
        // outside of the box, and check if its belongs to the domain.
        Box2D box = boundary.getBoundingBox();
        Point2D point = new Point2D(box.getMinX(), box.getMinY());

        return !boundary.isInside(point);
    }

    public boolean isEmpty() {
        return boundary.isEmpty()&&!this.contains(0, 0);
    }

    public Domain2D clip(Box2D box) {
        return new GenericDomain2D(
        		Boundary2DUtils.clipBoundary(this.getBoundary(), box));
    }

    /**
     * If the domain is bounded, returns the bounding box of its boundary,
     * otherwise returns an infinite bounding box.
     */
    public Box2D getBoundingBox() {
        if (this.isBounded())
            return boundary.getBoundingBox();
        return new Box2D(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * Returns a new domain which is created from the transformed domain of this
     * boundary.
     */
    public GenericDomain2D transform(AffineTransform2D trans) {
        Boundary2D transformed = boundary.transform(trans);
        if (!trans.isDirect())
            transformed = transformed.getReverseCurve();
        return new GenericDomain2D(transformed);
    }

    public boolean contains(double x, double y) {
        return boundary.getSignedDistance(x, y)<=0;
    }

    // ===================================================================
    // methods implementing the Shape interface

    public boolean contains(java.awt.geom.Point2D p) {
        return contains(p.getX(), p.getY());
    }

    public void draw(Graphics2D g2) {
        boundary.draw(g2);
    }

    public void fill(Graphics2D g2) {
        boundary.fill(g2);
    }
    

	// ===================================================================
	// methods implementing the GeometricObject2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
	 */
    public boolean almostEquals(GeometricObject2D obj, double eps) {
    	if (this==obj)
    		return true;
    	
        if(!(obj instanceof GenericDomain2D))
            return false;
        GenericDomain2D domain = (GenericDomain2D) obj;
        
        if(!boundary.almostEquals(domain.boundary, eps)) return false;
        return true;
    }


	// ===================================================================
	// methods overriding the Object class

    @Override
    public String toString() {
    	return "GenericDomain2D(boundary=" + boundary + ")";
    }
    
	/* (non-Javadoc)
	 * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
	 */
    @Override
    public boolean equals(Object obj) {
    	if (this==obj)
    		return true;
    	
        if(!(obj instanceof GenericDomain2D))
            return false;
        GenericDomain2D domain = (GenericDomain2D) obj;
        
        if(!boundary.equals(domain.boundary)) return false;
        return true;
    }
}
