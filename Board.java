package Chess;

/**
 * A class to keep track of the current arrangement of a chess board
 */
public class Board 
{
	private final int WIDTH = 8;
	private final int HEIGHT = WIDTH;
	private String[][] board = new String[WIDTH][HEIGHT];
	
	/**
	 * Constructs an empty chess board with dimensions WIDTH x HEIGHT
	 */
	public Board()
	{
		for(int i = 0; i < WIDTH; i++)
		{
			for(int j = 0; j < HEIGHT; j++)
			{
				board[i][j] = "  ";
			}
		}		
	}
	
	/**
	 * Prints the current chess board to the console with character labels for row and column 
	 */
	public void displayBoard()
	{
		System.out.println("  _______________________________________________________");
		for(int j = WIDTH - 1; j >= 0 ; j--)
		{
			System.out.print(" |      |      |      |      |      |      |      |      |\n" + (j + 1));
			for(int i = 0; i < HEIGHT; i++)
			{
				System.out.print("|  " + board[i][j] + "  ");
			}
			System.out.println("|\n |______|______|______|______|______|______|______|______|");
		}
		System.out.println("    a      b      c      d      e      f      g      h");
	}
	
	/**
	 * Adds the Piece parameter to the current board, replacing the previous contents of that square
	 * @param piece The specific piece that we wish to add to the board
	 */
	public void updatePiece(Piece piece)
	{
		int row = piece.getPosition() % 10;
		int column = (piece.getPosition() - row) / 10;
		board[column][row] = piece.getPiece();
	}
	
	/**
	 * Obtains the position of the Piece parameter, and replaces it with empty characters
	 * @param piece The specific piece that we wish to remove from the current board
	 */
	public void deleteOldPiece(Piece piece)
	{
		int row = piece.getPosition() % 10;
		int column = (piece.getPosition() - row) / 10;
		board[column][row] = "  ";
	}
}



