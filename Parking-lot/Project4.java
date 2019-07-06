import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

@SuppressWarnings("serial")
public class Project4 extends JFrame implements MouseInputListener, MouseMotionListener,
ActionListener {

	private board currentState; //how does the board look like
	private Point mouseOrigin; //which car is player moving
	private car beforeMove; //where was a car before the player has moved it
	private int startDir = 0; //keeps track of bidirectional dudes
	private int level = 1; //what level is the player on
	private JPanel playPanel; //current level

	private final int CELLSIZE = 75; //size of the game, each square is 50x50
	private final int NUMLEVELS = 12; //number of levels

	private Project4() {
		super("Paking lot project 4");
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		JMenuBar menu = new JMenuBar();
		//levels
		JMenu levels = new JMenu("levels");
		JMenuItem[] levelArray = new JMenuItem[NUMLEVELS];
		for (int i = 0; i < NUMLEVELS; i++) {
			levelArray[i] = new JMenuItem(String.valueOf(i + 1));
			levelArray[i].addActionListener(this);
			levels.add(levelArray[i]);
		}
		menu.add(levels);
		menu.add(createOptions()); //adding options
		this.setJMenuBar(menu);
		generateLevel("1");
		setEnabled(true);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	//create options menu
	private JMenu createOptions() {
		JMenu options = new JMenu("options");
		//reset button
		JMenuItem resetButton = new JMenuItem("reset");
		resetButton.addActionListener(this);
		JMenuItem solveButton = new JMenuItem("solve");
		solveButton.addActionListener(this);
		JMenuItem exitButton = new JMenuItem("exit");
		exitButton.addActionListener(this);
		options.add(resetButton);
		options.add(solveButton);
		options.add(exitButton);
		return options;
	}


	private void generateLevel(String lev) {
		//all of the levels will have to be named
		//where X is the level to play
		String forJP = "proj4-level" + lev+".txt";
		makeGuiForGame(forJP);
		//makeGuiForGame("proj3g.txt");//for checking the input file reading
	}

	private void makeGuiForGame(String s) {
		Container container = getContentPane();
		ReadFile f = new ReadFile();
		f.setPuzzle(s);
		currentState = f.getBoard();
		//startSolver(); //start solving that board
		int numPixelsX = currentState.getLength() * CELLSIZE;
		int numPixelsY = currentState.getWidth() * CELLSIZE;
		JPanel playPanel = new JPanel(null);
		playPanel.setBounds(0, 0, numPixelsX, numPixelsY);

		//add the buttons at each position
		int size = currentState.getCars().size();
		for (int i = 0; i < size; i++) {
			car c = currentState.getCars().get(i);
			//make the rectangle out of each car
			c.setRec(CELLSIZE);

			//add it to the board and place it as a rectangle
			playPanel.add(c);

			//make sure it is at a given position
			c.setBounds(c.getRec());
			//SET COLORS TO CARS MOVING IN DIFFERENT DIRECTIONS
			if (c.isVertical()) c.setForeground(Color.RED);
			if (c.isHorizontal()) c.setForeground(Color.BLUE);
			if (c.isVertical() && c.isHorizontal()) c.setForeground(Color.GREEN);
			c.addMouseMotionListener(this);
			c.addMouseListener(this);
		}
		playPanel.setSize(numPixelsX + 10, numPixelsY + 10); //magic constants :P
		setSize(numPixelsX + 10, numPixelsY + 50); //another magic constant! :P
		this.playPanel = playPanel;
		container.add(playPanel, BorderLayout.CENTER);
		container.repaint();
	}

	//somethig along the lines of message pop up asking if
	//player wants to move on to the next level.
	//if yes, load next level up, else exit
	private void hasPlayerWon() {
		//if won, ask to go to next level
		if (goalReached()) {
			//check best score
			if (level != NUMLEVELS) {
				//make a pop up asking to move on to next level
				//or exit the game
				int reply = JOptionPane.showConfirmDialog(null, "Level Passed, continue?",
						"Next Level?", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					Container container = getContentPane();
					container.remove(playPanel);
					level++;
					generateLevel(String.valueOf(level));
					container.repaint();
				} else {
					System.exit(0);
				}
			}
			//if level is 10 do something else
			else {
				//make pop up
				//congratulate and exit
				JOptionPane.showMessageDialog(null, "You won the last level, Exiting!");
				System.exit(0);
			}
		}

	}

	//checks current board if the Z piece has made it to the right side
	private boolean goalReached() {
		//if one of the positions in board length - 1, than player has won
		if (currentState.getCars().get(0).getPositions().lastElement().x + 1 == currentState.getLength()) return true;
		return false;
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Project4 g = new Project4();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//nothing
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//nothing
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//nothing
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseOrigin = e.getPoint();
		car c = (car) e.getSource();
		beforeMove = new car(c.getPositions(), c.isHorizontal(),
				c.isVertical(), c.getCarName());
		startDir = 0;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Move m = null;
		//start a thread to solve a puzzle
		char name = beforeMove.getCarName();
		car c = null;
		for (int i = 0; i < currentState.getCars().size(); i++) {
			if (name == currentState.getCars().get(i).getCarName()) {
				c = currentState.getCars().get(i);
				break;
			}
		}
		//if y changed vertical
		int movY = c.getPositions().get(0).y - beforeMove.getPositions().get(0).y;
		if (movY < 0) {
			m = new Move(name, -1, -1 * movY, true);
		} else if (movY > 0) {
			//moved down
			m = new Move(name, 1, movY, true);
		}
		//if x changed, horizontal
		int movX = c.getPositions().get(0).x - beforeMove.getPositions().get(0).x;
		if (movX < 0) {
			m = new Move(name, -1, -1 * movX, false);
		} else if (movX > 0) {
			//moved right
			m = new Move(name, 1, movX, false);
		}
		//if no change in x or y, no move
		if (movY != 0 || movX != 0) {
			currentState.addMove(m);
		}
		startDir = 0;
		hasPlayerWon();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point current = e.getPoint();
		car c = (car) e.getSource();
		if (startDir == 0) {
			if (Math.abs(current.y - mouseOrigin.y) >
					Math.abs(current.x - mouseOrigin.x))
				startDir = 1;
			else startDir = -1;
		}
		if (c.isVertical()) {
			if (startDir == 1 || !c.isHorizontal()) {
				int movedV = current.y - mouseOrigin.y;
				//check if legal move
				movedV = movedV / CELLSIZE;
				//	System.out.println("Mouse moved in vertical: " + movedV);
				if (checkMoveCar(c, movedV, true)) { //legal move
					//repaint the car
					//			System.out.println("Possible to move the car!");
					paintCar(c, movedV, true);
				}
			}
		}
		if (c.isHorizontal()) {
			if (startDir == -1 || !c.isVertical()) {

				int movedH = current.x - mouseOrigin.x;

				movedH = movedH / CELLSIZE;
				if (checkMoveCar(c, movedH, false)) { //legal move
					//repaint the car
					paintCar(c, movedH, false);
				}
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//nothing
	}

	//make a new rectangle for a car
	//assuming a legal move here
	private void paintCar(car c, int numS, boolean vert) {
		Vector<Position> newP = new Vector<>();
		if (vert) {
			for (Position oldP : c.getPositions()) {
				Position p = new Position(oldP.x, oldP.y + numS);
				newP.add(p);
			}
		} else {
			for (Position oldP : c.getPositions()) {
				Position p = new Position(oldP.x + numS, oldP.y);
				newP.add(p);
			}
		}
		//paint the car on the screen
		c.setNewPosition(newP);
		c.setRec(CELLSIZE);
		c.setBounds(c.getRec());
	}

	//return true if legal move for the car
	private boolean checkMoveCar(car c, int numS, boolean Vert) {
		if (Vert) {
			for (Position pos : c.getPositions()) {
				Position p = new Position(pos.x, pos.y + numS);
				if (!((currentState.isEmptyAt(p) || c.hasPosition(p)) &&
						(p.y >= 0 && p.y < currentState.getWidth())))
					return false;
			}
		} else {
			for (Position pos : c.getPositions()) {
				Position p = new Position(pos.x + numS, pos.y);
				if (!((currentState.isEmptyAt(p) || c.hasPosition(p)) &&
						(p.x >= 0 && p.x < currentState.getLength())))
					return false;
			}
		}
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem mitem = (JMenuItem) e.getSource();
		Container container = getContentPane();
		if (mitem.getText().equals("reset")) {
			container.remove(playPanel);
			generateLevel(String.valueOf(level));
			container.repaint();
		} else if (mitem.getText().equals("solve")) {
			startSolver();
		} else if (mitem.getText().equals("exit")) {
			System.exit(0);
		} else {
			level = Integer.parseInt(mitem.getText());
			container.remove(playPanel);
			generateLevel(mitem.getText());
		}
	}

	private void startSolver() {
		Solver solution = new Solver(currentState);
		Vector<Move> movesSol = solution.Solve();
		if (solution.SolveOrNot()) {
			String answer = "";
			for (Move m : movesSol)
				answer += m.toString() + "\n";
			JOptionPane.showMessageDialog(null, answer);

		} else {
			String answer = "";
			for (Move m : movesSol)
				answer += m.toString() + "\n";
			JOptionPane.showMessageDialog(null, "There is no solution" +"\n"+answer);

		}
	}
}