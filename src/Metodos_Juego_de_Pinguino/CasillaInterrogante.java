package Metodos_Juego_de_Pinguino;
import java.util.Random;

public class CasillaInterrogante extends Casilla {
    private Random r;
    
    public CasillaInterrogante(int posicion) {
        super(posicion);
        this.r = new Random();
    }

    public void realizarAccion(usuario jugador, Inventario inventario) {
        int evento = r.nextInt(4);
        switch (evento) {
            case 0:
                obtenerPez(jugador, inventario);
                break;
            case 1:
                obtenerBolasNieve(jugador, inventario);
                break;
            case 2:
                dadoRapido(jugador);
                break;
            case 3:
                dadoLento(jugador);
                break;
        }
    }
    
    private void obtenerPez(usuario jugador, Inventario inventario) {
        inventario.setCantidadPeces(Math.min(inventario.getCantidadPeces() + 1, 2));
        System.out.println(jugador.getNombre() + " ha ganado un pez!");
    }
    
    private void obtenerBolasNieve(usuario jugador, Inventario inventario) {
        int cantidad = r.nextInt(3) + 1;
        inventario.setCantidadBolasNieve(Math.min(inventario.getCantidadBolasNieve() + cantidad, 6));
        System.out.println(jugador.getNombre() + " ha conseguido " + cantidad + " bolas de nieve!");
    }
    
    private void dadoRapido(usuario jugador) {
        int avance = r.nextInt(6) + 5;
        jugador.avanzar(avance);
        System.out.println(jugador.getNombre() + " ha obtenido un dado rapido! Avanza " + avance + " casillas");
    }
    
    private void dadoLento(usuario jugador) {
        int avanceLento = r.nextInt(3) + 1;
        jugador.avanzar(avanceLento);
        System.out.println(jugador.getNombre() + " ha conseguido un dado lento! Avanza " + avanceLento + " casillas");
    }

	@Override
	public void realizarAccion(usuario jugador) {
		// TODO Auto-generated method stub
		
	}
}
