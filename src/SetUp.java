//package SnakeAndLadder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


//Specifications of the game (all can be changed) :
//Board.MIN_DIM = 5
//Board.MIN_DIM = 12 (width and height can be different)
//MAX_PLAYER    = 3  (Max no. of players to play simultaneously)
//TOTAL_MAX     = 30 (Max no. of players allowed)
//MAX_PATH      = 15 (Max no. of snakes or, ladders allowed)

//Default Settings :
//default no. of snakes  = height - 3
//default no. of ladders = height - 3
//DEFAULT_PLAYER         = 2
//DEFAULT_DIM            = 10

//Class SetUp : Main Frame
//	public SetUp();
//	Inner Class : private class GlassPane extends JComponent implements MouseListener, MouseMotionListener 
//				  public GlassPane();
//				  public void mouseClicked(MouseEvent e);
//				  public void paint(Graphics g);
//				  private void startGame();
//				  private void stopGame();
//		          private void changePlayerPosition(int serialNo, int position);
//				  private void callOneThirdSecond();
//				  private void firstTimerStopped(int position);
//				  private void nextPlayerTurn(int position);
//				  private void paintSnake(Graphics g, Point p1, Point p2);
//		    	  private void paintLadder(Graphics g, Point p1, Point p2);
class SetUp extends JFrame {
	private Dimension cellSize = new Dimension();
	private int tokenSize;
	private Players players = new Players();
	private Board board = new Board(Board.DEFAULT_DIM, Board.DEFAULT_DIM);
	private Path snakes[]  = new Path[Path.MAX_PATH];
	private Path ladders[] = new Path[Path.MAX_PATH];
	private InputDialog inputDialog = new InputDialog(this); 
	private GlassPane glassPane = new GlassPane();
	private JButton startButton = new JButton("Start");
	private JButton rollButton = new JButton("Roll dice");
	private JLabel status = new JLabel("Welcome to the Snake And Ladder, click the Start button");
	private Container gameContentPane;
	private static final long serialVersionUID = 9859978976L;		
	
	//initial construction of game			
	public SetUp(){
		super("Snake And Ladder");
	
		int side = -1;
		for(int i = 0; i < Path.MAX_PATH; i++) {
			side *= -1;
			snakes[i] = new Path(Path.SNAKE, side);
			ladders[i] = new Path(Path.LADDER, side);
		}	
			
		//bottomPanel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.setPreferredSize(new Dimension(400, 125));		
		bottomPanel.add(players.getPlayerPanel(), BorderLayout.NORTH);
		bottomPanel.add(startButton, BorderLayout.WEST);
		bottomPanel.add(rollButton, BorderLayout.EAST);
		status.setFont( new Font( "DejaVu Sans", Font.BOLD , 14 ) );
		bottomPanel.add(status, BorderLayout.SOUTH);
		
		//gameContentPane
		gameContentPane = getContentPane();
		gameContentPane.setLayout(new BorderLayout());
		gameContentPane.add(board.getBoardPanel(), BorderLayout.CENTER);
		gameContentPane.add(bottomPanel, BorderLayout.SOUTH);
			
		//enabling the glass pane
		setGlassPane(glassPane);	
		glassPane.setVisible(true);	

		cellSize.width = cellSize.height = 60;
		tokenSize = 20;
		setSize(3 + cellSize.width * inputDialog.getW(), 100 + cellSize.height * inputDialog.getH());
		setVisible(true);
	}
	
	//Main Control : to paint over components and catch mouse events
	private class GlassPane extends JComponent implements MouseListener, MouseMotionListener {
	
		public final static int INPUT = 1, IMPLEMENT = 2, PAINT_PATH_FIRST = 3, PAINT_PATH_SECOND = 4, PAINT_PATH_THIRD = 5, START = 6; 
		private int state = INPUT;
		private int value = 6;
		private int snakeNo, ladderNo;
		private Dimension currentSize;
		private boolean mustUpdatePoints = false;
		private Random rnd = new Random();
		private boolean secondClick = false, evenNoGame = false;
		private int tmpPosition, serialNo = 0;
		private javax.swing.Timer timer;
		private boolean isFollowPath = false;
		private Point followPathPoint;
		private Path followPath;
		private int movingPosition, finalPosition, step;
		private static final long serialVersionUID = 9859978976L;				
		
		public GlassPane() {
			addMouseListener(this);
			addMouseMotionListener(this);
		 	timer = new javax.swing.Timer(333, new ActionListener() {
 	
				public void actionPerformed(ActionEvent event) 
 				{
					if(isFollowPath) callOneTwentiethSecond();
					else 
					callOneThirdSecond();
 				}
 			} );
		}

