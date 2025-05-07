package controlador;

import java.sql.Connection;

import Metodos_Juego_de_Pinguino.bbdd;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Connection con = bbdd.conectarBaseDatos();
		
		
		//System.out.println(getClass().getResource("/pantallaPrincipal.fxml"));
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/inicio.fxml"));
	    Parent root = loader.load();

	    Scene scene = new Scene(root);
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("El Juego del Pingüino");
	    primaryStage.show();
	}

    public static void main(String[] args) {
        launch(args);
    }
}
