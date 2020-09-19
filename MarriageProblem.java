// Luke Britton June 2017
/* This class is where we encapsulate instances of the stable marriage problem and the algorithms
   to find stable marriages, find all rotations etc */
import java.util.*;
public class MarriageProblem
{
	
	private Male[] men;
	private Female[] women;
	// Two variables for the cases where we consider unequal sets of men and women
	private int noOfMen;
	private int noOfWomen;
	private int noOfMarriages;
	private boolean[] womanIsMarked;
	private LinkedList<Integer> markedWomen = new LinkedList<Integer>();
	private LinkedList<Rotation> rotations = new LinkedList<Rotation>();
	private boolean foundRotations = false;
	private boolean foundPredecessors = false;
	// Each mans shortlist contains all his stable matchings and the corresponding rotation the pair was found
	// Contains the predecessors of each rotation
	private Marriages manOptimal;
	private Marriages womanOptimal;


	// This object holds the actual marriage problem	
	public MarriageProblem(int noOfMen,int noOfWomen)
	{
		this.noOfMen = noOfMen;
		this.noOfWomen = noOfWomen;
		noOfMarriages = Math.min(noOfMen, noOfWomen);
		men = new Male[noOfMen];
		women = new Female[noOfWomen];
		womanIsMarked = new boolean[noOfWomen];
		
	}
	
	
	
	
	/**************************Methods for getting objects*******************************/
	public Male[] getMen()
	{
		return men;
	}
	
	public Female[] getWomen()
	{
		return women;
	}
	
	public Male getMan(int p)
	{
		return men[p - 1];
	}
	
	public Female getWoman(int p)
	{
		return women[p - 1];
	}
	
	
	public Marriages getManOptimal()
	{
		Female[] mensSpouses = new Female[noOfMen];
		Male[] womensSpouses = new Male[noOfWomen];
		
		for (int i = 0; i < noOfMen; i++)
		{
			mensSpouses[i] = manOptimal.getMansSpouse(i+1);
		}
		
		for (int i = 0; i < noOfWomen; i++)
		{
			womensSpouses[i] = manOptimal.getWomansSpouse(i+1);
		}
		
		Marriages marriages = new Marriages(mensSpouses, womensSpouses);
		return marriages;
	}
	
	public Marriages getWomanOptimal()
	{
		Female[] mensSpouses = new Female[noOfMen];
		Male[] womensSpouses = new Male[noOfWomen];
		
		for (int i = 0; i < noOfMen; i++)
		{
			mensSpouses[i] = womanOptimal.getMansSpouse(i+1);
		}
		
		for (int i = 0; i < noOfWomen; i++)
		{
			womensSpouses[i] = womanOptimal.getWomansSpouse(i+1);
		}
		
		Marriages marriages = new Marriages(mensSpouses, womensSpouses);
		return marriages;
	}
	
	public Rotation getRotation(int index)
	{
		if (index < 0 || index > rotations.size())
			throw new IllegalArgumentException("No rotation at that index");
			
		return rotations.get(index);
			
	}
	public LinkedList<Rotation> getRotations()
	{
		if (rotations.size() == 0)
			throw new IllegalArgumentException("There are no rotations");
			
		return rotations;
	}
	
	public int getNoOfRotations()
	{
		if (!foundRotations)
			throw new IllegalArgumentException("Error! Can't get no. of rotations until all rotations have been found.");
			
		return rotations.size();
	}
	
	/************************************************************************************/
	
	
	
	
	/************************Initiation Methods*******************************/
	
	
	public void addMen(Male[] m)
	{
		men = m;
	}
	
	
	public void addWomen(Female[] w)
	{
		women = w;
	}
	
