
/*
 * This class is created for solve and hint of this puzzle
 * It has a board boardToSolve which is the board read from ReadFile
 * It has a Queue<board> bQ for bfs and store the board 
 * It has a Map<String, String> previous to store the string which is converted from createHash(board)
 * so it will make sure we don't add a same board to bQ
 * It has an int to store the target car index so it can decide if this car reaches the targeted position
 * It also has information for car moving directions
 */

import java.util.*;
class Solver {
	private board boardToSolve; //board to solve
	private Queue<board> bQ; //Q of the boards to check
	private Map<String, String> previous; //hashmap for the Q to see what was checked
	private boolean goalreached;

	//constructor of Solver
	Solver(board BoardToSolve) {
		boardToSolve = cloneBoard(BoardToSolve);
		bQ = new LinkedList<>();
		previous = new HashMap<>();
		//findZ();
	}
	//Run BFS algorithm given by the professor
	Vector<Move> Solve() {
		int numMoves = 50000;
		int steps = 0;
		board shortSolved  = cloneBoard(boardToSolve);
		makeQ(createHash(boardToSolve), null, boardToSolve);//put the board I'm supposed to solve into the Q
		while (!bQ.isEmpty()&& steps<numMoves) {//while Q is not Empty
			board b = bQ.remove();
			steps ++;
			if (goalReached(b)) {
					shortSolved = b;
				break;
			}
			exploreMoves(b);//continue checking for shortest path
		}
		if(steps==numMoves){
			shortSolved=bQ.remove();
		}
		Vector<Move> moves = new Vector<>();//return the moves, if not solvable, return empty vector
		for (int i=boardToSolve.numMoves(); i < shortSolved.numMoves(); i++) {
			moves.add(shortSolved.getMoves().get(i));
		}
		return moves;
	}

	//check for possible moves, if any, add to Q
	private void exploreMoves(board b) {
		Vector<car> cars = b.getCars();//check car by car if there is a move to be made
		for(int i=0; i < cars.size(); i++) {
			VerticalMove(b,i,-1);
			VerticalMove(b,i,1);
			HorizontalMove(b,i,1);
			HorizontalMove(b,i,-1);
		}		
	}
	
	//check if car is movable in a certain direction on a given board	
	private boolean checkIfMovable(board b, int index, int dir, boolean Vertical, int s) {
		car CarToCheck = b.getCars().get(index);
		int numS = s*dir;
		if (Vertical) {
			if (!(CarToCheck.isVertical())) return false;
			for (Position pos: CarToCheck.getPositions()) {
				Position p = new Position(pos.x,pos.y+numS);
				//either it is empty, or the car that I'm moving is there now
				if (!((b.isEmptyAt(p) || CarToCheck.hasPosition(p)) &&(p.y>= 0 && p.y < b.getWidth())))
					return false;
			}
			return true;
		}
		else {		//same loop as above, but in horizontal
			if (!(CarToCheck.isHorizontal())) return false;
			for (Position pos: CarToCheck.getPositions()) {
				Position p = new Position(pos.x+numS,pos.y);
				if (!((b.isEmptyAt(p) || CarToCheck.hasPosition(p))&&(p.x >= 0 && p.x < b.getLength())))
					return false;
			}
			return true;
		}	
	}
		// make a vertical move for a car
	private void VerticalMove(board b, int i, int dir)
	{
		int j=0;
		String hash = createHash(b);//create this hash value
		while (checkIfMovable(b, i, dir, true, ++j)) {//a movable vertical piece
			board cloB = cloneBoard(b);//add new board to the Q after new car position has been made
			car carToMove = cloB.getCars().get(i);
			//Vector<Position> pos=carToMove.getPositions();//set new cars location
			for (Position p : carToMove.getPositions()) {//update the positions of this car
				p.y+=j*dir;
			}	
			Move newMove = new Move(carToMove.getCarName(), dir, j, true);//add the move to the list
			cloB.addMove(newMove);
			makeQ(createHash(cloB), hash, cloB);//add to the Q
		}
	}
	// make a horizontal move for a car
	private void HorizontalMove(board b, int i, int dir)
	{
		int j=0;
		String hash = createHash(b);
		while (checkIfMovable(b, i, dir, false,++j)) {	//a movable horizontal piece
			board cloB = cloneBoard(b);//add new board to the Q after new car position has been made
			car carToMove = cloB.getCars().get(i);
			//Vector<Position> pos=carToMove.getPositions();//set new cars location
			for (Position p : carToMove.getPositions()) {
				p.x+=j*dir;
			}	
			Move newMove = new Move(carToMove.getCarName(), dir, j, false);			//add the move to the list
			cloB.addMove(newMove);
			makeQ(createHash(cloB), hash, cloB);//add to the Q
		}
	}
	
	//make a copy of the board
	private board cloneBoard(board b) {
		//check each car if it is movable if it is add it to the Q
		Vector<car> cars = new Vector<>();
		for (int i=0; i < b.getCars().size(); i++) {		
			car cpcar = b.getCars().get(i);//copy cars
			Vector<Position> cpm = new Vector<>();
			for (int j=0; j < cpcar.getPositions().size(); j++) {			
				cpm.add(new Position(cpcar.getPositions().get(j).x,//copy the moves
						cpcar.getPositions().get(j).y));//previously is .x
			}
			cars.add(new car(cpm, cpcar.isHorizontal(), cpcar.isVertical(),//previously is isValid()
					 cpcar.getCarName()));
		}		
		Vector<Move> mvs = new Vector<>();//copy moves
		for (int i = 0; i < b.getMoves().size(); i++) {
			Move mv = new Move(b.getMoves().get(i).getCarName(),
					b.getMoves().get(i).getDir(),
					b.getMoves().get(i).getnumS(),
					b.getMoves().get(i).getVertical());
			mvs.add(mv);
		}
		return new board(b.getLength(), b.getWidth(), cars, mvs);
	}
	
	//is finalCar touching the right border?
	private boolean goalReached(board b) {
			if(b.getCars().get(0).getPositions().lastElement().x+1==boardToSolve.getLength()){
				goalreached= true;
				return true;
			}
		return false;
	}

	boolean SolveOrNot(){return goalreached; }

	//adds the board to a Q if it never has been added before
	private void makeQ(String next, String prev, board b) {
      if (!previous.containsKey(next)) {
          previous.put(next,prev);
          bQ.add(b);
      }
    }
	//creates possibly long hash of the map
	private String createHash(board b) {
		String retVal = "";
		int l = b.getLength();
		int w = b.getWidth();
		boolean added = false;
		for (int i =0; i < l; i++) {
			for (int j=0; j < w; j++)	{
				Position p= new Position(i,j);
				for (car c : b.getCars()) {
					if (c.isInPosition(p)) {
						retVal += c.getCarName();
						added = true;
						break;
					}
				}
				if (!added)
					retVal += '.';
				added = false;
			}
		}
		return retVal;
	}
}