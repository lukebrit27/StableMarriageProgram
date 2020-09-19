// Luke Britton June 2017
// This class is where we store marriages
import java.util.*;
import java.util.Arrays;
public class Marriages
{
	
	private int noOfMen;
	private int noOfWomen;
	private int noOfMarriages;
	private Female[] mensSpouses = new Female[noOfWomen];
	private Male[] womensSpouses = new Male[noOfMen]; 
	
	// Input each mans spouse and each womens spouse
	public Marriages(Female[] mensSpouses, Male[] womensSpouses)
	{
		noOfMen = mensSpouses.length;
		noOfWomen = womensSpouses.length;
		noOfMarriages = Math.min(noOfMen, noOfWomen);
		this.mensSpouses = mensSpouses;
		this.womensSpouses = womensSpouses;	
	}
	
	
	public Female getMansSpouse(int man)
	{
		if ((man < 0) || (man > noOfMen))
			throw new IllegalArgumentException(man + " is outside the bounds of the number of men");
			
		return mensSpouses[man - 1];
	}
	
	public Male getWomansSpouse(int woman)
	{
		
		if ((woman < 0) || (woman > noOfWomen))
			throw new IllegalArgumentException(woman + " is outside the bounds of the number of men");
			
		return womensSpouses[woman - 1];
	}
	
	public Female[] getMensSpouses()
	{
		Female[] spouses = new Female[noOfMen];
		
		for (int i = 0; i < noOfMen; i++)
			spouses[i] = mensSpouses[i];
		
		return spouses;
	}
	
	public Male[] getWomensSpouses()
	{
		Male[] spouses = new Male[noOfWomen];
		
		for (int i = 0; i < noOfWomen; i++)
			spouses[i] = womensSpouses[i];
		
		return spouses;
	}
	
	public void printMarriages()
	{
		System.out.println("W   M");
		
		for (int i = 0; i < noOfMarriages; i++)
		{
			System.out.print((i + 1) + "   " + womensSpouses[i].getPersonNo());
			System.out.println("");
		}
		
		System.out.println("isStable = " + isStable() + "\n");
	}
	
	// check if the marriage is stable
	public boolean isStable()
	{
		int spouseRank;
		int prefRank = 1;
		int currPrefNo;
		Person currPref;
		int currPrefSpouse;
		
		if (noOfMarriages == noOfMen)
		{
			for (int i = 0; i < noOfMarriages; i++)
			{
				spouseRank = mensSpouses[i].getPrefListRank(i + 1);
				while (prefRank < spouseRank)
				{
					currPref = mensSpouses[i].getPref(prefRank);
					currPrefNo = currPref.getPersonNo();
					currPrefSpouse = mensSpouses[currPrefNo - 1].getPersonNo();
				 
			
					if (currPref.getPrefListRank(mensSpouses[i].getPersonNo()) < currPref.getPrefListRank(currPrefSpouse))
						return false;
				
					prefRank++;
				}
			}
		}
	
		return true;
	}
	
	
	
	public boolean isEqualTo(Marriages M)
	{
		if (Arrays.equals(womensSpouses, M.getWomensSpouses()))
			return true;
				
		return false;
	}
	
	// check if a proposee prefer a proposer to their current spouse
	public boolean isPreferredToSpouse(Person proposee, Person proposer)
	{
		int proposeeIndex = proposee.getPersonNo() - 1;
		int proposerNo = proposer.getPersonNo();
		
		
		if (!proposee.isMale())
			if (proposee.getMostPreferred((Male)proposer, womensSpouses[proposeeIndex]) == proposerNo)
				return true;
			
		else if (proposee.isMale())
			if (proposee.getMostPreferred((Female)proposer, mensSpouses[proposeeIndex]) == proposerNo)
				return true;
				
		else	
			throw new IllegalArgumentException("Something went wrong. The proposee is neither male or female.");
		
		return false;
	}
	
	
	// Check that a rotation is valid for this marriage
	// A rotation is valid iff each pair in the rotation exists in this marriage
	public boolean isAValidRotation(Rotation R)
	{
		LinkedList<Integer> rot = R.getRotation();
		int rotSize = rot.size();
		int mansSpouse;
		
		
		for (int i = 0; i < rotSize; i+=2)
		{
			mansSpouse = getMansSpouse(rot.get(i)).getPersonNo();
			
			if (!(mansSpouse == rot.get(i + 1)))
				return false;
		}
		
		return true;
	}
	
	
	// calculates the social cost of the marriage (i.e. sum of the regrets)
	public int calculateSocialCost()
	{
		int socialCost = 0;
		Male man;
		Female woman;
		
		for ( int i = 0; i < noOfWomen; i++ )
		{
			man = getWomansSpouse(i + 1);
			woman = getMansSpouse(man.getPersonNo());
			
			socialCost += woman.getPrefListRank(man.getPersonNo()) 
			+ man.getPrefListRank(woman.getPersonNo());
		}
		return socialCost;
	}
	
	// Get the regret of some man in this marriage
	public int getMansRegret(int manNo)
	{
		int spouse = getMansSpouse(manNo).getPersonNo();
		Male man = getWomansSpouse(spouse);
		int regret = man.getPrefListRank(spouse);
		
		return regret;
	}
	
	// Get the regret of some woman in this marriage
	public int getWomansRegret(int womanNo)
	{
		int spouse = getWomansSpouse(womanNo).getPersonNo();
		Female woman = getMansSpouse(spouse);
		int regret = woman.getPrefListRank(spouse);
		
		return regret;
	}
	
	// Search for a woman with regret equal to the maximum regret and return that woman's personNo
	// If there is no woman with regret equal to the maximum regret we return -1
	public int findWomanWithMaxRegret()
	{
		int maxRegret = calculateMaxRegret();
		int regret;
		
		for (int i = 1; i <= noOfWomen; i++)
		{
			regret = getWomansRegret(i);
			
			if (regret == maxRegret)
				return i;
		}
		
		return -1;
	}
	
	// Search for a man with regret equal to the maximum regret and return that man's personNo
	// If there is no man with regret equal to the maximum regret we return -1 
	public int findManWithMaxRegret()
	{
		int maxRegret = calculateMaxRegret();
		int regret;
		
		for (int i = 1; i <= noOfMen; i++)
		{
			regret = getMansRegret(i);
			
			if (regret == maxRegret)
				return i;
		}
		
		return -1;
	}
	
	// Calculate the maximum regret of the marriage
	public int calculateMaxRegret()
	{
		int maxRegret = 0;
		Male man;
		Female woman;
		
		for (int i = 0; i < noOfWomen; i++)
		{
			man = getWomansSpouse(i + 1);
			woman = getMansSpouse(man.getPersonNo());
			
			maxRegret = Math.max(Math.max(woman.getPrefListRank(man.getPersonNo()), 
			man.getPrefListRank(woman.getPersonNo())), maxRegret);
		}
		
		return maxRegret;
	}
	

}