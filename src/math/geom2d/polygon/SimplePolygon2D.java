/* File SimplePolygon2D.java 
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

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.GeometricObject2D;
import math.geom2d.Point2D;
import math.geom2d.circulinear.CirculinearContourArray2D;
import math.geom2d.circulinear.CirculinearDomain2D;
import math.geom2d.circulinear.CirculinearDomain2DUtils;
import math.geom2d.circulinear.GenericCirculinearDomain2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * Represent a polygonal domain whose boundary is a single closed polyline.
 */
public class SimplePolygon2D implements Polygon2D {

    // ===================================================================
    // Static constructors
    
    /**
     * Static factory for creating a new SimplePolygon2D from a collection of
     * points.
     * @since 0.8.1
     */
    public static SimplePolygon2D create(Collection<? extends Point2D> points) {
    	return new SimplePolygon2D(points);
    }
    
    /**
     * Static factory for creating a new SimplePolygon2D from an array of
     * points.
     * @since 0.8.1
     */
    public static SimplePolygon2D create(Point2D[] points) {
    	return new SimplePolygon2D(points);
    }
    

    // ===================================================================
    // class variables

    /**
     * The inner ordered list of vertices. The last point is connected to the
     * first one.
     */
    protected ArrayList<Point2D> points;

    // ===================================================================
    // constructors

    /**
     * Empty constructor: no vertex.
     */
    public SimplePolygon2D() {
    	points = new ArrayList<Point2D>();
    }

    /**
     * Constructor from an array of points
     * 
     * @param tab the vertices stored in an array of Point2D
     */
    public SimplePolygon2D(Point2D[] tab) {
        points = new ArrayList<Point2D>(tab.length);
        for (Point2D element : tab)
            points.add(element);
    }

    /**
     * Constructor from two arrays, one for each coordinate.
     * 
     * @param xcoords the x coordinate of each vertex
     * @param ycoords the y coordinate of each vertex
     */
    public SimplePolygon2D(double[] xcoords, double[] ycoords) {
        points = new ArrayList<Point2D>(xcoords.length);
        for (int i = 0; i<xcoords.length; i++)
            points.add(new Point2D(xcoords[i], ycoords[i]));
    }

    public SimplePolygon2D(Collection<? extends Point2D> points) {
        this.points = new ArrayList<Point2D>(points.size());
        this.points.addAll(points);
    }

    
    // ===================================================================
    // methods specific to SimplePolygon2D

    /**
     * Adds a point as the last vertex.
     */
    public void addVertex(Point2D point) {
        this.points.add(point);
    }

    /**
     * Removes a vertex of the polygon.
     * 
     * @param point the vertex to be removed.
     */
    public void removeVertex(Point2D point) {
        this.points.remove(point);
    }

    /**
     * Computes area of the polygon, by returning the absolute value of the
     * signed area.
     */
    public double getArea() {
        return Math.abs(this.getSignedArea());
    }

    /**
     * Computes the signed area of the polygon. Algorithm is taken from page: <a
     * href="http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/">
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/</a>. Signed are
     * is positive if polygon is oriented counter-clockwise, and negative
     * otherwise. Result is wrong if polygon is self-intersecting.
     * 
     * @return the signed area of the polygon.
     */
    public double getSignedArea() {
        double area = 0;
        Point2D prev = this.points.get(points.size()-1);
        for (Point2D point : this.points) {
            area += prev.getX()*point.getY()-prev.getY()*point.getX();
            prev = point;
        }
        return area /= 2;
    }

    /**
     * Computes the centroid (center of mass) of the polygon. Algorithm is taken
     * from page: <a
     * href="http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/">
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/</a>.
     * 
     * @return the centroid of the polygon
     */
    public Point2D getCentroid() {
        double xc = 0;
        double yc = 0;
        double tmp = 0;
        Point2D prev = this.points.get(points.size()-1);
        for (Point2D point : this.points) {
            tmp = prev.getX()*point.getY()-prev.getY()*point.getX();
            xc += tmp*(point.getX()+prev.getX());
            yc += tmp*(point.getY()+prev.getY());
            prev = point;
        }
        double area = this.getSignedArea()*6;
        return new Point2D(xc/area, yc/area);
    }

    /**
     * Computes the winding number of the polygon. Algorithm adapted from
     * http://www.geometryalgorithms.com/Archive/algorithm_0103/algorithm_0103.htm
     * 
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return the number of windings of the curve around the point
     */
    public int getWindingNumber(double x, double y) {
        return Polygon2DUtils.windingNumber(points, new Point2D(x, y));
    }
    
