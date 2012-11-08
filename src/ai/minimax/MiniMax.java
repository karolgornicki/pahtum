package ai.minimax;

import util.Tuple;
import core.Board;

public class MiniMax {
	static String color;
	private Root root;
	
	public MiniMax(String color, Board board ) throws Exception {
		this.root = new Root(new MMNode(null, null, board, color));
//		Node.back_propagate(root.getRoot());
		MMNode.update_tree(this.root.getRoot());
	}
	
	public Tuple<Integer, Integer> getMove() {
		return this.root.getRoot().get_best_move();
	}
}
