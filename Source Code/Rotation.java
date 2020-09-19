// August 2017 Luke Britton
import java.util.*;
public class Rotation
{
	
	private LinkedList<Integer> rotation;
	private int rotationNo;
	private int rotationLength;
	private MarriageProblem prob;
	private int[] eliminatedUnstablePairs;
	private LinkedList<Rotation> predecessors = new LinkedList<Rotation>();
	private LinkedList<Rotation> successors = new LinkedList<Rotation>();
	private int rotWeight;
		
	// Each rotation has a rotation, a unique rotation number and the marriage problem as input
	/* The rotation input is the actual rotation in raw form. It is a LinkedList where each 
	   even index (inc. 0) stores a man and each odd index stores their corresponding female spouse */
	public Rotation(LinkedList<Integer> rotation, int rotationNo, MarriageProblem prob)
	{
		this.rotation = rotation;
		this.rotationNo = rotationNo;
		this.prob = prob;
		rotationLength = rotation.size();
		rotWeight = calculateWeight();
		
		// the raw rotation input should
		if (rotationLength%2 != 0)
			throw new IllegalArgumentException("This is not a rotation");	
			
		//pairLabels = new int[rotationLength/2];
	}
	
	// get the raw rotation 
	public LinkedList<Integer> getRotation()
	{
		return rotation;
	}
	
	public int getRotationNo()
	{
		return rotationNo;
	}
	
	public int getLength()
	{
		return rotationLength;
	}
	
	
	public int getPerson(int index)
	{
		if (index < 0 || index >= rotationLength)
			throw new IllegalArgumentException("No person a this index");
			
		return rotation.get(index);
	}
	
	public void addPredecessor(Rotation r)
	{
		predecessors.add(r);
	}
	
	public void addSuccessor(Rotation r)
	{
		successors.add(r);
	}
	
	public void printPredecessors()
	{
		int length = predecessors.size();
		System.out.print("π" + rotationNo + " predecessors: ");
		
		for (int i = 0; i < length; i++)
		{
			System.out.print(predecessors.get(i).getRotationNo() + ", ");
		}
		System.out.println();
	}
	
	// Get a person's spouse before the rotation is eliminated
	public int getOldSpouse(Person person)
	{
		int personNo = person.getPersonNo();
		int oldSpouse = -1;
		if (person.isMale())
		{
			for (int i = 0; i < rotationLength; i+=2)
			{
				if (rotation.get(i) == personNo)
					oldSpouse = rotation.get(i+1);
			}
		}
		
		else if (!person.isMale())
		{
			for (int i = 1; i < rotationLength; i+=2)
			{
				if (rotation.get(i) == personNo)
					oldSpouse = rotation.get(i-1);
			}
		}
		
		else 
			throw new IllegalArgumentException("This person is not in rotation π" + rotationNo);	
		
		return oldSpouse;
	}
	
	// Get a person's spouse after the rotation is eliminated
	public int getNewSpouse(Person person)
	{
		int personNo = person.getPersonNo();
		int newSpouse = -1;
		if (person.isMale())
		{
			for (int i = 0; i < rotationLength; i+=2)
			{
				if (rotation.get(i) == personNo)
					newSpouse = rotation.get((i+3)%rotationLength);
			}
		}
		
		else if (!person.isMale())
		{
			for (int i = 1; i < rotationLength; i+=2)
			{
				if (rotation.get(i) == personNo)
					newSpouse = rotation.get((i+rotationLength-3)%rotationLength);
			}
		}
		
		else 
			throw new IllegalArgumentException("This person is not in rotation π" + rotationNo);	
		
		
		
		return newSpouse;
	}
	
	// Check if a rotation is a predecessor of this rotation
	public boolean isAPredecessor(int rotNo)
	{
		int predListLength = predecessors.size();
		
		for ( int i = 0; i < predListLength; i++ )
		{
			if (predecessors.get(i).getRotationNo() == rotNo)
				return true;
		}
		
		return false;
	}
	
	// check if this rotation has predecessors
	public boolean hasPredecessors()
	{
		if (predecessors.size() == 0)
			return false;
			
		return true;
	}
	
	
	// returns true if the rotation has predecessors excluding the ones inputted
	public boolean hasPredecessors(LinkedList<Rotation> excludingRots)
	{
		int noOfPredecessors = predecessors.size();
		int noOfExclRots = excludingRots.size();
		Rotation curPred;
		boolean hasPred = false;
		boolean excluded;
		for(int i = 0; i < noOfPredecessors; i++)
		{
			excluded = false;
			curPred = predecessors.get(i);
			
			for (int j = 0; j < noOfExclRots; j++)
			{
				if (curPred.isEqualTo(excludingRots.get(j)))
				{
					excluded = true;
					break;
				}
			}
			
			if (!excluded)
			{
				hasPred = true;
				break;
			}
		}
		
		return hasPred;
	}	
	
	
	// check if this rotation has successors
	public boolean hasSuccessors()
	{
		if (successors.size() == 0)
			return false;
			
		return true;
	}
	
	// check if this rotation has a specific successor
	public boolean hasSuccessor(Rotation succ)
	{
		int succNo = succ.getRotationNo();
		int size = successors.size();
		
		for (int i = 0; i < size; i++)
		{
			if (succNo == successors.get(i).getRotationNo())
				return true; 
		}
		
		return false;
		
	}
	
	public LinkedList<Rotation> getPredecessors()
	{
		if (!hasPredecessors())
			throw new IllegalArgumentException("Error! π" + rotationNo + " has no predecessors." );
			
		return predecessors;
	}
	
	
	public LinkedList<Rotation> getSuccessors()
	{
		if (!hasSuccessors())
			throw new IllegalArgumentException("Error! π" + rotationNo + " has no successors." );
			
		return successors;
	}
	
	// get the successor rotation numbers
	public LinkedList<Integer> getSuccessorNos()
	{
		if (!hasSuccessors())
			throw new IllegalArgumentException("Error! π" + rotationNo + " has no successors." );
		
		LinkedList<Integer> successorNos = new LinkedList<Integer>();
		int noOfSuccessors = successors.size();
		
		for (int i = 0; i < noOfSuccessors; i++)
			successorNos.add(successors.get(i).getRotationNo());
			
		return successorNos;
	}
	
	public boolean isEqualTo(Rotation r)
	{
		if (rotationNo == r.getRotationNo())
			return true;
			
		return false;
	}
	
	// calculates the weight of this rotation
	private int calculateWeight()
	{
		int weight = 0;
		
		for (int i = 0; i < rotationLength; i+=2)
		{
			Male man = prob.getMan((int)rotation.get(i));
			Female woman = prob.getWoman((int)rotation.get(i+1));
			
			weight += ( man.getPrefListRank((int)rotation.get(i+1)) - man.getPrefListRank((int)rotation.get((i+3)%rotationLength)) ) 
			+ ( woman.getPrefListRank((int)rotation.get(i)) - woman.getPrefListRank((int)rotation.get((i-2+rotationLength)%rotationLength)) );		
		}
		
		return weight;
			
	}
	
	
	public int getWeight()
	{
		return rotWeight;
	}
	
	
	
	

	
}

