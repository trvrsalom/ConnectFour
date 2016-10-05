import java.util.*;
import java.awt.event.*;

public class ConnectFourController {
	
	public static ConnectFourView view;
	public static ConnectFourModel model;

    public static void main(String[] args) {
        view = new ConnectFourView("Connect Four");
      	model = new ConnectFourModel();

		//model.addListener(view);

        // view.addListener
        // add listeners to the view when a chip is clicked.
        view.addMouseListener(new MouseAdapter() { //set up listener
			@Override
			public void mousePressed(MouseEvent e) { //When mouse is clicked...
              //System.out.println("("+e.getX() + "," + e.getY()+")"); //print out coordinates
			int col = ((e.getX()-50)/100);
			col = col > 6 ? 6 : col; // sets the column to six if it is greater than six
			col = col < 0 ? 0 : col; // sets the columns to 0 if it is less than 0

			System.out.println(col);
			AnimateAction action = model.click(col);
			System.out.println(action.getCol());
			System.out.println(action.getRow());
			if(action != null) {
				System.out.println("actionNum:" + action.getAction());
				if (action.getAction() == 0) {
					view.displayColumnFullMessage();
				} else if (action.getAction() == 1) {
					view.addChip(action);
				} else if (action.getAction() == 2) {
					view.addChip(action);
					view.displayWinMessage(action.getGameState());
					// remove mouse click listeners
				} 

				model.printBoard();
			}
            view.repaint(); //repaints
            
			// handle the mouseClick in the logic.
            // if the mouse click is outside a chip, add a
          }
        });
    }
}
