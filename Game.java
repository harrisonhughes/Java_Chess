package Chess;

import java.util.Scanner;
public class Game 
{
	public static void main(String[] args) {
		board.displayBoard();
		while(true)
		{
			if(moves % 2 == 0)
			{
				if(white.getCheck())
				{
					System.out.print("CHECK... ");
				}
				System.out.print("Move " + (moves / 2 + 1) + ": White, enter move: ");
				move = input.nextLine();
				
				while(!move.toUpperCase().equals("RESIGN") && (move.toUpperCase().equals("DRAW") || !white.movePiece(move, board, black)))
				{
					if(move.toUpperCase().equals("DRAW"))
					{
						System.out.print("\nWhite has offered a draw.\nBlack, if you accept, enter \"Draw\" (otherwise press Enter): ");
						move = input.nextLine();
						if(move.toUpperCase().equals("DRAW"))
						{
							System.out.println("Draw by Player Agreement.");
							endGame();
						}
						else
						{
							System.out.print("White has declined the draw offer.\n\nWhite, enter move: ");
						}
					}
					move = input.nextLine();
				}
				if(move.toUpperCase().equals("RESIGN"))
				{
					System.out.println("White Resigns. Black Wins!");
					endGame();
				}
				board.displayBoard();
				if(black.checkGameStatus(white) == false)
				{
					endGame();
				}
			}
			else
			{
				if(black.getCheck())
				{
					System.out.print("CHECK... ");
				}
				System.out.print("Black, enter move: ");
				move = input.nextLine();
				
				while(!move.toUpperCase().equals("RESIGN") && (move.toUpperCase().equals("DRAW") || !black.movePiece(move, board, white)))
				{
					if(move.toUpperCase().equals("DRAW"))
					{
						System.out.print("\nBlack has offered a draw.\nWhite, if you accept, enter \"Draw\" (otherwise press Enter): ");
						move = input.nextLine();
						if(move.toUpperCase().equals("DRAW"))
						{
							System.out.println("Draw by Player Agreement.");
							endGame();
						}
						else
						{
							System.out.print("White has declined the draw offer.\n\nBlack, enter move: ");
						}
					}
					move = input.nextLine();
				}
				if(move.toUpperCase().equals("RESIGN"))
				{
					System.out.println("Black Resigns. White Wins!");
					endGame();
				}
				
				board.displayBoard();
				if(white.checkGameStatus(black) == false)
				{
					endGame();
				}
			}
			moves ++;
		}
	}
	public static Board board = new Board();
	public static Team white = new Team("W", board);
	public static Team black = new Team("B", board);
	public static String move = "";
	public static int moves = 0;
	public static Scanner input = new Scanner(System.in);
	public static void endGame()
	{
		input.close();
		System.exit(0);
	}
}


