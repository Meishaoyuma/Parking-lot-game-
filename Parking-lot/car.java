
/*
 * this class is created to store the information of each car
 * It has Vector<Position> positions to store the position a car has in a board
 * It has boolean vert, horiz to determine if this car can move vert or horiz
 * It has boolean playerBlock to see if this is the target car Z
 * It has a char name to store the car's name
 * It has Rectangle rec to later paint it on the board, it contains the dimensions of this car
 */

import java.awt.Rectangle;
import java.util.Vector;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class car extends JButton{
	private Vector<Position> positions;
	private boolean vert, horiz;
	private char name;
	private Rectangle rec;
	//contstructor
	car(Vector<Position> pos, boolean h, boolean v, char n) {
		positions = pos;
		vert = v;
		horiz = h;
		name = n;
		super.setText(String.valueOf(name));
	}
	
	Rectangle getRec(){
		return rec;
	}
	//to set the rectangle for each car 
	void setRec(int s) {
		int startX =1000000, startY=1000000, maxX=-1, maxY=-1;
		for(Position c : positions) {
			//check x
			if(c.x <= startX)
				startX = c.x;
			if(c.x >= maxX)
				maxX = c.x;
			//check y
			if (c.y <= startY)
				startY = c.y;
			if (c.y >= maxY)
				maxY = c.y;
		}
		int length = maxX-startX + 1;
		int height = maxY-startY + 1;
		
		rec = new Rectangle(s*startX, s*startY, s*length, s*height);
	}

	Vector<Position> getPositions() {
		return positions;
	}
	
	boolean hasPosition(Position p) {
		for (Position pos : positions) {
			if (pos.x == p.x &&pos.y == p.y) return true;
		}
		return false;
	}
	// to see if a given piece is in a Position p
	boolean isInPosition(Position p) {
		for(Position pos : positions)
			if(p.x == pos.x &&p.y == pos.y)
				return  true;
		return false;
	}
	
	void setNewPosition(Vector<Position> p) {
		positions = p;
	}
	
	boolean isVertical() { return vert; }
	
	boolean isHorizontal() {
		return horiz;
	}

	char getCarName() {
		return name;
	}

	void setName() {
		name = '#';
		super.setText(String.valueOf(name));
		
	}

	public String toString() {
		String ret = name + " ";
		if (vert)
			ret += "vertical ";
		if (horiz)
			ret += "horizontal";
		return ret;
	}
}