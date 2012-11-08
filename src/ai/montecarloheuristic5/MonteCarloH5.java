package ai.montecarloheuristic5;

import java.util.List;
import java.util.Random;

import util.Tuple;
import core.Board;
import core.Rules;

/**
 * Monte Carlo Tree Search with a scope of candidate plays narrowed down to the 
 * best 5 plays in accordance to the heuristics. The Selection policy is guided 
 * in the same fashion as UCT algorithm.
 * @author kg687
 */
public class MonteCarloH5 {
	private Board board;
	private String color;
	private Root root;
	private int allMovesNumber;
	private double c;
	

	/**
	 * Create engine's object and initialize main parameters.
	 * @param board Board with an initial state.
	 * @param color Monte-Carlo's color.
	 * @param moveNumber Current move number.
	 * @param allMovesNumber Number of remaining moves till the game reaches a 
	 * terminal state.
	 */
	public MonteCarloH5(Board board, String color, int moveNumber, 
			int allMovesNumber) {
		this.board = board;
		this.color = color;
		this.allMovesNumber = allMovesNumber;
		this.root = new Root(new Node(null, null, color, board, moveNumber, 
				this.color));
		this.c = 1 / Math.sqrt(2);
	}
	
	/**
	 * Run UCT-alike algorithm which selects the best move. 
	 * @param n Number of roll-outs.
	 * @return Best move as a tuple with coordinates.
	 * @throws Exception Don't remember,it has never occurred!
	 */
	public Tuple<Integer, Integer> uct(int n) throws Exception {
		while(n > 0) {
			//Create the duplicate of current board. Each time the tree is 
			//traversed the moves are played on this board, from initial state 
			//given to the constructor.
			Board tempBoard = this.board.duplicate();
			
			//Run Tree Policy algorithm starting from the root and produce a new
			//node.
			Node node = treePolicy(root.getRoot(), tempBoard);
			
			//Run a self-played random game starting from position defined by a 
			//newly created node (Default Policy algorithm). As a result produce 
			//the outcome of simulation (who win the game or a draw).
			String delta = defaultPolicy(node, tempBoard);
			
			//Propagate the result of the game upwards until it reaches the 
			//root.
			back_up(node, delta);
			
			//Adjust number of remaining roll-outs to perform.
			--n;
		}
		return bestChild(root.getRoot(), 0).getMove();
	}
	
	/**
	 * Expand the game tree of a new node<p>
	 * This method facilitates the selection policy by employing bestChild 
	 * method using formula proposed for UCT algorithm.  
	 * @param node Node which corresponds to the initial state (from which Tree 
	 * Policy kicks off).
	 * @param board A board on which moves which corresponds to each traversed 
	 * node are played (as we go deeper the board populates more).
	 * @return A new node.
	 * @throws Exception I don't remember. It has never occurred.
	 */
	private Node treePolicy(Node node, Board board) throws Exception {
		//While node is not a terminal state apply Tree Policy. Terminal state 
		//is the same as fully populated board.
		while(node.getMoveNumber() < this.allMovesNumber) {
			//Check if node is fully expanded.
			if(node.getUntriedMoves().size() != 0) {
				//Not fully expanded. Return a newly created node.
				Node newNode =  node.expand(board, this.color);
				return newNode;
			} else {
				//Node is fully expanded. Get color of currently investigated 
				//node.
				String color = node.getColor();
				
				//Select a child for which Tree Policy would be applied again. 
				//bestChild method relies on Boltzmann's distribution and it 
				//non-deterministic.
				try {
					node = bestChild(node, this.c);
				} catch(Exception e) {
					//Node is a terminal state.
					return node;
				}
				
				//Update a board of a move from selected node.
				board.makeMove(node.getMove(), color);
			}
		}
		return node;
	}
	
	/**
	 * This method conducts self-played game with random moves until the board
	 * is completely fulfilled. Valid moves for Monte Carlo's color are 
	 * narrowed down to the best 5 in accordance to the heuristic evaluation. 
	 * @param node Node from which simulation kicks off.
	 * @param board Board with initial position.
	 * @return winning side: "w" for white, "b" for black, "0" for draw.
	 * @throws Exception I don't remember. It has never occurred.
	 */
	private String defaultPolicy(Node node, Board board) throws Exception {
		Random generator = new Random();
		String color = node.getColor();
		int moveNumber = node.getMoveNumber();

		//Check if terminal state hasn't been reached. If not play next move.
		while(moveNumber < this.allMovesNumber) {
			List<Tuple<Integer, Integer>> listValidMoves;
			
			//Check who's turn is now.
			if(color.equals(this.color)) {
				//Narrow list of valid moves to the best 5 in accordance to the 
				//heuristic evaluation.
				listValidMoves = board.heuristic_bestX_moves(color, 5);
			} else {
				//Provide a list of all valid moves.
				listValidMoves = board.getListValidMoves();
			}
			
			//Select at random from given selection a move, and make it.
			board.makeMove(listValidMoves.get(generator.nextInt(
					listValidMoves.size())), color);
			
			//Switch the colors.
			if(color.equals("w")) {
				color = "b";
			} else {
				color = "w";
			}
			
			//Increment the move's counter.
			++moveNumber;
		}
		return Rules.calculateScore(board);
	}
	
	/**
	 * Propagate the outcome of the simulation from the leaf (given node) up to 
	 * the root.
	 * @param A node from which a default policy kicked off (a new leaf).
	 * @param delta The outcome of the simulation (w/b/0).
	 */
	private void back_up(Node node, String delta) {
		double value;
		
		//Assign numeric value based on the outcome of simulation and color of 
		//the move (whether this move is good for MC or not).
		if(delta.equals("0")) {
			value = .5;
		} else if(delta.equals(node.getColor())) {
			value = 0;
		} else {
			value = 1;
		}
		
		//Until the root is not reached update value and counter of visit of 
		//each visited node and go to its parent.
		while(node != null) {
			node.updateValue(value);
			node.updateVisit();
			
			//If not a draw, reverse the value (0 -> 1 or 1 -> 0).
			if(value != .5) {
				value = (value + 1) % 2;
			}
			
			//Go up in the tree (to the parent).
			node = node.getParent();
		}
	}
	
	/**
	 * Select the best child of given node.
	 * @param node The best child is selected among all children of this node.
	 * @param c Constant C (when =0 the the most robust child is selected).
	 * @return Best node.
	 */
	private Node bestChild(Node node, double c) {
		Node bestChild = null;
		double tempScore = -1;
		for(Node child: node.getChildren()) {
			double score = (child.getValue() / child.getVisit()) + 
					(c * Math.sqrt((2 * Math.log(node.getVisit())) / 
							(child.getVisit())));
			if(score >= tempScore) {
				bestChild = child;
				tempScore = score;
			}
		}
		return bestChild;
	}
}
