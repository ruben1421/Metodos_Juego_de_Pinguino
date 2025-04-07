package Metodos_Juego_de_Pinguino;

import java.util.ArrayList;
<<<<<<< HEAD

public class tablero {
    private ArrayList<casilla> casillas;
    private ArrayList<jugador> jugadores;
    private int turnos;
    private jugador jugadorActual;

    public tablero(int numeroDeCasillas) {
        this.casillas = new ArrayList<>();
        this.jugadores = new ArrayList<>();
        this.turnos = 0;
        this.jugadorActual = null;

        for (int i = 0; i < numeroDeCasillas; i++) {
            casillas.add(generarCasillaAleatoria(i));
        }
    }

    private casilla generarCasillaAleatoria(int posicion) {
        double aleatorio = Math.random();

        if (aleatorio < 0.2) {
            return new casillaAgujero(posicion);
        } else if (aleatorio < 0.4) {
            return new casillaInterrogante(posicion);
        } else if (aleatorio < 0.6) {
            return new casillaNormal(posicion);
        } else if (aleatorio < 0.8) {
            return new casillaOso(posicion);
        } else {
            return new casillaTrineo(posicion);
        }
    }

    public void generarTablero() {
        System.out.println("Tablero generado con " + casillas.size() + " casillas.");
        for (int i = 0; i < casillas.size(); i++) {
            System.out.println("Casilla " + i + ": " + casillas.get(i).getClass().getSimpleName());
        }
    }

    public void actualizarTablero() {
        System.out.println("Actualizando estado del tablero...");
    }

    public void actualizarJugador(jugador j) {
        this.jugadorActual = j;
        System.out.println("Jugador actual actualizado: " + j.getNombre());
=======
import java.util.Random;

public class tablero {
    private int numeroDeCasillas;
    private ArrayList<Casilla> casillas;
    private Random random;
    
    public tablero(int numeroDeCasillas) {
        this.numeroDeCasillas = numeroDeCasillas;
        this.casillas = new ArrayList<>(numeroDeCasillas);
        this.random = new Random();
    }
    public void generarTablero() {
        System.out.println("Generando tablero con " + numeroDeCasillas + " casillas...");
        casillas.clear(); // Limpiar en caso de regenerar

        for (int i = 0; i < numeroDeCasillas; i++) {
            String tipo;

            if (i == 0) {
                tipo = "Normal"; // Primera casilla siempre normal
            } else {
                int tipoAleatorio = random.nextInt(10);
                switch (tipoAleatorio) {
                    case 0: tipo = "Oso"; break;
                    case 1: tipo = "Agujero"; break;
                    case 2: tipo = "Trineo"; break;
                    case 3: tipo = "Interrogacion"; break;
                    default: tipo = "Normal"; break;
                }
            }
            
            casillas.add(new Casilla(tipo, tipo)); // Solo se asigna tipo
        }

        System.out.println("¡Tablero generado con éxito!");
    }
    
<<<<<<< HEAD
    public static void main(String[] args) {
    	tablero tablero = new tablero(50);
        tablero.generarTablero();
=======
    public Casilla getCasilla(int posicion) {
        if (posicion >= 0 && posicion < casillas.size()) {
            return casillas.get(posicion);
        }
        return null;
    }

    public int getNumeroDeCasillas() {
        return numeroDeCasillas;
    }
    


public void imprimirTablero() {
    System.out.println("Estado del tablero:");
    for (int i = 0; i < casillas.size(); i++) {
        System.out.println("Casilla " + i + ": " + casillas.get(i).getTipo());
>>>>>>> 0ab0ccf30e49bb1739415521011e4d1e0a708aab
>>>>>>> main
    }
}
}
