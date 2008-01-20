/* file : Label2D.java
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
 * Created on 16 avr. 2006
 *
 */
package math.geom2d;

/**
 * A label is a text located at a given position. 
 * @author dlegland
 */
public class Label2D implements Shape2D{
//TODO: think about validity of this shape (-> move to Euclide ?)
	protected Point2D position;
	protected String label;
	
	public Label2D(Point2D pos){
		this.position = pos;
		this.label = "Label";
	}

	public Label2D(Point2D pos, String text){
		this.position = pos;
		this.label = text;
	}
	
	public String getText(){
		return label;
	}
	
	public void setText(String text){
		label = text;
	}	
	
	public Point2D getPosition(){
		return position;
	}

	public void setPosition(Point2D point){
		position = point;
	}
	
	// ===================================================================
	// general methods

	public boolean contains(double x, double y){
		return false;
	}

	public boolean contains(double x, double y, double w, double h){
		return false;
	}

	public boolean contains(java.awt.geom.Point2D p){
		return false;
	}

	public boolean contains(java.awt.geom.Rectangle2D r){
		return false;
	}
	
	/**
	 * return a bounding box containing the point, with unit width and height.
	 */
	public java.awt.Rectangle getBounds(){
		return position.getBounds();
	}
	
	/**
	 * return a bounding box centered on the point, with unit width and height.
	 */
	public java.awt.geom.Rectangle2D getBounds2D(){
		return position.getBounds2D();
	}
	
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform t){
		return null;
	}

	/** 
	 * return a circle enclosing the point, which radius is the specified flatness.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform t, 
		double flatness){
		return null;
	}

	/**
	 * Tests if the Point2D intersects the interior of a specified rectangular area.
	 */
	public boolean intersects(double x, double y, double w, double h){
		return position.intersects(x, y, w, h);
	}

	/**
	 * Tests if the Point2D intersects the interior of a specified rectangle2D.
	 */
	public boolean intersects(java.awt.geom.Rectangle2D r){
		return(position.intersects(r));
	}
	
//	public Shape2D getTransformedShape(AffineTransform2D trans){
//		Point2D pos = (Point2D) position.getTransformedShape(trans);
//		return new Label2D(pos, label);
//	}
	
	
	// ===================================================================
	// accessors

	/**
	 * get the distance of the shape to the given point, or the distance of point
	 * to the frontier of the shape in the case of a plain shape.
	 */
	public double getDistance(java.awt.geom.Point2D p){
		return position.getDistance(p);
	}

	/**
	 * get the distance of the shape to the given point, specified by x and y, or 
	 * the distance of point to the frontier of the shape in the case of a plain 
	 * (i.e. fillable) shape.
	 */
	public double getDistance(double x, double y){
		return position.getDistance(x, y);
	}

	/** 
	 * Returns true if the shape is bounded, that is if we can draw a finite rectangle
	 * enclosing the shape. For example, a straight line or a parabola are not bounded.
	 */
	public boolean isBounded(){
		return true;
	}
	

	// ===================================================================
	// modifiers


	// ===================================================================
	// general methods

	public Shape2D getClippedShape(Box2D box){
		return position.getClippedShape(box);
	}
	
	public Shape2D clip(Box2D box){
		return position.getClippedShape(box);
	}
	
	/**
	 * return a bounding box centered on the point, with unit width and height.
	 */
	public Box2D getBoundingBox(){
		return new Box2D(this.getBounds2D());
	}
	
	public Label2D transform(AffineTransform2D trans){
		return new Label2D((Point2D)position.transform(trans), new String(label));
	}
}
