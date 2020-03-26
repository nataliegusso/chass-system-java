package chess;

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
		nextTurn();		//Troca o turno (rodada) e jogador
		return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);				//Retirei a peça da posição de origem
		Piece capturedPiece = board.removePiece(target);	//Remover a possível peça do destino (capturada)
		board.placePiece(p, target);						//Coloquei a peça na posição de destino
		return capturedPiece;
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
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {	 //Passa as posições na coordenada do xadrez
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}
	
	private void initialSetup() {		//Método que coloca as peças no tabuleiro, coordenadas do xadrez
		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Knights(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishops(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK));
		placeNewPiece('f', 8, new Bishops(board, Color.BLACK));
		placeNewPiece('g', 8, new Knights(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawns(board, Color.BLACK));
		placeNewPiece('b', 7, new Pawns(board, Color.BLACK));
		placeNewPiece('c', 7, new Pawns(board, Color.BLACK));
		placeNewPiece('d', 7, new Pawns(board, Color.BLACK));
		placeNewPiece('e', 7, new Pawns(board, Color.BLACK));
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
