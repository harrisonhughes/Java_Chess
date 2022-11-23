package Chess;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * A class to hold all of the rules and information that the Pawn piece has. This class inherits the Piece class and all 
 * of its information and rules that correspond to general behaviors of all pieces.
 */
public class Pawn extends Piece
{
	private boolean enPassantPossible = false;
	static int pieces = 0;
	
	/**
	 * Constructor, provides the team, piece type, and start position of the piece to the Piece superclass, and sends this 
	 * information to the board parameter to display its initial position.
	 * @param team A string code corresponding to the team of the piece, "W" for white, and "B" for black within this program
	 * @param board The chess board that keeps track of all of the pieces in the current game
	 */
	public Pawn(String team, Board board)
	{
		int white_first_position = 01;
		int black_first_position = 06;
		
		setTeam(team);
		setPiece(team + "P");
		
		if(team.equals("W"))
		{ // Each new piece added moves the starting position of the pawn one column to the right
			setPosition(white_first_position + 10 * pieces);
		}
		else
		{
			setPosition(black_first_position + 10 * pieces);
		}
		
		pieces++;
		if(pieces == 8) // Once 8 pawns have been added, reset the piece count to increment for the other team
		{
			pieces = 0;
		}	
		board.updatePiece(this);
	}
	
	/**
	 * Generates the vacuum moves for this Bishop piece based on its current position. Since vacuum moves corresponds to a piece's
	 * possible moves when it is the only piece on the board, no diagonal moves are possible as of yet, and therefore squares attacked
	 * must be calculated separately. Thus, the vacuum moves must only be copied to the possible moves list, and not the squares attacked
	 * @Override
	 */
	public void setVacuumMoves()
	{
		int row = getPosition() % 10;
		int column = getPosition() / 10;
		int direction = getTeam();
		LinkedList<Integer> vacuumMoves = new LinkedList<Integer>();
		
		vacuumMoves.add(10 * column + (row + direction));
		if(super.getFirstMove())
		{ // If the pawn has not moved, it may jump two squares in the correct direction, determined by the direction accessor
			vacuumMoves.add(10 * column + row + (2 * direction));
		}
		super.setPossibleMoves(vacuumMoves); // Initializes possible moves with the comprehensive list of vacuum moves
	}
	
	/**
	 * Generates a list of squares attacked based on the current position of the piece. No information from the vacuum moves list is needed
	 * @param piece An arbitrary piece, not used in calculation; no matter where this piece may be, a pawn always attacks its diagonals
	 */
	public void setSquaresAttacked(Piece piece)
	{
		final int MAX_BOARD_INDEX = 7;
		final int MIN_BOARD_INDEX = 0;

		int row = getPosition() % 10;
		int column = getPosition() / 10;
		int direction = getTeam();
		LinkedList<Integer> squaresAttacked = new LinkedList<Integer>();
		
		if(row + direction >= MIN_BOARD_INDEX && row + direction <= MAX_BOARD_INDEX)
		{ // If a pawns diagonal is a square within the bounds of the board, it is added to the squares attacked list
			if(column + direction <= MAX_BOARD_INDEX && column + direction >= MIN_BOARD_INDEX)
			{
				squaresAttacked.add(10 * (column + direction) + (row + direction));
			}
			if(column - direction <= MAX_BOARD_INDEX && column - direction >= MIN_BOARD_INDEX)
			{
				squaresAttacked.add(10 * (column - direction) + (row + direction));
			}
		}		
		setSquaresAttacked(squaresAttacked);
	}
	
	/**
	 * Takes the previous list of possible moves, and removes any moves that are blocked by the parameter piece. This method also 
	 * adds a new possible move if a piece of the opposing team is on a diagonal to the pawn, or if an en Passant move is possible
	 * @param piece A piece of any type whose information will be used to refine the possible moves of the current piece
	 */
	public void setPossibleMoves(Piece piece)
	{
		super.setPossibleMoves(piece);
		
		int thisPosition = getPosition();
		int thisRow = thisPosition % 10;
		int thisColumn = thisPosition / 10;
		int otherPosition = piece.getPosition();
		int otherRow = otherPosition % 10;
		int otherColumn = otherPosition / 10;
		int thisDirection = getTeam();
		
		LinkedList<Integer> possibleMoves = new LinkedList<Integer>(getPossibleMoves());
		ListIterator<Integer> moveIterator = possibleMoves.listIterator();
		int moveHolder;
		while(moveIterator.hasNext())
		{
			moveHolder = moveIterator.next();
			if(thisColumn == otherColumn && otherColumn == moveHolder / 10 && thisDirection * thisRow < thisDirection * otherRow)
			{ // This construct removes all possible moves that are blocked by a piece in front of a pawn
				if((thisDirection == 1 && moveHolder % 10 >= otherRow) || (thisDirection == -1 && moveHolder % 10 <= otherRow))
				{
					moveIterator.remove();
				}
			}
		}
		
		/* A diagonal possible move is added if there is a piece of the opposing team currently on a diagonal square, 
		  or if a diagonal move would lead to an en Passant capture */
		if(getTeam() != piece.getTeam() && (thisRow + thisDirection == otherRow 
				|| (piece.getEnPassant() && thisRow == otherRow && Math.abs(thisColumn - otherColumn) == 1)))
		{ 
			if(thisColumn + thisDirection == otherColumn)
			{
				possibleMoves.add(10 * (thisColumn + thisDirection) + (thisRow + thisDirection));
			}
			else if(thisColumn - thisDirection == otherColumn)
			{
				possibleMoves.add(10 * (thisColumn - thisDirection) + (thisRow + thisDirection));
			}
		}
		setPossibleMoves(possibleMoves);
	}

	/**
	 * Determines if the pawn is eligible to promote on the next move; simply, if it is one square away from the back rank
	 * @return true if the pawn is one square away from the back rank, false if not
	 */
	public boolean promotionPossible()
	{	
		int thisPosition = getPosition();
		int thisRow = thisPosition % 10;
		int direction = getTeam();
		
		if(thisRow + direction == 7 || thisRow + direction == 0)
		{ // If a pawn is one square away from its respective back rank, it is possible to promote (although it may not be a possible move)
			return true;
		}
		
		return false;
	}

	/**
	 * Mutator method, determines if this pawn has just moved two squares in the very last move
	 * @param ifPossible A truth value that reveals if this pawn has just made a 2-square move
	 */
	public void setEnPassant(boolean ifPossible)
	{
		enPassantPossible = ifPossible;
	}
	
	/**
	 * Accessor method, returns a value telling whether or not this pawn is eligible to be taken by an en passant move
	 * @return true if the pawn is eligible to be captured by an en passant, else false
	 */
	public boolean getEnPassant()
	{
		return enPassantPossible;
	}

}


