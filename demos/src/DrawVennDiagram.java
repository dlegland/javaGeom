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
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.curve.PolyCurve2D;
import math.geom2d.line.StraightLine2D;

import static java.lang.Math.*;

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
		StraightLine2D lineX = StraightLine2D.createHorizontal(origin);
		StraightLine2D lineY = StraightLine2D.createVertical(origin);

		// center circle
		Circle2D c0 = new Circle2D(x0, y0, r0);
		
		// intersection points of main circle with diagonals
		double tc0 = PI / 4;
		double tc1 = PI / 2 + PI / 4;
		double tc2 = PI + PI / 4;
		double tc3 = 3 * PI / 2 + PI / 4;
		Point2D pc0 = c0.point(tc0);
		Point2D pc1 = c0.point(tc1);
		Point2D pc2 = c0.point(tc2);
		Point2D pc3 = c0.point(tc3);
		
		// compute right circle
		StraightLine2D tangentC0 = new StraightLine2D(pc0, c0.tangent(tc0));
		StraightLine2D tangentC1 = new StraightLine2D(pc1, c0.tangent(tc1));
		StraightLine2D tangentC2 = new StraightLine2D(pc2, c0.tangent(tc2));
		StraightLine2D tangentC3 = new StraightLine2D(pc3, c0.tangent(tc3));
		
		// center of first circle of curve number 'd'
		Point2D pcd0 = tangentC0.intersection(tangentC3);
		Point2D pcd1 = tangentC1.intersection(tangentC0);
		Point2D pcd2 = tangentC2.intersection(tangentC1);
		Point2D pcd3 = tangentC3.intersection(tangentC2);
		
		// circles of curve D
		Circle2D cd0 = new Circle2D(pcd0, pcd0.distance(pc0));
		Circle2D cd1 = new Circle2D(pcd1, pcd1.distance(pc1));
		Circle2D cd2 = new Circle2D(pcd2, pcd2.distance(pc2));
		Circle2D cd3 = new Circle2D(pcd3, pcd3.distance(pc3));
		
		// circle arcs
		CircleArc2D cad0 = new CircleArc2D(cd0, 
				cd0.position(pc3), cd0.position(pc0), true);
		CircleArc2D cad1 = new CircleArc2D(cd1, 
				cd1.position(pc0), cd1.position(pc1), false);
		CircleArc2D cad2 = new CircleArc2D(cd2, 
				cd2.position(pc1), cd2.position(pc2), true);
		CircleArc2D cad3 = new CircleArc2D(cd3, 
				cd3.position(pc2), cd3.position(pc3), false);
		
		// Create curve d
		PolyCurve2D<CircleArc2D> cd = 
			new PolyCurve2D<CircleArc2D>(cad0, cad1, cad2, cad3);
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
		for (int i = 0; i < n; i++) {
			positions[i] = (2 * i + 1) * PI / n;
			points[i] = c0.point(positions[i]);
			tangents[i] = new StraightLine2D(points[i],
					c0.tangent(positions[i]));
	}
		
		// circle centers
		Point2D[] centers 	= new Point2D[n];
		Circle2D[] circles 	= new Circle2D[n];
		CircleArc2D[] arcs 	= new CircleArc2D[n];
		for(int i=0; i<n; i++){
			int j = (i-1+n) % n;
			centers[i] 	= tangents[i].intersection(tangents[j]);
			circles[i] 	= new Circle2D(
					centers[i], 
					centers[i].distance(points[i]));
			arcs[i]	= new CircleArc2D(
					circles[i], 
					circles[i].position(points[j]), 
					circles[i].position(points[i]), 
					i % 2 == 0);
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
