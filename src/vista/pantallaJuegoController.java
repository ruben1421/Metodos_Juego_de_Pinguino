package vista;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import Metodos_Juego_de_Pinguino.Casilla;
import Metodos_Juego_de_Pinguino.CasillaAgujero;
import Metodos_Juego_de_Pinguino.CasillaInterrogante;
import Metodos_Juego_de_Pinguino.CasillaNormal;
import Metodos_Juego_de_Pinguino.CasillaOso;
import Metodos_Juego_de_Pinguino.CasillaTrineo;
import Metodos_Juego_de_Pinguino.Inventario;
import Metodos_Juego_de_Pinguino.PartidaDAO;
import Metodos_Juego_de_Pinguino.bbdd;
import Metodos_Juego_de_Pinguino.tablero;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;

public class pantallaJuegoController {
	
	//@FXML vincula elementos definidos en un archivo FXML (como botones o textos) con variables o métodos en esta clase controladora.
	//Elementos del menú
    @FXML private MenuItem newGame;
    @FXML private MenuItem saveGame;
    @FXML private MenuItem loadGame;
    @FXML private MenuItem quitGame;

    //Botones del juego
    @FXML private Button dado;
    @FXML private Button rapido;
    @FXML private Button lento;
    @FXML private Button peces;
    @FXML private Button nieve;
    @FXML private Button opcionesJuego;

    //Textos del juego
    @FXML private Text dadoResultText;
    @FXML private Text rapido_t;
    @FXML private Text lento_t;
    @FXML private Text peces_t;
    @FXML private Text nieve_t;
    @FXML private Text eventos;

    //Elementos del tablero
    @FXML private HBox startPosition;
    @FXML private GridPane tablero;
    @FXML private Circle P1;
    @FXML private Circle P2;
    @FXML private Circle P3;
    @FXML private Circle P4;
    
    private Inventario inventario = new Inventario(); //Inventario del jugador
    private int p1Position = 0; 					  //Posición actual del jugador 1 en el tablero (inicia en 0)
    private final int COLUMNS = 5;					  //private final (variable): Nunca cambiará, es constante y solo visible en esta clase
    private final int ROWS = 10;					  //Configuración del tablero: 5 columnas y 10 filas (total 50 casillas)
    private int actionCount = 0;					  //Contador de acciones realizadas (para límite de mensajes en pantalla)
    private tablero juegoTablero = new tablero(50);
    int currentUserId = 1;							  //ID del usuario actual
    Connection dbConnection;						  //Conexión a la base de datos
    
    // Método para resaltar las casillas especiales
    private void highlightSpecialSquares() { 
    	// Limpiar resaltados anteriores
    	clearHighlights();
        
        // Resaltar posiciones de Oso (marrón)
        highlightPositions(juegoTablero.getOsoPositions(), Color.BROWN);
        
        // Resaltar posiciones de Agujero (negro)
        highlightPositions(juegoTablero.getAgujeroPositions(), Color.BLACK);
        
        // Resaltar posiciones de Trineo (azul claro)
        highlightPositions(juegoTablero.getTrineoPositions(), Color.LIGHTBLUE);
        
        // Resaltar posiciones de Interrogante (amarillo)
        highlightPositions(juegoTablero.getInterrogantePositions(), Color.GOLD);
    }
    
    private void clearHighlights() {
    	// Eliminar solo los rectángulos de resaltado
    	tablero.getChildren().removeIf(node -> node instanceof Rectangle);
    }
    
    // Método para resaltar casillas especiales del tablero con rectángulos de colores
    private void highlightPositions(ArrayList<Integer> positions, Color color) {
        for (int pos : positions) {
        	// Calcula fila y columna basado en la posición linealv
            int row = pos / COLUMNS; // Fila = posición / número de columnas
            int col = pos % COLUMNS; // Columna = resto de la división
            
            // Ajuste para el movimiento en zigzag (filas impares van de derecha a izquierda)
            if (row % 2 == 1) {
                col = (COLUMNS - 1) - col; // Invierte el orden de las columnas
            }
            
            // Crea un rectángulo semitransparente del color especificado
            Rectangle highlight = new Rectangle(55,55, color);
            highlight.setOpacity(0.3);  // Semi-transparent
            highlight.setStroke(Color.BLACK);
            highlight.setStrokeWidth(1);
            
            // Posiciona el rectángulo en el GridPane
            GridPane.setRowIndex(highlight, row);
            GridPane.setColumnIndex(highlight, col);
            
            // Añade al tablero y lo envía al fondo para no tapar otros elementos
            tablero.getChildren().add(highlight);
            highlight.toBack();
        }
    }

