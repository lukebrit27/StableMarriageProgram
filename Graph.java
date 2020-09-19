// Luke Britton August 2017
/* In this class we encapsulate the rotations graph which is represents the partial order 
   over the rotations. It can be extended to network flow graph that we will use to find
   the egalitarian stable marriage */
import java.util.*;
public class Graph
{
	private Node<Rotation> source = new Node<Rotation>();
	private Node<Rotation> sink = new Node<Rotation>();
	private ArrayList<Node<Rotation>> nodes = new ArrayList<Node<Rotation>>();
	private boolean[] visited;
	private ArrayList<Integer> minCut = new ArrayList<Integer>();
	private boolean pathExists = true;
	private boolean maxFlowFound = false;
	private int destination;
	private int notFound;
	private LinkedList<Rotation> rotations;
	private int noOfRotations;
	private boolean nodesMarked = false;
	private ArrayList<Integer> curPath = new ArrayList<Integer>();
	

	
	// Each graph has an inputted list of rotations 
	public Graph(LinkedList<Rotation> rotations)
	{
		this.rotations = rotations;
		noOfRotations = rotations.size();
		visited = new boolean[noOfRotations + 1];
		destination = noOfRotations;
		notFound = destination + 2;
		
		// Initialise the nodes, each of which are labelled by a unique rotation
		/* Initialise the source and sink so we can convert the rotations graph into a 
		   network flow graph. */
		initialiseNodes();
		initialiseSource();
		initialiseSink();
		
	}
	
	
	/* Initialise nodes by creating a node object for each unique rotation of the problem, 
	   adding parent nodes which are labelled by a rotations predecessor and adding 
	   child nodes which are labelled by a rotations successor */
	private void initialiseNodes()
	{
		for (int i = 0; i < noOfRotations; i++)
		{
			nodes.add(new Node<Rotation>(rotations.get(i)));
			visited[i] = false;
		}
			
		Node<Rotation> curNode;
		Node<Rotation> successorNode;
		LinkedList<Rotation> successors = new LinkedList<Rotation>();
		int noOfSuccessors;
		
		for (int i = 0; i < noOfRotations; i++)
		{
			curNode = nodes.get(i);
			
			if (rotations.get(i).hasSuccessors())
			{
				successors = rotations.get(i).getSuccessors();
				noOfSuccessors = successors.size();
				
				for (int j = 0; j < noOfSuccessors; j++)
				{
					successorNode = nodes.get(successors.get(j).getRotationNo());
					curNode.addChild(successorNode);
					successorNode.addParent(curNode);
				}
			}
		}
		
	}
	
	
	// Method to initialise the source of the flow network
	// Any node with a negative weight has a directed edge from the source to itself
	// Each of these edges has a capacity equal to the absolute value of the weight
	private void initialiseSource()
    {
		source.markAsSource();
		ArrayList<Integer> negRotations = getNegRotations();
		int noOfNegRotations = negRotations.size();
		int capacity;
		Rotation curRotation;
		Node<Rotation> curNode;
		
		for (int i = 0; i < noOfNegRotations; i++)
		{
			curNode = nodes.get(negRotations.get(i));
			curRotation = rotations.get(negRotations.get(i));
			capacity = Math.abs(curRotation.getWeight());
			source.addChild(curNode, capacity);	
			curNode.addParent(source);
		}	
    }
    
    
    
    // Method to initialise the sink of the flow network
	// Any node with a positive weight has a directed edge from itself to the sink
	// Each of these edges has a capacity equal to the weight
    private void initialiseSink()
    {
    	sink.markAsSink();
    	ArrayList<Integer> posRotations = getPosRotations();
    	int noOfPosRotations = posRotations.size();
    	int capacity;
    	Rotation curRotation;
    	Node<Rotation> curNode;
    	
    	for (int i = 0; i < noOfPosRotations; i++)
    	{
    		curNode = nodes.get(posRotations.get(i));
    		curRotation = rotations.get(posRotations.get(i));
    		capacity = curRotation.getWeight();
    		sink.addParent(curNode);
    		curNode.addChild(sink, capacity);
    	}
    }
    
    
    // returns the rotations with a negative weight
    private ArrayList<Integer> getNegRotations()
    {
    	ArrayList<Integer> negRotations = new ArrayList<Integer>();
    	
    	for (int i = 0; i < noOfRotations; i++)
    		if (rotations.get(i).getWeight() < 0)
    			negRotations.add(rotations.get(i).getRotationNo());
    			
    	return negRotations;
    }
    
