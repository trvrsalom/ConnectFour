import java.util.*;
import java.awt.event.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.io.*;
import java.net.*;

public class ConnectFourController {

	public static ConnectFourView view;
	public static ConnectFourModel model;

	public void singlePlayer() {
		view = new ConnectFourView("Connect Four");
		model = new ConnectFourModel();

		//model.addListener(view);

		// view.addListener
		// add listeners to the view when a chip is clicked.
		view.addMouseListener(new MouseAdapter() {                                                                                                                                                                                                                                                 //set up listener
			@Override
			public void mousePressed(MouseEvent e) {                                                                                                                                                                                                                                                                                                                                                                         //When mouse is clicked...
			  //System.out.println("("+e.getX() + "," + e.getY()+")"); //print out coordinates
			  int col = ((e.getX()-50)/100);
			  col = col > 6 ? 6 : col;                                                                                                                                                                                                                                                                                                                                                                       // sets the column to six if it is greater than six
			  col = col < 0 ? 0 : col;                                                                                                                                                                                                                                                                                                                                                                       // sets the columns to 0 if it is less than 0

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
			  view.repaint();                                                                                                                                                                                                                                                                                                                                                                       //repaints

			  // handle the mouseClick in the logic.
			  // if the mouse click is outside a chip, add a
			}
		});
	}

	SpotState ourColor;
	public void multiplayerHost() throws Exception {
		try {
			ourColor = SpotState.RED;
			view = new ConnectFourView("Connect Four");
			model = new ConnectFourModel();
			ServerSocket srvr = new ServerSocket(5889);
			String multiLineMsg[] = { "Tell your opponent to connect to: ", InetAddress.getLocalHost().getHostAddress() };
			JOptionPane pane = new JOptionPane();
			pane.setMessage(multiLineMsg);
			JDialog d = pane.createDialog(null, "Connect Four Host");
			d.setVisible(true);
			Socket skt = srvr.accept();
			System.out.print("Server has connected!\n");
			PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
			//out.print("data");
			int o = 1;
			out.print("YOOOOOO FUCKER");
			view.addMouseListener(new MouseAdapter() {                                                                                                                                                                                                                                                                                                                             //set up listener
				@Override
				public void mousePressed(MouseEvent e) {
				  int col = ((e.getX()-50)/100);
				  col = col > 6 ? 6 : col;                                                                                                                                                                                                                                                                                                                                                                                                                                                   // sets the column to six if it is greater than six
				  col = col < 0 ? 0 : col;
				  if(model.gameState == GameState.RED_TURN) {
				    out.print(col);
				    AnimateAction action = model.click(col);
				    if (action.getAction() == 0) {
				      view.displayColumnFullMessage();
						}
				    else if (action.getAction() == 1) {
				      view.addChip(action);
						}
				    else if (action.getAction() == 2) {
				      view.addChip(action);
				      view.displayWinMessage(action.getGameState());
				      // remove mouse click listeners
						}
				    model.printBoard();
					}
				  view.repaint();
				}
			});
			out.close();
			skt.close();
			srvr.close();


		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.print("Something went wrong!\n");
		}

	}

	public void multiplayerClient() {
		try {
			JFrame frame = new JFrame();
			Object addr = JOptionPane.showInputDialog(frame, "Enter your opponent's IP address");
			frame.setVisible(true);
			ourColor = SpotState.BLACK;
			view = new ConnectFourView("Connect Four Client");
			model = new ConnectFourModel();
			Socket skt = new Socket((String)addr, 5889);
			BufferedReader in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
			String fromServer;
			view.addMouseListener(new MouseAdapter() {                                                                                                                                                                                                                                                                                                                             //set up listener
				@Override
				public void mousePressed(MouseEvent e) {
				  //When mouse is clicked...
				  //System.out.println("("+e.getX() + "," + e.getY()+")"); //print out coordinates
				  int col = ((e.getX()-50)/100);
				  col = col > 6 ? 6 : col;                                                                                                                                                                                                                                                                                                                                                                                                                                                   // sets the column to six if it is greater than six
				  col = col < 0 ? 0 : col;
				  if(model.gameState == GameState.BLACK_TURN) {
				    AnimateAction action = model.click(col);
				    if (action.getAction() == 0) {
				      view.displayColumnFullMessage();
						}
				    else if (action.getAction() == 1) {
				      view.addChip(action);
						}
				    else if (action.getAction() == 2) {
				      view.addChip(action);
				      view.displayWinMessage(action.getGameState());
				      // remove mouse click listeners
						}
				    model.printBoard();
					}
				  view.repaint();
				}
			});
			System.out.println("here");
			while(true) {
				fromServer = in.readLine();
				System.out.println(fromServer);
				while (fromServer != null) {
					System.out.println("Server: " + fromServer);
					if (fromServer.equals("Bye."))
						break;
					else {
						if(model.gameState == GameState.RED_TURN) {
							AnimateAction action = model.click(Integer.parseInt(fromServer));
							if (action.getAction() == 0) {
								view.displayColumnFullMessage();
							}
							else if (action.getAction() == 1) {
								view.addChip(action);
							}
							else if (action.getAction() == 2) {
								view.addChip(action);
								view.displayWinMessage(action.getGameState());
								// remove mouse click listeners
							}
							model.printBoard();
						}
						view.repaint();
					}
					/*String fromUser = stdIn.readLine();
					   if (fromUser != null) {
					    System.out.println("Client: " + fromUser);
					    out.println(fromUser);
					   }*/
				}
				//fromServer = in.readLine();
			}

		}
		catch(Exception e) {
			System.out.print("Whoops! It didn't work!\n");
		}
	}

}
