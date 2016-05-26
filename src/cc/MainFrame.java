package cc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MainFrame extends JFrame
{
	/** Serializable id **/
	private static final long serialVersionUID = -3718511819649048230L;
	
	private MainApplet applet;
	
	public MainFrame(String s, Dimension size, MainApplet applet)
	{
		super(s);
		this.setFocusable(false);
		this.applet = applet;
		
		// initialize panel
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(size);

		// initialize applet with itself as keyboard input
		applet.setFocusable(true);
		applet.init();
		applet.start();
		panel.add(applet, BorderLayout.CENTER);

		// add to panel
		this.add(panel);
		applet.requestFocus();
		
		//menuBar
		JMenuBar menubar = new JMenuBar();
		this.setJMenuBar(menubar);
				
		JMenu file = new JMenu("File");
		menubar.add(file);
		JMenuItem exit = new JMenuItem("Exit");
		file.add(exit);
				
		JMenu help = new JMenu("Help");
		menubar.add(help);
		JMenuItem test = new JMenuItem("This is a test");
		file.add(test);
		
		// build window and display
		this.pack();
		this.setVisible(true);
		
		
		
		
		
		
	}

}
