/* File GeomPanel.java 
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
  
// Imports

import java.awt.*;
import math.geom2d.*;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.ClosedPolyline2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Ray2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.polygon.HalfPlane2D;
import math.geom2d.polygon.Polygon2D;
import math.geom2d.transform.AffineTransform2D;

import javax.swing.JPanel;


/**
 * class GeomPanel : easy way to draw geometric objects.
 */
public class GeomPanel extends JPanel{


	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// coordinates of point on the center of window.
	double x0=0, y0=0;
	
	// ratio of pixel per unit in each direction
	double zoomX=1, zoomY=1;
	
	boolean afficheAxe = true;
	
	java.awt.geom.AffineTransform transform = new java.awt.geom.AffineTransform();
	
	// ===================================================================
	// constructors
	
	/** Main constructor */
	public GeomPanel(){
	}
	
	
	// ===================================================================
	// accessors


	// ===================================================================
	// modifiers


	// ===================================================================
	// general methods

	public void updateTransform(){	
		// compute display limits
		Dimension dim = getSize();
		double xmin = x0 - ((double)dim.width)/zoomX/2;
		double ymax = y0 + ((double)dim.height)/zoomY/2;

		//System.out.println(xmin +" " +xmax + " " + ymin + " " + ymax + 
		//	"   cx : " + centreX + " cy : "+  centreY);
		transform = new java.awt.geom.AffineTransform(
			zoomX, 0, 0, -zoomY, -xmin*zoomX, ymax*zoomY);		
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		// set antialiasing to ON
		//RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
		//	RenderingHints.VALUE_ANTIALIAS_ON);
		//g2.addRenderingHints(hints);
		
		updateTransform();
		
		LineSegment2D edge = new LineSegment2D(0, 0, 100, 100);
		g2.draw(transform.createTransformedShape(edge));
		

		AffineTransform2D trans = AffineTransform2D.createTranslation(20, 80);
//		Edge2D edge2 = (Edge2D) edge.transform(trans);
		g2.draw(transform.createTransformedShape((LineSegment2D)edge.transform(trans)));
//		g2.draw(transform.createTransformedShape(edge2));

		AffineTransform2D rot = AffineTransform2D.createRotation(Math.PI/3);
		//LineSegment2D edge3 = (LineSegment2D) edge.transform(rot);
		g2.draw(transform.createTransformedShape((LineSegment2D)edge.transform(rot)));

		Circle2D circle1 = 	new Circle2D(60, 30, 50);
		g2.setColor(Color.blue);
		
		g2.draw(transform.createTransformedShape(circle1));
		g2.draw(transform.createTransformedShape(circle1.transform(trans)));
		g2.draw(transform.createTransformedShape(circle1.transform(rot)));
		
		g2.setColor(Color.black);
		Box2D window = new Box2D(-100, 100, -100, 100);
		Ray2D line1 = new Ray2D(-10, 4, 10, 4);
		Ray2D line2 = new Ray2D(5, -10, 5, 10);
		Ray2D line3 = new Ray2D(-30, -70, 20, 10);
		Ray2D line4 = new Ray2D(110, 30, 120, 40);
		//System.out.println(line1.getClippedShape(window));
		//System.out.println(line2.getClippedShape(window));
		Shape2D shape;
		if((shape=line1.clip(window))!=null) g2.draw(transform.createTransformedShape(shape));
		if((shape=line2.clip(window))!=null) g2.draw(transform.createTransformedShape(shape));
		if((shape=line3.clip(window))!=null) g2.draw(transform.createTransformedShape(shape));
		if((shape=line4.clip(window))!=null) g2.draw(transform.createTransformedShape(shape));
		
		line1 = new Ray2D(-10, -10, 30, 20);
		HalfPlane2D hp1 = new HalfPlane2D(line1);
		shape = hp1.clip(window);
		if((shape=hp1.clip(window))!=null) g2.draw(transform.createTransformedShape(shape));
		
		g2.draw(transform.createTransformedShape(new StraightLine2D(0,0,1,0)));
		g2.draw(transform.createTransformedShape(new StraightLine2D(0,0,0,1)));
		
		double[] tx = {40, -80, -100, -50};
		double[] ty = {30, 80, -60, 30};
		Polygon2D poly = new Polygon2D(tx, ty);
		g2.setColor(Color.green);
		g2.fill(transform.createTransformedShape(poly));
		g2.fill(transform.createTransformedShape(poly.transform(rot)));
		g2.setColor(Color.red);
		g2.draw(transform.createTransformedShape(new ClosedPolyline2D(tx, ty)));
		
		//g2.setColor(Color.blue);
		//g2.fill(transform.createTransformedShape(new Rectangle2D(-150, 30, 100, 50)));
		//g2.fill(transform.createTransformedShape(new Rectangle2D(-100, -100, 100, 50, Math.PI/3)));
	}
}