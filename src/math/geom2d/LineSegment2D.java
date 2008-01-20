/* File LineSegment2D.java 
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
package math.geom2d;

// Imports

/**
 * Straight Edge defined by two points.
 */
public class LineSegment2D extends LineArc2D{


	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables
	
	
	// ===================================================================
	// constructors
	
	/** Define a new Edge with two extremities. */
	public LineSegment2D(java.awt.geom.Point2D point1, java.awt.geom.Point2D point2){
		this(point1.getX(), point1.getY(), point2.getX(), point2.getY());
	}
	
	/** Define a new Edge with two extremities. */
	public LineSegment2D(double x1, double y1, double x2, double y2){
		super(x1, y1, x2-x1, y2-y1, 0, 1);
	}
	
	
	// ===================================================================
	// static methods
	
	public final static StraightLine2D getMedian(LineSegment2D edge){
		return new StraightLine2D(edge.x0+edge.dx*.5, edge.y0+edge.dy*.5, -edge.dy, edge.dx);
	}

	/** 
	 * Returns angle between two edges sharing one vertex.
	 */
	public final static double getEdgeAngle(LineSegment2D edge1, LineSegment2D edge2){
		double x0, y0, x1, y1, x2, y2;
		
		if(Math.abs(edge1.x0-edge2.x0)<Shape2D.ACCURACY && 
		   Math.abs(edge1.y0-edge2.y0)<Shape2D.ACCURACY){
			x0 = edge1.x0; y0=edge1.y0;
			x1 = edge1.x0 + edge1.dx; y1 = edge1.y0 + edge1.dy;
			x2 = edge2.x0 + edge2.dx; y2 = edge2.y0 + edge2.dy;
		}
		else if(Math.abs(edge1.x0+edge1.dx-edge2.x0)<Shape2D.ACCURACY && 
		   Math.abs(edge1.y0+edge1.dy-edge2.y0)<Shape2D.ACCURACY){
			x0 = edge1.x0 + edge1.dx; y0 = edge1.y0 + edge1.dy;
			x1 = edge1.x0; y1 = edge1.y0;
			x2 = edge2.x0 + edge2.dx; y2 = edge2.y0 + edge2.dy;
		}
		else if(Math.abs(edge1.x0+edge1.dx-edge2.x0-edge2.dx)<Shape2D.ACCURACY && 
		   Math.abs(edge1.y0+edge1.dy-edge2.y0-edge2.dy)<Shape2D.ACCURACY){
			x0 = edge1.x0 + edge1.dx; y0 = edge1.y0 + edge1.dy;
			x1 = edge1.x0; y1 = edge1.y0;
			x2 = edge2.x0; y2 = edge2.y0;
		}
		else if(Math.abs(edge1.x0-edge2.x0-edge2.dx)<Shape2D.ACCURACY && 
		   Math.abs(edge1.y0-edge2.y0-edge2.dy)<Shape2D.ACCURACY){
			x0 = edge1.x0; y0=edge1.y0;
			x1 = edge1.x0 + edge1.dx; y1 = edge1.y0 + edge1.dy;
			x2 = edge2.x0; y2 = edge2.y0;
		}
		else {// no common vertex -> return NaN
			return Double.NaN;
		}
		
		return Angle2D.getAngle(
			new StraightLine2D(x0, y0, x1-x0, y1-y0),
			new StraightLine2D(x0, y0, x2-x0, y2-y0));
	}

//	// ===================================================================
//	// accessors
//	
//	/** Always returns true, because an edge is bounded.*/
//	public boolean isBounded(){return true;}
//
//	public boolean equals(Object obj){
//		if(!(obj instanceof LineSegment2D)) return false;
//		return equals((LineSegment2D)obj);
//	}
//	
//	/**
//	 * Compare two edges, and returns true if they have the two same vertices.
//	 * @param edge : the edge to compare to.
//	 * @return true if extremities of both edges are the same.
//	 */
//	public boolean equals(LineSegment2D edge){
//		if(edge.x0==x0){
//			return(edge.y0==y0  && edge.dx==dx && edge.dy==dy);
//		}else if(edge.x0+edge.dx==x0){
//			return(edge.y0+edge.dy==y0 && edge.dx==-dx && edge.dy==-dy);
//		}
//		return false;
//	}
//	
//	/**
//	 * Get the distance of the point (x, y) to this edge.
//	 */
//	public double getDistance(Point2D p){
//		return getDistance(p.getX(), p.getY());
//	}
//	
//	/**
//	 * Get the distance of the point (x, y) to this edge.
//	 */
//	public double getDistance(double x, double y){
//		Point2D proj = super.getProjectedPoint(x, y);
//		if(contains(proj)) return proj.distance(x, y);
//		double d1 = Math.sqrt((x0-x)*(x0-x) + (y0-y)*(y0-y));
//		double d2 = Math.sqrt((x0+dx-x)*(x0+dx-x) + (y0+dy-y)*(y0+dy-y));
//		return Math.min(d1, d2);
//	}
//
//	public Shape2D getClippedShape(java.awt.geom.Rectangle2D rect){
//		return this;
//	}
//
//	/** 
//	 * Returns the length of the edge.
//	 */
//	public double getLength(){
//		return Math.sqrt(dx*dx + dy*dy);
//	}
//	
//	/**
//	 * Return the first point of the edge.
//	 * @return
//	 */
//	public Point2D getPoint1(){
//		return new Point2D(x0, y0);
//	}
//	
//	/**
//	 * Return the last point of the edge.
//	 * @return
//	 */
//	public Point2D getPoint2(){
//		return new Point2D(x0+dx, y0+dy);
//	}
//	
//	public double getX1(){
//		return x0;
//	}	
//	
//	public double getY1(){
//		return y0;
//	}
//	
//	public double getX2(){
//		return x0+dx;
//	}
//	
//	public double getY2(){
//		return y0+dy;
//	}
//	
//	
	/**
	 * Return the opposite vertex of the edge.
	 * @param point : one of the vertices of the edge
	 * @return the other vertex
	 */
	public Point2D getOtherPoint(Point2D point){
		if(point.equals(new Point2D(x0, y0))) return new Point2D(x0+dx, y0+dy);
		if(point.equals(new Point2D(x0+dx, y0+dy))) return new Point2D(x0, y0);
		return null;
	}
	
