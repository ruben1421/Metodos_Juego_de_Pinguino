package vista;

import java.util.Random;

import Metodos_Juego_de_Pinguino.Inventario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

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

    @FXML
    public void initialize() {
        eventos.setText("¡Bienvenido! Presiona 'Tirar los dados' para comenzar.");
        resetPlayerPositions();
        
        inventario.setCantidadPeces(1);
        inventario.setCantidadDados(1); 
        inventario.setCantidadBolasNieve(1);
        
        updatePowerUpCounts();
        updateButtonStates();
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
            // Remove from group if still grouped
            if (startPosition.getChildren().contains(P1)) {
                startPosition.getChildren().remove(P1);
                tablero.getChildren().add(P1);
            }
            
            // Calculate new position
            p1Position = Math.min(p1Position + steps, (COLUMNS * ROWS) - 1);
            updatePlayerPosition(P1, p1Position);
        }
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
        // ... rest of dice handling
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

    @FXML
    private void handleNewGame() { resetPlayerPositions(); }
    @FXML
    private void handleSaveGame() { addEvent("Juego guardado"); }
    @FXML
    private void handleLoadGame() { addEvent("Juego cargado"); }
    @FXML
    private void handleQuitGame() { System.exit(0); }
}