	    public void mouseMoved(MouseEvent e) {
	    }

	    public void mouseDragged(MouseEvent e) {
	    }
			
	    public void mouseEntered(MouseEvent e) {
	    }
	
	    public void mouseExited(MouseEvent e) {
	    }
	
	    public void mousePressed(MouseEvent e) {
	    }
		
	    public void mouseReleased(MouseEvent e) {
	    }
	    		
	    public void mouseClicked(MouseEvent e) {
			Point p = e.getPoint();
			Component component;

			switch(state) {
				case INPUT :		        
					p = SwingUtilities.convertPoint(this, p , gameContentPane);    	
		        	component = SwingUtilities.getDeepestComponentAt(gameContentPane, p.x, p.y);

			        if(component == startButton){
			        	startButton.setText("Implement");			        			        
			        	inputDialog.setVisible(true);
			        	state = IMPLEMENT;
			        	status.setText("Click Implement button");
			        }
			        break;

				case IMPLEMENT :
					p = SwingUtilities.convertPoint(this, p , gameContentPane);    	
		        	component = SwingUtilities.getDeepestComponentAt(gameContentPane, p.x, p.y);

			        if(component == startButton) {					
						//set width and height : board
						gameContentPane.remove(board.getBoardPanel());
						int a = inputDialog.getW();
						int b = inputDialog.getH();
						board.setBoard(a, b);
						cellSize.width = cellSize.height = 60;
						if(evenNoGame) SetUp.this.setSize(3 + cellSize.width * a, 100 + cellSize.height * b);
						else SetUp.this.setSize(4 + cellSize.width * a, 101 + cellSize.height * b);
						currentSize = this.getSize();
						evenNoGame = !evenNoGame; //for the next game, only use of evenNoGame variable
						gameContentPane.add(board.getBoardPanel(), BorderLayout.CENTER);
						snakeNo = ladderNo = 0;
						repaint();
						
						//setting up Players
						for(int serialNo = 1; serialNo <= inputDialog.getPlayerNo(); serialNo++) 
							players.addPlayer(inputDialog.getPlayerName(serialNo), serialNo);
							
						state = PAINT_PATH_FIRST; 
						status.setText("Just click anywhere to Start!!");
												
					}	
					break;
				
				case PAINT_PATH_FIRST :
					status.setText("Click two cells, one by one, make snakes(" + inputDialog.getSnakeNo() + ") and ladders(" + inputDialog.getLadderNo() + ")");
					if(!inputDialog.isSnakeManual()) {
						snakeNo = inputDialog.getSnakeNo();
						int i = 0,j,k,tmp;
						while(i < snakeNo) {
							j = rnd.nextInt(inputDialog.getW() * inputDialog.getH() - 1) + 1;
							k = rnd.nextInt(inputDialog.getW() * inputDialog.getH() - 1) + 1;
							if((j - 1)/inputDialog.getW() == (k - 1)/inputDialog.getW()) continue;
							if(j < k) {	tmp = j; j = k; k = tmp; }
							snakes[i].high = j;  snakes[i].low = k;
							//snakes[i].p2 = board.getCellOrigin(j);
							//snakes[i].p1 = board.getCellOrigin(k);
							i++;							
 						}
						mustUpdatePoints = true;
						updatePathPoints();
					}
						
					if(!inputDialog.isLadderManual()) {
						ladderNo = inputDialog.getLadderNo();
						int i = 0,j,k,tmp;
						while(i < ladderNo) {
							j = rnd.nextInt(inputDialog.getW() * inputDialog.getH() - 1) + 1;
							k = rnd.nextInt(inputDialog.getW() * inputDialog.getH() - 1) + 1;
							if((j - 1)/inputDialog.getW() == (k - 1)/inputDialog.getW()) continue;
							if(j < k) {	tmp = j; j = k; k = tmp; }
							ladders[i].high = j;  ladders[i].low = k;
							//ladders[i].p2 = board.getCellOrigin(j);
							//ladders[i].p1 = board.getCellOrigin(k);							
							i++;							
 						}
						mustUpdatePoints = true;
						updatePathPoints();
					}
					
					repaint();
					if(!inputDialog.isSnakeManual() && !inputDialog.isLadderManual()) startGame();								
					else if(inputDialog.isSnakeManual()) state = PAINT_PATH_SECOND;
					else state = PAINT_PATH_THIRD;
					break;
				
				case PAINT_PATH_SECOND :
					status.setText("Click two cells, one by one, make snakes(" + inputDialog.getSnakeNo() + ") and ladders(" + inputDialog.getLadderNo() + ")");
					p = SwingUtilities.convertPoint(this, p , gameContentPane);    	
		        	component = SwingUtilities.getDeepestComponentAt(gameContentPane, p.x, p.y);
					if((component != null) && (component instanceof JTextArea)) {
						if(!secondClick) {
							tmpPosition = (new Integer(((JTextArea)component).getText())).intValue(); 
							secondClick = !secondClick;	
						} else {
							int pos = (new Integer(((JTextArea)component).getText())).intValue();
							if((pos-1)/inputDialog.getW() == (tmpPosition-1)/inputDialog.getW()) {
								status.setText("Try again!!, a snake or, ladder never runs at the same level");
								break;
							}
							
							//successful second click
							snakes[snakeNo].high = pos;  
							snakes[snakeNo].low = tmpPosition;  
							if(snakes[snakeNo].high < snakes[snakeNo].low) {
								pos = snakes[snakeNo].high;  				
								snakes[snakeNo].high = snakes[snakeNo].low; 
								snakes[snakeNo].low = pos; 					
							}
							snakeNo++; //current no. of snakes
							mustUpdatePoints = true;
							updatePathPoints();
							repaint();
							secondClick = !secondClick;
							//if state changes or, not
							if(snakeNo == inputDialog.getSnakeNo()) {
								if(inputDialog.isLadderManual()) state = PAINT_PATH_THIRD; 
								else startGame();
							}
						}						
					} break;
					
				case PAINT_PATH_THIRD :
					status.setText("Click two cells, one by one, make snakes(" + inputDialog.getSnakeNo() + ") and ladders(" + inputDialog.getLadderNo() + ")");
					p = SwingUtilities.convertPoint(this, p , gameContentPane);    	
		        	component = SwingUtilities.getDeepestComponentAt(gameContentPane, p.x, p.y);
					if((component != null) && (component instanceof JTextArea)) {
						if(!secondClick) {
							tmpPosition = (new Integer(((JTextArea)component).getText())).intValue();
							secondClick = !secondClick;		
						} else {
							int pos = (new Integer(((JTextArea)component).getText())).intValue();
							if((pos-1)/inputDialog.getW() == (tmpPosition-1)/inputDialog.getW()) {
								status.setText("Try again!!, a snake or, ladder never runs at the same level");
								break;
							}
							
							//successful second click
							ladders[ladderNo].high = pos;  
							ladders[ladderNo].low = tmpPosition;  
							if(ladders[ladderNo].high < ladders[ladderNo].low) {
								pos = ladders[ladderNo].high;  					
								ladders[ladderNo].high = ladders[ladderNo].low; 
								ladders[ladderNo].low = pos; 					
							}
							ladderNo++; //current no. of snakes
							mustUpdatePoints = true;
							updatePathPoints();
							repaint();
							secondClick = !secondClick;
							//if state changes or, not
							if(ladderNo == inputDialog.getLadderNo()) startGame();
						}						
					} 
					break;
											
				case START :
					if(timer.isRunning()) break;
					System.out.println("Taking action for START state");
					p = SwingUtilities.convertPoint(this, p , gameContentPane);    	
		        	component = SwingUtilities.getDeepestComponentAt(gameContentPane, p.x, p.y);
					if(component == startButton) {stopGame(false); break;}
					if(component == rollButton) {
				    	//roll the dice, increase no. of steps
				    	value = rnd.nextInt(5) + 1;
				    	repaint();
				    	players.getPlayer(serialNo).steps++;				    	

						//changing the position of current player
						tmpPosition = players.getPlayer(serialNo).position + value;
						if(tmpPosition > inputDialog.getW() * inputDialog.getH()) {							
							String s = "Oh!! sorry " + inputDialog.getPlayerName(serialNo) + ", can't cross the end."; //no change
							serialNo++;
							if(serialNo > inputDialog.getPlayerNo()) serialNo = 1;
							s = s + " Next move : " + inputDialog.getPlayerName(serialNo);
							status.setText(s);
						} else { 
							changePlayerPosition(serialNo, tmpPosition); 				
							//System.out.println("call changePlayerPosition for first time");
						}
					}
							
					break;			        
		    }
		}
		