	public void setLineSegment(Point2D p1, Point2D p2){
		this.x0 = p1.getX();
		this.y0 = p1.getY();
		this.dx = p2.getX()-this.x0;
		this.dy = p2.getY()-this.y0;
	}

	public void setLineSegment(double x1, double y1, double x2, double y2){
		this.x0 = x1;
		this.y0 = y1;
		this.dx = x2-x1;
		this.dy = y2-y1;
	}
	
	/**
	 * Returns the LineSegment which start from last point of this line
	 * segment, and which ends at the fist point of this last segment.
	 */
	public LineSegment2D getReverseCurve(){
		return new LineSegment2D(x0+dx, y0+dy, x0, y0);
	}

//
//	public double getViewAngle(Point2D point){
//		double angle1 = StraightObject2D.getHorizontalAngle(point.getX(), point.getY(), x0, y0);
//		double angle2 = StraightObject2D.getHorizontalAngle(point.getX(), point.getY(), x0+dx, y0+dy);
//		
//		if(super.isPositivelyOriented(point)){
//			if(angle2>angle1) return angle2 - angle1;
//			else return 2*Math.PI - angle1 + angle2;
//		}else{
//			if(angle2>angle1) return angle2 - angle1 - 2*Math.PI;
//			else return angle2 - angle1;
//		}
//	}
//
//
//
//	
//	/** 
//	 * Returns the parameter of the first point of the edge, arbitrarly set to 0.
//	 */
//	public double getT0(){
//		return 0.0;
//	}
//
//	/** 
//	 * Returns the parameter of the last point of the edge, arbitraly set to 1.
//	 */
//	public double getT1(){
//		return 1.0;
//	}
//
//
//	public Point2D getPoint(double t){
//		if(t<0 || t>1) return null;
//		return new Point2D(x0 + dx*t, y0+dy*t);
//	}
//
//	public Point2D getPoint(double t, Point2D point){
//		if(point==null) point = new Point2D();
//		if(t<0 || t>1) point.setLocation(Double.NaN, Double.NaN);
//		else point.setLocation(x0 + dx*t, y0 + dy*t);
//		return point;
//	}
//
//	/**
//	 * Gets position of the point on the edge. If point belongs to the edge, 
//	 * this position is defined by the ratio :<p>
//	 * <code> t = (xp - x0)/dx <\code>, or equivalently :<p>
//	 * <code> t = (yp - y0)/dy <\code>.<p>
//	 * If point does not belong to edge, return Double.NaN. The current implementation 
//	 * uses the direction with the biggest derivative, in order to avoid divisions 
//	 * by zero.
//	 */
//	public double getPosition(Point2D point){
//		if(!contains(point)) return Double.NaN;
//		if(Math.abs(dx)>Math.abs(dy))
//			return (point.getX()-x0)/dx;
//		else
//			return (point.getY()-y0)/dy;
//	}
//	
	/** 
	 * Return the median of the edge, that is the locus of points located at equal
	 * distance of each vertex.
	 */
	public StraightLine2D getMedian(){
		// initial point is the middle of the edge -> x = x0+.5*dx
		// direction vector is the initial direction vector rotated by pi/2.
		return new StraightLine2D(x0+dx*.5, y0+dy*.5, -dy, dx);
	}
	
