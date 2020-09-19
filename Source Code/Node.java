// Luke Britton August 2017
import java.util.*;
public class Node<T>
{
	
	private ArrayList<Node<T>> children;
	private ArrayList<Integer> capacities = new ArrayList<Integer>();
	private ArrayList<Integer> flow = new ArrayList<Integer>();
	private Node<T> parent;
	private T label;
	private int height;
	private boolean isRoot = false;
	private boolean isSink = false;
	private boolean isSource = false;
	private boolean isMarked = false;
	private ArrayList<Node<T>> parents; 
	ArrayList<Node<T>> markedChildren;
	private int blockNo = -1;
	
	
	// We have six constructors in this class
	// This is because we use class for both the enumeration tree and the rotations graph
	public Node()
	{
		parents = new ArrayList<Node<T>>();
		children = new ArrayList<Node<T>>();
	}
	
	public Node(T label)
	{
		this.label = label;
		parents = new ArrayList<Node<T>>();
		children = new ArrayList<Node<T>>();
	}
	
	/* Each node (excluding the root) is labelled with the rotationNo associated with the edge 
	between it and it's parent */
	public Node(Node<T> parent, T label)
	{
		isRoot = false;
		this.parent = parent;
		this.label = label;
		children = new ArrayList<Node<T>>();
		
	}
	
	public Node(Node<T> parent, T label, int h)
	{
		isRoot = false;
		this.parent = parent;
		this.label = label;
		height = h;
		children = new ArrayList<Node<T>>();
		
	}
	
	public Node(ArrayList<Node<T>> parents)
	{
		isRoot = false;
		this.parents = parents;
		children = new ArrayList<Node<T>>();
		
	}
	public Node(ArrayList<Node<T>> parents, T label)
	{
		isRoot = false;
		this.parents = parents;
		this.label = label;
		children = new ArrayList<Node<T>>();
	}
	
