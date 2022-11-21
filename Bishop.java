package Chess;

import java.util.LinkedList;
import java.util.ListIterator;

public class Bishop extends Piece{
	private final int WHITE_START_LEFT = 20;
	private final int WHITE_START_RIGHT = 50;
	private final int BLACK_START_LEFT = 27;
	private final int BLACK_START_RIGHT = 57;
	static int pieces = 0;
	public Bishop(String team, Board board)
	{
		setTeam(team);
		super.setPiece(team + "B");
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
		setVacuumMoves();
	}
	
	@Override
	public void setVacuumMoves()
	{
		LinkedList<Integer> vacuumMoves = new LinkedList<Integer>();

		int column = getPosition() % 10;
		int row = getPosition() / 10;
		int iterator = 1;
		
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
		}
		super.setSquaresAttacked(moveList);
		super.setPossibleMoves(moveList);
	}	
	
	public void setPossibleMoves(Piece piece)
	{
		super.setPossibleMoves(piece);
	}
}



