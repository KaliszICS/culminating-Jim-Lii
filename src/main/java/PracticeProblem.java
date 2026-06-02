/*
Title: Minesweeper - Culminating Assignment
Author: Jim Li
Date Created: May 29, 2026
Date Last Modified: Jun 2, 2026
 */

import java.util.Scanner;
import java.util.Random;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.ArrayList;
public class PracticeProblem {

	public static void main(String args[]) {
		Scanner input = new Scanner(System.in);
		//welcome message and instructions (does not explain everything)
		System.out.println("Welcome to Minesweeper!");
		System.out.println("Completely reveal the board without hitting a mine to win.");
		System.out.println("If you hit a mine, you lose. Mines are revealed if you lose.\n");
		System.out.println("--- Symbols ---");
		System.out.println("Number of mines surrounding a tile: 0 to 8");
		System.out.println("Flag: F\nMine: M\nUnrevealed: ?\nIncorrect Flag: X\n");
		System.out.println("--- Actions ---");
		System.out.println("r [integer] [integer] - Reveals the tile at (x, y)");
		System.out.println("f [integer] [integer] - Flags the tile at (x, y)\n");

		//get grid size (cannot be 3x3 or smaller, or ridiculously large). Separate width and height
		System.out.print("Input grid width (between 4 and 40): ");
		int width = 0;
		while (width < 4 || width > 40){
			while (!input.hasNextInt()){
				if (!input.hasNextInt()){
					System.out.print("Invalid input!\nInput grid width (between 4 and 40): ");
				}
				input.nextLine();
			}
			width = input.nextInt();
			if (width < 4 || width > 40){
				System.out.print("Invalid input!\nInput grid width (between 4 and 40): ");
			}
		}

		System.out.print("Input grid height (between 4 and 40): ");
		int height = 0;
		while (height < 4 || height > 40){
			while (!input.hasNextInt()){
				if (!input.hasNextInt()){
					System.out.print("Invalid input!\nInput grid height (between 4 and 40): ");
				}
				input.nextLine();
			}
			height = input.nextInt();
			if (height < 4 || height > 40){
				System.out.print("Invalid input!\nInput grid height (between 4 and 40): ");
			}
		}

		//get mine amount (must be at least 1, cannot have less than 9 empty spaces)
		System.out.print("Input mine amount (between 1 and # of tiles, minus 9): ");
		int mines = 0;
		while (mines < 1 || mines > width * height - 9){
			while (!input.hasNextInt()){
				if (!input.hasNextInt()){
					System.out.print("Invalid input!\nInput mine amount (between 1 and # of tiles, minus 9): ");
				}
				input.nextLine();
			}
			mines = input.nextInt();
			if (mines < 1 || mines > width * height - 9){
				System.out.print("Invalid input!\nInput mine amount (between 1 and # of tiles, minus 9): ");
			}
		}

		//create the display array (starts as just question marks). Only array visible to player
		char[][] display = new char[width][height];
		for (int i = 0; i < height; i++){
			for (int j = 0; j < width; j++){
				display[j][i] = '?';
			}
		}

		//print display (starting board)
		printArray(display, width, height);

		//this array records each action/move
		//the first digit determines the type of action: 0 = invalid input, 1 = reveal, 2 = flag
		//the second and third digit are for the column number and row number, respectively.
		int[] action = new int[3];

		//take first action (no flagging)
		System.out.print("Input first action: ");
		while (action[0] != 1){ //while invalid
			action = processAction(display, width, height);
			if (action[0] == 0){
				System.out.print("Invalid Input!\nInput first action: ");
			}
			if (action[0] == 2){
				System.out.print("Cannot flag on first move!\nInput first action: ");
			}
		}
		action[0] = 0;
		int col = action[1], row = action[2];
		display[col][row] = '0';
		
		//generate the actual board
		char[][] board = generateBoard(width, height, mines, col, row);
		printArray(board, width, height);

		//print starting board
		display = clear(display, board, width, height, col, row);
		printArray(display, width, height);

		//this is where the game actually begins
		boolean loss = false; //boolean for if the player hits a mine
		int flags = 0, tiles = width * height, revealedTiles = tiles - count(display, '?', width, height);
		//game goes until a mine is hit or everything has been revealed
		while (!loss && revealedTiles < tiles - mines){
			System.out.println("Flags left: " + (mines - flags));
			System.out.print("Input next action: ");
			while (action[0] == 0 || action[0] == 3){
				action = processAction(display, width, height);
				if (action[0] == 0){
					System.out.print("Invalid Input!\nInput next action: ");
				}
				if (action[0] == 3){ //if you try to reveal a flag
					System.out.print("You put a flag there for a reason.\nInput next action: ");
				}
			}
			col = action[1];
			row = action[2]; //indexes of selected tile
			if (action[0] == 1){ //if action is reveal
				display[col][row] = board[col][row]; //show tile
				if (display[col][row] == 'M'){ //if mine
					loss = true;
				}
				//automatic clearing
				if (display[col][row] == '0'){ //if 0 is revealed
					display = clear(display, board, width, height, col, row);
				}
			}
			if (action[0] == 2){ //if flag
				if (display[col][row] == '?'){
					display[col][row] = 'F';
					flags++;
				} else if (display[col][row] == 'F'){
					display[col][row] = '?';
					flags--;
				}
			}
			action[0] = 0; //reset action
			revealedTiles = width * height - count(display, '?', width, height) - count(display, 'F', width, height);
			printArray(display, width, height);
		}
		
		//ending message
		if (loss){
			printArray(board, width, height);
			System.out.println("You Lost!");
		} else {
			printArray(board, width, height);
			System.out.println("You Win!");
		}
	}

