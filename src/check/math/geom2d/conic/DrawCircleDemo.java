/* file : DrawCircleDemo.java
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

import math.geom2d.conic.Circle2D;


public class DrawCircleDemo extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	Circle2D circle = new Circle2D(100, 100, 50);
	
	public DrawCircleDemo() {
		super();
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.CYAN);
		g.fillRect(30, 30, 180, 150);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLUE);
		circle.draw(g2);
	}

	public final static void main(String[] args){
		System.out.println("should draw a circle");
		
		JPanel panel = new DrawCircleDemo();
		JFrame frame = new JFrame("Draw circle demo");
		frame.setContentPane(panel);
		frame.setSize(400, 300);
		frame.setVisible(true);
		
	}
}
