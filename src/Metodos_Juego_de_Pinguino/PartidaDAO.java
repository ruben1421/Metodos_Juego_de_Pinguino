package Metodos_Juego_de_Pinguino;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PartidaDAO { //Clase para manejar las operaciones de base de datos relacionadas con las partidas
    private Connection con;

    public PartidaDAO(Connection con) { //Constructor que recibe la conexión a la base de datos
        this.con = con;
    }

    /**
     * Guarda una nueva partida en la base de datos con todos sus componentes:
     * - Datos básicos de la partida
     * - Información del jugador (inventario y posición)
     * - Estado del tablero (casillas)
     * 
     * @param fecha: Fecha de la partida en formato YYYY-MM-DD
     * @param estadoCasillas: Mapa con las posiciones y estados de las casillas
     * @param inventario: Objeto con los items del jugador
     * @param idJugador: ID del jugador en la base de datos
     * @param posicionJugador: Posición actual del jugador en el tablero
     * @return ID de la partida guardada o -1 si hubo error
     */
    public int guardarNuevaPartida(String fecha, Map<Integer, String> estadoCasillas, Inventario inventario, int idJugador, int posicionJugador) {
        int numPartida = -1;

        try {
        	//Obtiene el ID para la nueva partida
            numPartida = obtenerSiguienteIdPartida();

            //Inserta los datos de la partida
            String sqlPartida = "INSERT INTO Partida (num_partida, fecha) VALUES (?, ?)";
            PreparedStatement stmtPartida = con.prepareStatement(sqlPartida);
            stmtPartida.setInt(1, numPartida);
            stmtPartida.setDate(2, Date.valueOf(fecha)); 
            stmtPartida.executeUpdate();

            //Guarda la relación jugador-partida
            String sqlJugadorPartida = "INSERT INTO Jugador_Partida (id_jugador_partida, id_partida, id_jugador, inventario, posicion) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmtJugadorPartida = con.prepareStatement(sqlJugadorPartida);
            stmtJugadorPartida.setInt(1, obtenerSiguienteIdJugadorPartida()); 
            stmtJugadorPartida.setInt(2, numPartida);
            stmtJugadorPartida.setInt(3, idJugador);
            stmtJugadorPartida.setString(4, convertirInventarioAJSON(inventario));
            stmtJugadorPartida.setInt(5, posicionJugador);
            stmtJugadorPartida.executeUpdate();

            //Guarda el estado de las casillas del tablero
            String sqlCasilla = "INSERT INTO Casilla (id_casilla, num_partida, nombre, estado, tipo, posicion, descripcion) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmtCasilla = con.prepareStatement(sqlCasilla);

            for (Map.Entry<Integer, String> entry : estadoCasillas.entrySet()) {
                Integer posicion = entry.getKey();
                String estado = entry.getValue();

                int casillaId = obtenerSiguienteIdCasilla(); 
                stmtCasilla.setInt(1, casillaId);           
                stmtCasilla.setInt(2, numPartida);
                stmtCasilla.setString(3, "Casilla_" + posicion);
                stmtCasilla.setString(4, estado);
                stmtCasilla.setString(5, "Normal");
                stmtCasilla.setInt(6, posicion);
                stmtCasilla.setString(7, "Descripción de casilla " + posicion);
                stmtCasilla.addBatch();
            }
            stmtCasilla.executeBatch();

            System.out.println("Partida guardada exitosamente con ID: " + numPartida);
            return numPartida;

        } catch (SQLException e) {
            System.out.println("Error al guardar la partida: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Carga una partida existente desde la base de datos
     * @param idPartida: ID de la partida a cargar
     * @param idJugador: ID del jugador que carga la partida
     * @return Mapa con todos los datos necesarios para reconstruir la partida:
     *         - fecha: Fecha de la partida
     *         - inventario: Objeto Inventario del jugador
     *         - posicion: Posición actual del jugador
     *         - estado_casillas: Mapa con el estado de cada casilla
     */
    public Map<String, Object> cargarPartida(int idPartida, int idJugador) {
        Map<String, Object> datosCargados = new HashMap<>();

        try {
        	//Carga los datos básicos de la partida
            String sqlPartida = "SELECT * FROM Partida WHERE num_partida = ?";
            PreparedStatement stmt = con.prepareStatement(sqlPartida);
            stmt.setInt(1, idPartida);
            ResultSet rsPartida = stmt.executeQuery();

            if (rsPartida.next()) {
                datosCargados.put("fecha", rsPartida.getString("fecha"));
            }

            //Carga el inventario y posición del jugador
            String sqlJugadorPartida = "SELECT inventario, posicion FROM Jugador_Partida WHERE id_partida = ? AND id_jugador = ?";
            PreparedStatement stmtJugadorPartida = con.prepareStatement(sqlJugadorPartida);
            stmtJugadorPartida.setInt(1, idPartida);
            stmtJugadorPartida.setInt(2, idJugador);
            ResultSet rsInventario = stmtJugadorPartida.executeQuery();

            if (rsInventario.next()) {
                datosCargados.put("inventario", convertirJSONAInventario(rsInventario.getString("inventario")));
                datosCargados.put("posicion", rsInventario.getInt("posicion"));
            }

            //Carga el estado del tablero de Casilla
            String sqlCasillas = "SELECT posicion, estado FROM Casilla WHERE num_partida = ?";
            PreparedStatement stmtCasillas = con.prepareStatement(sqlCasillas);
            stmtCasillas.setInt(1, idPartida);
            ResultSet rsCasillas = stmtCasillas.executeQuery();

            Map<Integer, String> estadoCasillas = new HashMap<>();
            while (rsCasillas.next()) {
                estadoCasillas.put(rsCasillas.getInt("posicion"), rsCasillas.getString("estado"));
            }
            datosCargados.put("estado_casillas", estadoCasillas);

            System.out.println("Partida cargada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al cargar la partida: " + e.getMessage());
        }

        return datosCargados;
    }

    private String convertirInventarioAJSON(Inventario inventario) { // Convierte el inventario a formato JSON para guardarlo
        return String.format("{\"peces\":%d,\"dados\":%d,\"bolas_nieve\":%d}",
                inventario.getCantidadPeces(),
                inventario.getCantidadDados(),
                inventario.getCantidadBolasNieve());
    }

    private Inventario convertirJSONAInventario(String json) { //Convierte el JSON guardado a un objeto Inventario
        Inventario inventario = new Inventario();
        json = json.replaceAll("[{}]", ""); // Procesamiento simple del JSON (sin usar librerías externas)
        String[] entries = json.split(",");
        for (String entry : entries) {
            String[] keyValue = entry.split(":");
            String key = keyValue[0].replaceAll("\"", "");
            int value = Integer.parseInt(keyValue[1]);
            switch (key) {
                case "peces":
                    inventario.setCantidadPeces(value);
                    break;
                case "dados":
                    inventario.setCantidadDados(value);
                    break;
                case "bolas_nieve":
                    inventario.setCantidadBolasNieve(value);
                    break;
            }
        }
        return inventario;
    }

    public int obtenerSiguienteIdPartida() throws SQLException { //Obtiene el siguiente ID disponible para partidas
        String sql = "SELECT partida_seq.NEXTVAL FROM dual";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException("No se pudo obtener un nuevo ID de partida.");
    }
    
    public int obtenerSiguienteIdJugadorPartida() throws SQLException { //Obtiene el siguiente ID disponible para jugador_partida
    	String sql = "SELECT jugador_partida_seq.NEXTVAL FROM dual";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException("No se pudo obtener un nuevo ID de Jugador_Partida.");
    }
    
    public int obtenerSiguienteIdCasilla() throws SQLException { //Obtiene el siguiente ID disponible para casilla
        String sql = "SELECT casilla_seq.NEXTVAL FROM dual";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException("No se pudo obtener un nuevo ID de Casilla.");
    }
}