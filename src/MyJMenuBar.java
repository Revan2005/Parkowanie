import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class MyJMenuBar extends JMenuBar{

	public MyJMenuBar(MyJFrame frame){
		JMenu file = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");

		open.addActionListener(frame);
			
		file.add(open);
		add(file);
	}
	
}
