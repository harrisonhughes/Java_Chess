package Chess;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * A class to hold all of the rules and information that the Bishop piece has. This class inherits the Piece class and all 
 * of its information and rules that correspond to general behaviors of all pieces.
 */
public class Bishop extends Piece{
	private final int WHITE_START_LEFT = 20;
	private final int WHITE_START_RIGHT = 50;
	private final int BLACK_START_LEFT = 27;
	private final int BLACK_START_RIGHT = 57;
	static int pieces = 0;
	
	/**
	 * Constructor, provides the team, piece type, and start position of the piece to the Piece superclass, and sends this 
	 * information to the board parameter to display its initial position.
	 * @param team A string code corresponding to the team of the piece, "W" for white, and "B" for black within this program
	 * @param board The chess board that keeps track of all of the pieces in the current game
	 */
	public Bishop(String team, Board board)
	{
		setTeam(team);
		setPiece(team + "B");
		
		pieces ++;
		if(pieces == 1)
		{
			setPosition(WHITE_START_LEFT);
		}
		else if(pieces == 2)	
		{
			setPosition(WHITE_START_RIGHT);
		}
		else if(pieces == 3)
		{
			setPosition(BLACK_START_LEFT);
		}
		else
		{
			setPosition(BLACK_START_RIGHT);
		}
		board.updatePiece(this);
	}
	
	/**
	 * Generates the vacuum moves for this Bishop piece based on its current position
	 * @Override
	 */
	public void setVacuumMoves()
	{
		final int MAX_BOARD_INDEX = 7;
		final int MIN_BOARD_INDEX = 0;

		int row = getPosition() % 10;
		int column = getPosition() / 10;
		LinkedList<Integer> vacuumMoves = new LinkedList<Integer>();

		for(int diagonalsAway = 1; diagonalsAway <= MAX_BOARD_INDEX; diagonalsAway++)
		{ // the for loop starts at 1, as the bishop cannot move to its own square
			if((column + diagonalsAway) <= MAX_BOARD_INDEX && (row + diagonalsAway) <= MAX_BOARD_INDEX)
			{
				vacuumMoves.add(10 * (column + diagonalsAway) + (row + diagonalsAway));
			}
			if((column - diagonalsAway) >= MIN_BOARD_INDEX && (row + diagonalsAway) <= MAX_BOARD_INDEX)
			{
				vacuumMoves.add(10 * (column - diagonalsAway) + (row + diagonalsAway));
			}
			if((column + diagonalsAway) <= MAX_BOARD_INDEX && (row - diagonalsAway) >= MIN_BOARD_INDEX)
			{
				vacuumMoves.add(10 * (column + diagonalsAway) + (row - diagonalsAway));
			}
			if((column - diagonalsAway) >= MIN_BOARD_INDEX && (row - diagonalsAway) >= MIN_BOARD_INDEX)
			{
				vacuumMoves.add(10 * (column - diagonalsAway) + (row - diagonalsAway));
			}
		}	
		setSquaresAttacked(vacuumMoves); // Initializes squares attacked and possible moves with the comprehensive list of vacuum moves
		setPossibleMoves(vacuumMoves);
	}
	
	/**
	 * Takes the previous list of squares attacked and removes any vacuum moves that are blocked by the parameter piece
	 * @param piece A piece of any type whose information will be used to refine the squares attacked of the current piece
	 * @Override
	 */
	public void setSquaresAttacked(Piece piece)
	{		
		int thisPosition = getPosition();
		int thisColumn = thisPosition / 10;
		int thisRow = thisPosition % 10;
		int otherPosition = piece.getPosition();
		int otherColumn = otherPosition / 10;
		int otherRow = otherPosition % 10;
		
		LinkedList<Integer> squaresAttacked = new LinkedList<Integer>(getSquaresAttacked());
		ListIterator<Integer> moveIterator = squaresAttacked.listIterator();
		int moveHolder;
		while(moveIterator.hasNext())
		{
			moveHolder = moveIterator.next();
			if(Math.abs(thisColumn - otherColumn) == Math.abs(thisRow - otherRow)) 
				// To make sure the parameter piece's position is on a diagonal with the current piece, else it does not block any bishop moves
			{
				if(otherColumn > thisColumn && otherRow > thisRow)
				{ // Each of these constructs checks if the parameter piece blocks a possible move in a specific direction
					if((moveHolder / 10) > otherColumn && (moveHolder % 10) > otherRow)
					{
						moveIterator.remove();
						
					}
				}
				else if(otherColumn > thisColumn && otherRow < thisRow)
				{
					if((moveHolder / 10) > otherColumn && (moveHolder % 10) < otherRow)
					{
						moveIterator.remove();
					}
				}
				else if(otherColumn < thisColumn && otherRow > thisRow)
				{
					if((moveHolder / 10) < otherColumn && (moveHolder % 10) > otherRow)
					{
						moveIterator.remove();
					}
				}
				else if(otherColumn < thisColumn && otherRow < thisRow)
				{
					if((moveHolder / 10) < otherColumn && (moveHolder % 10) < otherRow)
					{
						moveIterator.remove();
					}
				}
			}
		}
		setSquaresAttacked(squaresAttacked);
		setPossibleMoves(squaresAttacked); // Updates possible moves list with the refined list of squares attacked
	}	
	
	/**
	 * Takes the previous list of possible moves and removes any move that lands on a piece that is on the same team
	 * @param piece A piece of any type whose information will be used to refine the possible moves of the current piece
	 * @Override
	 */
	public void setPossibleMoves(Piece piece)
	{
		super.setPossibleMoves(piece);
	}
}



