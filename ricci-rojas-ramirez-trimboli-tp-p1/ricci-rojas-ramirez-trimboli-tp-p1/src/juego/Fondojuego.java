package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Fondojuego {
	private int x;
	private int y;
	double angulo;
	Image imagenGameOver;
	Entorno entorno;
	public Fondojuego() {

		this.x = 400;
		this.y = 300;
		
		imagenGameOver = Herramientas.cargarImagen("lavafondo.jpg");

	}

	public void dibujarFondo(Entorno entorno) {

		entorno.dibujarImagen(imagenGameOver, this.x, this.y, 0, 1.8);

	}

}
