package boardgame;

public abstract class Piece {

	protected Position position;	//é só a matriz, não é a posição no jogo
	private Board board;
	
	public Piece(Board board) {		//não informa a posição no construtor pq é nula no começo
		this.board = board;
		position = null;		//não precisa declarar posição nula pq por padrão é nula
	}

	protected Board getBoard() {	//protected: limitar o acesso só p tabuleiro e subclasse de peças
		return board;
	}
	
	public abstract boolean[][] possibleMoves();	//abstract pq cada peça tem um movimento diferente e aqui é o generalizado
	
	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];	//Rook methods - método que faz um gancho com a subclasse
	}
	
	public boolean isThereAnyPossibleMove() {
		boolean[][] mat = possibleMoves();
		for (int i=0; i<mat.length; i++) {
			for (int j=0; j<mat.length; j++) {
				if (mat[i][j]) {
					return true;
				}
			}
		}
		return false;
	}
}