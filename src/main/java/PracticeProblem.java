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

		//get grid size
		System.out.print("Input grid size (between 2 and 50): ");
		int gridSize = 0;
		while (gridSize < 2 || gridSize > 50){
			while (!input.hasNextInt()){
				input.nextLine();
				if (!input.hasNextInt()){
					System.out.print("Invalid input!\nInput grid size (between 2 and 50): ");
				}
			}
			gridSize = input.nextInt();
			if (gridSize < 2 || gridSize > 50){
				System.out.print("Invalid input!\nInput grid size (between 2 and 50): ");
			}
		}

		//get mine amount
		System.out.print("Input mine amount (between 1 and # of tiles): ");
		int mines = 0;
		while (mines < 1 || mines > gridSize * gridSize - 1){
			while (!input.hasNextInt()){
				input.nextLine();
				if (!input.hasNextInt()){
					System.out.print("Invalid input!\nInput grid size (between 2 and 50): ");
				}
			}
			mines = input.nextInt();
			if (mines < 1 || mines > gridSize * gridSize - 1){
				System.out.print("Invalid input!\nInput grid size (between 2 and 50): ");
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
		
		//generate the board
		char[][] hidden = new char[gridSize][gridSize];
		for (int i = 0; i < gridSize; i++){
			for (int j = 0; j < gridSize; j++){
				display[i][j] = '?';
			}
		}
	}

	//method that requests input then converts it into action
	public static int[] processAction(){
		Scanner input = new Scanner(System.in);
		int[] action = {0, 0, 0};
		String actionInput = input.nextLine();
		return action;
	}

	//method that prints 2D arrays, for displaying field and testing
	public static void printArray(char[][] array, int gridSize){
		for (int i = 0; i < gridSize; i++){
			for (int j = 0; j < gridSize; j++){
				System.out.print(array[j][i] + " ");
			}
			System.out.println("");
		}
	}
}