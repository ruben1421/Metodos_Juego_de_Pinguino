package Metodos_Juego_de_Pinguino;

public class CasillaTrineo extends Casilla {
    private tablero tablero;
    
    public CasillaTrineo(int posicion, tablero tablero) {
        super(posicion);
        this.tablero = tablero;
    }
    
    public void realizarAccion(usuario jugador) {
        int nuevaPos = tablero.encontrarSiguienteTrineo(posicion);
        jugador.setPosicion(nuevaPos);
        System.out.println(jugador.getNombre() + " se ha subido al trineo y avanza a la casilla " + nuevaPos);
    }
}
