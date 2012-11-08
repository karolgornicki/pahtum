package util;

public class Tuple<A, B> {
	
	private final A value1;
	private final B value2;
	
	public Tuple(final A value1, final B value2) {
		this.value1 = value1;
		this.value2 = value2;
	}
	
	public A getFirstElement() {
		return this.value1;
	}
	
	public B getSecondElement() {
		return this.value2;
	}
	
	@Override
	public String toString() {
		return "(" + this.value1.toString() + ", " + this.value2.toString() + ")";
	}
}