	//method that generates the board. Avoids spawning mines on starting area or on already placed mines
	public static char[][] generateBoard(int width, int height, int mines, int startX, int startY){
		//mine generation
		Random random = new Random();
		char[][] board = new char[width][height];
		//create the board
		for (int i = 0; i < height; i++){
			for (int j = 0; j < width; j++){
				board[j][i] = '0';
			}
		}
		int placedMines = 0;
		while (placedMines < mines){ //go until mines reaches target
			int ranX = random.nextInt(width); //random position generation
			int ranY = random.nextInt(height);
			//logic that prevents mines in starting position and repeating mine spawns
			if ((ranX < startX - 1 || ranX > startX + 1 || ranY < startY - 1 || ranY > startY + 1) && board[ranX][ranY] != 'M'){
				board[ranX][ranY] = 'M';
				placedMines++;
			}
		}

		//number generation
		for (int i = 0; i < height; i++){
			for (int j = 0; j < width; j++){
				board[j][i] = findMines(board, width, height, j, i);
			}
		}
		return board;
	}

	//method that counts surrounding mines, for board generation
	public static char findMines(char[][] board, int width, int height, int col, int row){
		if (board[col][row] == '0'){ //if selected tile is 0
			char counter = '0';
			//first if: checks if index is out of bounds
			//second if: checks if index is a mine
			if (col - 1 > -1 && row - 1 > -1 && board[col-1][row-1] == 'M'){ //up left
				counter++;
			}
			if (col - 1 > -1 && board[col-1][row] == 'M'){ //left
				counter++;
			}
			if (col - 1 > -1 && row + 1 < height && board[col-1][row+1] == 'M'){ //down left
				counter++;
			}
			if (row + 1 < height && board[col][row+1] == 'M'){ //down
				counter++;
			}
			if (col + 1 < width && row + 1 < height && board[col+1][row+1] == 'M'){ //down right
				counter++;
			}
			if (col + 1 < width && board[col+1][row] == 'M'){ //right
				counter++;
			}
			if (col + 1 < width && row - 1 > -1 && board[col+1][row-1] == 'M'){ //up right
				counter++;
			}
			if (row - 1 > -1 && board[col][row-1] == 'M'){ //up
				counter++;
			}
			return counter;
		}
		return 'M'; //if it's a mine
	}

	//method that counts the amount of a certain character in a character array, for counting revealed tiles
	public static int count(char[][] array, char target, int width, int height){
		int counter = 0;
		for (int i = 0; i < height; i++){
			for (int j = 0; j < width; j++){
				if (array[j][i] == target){
					counter++;
				}
			}
		}
		return counter;
	}