    /**
     * Removes all the vertices of the polygon.
     */
    public void clearVertices() {
        this.points.clear();
    }
    
    /**
     * Changes the position of the i-th vertex.
     */
    public void setVertex(int index, Point2D position) {
        this.points.set(index, position);
    }

    
    // ===================================================================
    // methods inherited from Polygon2D interface

    /**
     * Returns the points of the polygon. The result is a pointer to the inner
     * collection of vertices.
     */
    public Collection<Point2D> getVertices() {
        return points;
    }

    /**
     * Returns the i-th vertex of the polygon.
     * 
     * @param i index of the vertex, between 0 and the number of vertices
     */
    public Point2D getVertex(int i) {
        return points.get(i);
    }

    /**
     * Returns the number of vertices of the polygon.
     * 
     * @since 0.6.3
     */
    public int getVertexNumber() {
        return points.size();
    }

    /**
     * Returns the set of edges, as a collection of LineSegment2D.
     */
    public Collection<LineSegment2D> getEdges() {

        int nPoints = this.points.size();
        ArrayList<LineSegment2D> edges = new ArrayList<LineSegment2D>(nPoints);

        if (nPoints==0)
            return edges;

        for (int i = 0; i<nPoints-1; i++)
            edges.add(new LineSegment2D(points.get(i), points.get(i+1)));

        edges.add(new LineSegment2D(points.get(nPoints-1), points.get(0)));

        return edges;
    }

    /**
     * Returns the number of edges. For a simple polygon, this equals the
     * number of vertices.
     */
    public int getEdgeNumber() {
        return points.size();
    }

