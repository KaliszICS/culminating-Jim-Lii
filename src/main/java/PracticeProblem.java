/*
Title: Minesweeper - Culminating Assignment
Author: Jim Li
Date Created: May 29, 2026
Date Last Modified: May 29, 2026
 */

/*
for (int i = 0; i < gridSize; i++){
			for (int j = 0; j < gridSize; j++){

			}
		}
 */

import java.util.Scanner;
import java.util.Random;
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
		System.out.println("R [integer] [integer] - Reveals the tile at (x, y)");
		System.out.println("F [integer] [integer] - Flags the tile at (x, y)\n");

		//get grid size (cannot be 3x3 or smaller, or ridiculously large). Grid size is for length AND width.
		System.out.print("Input grid size (between 4 and 40): ");
		int gridSize = 0;
		while (gridSize < 4 || gridSize > 40){
			while (!input.hasNextInt()){
				input.nextLine();
				if (!input.hasNextInt()){
					System.out.print("Invalid input!\nInput grid size (between 4 and 40): ");
				}
			}
			gridSize = input.nextInt();
			if (gridSize < 4 || gridSize > 40){
				System.out.print("Invalid input!\nInput grid size (between 4 and 40): ");
			}
		}

		//get mine amount (must be at least 1, cannot have less than 9 empty spaces)
		System.out.print("Input mine amount (between 1 and # of tiles, minus 9): ");
		int mines = 0;
		while (mines < 1 || mines > gridSize * gridSize - 9){
			while (!input.hasNextInt()){
				input.nextLine();
				if (!input.hasNextInt()){
					System.out.print("Invalid input!\nInput mine amount (between 1 and # of tiles, minus 9): ");
				}
			}
			mines = input.nextInt();
			if (mines < 1 || mines > gridSize * gridSize - 9){
				System.out.print("Invalid input!\nInput mine amount (between 1 and # of tiles, minus 9): ");
			}
		}

		//create the display array (starts as just question marks), only array visible to player
		char[][] display = new char[gridSize][gridSize];
		for (int i = 0; i < gridSize; i++){
			for (int j = 0; j < gridSize; j++){
				display[i][j] = '?';
			}
		}

		//print starting board
		printArray(display, gridSize);

		//this array records the next action
		//the first digit determines the type of action: 0 = invalid input, 1 = reveal, 2 = flag
		//the second and third digit are for the column number and row number, respectively.
		int[] action = {0, 0, 0};

		//take first action
		System.out.print("Input first action: ");
		while (action[0] != 1){ //while invalid
			action = processAction(gridSize);
			if (action[0] == 0){
				System.out.print("Invalid Input!\nInput first action: ");
			}
			if (action[0] == 2){
				System.out.print("Cannot flag on first move!\nInput first action: ");
			}
		}
		action[0] = 0;
		
		//generate the actual board
		char[][] board = generateBoard(gridSize, mines, action[1], action[2]);
		printArray(board, gridSize);

		//print starting board
		display[action[1]][action[2]] = board[action[1]][action[2]];
		printArray(display, gridSize);

		//this is where the game actually begins
		boolean loss = false; //boolean for if the player hits a mine
		int tiles = gridSize * gridSize, revealedTiles = 1; //one tile has already been revealed
		while (!loss && revealedTiles < tiles - mines){
			System.out.print("Input next action: ");
			while (action[0] == 0){
				action = processAction(gridSize);
				if (action[0] == 0){
					System.out.print("Invalid Input!\nInput first action: ");
				}
			}
			if (action[0] == 1){
				if (display[action[1]][action[2]] != board[action[1]][action[2]]){
					revealedTiles++;
				}
				display[action[1]][action[2]] = board[action[1]][action[2]];
				if (display[action[1]][action[2]] == 'M'){
					loss = true;
				}
			}
			if (action[0] == 2){
				display[action[1]][action[2]] = 'F';
			}
			action[0] = 0; //reset action
			printArray(display, gridSize);
		}
		
		//ending message
		if (loss){
			System.out.println("You lost!");
		}
	}

	//method that generates the board. Avoids spawning mines on starting area or on already placed mines
	public static char[][] generateBoard(int gridSize, int mines, int startX, int startY){
		//mine generation
		Random random = new Random();
		char[][] board = new char[gridSize][gridSize];
		//fill the array
		for (int i = 0; i < gridSize; i++){
			for (int j = 0; j < gridSize; j++){
				board[i][j] = '0';
			}
		}
		int placedMines = 0;
		while (placedMines < mines){ //go until mines reaches target
			int ranX = random.nextInt(gridSize); //random position generation
			int ranY = random.nextInt(gridSize);
			//logic that prevents mines in starting position and repeating mine spawns
			if ((ranX < startX - 1 || ranX > startX + 1 || ranY < startY - 1 || ranY > startY + 1) && board[ranX][ranY] != 'M'){
				board[ranX][ranY] = 'M';
				placedMines++;
			}
		}

		//number generation
		for (int i = 0; i < gridSize; i++){
			for (int j = 0; j < gridSize; j++){
				board[i][j] = findMines(board, gridSize, i, j);
			}
		}
		return board;
	}

	//method that counts surrounding mines
	public static char findMines(char[][] board, int gridSize, int row, int col){
		if (board[row][col] == '0'){ //if selected tile is 0
			char counter = '0';
			//first if: checks if index is out of bounds
			//second if: checks if index is a mine
			if (row - 1 > -1 && col - 1 > -1){ //up left
				if (board[row-1][col-1] == 'M'){
					counter++;
				}
			}
			if (row - 1 > -1){ //up
				if (board[row-1][col] == 'M'){
					counter++;
				}
			}
			if (row - 1 > -1 && col + 1 < gridSize){ //up right
				if (board[row-1][col+1] == 'M'){
					counter++;
				}
			}
			if (col + 1 < gridSize){ //right
				if (board[row][col+1] == 'M'){
					counter++;
				}
			}
			if (row + 1 < gridSize && col + 1 < gridSize){ //down right
				if (board[row+1][col+1] == 'M'){
					counter++;
				}
			}
			if (row + 1 < gridSize){ //down
				if (board[row+1][col] == 'M'){
					counter++;
				}
			}
			if (row + 1 < gridSize && col - 1 > -1){ //down left
				if (board[row+1][col-1] == 'M'){
					counter++;
				}
			}
			if (col - 1 > -1){ //left
				if (board[row][col-1] == 'M'){
					counter++;
				}
			}
			return counter;
		}
		return 'M'; //if it's a mine
	}

	//method that requests input then converts it into action	
	//if input is invalid, it will return 0 at index 0
	public static int[] processAction(int gridSize){
		Scanner input = new Scanner(System.in);
		int[] action = {0, 0, 0};
		String actionInput = input.nextLine(); //get input
		String[] splitInput = actionInput.split(" "); //split

		//if input has more than 3 things, immediately invalidate
		if (splitInput.length != 3){
			return action;
		}

		//check first part
		if (splitInput[0].equals("R")){ //if first thing is R
			action[0] = 1;
		} else if (splitInput[0].equals("F")){ //if first thing is F
			action[0] = 2;
		} else {
			action[0] = 0; //if invalid action
			return action;
		}

		//process the X and Y coordinates
		if (splitInput[1].matches("[\\d+]")){
			if (Integer.parseInt(splitInput[1]) < gridSize){
				action[1] = Integer.parseInt(splitInput[1]);
			}
		}
		if (splitInput[2].matches("[\\d+]")){
			if (Integer.parseInt(splitInput[2]) < gridSize){
				action[2] = Integer.parseInt(splitInput[2]);
			}
		}
		
		return action;
	}

	//method that prints 2D arrays, for displaying field and testing. Also shows coordinates.
	public static void printArray(char[][] array, int gridSize){
		System.out.print("   ");
		for (int i = 0; i < gridSize; i++){
			System.out.print(i);
			if (i < 10){
				System.out.print(" ");
			}
		}
		for (int i = 0; i < gridSize; i++){
			System.out.print("\n" + i + " ");
			if (i < 10){
				System.out.print(" ");
			}
			for (int j = 0; j < gridSize; j++){
				System.out.print(array[j][i] + " ");
			}
		}
		System.out.print("\n");
	}
}