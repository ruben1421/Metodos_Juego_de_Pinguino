package Metodos_Juego_de_Pinguino;
import java.util.Random;

public class Foca {
    private int posicion;
    private int turnosBloqueada;
    private Random random;
    
    public Foca(int posicion) {
        this.posicion = posicion;
        this.turnosBloqueada = 0;
        this.random = new Random();
    }
    
    public void tirarDado() {
        if (turnosBloqueada > 0) {
            turnosBloqueada--;
            System.out.println("La foca no puede moverse porque esta bloqueada");
            return;
        }
        
        int mov = random.nextInt(6) + 1; 
        posicion += mov;
        System.out.println("La foca ha avanzado " + mov + " casillas. Su nueva posicion es: " + posicion);
    }
}

