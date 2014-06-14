/**
 * File: 	ShapeArray2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 17 août 10
 */
package math.geom2d;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * Default Implementation of ShapeSet2D.
 * @author dlegland
 *
 */
public class ShapeArray2D<T extends Shape2D> 
implements ShapeSet2D<T>, Cloneable {

    // ===================================================================
    // Static constructors

	public static <T extends Shape2D> ShapeArray2D<T> create(Collection<T> shapes) {
		return new ShapeArray2D<T>(shapes);
	}

	public static <T extends Shape2D> ShapeArray2D<T> create(T... shapes) {
		return new ShapeArray2D<T>(shapes);
	}
	
	
    // ===================================================================
    // Class variables

    /** The inner array of curves */
    protected ArrayList<T> shapes;

    
    // ===================================================================
    // Constructors

    public ShapeArray2D() {
    	this.shapes = new ArrayList<T>();
    }
    
    public ShapeArray2D(int n) {
    	this.shapes = new ArrayList<T>(n);
    }
    
    public ShapeArray2D(Collection<? extends T> shapes) {
    	this.shapes = new ArrayList<T>(shapes.size());
    	this.shapes.addAll(shapes);
    }
    
    public ShapeArray2D(T[] shapes) {
    	this.shapes = new ArrayList<T>(shapes.length);
    	for(T shape : shapes)
    		this.shapes.add(shape);
    }
    
    // ===================================================================
    // Management of curves

    /**
     * Adds the shape to the shape set, if it does not already belongs to the
     * set.
     * 
     * @param shape the shape to add
     */
    public boolean add(T shape) {
        if(shapes.contains(shape))
        	return false;
        return shapes.add(shape);
    }

	public void add(int index, T shape) {
		this.shapes.add(index, shape);
	}

    /**
     * Returns the inner shape corresponding to the given index.
     * 
     * @param index index of the shape
     * @return the i-th inner curve
     */
    public T get(int index) {
        return shapes.get(index);
    }

    /**
     * Removes the specified shape from the shape set.
     * 
     * @param shape the shape to remove
     */
    public boolean remove(T shape) {
    	return shapes.remove(shape);
    }

	public T remove(int index) {
		return this.shapes.remove(index);
	}

    /**
     * Checks if the shape set contains the given shape.
     */
    public boolean contains(T shape) {
    	return shapes.contains(shape);
    }

	public int indexOf(T shape) {
		return shapes.indexOf(shape);
	}

    /**
     * Clears the inner shape collection.
     */
    public void clear() {
    	shapes.clear();
    }

	public int size() {
		return shapes.size();
	}
    
    /**
     * Returns the collection of shapes
     * 
     * @return the inner collection of shapes
     */
    public Collection<T> shapes() {
        return shapes;
    }

    
    // ===================================================================
    // Methods implementing the Shape2D interface

    /* (non-Javadoc)
	 * @see math.geom2d.Shape2D#clip(math.geom2d.Box2D)
	 */
	public Shape2D clip(Box2D box) {
		ArrayList<Shape2D> clippedShapes = new ArrayList<Shape2D>(this.size());
		for (T shape : shapes)
			clippedShapes.add(shape.clip(box));
		return new ShapeArray2D<Shape2D>(clippedShapes);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#contains(double, double)
	 */
	public boolean contains(double x, double y) {
        for (Shape2D shape : shapes) {
            if (shape.contains(x, y))
                return true;
        }
        return false;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#contains(java.awt.geom.Point2D)
	 */
	public boolean contains(Point2D p) {
		return contains(p.x(), p.y());
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g2) {
    	for(Shape2D shape : shapes)
    		shape.draw(g2);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#getBoundingBox()
	 */
	public Box2D boundingBox() {
        double xmin = Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        double ymax = Double.MIN_VALUE;

        Box2D box;
        for (Shape2D shape : shapes) {
        	
            box = shape.boundingBox();
            xmin = Math.min(xmin, box.getMinX());
            ymin = Math.min(ymin, box.getMinY());
            xmax = Math.max(xmax, box.getMaxX());
            ymax = Math.max(ymax, box.getMaxY());
        }

        return new Box2D(xmin, xmax, ymin, ymax);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#getDistance(Point2D)
	 */
	public double distance(Point2D p) {
		return this.distance(p.x(), p.y());
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#getDistance(double, double)
	 */
	public double distance(double x, double y) {
        double dist = Double.POSITIVE_INFINITY;
        for (Shape2D shape : shapes)
            dist = Math.min(dist, shape.distance(x, y));
        return dist;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#isBounded()
	 */
	public boolean isBounded() {
        for (Shape2D shape : shapes)
            if (!shape.isBounded())
                return false;
        return true;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#isEmpty()
	 */
	public boolean isEmpty() {
		return this.shapes.isEmpty();
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#transform(math.geom2d.AffineTransform2D)
	 */
	public ShapeSet2D<? extends Shape2D> transform(AffineTransform2D trans) {
    	// Allocate array for result
		ShapeArray2D<Shape2D> result = 
    		new ShapeArray2D<Shape2D>(shapes.size());
        
        // add each transformed curve
        for (Shape2D shape : this.shapes)
            result.add(shape.transform(trans));
        return result;
	}

    // ===================================================================
    // methods implementing GeometricObject2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
	 */
	public boolean almostEquals(GeometricObject2D obj, double eps) {
    	if (this == obj)
    		return true;
    	
        // check class, and cast type
        if (!(obj instanceof ShapeSet2D<?>))
            return false;
        ShapeArray2D<?> shapeSet = (ShapeArray2D<?>) obj;

        // check the number of curves in each set
        if (this.shapes.size() != shapeSet.shapes.size())
            return false;

        // return false if at least one couple of curves does not match
        Iterator<?> iter2 = shapeSet.shapes.iterator();
        for(T shape : shapes) {
        	if(!shape.almostEquals((GeometricObject2D) iter2.next(), eps))
        		return false;
        }
        
        // otherwise return true
        return true;
	}
	
    // ===================================================================
    // methods implementing the Iterable interface

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<T> iterator() {
		return shapes.iterator();
	}

    // ===================================================================
    // methods inherited from interface Object

    /**
     * Returns true if obj is a CurveSet2D with the same number of shapes, and
     * such that each shape belongs to both objects.
     */
    @Override
    public boolean equals(Object obj) {
    	if (this==obj)
    		return true;
    	
        // check class, and cast type
        if (!(obj instanceof ShapeSet2D<?>))
            return false;
        ShapeArray2D<?> shapeSet = (ShapeArray2D<?>) obj;

        // check the number of curves in each set
        if (this.shapes.size() != shapeSet.shapes.size())
            return false;

        // return false if at least one couple of curves does not match
        Iterator<?> iter2 = shapeSet.shapes.iterator();
        for(T shape : shapes) {
        	if(!shape.equals(iter2.next()))
        		return false;
        }
        
        // otherwise return true
        return true;
    }

	/**
	 * @deprecated use copy constructor instead (0.11.2)
	 */
	@Deprecated
    @Override
    public ShapeArray2D<? extends Shape2D> clone() {
        ArrayList<Shape2D> array = new ArrayList<Shape2D>(shapes.size());
        for(T shape : shapes)
            array.add(shape);
        return new ShapeArray2D<Shape2D>(array);
    }

}
