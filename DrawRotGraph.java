// Luke Britton August 2017
// This is the class where we actually physically draw the rotations/network flow graph
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import static java.awt.geom.AffineTransform.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
public class DrawRotGraph extends JPanel
{
	
	private Graph G;
	private ArrayList<ArrayList<Node<Rotation>>> blocks;
	private int noOfBlocks;
	private final int ARR_SIZE = 4;
	
	
	public DrawRotGraph(Graph G)
	{
		this.G = G;
		blocks = G.getBlocks();
		noOfBlocks = blocks.size();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		this.setBackground(Color.WHITE);
		
		int[] encountered = new int[noOfBlocks];
		for (int i = 0; i < noOfBlocks; i++)
				encountered[i] = -1;
				
		System.out.println("HEIGHT " + getHeight());
		
		if (noOfBlocks > 0)
		{
			DrawGraph(g, 0, getHeight()/4, 3*getHeight()/4, getWidth() / noOfBlocks,-1,blocks.get(0), encountered);
			DrawSource(g, getWidth()/2, getHeight()/8, 0, getHeight()/4, 3*getHeight()/4, getWidth() / noOfBlocks);
			DrawSink(g, getWidth()/2, 7*getHeight()/8, 0, getHeight()/4, 3*getHeight()/4, getWidth() / noOfBlocks);
		}
	}
	
	
	private void DrawSink(Graphics g, int x, int y, int StartWidth, int StartHeight, int EndHeight, int Level)
	{
		Node<Rotation> sink = G.getSink();
		g.setColor(Color.BLUE);
		String data = "T";
		String node = ".";
		Font font = new Font("Courier", Font.BOLD, 40);
    	g.setFont(font);
    	FontMetrics fm = g.getFontMetrics();
    	int nodeWidth = fm.stringWidth(node);
        int nodeHeight = fm.getHeight(); 	
      
       if (noOfBlocks % 2 == 1 && noOfBlocks != 1)
       		g.drawString(node, x - Level/2, y);
       	else
       		g.drawString(node, x, y);
       	
       	font = new Font("Courier", Font.BOLD, 20);
		g.setFont(font);
		fm = g.getFontMetrics();
		int dataWidth = fm.stringWidth(data);
		int dataHeight = fm.getHeight();
		
		if (noOfBlocks % 2 == 1 && noOfBlocks != 1)
			g.drawString(data, x - Level/2, y + dataHeight);
		else
			g.drawString(data, x, y + dataHeight);
		
		if (sink.hasParents())
		{
			int j = 0;
			while (sink.hasParent(j))
			{
				Node<Rotation> curParent = sink.getParent(j);
				int parBlockNo = curParent.getBlockNo();
				int parBlockSize = blocks.get(parBlockNo).size();
				int parBlockIndex = blocks.get(parBlockNo).indexOf(curParent);
				int SHeight;
				int EHeight;
				
				double xPos = StartWidth + parBlockNo*Level + Level/2;
				double yPos;
				double capXPos;
        		double capYPos; 
        		int cap = curParent.getCapacity(curParent.getNoOfChildren() - 1);
        		int flow = curParent.getFlow(curParent.getNoOfChildren() - 1);
        		String capacity = Integer.toString(flow) + "/"+Integer.toString(cap);
        		int capWidth = fm.stringWidth(capacity);
       			
				
				if (parBlockSize % 2 == 1 && parBlockSize != 1)
				{
					SHeight = StartHeight + (parBlockIndex)*(EndHeight - StartHeight)/(2*(parBlockSize - 1));
					EHeight = SHeight + (EndHeight - StartHeight)/(parBlockSize - 1);
				}
				
				else 
				{
					SHeight = StartHeight + (parBlockIndex)*(EndHeight - StartHeight)/parBlockSize;
					EHeight = SHeight + (EndHeight - StartHeight)/parBlockSize;
				}
				
				yPos = (SHeight + EHeight)/2;
				
				if (noOfBlocks % 2 == 1 && noOfBlocks != 1)
				{
					g.drawLine((int)xPos + nodeWidth/2, (int)yPos - nodeHeight/8, (int)x + nodeWidth/2 - Level/2, (int)y - nodeHeight/8);
					drawArrow(g, (int)xPos + nodeWidth/2, (int)yPos - nodeHeight/8, (int)x + nodeWidth/2 - Level/2, (int)y - nodeHeight/8);
					
					capXPos = ((int)xPos + nodeWidth/2 + (int)x + nodeWidth/2 - Level/2)/2;
					capYPos = ((int)yPos - nodeHeight/8 + (int)y - nodeHeight/8)/2;
				}
				
				else
				{
					g.drawLine((int)xPos + nodeWidth/2, (int)yPos - nodeHeight/8, (int)x + nodeWidth/2, (int)y - nodeHeight/8);
					drawArrow(g, (int)xPos + nodeWidth/2, (int)yPos - nodeHeight/8, (int)x + nodeWidth/2, (int)y - nodeHeight/8);
					
					capXPos = ((int)xPos + nodeWidth/2 + (int)x + nodeWidth/2)/2;
					capYPos = ((int)yPos - nodeHeight/8 + (int)y - nodeHeight/8)/2;
				}
				
				g.drawString(capacity, (int)capXPos - capWidth, (int)capYPos);
				j++;
			}
		}
		 
	}
	private void DrawSource(Graphics g, int x, int y, int StartWidth, int StartHeight, int EndHeight, int Level)
	{
		Node<Rotation> source = G.getSource();
		g.setColor(Color.BLUE);
		String data = "S";
		String node = ".";
		Font font = new Font("Courier", Font.BOLD, 40);
    	g.setFont(font);
    	FontMetrics fm = g.getFontMetrics();
    	int nodeWidth = fm.stringWidth(node);
        int nodeHeight = fm.getHeight(); 	
      	
      	if (noOfBlocks % 2 == 1 && noOfBlocks != 1)
       		g.drawString(node, x - Level/2, y);
       	else
       		g.drawString(node, x, y);
       	
       	font = new Font("Courier", Font.BOLD, 20);
		g.setFont(font);
		fm = g.getFontMetrics();
		int dataWidth = fm.stringWidth(data);
		int dataHeight = fm.getHeight();
		
		if (noOfBlocks % 2 == 1 && noOfBlocks != 1)
			g.drawString(data, x - Level/2, y - dataHeight);
		else
			g.drawString(data, x, y - dataHeight);
		
		if (source.hasChildren())
		{
			int j = 0;
			while (source.hasChild(j))
			{
				Node<Rotation> curChild = source.getChild(j);
				int childBlockNo = curChild.getBlockNo();
				int childBlockSize = blocks.get(childBlockNo).size();
				int childBlockIndex = blocks.get(childBlockNo).indexOf(curChild);
				int SHeight;
				int EHeight;
				
				double xPos = StartWidth + childBlockNo*Level + Level/2;
				double yPos;
				double capXPos;
        		double capYPos; 
        		int cap = source.getCapacity(j);
        		int flow = source.getFlow(j);
        		String capacity = Integer.toString(flow) + "/"+Integer.toString(cap);
        		int capWidth = fm.stringWidth(capacity);
       		
				
				if (childBlockSize % 2 == 1 && childBlockSize != 1)
				{
					SHeight = StartHeight + (childBlockIndex)*(EndHeight - StartHeight)/(2*(childBlockSize - 1));
					EHeight = SHeight + (EndHeight - StartHeight)/(childBlockSize - 1);
				}
				
				else 
				{
					SHeight = StartHeight + (childBlockIndex)*(EndHeight - StartHeight)/childBlockSize;
					EHeight = SHeight + (EndHeight - StartHeight)/childBlockSize;
				}
				
				yPos = (SHeight + EHeight)/2;
				
				if (noOfBlocks % 2 == 1 && noOfBlocks != 1)
				{
					g.drawLine((int)x + nodeWidth/2 - Level/2, (int)y - nodeHeight/8, (int)xPos + nodeWidth/2, (int)yPos - nodeHeight/8);
					drawArrow(g, (int)x + nodeWidth/2 - Level/2, (int)y - nodeHeight/8, (int)xPos + nodeWidth/2, (int)yPos - nodeHeight/8);
					
					capXPos = ((int)x + nodeWidth/2 - Level/2 + ((int)xPos + nodeWidth/2))/2;
					capYPos = ((int)y - nodeHeight/8 + ((int)yPos - nodeHeight/8))/2;
				}
				
				else
				{
					g.drawLine((int)x + nodeWidth/2, (int)y - nodeHeight/8, (int)xPos + nodeWidth/2, (int)yPos - nodeHeight/8);
					drawArrow(g, (int)x + nodeWidth/2, (int)y - nodeHeight/8, (int)xPos + nodeWidth/2, (int)yPos - nodeHeight/8);
					
					capXPos = ((int)x + nodeWidth/2  + ((int)xPos + nodeWidth/2))/2;
					capYPos = ((int)y - nodeHeight/8 + ((int)yPos - nodeHeight/8))/2;
				}
				
				g.drawString(capacity, (int)capXPos - capWidth, (int)capYPos);
				
				
				
				j++;
			}
		}
		 
	}
	
	
	private void DrawGraph(Graphics g,int StartWidth,int StartHeight,int EndHeight,int Level,int counter,
	ArrayList<Node<Rotation>> block, int[] encountered)
	{
        counter++;
        encountered[counter]++;
    	
        g.setColor(Color.BLACK);
        FontMetrics fm;
        
        int blockSize = block.size();
        int k = blockSize - 1;
        int l = 2*k;
        double sHeight;
        double eHeight;
        if (blockSize % 2 == 1 && blockSize != 1)
        {
			sHeight = StartHeight;
			eHeight = StartHeight + (EndHeight - StartHeight)/k;
		}
		
		else
		{
			sHeight = StartHeight;
			eHeight = StartHeight + (EndHeight - StartHeight)/blockSize;
		}
        for (int i = 0; i < blockSize; i++)
        {
        	
        	Node<Rotation> curNode = block.get(i);
        	int no = curNode.getLabel().getRotationNo();
        	String data = "π" + no;
        	String node = ".";
        	double xPos = StartWidth + Level/2;
        	double yPos;
        	
        	if (blockSize % 2 == 1 && blockSize != 1)
        	{
       			yPos = (sHeight + eHeight)/ 2;
       			sHeight = yPos;
       			eHeight = sHeight + (EndHeight - StartHeight)/k;	
       		}
       		
       		else
       		{
       			yPos = (sHeight + eHeight)/ 2;
       			sHeight = eHeight;
       			eHeight = sHeight + (EndHeight - StartHeight)/blockSize;
       		}
       		Font font = new Font("Courier", Font.BOLD, 40);
    		g.setFont(font);
    		fm = g.getFontMetrics();
       		int nodeWidth = fm.stringWidth(node);
        	int nodeHeight = fm.getHeight(); 	
       		g.drawString(node, (int)xPos, (int)(yPos));
       		
       		font = new Font("Courier", Font.BOLD, 20);
			g.setFont(font);
			fm = g.getFontMetrics();
			int dataWidth = fm.stringWidth(data);
        	int dataHeight = fm.getHeight();
        	g.drawString(data, (int)xPos, (int)(yPos) - dataHeight);
        	
        	if (counter != 0)
        	{
        		int j = 0;
        		while (curNode.hasParent(j))
        		{
        			Node<Rotation> curParent = curNode.getParent(j);
        			if (!curParent.isSource())
        			{
        				int parBlockNo = curParent.getBlockNo();
        				int parBlockSize = blocks.get(parBlockNo).size();
        				int parBlockIndex = blocks.get(parBlockNo).indexOf(curParent);
        				int parSHeight;
        				int parEHeight;
        				double x = StartWidth - (counter - parBlockNo)*Level + Level/2;
        				double y;
        			
        				
        				if (parBlockSize % 2 == 1 && parBlockSize != 1)
        				{
        					parSHeight = StartHeight + (parBlockIndex)*(EndHeight - StartHeight)/(2*(parBlockSize - 1));
        					parEHeight = parSHeight + (EndHeight - StartHeight)/(parBlockSize - 1);
        				}
        				
        				else 
        				{
        					parSHeight = StartHeight + (parBlockIndex)*(EndHeight - StartHeight)/parBlockSize;
        					parEHeight = parSHeight + (EndHeight - StartHeight)/parBlockSize;
        				}
        				
        				y = (parSHeight + parEHeight)/2;
        				
        				
        				g.drawLine((int)x + nodeWidth/2, (int)y - nodeHeight/8, (int)xPos + nodeWidth/2, (int)yPos - nodeHeight/8);
        				drawArrow(g, (int)x + nodeWidth/2, (int)y - nodeHeight/8, (int)xPos + nodeWidth/2, (int)yPos - nodeHeight/8);
        				
        				int f = curParent.getFlow(curParent.getChildIndex(curNode));
        				String flow = Integer.toString(f) + "/∞";
        				g.setColor(Color.BLUE);
        				
        				if (f > 0)
        					g.drawString(flow, (((int)x+nodeWidth/2) + ((int)xPos+nodeWidth/2))/2, ((int)y-nodeHeight/8 + ((int)yPos-nodeHeight/8))/2
        					 - dataHeight/2);
        				
        				g.setColor(Color.BLACK);
        			}
        			j++;
        		}
        	}	
        }
 
       	
        if (counter < noOfBlocks - 1)  
        		DrawGraph(g, StartWidth + Level, StartHeight, EndHeight, Level, counter, blocks.get(counter+1), encountered);
        			
    }
    
    
    
	private void drawArrow(Graphics g1, int x1, int y1, int x2, int y2)
	{
		Graphics2D g = (Graphics2D) g1.create();

		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx*dx + dy*dy)/4;
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		at.concatenate(AffineTransform.getRotateInstance(angle));
		g.transform(at);

		// Draw horizontal arrow starting in (0, 0)
		g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
					  new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
	}
}