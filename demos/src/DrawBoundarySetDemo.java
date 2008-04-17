/* file : DrawBoundarySetDemo.java
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

import math.geom2d.*;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.curve.BoundaryPolyCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveUtil;


public class DrawBoundarySetDemo extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	Curve2D curve = null;
	
	public DrawBoundarySetDemo() {
		super();
		
		double x0 = 100;
		double y0 = 100;
		double r  = 50;
		CircleArc2D arc1 = new CircleArc2D(x0, y0, r, 5*Math.PI/3, 2*Math.PI/3);
		CircleArc2D arc2 = new CircleArc2D(x0+r, y0, r, 2*Math.PI/3, 2*Math.PI/3);
		
		BoundaryPolyCurve2D<CircleArc2D> set = new BoundaryPolyCurve2D<CircleArc2D>();
		set.addCurve(arc1);
		set.addCurve(arc2);
		
		Box2D box = new Box2D(0, 400, 0, 400);
				
		curve = CurveUtil.clipBoundary(set, box);
		System.out.println(curve);
		
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.CYAN);
		g2.fillRect(30, 30, 180, 150);
		
		g2.setColor(Color.YELLOW);
		g2.fill(curve);
		
		g2.setColor(Color.BLUE);
		g2.draw(curve);
	}

	public final static void main(String[] args){
		System.out.println("should draw a circle");
		
		JPanel panel = new DrawBoundarySetDemo();
		JFrame frame = new JFrame("Draw circle demo");
		frame.setContentPane(panel);
		frame.setSize(400, 300);
		frame.setVisible(true);
		
	}
}
