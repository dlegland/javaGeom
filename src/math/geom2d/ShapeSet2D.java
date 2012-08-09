/**
 * File: 	ShapeSet2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 17 août 10
 */
package math.geom2d;


/**
 * A shape that is composed of several other shapes.
 * @author dlegland
 *
 */
public interface ShapeSet2D<T extends Shape2D> extends Shape2D, Iterable<T> {

	/**
	 * Appends the specified shape to the end of this set (optional operation).
	 */
	public boolean add(T shape);
	
	/**
	 * Inserts the specified shape at the specified position in this set 
	 * (optional operation).
	 */
	public void add(int index, T shape);
	
	/**
	 * Returns the shape at a given position.
	 * @param index the position of the shape
	 * @return the shape at the given position
	 */
	public T get(int index);
	
	/**
	 * Removes the first occurrence of the specified element from this list, 
	 * if it is present. If the list does not contain the element, it is
	 * unchanged. 
	 * Returns true if this list contained the specified element 
	 * (or equivalently, if this list changed as a result of the call). 
	 */
	public boolean remove(T shape);
	
	/**
	 * Removes the shape at the specified position in this set (optional
	 * operation).
	 */
	public T remove(int index);
	
	/** 
	 * Returns true if this set contains the shape.
	 */
	public boolean contains(T shape);

	/**
	 * Returns the index of the shape in this set. 
	 */
	public int indexOf(T shape);
	
	/**
	 * Returns the number of shapes stored in this set.
	 */
	public int size();

	/**
	 * Removes all the shapes stored in this set.
	 */
	public void clear();
}
