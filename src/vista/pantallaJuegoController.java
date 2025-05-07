package vista;

import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class pantallaJuegoController {

    // Menu items
    @FXML private MenuItem newGame;
    @FXML private MenuItem saveGame;
    @FXML private MenuItem loadGame;
    @FXML private MenuItem quitGame;

    // Buttons
    @FXML private Button dado;
    @FXML private Button rapido;
    @FXML private Button lento;
    @FXML private Button peces;
    @FXML private Button nieve;

    // Texts
    @FXML private Text dadoResultText;
    @FXML private Text rapido_t;
    @FXML private Text lento_t;
    @FXML private Text peces_t;
    @FXML private Text nieve_t;
    @FXML private Text eventos;

    // Game board and player pieces
    @FXML private GridPane tablero;
    @FXML private Circle P1;
    @FXML private Circle P2;
    @FXML private Circle P3;
    @FXML private Circle P4;
    
    //ONLY FOR TESTING!!!
    private int p1Position = 0;
    private final int COLUMNS = 5;
    private final int MAX_CELLS = 49; // 0-indexed

    @FXML
    private void initialize() {
        eventos.setText("¡Bienvenido! Presiona 'Tirar los dados' para comenzar.");
        resetPlayerPositions(); // Initialize all players to (0,0)
    }

    private void resetPlayerPositions() {
        GridPane.setRowIndex(P1, 0);
        GridPane.setColumnIndex(P1, 0);
        // Repeat for P2, P3, P4 if needed
    }

    // Button and menu actions

    @FXML
    private void handleNewGame() {
        System.out.println("New game.");
        // TODO
    }

    @FXML
    private void handleSaveGame() {
        System.out.println("Saved game.");
        // TODO
    }

    @FXML
    private void handleLoadGame() {
        System.out.println("Loaded game.");
        // TODO
    }

    @FXML
    private void handleQuitGame() {
        System.out.println("Exit...");
        // TODO
    }

    @FXML
    private void handleDado(ActionEvent event) {
        Random rand = new Random();
        int diceResult = rand.nextInt(6) + 1;
        dadoResultText.setText("Resultado: " + diceResult);
        moveP1(diceResult);
        eventos.setText(eventos.getText() + "\nJugador 1 avanza " + diceResult + " casillas.");
    }

    private void moveP1(int steps) {
        p1Position = Math.min(p1Position + steps, MAX_CELLS); // Cap at 49
        int row = p1Position / COLUMNS;
        int col = p1Position % COLUMNS;
        GridPane.setRowIndex(P1, row);
        GridPane.setColumnIndex(P1, col);
    }

    @FXML
    private void handleRapido() {
        System.out.println("Fast.");
        // TODO
    }

    @FXML
    private void handleLento() {
        System.out.println("Slow.");
        // TODO
    }

    @FXML
    private void handlePeces() {
        System.out.println("Fish.");
        // TODO
    }

    @FXML
    private void handleNieve() {
        System.out.println("Snow.");
        // TODO
    }
}
