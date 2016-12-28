package yy;

public class CarStruct {
	public int carCode;
	public int block;
	public CarStruct(Car car) {
		// TODO Auto-generated constructor stub
		if(car.isVertical()){
			if(car.getBlocks()[2]==36){
				carCode=4;
				block=car.getBlocks()[0];
			}else{
				carCode=2;
				block=car.getBlocks()[0];
			}
		}else{
			if(car.getBlocks()[2]==36){
				carCode=3;
				block=car.getBlocks()[0];
			}else{
				carCode=1;
				block=car.getBlocks()[0];
			}
			
		}
	}
	
}