    //Este método es para inicializar el juego
    @FXML
    public void initialize() {
        eventos.setText("¡Bienvenido! Presiona 'Tirar los dados' para comenzar.");
        resetPlayerPositions();
        
        inventario.setCantidadPeces(1);
        inventario.setCantidadDados(1); 
        inventario.setCantidadBolasNieve(1);
        clearHighlights();
        highlightSpecialSquares();
        generarNumeroDeCasillasDelTablero();
        updatePowerUpCounts();
        updateButtonStates();
    }
    
    private void generarNumeroDeCasillasDelTablero() {
        // Elimina los números antiguos del tablero, dejando las fichas de los jugadores
        tablero.getChildren().removeIf(nodo -> nodo instanceof Text && !nodo.equals(P1) && !nodo.equals(P2) && !nodo.equals(P3) && !nodo.equals(P4));

        // Generar número para cada casilla
        for (int fila = 0; fila < ROWS; fila++) {
            for (int columna = 0; columna < COLUMNS; columna++) {
                int posicion = fila * COLUMNS + columna;

                // Ajuste para el patrón serpiente
                int columnaMostrada = (fila % 2 == 1) ? (COLUMNS - 1 - columna) : columna; //Si la fila es impar, columnaMostrada = (COLUMNS - 1 - columna), si no, pues columnaMostrada = columna

                // Crear texto con el número de la casilla
                Text numeroCasilla = new Text(String.valueOf(posicion));
                numeroCasilla.setStyle("-fx-font-size: 24; -fx-fill: #000000; -fx-font-style: bold");

                //Crear un contenedor para mover el texto a la derecha-abajo
                StackPane contenedorCasilla = new StackPane();
                StackPane.setAlignment(numeroCasilla, Pos.BOTTOM_RIGHT);
                contenedorCasilla.getChildren().add(numeroCasilla);
                
                // Colocar en la posición correspondiente del GridPane
                GridPane.setRowIndex(contenedorCasilla, fila);
                GridPane.setColumnIndex(contenedorCasilla, columnaMostrada);

                // Añadir al tablero
                tablero.getChildren().add(contenedorCasilla);
                
                //En la posición final, añadimos la palabra fin a la izquierda-abajo
                if(posicion == 49) {
                	Text finCasilla = new Text("Fin!");
                	finCasilla.setStyle("-fx-font-size: 56; -fx-fill: #000000; -fx-font-style: bold");
                	
                	StackPane contenedorFin = new StackPane();
                	StackPane.setAlignment(finCasilla, Pos.BOTTOM_LEFT);
                	contenedorFin.getChildren().add(finCasilla);
                	
                    GridPane.setRowIndex(contenedorFin, fila);
                    GridPane.setColumnIndex(contenedorFin, columnaMostrada);
                    tablero.getChildren().add(contenedorFin);
                }
            }
        }
    }

 	// Actualiza los textos que muestran la cantidad de power-ups disponibles
    private void updatePowerUpCounts() {
        rapido_t.setText("Dado rápido: " + inventario.getCantidadDados());
        lento_t.setText("Dado lento: " + inventario.getCantidadDados());
        peces_t.setText("Peces: " + inventario.getCantidadPeces());
        nieve_t.setText("Bolas de nieve: " + inventario.getCantidadBolasNieve());
    }

    // Activa/desactiva los botones según la disponibilidad de power-ups
    private void updateButtonStates() {
        rapido.setDisable(inventario.getCantidadDados() <= 0);
        lento.setDisable(inventario.getCantidadDados() <= 0);
        peces.setDisable(inventario.getCantidadPeces() <= 0);
        nieve.setDisable(inventario.getCantidadBolasNieve() <= 0);
    }

    // Muestra un mensaje de error en el área de eventos del juego
    private void showError(String message) {
        eventos.setText(eventos.getText() + "\n[ERROR] " + message);
    }
    
