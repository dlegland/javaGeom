/* File LineObject2D.java 
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

import java.util.ArrayList;
import java.util.Collection;

// Imports

/**
 * Straight Object defined from 2 points. This object keep points reference in memory,
 * then changing location of point obtained with <code> getPoint2() </code> or
 * <code> getPoint2() </code> will change properties of line.<p>
 * Moreover, type of object can change if one or both of the ending points are set
 * to <code> null</code>. It is then an easy way represent Straight Lines, Edges or 
 * Rays in the same class. If both points exist, object is like an Edge2D. If one 
 * only the two points is set to <code> null </code>, it is like a Ray2D, with
 * orientation depending one the missing point. If the two Points are set to <code> 
 * null </code>, then the object is like a StraightLine2D. <p>
 * <p>
 * Example :<p>
 * <code>
 * // Create an Edge2D<br>
 * LineObject2D line = new LineObject2D(new Point2D(0, 0), new Point2D(1, 2));<br>
 * // Change direction of line, by changing second point :<br>
 * line.getPoint2().setLocation(4, 5);<br>
 * // Change position and direction of the line, by changing second point. 'line' is<br>
 * // now the edge (2,3)-(4,5)<br>
 * line.setPoint1(new Point2D(2, 3));<br>
 * // Transform into Ray2D :<br>
 * line.setPoint1(null);<br>
 * // Tranform into Line2D, going throug (2,3) and (4,5) :<br>
 * line.setPoint2(null);<br>
 * </code><p><p>
 * This class is maybe slower than Edge2D or StraightLine2D, because parameters are
 * updated each time a computation is made, causing lot of additional processing.
 */
public class LineObject2D extends StraightObject2D implements SmoothCurve2D, ContinuousOrientedCurve2D{


	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables
	
	private Point2D point1=null;
	private Point2D point2=null;
	
	
	// ===================================================================
	// constructors
	
	/** Define a new Edge with two extremities. */
	public LineObject2D(Point2D point1, Point2D point2){
		x0 = point1.getX();
		y0 = point1.getY();
		dx = point2.getX()-point1.getX();
		dy = point2.getY()-point1.getY();
		this.point1 = point1;
		this.point2 = point2;
	}
	
	/** Define a new Edge with two extremities. */
	public LineObject2D(double x1, double y1, double x2, double y2){
		x0 = x1;
		y0 = y1;
		dx = x2-x1;
		dy = y2-y1;
		point1 = new Point2D(x1, y1);
		point2 = new Point2D(x2, y2);
	}
	
	
	/**
	 * Recompute (x0,y0) and (dx,dy) from position of points. If point1 is set to
	 * null, recompute only (dx,dy). If point2 is set to null, recompute only (x0,y0).
	 * If both points are set to null , recompute nothing.
	 */
	private void updateParameters(){
		if(point1!=null){
			x0 = point1.getX();
			y0 = point1.getY();
		}
		if(point2!=null){
			dx = point2.getX()-x0;
			dy = point2.getY()-y0;
		}
	}
	

	// ===================================================================
	// accessors
	
	/** 
	 * return true only if both <code> point1</code> and <code>point2</code>
	 * are not set to null. If one of the two points is null, it is a Ray.
	 * If both points are set to null, it is a Straight Line.
	 */
	public boolean isBounded(){
		return point1!=null && point2!=null;
	}

	public boolean isColinear(StraightObject2D line){		
		updateParameters();
		return super.isColinear(line);
	}

	/**
	 * Test if the this object is parallel to the given one. This method is overloaded
	 * to update parameters before computation.
	 */
	public boolean isParallel(StraightObject2D line){		
		updateParameters();
		return super.isParallel(line);
	}

	public boolean equals(Object obj){
		if(!(obj instanceof LineObject2D)) return false;
		return equals((LineObject2D)obj);
	}
	
	/**
	 * Two LineObject2D are equals if the share the two same points, in the same order.
	 * @param edge : the edge to compare to.
	 * @return true if extremities of both edges are the same.
	 */
	public boolean equals(LineObject2D edge){
		return point1==edge.point1 && point2==edge.point2;
	}
	
	/**
	 * Get the distance of the point (x, y) to this edge.
	 */
	public double getDistance(java.awt.geom.Point2D p){
		return getDistance(p.getX(), p.getY());
	}
	
	/**
	 * Get the distance of the point (x, y) to this edge.
	 */
	public double getDistance(double x, double y){
		updateParameters();
		Point2D proj = super.getProjectedPoint(x, y);
		if(contains(proj)) return proj.distance(x, y);
		double d1=Double.POSITIVE_INFINITY;
		double d2=Double.POSITIVE_INFINITY;
		if(point1!=null) d1 = Math.sqrt((x0-x)*(x0-x) + (y0-y)*(y0-y));
		if(point2!=null) d2 = Math.sqrt((x0+dx-x)*(x0+dx-x) + (y0+dy-y)*(y0+dy-y));
		//System.out.println("dist lineObject2D : " + Math.min(d1, d2));
		return Math.min(d1, d2);
	}

