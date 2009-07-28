
package math.geom2d.polygon;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.circulinear.CirculinearBoundarySet2D;
import math.geom2d.circulinear.CirculinearCurve2DUtils;
import math.geom2d.circulinear.CirculinearDomain2D;
import math.geom2d.circulinear.GenericCirculinearDomain2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Boundary2DUtils;
import math.geom2d.domain.BoundarySet2D;
import math.geom2d.domain.ContinuousBoundary2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * A polygonal domain whose boundary is composed of several disjoint continuous
 * LinearRing2D.
 * 
 * @author dlegland
 */
public class MultiPolygon2D implements Domain2D, Polygon2D {

    ArrayList<LinearRing2D> rings = new ArrayList<LinearRing2D>();

    // ===================================================================
    // Constructors

    public MultiPolygon2D() {
    }

    public MultiPolygon2D(LinearRing2D ring) {
        rings.add(ring);
    }

    public MultiPolygon2D(LinearRing2D[] rings) {
        for (LinearRing2D ring : rings)
            this.rings.add(ring);
    }

    public MultiPolygon2D(SimplePolygon2D polygon) {
        rings.addAll(polygon.getBoundary().getCurves());
    }

    public MultiPolygon2D(Collection<LinearRing2D> lines) {
        rings.addAll(lines);
    }

    // ===================================================================
    // methods specific to MultiPolygon2D

    public void addPolygon(SimplePolygon2D polygon) {
        rings.addAll(polygon.getBoundary().getCurves());
    }

    /**
     * Return the set of (oriented) polygons forming this MultiPolygon2D.
     * 
     * @return a set of Polygon2D.
     */
    public Collection<SimplePolygon2D> getPolygons() {
        // allocate memory for polygon array
        ArrayList<SimplePolygon2D> polygons = new ArrayList<SimplePolygon2D>();
        
        // create a new SimplePolygon with each ring
        for (LinearRing2D ring : rings)
            polygons.add(new SimplePolygon2D(ring.getVertices()));
        return polygons;
    }

    /**
     * Deprecated use addRing instead (0.8.0)
     */
    @Deprecated 
    public void addPolyline(LinearRing2D ring) {
        rings.add(ring);
    }

    public void addRing(LinearRing2D ring) {
        rings.add(ring);
    }

    // ===================================================================
    // methods implementing the Polygon2D interface

  
    /* (non-Javadoc)
     * @see math.geom2d.polygon.Polygon2D#getRings()
     */
    public Collection<LinearRing2D> getRings() {
        return Collections.unmodifiableList(rings);
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
		return CirculinearCurve2DUtils.computeBuffer(
				this.getBoundary(), dist);
	}

	
    // ===================================================================
    // methods inherited from interface Domain2D

    public CirculinearBoundarySet2D<LinearRing2D> getBoundary() {
        return new CirculinearBoundarySet2D<LinearRing2D>(rings);
    }

    public Polygon2D complement() {
        // allocate memory for array of reversed rings
        ArrayList<LinearRing2D> reverseLines = new ArrayList<LinearRing2D>(rings.size());
        
        // reverse each ring
        for (LinearRing2D ring : rings)
            reverseLines.add(ring.getReverseCurve());
        
        // create the new MultiMpolygon2D with set of reversed rings
        return new MultiPolygon2D(reverseLines);
    }

    // ===================================================================
    // methods implementing the interface Polygon2D

    public Collection<LineSegment2D> getEdges() {
        ArrayList<LineSegment2D> edges = new ArrayList<LineSegment2D>();
        for (LinearRing2D ring : rings)
            edges.addAll(ring.getEdges());
        return edges;
    }

    public int getEdgeNumber() {
        int count = 0;
        for (LinearRing2D ring : rings)
            count += ring.getVertexNumber();
        return count;
    }

    public Collection<Point2D> getVertices() {
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        for (LinearRing2D ring : rings)
            points.addAll(ring.getVertices());
        return points;
    }

    /**
     * Returns the i-th vertex of the polygon.
     * 
     * @param i index of the vertex, between 0 and the number of vertices
     */
    public Point2D getVertex(int i) {
        int count = 0;
        LinearRing2D boundary = null;

        for (LinearRing2D ring : rings) {
            int nv = ring.getVertexNumber();
            if (count+nv>i) {
                boundary = ring;
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
        for (LinearRing2D ring : rings)
            count += ring.getVertexNumber();
        return count;
    }

    // ===================================================================
    // methods inherited from interface Shape2D

    public Box2D getBoundingBox() {
        // start with empty bounding box
        Box2D box = new Box2D(
        		Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 
                Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        
        // compute union of all bounding boxes
        for (LinearRing2D ring : this.rings)
            box = box.union(ring.getBoundingBox());
        
        // return result
        return box;
    }

    /**
     * Returns a new instance of MultiPolygon2D.
     */
    public MultiPolygon2D clip(Box2D box) {
        // call generic method for computing clipped boundary
        BoundarySet2D<?> boundary = 
            Boundary2DUtils.clipBoundary(this.getBoundary(), box);
        
        // convert boundary to list of rings
        ArrayList<LinearRing2D> boundaries = new ArrayList<LinearRing2D>(
                boundary.getCurveNumber());
        for (ContinuousBoundary2D curve : boundary.getBoundaryCurves())
            boundaries.add((LinearRing2D) curve);
        
        // create new MultiPolygon with the set of rings
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
        for (LinearRing2D ring : rings)
            area += ring.getSignedArea();

        // bounded if positive area
        return area>0;
    }

    /**
     * The MultiPolygon2D is empty either if it contains no ring, or if all
     * rings are empty.
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
        ArrayList<LinearRing2D> transformed = 
            new ArrayList<LinearRing2D>(rings.size());
        
        // trasnform each ring
        for (LinearRing2D ring : rings)
            transformed.add(ring.transform(trans));
        
        // creates a new MultiPolygon2D with the set of trasnformed rings
        return new MultiPolygon2D(transformed);
    }

    public boolean contains(java.awt.geom.Point2D point) {
        double angle = 0;
        for (LinearRing2D ring : this.rings)
            angle += ring.getWindingAngle(point);
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
    
    public boolean equals(Object obj) {
        if(!(obj instanceof MultiPolygon2D))
            return false;
        
        // check if the two objects have same number of rings
        MultiPolygon2D polygon = (MultiPolygon2D) obj;
        if(polygon.rings.size()!=this.rings.size()) 
            return false;
        
        // check each couple of ring
        for(int i=0; i<rings.size(); i++)
            if(!this.rings.get(i).equals(polygon.rings.get(i)))
                return false;
        
        return true;
    }
   
    public MultiPolygon2D clone() {
        // allocate memory for new ring array
        ArrayList<LinearRing2D> array = new ArrayList<LinearRing2D>(rings.size());
        
        // clone each ring
        for(LinearRing2D ring : rings)
            array.add(ring.clone());
        
        // create a new polygon with cloned rings
        return new MultiPolygon2D(array);
    }
}
