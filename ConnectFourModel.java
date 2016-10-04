import java.util.ArrayList;

public class ConnectFourModel /*implements ConnectFourViewListener*/ {
	public ArrayList<ConnectFourModelListener> listeners = new ArrayList<ConnectFourModelListener>();

	private SpotState[][] board;
	private GameState gameState; 
	private final int BOARD_COLUMNS = 7;
	private final int BOARD_ROWS = 6;

	public ConnectFourModel() {
		board = new SpotState[BOARD_COLUMNS][BOARD_ROWS];
		this.initializeBoardAsEmpty();
		this.gameState = GameState.RED_TURN;
		printBoard();
	}

	public void printBoard() {
		for (int i = 0; i < BOARD_COLUMNS; i++){
			for (int j = 0; j < BOARD_ROWS; j++) {
				System.out.print(board[i][j] + " " + i + " " + j + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public void initializeBoardAsEmpty() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = SpotState.EMPTY;
			}
		}
	}

	public void switchTurns() {
		this.gameState = (this.gameState == GameState.RED_TURN ? GameState.BLACK_TURN : GameState.RED_TURN);
	}

	public boolean columnFull(int col) {
		return board[col][0] != SpotState.EMPTY;
	}

	public int getLowestPosition(int col) {
		//if (!columnFull(col)) {
			for (int i = board[col].length - 1; i >= 0; i--) {
				if (board[col][i] == SpotState.EMPTY) {
					return i;
				}
			}
		//}
		return -1;
	}

	public void setChip (int col, int row, SpotState state) {
		board[col][row] = state;
	}

	public void addListener(ConnectFourModelListener listener) {
		listeners.add(listener);
	}

	public void addChip(int col, int row, SpotState state) {
		for(ConnectFourModelListener listener : listeners) listener.addChip(col, row, state);
	}

	public int[][] getNeighbors(int col, int row) {
		int[][] toRet =  {{col-1,row-1}, {col-1,row}, {col-1,row+1}, {col,row-1}, {col,row+1}, {col+1,row-1}, {col+1,row}, {col+1,row+1} };
		return toRet;
	}

	// 0 1 2
	// 3 X 4
	// 5 6 7

	public int countChain(int col, int row, int dir) {
		SpotState curr = board[col][row];
		int[][] neighbors = getNeighbors(col, row);
		//System.out.println(neighbors[dir][0]);

		//Check if valid neighbor
		if(neighbors[dir][0] < 0 || neighbors[dir][1] < 0) return 0;
		SpotState neighbor = board[neighbors[dir][0]][neighbors[dir][1]];
		//Check if same color
		if(neighbor != curr) return 0;
		//Continue recursing
		return countChain(neighbors[dir][0], neighbors[dir][1], dir) + 1;
	}

	public boolean checkWin(int col, int row) {
		// check if anyone has won the game yet.
		for(int i = 0; i <= 3; i++) {
			int chain = 0;
			chain += countChain(col, row, i);
			chain += countChain(col, row, 7 - i);
			if(chain >= 4) {
				return true;
			}
		}
		return false;
	}

	public AnimateAction click(int col) {
		int pos = getLowestPosition(col);
		System.out.println("lowest Position: " + pos);
		AnimateAction action;
		// if it is a valid move and no one has won yet
		if(pos > -1 && (gameState == GameState.RED_TURN || gameState == GameState.BLACK_TURN)) {
			// update model
			this.setChip(col, pos, gameState == GameState.RED_TURN ? SpotState.RED : SpotState.BLACK);
			// check win: UNCOMMENT LATER
			//if (checkWin(col, pos)) {
				//Red won
				//if(gameState == GameState.RED_TURN) gameState = GameState.RED_WIN;
				//Black won
				//else if(gameState == GameState.BLACK_TURN) gameState = GameState.BLACK_WIN;
			//}
			
			action = new AnimateAction(col, pos, gameState == GameState.RED_TURN ? SpotState.RED : SpotState.BLACK, gameState);
			switchTurns();
			System.out.println("gamestate: " + this.gameState);
		}
		else {
			action = null;//new AnimateAction(col, pos, gameState == GameState.RED_TURN ? SpotState.RED : SpotState.BLACK, gameState);
		}
		return action;
	}
}
