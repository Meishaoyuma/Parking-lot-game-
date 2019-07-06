/*
 This class is created to read the file from the a given .txt and store the information
 to a board
 */


import java.io.*;
import java.util.*;

import javax.swing.*;


class ReadFile
{
	private int length;
	private int width;
	private Vector<car> cars = new Vector<car>();
	private char name ='@';


	/***ADD THIS**/
	board getBoard() {
		return new board(length, width, cars);
	}
	
	/*
	 * This method takes the input file and get the proper information of the board
	 * It will store the length and length of the board
	 * and Each car information
	 */
	void setPuzzle(String s)
	{
		try
		{
			File file = new File(s);
			if (!file.exists())
				file.createNewFile();
			FileInputStream puzzle = new FileInputStream(file);
			DataInputStream in = new DataInputStream(puzzle);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	        String line;
	        String firstLine;

	        firstLine = reader.readLine();
	        String[] dimension = firstLine.split("[ ]+");
	        length = Integer.parseInt(dimension[0]);
	        width =Integer.parseInt(dimension[1]);
		        if (length <=0|| width <=0)//when the first line has wrong format
	        {
	        	System.err.println("Error: not a valid input file");
	        	System.exit(0);
	        }
	        while ((line = reader.readLine())!=null)
	        {
				String[] carPos;
	        	carPos = line.split("[ ]+");	        	
	        	car temp = null;
	        	Vector<Position> p = new Vector<>();
	        	int x = Integer.parseInt(carPos[0]);
	        	int y = Integer.parseInt(carPos[1]);
	        	Position tempP = new Position(y-1,x-1);
	        	p.add(tempP);
	        	int a = Integer.parseInt(carPos[2]);
	        	int b = Integer.parseInt(carPos[3]);
	        	for (int i = 1; i < b; i++) {//length > 1
	        		Position addp = new Position(y-1,x-1+i);
	        		p.add(addp);
	        	}
	        	for (int i = 1; i < a; i++) { //length > 1
	        		Position addp = new Position(y-1+i,x-1);
	        		p.add(addp);
	        	}	        	
	        	String move = carPos[4];
	        	if (move.equals("b")) /*CHANGED***/
	        		temp = new car(p,true, true,name);
	        	if (move.equals("h"))
	        		temp = new car(p,true,false,name);
	        	if (move.equals("v"))
	        		temp = new car(p,false,true,name);
	        	exceptionHandler(temp);
	        	//printTest(temp);
	        	if (name=='Z')//this deals with the situation where we have more than 25 cars
	        		name+=7;
	        	else
	        		name++;
	        	//count++;
	        }
	        if (cars.size()!=0)
	        {
	        	car target = cars.get(0);
	        	target.setName();
	        	//target.setPlayer();
	        	//printTest(target);
	        }
	        in.close();
	        puzzle.close();
	        reader.close();
		}
		catch (Exception e)
		{
			System.err.println("Error: "+e.getMessage());
		}	
	}
	
	private void exceptionHandler(car temp)
	{
		cars.add(temp);
		Vector<Position> p = temp.getPositions();
		int counter =0;
		while (counter<cars.size()-1)
		{
			car car1 = cars.get(counter);
			for (int i=0;i<p.size();i++)
				if (car1.hasPosition(p.get(i)))
				{
					JOptionPane.showMessageDialog(null,"The "+i+"th piece is overlapping with an existing piece","Error",JOptionPane.PLAIN_MESSAGE);
					cars.remove(temp);
					name--;
				}
			counter++;
		}
		for (int i=0;i<p.size();i++)
			if (length <p.get(i).x+1|| width <p.get(i).y+1)//||temp.hasPosition(new Position(p.get(i).getX(),p.get(i).getY())))//when the piece is out of bound
			{
				JOptionPane.showMessageDialog(null,"The "+i+"th piece is out of bound","Error",JOptionPane.PLAIN_MESSAGE);
				cars.remove(temp);
				name--;
			}
	}
}