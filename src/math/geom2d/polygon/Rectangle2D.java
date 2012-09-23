/* File Rectangle2D.java 
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
 */

// package

package math.geom2d.polygon;

// Imports
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.GeometricObject2D;
import math.geom2d.Point2D;
import math.geom2d.circulinear.CirculinearContourArray2D;
import math.geom2d.circulinear.CirculinearDomain2D;
import math.geom2d.circulinear.GenericCirculinearDomain2D;
import math.geom2d.circulinear.buffer.BufferCalculator;
import math.geom2d.line.LineSegment2D;
import math.geom2d.transform.CircleInversion2D;
import math.utils.EqualUtils;

/**
 * Rectangle2D defines a rectangle rotated around its first corner.
 */
public class Rectangle2D implements Polygon2D {

    // ===================================================================
    // constants

    // ===================================================================
    // class variables

    protected double x0;
    protected double y0;
    protected double w;
    protected double h;
    

    // ===================================================================
    // constructors

    /**
	 * Main constructor, specifying position of reference corner and rectangle
	 * dimensions.  
	 */
    public Rectangle2D(double x0, double y0, double w, double h) {
        this.x0 = x0;
        this.y0 = y0;
        this.w = w;
        this.h = h;
    }

    /** 
     * Empty constructor (size and position zero) 
     */
    public Rectangle2D() {
        this(0, 0, 0, 0);
    }

    /**
     * Constructor from awt, to allow easy construction from existing apps. 
     */
    public Rectangle2D(java.awt.geom.Rectangle2D rect) {
        this.x0 = rect.getX();
        this.y0 = rect.getY();
        this.w = rect.getWidth();
        this.h = rect.getHeight();
    }


    /** 
     * Creates a rectangle from two corner points. Origin and dimensions are
     * automatically determined.
     */
    public Rectangle2D(Point2D p1, Point2D p2) {
    	this.x0 = Math.min(p1.x(), p2.x());
    	this.y0 = Math.min(p1.y(), p2.y());
    	this.w = Math.max(p1.x(), p2.x()) - this.x0;
    	this.h = Math.max(p1.y(), p2.y()) - this.y0;
    }

    // ===================================================================
    // accessors

    public double getX() {
        return x0;
    }

    public double getY() {
        return y0;
    }

    public double getWidth() {
        return w;
    }

    public double getHeight() {
        return h;
    }

        
    // ===================================================================
	// methods inherited from interface Polygon2D
	
	/**
	 * Returns the vertices of the rectangle as a collection of points.
	 * 
	 * @return the vertices of the rectangle.
	 */
	public Collection<Point2D> vertices() {
		// Allocate memory
	    ArrayList<Point2D> array = new ArrayList<Point2D>(4);
	    
	    // add each vertex
		array.add(new Point2D(x0, y0));
		array.add(new Point2D(x0 + w, y0));
		array.add(new Point2D(x0 + w, y0 + h));
		array.add(new Point2D(x0, y0 + h));
	
		// return result array
	    return array;
	}

	/**
	 * Returns the number of vertices of the rectangle, which is 4.
	 * 
	 * @since 0.6.3
	 */
	public int vertexNumber() {
	    return 4;
	}

	/**
     * Returns the i-th vertex of the polygon.
     * 
     * @param i index of the vertex, between 0 and 3
     */
    public Point2D vertex(int i) {
        switch (i) {
        case 0:
            return new Point2D(x0, y0);
        case 1:
            return new Point2D(x0+w, y0);
        case 2:
            return new Point2D(x0+w, y0+h);
        case 3:
            return new Point2D(x0, y0+h);
        default:
            throw new IndexOutOfBoundsException();
        }
    }

	public void setVertex(int i, Point2D point) {
		throw new UnsupportedOperationException("Vertices of Rectangle objects can not be modified");
	}

	public void addVertex(Point2D point) {
		throw new UnsupportedOperationException("Vertices of Rectangle objects can not be modified");
	}

