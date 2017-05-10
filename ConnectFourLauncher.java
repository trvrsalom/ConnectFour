import java.util.*;
import java.awt.event.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ConnectFourLauncher {

	static ConnectFourController controller;

	public static void main(String[] theseareuselesswhydoweevenneedargstogohere) throws Exception {
		JFrame frame = new JFrame();

		Object stringArray[] = { "One computer", "Two computers" };
		int result = JOptionPane.showOptionDialog(frame, "How would you like to play?", "Select an Option",
		                                          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, stringArray,
		                                          null);

		controller = new ConnectFourController();
		if(result == 0) {
			//One Computer
			controller.singlePlayer();
		}
		else if(result == 1) {
			Object stringArray2[] = { "Host a server", "Connect to a server" };
			int result2 = JOptionPane.showOptionDialog(frame, "How would you like to play?", "Select an Option",
			                                           JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, stringArray2,
			                                           null);
			if(result2 == 0) {
				controller.multiplayerHost();
			}
			else if(result2 == 1) {
				controller.multiplayerClient();
			}
		}
	}

}
