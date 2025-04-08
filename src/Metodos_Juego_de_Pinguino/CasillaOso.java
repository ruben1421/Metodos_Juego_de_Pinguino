package Metodos_Juego_de_Pinguino;

public class CasillaOso extends Casilla {
    public CasillaOso(int posicion) {
        super(posicion);
    }
    
    public void realizarAccion(usuario jugador, Inventario inventario) {
        if (Inventario.getPeces() > 0) {
            Inventario.setPeces(Inventario.getPeces() - 1);
            System.out.println(jugador.getNombre() + " ha usado un pez para calmar al oso!");
        } else {
            jugador.setPosicion(0);
            System.out.println(jugador.getNombre() + " ha sido atacado por un oso y vuelve al inicio!");
        }
    }
}
