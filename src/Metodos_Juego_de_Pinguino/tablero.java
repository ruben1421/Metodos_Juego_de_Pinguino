package Metodos_Juego_de_Pinguino;

public class tablero {
    private int numeroDeCasillas;
    
    public tablero(int numeroDeCasillas) {
        this.numeroDeCasillas = numeroDeCasillas;
    }
    
    public void generarTablero() {
        System.out.println("Generando tablero con " + numeroDeCasillas + " casillas...");
    }
    
    public static void main(String[] args) {
    	tablero tablero = new tablero(50);
        tablero.generarTablero();
    }
}
