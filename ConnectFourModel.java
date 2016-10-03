import java.util.ArrayList;

public class ConnectFourModel implements ConnectFourViewListener {
    public ArrayList<ConnectFourModelListener> listeners = new ArrayList<ConnectFourModelListener>();

    private SpotState[][] board;
    private GameState gameState;

    public ConnectFourModel() {
        board = new SpotState[7][6];
        this.initializeBoardAsEmpty();
        this.gameState = GameState.RED_TURN;
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
        if (!columnFull(col)) {
            for (int i =  0; i < board[col].length; i++) {
                if (board[col][i] == SpotState.EMPTY) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void addListener(ConnectFourModelListener listener) {
        listeners.add(listener);
    }

    public void addChip(int col, int row, SpotState state) {
        for(ConnectFourModelListener listener : listeners) listener.addChip(col, row, state);
    }

    public int click(int col) {
			int pos = getLowestPosition(col);
      if(pos > -1 && (gameState == GameState.RED_TURN || gameState == GameState.BLACK_TURN)) {
				addChip(col, pos, gameState == GameState.RED_TURN ? SpotState.RED : SpotState.BLACK);
				switchTurns();
			}
    }
}
