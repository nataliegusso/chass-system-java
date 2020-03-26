package chess;

import boardgame.Position;

public class ChessPosition {
	
	private char column;
	private int row;
	
	public ChessPosition(char column, int row) {
		if (column < 'a' || column > 'h' || row < 1 || row > 8) {	// || alt+124 
			throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to h8.");
		}
		this.column = column;
		this.row = row;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	protected Position toPosition() {
		return new Position(8 - row, column - 'a');   //Posi��o do xadrez convertida na posi��o da matriz (ij)
	}
	
	protected static ChessPosition fromPosition(Position position) {
		return new ChessPosition((char)('a' - position.getColumn()), 8 - position.getRow()); //Posi��o da matriz convertida na posi��o do xadrez (ji)
	}
	
	@Override
	public String toString() {
		return "" + column + row;  //"" for�a o compilador a entender que � uma concatena��o de strings
	}
}