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

	  
	    public void aplastarJugador(Pinguino p) {
	        if (soborno) {
	            System.out.println("La foca no aplasta a " + p.getNombre() + " porque fue sobornada.");
	        } else {
	            System.out.println("La foca aplasta al jugador " + p.getNombre() + "!");
	          
	        }
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

	class Pinguino {
	    private String nombre;

	  
	    public Pinguino(String nombre) {
	        this.nombre = nombre;
	    }

	    
	    public String getNombre() {
	        return nombre;
	    }

	   
	    public void setNombre(String nombre) {
	        this.nombre = nombre;
	    }
	}

