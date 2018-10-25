
package math.geom2d.polygon;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.IGeometricObject2D;
import math.geom2d.Point2D;
import math.geom2d.circulinear.CirculinearContourArray2D;
import math.geom2d.circulinear.ICirculinearDomain2D;
import math.geom2d.circulinear.GenericCirculinearDomain2D;
import math.geom2d.domain.IBoundary2D;
import math.geom2d.domain.IDomain2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * A polygonal domain whose boundary is composed of several disjoint continuous LinearRing2D.
 * 
 * @author dlegland
 */
public class MultiPolygon2D implements IDomain2D, IPolygon2D {

    // ===================================================================
    // Static constructors

    public static MultiPolygon2D create(Collection<LinearRing2D> rings) {
        return new MultiPolygon2D(rings);
    }

    public static MultiPolygon2D create(LinearRing2D... rings) {
        return new MultiPolygon2D(rings);
    }

    // ===================================================================
    // class members

    ArrayList<LinearRing2D> rings = new ArrayList<LinearRing2D>(1);

    // ===================================================================
    // Constructors

    public MultiPolygon2D() {
    }

    /**
     * Ensures the inner buffer has enough capacity for storing the required number of rings.
     */
    public MultiPolygon2D(int nRings) {
        this.rings.ensureCapacity(nRings);
    }

    public MultiPolygon2D(LinearRing2D... rings) {
        for (LinearRing2D ring : rings)
            this.rings.add(ring);
    }

    public MultiPolygon2D(IPolygon2D polygon) {
        if (polygon instanceof SimplePolygon2D) {
            rings.add(((SimplePolygon2D) polygon).getRing());
        } else {
            rings.addAll(polygon.boundary().curves());
        }
    }

    public MultiPolygon2D(Collection<LinearRing2D> lines) {
        rings.addAll(lines);
    }

    // ===================================================================
    // Management of rings

    public void addRing(LinearRing2D ring) {
        rings.add(ring);
    }

    public void insertRing(int index, LinearRing2D ring) {
        rings.add(index, ring);
    }

    public void removeRing(LinearRing2D ring) {
        rings.remove(ring);
    }

    public void clearRings() {
        rings.clear();
    }

    public LinearRing2D getRing(int index) {
        return rings.get(index);
    }

    public void setRing(int index, LinearRing2D ring) {
        rings.set(index, ring);
    }

    public int ringNumber() {
        return rings.size();
    }

    // ===================================================================
    // methods implementing the Polygon2D interface

    /**
     * Computes the signed area of the polygon.
     * 
     * @return the signed area of the polygon.
     * @since 0.9.1
     */
    public double area() {
        return Polygons2D.computeArea(this);
    }

    /**
     * Computes the centroid (center of mass) of the polygon.
     * 
     * @return the centroid of the polygon
     * @since 0.9.1
     */
    public Point2D centroid() {
        return Polygons2D.computeCentroid(this);
    }

    public Collection<LineSegment2D> edges() {
        int nEdges = edgeNumber();
        ArrayList<LineSegment2D> edges = new ArrayList<LineSegment2D>(nEdges);
        for (LinearRing2D ring : rings)
            edges.addAll(ring.edges());
        return edges;
    }

    public int edgeNumber() {
        int count = 0;
        for (LinearRing2D ring : rings)
            count += ring.vertexNumber();
        return count;
    }

    public Collection<Point2D> vertices() {
        int nv = vertexNumber();
        ArrayList<Point2D> points = new ArrayList<Point2D>(nv);
        for (LinearRing2D ring : rings)
            points.addAll(ring.vertices());
        return points;
    }

    /**
     * Returns the i-th vertex of the polygon.
     * 
     * @param i
     *            index of the vertex, between 0 and the number of vertices minus one
     */
    public Point2D vertex(int i) {
        int count = 0;
        LinearRing2D boundary = null;

        for (LinearRing2D ring : rings) {
            int nv = ring.vertexNumber();
            if (count + nv > i) {
                boundary = ring;
                break;
            }
            count += nv;
        }

        if (boundary == null)
            throw new IndexOutOfBoundsException();

        return boundary.vertex(i - count);
    }

