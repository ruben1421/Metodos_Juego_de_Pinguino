<<<<<<< Updated upstream
=======
package Metodos_Juego_de_Pinguino;

import java.util.ArrayList;
import java.util.Random;

public class Tablero {
    private ArrayList<Casilla> casillas;
    private Random random;

    public Tablero(int numeroDeCasillas) {
        this.casillas = new ArrayList<>();
        this.random = new Random();
        generarCasillasAleatorias(numeroDeCasillas);
    }

    private void generarCasillasAleatorias(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            casillas.add(crearCasillaAleatoria(i));
        }
    }

    private Casilla crearCasillaAleatoria(int posicion) {
        int tipo = random.nextInt(5); // Genera número entre 0 y 4
        
        switch(tipo) {
<<<<<<< Updated upstream
            case 0: return new CasillaAgujero(posicion, null);
            case 1: return new CasillaOso(posicion);
            case 2: return new CasillaTrineo(posicion, null);
=======
            case 0: return new CasillaAgujero(posicion);
            case 1: return new CasillaOso(posicion);
            case 2: return new CasillaTrineo(posicion);
>>>>>>> Stashed changes
            case 3: return new CasillaInterrogante(posicion);
            default: return new CasillaNormal(posicion);
        }
    }

    public Casilla obtenerCasilla(int posicion) {
        if (posicion >= 0 && posicion < casillas.size()) {
            return casillas.get(posicion);
        }
        return null;
    }

    public void mostrarTablero() {
        System.out.println("Tablero con " + casillas.size() + " casillas generadas:");
        for (int i = 0; i < casillas.size(); i++) {
            System.out.println("Casilla " + i + ": " + casillas.get(i).getClass().getSimpleName());
        }
    }
<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes
