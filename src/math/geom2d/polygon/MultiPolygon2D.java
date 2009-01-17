
package math.geom2d.polygon;

import java.awt.Graphics2D;
import java.util.*;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Boundary2DUtils;
import math.geom2d.domain.BoundarySet2D;
import math.geom2d.domain.ContinuousBoundary2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * A polygonal domain whose boundary is composed of several disjoint continuous
 * Ring2D.
 * 
 * @author dlegland
 */
public class MultiPolygon2D implements Domain2D, Polygon2D {

    ArrayList<Ring2D> polylines = new ArrayList<Ring2D>();

    // ===================================================================
    // Constructors

    public MultiPolygon2D() {
    }

    public MultiPolygon2D(Ring2D polyline) {
        polylines.add(polyline);
    }

    public MultiPolygon2D(Ring2D[] polylines) {
        for (Ring2D polyline : polylines)
            this.polylines.add(polyline);
    }

    public MultiPolygon2D(SimplePolygon2D polygon) {
        polylines.addAll(polygon.getBoundary().getCurves());
    }

    public MultiPolygon2D(Collection<Ring2D> lines) {
        polylines.addAll(lines);
    }

    // ===================================================================
    // methods specific to MultiPolygon2D

    public void addPolygon(SimplePolygon2D polygon) {
        polylines.addAll(polygon.getBoundary().getCurves());
    }

    /**
     * Return the set of (oriented) polygons forming this MultiPolygon2D.
     * 
     * @return a set of Polygon2D.
     */
    public Collection<SimplePolygon2D> getPolygons() {
        ArrayList<SimplePolygon2D> polygons = new ArrayList<SimplePolygon2D>();
        for (Ring2D polyline : polylines)
            polygons.add(new SimplePolygon2D(polyline.getVertices()));
        return polygons;
    }

    public void addPolyline(Ring2D polyline) {
        polylines.add(polyline);
    }

    // ===================================================================
    // methods inherited from interface AbstractDomain2D

    public BoundarySet2D<Ring2D> getBoundary() {
        return new BoundarySet2D<Ring2D>(polylines);
    }

    public Polygon2D complement() {
        ArrayList<Ring2D> reverseLines = new ArrayList<Ring2D>(
                polylines.size());
        for (Ring2D line : polylines)
            reverseLines.add(line.getReverseCurve());
        return new MultiPolygon2D(reverseLines);
    }

    // ===================================================================
    // methods inherited from interface AbstractPolygon2D

    public Collection<LineSegment2D> getEdges() {
        ArrayList<LineSegment2D> edges = new ArrayList<LineSegment2D>();
        for (Ring2D line : polylines)
            edges.addAll(line.getEdges());
        return edges;
    }

    public int getEdgeNumber() {
        int count = 0;
        for (Ring2D line : polylines)
            count += line.getVertexNumber();
        return count;
    }

    public Collection<math.geom2d.Point2D> getVertices() {
        ArrayList<math.geom2d.Point2D> points = new ArrayList<math.geom2d.Point2D>();
        for (Ring2D line : polylines)
            points.addAll(line.getVertices());
        return points;
    }

    /**
     * Returns the i-th vertex of the polygon.
     * 
     * @param i index of the vertex, between 0 and the number of vertices
     */
    public Point2D getVertex(int i) {
        int count = 0;
        Ring2D boundary = null;

        for (Ring2D polyline : polylines) {
            int nv = polyline.getVertexNumber();
            if (count+nv>i) {
                boundary = polyline;
                break;
            }
            count += nv;
        }

        if (boundary==null)
            throw new IndexOutOfBoundsException();

        return boundary.getVertex(i-count);
    }

    public int getVertexNumber() {
        int count = 0;
        for (Ring2D line : polylines)
            count += line.getVertexNumber();
        return count;
    }

    // ===================================================================
    // methods inherited from interface Shape2D

    public Box2D getBoundingBox() {
        Box2D box = new Box2D(Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY);
        for (Ring2D line : this.polylines)
            box = box.union(line.getBoundingBox());
        return box;
    }

    /**
     * Returns a new instance of MultiPolygon2D.
     */
    public MultiPolygon2D clip(Box2D box) {
        BoundarySet2D<?> boundary = Boundary2DUtils.clipBoundary(this
                .getBoundary(), box);
        ArrayList<Ring2D> boundaries = new ArrayList<Ring2D>(
                boundary.getCurveNumber());
        for (ContinuousBoundary2D curve : boundary.getBoundaryCurves())
            boundaries.add((Ring2D) curve);
        return new MultiPolygon2D(boundaries);
    }

    public double getDistance(java.awt.geom.Point2D p) {
        return Math.max(this.getBoundary().getSignedDistance(p), 0);
    }

    public double getDistance(double x, double y) {
        return Math.max(this.getBoundary().getSignedDistance(x, y), 0);
    }

    public boolean isBounded() {
        // If boundary is not bounded, the polygon is not
        Boundary2D boundary = this.getBoundary();
        if (!boundary.isBounded())
            return false;

        // Computes the signed area
        double area = 0;
        for (Ring2D polyline : polylines)
            area += polyline.getSignedArea();

        // bounded if positive area
        return area>0;
    }

    public boolean isEmpty() {
        for (Ring2D polyline : polylines)
            if (!polyline.isEmpty())
                return false;
        return true;
    }

    public MultiPolygon2D transform(AffineTransform2D trans) {
        ArrayList<Ring2D> transformed = new ArrayList<Ring2D>(
                polylines.size());
        for (Ring2D line : polylines)
            transformed.add(line.transform(trans));
        return new MultiPolygon2D(transformed);
    }

    public boolean contains(java.awt.geom.Point2D point) {
        double angle = 0;
        for (Ring2D line : this.polylines)
            angle += line.getWindingAngle(point);
        return angle>Math.PI;
    }

    public boolean contains(double x, double y) {
        return this.contains(new math.geom2d.Point2D(x, y));
    }

    public void draw(Graphics2D g2) {
        g2.draw(this.getBoundary().getGeneralPath());
    }

    public void fill(Graphics2D g) {
        g.fill(this.getBoundary().getGeneralPath());
    }
}
