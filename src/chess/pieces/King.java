package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

	private ChessMatch chessMatch;

	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}

	@Override
	public String toString() {
		return "K";
	}

	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p == null || p.getColor() != getColor();		//Testa se a posição não é nula, se é adversária
	}
	
	private boolean testRookCastling(Position position) {  //Testar se a torre está apta para o movimento especial Castling (roque)
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;  //peça != nulo, se é torre, cor certa = rei, qtde movimentos = 0
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);  //posição auxiliar
		
		// Above
		p.setValues(position.getRow() - 1, position.getColumn());
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// Below
		p.setValues(position.getRow() + 1, position.getColumn());
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// Left
		p.setValues(position.getRow(), position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// Right
		p.setValues(position.getRow(), position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// NW (above & left)
		p.setValues(position.getRow() - 1, position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// NE (above & right)
		p.setValues(position.getRow() - 1, position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// SW (below & left)
		p.setValues(position.getRow() + 1, position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// SE (below & right)
		p.setValues(position.getRow() + 1, position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// #specialmove castling (roque)
		if (getMoveCount() == 0 && !chessMatch.getCheck()) {  //movimento do rei = 0 e não check
			// #specialmove castling kingside rook (roque pequeno): Torre mais perto do rei
			Position posT1 = new Position(position.getRow(), position.getColumn() + 3); //torre deve estar 3 colunas à direita do rei
			if (testRookCastling(posT1)) {
				Position p1 = new Position(position.getRow(), position.getColumn() + 1);  //casa 1 da direita do rei 
				Position p2 = new Position(position.getRow(), position.getColumn() + 2); //casa 2 da direita do rei 
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {		//casa 1 e 2 da direita do rei vazia?
					mat[position.getRow()][position.getColumn() + 2] = true;			//movimento do rei. O movimento da torre fica no makeMove e undoMove (ChessMatch)
				}
			}
			// #specialmove castling queenside rook (roque grande): Torre mais longe do rei
			Position posT2 = new Position(position.getRow(), position.getColumn() - 4); //torre deve estar 4 colunas à direita do rei
			if (testRookCastling(posT2)) {
				Position p1 = new Position(position.getRow(), position.getColumn() - 1);
				Position p2 = new Position(position.getRow(), position.getColumn() - 2);
				Position p3 = new Position(position.getRow(), position.getColumn() - 3);
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null) {
					mat[position.getRow()][position.getColumn() - 2] = true;
				}
			}
		}

		/*Castling (roque): o Rei e a Torre se movimentam simultaneamente.
			Condições:	- nem o Rei, nem a Torre terem sido movidos durante o jogo;
						- o rei não pode estar em check
						- todas as casas entre as Torres e o Rei deves estar desocupadas
		*/
		return mat;
	}
}