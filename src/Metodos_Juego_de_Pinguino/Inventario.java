package Metodos_Juego_de_Pinguino;

public class Inventario {
	
	// private static final: un valor constante que es solo accesible a los objetos de clase inventario
	private static final int maxPeces = 2;
	private static final int maxDados = 3;
	private static final int maxBolasNieve = 6;

	public int peces;
	public int dados;
	public int bolasNieve;
	
	public Inventario() {
		this.peces = 0;
		this.bolasNieve = 0;
		this.dados = 0;
	}

	public int getPeces() {
		return peces;
	}
	public int getBolasNieve() {
		return bolasNieve;
	}
	public int getDados() {
		return dados;
	}
	
	public void setPeces(int peces) {
		this.peces = peces;
	}
	public void setBolasNieve(int bolasNieve) {
		this.bolasNieve = bolasNieve;
	}
	public void setDados(int dados) {
		this.dados = dados;
	}
	
	public void masQueMax() {
		if(peces >= maxPeces) {
			peces = maxPeces;
		}
		if(bolasNieve >= maxBolasNieve) {
			bolasNieve = maxBolasNieve;
		}
		if(dados >= maxDados) {
			dados = maxDados;
		}
	}
}
