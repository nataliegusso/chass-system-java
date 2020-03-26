package chess;

import boardgame.BoardException;

public class ChessException extends BoardException {  //Uma exceção de xadrez tbm é de tabuleiro
	
	private static final long serialVersionUID = 1L;

	public ChessException(String msg) {
		super(msg);
	}
}