	public T getLabel()
	{
		if (isRoot)
			throw new IllegalArgumentException("Root node has no label");
			
		return label;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	
	public void addChild(Node<T> n)
	{
		children.add(n);
		capacities.add(-1);
		flow.add(0);
	}
	
	public void addChild(Node<T> n, int capacity)
	{
		if (capacity < 0)
			throw new IllegalArgumentException("Can't add a negative capacity");
			
		children.add(n);
		capacities.add(capacity);
		flow.add(0);
	}
	
	public boolean hasChildren()
	{
		if (children.size() == 0)
			return false;
			
		return true;
	}
	
	
	public ArrayList<Node<T>> getChildren()
	{
		return children;
	}
	
	
	public void addParent(Node<T> p)
	{
		if (isRoot)
			throw new IllegalArgumentException("Can't add parents to the root");
			
		parents.add(p);
	
	}
	
	public Node<T> getParent()
	{
		if (isRoot)
			throw new IllegalArgumentException("Root node has no parents");
			
		return parent;
	}
	
	public ArrayList<Node<T>> getParents()
	{
		return parents;
	}

	
	public boolean hasParents()
	{
		if (parents.size() == 0)
			return false;
			
		return true;
	}
	
	public int getNoOfChildren()
	{
		return children.size();
	}
	
	
	public void mark()
	{
		isMarked = true;
	}
	
	public void unmark()
	{
		isMarked = false;
	}
	
	public boolean isMarked()
	{
		return isMarked;
	}
	
	public void markAsRoot()
	{
		height = 1;
		isRoot = true;
	}
	
	public boolean isRoot()
	{
		return isRoot;
	}
	
	public void markAsSink()
	{
		isSink = true;
	}
	
	public void markAsSource()
	{
		isSource = true;
	}
	
	public boolean isSink()
	{
		return isSink;
	}
	
	public boolean isSource()
	{
		return isSource;
	}
	
	public boolean isLeaf()
	{
		if (children.size() == 0)
			return true;
			
		return false;
	}
	

	public void storeMarkedChildren()
	{
		int noOfChildren = children.size();
		markedChildren = new  ArrayList<Node<T>>();
		
		if (hasChildren())
		{
			for (int i = 0; i < noOfChildren; i++)
				if (children.get(i).isMarked())
					markedChildren.add(children.get(i));			
		}
		
	}
	
	public ArrayList<Node<T>> getMarkedChildren()
	{
		if (markedChildren == null)
			throw new IllegalArgumentException("Can't get unmarked children until marked children have been eliminated.");
			
		return markedChildren;
	}
	
	public boolean hasMarkedChildren()
	{
		if (markedChildren == null)
			throw new IllegalArgumentException("Can't get unmarked children until marked children have been eliminated.");
			
		if (markedChildren.size() == 0)
			return false;
			
		return true;
	}
	
	public boolean hasChild(int index)
	{
		if (index < 0 || index >= children.size())
			return false;
			
		return true;
	}
	
	
	
	public boolean hasParent(int index)
	{
		if (index < 0 || index >= parents.size())
			return false;
			
		return true;
	}
	
	
	public Node<T> getChild(int index)
	{
		if (!hasChild(index))
			throw new IllegalArgumentException("No child at this index");
			
		return children.get(index);
			
	}
	
	public boolean hasChild(Node<T> c)
	{
		int index = children.indexOf(c);
		
		if (index == -1)
			return false;
			
		return true;
			
	}
	
	public int getChildIndex(Node<T> c)
	{
		if (!hasChild(c))
			throw new IllegalArgumentException("Child does not exist");
			
		return children.indexOf(c);
	}
	
	public Node<T> getParent(int index)
	{
		if (!hasParent(index))
			throw new IllegalArgumentException("No parent at index " + index);
			
		return parents.get(index);
	}
	
	
	public boolean hasCapacity(int index)
	{
		if (!hasChild(index))
			throw new IllegalArgumentException("No child at this index");
			
		if (capacities.get(index) == -1)
			return false;
			
		return true;
	}
	public int getCapacity(int index)
	{
		if (!hasChild(index))
			throw new IllegalArgumentException("No child at this index");
			
		if (!hasCapacity(index))
			throw new IllegalArgumentException("This edge has no capacity.");
			
		return capacities.get(index);
	}
	
	public int getResidualCapacity(int index)
	{
		if (!hasChild(index))
			throw new IllegalArgumentException("No child at this index");
			
		 if (!hasCapacity(index))
			throw new IllegalArgumentException("This edge has no capacity.");
			
		return (getCapacity(index) - getFlow(index));
	}
	
	public int getRetractCapacity(int index)
	{
		if (!hasParent(index))
			throw new IllegalArgumentException("No parent at index " + index);
			
		Node<T> p = parents.get(index);
		int childIndex = p.getChildIndex(this);
		
		return p.getFlow(childIndex);
	}
	
	
	
	public int getFlow(int index)
	{
		if (!hasChild(index))
			throw new IllegalArgumentException("No child at this index");
			
		return flow.get(index);
	}

	
	public boolean canPushFlow()
	{
		int noOfChildren = children.size();
		
		for (int i = 0; i < noOfChildren; i++)
		{
			if (!hasCapacity(i))
				return true;
			
			if (getCapacity(i) > getFlow(i))
				return true;
		}
			
		return false;
	}
	
	
	public boolean canPushFlow(int index)
	{
		 if (!hasChild(index))
			throw new IllegalArgumentException("No child at this index");
			
		if (!hasCapacity(index))
			return true;
			
		if (getCapacity(index) > getFlow(index))
				return true;
				
		return false;
	}
	
	public boolean canRetractFlow(int index)
	{
		if (!hasParent(index))
			throw new IllegalArgumentException("No parent at this index");
		
		if (isSource || isSink)
			return false;
					
		Node<T> p = parents.get(index);
		int childIndex = p.getChildIndex(this);
		
		if (p.getFlow(childIndex) > 0)
			return true;
			
		return false;
	}
	
	public boolean canReceiveFlow()
	{
		int noOfParents = parents.size();
		
		for (int i = 0; i < noOfParents; i++)
			if (getParent(i).canPushFlow())
				return true;
				
		return false;
	}
	
	public void pushFlow(int index, int f)
	{
		if (!hasChild(index))
			throw new IllegalArgumentException("No child at this index");
			
		if (hasCapacity(index) && getCapacity(index) < f)
			throw new IllegalArgumentException("Error! Flow exceeds capacity!");
			
		int curFlow = flow.get(index);
		flow.set(index, curFlow + f);
	}
	
	public void retractFlow(int index, int f)
	{
		if (!hasChild(index))
			throw new IllegalArgumentException("No parent at this index");
		
		if (hasCapacity(index) && getCapacity(index) < f)
			throw new IllegalArgumentException("Error! Flow exceeds capacity!");
			
		int curFlow = flow.get(index);
		flow.set(index, curFlow - f);
		
	}
	
	
	public boolean hasSinkChild()
	{
		if (!hasChildren())
			return false;
		
		int noOfChildren = children.size();
		if (children.get(noOfChildren - 1).isSink())
			return true;
			
		return false;
	}
	
	public int getSinkChildIndex()
	{
		if (!hasSinkChild())
			throw new IllegalArgumentException("Node doesn't have a sink child");
			
		return (children.size() - 1);
	}
	
	
	public boolean hasSourceParent()
	{
		if (!hasParents())
			return false;
			
		int noOfParents = parents.size();
		if (parents.get(noOfParents - 1).isSource())
			return true;
			
		return false;
	}
	
	 
	public void addBlockNo(int no)
	{
		blockNo = no;
	}
	
	public int getBlockNo()
	{
		if (blockNo == -1)
			throw new IllegalArgumentException("Error! No blockNo has been added to this node.");
			
		return blockNo;
	}
}