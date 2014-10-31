//package SnakeAndLadder;

import javax.swing.*;
import java.awt.*;

//maintains database of unique players, scores and set up(panel) the players when the game starts 
//	public void addPlayer(String name, Color color, int serialNo);
//	public void removePlayer(int serialNo);
//	public Player getPlayer(int serialNo);
//	public JPanel getPlayerPanel();

class Players {
	
	public final static int TOTAL_MAX = 30;  
	public final static int MAX_PLAYER = 3; //max players to play simultaneously
	public final static int DEFAULT_PLAYER = 2;  	
	public final static Color PLAYER_COLOR[] = { Color.red, Color.blue, new Color(42, 155, 19) };
	private int total;
	private Player allPlayers[];
	private int index[]; //map serialNo of current players to index of allPlayers
	private JPanel playerPanel;
	private JTextField playerIcons[];
	
	public Players() {
		total = 0;
		allPlayers = new Player[TOTAL_MAX]; 	
		playerPanel = new JPanel();
		playerPanel.setLayout(new FlowLayout());
		index = new int[MAX_PLAYER]; 
		playerIcons = new JTextField[MAX_PLAYER];

		for(int i = 0; i < MAX_PLAYER; i++) {
			playerIcons[i] = new JTextField(8);
			playerIcons[i].setBackground(PLAYER_COLOR[i]);	
			playerIcons[i].setFont(new Font("DejaVu Sans", Font.BOLD, 13));				
			playerIcons[i].setEnabled(false);
			playerIcons[i].setDisabledTextColor(Color.white);		
			playerPanel.add(playerIcons[i]);
		}
	}
	
	public void addPlayer(String name, int serialNo) {
		playerIcons[serialNo - 1].setText(name);
		
		//checking the name for existing players
		boolean done = false;
		for(int i = 0; i < total; i++) 		
			if(name.equals(allPlayers[i].getName())) { 
				index[serialNo - 1] = i;
				done = true;
			}
			
		//else entering a new player
		if(!done) {	
			allPlayers[total++] = new Player(name);
			index[serialNo - 1] = total - 1;
			done = true;	
		}
			
		//initialise player to play
		getPlayer(serialNo).steps = 0;			 
		getPlayer(serialNo).position = 1;			 
	}
	
	public void removePlayer(int serialNo) {
		playerIcons[serialNo - 1].setText("");		
	}

	//for setting the scores, get the player
	public Player getPlayer(int serialNo) {
		return allPlayers[index[serialNo - 1]];
	}
	
	public JPanel getPlayerPanel() {
		return playerPanel;
	}

}