    // returns the rotations with a positive weight
    private ArrayList<Integer> getPosRotations()
    {
    	ArrayList<Integer> posRotations = new ArrayList<Integer>();
    	
    	for (int i = 0; i < noOfRotations; i++)
    		if (rotations.get(i).getWeight() > 0)
    			posRotations.add(rotations.get(i).getRotationNo());
    			
    		return posRotations;	
    }
    
    
	// We need this method to actually physically draw the rotations graph
	// Organises the nodes into blocks
	public ArrayList<ArrayList<Node<Rotation>>>  getBlocks()
	{
		ArrayList<ArrayList<Integer>> blocks = constructBlocks();
		ArrayList<ArrayList<Node<Rotation>>> nodeBlocks = new ArrayList<ArrayList<Node<Rotation>>>();
		
		int noOfBlocks = blocks.size();
		
		for (int i = 0; i < noOfBlocks; i++)
		{
			int blockSize = blocks.get(i).size();
			ArrayList<Node<Rotation>> curBlock = new ArrayList<Node<Rotation>>();
			for (int j = 0; j < blockSize; j++)
			{
				Node<Rotation> curNode = nodes.get(blocks.get(i).get(j));
				curBlock.add(curNode);
				curNode.addBlockNo(i);
			}
			nodeBlocks.add(curBlock);
		}
		
		return nodeBlocks;
	}
	
	// The first block includes all nodes that have no predecessors
	/* A node is in the second block includes iff that node is a successor of some node in
	   the first block  and that node doesn't have a predecessor which is 
	   not in the first block */
	/* A node is the third block iff that node is a successor of some node in
	   the second block  and that node doesn't have a predecessor which is 
	   not in the first block or second block, and so on until there are no more nodes to add */
   	private ArrayList<ArrayList<Integer>> constructBlocks()
   	{
   		ArrayList<ArrayList<Integer>> blocks = new ArrayList<ArrayList<Integer>>();
   		boolean[] addedToBlock = new boolean[noOfRotations];
   		ArrayList<Integer> curBlock = new ArrayList<Integer>();
   		int k = 0;
   		Rotation curRotation;
   		
   		
		for (int i = 0; i < noOfRotations; i++)
			addedToBlock[i] = false;
			
		if (noOfRotations > 0)	
			while (!rotations.get(k).hasPredecessors())
			{
				curBlock.add(k);
				addedToBlock[k] = true;
				k++;
			}
		
   		blocks.add(curBlock);
   		
   		for (int i = k; i < noOfRotations; i++)
   		{
   			int noOfBlocks = blocks.size();
   			int blockNo = -1;
   			curRotation = rotations.get(i);
   			
   			if (!curRotation.hasPredecessors())
   				blocks.get(0).add(i);
   				
   			else 
   			{
   				for (int j = 0; j < noOfBlocks; j++)
   				{
   					if (blockHasSuccessor(blocks.get(j), curRotation))
   						blockNo = j;
   				}
   			
				if (blockNo == noOfBlocks - 1)
					blocks.add(createNewBlock(i));
			
				else
					blocks.get(blockNo + 1).add(i);	
			}	
   		}
   				

   		return blocks;	
   		
   	}
   	
   	private ArrayList<Integer> createNewBlock(int element)
   	{
   		ArrayList<Integer> block = new ArrayList<Integer>();
   		block.add(element);
   		
   		return block;
   	} 
   	
   	// Check if some rotation/node in a block has a successor r
   	private boolean blockHasSuccessor(ArrayList<Integer> block, Rotation r)
   	{
   		int blockSize = block.size();
   		
   		for (int i = 0; i < blockSize; i++)
   		{
   			if (rotations.get(block.get(i)).hasSuccessor(r))
   				return true;
   		}
   		
   		return false;
   	}

   	// This method is recursive
	// start node is always the sink
	// Can only run this method once we have found the minimum cut
	/* Mark each node connected to the sink whose edge to the sink has not been cut 
	   by the minimum cut and also mark these nodes predecessors */
	// These marked nodes make up the maximum weighted downset
    private void markNodes(Node<Rotation> startNode)
    {
    	ArrayList<Node<Rotation>> parents;
    	int noOfParents;
    	
    	
    	if (startNode.hasParents())
    	{
    		parents = startNode.getParents();
    		noOfParents = parents.size();
    		
			for (int i = 0; i < noOfParents; i++)
			{
				
				if (parents.get(i).isMarked() || parents.get(i).isSource())
					continue;
					
				if (startNode.isSink() && isCut(parents.get(i)))
					continue;
					
				else
				{
					parents.get(i).mark();
					markNodes(parents.get(i));
				}
			}
    			
    	}
    	
    	nodesMarked = true;	
    }
    
    
    
