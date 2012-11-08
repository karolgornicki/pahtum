/**
 *  Project: Java PahTum
 *  Description:  A simple, graphic interface for PahTum. It enables to play 
 *  with different AIs. Created for the purpose of MSc dissertation.
 *  Copyright (C) 2012 Karol Gornicki, karol.gornicki (at) gmail.com
 *  Project Supervisor: Daniel Kudenko, kudenko (at) cs.york.ac.uk
 *  
 * *****************************************************************************
 * This program is free software; you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
 * details. 
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 * *****************************************************************************
 */

package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import util.Tuple;

import ai.charles2.Charles_2;
import ai.mch5boltzmann.MonteCarloH5Boltzmann;
import ai.montecarlo.MonteCarlo;
import ai.montecarloheuristic5.MonteCarloH5;

import core.Board;
import core.Player;
import core.Rules;


public class GUI extends JFrame implements MouseListener, ActionListener {

	private static final long serialVersionUID = 1L;
	static boolean gameOver = true;
	JLayeredPane layeredPane;
	JPanel pahtumBoard;
	JLabel piece;
	static int currentIndex = 0, numberOfMove = 0;
	static Player[] players =  {
		new Player("Unknown", "Unknown", "Unknow", 0)
	};
	static Board board = null;
	static int totalNumberOfMoves;
	static JFrame frame;
	static boolean capable = true;

	/**
	 * Set up top window menu bar and sample board.
	 * @param deadFields Number of dead fields.
	 */
	public GUI(int deadFields) {
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);
		JMenuItem miNewGame = new JMenuItem("New game");
		miNewGame.addActionListener(this);
		menuFile.add(miNewGame);
		this.setJMenuBar(menuBar);
		//End of top window menu menu.
		
