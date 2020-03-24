package application;

import chess.ChessPiece;

public class UI {

	public static void printBoard(ChessPiece[][] pieces){
		for (int i=0 ; i<pieces.length; i++) {      //Lembre: pieces.length lê o tamanho da matriz sem precisar colocar o tamanho
			System.out.print((8 - i) + " ");
			for (int j=0 ; j<pieces.length; j++) {  //length matriz quadrada
				printPiece(pieces[i][j]);
			}
			System.out.println(" ");  //quebra de linha
		}
		System.out.println("  a b c d e f g h ");
	}
	
	
	private static void printPiece(ChessPiece piece) {
		if (piece == null) {
			System.out.print("-");
		}else {
			System.out.print(piece);
		}
		System.out.print(" ");  //espaço entre peças
	}
}
