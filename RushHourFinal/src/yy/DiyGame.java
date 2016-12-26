package yy;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.w3c.dom.events.MouseEvent;

public class DiyGame extends Panel implements MouseListener,MouseMotionListener{
	int current_car_code=0;				//表示鼠标点击的左边的格子的例子的类型
	private Game diyGames = new Game();
	private float mouse_position_x=-1;
	private float mouse_position_y=-1;
	private boolean[] isFree = new boolean[36];
	public DiyGame() {
		// TODO Auto-generated constructor stub
		addMouseListener(this);
		addMouseMotionListener(this);
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
		/**************右边的格子*************左上角是200 10/
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
		if(current_car_code!=0){
			drawMoveCars(g);
		}
		drawCars(diyGames.getCarList(), g);
		
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
			System.out.println(vertical+" "+visible+" "+blocks);
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
		System.out.println("mousePress");
		int x=e.getX();
		int y=e.getY();
		if(x>25&&x<175&&y>30&&y<80){current_car_code=1;}
		if(x>25&&x<75&&y>150&&y<300){current_car_code=2;}
		if(x>25&&x<125&&y>400&&y<450){current_car_code=3;}
		if(x>25&&x<75&&y>500&&y<600){current_car_code=4;}
		System.out.println(current_car_code);
	}
	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("mouseReleased");
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
}
