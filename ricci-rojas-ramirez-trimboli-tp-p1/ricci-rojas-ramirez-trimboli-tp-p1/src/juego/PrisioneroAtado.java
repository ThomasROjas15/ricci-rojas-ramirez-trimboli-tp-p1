package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class PrisioneroAtado {
	double x, y;
	Image prisionero;
	boolean estaApoyado;
	double escala;
	double alto;
	double ancho;
	double velocidadVertical;
	
	public PrisioneroAtado(double x, double y) {
		this.x = x;
		this.y = y;
		prisionero = Herramientas.cargarImagen("PrisioneroAtado.gif");
		estaApoyado = false;
		escala = 0.25;
		alto = prisionero.getHeight(null) * escala;
		ancho = prisionero.getWidth(null) * escala-4;
		velocidadVertical=4;
	}
	
	public void mostrar(Entorno e) {
			e.dibujarImagen(prisionero, x, y, 0, escala);
	}
	
	public void movVertical() {
		if (!estaApoyado) {
			this.y += velocidadVertical;
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
