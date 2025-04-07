package Metodos_Juego_de_Pinguino;

public class Casilla {
	
	    private String tipo;
	    private String efecto;
	    
	
	    public Casilla(String tipo, String efecto) {
	        this.tipo = tipo;
	        this.efecto = efecto;
	    }
	    
	    private String asignarEfectoPorTipo(String tipo) {
	        switch (tipo) {
	            case "Oso":
	                return "retroceder";
	            case "Trineo":
	                return "avanzar";
	            case "Agujero":
	                return "retroceder";
	            case "Interrogacion":
	                return "evento"; // 
	            default:
	                return "ninguno";
	        }
	    }
	    public String getTipo() {
	        return tipo;
	    }
	    

	    public String getEfecto() {
	        return efecto;
	    }}



	    public void aplicarEfecto(usuario usuario) {
	        switch (efecto) {
	            case "retroceder":
	                usuario.retrocederCasillas(2);
	                System.out.println("¡Retrocedes 2 casillas!");
	                break;
	            case "avanzar":
	                usuario.avanzarCasillas(3);
	                System.out.println("¡Avanzas 3 casillas!");
	                break;
	            case "evento":
	                System.out.println("¡Ocurre un evento aleatorio! (por implementar)");
	                break;
	            case "ninguno":
	                System.out.println("Esta casilla no tiene efecto.");
	                break;
	            default:
	                System.out.println("Efecto desconocido.");
	                break;
	        }
	    }
	}

	    