		//Draw a sample board.
		drawBoard(deadFields);

	}

	/**
	 * Draw a board. Dead fields are randomly distributed.
	 * @param deadFields Number of dead fields.
	 */
	private void drawBoard(int deadFields) {
		GUI.totalNumberOfMoves = 49 - deadFields;

		Dimension boardSize = new Dimension(245, 245);
		layeredPane = new JLayeredPane();
		getContentPane().add(layeredPane);
		layeredPane.setPreferredSize(boardSize);
		layeredPane.addMouseListener(this);

		// Add a board to the Layered Pane.

		pahtumBoard = new JPanel();
		layeredPane.add(pahtumBoard, JLayeredPane.DEFAULT_LAYER);
		GridLayout gridLayout = new GridLayout(7, 7);
		gridLayout.setVgap(2);
		gridLayout.setHgap(2);
		pahtumBoard.setLayout(gridLayout);
		pahtumBoard.setPreferredSize(boardSize);
		pahtumBoard.setBounds(0, 0, boardSize.width, boardSize.height);

		board = new Board(1, true, deadFields);
		String[][] b = board.getState();

		for(int i = 0; i < 49; ++i) {
			JPanel square = new JPanel(new BorderLayout());
			pahtumBoard.add(square);
			if(b[i / 7][i % 7].equals("x")) {
				JLabel deadField = new JLabel(new ImageIcon(
				"/n/student/kg687/workspace/PahTum v3/img/deadFiled.jpg"));
				square.add(deadField);
			} else {
				square.setBackground(Color.getHSBColor(16, 83, 69));
			}
		}
	}

	/**
	 * Run application--set up GUI with a sample board.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		frame = new GUI(7);
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		//While loop secures that the application will never terminate (all by
		//itself).
		while(true) {
			//Check whether there are any empty fields left. If so, continue 
			//play.
			while(numberOfMove < totalNumberOfMoves) {
				System.out.println("Player " + players[currentIndex].getType() + "thinks");
				if(players[currentIndex].getType().equals("[AI] Monte-Carlo")) {
					//Monte-Carlo AI to play.
					
					Tuple<Integer, Integer> move;
					
					MonteCarlo mc = new MonteCarlo(
							board.duplicate(), 
							players[currentIndex].getColor(), 
							numberOfMove, 
							totalNumberOfMoves);

					//Get a new move.
					move = mc.uct(
							players[currentIndex].getSimulationNumber());

					//Perform the move on the board.
					board.makeMove(move, players[currentIndex].getColor());
					((GUI) frame).placeStone(
							move.getSecondElement(), 
							move.getFirstElement(), 
							players[currentIndex].getColor());

					//Increment number of currently made moves.
					++numberOfMove;

					//Adjust index of current player.
					currentIndex = (currentIndex + 1) % 2;
				} else if(players[currentIndex].getType().equals("[AI] MCTS Gibbs")) {
					//Gibbs (Boltzmann) AI to play.
					
					Tuple<Integer, Integer> move;
					
					MonteCarloH5Boltzmann mc = new MonteCarloH5Boltzmann(
							board.duplicate(), 
							players[currentIndex].getColor(), 
							numberOfMove, 
							totalNumberOfMoves);

					//Get a new move.
					move = mc.uct(
							players[currentIndex].getSimulationNumber());

					//Perform the move on the board.
					board.makeMove(move, players[currentIndex].getColor());
					((GUI) frame).placeStone(
							move.getSecondElement(), 
							move.getFirstElement(), 
							players[currentIndex].getColor());

					//Increment number of currently made moves.
					++numberOfMove;

					//Adjust index of current player.
					currentIndex = (currentIndex + 1) % 2;
				} else if(players[currentIndex].getType().equals("[AI] Charles")) {
					//Charles AI to play.
					
					Charles_2 charles = new Charles_2(players[currentIndex].getColor(), board);
					
					//Get a new move.
					Tuple<Integer, Integer> move = charles.getMove();
					
					//Perform the move on the board.
					board.makeMove(move, players[currentIndex].getColor());
					((GUI) frame).placeStone(
							move.getSecondElement(), 
							move.getFirstElement(), 
							players[currentIndex].getColor());

					//Increment number of currently made moves.
					++numberOfMove;

					//Adjust index of current player.
					currentIndex = (currentIndex + 1) % 2;
				} else if(players[currentIndex].getType().equals("[AI] MCTS Beam")) {
					//Beam Search agent to play.
					
					Tuple<Integer, Integer> move;

					MonteCarloH5 mc = new MonteCarloH5(
							board.duplicate(), 
							players[currentIndex].getColor(), 
							numberOfMove, 
							totalNumberOfMoves);

					//Get a new move.
					move = mc.uct(
							players[currentIndex].getSimulationNumber());

					//Perform the move on the board.
					board.makeMove(move, players[currentIndex].getColor());
					((GUI) frame).placeStone(
							move.getSecondElement(), 
							move.getFirstElement(), 
							players[currentIndex].getColor());

					//Increment number of currently made moves.
					++numberOfMove;

					//Adjust index of current player.
					currentIndex = (currentIndex + 1) % 2;
					
				}
			} //End of the while loop.
			
			//At this point the game is finished.
			System.out.println("The game has finished."); //It's not necessary.
			
			//Calculate the score and inform the user.
			if(GUI.gameOver) {
				if(Rules.calculateScore(board).equals("0")) {
					JOptionPane.showMessageDialog(null, "Draw");					
				} else {
					String winner = (Rules.calculateScore(board).equals(players[0].getColor()) ? players[0].getName() : players[1].getName());
					JOptionPane.showMessageDialog(null, "The winner is " + winner); 
				}
			}
			GUI.gameOver = false;
		}

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * This method places a stone in given location (specified by coordinates x 
	 * and x). Type of the stone (black or white) is specified by the color.
	 * @param column Coordinate representing column.
	 * @param row Coordinate representing row.
	 * @param color Type of the stone: "w" = white, "b" = black.
	 */
	private void placeStone(int column, int row, String color) {
		int number = row * 7 + column;
		System.out.println(color);
		JLabel piece;
		if(color.equals("w")) {
			piece = new JLabel(new ImageIcon(
					"/n/student/kg687/workspace/PahTum v3/img/w35.gif"));
		} else {
			piece = new JLabel(new ImageIcon(
					"/n/student/kg687/workspace/PahTum v3/img/b35.gif"));
		}
		JPanel panel = (JPanel) pahtumBoard.getComponent(number);
		panel.add(piece);
		validate();
		piece.setVisible(true);

	}

	/**
	 * Respond to the mouse click according to whether it is human player turn
	 * or not.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		piece = null;
		//Check if it is human player turn.
		if(players[currentIndex].getType().equals("Human")) {
			System.out.println("Human player clickd! " + 
					players[currentIndex].getName());
			//Perform a move on a selected field.
			Component c = pahtumBoard.findComponentAt(e.getX(), e.getY());
			System.out.println("x: " + e.getX()/35);
			System.out.println("y: " + e.getY()/35);
			if(!(c instanceof JLabel)) {
				// Empty square--make a move on the Layered Pane (visible in the GUI).
				placeStone((int)e.getX()/35, (int)c.getY()/35, players[currentIndex].getColor());
				//Update board of this move.
				try {
					board.makeMove(
							new Tuple<Integer, Integer>((int)e.getY()/35, (int)c.getX()/35), 
							players[currentIndex].getColor());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				//Adjust current player index.
				currentIndex = (currentIndex + 1) % 2;
				
				//Increment number of moves made.
				++numberOfMove;
			}
		} else {
			//Ignore the click. It's non-human player turn now.
			System.out.println(players[currentIndex].getName());
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Set up a new game for given players and draw a board for the new game.
	 * @param players 2 player for the new game. 
	 * @param deadFields Dead fields for the new game.
	 */
	public void newGame(Player[] players, int deadFields) {
		GUI.board = null;
		GUI.players = players;
		pahtumBoard.setVisible(false);
		drawBoard(deadFields);
		pahtumBoard.setVisible(true);
		GUI.totalNumberOfMoves = 49 - deadFields;
		GUI.numberOfMove = 0;
		GUI.currentIndex = 0;
		GUI.capable = true;
		GUI.gameOver = true;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("New game")) {
			new NewGameFrame(this);
		}
	}


}
