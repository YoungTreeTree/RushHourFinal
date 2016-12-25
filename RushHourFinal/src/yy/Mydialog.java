package yy;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Mydialog extends JDialog {
	public Mydialog(JFrame parent){
		super(parent,"paihangbang",true);
		JPanel panel=new JPanel();
		panel.add(new JLabel("dsad"));
		getContentPane().add(panel);
		this.setBounds(150, 150, 400, 400);
	}
}