package Metodos_Juego_de_Pinguino;

import java.util.Random;
import java.util.Scanner;

public class usuario {
    private int posicion;
    private String nombre;
    private String color;

    // Constructor
    public usuario(String nombre, String color, int posicion) {
        this.nombre = nombre;
        this.color = color;
        this.posicion = posicion;
    }

    // Getters
    public int getPosicion() {
        return posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getColor() {
        return color;
    }

    // Setters
    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setColor(String color) {
        this.color = color;
    }

    // Método para tirar dado
    public void tirarDado(int maximoDado) {
        int resultado = (int) (Math.random() * maximoDado) + 1;
        System.out.println(nombre + " tiró el dado y sacó: " + resultado);
        this.posicion += resultado;
    }

<<<<<<< HEAD
    // Método para actualizar posición manualmente
    public void posicion(int nuevaPosicion) {
        this.posicion = nuevaPosicion;
        System.out.println(nombre + " ahora está en la posición: " + this.posicion);
    }
}
=======
		        
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

		
>>>>>>> 0ab0ccf30e49bb1739415521011e4d1e0a708aab
		
		
	

