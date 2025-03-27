package Metodos_Juego_de_Pinguino;

import java.util.Random;

public class usuario {
			
		String nombre;
		String color;
		int posicion;
		int dados;
		int peces; 
		int bolasnieve;
		
		
		public usuario(String nombre, String color) {
	        this.nombre = nombre;
	        this.color = color;
	        this.posicion = 0;
	        this.dados = 0;
	        this.peces = 0;
	        this.bolasnieve = 0;
	    }
		 public int tirarDado(boolean especial) {
		        Random r = new Random();
		        int resultado;
		        if (especial) {
		            if (r.nextDouble() < 0.2) {
		                resultado = r.nextInt(6) + 5;
		            } else {
		                resultado = r.nextInt(3) + 1;
		            }
		        } else {
		            resultado = r.nextInt(6) + 1;
		        }
		        return resultado;
		    }
		}


		
		
		
	

