package ui;

import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class DeadFieldPane extends JPanel {
	private JComboBox comboOptions;

	public DeadFieldPane() {
		setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Dead fields"),
        BorderFactory.createEmptyBorder(5,5,5,5)));
		
		String[] options = {
				"Random",
				"3",
				"5",
				"7",
				"9",
				"11"
		};
		
		comboOptions = new JComboBox(options);
		this.add(comboOptions);
		
	}
	
	/**
	 * Get selected number of dead fields. When 'random' selected it returns 0. 
	 * @return Number of selected dead fields, 0 when 'random' chosen.
	 */
	public int getDeadFieldNumber() {
		if(comboOptions.getSelectedIndex() == 0) {
			Random generator = new Random();
			return Integer.parseInt(
					comboOptions.getItemAt(generator.nextInt(5) + 1).toString());
		} else {
			return Integer.parseInt(comboOptions.getSelectedItem().toString());
		}
	}
}
