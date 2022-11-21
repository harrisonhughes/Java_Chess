package Chess;

import java.util.LinkedList;
import java.util.ListIterator;

public class King extends Piece{
	private final int WHITE_START = 40;
	private final int BLACK_START = 47;
	
	public King(String team, Board board)
	{
		setTeam(team);
		super.setPiece(team + "K");
		
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
	
	@Override
	public void setVacuumMoves()
	{
		int row = getPosition() % 10;
		int column = getPosition() / 10;
		LinkedList<Integer> vacuumMoves = new LinkedList<Integer>();

		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				if(Math.abs(column - i) <= 1 && Math.abs(row - j) <= 1 && Math.abs(column - i) + Math.abs(row - j) != 0)
				{
					vacuumMoves.add(10 * i + j);
				}
			}
		}
		super.setVacuumMoves(vacuumMoves);
		super.setSquaresAttacked(vacuumMoves);
	}
	
	public void setSquaresAttacked(Piece piece)
	{
		return;
	}
	
	public void setPossibleMoves(Piece piece)
	{
		super.setPossibleMoves(piece);
		
		LinkedList<Integer> possibleMoves = new LinkedList<Integer>(getPossibleMoves());
		ListIterator<Integer> thisIterator = possibleMoves.listIterator();
		if(piece.getTeam() != getTeam())
		{	
			while(thisIterator.hasNext())
			{
				int thisMove = thisIterator.next();
				ListIterator<Integer> otherIterator = piece.getSquaresAttacked().listIterator();
				while(otherIterator.hasNext())
				{	
					if(thisMove == otherIterator.next())
					{
						thisIterator.remove();
					}
				}
			}
		}
		
		super.setPossibleMoves(possibleMoves);
	}
	
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


