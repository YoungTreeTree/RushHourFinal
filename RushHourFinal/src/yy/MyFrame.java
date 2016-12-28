package yy;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class MyFrame extends JFrame implements ActionListener{
	private String user_name;
	private JMenuItem jMenuItem_normal_new_game=new JMenuItem("New Game");
	private JMenuItem jMenuItem_record=new JMenuItem("Record");
	private JMenuItem jMenuItem_diy_game=new JMenuItem("New DIY Game");
	private JMenuItem jMenuItem_start_diy_game=new JMenuItem("Start DIY Game");
	private JMenuItem jMenuItem_skip=new JMenuItem("skip");
	private JMenuItem jMenuItem_undo=new JMenuItem("undo");
	private JMenuItem jMenuItem_reset=new JMenuItem("reset");
	private JMenuItem jMenuItem_auto=new JMenuItem("auto");
	private LoginPanel loginPanel=new LoginPanel();
	public MyFrame(){
		super();
		JMenuBar jMenuBar=new JMenuBar();
		JMenu JMenu_start=new JMenu("menu");
		JMenu JMenu_normal=new JMenu("normal");
		JMenu JMenu_diy=new JMenu("Diy");
		JMenu JMenu_operation=new JMenu("operation");
		/********************jMenuItem添加事件*****************/
		jMenuItem_normal_new_game.addActionListener(this);
		jMenuItem_record.addActionListener(this);
		jMenuItem_skip.addActionListener(this);
		jMenuItem_undo.addActionListener(this);
		jMenuItem_reset.addActionListener(this);
		jMenuItem_diy_game.addActionListener(this);
		jMenuItem_start_diy_game.addActionListener(this);
		jMenuItem_auto.addActionListener(this);
		/********************jMenu添加jMenuItem*****************/
		JMenu_start.add(JMenu_normal);
		JMenu_start.add(JMenu_diy);
		JMenu_normal.add(jMenuItem_normal_new_game);
		JMenu_normal.add(jMenuItem_record);
		JMenu_diy.add(jMenuItem_diy_game);
		JMenu_diy.add(jMenuItem_start_diy_game);
		JMenu_operation.add(jMenuItem_skip);
		JMenu_operation.add(jMenuItem_reset);
		JMenu_operation.add(jMenuItem_undo);
		JMenu_operation.add(jMenuItem_auto);
		/********************jMenuBar添加jMenu*****************/
		jMenuBar.add(JMenu_start);
		jMenuBar.add(JMenu_operation);
		/********************基本设置*****************/
		this.setJMenuBar(jMenuBar);
		this.setBounds(100, 100, 1000, 800);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setTitle("RushHour");
		this.add(loginPanel);
		
	}
	public void makeOperationOff(){
		jMenuItem_skip.setEnabled(false);
		jMenuItem_undo.setEnabled(false);
		jMenuItem_reset.setEnabled(false);
	}
	public void makeOperationOn(){
		jMenuItem_skip.setEnabled(true);
		jMenuItem_undo.setEnabled(true);
		jMenuItem_reset.setEnabled(true);
	}
	/********************监听menu的事件调用panel里面的函数*****************/
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object src=e.getSource();
		if(src==jMenuItem_normal_new_game){
			this.makeOperationOn();
			this.loginPanel.changeToGamePanel();
			this.loginPanel.gamePanel.start();
		}
		if(src==jMenuItem_record){
			this.loginPanel.gamePanel.callRecord(this);
		}
		if(src==jMenuItem_skip){
			this.loginPanel.gamePanel.skip();
		}
		if(src==jMenuItem_reset){
			this.loginPanel.gamePanel.reset();
		}
		if(src==jMenuItem_undo){
			this.loginPanel.gamePanel.undo();
		}
		if(src==jMenuItem_diy_game){
			this.makeOperationOff();
			this.loginPanel.changeToDiy();
		}
		if(src==jMenuItem_start_diy_game){
			this.makeOperationOn();
			this.loginPanel.changeToGamePanel();
			this.loginPanel.gamePanel.startDiyGame();
		}
		if(src==jMenuItem_auto){
			this.loginPanel.gamePanel.getMayBeOperation();
		}
	}
}