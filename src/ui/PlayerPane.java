package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import util.Tuple;

public class PlayerPane extends JPanel implements ActionListener{
	private JTextField textPlayerName;
	private JComboBox comboPlayerType;
	private String defaultPlayerName;
	private int simulationNumber;
	
	/**
	 * 
	 * @param defaultPlayerName
	 */
	public PlayerPane(String defaultPlayerName) {
		setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(defaultPlayerName),
        BorderFactory.createEmptyBorder(5,5,5,5)));
		
		this.defaultPlayerName = defaultPlayerName;
		this.simulationNumber = 0;
		
		textPlayerName = new JTextField(defaultPlayerName);
		textPlayerName.setColumns(15);
		textPlayerName.setEditable(true);
		String[] playerTypes = { 
				"Human", 
				"[AI] Monte-Carlo", 
				"[AI] MCTS Gibbs",
				"[AI] Charles",
				"[AI] MCTS Beam"
				};
		comboPlayerType = new JComboBox(playerTypes);
		comboPlayerType.addActionListener(this);
		
		this.add(textPlayerName);
		this.add(comboPlayerType);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Update "Player name" text field when different type of player is 
		//selected.
		if(comboPlayerType.getSelectedIndex() > 0) {
			//AI selected.
			textPlayerName.setText(
					comboPlayerType.getSelectedItem().toString());
			textPlayerName.setEditable(false);
			if(comboPlayerType.getSelectedIndex() == 1 || comboPlayerType.getSelectedIndex() == 2 || comboPlayerType.getSelectedIndex() == 4) {
				//Monte-Carlo selected. Ask for simulation number.
				this.simulationNumber = Integer.parseInt(
						JOptionPane.showInputDialog("Enter simulation number per move"));
			}
		} else {
			//Human selected.
			textPlayerName.setEditable(true);
			textPlayerName.setText(this.defaultPlayerName);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Tuple<String, Tuple<String, Integer>> getPlayerData() {
		return new Tuple<String, Tuple<String, Integer>>(
				textPlayerName.getText(), 
				new Tuple<String, Integer>(
						comboPlayerType.getSelectedItem().toString(), 
						this.simulationNumber));
		
	}

}
