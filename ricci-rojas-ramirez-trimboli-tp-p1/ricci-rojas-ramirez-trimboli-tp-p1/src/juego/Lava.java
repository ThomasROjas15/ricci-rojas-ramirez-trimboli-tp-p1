package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

class Lava {
	// Otras propiedades y métodos
	private double x;
	private double y;
	private double alto;
	Image imageLava;
	double escala;
	private double velocidadAscenso;

	public Lava(double x, double y, double velocidadAscenso) {
		
		this.x = x;
		this.y = y+40;
		this.escala = 1.6;
		this.imageLava = Herramientas.cargarImagen("lavanogif.png");
		this.alto =  imageLava.getHeight(null) * escala;
		this.velocidadAscenso = velocidadAscenso;
		
		
	}
	public void subir() {
		this.y -=velocidadAscenso;
	}
	public double GetY(){
		return y;
	}

	public void dibujarse(Entorno entorno) {
		imageLava = Herramientas.cargarImagen("lavanogif.png");
		entorno.dibujarImagen(imageLava, x, y, 0,escala);
		
	}
	
	public double getTecho() {
		return y - alto/2;
	}

	
	@Override
	public String toString() {
		return "-";
	}
}

