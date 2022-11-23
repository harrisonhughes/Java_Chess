package Chess;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * A class to hold all of the rules and information that the Queen piece has. This class inherits the Piece class and all 
 * of its information and rules that correspond to general behaviors of all pieces.
 */
public class Queen extends Piece
{
	/**
	 * Constructor, provides the team, piece type, and start position of the piece to the Piece superclass, and sends this 
	 * information to the board parameter to display its initial position.
	 * @param team A string code corresponding to the team of the piece, "W" for white, and "B" for black within this program
	 * @param board The chess board that keeps track of all of the pieces in the current game
	 */
	public Queen(String team, Board board)
	{
		final int WHITE_START = 30;
		final int BLACK_START = 37;
		
		setTeam(team);
		setPiece(team + "Q");
		
		if(team.equals("W"))
		{
			setPosition(WHITE_START);
		}
		else
		{
			setPosition(BLACK_START);
		}
		board.updatePiece(this);
	}
	
	/**
	 * Constructor, needed for pawn promotion, as a pawn must copy its position and team to the new Queen
	 * @param position The position of the promoted pawn
	 * @param team The team that the promoted pawn belonged to 
	 */
	public Queen(int position, String team)
	{
		setTeam(team);
		setPiece(team + "Q");
		setPosition(position);
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
		
		for(int i = column + 1; i <= MAX_BOARD_INDEX; i++)
		{ // All for loops begin "+1" or "-1" square away from the current position, as the Queen cannot move to its own square
			vacuumMoves.add(10 * i + row);
		}
		for(int i = column - 1; i >= MIN_BOARD_INDEX; i--)
		{
			vacuumMoves.add(10 * i + row);
		}
		for(int i = row + 1; i <= MAX_BOARD_INDEX; i++)
		{
			vacuumMoves.add(10 * column + i);
		}
		for(int i = row - 1; i >= MIN_BOARD_INDEX; i--)
		{
			vacuumMoves.add(10 * column + i);
		}
		
		for(int diagonalsAway = 1; diagonalsAway <= MAX_BOARD_INDEX; diagonalsAway++)
		{ // The for loop starts at 1, as the Queen cannot move to its own square
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
			{ // If the parameter piece is on a square diagonal to the current piece, remove invalid diagonal moves
				if(otherColumn > thisColumn && otherRow > thisRow)
				{
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
			else // Otherwise, check if it is on the same row or column, to remove invalid linear moves
			{ 
				if(thisColumn == otherColumn && moveHolder / 10 == thisColumn)
				{
					if(thisRow > otherRow)
					{
						if((moveHolder % 10) < otherRow)
						{
							moveIterator.remove();
						}
					}
				
					else if(thisRow < otherRow)
					{
						if((moveHolder % 10) > otherRow)
						{
							moveIterator.remove();
						}
					}
				}
				else if(thisRow == otherRow && moveHolder % 10 == thisRow)
				{
					if(thisColumn > otherColumn)
					{
						if((moveHolder / 10) < otherColumn)
						{
							moveIterator.remove();
						}
					}
					else if(thisColumn < otherColumn)
					{
						if((moveHolder / 10) > otherColumn)
						{
							moveIterator.remove();
						}
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



