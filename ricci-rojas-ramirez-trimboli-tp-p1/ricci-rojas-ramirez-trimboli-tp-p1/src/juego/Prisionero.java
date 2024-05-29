package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Prisionero {
	double x, y;
	Image spriteIzq;
	Image spriteDer;
	Image spritquiet;
	boolean dir; // false = Izq
	boolean quiet;
	boolean estaApoyado;
	boolean estaSaltando; //false = no esta saltando
	double escala;
	double alto;
	double ancho;
	int contadorSalto;
	double velocidad;
	double velocidadVertical;
	boolean ultimaDireccion; // Nueva variable para mantener la última dirección

	public Prisionero(double x, double y) {
		this.x = x;
		this.y = y;
		spriteIzq = Herramientas.cargarImagen("prisinerocaminandoizquierda.gif");
		spriteDer = Herramientas.cargarImagen("prisinerocaminando.gif");
		spritquiet = Herramientas.cargarImagen("prisioneroquietomasgrande.gif");
		dir = false;
		quiet = false;
		contadorSalto = 0;
		estaApoyado = false;
		estaSaltando = false;
		escala = 0.18;
		alto = spriteIzq.getHeight(null) * escala;
		ancho = spriteIzq.getWidth(null) * escala-4;
		velocidad = 2;
		velocidadVertical=4;
		ultimaDireccion = false; // Inicialmente apunta a la izquierda
	}

	public void mostrar(Entorno e) {
		if (quiet) {
			e.dibujarImagen(spritquiet, x, y, 0, escala);
		}
		else {
			if (dir) {
				e.dibujarImagen(spriteDer, x, y, 0, escala);
			}
			if(!dir) {
				e.dibujarImagen(spriteIzq, x, y, 0, escala);
			}
		}
	}

	public void moverse(boolean dirMov) {
		if (estaApoyado || estaSaltando) {
			if (dirMov) {
				this.x += velocidad;
			} else {
				this.x -= velocidad;
			}
			this.dir = dirMov;
			this.ultimaDireccion = dirMov; // Actualiza la última dirección
		}
	}

	
	
	public void movVertical() {
		if (!estaApoyado && !estaSaltando) {	
			this.y += velocidadVertical;
		}
		if(estaSaltando) {
			this.y -= 7;
			contadorSalto+=2;
		}
		if(contadorSalto == 40) {
			contadorSalto = 0;
			estaSaltando = false;
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