	public double getSignedDistance(java.awt.geom.Point2D p){
		return getSignedDistance(p.getX(), p.getY());
	}

	public double getSignedDistance(double x, double y){
		updateParameters();
		return super.getSignedDistance(x, y);
	}


	public boolean isPositivelyOriented(Point2D point){
		return this.getSignedDistance(point.getX(), point.getY())<0;
	}

	public double[][] getParametric(){
		updateParameters();
		return super.getParametric();
	}

	public double[] getCartesianEquation(){
		updateParameters();
		return super.getCartesianEquation();
	}

	public double[] getPolarCoefficients(){
		updateParameters();
		return super.getPolarCoefficients();
	}

	public double[] getSignedPolarCoefficients(){
		updateParameters();
		return super.getSignedPolarCoefficients();
	}		

	public double getHorizontalAngle(){
		updateParameters();
		return super.getHorizontalAngle();
	}

	public Point2D getProjectedPoint(Point2D p){
		updateParameters();
		return super.getProjectedPoint(p);
	}
	
	public Point2D getProjectedPoint(double x, double y){
		updateParameters();
		return super.getProjectedPoint(x, y);
	}
	
	/**
	 * Create a straight line parallel to this object, and going through the
	 * given point.
	 * @param point the point to go through
	 * @return the parallel through the point
	 */
	public StraightLine2D getParallel(Point2D point){
		updateParameters();
		return null;		
	}
	
	/**
	 * Create a straight line perpendicular to this object, and going through 
	 * the given point.
	 * @param point : the point to go through
	 * @return the perpendicular through point
	 */
	public StraightLine2D getPerpendicular(Point2D point){
		updateParameters();
		return super.getPerpendicular(point);		
	}
	
	
	
	public Shape2D getClippedShape(Box2D box){
		// get vertices of rectangle
		double x = box.getMinX();
		double y = box.getMinY();
		double w = box.getWidth();
		double h = box.getHeight();

		// case of vertical lines
		if(Math.abs(dx)<Shape2D.ACCURACY){
			if(x0>=x && x0<=x+w && y0<y+h)
				return new LineSegment2D(x0, Math.max(y, y0), x0, y+h);
			else return null;		
		}
	
		// case of horizontal lines
		if(Math.abs(dy)<Shape2D.ACCURACY){
			if(y0>=y && y0<=y+h && x0<x+w)
				return new LineSegment2D(Math.min(x, x0), y0, x+w, y0);
			else return null;
		}
		
		double t1 = (y-y0)/dy;
		double t2 = (x+w-x0)/dx;
		double t3 = (y+h-y0)/dy;
		double t4 = (x-x0)/dx;
		
		// sort the positions of the 4 points in ascending order
		double tmp;
		
		if(t1>t2){tmp=t1;t1=t2;t2=tmp;}
		if(t3>t4){tmp=t3;t3=t4;t4=tmp;}
		if(t2>t3){tmp=t2;t2=t3;t3=tmp;}
		if(t1>t2){tmp=t1;t1=t2;t2=tmp;}
		if(t3>t4){tmp=t3;t3=t4;t4=tmp;}
		if(t2>t3){tmp=t2;t2=t3;t3=tmp;}
		
		if(t2>0)
			return new LineSegment2D(getPoint(t2), getPoint(t3));
		else if(t3>0)
			return new LineSegment2D(getPoint(0), getPoint(t3));
		return null;		
	}

	/**
	 * Clip the line object by a box. The result is an instance of
	 * CurveSet2D<LineArc2D>, which 
	 * contains only instances of LineArc2D. If the line object is not
	 * clipped, the result is an instance of
	 * CurveSet2D<LineArc2D> which contains 0 curves.
	 */
	public CurveSet2D<? extends LineArc2D> clip(Box2D box) {
		// Clip the curve
		CurveSet2D<Curve2D> set = box.clipCurve(this);
		
		// Stores the result in appropriate structure
		CurveSet2D<LineArc2D> result =
			new CurveSet2D<LineArc2D> ();
		
		// convert the result
		for(Curve2D curve : set.getCurves()){
			if (curve instanceof LineArc2D)
				result.addCurve((LineArc2D) curve);
		}
		return result;
	}


