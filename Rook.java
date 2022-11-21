package Chess;

import java.util.LinkedList;
import java.util.ListIterator;

public class Rook extends Piece{
	private final int WHITE_START_LEFT = 00;
	private final int WHITE_START_RIGHT = 70;
	private final int BLACK_START_LEFT = 07;
	private final int BLACK_START_RIGHT = 77;
	static int pieces = 0;
	public Rook(String team, Board board)
	{
		setTeam(team);
		super.setPiece(team + "R");
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

		int row = getPosition() % 10;
		int column = (getPosition() - row) / 10;
		
		for(int i = column + 1; i <= 7; i++)
		{
			vacuumMoves.add(10 * i + row);
		}
		for(int i = column - 1; i >= 0; i--)
		{
			vacuumMoves.add(10 * i + row);
		}
		for(int i = row + 1; i <= 7; i++)
		{
			vacuumMoves.add(10 * column + i);
		}
		for(int i = row - 1; i >= 0; i--)
		{
			vacuumMoves.add(10 * column + i);
		}
		super.setVacuumMoves(vacuumMoves);
	}
	
	public void setSquaresAttacked(Piece piece)
	{
		LinkedList<Integer> moveList = new LinkedList<Integer>(getSquaresAttacked());
		ListIterator<Integer> thisIterator = moveList.listIterator();
		int moveHolder;
		
		int thisPosition = getPosition();
		int thisColumn = thisPosition / 10;
		int thisRow = thisPosition % 10;
		int otherPosition = piece.getPosition();
		int otherColumn = otherPosition / 10;
		int otherRow = otherPosition % 10;
	
		while(thisIterator.hasNext())
		{
			moveHolder = thisIterator.next();
			if(thisColumn == otherColumn)
			{
				if(thisRow > otherRow)
				{
					if((moveHolder % 10) < otherRow)
					{
						thisIterator.remove();
					}
				}
			
				else if(thisRow < otherRow)
				{
					if((moveHolder % 10) > otherRow)
					{
						thisIterator.remove();
					}
				}
			}
			else if(thisRow == otherRow)
			{
				if(thisColumn > otherColumn)
				{
					if((moveHolder / 10) < otherColumn)
					{
						thisIterator.remove();
					}
				}
				else if(thisColumn < otherColumn)
				{
					if((moveHolder / 10) > otherColumn)
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



