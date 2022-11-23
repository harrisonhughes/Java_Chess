package Chess;

import java.util.LinkedList;

/**
 * A class to hold all of the rules and information that the Knight piece has. This class inherits the Piece class and all 
 * of its information and rules that correspond to general behaviors of all pieces.
 */
public class Knight extends Piece{
	private final int WHITE_START_LEFT = 10;
	private final int WHITE_START_RIGHT = 60;
	private final int BLACK_START_LEFT = 17;
	private final int BLACK_START_RIGHT = 67;
	static int pieces = 0;
	
	/**
	 * Constructor, provides the team, piece type, and start position of the piece to the Piece superclass, and sends this 
	 * information to the board parameter to display its initial position.
	 * @param team A string code corresponding to the team of the piece, "W" for white, and "B" for black within this program
	 * @param board The chess board that keeps track of all of the pieces in the current game
	 */
	public Knight(String team, Board board)
	{
		setTeam(team);
		setPiece(team + "N");
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
	 * Generates the vacuum moves for this Knight piece based on its current position. Based on the rules of the knight, its vacuum moves are 
	 * the same as its squares attacked; this property eliminates the need for a separate method to calculate squares attacked
	 * @Override
	 */
	public void setVacuumMoves()
	{
		final int MAX_BOARD_INDEX = 7; 
		final int MIN_BOARD_INDEX = 0;
		final int KNIGHT_LONG_RANGE = 2;
		final int KNIGHT_SHORT_RANGE = 1;
		
		int row = getPosition() % 10;
		int column = (getPosition() - row) / 10;
		LinkedList<Integer> vacuumMoves = new LinkedList<Integer>();
		
		if((column + KNIGHT_LONG_RANGE) <= MAX_BOARD_INDEX)
		{
			if((row + KNIGHT_SHORT_RANGE) <= MAX_BOARD_INDEX)
			{
				vacuumMoves.add(10 * (column + KNIGHT_LONG_RANGE) + (row + KNIGHT_SHORT_RANGE));
			}
			
			if((row - KNIGHT_SHORT_RANGE) >= MIN_BOARD_INDEX)
			{
				vacuumMoves.add(10 * (column + KNIGHT_LONG_RANGE) + (row - KNIGHT_SHORT_RANGE));
			}
		}
		
		if((column - KNIGHT_LONG_RANGE) >= MIN_BOARD_INDEX)
		{
			if((row + KNIGHT_SHORT_RANGE) <= MAX_BOARD_INDEX)
			{
				vacuumMoves.add(10 * (column - KNIGHT_LONG_RANGE) + (row + KNIGHT_SHORT_RANGE));
			}
			
			if((row - KNIGHT_SHORT_RANGE) >= MIN_BOARD_INDEX)
			{
				vacuumMoves.add(10 * (column - KNIGHT_LONG_RANGE) + (row - KNIGHT_SHORT_RANGE));
			}
		}
		
		if((row + KNIGHT_LONG_RANGE) <= MAX_BOARD_INDEX)
		{
			if((column + KNIGHT_SHORT_RANGE) <= MAX_BOARD_INDEX)
			{
				vacuumMoves.add(10 * (column + KNIGHT_SHORT_RANGE) + (row + KNIGHT_LONG_RANGE));
			}
			
			if((column - KNIGHT_SHORT_RANGE) >= MIN_BOARD_INDEX)
			{
				vacuumMoves.add(10 * (column - KNIGHT_SHORT_RANGE) + (row + KNIGHT_LONG_RANGE));
			}
		}
		
		if((row - KNIGHT_LONG_RANGE) >= MIN_BOARD_INDEX)
		{
			if((column + KNIGHT_SHORT_RANGE) <= MAX_BOARD_INDEX)
			{
				vacuumMoves.add(10 * (column + KNIGHT_SHORT_RANGE) + (row - KNIGHT_LONG_RANGE));
			}
			
			if((column - KNIGHT_SHORT_RANGE) >= MIN_BOARD_INDEX)
			{
				vacuumMoves.add(10 * (column - KNIGHT_SHORT_RANGE) + (row - KNIGHT_LONG_RANGE));
			}
		}
		setSquaresAttacked(vacuumMoves); // Initializes squares attacked and possible moves with the comprehensive list of vacuum moves
		setPossibleMoves(vacuumMoves);
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