	/**
	 * Return more precise bounds for the LineObject. Return an instance of HRectangle2D.
	 */
	public Box2D getBoundingBox(){
		if(point1==null || point2==null)
			return new Box2D(
					Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 
					Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		updateParameters();
		return new Box2D(x0, x0+dx, y0, y0+dy);
	}

	/** 
	 * Returns the length of the edge.
	 */
	public double getLength(){
		updateParameters();
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	/**
	 * Return the first point of the edge. It corresponds to getPoint(0).
	 * @return the first point.
	 */
	public Point2D getPoint1(){
		return point1;
	}
	
	/**
	 * Return the last point of the edge. It corresponds to getPoint(1).
	 * @return the last point.
	 */
	public Point2D getPoint2(){
		return point2;
	}
	
	public double getX1(){
		if(point1==null) return x0;
		else return point1.getX();
	}	
	
	public double getY1(){
		if(point1==null) return y0;
		else return point1.getY();
	}
	
	public double getX2(){
		updateParameters();
		return x0+dx;
	}
	
	public double getY2(){
		updateParameters();
		return y0+dy;
	}
	
	
	/**
	 * Return the opposite vertex of the edge.
	 * @param point : one of the vertices of the edge
	 * @return the other vertex
	 */
	public Point2D getOtherPoint(Point2D point){
		if(point.equals(point1)) return point2;
		if(point.equals(point2)) return point1;
		return null;
	}
	
	public Vector2D getTangent(double t){
		return new Vector2D(dx, dy);
	}


	/**
	 * returns 0 as every straight object.
	 */
	public double getCurvature(double t){
		return 0.0;
	}

	public Polyline2D getAsPolyline(int n){
		updateParameters();
		Point2D[] points = new Point2D[n+1];
		double t0 = this.getT0();
		double t1 = this.getT1();
		double dt = (t1-t0)/n;
		for(int i=0; i<n; i++)
			points[i] = this.getPoint((double)i*dt + t0);
		return new Polyline2D(points);
	}

	/** 
	 * Returns an array containing the curve itself.
	 */
	public Collection<? extends SmoothCurve2D> getSmoothPieces() {
		ArrayList<LineObject2D> list = new ArrayList<LineObject2D>(1);
		list.add(this);
		return list;
	}

	public ContinuousOrientedCurve2D[] getBoundaryCurves(){
		return new ContinuousOrientedCurve2D[]{this};
	}
	
	public double getWindingAngle(java.awt.geom.Point2D point){
		updateParameters();
		
		double angle0 = super.getHorizontalAngle();
		double angle1 = (angle0 + Math.PI) % (2*Math.PI);
		if(point1!=null)
			angle1 = Angle2D.getHorizontalAngle(point.getX(), point.getY(), x0, y0);
		
		double angle2 = angle0;
		if(point2!=null)
			angle2 = Angle2D.getHorizontalAngle(point.getX(), point.getY(), x0+dx, y0+dy);
		
		if(this.isInside(point)){
			if(angle2>angle1) return angle2 - angle1;
			else return 2*Math.PI - angle1 + angle2;
		}else{
			if(angle2>angle1) return angle2 - angle1 - 2*Math.PI;
			else return angle2 - angle1;
		}
	}

	public boolean isInside(java.awt.geom.Point2D point){
		return this.getSignedDistance(point.getX(), point.getY())<0;
	}

	/** 
	 * Returns the parameter of the first point of the line Object. It is equal to
	 * 0 in the case of edge or positive Rays, and equals -Infinity in the case of
	 * StraightLine or negative rays.
	 */
	public double getT0(){
		if(point1==null) return Double.NEGATIVE_INFINITY;
		return 0.0;
	}

	/**
	* Returns the parameter of the first point of the line Object. It is equal to
	* 1 in the case of edge or negative Rays, and equals +Infinity in the case of
	* StraightLine or positive rays.
	*/
	public double getT1(){
		if(point2==null) return Double.POSITIVE_INFINITY;
		return 1.0;
	}


	public Point2D getPoint(double t){		
		if(t<0 && point1==null) return null;
		if(t>1 && point2==null) return null;
		if(t==0 && point1!=null) return point1;
		if(t==1 && point2!=null) return point2;
		updateParameters();
		return new Point2D(x0 + dx*t, y0+dy*t);
	}

	public Point2D getPoint(double t, Point2D point){
		if(point==null) point = new Point2D();
		updateParameters();
		if((t<0 && point1==null) || (t>1 && point2==null)) 
			point.setLocation(Double.NaN, Double.NaN);
		else 
			point.setLocation(x0 + dx*t, y0 + dy*t);
		return point;
	}

	/**
	 * Get the first point of the curve. 
	 * @return the first point of the curve
	 */
	public Point2D getFirstPoint(){
		return point1;
	}
	
	/**
	 * Get the last point of the curve. 
	 * @return the last point of the curve.
	 */
	public Point2D getLastPoint(){
		return point2;
	}

	/**
	 * Gets position of the point on the line. If point belongs to the line, 
	 * this position is defined by the ratio :<p>
	 * <code> t = (xp - x0)/dx <\code>, or equivalently :<p>
	 * <code> t = (yp - y0)/dy <\code>.<p>
	 * If point does not belong to edge, return Double.NaN. The current implementation 
	 * uses the direction with the biggest derivative, in order to avoid divisions 
	 * by zero.
	 */
	public double getPosition(Point2D point){
		if(!contains(point)) return Double.NaN;
		// not useful to update, because parameters were updated in contains() method
		//updateParameters();
		if(Math.abs(dx)>Math.abs(dy))
			return (point.getX()-x0)/dx;
		else
			return (point.getY()-y0)/dy;
	}
	
	/**
	 * return the line object which starts at <code>point2</code> and ends at
	 * <code>point1</code>.
	 */
	public LineObject2D getReverseCurve(){
		return new LineObject2D(point2, point1);
	}

	public Collection<ContinuousCurve2D> getContinuousCurves() {
		ArrayList<ContinuousCurve2D> list = new ArrayList<ContinuousCurve2D>(1);
		list.add(this);
		return list;
	}

	/** Return a new LineArc2D, which is the portion of the linearc delimited
	 * by parameters t0 and t1.
	 */
	public LineArc2D getSubCurve(double t0, double t1){
		t0 = Math.max(t0, getT0());
		t1 = Math.min(t1, getT1());
		return new LineArc2D(this, t0, t1);
	}
	
	
	// ===================================================================
	// mutators


	public void setPoint1(Point2D point){
		point1 = point;
		updateParameters();
	}
	
	public void setPoint2(Point2D point){
		point2 = point;
		updateParameters();
	}

	// ===================================================================
	// general methods

	/** 
	 * Return true if the point (x, y) lies on the line, with precision given 
	 * by Shape2D.ACCURACY.
	 */
	public boolean contains(double x, double y){
		updateParameters();
		boolean b = super.contains(x, y);
		double t;
		if(Math.abs(dx)>Math.abs(dy)) t = (x-x0)/dx;
		else t = (y-y0)/dy;
		
		return t>=0 && t<=1 && b;
	}

	/** Return false, because an line cannot contain a rectangle.*/
	public boolean contains(double x, double y, double w, double h){
		return false;
	}

	/** 
	 * Return true if the point p lies on the line, with precision given by 
	 * Shape2D.ACCURACY.
	 */
	public boolean contains(java.awt.geom.Point2D p){
		return contains(p.getX(), p.getY());
	}

	/**
	 * Return bounding box of the shape.
	 */
	public java.awt.Rectangle getBounds(){
		return this.getBoundingBox().getAsAWTRectangle();
	}
	
	/**
	 * Return more precise bounds for the shape.
	 */
	public java.awt.geom.Rectangle2D getBounds2D(){
		return this.getBoundingBox().getAsAWTRectangle2D();
	}

	
	public java.awt.geom.GeneralPath getInnerPath(){
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.lineTo((float)point1.getX(), (float)point1.getX());
		path.lineTo((float)point2.getX(), (float)point2.getY());
		return path;
	}

	public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path){
		path.lineTo((float)point1.getX(), (float)point1.getX());
		path.lineTo((float)point2.getX(), (float)point2.getY());
		return path;
	}
	
	/** 
	 * Return pathiterator for this edge.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform t){
		updateParameters();
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo((float)x0, (float)y0);
		path.lineTo((float)(x0+dx), (float)(y0+dy));
		return path.getPathIterator(t);
	}

	/** 
	 * Return pathiterator for this edge.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform t, double flatness){
		updateParameters();
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo((float)x0, (float)y0);
		path.lineTo((float)(x0+dx), (float)(y0+dy));
		return path.getPathIterator(t, flatness);
	}
	
	/**
	 * Tests if the Line intersects the interior of a specified rectangular area.
	 */
	public boolean intersects(double x, double y, double w, double h){
		return false;
	}

	/**
	 * Tests if the Line intersects the interior of a specified rectangle2D.
	 */
	public boolean intersects(java.awt.geom.Rectangle2D r){
		return false;
	}

	public LineObject2D transform(AffineTransform2D trans){
		updateParameters();
		double[] tab = trans.getCoefficients();
		double x1 = x0*tab[0] + y0*tab[1] + tab[2];
		double y1 = x0*tab[3] + y0*tab[4] + tab[5];
		return new LineObject2D(x1, y1, dx*tab[0]+dy*tab[1]+x1, dx*tab[3]+dy*tab[4]+y1);
	}

	public String toString(){
		updateParameters();
		return Double.toString(x0).concat(new String(" ")).concat(Double.toString(y0)).concat(
			new String(" ")).concat(Double.toString(dx)).concat(new String(" ")).concat(Double.toString(dy));
	}
}