	@Override
	public LineSegment2D transform(AffineTransform2D trans){
		double[] tab = trans.getCoefficients();
		double x1 = x0*tab[0] + y0*tab[1] + tab[2];
		double y1 = x0*tab[3] + y0*tab[4] + tab[5];
		double x2 = (x0+dx)*tab[0] + (y0+dy)*tab[1] + tab[2];
		double y2 = (x0+dx)*tab[3] + (y0+dy)*tab[4] + tab[5];
		return new LineSegment2D(x1, y1, x2, y2);
	}
	
//		
//	// ===================================================================
//	// mutators
//
//
//	public void setPoint1(Point2D point){
//		dx = (x0 + dx) - point.getX();
//		dy = (y0 + dy) - point.getY();
//		x0 = point.getX();
//		y0 = point.getY();
//	}
//	
//	public void setPoint2(Point2D point){
//		dx = point.getX()-x0;
//		dy = point.getY()-y0;
//	}
//
//	// ===================================================================
//	// general methods
//
//	/** 
//	 * Return true if the point (x, y) lies on the line, with precision given 
//	 * by Shape2D.ACCURACY.
//	 */
//	public boolean contains(double x, double y){
//		boolean b = super.contains(x, y);
//		double t;
//		if(Math.abs(dx)>Math.abs(dy)) t = (x-x0)/dx;
//		else t = (y-y0)/dy;
//		
//		return t>=0 && t<=1 && b;
//	}
//
//	/** Return false, because an edge cannot contain a rectangle.*/
//	public boolean contains(double x, double y, double w, double h){
//		return false;
//	}
//
//	/** 
//	 * Return true if the point p lies on the line, with precision given by 
//	 * Shape2D.ACCURACY.
//	 */
//	public boolean contains(java.awt.geom.Point2D p){
//		return contains(p.getX(), p.getY());
//	}
//
//	/**
//	 * Return bounding box of the edge.
//	 */
//	public java.awt.Rectangle getBounds(){
//		double x1 = Math.min(x0, x0+dx);
//		double x2 = Math.max(x0, x0+dx);
//		double y1 = Math.min(y0, y0+dy);
//		double y2 = Math.max(y0, y0+dy);
//		return new java.awt.Rectangle((int)x1, (int)y1, (int)(x2-x1), (int)(y2-y1));
//	}
//	
//	/**
//	 * Return null
//	 */
//	public java.awt.geom.Rectangle2D getBounds2D(){
//		double x1 = Math.min(x0, x0+dx);
//		double x2 = Math.max(x0, x0+dx);
//		double y1 = Math.min(y0, y0+dy);
//		double y2 = Math.max(y0, y0+dy);
//		return new java.awt.geom.Rectangle2D.Double(x1, y1, x2-x1, y2-y1);
//	}
//	
//	/** 
//	 * Return pathiterator for this edge.
//	 */
//	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform t){
//		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
//		path.moveTo((float)x0, (float)y0);
//		path.lineTo((float)(x0+dx), (float)(y0+dy));
//		return path.getPathIterator(t);
//	}
//
//	/** 
//	 * Return pathiterator for this edge.
//	 */
//	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform t, double flatness){
//		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
//		path.moveTo((float)x0, (float)y0);
//		path.lineTo((float)(x0+dx), (float)(y0+dy));
//		return path.getPathIterator(t, flatness);
//	}
//	
//	/**
//	 * Tests if the Line intersects the interior of a specified rectangular area.
//	 */
//	public boolean intersects(double x, double y, double w, double h){
//		return false;
//	}
//
//	/**
//	 * Tests if the Line intersects the interior of a specified rectangle2D.
//	 */
//	public boolean intersects(java.awt.geom.Rectangle2D r){
//		return false;
//	}
//
//	public Shape2D transform(AffineTransform2D trans){
//		double[] tab = trans.getCoefficients();
//		double x1 = x0*tab[0] + y0*tab[1] + tab[2];
//		double y1 = x0*tab[3] + y0*tab[4] + tab[5];
//		return new LineSegment2D(x1, y1, dx*tab[0]+dy*tab[1]+x1, dx*tab[3]+dy*tab[4]+y1);
//	}
//
//	public String toString(){
//		return Double.toString(x0).concat(new String(" ")).concat(Double.toString(y0)).concat(
//			new String(" ")).concat(Double.toString(dx)).concat(new String(" ")).concat(Double.toString(dy));
//	}
}