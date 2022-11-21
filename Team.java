package Chess;

import java.util.LinkedList;
import java.util.ListIterator;

public class Team {
	private LinkedList<Piece> teamPieces = new LinkedList<Piece>();
	private boolean check = false;
	
	public Team(String color, Board board)
	{
		teamPieces.add(new King(color, board));
		teamPieces.add(new Queen(color, board));
		teamPieces.add(new Rook(color, board));
		teamPieces.add(new Rook(color, board));
		teamPieces.add(new Bishop(color, board));
		teamPieces.add(new Bishop(color, board));
		teamPieces.add(new Knight(color, board));
		teamPieces.add(new Knight(color, board));
		teamPieces.add(new Pawn(color, board));
		teamPieces.add(new Pawn(color, board));
		teamPieces.add(new Pawn(color, board));
		teamPieces.add(new Pawn(color, board));
		teamPieces.add(new Pawn(color, board));
		teamPieces.add(new Pawn(color, board));
		teamPieces.add(new Pawn(color, board));
		teamPieces.add(new Pawn(color, board)); 
	}
	
	public Team(Team otherTeam)
	{
		teamPieces = new LinkedList<Piece>(otherTeam.getPieceList());
	}

	public boolean movePiece(String chessMove, Board board, Team otherTeam)
	{			
		if(castleKing(chessMove, board, otherTeam))
		{
			return true;
		}
			
		Piece currentPiece = getSpecificPiece(chessMove, otherTeam);
		
		if(currentPiece == null)
		{
			return false;
		}
		
		boolean promote = false;
		String promoteString = null;
		if(currentPiece.promotionPossible())
		{
			if(chessMove.contains("="))
			{
				promote = true;
				promoteString = chessMove;
				chessMove = chessMove.substring(0, chessMove.length() - 2); 
			}
			else
			{
				return false;
			}
		}
		
		boolean validMove = false;
		validMove = currentPiece.movePiece(chessMove, board);
		if(validMove)
		{
			if(promote)
			{
				currentPiece = promotePawn(promoteString, currentPiece, otherTeam, board);
			}
			for(int i = 0; i < otherTeam.getPieceList().size(); i++)
			{
				if(otherTeam.getPieceList().get(i).getPosition() == currentPiece.getPosition())
				{
					otherTeam.removePiece(otherTeam.getPieceList().get(i));
				}
			}
			for(int i = 0; i < teamPieces.size(); i++)
			{
				setMoves(teamPieces.get(i), otherTeam);
			}
			currentPiece.setFirstMove();
		}
		
		kingInCheck(otherTeam);
		return validMove;
	}
	
	public void printPossibleMoves(Team otherTeam)
	{	
		for(int i = 0; i < teamPieces.size(); i++)
		{
			Piece currentPiece = teamPieces.get(i);
			setMoves(currentPiece, otherTeam);
			System.out.println("Possible Moves for: " + currentPiece.getPiece());
			for(int j = 0; j < currentPiece.getPossibleMoves().size(); j++)
			{
					System.out.println(currentPiece.intToChess(currentPiece.getPossibleMoves().get(j)));
			}
			System.out.print("\n");
		}
		
	}
	
	public void setMoves(Piece currentPiece, Team otherTeam)
	{
		setPossibleMoves(currentPiece, otherTeam);
		removeCheckMoves(currentPiece, otherTeam);
	}
	
	public void setVacuumMoves(Piece currentPiece, Team otherTeam)
	{
		for(int i = 0; i < otherTeam.getPieceList().size(); i++)
		{
			otherTeam.getPieceList().get(i).setVacuumMoves();
		}
		currentPiece.setVacuumMoves();
	}
	
	public void setSquaresAttacked(Piece currentPiece, Team otherTeam)
	{
		setVacuumMoves(currentPiece, otherTeam);
		for(int i = 0; i < otherTeam.getPieceList().size(); i++)
		{
			for(int j = 0; j < otherTeam.getPieceList().size(); j++)
			{
				otherTeam.getPieceList().get(i).setSquaresAttacked(otherTeam.getPieceList().get(j));
			}
			for(int m = 0; m < teamPieces.size(); m++)
			{
				otherTeam.getPieceList().get(i).setSquaresAttacked(teamPieces.get(m));
			}
		}
		
		for(int i = 0; i < otherTeam.getPieceList().size(); i++)
		{
			currentPiece.setSquaresAttacked(otherTeam.getPieceList().get(i));
		}
		for(int j = 0; j < teamPieces.size(); j++)
		{
			currentPiece.setSquaresAttacked(teamPieces.get(j));
		}
	}
	
