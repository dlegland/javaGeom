/* file : DrawClippedEllipseDemo.java
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

import java.awt.*;

import javax.swing.*;

import math.geom2d.*;
import math.geom2d.conic.Ellipse2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Boundaries2D;


public class DrawClippedEllipseDemo extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	Ellipse2D ellipse = null;
	Box2D box = null;
	
	public DrawClippedEllipseDemo() {
		super();
		
		double x0 	= 200;
		double y0 	= 150;
		double r1  	= 150;
		double r2  	= 50;
		double theta = 0;
		ellipse = new Ellipse2D(x0, y0, r1, r2, theta);
		
		box = new Box2D(100, 300, 50, 250);
	
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
	
		g2.setColor(Color.YELLOW);
		ellipse.domain().fill(g2);
		
		g2.setColor(Color.BLUE);
		ellipse.draw(g2);

		g2.setColor(Color.BLUE);
		box.boundary().draw(g2);

		Curve2D clipped = ellipse.clip(box);
		g2.setStroke(new BasicStroke(4.0f));
		g2.setColor(Color.RED);
		clipped.draw(g2);

		Boundary2D boundary = Boundaries2D.clipBoundary(ellipse, box);
		g2.setStroke(new BasicStroke(2.0f));
		g2.setColor(Color.CYAN);
		boundary.domain().fill(g2);
		g2.setColor(Color.BLUE);
		boundary.draw(g2);
	}

	public final static void main(String[] args){
		System.out.println("should draw a clipped ellipse");
		
		JPanel panel = new DrawClippedEllipseDemo();
		JFrame frame = new JFrame("Draw clipped ellipse demo");
		frame.setContentPane(panel);
		frame.setSize(400, 300);
		frame.setVisible(true);
		
	}
}
