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

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

import math.geom2d.Box2D;
import math.geom2d.Shape2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.transform.AffineTransform2D;


/**
 * A domain defined from its boundary. The boundary curve must be correctly
 * oriented, non self intersecting, and clearly separating interior and
 * exterior.<p>
 * All contains and intersect tests are computed from the signed distance of
 * the boundary curve.
 * @author Legland
 */
public class GenericDomain2D implements Domain2D {

	protected Boundary2D boundary = null;


	public GenericDomain2D(Boundary2D boundary){
		this.boundary = boundary;
	}
	

	public Boundary2D getBoundary() {		
		return boundary;
	}

	public double getDistance(java.awt.geom.Point2D p) {
		return Math.max(boundary.getSignedDistance(p.getX(), p.getY()), 0);
	}

	public double getDistance(double x, double y) {
		return Math.max(boundary.getSignedDistance(x, y), 0);
	}

	/**
	 * return always true.
	 */
	public boolean isBounded() {
		return true;
	}

	public boolean isEmpty(){
		return boundary.isEmpty()&&!this.contains(0,0);
	}

	public Shape2D clip(Box2D box) {
		return new GenericDomain2D(Boundary2DUtils.clipBoundary(this.getBoundary(), box));
	}

	public Box2D getBoundingBox() {
		//TODO: manage infinite domains
		return boundary.getBoundingBox();
	}

	/**
	 * return a new domain which is created from the transformed domain of 
	 * this boundary.
	 */
	public GenericDomain2D transform(AffineTransform2D trans) {
		return new GenericDomain2D((Boundary2D) boundary.transform(trans));
	}

	public boolean contains(double x, double y) {
		return boundary.getSignedDistance(x, y)<=0;
	}

	public boolean contains(double x, double y, double w, double h){
		if(contains(x, y)) return true;
		if(contains(x+w, y)) return true;
		if(contains(x+w, y+h)) return true;
		if(contains(x, y+h)) return true;
		return false;
	}

	public boolean intersects(double x, double y, double w, double h){
		return boundary.intersects(x, y, w, h);
	}

	/**
	 * Return bounding box of the shape.
	 */
	public java.awt.Rectangle getBounds(){
		return this.getBoundingBox().getAsAWTRectangle();
	}
	
	/**
	 * Return more precise bounds for the shape.
	 */
	public java.awt.geom.Rectangle2D getBounds2D(){
		return this.getBoundingBox().getAsAWTRectangle2D();
	}

	public boolean contains(java.awt.geom.Point2D p) {
		return contains(p.getX(), p.getY());
	}

	public boolean contains(Rectangle2D rect) {
		return contains(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	public boolean intersects(Rectangle2D rect) {
		return false;
	}

	public PathIterator getPathIterator(AffineTransform trans) {
		return boundary.getPathIterator(trans);
	}

	public PathIterator getPathIterator(AffineTransform trans, double flatness) {
		return boundary.getPathIterator(trans, flatness);
	}

}
