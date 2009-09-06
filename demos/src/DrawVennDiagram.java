/* file : DrawVennDiagram.java
 * 
 * Project : Euclide
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
 * Created on 1 avr. 2007
 *
 */

import java.awt.*;
import javax.swing.*;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.curve.PolyCurve2D;
import math.geom2d.line.StraightLine2D;


public class DrawVennDiagram extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	double x0 = 300;
	double y0 = 200;
	double r0 = 100;
	
	public DrawVennDiagram() {
		super();
	}
	
	public void paintComponent(Graphics g){
		// origin
		Point2D origin = new Point2D(x0, y0);
		
		// Orthogonal lines
		StraightLine2D lineX = new StraightLine2D(origin, new Vector2D(1, 0));
		StraightLine2D lineY = new StraightLine2D(origin, new Vector2D(0, 1));

		// center circle
		Circle2D c0 = new Circle2D(x0, y0, r0);
		
		// intersection points of main circle with diagonals
		double tc0  = Math.PI/4;
		double tc1  = Math.PI/2+Math.PI/4;
		double tc2  = Math.PI+Math.PI/4;
		double tc3  = 3*Math.PI/2+Math.PI/4;
		Point2D pc0 = c0.getPoint(tc0);
		Point2D pc1 = c0.getPoint(tc1);
		Point2D pc2 = c0.getPoint(tc2);
		Point2D pc3 = c0.getPoint(tc3);
		
		// compute right circle
		StraightLine2D tangentC0 = new StraightLine2D(pc0, c0.getTangent(tc0));
		StraightLine2D tangentC1 = new StraightLine2D(pc1, c0.getTangent(tc1));
		StraightLine2D tangentC2 = new StraightLine2D(pc2, c0.getTangent(tc2));
		StraightLine2D tangentC3 = new StraightLine2D(pc3, c0.getTangent(tc3));
		
		// center of first circle of curve number 'd'
		Point2D pcd0 = tangentC0.getIntersection(tangentC3);
		Point2D pcd1 = tangentC1.getIntersection(tangentC0);
		Point2D pcd2 = tangentC2.getIntersection(tangentC1);
		Point2D pcd3 = tangentC3.getIntersection(tangentC2);
		
		// circles of curve D
		Circle2D cd0 = new Circle2D(pcd0, pcd0.getDistance(pc0));
		Circle2D cd1 = new Circle2D(pcd1, pcd1.getDistance(pc1));
		Circle2D cd2 = new Circle2D(pcd2, pcd2.getDistance(pc2));
		Circle2D cd3 = new Circle2D(pcd3, pcd3.getDistance(pc3));
		
		// circle arcs
		CircleArc2D cad0 = new CircleArc2D(cd0, 
				cd0.getPosition(pc3), cd0.getPosition(pc0), true);
		CircleArc2D cad1 = new CircleArc2D(cd1, 
				cd1.getPosition(pc0), cd1.getPosition(pc1), false);
		CircleArc2D cad2 = new CircleArc2D(cd2, 
				cd2.getPosition(pc1), cd2.getPosition(pc2), true);
		CircleArc2D cad3 = new CircleArc2D(cd3, 
				cd3.getPosition(pc2), cd3.getPosition(pc3), false);
		
		// Create curve d
		PolyCurve2D<CircleArc2D> cd = new PolyCurve2D<CircleArc2D>(
				new CircleArc2D[]{cad0, cad1, cad2, cad3});
		Graphics2D g2 = (Graphics2D) g;
		Box2D box = new Box2D(10, 590, 10, 390);
		
		
		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(1.0f));
		g2.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		lineX.clip(box).draw(g2);
		lineY.clip(box).draw(g2);
		c0.draw(g2);
		
		
		cd.draw(g2);
		
		PolyCurve2D<CircleArc2D> ce = this.createVennCurve(4);
		ce.draw(g2);
		
		PolyCurve2D<CircleArc2D> cf = this.createVennCurve(5);
		cf.draw(g2);
		
		PolyCurve2D<CircleArc2D> cg = this.createVennCurve(6);
		cg.draw(g2);
	
//		g2.draw(cad0);
//		g2.draw(cad1);
//		g2.draw(cad2);
//		g2.draw(cad3);
		
	}
	private PolyCurve2D<CircleArc2D> createVennCurve(int level){
		// center circle
		Circle2D c0 = new Circle2D(x0, y0, r0);
		
		// number of points, and circle arcs
		int n = (int) Math.pow(2, level-1);
		
		// Compute intersection points, and circle tangents
		double[] positions 	= new double[n];
		Point2D[] points 	= new Point2D[n];
		StraightLine2D[] tangents = new StraightLine2D[n];
		for(int i=0; i<n; i++){
			positions[i] = i*2*Math.PI/n + Math.PI/n;
			points[i] 	= c0.getPoint(positions[i]);
			tangents[i] = new StraightLine2D(points[i], 
					c0.getTangent(positions[i]));
		}
		
		// circle centers
		Point2D[] centers 	= new Point2D[n];
		Circle2D[] circles 	= new Circle2D[n];
		CircleArc2D[] arcs 	= new CircleArc2D[n];
		for(int i=0; i<n; i++){
			int j = (i-1+n) % n;
			centers[i] 	= tangents[i].getIntersection(tangents[j]);
			circles[i] 	= new Circle2D(
					centers[i], 
					centers[i].getDistance(points[i]));
			arcs[i] 	= new CircleArc2D(
					circles[i], 
					circles[i].getPosition(points[j]), 
					circles[i].getPosition(points[i]), 
					i%2 == 0);
		}
		
		// Create the curve
		PolyCurve2D<CircleArc2D> result = new PolyCurve2D<CircleArc2D>(arcs);
		return result;
	}

	
	public final static void main(String[] args){
		JPanel panel = new DrawVennDiagram();
		panel.setPreferredSize(new Dimension(600, 500));
		
		JFrame frame = new JFrame("Draw Venn Diagram demo");
		frame.setContentPane(panel);
		
		frame.pack();
		frame.setVisible(true);		
	}
}
