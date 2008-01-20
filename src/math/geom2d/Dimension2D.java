/*
 * File : Dimension2D.java
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
 * Created on 29 déc. 2003
 */
package math.geom2d;

/**
 * An utilitary class used for describing size of bounding boxes for shapes,
 * or size of shapes.
 * @author Legland
 */
public class Dimension2D extends java.awt.geom.Dimension2D {
//TODO: use same dimension as Java or declare specific type ? I prefere length+width... 
	public double width;
	public double height;
	
	/**
	 * initialize null dimension (width and height = 0).
	 */
	public Dimension2D() {
		super();
		width=0;
		height=0;
	}

	public Dimension2D(double w, double h) {
		width=w;
		height=h;
	}

	public Dimension2D(java.awt.geom.Dimension2D dim){
		super();
		width = dim.getWidth();
		height = dim.getHeight();
	}
	
	/**
	 * @see java.awt.geom.Dimension2D#getHeight()
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * @see java.awt.geom.Dimension2D#getWidth()
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Return  the dimension as a java.awt.Dimension object. The returned dimension
	 * is the smallest Dimension that contains the Dimension2D object.
	 * This methid is provided for compatibility with java.awt.Component and 
	 * java.awt.Dimension.
	 * @see java.awt.Component#getSize()
	 * @see java.awt.Dimension#getSize()
	 * @return a new instance of java.awt.Dimension, with width and height ceiled
	 * (Math.ceil) with those of original.
	 *
	 */
	public java.awt.Dimension getSize(){
		return new java.awt.Dimension((int)Math.ceil(width), (int)Math.ceil(height));
	}
	
	
	/**
	 * @see java.awt.geom.Dimension2D#setSize(double, double)
	 */
	public void setSize(double w, double h) {
		width = w;
		height = h;
	}
	
	/**
	 * @see java.awt.geom.Dimension2D#setSize(java.awt.geom.Dimension2D)
	 */
	public void setSize(java.awt.geom.Dimension2D dim) {
		width = dim.getWidth();
		height = dim.getHeight();
	}

}
