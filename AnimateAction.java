public class AnimateAction {
	private int col;
	private int row;
	private SpotState state;
	private int action;
	private GameState gameState;

	private final int DISPLAY_FULL_COLUMN_ERROR = 0;
	private final int ANIMATE_CHIP_DROP = 1;
	private final int ANIMATE_AND_DISPLAY_WIN_SCREEN = 2;

	public AnimateAction(int col, int row, int action, SpotState state, GameState gameState) {
		this.col = col;
		this.row = row;
		this.action = action;
		this.state = state;
		this.gameState = gameState;
	}

	public AnimateAction(int action) {
		this.action = action;
	}

	public int getCol() { return col; }
	public int getRow() { return row; }
	public int getAction() { return action; }
	public SpotState getSpotState() { return state; }
	public GameState getGameState() { return gameState; }
}
