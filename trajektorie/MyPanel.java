import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class MyPanel extends JPanel {
  ArrayList<Point> centers;
  ArrayList<Polygon> corners;

  public MyPanel(ArrayList<Point> centers, ArrayList<Polygon> corners) {
    this.centers = centers;
    this.corners = corners;
  }

  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D)g;
    drawBackground(g2d);
    //drawFilled(g2d);
    //drawContours(g2d);
    drawFilledWithContours(g2d);
    drawCenters(g2d);
  }

  private void drawCenters(Graphics2D g2d) {
    g2d.setColor(Color.RED);
    for(Point p : centers)
      g2d.fillOval(p.x, p.y, 5, 5);
  }

  private void drawFilledWithContours(Graphics2D g2d) {
    for(Polygon p : corners) {
      g2d.setColor(Color.BLUE);
      g2d.fillPolygon(p);
      g2d.setColor(Color.BLACK);
      g2d.drawPolygon(p);
    }
  }

  private void drawFilled(Graphics2D g2d) {
    g2d.setColor(Color.BLUE);
    for(Polygon p : corners)
      g2d.fillPolygon(p);
  }

  private void drawContours(Graphics2D g2d) {
    g2d.setColor(Color.BLACK);
    for(Polygon p : corners)
      g2d.drawPolygon(p);
  }

  private void drawBackground(Graphics2D g2d) {
    String filename = "background.bmp";
    BufferedImage bImage = readBackgroundFile(filename);
    g2d.drawImage(bImage, 0, 0, null);
  }

  private BufferedImage readBackgroundFile(String filename) {
    BufferedImage bImage = null;
    try {
      bImage = ImageIO.read(new File(filename));

    } catch (Exception e){
      e.printStackTrace();
    }
    return bImage;
  }

}
