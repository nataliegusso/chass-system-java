package boardgame;

public class Board {

	private int rows;
	private int columns;
	private Piece[][] pieces;  //Matriz de pe�as

	public Board(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public Piece piece (int row, int column) {  //Retorna a matriz
		if (!positionExists(row, column)) {
			throw new BoardException("Position is not on the board");
		}
		return pieces[row][column];  
	}
	
	public Piece piece (Position position) {  //Sobrecarga. Retorna a posi��o
		if (!positionExists(position)) {
			throw new BoardException("Position is not on the board");
		}
		return pieces[position.getRow()][position.getColumn()];
	}
	
	public void placePiece(Piece piece, Position position) {
		if (thereIsAPiece(position)) {
			throw new BoardException("There is alredy a piece on position " + position);
		}
		
		pieces[position.getRow()][position.getColumn()] = piece;   //Coloca a pe�a no tabuleiro
		piece.position = position;       //N�o � mais posi��o nula
	}
	
	public Piece removePiece(Position position) {
		if (!positionExists(position)) {
			throw new BoardException("Position is not on the board");
		}
		if (piece(position) == null) {
			return null;
		}
		Piece aux = piece(position);  //cria a vari�vel para conseguir retornar o valor nulo
		aux.position = null;          //atribui o valo nulo
		pieces[position.getRow()][position.getColumn()] = null;  //atribui o valor nulo para a posi��o
		return aux;
	}
		
	public boolean positionExists(int row, int column) {
		return row >= 0 && row < rows && column >= 0 && column < columns;
	}
		
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn());
	}
	
	public boolean thereIsAPiece(Position position) {
		if (!positionExists(position)) {
			throw new BoardException("Position is not on the board");
		}
		return piece(position) != null;
	}
	
}
