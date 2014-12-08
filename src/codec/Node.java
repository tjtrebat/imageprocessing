package codec;

public class Node implements Comparable<Node> {

	private int frequency;
	private Object symbol;
	private Node leftChild, rightChild;

	public Node() {
		this.frequency = 0;
		this.symbol = null;
	}

	public Node(Object symbol, int frequency) {
		this.symbol = symbol;
		this.frequency = frequency;
	}

	public boolean isLeaf() {
		return this.getLeftChild() == null && this.getRightChild() == null;
	}

	@Override
	public int compareTo(Node o) {
		return new Integer(this.getFrequency()).compareTo(o.getFrequency());
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public Object getSymbol() {
		return symbol;
	}

	public void setSymbol(Object symbol) {
		this.symbol = symbol;
	}

	public Node getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(Node leftChild) {
		this.leftChild = leftChild;
	}

	public Node getRightChild() {
		return rightChild;
	}

	public void setRightChild(Node rightChild) {
		this.rightChild = rightChild;
	}

}
