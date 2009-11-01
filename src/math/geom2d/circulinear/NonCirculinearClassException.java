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
public class NonCirculinearClassException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Object object;
	
	public NonCirculinearClassException(Object obj) {
		this.object = obj;
	}
	
	public Object getObject() {
		return object;
	}
}
