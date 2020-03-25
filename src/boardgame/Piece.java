package boardgame;

public class Piece {

	protected Position position;  //� s� a matriz, n�o � a posi��o no jogo
	private Board board;

	public Piece(Board board) {  //n�o informa a posi��o no construtor pq � nula no come�o
		this.board = board;
		position = null;  //n�o precisa declarar posi��o nula pq por padr�o � nula
	}
	protected Board getBoard() {  //protected: limitar o acesso s� p tabuleiro e subclasse de pe�as
		return board;
	}
}