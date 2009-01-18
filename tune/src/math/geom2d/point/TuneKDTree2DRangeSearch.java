/* file : TuneKDTree2DRangeSearch.java
 * 
 * Project : javaGeom
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

import java.util.ArrayList;

import math.geom2d.*;

/**
 * At the moment, this class computes only the construction time.
 * @author dlegland
 *
 */
public class TuneKDTree2DRangeSearch {
	
	private final static int nPoints = 100000;
	
	private final static int nIter = 20;
	
	public final static void main(String[] args){
		System.out.println("Tune KD Tree");
		
		// Create a new Convex hull calculator
        
        long[] times = new long[nIter];
        double total = 0;
        
        KDTree2D tree;
        Box2D range = new Box2D(150, 250, 150, 250);
        
		for (int i=0; i<nIter; i++) {
		    // Generate point coordinates
		    ArrayList<Point2D> points = new ArrayList<Point2D>(nPoints);
		    for(int p=0; p<nPoints; p++)
		        points.add(new Point2D(
		                Math.random()*200+100, 
		                Math.random()*200+100));        

		    // Compute time for creating convex hull
		    tree = new KDTree2D(points);
		    long t0 = System.currentTimeMillis();
		    tree.rangeSearch(range);
            long t1 = System.currentTimeMillis();
		    
		    times[i]= t1-t0;
		    total += times[i];
		    
		    System.out.println("Elapsed time: " + times[i] + "ms");
		}
       
		System.out.println("---");
		System.out.println("Average time: " + (total/nIter) + "ms");
	}
}
