package Chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;

public class Team {
	private LinkedList<Piece> teamPieces = new LinkedList<Piece>();
	private ArrayList<int[]> boardSnapshot = new ArrayList<int[]>();
	private static int noPawnMoveOrCapture = 0;
	private boolean pawnMove;
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
		try
		{
			for(int i = 0; i < teamPieces.size(); i++)
			{
				setMoves(teamPieces.get(i), otherTeam);
			}
			
			chessMove = formatChessMove(chessMove);
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
					System.out.print("Must clarify promotion type, try again: ");
					return false;
				}
			}

			if(currentPiece.movePiece(chessMove, board))
			{
				if(promote)
				{
					currentPiece = promotePawn(promoteString, currentPiece, otherTeam, board);
				}
				for(int i = 0; i < otherTeam.getPieceList().size(); i++)
				{
					if(otherTeam.getPieceList().get(i).getPosition() == currentPiece.getPosition())
					{
						otherTeam.removePiece(otherTeam.getPieceList().get(i), board);
					}
					else if(otherTeam.getPieceList().get(i).getEnPassant() 
							&& (currentPiece.getPosition() / 10) == (otherTeam.getPieceList().get(i).getPosition() / 10) 
							&& (currentPiece.getPosition() % 10) == ((otherTeam.getPieceList().get(i).getPosition() % 10) + currentPiece.getTeam()))
					{
						board.deleteOldPiece(otherTeam.getPieceList().get(i));
						otherTeam.removePiece(otherTeam.getPieceList().get(i), board);
					}
				}
				for(int i = 0; i < teamPieces.size(); i++)
				{
					teamPieces.get(i).setEnPassant(false);
				}
				if(currentPiece.getFirstMove() && ((currentPiece.getPosition() % 10 == 3) || (currentPiece.getPosition() % 10 == 4)))
				{
					currentPiece.setEnPassant(true);
				}
				currentPiece.setFirstMove();
			}
			return true;
		}
		catch(RuntimeException e)
		{
			System.out.print("Invalid move, try again: ");
			return false;
		}
	}
	
	public void printPossibleMoves(Team otherTeam)
	{	
		for(int i = 0; i < teamPieces.size(); i++)
		{
			Piece currentPiece = teamPieces.get(i);
			setMoves(currentPiece, otherTeam);
			System.out.println("Possible Moves for " + currentPiece.getPiece() + " on " + currentPiece.intToChess(currentPiece.getPosition()).substring(1, 3) + ": ");
			for(int j = 0; j < currentPiece.getPossibleMoves().size(); j++)
			{
					System.out.print(currentPiece.intToChess(currentPiece.getPossibleMoves().get(j)));
					if(j < currentPiece.getPossibleMoves().size() - 1)
					{
						System.out.print(", ");
					}
					else
					{
						System.out.print("\n");
					}
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
		if(chessMove.equals("0-0"))
		{
			columnShift = MOVE_COLUMN;
			rookWhitePosition = 70;
			rookBlackPosition = 77;
		}
		else if(chessMove.equals("0-0-0"))
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
		
		boolean piecesInWay = true;
		for(int i = 0; i < castleRook.getSquaresAttacked().size(); i++)
		{
			if(teamPieces.get(0).getPosition() == castleRook.getSquaresAttacked().get(i))
			{
				piecesInWay = false;
			}
		}
		if(piecesInWay)
		{
			return false;
		}
		
		
		int castleMove = teamPieces.get(0).getPosition() + (columnShift);
		String castleString = teamPieces.get(0).intToChess(castleMove);
		if(teamPieces.get(0).movePiece(castleString, board) && teamPieces.get(0).getFirstMove())
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
	
	public boolean enPassant(String chessMove, Board board, Team otherTeam)
	{
		return false;
	}
	
	public Piece getSpecificPiece(String chessMove, Team otherTeam)
	{
		boolean ambiguous = false;
		String rowOrColumn = "";
		String rowAndColumn = "";
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
		else if(chessMove.length() == 5)
		{
			ambiguous = true;
			char rowChoice = chessMove.charAt(1);
			char columnChoice = chessMove.charAt(2);
			if(rowChoice <= 'h' && rowChoice >= 'a')
			{
				rowAndColumn = "" + rowChoice;
			}
			if(columnChoice <= '8' && columnChoice >= '1')
			{
				rowAndColumn += columnChoice;
			}
		}
		
		Piece specificPiece = null;
		int piecesFound = 0;
		String pieceChoice = chessMove.substring(0, 1);		
		for(int i = 0; i < teamPieces.size(); i++)
		{
			String pieceType = teamPieces.get(i).getPiece().substring(1, 2);
			if(pieceChoice.equals(pieceType))
			{
				if(ambiguous && (teamPieces.get(i).intToChess(teamPieces.get(i).getPosition()).substring(1, 3).equals(rowAndColumn)  
						|| teamPieces.get(i).intToChess(teamPieces.get(i).getPosition()).substring(1, 2).equals(rowOrColumn)
						|| teamPieces.get(i).intToChess(teamPieces.get(i).getPosition()).substring(2, 3).equals(rowOrColumn)))
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
		if(piecesFound > 1)
		{
			System.out.print("Ambiguous move, try again: ");
			return null;
		}
		if(piecesFound == 0)
		{
			System.out.print("Invalid move, try again: ");
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
			removePiece(pawn, board);
			teamPieces.add(newQueen);
			board.deleteOldPiece(pawn);
			board.updatePiece(newQueen);
			return newQueen;
		}
		return null;
	}
	
	public void checkDraw(Team otherTeam)
	{
		int thisTotalPieces = teamPieces.size();
		int otherTotalPieces = otherTeam.getPieceList().size();
		int currentPieces = thisTotalPieces + otherTotalPieces;
		boolean insuffMaterial = true;
		if(thisTotalPieces <= 2 && otherTotalPieces <= 2)
		{
			for(int i = 0; i < thisTotalPieces; i++)
			{
				if(teamPieces.get(i).getPiece().substring(1).equals("Q") ||  teamPieces.get(i).getPiece().substring(1).equals("R") 
						|| teamPieces.get(i).getPiece().substring(1).equals("P"))
				{
					insuffMaterial = false;
				}
			}
			for(int i = 0; i < otherTotalPieces; i++)
			{
				if(otherTeam.getPieceList().get(i).getPiece().substring(1).equals("Q") ||  otherTeam.getPieceList().get(i).getPiece().substring(1).equals("R")
						|| otherTeam.getPieceList().get(i).getPiece().substring(1).equals("P"))
				{
					insuffMaterial = false;
				}
			}
			if(insuffMaterial)
			{
				System.out.print("Draw by Insufficient Material");
				System.exit(0);
			}
		}
		
		final int START_PIECES = 32;
		int lastSnapshotPieces = START_PIECES;
		if(boardSnapshot.size() >= 1)
		{
			lastSnapshotPieces = boardSnapshot.get(boardSnapshot.size() - 1).length;
		}

		if(pawnMove || currentPieces != lastSnapshotPieces)
		{
			boardSnapshot = new ArrayList<int[]>();
			noPawnMoveOrCapture = 0;
		}
		else
		{
			noPawnMoveOrCapture ++;
		}
		
		int[] currentBoard = new int[currentPieces];
		
		for(int i = 0; i < thisTotalPieces; i++)
		{
			currentBoard[i] = teamPieces.get(i).getPosition();
		}
		for(int i = 0; i < otherTotalPieces; i++)
		{
			currentBoard[i + thisTotalPieces] = otherTeam.getPieceList().get(i).getPosition();
		}
		boardSnapshot.add(currentBoard);

		int repitition = 0;
		for(int i = 0; i < boardSnapshot.size(); i++)
		{
			if(Arrays.equals(currentBoard, boardSnapshot.get(i)))
			{
				repitition ++;
			}
		}
		if(noPawnMoveOrCapture == 100)
		{
			System.out.print("Draw by 50-move rule");
			System.exit(0);
		}
		else if(repitition == 3)
		{
			System.out.print("Draw by Threefold Repitition");
			System.exit(0);
		}
	}
	
	public void checkGameStatus(Team otherTeam)
	{
		otherTeam.kingInCheck(this);
		for(int i = 0; i < teamPieces.size(); i++)
		{
			setMoves(teamPieces.get(i), otherTeam);
			if(teamPieces.get(i).getPossibleMoves().size() > 0)
			{
				otherTeam.checkDraw(this);
				otherTeam.kingInCheck(this);
				return;
			}
		}
		if(getCheck())
		{
			if(getPieceList().get(0).getTeam() == 1)
			{
				System.out.println("Checkmate. Black Wins!");
				System.exit(0);
			}
			System.out.println("Checkmate. White Wins!");
			System.exit(0);
		}
		System.out.println("Draw by Stalemate");
		System.exit(0);
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
	
	public void removePiece(Piece piece, Board board)
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

	public String formatChessMove(String chessMove)
	{
		for(int i = 0; i < chessMove.length(); i++)
		{
			if(chessMove.charAt(i) == 'x')
			{
				chessMove = chessMove.substring(0, i) + chessMove.substring(i + 1, chessMove.length());
			}
		}
		
		pawnMove = false;
		if(chessMove.charAt(0) >= 'a' && chessMove.charAt(0) <= 'h')
		{
			chessMove = "P" + chessMove;
			pawnMove = true;
		}
		
		if(chessMove.contains("+") || chessMove.contains("#"))
		{
			chessMove = chessMove.substring(0, chessMove.length() - 1);
		}
		return chessMove;
	}
}