    // Reinicia las posiciones de todos los jugadores al punto de inicio
    private void resetPlayerPositions() {
    	tablero.getChildren().removeAll(P1, P2, P3, P4);
        
    	// Vuelve a colocar los jugadores en la posición inicial
        startPosition.getChildren().setAll(P1, P2, P3, P4);
        GridPane.setRowIndex(startPosition, 0);
        GridPane.setColumnIndex(startPosition, 0);
        
        p1Position = 0;
    }

    // Actualiza la posición visual de un jugador en el tablero
    private void updatePlayerPosition(Circle player, int position) {
    	int row = position / COLUMNS;
        int col = position % COLUMNS;
        
        // Ajuste para el movimiento en serpiente (filas impares)
        if (row % 2 == 1) {
            col = (COLUMNS - 1) - col;
        }
        
        GridPane.setRowIndex(player, row);
        GridPane.setColumnIndex(player, col);
        player.toFront(); // Asegura que el jugador sea visible
    }

    // Método para mover el jugador
    private void movePlayer(Circle player, int steps) {
    	if (player == P1) {
    		// Si el jugador está en la posición inicial, lo mueve al tablero
            if (startPosition.getChildren().contains(P1)) {
                startPosition.getChildren().remove(P1);
                tablero.getChildren().add(P1);
            }
            // Calcula nueva posición sin pasarse del límite del tablero
            int oldPosition = p1Position;
            p1Position = Math.min(p1Position + steps, (COLUMNS * ROWS) - 1);
            
            // Actualiza posición visual
            updatePlayerPosition(P1, p1Position);
            
            // Aplica efecto de la casilla de llegada
            handleTileEffect(oldPosition, p1Position);
        }
    }
    
    // Maneja el menú contextual de opciones del juego que aparece al hacer clic en el botón.
    @FXML
    private void handleOpcionesJuego(ActionEvent event) {
        ContextMenu contextMenu = new ContextMenu();

        // Crear items del menú
        MenuItem guardarTabla = new MenuItem("Guardar Tabla");
        MenuItem cargarTabla = new MenuItem("Cargar Tabla");
        MenuItem resetearTabla = new MenuItem("Resetear Tabla");
        MenuItem salirDelJuego = new MenuItem("Salir del Juego");

        // Asignar acciones a cada opción
        guardarTabla.setOnAction(e -> handleSaveGame());
        cargarTabla.setOnAction(e -> handleLoadGame());
        resetearTabla.setOnAction(e -> handleResetTable());
        salirDelJuego.setOnAction(e -> handleQuitGame());

        // Añadir opciones al menú contextual
        contextMenu.getItems().addAll(guardarTabla, cargarTabla, resetearTabla, salirDelJuego);

        // Mostrar el menú debajo del botón de opciones
        contextMenu.show(opcionesJuego, javafx.geometry.Side.BOTTOM, 0, 0);
    }
    
    // Método para manejar los efectos especiales al caer en una casilla
    private void handleTileEffect(int oldPos, int newPos) {
        Casilla tile = juegoTablero.getCasilla(newPos);
        
        // Evalúa el tipo de casilla y ejecuta el efecto apropiado
        if (tile instanceof CasillaOso) {
            handleOsoTile();
        } 
        else if (tile instanceof CasillaAgujero) {
            handleAgujeroTile(newPos);
        }
        else if (tile instanceof CasillaTrineo) {
            handleTrineoTile(newPos);
        }
        else if (tile instanceof CasillaInterrogante) {
            handleInterroganteTile();
        }
    }

    // Método para manejar el efecto de caer en una casilla de oso
    private void handleOsoTile() {
        addEvent("¡Encontraste un oso!");
        
        if (inventario.getCantidadPeces() > 0) {
            inventario.getPeces().quitar(1);
            addEvent("Usaste un pez para calmar al oso!");
        } else {
        	//Mostrar un mensaje que indica que caíste en agujero
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("¡Ay no! ¡¡No tienes peces y ahora el oso quiere comerte!!");
            alert.setContentText("Retrocedes al inicio");
            alert.showAndWait();
        	
            p1Position = 0;
            updatePlayerPosition(P1, 0);
            addEvent("¡Sin peces! Vuelves al inicio");
        }
        updatePowerUpCounts(); // Actualiza contadores
    }

