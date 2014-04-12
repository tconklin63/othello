public class GameState {
  int[][] board;
  Move lastMove;
  int turn;
  String message;

  public GameState(int[][] b, int t, String m, Move lm) {
    this.board = new int[8][8];
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        this.board[i][j] = b[i][j];
      }
    }
    this.turn = t;
    this.message = m;
    this.lastMove = lm;
  }

  public void setBoard(int[][] b) {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        this.board[i][j] = b[i][j];
      }
    }
  }

  public void setTurn(int t) {
    this.turn = t;
  }

  public void setMessage(String m) {
    this.message = m;
  }

  public void setLastMove(Move lm) {
    this.lastMove = lm;
  }

  public int[][] getBoard() {
    return this.board;
  }

  public int getTurn() {
    return this.turn;
  }

  public String getMessage() {
    return this.message;
  }

  public Move getLastMove() {
    return this.lastMove;
  }
}
