// Luke Britton August 2017
// This class holds the enumeration tree graphic
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ETreeGraphic extends JFrame
{
	private JPanel contentPane;
	private enumerationTree T;
	private drawETree drawer;
	
	public ETreeGraphic(enumerationTree T)
	{
		super("Enumeration Tree");
		this.T = T;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 1000);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		drawETree drawer = new drawETree(T);
		contentPane.add(drawer);
		setContentPane(contentPane);
		setVisible(true);
	}
	
}