	public void setPossibleMoves(Piece currentPiece, Team otherTeam)
	{
		setSquaresAttacked(currentPiece, otherTeam);
		for(int i = 0; i < otherTeam.getPieceList().size(); i++)
		{
			for(int j = 0; j < otherTeam.getPieceList().size(); j++)
			{
				otherTeam.getPieceList().get(i).setPossibleMoves(otherTeam.getPieceList().get(j));
			}
			for(int m = 0; m < teamPieces.size(); m++)
			{
				otherTeam.getPieceList().get(i).setPossibleMoves(teamPieces.get(m));
			}
		}
		
		for(int i = 0; i < otherTeam.getPieceList().size(); i++)
		{
			currentPiece.setPossibleMoves(otherTeam.getPieceList().get(i));
		}
		for(int j = 0; j < teamPieces.size(); j++)
		{
			currentPiece.setPossibleMoves(teamPieces.get(j));
		}
	}
	
	public void removeCheckMoves(Piece currentPiece, Team otherTeam)
	{
		int positionHolder = currentPiece.getPosition();
		LinkedList<Integer> moveList = new LinkedList<Integer>(currentPiece.getPossibleMoves());
		ListIterator<Integer> thisIterator = moveList.listIterator();
		
		while(thisIterator.hasNext())
		{
			int currentMove = thisIterator.next();
			currentPiece.setPosition(currentMove);
			setPossibleMoves(currentPiece, otherTeam);
			
			LinkedList<Piece> otherPieces = new LinkedList<Piece>(otherTeam.getPieceList());
			ListIterator<Piece> otherIterator = otherPieces.listIterator();
			
			while(otherIterator.hasNext())
			{	
				Piece tempPiece = otherIterator.next();
				
				if(currentPiece.getPosition() == tempPiece.getPosition())
				{	
					tempPiece.setSquaresAttacked(new LinkedList<Integer>());
				}
			}
			
			otherTeam.kingInCheck(this);
			if(getCheck())
			{
				thisIterator.remove();
			}
		}
		
		currentPiece.setPosition(positionHolder);	
		setPossibleMoves(currentPiece, otherTeam);
		currentPiece.setPossibleMoves(moveList);
	}
	
	public boolean castleKing(String chessMove, Board board, Team otherTeam)
	{
		final int MOVE_COLUMN = 10;
		int columnShift;
		int rookWhitePosition;
		int rookBlackPosition;
		if(chessMove.equals("O-O"))
		{
			columnShift = MOVE_COLUMN;
			rookWhitePosition = 70;
			rookBlackPosition = 77;
		}
		else if(chessMove.equals("O-O-O"))
		{
			columnShift = -1 * MOVE_COLUMN;
			rookWhitePosition = 00;
			rookBlackPosition = 07;
		}
		else
		{
			return false;
		}
		
		Piece castleRook = null;
		for(int i = 0; i < teamPieces.size(); i++)
		{
			if((teamPieces.get(i).getPosition() == rookWhitePosition || teamPieces.get(i).getPosition() == rookBlackPosition) && teamPieces.get(i).getFirstMove())
			{
				castleRook = teamPieces.get(i);
			}
		}
		if(castleRook == null)
		{
			return false;
		}
			
		int castleMove = teamPieces.get(0).getPosition() + (columnShift);
		String castleString = teamPieces.get(0).intToChess(castleMove);
		
		if(chessMove.equals("O-O-O"))
		{
			castleMove += 2 * columnShift;
			castleString = castleRook.intToChess(castleMove);
			if(!castleRook.movePiece(castleString, board))
			{
				return false;
			}
			castleMove += -2 * columnShift;
			castleString = castleRook.intToChess(castleMove);
		}
		
		if(teamPieces.get(0).movePiece(castleString, board))
		{
			setMoves(teamPieces.get(0), otherTeam);
			castleMove += columnShift;
			castleString = teamPieces.get(0).intToChess(castleMove);
			if(teamPieces.get(0).movePiece(castleString, board))
			{
				board.deleteOldPiece(castleRook);
				castleRook.setPosition(castleMove - columnShift);
				board.updatePiece(castleRook);
				return true;
			}
			else
			{
				castleMove += -2 * columnShift;
				castleString = teamPieces.get(0).intToChess(castleMove);
				teamPieces.get(0).movePiece(castleString, board);
			}
		}
			return false;
	}
	
