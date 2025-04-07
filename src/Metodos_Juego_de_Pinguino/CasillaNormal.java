package Metodos_Juego_de_Pinguino;

public class CasillaNormal extends Casilla {
    public CasillaNormal(int posicion) {
        super(posicion);
    }
    
    public void realizarAccion(usuario jugador) {
        // No realiza ninguna acción especial
        System.out.println(jugador.getNombre() + " ha caido en una casilla normal");
    }
}