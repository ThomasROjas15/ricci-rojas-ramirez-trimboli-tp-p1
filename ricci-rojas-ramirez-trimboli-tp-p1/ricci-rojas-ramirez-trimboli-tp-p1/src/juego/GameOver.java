package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class GameOver {
	private int x;
	private int y;
	double angulo;
	Image imagenGameOver;
	Entorno entorno;
	public GameOver() {

		this.x = 400;
		this.y = 300;
		
		imagenGameOver = Herramientas.cargarImagen("GamaOver.jpg");

	}

	public void dibujarGameOver(Entorno entorno) {

		entorno.dibujarImagen(imagenGameOver, this.x, this.y, 0, 1.3);

	}

}


