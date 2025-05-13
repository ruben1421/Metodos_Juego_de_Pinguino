package controlador;

import java.sql.Connection;

import Metodos_Juego_de_Pinguino.bbdd;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

//Clase principal que inicia el juego
public class main extends Application {

	//Método que arranca la aplicación
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Conecta con la base de datos
		Connection con = bbdd.conectarBaseDatos();
		
		//System.out.println(getClass().getResource("/pantallaPrincipal.fxml"));
		//Carga la pantalla inicial desde el archivo FXML
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/inicio.fxml"));
	    Parent root = loader.load();

	    //Prepara y muestra la ventana
	    Scene scene = new Scene(root);
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("El Juego del Pingüino"); //Título de la ventana
	    primaryStage.show(); //Abre la ventana
	}

    public static void main(String[] args) { //Punto de entrada del programa
        launch(args); //Inicia la aplicación JavaFX
    }
}
