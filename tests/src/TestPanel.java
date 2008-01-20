// TestPanel.java
// a simple java file for a standard class


// Imports

import java.awt.event.*;
import javax.swing.*;

/**
 * class TestPanel
 */
public class TestPanel extends JFrame implements WindowListener, ActionListener{


	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables
	
	
	// ===================================================================
	// constructors
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/** Main constructor */
	public TestPanel(){
		GeomPanel panel = new GeomPanel();
		getContentPane().add(panel);
		addWindowListener(this);
	}
	
	
	// ===================================================================
	// accessors


	// ===================================================================
	// modifiers


	// ===================================================================
	// general methods


	// ===================================================================
	// display methods
	

	// ===================================================================
	// menu listener

	public void actionPerformed(ActionEvent evt){
	}
	

	// ===================================================================
	// Window Listener

	public void windowActivated(WindowEvent evt){}
	public void windowClosed(WindowEvent evt){}
	public void windowClosing(WindowEvent evt){System.exit(0);}
	public void windowDeactivated(WindowEvent evt){}
	public void windowDeiconified(WindowEvent evt){}
	public void windowIconified(WindowEvent evt){}
	public void windowOpened(WindowEvent evt){}


	// ===================================================================
	// main method
	public static void main(String[] arg){
		
		TestPanel app = new TestPanel();
		app.setSize(500, 400);
		app.setTitle("Geometry Toolbox Test Frame");
		app.setVisible(true);	
	
	}	

}