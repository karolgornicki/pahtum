package test;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;

import util.Tuple;

import ai.minimax.MiniMax;
import ai.montecarloheuristic.MonteCarloH;
import ai.montecarloheuristic2.MonteCarloH2;

import core.Board;
import core.Player;
import core.Rules;

/**
 * This class enables testing Monte-Carlo with Heuristic against Monte-Carlo 
 * with Heuristic (2). The test is constituted of 50 sets, each set is played as 
 * a match of 2 games where after one players switch sides.
 * @author kg687
 *
 */
public class TestMCTSHvsMCTSH2 {

	/**
	 * Run test case. During the test, after each game is finished it updates 
	 * the results to the external file in order to keep track about results 
	 * even when the execution of the program would be interrupted. Before 
	 * running this application make sure there is no previous record of matches 
	 * (this application will append new finding, and therefore they might 
	 * become difficult to distinguish from previous ones). 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//Counters which keep track of number of wins/draws that occurred.
		int montecaroloWinCount = 0, mctsh2WinCount = 0, drawCount = 0;
		
		//Board that is used in games.
		Board board = null;
		
		//Board that keeps copy of initial position, used to quickly reset the 
		//board before new game take place.
		Board initialPosition = null;
		
		//Array of all boards that are used in the test case.
		Board[] boardCollection = null;
		
		//Index of player that is entitled to make a move.
		int currentIndex = 0;
		
		//Number of all moves that was made during the game.
		int numberOfMove = 0;
		
		//Players participating in the test case.
		Player[] players =  {
			new Player("MCTS+H2", "mcts_h2", "w", 300),
			new Player("MCTS+H", "MCTS+H", "b", 50)
		};
		
		//Number of total moves. It is used to check whether the game is in 
		//terminate state or not (the game finishes when there is no empty 
		//fields in the board).
		int totalNumberOfMoves = 40;
		
		//Condition variables which check whether MiniMax is taking charge over
		//MC in terms of making moves. It occurs when the board is nearly full.
		//capable1 is designated for MCTS+H, capable2 for MCTS
		boolean capable1 = true, capable2;
		
		//Load board.
		try {
			FileInputStream fis = new FileInputStream("50_boards_9.sav");
			ObjectInputStream ois = new ObjectInputStream(fis);
			boardCollection = (Board[]) ois.readObject();
		} catch(Exception e) {
			System.err.println("Error" + e.getMessage());
		}
		
		//Check whether number of boards is OK. If not terminate program.
		if(boardCollection.length != 50) {
			System.err.println("Error, boardCollection has " + 
					boardCollection.length + " board.");
			System.exit(0);
		}
		
		//Boards are OK. Proceed to testing.
		for(int testIndex = 1; testIndex <= 100; ++testIndex) {
			//Reset settings.
			currentIndex = 0;
			numberOfMove = 0;
			capable1 = true;
			capable2 = true;

			//Swap players.
			Player tmp = players[0];
			players[0] = players[1];
			players[1] = tmp;

			//Reset the board to an initial state. When index is odd generate a 
			//new random board.
			if(testIndex % 2 == 1) {
				//Load a new board.
				board = boardCollection[(Integer) testIndex/2];
				initialPosition = board.duplicate();
			} else {
				//Reset the board.
				board = initialPosition.duplicate();
			}

			//Run a single game.
			while(numberOfMove < totalNumberOfMoves) {
				if(players[currentIndex].getType().equals("mcts_h2")) {
					Tuple<Integer, Integer> move;
					if(capable2) {
						//Monte-Carlo AI to play.
						MonteCarloH2 mc = new MonteCarloH2(
								board.duplicate(), 
								players[currentIndex].getColor(), 
								numberOfMove, 
								totalNumberOfMoves);
						try {
							move = mc.uct(players[currentIndex].
									getSimulationNumber());
						} catch(Exception e) {
							capable2 = false;
							MiniMax mm = new MiniMax(players[currentIndex].
									getColor().equals("w") ? "b" : "w", board);
							move = mm.getMove();
						}
					} else {
						MiniMax mm = new MiniMax(players[currentIndex].
								getColor().equals("w") ? "b" : "w", board);
						move = mm.getMove();
					}

					board.makeMove(move, players[currentIndex].getColor());

					//Increment number of currently made moves.
					++numberOfMove;

					//Adjust index of current player.
					currentIndex = (currentIndex + 1) % 2;
				} else if(players[currentIndex].getType().equals("MCTS+H")) {
					Tuple<Integer, Integer> move;
					if(capable1) {
						//Monte-Carlo AI to play.
						MonteCarloH mc = new MonteCarloH(
								board.duplicate(), 
								players[currentIndex].getColor(), 
								numberOfMove, 
								totalNumberOfMoves);
						try {
							move = mc.uct(players[currentIndex].
									getSimulationNumber());
						} catch(Exception e) {
							capable1 = false;
							MiniMax mm = new MiniMax(players[currentIndex].
									getColor().equals("w") ? "b" : "w", board);
							move = mm.getMove();
						}
					} else {
						MiniMax mm = new MiniMax(players[currentIndex].
								getColor().equals("w") ? "b" : "w", board);
						move = mm.getMove();
					}

					board.makeMove(move, players[currentIndex].getColor());

					//Increment number of currently made moves.
					++numberOfMove;

					//Adjust index of current player.
					currentIndex = (currentIndex + 1) % 2;
				}
			} //end of single game.
			
			String gameOutcome = Rules.calculateScore(board);
			BufferedWriter output = new BufferedWriter(
					new FileWriter("results_MCTSHvsMCTSH2.txt", true));
			output.append("Match #" + testIndex);
			output.newLine();
			output.append("Player 1: " + players[0].getName() + 
					" Player 2: " + players[1].getName());
			output.newLine();
			
			//Append the result to the text file and update counters..
			if(gameOutcome.equals("0")) {
				//The game was a draw.
				++drawCount;
				//Append information to the file.
				output.append("Result: draw");
				output.newLine();
				output.close();
			} else {
				//One side won the game.
				if(gameOutcome.equals(players[0].getColor())) {
					//Add note about the winner to the file.
					output.append("Result: " + players[0].getName() + " won");
					//Increment appropriate counter.
					if(players[0].getName().equals("MCTS+H2")) {
						++mctsh2WinCount;
					} else {
						++montecaroloWinCount;
					}
				} else {
					//Add note about the winner to the file.
					output.append("Result: " + players[1].getName() + " won");
					//Increment appropriate counter.
					if(players[1].getName().equals("MCTS+H2")) {
						++mctsh2WinCount;
					} else {
						++montecaroloWinCount;
					}
				}
				output.newLine();
				output.close();
			}			
		} //End of the test case.
		
		//Append total outcome of the test case to the file.
		BufferedWriter output1 = new BufferedWriter(
				new FileWriter("results_MCTSHvsMCTSH2.txt", true));
		output1.append("========================================");
		output1.newLine();
		output1.append("*Summary*");
		output1.newLine();
		output1.append("Draw occured: " + drawCount);
		output1.newLine();
		output1.append("Monte-Carlo with H. victories: " + montecaroloWinCount);
		output1.newLine();
		output1.append("Monte-Carlo with H. (2) victories: " + mctsh2WinCount);
		output1.newLine();
		output1.append("========================================");
		output1.close();

	} //End of main method.
}
