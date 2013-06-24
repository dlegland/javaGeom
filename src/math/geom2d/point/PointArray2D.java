/* file : PointSet2D.java
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
 * Created on 5 mai 2006
 *
 */

package math.geom2d.point;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.GeometricObject2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.circulinear.CirculinearDomain2D;
import math.geom2d.circulinear.CirculinearShape2D;
import math.geom2d.circulinear.buffer.BufferCalculator;
import math.geom2d.transform.CircleInversion2D;

/**
 * Represent the union of a finite number of Point2D.
 * 
 * @author dlegland
 */
public class PointArray2D 
implements PointSet2D, CirculinearShape2D, Cloneable {

    // ===================================================================
    // static constructors

	public static <T extends Point2D> PointArray2D create(Collection<T> points) {
		return new PointArray2D(points);
	}

	public static <T extends Point2D> PointArray2D create(T... points) {
		return new PointArray2D(points);
    }
    
    /**
     * Allocate memory for the specified number of points.
     */
    public static PointArray2D create(int size) {
    	return new PointArray2D(size);
    }
    
    // ===================================================================
    // inner variables

	/**
     * The inner collection of points composing the set.
     */
    protected ArrayList<Point2D> points = null;

    // ===================================================================
    // constructors
    
    /**
     * Creates a new PointArray2D without any points.
     */
    public PointArray2D() {
        this(0);
    }

    /**
     * Creates a new empty PointArray2D, but preallocates the memory for storing a
     * given amount of points.
     * 
     * @param n the expected number of points in the PointArray2D.
     */
    public PointArray2D(int n) {
        points = new ArrayList<Point2D>();
    }

    /**
     * Instances of Point2D are directly added, other Point are converted to
     * Point2D with the same location.
     */
    public PointArray2D(Point2D... points) {
        this(points.length);
        for (Point2D element : points)
            this.points.add(element);
    }

    /**
     * Copy constructor
     */
    public PointArray2D(PointSet2D set) {
        this(set.size());
        for (Point2D element : set)
            this.points.add(element);
    }

    /**
     * Points must be a collection of java.awt.Point. Instances of Point2D are
     * directly added, other Point are converted to Point2D with the same
     * location.
     * 
     * @param points
     */
    public PointArray2D(Collection<? extends Point2D> points) {
        this(points.size());

        for (Point2D point : points) {
            this.points.add(point);
        }
    }

    // ===================================================================
    // methods implementing the PointSet2D interface
    
    /**
     * Add a new point to the set of point. If point is not an instance of
     * Point2D, a Point2D with same location is added instead of point.
     * 
     * @param point
     */
    public boolean add(Point2D point) {
        return this.points.add(point);
    }

	public void add(int index, Point2D point) {
		this.points.add(index, point);
	}

    /**
     * Add a series of points
     * 
     * @param points an array of points
     */
    public void addAll(Point2D[] points) {
        for (Point2D element : points)
            this.add(element);
    }

    public void addAll(Collection<? extends Point2D> points) {
        this.points.addAll(points);
    }

    public Point2D get(int index) {
    	return this.points.get(index);
    }
    
	public boolean remove(Point2D point) {
		return this.points.remove(point);
	}
	
	public Point2D remove(int index) {
		return this.points.remove(index);
	}

	public int indexOf(Point2D point) {
		return this.points.indexOf(point);
	}

	/**
     * return an iterator on the internal point collection.
     * 
     * @return the collection of points
     */
    public Collection<Point2D> points() {
        return Collections.unmodifiableList(points);
    }

    /**
     * remove all points of the set.
     */
    public void clear() {
        this.points.clear();
    }

    /**
     * Returns the number of points in the set.
     * 
     * @return the number of points
     */
    public int size() {
        return points.size();
    }


    // ===================================================================
    // Methods implementing CirculinearShape2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearShape2D#buffer(double)
	 */
	public CirculinearDomain2D buffer(double dist) {
		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		return bc.computeBuffer(this, dist);
	}

	public PointArray2D transform(CircleInversion2D inv) {
    	
    	PointArray2D array = new PointArray2D(points.size());
    	
    	for (Point2D point : points) 
    		array.add(point.transform(inv));
    	
    	return array;
    }
   
   /**
     * Return distance to the closest point of the collection
     */
    public double distance(Point2D p) {
        return distance(p.x(), p.y());
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#distance(double, double)
     */
    public double distance(double x, double y) {
    	// basic checkup
        if (points.isEmpty())
            return Double.NaN;
        
        // find smallest distance
        double dist = Double.MAX_VALUE;
        for (Point2D point : points)
            dist = Math.min(dist, point.distance(x, y));
        
        // return distance to closest point
        return dist;
    }

    /**
     * Always return true.
     */
    public boolean isBounded() {
        return true;
    }
    
    /** 
     * Returns true if the point set is empty, i.e. the number of points is 0.
     */
    public boolean isEmpty() {
        return points.size() == 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#clip(java.awt.geom.Rectangle2D)
     */
    public PointArray2D clip(Box2D box) {
    	// allocate memory for result
        PointArray2D res = new PointArray2D(points.size());

        // select only points inside of box
        for (Point2D point : points) {
        	if (box.contains(point)) {
        		res.add(point);
        	}
        }
        
        // use array the right size
        res.points.trimToSize();
        
        // return result
        return res;
    }

    public Box2D boundingBox() {
    	// init with max values in each direction
        double xmin = Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        double ymax = Double.MIN_VALUE;

        // update max values with each point
        for (Point2D point : points) {
            xmin = Math.min(xmin, point.x());
            ymin = Math.min(ymin, point.y());
            xmax = Math.max(xmax, point.x());
            ymax = Math.max(ymax, point.y());
        }
        
        // create the bounding box
        return new Box2D(xmin, xmax, ymin, ymax);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#transform(math.geom2d.AffineTransform2D)
     */
    public PointArray2D transform(AffineTransform2D trans) {
        PointArray2D res = new PointArray2D(points.size());

        for (Point2D point : points)
            res.add(point.transform(trans));

        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Shape#contains(double, double)
     */
    public boolean contains(double x, double y) {
        for (Point2D point : points)
            if (point.distance(x, y) < Shape2D.ACCURACY)
                return true;
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Shape#contains(java.awt.geom.Point2D)
     */
    public boolean contains(Point2D point) {
        return contains(point.x(), point.y());
    }

    /**
     * Draws the point set on the specified Graphics2D, using default radius
     * equal to 1.
     * 
     * @param g2 the graphics to draw the point set
     */
    public void draw(Graphics2D g2) {
        this.draw(g2, 1);
    }

    /**
     * Draws the point set on the specified Graphics2D, by filling a disc with a
     * given radius.
     * 
     * @param g2 the graphics to draw the point set
     */
    public void draw(Graphics2D g2, double r) {
    	double x, y;
    	double w = 2 * r;
        for (Point2D point : points) {
        	x = point.x();
        	y = point.y();
            g2.fill(new java.awt.geom.Ellipse2D.Double(x-r, y-r, w, w));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<Point2D> iterator() {
        return points.iterator();
    }
    
    // ===================================================================
    // methods implementing GeometricObject2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
	 */
	public boolean almostEquals(GeometricObject2D obj, double eps) {
		if (this == obj)
			return true;
		
        if (!(obj instanceof PointSet2D))
            return false;
        
        PointSet2D set = (PointSet2D) obj;
        if (this.points.size() != set.size())
        	return false;

        Iterator<Point2D> iter = set.iterator();
        for (Point2D point : points) {
        	if (!point.almostEquals(iter.next(), eps))
        		return false;
        }
        
        return true;
	}
	
    // ===================================================================
    // methods overriding Object methods


    /**
     * Returns true if the given object is an instance of PointSet2D that
     * contains the same number of points, such that iteration on each set
     * returns equal points.
     */
    @Override
    public boolean equals(Object obj) {
    	if (this == obj)
    		return true;
    	
        if (!(obj instanceof PointSet2D))
            return false;
        
        PointSet2D set = (PointSet2D) obj;
        if (this.points.size() != set.size())
        	return false;
        
        Iterator<Point2D> iter = set.iterator();
        for (Point2D point : points) {
        	if (!point.equals(iter.next()))
        		return false;
        }
        
        return true;
    }
    
	/**
	 * @deprecated use copy constructor instead (0.11.2)
	 */
	@Deprecated
    @Override
    public PointArray2D clone() {
        PointArray2D set = new PointArray2D(this.size());
        for (Point2D point : this)
            set.add(point);
        return set;
    }

}
