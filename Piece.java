package Chess;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * A class to hold the rules and information that all chess pieces have in common. This class is inherited by all
 * specific piece subclasses, where rules specific to each piece are implemented.
 */
public class Piece {
	private int team;
	private int currentPosition;
	private LinkedList<Integer> squaresAttacked = new LinkedList<Integer>();
	private LinkedList<Integer> possibleMoves = new LinkedList<Integer>();
	private boolean firstMove = true;
	private String pieceType;
	
	/**
	 * If provided with a move that is possible for this specific piece to traverse to, the game board will move the specific
	 * piece to the requested square.
	 * @param chessMove a String in chess notation form that designates a specific position that this piece will be moved to
	 * @param board The game board that holds the current piece positions
	 * @return true if the requested move was carried out, false if the requested move was not possible for this specific piece
	 */
	public boolean movePiece(String chessMove, Board board)
	{	
		int newSquare = chessToInt(chessMove);
		
		boolean validMove = false;
		for(int i = 0; i < possibleMoves.size(); i++)
		{
			if(newSquare == possibleMoves.get(i))
			{
				validMove = true;
			}
		}
		
		if(validMove)
		{
			board.deleteOldPiece(this);
			setPosition(newSquare);
			board.updatePiece(this);
		}	
		return validMove;
	}

	/**
	 * If the parameter is a piece on the same team as the current piece, remove the possible move that places the current piece on
	 * the position of the parameter, if necessary. (A piece cannot move onto a square occupied by a piece of the same team: this is 
	 * the only rule regarding possible moves for a piece that is common to all piece types. This method is overridden by all piece
	 * subclasses to generate a list of possible moves based on the specific piece; after that is done, this superclass method is called.
	 * @param piece The piece whose position we will reference to the possible moves of the current piece, if they are on the same team.
	 */
	public void setPossibleMoves(Piece piece)
	{
		LinkedList<Integer> possibleMoves = new LinkedList<Integer>(getPossibleMoves());
		ListIterator<Integer> moveIterator = possibleMoves.listIterator();
		
		if(piece.getTeam() == getTeam())	
		{
			while(moveIterator.hasNext())
			{
				if(moveIterator.next() == piece.getPosition())
				{
					moveIterator.remove();
				}
			}
		}
		this.possibleMoves = possibleMoves;
	}
	
	/**
	 * Mutator method that replaces the current possibleMoves attribute with the contents of the parameter
	 * @param possibleMoves A new list of integers that will be copied to the possibleMoves attribute
	 */
	public void setPossibleMoves(LinkedList<Integer> possibleMoves)
	{
		this.possibleMoves = new LinkedList<Integer>(possibleMoves);
	}
	
	/**
	 * Accessor method that returns the current contents of the possibleMoves attribute
	 * @return the current contents of the possibleMoves list attribute
	 */
	public LinkedList<Integer> getPossibleMoves()
	{
		return possibleMoves;
	}
	
	/**
	 * This method is always overridden by the piece subclasses: there is no common behavior between piece types regarding the squares
	 * that each specific piece type attacks
	 * @param piece
	 */
	public void setSquaresAttacked(Piece piece)
	{
		return;
	}
	
	/**
	 * Mutator method that replaces the current squaresAttacked attribute with the contents of the parameter
	 * @param squaresAttacked A new list of integers that will be copied to the squaresAttacked attribute
	 */
	public void setSquaresAttacked(LinkedList<Integer> squaresAttacked)
	{
		this.squaresAttacked = new LinkedList<Integer>(squaresAttacked);
	}

	/**
	 * Accessor method that returns the current contents of the squaresAttacked attribute
	 * @return the current contents of the squaresAttacked list attribute
	 */
	public LinkedList<Integer> getSquaresAttacked()
	{
		return squaresAttacked;
	}
	
	/**
	 * This method is always overridden by the piece subclasses: there is no common behavior between piece types regarding the 
	 * "vacuum moves" for each specific piece. (In the context of this program, "vacuum moves" is a list of integers, where each 
	 * corresponds to a square that the specific piece could move to from its current position, if it were the only piece on the board. 
	 */
	public void setVacuumMoves()
	{
		return;
	}
	
	/**
	 * Mutator method that sets the piece attribute that will be displayed on the board: in this program, that is two characters; the team, followed by
	 * the piece type (e.g. WK, WB, BP)
	 * @param piece the two character string that identifies the team and piece type of this specific piece
	 */
	public void setPiece(String piece)
	{
		pieceType = piece;
	}
	
