package Metodos_Juego_de_Pinguino;

import java.util.Random;

public class usuario {
    private int posicion;
    private String nombre;
    private String color;

 
    public usuario(String nombre, String color, int posicion) {
        this.nombre = nombre;
        this.color = color;
        this.posicion = posicion;
    }


    public int getPosicion() {
        return posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getColor() {
        return color;
    }


    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setColor(String color) {
        this.color = color;
    }

  
    public void tirarDado(int maximoDado) {
        int resultado = (int) (Math.random() * maximoDado) + 1;
        System.out.println(nombre + " tiró el dado y el numero que sacó es: " + resultado);
       this.posicion =   this.posicion+ resultado ;
    }
    
    public void avanzar(int maximoDado) {
        int resultado = (int) (Math.random() * maximoDado) + 1;
        this.posicion += resultado;  
        System.out.println(nombre + " avanzó " + resultado + " casillas y ahora está en la posición " + this.posicion);
    }

}
		
		
		
	

