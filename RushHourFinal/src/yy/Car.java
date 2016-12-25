package yy;
import java.awt.Graphics;


public class Car implements Cloneable{
	private boolean visible;
	private boolean vertical;
	private int[]blocks=new int[3];//3位
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public void setVertical(boolean vertical) {
		this.vertical = vertical;
	}
	public void setBlocks(int[] blocks) {
		this.blocks[0]=blocks[0];
		this.blocks[1]=blocks[1];
		this.blocks[2]=blocks[2];
//		this.blocks=blocks;
	}
	
	public boolean isVertical() {
		return vertical;
	}
	public int[] getBlocks() {
		return blocks;
	}
	
	protected Car clone(){
		Car clone=new Car();//?
		try {
			clone=(Car)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
	
//	public Car myClone(Car orgCar){
//		Car cloneCar=new Car();
//		cloneCar=orgCar;
//		return orgCar;	
//	}

}
