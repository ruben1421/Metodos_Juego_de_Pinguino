package Metodos_Juego_de_Pinguino;
import java.util.Random;

public class CasillaInterrogante extends Casilla {
    private Random r;
    
    public CasillaInterrogante(int posicion) {
        super(posicion);
        this.r = new Random();
    }

    public void realizarAccion(usuario jugador) {
        int evento = r.nextInt(4);
        switch (evento) {
            case 0:
                obtenerPez(jugador);
                break;
            case 1:
                obtenerBolasNieve(jugador);
                break;
            case 2:
                dadoRapido(jugador);
                break;
            case 3:
                dadoLento(jugador);
                break;
        }
    }
    
    private void obtenerPez(usuario jugador) {
        Inventario.setPeces(Math.min(Inventario.getPeces() + 1, 2));
        System.out.println(jugador.getNombre() + " ha ganado un pez!");
    }
    
    private void obtenerBolasNieve(usuario jugador) {
        int cantidad = r.nextInt(3) + 1;
        jugador.setBolasNieve(Math.min(jugador.getBolasNieve() + cantidad, 6));
        System.out.println(jugador.getNombre() + " ha conseguido " + cantidad + " bolas de nieve!");
    }
    
    private void dadoRapido(usuario jugador) {
        int avance = r.nextInt(6) + 5;
        jugador.avanzarCasillas(avance);
        System.out.println(jugador.getNombre() + " ha obtenido un dado rapido! Avanza " + avance + " casillas");
    }
    
    private void dadoLento(usuario jugador) {
        int retroceso = r.nextInt(3) + 1;
        jugador.retrocederCasillas(retroceso);
        System.out.println(jugador.getNombre() + " ha conseguido un dado lento! Retrocede " + retroceso + " casillas");
    }
}
