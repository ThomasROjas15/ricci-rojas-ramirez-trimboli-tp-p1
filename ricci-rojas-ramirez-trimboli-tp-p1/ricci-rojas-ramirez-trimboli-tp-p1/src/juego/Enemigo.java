package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Enemigo {
	double x, y;
	Image spriteIzq;
	Image spriteDer;
	Image spritQuemado;
	boolean dir; 
	boolean estaApoyado;
	double escala;
	double alto;
	double ancho;
	double velocidad;
	double velocidadVertical;
	long ultimoTiempoDisparo;
	boolean seEstaQuemando;
	double ultimaPosicionx;
	
	public Enemigo(double x, double y) {
		this.x = x;
		this.y = y;
		spriteIzq = Herramientas.cargarImagen("soldadocorriendoizq.gif");
		spriteDer = Herramientas.cargarImagen("soldadocorriendoderecha.gif");
		spritQuemado = Herramientas.cargarImagen("enemigoquemandose.gif");
		dir = false;
		estaApoyado = false;
		seEstaQuemando = false;
		escala = 0.20;
		alto = spriteIzq.getHeight(null) * escala;
		ancho = spriteIzq.getWidth(null) * escala-4;
		velocidad = 1;
		velocidadVertical = 4;
		ultimoTiempoDisparo = System.currentTimeMillis();
	}
	
	public void mostrar(Entorno e) {
		if (seEstaQuemando) {
			e.dibujarImagen(spritQuemado, x, y, 0, escala);
		}
		else {
			if (dir) {
				e.dibujarImagen(spriteDer, x, y, 0, escala);
			}
			if (!dir) {
				e.dibujarImagen(spriteIzq, x, y, 0, escala);
			  }	
		}
	}
	
	public void moverse(boolean dirMov) {
		if (estaApoyado && !seEstaQuemando) {
			
				if (dirMov) {
					this.x += velocidad;
				} 
				else if (!dirMov){
					this.x -= velocidad;
				
				}
				this.dir = dirMov;
		}
		
		//mover verticalmente hacia abajo
		if (!estaApoyado) {
			this.y += velocidadVertical;
		}
	}
	

	
	public double getTecho() {
		return y - alto/2;
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
