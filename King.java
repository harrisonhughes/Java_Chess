package Chess;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * A class to hold all of the rules and information that the King piece has. This class inherits the Piece class and all 
 * of its information and rules that correspond to general behaviors of all pieces.
 */
public class King extends Piece{
	private final int WHITE_START = 40;
	private final int BLACK_START = 47;
	
	/**
	 * Constructor, provides the team, piece type, and start position of the piece to the Piece superclass, and sends this 
	 * information to the board parameter to display its initial position.
	 * @param team A string code corresponding to the team of the piece, "W" for white, and "B" for black within this program
	 * @param board The chess board that keeps track of all of the pieces in the current game
	 */
	public King(String team, Board board)
	{
		setTeam(team);
		setPiece(team + "K");
		
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
	 * Generates the vacuum moves for this king piece based on its current position. Based on the rules of the king, its vacuum moves are 
	 * the same as its squares attacked; this property eliminates the need for a separate method to calculate squares attacked in this class
	 * @Override
	 */
	public void setVacuumMoves()
	{
		final int BOARD_WIDTH = 8; 
		final int BOARD_HEIGHT = BOARD_WIDTH;
		final int KING_RANGE = 1;
		
		int row = getPosition() % 10; 
		int column = getPosition() / 10; 
		LinkedList<Integer> vacuumMoves = new LinkedList<Integer>();

		for(int i = 0; i < BOARD_WIDTH; i++)
		{
			for(int j = 0; j < BOARD_HEIGHT; j++)
			{
				if(Math.abs(column - i) <= KING_RANGE && Math.abs(row - j) <= KING_RANGE && Math.abs(column - i) + Math.abs(row - j) != 0)
				{
					vacuumMoves.add(10 * i + j);
				}
			}
		}
		setSquaresAttacked(vacuumMoves); // Initializes squares attacked and possible moves with the comprehensive list of vacuum moves
		setPossibleMoves(vacuumMoves);
	}
	
	/**
	 * Copies the current list of possible moves and removes any move that is illegal based on the position and rules of the piece 
	 * in the parameter.
	 * @param piece A piece of any type whose information will be used to refine the possible moves of the current piece
	 * @Override
	 */
	public void setPossibleMoves(Piece piece)
	{
		super.setPossibleMoves(piece);
		
		LinkedList<Integer> possibleMoves = new LinkedList<Integer>(getPossibleMoves());
		ListIterator<Integer> moveIterator = possibleMoves.listIterator();
		if(piece.getTeam() != getTeam())
		{	
			while(moveIterator.hasNext())
			{
				int currentMove = moveIterator.next();
				ListIterator<Integer> otherIterator = piece.getSquaresAttacked().listIterator();
				while(otherIterator.hasNext())
				{	
					if(currentMove == otherIterator.next())
					{
						moveIterator.remove();
					}
				}
			}
		}
		setPossibleMoves(possibleMoves);
	}
	
	/**
	 * Checks if the parameter piece attacks the current position of this piece, and returns the boolean result
	 * @param piece The piece that may or may not put the King in check
	 * @return true if the King is currently in check, false if else
	 * @Override
	 */
	public boolean isCheck(Piece piece)
	{
		for(int i = 0; i < piece.getSquaresAttacked().size(); i++)
		{
			if(piece.getSquaresAttacked().get(i) == getPosition())
			{
				return true;
			}
		}
		return false;
	}
}


