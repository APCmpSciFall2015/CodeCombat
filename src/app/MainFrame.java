package app;


import lib.Config;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainFrame extends JFrame implements ActionListener, ItemListener
{
	/** Serializable id **/
	private static final long serialVersionUID = -3718511819649048230L;
	
	private MainApplet applet;
	private JCheckBoxMenuItem pauseCbItem = new JCheckBoxMenuItem("Pause");
	private JCheckBoxMenuItem debugCbItem = new JCheckBoxMenuItem("Debug");
	private String configProperty;
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
		
		JMenu config = new JMenu("Config");
		menuItem = new JMenuItem("World Width");
		menuItem.addActionListener((ActionListener) this);
		config.add(menuItem);
		
		menuItem = new JMenuItem("World Height");
		menuItem.addActionListener((ActionListener) this);
		config.add(menuItem);
		
		menuItem = new JMenuItem("Noise Variance");
		menuItem.addActionListener((ActionListener) this);
		config.add(menuItem);
		
		menuItem = new JMenuItem("Noise Mean");
		menuItem.addActionListener((ActionListener) this);
		config.add(menuItem);
		
		menuItem = new JMenuItem("Number of Obstacles");
		menuItem.addActionListener((ActionListener) this);
		config.add(menuItem);
		
		menuItem = new JMenuItem("Obstacle Sizes");
		menuItem.addActionListener((ActionListener) this);
		config.add(menuItem);
		
		menuItem = new JMenuItem("Max Sprite Velocity");
		menuItem.addActionListener((ActionListener) this);
		config.add(menuItem);
		
		menuItem = new JMenuItem("Projectile Speed");
		menuItem.addActionListener((ActionListener) this);
		config.add(menuItem);
		
		menuItem = new JMenuItem("Projectile Radius");
		menuItem.addActionListener((ActionListener) this);
		config.add(menuItem);
		
		menuItem = new JMenuItem("Circle Respawn Time");
		menuItem.addActionListener((ActionListener) this);
		config.add(menuItem);
		
		menuItem = new JMenuItem("Circle Speed");
		menuItem.addActionListener((ActionListener) this);
		config.add(menuItem);
		
		menuItem = new JMenuItem("Circle FOV");
		menuItem.addActionListener((ActionListener) this);
		config.add(menuItem);
		
		menuItem = new JMenuItem("Circle Max Turning Angle");
		menuItem.addActionListener((ActionListener) this);
		config.add(menuItem);
		
		menuItem = new JMenuItem("Circle Reload Time");
		menuItem.addActionListener((ActionListener) this);
		config.add(menuItem);
		
		menuItem = new JMenuItem("Circle Radius");
		menuItem.addActionListener((ActionListener) this);
		config.add(menuItem);
		
		menuItem = new JMenuItem("Shield Radius");
		menuItem.addActionListener((ActionListener) this);
		config.add(menuItem);
		
		menuItem = new JMenuItem("Shield Respawn Time");
		menuItem.addActionListener((ActionListener) this);
		config.add(menuItem);
		
		menu.add(config);
		
		
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
		configProperty = null;
		String selectedItem = ((JMenuItem) e.getSource()).getText();
		switch(selectedItem)
		{
		case "Exit":
			System.exit(0);
			break;
							
			case "World Width":
				configProperty = "worldWidth";
				break;
			case "World Height":
				configProperty = "worldHeight";
				break;
			case "Noise Variance":
				configProperty = "worldNoiseVariance";
				break;
			case "Noise Mean":
				configProperty = "worldNoiseMean";
				break;
			case "Number of Obstacles":
				configProperty = "worldNumObstacles";
				break;
			case "Obstacle Sizes":
				configProperty = "obstacleSizes";
				break;
			case "Max Sprite Velocity":
				configProperty = "spriteMaxVelocity";
				break;
			case "Projectile Speed":
				configProperty = "projectileSpeed";
				break;
			case "Projectile Radius":
				configProperty = "projectileRadius";
				break;
			case "Circle Respawn Time":
				configProperty = "circleRespawnTime";
				break;
			case "Circle Speed":
				configProperty = "circleSpeed";
				break;
			case "Circle FOV":
				configProperty = "circleFOV";
				break;
			case "Circle Max Turning Angle":
				configProperty = "circleMaxTurningAngle";
				break;
			case "Circle Reload Time":
				configProperty = "circleReloadTime";
				break;
			case "Circle Radius":
				configProperty = "circleRadius";
				break;
			case "Shield Radius":
				configProperty = "shieldRadius";
				break;
			case "Shield Respawn Time":
				configProperty = "shieldRespawnTime";
				break;
				
			default:
				break;
		}
		if(configProperty != null) //if a config property was selected
		{
			String value = (String) JOptionPane.showInputDialog(this, "You are modifying " + selectedItem + ".",
					Main.CONFIG.get(configProperty));			
			if(value != null)
			{
				Main.CONFIG.set(configProperty, value);
				System.out.println("Config edit: " + configProperty + "=" + value);
			}
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

