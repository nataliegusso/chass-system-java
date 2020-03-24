package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.Bishops;
import chess.pieces.King;
import chess.pieces.Knights;
import chess.pieces.Pawns;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;

	public ChessMatch() {
		board = new Board(8, 8);
		initialSetup();  //Chama o método que coloca as peças no tabuleiro
		
	}
	
	public ChessPiece[][] getPieces(){
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		
		for (int i=0 ; i<board.getRows(); i++) {
			for (int j=0 ; j<board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i,j);  //Downcast (superclasse p subclasse)
			}												//Entende que é uma peça de xadrez	
		}
	return mat;			//Matriz de peças da partida
	}

	private void initialSetup() {  //Método que coloca as peças no tabuleiro
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
}