    private void storeMarkedNodes()
    {
    	if (!nodesMarked)
    		throw new IllegalArgumentException("Can't eliminate rotations until the nodes have been marked!");
    		
    	
    	for (int i = 0; i < noOfRotations; i++)
    		if (nodes.get(i).hasChildren())
    			nodes.get(i).storeMarkedChildren();	
    		
    }
    
   
   	
   	private ArrayList<Integer> getMarkedNodes()
   	{
   		
   		ArrayList<Integer> markedNodes = new ArrayList<Integer>();
   		for (int i = 0; i < noOfRotations; i++)
   			if (nodes.get(i).isMarked())
   				markedNodes.add(i);
   				
   		return markedNodes;
   		 	
   	}
   	
  	public LinkedList<Rotation> getMaxWeightedDownset()
  	{
  		markNodes(sink);
  		storeMarkedNodes();
  		
  		LinkedList<Rotation> downset = new LinkedList<Rotation>();
  		ArrayList<Integer> markedNodesIndexes = getMarkedNodes();
  		int size = markedNodesIndexes.size();
  		
  		int index;
  		
  		for (int i = 0; i < size; i++)
  		{
  			index = markedNodesIndexes.get(i);
  			downset.add(rotations.get(index));
  		}
  		
  		return downset;
  	} 	
   	
	
	
	private void resetVisited()
	{
		for (int i = 0; i <= noOfRotations; i++)
			visited[i] = false;
	}
	/* Since all edges in the network have infinite capacity except those connected to the sink or
	   source, we can check if a path exists simply by checking the capacity of the edges at the 
	   sink and source */
    private boolean pathExists()
    {
    	if (source.canPushFlow() && sink.canReceiveFlow())
    		return true;
    		
    	return false;	
    }
    