	/**
	 * Accessor method that returns the piece type
	 * @return the two character piece type
	 */
	public String getPiece()
	{
		return pieceType;
	}
	
	/**
	 * Sets the team attribute to "1" if the piece is on the "W" white team, and sets it to "-1" if else. Having the team attribute be of the 
	 * boolean data type may make more sense, but having integer values of opposite sign allows for simpler code in the program when dealing
	 * with team color (specifically with pawns; since they move in opposite direction based on their color, it is helpful to have a variable 
	 * that can standardize their movements in the eyes of the board).  
	 * @param team The string that designates the team of the piece
	 */
	public void setTeam(String team)
	{
		if(team.equals("W"))
		{
			this.team = 1;
		}
		else
		{
			this.team = -1;
		}
	}
	
	/**
	 * Accessor method that returns the contents of the team attribute
	 * @return 1 if the current piece is on the white team, -1 if the current piece is on the black team
	 */
	public int getTeam()
	{
		return team;
	}
	
	/**
	 * Mutator method that copies the parameter to the currentPosition attribute. Rather than a string, this is stored as an integer value that 
	 * the board array understands
	 * @param position an integer that corresponds to the position of a square on the board
	 */
	public void setPosition(int position)
	{
		currentPosition = position;
	}
	
	/**
	 * Accessor method to obtain the integer value of the current position of this piece
	 * @return the value of the currentPosition attribute
	 */
	public int getPosition()
	{
		return currentPosition;
	}

	/**
	 * This method is called on a piece after it has been moved, permanently setting it's "firstMove" attribute to false. The firstMove attribute
	 * exists in order to allow or not allow en passant or king castling moves. 
	 */
	public void setFirstMove()
	{
		firstMove = false;
	}
	
	/**
	 * Accessor method that returns the value of the firstMove attribute
	 * @return false if the piece has been moves at least once, true if else. 
	 */
	public boolean getFirstMove()
	{
		return firstMove;
	}
	
	/**
	 * Always overridden by the pawn subclass to determine if en passant is a possible move; no other piece types have this rule, so no code is needed
	 * @param ifPossible 
	 */
	public void setEnPassant(boolean ifPossible)
	{
		return;
	}
	
	/**
	 * Always overridden by the pawn subclass, which holds its own en passant attribute. All other piece types call this method, which will always
	 * return false
	 * @return false for every call
	 */
	public boolean getEnPassant()
	{
		return false;
	}
	
	/**
	 * Always overridden by the pawn subclass, which holds its own attribute for this rule. All other piece types call this method, which will always
	 * return false
	 * @return false for every call
	 */
	public boolean promotionPossible()
	{
		return false;
	}
	
	/**
	 * Always overridden by the king subclass, which holds its own calculation of this rule. All other piece types call this method, which will always
	 * return false
	 * @param piece
	 * @return false for every call
	 */
	public boolean isCheck(Piece piece) 
	{
		return false;
	}

	/**
	 * Takes in a parameter of a string in chess notation, and changes it into an integer value of the position that the string refers to. This allows
	 * the board class to understand which square the variable is referring to
	 * @param chessMove The move in chess notation corresponding to a certain square on the board
	 * @return An integer value corresponding to the same position as the parameter
	 */
	public int chessToInt(String chessMove)
	{	
		char stringRow = chessMove.charAt(chessMove.length() - 2);
		String stringCol = chessMove.substring(chessMove.length() - 1,chessMove.length());
		
		int column = Integer.parseInt(stringCol) - 1;
		int row = 0;
		while (stringRow > 'a')
		{
			row ++;
			stringRow --;
		}
		
		return 10 * row + column;
	}
	
	/**
	 * Takes in an integer that corresponds to a position on the board, and transforms it into chess notation, with the character that matches the piece
	 * type of this specific piece at the front
	 * @param position An integer corresponding to a position on the chess board
	 * @return A string in chess notation corresponding to a specific chess move
	 */
	public String intToChess(int position)
	{
		int column = (position % 10) + 1;
		int rowNum = (position / 10) + 1;
		char row = 'a';
		while(rowNum > 1)
		{
			row ++;
			rowNum --;
		}
		String chessMove = getPiece().substring(1, 2) + row + column ;
		
		return chessMove;
	}
}


