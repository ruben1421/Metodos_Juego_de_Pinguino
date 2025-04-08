
package Metodos_Juego_de_Pinguino;

import java.util.ArrayList;
import java.util.Random;

public class tablero {
    private ArrayList<Casilla> casillas;
    private Random random;

    public tablero(int numeroDeCasillas) {
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
            case 0: return new CasillaAgujero(posicion, null);
            case 1: return new CasillaOso(posicion);
            case 2: return new CasillaTrineo(posicion, null);
            case 0: return new CasillaAgujero(posicion);
            case 1: return new CasillaOso(posicion);
            case 2: return new CasillaTrineo(posicion);
            case 3: return new CasillaInterrogante(posicion);
            default: return new CasillaNormal(posicion);
        }
    }

    public Casilla obtenerCasilla(int posicion) {

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

    public void mainTablero(String[] args) {
    	tablero tablero = new tablero(50);
        tablero.generarTablero();
    }

    public Casilla getCasilla(int posicion) {
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
    public int getNumeroDeCasillas() {
        return numeroDeCasillas;
    }
    


public void imprimirTablero() {
    System.out.println("Estado del tablero:");
    for (int i = 0; i < casillas.size(); i++) {
        System.out.println("Casilla " + i + ": " + casillas.get(i).getTipo());

    }
}
    }
}
    }


