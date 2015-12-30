import javax.swing.JFrame;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.Polygon;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Arrays;
//import java.lang.Integer;

public class PointsToImageConverter {

  public static void main(String[] args) {
    JFrame frame = new JFrame("Ramka");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1200, 900);
    ArrayList<Point> centers = readCentersFromFile("trajektoria.txt");
    ArrayList<Polygon> corners = readCornersFromFile("trajektoria.txt");
    MyPanel panel = new MyPanel(centers, corners);
    frame.add(panel);

    frame.setVisible(true);
  }

  public static ArrayList<Point> readCentersFromFile(String filename) {
    ArrayList<Point> centers = new ArrayList<Point>();
    try {
      FileReader fReader = new FileReader(filename);
      BufferedReader bReader = new BufferedReader(fReader);
      centers = readCentersFromReader(bReader);
      //reader.close();
    } catch(Exception e){
      e.printStackTrace();
    }
    return centers;
  }

  public static ArrayList<Point> readCentersFromReader(BufferedReader bReader) {
    ArrayList<Point> centers = new ArrayList<Point>();
    String line;
    String[] splittedLine;
    int x, y;
    while(true) {
      try {
        line = bReader.readLine();
        splittedLine = line.split(" ");
        x = Integer.parseInt(splittedLine[0]);
        y = Integer.parseInt(splittedLine[1]);
        //System.out.println("x, y = " + x + " " + y);
        centers.add(new Point(x, y));
      } catch (Exception e) {
        break;
      }
    }
    return centers;
  }

  public static ArrayList<Polygon> readCornersFromFile(String filename) {
    ArrayList<Polygon> corners = new ArrayList<Polygon>();
    try {
      FileReader fReader = new FileReader(filename);
      BufferedReader bReader = new BufferedReader(fReader);
      corners = readCornersFromReader(bReader);
      //reader.close();
    } catch(Exception e){
      e.printStackTrace();
    }
    return corners;
  }

  public static ArrayList<Polygon> readCornersFromReader(BufferedReader bReader) {
    ArrayList<Polygon> corners = new ArrayList<Polygon>();
    String line;
    String[] splittedLine;
    int numPoints = 4;
    int[] xPoints = new int[numPoints];
    int[] yPoints = new int[numPoints];
    while(true) {
      try {
        line = bReader.readLine();
        splittedLine = line.split(" ");
        for(int i = 2; i < 2 + numPoints; i++)
          xPoints[i - 2] = Integer.parseInt(splittedLine[i]);
        for(int i = 2 + numPoints; i < 2 + 2*numPoints; i++)
          yPoints[i - (2 + numPoints)] = Integer.parseInt(splittedLine[i]);
        //System.out.println("xPoints, yPoinst = " + Arrays.toString(xPoints) + " " + Arrays.toString(yPoints));
        corners.add(new Polygon(xPoints, yPoints, numPoints));
      } catch (Exception e) {
        break;
      }
    }
    return corners;
  }

}