	// Method to initiate the population by adding preference lists manually 
	public void initiatePop(int[][] malePrefLists, int[][] femalePrefLists)
	{
		
		
		for (int i = 0; i < noOfMen; i++)
			men[i] = new Male(i + 1, noOfWomen);
			
		for (int i = 0; i < noOfWomen; i++)
		{
			women[i] = new Female(i + 1, noOfMen);
			women[i].addPrefList(this, femalePrefLists[i]);
			womanIsMarked[i] = false;
		}	
		
		for (int i = 0; i < noOfMen; i++)
			men[i].addPrefList(this, malePrefLists[i]);
			
		womanOptimal = galeShapley(false);
		manOptimal = galeShapley(true);
	}
	
	
	// Method to initiate the population by randomly generating preference lists
	public void initiateRandomPop()
	{
		for (int i = 0; i < noOfMen; i++)
			men[i] = new Male(i + 1, noOfWomen);	
			
		
		for (int i = 0; i < noOfWomen; i++)
		{
			women[i] = new Female(i + 1, noOfMen);
			women[i].generatePrefList(men);
			womanIsMarked[i] = false;
		}
		
		for (int i = 0; i < noOfMen; i++)
			men[i].generatePrefList(women);	
			
		womanOptimal = galeShapley(false);
		manOptimal = galeShapley(true);
	}
	
	
	private void initiateStringBufferArray(StringBuffer[] S)
	{
	 	int bufferLength = S.length;
	 	
	 	for (int i = 0; i < bufferLength; i++)
	 	{
	 		S[i] = new StringBuffer();
	 	}
	 	
	}
	/************************************************************************************/
	
	
	/********************************Printing Methods************************************/
	public void printPrefLists()
	{
		for (int i = 0; i < noOfMen; i++)
		{
			System.out.print("m" + (i + 1) + " ");
			for (int j = 1; j <= noOfWomen; j++)
			{
				System.out.print(men[i].getPref(j).getPersonNo() + " ");
			}
			System.out.println();
		}
		
		System.out.println();
		
		for (int i = 0; i < noOfWomen; i++)
		{
			System.out.print("w" + (i + 1) + " ");
			for (int j = 1; j <= noOfMen; j++)
			{
				System.out.print(women[i].getPref(j).getPersonNo() + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	
	public void printRotations()
	{
		int noOfRotations = rotations.size();
		
		if (!foundRotations)
			throw new IllegalArgumentException("Error! Rotations have not been found yet.");
		
		for (int i = 0; i < noOfRotations; i++)
		{
			LinkedList rotation = rotations.get(i).getRotation();
			int rotationLength = rotation.size();
			
			System.out.print("π" + i + ": { "  );
			for (int j = 0; j < rotationLength; j+=2)
				System.out.print("( " + rotation.get(j) + ", " + rotation.get(j + 1) + " ), ");
			
				
			System.out.print(" }\n");
			
		}	
		
	}
	
	public void printMaleShortLists()
	{
		if (!foundRotations)
			throw new IllegalArgumentException("Rotations not found yet");
			
		for (int i = 0; i < noOfMen; i++)
		{
			men[i].printShortList();
		}
		
	}
	
	public void printAllRotPredecessors()
	{
		int noOfRotations = rotations.size();
		
		for (int i = 0; i < noOfRotations; i++)
			rotations.get(i).printPredecessors();
	}
	/************************************************************************************/
	
	
	/********************************Gale Shapley Alg************************************/
	// Returns true if all inputted individuals of received a proposal
	private boolean allHaveProposal(Person[] people)
	{
		int noOfPeople = people.length;
		
		for (int i = 0; i < noOfPeople; i++)
		{
			if (people[i].proposalsIsEmpty())
				return false;	
		}
		
		return true;
			
	}
	
	private boolean isSymmetric()
	{
		if (noOfMen == noOfWomen)
			return true;
		
		return false;
	}
	
	// reset current proposers of each individual to -1 (i.e. no proposer)
	private void resetProposers()
	{
		if (isSymmetric())
		{
			for (int i = 0; i < noOfWomen; i++)
			{
				women[i].resetProposer();
				men[i].resetProposer();
			}
		}
		
		else 
		{
			for (int i = 0; i < noOfWomen; i++)
				women[i].resetProposer();
				
			for (int i = 0; i < noOfMen; i++)
				men[i].resetProposer();
		}
	}
	
	
	// method for implementing the gale shapley algorithm
	// If menPropose is true the men propose, if false the women propose 
	private Marriages galeShapley(boolean menPropose)
	{
		
		Person[] pref; 
		Female[] mensSpouses = new Female[noOfMen];
		Male[] womensSpouses = new Male[noOfWomen];
		
		if (menPropose)
		{
			pref = new Person[noOfMen];
			
			// Each man proposes to his first preference
			for (int i = 0; i < noOfMen; i++)
			{
				pref[i] = men[i].getNextPref();
				men[i].propose(pref[i]);
			}
			
			// Algorithm terminates when all women have received a proposal
			while (!allHaveProposal(women))
			{	
				// All men who are currently not engaged propose to their next preference
				for ( int i = 0; i < noOfMen; i++ )
				{
					if (!men[i].isEngaged(pref[i]))
					{
						pref[i] = men[i].getNextPref();
						men[i].propose(pref[i]);
					}
				}
				
			}
			// add all final current proposers as spouses
			for (int i = 0; i < noOfWomen; i++)
			{
				
				womensSpouses[i] = getMan(women[i].getCurrentProposer());
				mensSpouses[women[i].getCurrentProposer() - 1] = women[i];
				
				womensSpouses[i].resetProposer();
				mensSpouses[women[i].getCurrentProposer() - 1].resetProposer();
			}
			
		}
		
		else
		{
			pref = new Person[noOfWomen];
			
			// Each woman proposes to his first preference
			for (int i = 0; i < noOfWomen; i++)
			{
				pref[i] = women[i].getNextPref();
				women[i].propose(pref[i]);
			}
			// Algorithm terminates when all men have received a proposal	
			while (!allHaveProposal(men))
			{	
				// All women who are currently not engaged propose to their next preference
				for ( int i = 0; i < noOfWomen; i++ )
				{
					if (!women[i].isEngaged(pref[i]))
					{
						
						pref[i] = women[i].getNextPref();
						women[i].propose(pref[i]);
					}
				}
			
			}
			// add all final current proposers as spouses
			for (int i = 0; i < noOfMen; i++)
			{
				int curProp = men[i].getCurrentProposer();
				mensSpouses[i] = getWoman(curProp);
				womensSpouses[curProp - 1] = men[i];
				
				mensSpouses[i].resetProposer();
				womensSpouses[curProp - 1].resetProposer();
			}	
		}
		// create and return the new stable marriage
		Marriages m = new Marriages(mensSpouses, womensSpouses);
		return m;
	}
	/************************************************************************************/
	
	/******************************Break Marriage Methods********************************/
	// reset the next prefs for each individual to the prefList position of their partner in M
	private void resetNextPrefs(Marriages M)
	{
		int spouse;
		int rank1;
		int rank2;
		for (int i = 1; i <= noOfWomen; i++)
		{
			spouse = M.getWomansSpouse(i).getPersonNo();
			rank1 = getWoman(i).getPrefListRank(spouse);
			rank2 = getMan(spouse).getPrefListRank(i);
			getWoman(i).setNextPref(rank1);
			getMan(spouse).setNextPref(rank2);
			
		}
		
	}
	
	/* method to find a new stable marriage by breaking m's marriage in M to restart the 
	   Gale-Shapley algorithm */
	// Moves closer to the woman optimal marriage each time it finds a new stable marriage
	private Marriages breakMarriage(Marriages M, Male m)
	{
		// m becomes a freeMan, w (m's spouse in M) becomes "semi-free"
		// mens/womensFiances holds the current state of the proposal sequence
		Female[] mensFiances = M.getMensSpouses();
		Male[] womensFiances = M.getWomensSpouses();
		Male freeMan = m;
		Female w = mensFiances[m.getPersonNo() - 1];
		Person pref;
		Male currFiance;
		
		/* Algorithm terminates either when the freeMan has no more preferences or when 
		   w  receives a proposal from a man she prefers to m */
		while (freeMan.hasNextPref() && (womensFiances[w.getPersonNo() - 1] == m))
		{
			// get freeMan's next preference pref
			// get that preferences current fiance
			pref = freeMan.getNextPref();
			currFiance = womensFiances[pref.getPersonNo() - 1];
			
			/* if pref prefers the freeMan to her current fiance then pref becomes engaged to
			   the freeMan and her fiance becomes the new freeMan */
			if (pref.getPrefListRank(freeMan.getPersonNo()) < pref.getPrefListRank(currFiance.getPersonNo()))
			{
				womensFiances[pref.getPersonNo() - 1] = freeMan;
				mensFiances[freeMan.getPersonNo() - 1] = (Female)pref;
				freeMan = currFiance;
			}
		}
		/* If w did not receive a proposal from a man she prefers to m then we have not found 
		   a new stable marriage so we just return M */
		if (womensFiances[w.getPersonNo() - 1].getPersonNo() == m.getPersonNo())
			return M;
		
		// Otherwise we have found a new stable marriage which we return
		Marriages newMarriages = new Marriages(mensFiances, womensFiances);
		return newMarriages;
	}
	
	
	/* method to find a new stable marriage by breaking w's marriage in M to restart the 
	   Gale-Shapley algorithm */
	// Important for finding the man regret minimum marriage
	// Moves closer to the man optimal marriage each time it finds a new stable marriage
	public Marriages breakMarriageWomen(Marriages M, Female w)
	{
		// w becomes a freeWoman, m (w's spouse in M) becomes "semi-free"
		// mens/womensFiances holds the current state of the proposal sequence
		Female[] mensFiances = M.getMensSpouses();
		Male[] womensFiances = M.getWomensSpouses();
		Female freeWoman = w;
		Male m = womensFiances[w.getPersonNo() - 1];
		Person pref;
		Female currFiance;
	
		/* Algorithm terminates either when the freeWoman has no more preferences or when 
		   m  receives a proposal from a woman he prefers to w */
		while (freeWoman.hasNextPref() && (mensFiances[m.getPersonNo() - 1] == w))
		{
			// get freeWoman's next preference pref
			// get that preferences current fiance
			pref = freeWoman.getNextPref();
			currFiance = mensFiances[pref.getPersonNo() - 1];
			
			/* if pref prefers the freeWoman to his current fiance then pref becomes engaged to
			   the freeWoman and his fiance becomes the new freeWoman */
			if (pref.getPrefListRank(freeWoman.getPersonNo()) < pref.getPrefListRank(currFiance.getPersonNo()))
			{
				womensFiances[freeWoman.getPersonNo() - 1] = (Male)pref;
				mensFiances[pref.getPersonNo() - 1] = freeWoman;
				freeWoman = currFiance;
			}	
		}
		/* If m did not receive a proposal from a woman he prefers to w then we have not found 
		   a new stable marriage so we just return M */
		if (mensFiances[m.getPersonNo() - 1] == w)
			return M;
		
		// Otherwise we have found a new stable marriage which we return
		Marriages newMarriages = new Marriages(mensFiances, womensFiances);
		
		return newMarriages;
	}
	
	/*
	This method finds new marriages by breaking man m from his spouse w in M similar to
	the break marriage method. However the difference is the inclusion of 
	pauseBreakMarriage which used to store the rotations we find.
	*/
	private Marriages adjustedBreakMarriage(Marriages M, Male m)
	{
		
		// curMarriages corresponds to the last stable marriage found
		// mens/womens fiances holds the current state of the proposal sequence
		Marriages curMarriages = M;
		Female[] mensFiances = new Female[noOfMen];
		Male[] womensFiances = new Male[noOfWomen];
		
		for (int i = 0; i < noOfWomen; i++)
		{
			womensFiances[i] = M.getWomansSpouse(i + 1);
			mensFiances[womensFiances[i].getPersonNo() - 1] = getWoman(i + 1);
		}
		
		Male freeMan = m;
		Female w = curMarriages.getMansSpouse(m.getPersonNo());
		Person pref;
		Male currFiance;
		int i = 0;
		markedWomen.add(w.getPersonNo());
		womanIsMarked[w.getPersonNo() - 1] = true;
		
		while (freeMan.hasNextPref() && (womensFiances[w.getPersonNo() - 1] == m))
		{
			
			pref = freeMan.getNextPref();
			int prefIndex = pref.getPersonNo() - 1;
			currFiance = womensFiances[prefIndex];
			
			// If woman proposee is marked and prefers the proposer to her spouse in the original input marriage
			if (womanIsMarked[prefIndex] && curMarriages.isPreferredToSpouse((Female)pref, freeMan))
			{
					// call our pauseBreakMarriage method
					curMarriages = pauseBreakMarriage(curMarriages, mensFiances, womensFiances, (Female)pref, freeMan);
					
					
					// In the cases where we pause breakMarriage we treat the marking of women differently
					if (pref.getPrefListRank(freeMan.getPersonNo()) < pref.getPrefListRank(currFiance.getPersonNo()))
					{
						womensFiances[pref.getPersonNo() - 1] = freeMan;
						mensFiances[freeMan.getPersonNo() - 1] = (Female)pref;
						freeMan = currFiance;
					}	
					
					else
					{
						markedWomen.add(pref.getPersonNo());
						womanIsMarked[prefIndex] = true;
					}	
			}
					
			// if woman pref accepts proposal from the free man	
			else if (pref.getPrefListRank(freeMan.getPersonNo()) < pref.getPrefListRank(currFiance.getPersonNo()))
			{
				if (!womanIsMarked[prefIndex])
				{
					markedWomen.add(pref.getPersonNo());
					womanIsMarked[prefIndex] = true;
				}
				womensFiances[pref.getPersonNo() - 1] = freeMan;
				mensFiances[freeMan.getPersonNo() - 1] = (Female)pref;
				freeMan = currFiance;
			}
			i++;	
		}
		if (womensFiances[w.getPersonNo() - 1] == m)
			return M;
		Marriages newMarriages = new Marriages(mensFiances, womensFiances);
		return newMarriages;
	}
	
	// This method is an important part of the adjusted break marriage method
	// It is used to make note when a rotation is found by storing it in a class variable
	private	Marriages pauseBreakMarriage(Marriages M, Female[] mensFiances, Male[] womensFiances, Female w, Male m)
	{
		
		Female[] mensSpouses = new Female[noOfMen];
		Male[] womensSpouses = new Male[noOfWomen];
		LinkedList<Integer> rotation = new LinkedList<Integer>();
		int rotationSize;
		int noOfMarkedWomen = markedWomen.size();
		int wIndex = markedWomen.indexOf(w.getPersonNo());

		
		
		for (int i = 0; i < noOfWomen; i++)
		{
			womensSpouses[i] = M.getWomansSpouse(i + 1);
			mensSpouses[womensSpouses[i].getPersonNo() - 1] = getWoman(i + 1);
		}
		
		
		for(int i = wIndex; i < noOfMarkedWomen; i++)
		{
			int woman = markedWomen.get(wIndex);
			int spouse = M.getWomansSpouse(woman).getPersonNo();
			rotation.add(spouse);
			rotation.add(woman);
			
			// appends stable pairing to the mans shortlist
			getMan(spouse).addToShortList(getWoman(woman));
			// we also append the corresponding rotationNo so we know which rotation the stable pairing is in
			getMan(spouse).labelPair(rotations.size(), woman);
			markedWomen.remove(wIndex);
			womanIsMarked[woman - 1] = false;
			
			if (i == wIndex)
			{
				womensSpouses[woman - 1] = m;
				mensSpouses[m.getPersonNo() - 1] = getWoman(woman);
			}
			
			else 
			{
				womensSpouses[woman - 1] = womensFiances[woman - 1];
				mensSpouses[womensSpouses[woman - 1].getPersonNo() - 1] = mensFiances[womensSpouses[woman - 1].getPersonNo() - 1];
			}
			
		}
		
		//3A (see Gusfield's[1987] algorithm A)
		Rotation rot = new Rotation(rotation, rotations.size(), this);
		rotations.add(rot);
		
		//3B (see Gusfield's[1987] algorithm A)
		M = new Marriages(mensSpouses, womensSpouses);
		return M;
		
	}
	/************************************************************************************/
	
	/**********************************Gusfield's Alg A**********************************/
	public void gusfieldsAlgorithmA()
	{
		// Algorithm can only be run once for each problem instance
		if (foundRotations)
			throw new IllegalArgumentException("Already found all rotations for this problem");
		
		
		// Start with the man optimal marriage		
		Marriages currentMarriages = getManOptimal();
		Male currMan = men[0];
		int j = 0;
		
		
		// Terminates when we find the woman optimal marriage
		while (!womanOptimal.isEqualTo(currentMarriages))
		{
			j++;
			
		   /* Each time a woman receives a proposal in the adjusted break marriage method
		   she is marked. This is so we know what women are involved in the rotation when
		   we find one. At the end of each loop we reset the marked women. */
			while (markedWomen.size() > 0)
			{
				int currMarked = markedWomen.get(0);
				womanIsMarked[currMarked - 1] = false;
				markedWomen.remove(0);
			}
			
			
			/* Find the next man who's spouse is different in the current marriage and 
		    the woman optimal marriage */
			for (int i = 1; i <= noOfWomen; i++)
			{
				if (!(currentMarriages.getWomansSpouse(i).isEqualTo(womanOptimal.getWomansSpouse(i))) )
				{
					currMan = currentMarriages.getWomansSpouse(i);
					break;
				}
			}
			
			
			/* The adjusted break marriage method finds the next stable marriage. We call it
		   the "adjusted" break marriage method because it also stores the resultant 
		   rotation of the new stable marriage in a class list */
			currentMarriages = adjustedBreakMarriage(currentMarriages, currMan);	
			
			
					
			
		}	
		
		// adding woman optimal pairs to shortlists
		for(int i = 0; i < noOfWomen; i++)
		{
			int man = womanOptimal.getWomansSpouse(i + 1).getPersonNo();
			getMan(man).addToShortList(getWoman(i+1));
		}
		
		foundRotations = true;		
	}
	/************************************************************************************/
	
	
	
	

	/**************************Constructing Partial Order*********************************/
	
	// This method labels each unstable pair with a rotation
	// This will be needed when constructing the partial order over the rotations
	// Based on the first part of Gusfield's [1987] proof for Lemma 5
	private void labelEliminatedPairs()
	{
		if (!foundRotations)
			throw new IllegalArgumentException("You need to find all rotations first before you label unstable pairs");

		int noOfRotations = rotations.size();
	
		for (int i = 0; i < noOfRotations; i++)
		{
			Rotation rotation = rotations.get(i);
			int rotationLength = rotation.getLength();
			
			for (int j = 0; j < rotationLength; j+=2)
			{
				Female woman = getWoman(rotation.getPerson(j+1));
				int oldSpouse = rotation.getOldSpouse(woman);
				int newSpouse = rotation.getNewSpouse(woman);
				int oldSpouseRank = woman.getPrefListRank(oldSpouse);
				int newSpouseRank = woman.getPrefListRank(newSpouse);
				
				if (newSpouseRank > oldSpouseRank)
					throw new IllegalArgumentException("Error! woman " + woman.getPersonNo() + " prefers old spouse to her new one.");
				
				
				// add the unstable pairings and the corresponding eliminating rotation
				for (int k = newSpouseRank + 1; k <= oldSpouseRank; k++)
				{
					Male pref = (Male)woman.getPref(k);
					pref.labelPair(i, woman.getPersonNo());	
				}
			}
		}
		
		
	}
	
		
	public void constructPartialOrder() 
	{
		// Make sure that the rotations have been found
		if (!foundRotations)
			throw new IllegalArgumentException("You need to find all rotations first before you construct a partial order");
		
		
		int noOfRotations = rotations.size();
		LinkedList<Integer> currMen = new LinkedList<Integer>();
		
		// labels each unstable pair by the rotation it was eliminated by
		labelEliminatedPairs();
		
		
		for (int j = 1; j <= noOfMen; j++)
		{
			int currManNo = j;
			Person currMan = getMan(currManNo);
			int currManIndex = currManNo - 1;
			int manOptSpouseNo = manOptimal.getMansSpouse(currManNo).getPersonNo();
			int womanOptSpouseNo = womanOptimal.getMansSpouse(currManNo).getPersonNo();
		
			int womanOptSpouseRank = currMan.getPrefListRank(womanOptSpouseNo);
			int manOptSpouseRank = currMan.getPrefListRank(manOptSpouseNo);
			int currPrefRank = manOptSpouseRank;
			int currPref;
			int lastStablePref = currMan.getPerShortList(0).getPersonNo();
			int lastStablePrefIndex = 0;
			boolean encountered;
			Rotation rot1;
			Rotation rot2;
		
			// checking for consistency in the male optimal marriage and the shortlists
			if (manOptSpouseNo != currMan.getPerShortList(0).getPersonNo())
				throw new IllegalArgumentException("Error! Male optimal not found in shortlists.");
		
	
			/* Go through each man's preference list one by one starting at the position
			   of that man's partner in the male optimal marriage. Stop when we reach
			   the man's partner in the woman optimal marriage. */
			while (currPrefRank != womanOptSpouseRank)
			{
				currPrefRank += 1;
				currPref = currMan.getPref(currPrefRank).getPersonNo();
				encountered = false;
			
				// check that the current man has a label for the current preference
				if (!currMan.hasLabel(currPref))
					continue;
			
				else
				{
					// if currPref is a stable pairing with currMan we add a rotation predecessor
					if (currPref == currMan.getPerShortList(lastStablePrefIndex + 1).getPersonNo())
					{
						rot1 = rotations.get(currMan.getPairLabel(lastStablePref));
						rot2 = rotations.get(currMan.getPairLabel(currPref));
						
						if (!rot2.isAPredecessor(rot1.getRotationNo()))
						{
							rot2.addPredecessor(rot1);
							rot1.addSuccessor(rot2);	
						}
						
						lastStablePrefIndex++;
						lastStablePref = currMan.getPerShortList(lastStablePrefIndex).getPersonNo();
					}
			
			
					else
					{
						rot1 = rotations.get(currMan.getPairLabel(currPref));
						rot2 = rotations.get(currMan.getPairLabel(lastStablePref));
						// Check to see if we have already encountered the label of the unstable pair
						for (int k = manOptSpouseRank; k < currPrefRank; k++)
						{
					
							int pref = currMan.getPref(k).getPersonNo();
							if (!currMan.hasLabel(pref))
								continue;
						
							else	
							{
								if (currMan.getPairLabel(pref) == currMan.getPairLabel(currPref))
								{
									encountered = true;
									break;
								}
							}
						
						}
				
						// If we haven't we add a rotation predecessor
						if (!encountered && !rot2.isAPredecessor(rot1.getRotationNo()))
						{
							
							
							rot2.addPredecessor(rot1);
							rot1.addSuccessor(rot2);
						}
					}	
				}
			}
		}	
		
		foundPredecessors = true;		
	}
	/************************************************************************************/
	
	/***************************Enumerating Stable Marriages*****************************/
	public enumerationTree generateEnumerationTree()
	{
	
		if (!foundPredecessors)
			throw new IllegalArgumentException("You need to find all predecessors first before you build the enumeration tree");
			
		enumerationTree T = new enumerationTree(rotations);
		return T;
	}
	
	// Do this by counting the number of nodes in the enumeration tree
	public int getNoOfStableMarriages(enumerationTree T)
	{
		return T.getNoOfNodes();	
	}
	/************************************************************************************/
	
	/***************************Finding Egalitarian Marriage*****************************/
	// Create the rotations graph
	public Graph createGraph()
	{
		if (!foundPredecessors)
			throw new IllegalArgumentException("You need to find all predecessors first before you build the partial order graph");
			
		Graph G = new Graph(rotations);
		
		return G;
	}
	
	
	// Eliminate a rotation R for some marriage M
	private Marriages eliminateRotation(Marriages M, Rotation R)
	{
		if (!M.isAValidRotation(R))
			throw new IllegalArgumentException("Rotation not valid for this set of marriages so can't be eliminated");
			
		LinkedList<Integer> rot = R.getRotation();
		int rotSize = rot.size();
		Marriages newMarriages;
		Female[] malesSpouses = M.getMensSpouses();
		Male[] femalesSpouses = M.getWomensSpouses();
		int man;
		int woman;
		
		
		for (int i = 0; i < rotSize; i+=2)
		{
			man = rot.get(i);
			woman = rot.get((i+3)%rotSize);
			
			malesSpouses[man - 1] = getWoman(woman);
			femalesSpouses[woman - 1] = getMan(man);
		}
		
		newMarriages = new Marriages(malesSpouses, femalesSpouses);
		
		return newMarriages;
	}
	
	
	/* Do this by getting the maximum weighted downset and then eliminating the
	   rotations in that downset starting at the male optimal marriage */
	public Marriages getOptimalMarriages(Graph G)
	{
		LinkedList<Rotation> maxDownset = G.getMaxWeightedDownset();
		int setSize = maxDownset.size();
		
		Marriages curMarriages = getManOptimal(); 

			
		for (int i = 0; i < setSize; i++)
		{
			curMarriages = eliminateRotation(curMarriages, maxDownset.get(i));
			
		}
		
		return curMarriages;	
	}
	
	// returns the summated weight of a rotation set
	private int getWeightOfRotationSet(LinkedList<Rotation> rots)
	{
		int weight = 0;
		int rotsSize = rots.size();
		
		for (int i = 0; i < rotsSize; i++)
		{
			weight += rots.get(i).getWeight();
		}
		
		return weight;
	}
	/************************************************************************************/
	
	
	/***************************Finding Min. Regret Marriage*****************************/
	
	// Check if the woman regret minimum marriage exists
	/* It exists iff there is at least one woman in the male optimal marriage with regret 
	   equal to the maximum regret of the marriage */
	private boolean womanRegretMinExists()
	{
		if (manOptimal == null)	
			throw new IllegalArgumentException("Man optimal marriage has not been found yet.");
		
		int manOptRegret = manOptimal.calculateMaxRegret();
		int regret;
		
		for (int i = 0; i < noOfWomen; i++)
		{
			regret = manOptimal.getWomansRegret(i + 1);
			
			if (regret == manOptRegret)
				return true;
		}
		
		return false;
	}
	
	
	// Check if the man regret minimum marriage exists
	/* It exists iff there is at least one man in the female optimal marriage with regret 
	   equal to the maximum regret of the marriage */
	private boolean manRegretMinExists()
	{
		if (womanOptimal == null)	
			throw new IllegalArgumentException("Woman optimal marriage has not been found yet.");
		
		int womanOptRegret = womanOptimal.calculateMaxRegret();
		int regret;
		
		for (int i = 0; i < noOfMen; i++)
		{
			regret = womanOptimal.getMansRegret(i + 1);
			
			if (regret == womanOptRegret)
				return true;
		}
		
		return false;
	}
	
	
	// We find the woman regret minimum using gusfield's algorithm B
	private Marriages findWomanRegretMin()
	{
		if (manOptimal == null || womanOptimal == null)	
			throw new IllegalArgumentException("Man or woman optimal marriage has not been found yet.");
		
		Marriages curMarriages = getManOptimal();
		Marriages possibleMarriages;
		int curWoman = curMarriages.findWomanWithMaxRegret();
		int spouse;
		
		// While there exists a woman with maximum regret in the currentMarriages
		/* If we encounter a marriage where there exists no woman with maximum regret 
		   then we return the last marriage where there did exist a woman with max regret */
		while (curWoman != -1)
		{
			spouse = curMarriages.getWomansSpouse(curWoman).getPersonNo();
			
			/* If the curWoman's spouse in the curMarriages is the same as her spouse in 
			   the woman optimal marriage return the curMarriages */
			if (spouse == womanOptimal.getWomansSpouse(curWoman).getPersonNo())
				return curMarriages;
			
			/* otherwise break the curMarriages to find a new stable marriage that moves 
			   closer to the female optimal marriage */
			possibleMarriages = breakMarriage(curMarriages, getMan(spouse));
			curWoman = possibleMarriages.findWomanWithMaxRegret();
			
			/* The newly found stable marriage becomes the curMarriages iff there exists
			   a woman with max regret in that new stable marriage */
			if (curWoman != -1)
				curMarriages = possibleMarriages;		
		}
		
		return curMarriages;
	}
	
	// We find the man regret minimum using gusfield's algorithm B
	private Marriages findManRegretMin()
	{
		if (manOptimal == null || womanOptimal == null)	
			throw new IllegalArgumentException("Man or woman optimal marriage has not been found yet.");
			
		Marriages curMarriages = getWomanOptimal();
		Marriages possibleMarriages;
		int curMan = curMarriages.findManWithMaxRegret();
		int spouse;
		
		// Continue while there exists a man with maximum regret in the currentMarriages
		/* If we encounter a marriage where there exists no man with maximum regret 
		   then we return the last marriage where there did exist a man with max regret */
		while (curMan != -1)
		{
			spouse = curMarriages.getMansSpouse(curMan).getPersonNo();
			
			/* If the curMan's spouse in the curMarriages is the same as his spouse in 
			   the man optimal marriage return the curMarriages */
			if (spouse == manOptimal.getMansSpouse(curMan).getPersonNo())
				return curMarriages;
			
			/* otherwise break the curMarriages to find a new stable marriage that moves 
			   closer to the male optimal marriage */
			possibleMarriages = breakMarriageWomen(curMarriages, getWoman(spouse));
			curMan = possibleMarriages.findManWithMaxRegret();
			
			/* The newly found stable marriage becomes the curMarriages iff there exists
			   a man with max regret in that new stable marriage */
			if (curMan != -1)
				curMarriages = possibleMarriages;	
			
		}
		
		return curMarriages;
	}
	
	
	public Marriages gusfieldsAlgorithmB()
	{
		/* The woman regret minimum doesn't exist if there is no woman with regret
	   equal to the maximum regret of the man optimal marriage. In such cases 
	   the man optimal marriage is the minimum regret marriage so we return it. */
		if (!womanRegretMinExists())
			return getManOptimal();
		
		/* The man regret minimum doesn't exist if there is no man with regret
	   equal to the maximum regret of the woman optimal marriage. In such cases 
	   the woman optimal marriage is the minimum regret marriage so we return it.*/	
		if (!manRegretMinExists())
			return getWomanOptimal();
		
		// If both the man and woman regret minimum's exist we can execute Gusfield's Alg B	
		Marriages minRegretMarriage;
		resetNextPrefs(manOptimal);
		Marriages womanRegretMin = findWomanRegretMin();
		resetNextPrefs(womanOptimal);
		Marriages manRegretMin = findManRegretMin();
		
		// Take the one with minimum max regret as the minimum regret marriage
		if (womanRegretMin.calculateMaxRegret() <= manRegretMin.calculateMaxRegret())
			minRegretMarriage = womanRegretMin;
			
		else
			minRegretMarriage = manRegretMin;
			
		return minRegretMarriage;
	}
	
	/************************************************************************************/
	
}