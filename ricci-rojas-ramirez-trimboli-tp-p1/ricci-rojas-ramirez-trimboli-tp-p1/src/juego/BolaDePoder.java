package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class BolaDePoder {
	double x, y, escala, alto, ancho, velocidad;
	boolean dir; // false = Izq
	Image spriteIzq;
	Image spriteDer;

	public BolaDePoder(double x, double y, boolean direccion) {
		this.x = x;
		this.y = y;
		spriteIzq = Herramientas.cargarImagen("boladepoderr.png");
		spriteDer = Herramientas.cargarImagen("boladepoderr.png");
		dir = direccion;
		escala = 0.05;
		alto = spriteIzq.getHeight(null) * escala;
		ancho = spriteIzq.getWidth(null) * escala;
		this.velocidad = 5;
	}

	public void mostrar(Entorno e) {
		if (dir) {
			e.dibujarImagen(spriteDer, x, y, 0, escala);
		} else {
			e.dibujarImagen(spriteIzq, x, y, 0, escala);
		}
	}

	public void moverse() {
		if (this.dir) {
			this.x += velocidad;
		} else {
			this.x -= velocidad;
		}
	}
	
	public double getTecho() {
		return y - alto / 2;
	}

	public double getPiso() {
		return y + alto / 2;
	}

	public double getDerecho() {
		return x + ancho / 2;
	}

	public double getIzquierdo() {
		return x - ancho / 2;
	}

}

