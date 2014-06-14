/* file : DrawParabolaDemo.java
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

package math.geom2d.conic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.domain.GenericDomain2D;
import math.geom2d.polygon.LinearRing2D;
import math.geom2d.polygon.Polyline2D;


public class DrawParabolaDemo extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	double x0 = 150;
	double y0 = 100;
	double a  = .05;

	Parabola2D parabola = null;
	Box2D box = null;
	
	public DrawParabolaDemo() {
		super();
		
		parabola = new Parabola2D(x0, y0, a, 0);
		
		box = new Box2D(50, 250, 50, 250);
	
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
	
		ParabolaArc2D arc = new ParabolaArc2D(parabola, -30, 30);
		
		Polyline2D polyline = arc.asPolyline(10);
		
		
		g2.setColor(Color.YELLOW);
		new GenericDomain2D(new LinearRing2D(polyline.vertices())).fill(g2);

		g2.setColor(Color.BLUE);
		box.boundary().draw(g2);

		// Draw the clipped parabola
		CurveSet2D<?> clipped = parabola.clip(box);
		if (!clipped.isEmpty()){
			g2.setStroke(new BasicStroke(1.0f));
			g2.setColor(Color.RED);
			clipped.draw(g2);
			
			AffineTransform2D transfo = 
				AffineTransform2D.createRotation(x0, y0, Math.PI/2);
			Curve2D transformed = clipped.transform(transfo);
			transformed.draw(g2);
		}
		
		// Draw parabola origin
		Point2D p1 = parabola.point(0);
		p1.draw(g2, 4);
		
		g2.setStroke(new BasicStroke(2.0f));
		g2.setColor(Color.CYAN);
		parabola.domain().clip(box).fill(g2);
		g2.setColor(Color.BLUE);
		clipped.draw(g2);
	}

	public final static void main(String[] args){
		System.out.println("should draw a parabola");
		
		JPanel panel = new DrawParabolaDemo();
		JFrame frame = new JFrame("Draw parabola demo");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);
	}
}
