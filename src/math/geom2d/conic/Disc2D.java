/* File Disc2D.java 
 *
 * Project : EuclideJ
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

package math.geom2d.conic;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Boundary2DUtil;
import math.geom2d.domain.Domain2D;
import math.geom2d.domain.GenericDomain2D;
import math.geom2d.transform.AffineTransform2D;


/**
 * A domain of the plane whose boundary is a circle.
 * @author Legland
 */
public class Disc2D implements Domain2D {

	protected Circle2D circle = new Circle2D(0, 0, 1);


	public Disc2D(){
	}
	
	public Disc2D(Circle2D circle){
		this.circle = circle;
	}
	
	public Disc2D(Point2D p, double r){
		circle = new Circle2D(p, r);
	}
	
	public Boundary2D getBoundary() {		
		return circle;
	}

	public double getDistance(java.awt.geom.Point2D p) {
		return Math.max(circle.getSignedDistance(p.getX(), p.getY()), 0);
	}

	public double getDistance(double x, double y) {
		return Math.max(circle.getSignedDistance(x, y), 0);
	}

	/**
	 * return always true.
	 */
	public boolean isBounded() {
		return true;
	}

	public boolean isEmpty(){
		return false;
	}

	public Shape2D clip(Box2D box) {
		return new GenericDomain2D(Boundary2DUtil.clipBoundary(this.getBoundary(), box));
	}

	public Box2D getBoundingBox() {
		return circle.getBoundingBox();
	}

	public Shape2D transform(AffineTransform2D trans) {
		return null;
	}

	public boolean contains(double x, double y) {
		return circle.getSignedDistance(x, y)<=0;
	}

	public boolean contains(double x, double y, double w, double h){
		if(contains(x, y)) return true;
		if(contains(x+w, y)) return true;
		if(contains(x+w, y+h)) return true;
		if(contains(x, y+h)) return true;
		return false;
	}

	public boolean contains(java.awt.geom.Point2D p) {
		return contains(p.getX(), p.getY());
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

	public boolean contains(Rectangle2D rect) {
		return contains(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	public boolean intersects(double x, double y, double w, double h){
		return circle.intersects(x, y, w, h);
	}

	public boolean intersects(Rectangle2D rect) {
		return circle.intersects(rect);
	}

	public PathIterator getPathIterator(AffineTransform trans) {
		return circle.getPathIterator(trans);
	}

	public PathIterator getPathIterator(AffineTransform trans, double flatness) {
		return circle.getPathIterator(trans, flatness);
	}

}
