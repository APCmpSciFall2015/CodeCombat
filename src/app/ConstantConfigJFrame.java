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

public class ConstantConfigJFrame extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 2970521899937204863L;
	private JComboBox<String> comboBox;
	private String configProperty = "";

	public ConstantConfigJFrame(String s, Dimension size)
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
	public void actionPerformed(ActionEvent e)
	{
		if (((JButton) e.getSource()).getText() == "Ok")
		{
			// fun with hard coded logic
			String selectedItem = comboBox.getSelectedItem().toString();
			switch (selectedItem)
			{
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

			// get the value and update the config (changes take effect on restart)
			String value = (String) JOptionPane.showInputDialog(this, "You are modifying " + selectedItem + ".",
					Main.CONFIG.get(configProperty));			
			if(value != null)
			{
				Main.CONFIG.set(configProperty, value);
				System.out.println("Config edit: " + configProperty + "=" + value);
			}
		}
	}
}
