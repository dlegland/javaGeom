/* file : DrawCircularShapeDemo.java
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

package math.geom2d.domain;

import java.awt.*;

import javax.swing.*;

import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.domain.Domain2D;


public class DrawCircularShapeDemo extends JPanel{

	private static final long serialVersionUID = 1L;
	
	double xl = 100;
	double xc = 200;
	double xu = 300;
	
	double yl = 100;
	double yc = 200;
	double yu = 300;

	double r = 50;
	double r2 = 25;
	
	
	Domain2D domain = null;
	
	
	public DrawCircularShapeDemo() {
		super();
		
		// angular extent of external arcs
		double ext1 = 3*Math.PI/2;
		
		// angular extent of internal arcs
		double ext2 = -Math.PI;
		
		// build the external arcs
		CircleArc2D arcll = new CircleArc2D(xl, yl, r, Math.PI/2, ext1);
		CircleArc2D arcul = new CircleArc2D(xu, yl, r, Math.PI, ext1);
		CircleArc2D arcuu = new CircleArc2D(xu, yu, r, 3*Math.PI/2, ext1);
		CircleArc2D arclu = new CircleArc2D(xl, yu, r, 0, ext1);
		
		// build the internal arcs
		CircleArc2D arccl = new CircleArc2D(xc, yl, r, Math.PI, ext2);
		CircleArc2D arcuc = new CircleArc2D(xu, yc, r, 3*Math.PI/2, ext2);
		CircleArc2D arccu = new CircleArc2D(xc, yu, r, 0, ext2);
		CircleArc2D arclc = new CircleArc2D(xl, yc, r, Math.PI/2, ext2);
		
		// Build the main boundary curve
		Contour2D boundary0 = new BoundaryPolyCurve2D<CircleArc2D>(
				new CircleArc2D[]{
						arcll, arccl, arcul, arcuc,
						arcuu, arccu, arclu, arclc});
		
		// Build the inner boundaries
		Contour2D boundaryll = new Circle2D(xu, yu, r2, false);
		Contour2D boundarylu = new Circle2D(xl, yu, r2, false);
		Contour2D boundaryuu = new Circle2D(xl, yl, r2, false);
		Contour2D boundaryul = new Circle2D(xu, yl, r2, false);
		Contour2D boundarycc = new Circle2D(xc, yc, r2, false);
		
		
		// Gather the different boundaries
		Boundary2D boundary = new ContourArray2D<Contour2D>(
			new Contour2D[]{
					boundary0, 
					boundarycc,
					boundaryll, boundarylu, boundaryuu, boundaryul}
		);
		
		// Build the domain
		domain = new GenericDomain2D(boundary);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.CYAN);
		domain.fill(g2);
		
		g2.setColor(Color.BLUE);
		domain.boundary().draw(g2);
	}

	public final static void main(String[] args){
		JPanel panel = new DrawCircularShapeDemo();
		JFrame frame = new JFrame("Draw a circular shape");
		panel.setPreferredSize(new Dimension(400, 400));
		frame.setContentPane(panel);
		frame.setVisible(true);
		frame.pack();
	}
}
