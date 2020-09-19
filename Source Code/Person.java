// Luke Britton June 2017
// Class where we define the person object
import java.util.*;
public class Person
{
	
	
	private int personNo;
	private int listLength;
	private Person[] prefList;
	private int currentProposer = -1;
	private int nextPref = -1;
	protected boolean isMale;
	private LinkedList<Person> shortList = new LinkedList<Person>();
	private int[] prefListRotLabels;
	
	/* For each person we input a personNo and a listLength for the length of that person's 
	   preference list */
	public Person(int personNo, int listLength)
	{
		this.personNo = personNo;
		this.listLength = listLength; 
		prefList = new Person[listLength];
		prefListRotLabels = new int[listLength];
		
		// Label each preference by the rotation they are eliminated by
		// Important for constructing the partial in MarriageProblem class
		for (int i = 0; i < listLength; i++)
		{
			prefListRotLabels[i] = -1;
		}
	} 
	
	
	public int getPersonNo()
	{
		return personNo;
	}
	
	
	// get preference by their rank in the preference list
	public Person getPref(int rank)
	{
		if (rank <= 0 ||rank - 1 >= listLength)
			throw new IllegalArgumentException("rank outside bounds of preference list");
			
		return prefList[rank - 1];
	}
	
	
	// add a preference list for this person
	public void addPrefList(MarriageProblem prob, int[] list)
	{
		if (list.length != listLength)
			throw new IllegalArgumentException("Added pref list not equal to the no. of the opposite sex");
		
		if (isMale)
			for (int i = 0; i < listLength; i++)
			{
				prefList[i] = (Female)prob.getWoman(list[i]);
			}
			
		else
			for (int i = 0; i < listLength; i++)
			{
				prefList[i] = (Male)prob.getMan(list[i]);
			}
			
	}
	
	// Check if the person is male or female
	public boolean isMale()
	{
		return isMale;
	}
	
	
	// Each person has a current proposer
	// Important for implementing the gale-shapley algorithm in the marriage problem class
	public boolean isCurrentProposer(Person p)
	{	
		if (currentProposer == p.getPersonNo())
			return true;
		
		return false;
	}
	
	
	public int getCurrentProposer()
	{	
		return currentProposer;
	}
	
	
	private void changeCurrentProposer(int person)
	{
		currentProposer = person;
	}
	
	// -1 indicates there is no current proposer
	public void resetProposer()
	{
		currentProposer = -1;	
	}
	
	// generate a random preference list given a list of people
	public void generatePrefList(Person[] personList)
	{
		
		int listLength = personList.length;
		int[] randList = new int[listLength];
		int randomNo;
		int temp;
		
		for (int i = 0; i < listLength; i++)
			randList[i] = i;
			
			
		for (int i = 0; i < listLength; i++)
		{
			temp = randList[i];
			randomNo = (int)(Math.random()*listLength);
			randList[i] = randList[randomNo];
			randList[randomNo] = temp;
		}
		
		// We generate a random order and then add it to our preference list
		for (int i = 0; i < listLength; i++)
			prefList[i] = personList[randList[i]];
		
	}
	

	// Check if a person is preferred to the current proposer
	private boolean preferredToCurProposer(Person person)
	{
		int personRank = getPrefListRank(person.getPersonNo());
		int curProposerRank = getPrefListRank(currentProposer);
		
		if (personRank <= curProposerRank)
			return true;
			
		return false;
	}
	
	
	// Part of the gale-shapley algorithm in the marriage problem class
	/* Method where this person proposes to another person. Accepted if they are more preferred 
	   to the proposee's current proposer */
	public void propose(Person person)
	{
		if (person.proposalsIsEmpty())
			person.changeCurrentProposer(personNo);
			
		else
		{
			if (person.preferredToCurProposer(this))
				person.changeCurrentProposer(personNo);
		}
	}
	
	
	public boolean proposalsIsEmpty()
	{
		if (currentProposer == -1)
			return true;
			
		else	
			return false;	
	}
	
	
	public boolean isEngaged(Person person)
	{
		if (person.getCurrentProposer() == personNo)
			return true;
			
		else 
			return false;
	}
	
	
	// Used for Gale-Shapley and break marriage operations
	// Checking if this person has anyone left that they haven't proposed to
	public boolean hasNextPref()
	{
		if (nextPref >= (listLength - 1))
			return false;
			
		return true;
	}
	
	
	public Person getNextPref()
	{
		int listLength = prefList.length;
		
		if (!hasNextPref())
			throw new IllegalArgumentException("No next preference");
		
		else
			nextPref++;
			
		return prefList[nextPref];
	}
	
	// Sometimes we need to reset the nextPref value. This method does that
	public void setNextPref(int rank)
	{
		if (rank <= 0 || rank > listLength)
			throw new IllegalArgumentException("Error! Can't set next pref, rank out side bounds of pref list.");
			
		nextPref = rank - 1;
	}
	
	// Get the rank of a person in this person's preference list
	public int getPrefListRank(int person)
	{
		if (personNo <= 0 || personNo > listLength)
			throw new IllegalArgumentException("Error! No such person exists.");
		
		int listLength = prefList.length;
		int rank = -1;
		
		for (int i = 0; i < listLength; i++)
		{
			if (prefList[i].getPersonNo() == person)
			{
				rank = i+1;
				break;
			}
		}
		
		return rank;
	}
	
	// Checking if this person is equal to the inputted person
	public boolean isEqualTo(Person p)
	{
		if ((personNo == p.getPersonNo()) && (isMale == p.isMale()))	
			return true;
			
		return false;
	}
	
	
	public int getMostPreferred(Person p1, Person p2)
	{
		if (!(p1.isMale() == p2.isMale()))
			throw new IllegalArgumentException("Can't compare individuals of different sexes");
			
		if ( getPrefListRank(p1.getPersonNo()) < getPrefListRank(p2.getPersonNo()) )
			return p1.getPersonNo();
			
		return p2.getPersonNo();
	}

	/* add's a person to this person's shortlist. A person should only be added to the shortlist 
	   if they create a stable pair with this person */
	public void addToShortList(Person p)
	{
		if (isMale == p.isMale())
			throw new IllegalArgumentException("Can't add individuals of the same sex to a persons shortlist.");
			
		else
			shortList.add(p);
	}
	
	// get this person's shorlist
	public Person getPerShortList(int index)
	{
		int length = shortList.size();
		
		if (index < 0 || index >= length)
			throw new IllegalArgumentException("Error! No person exists at this index in the shortlist.");
			
		return shortList.get(index);
	}
	
	public void printShortList()
	{
		int length = shortList.size();
		
		System.out.print(personNo + "'s shortlist: ");
		for (int i = 0; i < length; i++)
		{
			System.out.print(shortList.get(i).getPersonNo() + " ");
		}
		
		System.out.println();
	}
	
	// label a pair with a rotation
	public void labelPair(int rotation, int person)
	{
		if (person <= 0 || person > listLength)
			throw new IllegalArgumentException("Error! No such person exists.");
			
		else
			prefListRotLabels[person - 1] = rotation;
	}
	
	public int getPairLabel(int person)
	{
		if (person <= 0 || person > listLength)
			throw new IllegalArgumentException("Error! No such person exists.");
			
		else if (prefListRotLabels[person - 1] == -1)
			throw new IllegalArgumentException("Error! Pair not labelled.");
			
		return prefListRotLabels[person - 1];
	}
	
	
	public boolean hasLabel(int person)
	{
		if (prefListRotLabels[person - 1] == -1)
			return false;
			
		return true;
	}


}