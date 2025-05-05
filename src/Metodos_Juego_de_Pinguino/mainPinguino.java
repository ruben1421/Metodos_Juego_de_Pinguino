package Metodos_Juego_de_Pinguino;
import java.util.Scanner;

public class mainPinguino {
	    public static void main(String[] args) {
	        mainPinguino(args);
	    }

	    public static void mainPinguino(String[] args) {
	        Scanner s = new Scanner(System.in);
	        boolean juegoEntero = true;
	        
	        while(juegoEntero) {
	            System.out.println("BIENVENIDOS AL JUEGO DEL PINGÜINO");
	            System.out.println("Pulse cualquier tecla para continuar");
	            s.nextLine();
	            
	            int opcion;
	            do {
	                System.out.println("Opciones de Juego");
	                System.out.println("1 = Nueva Partida ");
	                System.out.println("2 = Cargar Partida");
	                System.out.println("3 = Salir del Juego");
	                System.out.print("Seleccione una opción: ");
	                
	                try {
	                    opcion = Integer.parseInt(s.nextLine());
	                } catch (NumberFormatException e) {
	                    System.out.println("Por favor, ingrese un número válido.");
	                    opcion = 0;
	                    continue;
	                }
	                
	                switch(opcion) {
	                    case 1:
	                        System.out.println("Has escogido crear una nueva partida");
	                        crearNuevaPartida(s);
	                        break;
	                    case 2:
	                        System.out.println("Esta opción no está disponible sin conexión a base de datos");
	                        break;
	                    case 3:
	                        juegoEntero = false;
	                        System.out.println("Saliendo del Juego...");
	                        break;
	                    default:
	                        System.out.println("No has escogido una opción correcta");
	                        break;
	                }
	            } while(opcion != 1 && opcion != 2 && opcion != 3);
	        }
	    }
	    
	    private static void crearNuevaPartida(Scanner s) {
	        System.out.print("Ingrese el nombre de usuario: ");
	        String nombre = s.nextLine();
	        
	        System.out.print("Ingrese el color de usuario: ");
	        String color = s.nextLine();
	        
	        // Crear el usuario y su inventario
	        usuario jugador = new usuario(nombre, color, 0);
	        Inventario inventario = new Inventario();
	        
	        // Crear el tablero del juego
	        tablero tableroJuego = new tablero(50);
	        tableroJuego.generarTablero();
	        
	        // Iniciar el juego
	        jugarPartida(jugador, inventario, tableroJuego);
	    }
	    
	    private static void jugarPartida(usuario jugador, Inventario inventario, tablero tableroJuego) {
	        Scanner s = new Scanner(System.in);
	        boolean partidaActiva = true;
	        int turno = 1;
	        
	        System.out.println("¡Comienza la partida para " + jugador.getNombre() + "!");
	        
	        while (partidaActiva && jugador.getPosicion() < tableroJuego.getNumeroDeCasillas() - 1) {
	            System.out.println("Turno: " + turno);
	            System.out.println(jugador.getNombre() + " está en la posición " + jugador.getPosicion());
	            System.out.println("Inventario: Peces: " + inventario.getCantidadPeces() + 
	                             ", Dados: " + inventario.getCantidadDados() + 
	                             ", Bolas de nieve: " + inventario.getCantidadBolasNieve());
	            
	            System.out.println("Opciones:");
	            System.out.println("1. Tirar dado");
	            System.out.println("2. Usar objeto");
	            System.out.println("3. Salir");
	            System.out.print("Seleccione una opción: ");
	            
	            int opcion;
	            try {
	                opcion = Integer.parseInt(s.nextLine());
	            } catch (NumberFormatException e) {
	                System.out.println("Por favor, ingrese un número válido.");
	                continue;
	            }
	            
	            switch (opcion) {
	                case 1:
	                    // Tirar dado y mover
	                    jugador.tirarDado(6);
	                    
	                    // Comprobar que no se pase del tablero
	                    if (jugador.getPosicion() >= tableroJuego.getNumeroDeCasillas() - 1) {
	                        jugador.setPosicion(tableroJuego.getNumeroDeCasillas() - 1);
	                        System.out.println("¡Has llegado al final del tablero!");
	                    }
	                    
	                    // Ejecutar acción de la casilla donde cae
	                    Casilla casillaActual = tableroJuego.getCasilla(jugador.getPosicion());
	                    if (casillaActual != null) {
	                        casillaActual.añadirJugador(jugador);
	                    } else {
	                        System.out.println("¡Casilla no encontrada! Posición: " + jugador.getPosicion());
	                    }
	                    
	                    if (jugador.getPosicion() >= tableroJuego.getNumeroDeCasillas() - 1) {
	                        System.out.println("¡FELICIDADES! ¡" + jugador.getNombre() + " ha ganado la partida!");
	                        partidaActiva = false;
	                    }
	                    
	                    turno++;
	                    break;
	                    
	                case 2:
	                    System.out.println("Selecciona objeto a usar:");
	                    System.out.println("1. Pez (" + inventario.getCantidadPeces() + ")");
	                    System.out.println("2. Dado (" + inventario.getCantidadDados() + ")");
	                    System.out.println("3. Bola de nieve (" + inventario.getCantidadBolasNieve() + ")");
	                    System.out.println("4. Cancelar");
	                    
	                    int objetoSeleccionado;
	                    try {
	                        objetoSeleccionado = Integer.parseInt(s.nextLine());
	                    } catch (NumberFormatException e) {
	                        System.out.println("Opción no válida.");
	                        continue;
	                    }
	                    
	                    manejarUsoDeObjeto(objetoSeleccionado, jugador, inventario);
	                    break;
	                    
	                case 3:
	                    System.out.println("Saliendo de la partida...");
	                    partidaActiva = false;
	                    break;
	                    
	                default:
	                    System.out.println("Opción no válida.");
	                    break;
	            }
	        }
	    }
	    
	    private static void manejarUsoDeObjeto(int objetoSeleccionado, usuario jugador, Inventario inventario) {
	        switch (objetoSeleccionado) {
	            case 1: // Usar pez
	                if (inventario.getCantidadPeces() > 0) {
	                    System.out.println("Has usado un pez.");
	                    inventario.setCantidadPeces(inventario.getCantidadPeces() - 1);
	                } else {
	                    System.out.println("No tienes peces disponibles.");
	                }
	                break;
	            case 2: // Usar dado
	                if (inventario.getCantidadDados() > 0) {
	                    System.out.println("Has usado un dado especial.");
	                    inventario.setCantidadDados(inventario.getCantidadDados() - 1);
	                    jugador.avanzar(10); // Dado especial avanza más
	                } else {
	                    System.out.println("No tienes dados disponibles.");
	                }
	                break;
	            case 3: // Usar bola de nieve
	                if (inventario.getCantidadBolasNieve() > 0) {
	                    System.out.println("Has lanzado una bola de nieve.");
	                    inventario.setCantidadBolasNieve(inventario.getCantidadBolasNieve() - 1);
	                } else {
	                    System.out.println("No tienes bolas de nieve disponibles.");
	                }
	                break;
	            case 4:
	                System.out.println("Uso de objeto cancelado.");
	                break;
	            default:
	                System.out.println("Opción no válida.");
	                break;
	        }
	    
	    
	    }
	
}