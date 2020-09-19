// Luke Britton August 2017
public class Edge<T>
{
	
	private Node<T> node1;
	private Node<T> node2;
	private int capacity = 0;
	private int flow = 0;
	
	// Each edge is made up of two nodes and a rotation label
	public Edge(Node<T> node1, Node<T> node2)
	{
		this.node1 = node1;
		this.node2 = node2;
	}
	
	public Node getFirstNode()
	{
		return node1;
	}
	
	public Node getSecondNode()
	{
		return node2; 
	}
	
	
	public void addCapacity(int cap)
	{
		if (cap < 0)
			throw new IllegalArgumentException("Can't add a negative capacity");
			
		capacity = cap;
	}
	
	public void addFlow(int f)
	{
		if (f < 0)
			throw new IllegalArgumentException("Can't add a negative flow.");
			
		if (f > capacity)
			throw new IllegalArgumentException("Error! Flow is greater than capacity");
			
		flow
	}
	
	public int getCapacity()
	{
		return capacity;
	}
	
}