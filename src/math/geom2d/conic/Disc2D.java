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

import java.awt.Graphics2D;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Boundary2DUtils;
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
	
	public Disc2D(double x0, double y0, double r){
		circle = new Circle2D(x0, y0, r);
	}
	
	
	public Boundary2D getBoundary() {		
		return circle;
	}

	public Domain2D complement(){
		return new GenericDomain2D(circle.getReverseCurve());
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

	public Domain2D clip(Box2D box) {
		return new GenericDomain2D(Boundary2DUtils.clipBoundary(this.getBoundary(), box));
	}

	public Box2D getBoundingBox() {
		return circle.getBoundingBox();
	}

	public Domain2D transform(AffineTransform2D trans) {
		return null;
	}

	public boolean contains(double x, double y) {
		return circle.getSignedDistance(x, y)<=0;
	}

	public boolean contains(java.awt.geom.Point2D p) {
		return contains(p.getX(), p.getY());
	}
	
	public void draw(Graphics2D g2){
		circle.draw(g2);
	}

	public void fill(Graphics2D g2){
		circle.fill(g2);
	}
}
