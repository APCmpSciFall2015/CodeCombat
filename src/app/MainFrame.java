package app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

/**
 * The MainFrame class is responsible for rendering and displaying a window for
 * the user to interact with
 * @author Robert and Richard
 * @version 0.1
 */
public class MainFrame extends JFrame implements ActionListener, ItemListener, KeyListener
{

	/** Serializable id *. */
	private static final long serialVersionUID = -3718511819649048230L;

	/** The applet the window displays. */
	private MainApplet applet;

	/** The checkbox item for handling pausing */
	private JCheckBoxMenuItem pauseCbItem = new JCheckBoxMenuItem("Pause");

	/** The checkbox item for handling debugging */
	private JCheckBoxMenuItem debugCbItem = new JCheckBoxMenuItem("Debug");

	/** The list of AIs avalible to the user. */
	private List<String> minds = lib.Parser.parseImmutableStringList(Main.CONFIG.get("mindTypes"));

	/** The groups of radio buttons to select the AIs for each bot. */
	private List<ButtonGroup> buttonGroups = new ArrayList<ButtonGroup>();

	/** The 2D list of radio buttons for selecting the AI for the bot. */
	private List<List<JRadioButtonMenuItem>> radioButtons = new ArrayList<List<JRadioButtonMenuItem>>();

	/** The configuration menu properties. */
	private List<String> configKeys = Main.CONFIG.keysToSortedList();

	// Constructors
	// -------------------------------------------------------------

	/**
	 * Creates and initializes the JFrame.
	 * @param s the title of the window
	 * @param size the size of the window
	 * @param applet the applet the window displays
	 */
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

		// menuBar
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		// create file menu
		JMenu menu = new JMenu("File");
		menuBar.add(menu);

		// add exit option
		JMenuItem menuItem = new JMenuItem("Exit");
		menuItem.addActionListener((ActionListener) this);
		menu.add(menuItem);

		// add exit option
		menuItem = new JMenuItem("Main Menu");
		menuItem.addActionListener((ActionListener) this);
		menu.add(menuItem);

		// add config menu
		JMenu config = new JMenu("Config");
		for (String key : configKeys)
		{
			menuItem = new JMenuItem(key);
			menuItem.addActionListener((ActionListener) this);
			config.add(menuItem);
		}
		menu.add(config);

		// add pause toggle
		pauseCbItem.addItemListener(this);
		menu.add(pauseCbItem);

		// add debug toggle
		debugCbItem.addItemListener(this);
		menu.add(debugCbItem);

		menu = new JMenu("Bot Settings");
		for (int i = 0; i < Integer.parseInt(Main.CONFIG.get("worldMaxCircles")); i++)
		{
			radioButtons.add(new ArrayList<JRadioButtonMenuItem>());
			JMenu bot = new JMenu("Bot " + (i + 1));
			buttonGroups.add(new ButtonGroup());

			// add no mind
			JRadioButtonMenuItem radioItem = new JRadioButtonMenuItem("No Mind");
			buttonGroups.get(i).add(radioItem);
			radioButtons.get(i).add(radioItem);
			radioItem.setActionCommand("null");
			radioItem.addActionListener(this);
			bot.add(radioItem);

			// add disabled
			radioItem = new JRadioButtonMenuItem("Disabled");
			buttonGroups.get(i).add(radioItem);
			radioButtons.get(i).add(radioItem);
			radioItem.setActionCommand("");
			radioItem.addActionListener(this);
			bot.add(radioItem);

			// add mind types
			for (String mind : minds)
			{
				radioItem = new JRadioButtonMenuItem(mind);
				buttonGroups.get(i).add(radioItem);
				radioItem.addActionListener(this);
				radioItem.setActionCommand(mind);

				bot.add(radioItem);
				radioButtons.get(i).add(radioItem);
			}

			menu.add(bot);
		}

		menuBar.add(menu);

		for (int i = 0; i < buttonGroups.size(); i++)
		{
			if ("null".equals(Main.GAME_SETTINGS.get("slot" + (i + 1))))
			{
				radioButtons.get(i).get(0).setSelected(true);
			}
			else
			{
				radioButtons.get(i).get(getMindIndex(Main.GAME_SETTINGS.get("slot" + (i + 1)))).setSelected(true);
			}
		}

		// build window and display
		this.pack();
		this.setVisible(true);
	}

	// Functional methods
	// -------------------------------------------------

	/**
	 * Updates the checkbox items on the menu.
	 */
	public void updateMenu()
	{
		pauseCbItem.setState(applet.getGameState() == Main.GameState.PAUSED);
		debugCbItem.setState(Main.debug);
	}

	/**
	 * Gets the index of an AI for.
	 * @param mindName the name of the AI
	 * @return the index the AI is located
	 */
	public int getMindIndex(String mindName)
	{
		if (mindName.equals("null"))
		{
			return 0;
		}
		else if (mindName.equals(""))
		{
			return 1;
		}
		else
		{
			for (int x = 0; x < minds.size(); x++)
			{
				if (mindName.indexOf(minds.get(x)) >= 0)
				{
					return x + 1;
				}
			}
			return -1;
		}
	}

	// Overridden methods
	// ------------------------------------------------------

	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand() == "Exit")
		{
			System.exit(0);
		}
		if (e.getActionCommand() == "Main Menu")
		{
			applet.setGameState(Main.GameState.MENU);
		}

		for (int x = 0; x < buttonGroups.size(); x++)
		{
			// set no mind
			if ("null".equals(buttonGroups.get(x).getSelection().getActionCommand()))
			{
				Main.GAME_SETTINGS.set("slot" + (x + 1), "null");
			}
			// set disabled
			else if ("".equals(buttonGroups.get(x).getSelection().getActionCommand()))
			{
				Main.GAME_SETTINGS.set("slot" + (x + 1), "");
			}
			// set mind
			else
			{
				Main.GAME_SETTINGS.set("slot" + (x + 1), buttonGroups.get(x).getSelection().getActionCommand());
			}
		}

		if (configKeys.contains(e.getActionCommand()))
		{
			String configProperty = e.getActionCommand();
			String value = (String) JOptionPane.showInputDialog(this, "You are modifying " + e.getActionCommand() + ".",
					Main.CONFIG.get(configProperty));
			if (value != null)
			{
				Main.CONFIG.set(configProperty, value);
				System.out.println("Config edit: " + configProperty + "=" + value);
				
				// update game settings if necessary
				if(configProperty.equals("worldMaxCircles") && Main.GAME_SETTINGS.get("slot" + value) == null)
				{	
					Main.GAME_SETTINGS.set("slot" + value, "");
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(ItemEvent e)
	{

		switch (((JMenuItem) e.getSource()).getText())
		{
		case "Pause":
			if (e.getStateChange() == ItemEvent.SELECTED)
			{
				applet.pause();
			}
			else
			{
				applet.resume();
			}
			break;
		case "Debug":
			if (e.getStateChange() == ItemEvent.SELECTED)
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

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e)
	{

	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e)
	{
	}

}
