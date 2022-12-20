package Chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * A class to create a team of movable chess pieces and manage the various rules of the game
 *
 */
public class Team 
{
	private LinkedList<Piece> teamPieces = new LinkedList<Piece>();
	private ArrayList<int[]> boardSnapshot = new ArrayList<int[]>();
	private static int noPawnMoveOrCapture = 0;
	private boolean pawnMove;
	private boolean check = false;
	
	/**
	 * Constructor, adds the correct number of pieces for each piece type to the list attribute, passing both the team color
	 * and the shared board class to each piece
	 * @param color The team that the list of pieces will be added to
	 * @param board The shared board that keeps track of the piece locations
	 */
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
		
		for(int i = 0; i < teamPieces.size(); i++)
		{
			setPossibleMoves(teamPieces.get(i), this);
		}
	}
	
	/**
	 * Moves a chess piece based on the chess notation in the chessMove parameter, and updates the board based on 
	 * the result of the move. 
	 * @param chessMove Chess notation that describes the piece to move, and the position to move it to
	 * @param board The shared board that keeps track of piece locations
	 * @param otherTeam The opposing team of chess pieces, needed to calculate if the move is valid
	 * @return True if the move was able to proceed, false for any error in the process
	 */
	public boolean movePiece(String chessMove, Board board, Team otherTeam)
	{		
		final int FOURTH_ROW = 3;
		final int FIFTH_ROW = 4;
		try
		{ // Try block captures any input issues in chessMove to prevent runtime errors			
			chessMove = formatChessMove(chessMove); 
			if(castleKing(chessMove, board, otherTeam))
			{ // Castle operation is defined in its own method; returns false if castling is not called for, or if it is invalid
				return true;
			}
			
			Piece currentPiece = getSpecificPiece(chessMove);
			if(currentPiece == null) // If getSpecificPiece returns null, the chessMove string was invalid for the current board
			{
				return false;
			}
			
			boolean promote = false;
			String promoteString = null;
			if(currentPiece.promotionPossible())
			{ // If a piece is one square away from the back rank
				if(chessMove.contains("=Q") || chessMove.contains("=R") || chessMove.contains("=B") || chessMove.contains("=N"))
				{ // If a valid promotion code has been entered
					promote = true;
					promoteString = chessMove;
					chessMove = chessMove.substring(0, chessMove.length() - 2); 
				}
				// If the current piece is a pawn that is one move away from the back rank, a promotion type must be entered or the move is invalid
				else
				{
					System.out.print("Must clarify proper promotion type, try again: ");
					return false;
				}
			}
			// Now that all special move considerations have been accounted for, we can run the general Piece class move method
			if(currentPiece.movePiece(chessMove, board))
			{ 
				if(promote)
				{ // If all necessary qualifications for a promotion are met, currentPiece is updated to its new piece type
					currentPiece = promotePawn(promoteString, currentPiece, board);
					if(currentPiece == null)
					{
						System.out.print("Invalid promotion type, try again: ");
						return false;
					}
				}
				for(int i = 0; i < otherTeam.getPieceList().size(); i++)
				{
					// If the current piece is a pawn that is behind another piece, then we have the correct possible en Passant format
					boolean enPassantFormat = (currentPiece.getClass() == Pawn.class) && (currentPiece.getPosition() / 10) 
							== (otherTeam.getPieceList().get(i).getPosition() / 10) && (currentPiece.getPosition() % 10) 
							== ((otherTeam.getPieceList().get(i).getPosition() % 10) + currentPiece.getTeam());
					
					if(otherTeam.getPieceList().get(i).getPosition() == currentPiece.getPosition())
					{ // If the current piece has moved onto the square of an opposing piece, that piece is deleted from the record
						otherTeam.removePiece(otherTeam.getPieceList().get(i));
					}
					else if(otherTeam.getPieceList().get(i).getEnPassant() && enPassantFormat)
					{ // If an opposing pawn has just moved two squares, and a pawn has moved behind it, an en Passant move has been executed
						board.deleteOldPiece(otherTeam.getPieceList().get(i));
						otherTeam.removePiece(otherTeam.getPieceList().get(i));
					}
				}
				for(int i = 0; i < teamPieces.size(); i++)
				{ // Now that a new piece has been moved, we switch en Passant flags back to false;
					teamPieces.get(i).setEnPassant(false);
				}
				if(currentPiece.getFirstMove() && ((currentPiece.getPosition() % 10 == FOURTH_ROW) || (currentPiece.getPosition() % 10 == FIFTH_ROW)))
				{ // If the previous move was a pawn that moved two squares onto the fourth or fifth row, we set its en Passant flag to true
					currentPiece.setEnPassant(true);
				}
				currentPiece.setFirstMove(); // Set first move to false for the current piece
				
				for(int i = 0; i < otherTeam.getPieceList().size(); i++)
				{ // Set the possible moves for the other team
					otherTeam.setMoves(otherTeam.getPieceList().get(i),this);
				}
				return true;
			}
			return false;
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
			if(currentPiece.getPossibleMoves().size() > 0)
			{
				System.out.println("Possible Moves for " + currentPiece.getPiece() + " on " 
									+ currentPiece.intToChess(currentPiece.getPosition()).substring(1, 3) + ": ");
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
		
	}
	
	/**
	 * Sets all possible moves for a specific piece and the opposing team in terms of the current state of the board
	 * @param currentPiece The piece we wish to set all possible moves for
	 * @param otherTeam The opposing team that may influence the possible moves for the current piece
	 */
	public void setMoves(Piece currentPiece, Team otherTeam)
	{
		// The possible moves of the other team must be calculated before the current piece, as they may influence the possible list
		setPossibleMoves(currentPiece, otherTeam);
		removeCheckMoves(currentPiece, otherTeam);
	}
	
	/**
	 * Sets all vacuum moves for a specific piece and all pieces on the opposing team
	 * @param currentPiece The piece we wish to set vacuum moves for 
	 * @param otherTeam The opposing team that may influence the vacuum moves for the current piece
	 */
	public void setVacuumMoves(Piece currentPiece, Team otherTeam)
	{
		for(int i = 0; i < otherTeam.getPieceList().size(); i++)
		{
			otherTeam.getPieceList().get(i).setVacuumMoves();
		}
		currentPiece.setVacuumMoves();
	}
	
	/**
	 * Sets all squares attacked for a specific piece and for the other team
	 * @param currentPiece The piece we wish to set all squares attacked for
	 * @param otherTeam The opposing team that may influence the squares attacked for the current piece
	 */
	public void setSquaresAttacked(Piece currentPiece, Team otherTeam)
	{
		setVacuumMoves(currentPiece, otherTeam); // vacuum moves generated to initialize the list for squares attacked in the Piece class
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
	
	/**
	 * Sets all possible moves for a specific piece and for the other team
	 * @param currentPiece The piece we wish to set all possible moves for
	 * @param otherTeam The opposing team that may influence the possible moves for the current piece
	 */
	public void setPossibleMoves(Piece currentPiece, Team otherTeam)
	{
		setSquaresAttacked(currentPiece, otherTeam); // squares attacked generated to refine the list for possible moves in the Piece class
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
	/**
	 * Takes the list of possible moves for a specific piece and removes all moves that lead to its' own king being in check
	 * @param currentPiece The piece whose possible moves we wish to fully refine
	 * @param otherTeam The team that could potentially put the current piece's team in check
	 */
	public void removeCheckMoves(Piece currentPiece, Team otherTeam)
	{
		int positionHolder = currentPiece.getPosition(); // To hold the original position of the piece
		LinkedList<Integer> moveList = new LinkedList<Integer>(currentPiece.getPossibleMoves());
		ListIterator<Integer> moveIterator = moveList.listIterator();
		
		while(moveIterator.hasNext())
		{ // Set the position of the current piece to each possible move, generate new possible moves, and see if the king is in check
			int currentMove = moveIterator.next();
			currentPiece.setPosition(currentMove);
			setPossibleMoves(currentPiece, otherTeam);
			
			LinkedList<Piece> otherPieces = otherTeam.getPieceList();
			ListIterator<Piece> otherIterator = otherPieces.listIterator();
			
			while(otherIterator.hasNext())
			{	
				Piece tempPiece = otherIterator.next();
				// If the temporary position lands on an opposing piece, empty its list of squares attacked so it cannot put the king in check
				if(currentPiece.getPosition() == tempPiece.getPosition())
				{	
					tempPiece.setSquaresAttacked(new LinkedList<Integer>());
				}
			}
			
			otherTeam.kingInCheck(this);
			if(getCheck()) // If the king is in check after a temporary move, it is invalid
			{
				moveIterator.remove();
			}
		}
		
		currentPiece.setPosition(positionHolder); // Reset original position	
		setPossibleMoves(currentPiece, otherTeam); // Reset all moves for current piece and other team
		currentPiece.setPossibleMoves(moveList); // Save the possible moves list that this method generated to the current piece
	}
	
	/**
	 * Perform a castle move if a valid castle command is given, and a castle is currently possible
	 * @param chessMove Chess notation, must be of a valid castle command for this method to perform any operations
	 * @param board The shared board that keeps track of the piece locations
	 * @param otherTeam The opposing team of pieces that may prohibit a castle move
	 * @return True if a castling move was executed, false for all else
	 */
	public boolean castleKing(String chessMove, Board board, Team otherTeam)
	{
		// Adding 10 to a position integer refers to the square one column to the left: this constant is useful for castle moves
		final int MOVE_COLUMN = 10; 
		int columnShift;
		int rookWhitePosition;
		int rookBlackPosition;
		if(chessMove.equals("0-0"))
		{ // Short castle, king will shift in positive direction
			columnShift = MOVE_COLUMN;
			rookWhitePosition = 70; // integer position for each respective rook
			rookBlackPosition = 77;
		}
		else if(chessMove.equals("0-0-0"))
		{ // Long castle, king will shift in negative direction
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
		{ // Finds the correct rook given the castle direction and the team color: if the rook has made a previous move, castling is invalid
			Piece currentPiece = teamPieces.get(i);
			int currentPiecePosition = currentPiece.getPosition();
			if((currentPiecePosition == rookWhitePosition || currentPiecePosition == rookBlackPosition) && currentPiece.getFirstMove())
			{
				castleRook = currentPiece;
			}
		}
		if(castleRook == null)
		{ // Rook is not on its starting square, or it has made at least one move
			return false;
		}
		
		boolean piecesInWay = true;
		Piece thisKing = teamPieces.get(0);
		for(int i = 0; i < castleRook.getSquaresAttacked().size(); i++)
		{ // If the specific rook attacks its own king, we know there are no pieces between them that would block a castle move
			if(thisKing.getPosition() == castleRook.getSquaresAttacked().get(i))
			{
				piecesInWay = false;
			}
		}
		if(piecesInWay)
		{ // If a piece is between the king and the rook, we cannot currently castle
			return false;
		}
		
		int castleMove = thisKing.getPosition() + columnShift; // One column away from king, on the correct castle side
		String castleString = thisKing.intToChess(castleMove);
		if(thisKing.getFirstMove() && thisKing.movePiece(castleString, board))
		{ // If the king has not moved and can move one column towards its correct castle side
			setMoves(thisKing, otherTeam);
			castleMove += columnShift; // One more column towards the correct castle side
			castleString = thisKing.intToChess(castleMove);
			if(thisKing.movePiece(castleString, board))
			{ // If the king can move one more column: moving one column at a time confirms that the king is not castling through, or into check
				board.deleteOldPiece(castleRook); // Delete and update rook position
				castleRook.setPosition(castleMove - columnShift); // One column towards the center from the king
				board.updatePiece(castleRook);
				return true;
			}
			else
			{ // If the king can move to the first column but not the second, we must move it back to its original square
				castleMove += -2 * columnShift; // Kings starting position, two columns towards the center from the castle column
				castleString = thisKing.intToChess(castleMove);
				thisKing.movePiece(castleString, board);
			}
		}
			return false;
	}
	
	/**
	 * Promotes a pawn that has moved to the back rank to a piece of higher order
	 * @param chessMove The requested move that contains the promotion type
	 * @param pawn The pawn that has reached the back rank
	 * @param board The shared board that keeps track of the piece locations
	 * @return The piece of the newly promoted type, or null if an invalid promotion type was requested
	 */
	public Piece promotePawn(String chessMove, Piece pawn, Board board)
	{
		int promoteSquare = pawn.getPosition();
		String promoteTeam = pawn.getPiece().substring(0, 1); // "W" or "B"
		String promotionType = chessMove.substring(chessMove.length() - 1, chessMove.length());
		if(promotionType.equals("Q"))
		{
			Queen newQueen = new Queen(promoteSquare, promoteTeam); // copies pawn position and team into a new Queen 
			removePiece(pawn);
			teamPieces.add(newQueen);
			board.updatePiece(newQueen);
			return newQueen;
		}
		else if(promotionType.equals("R"))
		{
			Rook newRook = new Rook(promoteSquare, promoteTeam);
			removePiece(pawn);
			teamPieces.add(newRook);
			board.updatePiece(newRook);
			return newRook;
		}
		else if(promotionType.equals("B"))
		{
			Bishop newBishop = new Bishop(promoteSquare, promoteTeam); 
			removePiece(pawn);
			teamPieces.add(newBishop);
			board.updatePiece(newBishop);
			return newBishop;
		}
		else if(promotionType.equals("N"))
		{
			Knight newKnight = new Knight(promoteSquare, promoteTeam); 
			removePiece(pawn);
			teamPieces.add(newKnight);
			board.updatePiece(newKnight);
			return newKnight;
		}
		return null;
	}
	
	/**
	 * Find and return the specific piece from the chess notation in the chessMove parameter
	 * @param chessMove Chess notation that refers to a specific piece
	 * @return A specific piece on the board, or null if no specific piece is found
	 */
	public Piece getSpecificPiece(String chessMove)
	{
		boolean ambiguous = false;
		String rowOrColumn = "";
		String rowAndColumn = "";
		if(chessMove.contains("="))
		{ // This method does not deal with promotion operations
			chessMove = chessMove.substring(0, chessMove.length() - 2);
		}
		if(chessMove.length() == 4)
		{ // A length of four signals that a clarification character for row or column is present
			ambiguous = true;
			char pieceChoice = chessMove.charAt(1);
			if(pieceChoice <= 'h' && pieceChoice >= 'a')
			{ // If the clarification is of a column character
				rowOrColumn = "" + pieceChoice;
			}
			else if(pieceChoice <= '8' && pieceChoice >= '1')
			{ // If the clarification is of a row character
				rowOrColumn = "" + pieceChoice;
			}
		}
		else if(chessMove.length() == 5)
		{ // A length of five signals that  clarification characters for row and column is present
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
		int piecesFound = 0; // Incremented each time a possible piece candidate is found
		String pieceChoice = chessMove.substring(0, 1);		
		for(int i = 0; i < teamPieces.size(); i++)
		{
			String pieceType = teamPieces.get(i).getPiece().substring(1, 2);
			if(pieceChoice.equals(pieceType))
			{
				boolean correctPosition = teamPieces.get(i).intToChess(teamPieces.get(i).getPosition()).substring(1, 3).equals(rowAndColumn);
				boolean correctColumn = teamPieces.get(i).intToChess(teamPieces.get(i).getPosition()).substring(1, 2).equals(rowOrColumn);
				boolean correctRow = teamPieces.get(i).intToChess(teamPieces.get(i).getPosition()).substring(2, 3).equals(rowOrColumn);
				if(ambiguous && (correctPosition || correctColumn || correctRow))
				{ // If the clarification character(s) match the position of a piece of correct type
					for(int j = 0; j < teamPieces.get(i).getPossibleMoves().size(); j++)
					{
						if(teamPieces.get(i).getPossibleMoves().get(j) == teamPieces.get(i).chessToInt(chessMove))
						{ // If the move stated by the chessMove parameter is possible for the selected piece, we have found a candidate
							specificPiece = teamPieces.get(i);
							piecesFound ++;
						}
					}
				}
				else if(!ambiguous)
				{ // If it is a normal move without any clarification characters
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
		{ // We must have further clarification: more than one piece of the same type can move to the same square given the requested move
			System.out.print("Ambiguous move, try again: ");
			return null;
		}
		if(piecesFound == 0)
		{ // The requested move is either not possible, or not a valid move request
			System.out.print("Invalid move, try again: ");
			return null;
		}
		return specificPiece;
	}
	
	/**
	 * Standardizes chess notation into a string that is more manageable for the program
	 * @param chessMove Chess Notation of a possible move
	 * @return The standardized string
	 */
	public String formatChessMove(String chessMove)
	{
		for(int i = 0; i < chessMove.length(); i++)
		{ // 'x' in chess notation implies a piece is to be captures, but the program does not need this character to deal with captures
			if(chessMove.charAt(i) == 'x')
			{
				chessMove = chessMove.substring(0, i) + chessMove.substring(i + 1, chessMove.length());
			}
		}
		
		if(chessMove.charAt(0) >= 'a' && chessMove.charAt(0) <= 'h')
		{ // Pawn moves in chess notation need not begin with 'P'; adding this piece type to pawn moves gives the program the information
			chessMove = "P" + chessMove;
		}
		pawnMove = false;
		if(chessMove.charAt(0) == 'P')
		{
			pawnMove = true;
		}
		
		if(chessMove.contains("+") || chessMove.contains("#"))
		{ // Check and checkmate symbols are traditional in chess notation, but again, the program does not need them to figure out these rules
			chessMove = chessMove.substring(0, chessMove.length() - 1);
		}
		return chessMove;
	}
	
	/**
	 * Checks for a draw between the two teams; threefold repetition, insufficient pieces remaining to win for either team, 
	 * and the 50 move rule. If a draw is found, the program is halted and a message is displayed to the user. 
	 * @param otherTeam The opposing team of pieces that is needed to calculate draw results
	 */
	public boolean checkDraw(Team otherTeam)
	{
		int thisTotalPieces = teamPieces.size();
		int otherTotalPieces = otherTeam.getPieceList().size();
		int currentPieces = thisTotalPieces + otherTotalPieces;
		boolean insuffMaterial = true;
		if(thisTotalPieces <= 2 && otherTotalPieces <= 2)
		{ // If either team has 2 remaining pieces besides its King, there is sufficient material to checkmate
			for(int i = 0; i < thisTotalPieces; i++)
			{
				if(teamPieces.get(i).getClass() == Queen.class || teamPieces.get(i).getClass() == Rook.class  
						|| teamPieces.get(i).getClass() == Pawn.class) 
				{ // If either team has a Queen, Rook, or pawn, there is sufficient material to checkmate
					insuffMaterial = false;
				}
			}
			for(int i = 0; i < otherTotalPieces; i++)
			{
				if(otherTeam.getPieceList().get(i).getClass() == Queen.class || otherTeam.getPieceList().get(i).getClass() == Rook.class 
						|| otherTeam.getPieceList().get(i).getClass() == Pawn.class)
				{
					insuffMaterial = false;
				}
			}
			if(insuffMaterial)
			{ // If both teams have at most one king and a minor piece remaining, there is no possible checkmate
				System.out.print("Draw by Insufficient Material");
				return false;
			}
		}
		
		final int START_PIECES = 32; // Pieces on the board at the beginning of the game
		int lastSnapshotPieces = START_PIECES; // The amount of pieces on the board after the last move for this specific team
		if(boardSnapshot.size() >= 1)
		{ // retrieves the number of pieces on the board from the last move, saved from the last snapshot
			lastSnapshotPieces = boardSnapshot.get(boardSnapshot.size() - 1).length;
		}

		if(pawnMove || currentPieces != lastSnapshotPieces)
		{ // If a pawn has just moved or a piece has been captured (piece count is different than the last move)
			/*
			 * The board snapshot holds integer arrays of the positions of all pieces on the board for both teams; if there ever 
			 * exists three identical snapshots within the boardSnapshot attribute, then the same position has been reached three 
			 * times and is therefore a draw by threefold repetition. If a pawn is moved or if there is a capture, we can assume that
			 * we have reached a unique position, and can therefore erase all previous snapshots  
			 */
			boardSnapshot = new ArrayList<int[]>(); 
			noPawnMoveOrCapture = 0; // Reset 50 move count
		}
		else
		{
			noPawnMoveOrCapture ++;
		}
		
		int[] currentBoard = new int[currentPieces]; // To hold the current set of piece positions
		
		for(int i = 0; i < thisTotalPieces; i++)
		{ // Adding team pieces
			currentBoard[i] = teamPieces.get(i).getPosition();
		}
		for(int i = 0; i < otherTotalPieces; i++)
		{ // Adding other team pieces
			currentBoard[i + thisTotalPieces] = otherTeam.getPieceList().get(i).getPosition();
		}
		boardSnapshot.add(currentBoard);

		int repitition = 0;
		for(int i = 0; i < boardSnapshot.size(); i++)
		{
			if(Arrays.equals(currentBoard, boardSnapshot.get(i)))
			{ // If this current position has been reached before, increment repetition
				repitition ++;
			}
		}
		if(noPawnMoveOrCapture == 100)
		{ // Since noPawnMoveOrCapture is static, a value of 100 in this variable means that 50 full turns have passed without a capture or pawn move
			System.out.print("Draw by 50-move rule");
			return false;
		}
		else if(repitition == 3)
		{
			System.out.print("Draw by Threefold Repitition");
			return false;
		}
		return true;
	}
	
	/**
	 * Checks to see if a draw or a decisive result has been found; if this is true, the program halts, and a message is displayed
	 * to the user. Otherwise, the program continues. 
	 * @param otherTeam
	 */
	public boolean checkGameStatus(Team otherTeam)
	{
		final int WHITE_TEAM = 1;
		 
		if(otherTeam.checkDraw(this) == false)
		{// Halts program if there is a draw, else this method continues
			return false;
		}
		otherTeam.kingInCheck(this);
		for(int i = 0; i < teamPieces.size(); i++)
		{ // If the current team has a possible move, the game will continue 
			if(teamPieces.get(i).getPossibleMoves().size() > 0)
			{
				return true;
			}
		}
		
		if(getCheck())
		{
			if(teamPieces.get(0).getTeam() == WHITE_TEAM) // Checking the team of an arbitrary piece of the current team
			{ // If the white team has no moves and is in check, else if the black team has no moves and is in check
				System.out.println("Checkmate. Black Wins!");
				return false;
			}
			else
			{
				System.out.println("Checkmate. White Wins!");
				return false;
			}
		} 
		System.out.println("Draw by Stalemate"); // If either team has no moves and is not in check
		return false;
	}
	/**
	 * Checks if a piece on this team currently attacks the opposing King
	 * @param otherTeam The opposing team whose king may be under attack
	 */
	public void kingInCheck(Team otherTeam)
	{
		for(int i = 0; i < teamPieces.size(); i++)
		{
			Piece opposingKing = otherTeam.getPieceList().get(0);
			if(opposingKing.isCheck(teamPieces.get(i)))
			{ // See if the current piece attacks the opposing king
				otherTeam.setCheck(true);
				return;
			}
		}
		otherTeam.setCheck(false);
	}
	
	/**
	 * Mutator method, sets the value of the check attribute
	 * @param check True if the King is in check, false if not
	 */
	public void setCheck(boolean check)
	{
		this.check = check;
	}
	
	/**
	 * Accessor method, returns the value of the check attribute
	 * @return true if this teams King is currently attacked
	 */
	public boolean getCheck()
	{
		return check;
	}
	/**
	 * Accessor method, retrieves the value of the teamPieces attribute
	 * @return the a linked list containing all of the current pieces for this team
	 */
	public LinkedList<Piece> getPieceList()
	{
		return teamPieces;
	}
	
	/**
	 * Removes a specific piece from the teamPieces lists
	 * @param piece The piece to be removed from the list
	 */
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

