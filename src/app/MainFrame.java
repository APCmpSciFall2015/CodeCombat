package app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MainFrame extends JFrame implements ActionListener
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
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
				
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem("Exit");
		menuItem.addActionListener((ActionListener) this);
		menu.add(menuItem);
		
		menu = new JMenu("Help");
		menuBar.add(menu);
		
		
	
		
		// build window and display
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(((JMenuItem) e.getSource()).getText())
		{
		case "Exit":
		
			break;
			default:
				break;
		}
		
	}

}

