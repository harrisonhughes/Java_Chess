package Chess;

import java.util.Scanner;


public class Game {

	public static void main(String[] args) {
		Board board = new Board();
		Team white = new Team("W", board);
		Team black = new Team("B", board);
		board.displayBoard();
	
		Scanner input = new Scanner(System.in);
		String move;
		for(int i = 0; i < 100; i++)
		{
			if(white.kingInCheckmate(black) && i != 0)
			{
				System.out.println("CHECKMATE");
				System.exit(0);
			}
			if(white.getCheck())
			{
				System.out.print("CHECK... ");
			}
			System.out.print("Move " + (i + 1) + ": White, enter move: ");
			move = input.nextLine();
			
			if(move.equals("Print"))
			{
				white.printPossibleMoves(black);
				System.out.print("White, enter move; ");
				move = input.nextLine();
			}
			while(!white.movePiece(move, board, black))
			{
				System.out.print("Invalid square, try again: ");
				move = input.nextLine();
			}
			board.displayBoard();
			
			if(black.kingInCheckmate(white))
			{
				System.out.println("CHECKMATE");
				System.exit(0);
			}
			
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
				System.out.print("Invalid square, try again: ");
				move = input.nextLine();
			}
			board.displayBoard();
		}
		input.close();
	}
}


