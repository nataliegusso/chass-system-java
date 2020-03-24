package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Color;

public class Bishops extends ChessPiece{   // 2 BISPOS 
	
	public Bishops(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "B";
	}
}