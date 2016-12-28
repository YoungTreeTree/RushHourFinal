package yy;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

public class GamePanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
	private static Watch watch = new Watch();
	private static HashMap<String, Integer> record = new HashMap<String, Integer>();
	private boolean[] isFree = new boolean[36];
	private int currentCar = 1;// 1,2,3,4,5,6
	private int currentGame = 0;// firstly 0
	private int currentOperation = 0;
//	private int ifNext=0;
	private ArrayList<Game> allGames = new ArrayList<Game>();
	private ArrayList<Operation> currentGameOperations = new ArrayList<Operation>();
	public ArrayList<Car> initialCars = new ArrayList<Car>();// 该局初始的carList
	public static int totalSteps = 0;
	// ***************新加的全局变量**************
	public Point startPoint = new Point(0, 0);// 以(0,0)为参照
	public Point draggingPoint = new Point(0, 0);
	// public Point endPoint;
	public boolean isDragging = false;
	public int m = 100;// 方块大小
	/****************小车的图片**************/
	Image[] image1 =new Image[6];
	Image[] image2 =new Image[6];
	/*   是不是diy 的游戏  */
	boolean ifDiy;
	Image background=Toolkit.getDefaultToolkit().getImage("src/background.jpg");
	private JFrame myFrame;
	public void printCarList(ArrayList<Car> carList) {
		/*
		for (int i = 0; i < 6; i++) {
			System.out.println(carList.get(i).getBlocks()[0] + " " + carList.get(i).getBlocks()[1] + " "
					+ carList.get(i).getBlocks()[2]);
		}*/
	}

	public void initialize(String path) {
		// ******监听器暂时加在这里*************
		addMouseListener(this);
		addMouseMotionListener(this);
		/**********
		 * 这里需要一个函数把txt里面的记录放进来********* 新写一个类 参数是文件的路径 String path和
		 * HashMap<String, Long> record
		 */
		if(ifDiy==false){
			try {
				record=new FileReader().fromFile("src/record.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i=0;i<6;i++){
			image1[i]=Toolkit.getDefaultToolkit().getImage("src/car1"+i+".jpg");
		}
		for(int i=0;i<6;i++){
			image2[i]=Toolkit.getDefaultToolkit().getImage("src/car2"+i+".jpg");
		}
		for(int i=0;i<6;i++){
			Icon icon=new ImageIcon("src/car1"+i+".jpg");
		}
		for(int i=0;i<6;i++){
			Icon icon=new ImageIcon("src/car2"+i+".jpg");
		}
		Icon icon=new ImageIcon("src/background.jpg");
		watch.start();
		currentGame = 0;
		totalSteps = 0;
		String fileName = "src/"+path+".txt";
		setGames(fileName);
		for (int i = 0; i < 36; i++) {
			isFree[i] = true;
		}
		initialCars.clear();
		for (int i = 0; i < allGames.get(currentGame).getCarList().size(); i++) {
			Car tempCar = new Car();
			tempCar.setBlocks(allGames.get(currentGame).getCarList().get(i).getBlocks());
			tempCar.setVertical(allGames.get(currentGame).getCarList().get(i).isVertical());
			tempCar.setVisible(allGames.get(currentGame).getCarList().get(i).isVisible());
			initialCars.add(tempCar);
		}
		repaint();
	}

	public void gotoNextGame() {
		if (currentGame < (allGames.size() - 1)) {
			System.out.println("go to new game");
			currentGame++;
			currentCar = 1;
			currentOperation = 0;
			totalSteps = 0;
			watch.start();
			currentGameOperations.clear();
			initialCars.clear();
			for (int i = 0; i < allGames.get(currentGame).getCarList().size(); i++) {
				Car tempCar = new Car();
				tempCar.setBlocks(allGames.get(currentGame).getCarList().get(i).getBlocks());
				tempCar.setVertical(allGames.get(currentGame).getCarList().get(i).isVertical());
				tempCar.setVisible(allGames.get(currentGame).getCarList().get(i).isVisible());
				initialCars.add(tempCar);
			}
			for (int i = 0; i < 36; i++) {
				isFree[i] = true;
			}
			repaint();
			GamePanel.this.requestFocus();
		} else {
			System.out.println("all games win");
		}
	}

	public void setGames(String fileName) {
		int[][] position = new int[6][5];
		int gameNum = 0;// 当前局数
		int carNum = 0;// 当前哪辆小车
		int allGameNum = 0;
		allGames.clear();
		try {
			FileInputStream fileInputStream = new FileInputStream(fileName);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader br = new BufferedReader(inputStreamReader);
			String lineString = br.readLine();// 从文件里面读出一行

			while (lineString != null) {
				String[] arrayString = lineString.split(" ");
				int temp = arrayString.length;// temp=5
				for (int i = 0; i < temp; i++) {
					position[carNum][i] = Integer.parseInt(arrayString[i]);// 每读一行就加到position[carNum][]里面去
				}
				if (carNum < 5) {
					carNum++;
				} else if (carNum == 5) {// 读完6行，即一轮
					Game newGame = new Game();
					carNum = 0;
					newGame.setCars(position);
					// if()
					int[] tempblock = new int[3];
					tempblock = newGame.getCarList().get(0).getBlocks();
					allGames.add(newGame);
					gameNum++;
				}
				lineString = br.readLine();// 继续从文件里面读
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		allGameNum = gameNum;// 总的局数
	}

	// lines
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(background, 200, 10, 600, 600, null);
		/*
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
*/
		g.drawString("steps:" + totalSteps, 100, 100);
		drawTime(g);
		drawCars(allGames.get(currentGame).getCarList(), g);
	}

	/***************** 画小车 *****************/
	public void drawCars(ArrayList<Car> cars, Graphics g) {// draw cars by
															// ArrayList<cCar>
		// System.out.println("drawing cars:");
		// printCarList(cars);
		for (int i = 0; i < 6; i++) {
		//	Icon icon = new ImageIcon("src/abc.png");// ?why
		//	Image image = Toolkit.getDefaultToolkit().getImage("src/abc.png");
			boolean vertical = cars.get(i).isVertical();
			boolean visible = cars.get(i).isVisible();
			int[] blocks = cars.get(i).getBlocks();
			int hostBlock=0;
			int num;// 小车长度
			if (blocks[2] > 35) {
				num = 2;
			} else {
				num = 3;
			}
			int x0 = 200;
			int y0 = 10;
			int m = 100;
			if (isDragging && i == currentCar-1) {// 正在拖动的小车，根据draggingPoint来画
				//boolean isDraggable=true;
				//Point checkPoint=new Point(0, 0);
				int neighbours=0;
				int x_part=(int) (draggingPoint.getX()-startPoint.getX());
				int y_part=(int) (draggingPoint.getY()-startPoint.getY());//正或负
				if(vertical){
					if(y_part>0){//down
						neighbours=freeNeighbours(blocks[num-1], 's');
						if(y_part<=neighbours*m){//可以拖动
							if(allGames.get(currentGame).getCarList().get(i).isVertical()){
							g.drawImage(image2[i], (blocks[0] % 6) * m + x0, (blocks[0] / 6) * m + y0+y_part, m, m * num, null, null);
							}
							else{
							g.drawImage(image1[i], (blocks[0] % 6) * m + x0, (blocks[0] / 6) * m + y0+y_part, m, m * num, null, null);
							}
						}
						else{//碰到边界或其他小车，无法拖动，画出临界位置
							if(allGames.get(currentGame).getCarList().get(i).isVertical()){
								g.drawImage(image2[i], (blocks[0] % 6) * m + x0, ((blocks[0]+6*neighbours) / 6) * m + y0, m, m * num, null, null);		
							}
							else{
								g.drawImage(image1[i], (blocks[0] % 6) * m + x0, ((blocks[0]+6*neighbours) / 6) * m + y0, m, m * num, null, null);		
							}
						}		
					}
					else{//y_part<=0//up
						neighbours=freeNeighbours(blocks[0], 'w');
						if(-y_part<=neighbours*m){//可以拖动
							if(allGames.get(currentGame).getCarList().get(i).isVertical()){
								g.drawImage(image2[i], (blocks[0] % 6) * m + x0, (blocks[0] / 6) * m + y0+y_part, m, m * num, null, null);
							}
							else{
								g.drawImage(image1[i], (blocks[0] % 6) * m + x0, (blocks[0] / 6) * m + y0+y_part, m, m * num, null, null);
							}
							
						}
						else{//碰到边界或其他小车，无法拖动，画出临界位置
							if(allGames.get(currentGame).getCarList().get(i).isVertical()){
								g.drawImage(image2[i], (blocks[0] % 6) * m + x0, ((blocks[0]-6*neighbours) / 6) * m + y0, m, m * num, null, null);		
							}
							else{
								g.drawImage(image1[i], (blocks[0] % 6) * m + x0, ((blocks[0]-6*neighbours) / 6) * m + y0, m, m * num, null, null);		
							}
						}					
					}				
				}
				else{//not vertical
					if(x_part>0){//right
						neighbours=freeNeighbours(blocks[num-1], 'd');
						if(x_part<=neighbours*m){//可以拖动
							if(allGames.get(currentGame).getCarList().get(i).isVertical()){
								g.drawImage(image2[i], (blocks[0] % 6) * m + x0+x_part, (blocks[0] / 6) * m + y0, m*num, m, null, null);	
							}
							else{
								g.drawImage(image1[i], (blocks[0] % 6) * m + x0+x_part, (blocks[0] / 6) * m + y0, m*num, m, null, null);
							}
							
						}
						else{//碰到边界或其他小车，无法拖动，画出临界位置
							if(allGames.get(currentGame).getCarList().get(i).isVertical()){
								g.drawImage(image2[i], ((blocks[0]+neighbours )% 6) * m + x0, (blocks[0] / 6) * m + y0, m*num, m , null, null);		
							}
							else{
								g.drawImage(image1[i], ((blocks[0]+neighbours )% 6) * m + x0, (blocks[0] / 6) * m + y0, m*num, m , null, null);		
							}
							
						}							
					}
					else{//x_part<=0//left
						hostBlock=blocks[0];
						neighbours=freeNeighbours(hostBlock, 'a');
						if(-x_part<=neighbours*m){//可以拖动
							if(allGames.get(currentGame).getCarList().get(i).isVertical()){
								g.drawImage(image2[i], (blocks[0] % 6) * m + x0+x_part, (blocks[0] / 6) * m + y0, m*num, m, null, null);
							}
							else{
								g.drawImage(image1[i], (blocks[0] % 6) * m + x0+x_part, (blocks[0] / 6) * m + y0, m*num, m, null, null);
							}
							
						}
						else{//碰到边界或其他小车，无法拖动，画出临界位置
							if(allGames.get(currentGame).getCarList().get(i).isVertical()){
								g.drawImage(image2[i], ((blocks[0]-neighbours) % 6) * m + x0, (blocks[0]/ 6) * m + y0, m*num, m , null, null);
							}
							else{
								g.drawImage(image1[i], ((blocks[0]-neighbours) % 6) * m + x0, (blocks[0]/ 6) * m + y0, m*num, m , null, null);
							}
									
						}			
					}			
				}
				
			} else {//other cars
				if (visible) {
					if (vertical) {
						if(allGames.get(currentGame).getCarList().get(i).isVertical()){
							g.drawImage(image2[i], (blocks[0] % 6) * m + x0, (blocks[0] / 6) * m + y0, m, m * num, null, null);
						}
						else{
							g.drawImage(image1[i], (blocks[0] % 6) * m + x0, (blocks[0] / 6) * m + y0, m, m * num, null, null);
						}
						
					} else {
						if(allGames.get(currentGame).getCarList().get(i).isVertical()){
							g.drawImage(image2[i], (blocks[0] % 6) * m + x0, (blocks[0] / 6) * m + y0, m * num, m, null, null);
						}
						else{
							g.drawImage(image1[i], (blocks[0] % 6) * m + x0, (blocks[0] / 6) * m + y0, m * num, m, null, null);
						}
						
					}
					for (int j = 0; j < num; j++) {
						isFree[blocks[j]] = false;
					}
				} else {
				} // invisible
			}
		}
		// System.out.println("draw cars over");
	}

	/******************* drawTime **********************/
	public void drawTime(Graphics g) {
		int time = (int) (30 - watch.getNowTime() / 1000);
		if (time < 0) {
			time = 0;
		}
		g.drawString(time + "", 200, 10);
	}

	/******************** 键盘事件 **********************/

	public boolean isFocusTraversable() { // 允许面板获得焦点 //?why
		return true;
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		char KeyChar = e.getKeyChar();
		// 123456
		if (KeyChar >= '1' && KeyChar <= '6') {
			System.out.println("press 123456");
			switch (KeyChar) {
			case '1':
				currentCar = 1;
				break;
			case '2':
				currentCar = 2;
				break;
			case '3':
				currentCar = 3;
				break;
			case '4':
				currentCar = 4;
				break;
			case '5':
				currentCar = 5;
				break;
			case '6':
				currentCar = 6;
				break;
			default:
				break;
			}
		}

		// wasd
		else if (KeyChar == 'w' || KeyChar == 'a' || KeyChar == 's' || KeyChar == 'd') {
			switch (KeyChar) {
			case 'w':
				move_w(true);
				break;
			case 'a':
				move_a(true);
				break;
			case 's':
				move_s(true);
				break;
			case 'd':
				move_d(true);
				break;
			default:
				break;
			}
		}
	}

	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			repaint();
			if (Math.abs((int) (30000 - watch.getNowTime())) < 500) {
				watch.end();
				/******* 弹出对话框 reset or skip *******/
				System.out.println("yyyyy");
				System.out.println("yyyyy");
				System.out.println("yyyyy");
			}
			new Timer().schedule(new MyTimerTask(), 1000);

		}
	}

	/************* menu上面的操作 监听器设置在MyFrame上面，留给MyFramed调用 *****************/
	public void startDiyGame() {
		// TODO Auto-generated method stub
		addKeyListener(this);
		this.requestFocus();
		new Timer().schedule(new MyTimerTask(), 1000);
		ifDiy=true;
		initialize("DiyCarPosition");
	}

	public void callRecord(JFrame jFrameP) {
		Mydialog mydialog = new Mydialog(jFrameP,"record",currentGameOperations);
		mydialog.show();
	}
	public void callOperation(JFrame jFrameP){
		Mydialog mydialog = new Mydialog(jFrameP,"operation",currentGameOperations);
		mydialog.show();
	}
	public void skip() {
		System.out.println("press skip");
		gotoNextGame();
	}

	public void start() {
		// TODO Auto-generated method stub
		addKeyListener(this);
		this.requestFocus();
		new Timer().schedule(new MyTimerTask(), 1000);
		ifDiy=false;
		initialize("CarPosition");
	}

	public void undo() {
		if (currentGameOperations.size() > 0) {
			Operation lastOperation = currentGameOperations.get(currentOperation - 1);
			int lastCar = lastOperation.getSelectCar();
			char lastMove = lastOperation.getPressMove();
			switch (lastMove) {
			case 'w':
				move_s(false);
				break;
			case 'a':
				move_d(false);
				break;
			case 's':
				move_w(false);
				break;
			case 'd':
				move_a(false);
				break;
			default:
				break;
			}
		} else {
			System.out.println("cannot move back");
		}
		return;
	}

	public void reset() {
		// TODO Auto-generated method stub
		totalSteps = 0;
		System.out.println("press reset");
		printCarList(initialCars);
		// ArrayList<Car> tempCarList=new ArrayList<Car>();
		// tempCarList=allGames.get(currentGame).
		allGames.get(currentGame).setCarList(initialCars);
		for (int i = 0; i < 36; i++) {
			isFree[i] = true;
		}
		printCarList(initialCars);
		printCarList(allGames.get(currentGame).getCarList());
		currentOperation = 0;
		currentGameOperations.clear();
		repaint();
		System.out.println("reset over");
		watch.start();
		GamePanel.this.requestFocus();
	}

	// 小车移动函数 wasd，会判断是否可以移动
	// 改变carlist，isFree，totalStep，Operation，并且repaint
	// true step+1 else step-1
	public void move_w(Boolean plusStep) {
		if (plusStep) {// step++ case
			boolean vertical = allGames.get(currentGame).getCarList().get(currentCar - 1).isVertical();
			int[] blocks = allGames.get(currentGame).getCarList().get(currentCar - 1).getBlocks();
			int len;
			if (blocks[2] > 35) {
				len = 2;
			} else {
				len = 3;
			}
			int i;
			int[] temp = { 0, 1, 36 };
			boolean movable = vertical && blocks[0] / 6 > 0 && isFree[blocks[0] - 6];
			if (movable) {// 小车竖直方向
				i = 0;
				while (i < len) {
					temp[i] = blocks[i] - 6;
					i++;
				}
				// change isFree
				for (i = 0; i < len; i++) {
					isFree[blocks[i]] = true;
				}
				for (i = 0; i < len; i++) {
					isFree[temp[i]] = false;
				}
				// change carlist
				blocks = temp;
				allGames.get(currentGame).getCarList().get(currentCar - 1).setBlocks(blocks);
				// change totalSteps
				totalSteps++;
				// change operations
				Operation newOperation = new Operation(currentCar, 'w');
				currentGameOperations.add(newOperation);
				currentOperation++;
				repaint();
			} else// unmovable
				return;
		} else {// step-- case
			if (currentGameOperations.size() > 0) {
				// change totalSteps
				totalSteps--;
				// change Operations
				Operation lastOperation = currentGameOperations.get(currentOperation - 1);
				currentGameOperations.remove(currentOperation - 1);
				currentOperation--;
				int lastCar = lastOperation.getSelectCar();
				int[] temp = new int[3];
				temp[2] = 36;
				int[] blocks = new int[3];
				blocks = allGames.get(currentGame).getCarList().get(lastCar - 1).getBlocks();
				int len;
				if (blocks[2] > 35) {
					len = 2;
				} else {
					len = 3;
				}
				int i = 0;
				while (i < len) {
					temp[i] = blocks[i] - 6;
					i++;
				}
				// chage isFree
				for (i = 0; i < len; i++) {
					isFree[blocks[i]] = true;
				}
				for (i = 0; i < len; i++) {
					isFree[temp[i]] = false;
				}
				// change carlist
				blocks = temp;
				allGames.get(currentGame).getCarList().get(lastCar - 1).setBlocks(blocks);// 修改上辆小车的blocks
				repaint();
				currentCar = 1;
				GamePanel.this.requestFocus();
			} else
				System.out.println("cannot moveback");
		}
	}

	public void move_a(Boolean plusStep) {
		if (plusStep) {// step++ case
			boolean vertical = allGames.get(currentGame).getCarList().get(currentCar - 1).isVertical();
			int[] blocks = allGames.get(currentGame).getCarList().get(currentCar - 1).getBlocks();
			int len;
			if (blocks[2] > 35) {
				len = 2;
			} else {
				len = 3;
			}
			int i;
			int[] temp = { 0, 1, 36 };
			boolean movable = !vertical && blocks[0] % 6 > 0 && isFree[blocks[0] - 1];
			if (movable) {// 小车竖直方向
				i = 0;
				while (i < len) {
					temp[i] = blocks[i] - 1;
					i++;
				}
				// change isFree
				for (i = 0; i < len; i++) {
					isFree[blocks[i]] = true;
				}
				for (i = 0; i < len; i++) {
					isFree[temp[i]] = false;
				}
				// change carlist
				blocks = temp;
				allGames.get(currentGame).getCarList().get(currentCar - 1).setBlocks(blocks);
				// change totalSteps
				totalSteps++;
				// change operations
				Operation newOperation = new Operation(currentCar, 'a');
				currentGameOperations.add(newOperation);
				currentOperation++;
				repaint();
			} else// unmovable
				return;
		} else {// step-- case
			if (currentGameOperations.size() > 0) {
				// change totalSteps
				totalSteps--;
				// change Operations
				Operation lastOperation = currentGameOperations.get(currentOperation - 1);
				currentGameOperations.remove(currentOperation - 1);
				currentOperation--;
				int lastCar = lastOperation.getSelectCar();
				int[] temp = new int[3];
				temp[2] = 36;
				int[] blocks = new int[3];
				blocks = allGames.get(currentGame).getCarList().get(lastCar - 1).getBlocks();
				int len;
				if (blocks[2] > 35) {
					len = 2;
				} else {
					len = 3;
				}
				int i = 0;
				while (i < len) {
					temp[i] = blocks[i] - 1;
					i++;
				}
				// chage isFree
				for (i = 0; i < len; i++) {
					isFree[blocks[i]] = true;
				}
				for (i = 0; i < len; i++) {
					isFree[temp[i]] = false;
				}
				// change carlist
				blocks = temp;
				allGames.get(currentGame).getCarList().get(lastCar - 1).setBlocks(blocks);// 修改上辆小车的blocks
				repaint();
				currentCar = 1;
				GamePanel.this.requestFocus();
			} else
				System.out.println("cannot moveback");
		}
	}

	public void move_s(Boolean plusStep) {
		if (plusStep) {// step++ case
			boolean vertical = allGames.get(currentGame).getCarList().get(currentCar - 1).isVertical();
			int[] blocks = allGames.get(currentGame).getCarList().get(currentCar - 1).getBlocks();
			int len;
			if (blocks[2] > 35) {
				len = 2;
			} else {
				len = 3;
			}
			int i;
			int[] temp = { 0, 1, 36 };
			boolean movable = vertical && blocks[len - 1] / 6 < 5 && isFree[blocks[len - 1] + 6];
			if (movable) {// 小车竖直方向
				i = 0;
				while (i < len) {
					temp[i] = blocks[i] + 6;
					i++;
				}
				// change isFree
				for (i = 0; i < len; i++) {
					isFree[blocks[i]] = true;
				}
				for (i = 0; i < len; i++) {
					isFree[temp[i]] = false;
				}
				// change carlist
				blocks = temp;
				allGames.get(currentGame).getCarList().get(currentCar - 1).setBlocks(blocks);
				// change totalSteps
				totalSteps++;
				// change operations
				Operation newOperation = new Operation(currentCar, 's');
				currentGameOperations.add(newOperation);
				currentOperation++;
				repaint();
			} else// unmovable
				return;
		} else {// step-- case
			if (currentGameOperations.size() > 0) {
				// change totalSteps
				totalSteps--;
				// change Operations
				Operation lastOperation = currentGameOperations.get(currentOperation - 1);
				currentGameOperations.remove(currentOperation - 1);
				currentOperation--;
				int lastCar = lastOperation.getSelectCar();
				int[] temp = new int[3];
				temp[2] = 36;
				int[] blocks = new int[3];
				blocks = allGames.get(currentGame).getCarList().get(lastCar - 1).getBlocks();
				int len;
				if (blocks[2] > 35) {
					len = 2;
				} else {
					len = 3;
				}
				int i = 0;
				while (i < len) {
					temp[i] = blocks[i] + 6;
					i++;
				}
				// chage isFree
				for (i = 0; i < len; i++) {
					isFree[blocks[i]] = true;
				}
				for (i = 0; i < len; i++) {
					isFree[temp[i]] = false;
				}
				// change carlist
				blocks = temp;
				allGames.get(currentGame).getCarList().get(lastCar - 1).setBlocks(blocks);// 修改上辆小车的blocks
				repaint();
				currentCar = 1;
				GamePanel.this.requestFocus();
			} else
				System.out.println("cannot moveback");
		}
	}

	public void move_d(Boolean plusStep) {
		if (plusStep) {// step++ case//maybe win
			boolean vertical = allGames.get(currentGame).getCarList().get(currentCar - 1).isVertical();
			int[] blocks = allGames.get(currentGame).getCarList().get(currentCar - 1).getBlocks();
			int len;
			if (blocks[2] > 35) {
				len = 2;
			} else {
				len = 3;
			}

			if (currentCar == 1 && blocks[1] == 17) {// success
				System.out.println("success!!!!!!!!!");
				callOperation(myFrame);
				watch.end();
				System.out.println("getTime" + watch.getTime());
				if(ifDiy==false){
					System.out.println("21");
					System.out.println(record.get(currentGame + ""));
					System.out.println(totalSteps);
					if (record.get(currentGame + "").compareTo(totalSteps) > 0) {
						record.replace(currentGame + "", record.get(currentGame + ""), totalSteps);
						/*************** 这里要将记录的txt清空 把新的record写进去 ***************/
						new FileReader().toFile("src/record.txt", record);
					}
				}
				this.removeMouseListener(this);
				this.removeMouseMotionListener(this);
				gotoNextGame();
				this.addMouseListener(this);
				this.addMouseMotionListener(this);
				return;
			}

			int i;
			int[] temp = { 0, 1, 36 };
			boolean movable = !vertical && blocks[len - 1] % 6 < 5 && isFree[blocks[len - 1] + 1];
			if (movable) {// 小车竖直方向
				i = 0;
				while (i < len) {
					temp[i] = blocks[i] + 1;
					i++;
				}
				// change isFree
				for (i = 0; i < len; i++) {
					isFree[blocks[i]] = true;
				}
				for (i = 0; i < len; i++) {
					isFree[temp[i]] = false;
				}
				// change carlist
				blocks = temp;
				allGames.get(currentGame).getCarList().get(currentCar - 1).setBlocks(blocks);
				// change totalSteps
				totalSteps++;
				// change operations
				Operation newOperation = new Operation(currentCar, 'd');
				currentGameOperations.add(newOperation);
				currentOperation++;
				repaint();
			} else// unmovable
				return;
		} else {// step-- case
			if (currentGameOperations.size() > 0) {
				// change totalSteps
				totalSteps--;
				// change Operations
				Operation lastOperation = currentGameOperations.get(currentOperation - 1);
				currentGameOperations.remove(currentOperation - 1);
				currentOperation--;
				int lastCar = lastOperation.getSelectCar();
				int[] temp = new int[3];
				temp[2] = 36;
				int[] blocks = new int[3];
				blocks = allGames.get(currentGame).getCarList().get(lastCar - 1).getBlocks();
				int len;
				if (blocks[2] > 35) {
					len = 2;
				} else {
					len = 3;
				}
				int i = 0;
				while (i < len) {
					temp[i] = blocks[i] + 1;
					i++;
				}
				// chage isFree
				for (i = 0; i < len; i++) {
					isFree[blocks[i]] = true;
				}
				for (i = 0; i < len; i++) {
					isFree[temp[i]] = false;
				}
				// change carlist
				blocks = temp;
				allGames.get(currentGame).getCarList().get(lastCar - 1).setBlocks(blocks);// 修改上辆小车的blocks
				repaint();
				currentCar = 1;
				GamePanel.this.requestFocus();
			} else
				System.out.println("cannot moveback");
		}
	}

	// *****************用到的函数*******************
	// //根据小车左上角坐标（x，y）和长度len（2or3），给出blocks[]
	// public int[] changeToBlocks(int x,int y,int len){
	// int x0=0;
	// int y0=0;
	// int m=100;
	// int[] blocks=new int[3];
	// return blocks;
	// }
	
	//
	public int freeNeighbours(int hostBlock,char direction){
		int num=0;
		if(direction=='w'){
			num=0;
			while(hostBlock/6>0&&isFree[hostBlock-6]){
				num++;
				hostBlock-=6;
			}		
		}
		else if(direction=='a'){
			num=0;
			while(hostBlock%6>0&&isFree[hostBlock-1]){
				num++;
				hostBlock-=1;
			}		
		}
		else if(direction=='s'){
			num=0;
			while(hostBlock/6<5&&isFree[hostBlock+6]){
				num++;
				hostBlock+=6;
			}		
		}
		else if(direction=='d'){
			num=0;
			while((hostBlock%6)<5&&isFree[hostBlock+1]){
				num++;
				hostBlock+=1;
			}	
		}
		return num;
	}

	// 将（x，y）转换成方格数(0-35 or 36，36代表不在棋盘内)
	public int changeToBlock(int x, int y) {
		int block = 36;
		int x0 = 200;
		int y0 = 10;// (x0,y0)是棋盘左上角坐标
		int m = 100;// 方格大小
		int col = (x - x0) / m;
		int line = (y - y0) / m;
		block = line * 6 + col;
		return block;
	}

	// 传入方块数，返回方块所在小车（1-6 or 0，0代表该方块空闲）
	public int pointsAtCar(int block) {
		int car = 0;
		boolean found = false;
		ArrayList<Car> tempCarList = allGames.get(currentGame).getCarList();
		int[] tempBlocks = new int[3];
		for (int i = 0; i < tempCarList.size(); i++) {
			tempBlocks = tempCarList.get(i).getBlocks();
			for (int j = 0; j < tempBlocks.length; j++) {
				if (tempBlocks[j] == block) {
					found = true;
					car = i + 1;
					return car;
				}
			}
		}
		return car;// car=0,not found
	}

	// ******************鼠标事件********************
	public void mouseDragged(MouseEvent e) {
		if (isDragging) {
			draggingPoint.setLocation(e.getX(), e.getY());
			repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	// if possible,change currentCar,startPoint,isDragging
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int block = changeToBlock(x, y);
		int car = pointsAtCar(block);
		if (car != 0) {
			currentCar = car;
			startPoint.setLocation(x, y);
			isDragging = true;
		}
	}

	public void mouseReleased(MouseEvent e) {
		boolean xminus = false;
		boolean yminus = false;
		int x = e.getX();
		int y = e.getY();
		if (isDragging) {
			int x_part = (int) (x - startPoint.getX());
			int y_part = (int) (y - startPoint.getY());// 移动的距离
			if (x_part < 0) {
				x_part = -x_part;
				xminus = true;
			}
			if (y_part < 0) {
				y_part = -y_part;
				yminus = true;
			}

			// x_part,y_part 均为正值，正负看xminus,yminus
			if (allGames.get(currentGame).getCarList().get(currentCar - 1).isVertical()) {// 可竖直移动
				int move_block = (y_part + m / 2) / m;
				if (!yminus) {// move down
					for (int i = 0; i < move_block; i++) {
						move_s(true);
					}
				} else if (yminus) {// move up
					for (int i = 0; i < move_block; i++) {
						move_w(true);
					}
				} else {// 拖动距离少于m/2
					System.out.println("no move");
				}
			} else {// 可水平移动
				int move_block = (x_part + m / 2) / m;
				if (!xminus) {// move right
					for (int i = 0; i < move_block; i++) {
						move_d(true);
					}
				} else if (xminus) {// move left
					for (int i = 0; i < move_block; i++) {
						move_a(true);
					}
				} else {// 拖动距离少于m/2
					System.out.println("no move");
				}
			}
			isDragging = false;
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
/*                   自动                   */
//	private ArrayList<Operation> getMayBeOperation(){
	void getMayBeOperation(){
		ArrayList<Operation> mayBeOperations=new ArrayList<Operation>();
		for(int i=1;i<=6;i++){
			currentCar=i;
			CarStruct carStruct=new CarStruct(allGames.get(currentGame).getCarList().get(currentCar-1));
			switch (carStruct.carCode) {
			case 1:
				if(carStruct.block%6!=0){
				if(isFree[carStruct.block-1]){
					Operation operation=new Operation();
					operation.setSelectCar(currentCar);
					operation.setPressMove('a');
					mayBeOperations.add(operation);
				}}
				if(carStruct.block%6<=2){
				if(isFree[carStruct.block+3]){
					Operation operation=new Operation();
					operation.setSelectCar(currentCar);
					operation.setPressMove('d');
					mayBeOperations.add(operation);
				}}
				break;
			case 2:	
				if(carStruct.block>=6){
				if(isFree[carStruct.block-6]){
					Operation operation=new Operation();
					operation.setSelectCar(currentCar);
					operation.setPressMove('w');
					mayBeOperations.add(operation);
				}}
				if(carStruct.block<=17){
				if(isFree[carStruct.block+18]){
					Operation operation=new Operation();
					operation.setSelectCar(currentCar);
					operation.setPressMove('s');
					mayBeOperations.add(operation);
				}	}
				break;
			case 3:
				if(carStruct.block%6!=0){
				if(isFree[carStruct.block-1]){
					Operation operation=new Operation();
					operation.setSelectCar(currentCar);
					operation.setPressMove('a');
					mayBeOperations.add(operation);
				}}
				if(carStruct.block%6<=3){
				if(isFree[carStruct.block+2]){
					Operation operation=new Operation();
					operation.setSelectCar(currentCar);
					operation.setPressMove('d');
					mayBeOperations.add(operation);
				}	}
				break;
			case 4:
				if(carStruct.block>=6){
				if(isFree[carStruct.block-6]){
					Operation operation=new Operation();
					operation.setSelectCar(currentCar);
					operation.setPressMove('w');
					mayBeOperations.add(operation);
				}}
				if(carStruct.block<=23){
				if(isFree[carStruct.block+12]){
					Operation operation=new Operation();
					operation.setSelectCar(currentCar);
					operation.setPressMove('s');
					mayBeOperations.add(operation);
				}}	
				break;
			default:
				break;
			}
		}
		for(int i=0;i<mayBeOperations.size();i++){
			System.out.println(mayBeOperations.get(i).getSelectCar()+"  "+mayBeOperations.get(i).getPressMove());
		}
	}
}
