package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Color;

public class Pawns extends ChessPiece {   // 8 PEÕES

	public Pawns(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "P";
	}
}