	public Piece getSpecificPiece(String chessMove, Team otherTeam)
	{
		boolean ambiguous = false;
		String rowOrColumn = null;
		if(chessMove.contains("="))
		{
			chessMove = chessMove.substring(0, chessMove.length() - 2);
		}
		if(chessMove.length() == 4)
		{
			ambiguous = true;
			char pieceChoice = chessMove.charAt(1);
			if(pieceChoice <= 'h' && pieceChoice >= 'a')
			{
				rowOrColumn = "" + pieceChoice;
			}
			else if(pieceChoice <= '8' && pieceChoice >= '1')
			{
				rowOrColumn = "" + pieceChoice;
			}
		}
		
		Piece specificPiece = null;
		int piecesFound = 0;
		String pieceChoice = chessMove.substring(0, 1);		
		for(int i = 0; i < teamPieces.size(); i++)
		{
			String pieceType = teamPieces.get(i).getPiece().substring(1, 2);
			setMoves(teamPieces.get(i), otherTeam);
			if(pieceChoice.equals(pieceType))
			{
				if(ambiguous && teamPieces.get(i).intToChess(teamPieces.get(i).getPosition()).substring(1, 2).equals(rowOrColumn))
				{
					specificPiece = teamPieces.get(i);
					piecesFound ++;
				}
				else if(ambiguous && teamPieces.get(i).intToChess(teamPieces.get(i).getPosition()).substring(2, 3).equals(rowOrColumn))
				{
					specificPiece = teamPieces.get(i);
					piecesFound ++;
				}
				else if(!ambiguous)
				{
					for(int j = 0; j < teamPieces.get(i).getPossibleMoves().size(); j++)
					{
						if(teamPieces.get(i).getPossibleMoves().get(j) == teamPieces.get(i).chessToInt(chessMove))
						{
							specificPiece = teamPieces.get(i);
							piecesFound ++;
						}
					}
				}
			}
		}
		if(piecesFound != 1)
		{
			return null;
		}
		return specificPiece;
	}
	
	public Piece promotePawn(String chessMove, Piece pawn, Team otherTeam, Board board)
	{

		int promoteSquare = pawn.getPosition();
		String promoteTeam = pawn.getPiece().substring(0, 1);
		String promotionType = chessMove.substring(chessMove.length() - 1, chessMove.length());
		if(promotionType.equals("Q"))
		{
			Queen newQueen = new Queen(promoteSquare, promoteTeam);
			removePiece(pawn);
			teamPieces.add(newQueen);
			board.deleteOldPiece(pawn);
			board.updatePiece(newQueen);
			return newQueen;
		}
		return null;
	}
	
	public boolean kingInCheckmate(Team otherTeam)
	{
		for(int i = 0; i < teamPieces.size(); i++)
		{
			setMoves(teamPieces.get(i), otherTeam);
			if(teamPieces.get(i).getPossibleMoves().size() > 0)
			{
				otherTeam.kingInCheck(this);
				return false;
			}
		}
		// Check and Stale
		return true;
	}
	
	public void kingInCheck(Team otherTeam)
	{
		for(int i = 0; i < teamPieces.size(); i++)
		{
			if(otherTeam.getPieceList().get(0).isCheck(teamPieces.get(i)))
			{
				otherTeam.setCheck(true);
				return;
			}
		}
		otherTeam.setCheck(false);
	}
	
	public void setCheck(boolean check)
	{
		this.check = check;
	}
	
	public boolean getCheck()
	{
		return check;
	}
	
	public LinkedList<Piece> getPieceList()
	{
		return teamPieces;
	}
	
	public void removePiece(Piece piece)
	{
		ListIterator<Piece> teamIterator = teamPieces.listIterator();
		Piece currentPiece;
		while(teamIterator.hasNext())
		{
			currentPiece = teamIterator.next();
			if(piece.getPosition() == currentPiece.getPosition())
			{
				teamIterator.remove();
				return;
			}
		}
	}

	

}

