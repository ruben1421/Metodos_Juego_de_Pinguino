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
    
<<<<<<< HEAD
    public void golpearCola(usuario rival) {
=======
    public void golpearCola(usuario rival,usuario posicion) {
>>>>>>> bfb66b28a0984c4a6275e19d7fcfa4c000311449
        System.out.println("La Foca intenta pegar a " + rival.getNombre());
        int golpe = random.nextInt(2);
        
        if (golpe == 1) {
            rival.setposicion(2); 
            System.out.println(rival.getNombre() + " ha sido golpeado y retrocede 2 casillas.");
        } else {
            System.out.println("El golpe ha fallado y no ha afectado");
        }
    }
}
