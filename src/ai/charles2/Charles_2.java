package ai.charles2;

import java.util.ArrayList;
import java.util.Random;

import core.Board;
import util.Tuple;

/**
 * Charles_2 PahTum AI. This engine selects move to play based on evaluation of 
 * the potential of each field and how newly placed stone immediately contribute
 * to the game.
 * 
 * Arguably the best so far.
 * @author kg687
 *
 */
public class Charles_2 {
	private Board board;
	private String color;
	
	/**
	 * Instantiate the engine.
	 * @param color Color to play.
	 * @param board Board with initial position.
	 */
	public Charles_2(String color, Board board) {
		this.color = color;
		this.board = board;
	}
	
	/**
	 * Select best move in given (as parameters to the constructor) situation.
	 * @return Tuple with the best move.
	 */
	public Tuple<Integer, Integer>getMove() {
		ArrayList<Tuple<Integer,Integer>> list = board.heuristic_adjustment_listMoves(this.color);
		Random generator = new Random();
		
		return list.get(generator.nextInt(list.size()));
	}
}