    /* (non-Javadoc)
     * @see math.geom2d.polygon.Polygon2D#getRings()
     */
    public Collection<LinearRing2D> getRings() {
        ArrayList<LinearRing2D> rings = new ArrayList<LinearRing2D>(1);
        rings.add(new LinearRing2D(points));
        return rings;
    }

    
	// ===================================================================
    // methods inherited from Domain2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearDomain2D#transform(math.geom2d.transform.CircleInversion2D)
	 */
	public CirculinearDomain2D transform(CircleInversion2D inv) {
		return new GenericCirculinearDomain2D(
				this.getBoundary().transform(inv));
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearShape2D#getBuffer(double)
	 */
	public CirculinearDomain2D getBuffer(double dist) {
		return CirculinearDomain2DUtils.computeBuffer(this, dist);
	}

	// ===================================================================
    // methods inherited from Domain2D interface

    /**
     * Returns a set of one LinearRing2D, which encloses the polygon.
     */
    public CirculinearContourArray2D<LinearRing2D> getBoundary() {
        Point2D[] array = new Point2D[this.points.size()];
        for (int i = 0; i<this.points.size(); i++)
            array[i] = this.points.get(i);

        return new CirculinearContourArray2D<LinearRing2D>(
        		new LinearRing2D(array));
    }

    /**
     * Returns the polygon created by reversing the order of the vertices.
     */
    public SimplePolygon2D complement() {
        int nPoints = this.points.size();

        Point2D[] res = new Point2D[nPoints];

        if (nPoints>0)
            res[0] = this.points.get(0);

        for (int i = 1; i<nPoints; i++) {
            res[i] = this.points.get(nPoints-i);
        }
        return new SimplePolygon2D(res);
    }

    
    // ===================================================================
    // methods inherited from Shape2D interface

    /**
     * Returns the distance of the point to the polygon. This is actually the
     * minimal distance computed for each edge if the polygon, or ZERO if the
     * point belong to the polygon.
     */
    public double getDistance(java.awt.geom.Point2D p) {
        return getDistance(p.getX(), p.getY());
    }

    /**
     * Returns the distance of the point to the polygon. This is actually the
     * minimal distance computed for each edge if the polygon, or ZERO if the
     * point belong to the polygon.
     */
    public double getDistance(double x, double y) {
        if (contains(x, y))
            return 0;
        return getBoundary().getDistance(x, y);
    }

    /**
     * Returns the signed distance of the shape to the given point: this distance
     * is positive if the point lies outside the shape, and is negative if the
     * point lies inside the shape. In this case, absolute value of distance is
     * equals to the distance to the border of the shape.
     */
    public double getSignedDistance(java.awt.geom.Point2D p) {
        return getSignedDistance(p.getX(), p.getY());
    }

    /**
     * Returns the signed distance of the shape to the given point: this distance
     * is positive if the point lies outside the shape, and is negative if the
     * point lies inside the shape. In this case, absolute value of distance is
     * equals to the distance to the border of the shape.
     */
    public double getSignedDistance(double x, double y) {
        double dist = getBoundary().getDistance(x, y);
        if (contains(x, y))
            return -dist;
        else
            return dist;
    }

    /**
     * Returns the shape formed by the polygon clipped by the given box.
     */
    public Polygon2D clip(Box2D box) {
        return Polygon2DUtils.clipPolygon(this, box);
    }

    /**
     * Returns the bounding box of the polygon.
     */
    public Box2D getBoundingBox() {
        return getBoundary().getBoundingBox();
    }

    /**
     * Returns true if polygon is oriented counter-clockwise, false otherwise.
     */
    public boolean isBounded() {
        return this.getSignedArea()>0;
    }

    public boolean isEmpty() {
        return points.size()==0;
    }

    /**
     * Returns the new Polygon created by an affine transform of this polygon.
     * If the transform is not direct, the order of vertices is reversed.
     */
    public SimplePolygon2D transform(AffineTransform2D trans) {
        int nPoints = this.points.size();

        Point2D[] array = new Point2D[nPoints];
        Point2D[] res = new Point2D[nPoints];

        for (int i = 0; i<nPoints; i++) {
            array[i] = this.points.get(i);
            res[i] = new Point2D();
        }
        trans.transform(array, res);

        SimplePolygon2D poly = new SimplePolygon2D(res);
        if (!trans.isDirect())
            poly = poly.complement();

        return poly;
    }

    // ===================================================================
    // methods inherited from Shape interface

    /**
     * Return true if the point p lies inside the polygon, with precision given
     * by Shape2D.ACCURACY.
     */
    public boolean contains(java.awt.geom.Point2D p) {
        return contains(p.getX(), p.getY());
    }

    /**
     * Returns true if the point (x, y) lies inside the polygon, with precision
     * given by Shape2D.ACCURACY.
     */
    public boolean contains(double x, double y) {
        return this.getWindingNumber(x, y)==1
                ||this.getBoundary().contains(x, y);
    }

    /**
     * Returns a general path iterator.
     */
    public java.awt.geom.GeneralPath getGeneralPath() {
        java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
        if (points.size()<2)
            return path;

        // move to first point
        Point2D point = points.get(0);
        path.moveTo((float) (point.getX()), (float) (point.getY()));

        // line to each point
        for (int i = 0; i<points.size(); i++) {
            point = points.get(i);
            path.lineTo((float) (point.getX()), (float) (point.getY()));
        }

        // close polygon
        point = points.get(0);
        path.lineTo((float) (point.getX()), (float) (point.getY()));
        path.closePath();

        return path;
    }

    public void draw(Graphics2D g2) {
        g2.draw(this.getGeneralPath());
    }

    public void fill(Graphics2D g) {
        g.fill(this.getGeneralPath());
    }


	// ===================================================================
	// methods implementing the GeometricObject2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
	 */
    public boolean almostEquals(GeometricObject2D obj, double eps) {
    	if (this==obj)
    		return true;
    	
        if (!(obj instanceof SimplePolygon2D))
            return false;
        SimplePolygon2D polygon = (SimplePolygon2D) obj;

        if (polygon.getVertexNumber()!=this.getVertexNumber())
            return false;

        if (!polygon.getBoundingBox().almostEquals(this.getBoundingBox(), eps))
            return false;

        for (Point2D point : polygon.getVertices()) {
        	//TODO: better check of contains
            if (!this.points.contains(point))
                return false;
        }

        return true;
    }

    // ===================================================================
    // methods inherited from Object interface

    /**
     * Tests if the two polygons are equal. Test first the number of vertices,
     * then the bounding boxes, then if each vertex of the polygon is contained
     * in the vertices array of this polygon.
     */
    @Override
    public boolean equals(Object obj) {
    	if (this==obj)
    		return true;
        if (!(obj instanceof SimplePolygon2D))
            return false;

        SimplePolygon2D polygon = (SimplePolygon2D) obj;

        if (polygon.getVertexNumber()!=this.getVertexNumber())
            return false;

        if (!polygon.getBoundingBox().equals(this.getBoundingBox()))
            return false;

        for (Point2D point : polygon.getVertices()) {
            if (!this.points.contains(point))
                return false;
        }

        return true;
    }
    
    @Override
    public SimplePolygon2D clone() {
        ArrayList<Point2D> array = new ArrayList<Point2D>(points.size());
        for(Point2D point : points)
            array.add(point.clone());
        return new SimplePolygon2D(array);
    }

}