    /**
     * Sets the position of the i-th vertex of this polygon.
     * 
     * @param i
     *            index of the vertex, between 0 and the number of vertices
     */
    public void setVertex(int i, Point2D point) {
        int count = 0;
        LinearRing2D boundary = null;

        for (LinearRing2D ring : rings) {
            int nv = ring.vertexNumber();
            if (count + nv > i) {
                boundary = ring;
                break;
            }
            count += nv;
        }

        if (boundary == null)
            throw new IndexOutOfBoundsException();

        boundary.setVertex(i - count, point);
    }

    /**
     * Adds a vertex at the end of the last ring of this polygon.
     * 
     * @throws RuntimeException
     *             if this MultiPolygon does not contain any ring
     */
    public void addVertex(Point2D position) {
        // get the last ring
        if (rings.size() == 0) {
            throw new RuntimeException("Can not add a vertex to a multipolygon with no ring");
        }
        LinearRing2D ring = rings.get(rings.size() - 1);
        ring.addVertex(position);
    }

    /**
     * Inserts a vertex at the given position
     * 
     * @throws RuntimeException
     *             if this polygon has no ring
     * @throws IllegalArgumentException
     *             if index is not smaller than vertex number
     */
    public void insertVertex(int index, Point2D point) {
        // check number of rings
        if (rings.size() == 0) {
            throw new RuntimeException("Can not add a vertex to a multipolygon with no ring");
        }

        // Check number of vertices
        int nv = this.vertexNumber();
        if (nv <= index) {
            throw new IllegalArgumentException("Can not insert vertex at position " + index + " (max is " + nv + ")");
        }

        // Find the ring that correspond to index
        int count = 0;
        LinearRing2D boundary = null;

        for (LinearRing2D ring : rings) {
            nv = ring.vertexNumber();
            if (count + nv > index) {
                boundary = ring;
                break;
            }
            count += nv;
        }

        if (boundary == null)
            throw new IndexOutOfBoundsException();

        boundary.insertVertex(index - count, point);
    }

    /**
     * Returns the i-th vertex of the polygon.
     * 
     * @param i
     *            index of the vertex, between 0 and the number of vertices minus one
     */
    public void removeVertex(int i) {
        int count = 0;
        LinearRing2D boundary = null;

        for (LinearRing2D ring : rings) {
            int nv = ring.vertexNumber();
            if (count + nv > i) {
                boundary = ring;
                break;
            }
            count += nv;
        }

        if (boundary == null)
            throw new IndexOutOfBoundsException();

        boundary.removeVertex(i - count);
    }

    /**
     * Returns the total number of vertices in this polygon. The total number is computed as the sum of vertex number in each ring of the polygon.
     */
    public int vertexNumber() {
        int count = 0;
        for (LinearRing2D ring : rings)
            count += ring.vertexNumber();
        return count;
    }

    /**
     * Computes the index of the closest vertex to the input point.
     */
    public int closestVertexIndex(Point2D point) {
        double minDist = Double.POSITIVE_INFINITY;
        int index = -1;

        int i = 0;
        for (LinearRing2D ring : this.rings) {
            for (Point2D vertex : ring.vertices()) {
                double dist = vertex.distance(point);
                if (dist < minDist) {
                    index = i;
                    minDist = dist;
                }
                i++;
            }

        }

        return index;
    }

