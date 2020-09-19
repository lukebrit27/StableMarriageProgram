// Luke Britton August 2017
// This class holds the rotations/network flow graphic
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class RotGraphGraphic extends JFrame
{
	private JPanel contentPane;
	private Graph G;
	private DrawRotGraph drawer;
	
	public RotGraphGraphic(Graph G)
	{
		super("Rotations Graph");
		this.G = G;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 1000);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		DrawRotGraph drawer = new DrawRotGraph(G);
		contentPane.add(drawer);
		setContentPane(contentPane);
		setVisible(true);
	}
}