package Metodos_Juego_de_Pinguino;

public class Casilla {
	
	    private String tipo;
	    private String efecto;
	    
	
	    public Casilla(String tipo, String efecto) {
	        this.tipo = tipo;
	        this.efecto = efecto;
	    }
	    
	    public String getTipo() {
	        return tipo;
	    }
	    
	    public String getEfecto() {
	        return efecto;
	    }
	    
	    
	    
	    public void aplicarEfecto(usuario usuario) {
	    	if (this.efecto.equals("retroceder")) {
	    		usuario.retrocederCasillas(2);
	        } else if (this.efecto.equals("avanzar")) {
	        	usuario.avanzarCasillas(3); 
	        } else {
	            System.out.println("Efecto desconocido");
	        }
	    }
	    
	    
}
