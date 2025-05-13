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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;

public class pantallaJuegoController {

    @FXML private MenuItem newGame;
    @FXML private MenuItem saveGame;
    @FXML private MenuItem loadGame;
    @FXML private MenuItem quitGame;

    @FXML private Button dado;
    @FXML private Button rapido;
    @FXML private Button lento;
    @FXML private Button peces;
    @FXML private Button nieve;
    @FXML private Button opcionesJuego;

    @FXML private Text dadoResultText;
    @FXML private Text rapido_t;
    @FXML private Text lento_t;
    @FXML private Text peces_t;
    @FXML private Text nieve_t;
    @FXML private Text eventos;

    @FXML private HBox startPosition;
    @FXML private GridPane tablero;
    @FXML private Circle P1;
    @FXML private Circle P2;
    @FXML private Circle P3;
    @FXML private Circle P4;
    
    private Inventario inventario = new Inventario();
    private int p1Position = 0;
    private final int COLUMNS = 5;
    private final int ROWS = 10;
    private int actionCount = 0;
    private tablero juegoTablero = new tablero(50);
    int currentUserId = 1; // This should come from login
    Connection dbConnection; // Set this after login
    
    private void highlightSpecialSquares() {
        // Clear previous highlights
        tablero.getChildren().removeIf(node -> node instanceof Rectangle);
        
        // Highlight Oso positions (brown)
        highlightPositions(juegoTablero.getOsoPositions(), Color.BROWN);
        
        // Highlight Agujero positions (black)
        highlightPositions(juegoTablero.getAgujeroPositions(), Color.BLACK);
        
        // Highlight Trineo positions (light blue)
        highlightPositions(juegoTablero.getTrineoPositions(), Color.LIGHTBLUE);
        
        // Highlight Interrogante positions (yellow)
        highlightPositions(juegoTablero.getInterrogantePositions(), Color.GOLD);
    }
    
    private void highlightPositions(ArrayList<Integer> positions, Color color) {
        for (int pos : positions) {
            int row = pos / COLUMNS;
            int col = pos % COLUMNS;
            
            // Snake pattern adjustment
            if (row % 2 == 1) {
                col = (COLUMNS - 1) - col;
            }
            
            Rectangle highlight = new Rectangle(55,55, color);
            highlight.setOpacity(0.3);  // Semi-transparent
            highlight.setStroke(Color.BLACK);
            highlight.setStrokeWidth(1);
            
            GridPane.setRowIndex(highlight, row);
            GridPane.setColumnIndex(highlight, col);
            
            tablero.getChildren().add(highlight);
            highlight.toBack();  // Ensure it's behind other elements
        }
    }