    /* Note if findingMinCut=true this method will find the minimum cut
       given we have already found the maximum flow */
    /* An edge is in the min cut iff it goes from a node that can have flow pushed 
       through it to one that can't (again only given we've found the max flow) */
   	private int findNextNode(Node<Rotation> curNode, boolean findingMinCut)
   	{
   		int index = 0;
   		int num;
   		/* if curNode has a child node that is the sink we want to check it first since
   		   if we can push flow through it then we have found a path */
   		if (curNode.hasSinkChild())
   		{
   			/* if we can push flow through the sink, mark destination as reached and 
   			   return the sink's child index for curNode */
   			if (curNode.canPushFlow(curNode.getSinkChildIndex()))
   			{
   				index = curNode.getSinkChildIndex();
   				visited[destination] = true;
   				return (index + 1);
   			}
   			
   			/* else if we can't push flow through the sink and we are finding the min cut 
   			   then this edge is in the minimum cut */
   			else if (findingMinCut)
   			{
   				int i = curNode.getLabel().getRotationNo();
   				int j = curNode.getSinkChildIndex();
   				minCut.add(i);
   				minCut.add(j);	
   			}
   		}
   		// Go through each child node to check if we can push flow through it
   		while (curNode.hasChild(index))
   		{
			/* Already dealt with the sink above and the sink will be the last child alway
			   so we break from the loop */
			if (curNode.getChild(index).isSink())
				break;
			
			else
			{
				// get the label of the current child
				num = curNode.getChild(index).getLabel().getRotationNo();
				// if can push flow through current child return it's index and mark as visited
				if (curNode.canPushFlow(index) && !visited[num])
				{
					visited[num] = true;
					return (index + 1);
				}
			}
   			index++;
   		}
   		index = 0;
   		/* If we come up empty with the above loop then we look for an augmented path where we 
   		   move backwards along edges. Check each parent of the curNode to see if we can retract 
   		   flow */
   		while (curNode.hasParent(index) && !findingMinCut)
   		{
   			// If curNode has a parent which is the source we skip that parent
   			if (curNode.getParent(index).isSource())
   			{
   				index++;
   				continue;
   			}
   				
   			else
   			{
   				// get the label of the current parent
   				num = curNode.getParent(index).getLabel().getRotationNo();
   				/* if can retract flow through current parent return the negation of it's
   				   index and mark as visited. We return the negation of the index to
   				   indicate that we need to move backwards along the edge */
   				if (curNode.canRetractFlow(index) && !visited[num])
   				{
   					visited[num] = true;
   					return (-(index + 1));
   				}
   			}
   			
   			index++;
   		}
   		// Otherwise there are no more paths 
   		if (curNode.isSource())
   			pathExists = false;
   		return notFound;
   		
   	}
   	
   
    private  LinkedList<Integer> findPath(boolean findingMinCut)
    {
    	/* path holds the child index (parent index if augmented) of each node 
    	   in the path starting at the source */
    	// pathNodes holds the actual nodes involved in the path
    	Node<Rotation> curNode = source;
    	LinkedList<Integer> path = new LinkedList<Integer>();
    	LinkedList<Integer> pathNodes = new LinkedList<Integer>();
    	int num;
    	/* terminates either when we reach our destination (i.e. the sink) or
    	   when we can't find a path */
    	while(!visited[destination] && pathExists)
    	{
    		// find next node finds a node we can push flow through and returns it's index
    		/* Each time a node is found it is marked as visited. This is so we do not 
    		   end up in a loop where we are repeatedly visiting the same node. We reset
    		   all visited each time we find a path. */ 
    		num = findNextNode(curNode, findingMinCut);
    		// if no node is found we move backwards to the previous node in the path.
    		// not found is an unreachable number > than destination
    		if (num == notFound)
    		{
    			if (pathNodes.size() != 0)
    			{
					path.remove(path.size() - 1);
    				pathNodes.remove(pathNodes.size() - 1);
    				if (pathNodes.size() != 0)
    				{
						num = pathNodes.get(pathNodes.size() - 1);
						curNode = nodes.get(num);
					}
					else
						curNode = source;
				}
				else
					curNode = source;
			}
    		else if (!visited[destination])
    		{
    			// if num is negative it means we move backwards along the edge (augmented)
    			// Thus we store the parent index as opposed to the child index
    			if (num < 0)
    			{
    				int k = Math.abs(num) - 1;
    				curNode = curNode.getParent(k);
					pathNodes.add(curNode.getLabel().getRotationNo());
					path.add(num);	
    			}
    			// if num is positive we move forwards along 
    			else
    			{
					curNode = curNode.getChild(num - 1);
					pathNodes.add(curNode.getLabel().getRotationNo());
					path.add(num);	
				}					
    		}
    		
    		else 
    			path.add(num);		
    	}	
    	return path;	
    }

   	// returns the minimum capacity along some path
    private int getBottleneckFlow(LinkedList<Integer> path)
    {
    	
    	int pathLength = path.size();
    	int curIndex =	path.get(0) - 1;
    	int remainingCap = source.getResidualCapacity(curIndex);
    	Node<Rotation> curNode = source.getChild(curIndex);
    	int bottleneck = remainingCap;
    	
    	
    	for (int i = 1; i < pathLength; i++)
    	{
    		// if an edge along a path is augmented then we have to check how much flow we can retract
    		if (path.get(i) < 0)
    		{
				
				curIndex = Math.abs(path.get(i)) - 1;
				remainingCap = curNode.getRetractCapacity(curIndex);
				bottleneck = Math.min(bottleneck,remainingCap);
				curNode = curNode.getParent(curIndex);
			}
			
			// Otherwise check the remaining capacity
			else
			{
				curIndex = path.get(i) - 1;
				if (curNode.hasCapacity(curIndex))
				{
					remainingCap = curNode.getResidualCapacity(curIndex);
					bottleneck = Math.min(bottleneck,remainingCap);
				}
				curNode = curNode.getChild(curIndex);
			}
    		
    	}
    	
    	return bottleneck;
    }
    
    // Once we've found the bottleneck we push the flow through the path
    private void pushFlowThroughPath(LinkedList<Integer> path, int f)
    {
    	Node<Rotation> curNode = source;
    	Node<Rotation> parent;
    	int pathLength = path.size();
    	int curIndex;
    	
    	for (int i = 0; i < pathLength; i++)
    	{
    		// Once again we deal with an augmented edge differently by retracting flow 
    		if (path.get(i) < 0)
    		{
    			curIndex = Math.abs(path.get(i)) - 1;
    			parent = curNode.getParent(curIndex);
    			int childIndex = parent.getChildIndex(curNode);
				parent.retractFlow(childIndex, f);
				curNode = parent;

    		}
    		// Otherwise we push flow
    		else
    		{
				curIndex = path.get(i) - 1;
				curNode.pushFlow(curIndex, f);
				curNode = curNode.getChild(curIndex);
    		}
    	}
    }
    
