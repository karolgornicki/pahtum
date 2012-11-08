package core;

import util.Tuple;

public interface Engine {
	Tuple<Integer, Integer> uct(int n) throws Exception;
}
