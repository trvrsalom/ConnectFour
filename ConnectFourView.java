import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

// this class has to have the listeners to change the view as well as the

public class ConnectFourView extends JFrame /*implements ConnectFourModelListener*/ {
	private ArrayList<ConnectFourViewListener> listeners = new ArrayList<ConnectFourViewListener>();
	private int numCols = 7;
	private int numRows = 6;
	private Chip[][] board = new Chip[numCols][numRows];

	private final int ANIMATION_DELAY = 100;

	public ConnectFourView(String title) {
		// JFrame frame = new JFrame();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(title);     //sets title

		this.setSize(numCols*100+100,numRows*100+125);
		this.getContentPane().setBackground(Color.YELLOW);

		for(int i = 0; i < numCols; i++) {
			for(int j = 0; j < numRows; j++) {
				board[i][j] = new Chip(i*100+100,j*100+100,Color.WHITE);         // create Chip
				this.add(board[i][j]);
				this.setVisible(true);                 //Ensures component is visible
			}
		}
	}

	public void addListener(ConnectFourViewListener listener) {
		listeners.add(listener);
	}

	public void click(int col) {
		for(ConnectFourViewListener listener : listeners) listener.click(col);
	}

	public void addChip(AnimateAction action) {
		//System.out.println("action number: " + action.getAction());
		int col = action.getCol();
		int row = action.getRow();
		SpotState state = action.getSpotState();
		//System.out.println(state);
		Color color = (state == SpotState.RED ? Color.RED : Color.BLACK);

		for (int i = 0; i < row; i++) {     //this does animating
			board[col][i].setColor(color);       // recolor chip
			try{Thread.sleep(ANIMATION_DELAY);}
			catch(InterruptedException e) {}
			board[col][i].setColor(Color.WHITE);       // back to white
		}

		board[col][row].setColor(color);     // recolor Circle
	}

	public void displayWinMessage(GameState state) {
		if (state == GameState.RED_WIN) {
			JOptionPane.showMessageDialog(this, "Red Wins!!!");
		} else if (state == GameState.BLACK_WIN) {
			JOptionPane.showMessageDialog(this, "Black Wins!!!");
		}
	}

	public void displayColumnFullMessage() {
		JOptionPane.showMessageDialog(this, "Column full. Please choose a different column.");
	}

}

class Chip extends JComponent {
	private int x = 0;    //circle ypoint
	private int y = 0;    //circle xpoint
	private int d = 90;   //circle diameter
	private Color c = Color.BLACK;

	public Chip(int x, int y, Color c){   //assigns values
		this.x = x;
		this.y = y;
		this.c = c;
	}

	public void setColor(Color c){
		this.c = c;
		this.paintImmediately(0, 0, 1000, 1000);
	}

	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;          //Recover Graphics2D
		g2.setColor(c);                          //Sets color
		Ellipse2D.Double circle = new Ellipse2D.Double(x-d/2, y-d/2, d, d);
		g2.fill(circle);
	}

}
