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
        casillas.clear();

        for (int i = 0; i < numeroDeCasillas; i++) {
            Casilla casilla;

            if (i == 0) {
                casilla = new CasillaNormal(i); // La primera casilla es siempre normal
            } else {
                int tipoAleatorio = random.nextInt(4); // Cambié el rango a 4 para evitar duplicados
                switch (tipoAleatorio) {
                    case 0: casilla = new CasillaAgujero(i, null); break;
                    case 1: casilla = new CasillaOso(i); break;
                    case 2: casilla = new CasillaTrineo(i, null); break;
                    case 3: casilla = new CasillaInterrogante(i); break;
                    default: casilla = new CasillaNormal(i); break;
                }
            }

            casillas.add(casilla);
        }
    }

    public static void mainTablero(String[] args) {
    	tablero tablero = new tablero(50);
        tablero.generarTablero();
    }

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
        for (int i = 0; i < casillas.size(); i++) {
            System.out.println("Casilla " + i + ": " + casillas.get(i).getClass().getSimpleName());
        }
    }

 

	public int encontrarAgujeroAnterior(int posicion) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int encontrarSiguienteTrineo(int posicion) {
		// TODO Auto-generated method stub
		return 0;
	}
}
