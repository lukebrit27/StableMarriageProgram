// Luke Britton June 2017
import java.util.*;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
public class StableMarriageProgram
{




	public static void main(String[] args)
	{




		double j;
		int x = 30;

		//while (x < 90)
		//{

		int n = x;
		int i = 0;
		j = 1;
		System.out.println("TEST BATCH " + x);
		//x+=1;


		double womanOptSocialAvg = 0;
		double womanOptRegretAvg = 0;
		double manOptSocialAvg = 0;
		double manOptRegretAvg = 0;
		double noOfStableMarriagesAvg = 0;
		double optSocialAvg = 0;
		double optRegretAvg = 0;
		double minRgtSocialAvg = 0;
		double minRgtAvg = 0;


		double womanOptSocialSD= 0;
		double womanOptRegretSD = 0;
		double manOptSocialSD = 0;
		double manOptRegretSD = 0;
		double noOfStableMarriagesSD = 0;
		double optSocialSD = 0;
		double optRegretSD = 0;
		double minRgtSocialSD = 0;
		double minRgtSD = 0;

		int maxWomanOptSC = 0;
		int maxWomanOptRGT = 0;
		int maxManOptSC = 0;
		int maxManOptRGT = 0;
		int maxEnumeration = 0;
		int maxOptSC = 0;
		int maxOptRGT = 0;
		int maxMinRgtSC = 0;
		int maxMinRgtRGT = 0;

		int minWomanOptSC = 0;
		int minWomanOptRGT = 0;
		int minManOptSC = 0;
		int minManOptRGT = 0;
		int minEnumeration = 0;
		int minOptSC = 0;
		int minOptRGT = 0;
		int minMinRgtSC = 0;
		int minMinRgtRGT = 0;



		ArrayList<Double> resultsWomanOptSC = new ArrayList<Double>();
		ArrayList<Double> resultsWomanOptRGT = new ArrayList<Double>();
		ArrayList<Double> resultsManOptSC = new ArrayList<Double>();
		ArrayList<Double> resultsManOptRGT = new ArrayList<Double>();
		ArrayList<Double> resultsEnmtn = new ArrayList<Double>();
		ArrayList<Double> resultsOptSC = new ArrayList<Double>();
		ArrayList<Double> resultsOptRGT = new ArrayList<Double>();
		ArrayList<Double> resultsMinRgtSC = new ArrayList<Double>();
		ArrayList<Double> resultsMinRgtRGT = new ArrayList<Double>();





		while (i < (int)j)
		{
			i++;
			if (i%50000 == 0)
				System.out.println(i + " tests completed");


			/**************************Population Initiation*****************************/
			System.out.println("PROBLEM " + i);
			int noOfMen = n;
			int noOfWomen = n;
			int[][] malePrefLists = new int[noOfMen][noOfWomen];
			int[][] femalePrefLists = new int[noOfWomen][noOfMen];


			malePrefLists[0] = new int[]{1,2,3};
			malePrefLists[1] = new int[]{2,3,1};
			malePrefLists[2] = new int[]{3,2,1};

			femalePrefLists[0] = new int[]{1,2,3};
			femalePrefLists[1] = new int[]{2,3,1};
			femalePrefLists[2] = new int[]{3,2,1};

			MarriageProblem  problem = new MarriageProblem(noOfMen, noOfWomen);
			//problem.initiatePop(malePrefLists, femalePrefLists);
			problem.initiateRandomPop();
			problem.printPrefLists();
			/**************************Population Initiation*****************************/


			/************************Algorithms being Executed***************************/

			// WOMAN OPT
			Marriages womanOptimal = problem.getWomanOptimal();
			int wOptSC = womanOptimal.calculateSocialCost();
			int wOptRGT = womanOptimal.calculateMaxRegret();

			// MAN OPT
			Marriages manOptimal = problem.getManOptimal();
			int mOptSC = manOptimal.calculateSocialCost();
			int mOptRGT = manOptimal.calculateMaxRegret();

			// ROTATIONS
			problem.gusfieldsAlgorithmA();
			problem.constructPartialOrder();

			// ENUMERATION
			enumerationTree T = problem.generateEnumerationTree();
			int noOfStableMarriages = problem.getNoOfStableMarriages(T);

			// EGALITARIAN
			Graph G = problem.createGraph();
			G.FordFulkerson();
			G.findMinCut();
			Marriages optMarriages = problem.getOptimalMarriages(G);
			int optSC = optMarriages.calculateSocialCost();
			int optRGT = optMarriages.calculateMaxRegret();

			// MINIMUM REGRET
			Marriages minRegretMarriages = problem.gusfieldsAlgorithmB();
			int minRgtSC = minRegretMarriages.calculateSocialCost();
			int minRgtRGT = minRegretMarriages.calculateMaxRegret();

			/************************Algorithms being Executed***************************/



			/************************Woman and Man Optimal Prints*************************/

			System.out.println("WOMAN OPTIMAL");
			womanOptimal.printMarriages();

			System.out.println("Social Cost: " + wOptSC);
			System.out.println("Max Regret: " + wOptRGT);

			System.out.println();
			System.out.println("MAN OPTIMAL");
			manOptimal.printMarriages();

			System.out.println("Social Cost: " + mOptSC);
			System.out.println("Max Regret: " + mOptRGT);

			/************************Woman and Man Optimal Prints*************************/

			System.out.println();

			/******************************Rotation Prints********************************/

			System.out.println("ROTATIONS");
			problem.printRotations();
			System.out.println("No of rotations: " + problem.getNoOfRotations());
			problem.printAllRotPredecessors();

			/******************************Rotation Prints********************************/

			System.out.println();

			/*****************************Enumeration Prints*******************************/
			System.out.println("No of Stable Marriages: " + noOfStableMarriages);
			/*****************************Enumeration Prints*******************************/

			System.out.println();

			/******************************Optimal Prints**********************************/

			System.out.println("\nEgalitarian Optimal Stable Marriage");
			optMarriages.printMarriages();
			System.out.println("Social Cost: " + optSC);
			System.out.println("Max Regret: " + optRGT);

			/******************************Optimal Prints**********************************/

			System.out.println();

			/*****************************Min Regret Prints********************************/

			System.out.println("\nMinimum Regret Stable Marriage");
			minRegretMarriages.printMarriages();
			System.out.println("Social Cost: " + minRgtSC);
			System.out.println("Max Regret: " + minRgtRGT);
			/*****************************Min Regret Prints********************************/



			/******************************Average Calcs*********************************/
			womanOptSocialAvg += wOptSC/j;
			womanOptRegretAvg += wOptRGT/j;
			manOptSocialAvg += mOptSC/j;
			manOptRegretAvg += mOptRGT/j;
			noOfStableMarriagesAvg += noOfStableMarriages/j;
			optSocialAvg += optSC/j;
			optRegretAvg += optRGT/j;
			minRgtSocialAvg += minRgtSC/j;
			minRgtAvg += minRgtRGT/j;
			/******************************Average Calcs*********************************/


			/****************************Max and Min Calcs*******************************/
			maxWomanOptSC = Math.max(maxWomanOptSC, wOptSC);
			maxWomanOptRGT = Math.max(maxWomanOptRGT, wOptRGT);
			maxManOptSC = Math.max(maxManOptSC, mOptSC);
			maxManOptRGT = Math.max(maxManOptRGT, mOptRGT);
			maxEnumeration = Math.max(noOfStableMarriages, maxEnumeration);
			maxOptSC = Math.max(maxOptSC, optSC);
			maxOptRGT = Math.max(maxOptRGT, optRGT);
			maxMinRgtSC = Math.max(maxMinRgtSC, minRgtSC);
			maxMinRgtRGT = Math.max(maxMinRgtRGT, minRgtRGT);

			if (i == 1)
			{
				minWomanOptSC =wOptSC;
				minWomanOptRGT = wOptRGT;
				minManOptSC = mOptSC;
				minManOptRGT = mOptRGT;
				minEnumeration = noOfStableMarriages;
				minOptSC = optSC;
				minOptRGT = optRGT;
				minMinRgtSC = minRgtSC;
				minMinRgtRGT = minRgtRGT;
			}

			else
			{
				minWomanOptSC = Math.min(minWomanOptSC, wOptSC);
				minWomanOptRGT = Math.min(minWomanOptRGT, wOptRGT);
				minManOptSC = Math.min(minManOptSC, mOptSC);
				minManOptRGT = Math.min(minManOptRGT, mOptRGT);
				minEnumeration = Math.min(noOfStableMarriages, minEnumeration);
				minOptSC = Math.min(minOptSC, optSC);
				minOptRGT = Math.min(minOptRGT, optRGT);
				minMinRgtSC = Math.min(minMinRgtSC, minRgtSC);
				minMinRgtRGT = Math.min(minMinRgtRGT, minRgtRGT);
			}



			/****************************Max and Min Calcs*******************************/


			/******************************Results Calcs*********************************/
			resultsWomanOptSC.add((double)wOptSC);
			resultsWomanOptRGT.add((double)wOptRGT);
			resultsManOptSC.add((double)mOptSC);
			resultsManOptRGT.add((double)mOptRGT);
			resultsEnmtn.add((double)noOfStableMarriages);
			resultsOptSC.add((double)optSC);
			resultsOptRGT.add((double)optRGT);
			resultsMinRgtSC.add((double)minRgtSC);
			resultsMinRgtRGT.add((double)minRgtRGT);
			/******************************Results Calcs*********************************/


			/*********************************Graphs*************************************/
			if (j == 1)
			{
				ETreeGraphic eTreeGraph = new ETreeGraphic(T);
				RotGraphGraphic rotsGraph = new RotGraphGraphic(G);
			}
			/*********************************Graphs*************************************/


		}


		System.out.println();


		System.out.println("Problem Size = " + n + ", NoOfTests = " + j);
		System.out.println ("manOptSocialAvg " + (manOptSocialAvg));
		System.out.println ("manOptRegretAvg " + (manOptRegretAvg));
		System.out.println ("womanOptSocialAvg " + (womanOptSocialAvg));
		System.out.println ("womanOptRegretAvg " + (womanOptRegretAvg));
		System.out.println ("noOfStableMarriagesAvg " + (noOfStableMarriagesAvg));
		System.out.println ("optSocialAvg " + (optSocialAvg));
		System.out.println ("optRegretAvg " + (optRegretAvg));
		System.out.println ("minRgtSocialAvg " + (minRgtSocialAvg));
		System.out.println ("minRgtAvg " + (minRgtAvg));

		System.out.println();



		System.out.println ("maxManOptSC " + maxManOptSC);
		System.out.println ("maxManOptRGT " + maxManOptRGT);
		System.out.println ("maxWomanOptSC " + maxWomanOptSC);
		System.out.println ("maxWomanOptRGT " + maxWomanOptRGT);
		System.out.println ("MaxEnumeration " + maxEnumeration);
		System.out.println ("maxOptSC " + maxOptSC);
		System.out.println ("maxOptRGT " + maxOptRGT);
		System.out.println ("maxMinRgtSC " + maxMinRgtSC);
		System.out.println ("maxMinRgtRGT " + maxMinRgtRGT);

		System.out.println();

		System.out.println ("minManOptSC " + minManOptSC);
		System.out.println ("minManOptRGT " + minManOptRGT);
		System.out.println ("minWomanOptSC " + minWomanOptSC);
		System.out.println ("minWomanOptRGT " + minWomanOptRGT);
		System.out.println ("MinEnumeration " + minEnumeration);
		System.out.println ("minOptSC " + minOptSC);
		System.out.println ("minOptRGT " + minOptRGT);
		System.out.println ("minMinRgtSC " + minMinRgtSC);
		System.out.println ("minMinRgtRGT " + minMinRgtRGT);


		System.out.println();
		/****************************Standard Deviation Calcs*****************************/
		for (int k = 0; k < j; k++)
		{
			womanOptSocialSD += (resultsWomanOptSC.get(k) - womanOptSocialAvg)*(resultsWomanOptSC.get(k) - womanOptSocialAvg);
			womanOptRegretSD += (resultsWomanOptRGT.get(k) - womanOptRegretAvg)*(resultsWomanOptRGT.get(k) - womanOptRegretAvg);
			manOptSocialSD += (resultsManOptSC.get(k) - manOptSocialAvg)*(resultsManOptSC.get(k) - manOptSocialAvg);
			manOptRegretSD += (resultsManOptRGT.get(k) - manOptRegretAvg)*(resultsManOptRGT.get(k) - manOptRegretAvg);
			noOfStableMarriagesSD += (resultsEnmtn.get(k) - noOfStableMarriagesAvg)*(resultsEnmtn.get(k) - noOfStableMarriagesAvg);
			optSocialSD += (resultsOptSC.get(k) - optSocialAvg)*(resultsOptSC.get(k) - optSocialAvg);
			optRegretSD += (resultsOptRGT.get(k) - optRegretAvg)*(resultsOptRGT.get(k) - optRegretAvg);
			minRgtSocialSD += (resultsMinRgtSC.get(k) - minRgtSocialAvg)*(resultsMinRgtSC.get(k) - minRgtSocialAvg);
			minRgtSD += (resultsMinRgtRGT.get(k) - minRgtAvg)*(resultsMinRgtRGT.get(k) - minRgtAvg);
		}

		womanOptSocialSD = womanOptSocialSD/j;
		womanOptSocialSD = Math.sqrt(womanOptSocialSD);
		womanOptRegretSD = womanOptRegretSD/j;
		womanOptRegretSD = Math.sqrt(womanOptRegretSD);
		manOptSocialSD = manOptSocialSD/j;
		manOptSocialSD = Math.sqrt(manOptSocialSD);
		manOptRegretSD = manOptRegretSD/j;
		manOptRegretSD = Math.sqrt(manOptRegretSD);
		noOfStableMarriagesSD = noOfStableMarriagesSD/j;
		noOfStableMarriagesSD = Math.sqrt(noOfStableMarriagesSD);
		optSocialSD = optSocialSD/j;
		optSocialSD = Math.sqrt(optSocialSD);
		optRegretSD = optRegretSD/j;
		optRegretSD = Math.sqrt(optRegretSD);
		minRgtSocialSD = minRgtSocialSD/j;
		minRgtSocialSD = Math.sqrt(minRgtSocialSD);
		minRgtSD = minRgtSD/j;
		minRgtSD = Math.sqrt(minRgtSD);


		System.out.println("manOptSocialSD " + manOptSocialSD);
		System.out.println("manOptRegretSD " + manOptRegretSD);
		System.out.println("womanOptSocialSD " + womanOptSocialSD);
		System.out.println("womanOptRegretSD " + womanOptRegretSD);
		System.out.println("noOfStableMarriagesSD " + noOfStableMarriagesSD);
		System.out.println("optSocialSD " + optSocialSD);
		System.out.println("optRegretSD " + optRegretSD);
		System.out.println("minRgtSocialSD " + minRgtSocialSD);
		System.out.println("minRgtSD " + minRgtSD);
		/****************************Standard Deviation Calcs*****************************/


		//}








	}
}
