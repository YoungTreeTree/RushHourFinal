package yy;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Mydialog extends JDialog {
	public Mydialog(JFrame parent,String code,ArrayList<Operation>operations){
		super(parent);
		if(code=="record"){
			this.setTitle("排行榜");
			JPanel panel=new JPanel();
			HashMap<String, Integer>record =new HashMap<>();
			try {
				record=new FileReader().fromFile("src/record.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String panelString = "<html>";
			for(int i=0;i<record.size();i++){
				panelString+="Game&nbsp;"+(i+1)+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+record.get(i+"")+"步<br><br>";
			}
			panelString+="</html>";
			panel.add(new JLabel(panelString));
			getContentPane().add(panel);
			this.setBounds(150, 150, 400, 400);
		}
		if(code=="operation"){
			this.setTitle("operation");
			JPanel panel=new JPanel();
			String panelString="<html>";
			panelString+="车号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作<br>";
			for(int i=0;i<operations.size();i++){
				panelString+=(operations.get(i).getSelectCar()+1)+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+operations.get(i).getPressMove()+"<br>";
			}
			panelString+="</html>";
			panel.add(new JLabel(panelString));
			getContentPane().add(panel);
			this.setBounds(150, 150, 400, 400);
		}
	}
}