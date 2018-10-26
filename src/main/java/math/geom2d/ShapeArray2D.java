/**
 * File: 	ShapeArray2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 17 ao�t 10
 */
package math.geom2d;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import math.geom2d.point.Point2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * Default Implementation of IShapeSet2D.
 * 
 * @author dlegland
 *
 *         TODO not immutable
 */
public class ShapeArray2D<T extends IShape2D> implements IShapeSet2D<T>, Serializable {
    private static final long serialVersionUID = 1L;

    public static <T extends IShape2D> ShapeArray2D<T> create(Collection<T> shapes) {
        return new ShapeArray2D<>(shapes);
    }

    @SafeVarargs
    public static <T extends IShape2D> ShapeArray2D<T> create(T... shapes) {
        return new ShapeArray2D<>(shapes);
    }

    // ===================================================================
    // Class variables

    /** The inner array of curves */
    protected ArrayList<T> shapes;

    // ===================================================================
    // Constructors

    public ShapeArray2D() {
        this.shapes = new ArrayList<>();
    }

    public ShapeArray2D(int n) {
        this.shapes = new ArrayList<>(n);
    }

    public ShapeArray2D(Collection<? extends T> shapes) {
        this.shapes = new ArrayList<>(shapes.size());
        this.shapes.addAll(shapes);
    }

    public ShapeArray2D(T[] shapes) {
        this.shapes = new ArrayList<>(shapes.length);
        for (T shape : shapes)
            this.shapes.add(shape);
    }

    // ===================================================================
    // Management of curves

    /**
     * Adds the shape to the shape set, if it does not already belongs to the set.
     * 
     * @param shape
     *            the shape to add
     */
    @Override
    public boolean add(T shape) {
        if (shapes.contains(shape))
            return false;
        return shapes.add(shape);
    }

    @Override
    public void add(int index, T shape) {
        this.shapes.add(index, shape);
    }

    /**
     * Returns the inner shape corresponding to the given index.
     * 
     * @param index
     *            index of the shape
     * @return the i-th inner curve
     */
    @Override
    public T get(int index) {
        return shapes.get(index);
    }

    /**
     * Removes the specified shape from the shape set.
     * 
     * @param shape
     *            the shape to remove
     */
    @Override
    public boolean remove(T shape) {
        return shapes.remove(shape);
    }

    @Override
    public T remove(int index) {
        return this.shapes.remove(index);
    }

    /**
     * Checks if the shape set contains the given shape.
     */
    @Override
    public boolean contains(T shape) {
        return shapes.contains(shape);
    }

    @Override
    public int indexOf(T shape) {
        return shapes.indexOf(shape);
    }

    /**
     * Clears the inner shape collection.
     */
    @Override
    public void clear() {
        shapes.clear();
    }

    @Override
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

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#clip(math.geom2d.Box2D)
     */
    @Override
    public IShape2D clip(Box2D box) {
        ArrayList<IShape2D> clippedShapes = new ArrayList<>(this.size());
        for (T shape : shapes)
            clippedShapes.add(shape.clip(box));
        return new ShapeArray2D<>(clippedShapes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#contains(double, double)
     */
    @Override
    public boolean contains(double x, double y) {
        for (IShape2D shape : shapes) {
            if (shape.contains(x, y))
                return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#contains(java.awt.geom.Point2D)
     */
    @Override
    public boolean contains(Point2D p) {
        return contains(p.x(), p.y());
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#draw(java.awt.Graphics2D)
     */
    @Override
    public void draw(Graphics2D g2) {
        for (IShape2D shape : shapes)
            shape.draw(g2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#getBoundingBox()
     */
    @Override
    public Box2D boundingBox() {
        double xmin = Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        double ymax = Double.MIN_VALUE;

        Box2D box;
        for (IShape2D shape : shapes) {

            box = shape.boundingBox();
            xmin = Math.min(xmin, box.getMinX());
            ymin = Math.min(ymin, box.getMinY());
            xmax = Math.max(xmax, box.getMaxX());
            ymax = Math.max(ymax, box.getMaxY());
        }

        return new Box2D(xmin, xmax, ymin, ymax);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#getDistance(Point2D)
     */
    @Override
    public double distance(Point2D p) {
        return this.distance(p.x(), p.y());
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#getDistance(double, double)
     */
    @Override
    public double distance(double x, double y) {
        double dist = Double.POSITIVE_INFINITY;
        for (IShape2D shape : shapes)
            dist = Math.min(dist, shape.distance(x, y));
        return dist;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#isBounded()
     */
    @Override
    public boolean isBounded() {
        for (IShape2D shape : shapes)
            if (!shape.isBounded())
                return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return this.shapes.isEmpty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#transform(math.geom2d.AffineTransform2D)
     */
    @Override
    public IShapeSet2D<? extends IShape2D> transform(AffineTransform2D trans) {
        // Allocate array for result
        ShapeArray2D<IShape2D> result = new ShapeArray2D<>(shapes.size());

        // add each transformed curve
        for (IShape2D shape : this.shapes)
            result.add(shape.transform(trans));
        return result;
    }

    // ===================================================================
    // methods implementing GeometricObject2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
     */
    @Override
    public boolean almostEquals(IGeometricObject2D obj, double eps) {
        if (this == obj)
            return true;

        // check class, and cast type
        if (!(obj instanceof IShapeSet2D<?>))
            return false;
        ShapeArray2D<?> shapeSet = (ShapeArray2D<?>) obj;

        // check the number of curves in each set
        if (this.shapes.size() != shapeSet.shapes.size())
            return false;

        // return false if at least one couple of curves does not match
        Iterator<?> iter2 = shapeSet.shapes.iterator();
        for (T shape : shapes) {
            if (!shape.almostEquals((IGeometricObject2D) iter2.next(), eps))
                return false;
        }

        // otherwise return true
        return true;
    }

    // ===================================================================
    // methods implementing the Iterable interface

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return shapes.iterator();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((shapes == null) ? 0 : shapes.hashCode());
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
        ShapeArray2D<?> other = (ShapeArray2D<?>) obj;
        if (shapes == null) {
            if (other.shapes != null)
                return false;
        } else if (!shapes.equals(other.shapes))
            return false;
        return true;
    }
}
