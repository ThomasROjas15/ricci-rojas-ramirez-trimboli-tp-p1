package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Winner {
	private int x;
	private int y;
	double angulo;
	Image imagenWinner;
	Entorno entorno;
	
	public Winner() {

		this.x = 400;
		this.y = 300;
		
		imagenWinner = Herramientas.cargarImagen("Winner.jpg");

	}

	public void dibujarWinner(Entorno entorno) {

		entorno.dibujarImagen(imagenWinner, this.x, this.y, 0, 1.5);

	}
}
