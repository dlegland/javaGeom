package math.geom2d.polygon;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.*;

import math.geom2d.Box2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Boundary2DUtils;
import math.geom2d.domain.BoundarySet2D;
import math.geom2d.domain.ContinuousBoundary2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.line.ClosedPolyline2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * A polygonal domain whose boundary is composed of several disjoint
 * continuous ClosedPolyline2D.
 * @author dlegland
 *
 */
public class MultiPolygon2D implements Domain2D, Polygon2D {
	
	ArrayList<ClosedPolyline2D> polylines = new ArrayList<ClosedPolyline2D>();
	
	// ===================================================================
	// Constructors
	
	public MultiPolygon2D(){		
	}
	
	public MultiPolygon2D(ClosedPolyline2D polyline){
		polylines.add(polyline);
	}

	public MultiPolygon2D(SimplePolygon2D polygon){
		polylines.add((ClosedPolyline2D) polygon.getBoundary());
	}
	
	public MultiPolygon2D(Collection<ClosedPolyline2D> lines){
		polylines.addAll(lines);
	}

//	public MultiPolygon2D(Collection<Polygon2D> polygons){
//		for(Polygon2D polygon : polygons)
//			polylines.add((ClosedPolyline2D) polygon.getBoundary());
//	}
	
	// ===================================================================
	// methods specific to MultiPolygon2D
	
	
	public void addPolygon(SimplePolygon2D polygon){
		polylines.add((ClosedPolyline2D) polygon.getBoundary());
	}
	
	/**
	 * Return the set of (oriented) polygons forming this MultiPolygon2D.
	 * @return a set of Polygon2D.
	 */
	public Collection<SimplePolygon2D> getPolygons(){
		ArrayList<SimplePolygon2D> polygons = 
			new ArrayList<SimplePolygon2D>();
		for(ClosedPolyline2D polyline : polylines)
			polygons.add(new SimplePolygon2D(polyline.getPoints()));
		return polygons;
	}
	
	public void addPolyline(ClosedPolyline2D polyline){
		polylines.add(polyline);
	}
	
	// ===================================================================
	// methods inherited from interface AbstractDomain2D
	
	public BoundarySet2D<ClosedPolyline2D> getBoundary() {
		return new BoundarySet2D<ClosedPolyline2D>(polylines);
	}

	// ===================================================================
	// methods inherited from interface AbstractPolygon2D
	
	public Collection<LineSegment2D> getEdges() {
		ArrayList<LineSegment2D> edges = new ArrayList<LineSegment2D>();
		for(ClosedPolyline2D line : polylines)
			edges.addAll(line.getEdges());
		return edges;
	}

	public Collection<math.geom2d.Point2D> getVertices() {
		ArrayList<math.geom2d.Point2D> points = new ArrayList<math.geom2d.Point2D>();
		for(ClosedPolyline2D line : polylines)
			points.addAll(line.getPoints());
		return points;
	}

	public int getVerticesNumber() {
		int count = 0;
		for(ClosedPolyline2D line : polylines)
			count += line.getVerticesNumber();
		return count;
	}

	// ===================================================================
	// methods inherited from interface Shape2D
	
	public Box2D getBoundingBox() {
		Box2D box = new Box2D(
				Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
		for(ClosedPolyline2D line : this.polylines)
			box = box.union(line.getBoundingBox());
		return box;
	}

	/**
	 * Returns a new instance of MultiPolygon2D.
	 */
	public MultiPolygon2D clip(Box2D box) {
		BoundarySet2D<?> boundary = 
			Boundary2DUtils.clipBoundary(this.getBoundary(), box);
		ArrayList<ClosedPolyline2D> boundaries = 
			new ArrayList<ClosedPolyline2D>(boundary.getCurveNumber());
		for(ContinuousBoundary2D curve : boundary.getBoundaryCurves())
			boundaries.add((ClosedPolyline2D) curve);
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
		if(!boundary.isBounded()) return false;

		// Computes the signed area
		double area = 0;
		for(ClosedPolyline2D polyline : polylines)
			area += polyline.getSignedArea();
		
		// bounded if positive area
		return area>0;
	}

	public boolean isEmpty(){
		for(ClosedPolyline2D polyline : polylines)
			if(!polyline.isEmpty())
				return false;
		return true;
	}
	
	public MultiPolygon2D transform(AffineTransform2D trans) {
		ArrayList<ClosedPolyline2D> transformed =
			new ArrayList<ClosedPolyline2D>(polylines.size());
		for(ClosedPolyline2D line : polylines)
			transformed.add((ClosedPolyline2D) line.transform(trans));
		return new MultiPolygon2D(transformed);
	}

	public boolean contains(java.awt.geom.Point2D point) {
		double angle = 0;
		for(ClosedPolyline2D line : this.polylines)
			angle += line.getWindingAngle(point);
		return angle>Math.PI;
	}

	public boolean contains(double x, double y) {
		return this.contains(new math.geom2d.Point2D(x, y)); 
	}

	public boolean contains(Rectangle2D rect) {
		return this.contains(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	public boolean contains(double xr, double yr, double wr, double hr) {
		if (this.getBoundary().intersects(xr, yr, wr, hr)) return false;
		if (this.contains(xr, yr)) return true;
		return false;
	}

	public Rectangle getBounds() {
		return this.getBoundingBox().getAsAWTRectangle();
	}

	public Rectangle2D getBounds2D() {
		return this.getBoundingBox().getAsAWTRectangle2D();
	}

	public PathIterator getPathIterator(AffineTransform at) {
		return this.getBoundary().getPathIterator(at);
	}

	public PathIterator getPathIterator(AffineTransform at, double fl) {
		return this.getBoundary().getPathIterator(at, fl);
	}

	public boolean intersects(Rectangle2D rect) {
		return intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	public boolean intersects(double xr, double yr, double wr, double hr) {
		if (this.contains(xr, yr)) return true;
		if (this.getBoundary().intersects(xr, yr, wr, hr)) return true;
		return false;
	}


}
