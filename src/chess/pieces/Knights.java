package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Color;

public class Knights extends ChessPiece {   // 2 CAVALOS

	public Knights(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "N";
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		return mat;
	}
}