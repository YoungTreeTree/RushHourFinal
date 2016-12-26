package yy;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.w3c.dom.events.MouseEvent;

public class DiyGame extends Panel implements MouseListener,MouseMotionListener,ActionListener{
	int current_car_code=0;				//表示鼠标点击的左边的格子的例子的类型
	JButton jButton_reset=new JButton("reset");
	JButton jButton_start=new JButton("start");
	private Game diyGames = new Game();
	private float mouse_position_x=-1;
	private float mouse_position_y=-1;
	private boolean[] isFree = new boolean[36];
	public DiyGame() {
		// TODO Auto-generated constructor stub
		addMouseListener(this);
		addMouseMotionListener(this);
		jButton_reset.setBounds(870, 110, 100, 50);
		this.add(jButton_reset);
		jButton_reset.addActionListener(this);
		jButton_start.setBounds(870, 310, 100, 50);
		this.add(jButton_start);
		jButton_start.addActionListener(this);
		/*********一开始的的时候所有的格子除了初始的两个都是free的*******/
		for(int i=0;i<36;i++){
			isFree[i]=true;
		}
		isFree[12]=false;
		isFree[13]=false;
		/*测试用
		int num[]=new int []{12, 13, 36};
		Car car =new Car();
		car.setBlocks(num);
		car.setVertical(true);
		car.setVisible(true);
		diyGames.carList.add(car);
		*/
		
	}
	public void paint(Graphics g) {
		super.paint(g);
		/**************右边的格子*************左上角是200 10*/
		for (int i = 0; i < 7; i++) {
			int x1 = 200;
			int y1 = 10 + 100 * i;
			g.drawLine(x1, y1, x1 + 600, y1);
		}
		for (int i = 0; i < 7; i++) {
			int x1 = 200 + 100 * i;
			int y1 = 10;
			g.drawLine(x1, y1, x1, y1 + 600);
		}
		/**************左边的例子*************/
		Icon icon = new ImageIcon("src/abc.png");
		Image image = Toolkit.getDefaultToolkit().getImage("src/abc.png");
		g.drawImage(image,25,30,150,50,null,null);
		g.drawImage(image,25,150,50,150,null,null);
		g.drawImage(image,25,400,100,50,null,null);
		g.drawImage(image,25,500,50,100,null,null);
		drawCars(diyGames.getCarList(), g);
		if(current_car_code!=0){
			drawMoveCars(g);
		}
		/************右边的按钮 reset 和 start ***********/
		
		
		
	}

	public void drawCars(ArrayList<Car> cars, Graphics g) {// draw cars by
		// ArrayList<Car>
		System.out.println("drawing cars: \n there are "+cars.size()+" cars ");
		for (int i = 0; i < cars.size(); i++) {
			Icon icon = new ImageIcon("src/abc.png");// ?why
			Image image = Toolkit.getDefaultToolkit().getImage("src/abc.png");
			boolean vertical = cars.get(i).isVertical();
			boolean visible = cars.get(i).isVisible();
			int[] blocks = cars.get(i).getBlocks();
			//System.out.println(vertical+" "+visible+" "+blocks);
			int num;// 小车长度
			if (blocks[2] > 35) {
				num = 2;
			} else {
				num = 3;
			}
			int x0 = 200;
			int y0 = 10;
			int m = 100;
			if (visible) {
				if (vertical) {
					g.drawImage(image, (blocks[0] % 6) * m + x0, (blocks[0] / 6) * m + y0, m, m * num, null, null);
				} else {
					g.drawImage(image, (blocks[0] % 6) * m + x0, (blocks[0] / 6) * m + y0, m * num, m, null, null);
				}

				for (int j = 0; j < num; j++) {
					isFree[blocks[j]] = false;
				}

			} else {
			} // invisible

		}
		System.out.println("draw cars over");
	}

