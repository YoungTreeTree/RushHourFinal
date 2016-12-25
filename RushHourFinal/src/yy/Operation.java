package yy;

public class Operation {
	private int selectCar;
	private char pressMove;
	public Operation(){
	}
	public Operation(int selectCar,char pressMove){
		this.selectCar=selectCar;
		this.pressMove=pressMove;
	}
	public int getSelectCar() {
		return selectCar;
	}
	public void setSelectCar(int selectCar) {
		this.selectCar = selectCar;
	}
	public char getPressMove() {
		return pressMove;
	}
	public void setPressMove(char pressMove) {
		this.pressMove = pressMove;
	}

}
