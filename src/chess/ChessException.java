package chess;

import boardgame.BoardException;

public class ChessException extends BoardException {  //Uma exce��o de xadrez tbm � de tabuleiro
	
	private static final long serialVersionUID = 1L;

	public ChessException(String msg) {
		super(msg);
	}
}