    // Método para manejar el efecto de caer en una casilla de agujero
    private void handleAgujeroTile(int pos) {
        addEvent("¡Caíste en un agujero!");
        
        // Busca posición del agujero anterior
        int newPos = juegoTablero.encontrarAgujeroAnterior(pos);
        
        //Mostrar un mensaje que indica que caíste en agujero
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("¡Ay no! ¡¡Caíste en un agujero!!");
        alert.setContentText("Retrocedes al agujero anterior [casilla " + newPos + "]");
        alert.showAndWait();
        
        p1Position = newPos;
        updatePlayerPosition(P1, newPos);
        addEvent("Retrocedes a la casilla " + newPos);
    }

    // Método para manejar el efecto de caer en una casilla de trineo
    private void handleTrineoTile(int pos) {
        addEvent("¡Encontraste un trineo!");
        
        // Busca posición del siguiente trineo
        int newPos = juegoTablero.encontrarSiguienteTrineo(pos);
        
        //Mostrar un mensaje que indica que caíste en trineo
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("¡Vaya! ¡¡Parece que caíste en un trineo!!");
        alert.setContentText("Avanzas al trineo siguiente [casilla " + newPos + "]");
        alert.showAndWait();
        
        p1Position = newPos;
        updatePlayerPosition(P1, newPos);
        addEvent("¡Zummm! Avanzas a la casilla " + newPos);
    }

    // Método para manejar el efecto de caer en una casilla interrogante
    private void handleInterroganteTile() {
        addEvent("¡Casilla sorpresa!");
        
        Random r = new Random();
        int evento = r.nextInt(4); // Genera número aleatorio 0-3
        switch (evento) {
            case 0: // Recompensa: Pez
                inventario.getPeces().agregar(1);
                addEvent("¡Ganaste un pez!");
                break;
            case 1: // Recompensa: Bolas de nieve (1-3)
                int bolasNieve = r.nextInt(3) + 1;
                inventario.getBolasNieve().agregar(bolasNieve);
                addEvent("¡Ganaste " + bolasNieve + " bolas de nieve!");
                break;
            case 2: // Recompensa: Avance rápido (5-10 casillas)
                int avanceRapido = r.nextInt(6) + 5;
                p1Position = Math.min(p1Position + avanceRapido, (COLUMNS * ROWS) - 1);
                updatePlayerPosition(P1, p1Position);
                addEvent("¡Dado rápido! Avanzas " + avanceRapido + " casillas");
                break;
            case 3: // Recompensa: Avance lento (1-3 casillas)
                int avanceLento = r.nextInt(3) + 1;
                p1Position = Math.min(p1Position + avanceLento, (COLUMNS * ROWS) - 1);
                updatePlayerPosition(P1, p1Position);
                addEvent("¡Dado lento! Avanzas " + avanceLento + " casillas");
                break;
        }
        updatePowerUpCounts(); // Actualiza los contadores de power-ups
    }

    // Añade un mensaje al registro de eventos del juego	
    private void addEvent(String message) {
        actionCount++;
        if (actionCount > 3) {
            eventos.setText(message); // Reinicia el texto con el nuevo mensaje
            actionCount = 1;		  // Resetea el contador
        } else {
            eventos.setText(eventos.getText() + "\n" + message); // Añade línea nueva
        }
    }

    // Maneja la acción de tirar el dado
    @FXML
    private void handleDado(ActionEvent event) {
        Random rand = new Random();
        int diceResult = rand.nextInt(6) + 1;
        movePlayer(P1, diceResult); // Mueve al jugador
        
        addEvent("Tiraste un " + diceResult + " y avanzaste a la casilla " + p1Position);
        
        if (p1Position >= juegoTablero.getNumeroDeCasillas() - 1) {
            addEvent("🎉 ¡Felicidades! Has llegado al final del tablero.");
            disableGameButtons(); // Desactiva los botones al ganar
        }
    }
    
    // Desactiva todos los botones de acciones del juego
    private void disableGameButtons() {
        dado.setDisable(true);
        rapido.setDisable(true);
        lento.setDisable(true);
        peces.setDisable(true);
        nieve.setDisable(true);
    }

