package chess;

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
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
		
	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();		 //Chama o m�todo que coloca as pe�as no tabuleiro
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
	
	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i=0; i<board.getRows(); i++) {
			for (int j=0; j<board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);	//Downcast (superclasse p subclasse)
			}												//Entende que � uma pe�a de xadrez	
		}
		return mat;		//Matriz de pe�as da partida
	}
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();	//Converte posi��o de xadrez em posi��o de matriz
		validateSourcePosition(position);					 // e valida
		return board.piece(position).possibleMoves();		// Imprime possiveis movimentos
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();

		validateSourcePosition(source);				//valida a posi��o de origem
		validateTargetPosition(source, target);		//valida a posi��o de destino

		Piece capturedPiece = makeMove(source, target);
		
		if (testCheck(currentPlayer)) {		// testa se o movimento colocou o jogador em check, pq n�o pode ser feito este movimento
			undoMove(source, target, capturedPiece);	//desfaz o movimento
			throw new ChessException("You can't put yourself in check");
		}

		check = (testCheck(opponent(currentPlayer))) ? true : false;  //testa se o oponente ficou em check
		
		nextTurn();		//Troca o turno (rodada) e jogador
		return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);				//Retirei a pe�a da posi��o de origem
		Piece capturedPiece = board.removePiece(target);	//Remover a poss�vel pe�a do destino (capturada)
		board.placePiece(p, target);						//Coloquei a pe�a na posi��o de destino
		
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		Piece p = board.removePiece(target);			//Desfaz o que foi feito em makeMove 
		board.placePiece(p, source);
		
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
	}
		
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {  //Testa se pe�a � da cor do turno (rodada). 
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
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;  //Se currentPlayer � branco, vira preto, sen�o vira branco
	}
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());  //Downcast de chesspiece pq � l� que a pe�a tem cor
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board");  //se n�o achar o rei
	}
	
	private boolean testCheck(Color color) {  
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {	//varre todas as pe�as advers�rias para ver se alguma leva � po��o do rei
			boolean[][] mat = p.possibleMoves();  //matriz de movimentos poss�veis de p: se nesta matriz a posi��o correspondente � posi��o do rei for true, o rei est� em check
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;       //pega e retorna a cor pq est� testando se o rei desta cor est� em check
			}
		}
		return false;
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {	 //Passa as posi��es na coordenada do xadrez
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	private void initialSetup() {		//M�todo que coloca as pe�as no tabuleiro, coordenadas do xadrez
		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Knights(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishops(board, Color.BLACK));
		placeNewPiece('d', 8, new King(board, Color.BLACK));
		placeNewPiece('e', 8, new Queen(board, Color.BLACK));
		placeNewPiece('f', 8, new Bishops(board, Color.BLACK));
		placeNewPiece('g', 8, new Knights(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawns(board, Color.BLACK));
		placeNewPiece('b', 7, new Pawns(board, Color.BLACK));
		placeNewPiece('c', 7, new Pawns(board, Color.BLACK));
		placeNewPiece('d', 7, new Pawns(board, Color.BLACK));
		placeNewPiece('e', 7, new Rook(board, Color.BLACK));
		placeNewPiece('f', 7, new Pawns(board, Color.BLACK));
		placeNewPiece('g', 7, new Pawns(board, Color.BLACK));
		placeNewPiece('h', 7, new Pawns(board, Color.BLACK));

		placeNewPiece('a', 2, new Pawns(board, Color.WHITE));
		placeNewPiece('b', 2, new Pawns(board, Color.WHITE));
		placeNewPiece('c', 2, new Rook(board, Color.WHITE));
		placeNewPiece('d', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 2, new Rook(board, Color.WHITE));
		placeNewPiece('f', 2, new Pawns(board, Color.WHITE));
		placeNewPiece('g', 2, new Pawns(board, Color.WHITE));
		placeNewPiece('h', 2, new Pawns(board, Color.WHITE));
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Knights(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishops(board, Color.WHITE));
		placeNewPiece('d', 1, new King(board, Color.WHITE));
		placeNewPiece('e', 1, new Queen(board, Color.WHITE));
		placeNewPiece('f', 1, new Bishops(board, Color.WHITE));
		placeNewPiece('g', 1, new Knights(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
	}

/*	private void initialSetup() {  //M�todo que coloca as pe�as no tabuleiro, coordenadas da matriz
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
