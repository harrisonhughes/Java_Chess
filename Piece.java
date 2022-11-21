package Chess;

import java.util.LinkedList;
import java.util.ListIterator;

public class Piece {
	private final boolean WHITE = true;
	private final boolean BLACK = false;
	private boolean team;
	private boolean firstMove = true;
	private int currentPosition;
	private LinkedList<Integer> vacuumMoves = new LinkedList<Integer>();
	private LinkedList<Integer> squaresAttacked = new LinkedList<Integer>();
	private LinkedList<Integer> possibleMoves = new LinkedList<Integer>();
	private String pieceType;
		
	public LinkedList<Integer> getSquaresAttacked()
	{
		return squaresAttacked;
	}
	
	public void setSquaresAttacked(LinkedList<Integer> squaresAttacked)
	{
		this.squaresAttacked = new LinkedList<Integer>(squaresAttacked);
	}
	
	public void setSquaresAttacked(Piece piece)
	{
		// Polymorphism
	}
	
	public void setPossibleMoves(Piece piece)
	{
		LinkedList<Integer> possibleMoves = new LinkedList<Integer>(getPossibleMoves());
		ListIterator<Integer> thisIterator = possibleMoves.listIterator();
		
		if(piece.getTeam() == getTeam())	
		{
			while(thisIterator.hasNext())
			{
				if(thisIterator.next() == piece.getPosition())
				{
					thisIterator.remove();
				}
			}
		}
		this.possibleMoves = possibleMoves;
	}
	
	public void setPossibleMoves(LinkedList<Integer> possibleMoves)
	{
		this.possibleMoves = new LinkedList<Integer>(possibleMoves);
	}
	
	public LinkedList<Integer> getPossibleMoves()
	{
		return possibleMoves;
	}
	
	public void setPiece(String piece)
	{
		pieceType = piece;
	}
	
	public String getPiece()
	{
		return pieceType;
	}
	
	public void setTeam(String team)
	{
		if(team.equals("w") || team.equals("W"))
		{
			this.team = WHITE;
		}
		else
		{
			this.team = BLACK;
		}
	}
	
	public boolean getTeam()
	{
		return team;
	}
	
	public void setPosition(int position)
	{
		currentPosition = position;
	}
	
	public int getPosition()
	{
		return currentPosition;
	}
	
	public void setVacuumMoves(LinkedList<Integer> vacuumMoves)
	{
		this.vacuumMoves = new LinkedList<Integer>(vacuumMoves);
		this.squaresAttacked = new LinkedList<Integer>(vacuumMoves);
		this.possibleMoves = new LinkedList<Integer>(vacuumMoves);
	}
	
	public void setVacuumMoves()
	{
		// Polymorphism
	}
	
	public LinkedList<Integer> getVacuumMoves()
	{
		return vacuumMoves;
	}
	
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
	
	public boolean isCheck(Piece piece) 
	{
		return false;
	}

	public void setFirstMove()
	{
		firstMove = false;
	}
	
	public boolean getFirstMove()
	{
		return firstMove;
	}

	public boolean promotionPossible()
	{
		return false;
	}
}


