
/*
 * This is a class created to store all the moves a board has
 * It includes char as carName, int direction to decide up, down, left, right
 * int numSpaces to decide how many space it contains in one move
 * boolean Vertical to see if it can move vertically
 */

public class Move {
	private char carName;
	private int direction;
	private int numSpaces;
	private boolean Vertical;
	
	Move(char n, int dir, int numS, boolean v) {
		carName = n;
		direction = dir;
		Vertical = v;
		numSpaces = numS;
	}
	
	boolean getVertical() {
		return Vertical;
	}
	
	char getCarName() {
		return carName;
	}
	int getDir() {
		return direction;
	}
	int getnumS() {
		return numSpaces;
	}
	public String toString() {
		String ret = "Car: " + carName + " Steps: " +numSpaces+
				" direction: ";
		if (Vertical) {
			if (direction==-1) ret+= "up";
			else ret+= "down";
		}
		else {
			if (direction==1) ret+="right";
			else ret+= "left";
		}
		return ret;
	}
}