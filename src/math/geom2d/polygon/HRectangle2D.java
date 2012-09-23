/* File HRectangle2D.java 
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

// Imports

/**
 * HRectangle2D defines a rectangle with edges parallel to main axis. Thus, it
 * can not be rotated, contrary to Rectangle2D. This class is actually simply a
 * wrapper of class <code>java.awt.geom.Rectangle2D.Double</code> with
 * interface <code>AbstractPolygon</code>.
 * @deprecated since 0.11.0
 */
@Deprecated
public class HRectangle2D extends java.awt.geom.Rectangle2D.Double implements
        Polygon2D {

    // ===================================================================
    // constants

    private static final long serialVersionUID = 1L;

    // ===================================================================
    // class variables

    // ===================================================================
    // constructors

    /** Main constructor */
    public HRectangle2D(double x0, double y0, double w, double h) {
        super(x0, y0, w, h);
    }

    /** Empty constructor (size and position zero) */
    public HRectangle2D() {
        super(0, 0, 0, 0);
    }

    /** Constructor from awt, to allow easy construction from existing apps. */
    public HRectangle2D(java.awt.geom.Rectangle2D rect) {
        super(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    /** Main constructor */
    public HRectangle2D(Point2D point, double w, double h) {
        super(point.getX(), point.getY(), w, h);
    }

    // ===================================================================
    // methods inherited from interface Polygon2D


    public Collection<Point2D> vertices() {
        ArrayList<Point2D> points = new ArrayList<Point2D>(4);
        points.add(new Point2D(x, y));
        points.add(new Point2D(x+width, y));
        points.add(new Point2D(x+width, y+height));
        points.add(new Point2D(x, y+height));
        return points;
    }

    /**
     * Returns the i-th vertex of the polygon.
     * 
     * @param i index of the vertex, between 0 and 3
     */
    public Point2D vertex(int i) {
        switch (i) {
        case 0:
            return new Point2D(x, y);
        case 1:
            return new Point2D(x+width, y);
        case 2:
            return new Point2D(x+width, y+height);
        case 3:
            return new Point2D(x, y+height);
        default:
            throw new IndexOutOfBoundsException();
        }
    }

	public void setVertex(int i, Point2D point) {
		throw new UnsupportedOperationException("Vertices of HRectangle objects can not be modified");
	}

	public void addVertex(Point2D point) {
		throw new UnsupportedOperationException("Vertices of HRectangle objects can not be modified");
	}

	public void insertVertex(int i, Point2D point) {
		throw new UnsupportedOperationException("Vertices of HRectangle objects can not be modified");
	}

	public void removeVertex(int i) {
		throw new UnsupportedOperationException("Vertices of HRectangle objects can not be modified");
	}

    /**
     * Returns the number of vertex, which is 4.
     * 
     * @since 0.6.3
     */
    public int vertexNumber() {
        return 4;
    }

    /**
     * Computes the index of the closest vertex to the input point.
     */
    public int closestVertexIndex(Point2D point) {
    	double minDist = java.lang.Double.POSITIVE_INFINITY;
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
    
    public Collection<LineSegment2D> edges() {
        ArrayList<LineSegment2D> edges = new ArrayList<LineSegment2D>(4);
        edges.add(new LineSegment2D(x, y, x+width, y));
        edges.add(new LineSegment2D(x+width, y, x+width, y+height));
        edges.add(new LineSegment2D(x+width, y+height, x, y+height));
        edges.add(new LineSegment2D(x, y+height, x, y));
        return edges;
    }

    public int edgeNumber() {
        return 4;
    }

    /**
     * Computes the signed area of the polygon. 
     * @return the signed area of the polygon.
     * @since 0.9.1
     */
    public double area() {
    	return Polygons2D.computeArea(this);
    }

    /**
     * Computes the centroid (center of mass) of the polygon. 
     * @return the centroid of the polygon
     * @since 0.9.1
     */
    public Point2D centroid() {
    	return Polygons2D.computeCentroid(this);
    }
    
    
	// ===================================================================
    // methods implementing the CirculinearShape2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearDomain2D#transform(math.geom2d.transform.CircleInversion2D)
	 */
	public CirculinearDomain2D transform(CircleInversion2D inv) {
		return new GenericCirculinearDomain2D(
				this.boundary().transform(inv));
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearShape2D#getBuffer(double)
	 */
	public CirculinearDomain2D buffer(double dist) {
		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		return bc.computeBuffer(this.boundary(), dist);
	}

    // ===================================================================
    // methods inherited from interface Domain2D

	/* (non-Javadoc)
	 * @see math.geom2d.domain.Domain2D#getAsPolygon(int)
	 */
	public Polygon2D asPolygon(int n) {
		return this;
	}

    public CirculinearContourArray2D<LinearRing2D> boundary() {
        return new CirculinearContourArray2D<LinearRing2D>(this.asRing());
    }

	/* (non-Javadoc)
	 * @see math.geom2d.domain.Domain2D#contours()
	 */
	public Collection<LinearRing2D> contours() {
       ArrayList<LinearRing2D> rings = new ArrayList<LinearRing2D>(1);
       rings.add(this.asRing());
       return rings;
	}

	private LinearRing2D asRing() {
        Point2D pts[] = new Point2D[4];
        pts[0] = new Point2D(x, y);
        pts[1] = new Point2D(width+x, y);
        pts[2] = new Point2D(width+x, y+height);
        pts[3] = new Point2D(x, y+height);
		
        return new LinearRing2D(pts);
	}
	
    public Polygon2D complement() {
        Point2D pts[] = new Point2D[4];
        pts[0] = new Point2D(x, y);
        pts[1] = new Point2D(x, y+height);
        pts[2] = new Point2D(width+x, y+height);
        pts[3] = new Point2D(width+x, y);
        return new SimplePolygon2D(pts);
    }

    // ===================================================================
    // methods overriding the Shape2D interface

    /**
     * Returns the distance of the point to the polygon. The result is the
     * minimal distance computed for each edge if the polygon, or ZERO if the
     * point lies inside the polygon.
     */
    public double distance(Point2D p) {
        return distance(p.getX(), p.getY());
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
     * Returns the clipping of the rectangle, as an instance of HRectangle2D. If
     * rectangle is outside clipping box, returns an instance of HRectangle with
     * 0 width and height.
     */
    public HRectangle2D clip(Box2D box) {
        double xmin = Math.max(this.getMinX(), box.getMinX());
        double xmax = Math.min(this.getMaxX(), box.getMaxX());
        double ymin = Math.max(this.getMinY(), box.getMinY());
        double ymax = Math.min(this.getMaxY(), box.getMaxY());
        if (xmin>xmax||ymin>ymax)
            return new HRectangle2D(xmin, ymin, 0, 0);
        else
            return new HRectangle2D(xmin, xmax, xmax-xmin, ymax-ymin);
    }

    /** Always returns true, because a rectangle is always bounded. */
    public boolean isBounded() {
        return true;
    }

    public Box2D boundingBox() {
        return new Box2D(this.getMinX(), this.getMaxX(), this.getMinY(), this
                .getMaxY());
    }

    /**
     * Return the new Polygon created by an affine transform of this polygon.
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

    public void draw(Graphics2D g2) {
        g2.draw(this.boundary().getGeneralPath());
    }

    public void fill(Graphics2D g2) {
        g2.fill(this.boundary().getGeneralPath());
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
        if (!(obj instanceof HRectangle2D))
            return false;
        HRectangle2D rect = (HRectangle2D) obj;

        // check all 4 corners of the first rectangle
        boolean ok;
        for (Point2D point : this.vertices()) {
            ok = false;

            // compare with all 4 corners of second rectangle
            for (Point2D point2 : rect.vertices())
                if (point.almostEquals(point2, eps))
                    ok = true;

            // if the point does not belong to the corners of the other
            // rectangle,
            // then the two rectangles are different
            if (!ok)
                return false;
        }

        // test ok for 4 corners, then the two rectangles are the same.
        return true;
    }

    // ===================================================================
    // general methods

    /**
     * Test if rectangles are the same. We consider two rectangles are equal if
     * their corners are the same. Then, we can have different origins and
     * different angles, but equal rectangles.
     */
    @Override
    public boolean equals(Object obj) {

        // check class, and cast type
        if (!(obj instanceof HRectangle2D))
            return false;
        HRectangle2D rect = (HRectangle2D) obj;

        // check all 4 corners of the first rectangle
        boolean ok;
        for (Point2D point : this.vertices()) {
            ok = false;

            // compare with all 4 corners of second rectangle
            for (Point2D point2 : rect.vertices())
                if (point.equals(point2))
                    ok = true;

            // if the point does not belong to the corners of the other
            // rectangle,
            // then the two rectangles are different
            if (!ok)
                return false;
        }

        // test ok for 4 corners, then the two rectangles are the same.
        return true;
    }

	public boolean contains(Point2D p) {
		return this.contains(p.getX(), p.getY());
	}

}