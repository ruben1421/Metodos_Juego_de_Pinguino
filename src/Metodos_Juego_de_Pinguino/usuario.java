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

    // Método para actualizar posición manualmente
    public void posicion(int nuevaPosicion) {
        this.posicion = nuevaPosicion;
        System.out.println(nombre + " ahora está en la posición: " + this.posicion);
    }
}
		
		
	

