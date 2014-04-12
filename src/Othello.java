import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintStream;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

public class Othello extends DBApplet implements MouseListener {
  private static final long serialVersionUID = 1L;
  int[][] mainBoard = new int[8][8];
  double[][] positionScore = new double[8][8];
  int currentPlayer;
  int mouseX;
  int mouseY;
  int boardX;
  int boardY;
  int whiteScore;
  int blackScore;
  Move lastMove = null;
  String message;
  String alert;
  Random r;
  Stack<GameState> undoStack;
  Stack<GameState> redoStack;
  boolean thinking = false;
  final int NEITHER = 0;
  final int WHITE = 1;
  final int BLACK = -1;
  final int MAX_SEARCH_DEPTH = 5;
  final int DOUBLE_MOVE_BONUS = 100;
  final int WINNING_MOVE_BONUS = 1000;
  final double THRESHOLD = 0.2D;
  final double CORNER = 10.0D;
  final double BEST_SIDE = 0.5D;
  final double MIDDLE_SIDE = 0.25D;
  final double INNER_CORNER = 0.2D;
  final double INNER_SIDE = 0.15D;
  final double ADJACENT_SIDE = 0.1D;
  final double PRE_MIDDLE_SIDE = 0.05D;
  final double PRE_SIDE = 0.03D;
  final double PRE_CORNER = 0.01D;
  final double START = 0.0D;

