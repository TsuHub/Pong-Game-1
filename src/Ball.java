/*===========*
*			 *
*	TsuHub   *
*			 *
=============*/

import java.awt.*;

/**
	Esta classe representa a bola usada no jogo. A classe princial do jogo (Pong)
	instancia um objeto deste tipo quando a execução é iniciada.
*/
public class Ball {

	private double cx;
	private double cy;
	private double width;
	private double height;
	private Color color;
	private double speed;

	/**
	 * 	defaultCx = Variável que guarda a coordenada inicial da bola no eixo x.
	 * 	defaultCy = Variável que guarda a coordenada inicial da bola no eixo y.
	 *
	 * 	Variáveis responsáveis por definir o sentido da bola tornando a velocidade negativa ou positiva.
	 * 	(para a esquerda caso seja negativo, e direita, caso positivo para speedX)
	 * 	(para cima caso seja negativo e para baixo caso positivo, para o speedY).
	 * 	Ambas as variáveis tomam valor 1 ou -1.
	 * 	speedX.
	 * 	speedY.
	 *
	 * 	Variáveis de referência a objetos do tipo Score. Resonsáveis por atribuir pontos
	 * 	aos jogadores com a chamada do método inc.
	 * 	private Score scoreP1;
	 * 	private Score scoreP2;
	 */
	private double defaultCx;
	private double defaultCy;
	private int speedX;
	private int speedY;

	private String playerId;

	private Score scoreP1;
	private Score scoreP2;


	/**
		Construtor da classe Ball. Observe que quem invoca o construtor desta classe define a velocidade da bola 
		(em pixels por millisegundo), mas não define a direção deste movimento. A direção do movimento é determinada 
		aleatóriamente pelo construtor.

		@param cx coordenada x da posição inicial da bola (centro do retangulo que a representa).
		@param cy coordenada y da posição inicial da bola (centro do retangulo que a representa).
		@param width largura do retangulo que representa a bola.
		@param height altura do retangulo que representa a bola.
		@param color cor da bola.
		@param speed velocidade da bola (em pixels por millisegundo).
	*/
	public Ball(double cx, double cy, double width, double height, Color color, double speed){
		this.cx = cx;
		this.cy = cy;
		this.width = width;
		this.height = height;
		this.color = color;
		this.speed = speed;

		this.defaultCx = cx;
		this.defaultCy = cy;
		this.speedX = 1;
		this.speedY = 1;

		this.scoreP1 = new Score("Player 1");
		this.scoreP2 = new Score("Player 2");
	}

	/**
		Método chamado sempre que a bola precisa ser (re)desenhada.
	*/
	public void draw(){
		GameLib.setColor(this.color);
		GameLib.fillRect(getCx(), getCy(), getWidth(), getHeight());
	}

	/**
		Método chamado quando o estado (posição) da bola precisa ser atualizado.
		
		@param delta quantidade de millisegundos que se passou entre o ciclo anterior de atualização do jogo e o atual.
	*/
	public void update(long delta){
		setSpeed(delta);
	}

	/**
		Método chamado quando detecta-se uma colisão da bola com um jogador.
	
		@param playerId uma string cujo conteúdo identifica um dos jogadores.
	*/
	public void onPlayerCollision(String playerId){
		this.playerId = playerId;
	}

	/**
		Método chamado quando detecta-se uma colisão da bola com uma parede.

		@param wallId uma string cujo conteúdo identifica uma das paredes da quadra.
	*/
	public void onWallCollision(String wallId){
		if ( wallId.equals("Right") ) {
			scoreP1.inc();
		}
		else if (wallId.equals(("Left"))){
			scoreP2.inc();
		}
	}

