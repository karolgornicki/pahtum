package ai.mch5boltzmann_t15;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.Tuple;
import core.Board;
import core.Engine;
import core.Rules;

/**
 * Monte-Carlo Tree Search with narrowed scope to the best 5 plays in
 * accordance to the Charles_2's heuristic. It uses Bolztmann's distribution
 * in order to select child node for further exploration. 
 * @author kg687
 *
 */
public class MonteCarloH5Boltzmann_t15 implements Engine {
	private Board board;			//Board which holds starting position.
	private String color;			//MC's color.
	private Root root;				//Root of the game tree.
	private int allMovesNumber;		//Number of moves to make till the game 
	//reaches a terminate state (ends) -- counts from the very beginning of the 
	//game. Basically is's 49 minus number of black holes.
	
	/**
	 * Create engine's object and initialize main parameters.
	 * @param board Board with an initial state.
	 * @param color Monte-Carlo's color.
	 * @param allMovesNumber Number of remaining moves till the game reaches a 
	 * terminate state (ends)
	 * @param root 
	 */
	public MonteCarloH5Boltzmann_t15(Board board, String color, int moveNumber, 
			int allMovesNumber) {
		this.board = board;
		this.color = color;
		this.allMovesNumber = allMovesNumber;
		this.root = new Root(new Node(null, null, color, board, moveNumber));
	}
	
	/**
	 * Run UCT-alike algorithm which selects the best move. The algorithm is 
	 * based on Boltzmann's probability distribution formula.
	 * @param rollOuts Number of roll-outs (computational limitation). Describes
	 * how many simulations run beforehand the selection of the best move.
	 * @return Best move as a tuple with coordinates.
	 * @throws Exception Don't remember, will check later!
	 */
	public Tuple<Integer, Integer> uct(int rollOuts) throws Exception {
		while(rollOuts > 0) {
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
			backUp(node, delta);
			
			--rollOuts;
		}
		return getHighestQualityChild(root.getRoot()).getMove();
	}
	
	/**
	 * Expand tree of a new node. <p>
	 * In order to do so it employs bestChild method which relay on Boltzmann's
	 * probability distribution and is non-deterministic (applicable only to 
	 * fully expanded nodes). While going deeper into the game tree the board 
	 * becomes more populated and resembles to the situation from the last 
	 * visited node.
	 * @param node Node which corresponds to the initial state (from which Tree 
	 * Policy kicks off).
	 * @param board A board on which moves which corresponds to each traversed 
	 * node are played (as we go deeper the board populates more).
	 * @return A new node.
	 * @throws Exception 
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
					node = bestChild(node);
				} catch(Exception e) {
					//node is a terminal state.
					return node;
				}
				
				//Update a board of a move from selected node.
				board.makeMove(node.getMove(), color);
			}
		}
		return node;
	}
	
	/**
	 * Conduct self-played random game until the board is fully populated. 
	 * @param node Node from which simulation kicks off.
	 * @param board Board with stating position.
	 * @return Winning side: "w" for white, "b" for black, "0" for draw.
	 * @throws Exception Don't remember... it doesn't occur.
	 */
	private String defaultPolicy(Node node, Board board) throws Exception {
		Random generator = new Random();
		String color = node.getColor();
		int moveNumber = node.getMoveNumber();

		//Check if terminal state hasn't been reached. If not play next move.
		while(moveNumber < this.allMovesNumber) {
			List<Tuple<Integer, Integer>> listValidMoves;
			
			//Narrow list of valid moves to the best 5 in accordance to the 
			//heuristic function.
			listValidMoves = board.heuristic_bestX_moves(color, 5);
			
			//Select at random from given selection a move, and make it.
			board.makeMove(listValidMoves.get(generator.nextInt(
					listValidMoves.size())), color);
			
			//Switch the colors.
			color = color.equals("w") ? "b" : "w";
			
			//Increment the move's counter.
			++moveNumber;
		}
		
		//The simulation reached the terminate state, return the outcome of a 
		//game (who win: "w"/"b"/"0").
		return Rules.calculateScore(board);
	}
	
	/**
	 * Propagate the outcome of the simulation from the leaf (given node) up to 
	 * the root.
	 * @param node A node from which a default policy kicked off (a new leaf).
	 * @param delta The outcome of the simulation (w/b/0).
	 */
	private void backUp(Node node, String delta) {
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
	 * Select child of given node with the highest value (quality).
	 * @param node Node amongst which child selection have to take place.
	 * @return Best node.
	 */
	private Node getHighestQualityChild(Node node) {
		Node bestChild = null;
		double tmpQuality = -1;
		
		//Check all children.
		for(Node child : node.getChildren()) {
			if(child.getValue()  > tmpQuality) {
				tmpQuality = child.getValue();
				bestChild = child;
			}
		}
		return bestChild;
	}
	
	/**
	 * Select best child based on Boltzmann's probability distribution. 
	 * <p> The probability assigned to each candidate play is based on potential
	 * calculated by the heuristic function. 
	 * <p> In order to calculate exact probability constant T = 2.5 was used.
	 * <p> Attention: The name is a bit misleading - it's not best child as a 
	 * matter of fact.
	 * @param node Node amongst child "the best one" has to be selected.
	 * @return
	 */
	private Node bestChild(Node node) {
		double t = 1.5;
		Node selectedChild = null;
		Random generator = new Random();
		double bestFitProb = 100;
		
		//Get random double in [0,1] range. 
		double randomNumber = generator.nextDouble();
		
		//X. Make sure all children has probability assigned.
		for(Node child : node.getChildren()) {
			double sum = 0;
			for(Node kid : node.getChildren()) {
				sum += Math.exp(kid.getPotential() / t);
			}
			if(child.getProbability() == -1) {
				child.setProbability((Math.exp(child.getPotential() / t)) / 
						(sum));
			}
		}//end X.
		
		//Y. Organize nodes in a list.
		List<Tuple<Double, Node>> organizedChildren = 
			new ArrayList<Tuple<Double, Node>>();
		List<Node> candidateChildren = new ArrayList<Node>();
		for(Node item : node.getChildren()) {
			candidateChildren.add(item);
		}
		
		while(candidateChildren.size() != 0) {
			double tmpProbability = 99999999;
			Node tmpChild = null;
			
			for(Node item : candidateChildren) {
				double currentProbability = item.getProbability();
				if(currentProbability < tmpProbability) {
					tmpProbability = currentProbability;
					tmpChild = item;
				}
			}
			candidateChildren.remove(tmpChild);
			
			double summedProbabilities = 0;
			if(!organizedChildren.isEmpty()) {
				summedProbabilities = organizedChildren.get(
						organizedChildren.size()-1).getFirstElement();
			}

			organizedChildren.add(new Tuple<Double, Node>(
					summedProbabilities + tmpProbability, tmpChild));
		}//end Y.

		//Select which child is associated with a range that satisfies randomly 
		//picked number.
		for(Tuple<Double, Node> item : organizedChildren) {
			if(randomNumber < item.getFirstElement()) {
				if(item.getFirstElement() < bestFitProb) {
					bestFitProb = item.getFirstElement();
					selectedChild = item.getSecondElement();
				}
			}
		}
		
		//Return selected child.
		return selectedChild;
	}
}
