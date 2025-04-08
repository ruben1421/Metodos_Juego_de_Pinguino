package Metodos_Juego_de_Pinguino;
import java.util.Random;

public class Foca {
    
	 private boolean soborno;

    public void golpearCola(usuario rival) {

	  
	    public Foca() {
	        this.soborno = false; 
	    }


	    public boolean isSobornado() {
	        return soborno;
	    }


    public void golpearCola(usuario rival,usuario posicion) {


	    public void setSoborno(boolean soborno) {
	        this.soborno = soborno;
	    }


	  
	    public void golpearJugador(Pinguino p) {
	        if (soborno) {
	            System.out.println("La foca no golpea a " + p.getNombre() + " porque fue sobornada.");
	        } else {
	            System.out.println("La foca golpea al jugador " + p.getNombre() + "!");
	           
	        }
	    }

	
	    public void esSobornado() {
	        if (soborno) {
	            System.out.println("La foca ha sido sobornada.");
	        } else {
	            System.out.println("La foca no ha sido sobornada.");
	        }
	    }

	
	    public static void main(String[] args) {

	        Pinguino pinguino = new Pinguino("Pipo");

	  
	        Foca foca = new Foca();

	       
	        foca.esSobornado();
	        
	        
	        foca.setSoborno(true);
	        foca.esSobornado();
	        
	        
	        foca.aplastarJugador(pinguino);
	        foca.golpearJugador(pinguino);
	    }
	}

        System.out.println("La Foca intenta pegar a " + rival.getNombre());
       
        if (rival.getPeces() > 0) {
            // Si el jugador tiene peces, puede alimentar a la foca para bloquearla
            rival.setPeces(rival.getPeces() - 1);
            turnosBloqueada = 2;
            System.out.println(rival.getNombre() + " alimentó a la foca con un pez! La foca estará bloqueada 2 turnos.");
        } else {
        
        int golpe = random.nextInt(2);
        
        if (golpe == 1) {
            rival.setposicion(2); 
            System.out.println(rival.getNombre() + " ha sido golpeado y retrocede 2 casillas.");
        } else {
            System.out.println("El golpe ha fallado y no ha afectado");
        }
    }
 }
}

