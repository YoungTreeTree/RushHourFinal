package yy;

import java.awt.Graphics;
import java.awt.Image;
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

public class GamePanel extends JPanel implements KeyListener,MouseListener,MouseMotionListener{
	private static Watch watch = new Watch();
	private static HashMap<String, Long> record = new HashMap<String, Long>();
	private boolean[] isFree = new boolean[36];
	private int currentCar = 1;// 1,2,3,4,5,6
	private int currentGame = 0;// firstly 0
	private int currentOperation = 0;
	private ArrayList<Game> allGames = new ArrayList<Game>();
	private ArrayList<Operation> currentGameOperations = new ArrayList<Operation>();
	public ArrayList<Car> initialCars = new ArrayList<Car>();// 该局初始的carList
	public static int totalSteps = 0;

	public void printCarList(ArrayList<Car> carList) {
		for (int i = 0; i < 6; i++) {
			System.out.println(carList.get(i).getBlocks()[0] + " " + carList.get(i).getBlocks()[1] + " "
					+ carList.get(i).getBlocks()[2]);
		}
	}

	public void initialize() {
		//******监听器暂时加在这里*************
		addMouseListener(this);
		addMouseMotionListener(this);
		/**********
		 * 这里需要一个函数把txt里面的记录放进来********* 新写一个类 参数是文件的路径 String path和
		 * HashMap<String, Long> record
		 */
		for (int i = 0; i < 2; i++) {
			char ch = (char) (i + '0');
			record.put(ch + "", (long) 999999);
			// System.out.println(ch+""+" "+record.get(ch+""));
		}

		watch.start();
		currentGame = 0;
		totalSteps = 0;
		String fileName = "src/CarPosition.txt";
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

		g.drawString("steps:" + totalSteps, 100, 100);
		drawTime(g);
		drawCars(allGames.get(currentGame).getCarList(), g);
	}

	/***************** 画小车 *****************/
	public void drawCars(ArrayList<Car> cars, Graphics g) {// draw cars by
															// ArrayList<cCar>
		System.out.println("drawing cars:");
		printCarList(cars);
		for (int i = 0; i < 6; i++) {
			Icon icon = new ImageIcon("src/abc.png");// ?why
			Image image = Toolkit.getDefaultToolkit().getImage("src/abc.png");
			boolean vertical = cars.get(i).isVertical();
			boolean visible = cars.get(i).isVisible();
			int[] blocks = cars.get(i).getBlocks();
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
		System.out.println("keyyyyyyyyy");
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
			System.out.println("press wasd");
			switch (KeyChar) {
			case 'w':move_w(true);
			    break;
			case 'a':move_a(true);
		        break;
			case 's':move_s(true);
	  	        break;
			case 'd':move_d(true);
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
	public void startNewGame() {
		// TODO Auto-generated method stub
		initialize();
	}

	public void callRecord(JFrame jFrameP) {
		Mydialog mydialog = new Mydialog(jFrameP);
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
		initialize();
	}

	public void undo() {
     	if (currentGameOperations.size() > 0) {
			Operation lastOperation = currentGameOperations.get(currentOperation - 1);
			int lastCar = lastOperation.getSelectCar();
			char lastMove = lastOperation.getPressMove();
			switch (lastMove) {
			case 'w':move_s(false);
				break;
			case 'a':move_d(false);
				break;
			case 's':move_w(false);
				break;
			case 'd':move_a(false);
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
		System.out.println("initial list1:");
		printCarList(initialCars);
		// ArrayList<Car> tempCarList=new ArrayList<Car>();
		// tempCarList=allGames.get(currentGame).
		allGames.get(currentGame).setCarList(initialCars);
		for (int i = 0; i < 36; i++) {
			isFree[i] = true;
		}
		System.out.println("initial list2:");
		printCarList(initialCars);
		System.out.println("the list:");
		printCarList(allGames.get(currentGame).getCarList());
		currentOperation = 0;
		currentGameOperations.clear();
		repaint();
		System.out.println("reset over");
		watch.start();
		GamePanel.this.requestFocus();
	}

	// 小车移动函数 wasd
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
				/***         **/
				watch.end();
				System.out.println("getTime" + watch.getTime());
				long thisRecord = watch.getTime();
				if (record.get(currentGame + "").compareTo(thisRecord) > 0) {
					record.replace(currentGame + "", record.get(currentGame + ""), thisRecord);
					/*************** 这里要将记录的txt清空 把新的record写进去 ***************/
				}
				gotoNextGame();
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
	
//*****************用到的函数*******************
//根据小车左上角坐标（x，y）和长度len（2or3），给出blocks[]
	public int[] changeToBlocks(int x,int y,int len){
		int[] blocks=new int[3];
		return blocks;	
	}
//（依据当前小车位置(carlist)）判断(x,y)所在位置是否空闲
	public boolean isFreeLocation(int x,int y){
		boolean free=true;
		return free;
	}
//传入方块数，返回方块所在小车（1-6 or 0，0代表该方块空闲）
	public int pointsAtCar(int block){
		int car=0;
		return car;
	}
//******************鼠标事件********************
	public void mouseDragged(MouseEvent e) {
		int x=e.getX();
		System.out.println("dragged:"+x);
	}

	public void mouseMoved(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
	
	}