		private void startGame() {
			state = START;
			startButton.setText("End game");
			serialNo = 1;
			status.setText("Game Moves : " + inputDialog.getPlayerName(serialNo) + " rolls the dice");
			repaint();
		}
		
		private void stopGame(boolean correctEnd) {
			//setting for another game state = INPUT to repeat all input procedure
			state = INPUT;
			status.setText("To play again, click Start button.");
			startButton.setText("Start");
			repaint();

			//abruptly ends the game if "End game" startButton is clicked
			if(!correctEnd) return;

			//current serialNo player wins the game, setting his score
			int oldScore = players.getPlayer(serialNo).score;
			int newScore = players.getPlayer(serialNo).steps;
			String s = "Congratulations!! " + inputDialog.getPlayerName(serialNo) + " has won the game";			
			if(oldScore == 0) {
				players.getPlayer(serialNo).score = newScore;
				s = s + " for the first time with just " + newScore + " no. of moves";
			} else if(newScore < oldScore ) {
				players.getPlayer(serialNo).score = newScore;
				s = s + " improving well from the older score " + oldScore + " to the new, just " + newScore;
			} else s = s + " with a score of " + newScore + " but his best is still " + oldScore; 
			
			status.setText(inputDialog.getPlayerName(serialNo)  + " has won the game.");
			JOptionPane.showMessageDialog(SetUp.this, s);
			status.setText("To play again, click Start button.");

		}
		