	/**
		Método que verifica se houve colisão da bola com uma parede.
	 	Caso o Id da parede for igual a "Right", uma condição é feita, se a coordenada x da bola mais a metade
	 	de sua largura for igual (maior igual, compensando a possível falta de sincronia no processamento da
	 	coordenada da bola) a coordenada x da parede menos a metade de sua largura (o que configura uma colisão),
	 	a velocidade da bola, referente ao eixo X fica negativa, e 1 ponto é incrementado, posteriormente, a
	 	posição da bola é reiniciada. Essa chamada é repetida mais uma vez para a parede esquerda.
	 	Para o teto e o chão, apenas as suas velocidades, em relação ao eixo y, são alteradas.

		@param wall referência para uma instância de Wall contra a qual será verificada a ocorrência de colisão da bola.
		@return um valor booleano que indica a ocorrência (true) ou não (false) de colisão.
	*/
	public boolean checkCollision(Wall wall){

		if (wall.getId().equals("Right")) {
			if (this.getCx() + this.getWidth()/2 >= wall.getCx() - wall.getWidth()/2) {
				this.speedX = -1;
				onWallCollision(wall.getId());
				restart();
			}
		}

		if (wall.getId().equals("Left")) {
			if (this.getCx() - this.getWidth()/2 <= wall.getCx() - wall.getWidth()/2) {
				this.speedX = 1;
				onWallCollision(wall.getId());
				restart();
			}
		}

		if (wall.getId().equals("Bottom")) {
			if (this.getCy() + this.getHeight()/2 >= wall.getCy() - wall.getHeight()/2) this.speedY = -1;
		}

		if (wall.getId().equals("Top")) {
			if (this.getCy() - this.getHeight()/2 <= wall.getCy() + wall.getHeight()/2) this.speedY = 1;
		}
		return false;
	}

	/**
		Método que verifica se houve colisão da bola com um jogador.
	 	Sobrecarga de método. Assim como no método para a colisão com a parede, é compensado as espessuras da
	 	parede com a espessura da bola. O que difere aqui é que possuimos um limite superior e inferior onde
	 	a coordenada da bola no eixo y deve estar entre esses limites.

		@param player referência para uma instância de Player contra o qual será verificada a ocorrência de colisão da bola.
		@return um valor booleano que indica a ocorrência (true) ou não (false) de colisão.
	*/
	public boolean checkCollision(Player player){
		if (player.getId().equals("Player 1")) {
			if (this.getCx() - this.getWidth()/2 <= player.getCx() + player.getWidth()/2) {
				if (this.getCy() >= (player.getCy() - player.getHeight()/2) && this.getCy() <= (player.getCy() + player.getHeight() / 2)) {
					this.speedX = 1;
				}
			}
		}

		if (player.getId().equals("Player 2")) {
			if (this.getCx() + this.getWidth()/2 >= player.getCx() - player.getWidth()/2) {
				if (this.getCy() >= (player.getCy() - player.getHeight()/2) && this.getCy() <= (player.getCy() + player.getHeight() / 2)) {
					this.speedX = -1;
				}
			}
		}
		return false;
	}

	/**
		Método que devolve a coordenada x do centro do retângulo que representa a bola.
		@return o valor double da coordenada x.
	*/
	public double getCx(){
		return this.cx;
	}

	/**
		Método que devolve a coordenada y do centro do retângulo que representa a bola.
		@return o valor double da coordenada y.
	*/
	public double getCy(){
		return this.cy;
	}

	public double getWidth(){
		return this.width;
	}

	public double getHeight(){
		return this.height;
	}

	/**
		Método que devolve a velocidade da bola.
		@return o valor double da velocidade.
	*/
	public double getSpeed(){
		return this.speed;
	}

	/**
	 * Método que devolve a coordenada inicial da bola no eixo X.
	 * @return o valor double da coordenada X.
	 */
	public double getDefaultCx() {
		return this.defaultCx;
	}

	/**
	 * Método que devolve a coordenada inicial da bola no eixo Y.
	 * @return o valor double da coordenada Y.
	 */
	public double getDefaultCy() {
		return this.defaultCy;
	}

	/**
	 * Define o sentido da bola, considerando a velocidade positiva ou negativa
	 * tanto para o eixo x (speedX) quanto para o eixo y (speedY).
	 * @param delta
	 */
	public void setSpeed(long delta) {
		this.cx = this.getCx() + this.speedX * this.getSpeed() * delta;
		this.cy = this.getCy() + this.speedY * this.getSpeed() * delta;
	}

	/**
	 * Método chamado quando um dos jogadores pontua no jogo, reiniciando
	 * a posição inicial da bola com a chamada de getDefaultCx() e getDefaultCy().
	 */
	public void restart() {
		this.cx = this.getDefaultCx();
		this.cy = this.getDefaultCy();
	}
}
