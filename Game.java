package Chess;

import java.util.Scanner;
public class Game 
{

	public static void main(String[] args) {
		Board board = new Board();
		Team white = new Team("W", board);
		Team black = new Team("B", board);
		board.displayBoard();
	
		Scanner input = new Scanner(System.in);
		String move = "";
		int moves = 0;
		
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
				
				while(!move.equals("Resign") && !white.movePiece(move, board, black))
				{
					move = input.nextLine();
				}
				if(move.equals("Resign"))
				{
					board.displayBoard();
					System.out.println("White resigns on move " + moves + ".");
					break;
				}
				board.displayBoard();
				black.checkGameStatus(white);
			}
			else
			{
				if(black.getCheck())
				{
					System.out.print("CHECK... ");
				}
				System.out.print("Black, enter move; ");
				move = input.nextLine();
				
				while(!move.equals("Resign") && !black.movePiece(move, board, white))
				{
					move = input.nextLine();
				}
				if(move.equals("Resign"))
				{
					board.displayBoard();
					System.out.println("Black resigns on move " + moves + ".");
					break;
				}
				
				board.displayBoard();
				white.checkGameStatus(black);
			}
			moves ++;
		}
		input.close();
	}
}


