/* file : CheckAsymptotesHyperbola2D.java
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.line.StraightLine2D;

/**
 * Check the transformation of Hyperbolas by affine transforms.
 * @author dlegland
 *
 */
public class CheckAsymptotesHyperbola2D extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	double x0 = 150;
	double y0 = 150;
	double a  = 50;
	double b  = 20;
	double theta  = 0;

	Point2D origin = new Point2D(x0, y0);
	
	Hyperbola2D hyperbola = null;
	Box2D box = null;
	
	public CheckAsymptotesHyperbola2D() {
		super();
		
		hyperbola = new Hyperbola2D(x0, y0, a, b, theta);
		box = new Box2D(50, 350, 50, 350);
	}
	
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		// The rotation of the base hyperbola
		double theta = Math.PI/3;
		AffineTransform2D rot = 
			AffineTransform2D.createRotation(origin, theta);
		Hyperbola2D rotated = hyperbola.transform(rot);
		//Hyperbola2D rotated = hyperbola;

		// Draw the hyperbola and its domain
		g2.setColor(Color.CYAN);
		rotated.domain().clip(box).fill(g2);
		g2.setColor(Color.BLUE);
		rotated.clip(box).draw(g2);
		
		// extract the asymptotes, and draw them
		g2.setColor(new Color(0, 127, 0));
		for(StraightLine2D asymptote : rotated.asymptotes())
			asymptote.clip(box).draw(g2);
		
		// draw the bounding box
		g2.setColor(Color.BLACK);
		box.boundary().draw(g2);

		// Draw the origin point
		Point2D p1 = new Point2D(x0, y0);
		p1.draw(g2, 4);
	}

	public final static void main(String[] args){
		System.out.println("Hyperbola asymptotes");
		
		JPanel panel = new CheckAsymptotesHyperbola2D();
		panel.setPreferredSize(new Dimension(400, 400));
		JFrame frame = new JFrame("Check asymptotes of hyperbola");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