    // ===================================================================
    // methods implementing the Domain2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearDomain2D#transform(math.geom2d.transform.CircleInversion2D)
     */
    public ICirculinearDomain2D transform(CircleInversion2D inv) {
        return new GenericCirculinearDomain2D(this.boundary().transform(inv).reverse());
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearShape2D#buffer(double)
     */
    public ICirculinearDomain2D buffer(double dist) {
        return Polygons2D.createBuffer(this, dist);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.domain.Domain2D#asPolygon(int)
     */
    public IPolygon2D asPolygon(int n) {
        return this;
    }

    public CirculinearContourArray2D<LinearRing2D> boundary() {
        return CirculinearContourArray2D.create(rings.toArray(new LinearRing2D[0]));
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.domain.Domain2D#contours()
     */
    public Collection<LinearRing2D> contours() {
        return Collections.unmodifiableList(rings);
    }

    public IPolygon2D complement() {
        // allocate memory for array of reversed rings
        ArrayList<LinearRing2D> reverseLines = new ArrayList<LinearRing2D>(rings.size());

        // reverse each ring
        for (LinearRing2D ring : rings)
            reverseLines.add(ring.reverse());

        // create the new MultiMpolygon2D with set of reversed rings
        return new MultiPolygon2D(reverseLines);
    }

    // ===================================================================
    // methods inherited from interface Shape2D

    public Box2D boundingBox() {
        // start with empty bounding box
        Box2D box = new Box2D(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);

        // compute union of all bounding boxes
        for (LinearRing2D ring : this.rings)
            box = box.union(ring.boundingBox());

        // return result
        return box;
    }

    /**
     * Clips the polygon with the specified box.
     */
    public IPolygon2D clip(Box2D box) {
        return Polygons2D.clipPolygon(this, box);
    }

    public double distance(Point2D p) {
        return Math.max(this.boundary().signedDistance(p), 0);
    }

    public double distance(double x, double y) {
        return Math.max(this.boundary().signedDistance(x, y), 0);
    }

    public boolean isBounded() {
        // If boundary is not bounded, the polygon is not
        IBoundary2D boundary = this.boundary();
        if (!boundary.isBounded())
            return false;

        // Computes the signed area
        double area = 0;
        for (LinearRing2D ring : rings)
            area += ring.area();

        // bounded if positive area
        return area > 0;
    }

    /**
     * The MultiPolygon2D is empty either if it contains no ring, or if all rings are empty.
     */
    public boolean isEmpty() {
        // return true if at least one ring is not empty
        for (LinearRing2D ring : rings)
            if (!ring.isEmpty())
                return false;
        return true;
    }

    public MultiPolygon2D transform(AffineTransform2D trans) {
        // allocate memory for transformed rings
        ArrayList<LinearRing2D> transformed = new ArrayList<LinearRing2D>(rings.size());

        // transform each ring
        for (LinearRing2D ring : rings)
            transformed.add(ring.transform(trans));

        // creates a new MultiPolygon2D with the set of trasnformed rings
        return new MultiPolygon2D(transformed);
    }

    public boolean contains(Point2D point) {
        double angle = 0;
        for (LinearRing2D ring : this.rings)
            angle += ring.windingAngle(point);

        double area = this.area();
        if (area > 0) {
            return angle > Math.PI;
        } else {
            return angle > -Math.PI;
        }
    }

    public boolean contains(double x, double y) {
        return this.contains(new math.geom2d.Point2D(x, y));
    }

    public void draw(Graphics2D g2) {
        g2.draw(this.boundary().getGeneralPath());
    }

    public void fill(Graphics2D g) {
        g.fill(this.boundary().getGeneralPath());
    }

    // ===================================================================
    // methods implementing the GeometricObject2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
     */
    public boolean almostEquals(IGeometricObject2D obj, double eps) {
        if (this == obj)
            return true;

        if (!(obj instanceof MultiPolygon2D))
            return false;
        MultiPolygon2D polygon = (MultiPolygon2D) obj;

        // check if the two objects have same number of rings
        if (polygon.rings.size() != this.rings.size())
            return false;

        // check each couple of ring
        for (int i = 0; i < rings.size(); i++)
            if (!this.rings.get(i).almostEquals(polygon.rings.get(i), eps))
                return false;

        return true;
    }

    // ===================================================================
    // methods overriding the Object class

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof MultiPolygon2D))
            return false;

        // check if the two objects have same number of rings
        MultiPolygon2D polygon = (MultiPolygon2D) obj;
        if (polygon.rings.size() != this.rings.size())
            return false;

        // check each couple of ring
        for (int i = 0; i < rings.size(); i++)
            if (!this.rings.get(i).equals(polygon.rings.get(i)))
                return false;

        return true;
    }

}
