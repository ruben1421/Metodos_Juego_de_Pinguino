package Metodos_Juego_de_Pinguino;
import java.util.ArrayList;

public abstract class Casilla {
   int posicion;
  ArrayList<usuario> jugadoresActuales;
    
    public Casilla(int posicion) {
        this.posicion = posicion;
        this.jugadoresActuales = new ArrayList<>();
    }
    
    public int getPosicion() {
        return posicion;
    }
    
    public ArrayList<usuario> getJugadoresActuales() {
        return jugadoresActuales;
    }
    
    public void añadirJugador(usuario jugador) {
        jugadoresActuales.add(jugador);
        realizarAccion(jugador);
    }
   
    public void quitarJugador(usuario jugador) {
        jugadoresActuales.remove(jugador);
    }
    
    public abstract void realizarAccion(usuario jugador);
}
