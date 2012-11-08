package ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import core.Player;

import util.Tuple;

public class NewGameFrame extends JFrame implements ActionListener{
	private GUI masterGui;
	private JPanel mainPane;
	private PlayerPane player1, player2;
	DeadFieldPane deadFields;
	private JButton buttonApply;

	public NewGameFrame(GUI gui) {
		this.masterGui = gui;

		player1 = new PlayerPane("Player 1");
		player2 = new PlayerPane("Player 2");
		deadFields = new DeadFieldPane();

		buttonApply = new JButton("Apply");
		buttonApply.addActionListener(this);

		mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
		mainPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPane.add(player1);
		mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPane.add(player2);
		mainPane.add(deadFields);
		mainPane.add(buttonApply);

		this.add(mainPane);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("Apply")) {
			//Close this frame.
			this.dispose();
			
			System.out.println("aaaa");
			//Get data regarding player1.
			Tuple<String, Tuple<String, Integer>> player1Data = 
				player1.getPlayerData();
			//Get data regarding player2.
			Tuple<String, Tuple<String, Integer>> player2Data = 
				player2.getPlayerData();
			//Get data regarding dead fields.
			int deadFields = this.deadFields.getDeadFieldNumber();
			System.out.println(deadFields);
			//Update bonded GUI of gathered data.
			System.out.println("bbb");
			Player[] players = {
					new Player(player1Data.getFirstElement(), 
							player1Data.getSecondElement().getFirstElement(), 
							"w", 
							player1Data.getSecondElement().getSecondElement()),
					new Player(player2Data.getFirstElement(), 
							player2Data.getSecondElement().getFirstElement(), 
							"b", 
							player2Data.getSecondElement().getSecondElement()),	
			};
			System.out.println("ccc");
			try {
				masterGui.newGame(players, deadFields);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.out.println("ddd");
			
		}

	}

}
