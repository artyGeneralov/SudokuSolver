import java.util.Scanner;

public class SudokuSolver {
	final static int PUZZLE_SIZE = 9;

	public static int[][] puzzle_sol = new int[PUZZLE_SIZE][PUZZLE_SIZE]; // solution
	
	public static void main(String[] args) {
		
		// test puzzle
		int[][] cur_puzzle = { { 1, 5, 0, 0, 0, 0, 0, 0, 0 }, { 3, 9, 6, 2, 1, 0, 0, 0, 0 }, { 0, 0, 4, 0, 5, 3, 1, 0, 6 },
				{ 4, 1, 0, 9, 0, 2, 0, 7, 8 }, { 7, 0, 3, 0, 0, 5, 4, 0, 9 }, { 0, 0, 8, 3, 0, 0, 5, 6, 1 },
				{ 0, 0, 0, 0, 3, 0, 0, 8, 0 }, { 8, 3, 0, 6, 0, 0, 2, 0, 0 }, { 0, 4, 0, 0, 0, 7, 0, 1, 3 } };
		
		/*int [][] cur_puzzle2 = new int[PUZZLE_SIZE][PUZZLE_SIZE];
		parsePuzzle(cur_puzzle2);
		System.out.println("\nYour puzzle:\n");
		toString(cur_puzzle2);
		solve(cur_puzzle2);
		System.out.println("\nYour complete puzzle:\n");
		toString(puzzle_sol);*/
		System.out.println("Empty: ");
		toString(cur_puzzle);
		solve(cur_puzzle);
		System.out.println("\nSolved\n");
		toString(puzzle_sol);
	}

	public static void parsePuzzle(int[][] cur_puzzle)
	{
		Scanner input = new Scanner(System.in);
		System.out.println("You will enter your puzzle row by row, type 0 for empty space.");
		for(int i = 0; i < PUZZLE_SIZE; i++)
			for(int j = 0; j < PUZZLE_SIZE; j++)
			{
				System.out.printf("Please input number for row %d, column %d: ", i+1, j+1);
				cur_puzzle[i][j] = input.nextInt();
			}
	}
	/*
	 * public solve method, invokes an overloaded solve method for recursion
	 * purposes.
	 */
	public static void solve(int[][] puzzle) {
		solve(puzzle, 0, 0);
	}

	/*
	 * Overloaded solve method for recursion purposes
	 */
	private static void solve(int[][] puzzle, int curRow, int curCol) {


		// find the next square that has a 0 in it
		while (curRow < PUZZLE_SIZE && curCol < PUZZLE_SIZE && puzzle[curRow][curCol] != 0) {
			if (curCol == PUZZLE_SIZE - 1) {
				curRow++;
				curCol = 0;
			} else
				curCol++;
		}
		
		/*
		 * If we've reached the end (trying to access an element that doesnt exist at
		 * the end) then the puzzle is complete and we can set the main puzzle array to
		 * that new found puzzle
		 */
		if (curRow == PUZZLE_SIZE || curCol == PUZZLE_SIZE) {
			puzzle_sol = copyArr(puzzle);
			return;
		}
				
		int[][] tempPuzzle = copyArr(puzzle); // another helper array to save the state of the board before the change so that recursion can happen.
		
		/*
		 * Insert every number from 1 to 9 into every square on the grid. if the number
		 * is wrong try the next number if the number fits solve for the next sqare
		 * 
		 */
		for (int curNum = 1; curNum <= PUZZLE_SIZE; curNum++) {
			tempPuzzle[curRow][curCol] = curNum;
			// if everything is fine
			if (checkColumn(tempPuzzle, curCol) && checkRow(tempPuzzle, curRow)
					&& checkIsland(tempPuzzle, curRow, curCol)) {
				// determine next position
				int nextRow, nextCol;
				if (curCol == PUZZLE_SIZE - 1) {
					nextRow = curRow + 1;
					nextCol = 0;
				} else {
					nextRow = curRow;
					nextCol = curCol + 1;
				}
				// solve in next position
				solve(tempPuzzle, nextRow, nextCol);
			}
		}

	}

	/*
	 * 2D array toString method
	 */
	private static void toString(int[][] puzzle) {
		for (int i = 0; i < puzzle.length; i++) {
			if (i != 0 && i % 3 == 0)
				System.out.print("_ _ _ _ _ _ _ _ _ _ _\n\n");
			for (int j = 0; j < puzzle[0].length; j++) {
				if (j != 0 && j % 3 == 0)
					System.out.print("| ");
				System.out.printf("%d ", puzzle[i][j]);
			}
			System.out.println();
		}
	}

	public static boolean checkPuzzle(int[][] puzzle) {
		for (int row = 0; row < PUZZLE_SIZE; row++) {
			if (!checkRow(puzzle, row))
				return false;
			for (int col = 0; col < PUZZLE_SIZE; col++) {
				if (!checkColumn(puzzle, col))
					return false;
				if (!checkIsland(puzzle, row, col))
					return false;
			}
		}
		return true;
	}

	/*
	 * Check if a number is ok for column true for ok, false for wrong
	 */
	private static boolean checkRow(int[][] puzzle, int row) {
		for (int i = 0; i < PUZZLE_SIZE; i++) {
			int curNum = puzzle[row][i];
			if (curNum == 0)
				continue;
			for (int j = 0; j < PUZZLE_SIZE; j++) {
				if (j == i)
					continue;
				if (puzzle[row][j] == 0)
					continue;
				if (puzzle[row][j] == curNum)
					return false;
			}
		}
		return true;
	}

	/*
	 * Check if a number is ok for column true for ok, false for wrong
	 */
	private static boolean checkColumn(int[][] puzzle, int col) {
		for (int i = 0; i < PUZZLE_SIZE; i++) {
			int curNum = puzzle[i][col];
			if (curNum == 0)
				continue;
			for (int j = 0; j < PUZZLE_SIZE; j++) {
				if (j == i)
					continue;
				if (puzzle[j][col] == 0)
					continue;
				if (puzzle[j][col] == curNum)
					return false;
			}
		}
		return true;
	}

	/*
	 * check whether a number is ok for region true for ok, false for wrong
	 */
	private static boolean checkIsland(int puzzle[][], int row, int col) {
		int minRow = row - row % 3;
		int minCol = col - col % 3;
		int maxRow = minRow + 2;
		int maxCol = minCol + 2;
		for (int r = minRow; r <= maxRow; r++) {
			for (int c = minCol; c <= maxCol; c++) {
				int curNum = puzzle[r][c];
				if (curNum == 0)
					continue;
				for (int i = minRow; i <= maxRow; i++) {
					for (int j = minCol; j <= maxCol; j++) {
						if (i == r && j == c)
							continue;
						if (puzzle[i][j] == 0)
							continue;
						if (puzzle[i][j] == curNum)
							return false;
					}
				}
			}
		}
		return true;
	}

	/*
	 * Helper function makes a copy of an array
	 */
	private static int[][] copyArr(int[][] arr) {
		int[][] cpy = new int[arr.length][arr[0].length];
		for (int i = 0; i < cpy.length; i++)
			for (int j = 0; j < cpy[0].length; j++)
				cpy[i][j] = arr[i][j];
		return cpy;
	}
}
