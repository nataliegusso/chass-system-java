package boardgame;

public class Piece {

	protected Position position;  //é só a matriz, não é a posição no jogo
	private Board board;

	public Piece(Board board) {  //não informa a posição no construtor pq é nula no começo
		this.board = board;
		position = null;  //não precisa declarar posição nula pq por padrão é nula
	}
	protected Board getBoard() {  //protected: limitar o acesso só p tabuleiro e subclasse de peças
		return board;
	}
}