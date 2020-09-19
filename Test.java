import java.util.*;
import javax.swing.*;
public class Test
{
	public static void main(String[] args)
	{
		/*Male[] men = new Male[2];
		Female[] women = new Female[2];
		
		for (int i = 0; i < 2; i++)
		{
			men[i] = new Male(i+1, 2);
			women[i] = new Female(i+1, 2);
			
		}
		
		for (int i = 0; i < 2; i++)
		{
			men[i].generatePrefList(women);
			women[i].generatePrefList(men);
		}
		
		men[0].addSpouse(women[0]);
		
		System.out.println("Spouse of man 1: " + men[0].getSpouse().getPersonNo());
		*/
		//LinkedList<LinkedList> hello;
		
		
		// Efficient alg for "optimal" stable marriage exmaple
		/*
		malePrefLists[0] = new int[]{3,1,5,7,4,2,8,6};
		malePrefLists[1] = new int[]{6,1,3,4,8,7,5,2};
		malePrefLists[2] = new int[]{7,4,3,6,5,1,2,8};
		malePrefLists[3] = new int[]{5,3,8,2,6,1,4,7};
		malePrefLists[4] = new int[]{4,1,2,8,7,3,6,5};
		malePrefLists[5] = new int[]{6,2,5,7,8,4,3,1};
		malePrefLists[6] = new int[]{7,8,1,6,2,3,4,5};
		malePrefLists[7] = new int[]{2,6,7,1,8,3,4,5};
		
		femalePrefLists[0] = new int[]{4,3,8,1,2,5,7,6};
		femalePrefLists[1] = new int[]{3,7,5,8,6,4,1,2};
		femalePrefLists[2] = new int[]{7,5,8,3,6,2,1,4};
		femalePrefLists[3] = new int[]{6,4,2,7,3,1,5,8};
		femalePrefLists[4] = new int[]{8,7,1,5,6,4,3,2};
		femalePrefLists[5] = new int[]{5,4,7,6,2,8,3,1};
		femalePrefLists[6] = new int[]{1,4,5,6,2,8,3,7};
		femalePrefLists[7] = new int[]{2,5,4,3,7,8,1,6};
		
		
		
		
		// Gusfield's(1987) example
		malePrefLists[0] = new int[]{5,7,1,2,6,8,4,3};
		malePrefLists[1] = new int[]{2,3,7,5,4,1,8,6};
		malePrefLists[2] = new int[]{8,5,1,4,6,2,3,7};
		malePrefLists[3] = new int[]{3,2,7,4,1,6,8,5};
		malePrefLists[4] = new int[]{7,2,5,1,3,6,8,4};
		malePrefLists[5] = new int[]{1,6,7,5,8,4,2,3};
		malePrefLists[6] = new int[]{2,5,7,6,3,4,8,1};
		malePrefLists[7] = new int[]{3,8,4,5,7,2,6,1};
		
		femalePrefLists[0] = new int[]{5,3,7,6,1,2,8,4};
		femalePrefLists[1] = new int[]{8,6,3,5,7,2,1,4};
		femalePrefLists[2] = new int[]{1,5,6,2,4,8,7,3};
		femalePrefLists[3] = new int[]{8,7,3,2,4,1,5,6};
		femalePrefLists[4] = new int[]{6,4,7,3,8,1,2,5};
		femalePrefLists[5] = new int[]{2,8,5,4,6,3,7,1};
		femalePrefLists[6] = new int[]{7,5,2,1,8,6,4,3};
		femalePrefLists[7] = new int[]{7,4,1,5,2,3,6,8};
		
		
		
		// Randomly generated example
		malePrefLists[0] = new int[]{8,2,6,5,7,3,1,4};
		malePrefLists[1] = new int[]{4,5,2,8,7,3,1,6};
		malePrefLists[2] = new int[]{7,1,6,5,2,4,8,3};
		malePrefLists[3] = new int[]{2,1,8,3,7,5,6,4};
		malePrefLists[4] = new int[]{5,4,7,8,2,6,3,1};
		malePrefLists[5] = new int[]{2,6,5,3,7,8,1,4};
		malePrefLists[6] = new int[]{3,7,5,4,6,2,8,1};
		malePrefLists[7] = new int[]{3,4,7,2,8,6,1,5};
		
		femalePrefLists[0] = new int[]{2,3,6,8,5,7,1,4};
		femalePrefLists[1] = new int[]{6,1,5,7,4,2,8,3};
		femalePrefLists[2] = new int[]{3,2,1,8,7,4,6,5};
		femalePrefLists[3] = new int[]{3,7,1,2,4,6,5,8};
		femalePrefLists[4] = new int[]{6,2,4,8,5,7,3,1};
		femalePrefLists[5] = new int[]{8,1,3,6,7,5,2,4};
		femalePrefLists[6] = new int[]{3,6,8,5,4,7,1,2};
		femalePrefLists[7] = new int[]{2,5,8,1,7,6,3,4};
		*/
		
		
		
		// Self Constructed 2n example
		/*
			for (int a = 0; a < n; a+=2)
			{
				malePrefLists[a][0] = a + 1;
				malePrefLists[a][1] = a + 2;
				malePrefLists[a + 1][0] = a + 2;
				malePrefLists[a + 1][1] = a + 1;
				femalePrefLists[a][0] = a + 2;
				femalePrefLists[a][1] = a + 1;
				femalePrefLists[a + 1][0] = a + 1;
				femalePrefLists[a + 1][1] = a + 2;
			}
		
			
			for (int a = 0; a < noOfMen; a++)
			{
				int max = Math.max(malePrefLists[a][0], malePrefLists[a][1]);
				int min = Math.min(malePrefLists[a][0], malePrefLists[a][1]);
			
					for (int b = 1; b < min; b++)
						malePrefLists[a][b+1] = b;
			
					for (int b = max; b < noOfWomen; b++)
						malePrefLists[a][b] = b + 1;
				
			}
		
			for (int a = 0; a < noOfWomen; a++)
			{
				int max = Math.max(femalePrefLists[a][0], femalePrefLists[a][1]);
				int min = Math.min(femalePrefLists[a][0], femalePrefLists[a][1]);
				for (int b = 1; b < min; b++)
					femalePrefLists[a][b+1] = b;
				for (int b = max; b < noOfMen; b++)
					femalePrefLists[a][b] = b + 1;
			
			}
			*/
			
			
			// One stable marriage example
			/*
			malePrefLists[0] = new int[]{1,2,3};
			malePrefLists[1] = new int[]{2,3,1};
			malePrefLists[2] = new int[]{3,2,1};
			
			femalePrefLists[0] = new int[]{1,2,3};
			femalePrefLists[1] = new int[]{2,3,1};
			femalePrefLists[2] = new int[]{3,2,1};
			*/
		
		JFrame f = new JFrame("Title");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(400, 250);
		f.setVisible(true);
	}
}