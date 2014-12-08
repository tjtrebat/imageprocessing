package codec;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;

public class Huffman {

	private Queue<Node> queue;
	private Map<Object, String> prefixCodes;

	public Huffman() {
		this.queue = new PriorityQueue<Node>();
		this.prefixCodes = new HashMap<Object, String>();
	}

	public boolean addSymbol(Object symbol) {
		// a node for the symbol
		Node node = null;
		// attempt to retrieve Node with symbol
		Iterator<Node> iterator = this.getQueue().iterator();
		while (iterator.hasNext()) {
			Node n = iterator.next();
			if (symbol.equals(n.getSymbol()))
				node = n;
		}
		// add node to queue if not present
		if (node == null) {
			node = new Node(symbol, 1);
			this.getQueue().add(node);
			return true;
		}
		// update the node's frequency
		node.setFrequency(node.getFrequency() + 1);
		return false;
	}

	public Node buildTree() {
		int n = this.getQueue().size();
		for (int i = 0; i < n - 1; i++) {
			Node node = new Node();
			node.setLeftChild(this.extractMin());
			node.setRightChild(this.extractMin());
			node.setFrequency(node.getLeftChild().getFrequency()
					+ node.getRightChild().getFrequency());
			this.getQueue().add(node);
		}
		return this.extractMin();
	}

	public Node extractMin() {
		return this.getQueue().poll();
	}

	public void buildCodeTable(Node node, String prefixCode) {
		if (node.isLeaf())
			this.getPrefixCodes().put(node.getSymbol(), prefixCode);
		else {
			this.buildCodeTable(node.getLeftChild(), prefixCode + "0");
			this.buildCodeTable(node.getRightChild(), prefixCode + "1");
		}
	}

	public Queue<Node> getQueue() {
		return queue;
	}

	public void setQueue(Queue<Node> queue) {
		this.queue = queue;
	}

	public Map<Object, String> getPrefixCodes() {
		return prefixCodes;
	}

	public void setPrefixCodes(Map<Object, String> prefixCodes) {
		this.prefixCodes = prefixCodes;
	}

	public static void main(String[] args) {
		Node[] nodes = new Node[] { new Node("a", 45), new Node("b", 13),
				new Node("c", 12), new Node("d", 16), new Node("e", 9),
				new Node("f", 5) };
		Huffman h = new Huffman();
		for (Node node : nodes)
			h.getQueue().add(node);
		Node root = h.buildTree();
		System.out.println(root.getFrequency());
		h.buildCodeTable(root, "");
		for (Entry<Object, String> entry : h.getPrefixCodes().entrySet())
			System.out.println(String.format("%s: %s", entry.getKey()
					.toString(), entry.getValue()));
	}

}
