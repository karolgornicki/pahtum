package crashtest;

import ai.minimax.MiniMax;
import core.Board;

public class test_minimax {
	public static void main(String[] args) throws Exception {
		Board b = new Board(3, false, 0);
		MiniMax mm = new MiniMax("b", b);
		System.out.println(mm.getMove().toString());
	}
}
