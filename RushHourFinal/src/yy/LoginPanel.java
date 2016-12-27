package yy;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends JPanel implements ActionListener{
	private String user_name;
	private String password;
	public  GamePanel gamePanel=new GamePanel();
	public  DiyGame diyGame=new DiyGame();
	int reCode=0;
	JLabel jLabel_name=new JLabel("用户名 : ");
	JLabel jLabel_password=new JLabel("密码 : ");
	JTextField jTextFieldTextField_name=new JTextField();
	JPasswordField jPasswordField_password=new JPasswordField();
	JButton jButton_submit=new JButton("登录");
	public LoginPanel() {
		// TODO Auto-generated constructor stub
		super();
		this.setLayout(null);
		jLabel_name.setBounds(350, 200,100,20);
		jLabel_name.setFont(new Font(Font.DIALOG, 0, 20));
		jLabel_password.setBounds(350, 250,100,20);
		jLabel_password.setFont(new Font(Font.DIALOG, 0, 20));
		jTextFieldTextField_name.setBounds(450, 200, 150, 20);
		jTextFieldTextField_name.setSize(150, 20);
		jPasswordField_password.setBounds(450, 250, 150, 20);
		jPasswordField_password.setSize(150, 20);
		jButton_submit.setBounds(430, 300, 70, 20);
		jButton_submit.addActionListener(this);
		this.add(jLabel_name,0);
		this.add(jLabel_password,1);
		this.add(jTextFieldTextField_name,2);
		this.add(jPasswordField_password,3);
		this.add(jButton_submit,4);
	}
	public void changeToDiy(){
		this.removeAll();
		this.add(diyGame);
		diyGame.setBounds(0, 0, 1000, 800);
	}
	public void changeToGamePanel(){
		this.removeAll();
		this.add(gamePanel);
		gamePanel.setBounds(0, 0, 1000, 800);
		gamePanel.start();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object src=e.getSource();
		System.out.println(jPasswordField_password.getPassword());
		if(src==jButton_submit){
			/***************验证登录的先注释掉，还没有链接数据库****************/
		//if(jTextFieldTextField_name.getText().equals("Young")&&
		//			jPasswordField_password.getText().equals("911")){
				System.out.println("success");
				this.removeAll();
				this.add(gamePanel);
				gamePanel.setBounds(0, 0, 1000, 800);
				gamePanel.start();
				//	}
		}
	}
	
}

