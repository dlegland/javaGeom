# javaGeom
JavaGeom is a geometry library for Java. 
It provides a general framework for creating, manipulating, and combining geometric primitives. Among the features:
* computation of intersection points between shapes
* composition create new shapes, 
* measures on shapes (area, perimeter...)

The project was formerly hosted on SourceForge, but is now hosted on GitHub: https://github.com/dlegland/javaGeom.

Some examples of use are in the "demo" directory.

## Sample application

Here is the content of the "JavaGeomDemo" application, that can be found in the demo directory:

    import java.awt.Graphics;
    import java.awt.Graphics2D;
    import java.util.Collection;
      
    import javax.swing.JFrame;
    import javax.swing.JPanel;
    
    import math.geom2d.AffineTransform2D;
    import math.geom2d.Box2D;
    import math.geom2d.Point2D;
    import math.geom2d.conic.Circle2D;
    import math.geom2d.line.LineSegment2D;
    import math.geom2d.line.StraightLine2D;
    
    public class JavaGeomDemo extends JPanel{
    	
      public JavaGeomDemo() {}
  	
      public void paintComponent(Graphics g){
          Graphics2D g2 = (Graphics2D) g;
  
          // Create some points
          Point2D p1 = new Point2D(40, 30);
          Point2D p2 = new Point2D(180, 100);
  				
          // Draw the two points, using a radius of 2 pixels.
          p1.draw(g2, 2);
          p2.draw(g2, 2);
		  
          // Create and draw a circle
          Circle2D circle1 = new Circle2D(80, 120, 40);
          circle1.draw(g2);
		
          // Create a line
          StraightLine2D line1 = new StraightLine2D(p1, p2);
  		
          // Draw the line, after clipping
          Box2D box = new Box2D(0, 200, 0, 200);
          line1.clip(box).draw(g2);
	  	
          Point2D p3 = new Point2D(20, 120);
          Point2D p4 = new Point2D(40, 140);
		  
          // Create line segment
          LineSegment2D  edge  = new LineSegment2D(p3, p4);
          edge.draw(g2);
  		
          // Compute a median line, and draw it
          StraightLine2D line2 = StraightLine2D.createMedian(p3, p4);
          p4.draw(g2, 2);
          p4.draw(g2, 2);
          line2.clip(box).draw(g2);
  
          // Compute intersection between 2 lines
          Point2D intLine = line2.intersection(line1);
          intLine.draw(g2, 2);
      
          // Compute intersections between a circle and lines
          Collection<Point2D> intersects = circle1.intersections(line2);
          for(Point2D point : intersects)
              point.draw(g2, 2);
      
          // Create some affine transforms
          AffineTransform2D sca = AffineTransform2D.createScaling(p4, 2, .8);
          AffineTransform2D tra = AffineTransform2D.createTranslation(30, 40);
          AffineTransform2D rot = AffineTransform2D.createRotation(p4, Math.PI/2);
      
          // Display the transformed shapes.
          circle1.transform(sca).draw(g2);
          circle1.transform(tra).draw(g2);
          line2.transform(rot).clip(box).draw(g2);
      }
  
      public final static void main(String[] args){
          JPanel panel = new JavaGeomDemo();
          JFrame frame = new JFrame("JavaGeom Demo");
          frame.setContentPane(panel);
          frame.setSize(250, 250);
          frame.setVisible(true);
      }
    }
