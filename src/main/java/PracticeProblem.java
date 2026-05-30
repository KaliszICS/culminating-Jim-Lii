/*
Title: Minesweeper - Culminating Assignment
Author: Jim Li
Date Created: May 29, 2026
Date Last Modified: May 29, 2026
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
		
		//generate the actual board
		char[][] board = generateBoard(gridSize, mines, action[1], action[2]);
		printArray(board, gridSize);
	}

	//method that generates the board. Avoids spawning mines on starting area or on already placed mines
	public static char[][] generateBoard(int gridSize, int mines, int startX, int startY){
		Random random = new Random();
		char[][] board = new char[gridSize][gridSize];
		//fill the array
		for (int i = 0; i < gridSize; i++){
			for (int j = 0; j < gridSize; j++){
				board[i][j] = '0';
			}
		}
		int placedMines = 0;
		while (placedMines < mines){
			int ranX = random.nextInt(gridSize);
			int ranY = random.nextInt(gridSize);
			//logic that prevents mines in starting position and repeating mine spawns
			if ((ranX < startX - 1 || ranX > startX + 1 || ranY < startY - 1 || ranY > startY + 1) && board[ranX][ranY] != 'M'){
				board[ranX][ranY] = 'M';
				placedMines++;
			}
		}
		return board;
	}

	//method that requests input then converts it into action	
	//if input is invalid, it will return 0 at index 0
	public static int[] processAction(){
		Scanner input = new Scanner(System.in);
		int[] action = {0, 0, 0};
		String actionInput = input.nextLine();
		String[] splitInput = actionInput.split(" ");
		if (splitInput.length != 3){ //if input has more than 3 things
			return action;
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