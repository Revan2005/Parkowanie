package parkowanie;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class MyJFrame extends JFrame implements ActionListener{
	MyJMenuBar menu;
	MyPanel panel;
	JButton ostre, fuzzy1, fuzzy2, stop; 
	
	public MyJFrame(String s){
		super(s);
		setLayout(new BorderLayout());
		setBounds(10, 10, 800, 500);
		
		MyJMenuBar menu = new MyJMenuBar(this);
		add(menu, BorderLayout.NORTH);
		
		panel = new MyPanel(this);
		add(panel, BorderLayout.CENTER);
		
		JFrame buttonFrame = new JFrame();
		buttonFrame.setBounds(100, 700, 400, 100);
		ostre = new JButton("Ostre");
		ostre.addActionListener(panel.kierowca);
		buttonFrame.add(ostre, BorderLayout.WEST);
		
		fuzzy1 = new JButton("Fuzzy1");
		fuzzy1.addActionListener(panel.kierowca);
		buttonFrame.add(fuzzy1, BorderLayout.NORTH);		
		
		fuzzy2 = new JButton("Fuzzy2");
		fuzzy2.addActionListener(panel.kierowca);
		buttonFrame.add(fuzzy2, BorderLayout.CENTER);
		
		stop = new JButton("Stop");
		stop.addActionListener(panel.kierowca);
		buttonFrame.add(stop, BorderLayout.EAST);
		buttonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buttonFrame.setVisible(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "JPG Images", "jpg");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to open this file: " +
	            chooser.getSelectedFile().getName());
	       File selected_file = chooser.getSelectedFile();
	       try {
	    	   BufferedImage parkingMap = ImageIO.read(selected_file);
	    	   panel.restart(parkingMap);
	       } catch (IOException e1) {
	    	   // TODO Auto-generated catch block
	    	   e1.printStackTrace();
	       }
	    }	
	}
}