	private void drawMoveCars(Graphics g) {
		// TODO Auto-generated method stub
		Icon icon = new ImageIcon("src/abc.png");
		Image image = Toolkit.getDefaultToolkit().getImage("src/abc.png");
		int width=0;
		int height=0;
		if(current_car_code==1){
			width=300;
			height=100;
		}
		else if(current_car_code==2){
			width=100;
			height=300;
		}
		else if(current_car_code==3){
			width=200;
			height=100;
		}
		else if(current_car_code==4){
			width=100;
			height=200;
		}
		g.drawImage(image,(int)mouse_position_x-50,(int)mouse_position_y-50,width,height,null,null);
	}
	@Override
	public void mouseDragged(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("drag");
		this.mouse_position_x=e.getX();
		this.mouse_position_y=e.getY();
		repaint();
	}
	@Override
	public void mouseMoved(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("mouseClicked");
	}
	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
		if(diyGames.getCarList().size()==6){current_car_code=0;return ;}
		System.out.println("mousePress");
		int x=e.getX();
		int y=e.getY();
		if(x>25&&x<175&&y>30&&y<80){current_car_code=1;}
		if(x>25&&x<75&&y>150&&y<300){current_car_code=2;}
		if(x>25&&x<125&&y>400&&y<450){current_car_code=3;}
		if(x>25&&x<75&&y>500&&y<600){current_car_code=4;}
	}
	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
		if(current_car_code==0)return;
		System.out.println("mouseReleased");
		int x=e.getX();
		int y=e.getY();
		int blockNum[]=new int[3];
		boolean ifAdd=true;
		boolean vertical=true;
		blockNum[0]=changeToBlock(x, y);
		switch(current_car_code){
		case 1 :
			vertical=false;
			if(blockNum[0]%6>3){
				ifAdd=false;
				break;
			}else{
				blockNum[1]=blockNum[0]+1;
				blockNum[2]=blockNum[0]+2;
			}
			
			if(!checkFree(blockNum)){ifAdd=false;break;}
			break;
		case 2 :
			vertical=true;
			if(blockNum[0]>=24){
				ifAdd=false;
				break;
			}else{
				blockNum[1]=blockNum[0]+6;
				blockNum[2]=blockNum[0]+12;
			}
			
			if(!checkFree(blockNum)){ifAdd=false;break;}
			break;
		case 3 :
			vertical=false;
			if(blockNum[0]%6==5){
				ifAdd=false;
				break;
			}else{
				blockNum[1]=blockNum[0]+1;
				blockNum[2]=36;
			}
			if(!checkFree(blockNum)){ifAdd=false;break;}
			break;
		case 4 :
			vertical=true;
			if(blockNum[0]>=30){
				ifAdd=false;
				break;
			}else{
				blockNum[1]=blockNum[0]+6;
				blockNum[2]=36;
			}
			
			if(!checkFree(blockNum)){ifAdd=false;break;}
			break;
		}
		if(ifAdd){
			Car car=new Car();
			car.setBlocks(blockNum);
			car.setVertical(vertical);
			car.setVisible(true);
			diyGames.getCarList().add(car);
			setNotFree(blockNum);
		}
		
		current_car_code=0;
		repaint();
	}
	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	/*****************把坐标变成方格的编号****************/
	public int changeToBlock(int x, int y) {
		int block = 36;
		int x0 = 200;
		int y0 = 10;// (x0,y0)是棋盘左上角坐标
		int m = 100;// 方格大小
		int col = (x - x0) / m;
		int line = (y - y0) / m;
		block = line * 6 + col;
		System.out.println(block);
		return block;
	}
	/*****************用于检测add的小车是否冲突*******************/
	public boolean checkFree(int blockNum[]){
		boolean reboolean=true;
		for(int i=0;i<blockNum.length;i++){
			if(blockNum[i]==36){}else{
				if(!isFree[blockNum[i]]){
					System.out.println(isFree[blockNum[i]]);
					reboolean=false;
				}
			}
		}
		return reboolean;
	}
	/*****************将新插入的小车的方块设置成false*******************/
	public void setNotFree(int blockNum[]){
		for(int i=0;i<blockNum.length;i++){
			if(blockNum[i]==36){}else{
				isFree[blockNum[i]]=false;
			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object src=e.getSource();
		if(src==jButton_reset){
			diyGames.getCarList().clear();
		}
	}
}
