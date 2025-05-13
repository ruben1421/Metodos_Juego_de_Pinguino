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

	// Elementos del menú principal
    @FXML private MenuItem newGame;		// Opción "Nuevo Juego"
    @FXML private MenuItem saveGame;	// Opción "Guardar Juego"
    @FXML private MenuItem loadGame;	// Opción "Cargar Juego"
    @FXML private MenuItem quitGame;	// Opción "Salir del Juego"

    // Campos para el formulario de login
    @FXML private TextField userField;		// Campo de texto para nombre de usuario
    @FXML private PasswordField passField;	// Campo de contraseña

    // Botones de la pantalla de login
    @FXML private Button loginButton;		// Botón para iniciar sesión
    @FXML private Button registerButton;	// Botón para registrar nuevo usuario

    // Método que se ejecuta automáticamente al cargar la pantalla FXML.
    @FXML
    private void initialize() {
        System.out.println("pantallaPrincipalController inicializado");
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
    
    // Maneja el proceso de inicio de sesión del usuario
    @FXML
    private void handleLogin(ActionEvent event) {
    	// Obtiene credenciales ingresadas
    	String username = userField.getText();
        String password = passField.getText();
        System.out.println("Login pressed: " + username + " / " + password);

        // Valida campos completos
        if (!username.isEmpty() && !password.isEmpty()) {
            Connection con = null;
            try {
            	// 1. Conexión a la base de datos
                con = bbdd.conectarBaseDatos();

                if (con == null) {
                    showError("No se pudo conectar a la base de datos.");
                    return;
                }

                // 2. Autenticación del usuario
                int userId = obtenerIdJugadorDesdeBD(con, username);
                if (userId == -1) {
                    showError("Usuario no encontrado.");
                    con.close();
                    return;
                }

                // 3. Carga de pantalla de juego
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/pantallaJuego.fxml"));
                Parent pantallaJuegoRoot = loader.load();

                // 4. Configuración del controlador del juego
                pantallaJuegoController juegoController = loader.getController();
                juegoController.dbConnection = con;
                juegoController.currentUserId = userId;

                // 5. Transición a pantalla de juego
                Scene pantallaJuegoScene = new Scene(pantallaJuegoRoot);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(pantallaJuegoScene);
                stage.setTitle("Pantalla de Juego");

            } catch (Exception e) {
                e.printStackTrace();
                showError("Error al iniciar sesión: " + e.getMessage());
                try {
                	// Cierra conexión si hubo error antes de transferirla
                    if (con != null) con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            showError("Por favor, ingresa usuario y contraseña.");
        }
    }
    
    // Muestra un diálogo de error al usuario con el mensaje especificado
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");		// Título de la ventana
        alert.setHeaderText(null);		// Sin encabezado adicional
        alert.setContentText(message);	// Mensaje principal
        alert.showAndWait();			// Muestra y espera cierre
    }

    // Obtiene el ID de un jugador desde la base de datos usando su nickname
    private int obtenerIdJugadorDesdeBD(Connection con, String nickname) {
        try {
        	// Consulta SQL parametrizada para seguridad
            String sql = "SELECT id_jugador FROM Jugador WHERE nickname = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nickname); // Previene SQL injection
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_jugador"); // Devuelve ID si existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Valor por defecto si no se encuentra o hay error
    }

    // Maneja el registro de nuevos usuarios en el sistema
    @FXML
    private void handleRegister() {
        System.out.println("Register pressed"); // Log para depuración

        // Obtiene valores de los campos de texto
        String username = userField.getText().trim();
        String password = passField.getText().trim();

        // Valida que los campos no estén vacíos
        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password cannot be empty.");
            return;
        }

        try {
        	// Establece conexión con la base de datos
            Connection con = bbdd.conectarBaseDatos();
            if (con == null) {
                showError("Could not connect to database.");
                return;
            }

            // Verifica si el usuario ya existe
            String checkSql = "SELECT COUNT(*) FROM Jugador WHERE nickname = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                showError("Username already exists.");
                return;
            }

            // Obtener nuevo ID para el usuario en la base de datos
            int newUserId = getNextUserId(con);

            // Inserta el nuevo usuario
            String insertSql = "INSERT INTO Jugador (id_jugador, nickname, contraseña, num_partidas) VALUES (?, ?, ?, 0)";
            PreparedStatement insertStmt = con.prepareStatement(insertSql);
            insertStmt.setInt(1, newUserId);					// Asigna el nuevo ID
            insertStmt.setString(2, username);					// Nombre de usuario
            insertStmt.setString(3, hashPassword(password)); 	// Contraseña hasheada

            // Ejecuta la inserción y verifica si fue exitosa
            int rowsAffected = insertStmt.executeUpdate();

            // Muestra mensaje de éxito o error según el resultado
            if (rowsAffected > 0) {
                showSuccess("Registration successful! Welcome, " + username);
                userField.clear();	// Limpia campo de usuario
                passField.clear();	// Limpia campo de contraseña
            } else {
                showError("Registration failed.");
            }

        } catch (SQLException e) {
        	// Maneja errores de base de datos
            showError("Database error: " + e.getMessage());
        }
    }
    
    // Obtiene el siguiente ID disponible para un nuevo jugador en la base de datos
    private int getNextUserId(Connection con) throws SQLException {
        String maxIdSql = "SELECT MAX(id_jugador) FROM Jugador";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(maxIdSql);
        
        int maxId = 1; // Valor por defecto si no hay registros
        
        if (rs.next()) {
            maxId = rs.getObject(1, Integer.class) + 1;
        }
        return maxId;
    }
    
    // Genera un hash básico de una contraseña para almacenamiento seguro
    private String hashPassword(String password) {
        return Integer.toHexString(password.hashCode()); // Hash básico
    }
    
    // Muestra un diálogo de operación exitosa al usuario
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null); // Sin encabezado adicional
        alert.setContentText(message);
        alert.showAndWait(); // Muestra y espera interacción
    }
}
