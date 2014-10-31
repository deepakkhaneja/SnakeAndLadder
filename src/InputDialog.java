//package SnakeAndLadder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//InputDialog GameSetup : sets the range of inputs
//	public int getW();
//	public int getH();
//	public int getSnakeNo();
//	public int getLadderNo();
//	public boolean isSnakeManual();
//	public boolean isLadderManual();
//	public int getPlayerNo();
//	public String getPlayerName(int serialNo);

class InputDialog extends JDialog {

	private Component owner = new Container();
	private JComboBox width = new JComboBox(), height = new JComboBox();
	private Integer dimension[];
	private JComboBox snake = new JComboBox(), ladder = new JComboBox();
	private Integer noOfPath[] = new Integer[Path.MAX_PATH];
	private JCheckBox snakeCheck = new JCheckBox("Manually set the snakes on board", false);
	private JCheckBox ladderCheck = new JCheckBox("Manually set the ladders on board", false);
	private JComboBox playerNo = new JComboBox();
	private Integer noOfPlayer[] = new Integer[Players.MAX_PLAYER];
	private JTextField playerNames[] = new JTextField[Players.MAX_PLAYER];
	private JButton okay = new JButton("OK");
	private Container contentPane;
	private static final long serialVersionUID = 9859973785L;	
	
	public InputDialog(Frame o) {
		
		super(o, "Game Setup", true);
		
		this.owner = o;
		//width and height
		width.addItem("Width of Board");
		height.addItem("Height of Board");
		
		dimension = new Integer[Board.MAX_DIM - Board.MIN_DIM + 1];
		for(int i = Board.MIN_DIM; i <= Board.MAX_DIM; i++) {
			dimension[i - Board.MIN_DIM] = new Integer(i);
			width.addItem(dimension[i - Board.MIN_DIM]);
			height.addItem(dimension[i - Board.MIN_DIM]);
		}

		//snakes and ladders
		snake.addItem("No. of Snakes");
		ladder.addItem("No. of Ladders");
		
		for(int i = 1; i <= Path.MAX_PATH; i++) {
			noOfPath[i - 1] = new Integer(i);
			snake.addItem(noOfPath[i - 1]);
			ladder.addItem(noOfPath[i - 1]);
		}

		JPanel dimensionPanel = new JPanel();
		dimensionPanel.setLayout(new FlowLayout());		
		dimensionPanel.add(width);
		dimensionPanel.add(height);
		
		JPanel pathPanel = new JPanel();
		pathPanel.setLayout(new GridLayout(4, 1, 7, 7));		
		pathPanel.add(snake);
		pathPanel.add(snakeCheck);
		pathPanel.add(ladder);
		pathPanel.add(ladderCheck);

		JPanel playerPanel = new JPanel();
		playerPanel.setLayout(new GridLayout(1 + Players.MAX_PLAYER, 1 , 7, 7));		
		playerPanel.add(playerNo);

		playerNo.addItem("No. of Players");
		
		for(int i = 1; i <= Players.MAX_PLAYER; i++) {
			noOfPlayer[i - 1] = new Integer(i);
			playerNo.addItem(noOfPlayer[i - 1]);
			playerNames[i - 1] = new JTextField("Player_" + i, 12);
			if(i > Players.DEFAULT_PLAYER) playerNames[i - 1].setEnabled(false);			 
			playerPanel.add(playerNames[i - 1]);
		}

		//player no. and names
		playerNo.addItemListener(
			new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getSource() == InputDialog.this.playerNo) {
						int i;
						for(i = 1; i <= InputDialog.this.getPlayerNo(); i++) InputDialog.this.playerNames[i - 1].setEnabled(true);
						for( ; i <= Players.MAX_PLAYER; i++) InputDialog.this.playerNames[i - 1].setEnabled(false);
					}
				}
			}
		);
				
		//okay Button event and bottomPanel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.add(okay);		
		bottomPanel.setPreferredSize(new Dimension(250, 100));
		okay.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					InputDialog.this.setVisible(false);
					JOptionPane.showMessageDialog(owner, "Click the \"Implement\" Button and start making snakes and ladders");

				}
			}
		);

		//contentPane		
		contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
		contentPane.add(dimensionPanel);
		contentPane.add(pathPanel);
		contentPane.add(playerPanel);
		contentPane.add(bottomPanel);
						
		setSize(350, 450);
	}

	public int getW() {
		Object o = width.getSelectedItem();
		if(o instanceof java.lang.Integer) return ((Integer)o).intValue(); 
		else return Board.DEFAULT_DIM;
	}
	
	public int getH() {
		Object o = height.getSelectedItem();
		if(o instanceof java.lang.Integer) return ((Integer)o).intValue(); 
		else return Board.DEFAULT_DIM;
	}
	
	public int getSnakeNo() {
		Object o = snake.getSelectedItem();
		if(o instanceof java.lang.Integer) return ((Integer)o).intValue(); 
		else return getH() - 3;
	}
	
	public int getLadderNo() {
		Object o = ladder.getSelectedItem();
		if(o instanceof java.lang.Integer) return ((Integer)o).intValue(); 
		else return getH() - 3;
	}
	
	public boolean isSnakeManual() {
		return snakeCheck.isSelected();
	}
	
	public boolean isLadderManual() {
		return ladderCheck.isSelected();
	}
	
	public int getPlayerNo() {
		Object o = playerNo.getSelectedItem();
		if(o instanceof java.lang.Integer) return ((Integer)o).intValue(); 
		else return Players.DEFAULT_PLAYER;
	}
	
	public String getPlayerName(int serialNo) {
		String name = playerNames[serialNo - 1].getText();
		if(name.equals("")) {
			name = "Player_" + serialNo;
			playerNames[serialNo - 1].setText(name);
			return name;
		}
		return name;
	}

}