    // Maneja el uso del dado rápido
    @FXML
    private void handleRapido() {
        if (inventario.getCantidadDados() <= 0) {
            showError("No tienes dados disponibles");
            return;
        }
        inventario.getDados().quitar(1); // Consume 1 dado
        movePlayer(P1, 3); // Avanza 3 casillas
        addEvent("Usaste un dado rápido (+3 movimientos)");
        updatePowerUpCounts(); // Actualiza contadores
        updateButtonStates(); // Actualiza estado de botones
    }

    // Maneja el uso del dado lento
    @FXML
    private void handleLento() {
        if (inventario.getCantidadDados() <= 0) {
            showError("No tienes dados disponibles");
            return;
        }
        inventario.getDados().quitar(1); // Consume 1 dado
        movePlayer(P1, 1); // Avanza 1 casilla
        addEvent("Usaste un dado lento (+1 movimiento)");
        updatePowerUpCounts();
        updateButtonStates();
    }
    
    // Maneja el uso de peces
    @FXML
    private void handlePeces() {
        if (inventario.getCantidadPeces() <= 0) {
            showError("No tienes peces disponibles");
            return;
        }
        inventario.getPeces().quitar(1); // Consume 1 pez
        movePlayer(P1, 2); // Avanza 2 casillas
        addEvent("Usaste un pez (+2 movimientos)");
        updatePowerUpCounts();
        updateButtonStates();
    }

    // Maneja el uso de bolas de nieve
    @FXML
    private void handleNieve() { 
        if (inventario.getCantidadBolasNieve() <= 0) {
            showError("No tienes bolas de nieve disponibles");
            return;
        }
        inventario.getBolasNieve().quitar(1); // Consume 1 bola
        movePlayer(P1, 4); // Avanza 4 casillas
        addEvent("Usaste una bola de nieve (+4 movimientos)");
        updatePowerUpCounts();
        updateButtonStates();
    }
    
    // Añade peces al inventario del jugador y actualiza la interfaz
    public void addPeces(int cantidad) {
        inventario.getPeces().agregar(cantidad); // Añade al inventario
        updatePowerUpCounts(); // Actualiza los contadores
        updateButtonStates(); // Habilita botones si es necesario
        addEvent("¡Obtuviste " + cantidad + " pez(es)!"); // Notificación al jugador
    }

    // Añade dados al inventario del jugador y actualiza la interfaz  
    public void addDados(int cantidad) {
        inventario.getDados().agregar(cantidad);
        updatePowerUpCounts();
        updateButtonStates();
        addEvent("¡Obtuviste " + cantidad + " dado(s)!");
    }

    // Añade bolas de nieve al inventario del jugador y actualiza la interfaz
    public void addBolasNieve(int cantidad) {
        inventario.getBolasNieve().agregar(cantidad);
        updatePowerUpCounts();
        updateButtonStates();
        addEvent("¡Obtuviste " + cantidad + " bola(s) de nieve!");
    }
    
    // Reinicia el tablero cargando un estado específico de casillas
    private void reiniciarTableroConEstado(Map<Integer, String> estadoCasillas) {
    	// Crea un tablero nuevo con 50 casillas (estado inicial)
        juegoTablero = new tablero(50);
        juegoTablero.generarTablero(); // Genera el tablero por defecto
        clearHighlights();			   // Limpia resaltados anteriores

        // Reemplaza las casillas con los valores guardados
        for (Map.Entry<Integer, String> entry : estadoCasillas.entrySet()) {
            int pos = entry.getKey(); // Obtiene posición de la casilla
            String tipo = entry.getValue(); // Obtiene tipo de casilla

            // Crea la casilla según el tipo guardado
            switch (tipo) {
                case "CasillaNormal":
                    juegoTablero.casillas.set(pos, new CasillaNormal(pos));
                    break;
                case "CasillaOso":
                    juegoTablero.casillas.set(pos, new CasillaOso(pos)); 
                    juegoTablero.osoPositions.add(pos); // Registra posición de oso
                    break;
                case "CasillaAgujero":
                    juegoTablero.casillas.set(pos, new CasillaAgujero(pos, juegoTablero));
                    juegoTablero.agujeroPositions.add(pos); // Registra posición de agujero
                    break;
                case "CasillaTrineo":
                    juegoTablero.casillas.set(pos, new CasillaTrineo(pos, juegoTablero));
                    juegoTablero.trineoPositions.add(pos); // Registra posición de trineo
                    break;
                case "CasillaInterrogante":
                    juegoTablero.casillas.set(pos, new CasillaInterrogante(pos));
                    juegoTablero.interrogantePositions.add(pos); // Registra posición de interrogante
                    break;
            }
        }
        
        highlightSpecialSquares(); // Resalta casillas especiales
    }

