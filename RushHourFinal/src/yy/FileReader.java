package yy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.omg.CORBA.PUBLIC_MEMBER;

public class FileReader {// 默认6辆小车
	public void writeGame(String fileName, Game theGame) {// write to file
		Car tempCar = new Car();
		String vertical = " ";
		String visible = " ";
		int[] blocks = new int[3];
		File file = new File(fileName);
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			fw = new FileWriter(file);
			writer = new BufferedWriter(fw);
			for (int i = 0; i < 6; i++) {// 每一行，6行为一局
				tempCar = theGame.getCarList().get(i);
				if (tempCar.isVisible()) {
					visible="1";
				} else {
					visible="0";
				}
				if (tempCar.isVertical()) {
					vertical="1";
				} else {
					vertical="0";
				}
				blocks=tempCar.getBlocks();
				//写进文件
				writer.write(visible);
				writer.write(" ");
				writer.write(vertical);
				writer.write(" ");
				writer.write(blocks[0]+"");
				writer.write(" ");
				writer.write(blocks[1]+"");
				writer.write(" ");
				writer.write(blocks[2]+"");
				writer.write(" ");
				writer.newLine();
			}
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				writer.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void toFile(String fileName, HashMap<String, Integer> hashMap) {
		Object key, value;
		Iterator iter = hashMap.entrySet().iterator();
		File file = new File(fileName);
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			fw = new FileWriter(file);
			writer = new BufferedWriter(fw);
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				key = entry.getKey();
				value = entry.getValue();
				writer.write(key.toString());
				writer.write(" ");
				writer.write(value.toString());
				writer.newLine();// 换行
			}
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public HashMap<String, Integer> fromFile(String fileName) throws IOException {
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		hashMap.clear();
		String key;
		Integer value, len;
		FileInputStream fileInputStream = new FileInputStream(fileName);
		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
		BufferedReader br = new BufferedReader(inputStreamReader);
		String lineString = br.readLine();
		while (lineString != null) {
			String[] arrayString = lineString.split(" ");
			len = arrayString.length;
			if (len == 2) {
				key = arrayString[0];
				value = Integer.parseInt(arrayString[1]);
				hashMap.put(key, value);
			} else {
				System.out.println("File Reader Error!");
			}
			lineString = br.readLine();
		}
		br.close();
		inputStreamReader.close();
		fileInputStream.close();
		return hashMap;
	}

	public static void main(String[] args) throws IOException {
		// HashMap<String, Integer> testHashMap = new HashMap<String,
		// Integer>();
		// //toflle
		// testHashMap.put("1", 10);
		// testHashMap.put("2", 20);
		// testHashMap.put("3", 30);
		// new FileReader().toFile("src/HighestScore.txt",testHashMap);

		// fromfile
		// testHashMap = new FileReader().fromFile("src/HighestScore.txt");
		// String key;
		// Integer value;
		// Iterator iterator = testHashMap.entrySet().iterator();
		// while (iterator.hasNext()) {
		// Map.Entry entry = (Map.Entry) iterator.next();
		// key = (String) entry.getKey();
		// value = (Integer) entry.getValue();
		// System.out.println(key + " " + value);
		Game tempGame = new Game();
		ArrayList<Car> tempCarList=new ArrayList<Car>();
		int[] tempBlock = { 1, 2, 3 };
		for (int i = 0; i < 6; i++) {
			Car tempCar = new Car();
			tempCar.setVisible(true);
			tempCar.setVertical(false);
			tempCar.setBlocks(tempBlock);
			tempCarList.add(tempCar);
		}
		tempGame.setCarList(tempCarList);
//		for (int i = 0; i < 6; i++) {
//			Car tempCar = tempGame.getCarList().get(i);
//			System.out.println(tempCar.isVisible());
//			System.out.println(tempCar.isVertical());
//			System.out.println(tempCar.getBlocks()[0]);
//			System.out.println(tempCar.getBlocks()[1]);
//			System.out.println(tempCar.getBlocks()[2]);
//			System.out.println("");
//		}
		new FileReader().writeGame("src/userGame.txt", tempGame);
	}
}
