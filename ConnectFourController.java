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
		view.addMouseListener(new MouseAdapter() {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 //set up listener
			@Override
			public void mousePressed(MouseEvent e) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         //When mouse is clicked...
			  //System.out.println("("+e.getX() + "," + e.getY()+")"); //print out coordinates
			  int col = ((e.getX()-50)/100);
			  col = col > 6 ? 6 : col;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       // sets the column to six if it is greater than six
			  col = col < 0 ? 0 : col;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       // sets the columns to 0 if it is less than 0

			  //System.out.println(col);
			  AnimateAction action = model.click(col);
			  //System.out.println(action.getCol());
			  //System.out.println(action.getRow());
			  if(action != null) {
			    //System.out.println("actionNum:" + action.getAction());
			    if (action.getAction() == 0) {
			      view.displayColumnFullMessage();
					} else if (action.getAction() == 1) {
			      view.addChip(action);
					} else if (action.getAction() == 2) {
			      view.addChip(action);
			      view.displayWinMessage(action.getGameState());
			      // remove mouse click listeners
					}

			    //model.printBoard();
				}
			  view.repaint();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       //repaints

			  // handle the mouseClick in the logic.
			  // if the mouse click is outside a chip, add a
			}
		});
	}

	public SpotState ourColor;
	public ServerSocket srvr;
	public Socket skt;
	public PrintWriter out;
	public BufferedReader in;

	public void multiplayerHost() throws Exception {
		System.out.println("------------HOST------------");
		ourColor = SpotState.RED;

		String multiLineMsg[] = { "Tell your opponent to connect to: ", InetAddress.getLocalHost().getHostAddress() };
		JOptionPane pane = new JOptionPane();
		pane.setMessage(multiLineMsg);
		JDialog d = pane.createDialog(null, "Connect Four Host");
		d.setVisible(true);

		try {
			srvr = new ServerSocket(5889);
			skt = srvr.accept();
			in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
			out = new PrintWriter(skt.getOutputStream(), true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		view = new ConnectFourView("Connect Four Host");
		model = new ConnectFourModel();

		view.addMouseListener(new MouseAdapter() {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           //set up listener
			@Override
			public void mousePressed(MouseEvent e) {
			  int col = ((e.getX()-50)/100);
			  col = col > 6 ? 6 : col;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 // sets the column to six if it is greater than six
			  col = col < 0 ? 0 : col;
			  if(model.gameState == GameState.RED_TURN) {
			    //sendHostMessage(col);
			    AnimateAction action = model.click(col);
			    if (action.getAction() == 0) {
			      view.displayColumnFullMessage();
					}
			    else if (action.getAction() == 1) {
			      view.addChip(action);
			      try {

			        out.print(col + "\n");
			        out.flush();
			        System.out.println("Sending Data");
						}
			      catch(Exception e2) {
			        e2.printStackTrace();
						}
			      hostListen();
					}
			    else if (action.getAction() == 2) {
			      view.addChip(action);
			      try {
			        out.print(col + "\n");
			        out.flush();
			        System.out.println("Sending Data");
						}
			      catch(Exception e2) {
			        e2.printStackTrace();
						}
			      view.displayWinMessage(action.getGameState());
			      // remove mouse click listeners
					}
				}
			  view.repaint();
			}
		});
		/*try {
		    String inputLine;
		    System.out.println("Listening");
		    while (!in.ready()) {}
		    System.out.println(in.readLine());

		    System.out.println("Done");

		   }
		   catch(Exception e) {
		    System.out.println("Exception caught when trying to listen on port or listening for a connection");
		    System.out.println(e.getMessage());
		   }*/
	}

	public void hostListen() {
		try {
			while(!in.ready()) {}
			int col = Integer.parseInt(in.readLine());
			col = col > 6 ? 6 : col;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     // sets the column to six if it is greater than six
			col = col < 0 ? 0 : col;
			if(model.gameState == GameState.BLACK_TURN) {
				AnimateAction action = model.click(col);
				if (action.getAction() == 1) {
					view.addChip(action);
				}
				else if (action.getAction() == 2) {
					view.addChip(action);
					view.displayWinMessage(action.getGameState());
					// remove mouse click listeners
				}
			}
			view.repaint();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void clientListen()  {
		try {
			System.out.println("LISTENING");
			while(!in.ready()) {}
			int col = Integer.parseInt(in.readLine());
			System.out.println(col);
			col = col > 6 ? 6 : col;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     // sets the column to six if it is greater than six
			col = col < 0 ? 0 : col;
			System.out.println(model);
			if(model.gameState == GameState.RED_TURN) {
				AnimateAction action = model.click(col);
				if (action.getAction() == 1) {
					view.addChip(action);
				}
				else if (action.getAction() == 2) {
					view.addChip(action);
					view.displayWinMessage(action.getGameState());
					// remove mouse click listeners
				}
			}
			view.repaint();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void multiplayerClient() throws Exception {
		System.out.println("------------CLIENT------------");
		ourColor = SpotState.BLACK;

		JFrame frame = new JFrame();
		Object addr = JOptionPane.showInputDialog(frame, "Enter your opponent's IP address");
		frame.setVisible(true);

		try {
			skt = new Socket((String)addr, 5889);
			in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
			out = new PrintWriter(skt.getOutputStream(), true);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		view = new ConnectFourView("Connect Four Client");
		model = new ConnectFourModel();

		clientListen();

		view.addMouseListener(new MouseAdapter() {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           //set up listener
			@Override
			public void mousePressed(MouseEvent e) {
			  int col = ((e.getX()-50)/100);
			  col = col > 6 ? 6 : col;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 // sets the column to six if it is greater than six
			  col = col < 0 ? 0 : col;
			  if(model.gameState == GameState.BLACK_TURN) {
			    //sendHostMessage(col);

			    AnimateAction action = model.click(col);
			    if (action.getAction() == 0) {
			      view.displayColumnFullMessage();
					}
			    else if (action.getAction() == 1) {
			      view.addChip(action);
			      try {
			        out.print(col + "\n");
			        out.flush();
			        System.out.println("Sending Data");
						}
			      catch(Exception e2) {
			        e2.printStackTrace();
						}
			      clientListen();
					}
			    else if (action.getAction() == 2) {
			      view.addChip(action);
			      try {
			        out.print(col + "\n");
			        out.flush();
			        System.out.println("Sending Data");
						}
			      catch(Exception e2) {
			        e2.printStackTrace();
						}
			      view.displayWinMessage(action.getGameState());
			      // remove mouse click listeners
					}
				}
			  view.repaint();
			}
		});

	}

	/*public ServerSocket srvr;
	   public Socket skt;
	   public PrintWriter out;
	   public BufferedReader in;

	   public void setupHostServer() {
	    try {
	        srvr = new ServerSocket(5889);
	        skt = srvr.accept();
	        in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
	    }
	    catch(Exception e) {
	        e.printStackTrace();
	        System.out.print("Something went wrong! setup\n");
	    }
	   }

	   public void sendHostMessage(int col) {
	    try {
	        out = new PrintWriter(skt.getOutputStream(), true);
	        out.print("" + col);
	        out.close();
	    }
	    catch(Exception e) {
	        e.printStackTrace();
	        System.out.print("Something went wrong! send\n");
	    }
	   }

	   public void setupClient(String address) {
	    try {
	        skt = new Socket((String)address, 5889);
	        in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
	    }
	    catch(Exception e) {
	        e.printStackTrace();
	        System.out.print("Something went wrong! setup\n");
	    }
	   }

	   public void sendClientMessage(int col) {
	    try {
	        out = new PrintWriter(skt.getOutputStream(), true);
	        out.print("" + col);
	        out.close();
	    }
	    catch(Exception e) {
	        e.printStackTrace();
	        System.out.print("Something went wrong! send\n");
	    }
	   }

	   SpotState ourColor;
	   public void multiplayerHost() throws Exception {
	    System.out.println("HOST");
	    try {
	        ourColor = SpotState.RED;
	        view = new ConnectFourView("Connect Four");
	        model = new ConnectFourModel();
	        setupHostServer();
	        String multiLineMsg[] = { "Tell your opponent to connect to: ", InetAddress.getLocalHost().getHostAddress() };
	        JOptionPane pane = new JOptionPane();
	        pane.setMessage(multiLineMsg);
	        JDialog d = pane.createDialog(null, "Connect Four Host");
	        d.setVisible(true);
	        //System.out.print("Server has connected!\n");
	        int o = 1;
	        String fromServer;
	        view.addMouseListener(new MouseAdapter() {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   //set up listener
	            @Override
	            public void mousePressed(MouseEvent e) {
	              int col = ((e.getX()-50)/100);
	              col = col > 6 ? 6 : col;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           // sets the column to six if it is greater than six
	              col = col < 0 ? 0 : col;
	              if(model.gameState == GameState.RED_TURN) {
	                //System.out.println("calling " + col);
	                sendHostMessage(col);
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
	                //model.printBoard();
	                }
	              view.repaint();
	            }
	        });
	        while(true) {
	            System.out.println(in.readLine());
	            fromServer = in.readLine();
	            while (fromServer != null && model.gameState == GameState.BLACK_TURN) {
	                System.out.println("Server: " + fromServer);
	                if (fromServer.equals("ECONN"))
	                    break;
	                else {
	                    if(model.gameState == GameState.BLACK_TURN) {
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
	                        //model.printBoard();
	                    }
	                    view.repaint();
	                }
	            }
	            //fromServer = in.readLine();
	        }

	    }
	    catch(Exception e) {
	        e.printStackTrace();
	        System.out.print("Something went wrong!\n");
	    }

	   }

	   public void multiplayerClient() {
	    System.out.println("CLIENT");
	    try {
	        JFrame frame = new JFrame();
	        Object addr = JOptionPane.showInputDialog(frame, "Enter your opponent's IP address");
	        frame.setVisible(true);
	        ourColor = SpotState.BLACK;
	        view = new ConnectFourView("Connect Four Client");
	        model = new ConnectFourModel();
	        setupClient((String)addr);
	        String fromServer;
	        view.addMouseListener(new MouseAdapter() {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   //set up listener
	            @Override
	            public void mousePressed(MouseEvent e) {
	              //When mouse is clicked...
	              //System.out.println("("+e.getX() + "," + e.getY()+")"); //print out coordinates
	              int col = ((e.getX()-50)/100);
	              col = col > 6 ? 6 : col;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           // sets the column to six if it is greater than six
	              col = col < 0 ? 0 : col;
	              sendClientMessage(col);
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
	                //model.printBoard();
	                }
	              view.repaint();
	            }
	        });
	        System.out.println("here");
	        while(true) {
	            fromServer = in.readLine();
	            while (fromServer != null && model.gameState == GameState.RED_TURN) {
	                System.out.println("Server: " + fromServer);
	                if (fromServer.equals("ECONN"))
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
	                        //model.printBoard();
	                    }
	                    view.repaint();
	                }
	            }
	            //fromServer = in.readLine();
	        }

	    }
	    catch(Exception e) {
	        System.out.print("Whoops! It didn't work!\n");
	    }
	   }*/

}
