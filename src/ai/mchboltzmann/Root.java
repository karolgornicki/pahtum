package ai.mchboltzmann;

/**
 * Represents root of the game tree.
 * @author kg687
 *
 */
public class Root {
	private Node root;

	/**
	 *Create a root and bond it with a given node.
	 * @param node Node with initial stage.
	 */
	public Root(Node node) {
		this.root = node;
	}
	
	/**
	 * @return the root
	 */
	public Node getRoot() {
		return root;
	}
}