	public void insertVertex(int i, Point2D point) {
		throw new UnsupportedOperationException("Vertices of Rectangle objects can not be modified");
	}

	public void removeVertex(int i) {
		throw new UnsupportedOperationException("Vertices of Rectangle objects can not be modified");
	}

    /**
     * Computes the index of the closest vertex to the input point.
     */
    public int closestVertexIndex(Point2D point) {
    	double minDist = Double.POSITIVE_INFINITY;
    	int index = -1;
    	
    	int i = 0;
    	for (Point2D vertex : this.vertices()) {
    		double dist = vertex.distance(point);
    		if (dist < minDist) {
    			index = i;
    			minDist = dist;
    		}
    		i++;
    	}
    	
    	return index;
    }
    
    /**
     * Returns the four edges that constitute the boundary of this rectangle.
     */
    public Collection<LineSegment2D> edges() {
        ArrayList<LineSegment2D> edges = new ArrayList<LineSegment2D>(4);
		edges.add(new LineSegment2D(x0, y0, x0 + w, y0));
		edges.add(new LineSegment2D(x0 + w, y0, x0 + w, y0 + h));
		edges.add(new LineSegment2D(x0 + w, y0 + h, x0, y0 + h));
		edges.add(new LineSegment2D(x0, y0 + h, x0, y0));
        return edges;
    }

    /**
     * Returns 4, as a rectangle has four edges.
     */
    public int edgeNumber() {
        return 4;
    }

    /**
     * Computes the area of this rectangle, given by the product of width by
     * height. 
     * @return the signed area of the polygon.
     * @since 0.9.1
     */
    public double area() {
    	return this.w * this.h;
    }

    /**
     * Computes the centroid (center of mass) of this rectangle.  
     * @return the centroid of the polygon
     * @since 0.9.1
     */
    public Point2D centroid() {
    	double xc = x0 + this.w  / 2;
    	double yc = y0 + this.h  / 2;
    	return new Point2D(xc, yc);
    }
    
    // ===================================================================
    // methods inherited from Domain2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.domain.Domain2D#asPolygon(int)
	 */
	public Polygon2D asPolygon(int n) {
		return this;
	}

