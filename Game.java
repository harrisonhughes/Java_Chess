package Chess;

import java.util.Scanner;


public class Game {

	public static void main(String[] args) {
		Board board = new Board();
		Team white = new Team("W", board);
		Team black = new Team("B", board);
		board.displayBoard();
	
		Scanner input = new Scanner(System.in);
		String move = "";
		int moves = 0;
		while(!move.equals("Resign"))
		{
			if(moves % 2 == 0)
			{
				if(white.getCheck())
				{
					System.out.print("CHECK... ");
				}
				System.out.print("Move " + (moves / 2 + 1) + ": White, enter move: ");
				move = input.nextLine();
				
				if(move.equals("Print"))
				{
					white.printPossibleMoves(black);
					System.out.print("White, enter move; ");
					move = input.nextLine();
				}
				while(!white.movePiece(move, board, black))
				{
					move = input.nextLine();
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
				
				if(move.equals("Print"))
				{
					black.printPossibleMoves(white);
					System.out.print("Black, enter move; ");
					move = input.nextLine();
				}
				while(!black.movePiece(move, board, white))
				{
					move = input.nextLine();
				}
				board.displayBoard();
				white.checkGameStatus(black);
			}
			moves ++;
		}
		input.close();
	}
}


