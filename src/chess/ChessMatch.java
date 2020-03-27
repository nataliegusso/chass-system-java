package chess;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishops;
import chess.pieces.King;
import chess.pieces.Knights;
import chess.pieces.Pawns;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {
	
	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
		
	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();		 //Chama o método que coloca as peças no tabuleiro
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
	
	public ChessPiece getPromoted() {
		return promoted;
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i=0; i<board.getRows(); i++) {
			for (int j=0; j<board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);	//Downcast (superclasse p subclasse)
			}												//Entende que é uma peça de xadrez	
		}
		return mat;		//Matriz de peças da partida
	}
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();	//Converte posição de xadrez em posição de matriz
		validateSourcePosition(position);					 // e valida
		return board.piece(position).possibleMoves();		// Imprime possiveis movimentos
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();

		validateSourcePosition(source);				//valida a posição de origem
		validateTargetPosition(source, target);		//valida a posição de destino

		Piece capturedPiece = makeMove(source, target);
			
		if (testCheck(currentPlayer)) {		// testa se o movimento colocou o jogador em check, pq não pode ser feito este movimento
			undoMove(source, target, capturedPiece);	//desfaz o movimento
			throw new ChessException("You can't put yourself in check");
		}

		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		
		// #specialmove promotion
		promoted = null;
		if (movedPiece instanceof Pawns) {
			if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0) || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
				promoted = (ChessPiece)board.piece(target);
				promoted = replacePromotedPiece("Q");  //Troca por padrão pela rainha, depois pergunta p usuário qual ela quer. è mais fácil assim
			}
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;  //testa se o oponente ficou em check
		
		if (testCheckMate(opponent(currentPlayer))) {		//Se a jogada deixou em check mate, acabou a jogada
			checkMate = true;
		}
		else {
			nextTurn();		//Troca o turno (rodada) e jogador
		}

		// #specialmove en passant
		if (movedPiece instanceof Pawns && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
			enPassantVulnerable = movedPiece;
		}else {
			enPassantVulnerable = null;
		}
		
		return (ChessPiece)capturedPiece;
	}
	
	public ChessPiece replacePromotedPiece(String type) {
		if (promoted == null) {
			throw new IllegalStateException("There is no piece to be promoted");
		}
		if (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
			throw new InvalidParameterException("Invalid type for promotion");
		}

		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);

		ChessPiece newPiece = newPiece(type, promoted.getColor());  //chama o método abaixo
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);

		return newPiece;
	}

	private ChessPiece newPiece(String type, Color color) {  //Instanciar uma peça específica conforme a letra dada no método anterior
		if (type.equals("B")) return new Bishops(board, color);
		if (type.equals("N")) return new Knights(board, color);
		if (type.equals("Q")) return new Queen(board, color);
		return new Rook(board, color);
	}
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source);		//Retirei a peça da posição de origem
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);	//Remover a possível peça do destino (capturada)
		board.placePiece(p, target);						//Coloquei a peça na posição de destino
		
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		// #specialmove castling kingside rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {	//testa roque pequeno
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);	//onde estava
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);	//onde vai
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);			//tira de onde estava
			board.placePiece(rook, targetT);									// move
			rook.increaseMoveCount();
		}
		
		// #specialmove castling queenside rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {	//testa roque grande
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}	
		
		// #specialmove en passant
		if (p instanceof Pawns) {
			if (source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) {
					pawnPosition = new Position(target.getRow() + 1, target.getColumn());
				}else {
					pawnPosition = new Position(target.getRow() - 1, target.getColumn());
				}
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}
	
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);		//Desfaz o que foi feito em makeMove 
		p.decreaseMoveCount();
		board.placePiece(p, source);
		
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
		
		// #specialmove castling kingside rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}

		// #specialmove castling queenside rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		
		// #specialmove en passant
		if (p instanceof Pawns) {
			if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece)board.removePiece(target);
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) {
					pawnPosition = new Position(3, target.getColumn());
				}else {
					pawnPosition = new Position(4, target.getColumn());
				}
				board.placePiece(pawn, pawnPosition);
			}
		}
	}
		
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {  //Testa se peça é da cor do turno (rodada). 
			throw new ChessException("The chosen piece is not yours"); 
		}	
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;  //Se currentPlayer é branco, vira preto, senão vira branco
	}
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());  //Downcast de chesspiece pq é lá que a peça tem cor
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board");  //se não achar o rei
	}
	
	private boolean testCheck(Color color) {  
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {	//varre todas as peças adversárias para ver se alguma leva à poção do rei
			boolean[][] mat = p.possibleMoves();  //matriz de movimentos possíveis de p: se nesta matriz a posição correspondente à posição do rei for true, o rei está em check
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;       //pega e retorna a cor pq está testando se o rei desta cor está em check
			}
		}
		return false;
	}
	
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {       //Testa se está em check, pq se não estiver, não tem como estar em check mate
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i=0; i<board.getRows(); i++) {
				for (int j=0; j<board.getColumns(); j++) {
					if (mat[i][j]) {	//É movimento possível? Tira do check? Devo mover para testar
						Position source = ((ChessPiece)p).getChessPosition().toPosition();  //Pega a posição p de origem
						Position target = new Position(i, j);		//Qual o destino?	
						Piece capturedPiece = makeMove(source, target);  //Faz o movimento
						boolean testCheck = testCheck(color);			//Faz o teste se ainda está em check
						undoMove(source, target, capturedPiece);		//Desfaz o movimento pq foi só teste
						if (!testCheck) {							//Testa se tirou do check
							return false;
						}
					}
				}
			}
		}
		return true;
	}	
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {	 //Passa as posições na coordenada do xadrez
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	private void initialSetup() {		//Método que coloca as peças no tabuleiro, coordenadas do xadrez
        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Knights(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishops(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK, this));
		placeNewPiece('f', 8, new Bishops(board, Color.BLACK));
		placeNewPiece('g', 8, new Knights(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawns(board, Color.BLACK, this));
		placeNewPiece('b', 7, new Pawns(board, Color.BLACK, this));
		placeNewPiece('c', 7, new Pawns(board, Color.BLACK, this));
		placeNewPiece('d', 7, new Pawns(board, Color.BLACK, this));
		placeNewPiece('e', 7, new Pawns(board, Color.BLACK, this));
		placeNewPiece('f', 7, new Pawns(board, Color.BLACK, this));
		placeNewPiece('g', 7, new Pawns(board, Color.BLACK, this));
		placeNewPiece('h', 7, new Pawns(board, Color.BLACK, this));

		placeNewPiece('a', 2, new Pawns(board, Color.WHITE, this));
		placeNewPiece('b', 2, new Pawns(board, Color.WHITE, this));
		placeNewPiece('c', 2, new Pawns(board, Color.WHITE, this));
		placeNewPiece('d', 2, new Pawns(board, Color.WHITE, this));
		placeNewPiece('e', 2, new Pawns(board, Color.WHITE, this));
		placeNewPiece('f', 2, new Pawns(board, Color.WHITE, this));
		placeNewPiece('g', 2, new Pawns(board, Color.WHITE, this));
		placeNewPiece('h', 2, new Pawns(board, Color.WHITE, this));
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Knights(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishops(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE, this));
		placeNewPiece('f', 1, new Bishops(board, Color.WHITE));
		placeNewPiece('g', 1, new Knights(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
	}

/*	private void initialSetup() {  //Método que coloca as peças no tabuleiro, coordenadas da matriz
		board.placePiece(new Rook(board, Color.BLACK), new Position(0,0));
		board.placePiece(new Knights(board, Color.BLACK), new Position(0,1));
		board.placePiece(new Bishops(board, Color.BLACK), new Position(0,2));
		board.placePiece(new Queen(board, Color.BLACK), new Position(0,3));
		board.placePiece(new King(board, Color.BLACK), new Position(0,4));
		board.placePiece(new Bishops(board, Color.BLACK), new Position(0,5));
		board.placePiece(new Knights(board, Color.BLACK), new Position(0,6));
		board.placePiece(new Rook(board, Color.BLACK), new Position(0,7));
		board.placePiece(new Pawns(board, Color.BLACK), new Position(1,0));
		board.placePiece(new Pawns(board, Color.BLACK), new Position(1,1));
		board.placePiece(new Pawns(board, Color.BLACK), new Position(1,2));
		board.placePiece(new Pawns(board, Color.BLACK), new Position(1,3));
		board.placePiece(new Pawns(board, Color.BLACK), new Position(1,4));
		board.placePiece(new Pawns(board, Color.BLACK), new Position(1,5));
		board.placePiece(new Pawns(board, Color.BLACK), new Position(1,6));
		board.placePiece(new Pawns(board, Color.BLACK), new Position(1,7));

		board.placePiece(new Pawns(board, Color.WHITE), new Position(6,0));
		board.placePiece(new Pawns(board, Color.WHITE), new Position(6,1));
		board.placePiece(new Pawns(board, Color.WHITE), new Position(6,2));
		board.placePiece(new Pawns(board, Color.WHITE), new Position(6,3));
		board.placePiece(new Pawns(board, Color.WHITE), new Position(6,4));
		board.placePiece(new Pawns(board, Color.WHITE), new Position(6,5));
		board.placePiece(new Pawns(board, Color.WHITE), new Position(6,6));
		board.placePiece(new Pawns(board, Color.WHITE), new Position(6,7));
		board.placePiece(new Rook(board, Color.WHITE), new Position(7,0));
		board.placePiece(new Knights(board, Color.WHITE), new Position(7,1));
		board.placePiece(new Bishops(board, Color.WHITE), new Position(7,2));
		board.placePiece(new King(board, Color.WHITE), new Position(7,4));
		board.placePiece(new Queen(board, Color.WHITE), new Position(7,3));
		board.placePiece(new Bishops(board, Color.WHITE), new Position(7,5));
		board.placePiece(new Knights(board, Color.WHITE), new Position(7,6));
		board.placePiece(new Rook(board, Color.WHITE), new Position(7,7));
	}
*/
}