  public void init() {
    addMouseListener(this);
    this.r = new Random();
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        this.mainBoard[i][j] = 0;
      }
    }
    this.currentPlayer = 0;
    this.mouseX = 0;
    this.mouseY = 0;
    this.boardX = 0;
    this.boardY = 0;
    this.whiteScore = 0;
    this.blackScore = 0;
    this.message = "Welcome to Othello! Press new game to start.";
    this.alert = "";
    this.positionScore[0][0] = 10.0D;
    this.positionScore[1][0] = 0.1D;
    this.positionScore[2][0] = 0.5D;
    this.positionScore[3][0] = 0.25D;
    this.positionScore[4][0] = 0.25D;
    this.positionScore[5][0] = 0.5D;
    this.positionScore[6][0] = 0.1D;
    this.positionScore[7][0] = 10.0D;
    this.positionScore[0][1] = 0.1D;
    this.positionScore[1][1] = 0.01D;
    this.positionScore[2][1] = 0.03D;
    this.positionScore[3][1] = 0.05D;
    this.positionScore[4][1] = 0.05D;
    this.positionScore[5][1] = 0.03D;
    this.positionScore[6][1] = 0.01D;
    this.positionScore[7][1] = 0.1D;
    this.positionScore[0][2] = 0.5D;
    this.positionScore[1][2] = 0.03D;
    this.positionScore[2][2] = 0.2D;
    this.positionScore[3][2] = 0.15D;
    this.positionScore[4][2] = 0.15D;
    this.positionScore[5][2] = 0.2D;
    this.positionScore[6][2] = 0.03D;
    this.positionScore[7][2] = 0.5D;
    this.positionScore[0][3] = 0.25D;
    this.positionScore[1][3] = 0.05D;
    this.positionScore[2][3] = 0.15D;
    this.positionScore[3][3] = 0.0D;
    this.positionScore[4][3] = 0.0D;
    this.positionScore[5][3] = 0.15D;
    this.positionScore[6][3] = 0.05D;
    this.positionScore[7][3] = 0.25D;
    this.positionScore[0][4] = 0.25D;
    this.positionScore[1][4] = 0.05D;
    this.positionScore[2][4] = 0.15D;
    this.positionScore[3][4] = 0.0D;
    this.positionScore[4][4] = 0.0D;
    this.positionScore[5][4] = 0.15D;
    this.positionScore[6][4] = 0.05D;
    this.positionScore[7][4] = 0.25D;
    this.positionScore[0][5] = 0.5D;
    this.positionScore[1][5] = 0.03D;
    this.positionScore[2][5] = 0.2D;
    this.positionScore[3][5] = 0.15D;
    this.positionScore[4][5] = 0.15D;
    this.positionScore[5][5] = 0.2D;
    this.positionScore[6][5] = 0.03D;
    this.positionScore[7][5] = 0.5D;
    this.positionScore[0][6] = 0.1D;
    this.positionScore[1][6] = 0.01D;
    this.positionScore[2][6] = 0.03D;
    this.positionScore[3][6] = 0.05D;
    this.positionScore[4][6] = 0.05D;
    this.positionScore[5][6] = 0.03D;
    this.positionScore[6][6] = 0.01D;
    this.positionScore[7][6] = 0.1D;
    this.positionScore[0][7] = 10.0D;
    this.positionScore[1][7] = 0.1D;
    this.positionScore[2][7] = 0.5D;
    this.positionScore[3][7] = 0.15D;
    this.positionScore[4][7] = 0.15D;
    this.positionScore[5][7] = 0.5D;
    this.positionScore[6][7] = 0.1D;
    this.positionScore[7][7] = 10.0D;
  }

  public void paint(Graphics g) {
    Color textColor = new Color(0, 0, 200);
    Color alertColor = new Color(255, 0, 0);
    Color boardColor = new Color(0, 150, 0);
    Color bgColor = new Color(200, 255, 200);
    Color startButtonColor = new Color(200, 150, 150);
    Color strategyButtonColor = new Color(180, 180, 180);
    Color undoColor = new Color(255, 50, 255);
    Color redoColor = new Color(50, 255, 50);
    setBackground(bgColor);
    g.setColor(bgColor);
    g.fillRect(0, 450, 500, 50);
    g.fillRect(0, 30, 500, 20);

    g.setColor(startButtonColor);
    g.fillRect(5, 5, 70, 25);
    g.setColor(strategyButtonColor);
    g.fillRect(80, 5, 90, 25);
    g.fillRect(175, 5, 60, 25);
    g.fillRect(240, 5, 85, 25);
    g.fillRect(330, 5, 55, 25);
    g.setColor(undoColor);
    g.fillRect(5, 470, 40, 25);
    g.setColor(redoColor);
    g.fillRect(455, 470, 40, 25);

    g.setColor(textColor);
    g.drawString(this.message, 250 - this.message.length() * 3, 490);
    g.drawString("New Game", 10, 20);
    g.drawString("Random Move", 85, 20);
    g.drawString("Max Flips", 180, 20);
    g.drawString("Best Position", 245, 20);
    g.drawString("Minimax", 335, 20);
    g.drawString("undo", 10, 485);
    g.drawString("redo", 463, 485);
    String score = "White = " + this.whiteScore + " Black = " + this.blackScore;
    g.drawString(score, 250 - score.length() * 3, 470);
    g.setColor(alertColor);
    g.drawString(this.alert, 250 - this.alert.length() * 3, 45);

    g.setColor(boardColor);
    g.fillRect(50, 50, 400, 400);
    g.setColor(Color.black);
    for (int i = 50; i <= 450; i += 50) {
      g.drawLine(i, 50, i, 450);
    }
    for (int i = 50; i <= 450; i += 50) {
      g.drawLine(50, i, 450, i);
    }
    g.fillOval(146, 146, 8, 8);
    g.fillOval(346, 346, 8, 8);
    g.fillOval(146, 346, 8, 8);
    g.fillOval(346, 146, 8, 8);
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (this.mainBoard[i][j] == 1) {
          g.setColor(Color.white);
          g.fillOval(i * 50 + 52, j * 50 + 52, 46, 46);
          if ((this.lastMove != null) && (this.lastMove.getX() == i)
              && (this.lastMove.getY() == j)) {
            g.setColor(Color.red);
            g.drawOval(i * 50 + 52, j * 50 + 52, 46, 46);
            g.drawOval(i * 50 + 51, j * 50 + 51, 48, 48);
          }
        }
        if (this.mainBoard[i][j] == -1) {
          g.setColor(Color.black);
          g.fillOval(i * 50 + 52, j * 50 + 52, 46, 46);
          if ((this.lastMove != null) && (this.lastMove.getX() == i)
              && (this.lastMove.getY() == j)) {
            g.setColor(Color.white);
            g.drawOval(i * 50 + 52, j * 50 + 52, 46, 46);
          }
        }
      }
    }
  }

  public void mouseReleased(MouseEvent me) {
    this.mouseX = me.getX();
    this.mouseY = me.getY();
    handleMouseRelease();
  }

  public void mouseClicked(MouseEvent me) {
  }

  public void mousePressed(MouseEvent me) {
  }

  public void mouseEntered(MouseEvent me) {
  }

  public void mouseExited(MouseEvent me) {
  }

  private void handleMouseRelease() {
    if (this.thinking) {
      return;
    }
    if ((this.mouseX > 5) && (this.mouseX < 75) && (this.mouseY > 5)
        && (this.mouseY < 30)) {
      newGame();
    }
    if ((this.mouseX > 80) && (this.mouseX < 170) && (this.mouseY > 5)
        && (this.mouseY < 30)) {
      randomMove();
    }
    if ((this.mouseX > 175) && (this.mouseX < 235) && (this.mouseY > 5)
        && (this.mouseY < 30)) {
      maxFlips();
    }
    if ((this.mouseX > 240) && (this.mouseX < 325) && (this.mouseY > 5)
        && (this.mouseY < 30)) {
      bestPosition();
    }
    if ((this.mouseX > 330) && (this.mouseX < 385) && (this.mouseY > 5)
        && (this.mouseY < 30)) {
      this.alert = "Thinking...please wait.";
      repaint();
      Thread thread = new Thread() {
        public void run() {
          Othello.this.thinking = true;
          Othello.this.minimax();
          Othello.this.thinking = false;
          Othello.this.repaint();
        }
      };
      thread.start();
    }
    if ((this.mouseX > 5) && (this.mouseX < 45) && (this.mouseY > 470)
        && (this.mouseY < 495)) {
      undoMove();
    }
    if ((this.mouseX > 455) && (this.mouseX < 495) && (this.mouseY > 470)
        && (this.mouseY < 495)) {
      redoMove();
    }
    if ((this.mouseX > 50) && (this.mouseX < 450) && (this.mouseY > 50)
        && (this.mouseY < 450)) {
      if (this.currentPlayer == 0) {
        this.alert = "You must first start a game.";
      } else {
        int x = this.boardX = (this.mouseX - 50) / 50;
        int y = this.boardY = (this.mouseY - 50) / 50;
        this.currentPlayer = makeMove(x, y, true, this.mainBoard,
            this.currentPlayer);
      }
    }
    repaint();
  }

  private void newGame() {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        this.mainBoard[i][j] = 0;
      }
    }
    this.mainBoard[3][3] = 1;
    this.mainBoard[4][4] = 1;
    this.mainBoard[4][3] = -1;
    this.mainBoard[3][4] = -1;
    this.currentPlayer = 1;
    this.whiteScore = 2;
    this.blackScore = 2;
    this.message = "White, your move. Click a board square or gray stategy button.";
    this.alert = "";
    this.undoStack = new Stack();
    this.redoStack = new Stack();
    this.lastMove = null;
  }

  private void randomMove() {
    if (this.currentPlayer == 0) {
      this.alert = "You must first start a game.";
    } else {
      Vector<Move> validMoves = getValidMoves(this.mainBoard,
          this.currentPlayer);
      int move = this.r.nextInt(validMoves.size());
      this.currentPlayer = makeMove(((Move) validMoves.elementAt(move)).getX(),
          ((Move) validMoves.elementAt(move)).getY(), true, this.mainBoard,
          this.currentPlayer);
    }
  }

  private void maxFlips() {
    if (this.currentPlayer == 0) {
      this.alert = "You must first start a game.";
    } else {
      Vector<Move> validMoves = new Vector();
      Vector<Move> bestMoves = new Vector();
      String tmpMessage = this.message;
      for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
          if (legalMove(i, j, this.currentPlayer, this.mainBoard)) {
            int[][] tmpBoard = copyBoard(this.mainBoard);
            if (this.currentPlayer == 1) {
              int currentScore = this.whiteScore;
              makeMove(i, j, false, this.mainBoard, this.currentPlayer);
              int scoreDiff = this.whiteScore - currentScore;
              validMoves.addElement(new Move(i, j, scoreDiff));
              this.whiteScore = currentScore;
            } else {
              int currentScore = this.blackScore;
              makeMove(i, j, false, this.mainBoard, this.currentPlayer);
              int scoreDiff = this.blackScore - currentScore;
              validMoves.addElement(new Move(i, j, scoreDiff));
              this.blackScore = currentScore;
            }
            this.mainBoard = tmpBoard;
          }
        }
      }
      this.message = tmpMessage;

      int mostFlips = 0;
      for (int i = 0; i < validMoves.size(); i++) {
        int flips = ((Move) validMoves.elementAt(i)).getNumFlips();
        if (flips > mostFlips) {
          mostFlips = flips;
        }
      }
      for (int i = 0; i < validMoves.size(); i++) {
        if (((Move) validMoves.elementAt(i)).getNumFlips() == mostFlips) {
          bestMoves.addElement((Move) validMoves.elementAt(i));
        }
      }
      int move = this.r.nextInt(bestMoves.size());
      this.currentPlayer = makeMove(((Move) bestMoves.elementAt(move)).getX(),
          ((Move) bestMoves.elementAt(move)).getY(), true, this.mainBoard,
          this.currentPlayer);
    }
  }

  private void bestPosition() {
    if (this.currentPlayer == 0) {
      this.alert = "You must first start a game.";
    } else {
      Vector<Move> validMoves = new Vector();
      Vector<Move> bestMoves = new Vector();
      Vector<Move> bestFlips = new Vector();
      String tmpMessage = this.message;
      for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
          if (legalMove(i, j, this.currentPlayer, this.mainBoard)) {
            int[][] tmpBoard = copyBoard(this.mainBoard);
            if (this.currentPlayer == 1) {
              int currentScore = this.whiteScore;
              makeMove(i, j, false, this.mainBoard, this.currentPlayer);
              int scoreDiff = this.whiteScore - currentScore;
              validMoves.addElement(new Move(i, j, scoreDiff,
                  this.positionScore[i][j]));
              this.whiteScore = currentScore;
            } else {
              int currentScore = this.blackScore;
              makeMove(i, j, false, this.mainBoard, this.currentPlayer);
              int scoreDiff = this.blackScore - currentScore;
              validMoves.addElement(new Move(i, j, scoreDiff,
                  this.positionScore[i][j]));
              this.blackScore = currentScore;
            }
            this.mainBoard = tmpBoard;
          }
        }
      }
      this.message = tmpMessage;

      double bestScore = (-1.0D / 0.0D);
      for (int i = 0; i < validMoves.size(); i++) {
        double score = ((Move) validMoves.elementAt(i)).getPositionScore();
        if (score > bestScore) {
          bestScore = score;
        }
      }
      int mostFlips = 0;
      for (int i = 0; i < validMoves.size(); i++) {
        if (((Move) validMoves.elementAt(i)).getPositionScore() == bestScore) {
          bestMoves.addElement((Move) validMoves.elementAt(i));
          int flips = ((Move) validMoves.elementAt(i)).getNumFlips();
          if (flips > mostFlips) {
            mostFlips = flips;
          }
        }
      }
      for (int i = 0; i < bestMoves.size(); i++) {
        if (((Move) bestMoves.elementAt(i)).getNumFlips() == mostFlips) {
          bestFlips.addElement((Move) bestMoves.elementAt(i));
        }
      }
      int move = this.r.nextInt(bestFlips.size());
      this.currentPlayer = makeMove(((Move) bestFlips.elementAt(move)).getX(),
          ((Move) bestFlips.elementAt(move)).getY(), true, this.mainBoard,
          this.currentPlayer);
    }
  }

  private void undoMove() {
    if (this.undoStack.size() == 0) {
      this.alert = "Can't undo";
    } else {
      this.redoStack.push(new GameState(this.mainBoard, this.currentPlayer,
          this.message, this.lastMove));
      this.mainBoard = copyBoard(((GameState) this.undoStack.peek()).getBoard());
      this.currentPlayer = ((GameState) this.undoStack.peek()).getTurn();
      this.lastMove = ((GameState) this.undoStack.peek()).getLastMove();
      this.message = ((GameState) this.undoStack.pop()).getMessage();
      this.alert = "";
      updateScore();
    }
  }

  private void redoMove() {
    if (this.redoStack.size() == 0) {
      this.alert = "Can't redo";
    } else {
      this.undoStack.push(new GameState(this.mainBoard, this.currentPlayer,
          this.message, this.lastMove));
      this.mainBoard = copyBoard(((GameState) this.redoStack.peek()).getBoard());
      this.currentPlayer = ((GameState) this.redoStack.peek()).getTurn();
      this.lastMove = ((GameState) this.redoStack.peek()).getLastMove();
      this.message = ((GameState) this.redoStack.pop()).getMessage();
      this.alert = "";
      updateScore();
    }
  }

  private int makeMove(int x, int y, boolean undoable, int[][] board, int player) {
    if (legalMove(x, y, player, board)) {
      if (undoable) {
        this.undoStack.push(new GameState(board, player, this.message,
            this.lastMove));
        this.redoStack = new Stack();
        this.lastMove = new Move(x, y);
      }
      board[x][y] = player;
      flipPieces(x, y, board, player);
      if (player == 1) {
        if (hasValidMoves(-1, board)) {
          player = -1;
          if (undoable) {
            this.message = "Black, your move. Click a board square or gray stategy button.";
            this.alert = "";
          }
        } else if (hasValidMoves(1, board)) {
          if (undoable) {
            this.message = "White, your move. Click a board square or gray stategy button.";
            this.alert = "Black has no moves.";
          }
        } else if (this.whiteScore > this.blackScore) {
          if (undoable) {
            this.message = "Game Over. White Wins!!";
            this.alert = "Game Over. White Wins!!";
          }
          player = 0;
        } else if (this.blackScore > this.whiteScore) {
          if (undoable) {
            this.message = "Game Over. Black Wins!!";
            this.alert = "Game Over. Black Wins!!";
          }
          player = 0;
        } else {
          if (undoable) {
            this.message = "Game Over. It's a draw.";
            this.alert = "Game Over. It's a draw.";
          }
          player = 0;
        }
      } else if (hasValidMoves(1, board)) {
        player = 1;
        if (undoable) {
          this.message = "White, your move. Click a board square or gray stategy button.";
          this.alert = "";
        }
      } else if (hasValidMoves(-1, board)) {
        if (undoable) {
          this.message = "Black, your move. Click a board square or gray stategy button.";
          this.alert = "White has no moves.";
        }
      } else if (this.whiteScore > this.blackScore) {
        if (undoable) {
          this.message = "Game Over. White Wins!!";
          this.alert = "Game Over. White Wins!!";
        }
        player = 0;
      } else if (this.blackScore > this.whiteScore) {
        if (undoable) {
          this.message = "Game Over. Black Wins!!";
          this.alert = "Game Over. Black Wins!!";
        }
        player = 0;
      } else {
        if (undoable) {
          this.message = "Game Over. It's a draw.";
          this.alert = "Game Over. It's a draw.";
        }
        player = 0;
      }
    } else if (undoable) {
      if (player == 1) {
        this.message = "White, your move. Click a board square or gray stategy button.";
        this.alert = "Not a valid move. Try again.";
      } else {
        this.message = "Black, your move. Click a board square or gray stategy button.";
        this.alert = "Not a valid move. Try again.";
      }
    }
    return player;
  }

  private void flipPieces(int x, int y, int[][] board, int player) {
    int opponent;
    if (player == 1) {
      opponent = -1;
    } else {
      opponent = 1;
    }
    if (checkN(x, y, player, opponent, board)) {
      flipN(x, y, player, opponent, board);
    }
    if (checkNW(x, y, player, opponent, board)) {
      flipNW(x, y, player, opponent, board);
    }
    if (checkW(x, y, player, opponent, board)) {
      flipW(x, y, player, opponent, board);
    }
    if (checkSW(x, y, player, opponent, board)) {
      flipSW(x, y, player, opponent, board);
    }
    if (checkS(x, y, player, opponent, board)) {
      flipS(x, y, player, opponent, board);
    }
    if (checkSE(x, y, player, opponent, board)) {
      flipSE(x, y, player, opponent, board);
    }
    if (checkE(x, y, player, opponent, board)) {
      flipE(x, y, player, opponent, board);
    }
    if (checkNE(x, y, player, opponent, board)) {
      flipNE(x, y, player, opponent, board);
    }
    updateScore();
  }

  private void flipN(int x, int y, int player, int opponent, int[][] board) {
    int i = y - 1;
    while (i > 0) {
      if (board[x][i] == player) {
        break;
      }
      if (board[x][i] == opponent) {
        board[x][i] = player;
      }
      i--;
    }
  }

  private void flipNW(int x, int y, int player, int opponent, int[][] board) {
    if (x < y) {
      int i = 1;
      while (i < x) {
        if (board[(x - i)][(y - i)] == player) {
          break;
        }
        if (board[(x - i)][(y - i)] == opponent) {
          board[(x - i)][(y - i)] = player;
        }
        i++;
      }
    } else {
      int i = 1;
      while (i < y) {
        if (board[(x - i)][(y - i)] == player) {
          break;
        }
        if (board[(x - i)][(y - i)] == opponent) {
          board[(x - i)][(y - i)] = player;
        }
        i++;
      }
    }
  }

  private void flipW(int x, int y, int player, int opponent, int[][] board) {
    int i = x - 1;
    while (i > 0) {
      if (board[i][y] == player) {
        break;
      }
      if (board[i][y] == opponent) {
        board[i][y] = player;
      }
      i--;
    }
  }

  private void flipSW(int x, int y, int player, int opponent, int[][] board) {
    if (x < 7 - y) {
      int i = 1;
      while (i < x) {
        if (board[(x - i)][(y + i)] == player) {
          break;
        }
        if (board[(x - i)][(y + i)] == opponent) {
          board[(x - i)][(y + i)] = player;
        }
        i++;
      }
    } else {
      int i = 1;
      while (i < 8 - y) {
        if (board[(x - i)][(y + i)] == player) {
          break;
        }
        if (board[(x - i)][(y + i)] == opponent) {
          board[(x - i)][(y + i)] = player;
        }
        i++;
      }
    }
  }

  private void flipS(int x, int y, int player, int opponent, int[][] board) {
    int i = y + 1;
    while (i < 8) {
      if (board[x][i] == player) {
        break;
      }
      if (board[x][i] == opponent) {
        board[x][i] = player;
      }
      i++;
    }
  }

  private void flipSE(int x, int y, int player, int opponent, int[][] board) {
    if (x > y) {
      int i = 1;
      while (i < 8 - x) {
        if (board[(x + i)][(y + i)] == player) {
          break;
        }
        if (board[(x + i)][(y + i)] == opponent) {
          board[(x + i)][(y + i)] = player;
        }
        i++;
      }
    } else {
      int i = 1;
      while (i < 8 - y) {
        if (board[(x + i)][(y + i)] == player) {
          break;
        }
        if (board[(x + i)][(y + i)] == opponent) {
          board[(x + i)][(y + i)] = player;
        }
        i++;
      }
    }
  }

  private void flipE(int x, int y, int player, int opponent, int[][] board) {
    int i = x + 1;
    while (i < 8) {
      if (board[i][y] == player) {
        break;
      }
      if (board[i][y] == opponent) {
        board[i][y] = player;
      }
      i++;
    }
  }

  private void flipNE(int x, int y, int player, int opponent, int[][] board) {
    if (7 - x < y) {
      int i = 1;
      while (i < 8 - x) {
        if (board[(x + i)][(y - i)] == player) {
          break;
        }
        if (board[(x + i)][(y - i)] == opponent) {
          board[(x + i)][(y - i)] = player;
        }
        i++;
      }
    } else {
      int i = 1;
      while (i < y) {
        if (board[(x + i)][(y - i)] == player) {
          break;
        }
        if (board[(x + i)][(y - i)] == opponent) {
          board[(x + i)][(y - i)] = player;
        }
        i++;
      }
    }
  }

  private boolean legalMove(int x, int y, int player, int[][] board) {
    int opponent;
    if (player == 1) {
      opponent = -1;
    } else {
      opponent = 1;
    }
    if (board[x][y] != 0) {
      return false;
    }
    boolean N = checkN(x, y, player, opponent, board);
    boolean NW = checkNW(x, y, player, opponent, board);
    boolean W = checkW(x, y, player, opponent, board);
    boolean SW = checkSW(x, y, player, opponent, board);
    boolean S = checkS(x, y, player, opponent, board);
    boolean SE = checkSE(x, y, player, opponent, board);
    boolean E = checkE(x, y, player, opponent, board);
    boolean NE = checkNE(x, y, player, opponent, board);
    return (N) || (NW) || (W) || (SW) || (S) || (SE) || (E) || (NE);
  }

  private boolean checkN(int x, int y, int player, int opponent, int[][] board) {
    if (y < 2) {
      return false;
    }
    if (board[x][(y - 1)] != opponent) {
      return false;
    }
    int i = 2;
    while (i < y + 1) {
      if (board[x][(y - i)] == player) {
        return true;
      }
      if (board[x][(y - i)] == 0) {
        return false;
      }
      i++;
    }
    return false;
  }

  private boolean checkNW(int x, int y, int player, int opponent, int[][] board) {
    if ((x < 2) || (y < 2)) {
      return false;
    }
    if (board[(x - 1)][(y - 1)] != opponent) {
      return false;
    }
    if (x < y) {
      int i = 2;
      while (i < x + 1) {
        if (board[(x - i)][(y - i)] == player) {
          return true;
        }
        if (board[(x - i)][(y - i)] == 0) {
          return false;
        }
        i++;
      }
    } else {
      int i = 2;
      while (i < y + 1) {
        if (board[(x - i)][(y - i)] == player) {
          return true;
        }
        if (board[(x - i)][(y - i)] == 0) {
          return false;
        }
        i++;
      }
    }
    return false;
  }

  private boolean checkW(int x, int y, int player, int opponent, int[][] board) {
    if (x < 2) {
      return false;
    }
    if (board[(x - 1)][y] != opponent) {
      return false;
    }
    int i = 2;
    while (i < x + 1) {
      if (board[(x - i)][y] == player) {
        return true;
      }
      if (board[(x - i)][y] == 0) {
        return false;
      }
      i++;
    }
    return false;
  }

  private boolean checkSW(int x, int y, int player, int opponent, int[][] board) {
    if ((x < 2) || (y > 5)) {
      return false;
    }
    if (board[(x - 1)][(y + 1)] != opponent) {
      return false;
    }
    if (x < 7 - y) {
      int i = 2;
      while (i < x + 1) {
        if (board[(x - i)][(y + i)] == player) {
          return true;
        }
        if (board[(x - i)][(y + i)] == 0) {
          return false;
        }
        i++;
      }
    } else {
      int i = 2;
      while (i < 8 - y) {
        if (board[(x - i)][(y + i)] == player) {
          return true;
        }
        if (board[(x - i)][(y + i)] == 0) {
          return false;
        }
        i++;
      }
    }
    return false;
  }

  private boolean checkS(int x, int y, int player, int opponent, int[][] board) {
    if (y > 5) {
      return false;
    }
    if (board[x][(y + 1)] != opponent) {
      return false;
    }
    int i = 2;
    while (i < 8 - y) {
      if (board[x][(y + i)] == player) {
        return true;
      }
      if (board[x][(y + i)] == 0) {
        return false;
      }
      i++;
    }
    return false;
  }

  private boolean checkSE(int x, int y, int player, int opponent, int[][] board) {
    if ((x > 5) || (y > 5)) {
      return false;
    }
    if (board[(x + 1)][(y + 1)] != opponent) {
      return false;
    }
    if (x > y) {
      int i = 2;
      while (i < 8 - x) {
        if (board[(x + i)][(y + i)] == player) {
          return true;
        }
        if (board[(x + i)][(y + i)] == 0) {
          return false;
        }
        i++;
      }
    } else {
      int i = 2;
      while (i < 8 - y) {
        if (board[(x + i)][(y + i)] == player) {
          return true;
        }
        if (board[(x + i)][(y + i)] == 0) {
          return false;
        }
        i++;
      }
    }
    return false;
  }

  private boolean checkE(int x, int y, int player, int opponent, int[][] board) {
    if (x > 5) {
      return false;
    }
    if (board[(x + 1)][y] != opponent) {
      return false;
    }
    int i = 2;
    while (i < 8 - x) {
      if (board[(x + i)][y] == player) {
        return true;
      }
      if (board[(x + i)][y] == 0) {
        return false;
      }
      i++;
    }
    return false;
  }

  private boolean checkNE(int x, int y, int player, int opponent, int[][] board) {
    if ((x > 5) || (y < 2)) {
      return false;
    }
    if (board[(x + 1)][(y - 1)] != opponent) {
      return false;
    }
    if (7 - x < y) {
      int i = 2;
      while (i < 8 - x) {
        if (board[(x + i)][(y - i)] == player) {
          return true;
        }
        if (board[(x + i)][(y - i)] == 0) {
          return false;
        }
        i++;
      }
    } else {
      int i = 2;
      while (i < y + 1) {
        if (board[(x + i)][(y - i)] == player) {
          return true;
        }
        if (board[(x + i)][(y - i)] == 0) {
          return false;
        }
        i++;
      }
    }
    return false;
  }

  private void updateScore() {
    int white = 0;
    int black = 0;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (this.mainBoard[i][j] == 1) {
          white++;
        }
        if (this.mainBoard[i][j] == -1) {
          black++;
        }
      }
    }
    this.whiteScore = white;
    this.blackScore = black;
  }

  private boolean hasValidMoves(int player, int[][] board) {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (legalMove(i, j, player, board)) {
          return true;
        }
      }
    }
    return false;
  }

  private void minimax() {
    if (this.currentPlayer == 0) {
      this.alert = "You must first start a game.";
    } else {
      Vector<Move> validMoves = getValidMoves(this.mainBoard,
          this.currentPlayer);
      int maxDepth = 5;
      if (validMoves.size() > 8) {
        maxDepth--;
      }
      if (validMoves.size() > 15) {
        maxDepth--;
      }
      Vector<Move> bestMoves = new Vector();
      double bestScore = (-1.0D / 0.0D);
      for (int i = 0; i < validMoves.size(); i++) {
        Move move = (Move) validMoves.elementAt(i);
        int[][] board = copyBoard(this.mainBoard);
        int nextPlayer = makeMove(move.getX(), move.getY(), false, board,
            this.currentPlayer);
        double score = 0.0D;
        if (nextPlayer == 0) {
          score = calculateHeuristic(board, this.currentPlayer);
        } else if (nextPlayer == this.currentPlayer) {
          score = calculateHeuristic(board, nextPlayer) + 100.0D;
        } else {
          score = -minimax(board, nextPlayer, 0, maxDepth, (-1.0D / 0.0D),
              (1.0D / 0.0D));
        }
        bestScore = Math.max(score, bestScore);
        move.setHueristicScore(score);
        System.out.println("score=" + score);
      }
      System.out.println("bestScore=" + bestScore + ", maxDepth=" + maxDepth);
      for (int i = 0; i < validMoves.size(); i++) {
        if (((Move) validMoves.get(i)).getHueristicScore() > bestScore - 0.2D) {
          bestMoves.add((Move) validMoves.get(i));
        }
      }
      int move = this.r.nextInt(bestMoves.size());
      System.out.println("selectedScore="
          + ((Move) bestMoves.get(move)).getHueristicScore());
      this.currentPlayer = makeMove(((Move) bestMoves.get(move)).getX(),
          ((Move) bestMoves.get(move)).getY(), true, this.mainBoard,
          this.currentPlayer);
    }
  }

  private double minimax(int[][] board, int player, int depth, int maxDepth,
      double alpha, double beta) {
    Vector<Move> validMoves = getValidMoves(board, player);
    if ((depth == maxDepth) || (validMoves.size() == 0)) {
      return calculateHeuristic(board, player);
    }
    for (int i = 0; i < validMoves.size(); i++) {
      Move move = (Move) validMoves.elementAt(i);
      int[][] nextBoard = copyBoard(board);
      int nextPlayer = makeMove(move.getX(), move.getY(), false, nextBoard,
          player);
      if (nextPlayer == player) {
        alpha = Math.max(alpha, calculateHeuristic(nextBoard, player) + 100.0D);
      } else {
        alpha = Math
            .max(
                alpha,
                -minimax(nextBoard, nextPlayer, depth + 1, maxDepth, -beta,
                    -alpha));
      }
      if (alpha >= beta) {
        break;
      }
    }
    return alpha;
  }

  private Vector<Move> getValidMoves(int[][] board, int player) {
    Vector<Move> validMoves = new Vector();
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (legalMove(i, j, player, board)) {
          validMoves.add(new Move(i, j, calculateHeuristic(board, player)));
        }
      }
    }
    return validMoves;
  }

  private double calculateHeuristic(int[][] board, int player) {
    double value = 0.0D;
    int numEmpty = 0;
    int opponent = -1;
    if (player == -1) {
      opponent = 1;
    }
    int opponentCount = 0;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (board[i][j] == 0) {
          numEmpty++;
        }
        if (board[i][j] == opponent) {
          opponentCount++;
        }
      }
    }
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (board[i][j] == player) {
          value += this.positionScore[i][j] * (numEmpty / 64.0D) + 1.0D;
        } else if (board[i][j] == opponent) {
          value -= this.positionScore[i][j] * (numEmpty / 64.0D) + 1.0D;
        }
      }
    }
    if (opponentCount == 0) {
      value += 1000.0D;
    }
    return value;
  }

  private int[][] copyBoard(int[][] board) {
    int[][] newBoard = new int[8][8];
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        newBoard[i][j] = board[i][j];
      }
    }
    return newBoard;
  }
}
