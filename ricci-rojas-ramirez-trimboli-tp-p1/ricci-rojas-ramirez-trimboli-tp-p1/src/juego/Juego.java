package juego;

import entorno.Entorno;
import entorno.InterfaceJuego;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Juego extends InterfaceJuego {

		private Entorno entorno;
		private GameOver gameOver;
		private Winner winner;
		private Fondojuego fondo;
		Prisionero prisionero;
		PrisioneroAtado prisioneroAtado;
		Piso[] p;
		BolaDePoder bolaDePoder;
		List<Bala> balasEnemigas; 
		List<Enemigo> soldados;
		int cont = 80;
		int cont2 = 80;
		Lava lava;
		long ultimoTiempoLava;
		long intervaloLava;
		long intervaloDisparo;
		long tiempoActual;
		int enemigosEliminados = 0;
		int puntaje = 0;
		
		
		
		

		public Juego() {

			// Inicializa el objeto entorno
			this.entorno = new Entorno(this, "Prueba del Entorno", 800, 600);
			this.gameOver= new GameOver();
			this.winner = new Winner();
			this.fondo= new Fondojuego();
			lava = new Lava(400, 1000, 0.1);
			prisionero = new Prisionero(300, 540);
			prisioneroAtado = new PrisioneroAtado(40,70);
			p = new Piso[5];
			soldados = new ArrayList<>();
			balasEnemigas = new ArrayList<>();
			for (int i = 0; i < p.length; i++) {
				p[i] = new Piso(120 + i * (entorno.alto() / p.length));
			}

			for (int i = 0; i < 8; i++) {
				if (i % 2 == 0) {
					soldados.add(new Enemigo(20, cont));
					cont = cont + 100;
				} else {
					soldados.add(new Enemigo(780, cont2));
					cont2 = cont2 + 100;
				}
			}

			ultimoTiempoLava = System.currentTimeMillis();
			intervaloLava = 10;
			intervaloDisparo = 3000;

			// Inicializar lo que haga falta para el juego
			// ...

			// Inicia el juego!
			this.entorno.iniciar();
			
		}

		/**
		 * Durante el juego, el método tick() será ejecutado en cada instante y por lo tanto es el método más importante de esta clase. Aquí se debe actualizar el estado interno del juego para simular el paso del tiempo (ver el enunciado del TP para mayor detalle).
		 */
		public void tick() {
			
			//seccion  relacionada con el entorno del juego
			dibujarInformacion();
			fondo.dibujarFondo(entorno); //muestra en el entorno la imagen del fondo del volcan del juego.
			tiempoActual = System.currentTimeMillis();// Procesamiento de un instante de tiempo para guardar en la variable el tiempo actual durante el juego
			
			if (prisionero == null) { //sentencia de control de flujo para frenar la ejecucion del juego y mostrar en el entrono la imagen de game over si el personaje principal "muere"
				gameOver.dibujarGameOver(entorno);
				return;
			}
			if (prisioneroAtado == null )  { //sentencia de control de flujo para frenar la ejecucion del juego y mostrar en el entorno la imagen de winner si el personaje principal "salva" al pricionero atado
				winner.dibujarWinner(entorno);
				return;
			}
			
			//seccion de la generacion de pisos
			for (int i = 0; i < p.length; i++) {//recorre la lista de los pisos y los muestra en el entorno
				p[i].mostrar(entorno);
			}
			
			//seccion del prisionero atado al que hay que liberar para ganar el juego
			prisioneroAtado.mostrar(entorno);
			prisioneroAtado.movVertical();
			prisioneroAtado.estaApoyado = detectarApoyo(prisioneroAtado, p);//detectar apoyo del prisionero atado
			
			//seccion del prisionero (personaje principal)
			if (prisionero != null) { //si el prisionero no es un objeto inexistente o null sus funciones se ejecutaran sin afectar el juego
				
				prisionero.mostrar(entorno);
				prisionero.movVertical();
				
				if (entorno.estaPresionada(entorno.TECLA_DERECHA) || entorno.estaPresionada(entorno.TECLA_IZQUIERDA)
						|| entorno.estaPresionada(entorno.TECLA_ARRIBA) && prisionero.estaApoyado
						|| entorno.estaPresionada(entorno.TECLA_ESPACIO)) { //esta sentancia comprueba que el prisionero no este moviendose.
					prisionero.quiet = false;
	
					if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
						prisionero.moverse(true);
					}
					if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
						prisionero.moverse(false);
					}
					if (bolaDePoder == null && entorno.estaPresionada(entorno.TECLA_ESPACIO)) {
						bolaDePoder = new BolaDePoder(prisionero.x, prisionero.y, prisionero.ultimaDireccion);
					}
					if (entorno.estaPresionada(entorno.TECLA_ARRIBA) && prisionero.estaApoyado) {
						prisionero.estaSaltando = true;
					}
				} 
				
				else {
					prisionero.quiet = true;//si no se esta moviondo entonces la variable de instancia de la clase prisionero para saber si esta quieto pasa a ser true  
				}
				
				
				prisionero.estaApoyado = detectarApoyo(prisionero, p);//detecta si el prisionero esta apoyado o no sobre el piso
				
	
				if (detectarColision(prisionero, p)) {//esto es para que cuando el pricionero choque contra un bloque del piso deje de saltar y caiga.
					System.out.println("Colision encontrada");
					prisionero.estaSaltando = false;
					prisionero.contadorSalto = 0;
				}
	
				if (detectarColision(prisionero, lava)) {//esto es para que el pricionero sea null si entra en contacto con la lava
					System.out.println("perdiste");
					prisionero = null;
				}
				
				//detectar colision entre el prisionero y el prisionero atado para que este se vuelva null si es que hay colision 
				if (prisionero!= null && prisioneroAtado !=null && detectarColision (prisioneroAtado, prisionero)) { 
					System.out.println("ganaste!!");
					prisioneroAtado = null;
				}
			}	
			
			
			//seccion de enemigos
			
			//detecta si cada enemigo esta apoyado sobre el piso
			for (Enemigo soldado : soldados) {
				if (soldado != null) {
					soldado.estaApoyado = detectarApoyo(soldado, p);
				}
			}
			
			// control de enemigos: Ubicación y movimiento de los soldados enemigos:
			for (Enemigo soldado : soldados) {
				if (soldado != null) { //si el soldado no es null procede a aplicarle los metodos para su funcionamiento adecuado en el juego
				
					soldado.mostrar(entorno);// Mostrar soldado en el entorno
					soldado.estaApoyado = detectarApoyo(soldado, p);// Detectar apoyo, si no esta apoyado (estaApoyado=false) cae

					// sentencias de control de los límites de la pantalla:
					if (soldado.x == 800) {
							soldado.dir = false; // si llega al limite derecho de la pantalla cambiar dirección hacia la izquierda
						} 
					else if (soldado.x == 0) {
							soldado.dir = true; // di llega al limite izquierdo de la pantalla Cambiar dirección hacia la derecha
						}
					soldado.moverse(soldado.dir);//llamada al metodo de instancia de la clase para mover al soldado a la direccion correspondiente
				}
			}
			
			//deteccion si el soldado se esta quemando con la lava
			for (Enemigo soldado : soldados) {
				if (soldado.estaApoyado && soldado.getPiso() >= lava.getTecho()) {
					soldado.seEstaQuemando = true;
				}else if(soldado.getTecho() + soldado.velocidadVertical >= lava.getTecho()){
					soldado.seEstaQuemando = true;
				}
			}
			
			//remueve un soldado de la lista si este colisiona con la lava
			soldados.removeIf(soldado -> detectarColision(lava, soldado));

			
			//recorre la lista de soldados para detectar si alguno colisiona con el prisionero
			for (int i = 0; i <soldados.size();i++) {  
				Enemigo soldado = soldados.get(i); //obtener el elemeneto en la posicion I
				if (prisionero!= null && detectarColision (prisionero, soldado)) { 
					System.out.println("el prisionero colisiono con el enemigo");
					prisionero = null;
				}
			}
			
			
			 //seccion de disparos:
			
			//genera el disparo del pricionero
			if (bolaDePoder != null) {
				bolaDePoder.mostrar(entorno);
				bolaDePoder.moverse();
			}
			
			//controla los limites del recorrido del disparo del pricionero
			if (bolaDePoder != null && (bolaDePoder.x < -0.1 * entorno.ancho() || bolaDePoder.x > entorno.ancho() * 1.1)) {
				bolaDePoder = null;//al llegar a uno de los limites la bola de poder se vuelve null para asi poder efectuar otro disparo
			}
			
			//genera y controla el tiempo de los disparos de las balas enemigas
			for (Enemigo soldado : soldados) {
				if (soldado != null && !soldado.seEstaQuemando && tiempoActual - soldado.ultimoTiempoDisparo >= intervaloDisparo) {//si el soldado no existe, no se esta qumando y pasa el tiempo de intervalo de cada disparo se efectua un nuevo disparo de cada soldado
			        balasEnemigas.add(new Bala(soldado.x, soldado.y, soldado.dir));
			        soldado.ultimoTiempoDisparo = tiempoActual; // Actualizar el tiempo de disparo después de disparar
			    }
			}
			
			//verifica colisiones entre las balas del prisionero y los enemigos
			for (int i = 0; i < soldados.size(); i++) {//recorre la lista de soldados 
			    Enemigo soldado = soldados.get(i); //obtener el elemeneto en la posicion I
			    if (soldado != null && detectarColision(bolaDePoder, soldado)) {
			        soldados.remove(i); // Elimina el enemigo de la lista
			        i--; // Retrocede el índice para evitar errores
			        bolaDePoder =null;
			        enemigosEliminados++;//se incrementa la variable que cuanta los enemigos eliminados
			        puntaje+=2;//se incrementa el puntaje de la partida
			    }
			    
			}

			//muestra y controla el movimiento y las colisiones de las balas enemigas contra el prisionero y la bola de poder  en el entorno
			Iterator<Bala> iteratorBalasEnemigas = balasEnemigas.iterator();
		    while (iteratorBalasEnemigas.hasNext()) {
		        Bala balaEnemiga = iteratorBalasEnemigas.next();//le asigna el valor del elemento i de la lista de balasenemigas a una variable balaEnemiga de la clase Bala
		        if (balaEnemiga != null) {
		            balaEnemiga.mostrar(entorno);
		            balaEnemiga.moverse();
		            if (balaEnemiga.x < -0.1 * entorno.ancho() || balaEnemiga.x > entorno.ancho() * 1.1) {//controla los limites del recorrido de las balas enemigas
		                iteratorBalasEnemigas.remove();
		            } else if (prisionero != null && detectarColision(prisionero, balaEnemiga)) {//si el prisionero colisiona con una bala enemiga este pasa a ser null y la vala se remueve 
		                System.out.println("el prisionero fue abatido");
		                prisionero = null;
		                iteratorBalasEnemigas.remove();
		            } else if (bolaDePoder != null && detectarColision(balaEnemiga, bolaDePoder)) {//si la bola de poder y la bala enemiga colisionan la bala enemiga se remueve y la bola de poder pasa aser null
		                System.out.println("Bala enemiga colisionó con la bola de poder");
		                iteratorBalasEnemigas.remove();
		                bolaDePoder = null;
		            }
		        }
		    }
				
		    
		    //seccion lava:
		    
			// Control de la lava
			if (tiempoActual - ultimoTiempoLava >= intervaloLava) {//si paso el intervalo de tiempo delimitado la lava sube
				lava.subir();
				ultimoTiempoLava = tiempoActual;//se actualiza despues de que la lava suba 
			}
			lava.dibujarse(entorno);

		}
		

		@SuppressWarnings("unused")
		public static void main(String[] args) {
			Juego juego = new Juego();
			}
		

		// Método de detección de apoyo para el prisionero  con el piso
		public boolean detectarApoyo(Prisionero pri, Bloque bl) {
			return Math.abs((pri.getPiso() - bl.getTecho())) < 2 && (pri.getIzquierdo() < (bl.getDerecho()))
					&& (pri.getDerecho() > (bl.getIzquierdo()));
		}

		public boolean detectarApoyo(Prisionero ba, Piso pi) {
			for (int i = 0; i < pi.bloques.length; i++) {
				if (pi.bloques[i] != null && detectarApoyo(ba, pi.bloques[i])) {
					return true;
				}
			}

			return false;
		}
		
		public boolean detectarApoyo(Prisionero ba, Piso[] pisos) {
			for (int i = 0; i < pisos.length; i++) {
				if (detectarApoyo(ba, pisos[i])) {
					return true;
				}
			}

			return false;
		}
		
		// Método de detección de apoyo para los enemigos con el piso
		public boolean detectarApoyo(Enemigo sol, Bloque bl) {
			return Math.abs((sol.getPiso() - bl.getTecho())) < 2 && (sol.getIzquierdo() < (bl.getDerecho()))
					&& (sol.getDerecho() > (bl.getIzquierdo()));
		}

		

		public boolean detectarApoyo(Enemigo sol, Piso pi) {
			for (int i = 0; i < pi.bloques.length; i++) {
				if (pi.bloques[i] != null && detectarApoyo(sol, pi.bloques[i])) {
					return true;
				}
			}

			return false;
		}

		
		public boolean detectarApoyo(Enemigo sol, Piso[] pisos) {
			for (int i = 0; i < pisos.length; i++) {
				if (detectarApoyo(sol, pisos[i])) {
					return true;
				}
			}

			return false;
		}

		//metodo de deteccion de apoyo para el pridionero atado con el piso
		public boolean detectarApoyo(PrisioneroAtado pri, Bloque bl) {
			return Math.abs(pri.getPiso() - bl.getTecho()) < 2 && (pri.getIzquierdo() < (bl.getDerecho()))
					&& (pri.getDerecho() > (bl.getIzquierdo()));
		}
		

		public boolean detectarApoyo(PrisioneroAtado ba, Piso pi) {
			for (int i = 0; i < pi.bloques.length; i++) {
				if (pi.bloques[i] != null && detectarApoyo(ba, pi.bloques[i])) {
					return true;
				}
			}

			return false;
		}
		
		public boolean detectarApoyo(PrisioneroAtado ba, Piso[] pisos) {
			for (int i = 0; i < pisos.length; i++) {
				if (detectarApoyo(ba, pisos[i])) {
					return true;
				}
			}

			return false;
		}
		
		// Métodos de detección de apoyo de la lava con prisionero y los enemigos
		public boolean detectarApoyo(Prisionero ba, Lava lav) {
			return Math.abs((ba.getPiso() - lav.getTecho())) < 2;
		}

		public boolean detectarApoyo(Enemigo sol, Lava lav) {
			return Math.abs((sol.getPiso() - lav.getTecho())) < 2;
		}

		// Métodos de colisión con el prisionero y los bloques
		public boolean detectarColision(Prisionero ba, Bloque bl) {
			return Math.abs((ba.getTecho() - bl.getPiso())) < 3.5 && (ba.getIzquierdo() < (bl.getDerecho()))
					&& (ba.getDerecho() > (bl.getIzquierdo()));
		}

		public boolean detectarColision(Prisionero ba, Piso pi) {
			for (int i = 0; i < pi.bloques.length; i++) {
				if (pi.bloques[i] != null && detectarColision(ba, pi.bloques[i])) {
					if (pi.bloques[i].rompible) {
						pi.bloques[i] = null;
					}
					return true;
				}
			}

			return false;
		}

		public boolean detectarColision(Prisionero ba, Piso[] pisos) {
			for (int i = 0; i < pisos.length; i++) {
				if (detectarColision(ba, pisos[i])) {
					return true;
				}
			}

			return false;
		}

		// Métodos de colisión con el prisionero y los enemigos con la lava
		public boolean detectarColision(Prisionero ba, Lava lav) {
			if(prisionero.estaApoyado) {
				return (prisionero.getPiso()-2 >= lava.getTecho());
			}else {
			return (prisionero.getPiso() + prisionero.velocidadVertical >= lava.getTecho());
			}
		}

		public boolean detectarColision(Lava lava, Enemigo sol) {
			if(sol.estaApoyado) {
				return (sol.getTecho() >= lava.getTecho());
			}else {
			return (sol.getTecho() + sol.velocidadVertical >= lava.getTecho());
			}
		}
		
		//metodo para detectar la colision entre el prisionero y la bala enemiga
		public boolean detectarColision(Prisionero ba, Bala bala) {
		    return (Math.abs(ba.x - bala.x) < ba.ancho / 2 + bala.ancho / 2) &&
		           (Math.abs(ba.y - bala.y) < ba.alto / 2 + bala.alto / 2);
		}
		
		//metodo para detectar la colision entre el enemigo y la bola de poder
		public boolean detectarColision(BolaDePoder bolaDePoder, Enemigo enemigo) {
		    return bolaDePoder != null && enemigo != null &&
		           Math.abs(bolaDePoder.x - enemigo.x) < bolaDePoder.ancho / 2 + enemigo.ancho / 2 &&
		           Math.abs(bolaDePoder.y - enemigo.y) < bolaDePoder.alto / 2 + enemigo.alto / 2;
		}
		
		//metodo para detectar la colision entre el prisionero y el enemigo
		public boolean detectarColision(Prisionero ba, Enemigo sol) {
		    return (Math.abs(ba.x - sol.x) < ba.ancho / 2 + sol.ancho / 2) &&
		           (Math.abs(ba.y - sol.y) < ba.alto / 2 + sol.alto / 2);
		}
		
		//metodo para detectar la colision entre el prisionero atado y el prisionero
		public boolean detectarColision(PrisioneroAtado ba, Prisionero pri) {
		    return (Math.abs(ba.x - pri.x) < ba.ancho / 2 + pri.ancho / 2) &&
		           (Math.abs(ba.y - pri.y) < ba.alto / 2 + pri.alto / 2);
		}
		    
		//metodo para dibujar la informacion del juego (los puntos y enemigos eliminados)
		public void dibujarInformacion() {
			entorno.cambiarFont("Arial", 18, Color.WHITE );
;			entorno.escribirTexto("enemigos eliminados; "+ enemigosEliminados, 20, 20);
			entorno.escribirTexto("puntaje: "+ puntaje, 20, 40);
		}
		
		// Método para detectar colisión entre la bala y la bola de poder
		public boolean detectarColision(Bala bala, BolaDePoder bolaDePoder) {
		    return Math.abs(bala.x - bolaDePoder.x) < (bala.ancho + bolaDePoder.ancho) / 2 &&
		           Math.abs(bala.y - bolaDePoder.y) < (bala.alto + bolaDePoder.alto) / 2;
		}



		
	}