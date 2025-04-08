package Metodos_Juego_de_Pinguino;
import java.util.ArrayList;

public class Inventario {
	
	private ArrayList<Item> items; //Un arraylist del clase item
	
	//C0nstructor del clase inventario
	public Inventario() {
		items = new ArrayList<>(); 
		
		//Aqui añadimos al arraylist los items peces, dados, y bolas de nieve
		items.add(new Item(Item.peces, 0));
		items.add(new Item(Item.dados, 0));
		items.add(new Item(Item.bolasNieve, 0));
	}
	
	public Item encontrarItem(String nombre) { //Este metodo es para encontrar items y devolverlo
        for (Item item : items) { //Para cada item del arraylist items
            if (item.getNombre().equals(nombre)) { //Si el item es igual al nombre, devuelve el item
                return item;
            }
        }
        return null;
	}
    
    public Item getPeces() { //Devuelve la palabra "Peces"
        return encontrarItem(Item.peces);
    }
    
    public Item getDados() { //Devuelve la palabra "Dados"
        return encontrarItem(Item.dados);
    }
    
    public Item getBolasNieve() { //Devuelve la palabra "Bolas de nieve"
        return encontrarItem(Item.bolasNieve);
    }
    
    public int getCantidadPeces() { //Devuelve la cantidad de peces
        return getPeces().getCantidad();
    }
    public int getCantidadDados() { //Devuelve la cantidad de dados
        return getDados().getCantidad();
    }
    public int getCantidadBolasNieve() { //Devuelve la cantidad de bolas de nieve+
        return getBolasNieve().getCantidad();
    }
    
    //Cambiar la cantidad de los items individualmente
    public void setCantidadPeces(int cantidad) {
        getPeces().setCantidad(cantidad);
    }
    public void setCantidadDados(int cantidad) {
        getDados().setCantidad(cantidad);
    }
    public void setCantidadBolasNieve(int cantidad) {
        getBolasNieve().setCantidad(cantidad);
    }
    
    public void masQueMax() { //Método para garantizar que ningún item exceda su máximo
        for (Item item : items) {
            item.setCantidad(item.getCantidad());
        }
    }
}
