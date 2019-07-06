
/*
 * This class is created to store the position information which contains two int x, y
 * They correspond to the actual position on the board
 */

public class Position {
	int x;
	int y;
	
	Position(int xPos, int yPos) {
		x = xPos;
		y = yPos;
	}
	public String toString() {
		return x + "," + y;
	}
}