package app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MainFrame extends JFrame implements ActionListener, ItemListener
{
	/** Serializable id **/
	private static final long serialVersionUID = -3718511819649048230L;
	
	private MainApplet applet;
	private JCheckBoxMenuItem pauseCbItem = new JCheckBoxMenuItem("Pause");
	private JCheckBoxMenuItem debugCbItem = new JCheckBoxMenuItem("Debug");
	
	public MainFrame(String s, Dimension size, MainApplet applet)
	{
		super(s);
		this.setFocusable(false);
		this.applet = applet;
		this.applet.setFrame(this);
		
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
		
		menuItem = new JMenuItem("Config");
		menuItem.addActionListener((ActionListener) this);
		menu.add(menuItem);
		
		
		pauseCbItem.addItemListener((ItemListener) this);
		menu.add(pauseCbItem);
		
		debugCbItem.addItemListener((ItemListener) this);
		menu.add(debugCbItem);
		
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
			System.exit(0);
			break;
			
		case "Config":
			new SetupJFrame("Configure Options", new Dimension(200, 80));
			break;
			
			default:
				break;
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e)
	{
		switch(((JMenuItem) e.getSource()).getText())
		{
		case "Pause":
			if(e.getStateChange() == ItemEvent.SELECTED)
			{
				applet.pause();
			}
			else
			{
				applet.resume();
			}
			break;
		case "Debug":
			if(e.getStateChange() == ItemEvent.SELECTED)
			{
				Main.debug = true;
			}
			else
			{
				Main.debug = false;
			}
			break;
			
			default:
				break;
		}
	}
	
	public void updateMenu()
	{
		pauseCbItem.setState(applet.getGameState() == Main.GameState.PAUSED);		
		debugCbItem.setState(Main.debug);
	}

}

