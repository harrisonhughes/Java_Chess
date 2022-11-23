package Chess;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * A class to hold all of the rules and information that the Rook piece has. This class inherits the Piece class and all 
 * of its information and rules that correspond to general behaviors of all pieces.
 */
public class Rook extends Piece{
	private final int WHITE_START_LEFT = 00;
	private final int WHITE_START_RIGHT = 70;
	private final int BLACK_START_LEFT = 07;
	private final int BLACK_START_RIGHT = 77;
	static int pieces = 0;
	
	/**
	 * Constructor, provides the team, piece type, and start position of the piece to the Piece superclass, and sends this 
	 * information to the board parameter to display its initial position.
	 * @param team A string code corresponding to the team of the piece, "W" for white, and "B" for black within this program
	 * @param board The chess board that keeps track of all of the pieces in the current game
	 */
	public Rook(String team, Board board)
	{
		setTeam(team);
		setPiece(team + "R");
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
	 * Generates the vacuum moves for this Rook piece based on its current position
	 * @Override
	 */
	public void setVacuumMoves()
	{		
		final int MAX_BOARD_INDEX = 7; 
		final int MIN_BOARD_INDEX = 0;
	
		int row = getPosition() % 10;
		int column = (getPosition() - row) / 10;
		LinkedList<Integer> vacuumMoves = new LinkedList<Integer>();
		// All for loops begin "+1" or "-1" square away from the current position, as the rook cannot move to its own square
		for(int i = column + 1; i <= MAX_BOARD_INDEX; i++)
		{
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
			if(thisColumn == otherColumn)
			{
				if(thisRow > otherRow) 
				{ // Each of these constructs checks if the parameter piece blocks a possible move in a specific direction
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
			else if(thisRow == otherRow)
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



