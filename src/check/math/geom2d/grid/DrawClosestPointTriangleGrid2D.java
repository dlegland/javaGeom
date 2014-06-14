package math.geom2d.grid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.LineSegment2D;

public class DrawClosestPointTriangleGrid2D  extends JPanel{

	/** */
	private static final long serialVersionUID = 1L;
	
	double x0 = 50;
	double y0 = 50;
	double s = 60;
	
	Box2D box = new Box2D(30, 310, 30, 310);
	
	Grid2D grid = new TriangleGrid2D(x0, y0, s);
	
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		// draw the box in blue
		g2.setColor(Color.BLUE);
		box.asRectangle().boundary().draw(g2);
		
		// the the edges of the grid
		g2.setColor(Color.BLACK);
		for(LineSegment2D line : grid.getEdges(box))
			line.draw(g2);

		// draw vertices of the grid, as black circles
		for(Point2D point : grid.getVertices(box))
			point.draw(g2, 3);

		Point2D[] points = new Point2D[]{
				new Point2D(x0+s/3+3*s, y0+s/3),
				new Point2D(x0+2*s/3+3*s, y0+s/3),
				new Point2D(x0+s/2+3*s, y0+2*s/3),
				new Point2D(x0+s/2+3*s, y0-s/2)
		};
		
		// draw the origin, as a red circle 
		g.setColor(Color.RED);
		new Circle2D(x0, y0, 3).draw(g2);
		
		// draw test points with edge
		g.setColor(Color.BLUE);
		for(Point2D point : points){
			LineSegment2D edge = new LineSegment2D(point,
					grid.getClosestVertex(point));
			edge.draw(g2);
			point.draw(g2, 2);
		}
	}

	public final static void main(String[] args){
		System.out.println("should draw a triangle grid");
		
		JPanel panel = new DrawClosestPointTriangleGrid2D();
		JFrame frame = new JFrame("Draw a triangle grid");
		frame.setContentPane(panel);
		frame.setSize(400, 400);
		frame.setVisible(true);
		
	}}
