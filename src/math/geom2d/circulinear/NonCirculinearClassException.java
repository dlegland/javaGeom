/**
 * File: 	NonCirculinearClassException.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 1 nov. 09
 */
package math.geom2d.circulinear;


/**
 * @author dlegland
 *
 */
public class NonCirculinearClassException extends NonCirculinearShape2DException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NonCirculinearClassException(Object obj) {
		super(obj);
	}
}
