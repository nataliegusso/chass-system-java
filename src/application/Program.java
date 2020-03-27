package application;   //Teclas de atalho: https://blog.algaworks.com/atalhos-e-configuracoes-para-ganhar-produtividade-com-eclipse/
						//https://help.github.com/pt/github/getting-started-with-github/keyboard-shortcuts
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();	//Intanciar a partida
		List<ChessPiece> captured = new ArrayList<>();
		
		while (!chessMatch.getCheckMate()) {		//Enquanto não estiver com check mate 
			try {
				UI.clearScreen();					//Limpa a tela (tudo que foi digitado antes)
				//UI.printBoard(chessMatch.getPieces()); //Criando uma classe user interface. Esse método recebe a matriz de peças //Imprimir peças
				UI.printMatch(chessMatch, captured);	 // Imprime a partida (tabuleiro, turno, jogador)										
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.readChessPosition(sc);
				
				boolean[][] possibleMoves = chessMatch.possibleMoves(source); //Lê a posição de origem
				UI.clearScreen();											//Limpa tela
				UI.printBoard(chessMatch.getPieces(), possibleMoves); //Imprime o tabuleiro com sobrecarga dos movimentos possiveis colorindo

				System.out.println();
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(sc);
				
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
				
				if (capturedPiece != null) {
					captured.add(capturedPiece);
				}
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
		UI.clearScreen();		//Finaliza a partida
		UI.printMatch(chessMatch, captured);
	}
}