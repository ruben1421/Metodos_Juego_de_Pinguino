package Metodos_Juego_de_Pinguino;

import java.util.ArrayList;

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
    }
}
