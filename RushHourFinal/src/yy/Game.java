package yy;
import java.util.ArrayList;

import javax.swing.text.Position;


public class Game {
	public ArrayList<Car> carList = new ArrayList<Car>();

	public ArrayList<Car> getCarList() {
		return carList;
	}

	public void setCars(int position[][]) {//position:6*5
		int[] blocks = new int[3];
		for (int i = 0; i < 6; i++) {//set each car
			Car theCar = new Car();
			if (position[i][0] ==1) {//visible
				theCar.setVisible(true);
			} 
			else if(position[i][0]==0){//invisible
				theCar.setVisible(false);// blocks
			}
			if(position[i][1]==0){
				theCar.setVertical(false);
			} 
			else if(position[i][1]==1){
				theCar.setVertical(true);
			}
			blocks[0]=position[i][2];
			blocks[1]=position[i][3];
			blocks[2]=position[i][4];
			theCar.setBlocks(blocks);
			carList.add(theCar);
		}
	}

	public void setCarList(ArrayList<Car> carList) {//carList已被初始化时调用
		//this.carList = (ArrayList<Car>) carList.clone();
		this.carList.clear();
		for(int i=0;i<carList.size();i++){
			Car tempCar=new Car();
			tempCar.setBlocks(carList.get(i).getBlocks());
			tempCar.setVertical(carList.get(i).isVertical());
			tempCar.setVisible(carList.get(i).isVisible());
			this.carList.add(tempCar);
		}
		
	}
//	public void printCarList(){
//		for(int i=0;i<6;i++){
//				System.out.println(this.carList.get(i).getBlocks()[0]+" "+this.carList.get(i).getBlocks()[1]
//						+" "+this.carList.get(i).getBlocks()[2]);
//		}
//	}

}