	//method that requests input then converts it into action	
	//if input is invalid, it will return 0 at index 0
	public static int[] processAction(char[][] display, int width, int height){
		Scanner input = new Scanner(System.in);
		int[] action = {0, 0, 0};
		String actionInput = input.nextLine(); //get input
		String[] splitInput = actionInput.split(" "); //split

		//if input has more than 3 things, immediately invalidate
		if (splitInput.length != 3){
			return action;
		}

		//check first part
		if (splitInput[0].equals("r")){ //if first thing is R
			action[0] = 1;
		} else if (splitInput[0].equals("f")){ //if first thing is F
			action[0] = 2;
		} else {
			action[0] = 0; //if invalid action
			return action;
		}

		//try to parse the column and row
		if (splitInput[1].matches("[0-9]+") && Integer.parseInt(splitInput[1]) < width){ //col
			action[1] = Integer.parseInt(splitInput[1]);
		}
		if (splitInput[2].matches("[0-9]+") && Integer.parseInt(splitInput[2]) < height){ //row
			action[2] = Integer.parseInt(splitInput[2]);
		}

		//if you try to reveal a flag for some reason
		if (action[0] == 1 && display[action[1]][action[2]] == 'F'){
			action[0] = 3;
		}
		return action;
	}

	//method that automatically clears the board, so the game is more playable
	//only runs if the revealed thing is a 0
	public static char[][] clear(char[][] display, char[][] board, int width, int height, int startCol, int startRow){
		Queue<int[]> queue = new ArrayDeque<>(); //queue for things to visit
		ArrayList<int[]> visited = new ArrayList<>(); //arraylist of visited tiles
		queue.add(new int[]{startCol, startRow});
		while (!queue.isEmpty()){ //go until everything possible is cleared
			int col = queue.peek()[0];
			int row = queue.remove()[1];
			if (!visited(visited, col, row)){
				//queue anything around the 0 that is also a 0
				if (col - 1 > -1 && row - 1 > -1){ //up left
					display[col - 1][row - 1] = board[col - 1][row - 1];
					if (board[col - 1][row - 1] == '0'){
						queue.add(new int[]{col - 1, row - 1});
					}
				}
				if (col - 1 > -1){ //left
					display[col - 1][row] = board[col - 1][row];
					if (board[col - 1][row] == '0'){
						queue.add(new int[]{col - 1, row});
					}
				}
				if (col - 1 > -1 && row + 1 < height){ //down left
					display[col - 1][row + 1] = board[col - 1][row + 1];
					if (board[col - 1][row + 1] == '0'){
						queue.add(new int[]{col - 1, row + 1});
					}
				}
				if (row + 1 < height){ //down
					display[col][row + 1] = board[col][row + 1];
					if (board[col][row + 1] == '0'){
						queue.add(new int[]{col, row + 1});	
					}
				}
				if (col + 1 < width && row + 1 < height){ //down right
					display[col + 1][row + 1] = board[col + 1][row + 1];
					if (board[col + 1][row + 1] == '0'){
						queue.add(new int[]{col + 1, row + 1});
					}
				}
				if (col + 1 < width){ //right
					display[col + 1][row] = board[col + 1][row];
					if (board[col + 1][row] == '0'){
						queue.add(new int[]{col + 1, row});
					}
				}
				if (col + 1 < width && row - 1 > -1){ //up right
					display[col + 1][row - 1] = board[col + 1][row - 1];
					if (board[col + 1][row - 1] == '0'){
						queue.add(new int[]{col + 1, row - 1});
					}
				}
				if (row - 1 > -1){ //up
					display[col][row - 1] = board[col][row - 1];
					if (board[col][row - 1] == '0'){
						queue.add(new int[]{col, row - 1});
					}
				}
			}
			visited.add(new int[]{col, row});
		}
		return display;
	}

	//method for clear() that checks if something has been visited
	public static boolean visited(ArrayList<int[]> visited, int col, int row){
		for (int i = 0; i < visited.size(); i++){
			int checkCol = visited.get(i)[0];
			int checkRow = visited.get(i)[1];
			if (col == checkCol && row == checkRow){
				return true;
			}
		}
		return false;
	}

	//method that prints 2D arrays, for displaying field and testing. Also shows coordinates.
	public static void printArray(char[][] array, int width, int height){
		System.out.print("\n   ");
		for (int i = 0; i < width; i++){ //top numbers
			System.out.print(i);
			if (i < 10){
				System.out.print(" ");
			}
		}
		for (int i = 0; i < height; i++){ //side numbers and tiles
			System.out.print("\n" + i + " ");
			if (i < 10){
				System.out.print(" ");
			}
			for (int j = 0; j < width; j++){
				System.out.print(array[j][i] + " ");
			}
		}
		System.out.print("\n");
	}
}