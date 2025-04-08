package Metodos_Juego_de_Pinguino;

import java.util.ArrayList;

public class clase_pinguino extends usuario{

	private ArrayList<String> inventario;
	
	
	
	public clase_pinguino(String nombre, String color, int posicion) {
		super(nombre, color, posicion);
	
	}

    public ArrayList<String> getInventario() {
        return inventario;
    }

    public void setInventario(ArrayList<String> inventario) {
        this.inventario = inventario;
    }


    public void gestionarBatalla() {
        System.out.println(getNombre() + " está gestionando la batalla...");

    }

    
    public void usarObjeto() {
        if (!inventario.isEmpty()) {
            String objeto = inventario.remove(inventario.size() - 1);
            System.out.println(getNombre() + " usó el objeto " + objeto + ".");
        } else {
            System.out.println(getNombre() + " no tiene objetos para usar.");
        }
    }

    // Método para añadir un objeto al inventario
    public void añadirItem(String objeto) {
        inventario.add(objeto);
        System.out.println(objeto + " ha sido añadido al inventario de " + getNombre() + ".");
    }

    // Método para quitar un objeto del inventario
    public void quitarItem(String objeto) {
        if (inventario.contains(objeto)) {
            inventario.remove(objeto);
            System.out.println(objeto + " ha sido quitado del inventario de " + getNombre() + ".");
        } else {
            System.out.println(objeto + " no está en el inventario de " + getNombre() + ".");
        }
    }
}
   
