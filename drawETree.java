// Luke Britton August 2017
// This is the class where we actually physically draw the enumeration tree
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class drawETree extends JPanel
{
	
	private enumerationTree T;
	private int treeHeight;
	private int[] levels;
	
	
	public drawETree(enumerationTree T)
	{
		this.T = T;	
		treeHeight = T.getHeight(T.getRoot());
		levels = new int[treeHeight];	
		for (int i = 0; i < treeHeight; i++)
				levels[i] = 0;
				
		getLevels(levels,-1,T.getRoot());
		
		
	}
	
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		this.setBackground(Color.WHITE);
		
		
		int[] encountered = new int[treeHeight];
		for (int i = 0; i < treeHeight; i++)
				encountered[i] = -1;
		
		DrawTree(g, getWidth()/4, 3*getWidth()/4, 0, getHeight() / treeHeight,-1,0,0,T.getRoot(),encountered);
	}
	
	
	private void getLevels(int[] levels, int counter, Node<Rotation> node)
	{
		counter++;
		levels[counter]++;
		if (node.hasChildren())
		{
			int noOfChildren = node.getNoOfChildren();
			for (int i = 0; i < noOfChildren; i++)
        	{
				getLevels(levels, counter, node.getChild(i));
			}
		}
		
		
	}
	
	private int noOfPreviousNodes(int[] levels, int counter)
	{
		int sum = 0;
		for (int i = 0; i < counter; i++ )
			sum += levels[i];
			
		return sum;
	}
	
	public void DrawTree(Graphics g,int StartWidth,int EndWidth,int StartHeight,int Level,int counter,
	int x,int y,Node<Rotation> node, int[] encountered)
	{
        counter++;
        encountered[counter]++;
        int no = encountered[counter] + noOfPreviousNodes(levels, counter);
        String data = "M" + no;
        Font font = new Font("Courier", Font.BOLD, 20);
        g.setFont(font);
        g.setColor(Color.BLACK);
        FontMetrics fm = g.getFontMetrics();
        int dataWidth = fm.stringWidth(data);
        int dataHeight = fm.getHeight();
        int xPos = (StartWidth + EndWidth)/2 ;
        int yPos = StartHeight + Level / 2;
        g.drawString(data, xPos - dataWidth / 2, yPos);
		
		if (!node.isRoot())
		{
			g.drawLine(x, y, xPos, yPos - dataHeight/2);
			int label = node.getLabel().getRotationNo();
			String rot = "π" + label;
			int rotWidth = fm.stringWidth(rot);
			g.setColor(Color.BLUE);
			font.deriveFont(Font.PLAIN, 15);
			g.setFont(font);
			g.drawString(rot, (x + xPos)/2, (y + yPos - dataHeight/2)/2);
			
			
		}
			
        if (node.hasChildren())  
        {          
        	int noOfChildren = node.getNoOfChildren();
    		int newStartWidth = StartWidth;
    		int newEndWidth;
        	for (int i = 0; i < noOfChildren; i++)
        	{
        		newEndWidth = (i+1)*(StartWidth + EndWidth)/noOfChildren;
        		DrawTree(g, StartWidth, newEndWidth, StartHeight+Level, Level, counter, xPos, yPos, node.getChild(i), encountered);
        		newStartWidth = newEndWidth;
        		
        	}
        		
        	
        }
        
     
    }
}