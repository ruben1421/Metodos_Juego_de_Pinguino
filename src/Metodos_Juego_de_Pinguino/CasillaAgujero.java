package Metodos_Juego_de_Pinguino;

public class CasillaAgujero extends Casilla {
    private Tablero tablero;
    
    public CasillaAgujero(int posicion, Tablero tablero) {
        super(posicion);
        this.tablero = tablero;
    }
    
    public void realizarAccion(usuario jugador) {
        int nuevaPos = tablero.encontrarAgujeroAnterior(posicion);
        jugador.setPosicion(nuevaPos);
        System.out.println(jugador.getNombre() + " ha caido en un agujero y retrocede a la casilla " + nuevaPos);
    }
}