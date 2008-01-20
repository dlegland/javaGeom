/* file : PolarVector2D.java
 * 
 * Project : geometry
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
 * Created on 3 déc. 2006
 *
 */
package math.geom2d;

/**
 * A simple class whoich reprensents polar coordinate in 2D. 
 * First parameter rho is a displacement value, which can be negative.
 * Second parameter phi is the angle of direction og the displacement. 
 * @deprecated use Vector2D.createPolar() instead 
 * @author dlegland
 */
public class PolarVector2D extends Vector2D{

	protected double rho;
	protected double theta;
	
	public PolarVector2D() {
		this(0, 0);
	}
	
	public PolarVector2D(Point2D point){
		double x = point.getX();
		double y = point.getY();
		this.rho = Math.sqrt(x*x+y*y);
		this.theta = Angle2D.formatAngle(Math.atan2(y, x));
		this.dx = rho*Math.cos(theta);
		this.dy = rho*Math.sin(theta);
	}

	public PolarVector2D(double rho, double theta){
		this.rho = rho;
		this.theta = theta;
		this.dx = rho*Math.cos(theta);
		this.dy = rho*Math.sin(theta);
	}
	
	/**
	 * @return Returns the rho.
	 */
	public double getRho() {
		return this.rho;
	}

	/**
	 * @return Returns the theta.
	 */
	public double getTheta() {
		return this.theta;
	}

	/**
	 * @param rho The rho to set.
	 */
	public void setRho(double rho) {
		this.rho = rho;
		this.dx = rho*Math.cos(theta);
		this.dy = rho*Math.sin(theta);
	}

	/**
	 * @param theta The theta to set.
	 */
	public void setTheta(double theta) {
		this.theta = theta;
		this.dx = rho*Math.cos(theta);
		this.dy = rho*Math.sin(theta);
	}

}
