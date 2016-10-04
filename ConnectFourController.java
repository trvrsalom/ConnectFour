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
							col = col > 6 ? 6 : col;
							col = col < 0 ? 0 : col;
							AnimateAction action = model.click(col);
							if(action != null) {
								view.addChip(action);
							}
              view.repaint(); //repaints
              // handle the mouseClick in the logic.
              // if the mouse click is outside a chip, add a
          }
        });
    }
}
