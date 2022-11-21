package Chess;

import java.util.LinkedList;
import java.util.ListIterator;

public class Pawn extends Piece{
	private int white_first = 01;
	private int black_first = 06;

	static int pieces = 0;
	public Pawn(String team, Board board)
	{
		setTeam(team);
		super.setPiece(team + "P");
		if(team.equals("W"))
		{
			setPosition(white_first + 10 * pieces);
		}
		else
		{
			setPosition(black_first + 10 * pieces);
		}
		pieces++;
		
		board.updatePiece(this);
		setVacuumMoves();
		
		if(pieces == 8)
		{
			pieces = 0;
		}
	}
	
	@Override
	public void setVacuumMoves()
	{
		int row = getPosition() % 10;
		int column = getPosition() / 10;
		LinkedList<Integer> vacuumMoves = new LinkedList<Integer>();
		
		int color = -1;
		if(super.getTeam())
		{
			color = 1;
		}
		
		vacuumMoves.add(10 * column + row + color);

		if(super.getFirstMove())
		{
			vacuumMoves.add(10 * column + row + (2 * color));
		}
		super.setVacuumMoves(vacuumMoves);
	}
	
	public void setSquaresAttacked(Piece piece)
	{
		LinkedList<Integer> squaresAttacked = new LinkedList<Integer>();
		int thisPosition = getPosition();
		int thisColumn = thisPosition / 10;
		int thisRow = thisPosition % 10;
		
		int color = -1;
		if(super.getTeam())
		{
			color = 1;
		}
		
		if(thisRow + color >= 0 && thisRow + color <= 7)
		{
			if(thisColumn + color <= 7 && thisColumn + color >= 0)
			{
				squaresAttacked.add(10 * (thisColumn + color) + (thisRow + color));
			}
			if(thisColumn - color <= 7 && thisColumn - color >= 0)
			{
				squaresAttacked.add(10 * (thisColumn - color) + (thisRow + color));
			}
		}		
		super.setSquaresAttacked(squaresAttacked);
	}
	
	public void setPossibleMoves(Piece piece)
	{
		super.setPossibleMoves(piece);
		
		LinkedList<Integer> moveList = new LinkedList<Integer>(getPossibleMoves());
		ListIterator<Integer> thisIterator = moveList.listIterator();
		
		int color = -1;
		if(super.getTeam())
		{
			color = 1;
		}
		
		int thisPosition = getPosition();
		int thisColumn = thisPosition / 10;
		int thisRow = thisPosition % 10;
		int otherPosition = piece.getPosition();
		int otherColumn = otherPosition / 10;
		int otherRow = otherPosition % 10;
		
		int moveHolder;
		while(thisIterator.hasNext())
		{
			moveHolder = thisIterator.next();
			if(thisColumn == otherColumn && otherColumn == moveHolder / 10 && color * thisRow < color * otherRow)
			{
				if((color == 1 && moveHolder % 10 >= otherRow) || (color == -1 && moveHolder % 10 <= otherRow))
				{
					thisIterator.remove();
				}
			}
		}
		
		if(getTeam() != piece.getTeam() && thisRow + color == otherRow)
		{
			if(thisColumn + color == otherColumn)
			{
				moveList.add(10 * (thisColumn + color) + (thisRow + color));
			}
			else if(thisColumn - color == otherColumn)
			{
				moveList.add(10 * (thisColumn - color) + (thisRow + color));
			}
		}
		super.setPossibleMoves(moveList);
	}

	public boolean promotionPossible()
	{
		int color = -1;
		if(super.getTeam())
		{
			color = 1;
		}
		
		int thisPosition = getPosition();
		int thisRow = thisPosition % 10;
		
		if(thisRow + color == 7 || thisRow + color == 0)
		{
			return true;
		}
		
		return false;
	}
}


