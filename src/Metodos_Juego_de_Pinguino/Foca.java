package Metodos_Juego_de_Pinguino;
import java.util.Random;

public class Foca {
    
	 private boolean soborno;

	  
	    public void Foca() {
	        this.soborno = false;
	    }


	    public boolean isSobornado() {
	        return soborno;
	    }



	    public void setSoborno(boolean soborno) {
	        this.soborno = soborno;
	    }

	    // Método para golpear al jugador
	    public void golpearJugador(clase_pinguino pinguino) {
	        if (soborno) {
	            System.out.println("La foca no golpea a " + pinguino + " porque fue sobornada.");
	        } else {
	            System.out.println("La foca golpea al jugador " + pinguino + "!");
	        }
	    }

	    // Método para aplastar al jugador
	    public void aplastarJugador(clase_pinguino pinguino) {
	        if (soborno) {
	            System.out.println("La foca no aplasta a " + pinguino + " porque fue sobornada.");
	        } else {
	            System.out.println("La foca aplasta al jugador " + pinguino + "!");
	        }
	    }

	    // Método para verificar si la foca ha sido sobornada
	    public void esSobornado() {
	        if (soborno) {
	            System.out.println("La foca ha sido sobornada.");
	        } else {
	            System.out.println("La foca no ha sido sobornada.");
	        }
	    }

	
	    public static void main(String[] args, usuario rival, Inventario inventario) {

	    	clase_pinguino pinguino = new clase_pinguino("Pipo", "Azul", 1);

	  
	        Random r = new Random();
	        Foca foca = new Foca();
	       

	       
	        foca.esSobornado();
	        
	        
	        foca.setSoborno(true);
	        foca.esSobornado();
	        
	        
	        foca.aplastarJugador(pinguino);
	        foca.golpearJugador(pinguino);
	    

	    


        System.out.println("La Foca intenta pegar a " + rival.getNombre());
       
        if (inventario.getCantidadPeces() > 0) {
            // Si el jugador tiene peces, puede alimentar a la foca para bloquearla
            inventario.setCantidadPeces(inventario.getCantidadPeces() - 1);
            turnosBloqueada = 2;
            System.out.println(rival.getNombre() + " alimentó a la foca con un pez! La foca estará bloqueada 2 turnos.");
        } else {
        
        int golpe = r.nextInt(2);
        
        if (golpe == 1) {
            rival.setPosicion(2); 
            System.out.println(rival.getNombre() + " ha sido golpeado y retrocede 2 casillas.");
        } else {
            System.out.println("El golpe ha fallado y no ha afectado");
        }
    }
	   }
}
