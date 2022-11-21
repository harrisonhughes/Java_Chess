package Chess;

public class Board {
	private final int WIDTH = 8;
	private final int HEIGHT = WIDTH;
	private String[][] board = new String[WIDTH][HEIGHT];
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
	
	public void deleteOldPiece(Piece piece)
	{
		int column = piece.getPosition() % 10;
		int row = (piece.getPosition() - column) / 10;
		board[row][column] = "  ";
	}
	
	public void updatePiece(Piece piece)
	{
		int column = piece.getPosition() % 10;
		int row = (piece.getPosition() - column) / 10;
		board[row][column] = piece.getPiece();
	}
}



