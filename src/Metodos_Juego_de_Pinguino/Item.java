package Metodos_Juego_de_Pinguino;

public class Item {
	
	private String nombre; //Nombre de item
	private int cantidad; //Cantidad de item
	private int maxCantidad; //Y la max cantidad del item
	
	// private static final: un valor constante que es solo accesible a los objetos de clase item
	public static final int maxCantidadPeces = 2; //La cantidad maxima de peces será 2 y así sucesivamente
	public static final int maxCantidadDados = 3;
	public static final int maxCantidadBolasNieve = 6;
	
    public static final String peces = "Peces";
    public static final String dados = "Dados";
    public static final String bolasNieve = "Bolas Nieve";
	
    //Constructor del clase item
	public Item(String nombre, int cantidad) {
		this.nombre = nombre; 
		this.cantidad = cantidad;
		
        switch(nombre) { //Este switch-case construye la cantidad maxima de cada item
        case peces:
            this.maxCantidad = maxCantidadPeces;
            break;
        case dados:
            this.maxCantidad = maxCantidadDados;
            break;
        case bolasNieve:
            this.maxCantidad = maxCantidadBolasNieve;
            break;
        }
	}
	
    public String getNombre() { //Devuelve el valor del nombre del item
        return nombre;
    }
    public int getCantidad() { //Devuelve el valor de la cantidad del item
        return cantidad;
    }
    
    public void setCantidad(int cantidad) { //Este metodo es para colocar el cantidad del item mientras manteniendo el maximo
        this.cantidad = Math.min(cantidad, maxCantidad); //El Math.min devuelve el numero menor entre los dos. por ejemplo: Math.min(7, 4) = 4
    }
    
    public void agregar(int cantidad) { //Este metodo es para agregar al cantidad del item mientras manteniendo el maximo
        this.cantidad = Math.min(this.cantidad + cantidad, maxCantidad);
    }
    
    public void quitar(int cantidad) { //Este metodo es para restar al cantidad del item mientras manteniendo el maximo
        this.cantidad = Math.max(this.cantidad - cantidad, 0);
    }
}