		//changes player position from old to new succesively(old != new)
		private void changePlayerPosition(int serialNo, int position) {
			int oldPosition = players.getPlayer(serialNo).position;
			finalPosition = position;
			movingPosition = oldPosition;
			if(position > oldPosition) step = 1; else step = -1;
			timer.setDelay(333);
			timer.start();
		}
		
		private void callOneThirdSecond() {
			players.getPlayer(serialNo).position = movingPosition;
			repaint();
			movingPosition = movingPosition + step;
			if((step > 0 && movingPosition > finalPosition) 
			|| (step < 0 && movingPosition < finalPosition)) {
				timer.stop();
				firstTimerStopped(finalPosition);
			}
		}
			    
		private void followPath(Path p) {
			followPathPoint = (p.pathType == Path.SNAKE)? new Point(p.p2.x, p.p2.y) : 
															new Point(p.p1.x, p.p1.y);
			isFollowPath = true;
			followPath = p;
			timer.setDelay(50);
			timer.start();
		}
		
		private void callOneTwentiethSecond() {
			followPathPoint = followPath.nextPoint();
			if(followPathPoint != null) repaint();
			else {
				timer.stop();
				isFollowPath = false;
				repaint();
				nextPlayerTurn(players.getPlayer(serialNo).position);
			}
		}
		
		private void firstTimerStopped(int position) {
			if(position == inputDialog.getW() * inputDialog.getH()) {
				//game stops
				stopGame(true);
			} else {
				//search for snakes 
				for(int i = 0; i < snakeNo; i++)
					if(snakes[i].high == position) {
						status.setText("Sorry!! " + inputDialog.getPlayerName(serialNo) + " got cut by a snake down from "
																			+ snakes[i].high + " to " + snakes[i].low);
						players.getPlayer(serialNo).position = snakes[i].low;
						followPath(snakes[i]);
						//changePlayerPosition(serialNo, snakes[i].low);
						return;
					}
				//search for ladders 
				for(int i = 0; i < ladderNo; i++)
					if(ladders[i].low == tmpPosition) {
						status.setText("Yahoo!! lucky " + inputDialog.getPlayerName(serialNo) + " just runs a ladder up from "
																			+ ladders[i].low + " to " + ladders[i].high);
						players.getPlayer(serialNo).position = ladders[i].high;
						followPath(ladders[i]);
						//changePlayerPosition(serialNo, ladders[i].high);
						return;
					}
					nextPlayerTurn(position);
			}														
		}

		private void nextPlayerTurn(int position) {
			if(position == inputDialog.getW() * inputDialog.getH()) {
				//game stops
				stopGame(true);
			} else {	
				serialNo++;
				if(serialNo > inputDialog.getPlayerNo()) serialNo = 1;
				status.setText("Game Moves : " + inputDialog.getPlayerName(serialNo) + " rolls the dice");
			}
		}
		
