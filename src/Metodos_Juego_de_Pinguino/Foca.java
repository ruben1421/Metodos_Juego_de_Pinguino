package Metodos_Juego_de_Pinguino;
import java.util.Random;

public class Foca {
    
	 private boolean soborno;

	    public Foca() {
	        this.soborno = false;
	    }

	    public boolean isSobornado() {
	        return soborno;
	    }

	    public void setSoborno(boolean soborno) {
	        this.soborno = soborno;
	    }

	    // Método para golpear al jugador
	    public void golpearJugador(String nombreJugador) {
	        if (soborno) {
	            System.out.println("La foca no golpea a " + nombreJugador + " porque fue sobornada.");
	        } else {
	            System.out.println("La foca golpea al jugador " + nombreJugador + "!");
	        }
	    }

	    // Método para aplastar al jugador
	    public void aplastarJugador(String nombreJugador) {
	        if (soborno) {
	            System.out.println("La foca no aplasta a " + nombreJugador + " porque fue sobornada.");
	        } else {
	            System.out.println("La foca aplasta al jugador " + nombreJugador + "!");
	        }
	    }

	    // Método para verificar si la foca ha sido sobornada
	    public void esSobornado() {
	        if (soborno) {
	            System.out.println("La foca ha sido sobornada.");
	        } else {
	            System.out.println("La foca no ha sido sobornada.");
	        }
	    }}
