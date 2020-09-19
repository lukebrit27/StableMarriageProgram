// Luke Britton August 2017
/* We use this class to construct enumeration trees which can be used to count the number of 
   stable marriages */
import java.util.*;
public class enumerationTree
{
	private Node<Rotation> root;
	private LinkedList<Rotation> rotations; 
	private int noOfRotations;
	
	// Each enumeration tree has a list of rotations inputted which we will use to label the edges
	// Each node in the enumeration tree is labelled by a unique stable marriage
	/* The rotations on a path from the root to some node are the rotations we have to eliminate
	   starting at the male optimal marriage to get the stable marriage labelling that node */
	public enumerationTree(LinkedList<Rotation> rotations) 
	{
       	this.rotations = rotations;
       	noOfRotations = rotations.size();
        root = new Node<Rotation>();
        initialiseRoot();
        buildTree(root);
       
    }
    
    // Add child nodes to the the root and label the edges with rotations with no predecessors
    private void initialiseRoot()
    {
		root.markAsRoot();
		
		for (int i = 0; i < noOfRotations; i++)
		{
			if (!rotations.get(i).hasPredecessors())
				root.addChild(new Node<Rotation>(root, rotations.get(i), root.getHeight() + 1));
		}
    }
   	
   	
   	// Get all the rotations labelling edges along the path from the root to a node
   	private LinkedList<Rotation> getAncestorLabels(Node<Rotation> node)
   	{
   		LinkedList<Rotation> ancestors = new LinkedList<Rotation>();
   		
   		if (node.isRoot())
   			throw new IllegalArgumentException("Error! The root node has no ancestors");
   			
   		if (node.getParent().isRoot())
   		{
   			ancestors.add(node.getLabel());	
   		}
   		
   		else
   		{
   			ancestors = getAncestorLabels(node.getParent());
   			ancestors.add(node.getLabel());
   		}
   			
   		return ancestors;
   	}
   	
   	
   	// This is a recursive procedure, the initial start node is the root
    private void buildTree(Node<Rotation> startNode)
    {
    	// check if the start node has children
		// This is the termination condition of the recursion
    	if (startNode.hasChildren())
    	{
    	
    		// Get the children of the start node
			/* Note that the root node is already initialised before the start of 
			this procedure */
    		ArrayList<Node<Rotation>> children = startNode.getChildren();
    		int noOfChildren = children.size();
    	
			for (int i = 0; i < noOfChildren; i++)
			{
				Node<Rotation> curNode = children.get(i);
				Rotation curLabel = curNode.getLabel();
				int curLabelNo = curLabel.getRotationNo();
				
				/* Get the rotation labels along the path from the root node
				   to the current node */
				LinkedList<Rotation> ancestorLabels = getAncestorLabels(curNode);
				
				/* Rotations have been labelled numerically according to the 
				   topological ordering of the Rotations graph. So we only need
				   to check rotations with a larger label. */
				for (int j = curLabelNo+1; j < noOfRotations; j++)
				{
					/* if rotation j doesn't have any predecessors excluding it's
					   ancestors add it to the current node */ 
					if (!rotations.get(j).hasPredecessors(ancestorLabels))
						curNode.addChild(new Node<Rotation>(curNode,rotations.get(j),curNode.getHeight() + 1));
				}
				
				// recursive statement
				if (curNode.hasChildren())
					buildTree(curNode);
			}	
		}
    }
    
    
    // Method which recursively counts the nodes in our enumeration tree
    // Once we have built the enumeration tree we use this method to count the number of stable marriages
    private int countNodes(Node<Rotation> startNode)
    {
    	int count = 0;
    	
    	if (startNode.isRoot())
    		count = 1;
    	
    	if(startNode.hasChildren())
    	{
			ArrayList<Node<Rotation>> children = startNode.getChildren();
			int noOfChildren = children.size();
			Node<Rotation> curNode;
		
			if (startNode.isRoot())
				count += noOfChildren;
			
			else
				count = noOfChildren;
		
			for (int i = 0; i < noOfChildren; i++ )
			{
				curNode = children.get(i);
			
				if (curNode.hasChildren())
					count += countNodes(curNode);		
			}
		}
		
    	
    	return count;
    }
    
    // Returns the number of nodes in the enumeration treee
    public int getNoOfNodes()
    {
    	int noOfNodes = countNodes(root);
    	return noOfNodes;
    }
    
    // method which recursively gets the height of the enumeration tree
    // useful for when we draw the enumeration tree in the drawETree class
    public int getHeight(Node<Rotation> startNode)
    {
    	int maxHeight = startNode.getHeight();
    	
    	
    	if (startNode.hasChildren())
    	{
    		int i = 0;
    		while (startNode.hasChild(i))
    		{
    			maxHeight = Math.max(maxHeight, getHeight(startNode.getChild(i)));
    			i++;
    		}
    	}
    	
    	return maxHeight;
    }
    
  
    
    public Node<Rotation> getRoot()
    {
    	return root;
    }
    
    
}