package Metodos_Juego_de_Pinguino;

import java.util.Random;
import java.util.Scanner;

public class usuario {
			
	
		
		String nombre;
		String color;
		int posicion;
		int dados;
		int peces; 
		int bolasnieve;
		int id;
		
		
		public usuario(String nombre, String color) {
	        this.nombre = nombre;
	        this.color = color;
	        this.posicion = 0;
	        this.dados = 0;
	        this.peces = 0;
	        this.bolasnieve = 0;
	        this.id = id;
	    }
		
		
		public int getId() {
			return id;
		}
		
		 public String getNombre() {
		        return nombre; 
		    }

		    public void setNombre(String nombre) {
		        this.nombre = nombre; 
		    }

		    public String getColor() {
		        return color;	
		    }
		    
		    public void setColor(String color) {
		        this.color = color; 
		    }
		    
		    public int getPosicion() {
		        return posicion;	
		    }
		    
		    public void setposicion(int posicion) {
		        this.posicion = posicion; 
		    }

		    public int getDados() {
		        return dados;	
		    }
		    
		    public void setDados(int dados) {
		        this.dados = dados; 
		    }

		    
		    public int getPeces() {
		        return peces;	
		    }
		    
		    public void setPeces(int peces) {
		        this.peces = peces; 
		    }

		    public int getBolasNieve() {
		        return bolasnieve;	
		    }
		    
		    public void setBolasNieve(int bolasnieve) {
		        this.bolasnieve = bolasnieve; 
		    }



		    public void tirarDado() {
		        Scanner s = new Scanner(System.in);

		        
		        System.out.println("¿Qué tipo de dado quieres lanzar?");
		        System.out.println("1. Dado normal (6 caras)");
		        System.out.println("2. Dado especial (8 caras)");
		        System.out.print("Ingresa 1 o 2: ");
		        int opcion = s.nextInt();

		        Random rand = new Random();
		        int resultado;

		        if (opcion == 2) {
		            resultado = rand.nextInt(8) + 1;  
		            System.out.println(nombre + " ha lanzado un dado especial y ha avanzado " + resultado + " casillas.");
		        } else {
		            resultado = rand.nextInt(6) + 1;  
		            System.out.println(nombre + " ha lanzado un dado normal y ha avanzado " + resultado + " casillas.");
		        }

		        this.posicion += resultado;
		        System.out.println("Posición actual de " + nombre + ": " + posicion);
		    }
		    public void lanzarBolaNieve(usuario objetivo) {
		        if (bolasnieve > 0) {
		            bolasnieve--;
		            objetivo.posicion -= 3;
		            System.out.println(nombre + " ha lanzado una bola de nieve a " + objetivo.nombre + "! Ahora " + objetivo.nombre + " retrocede 3 casillas. Posición de " + objetivo.nombre + ": " + objetivo.posicion);
		        } else {
		            System.out.println(nombre + " no tiene bolas de nieve disponibles.");
		        }
		    }
		 
	 
		 public void retrocederCasillas(int cantidad) {
			    this.posicion = Math.max(0, this.posicion - cantidad);  
		    }
			public void avanzarCasillas(int cantidad) {
			    this.posicion += cantidad;  
		    }
		}

		
		
		
	

