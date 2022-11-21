package Chess;

import java.util.LinkedList;
import java.util.ListIterator;

public class Queen extends Piece{
	private final int WHITE_START = 30;
	private final int BLACK_START = 37;
	public Queen(String team, Board board)
	{
		setTeam(team);
		super.setPiece(team + "Q");
		
		if(team.equals("W"))
		{
			setPosition(WHITE_START);
		}
		else
		{
			setPosition(BLACK_START);
		}
		board.updatePiece(this);
		setVacuumMoves();
	}
	
	public Queen(int position, String team)
	{
		setTeam(team);
		super.setPiece(team + "Q");
		setPosition(position);
		setVacuumMoves();
	}
	
	@Override
	public void setVacuumMoves()
	{
		LinkedList<Integer> vacuumMoves = new LinkedList<Integer>();

		int column = getPosition() % 10;
		int row = getPosition() / 10;
		int iterator = 1;
		
		for(int i = row + 1; i <= 7; i++)
		{
			vacuumMoves.add(10 * i + column);
		}
		for(int i = row - 1; i >= 0; i--)
		{
			vacuumMoves.add(10 * i + column);
		}
		for(int i = column + 1; i <= 7; i++)
		{
			vacuumMoves.add(10 * row + i);
		}
		for(int i = column - 1; i >= 0; i--)
		{
			vacuumMoves.add(10 * row + i);
		}
		
		while(iterator <= 7)
		{	
			if((row + iterator) <= 7 && (column + iterator) <= 7)
			{
				vacuumMoves.add(10 * (row + iterator) + (column + iterator));
			}
			if((row - iterator) >= 0 && (column + iterator) <= 7)
			{
				vacuumMoves.add(10 * (row - iterator) + (column + iterator));
			}
			if((row + iterator) <= 7 && (column - iterator) >= 0)
			{
				vacuumMoves.add(10 * (row + iterator) + (column - iterator));
			}
			if((row - iterator) >= 0 && (column - iterator) >= 0)
			{
				vacuumMoves.add(10 * (row - iterator) + (column - iterator));
			}
			iterator++;
		}
		super.setVacuumMoves(vacuumMoves);
	}
	
	public void setSquaresAttacked(Piece piece)
	{
		super.setPossibleMoves(piece);
		
		LinkedList<Integer> moveList = new LinkedList<Integer>(getSquaresAttacked());
		ListIterator<Integer> thisIterator = moveList.listIterator();
		int moveHolder;
		
		int thisPosition = getPosition();
		int thisRow = thisPosition / 10;
		int thisColumn = thisPosition % 10;
		int otherPosition = piece.getPosition();
		int otherRow = otherPosition / 10;
		int otherColumn = otherPosition % 10;
		
		while(thisIterator.hasNext())
		{
			moveHolder = thisIterator.next();
			if(Math.abs(thisRow - otherRow) == Math.abs(thisColumn - otherColumn))
			{
				if(otherRow > thisRow && otherColumn > thisColumn)
				{
					if((moveHolder / 10) > otherRow && (moveHolder % 10) > otherColumn)
					{
						thisIterator.remove();		
					}
				}
				else if(otherRow > thisRow && otherColumn < thisColumn)
				{
					if((moveHolder / 10) > otherRow && (moveHolder % 10) < otherColumn)
					{
						thisIterator.remove();
					}
				}
				else if(otherRow < thisRow && otherColumn > thisColumn)
				{
					if((moveHolder / 10) < otherRow && (moveHolder % 10) > otherColumn)
					{
						thisIterator.remove();
					}
				}
				else if(otherRow < thisRow && otherColumn < thisColumn)
				{
					if((moveHolder / 10) < otherRow && (moveHolder % 10) < otherColumn)
					{
						thisIterator.remove();
					}
				}
			}
			else
			{
				if(thisRow == otherRow && moveHolder / 10 == thisRow)
				{
					if(thisColumn > otherColumn)
					{
						if((moveHolder % 10) < otherColumn)
						{
							thisIterator.remove();
						}
					}
				
					else if(thisColumn < otherColumn)
					{
						if((moveHolder % 10) > otherColumn)
						{
							thisIterator.remove();
						}
					}
				}
				else if(thisColumn == otherColumn && moveHolder % 10 == thisColumn)
				{
					if(thisRow > otherRow)
					{
						if((moveHolder / 10) < otherRow)
						{
							thisIterator.remove();
						}
					}
					else if(thisRow < otherRow)
					{
						if((moveHolder / 10) > otherRow)
						{
							thisIterator.remove();
						}
					}
				}
			}
		}
		super.setSquaresAttacked(moveList);
		super.setPossibleMoves(moveList);
	}	
	
	public void setPossibleMoves(Piece piece)
	{
		super.setPossibleMoves(piece);
	}
}



