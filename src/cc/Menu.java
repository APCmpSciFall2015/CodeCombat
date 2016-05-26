package cc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class Menu implements ActionListener, ItemListener
{
	public JMenuBar createMenuBar()
	{
		JMenuBar menuBar;
		JMenu menu;
		JCheckBoxMenuItem cbMenuItem;
		
		menuBar = new JMenuBar();
		
		menu = new JMenu("Game Controls");
		menuBar.add(menu);
		
		cbMenuItem = new JCheckBoxMenuItem("Pause");
		cbMenuItem.addActionListener(this);
		menu.add(cbMenuItem);
		
		cbMenuItem = new JCheckBoxMenuItem("Debug");
		cbMenuItem.addActionListener(this);
		menu.add(cbMenuItem);
		return menuBar;
	}

	@Override
	public void itemStateChanged(ItemEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		
	}
	
}