	// ===================================================================
	// methods inherited from interface CirculinearShape2D
	
	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearDomain2D#transform(math.geom2d.transform.CircleInversion2D)
	 */
	public CirculinearDomain2D transform(CircleInversion2D inv) {
		return new GenericCirculinearDomain2D(
				this.boundary().transform(inv));
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearShape2D#buffer(double)
	 */
	public CirculinearDomain2D buffer(double dist) {
		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		return bc.computeBuffer(this.boundary(), dist);
	}

	
    // ===================================================================
    // methods inherited from interface Domain2D

   public CirculinearContourArray2D<LinearRing2D> boundary() {
        return new CirculinearContourArray2D<LinearRing2D>(asRing());
    }

	/* (non-Javadoc)
	 * @see math.geom2d.domain.Domain2D#contours()
	 */
	public Collection<LinearRing2D> contours() {
       ArrayList<LinearRing2D> rings = new ArrayList<LinearRing2D>(1);
       rings.add(this.asRing());
       return rings;
	}

	/**
	 * Returns the ring that constitute the boundary of this rectangle.
	 * @return
	 */
	private LinearRing2D asRing() {
        Point2D pts[] = new Point2D[4];
		pts[0] = new Point2D(x0, y0);
		pts[1] = new Point2D(x0 + w, y0);
		pts[2] = new Point2D(x0 + w, y0 + h);
		pts[3] = new Point2D(x0, y0 + h);
		
        return new LinearRing2D(pts);
	}

	/**
	 * Returns a new simple Polygon whose vertices are in reverse order of
	 * this rectangle.
	 */
	public Polygon2D complement() {
        Point2D pts[] = new Point2D[4];
		pts[0] = new Point2D(x0, y0);
		pts[1] = new Point2D(x0, y0 + h);
		pts[2] = new Point2D(x0 + w, y0 + h);
		pts[3] = new Point2D(x0 + w, y0);

        return new SimplePolygon2D(pts);
    }

    // ===================================================================
    // methods inherited from Shape2D interface

    /** 
     * Always returns true, because a rectangle is always bounded. 
     */
    public boolean isBounded() {
        return true;
    }

    public boolean isEmpty() {
        return false;
    }

    /**
     * Returns the distance of the point to the polygon. The result is the
     * minimal distance computed for each edge if the polygon, or ZERO if the
     * point lies inside the polygon.
     */
    public double distance(Point2D p) {
        return distance(p.x(), p.y());
    }

    /**
     * Returns the distance of the point to the polygon. The result is the
     * minimal distance computed for each edge if the polygon, or ZERO if the
     * point lies inside the polygon.
     */
    public double distance(double x, double y) {
        double dist = boundary().signedDistance(x, y);
        return Math.max(dist, 0);
    }

    /**
     * Returns the clipped polygon.
     */
    public Polygon2D clip(Box2D box) {
    	return Polygons2D.clipPolygon(this, box);
    }

    /**
     * Returns the bounding box of the rectangle.
     */
    public Box2D boundingBox() {
		return new Box2D(x0, x0 + w, y0, y0 + h);
    }

    /**
     * Returns the new Polygon created by an affine transform of this polygon.
     */
    public SimplePolygon2D transform(AffineTransform2D trans) {
        int nPoints = 4;
        Point2D[] array = new Point2D[nPoints];
        Point2D[] res = new Point2D[nPoints];
        Iterator<Point2D> iter = this.vertices().iterator();
        for (int i = 0; i<nPoints; i++) {
            array[i] = iter.next();
            res[i] = new Point2D();
        }

        trans.transform(array, res);
        return new SimplePolygon2D(res);
    }

    // ===================================================================
    // methods inherited from Shape interface

    /**
     * Checks if this rectangle contains the given point.
     */
    public boolean contains(Point2D point) {
        return contains(point.x(), point.y());
    }

    /**
     * Checks if this rectangle contains the point given by (x,y)
     */
    public boolean contains(double x, double y) {
        if (x < this.x0)
        	return false;
        if (x > this.x0 + this.w)
        	return false;
        if (y < this.y0)
        	return false;
        if (y > this.y0 + this.h)
        	return false;
        return true;
    }

    public void draw(Graphics2D g2) {
    	this.asRing().draw(g2);
    }

    public void fill(Graphics2D g2) {
    	this.asRing().fill(g2);
    }


	// ===================================================================
	// methods implementing the GeometricObject2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
	 */
    public boolean almostEquals(GeometricObject2D obj, double eps) {
    	if (this==obj)
    		return true;
    	
        // check class, and cast type
        if (!(obj instanceof Rectangle2D))
            return false;
        Rectangle2D rect = (Rectangle2D) obj;

        // check all 4 corners of the first rectangle
        boolean ok;
        for (Point2D point : this.vertices()) {
            ok = false;

            // compare with all 4 corners of second rectangle
            for (Point2D point2 : rect.vertices())
                if (point.almostEquals(point2, eps)) {
                    ok = true;
                    break;
                }

            // if the point does not belong to the corners of the other
            // rectangle, then the two rect are different
            if (!ok)
                return false;
        }

        // test ok for 4 corners, then the two rectangles are the same.
        return true;
    }

    // ===================================================================
    // methods inherited from Object interface

    /**
     * Tests if rectangles are the same.
     */
    @Override
    public boolean equals(Object obj) {
       	if (this == obj)
    		return true;

       	// check class, and cast type
        if (!(obj instanceof Rectangle2D))
            return false;
        Rectangle2D that = (Rectangle2D) obj;

        // Compare each field
		if (!EqualUtils.areEqual(this.x0, that.x0)) 
			return false;
		if (!EqualUtils.areEqual(this.y0, that.y0)) 
			return false;
		if (!EqualUtils.areEqual(this.w, that.w)) 
			return false;
		if (!EqualUtils.areEqual(this.h, that.h)) 
			return false;

        return true;
    }

}