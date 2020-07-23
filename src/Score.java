/*===========*
*			 *
*	TsuHub   *
*			 *
=============*/

import java.awt.*;

/**
	Esta classe representa um placar no jogo. A classe princial do jogo (Pong)
	instancia dois objeto deste tipo, cada um responsável por gerenciar a pontuação
	de um player, quando a execução é iniciada.

 	Foram utilizadas dois valores estáticos (score1 e score2) para armazenar os
 	pontos de cada um dos jogadores, assim não há perda dessa informação.
*/
public class Score {
	private String playerId;
	private static int score1;
	private static int score2;

	/**
		Construtor da classe Score.

		@param playerId uma string que identifica o player ao qual este placar está associado.
	*/
	public Score(String playerId){
		this.playerId = playerId;
	}

	/**
		Método de desenho do placar. Caso o Id seja do Player 1, desenha-se seu nome
	 	com alinhamento à esquerda, caso seja o Player 2, alinhamento à direita.
	*/
	public void draw(){
		if (this.playerId.equals("Player 1")) {
			GameLib.setColor(Color.GREEN);
			GameLib.drawText(this.playerId + ": " + this.getScore(), 70, GameLib.ALIGN_LEFT);
		} else {
			GameLib.setColor(Color.BLUE);
			GameLib.drawText(this.playerId + ": " + this.getScore(), 70, GameLib.ALIGN_RIGHT);
		}
	}

	/**
		Método que incrementa em 1 unidade a contagem de pontos.
	 	+1 para o primeiro jogador, caso o Id que chamar este método seja do Player 1,
	 	e o mesmo para o segundo jogador.
	*/
	public void inc(){
		if (playerId.equals("Player 1")) score1++;
		else if (playerId.equals("Player 2")) score2++;
	}

	/**
		Método que devolve a contagem de pontos mantida pelo placar.

		@return o valor inteiro referente ao total de pontos.
	*/
	public int getScore(){
		int score = 0;
		if(this.playerId.equals("Player 1")) score = score1;
		else if(this.playerId.equals("Player 2")) score = score2;
		return score;
	}
}
