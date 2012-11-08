package ai.mch7boltzmann_t40;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.Board;

import util.Tuple;

/**
 * Represents node in a game tree which is used by Monte-Carlo Tree Search with 
 * Boltzmann's formula. Each node hold information regarding potential and 
 * probability of a move associated with each node.
 * @author kg687
 *
 */
public class Node {
	private Tuple<Integer, Integer> move;	//Move associated with the node.
	private int potential;					//Potential of the node.
	private double probability;				//Probability of the node.
	private String color;					//Color of the move associated to 
	//this node.
	
	private Node parent;					//Parent of this node.
	private int value, visit, moveNumber;	//Counters of quality of the node, 
	//number of visit and number of move in a game respectively.
	
	private List<Node> children;			//Collection of children.
	private List<Tuple<Integer, Integer>> untriedMoves;		//Collection of 
	//untried moves.
	
	/**
	 * Initialize a new node with given parameters.
	 * @param parent Parent of the node.
	 * @param move Move that this node represents.
	 * @param color Color of the move which is represented by this node.
	 * @param board Board which includes already aforementioned move. Used to 
	 * calculate the potential associated with this move.
	 * @param moveNumber Current number of moves made in the game.
	 * @param mcColor Monte-Carlo's color.
	 */
	public Node(Node parent, Tuple<Integer, Integer> move, String color, 
			Board board, int moveNumber) {
		this.move = move;
		this.color = color;
		this.parent = parent;
		this.probability = -1;
		this.value = 0;
		this.visit = 0;
		this.children = new ArrayList<Node>();
		this.untriedMoves = board.heuristic_bestX_moves(color, 7);
		this.moveNumber = moveNumber;
		
		//Update potential.
		if(move != null) {
			this.potential = board.getHeuristicValue(
					move.getFirstElement(), 
					move.getSecondElement(), 
					color.equals("w") ? "b" : "w");
		}
	}
	
	public void setProbability(double value) {
		this.probability = value;
	}
	
	public double getProbability() {
		return this.probability;
	}
	
	public void setPotential(int value) {
		this.potential = value;
	}
	
	public int getPotential() {
		return this.potential;
	}

	/**
	 * Expand current node. Newly created node will have up to 5 untried moves 
	 * based on heuristic evaluation of position of each candidate play from 
	 * given board. 
	 * @param board Board with initial position. (Foundation for evaluation of 
	 * each candidate play)
	 * @param mcColor Monte-Carlo's color.
	 * @return New node.
	 * @throws Exception Doesn't occur.
	 */
	public Node expand(Board board, String mcColor){
		String newColor;
		
		//Get random from untried moves and next remove from list of untried 
		//moves.
		Random generator = new Random();
		int randomIndex = generator.nextInt(this.untriedMoves.size());
		Tuple<Integer, Integer> move = this.untriedMoves.get(randomIndex);
		this.untriedMoves.remove(randomIndex);
		
		//Make selected move on the board.
		try {
			board.makeMove(move, color);
		} catch (Exception e) {
			//Doesn't happen.
			e.printStackTrace();
		}
		
		//Switch colors.
		newColor = this.color.equals("w") ? "b" : "w";
		
		//Initialize a new node.
		Node node = new Node(this, move, newColor, board, this.moveNumber + 1);
		
		//Add new node as a child to the THIS node.
		this.children.add(node);
		return node;
	}
	/**
	 * @return the moveNumber
	 */
	public int getMoveNumber() {
		return moveNumber;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value of which update
	 */
	public void updateValue(double value) {
		this.value += value;
	}

	/**
	 * @return the visit
	 */
	public int getVisit() {
		return visit;
	}

	/**
	 * @param visit the visit to set
	 */
	public void updateVisit() {
		this.visit += 1;
	}

	/**
	 * @return the move
	 */
	public Tuple<Integer, Integer> getMove() {
		return move;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @return the parent
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * @return the children
	 */
	public List<Node> getChildren() {
		return children;
	}

	/**
	 * @return the untriedMoves
	 */
	public List<Tuple<Integer, Integer>> getUntriedMoves() {
		return untriedMoves;
	}
}
