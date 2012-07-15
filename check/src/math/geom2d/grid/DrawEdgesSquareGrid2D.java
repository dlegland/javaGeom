package math.geom2d.grid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;

public class DrawEdgesSquareGrid2D  extends JPanel{

	/** */
	private static final long serialVersionUID = 1L;
	
	double x0 = 130;
	double y0 = 185;
	
	Box2D box = new Box2D(30, 270, 30, 270);
	
	Grid2D grid = new SquareGrid2D(x0, y0, 50);
	
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLUE);
		box.asRectangle().boundary().draw(g2);
		
		g2.setColor(Color.BLACK);
		Collection<LineSegment2D> lines = grid.getEdges(box);
		for(LineSegment2D line : lines)
			line.draw(g2);

		g.setColor(Color.RED);
		new Point2D(x0, y0).draw(g2, 4);
	}

	public final static void main(String[] args){
		System.out.println("should draw a square grid");
		
		JPanel panel = new DrawEdgesSquareGrid2D();
		JFrame frame = new JFrame("Draw a square grid");
		frame.setContentPane(panel);
		frame.setSize(400, 400);
		frame.setVisible(true);
	}
}
