package Metodos_Juego_de_Pinguino;

public class Inventario {
	
	// private static final: un valor constante que es solo accesible a los objetos de clase inventario
	private static final int maxPeces = 2;
	private static final int maxBolasNieve = 6;

	public int peces;
	public int bolasNieve;
	
	public Inventario() {
		this.peces = 0;
		this.bolasNieve = 0;
	}
	
	public int getPeces() {
		return peces;
	}
	public int getBolasNieve() {
		return bolasNieve;
	}
	
	public void setPeces(int peces) {
		this.peces = peces;
	}
	public void setBolasNieve(int bolasNieve) {
		this.bolasNieve = bolasNieve;
	}
}
