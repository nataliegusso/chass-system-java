package boardgame;

public class Board {

	private int rows;
	private int columns;
	private Piece[][] pieces;  //Matriz de peças

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
	
	public Piece piece (Position position) {  //Sobrecarga. Retorna a posição
		if (!positionExists(position)) {
			throw new BoardException("Position is not on the board");
		}
		return pieces[position.getRow()][position.getColumn()];
	}
	
	public void placePiece(Piece piece, Position position) {
		if (thereIsAPiece(position)) {
			throw new BoardException("There is alredy a piece on position " + position);
		}
		
		pieces[position.getRow()][position.getColumn()] = piece;   //Coloca a peça no tabuleiro
		piece.position = position;       //Não é mais posição nula
	}
	
	public Piece removePiece(Position position) {
		if (!positionExists(position)) {
			throw new BoardException("Position is not on the board");
		}
		if (piece(position) == null) {
			return null;
		}
		Piece aux = piece(position);  //cria a variável para conseguir retornar o valor nulo
		aux.position = null;          //atribui o valo nulo
		pieces[position.getRow()][position.getColumn()] = null;  //atribui o valor nulo para a posição
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
