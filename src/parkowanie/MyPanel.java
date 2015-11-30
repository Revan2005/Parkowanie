package parkowanie;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class MyPanel extends JPanel implements KeyListener{
	
	BufferedImage parking_map;
	ModelOtoczenia modelOtoczenia;
	Car car;
	Kierowca kierowca;
	MyJFrame parent_frame;
	//WidokAutaZGory widok;
	
	
	
	public MyPanel(MyJFrame frame){
		setFocusable(true);
		addKeyListener(this);
		parent_frame = frame;
		File flPlik =  new File("/home/tomek/PROJEKTY/ObliczeniaMiekkie/Parkowanie/Maps/2.bmp");
		try {
			parking_map =   ImageIO.read(flPlik);
			int width = parking_map.getWidth(null);
	 	    int height = parking_map.getHeight(null);
	 	    parent_frame.setSize(width, height+50);
			//restart();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.out.println("printstaktrejs");
		}
		modelOtoczenia = new ModelOtoczenia(parking_map);
		car = new Car(this);
 	    kierowca = new Kierowca(car, modelOtoczenia);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		//drawAll rysuje samochod I MAPE PARKINGU!!
		drawAll(g2d);
	}
	
	public void drawAll(Graphics2D g2d){
		g2d.drawImage(parking_map, 0, 0, null);
		drawCarView(g2d, car);
		drawUkladOdniesienia(g2d, modelOtoczenia);
	}
	
	public void drawCarView(Graphics2D g2d, Car c) {
		c.drawWidok(g2d);
	}
	
	public void drawUkladOdniesienia(Graphics2D g2d, ModelOtoczenia modelOtoczenia){
		modelOtoczenia.drawUkladOdniesienia(g2d);
	}
	
	
	
	
	public void restart(BufferedImage img){
		parking_map = img;
		car.restart(parking_map);
		kierowca.restart(parking_map);
 	   	//car.setMap(parking_map);
 	    int width = parking_map.getWidth(null);
 	    int height = parking_map.getHeight(null);
 	    parent_frame.setSize(width, height+50);
 	    
 	    repaint();
	}

	
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		char c = e.getKeyChar();
		//screenShotInterval++;
		car.move(c);
		//repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	


}
