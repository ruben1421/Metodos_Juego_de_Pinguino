package vista;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;

import Metodos_Juego_de_Pinguino.bbdd;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class pantallaPrincipalController {

    @FXML private MenuItem newGame;
    @FXML private MenuItem saveGame;
    @FXML private MenuItem loadGame;
    @FXML private MenuItem quitGame;

    @FXML private TextField userField;
    @FXML private PasswordField passField;

    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @FXML
    private void initialize() {
        // This method is called automatically after the FXML is loaded
        // You can set initial values or add listeners here
        System.out.println("pantallaPrincipalController initialized");
    }

    @FXML
    private void handleNewGame() {
        System.out.println("New Game clicked");
        // TODO
    }

    @FXML
    private void handleSaveGame() {
        System.out.println("Save Game clicked");
        // TODO
    }

    @FXML
    private void handleLoadGame() {
        System.out.println("Load Game clicked");
        // TODO
    }

    @FXML
    private void handleQuitGame() {
        System.out.println("Quit Game clicked");
        // TODO
        System.exit(0);
    }
    
    @FXML
    private void handleLogin(ActionEvent event) {
    	String username = userField.getText();
        String password = passField.getText();
        System.out.println("Login pressed: " + username + " / " + password);

        if (!username.isEmpty() && !password.isEmpty()) {
            Connection con = null;
            try {
                // First connect to the database
                con = bbdd.conectarBaseDatos();

                if (con == null) {
                    showError("No se pudo conectar a la base de datos.");
                    return;
                }

                // Fetch user ID before proceeding
                int userId = obtenerIdJugadorDesdeBD(con, username);
                if (userId == -1) {
                    showError("Usuario no encontrado.");
                    con.close();
                    return;
                }

                // Load the game screen only after successful login
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantallaJuego.fxml"));
                Parent pantallaJuegoRoot = loader.load();

                // Pass connection and user ID to the game controller
                pantallaJuegoController juegoController = loader.getController();
                juegoController.dbConnection = con;
                juegoController.currentUserId = userId;

                Scene pantallaJuegoScene = new Scene(pantallaJuegoRoot);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(pantallaJuegoScene);
                stage.setTitle("Pantalla de Juego");

            } catch (Exception e) {
                e.printStackTrace();
                showError("Error al iniciar sesión: " + e.getMessage());
                try {
                    if (con != null) con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            showError("Por favor, ingresa usuario y contraseña.");
        }
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private int obtenerIdJugadorDesdeBD(Connection con, String nickname) {
        try {
            String sql = "SELECT id_jugador FROM Jugador WHERE nickname = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nickname);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_jugador");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Default error value
    }

    @FXML
    private void handleRegister() {
        System.out.println("Register pressed");

        String username = userField.getText().trim();
        String password = passField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password cannot be empty.");
            return;
        }

        try {
            Connection con = bbdd.conectarBaseDatos();
            if (con == null) {
                showError("Could not connect to database.");
                return;
            }

            // Check if username already exists
            String checkSql = "SELECT COUNT(*) FROM Jugador WHERE nickname = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                showError("Username already exists.");
                return;
            }

            // Get next available id_jugador
            int newUserId = getNextUserId(con);

            // Insert new user
            String insertSql = "INSERT INTO Jugador (id_jugador, nickname, contraseña, num_partidas) VALUES (?, ?, ?, 0)";
            PreparedStatement insertStmt = con.prepareStatement(insertSql);
            insertStmt.setInt(1, newUserId);
            insertStmt.setString(2, username);
            insertStmt.setString(3, hashPassword(password)); // Simple hashing example

            int rowsAffected = insertStmt.executeUpdate();

            if (rowsAffected > 0) {
                showSuccess("Registration successful! Welcome, " + username);
                userField.clear();
                passField.clear();
            } else {
                showError("Registration failed.");
            }

        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }
    
    private int getNextUserId(Connection con) throws SQLException {
        String maxIdSql = "SELECT MAX(id_jugador) FROM Jugador";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(maxIdSql);
        int maxId = 1; // Default starting ID
        if (rs.next()) {
            maxId = rs.getObject(1, Integer.class) + 1;
        }
        return maxId;
    }
    
    private String hashPassword(String password) {
        return Integer.toHexString(password.hashCode()); // Basic hashing
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
