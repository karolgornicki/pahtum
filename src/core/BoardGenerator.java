package core;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import core.Board;

public class BoardGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Board[] boardCollection = new Board[200];
		//Generate boards (9 dead fields each, no guarantee that in terms of 
		//duplicates).
		for(int i = 0; i < 200; ++i) {
			boardCollection[i] = new Board(1, true, 3);
		}

		//Save to the file.
		try {
			FileOutputStream fos = new FileOutputStream("200_boards_3.sav");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(boardCollection);
			oos.close();
		} catch(Exception e) {
			System.err.println("Error occured during saving.");
			e.printStackTrace();
		}
	}

}
