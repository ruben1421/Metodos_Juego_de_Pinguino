package Metodos_Juego_de_Pinguino;

import java.util.ArrayList;
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

    public static void main(String[] args) {
    	tablero tablero = new tablero(50);
        tablero.generarTablero();

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

    }
}
}
