import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;

public class DBApplet extends Applet {
  private static final long serialVersionUID = 1L;
  Image offScreenBuffer;

  public void update(Graphics g) {
    if ((this.offScreenBuffer == null)
        || (this.offScreenBuffer.getWidth(this) != getSize().width)
        || (this.offScreenBuffer.getHeight(this) != getSize().height)) {
      this.offScreenBuffer = createImage(getSize().width, getSize().height);
    }
    Graphics gr = this.offScreenBuffer.getGraphics();

    paint(gr);

    g.drawImage(this.offScreenBuffer, 0, 0, this);
  }
}