    @FXML
    public void initialize() {
        eventos.setText("¡Bienvenido! Presiona 'Tirar los dados' para comenzar.");
        resetPlayerPositions();
        
        inventario.setCantidadPeces(1);
        inventario.setCantidadDados(1); 
        inventario.setCantidadBolasNieve(1);
        
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
                int columnaMostrada = (fila % 2 == 1) ? (COLUMNS - 1 - columna) : columna;

                // Crear texto con el número de la casilla
                Text numeroCasilla = new Text(String.valueOf(posicion + 1));
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
            }
        }
    }

    private void updatePowerUpCounts() {
        rapido_t.setText("Dado rápido: " + inventario.getCantidadDados());
        lento_t.setText("Dado lento: " + inventario.getCantidadDados());
        peces_t.setText("Peces: " + inventario.getCantidadPeces());
        nieve_t.setText("Bolas de nieve: " + inventario.getCantidadBolasNieve());
    }

    private void updateButtonStates() {
        rapido.setDisable(inventario.getCantidadDados() <= 0);
        lento.setDisable(inventario.getCantidadDados() <= 0);
        peces.setDisable(inventario.getCantidadPeces() <= 0);
        nieve.setDisable(inventario.getCantidadBolasNieve() <= 0);
    }

    private void showError(String message) {
        eventos.setText(eventos.getText() + "\n[ERROR] " + message);
    }
    
    private void resetPlayerPositions() {
    	tablero.getChildren().removeAll(P1, P2, P3, P4);
        
        // Add back to starting HBox
        startPosition.getChildren().setAll(P1, P2, P3, P4);
        GridPane.setRowIndex(startPosition, 0);
        GridPane.setColumnIndex(startPosition, 0);
        
        p1Position = 0;
    }

    private void updatePlayerPosition(Circle player, int position) {
    	int row = position / COLUMNS;
        int col = position % COLUMNS;
        
        // Snake pattern movement
        if (row % 2 == 1) {
            col = (COLUMNS - 1) - col;
        }
        
        GridPane.setRowIndex(player, row);
        GridPane.setColumnIndex(player, col);
        player.toFront(); // Ensure visible
    }

    private void movePlayer(Circle player, int steps) {
    	if (player == P1) {
            if (startPosition.getChildren().contains(P1)) {
                startPosition.getChildren().remove(P1);
                tablero.getChildren().add(P1);
            }
            
            int oldPosition = p1Position;
            p1Position = Math.min(p1Position + steps, (COLUMNS * ROWS) - 1);
            updatePlayerPosition(P1, p1Position);
            
            handleTileEffect(oldPosition, p1Position);
        }
    }
    
    @FXML
    private void handleOpcionesJuego(ActionEvent event) {
    	// Use the button itself as the source
        ContextMenu contextMenu = new ContextMenu();

        MenuItem guardarTabla = new MenuItem("Guardar Tabla");
        MenuItem cargarTabla = new MenuItem("Cargar Tabla");
        MenuItem resetearTabla = new MenuItem("Resetear Tabla");
        MenuItem salirDelJuego = new MenuItem("Salir del Juego");

        // Set actions for each item
        guardarTabla.setOnAction(e -> handleSaveGame());
        cargarTabla.setOnAction(e -> handleLoadGame());
        resetearTabla.setOnAction(e -> handleResetTable());
        salirDelJuego.setOnAction(e -> handleQuitGame());

        // Add items to context menu
        contextMenu.getItems().addAll(guardarTabla, cargarTabla, resetearTabla, salirDelJuego);

        // Show context menu near the button
        contextMenu.show(opcionesJuego, javafx.geometry.Side.BOTTOM, 0, 0);
    }
    
    private void handleTileEffect(int oldPos, int newPos) {
        Casilla tile = juegoTablero.getCasilla(newPos);
        
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

    private void handleOsoTile() {
        addEvent("¡Encontraste un oso!");
        P1.setFill(Color.DARKRED);
        
        if (inventario.getCantidadPeces() > 0) {
            inventario.getPeces().quitar(1);
            addEvent("Usaste un pez para calmar al oso!");
        } else {
            p1Position = 0;
            updatePlayerPosition(P1, 0);
            addEvent("¡Sin peces! Vuelves al inicio");
        }
        updatePowerUpCounts();
    }

    private void handleAgujeroTile(int pos) {
        addEvent("¡Caíste en un agujero!");
        P1.setFill(Color.BLACK);
        
        int newPos = juegoTablero.encontrarAgujeroAnterior(pos);
        p1Position = newPos;
        updatePlayerPosition(P1, newPos);
        addEvent("Retrocedes a la casilla " + newPos);
    }

    private void handleTrineoTile(int pos) {
        addEvent("¡Encontraste un trineo!");
        P1.setFill(Color.LIGHTBLUE);
        
        int newPos = juegoTablero.encontrarSiguienteTrineo(pos);
        p1Position = newPos;
        updatePlayerPosition(P1, newPos);
        addEvent("¡Zummm! Avanzas a la casilla " + newPos);
    }

    private void handleInterroganteTile() {
        addEvent("¡Casilla sorpresa!");
        P1.setFill(Color.GOLD);
        
        Random r = new Random();
        int evento = r.nextInt(4);
        switch (evento) {
            case 0:
                inventario.getPeces().agregar(1);
                addEvent("¡Ganaste un pez!");
                break;
            case 1:
                int bolasNieve = r.nextInt(3) + 1;
                inventario.getBolasNieve().agregar(bolasNieve);
                addEvent("¡Ganaste " + bolasNieve + " bolas de nieve!");
                break;
            case 2:
                int avanceRapido = r.nextInt(6) + 5;
                p1Position = Math.min(p1Position + avanceRapido, (COLUMNS * ROWS) - 1);
                updatePlayerPosition(P1, p1Position);
                addEvent("¡Dado rápido! Avanzas " + avanceRapido + " casillas");
                break;
            case 3:
                int avanceLento = r.nextInt(3) + 1;
                p1Position = Math.min(p1Position + avanceLento, (COLUMNS * ROWS) - 1);
                updatePlayerPosition(P1, p1Position);
                addEvent("¡Dado lento! Avanzas " + avanceLento + " casillas");
                break;
        }
        updatePowerUpCounts();
    }

    private void addEvent(String message) {
        actionCount++;
        if (actionCount > 3) {
            eventos.setText(message);
            actionCount = 1;
        } else {
            eventos.setText(eventos.getText() + "\n" + message);
        }
    }

    @FXML
    private void handleDado(ActionEvent event) {
        Random rand = new Random();
        int diceResult = rand.nextInt(6) + 1;
        movePlayer(P1, diceResult);
        
        addEvent("Tiraste un " + diceResult + " y avanzaste a la casilla " + p1Position);
        
        if (p1Position >= juegoTablero.getNumeroDeCasillas() - 1) {
            addEvent("🎉 ¡Felicidades! Has llegado al final del tablero.");
            disableGameButtons(); // Optional: Disable buttons after winning
        }
    }
    
    private void disableGameButtons() {
        dado.setDisable(true);
        rapido.setDisable(true);
        lento.setDisable(true);
        peces.setDisable(true);
        nieve.setDisable(true);
    }

    @FXML
    private void handleRapido() {
        if (inventario.getCantidadDados() <= 0) {
            showError("No tienes dados disponibles");
            return;
        }
        inventario.getDados().quitar(1);
        movePlayer(P1, 3);
        addEvent("Usaste un dado rápido (+3 movimientos)");
        updatePowerUpCounts();
        updateButtonStates();
    }

    @FXML
    private void handleLento() {
        if (inventario.getCantidadDados() <= 0) {
            showError("No tienes dados disponibles");
            return;
        }
        inventario.getDados().quitar(1);
        movePlayer(P1, 1);
        addEvent("Usaste un dado lento (+1 movimiento)");
        updatePowerUpCounts();
        updateButtonStates();
    }

    @FXML
    private void handlePeces() {
        if (inventario.getCantidadPeces() <= 0) {
            showError("No tienes peces disponibles");
            return;
        }
        inventario.getPeces().quitar(1);
        movePlayer(P1, 2);
        addEvent("Usaste un pez (+2 movimientos)");
        updatePowerUpCounts();
        updateButtonStates();
    }

    @FXML
    private void handleNieve() {
        if (inventario.getCantidadBolasNieve() <= 0) {
            showError("No tienes bolas de nieve disponibles");
            return;
        }
        inventario.getBolasNieve().quitar(1);
        movePlayer(P1, 4);
        addEvent("Usaste una bola de nieve (+4 movimientos)");
        updatePowerUpCounts();
        updateButtonStates();
    }
    
    public void addPeces(int cantidad) {
        inventario.getPeces().agregar(cantidad);
        updatePowerUpCounts();
        updateButtonStates();
        addEvent("¡Obtuviste " + cantidad + " pez(es)!");
    }

    public void addDados(int cantidad) {
        inventario.getDados().agregar(cantidad);
        updatePowerUpCounts();
        updateButtonStates();
        addEvent("¡Obtuviste " + cantidad + " dado(s)!");
    }

    public void addBolasNieve(int cantidad) {
        inventario.getBolasNieve().agregar(cantidad);
        updatePowerUpCounts();
        updateButtonStates();
        addEvent("¡Obtuviste " + cantidad + " bola(s) de nieve!");
    }
    
    private void reiniciarTableroConEstado(Map<Integer, String> estadoCasillas) {
        juegoTablero = new tablero(50); // Reset board
        juegoTablero.generarTablero(); // Generate default first

        // Replace with loaded values
        for (Map.Entry<Integer, String> entry : estadoCasillas.entrySet()) {
            int pos = entry.getKey();
            String tipo = entry.getValue();

            switch (tipo) {
                case "CasillaNormal":
                    juegoTablero.casillas.set(pos, new CasillaNormal(pos));
                    break;
                case "CasillaOso":
                    juegoTablero.casillas.set(pos, new CasillaOso(pos));
                    juegoTablero.osoPositions.add(pos);
                    break;
                case "CasillaAgujero":
                    juegoTablero.casillas.set(pos, new CasillaAgujero(pos, juegoTablero));
                    juegoTablero.agujeroPositions.add(pos);
                    break;
                case "CasillaTrineo":
                    juegoTablero.casillas.set(pos, new CasillaTrineo(pos, juegoTablero));
                    juegoTablero.trineoPositions.add(pos);
                    break;
                case "CasillaInterrogante":
                    juegoTablero.casillas.set(pos, new CasillaInterrogante(pos));
                    juegoTablero.interrogantePositions.add(pos);
                    break;
            }
        }
    }

    @FXML
    private void handleNewGame() { resetPlayerPositions(); }
    
    @FXML
    private void handleSaveGame() { 
        try {
            // Create board state map
            Map<Integer, String> boardState = new HashMap<>();
            for (int i = 0; i < juegoTablero.getNumeroDeCasillas(); i++) {
                Casilla casilla = juegoTablero.getCasilla(i);
                boardState.put(i, casilla.getClass().getSimpleName());
            }

            int currentPlayerPosition = 0;

            bbdd bb = new bbdd();
            Connection dbConnection = bb.conectarBaseDatos();
            PartidaDAO partidaDAO = new PartidaDAO(dbConnection);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Save without passing numPartida manually
            int currentGameId = partidaDAO.guardarNuevaPartida(
            	    LocalDate.now().format(formatter), // Fecha
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
    
    private void handleResetTable() {
        addEvent("🔄 Reiniciando el tablero...");
        juegoTablero = new tablero(50);
        juegoTablero.generarTablero();
        highlightSpecialSquares();
        resetPlayerPositions();
        updatePowerUpCounts();
        updateButtonStates();
        addEvent("🔁 Tablero reiniciado correctamente.");
    }
    
    @FXML
    private void handleLoadGame() { 
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Cargar Partida");
        dialog.setHeaderText("Introduce el ID de la partida que deseas cargar:");
        dialog.setContentText("ID de partida:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int gameIdToLoad = Integer.parseInt(result.get());

                bbdd bb = new bbdd();
                Connection dbConnection = bb.conectarBaseDatos();

                PartidaDAO partidaDAO = new PartidaDAO(dbConnection);
                Map<String, Object> loadedData = partidaDAO.cargarPartida(gameIdToLoad, currentUserId);

                if (loadedData.isEmpty()) {
                    showError("No se encontró ninguna partida con ese ID.");
                } else {
                    // Successfully loaded
                    addEvent("Partida cargada exitosamente (ID: " + gameIdToLoad + ").");

                    // Optionally store it temporarily
                    // currentGameId = gameIdToLoad;
                }

            } catch (Exception e) {
                showError("No se pudo cargar la partida: " + e.getMessage());
            }
        } else {
            addEvent("Carga de partida cancelada.");
        }
    }
    
    @FXML
    private void handleQuitGame() {
    	addEvent("🚪 Saliendo del juego...");
    	System.exit(0); 
    }
}