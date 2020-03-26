package boardgame;

public abstract class Piece {

	protected Position position;	//� s� a matriz, n�o � a posi��o no jogo
	private Board board;
	
	public Piece(Board board) {		//n�o informa a posi��o no construtor pq � nula no come�o
		this.board = board;
		position = null;		//n�o precisa declarar posi��o nula pq por padr�o � nula
	}

	protected Board getBoard() {	//protected: limitar o acesso s� p tabuleiro e subclasse de pe�as
		return board;
	}
	
	public abstract boolean[][] possibleMoves();	//abstract pq cada pe�a tem um movimento diferente e aqui � o generalizado
	
	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];	//Rook methods - m�todo que faz um gancho com a subclasse
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