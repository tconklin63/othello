public class Move {
  private int xPos;
  private int yPos;
  private int numFlips;
  private double positionScore;
  private double hueristicScore;

  public Move(int x, int y) {
    this.xPos = x;
    this.yPos = y;
  }

  public Move(int x, int y, int nf) {
    this.xPos = x;
    this.yPos = y;
    this.numFlips = nf;
  }

  public Move(int x, int y, int nf, double ps) {
    this.xPos = x;
    this.yPos = y;
    this.numFlips = nf;
    this.positionScore = ps;
  }

  public Move(int x, int y, double hs) {
    this.xPos = x;
    this.yPos = y;
    this.hueristicScore = hs;
  }

  public void setX(int x) {
    this.xPos = x;
  }

  public void setY(int y) {
    this.yPos = y;
  }

  public void setNumFlips(int nf) {
    this.numFlips = nf;
  }

  public void setScore(int ps) {
    this.positionScore = ps;
  }

  public int getX() {
    return this.xPos;
  }

  public int getY() {
    return this.yPos;
  }

  public int getNumFlips() {
    return this.numFlips;
  }

  public double getPositionScore() {
    return this.positionScore;
  }

  public double getHueristicScore() {
    return this.hueristicScore;
  }

  public void setHueristicScore(double hs) {
    this.hueristicScore = hs;
  }
}