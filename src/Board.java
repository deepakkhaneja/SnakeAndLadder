//package SnakeAndLadder;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

//set up the board graphics and panel
//	public void changePositionColor(int position, int serialNo);
//	public JPanel getBoardPanel();
//	public Point getCellOrigin (int position);
//	public Dimension getCellSize(Dimension d);

public class Board {

	public final static int MIN_DIM = 5, MAX_DIM = 12, DEFAULT_DIM = 8;
	public final static Color CELL_COLOR = new Color(236, 255, 159);	
	private int width, height;
	private JPanel boardPanel;
	private JTextArea cell[][];
			
	public Board(int width, int height) {
		cell = new JTextArea[Board.MAX_DIM][Board.MAX_DIM]; //width = no. of columns, height= no. of rows
		for(int i = 0; i < Board.MAX_DIM; i++)
			for(int j = 0; j < Board.MAX_DIM; j++)
				cell[i][j] = new JTextArea(1, 1);
		
		setBoard(width, height);		
	}
	
	//take a new JPanel() and set the board 
	public void setBoard(int width, int height) {
		this.width = width;
		this.height = height;
		
		boardPanel = new JPanel();
		boardPanel.setLayout( new GridLayout(this.height, this.width, 0, 0) );
		boardPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.blue, new Color(59, 158, 179)));
		
		for(int row = this.height - 1, col = this.width - 1; row >= 0; row--)
			for(int i = this.width - 1; i >= 0; i--) {
				if(row % 2 == 0) col = this.width - 1 - i; else col = i; //increasing or, decreasing order of cols
				cell[row][col].setEnabled(false);
				cell[row][col].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.blue, new Color(59, 158, 179)));
				cell[row][col].setBackground(CELL_COLOR);
				cell[row][col].setFont(new Font("DejaVu Sans", Font.BOLD, 13));				
				cell[row][col].setDisabledTextColor(new Color(85, 107, 47));
				cell[row][col].setText((new Integer(row * width + col + 1)).toString());
				boardPanel.add(cell[row][col]); 
			}
						
	} 

	//changes background color, given position and color, 1 <= position <= height * width
	public void changePositionColor(int position, Color c) {
		position--;
		cell[position / width][position % width].setBackground(c);
	}
	
	public JPanel getBoardPanel() {
		return boardPanel;
	}

	public Point getCellOrigin (int position) {
		position--;
		Dimension d = cell[position / width][position % width].getSize(null);		
		Point p = cell[position / width][position % width].getLocation(null);
		p.x = p.x + d.width / 2; 
		p.y = p.y + d.height / 2; 
		return p;
	}	
	
	public Dimension getCellSize(Dimension d) {
		return cell[0][0].getSize(d);
	} 	 
}	
