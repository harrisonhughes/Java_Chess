package Chess;

import java.util.LinkedList;

public class Knight extends Piece{
	private final int WHITE_START_LEFT = 10;
	private final int WHITE_START_RIGHT = 60;
	private final int BLACK_START_LEFT = 17;
	private final int BLACK_START_RIGHT = 67;
	static int pieces = 0;
	public Knight(String team, Board board)
	{
		setTeam(team);
		super.setPiece(team + "N");
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
		int row = (getPosition() - column) / 10;
		if((row + 2) < 8)
		{
			if((column + 1) < 8)
			{
				vacuumMoves.add(10 * (row + 2) + (column + 1));
			}
			
			if((column - 1) > -1)
			{
				vacuumMoves.add(10 * (row + 2) + (column - 1));
			}
		}
		
		if((row - 2) > -1)
		{
			if((column + 1) < 8)
			{
				vacuumMoves.add(10 * (row - 2) + (column + 1));
			}
			
			if((column - 1) > -1)
			{
				vacuumMoves.add(10 * (row - 2) + (column - 1));
			}
		}
		
		if((column + 2) < 8)
		{
			if((row + 1) < 8)
			{
				vacuumMoves.add(10 * (row + 1) + (column + 2));
			}
			
			if((row - 1) > -1)
			{
				vacuumMoves.add(10 * (row -1 ) + (column + 2));
			}
		}
		
		if((column - 2) > -1)
		{
			if((row + 1) < 8)
			{
				vacuumMoves.add(10 * (row + 1) + (column - 2));
			}
			
			if((row - 1) > -1)
			{
				vacuumMoves.add(10 * (row - 1) + (column - 2));
			}
		}
		super.setVacuumMoves(vacuumMoves);
	}
	
	public void setSquaresAttacked(Piece piece)
	{
		LinkedList<Integer> squaresAttacked = new LinkedList<Integer>(getSquaresAttacked());
		super.setSquaresAttacked(squaresAttacked);
		super.setPossibleMoves(squaresAttacked);
	}
	
	public void setPossibleMoves(Piece piece)
	{
		super.setPossibleMoves(piece);
	}
}