		// if must, it will update else it updates only if size changed from previous'
		private void updatePathPoints() {
			if(currentSize.equals(this.getSize()) && !mustUpdatePoints) { mustUpdatePoints = false; return; }
			currentSize = this.getSize();
			mustUpdatePoints = false;
			System.out.println("Updating points");
			for(int i = 0; i < snakeNo; i++) {
				snakes[i].p1 = board.getCellOrigin(snakes[i].low);
				snakes[i].p2 = board.getCellOrigin(snakes[i].high);
				snakes[i].readyPath();
			}

			for(int i = 0; i < ladderNo; i++) {
				ladders[i].p1 = board.getCellOrigin(ladders[i].low);
				ladders[i].p2 = board.getCellOrigin(ladders[i].high);
				ladders[i].readyPath();
			}
			
		}
		

		public void paint(Graphics g) {
			cellSize = board.getCellSize(cellSize);
			int a = inputDialog.getW(), b = inputDialog.getH();
			int x = (3 + cellSize.width * a)/2 - 25, y = 100 + cellSize.height * b - 56;
			int big = 49;
			int small = 7;
					
			//draws dice
			g.setColor(Color.white);
			g.fillRect(x, y, big, big);			
			g.setColor(Color.black);
			g.drawRect(x, y, big, big);
				
			switch(value) {
			case 1 : g.fillRect(x + 21, y + 21, small, small); 
					 	 break;
							
				case 2 : g.fillRect(x + 10, y + 10, small, small); 
				         g.fillRect(x + 32, y + 32, small, small);
						 break;

				case 3 : g.fillRect(x + 7, y + 7, small, small); 
				         g.fillRect(x + 21, y + 21, small, small);
				         g.fillRect(x + 35, y + 35, small, small);						 
						 break;
	
				case 4 : g.fillRect(x + 10, y + 10, small, small); 
				         g.fillRect(x + 32, y + 32, small, small);
						 g.fillRect(x + 50 - 10 - 7, y + 10, small, small); 
				         g.fillRect(x + 10, y + 50 - 10 - 7, small, small);
						break;

				case 5 : g.fillRect(x + 7, y + 7, small, small); 
				         g.fillRect(x + 21, y + 21, small, small);
				         g.fillRect(x + 35, y + 35, small, small);
				         g.fillRect(x + 35, y + 7, small, small);
				         g.fillRect(x + 7, y + 35, small, small);						 
						 break;
	
				case 6 : g.fillRect(x + 7, y + 10, small, small); 
				         g.fillRect(x + 21, y + 10, small, small);
						 g.fillRect(x + 35, y + 10, small, small); 
				         g.fillRect(x + 7, y + 32, small, small); 
				         g.fillRect(x + 21, y + 32, small, small);
						 g.fillRect(x + 35, y + 32, small, small); 
				         break;
		         
				default : break;
							 
			}
			
			//snakeNo snakes and ladderNo ladders
			if(state == PAINT_PATH_FIRST || state == PAINT_PATH_SECOND || state == PAINT_PATH_THIRD || state == START) {
				updatePathPoints();
				for(int i = 0; i < snakeNo; i++) snakes[i].drawPath(g);
				for(int i = 0; i < ladderNo; i++) ladders[i].drawPath(g);
			}
			
			//paint player tokens
			if(state == START) {
				Point pi;
				tokenSize = (cellSize.width > cellSize.height)? cellSize.height/3 : cellSize.width/3;
				for(int i = 1; i <= inputDialog.getPlayerNo(); i++) {
					 pi = board.getCellOrigin(players.getPlayer(i).position);
		   		 	 g.setColor(Players.PLAYER_COLOR[i-1]);
					 //code changes if Players.MAX_PLAYER changes
					 if(i == 1) {
					 	pi.x = pi.x - 4 - tokenSize;
					 	pi.y = pi.y - 4 - tokenSize;
					 } else if(i == 2) {
					 	pi.x = pi.x + 4;
					 	pi.y = pi.y - 4 - tokenSize;
					 } else {
					 	pi.x = pi.x - 4 - (tokenSize+1)/2;
					 	pi.y = pi.y + 4;
					 }
					 if(i == serialNo) if(isFollowPath) 
					 	{
						 pi = followPathPoint;
						 System.out.println("pi = (" + pi.x + ", " + pi.y + ")" + " about to draw"); 
						 //g.fillRect(pi.x, pi.y, tokenSize, tokenSize);
						 pi.x -= tokenSize/2; pi.y -= tokenSize/2; 
						 System.out.println("pi = (" + pi.x + ", " + pi.y + ")" + " about to draw");								  
						 //g.fillRect(pi.x, pi.y, tokenSize, tokenSize);
					 	}  
					 
					 g.fillOval(pi.x, pi.y, tokenSize, tokenSize);						 
				}
			}
			
		}		
	
	}

		
}