    // Maneja el inicio de un nuevo juego
    @FXML
    private void handleNewGame() { resetPlayerPositions(); }
    
    // Maneja el guardado de la partida actual
    @FXML
    private void handleSaveGame() { 
        try {
        	// Prepara el estado del tablero (posición -> tipo de casilla)
            Map<Integer, String> boardState = new HashMap<>();
            for (int i = 0; i < juegoTablero.getNumeroDeCasillas(); i++) {
                Casilla casilla = juegoTablero.getCasilla(i);
                boardState.put(i, casilla.getClass().getSimpleName());
            }

            int currentPlayerPosition = 0; // Posición actual del jugador

            bbdd bb = new bbdd(); // Conexión con la base de datos
            Connection dbConnection = bb.conectarBaseDatos();
            PartidaDAO partidaDAO = new PartidaDAO(dbConnection);

            // Formato de fecha para el guardado
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Guarda la partida y obtiene el ID generado
            int currentGameId = partidaDAO.guardarNuevaPartida(
            	    LocalDate.now().format(formatter), // Fecha actual
            	    boardState,                        // Estado del tablero
            	    inventario,                        // Inventario del jugador
            	    currentUserId,                     // ID del jugador
            	    currentPlayerPosition              // Posición actual del jugador
            	);

            if (currentGameId != -1) {
                addEvent("Partida guardada con éxito (ID: " + currentGameId + ").");
            } else {
                showError("Hubo un problema al guardar la partida.");
            }
        } catch (Exception e) {
            showError("No se pudo guardar la partida: " + e.getMessage());
        } 
    }
    
    // Reinicia completamente el tablero de juego
    private void handleResetTable() {
        addEvent("🔄 Reiniciando el tablero...");
        juegoTablero = new tablero(50);	// Nuevo tablero
        juegoTablero.generarTablero();	// Genera casillas
        clearHighlights();				// Limpiar resaltados anteriores
        highlightSpecialSquares(); 		// Resalta casillas especiales
        resetPlayerPositions(); 		// Reinicia jugadores
        updatePowerUpCounts(); 			// Actualiza contadores
        updateButtonStates();			// Habilita/deshabilita botones
        addEvent("🔁 Tablero reiniciado correctamente.");
    }
    
    // Maneja la carga de una partida guardada
    @FXML
    private void handleLoadGame() {
    	// Configura el diálogo de carga
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Cargar Partida");
        dialog.setHeaderText("Introduce el ID de la partida que deseas cargar:");
        dialog.setContentText("ID de partida:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int gameIdToLoad = Integer.parseInt(result.get());

                // Conexión a la base de datos
                bbdd bb = new bbdd();
                Connection dbConnection = bb.conectarBaseDatos();
                PartidaDAO partidaDAO = new PartidaDAO(dbConnection);
                
                // Intenta cargar la partida
                Map<String, Object> loadedData = partidaDAO.cargarPartida(gameIdToLoad, currentUserId);

                if (loadedData.isEmpty()) { // Si loadedData es vacio
                    showError("No se encontró ninguna partida con ese ID.");
                } else {
                    addEvent("Partida cargada exitosamente (ID: " + gameIdToLoad + ").");
                }

            } catch (Exception e) {
                showError("No se pudo cargar la partida: " + e.getMessage());
            }
        } else {
            addEvent("Carga de partida cancelada.");
        }
    }
    
    // Cierra completamente la aplicación
    @FXML
    private void handleQuitGame() {
    	addEvent("🚪 Saliendo del juego...");
    	System.exit(0); // Termina la aplicación
    }
}