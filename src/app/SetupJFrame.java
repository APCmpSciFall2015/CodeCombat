package app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SetupJFrame extends JFrame implements ActionListener
{
	private JComboBox<String> comboBox;
	private String configOption = "";
	public SetupJFrame(String s, Dimension size)
	{
		super(s);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(size);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		JLabel prompt = new JLabel("Select a parameter to modify");
		prompt.setAlignmentX(CENTER_ALIGNMENT);
		
		panel.add(prompt);
		
		DefaultComboBoxModel<String> options = new DefaultComboBoxModel<String>();
		options.addElement("World Width");
		options.addElement("World Height");
		options.addElement("Noise Variance");
		options.addElement("Noise Mean");
		options.addElement("Number of Obstacles");
		options.addElement("Obstacle Sizes");
		options.addElement("Max Sprite Velocity");
		options.addElement("Projectile Speed");
		options.addElement("Projectile Radius");
		options.addElement("Circle Respawn Time");
		options.addElement("Circle Speed");
		options.addElement("Circle FOV");
		options.addElement("Circle Max Turning Angle");
		options.addElement("Circle Reload Time");
		options.addElement("Circle Radius");
		options.addElement("Shield Radius");
		options.addElement("Shield Respawn Time");
		
		comboBox = new JComboBox<String>(options);
		panel.add(comboBox);
		
		JButton button = new JButton("Ok");
		button.addActionListener((ActionListener) this);
		button.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(button);
		
		this.add(panel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(((JButton) e.getSource()).getText())
		{
		case "Ok":
			String selectedItem = comboBox.getSelectedItem().toString();
			switch(selectedItem)
			{
			case "World Width":
				configOption = "worldWidth";
				break;
			case "World Height":
				configOption = "worldHeight";
				break;
			case "Noise Variance":
				configOption = "worldNoiseVariance";
				break;
			case "Noise Mean":
				configOption = "worldNoiseMean";
				break;
			case "Number of Obstacles":
				configOption = "worldNumObstacles";
				break;
			case "Obstacle Sizes":
				configOption = "obstacleSizes";
				break;
			case "Max Sprite Velocity":
				configOption = "spriteMaxVelocity";
				break;
			case "Projectile Speed":
				configOption = "projectileSpeed";
				break;
			case "Projectile Radius":
				configOption = "projectileRadius";
				break;
			case "Circle Respawn Time":
				configOption = "circleRespawnTime";
				break;
			case "Circle Speed":
				configOption = "circleSpeed";
				break;
			case "Circle FOV":
				configOption = "circleFOV";
				break;
			case "Circle Max Turning Angle":
				configOption = "circleMaxTurningAngle";
				break;
			case "Circle Reload Time":
				configOption = "circleReloadTime";
				break;
			case "Circle Radius":
				configOption = "circleRadius";
				break;
			case "Shield Radius":
				configOption = "shieldRadius";
				break;
			case "Shield Respawn Time":
				configOption = "shieldRespawnTime";
				break;
				default:
					break;
			}
			
			String value = (String) JOptionPane.showInputDialog(this, "You are modifying " + selectedItem + ".", Main.CONFIG.get(configOption));
		break;
		
		default:
			break;
		}
	}
}
