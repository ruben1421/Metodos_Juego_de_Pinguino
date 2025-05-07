module Metodos_Juego_de_Pinguino {
	requires java.sql;
	    requires javafx.controls;
	    requires javafx.fxml;
	    
	    opens vista to javafx.fxml;
	    exports vista;
	    exports controlador;
}