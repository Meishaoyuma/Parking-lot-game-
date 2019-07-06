/*
 * This contains the information a board has
 * It has int length and width to decide the dimension of this puzzle
 * It has Vector<car> cars to store all the cars in a board
 * It has Vector<Move> movesDone to store all the Moves done so far
 */

import java.util.Vector;

class board {
	private int length;
	private int width;
	private Vector<car> cars;
	private Vector<Move> movesDone;
	
	//constructor of board
	board(int x, int y, Vector<car> w, Vector<Move> m) {
		length = x;
		width = y;
		cars = w;
		movesDone = m;
	}
	//constructor of board
	board(int x, int y, Vector<car> w) {
		length = x;
		width = y;
		cars = w;
		movesDone = new Vector<>();
	}
	
	Vector<Move> getMoves() {
		return movesDone;
	}
	
	boolean isEmptyAt(Position p) {
		for (car c : cars) {
			if(c.isInPosition(p)) return false;
		}
		return true;
	}
	
	void addMove(Move m) {
		movesDone.add(m);
	}
	
	int numMoves() {
		return movesDone.size();
	}
	
	Vector<car> getCars() {
		return cars;
	}
	
	int getLength() {
		return length;
	}
	
	int getWidth() {
		return width;
	}
}