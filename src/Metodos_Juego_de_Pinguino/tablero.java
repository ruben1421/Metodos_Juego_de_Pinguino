package Metodos_Juego_de_Pinguino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class tablero {
    private int numeroDeCasillas;
    public ArrayList<Casilla> casillas;
    private Random random;
    
    public ArrayList<Integer> osoPositions = new ArrayList<>();
    public ArrayList<Integer> agujeroPositions = new ArrayList<>();
    public ArrayList<Integer> trineoPositions = new ArrayList<>();
    public ArrayList<Integer> interrogantePositions = new ArrayList<>();
    
    // Configuration for special tiles distribution
    private static final int MIN_SPECIAL_TILES = 15;
    private static final int MAX_SPECIAL_TILES = 25;
    private static final double PROBABILITY_OSO = 0.25;
    private static final double PROBABILITY_AGUJERO = 0.1;
    private static final double PROBABILITY_TRINEO = 0.25;
    private static final double PROBABILITY_INTERROGANTE = 0.25;

    public tablero(int numeroDeCasillas) {
        this.numeroDeCasillas = numeroDeCasillas;
        this.casillas = new ArrayList<>(numeroDeCasillas);
        this.random = new Random();
        generarTablero();
    }

    public void generarTablero() {
        casillas.clear();
        
        // Always start with normal tile
        casillas.add(new CasillaNormal(0));
        
        // Generate special tiles with controlled distribution
        for (int i = 1; i < numeroDeCasillas - 1; i++) {
            double roll = random.nextDouble();
            Casilla casilla;
            
            if (roll < PROBABILITY_OSO) {
                casilla = new CasillaOso(i);
                osoPositions.add(i);
            } 
            else if (roll < PROBABILITY_OSO + PROBABILITY_AGUJERO) {
                casilla = new CasillaAgujero(i, this);
                agujeroPositions.add(i);
            } 
            else if (roll < PROBABILITY_OSO + PROBABILITY_AGUJERO + PROBABILITY_TRINEO) {
                casilla = new CasillaTrineo(i, this);
                trineoPositions.add(i);
            } 
            else if (roll < PROBABILITY_OSO + PROBABILITY_AGUJERO + PROBABILITY_TRINEO + PROBABILITY_INTERROGANTE) {
                casilla = new CasillaInterrogante(i);
                interrogantePositions.add(i);
            } 
            else {
                casilla = new CasillaNormal(i);
            }
            
            casillas.add(casilla);
        }
        
        // Always finish with normal tile
        casillas.add(new CasillaNormal(numeroDeCasillas - 1));
        
        // Ensure minimum number of special tiles
        asegurarMinimoEspeciales();
    }

    private void asegurarMinimoEspeciales() {
        int specialCount = contarCasillasEspeciales();
        
        while (specialCount < MIN_SPECIAL_TILES) {
            // Find a normal tile to replace (excluding first and last)
            int pos = 1 + random.nextInt(numeroDeCasillas - 2);
            if (casillas.get(pos) instanceof CasillaNormal) {
                double roll = random.nextDouble();
                if (roll < 0.5) {
                    casillas.set(pos, new CasillaInterrogante(pos));
                } else {
                    casillas.set(pos, new CasillaAgujero(pos, this));
                }
                specialCount++;
            }
        }
    }

    private int contarCasillasEspeciales() {
        int count = 0;
        for (Casilla c : casillas) {
            if (!(c instanceof CasillaNormal)) {
                count++;
            }
        }
        return count;
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
        for (int i = posicion - 1; i >= 0; i--) {
            if (casillas.get(i) instanceof CasillaAgujero) {
                return i;
            }
        }
        return 0; // Return start if no hole found
    }

    public int encontrarSiguienteTrineo(int posicion) {
        for (int i = posicion + 1; i < numeroDeCasillas; i++) {
            if (casillas.get(i) instanceof CasillaTrineo) {
                return i;
            }
        }
        return numeroDeCasillas - 1; // Return finish if no sled found
    }
    
    public ArrayList<Integer> getOsoPositions() { return osoPositions; }
    public ArrayList<Integer> getAgujeroPositions() { return agujeroPositions; }
    public ArrayList<Integer> getTrineoPositions() { return trineoPositions; }
    public ArrayList<Integer> getInterrogantePositions() { return interrogantePositions; }
}