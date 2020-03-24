package application;

import chess.ChessMatch;

public class Program {

	public static void main(String[] args) {

		ChessMatch chessMatch = new ChessMatch(); //Intanciar a partida
		UI.printBoard(chessMatch.getPieces()); //Criando uma classe user interface. Esse método recebe a matriz de peças
										//Imprimir peças
		
		
	}
}
