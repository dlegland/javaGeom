/* file : Polyline2D.java
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
 * Created on 8 mai 2006
 *
 */

package math.geom2d.polygon;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import math.geom2d.*;
import math.geom2d.circulinear.CirculinearContinuousCurve2D;
import math.geom2d.circulinear.CirculinearDomain2D;
import math.geom2d.circulinear.PolyCirculinearCurve2D;
import math.geom2d.circulinear.buffer.BufferCalculator;
import math.geom2d.curve.AbstractContinuousCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.Curve2DUtils;
import math.geom2d.curve.CurveArray2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.LinearShape2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.point.PointSet2DUtils;
import math.geom2d.transform.CircleInversion2D;

/**
 * A polyline is a continuous curve where each piece of the curve is a
 * LineSegment2D.
 * 
 * @author dlegland
 */
public class Polyline2D extends AbstractContinuousCurve2D
implements CirculinearContinuousCurve2D, Cloneable {

    // ===================================================================
    // Static methods
    
    /**
     * Static factory for creating a new Polyline2D from a collection of
     * points.
     * @since 0.8.1
     */
    public static Polyline2D create(Collection<? extends Point2D> points) {
    	return new Polyline2D(points);
    }
    
    /**
     * Static factory for creating a new Polyline2D from an array of
     * points.
     * @since 0.8.1
     */
    public static Polyline2D create(Point2D... points) {
    	return new Polyline2D(points);
    }
    

    // ===================================================================
    // class variables
    
    protected ArrayList<Point2D> vertices = new ArrayList<Point2D>();

    
    // ===================================================================
    // Contructors

    public Polyline2D() {
    }

    /**
     * Creates a new polyline by allocating enough memory for the specified number of vertices.
     * @param nVertices
     */
    public Polyline2D(int nVertices) {
    	this.vertices = new ArrayList<Point2D>(nVertices);
    }

    public Polyline2D(Point2D initialPoint) {
        this.vertices.add(initialPoint);
    }

    public Polyline2D(Point2D... vertices) {
        for (Point2D vertex : vertices)
            this.vertices.add(vertex);
    }

    public Polyline2D(Collection<? extends Point2D> vertices) {
        this.vertices.addAll(vertices);
    }

    public Polyline2D(double[] xcoords, double[] ycoords) {
        for (int i = 0; i<xcoords.length; i++)
            vertices.add(new Point2D(xcoords[i], ycoords[i]));
    }


    // ===================================================================
    // Methods specific to Polyline2D

    /**
     * Return an iterator on the collection of points.
     */
    public Iterator<Point2D> getPointsIterator() {
        return vertices.iterator();
    }
    
    /**
     * Return the collection of points as an array of Point2D.
     * 
     * @return an array of Point2D
     */
    public Point2D[] getPointArray() {
        Point2D[] tab = new Point2D[vertices.size()];
        for (int i = 0; i<vertices.size(); i++)
            tab[i] = vertices.get(i);
        return tab;
    }

    /**
     * @deprecated replaced by addVertex(Point2D) (0.9.3)
     */
    @Deprecated
    public void addPoint(Point2D point) {
        vertices.add(point);
    }

    /**
     * Add a vertex at the end of this polyline.
     * @return true if the vertex was correctly added
     * @since 0.9.3
     */
    public boolean addVertex(Point2D vertex) {
    	 return vertices.add(vertex);
    }
    
    /**
     * Insert a vertex at a given position in the polyline.
     * @return true if the vertex was correctly added
     * @since 0.9.3
     */
    public void insertVertex(int index, Point2D vertex) {
    	vertices.add(index, vertex);
    }
    
    /**
     * Removes the first instance of the given vertex from this polyline.
     * @param vertex the position of the vertex to remove
     * @return true if the vertex was actually removed
     * @since 0.9.3
     */
    public boolean removeVertex(Point2D vertex) {
        return vertices.remove(vertex);
    }
    
    /**
     * Removes the vertex specified by the index.
     * @param index the index of the vertex to remove
     * @return the position of the vertex removed from the polyline
     * @since 0.9.3
     */
    public Point2D removeVertex(int index) {
    	return this.vertices.remove(index);
    }

    /**
     * @deprecated replaced by removeVertex(Point2D) (0.9.3)
     */
    @Deprecated
    public void removePoint(Point2D point) {
        vertices.remove(point);
    }

    /**
     * Changes the position of the i-th vertex.
     *  @since 0.9.3
     */
    public void setVertex(int index, Point2D position) {
        this.vertices.set(index, position);
    }

   /**
     * @deprecated replaced by clearVertices()
     */
    @Deprecated
    public void clearPoints() {
        vertices.clear();
    }

    public void clearVertices() {
        vertices.clear();
    }

    /**
     * Returns the vertices of the polyline.
     */
    public Collection<Point2D> vertices() {
        return vertices;
    }

    /**
     * Returns the i-th vertex of the polyline.
     * 
     * @param i index of the vertex, between 0 and the number of vertices
     */
    public Point2D vertex(int i) {
        return vertices.get(i);
    }

    /**
     * Returns the number of vertices.
     * 
     * @return the number of vertices
     */
    public int vertexNumber() {
        return vertices.size();
    }

    /**
     * Returns an array of LineSegment2D. The number of edges is the number of
     * vertices minus one.
     * 
     * @return the edges of the polyline
     */
    public Collection<LineSegment2D> edges() {
        int n = vertices.size();
        ArrayList<LineSegment2D> edges = new ArrayList<LineSegment2D>(n);

        if (n<2)
            return edges;

        for (int i = 0; i<n-1; i++)
            edges.add(new LineSegment2D(vertices.get(i), vertices.get(i+1)));

        return edges;
    }
    
    public LineSegment2D edge(int index) {
    	return new LineSegment2D(vertices.get(index), vertices.get(index+1));
    }

    public LineSegment2D firstEdge() {
        if (vertices.size() < 2)
            return null;
        return new LineSegment2D(vertices.get(0), vertices.get(1));
    }

    public LineSegment2D lastEdge() {
        int n = vertices.size();
        if (n < 2)
            return null;
        return new LineSegment2D(vertices.get(n-2), vertices.get(n-1));
    }

    // ===================================================================
    // methods implementing the CirculinearCurve2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#getLength()
	 */
	public double length() {
		double sum = 0;
		for(LineSegment2D edge : this.edges())
			sum += edge.length();
		return sum;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#getLength(double)
	 */
	public double length(double pos) {
		//init
		double length = 0;
		
		// add length of each curve before current curve
		int index = (int) Math.floor(pos);
		for(int i=0; i<index; i++)
			length += this.edge(i).length();
		
		// add portion of length for last curve
		if(index < vertices.size()-1) {
			double pos2 = pos-index;
			length += this.edge(index).length(pos2);
		}
		
		// return computed length
		return length;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#getPosition(double)
	 */
	public double position(double length) {
		
		// position to compute
		double pos = 0;
		
		// index of current curve
		int index = 0;
		
		// cumulative length
		double cumLength = this.length(this.getT0());
		
		// iterate on all curves
		for(LineSegment2D edge : edges()) {
			// length of current curve
			double edgeLength = edge.length();
			
			// add either 2, or fraction of length
			if(cumLength + edgeLength < length) {
				cumLength += edgeLength;
				index ++;
			} else {
				// add local position on current curve
				double pos2 = edge.position(length - cumLength);
				pos = index + pos2;
				break;
			}			
		}
		
		// return the result
		return pos;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearShape2D#getBuffer(double)
	 */
	public CirculinearDomain2D buffer(double dist) {
		BufferCalculator bc = BufferCalculator.getDefaultInstance();

		// basic check to avoid degenerate cases
		if (PointSet2DUtils.hasAdjacentMultipleVertices(this.vertices)) {
			Polyline2D poly2 = Polyline2D.create(
					PointSet2DUtils.filterAdjacentMultipleVertices(this.vertices));
			return bc.computeBuffer(poly2, dist);			
		}
		
		return bc.computeBuffer(this, dist);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#getParallel(double)
	 */
	public CirculinearContinuousCurve2D parallel(double d) {
		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		return bc.createContinuousParallel(this, d);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearCurve2D#transform(math.geom2d.transform.CircleInversion2D)
	 */
	public CirculinearContinuousCurve2D transform(CircleInversion2D inv) {
		
		// Create array for storing transformed arcs
		Collection<LineSegment2D> edges = this.edges();
		ArrayList<CirculinearContinuousCurve2D> arcs = 
			new ArrayList<CirculinearContinuousCurve2D>(edges.size());
		
		// Transform each arc
		for(LineSegment2D edge : edges) {
			arcs.add(edge.transform(inv));
		}
		
		// create the transformed shape
		return new PolyCirculinearCurve2D<CirculinearContinuousCurve2D>(arcs);
	}

    // ===================================================================
    // Methods inherited from ContinuousCurve2D

	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#getLeftTangent(double)
	 */
	public Vector2D leftTangent(double t) {
		int index = (int) Math.floor(t);
		if(Math.abs(t-index)<Shape2D.ACCURACY)
			index--;
		return this.edge(index).tangent(0);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#getRightTangent(double)
	 */
	public Vector2D rightTangent(double t) {
		int index = (int) Math.ceil(t);
		return this.edge(index).tangent(0);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.curve.ContinuousCurve2D#getCurvature(double)
	 */
	public double curvature(double t) {
		double index = Math.round(t);
		if (Math.abs(index - t) > Shape2D.ACCURACY)
			return 0;
		else
			return Double.POSITIVE_INFINITY;
			
	}

    // ===================================================================
    // Methods implementing OrientedCurve2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.OrientedCurve2D#getSignedDistance(double, double)
     */
    public double distanceSigned(double x, double y) {
        double dist = this.distance(x, y);
        if (isInside(new Point2D(x, y)))
            return -dist;
        else
            return dist;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.OrientedCurve2D#getSignedDistance(Point2D)
     */
    public double distanceSigned(Point2D point) {
        double dist = this.distance(point.getX(), point.getY());
        if (isInside(point))
            return -dist;
        else
            return dist;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.OrientedCurve2D#getWindingAngle(Point2D)
     */
    public double windingAngle(Point2D point) {
        double angle = 0;
        int n = vertices.size();
        for (int i = 0; i<n-1; i++)
            angle += new LineSegment2D(vertices.get(i), vertices.get(i+1))
                    .windingAngle(point);

        return angle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.OrientedCurve2D#isInside(Point2D)
     */
    public boolean isInside(Point2D pt) {
        if (new LinearRing2D(this.getPointArray()).isInside(pt))
            return true;

        if (this.vertices.size()<3)
            return false;

        Point2D p0 = this.firstPoint();
        Point2D q0 = this.vertices.get(1);
        if (new StraightLine2D(q0, p0).isInside(pt))
            return false;

        Point2D p1 = this.lastPoint();
        Point2D q1 = this.vertices.get(vertices.size()-2);
        if (new StraightLine2D(p1, q1).isInside(pt))
            return false;

        if (new StraightLine2D(p0, p1).isInside(pt))
            return true;

        return false;
    }

    // ===================================================================
    // Methods inherited from ContinuousCurve2D

    /**
     * return false, as Polyline2D is not closed by definition.
     */
    public boolean isClosed() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.ContinuousCurve2D#getSmoothPieces()
     */
    public Collection<? extends LineSegment2D> smoothPieces() {
        return edges();
    }

    // ===================================================================
    // Methods inherited from Curve2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Curve2D#getIntersections(math.geom2d.LinearShape2D)
     */
    public Collection<Point2D> intersections(LinearShape2D line) {
        ArrayList<Point2D> list = new ArrayList<Point2D>();

        // extract intersections with each edge, and add to a list
        Point2D point;
        for (LineSegment2D edge : this.edges()) {
        	// do not process edges parallel to intersection line
        	if (edge.isParallel(line))
        		continue;
        	
			point = edge.intersection(line);
			if (point != null)
				if (!list.contains(point))
					list.add(point);
		}

        // return result
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Curve2D#getPoint(double, math.geom2d.Point2D)
     */
    public math.geom2d.Point2D point(double t) {
        // format position to stay between limits
        double t0 = this.getT0();
        double t1 = this.getT1();
        t = Math.max(Math.min(t, t1), t0);

        // index of vertex before point
        int ind0 = (int) Math.floor(t+Shape2D.ACCURACY);
        double tl = t-ind0;
        Point2D p0 = vertices.get(ind0);

		// check if equal to a vertex
		if (Math.abs(t - ind0) < Shape2D.ACCURACY)
			return new Point2D(p0);

        // index of vertex after point
        int ind1 = ind0+1;
        Point2D p1 = vertices.get(ind1);

        // position on line;

        double x0 = p0.getX();
        double y0 = p0.getY();
        double dx = p1.getX()-x0;
        double dy = p1.getY()-y0;

        return new Point2D(x0+tl*dx, y0+tl*dy);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Curve2D#getPosition(math.geom2d.Point2D)
     */
    public double position(Point2D point) {
        int ind = 0;
        double dist, minDist = Double.POSITIVE_INFINITY;
        double x = point.getX();
        double y = point.getY();

        int i = 0;
        LineSegment2D closest = null;
        for (LineSegment2D edge : this.edges()) {
            dist = edge.distance(x, y);
            if (dist<minDist) {
                minDist = dist;
                ind = i;
                closest = edge;
            }
            i++;
        }

        return closest.position(point)+ind;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Curve2D#getPosition(math.geom2d.Point2D)
     */
    public double project(Point2D point) {
        double dist, minDist = Double.POSITIVE_INFINITY;
        double x = point.getX();
        double y = point.getY();
        double pos = Double.NaN;

        int i = 0;
        for (LineSegment2D edge : this.edges()) {
            dist = edge.distance(x, y);
            if (dist<minDist) {
                minDist = dist;
                pos = edge.project(point)+i;
            }
            i++;
        }

        return pos;
    }

    /**
     * returns 0.
     */
    public double getT0() {
        return 0;
    }

    /**
     * return the number of points in the polyline, minus one.
     */
    public double getT1() {
        return vertices.size()-1;
    }

	@Override
    public Point2D firstPoint() {
        if (vertices.size()==0)
            return null;
        return vertices.get(0);
    }

    /**
     * if polyline is closed, return the first point.
     */
	@Override
    public Point2D lastPoint() {
        if (vertices.size()==0)
            return null;
        return vertices.get(vertices.size()-1);
    }

    public Collection<Point2D> singularPoints() {
        return vertices;
    }

    public boolean isSingular(double pos) {
        if (Math.abs(pos-Math.round(pos))<Shape2D.ACCURACY)
            return true;
        return false;
    }

    /**
     * Returns the polyline with same points considered in reverse order.
     * Reversed polyline keep same references as original polyline.
     */
    public Polyline2D reverse() {
        Point2D[] points2 = new Point2D[vertices.size()];
        int n = vertices.size();
        if (n>0) {
            for (int i = 0; i<n; i++)
                points2[i] = vertices.get(n-1-i);
        }
        return new Polyline2D(points2);
    }

	@Override
    public Collection<? extends Polyline2D> continuousCurves() {
    	return wrapCurve(this);
    }


    /**
     * Return an instance of Polyline2D. If t1 is lower than t0, return an
     * instance of Polyline2D with zero points.
     */
    public Polyline2D subCurve(double t0, double t1) {
        // code adapted from CurveSet2D

        Polyline2D res = new Polyline2D();

        if (t1<t0)
            return res;

        // number of points in the polyline
        int indMax = (int) this.getT1();

        // format to ensure t is between T0 and T1
        t0 = Math.min(Math.max(t0, 0), indMax);
        t1 = Math.min(Math.max(t1, 0), indMax);

        // find curves index
        int ind0 = (int) Math.floor(t0);
        int ind1 = (int) Math.floor(t1);

        // need to subdivide only one line segment
        if (ind0==ind1) {
            // extract limit points
            res.addPoint(this.point(t0));
            res.addPoint(this.point(t1));
            // return result
            return res;
        }

        // add the point corresponding to t0
        res.addPoint(this.point(t0));

        // add all the whole points between the 2 cuts
        for (int n = ind0+1; n<=ind1; n++)
            res.addPoint(vertices.get(n));

        // add the last point
        res.addPoint(this.point(t1));

        // return the polyline
        return res;
    }

    // ===================================================================
    // Methods implementing the Shape2D interface

    /** Always returns true, because a polyline is always bounded. */
    public boolean isBounded() {
        return true;
    }

    /**
     * Returns true if the polyline does not contain any point.
     */
    public boolean isEmpty() {
        return vertices.size()==0;
    }

    /**
     * Clip the polyline by a box. The result is an instance of CurveSet2D<Polyline2D>,
     * which contains only instances of Polyline2D. If the ellipse arc is not
     * clipped, the result is an instance of CurveSet2D<Polyline2D> which
     * contains 0 curves.
     */
    public CurveSet2D<? extends Polyline2D> clip(Box2D box) {
        // Clip the curve
        CurveSet2D<? extends Curve2D> set = Curve2DUtils.clipCurve(this, box);

        // Stores the result in appropriate structure
        CurveArray2D<Polyline2D> result =
        	new CurveArray2D<Polyline2D>(set.size());

        // convert the result
        for (Curve2D curve : set.curves()) {
            if (curve instanceof Polyline2D)
                result.add((Polyline2D) curve);
        }
        return result;
    }

    public Box2D boundingBox() {
        double xmin = Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        double ymax = Double.MIN_VALUE;

        Iterator<Point2D> iter = vertices.iterator();
        Point2D point;
        double x, y;
        while (iter.hasNext()) {
            point = iter.next();
            x = point.getX();
            y = point.getY();
            xmin = Math.min(xmin, x);
            xmax = Math.max(xmax, x);
            ymin = Math.min(ymin, y);
            ymax = Math.max(ymax, y);
        }

        return new Box2D(xmin, xmax, ymin, ymax);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#getDistance(double, double)
     */
    public double distance(double x, double y) {
        double dist = Double.MAX_VALUE;
        for (LineSegment2D edge : this.edges()) {
        	if (edge.length()==0)
        		continue;
            dist = Math.min(dist, edge.distance(x, y));
        }
        return dist;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#getDistance(Point2D)
     */
    public double distance(Point2D point) {
        return distance(point.getX(), point.getY());
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#transform(math.geom2d.AffineTransform2D)
     */
    public Polyline2D transform(AffineTransform2D trans) {
        Point2D[] pts = new Point2D[vertices.size()];
        for (int i = 0; i<vertices.size(); i++)
            pts[i] = trans.transform(vertices.get(i));
        return new Polyline2D(pts);
    }

    // ===================================================================
    // Methods inherited from Shape interface

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Shape#contains(double, double)
     */
    public boolean contains(double x, double y) {
        for (LineSegment2D edge : this.edges()) {
        	if (edge.length()==0)
        		continue;
            if (edge.contains(x, y))
                return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Shape#contains(Point2D)
     */
    public boolean contains(Point2D point) {
        return this.contains(point.getX(), point.getY());
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.ContinuousCurve2D#appendPath(java.awt.geom.GeneralPath)
     */
    public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path) {

        if (vertices.size()<2)
            return path;

        // get point iterator
        Iterator<Point2D> iter = vertices.iterator();

        // avoid first point
        Point2D point = iter.next();
       
        // line to each other point
        while (iter.hasNext()) {
            point = iter.next();
            path.lineTo((float) (point.getX()), (float) (point.getY()));
        }

        return path;
    }

    /**
     * Return a general path iterator.
     */
    public java.awt.geom.GeneralPath asGeneralPath() {
        java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
        if (vertices.size()<2)
            return path;

        // get point iterator
        Iterator<Point2D> iter = vertices.iterator();

        // move to first point
        Point2D point = iter.next();
        path.moveTo((float) (point.getX()), (float) (point.getY()));

        // line to each other point
        while (iter.hasNext()) {
            point = iter.next();
            path.lineTo((float) (point.getX()), (float) (point.getY()));
        }

        return path;
    }

    @Override
    public void draw(Graphics2D g2) {
    	g2.draw(this.asGeneralPath());
    }


	// ===================================================================
	// methods implementing the GeometricObject2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
	 */
    public boolean almostEquals(GeometricObject2D obj, double eps) {
    	if (this==obj)
    		return true;
    	
        if (!(obj instanceof Polyline2D))
            return false;
        Polyline2D polyline = (Polyline2D) obj;

        if (vertices.size()!=polyline.vertices.size())
            return false;
        for (int i = 0; i<vertices.size(); i++)
            if (!(vertices.get(i)).almostEquals(polyline.vertices.get(i), eps))
                return false;
        return true;
    }

    // ===================================================================
    // Methods inherited from the Object Class

    @Override
    public boolean equals(Object object) {
    	if (this==object)
    		return true;
        if (!(object instanceof Polyline2D))
            return false;
        Polyline2D polyline = (Polyline2D) object;

        if (vertices.size()!=polyline.vertices.size())
            return false;
        for (int i = 0; i<vertices.size(); i++)
            if (!(vertices.get(i)).equals(polyline.vertices.get(i)))
                return false;
        return true;
    }
    
    @Override
    public Polyline2D clone() {
        ArrayList<Point2D> array = new ArrayList<Point2D>(vertices.size());
        for(Point2D point : vertices)
            array.add(point.clone());
        return new Polyline2D(array);
    }

}
