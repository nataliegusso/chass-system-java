package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();	//Intanciar a partida
		
		while (true) {
			try {
				UI.clearScreen();					//Limpa a tela (tudo que foi digitado antes)
				//UI.printBoard(chessMatch.getPieces()); //Criando uma classe user interface. Esse m�todo recebe a matriz de pe�as //Imprimir pe�as
				UI.printMatch(chessMatch);		 // Imprime a partida (tabuleiro, turno, jogador)										
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.readChessPosition(sc);
				
				boolean[][] possibleMoves = chessMatch.possibleMoves(source); //L� a posi��o de origem
				UI.clearScreen();											//Limpa tela
				UI.printBoard(chessMatch.getPieces(), possibleMoves); //Imprime o tabuleiro com sobrecarga dos movimentos possiveis colorindo

				System.out.println();
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(sc);
				
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
			}
			catch (ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine();	//Vai aguardar o enter
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
	}
}