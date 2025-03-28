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
	    
	    public void aplicarEfecto(Usuario usuario) {
	        if (this.efecto.equals("retroceder")) {
	            usuario.avanzarCasillas(-2);
	        } else if (this.efecto.equals("avanzar")) {
	            usuario.avanzarCasillas(3);
	        } else {
	            System.out.println("Efecto desconocido");
	        }
	    }
	    
	    public static void testCasilla() {
	        Usuario pinguinoPino = new Pinguino(1, "azul", "PinguinoPino");
	        Casilla casillaAvance = new Casilla("normal", "avanzar");
	        Casilla casillaRetroceso = new Casilla("normal", "retroceder");
	        
	        System.out.println("Posicion inicial: 0");
	        casillaAvance.aplicarEfecto(pinguinoPino);
	        System.out.println("Despuws de avanzar: (verificar la salida esperada)");
	        casillaRetroceso.aplicarEfecto(pinguinoPino);
	        System.out.println("Despuws de retroceder: (verificar si el error pasa o no)");
	    }

}
