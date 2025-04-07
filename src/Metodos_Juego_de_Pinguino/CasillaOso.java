package Metodos_Juego_de_Pinguino;

public class CasillaOso extends Casilla {
    public CasillaOso(int posicion) {
        super(posicion);
    }
    
    public void realizarAccion(usuario jugador) {
        if (jugador.getPeces() > 0) {
            jugador.setPeces(jugador.getPeces() - 1);
            System.out.println(jugador.getNombre() + " ha usado un pez para calmar al oso!");
        } else {
            jugador.setPosicion(0);
            System.out.println(jugador.getNombre() + " ha sido atacado por un oso y vuelve al inicio!");
        }
    }
}
