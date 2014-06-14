/* file : CheckJarvisMarchSedgewick.java
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

package math.geom2d.point;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import math.geom2d.*;
import math.geom2d.line.LineSegment2D;

/**
 * Demo of nearest neighbor search using a kd-tree in 2 dimensions.
 * @author dlegland
 *
 */
public class NearestNeighborKDTree2DRandomDemo extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	private final static int nbPoints = 100;
	
	ArrayList<Point2D> points;
	ArrayList<Point2D> probes;
    KDTree2D tree;
	
	public NearestNeighborKDTree2DRandomDemo() {
		super();
		
		// Point coordinate are multiplied by 10 for better drawing
		points = new ArrayList<Point2D>(nbPoints);
		for (int i = 0; i < nbPoints; i++) {
		    points.add(new Point2D(
		    		Math.random() * 300 + 50,
					Math.random() * 300 + 50));
		}
		
		tree = new KDTree2D(points);
		
		probes = new ArrayList<Point2D>(9);
		for (double x = 100; x <= 300; x += 100)
			for (double y = 100; y <= 300; y += 100)
				probes.add(new Point2D(x, y));

        this.setBackground(Color.WHITE);
	}
	
	public void paintComponent(Graphics g){
	    super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		drawTree(g2, tree.getRoot(), 0, 50, 350, 50, 350);
		
		g2.setColor(Color.BLACK);
		new Box2D(50, 350, 50, 350).draw(g2);
        
        g2.setColor(Color.RED);
        for(Point2D point : probes){
            Point2D nearest = tree.nearestNeighbor(point);
            new LineSegment2D(point, nearest).draw(g2);
            point.draw(g2, 2.5);
        }
	}

	private void drawTree(Graphics2D g2, KDTree2D.Node node, int step,
	        double xmin, double xmax, double ymin, double ymax) {
	    if(node==null)
	        return;
	    
	    int dir = step%2;
	    Point2D point = node.getPoint();
        double x = point.x();
        double y = point.y();
        
	    if(dir==0){
	        // Draw vertical line
	        g2.setColor(Color.BLUE);
            new LineSegment2D(x, ymin, x, ymax).draw(g2);
	        
	        // reduce x range for each sub tree
	        drawTree(g2, node.getLeftChild(), step+1, xmin, x, ymin, ymax);
	        drawTree(g2, node.getRightChild(), step+1, x, xmax, ymin, ymax);
	    } else {
	        // Draw horizontal line
	        g2.setColor(Color.BLUE);
	        new LineSegment2D(xmin, y, xmax, y).draw(g2);
	        
	        // reduce y range for each sub tree
	        drawTree(g2, node.getLeftChild(), step+1, xmin, xmax, ymin, y);
	        drawTree(g2, node.getRightChild(), step+1, xmin, xmax, y, ymax);
	    }
	    
	    g2.setColor(Color.BLACK);
        point.draw(g2, 2.5);
	}

	public final static void main(String[] args){
		JPanel panel = new NearestNeighborKDTree2DRandomDemo();
		JFrame frame = new JFrame("Draw KD Tree Demo");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);
	}
}