    // Our implementation of the ford fulkerson algorithm
    public void FordFulkerson()
    {
    	LinkedList<Integer> curPath;
    	int flow;
    	
    	while (pathExists)
    	{
    		curPath = findPath(false);
    		if (!pathExists)
    			break;
    			
    		flow = getBottleneckFlow(curPath);
    		pushFlowThroughPath(curPath, flow);
    		resetVisited();	
    	}
    	resetVisited();
    	pathExists = true;
    	maxFlowFound = true;	
    }
    
    // get the maximum flow of the network flow graph 
    public int getMaxFlow()
    {
    	if (!maxFlowFound)
    		throw new IllegalArgumentException("Max flow not found.");
    		
    	int index = 0;
    	int maxFlow = 0;
    
    	
    	while (source.hasChild(index))
    	{
    		maxFlow += source.getFlow(index);
    	}
    	
    	return maxFlow;
    		
    }
    
    // find the minimum cut
    // Only works if we have already found the maximum flow
    public void findMinCut()
    {
    	if (!maxFlowFound)
    		throw new IllegalArgumentException("Max flow not found so can't find min cut.");
    		
    	int index = 0;
    	LinkedList<Integer> path;
    	// First we check if any source edges are in the min cut
    	while (source.hasChild(index))
    	{
    		// if can't push flow through to a source child add the source and child index to min cut
    		if (!source.canPushFlow(index))
    		{
    			// We use -1 to represent the source since we don't have an index for it
    			minCut.add(-1);
    			minCut.add(index);
    		}
    		index++;
    	}
    	
    	// This method finds the rest of the edges in the min cut
    	path = findPath(true);
    }
    
    // check if a node connected to has been cut by the minimum cut
    private boolean isCut(Node<Rotation> node)
    {
    	if (node.isSource() || node.isSink())
    		throw new IllegalArgumentException("Can't check if cut at the sink or source.");
    		
    	int nodeIndex = node.getLabel().getRotationNo();
    	int size = minCut.size();
    	int curNode;
    	
    	for (int i = 0; i < size; i+=2)
    	{
    		// If a min cut edge is connected to the source we check the child node of the edge
    		if (minCut.get(i) == -1)
    		{
    			curNode = source.getChild(minCut.get(i+1)).getLabel().getRotationNo();
    			if (curNode == nodeIndex)
    				return true;
    		}
    		
    		// Otherwise we check the parent node of the edge
    		// If an edge in the min cut is not connected to the source then it is connected to the sink
    		// This because all other edges have infinite capacity so they can't be in the min cut 
    		else
    		{
    			if (minCut.get(i) == nodeIndex)
    				return true;
    		}
    	}
    	
    	
    	return false;
    	
    }
    
    public void printMinCut()
    {
    	System.out.println();
    	
    	System.out.println("Min Cut");
    	
    	int length = minCut.size();
    	
    	for (int i = 0; i < length; i+=2)
    	{
    		if (minCut.get(i) == -1)
    			System.out.println("Source -> π" + source.getChild(minCut.get(i+1)).getLabel().getRotationNo());
    			
    		else
    		{
    			Node<Rotation> curNode = nodes.get(minCut.get(i));
    			System.out.println("π" + curNode.getLabel().getRotationNo() + " -> sink");
    		}
    		
    	}
    }
    
    
    
    public void printNegRotations()
    {
    	int i=0;
    	System.out.println("Negative Rotations");
		while (source.hasChild(i))
		{
			System.out.print("π" + source.getChild(i).getLabel().getRotationNo());
			System.out.println("  cap " + source.getCapacity(i));
			i++;
		}
		
		
    }
    
    public void printPosRotations()
    {
   	 	int i=0;
		System.out.println("Positive Rotations");
		while (sink.hasParent(i))
		{
			System.out.print("π" + sink.getParent(i).getLabel().getRotationNo());
			System.out.println("  cap " + sink.getParent(i).getLabel().getWeight());
			i++;
		}
	}
	
	public Node<Rotation> getSource()
	{
		return source;
	}
	
	public Node<Rotation> getSink()
	{
		return sink;
	}
}