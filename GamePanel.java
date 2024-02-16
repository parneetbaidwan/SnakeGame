import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;



import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
	
	// screen width
	static final int SCREEN_WIDTH = 600;
	
	// screen height
	static final int SCREEN_HEIGHT = 600;
	
	// how big we want the objects in the game
	// increasing this number decreased the number of grids, making the screen bigger
	static final int UNIT_SIZE = 25;
	
	// how many objects can fit in the screen
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
	
	// fairy quick --> higher the number - slower the game
	static final int DELAY = 75;
	
	// holds the x-coordinates of the body+head
	final int x[] = new int[GAME_UNITS];
	
	// holds the y-coordinates of the body+head
	final int y[] = new int[GAME_UNITS];
	
	// initial number of body parts
	int bodyParts = 6;
	
	// number of apples eaten
	int applesEaten;
	
	// x-coordinate of apple position - appears randomly
	int appleX;
	
	// y-coordinate of apple position - appears randomly
	int appleY;
	
	// R-right; L-left; U-up; D-down
	char direction = 'R';
	
	boolean running = false;
	
	Timer timer;
	
	Random random;
	
	
	// constructor
	GamePanel(){
		// new instance of random
		random = new Random();
		
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.white);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		
		startGame();
	}
	
	public void startGame() {
		// creates a new apple on the screen
		newApple();
		
		// game is running
		running = true;
		
		// using delay and action listener interface
		timer = new Timer(DELAY, this);
		
		// start timer
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		// at this point the game would be all black
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {

		if (running) {
			// making a grid
			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				// vertical lines are drawn with x-axis
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);

				// horizontal lines are drawn with y-axis
				g.drawLine(0, i * UNIT_SIZE, SCREEN_HEIGHT, i * UNIT_SIZE);
			}

			// draw the apple
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			// draw the head and body of snake
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) { // head of snake
					g.setColor(Color.red);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}

				else { // body
					g.setColor(Color.black);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}

			g.setColor(Color.black);
			g.setFont(new Font("Ink Free" , Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
					g.getFont().getSize());
		}

		else {
			gameOver(g);
		}
	}
	
	public void newApple() {
		// generate the coordinates of a new apple whenever this method is called
		// allows for even placement
		
		// will appear along the x-axis
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		
		// will appear along the y-axis
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;	
	}
	
	public void move() {
		// moving the snake
		
		//iterate through all the body parts of the snake
		for(int i = bodyParts; i > 0; i--) {
			// shifting all the coordinates in the array by 1
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		// change the direction the snake is moving in
		switch(direction) {
		
		// minus is up
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
			
		// plus is down
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break; 

		// minus is left
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;

		// plus is right
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}

	}
	
	public void checkApple() {
		// if there is an apple at the location of the snakes head
		if((x[0] == appleX && y[0] == appleY)) {
			
			// the number of body parts increases
			bodyParts++;
			
			// the number of apples eaten increases
			applesEaten++;
			
			// call the method to randomly display a new apple
			newApple();
		}
		
	}
	
	public void checkCollisions() {
		// head collide with body
		for(int i = bodyParts; i>0; i--) {
			// head collided with body
			if((x[0] == x[i]) && (y[0] == y[i])) { // x[0] is the head
				running = false; // game over
			}
		}
		
		// check if head touches left border
		if (x[0] < 0) {
			running = false;
		}
		
		// check if head touches right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		
		// check if head touches top border
		if (y[0] < 0) {
			running = false;
		}
		
		// check if head touches bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		// if running is false, stop the timer
		if (!running) {
			timer.stop();
		}
	}
	
	
	public void gameOver(Graphics g) {
		// game over text
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	 
		// score text
		//Score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 50));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/2,g.getFont().getSize());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// check if game is running, move the snake
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
		
	}
	
	// inner class
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			// four cases, one for each arrow key
			
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break ;
				
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
				
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
				
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}

}
